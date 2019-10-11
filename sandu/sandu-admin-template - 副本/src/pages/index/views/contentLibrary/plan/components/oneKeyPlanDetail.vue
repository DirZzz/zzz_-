<template>
    <div>
        <!-- 智能方案详情页面 -->
        <v-nav :nav="nav"></v-nav>
        <div class="planDetailFrame">
            <!--<transition name="fade">-->
                <!--<el-button round class="editBtn" size="small" v-if="isPer('solution:edit')"-->
                           <!--v-show="activeName==='first'" @click="gotoAddProduct">-->
                    <!--编辑-->
                <!--</el-button>-->
            <!--</transition>-->
            <!--智能子方案-->
        <header>
            <h1>方案详情</h1>
        </header>
        <ul class="subPlanName"  v-show="planNameCombo != null && planNameCombo.length > 1">
            <li v-for="(name,index) in planNameCombo" :key="index" >
                <span :class="{'active': index == currentActive}" @click="getDetialMessage(name.planId,name.isPrimary,index)">{{name.spaceTypeName}} {{index + 1}} <b v-if="name.isPrimary == 0" class="mainPlan">主</b></span>
            </li> <!--{{name.planName}}-->
        </ul>


            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="基础信息" name="first" class="tabMarTop">
                    <div class="planLibBaseMsg">
                        <el-col :span="12">
                            <!-- 智能方案 detailPlanType为真全屋， -->
                            <ul  class="rigthTxt">
                                <li><span>方案名称：</span> <b>{{planInfo.planName}}</b></li>
                                <li><span>方案ID：</span> <b>{{planInfo.planId}}</b></li>
                                <li><span>来源：</span>
                                    <b v-if="planInfo.origin">
                                        <b v-if="planInfo.origin === 'deliver'">企业交付</b>
                                        <b v-else-if="planInfo.origin === 'share'">分享方案</b>
                                        <b v-else>内部制作</b>
                                    </b>
                                    <b v-else>—无—</b>
                                </li>
                                <li><span>完成时间：</span> <b>{{planInfo.completeDate}}</b></li>
                                <li><span>品牌：</span>
                                    <b v-if="planInfo.brandName">{{planInfo.brandName}}</b>
                                    <b v-else>—无—</b>
                                </li>
                                <li><span>设计师：</span>
                                    <b v-if="planInfo.designer">{{planInfo.designer}}</b>
                                    <b v-else>—无—</b>
                                </li>
                                <li><span>方案类型：</span> <b>{{planInfo.recommendedType===1 ? '普通方案': '智能方案'}}</b></li>
                                <li><span>风格：</span>
                                    <b v-if="planInfo.designStyleName">{{planInfo.designStyleName}}</b>
                                    <b v-else>—无—</b>
                                </li>


                                <li v-if="planInfo.isPrimary == 0  || planInfo.isPrimary == 2">
                                    <span>装修报价：</span>
                                    <b class="showPrice"  v-for="(item,index) in planInfo.decoratePriceInfoList" :key="index">
                                       {{item.decoratePriceTypeName}}{{item.decoratePrice}} / 平方米
                                    </b>
                                    <b v-if="planInfo.decoratePriceInfoList == null|| planInfo.decoratePriceInfoList.length == 0">—无—</b>
                                </li>
                                <li><span>适用面积：</span>
                                    <b v-if="planInfo.applyAreaNames">({{planInfo.applyAreaNames.join(',')}})m²</b>
                                    <b v-else>—无—</b>
                                </li>
                                <li><span>方案描述：</span>
                                    <b class="remarks" v-if="planInfo.planDesc">{{planInfo.planDesc}}</b>
                                    <b class="remarks" v-else>—无—</b>
                                </li>
                                <li><span>备注：</span>
                                    <b class="remarks" v-if="planInfo.remark">{{planInfo.remark}}</b>
                                    <b class="remarks" v-else>—无—</b>
                                </li>
                                <!--<li><span>上架情况：</span>
                                  <b class="remarks">
                                    &lt;!&ndash;<span v-for="dit in (planInfo.shelfStatus?planInfo.shelfStatus.split(','): [])">&ndash;&gt;
                                      &lt;!&ndash;<span v-if="dit === 'ONEKEY'">一键方案</span>&ndash;&gt;
                                      &lt;!&ndash;<span v-if="dit === 'OPEN'">公开方案</span>&ndash;&gt;
                                      &lt;!&ndash;<span v-if="dit === 'TEMPLATE'">样板方案</span>&ndash;&gt;
                                    &lt;!&ndash;</span>&ndash;&gt;
                                    &lt;!&ndash;<span v-if="!planInfo.shelfStatus">未上架</span>&ndash;&gt;
                                    <span v-for="dit in planInfo.distributionStatus">
                                      <span v-if="dit === '2c'">线上管理</span>
                                      <span v-if="dit === '2b'">渠道管理</span>
                                    </span>
                                    <span v-if="!planInfo.distributionStatus">未分配</span>
                                  </b>
                                </li>-->
                            </ul>
                        </el-col>
                        <el-col :span="12">
                            <div class="showImage">
                                <img v-if="planInfo.picPath" :src="BASE_URL.sourceBaseUrl+planInfo.picPath" width="600" height="400">
                            </div>
                        </el-col>

                    </div>
                </el-tab-pane>
                <el-tab-pane  label="产品列表" name="second">
                    <el-row :gutter="10" class="tabMarTop">
                        <el-col>
                            <!--产品类别:-->
                            <category-tree @change="listPlanProducts" v-model="planProductList.query.categoryIds"
                                           style="margin-right: 20px"></category-tree>
                            <!--是否未公开产品:-->
                            <is-open-product-select @change="listPlanProducts" v-model="planProductList.query.secrecy"
                                                    style="margin-right: 20px"></is-open-product-select>
                            <!--是否显示:-->
                            <is-show-select @change="listPlanProducts" v-model="planProductList.query.displayStatus"
                                            style="margin-right: 20px"></is-show-select>
                            <el-button round @click="listPlanProducts" type="primary" >搜索</el-button>
                            <el-button round @click="handleProductReset" class="searchBtn">重置</el-button>
                        </el-col>
                    </el-row>
                    <plan-product :products="planProductList.products"></plan-product>
                    <!--分页组件-->
                    <div class="pageFrame">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="planProductList.query.page"
                                :page-sizes="[5, 10, 20]"
                                :page-size="planProductList.query.limit"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="planProductList.total">
                        </el-pagination>
                    </div>
                    <!-- 产品列表结束 -->
                </el-tab-pane>
                <el-tab-pane  label="效果图" name="third" class="tabMarTop">
                    <el-row :gutter="24" class="rowpic">
                        <el-col :span="3">照片级效果图
                        <a class="viewtext"  target="view_window" :href="picPath"><img class="searchImg"  width="20px" height="20px" src="../../../../assets/images/icons/searchbtn.png">浏览</a>
                        </el-col>
                        <el-col :span="20">
                            <el-col :span="4" class="setDefaultPic" v-for="(pic,picIndex) in effectDiagram.pics"
                                    :key="picIndex">
                                <img :src="BASE_URL.sourceBaseUrl+pic.picPath" width="312" height="176"/>
                                <ul >
                                    <li>
                                        <el-button type="text" @click="setDefaultPic(pic)" :disabled="isEdit">设为缩略图</el-button>
                                    </li>
                                    <li>
                                        <el-button type="text" :disabled="disDeletePic || isEdit"
                                                   @click="banPic(pic, effectDiagram.pics)">
                                            <span v-if="pic.isDeleted === 1" :disabled="isEdit">启用</span>
                                            <span v-if="pic.isDeleted === 0" :disabled="isEdit">禁用</span>
                                        </el-button>
                                    </li>
                                    <li>
                                        <el-button type="text" :disabled="disDeletePic" @click="deletePic(pic)">删除
                                        </el-button>
                                    </li>
                                </ul>
                            </el-col>
                        </el-col>
                    </el-row>
                    <el-row :gutter="24" class="rowpic">
                        <el-col :span="3">单点720全景图
                            <a class="viewtext" target="view_window" :href="vr720Single"><img class="searchImg"  width="20px" height="20px" src="../../../../assets/images/icons/searchbtn.png">浏览</a>
                        </el-col>
                        <el-col :span="20">
                            <el-col :span="4" class="setDefaultPic"
                                    v-for="(pic, picIndex) in effectDiagram.single720Pic"
                                    :key="picIndex">
                                <img :src="BASE_URL.sourceBaseUrl+pic.picPath" width="312" height="176"/>
                                <ul>
                                    <li>
                                        <el-button type="text" @click="setDefaultPic(pic)">设为缩略图</el-button>
                                    </li>

                                    <li>
                                        <el-button type="text" :disabled="disDeleteSingle"
                                                   @click="banPic(pic, effectDiagram.single720Pic)">
                                            <span v-if="pic.isDeleted === 1">启用</span>
                                            <span v-if="pic.isDeleted === 0">禁用</span>
                                        </el-button>
                                    </li>
                                    <li>
                                        <el-button type="text" :disabled="disDeleteSingle" @click="deletePic(pic)">删除
                                        </el-button>
                                    </li>
                                </ul>
                            </el-col>
                        </el-col>
                    </el-row>
                    <el-row :gutter="24" class="rowpic">
                        <el-col :span="3">多点720全景图
                            <a class="viewtext" target="view_window" :href="vr720Roam"><img class="searchImg"  width="20px" height="20px" src="../../../../assets/images/icons/searchbtn.png">浏览</a>
                        </el-col>
                        <el-col :span="20">
                            <el-col :span="4" class="setDefaultPic" v-for="(pic,picIndex) in effectDiagram.multi720Pic"
                                    :key="picIndex">
                                <img :src="BASE_URL.sourceBaseUrl+pic.picPath" width="312" height="176"/>
                                <ul>
                                    <li>
                                        <el-button type="text" @click="setDefaultPic(pic)">设为缩略图</el-button>
                                    </li>

                                    <li>
                                        <el-button type="text" :disabled="disDeleteMult"
                                                   @click="banPic(pic, effectDiagram.multi720Pic)">
                                            <span v-if="pic.isDeleted === 1">启用</span>
                                            <span v-if="pic.isDeleted === 0">禁用</span>
                                        </el-button>
                                    </li>
                                    <li>
                                        <el-button type="text" :disabled="disDeleteMult" @click="deletePic(pic)">删除
                                        </el-button>
                                    </li>
                                </ul>
                            </el-col>
                        </el-col>
                    </el-row>
                    <el-row :gutter="24" class="rowpic">
                        <el-col :span="3">漫游视频
                            <a class="viewtext" target="view_window" :href="videoPath"><img class="searchImg"  width="20px" height="20px" src="../../../../assets/images/icons/searchbtn.png">浏览</a>
                        </el-col>
                        <el-col :span="20">
                            <el-col :span="4" class="setDefaultPic" v-for="(pic,picIndex) in effectDiagram.videoPic"
                                    :key="picIndex">
                                <img :src="BASE_URL.sourceBaseUrl+pic.picPath" width="312" height="176"
                                     @mouseover="showButton"/>
                                <ul>
                                    <li>
                                        <el-button type="text" @click="setDefaultPic(pic)">设为缩略图</el-button>
                                    </li>

                                    <li>
                                        <el-button type="text" :disabled="disDeleteMedia"
                                                   @click="banPic(pic, effectDiagram.videoPic)">
                                            <span v-if="pic.isDeleted === 1">启用</span>
                                            <span v-if="pic.isDeleted === 0">禁用</span>
                                        </el-button>
                                    </li>
                                    <li>
                                        <el-button type="text" :disabled="disDeleteMedia" @click="deletePic(pic)">删除
                                        </el-button>
                                    </li>
                                </ul>
                            </el-col>
                        </el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="设计理念" v-if="isShow" name="four">
                    <div style="height: 600px">
                        <!--<vue-editor useCustomImageHandler :disabled="isEdit" :editorOptions="editorSettings" v-model="textarea3" class="editor"-->
                                    <!--@imageAdded="handleImageAdded"></vue-editor>-->
                        <!--<p v-else style="text-align: center; margin-top: 20px">暂无信息</p>-->
                        <!--<div class="footerBtn">-->
                        <!--<el-button round class="confirmBtn" :disabled="isEdit"  type="primary" size="small" @click="saveContent">保存</el-button>-->
                        <!--<el-button round class="confirmBtn" :disabled="isEdit"  size="small" @click="cancelSave">取消</el-button>-->
                        <!--</div>-->
                        <!--富文本html区域-->
                        <div v-html="textarea3 || '无'" ref="editorContent" style="text-indent: 32px"></div>
                        <!--富文本html区域-->
                    </div>

                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
    import PlanProduct from '../../../base/plan/PlanProducts.vue'
    import CategoryTree from '../../../base/category/CategoryTree.vue'
    import IsShowSelect from '../../../base/plan/IsShowSelect.vue'
    import IsOpenProductSelect from '../../../base/plan/IsOpenProductSelect.vue'
    import qs from 'qs'

    import {Quill, VueEditor} from "vue2-editor";
    import {ImageDrop} from 'quill-image-drop-module'
    import ImageResize from 'quill-image-resize-module'

    Quill.register('modules/imageDrop', ImageDrop)
    Quill.register('modules/imageResize', ImageResize)

    export default {
        components: {
            IsOpenProductSelect,
            IsShowSelect,
            CategoryTree,
            PlanProduct,
            VueEditor
        },
        data() {
            return {
                picPath:'',
                videoPath:'',
                vr720Roam:'',
                vr720Single:'',
                nav: [
                    { nav: "方案库", url: "/plan" },
                    { nav: "方案详情", url: "" }
                ],
                isShow: false,
                isEdit: false,
                searchPlanId:'',
                editorSettings: {
                    modules: {
                        imageDrop: true,
                        imageResize: {}
                    }
                },
                currentActive:0,
                designPlanId:'',
                isActive: 'mainPlan',
                isActive2:'',
                planNameCombo:[],
                subPlanNameCombo:[],
                wholeSubPlanName:[],
                introducedPicIds: [], // 富文本图片ID
                introducedPicImg: [], // 富文本图片信息
                textarea3: "",
                showHide: '',
                activeName: 'first', // tabs 默认激活项
                planId: 0,
                planInfo: {},
                disDeletePic: false,
                disDeleteSingle: false,
                disDeleteMult: false,
                disDeleteMedia: false,
                effectDiagram: {},
                planProductList: {
                    products: [],   // 智能方案列表
                    total: 0,
                    query: {
                        page: 1,
                        limit: 10,
                        categoryIds: []
                    }
                }
            }
        },
        methods: {
            getReviewPlanImgesUrl(){
                this.API.reViewPlanImges({
                    planId:this.planId
                }).then(res=>{
                    this.picPath = res.data.picPath
                    this.videoPath = res.data.videoPath
                    this.vr720Roam = res.data.vr720Roam
                    this.vr720Single = res.data.vr720Single
                })
            },
            isPer(per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            // 获取子方案的名称
            getSubPlanName(){
                    this.API.OneKeyDesignPlanName({
                        planId:this.planId,
                        companyId:sessionStorage.getItem('companyID'),
                    }).then((res) => {
                        this.planNameCombo = res.data
                    })
            },
            wholeDetail() {
                this.isEdit = false
                this.isShow = true
                this.planId = sessionStorage.getItem('mainPlanId')
                localStorage.setItem('detailPlanType','3');
                this.isActive = 'mainPlan'
                this.designPlanId = this.planId
                this.getWholeDetail()
                this.getContent(this.planId)
            },
            getWholeDetail(val){
                let ids = val || this.planId
                let obj = {
                    planId:ids,
                    companyId:sessionStorage.getItem('companyID'),
                }
                this.API.getdetailplan(obj).then((resp) => {
                    this.planInfo = resp.data
                })
            },
            // getWholeSubPlanName(){
            //     if(this.detailPlanType) {
            //         this.API.wholeGroup({
            //             planId:this.planId,
            //             companyId:sessionStorage.getItem('companyID'),
            //         }).then(res => {
            //             this.wholeSubPlanName = res.data
            //         })
            //     }
            // },
            getContent(id){
                let ids = id || this.planId
                this.designPlanId = id
                this.API.getPlanTextarea({
                        id:ids
                    }).then((res) => {
                        this.textarea3= res.data
                    })
            },
            saveContent() {
                 let ids = this.designPlanId || this.planId
                   this.API.planTextarea({
                        id:ids,
                        content:this.textarea3
                    }).then((res) => {
                        if(res) {
                            this.$message.success('保存成功')
                        }
                    })
            },
            cancelSave() {
                this.$router.push('/plan')
            },
            //获取富文本的图片id
            acquireIntroducedPicId() {
                let img = this.$refs.editorContent.getElementsByTagName("img");
                let id = [];
                if (img.length > 0) {
                    for (let i = 0; i < img.length; i++) {
                        for (let j = 0; j < this.introducedPicImg.length; j++) {
                            if (
                                decodeURI(
                                    img[i].src.substring(this.BASE_URL.sourceBaseUrl.length)
                                ) == decodeURI(this.introducedPicImg[j].picPath)
                            ) {
                                id.push(this.introducedPicImg[j].id);
                            }
                        }
                    }
                }
                this.introducedPicIds = id;
            },
            handleImageAdded(file, Editor, cursorLocation, resetUploader) {
                const isLt4M = file.size / 1024 / 1024 < 4;
                if (!isLt4M) {
                    this.$message({
                        type: 'error',
                        message: '文件大小4M以内!'
                    });
                    return;
                }
                let formData = new FormData();
                formData.append("file", file);
                formData.append("businessId ", sessionStorage.getItem("shopId"));
                this.API.blogpicUp(formData).then(res => {
                    if (res.success) {
                        let url = this.BASE_URL.sourceBaseUrl + res.obj.picPath; // Get url from response
                        if (res.obj.id) {
                            this.introducedPicImg.push(res.obj);
                        }
                        Editor.insertEmbed(cursorLocation, "image", url);
                        resetUploader();
                    } else {
                        this.$message({
                            type: "error",
                            message: res.message
                        });
                    }
                });
            },
            showButton() {
            },
            handleClick(tab, event) {
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页模块
            handleSizeChange: function (size) {
                this.planProductList.query.limit = size
                this.listPlanProducts()
            },
            toggleStatus(row) {
                const params = {
                    planId: this.planId,
                    productId: row.id,
                    status: row.displayStatus
                }
                this.API.OnekeyPlanToggleProductDisplay(params).then(resp => {
                    this.listPlanProducts()
                })
            },
            handleCurrentChange: function (currentPage) {
                this.planProductList.query.page = currentPage
                this.listPlanProducts()
            },
            getDetialMessage(planId,isPrimary,index){
                console.log(isPrimary, 'isPrimary')
               // console.log(planId)
                if(isPrimary == 0) {
                    this.isEdit = false
                }else{
                    this.isEdit = true;
                }
                this.currentActive = index
                this.planId = planId
                sessionStorage.setItem('editPlanId',planId)
                this.getBaseInfo(planId)
                this.listPlanProducts(planId)
                this.getEffectDiagram(planId)
                this.getContent(planId);
                this.getReviewPlanImgesUrl();
            },
            getWholeDetialMessage(planId,index){
                this.activeName = 'first'
                this.isShow=false
                    this.isActive = index
                    //this.getWholeDetail(planId)
                   /* this.API.OneKeyDesignPlanName({
                        planId:planId,
                        companyId:sessionStorage.getItem('companyID'),
                    }).then((res) => {
                        this.subPlanNameCombo = res.data
                    })*/
                this.getContent(planId)
                localStorage.setItem('detailPlanType','0');
                sessionStorage.setItem('editPlanId',planId)
                this.planId = planId
                //this.getBaseInfo(planId)
                this.oneKeyPlanDetail(planId)
            },
            // 全屋方案的子方案名称
            getWholePlanSubName(planId,index) {
                this.isActive2 = index
                this.getContent(planId)
                localStorage.setItem('detailPlanType','0');
                sessionStorage.setItem('editPlanId',planId)
                //this.getBaseInfo(planId)
                this.oneKeyPlanDetail(planId)
            },
            // 一键方案的详情
            oneKeyPlanDetail(id){
                this.API.OneKeyDesignPlanBaseInfo(id)
                    .then((resp) => {
                        this.planInfo = resp.data
                        if (this.planInfo.distributionStatus) {
                            this.planInfo.distributionStatus = this.planInfo.distributionStatus.split(',')
                        }

                    })
            },
            getBaseInfo(id) {
                this.isShow = true
                let ids = id || this.planId
                    this.oneKeyPlanDetail(ids)
                    this.searchProductList(ids)
            },
            getEffectDiagram(id) {
                let ids = id || this.planId
                this.API.GetOneKeyEffectDiagram(ids).then(resp => {
                    this.effectDiagram = resp.data;
                    for (let i = 0; i < this.effectDiagram.videoPic.length; i++) {
                        if (this.effectDiagram.videoPic.length === 1) {
                            this.disDeleteMedia = true;
                        }
                    }
                    for (let i = 0; i < this.effectDiagram.single720Pic.length; i++) {
                        if (this.effectDiagram.single720Pic.length === 1) {
                            this.disDeleteSingle = true;
                        }
                    }
                    for (let i = 0; i < this.effectDiagram.pics.length; i++) {
                        if (this.effectDiagram.pics.length === 1) {
                            this.disDeletePic = true;
                        }
                    }
                    for (let i = 0; i < this.effectDiagram.multi720Pic.length; i++) { //
                        if (this.effectDiagram.multi720Pic.length === 1) {
                            this.disDeleteMult = true;
                        }
                    }
                })
            },
            searchProductList(ids){
                const params = {
                    page: this.planProductList.query.page,
                    limit: this.planProductList.query.limit,
                    planId: ids,
                    productType: this.planProductList.query.categoryIds[this.planProductList.query.categoryIds.length - 1],
                    categoryCode: this.planProductList.query.categoryIds[this.planProductList.query.categoryIds.length - 1],
                    secrecy: this.planProductList.query.secrecy,
                    displayStatus: this.planProductList.query.displayStatus
                }
                this.API.ListOneKeyPlanProducts(params).then(resp => {
                    this.planProductList.products = resp.data
                    this.planProductList.total = resp.total
                })
            },
            listPlanProducts() {
                let ids = this.planId || sessionStorage.getItem('editPlanId')
                this.searchProductList(ids)
            },
            gotoAddProduct() {
                // mainPlanId
                localStorage.setItem('breadcrumb3', '方案编辑')
                let ids = this.planId || sessionStorage.getItem('editPlanId')
                console.log(ids)
                console.log(sessionStorage.getItem('editPlanId'), 'session ids')
                //if(sessionStorage.getItem('mainPlanId')) {
                /*if(this.isActive == 'mainPlan') {
                    this.$router.push(`/plan/onekey/${sessionStorage.getItem('mainPlanId')}/edit`)
                } else {
                    this.$router.push(`/plan/onekey/${ids}/edit`)
                }*/
                this.$router.push(`/plan/onekey/${ids}/edit`)
            },
            handleProductReset() {
                this.planProductList.query = {
                    page: 1,
                    limit: 10,
                    categoryIds: []
                }
                let ids = this.searchPlanId || this.planId
                 this.searchProductList(ids)
            },
            // 设置默认图片
            setDefaultPic(pic) {
                this.$confirm('确定设置么?', '提示', {
                    type: 'warning'
                }).then(() => {
                    const params = {
                        picId: pic.picId,
                        planId: this.planId
                    }
                    this.API.SetOnekeyPlanDefaultPic(params).then(resp => {
                        this.$message({
                            type: 'success',
                            message: '设置成功'
                        })
                        this.getBaseInfo()
                    })
                })
            },

            // 禁用效果图
            banPic(pic, type) {
                let j = 0;
                for (let i = 0; i < type.length; i++) {
                    if (type[i].isDeleted == 0) {
                        j++;
                    }
                }
                if (j == 1 && pic.isDeleted == 0) {
                    console.log(111)
                    this.$message({
                        type: 'warning',
                        message: '至少要启用1条'
                    })
                    return;
                }
                this.$confirm('确定此操作么?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.API.BanEffectPic(pic.picId).then(resp => {
                        this.$message({
                            type: 'success',
                            message: '成功'
                        })
                        this.getEffectDiagram()
                    })
                })
            },
            // 删除效果图
            deletePic(pic) {
                this.$confirm('确定删除么?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.API.DeleteEffectPic(pic.picId).then(resp => {
                        this.$message({
                            type: 'success',
                            message: '删除成功'
                        })
                        this.getEffectDiagram()
                    })
                })
            },
        },
        created() {
            this.planId = this.$route.params.planId
            sessionStorage.setItem('mainPlanId',this.planId)
            this.getSubPlanName()
            this.getBaseInfo()
            this.getEffectDiagram()
            this.listPlanProducts()
            this.getContent()
            this.getReviewPlanImgesUrl()
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../../assets/css/mixin";
    .el-tabs{
        .el-tab-pane{
            background: #fff;
        }
    }
    .el-tabs__nav-scroll{
        background: #fafafa!important;
    }
    .subPlanName{
        /*border-bottom: solid 1px #eaeaea;*/
        background: #f5f7fa;
        width: 96%;
        margin: 20px auto;
        li{
            display: inline-block;
            padding:10px 8px;
            color: #666;
            position: relative;
            span{
                position: inherit;
                display: inline-block;
                padding:0 10px;
                height: 28px;
                line-height: 28px;
                font-size: 12px;
                border-radius: 4px;
                min-width: 70px;
                cursor: pointer;
                text-align: center;
                &.active{
                    border: solid 1px #ff6419;
                    color: #ff6419;
                    background-color: #fff;
                }
            }
        }
        li:nth-child(2n){
            &:after{
                content: '|';
                display: inline-block;
                margin-left: 12px;
            }
            &:before{
                content: '|';
                display: inline-block;
                margin-right: 12px;
            }
        }
        div{
            padding: 10px;
            background: #eaeaea;
            p{
                position: inherit;
                display: inline-block;
                padding:0 10px;
                height: 22px;
                line-height: 22px;
                font-size: 12px;
                cursor: pointer;
                &.active{
                    background: #eaeaea;
                    color: #ff6419;
                }
            }
        }
    }
    .mainPlan{
        background: #ff6419;
        color: #fff;
        display: inline-block;
        width: 18px;
        height: 18px;
        border-radius: 10px;
        font-size: 12px;
        line-height: 18px;
        text-align: center;
        position: inherit;
        left: 3px;
    }
    .editor{
        margin:30px 0 30px 30px;
        width:80%;
        height: 500px;
        padding-bottom: 30px;
    }
    .footerBtn{
        padding: 30px;
        text-align: right;
        width:80%;
        .el-button{
            width: 100px;
        }
    }
    .tabMarTop {
        padding: 0 30px;
        margin-top: 5px;
        .viewtext{
            text-align:left;
            height:24px;
            line-height:24px;
            font-size: 14px;
            padding-left: 10px;
            margin-top: 10px;
            display: block;
            color: #999999;
            cursor: pointer;
            .searchImg{
                vertical-align: middle;
                margin-right:6px;
            }
        }
    }

    .rigthTxt {
        li {
            span {
                text-align: right;
                margin-right: 5px;
            }
        }
    }

    .planDetailFrame {
        /*padding: 20px;*/
        position: relative;
        box-sizing: padding-box;
        background: #fff;
        border-radius: $borderRadius;
        padding-top: 20px;
        .right {
            overflow: hidden;
            float: right;
        }
        header {
            h1 {
                font-weight: normal;
                font-size: 18px;
                padding-bottom: 15px;
                color: $greyOverButton;
                border-bottom: solid 1px $greyBorderEEE;
                margin-left: 20px;
            }
        }
    }

    .editBtn {
        position: absolute;
        right: 20px;
        border: solid 1px #ff6419;
        z-index: 111;
        color: #ff6419;
        top: 9px;
        width: 80px;
        text-align: center;
    }

    .button-style {
        width: 130px;
        height: 40px;
        border-radius: 20px;
        color: #ff6419;
        font-size: 16px;
        border: 1px solid #ff6419;
        background-color: #fff;
    }

    .rowpic {
        border-bottom: solid 1px #e3e3e3;
        margin: 12px 0 25px 0;
    }

    .setDefaultPic {
        position: relative;
        margin-bottom: 25px;
        margin-left: 15px;
        width: 312px;
        height: 176px;
        ul {
            position: absolute;
            width: 100%;
            bottom: 0;
            background: rgba(0, 0, 0, .5);
            height: 26px;
            text-align: center;
            li {
                display: inline-block;
                text-align: center;
                font-size: 12px;
                color: #fff;
                width: 25%;
                .el-button {
                    color: #fff;
                    height: 16px;
                    width: 100%;
                    line-height: 0 !important;
                    font-size: 12px;
                }
                &:first-of-type {
                    width: 40%;
                }
                &:hover {
                    background: rgba(0, 0, 0, .3);
                    cursor: pointer;
                }
            }
        }
    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .planLibBaseMsg {
        ul {
            min-width: 780px;
            padding-top: 20px;
            li {
                font-size: 14px;
                display: inline-block;
                margin-bottom: 46px;
                white-space: nowrap;
                span {
                    display: inline-block;
                    width: 80px;
                    vertical-align: top;
                }
                p{
                    display: inline-block;
                    border-right: solid 1px #666;
                    padding: 0 10px;
                    vertical-align: -2px;
                    height: 14px;
                    line-height: 14px;
                    &:last-of-type{
                        border: none;
                    }
                    &:first-of-type{
                        padding-left: 0;
                    }
                }
                b {
                    display: inline-block;
                    font-weight: normal;
                    min-width: 280px;
                    &.remarks {
                        white-space: normal;
                        max-width: 650px;
                        min-width: 380px;
                        word-wrap: break-word;
                    }
                }
            }
        }
    }

    .productBaseInfo {
        height: 40px;
        line-height: 40px;
        background-color: #f6f6f6;
        border-left: solid 4px $leaveButtonStatus;
        color: $color666;
        font-size: 16px;
        padding-left: 10px;
        font-weight: bold;
        margin-top: 90px;
        margin-bottom: 30px;
        width: 100%;
    }

    .el-carousel__item h3 {
        color: #475669;
        font-size: 14px;
        opacity: 0.75;
        line-height: 200px;
        margin: 0;
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }

    .showImage {
        /*img {*/
        /*width: 100%;*/
        /*}*/
    }

    .showClass {
        margin-bottom: 10px;
    }

</style>

