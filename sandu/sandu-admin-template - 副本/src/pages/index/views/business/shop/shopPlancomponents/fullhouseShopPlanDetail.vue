<template>
    <div>
        <!-- 全屋方案详情页面 -->
        <pos-nav></pos-nav>
        <div class="planDetailFrame">
            <transition name="fade">
                <!-- <el-button round class="editBtn" size="small" v-if="isPer('solution:edit')"
                           v-show="activeName==='first'" @click="gotoAddProduct">
                    编辑
                </el-button> -->
            </transition>
            <!--全屋子案-->
        <ul class="subPlanName">
            <li><span :class="{'active': isActive == 'mainPlan'}" @click="wholeDetail(planInfo.id)">全屋方案<b class="mainPlan">主</b></span></li> <!--{{wholePlanName}}-->
            <li v-for="(name,index) in wholeSubPlanName" :key="index">
                <span :class="{'active': index == isActive}" @click="getWholeDetialMessage(name.planId,index)"> {{name.spaceTypeName}} <b class="mainPlan" v-if="name.isPrimary == 0">主</b></span>
            </li>
        </ul>

            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="基础信息" name="first" class="tabMarTop">
                    <div class="planLibBaseMsg">
                        <el-col :span="12">
                            <!--全屋方案详情-->
                            <ul  class="rigthTxt">
                                <li><span>方案名称：</span> <b>{{planInfo.planName}}</b></li>
                                <li><span>方案编号：</span> <b>{{planInfo.planCode}}</b></li>
                                <li><span>来源：</span>
                                    <b v-if="planInfo.sourceName">
                                        {{planInfo.sourceName}}
                                    </b>
                                    <b v-else-if="planInfo.origin">
                                        <b v-if="planInfo.origin === 'deliver'">企业交付</b>
                                        <b v-else-if="planInfo.origin === 'share'">分享方案</b>
                                        <b v-else>内部制作</b>
                                    </b>
                                    <b v-else>—无—</b>
                                </li>
                                <li><span>完成时间：</span>
                                    <b v-if="planInfo.gmtCreate">{{planInfo.gmtCreate}}</b>
                                    <b v-else>{{planInfo.completeDate}}</b>
                                </li>
                                <li><span>品牌：</span>
                                    <b v-if="planInfo.brandName">{{planInfo.brandName}}</b>
                                    <b v-else>—无—</b>
                                </li>
                                <li><span>设计师：</span>
                                    <b v-if="planInfo.designer">{{planInfo.designer}}</b>
                                    <b v-else-if="planInfo.userName">{{planInfo.userName}}</b>
                                    <b v-else>—无—</b>
                                </li>
                                <li><span>方案类型：</span>
                                   <b>{{planInfo.recommendedType == 1 ? '普通方案' : planInfo.recommendedType == 2 ? '智能方案': '全屋方案'}}</b>
                                </li>
                                <li><span>风格：</span>
                                    <b v-if="planInfo.planStyleName">{{planInfo.planStyleName}}</b>
                                    <b v-else-if="planInfo.designStyleName">{{planInfo.designStyleName}}</b>
                                    <b v-else>—无—</b>
                                </li>
                                <li style="width: 80%" v-if="planInfo.isPrimary == null">
                                    <span>装修报价：</span>
                                    <p class="showPrice" v-for="(item,index) in planInfo.decoratePriceInfoList" :key="index">
                                        {{item.decoratePriceTypeName}}{{item.decoratePrice}} / 平方米
                                    </p>
                                    <b v-if="planInfo.decoratePriceInfoList.length ==  0">—无—</b>
                                </li>
                                <li><span>方案描述：</span>
                                    <b class="remarks" v-if="planInfo.planDescribe">{{planInfo.planDescribe}}</b>
                                    <b class="remarks" v-else-if="planInfo.planDesc">{{planInfo.planDesc}}</b>
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
                                <img v-if="planInfo.planPicPath" :src="BASE_URL.sourceBaseUrl+planInfo.planPicPath" width="600" height="400">
                                <img v-else :src="BASE_URL.sourceBaseUrl+planInfo.picPath" width="600" height="400">
                            </div>
                        </el-col>
                    </div>
                </el-tab-pane>
                <!--
                <el-tab-pane label="设计理念" v-if="isShow" name="four" style>
                         <div v-if="textarea3"  style="padding:30px;" v-text="textarea3">
                             
                         </div>
                         <div v-else style="padding:30px;font-size:16px;text-align:center;">暂无数据 </div>
                    富文本html区域
                    <div v-show="false" v-html="textarea3" ref="editorContent"></div>
                      富文本html区域
                </el-tab-pane>-->
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

    import {VueEditor, Quill} from "vue2-editor";
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
                wholePlanName:'',
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
        computed: {
          detailPlanType(){
              if(localStorage.getItem('detailPlanType')==3){
                  return  true;
              }else{
                  return false;
              }
          }  
        },
        methods: {
            isPer(per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            wholeDetail() {
                this.isEdit = false
                this.isShow = true
                this.planId = sessionStorage.getItem('mainPlanId')
               // localStorage.setItem('detailPlanType','3');
                localStorage.setItem('isNowPlanType','true');
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
                this.API.shopFullhouseDetail(obj).then((resp) => {
                    this.planInfo = resp.data
                })
            },
            getWholeSubPlanName(){
                    this.API.wholeGroup({
                        planId:this.planId,
                        companyId:sessionStorage.getItem('companyID'),
                    }).then(res => {
                        this.wholeSubPlanName = res.data
                    })
            },
            getContent(id){
                let ids = id || this.planId
                this.designPlanId = id
                if(this.isActive=='mainPlan'){
                        this.API.getfullhouseEditorInfo({
                            id:ids
                        }).then((res) => {
                            this.textarea3= res.data
                        })
                    }else{
                        this.API.getPlanTextarea({
                            id:ids
                        }).then((res) => {
                            this.textarea3= res.data
                        })
                    }
            },
            saveContent() {
                 let ids = this.designPlanId || this.planId
               if(this.isActive=='mainPlan'){
                         this.API.fullhouseEiditorSave({
                             id:ids,
                             content:this.textarea3
                         }).then((res) => {
                             if(res) {
                                 this.$message.success('保存成功')
                             }
                         })
                     } else {
                         this.API.planTextarea({
                             id:ids,
                             content:this.textarea3
                         }).then((res) => {
                             if(res) {
                                 this.$message.success('保存成功')
                             }
                         })
                     }
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
                this.getContent(planId)
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
               // localStorage.setItem('detailPlanType','0');
               localStorage.setItem('isNowPlanType','false');
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
                this.getWholeDetail()
                this.searchProductList()
                
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
            getWholePlanName(){
                this.wholePlanName = localStorage.getItem('wholePlanName')
            }
        },
        created() {
            this.getWholePlanName()
            this.planId = this.$route.params.planId
            sessionStorage.setItem('mainPlanId',this.planId)
          //  this.getSubPlanName()
            this.getBaseInfo()
            this.getEffectDiagram()
            this.listPlanProducts()
            this.getContent()
            this.getWholeSubPlanName()
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
        border-bottom: solid 1px #eaeaea;
        li{
            display: inline-block;
            padding:0 22px;
            margin-top: 22px;
            color: #666;
            position: relative;
            span{
                position: inherit;
                display: inline-block;
                padding:0 10px;
                height: 32px;
                line-height: 32px;
                font-size: 12px;
                border-radius: 4px;
                cursor: pointer;
                &.active{
                    /*border: solid 1px #ff6419;*/
                    color: #ff6419;
                    background: #eaeaea;
                }
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
        margin-top: 5px
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
        margin-bottom: 20px;
        .right {
            overflow: hidden;
            float: right;
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

