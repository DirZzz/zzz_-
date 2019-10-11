<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <div class="addActiveFrame">
            <div class="headerTit">新增</div>
            <el-form :model="activeAdd" :rules="rules" ref="ruleForm" label-width="160px" class="demo-ruleForm">
                <el-row :gutter="24">
                    <el-col :span="12">
                        <el-form-item label="活动名称：" prop="actName">
                            <el-input clearable size="samll" v-model="activeAdd.actName" maxlength="20" 
                                      placeholder="请输入活动名称"></el-input>
                        </el-form-item>
                        <el-form-item label="活动商品名称：" prop="productName">
                            <el-input clearable size="samll" v-model="activeAdd.productName" maxlength="20"
                                      placeholder="请输入活动商品名称"></el-input>
                        </el-form-item>
                        <div style="display: flex;position: relative">
                            <span style="color:#f43b32;position: absolute;top: 10px;left: 20px">*</span>
                            <el-form-item label="活动商品图：" prop="imageUrl">
                                <div style="height: 150px;">
                                    <el-upload
                                            class="imgPosition"
                                            :action="activeUpload"
                                            :headers="headers"
                                            :data="uploadParams"
                                            :on-preview="handlePictureCardPreview"
                                            list-type=""
                                            :show-file-list="false"
                                            :on-success="handleAvatarSuccess"
                                            :before-upload="beforeAvatarUpload"
                                            :on-remove="handleRemove">
                                        <img v-if="imageUrl" :src="imageUrl" @mouseenter="showProductImg"
                                             @mouseleave="hideUploadProductImg" width="168" height="168" alt="">
                                        <!--<img v-else src="../../../assets/images/no_img.jpg" width="200" alt="">-->
                                        <div class="reUpload" v-else></div>
                                        <div class="reUpload2 posIndex" @mouseenter="showProductImg"
                                             @mouseleave="hideUploadProductImg" v-if="isShowProductImg">重新上传
                                        </div>
                                        <!--<img  v-if="img.imagePath" :src="BASE_URL.sourceBaseUrl + img.picPath">-->
                                    </el-upload>
                                </div>
                            </el-form-item>
                        </div>
                        <el-form-item label="商品实际库存：" prop="productCount">
                            <el-input style="width: 280px" clearable size="samll" v-model="activeAdd.productCount"
                                      oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" maxlength="5"
                                      placeholder="请输入商品实际库存数量"></el-input>
                        </el-form-item>
                        <el-form-item label="商品显示库存：" prop="productDisplayCount">
                            <el-input style="width: 280px" clearable size="samll" v-model="activeAdd.productDisplayCount"
                                      oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" maxlength="5"
                                      placeholder="请输入商品显示库存数量"></el-input>
                        </el-form-item>
                        <el-form-item label="商品价格：" class="mulPrice">
                            <el-form-item prop="productOriginalPrice" style="padding:0!important; margin-bottom: 20px">
                                <p><span style="color:#f43b32">*</span>原&emsp;价
                                    <el-input size="mini" clearable v-model="activeAdd.productOriginalPrice"
                                              oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                                              maxlength="8" placeholder="请输入原价"></el-input>
                                    元
                                </p>
                            </el-form-item>

                            <!--<el-form-item prop="productDiscountPrice" style="padding:0!important; margin-bottom: 20px">-->
                                <!--<p><span style="color:#f43b32">*</span>优惠价-->
                                    <!--<el-input size="mini" clearable v-model="activeAdd.productDiscountPrice"-->
                                              <!--oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"-->
                                              <!--maxlength="8" placeholder="请输入优惠价"></el-input>-->
                                    <!--元-->
                                <!--</p>-->
                            <!--</el-form-item>-->

                            <el-form-item prop="productMinPrice" style="padding:0!important; margin-bottom: 20px">
                                <p><span style="color:#f43b32">*</span>最低价
                                    <el-input size="mini" clearable v-model="activeAdd.productMinPrice"
                                              oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                                              maxlength="8" placeholder="">请输入最低价
                                    </el-input>
                                    元
                                </p>
                            </el-form-item>

                            <!--<el-form-item  prop='myCutPriceMax'  style="padding:0!important; margin-bottom: 20px"> 自己砍价金额-->

                        </el-form-item>
                        <el-form-item label="自己砍价金额：" class="myMod" required>
                            <el-form-item prop="myCutPriceMin" style="display: inline-block">
                                <el-input size="samll" clearable v-model="activeAdd.myCutPriceMin"
                                          oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" maxlength="8"
                                          placeholder="砍价最小金额"></el-input>
                                —
                            </el-form-item>
                            <el-form-item prop="myCutPriceMax" style="display: inline-block">
                                <el-input size="samll" clearable v-model="activeAdd.myCutPriceMax"
                                          oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" maxlength="8"
                                          placeholder="砍价最大金额"></el-input>
                                元
                            </el-form-item>

                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="好友砍价金额：" class="mod" required="">
                            <p>
                                <!--<el-radio v-model="activeAdd.activePriceMode" :label="1">固定金额</el-radio>-->
                                <el-form-item prop="cutMethodPriceMin" style="display: inline-block">
                                    <el-input clearable size="samll" v-model="activeAdd.cutMethodPriceMin"
                                              oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                                              maxlength="8"></el-input>
                                    —
                                </el-form-item>

                                <el-form-item prop="cutMethodPriceMax" style="display: inline-block">
                                    <el-input clearable size="samll" v-model="activeAdd.cutMethodPriceMax"
                                              oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                                              maxlength="8"></el-input>
                                    元
                                </el-form-item>
                            </p>
                            <!-- <p>
                                 <el-radio v-model="activeAdd.activePriceMode" :label="2">固定人数</el-radio>
                                 <el-input clearable size="samll" v-model="activeAdd.activePersonal1"></el-input> —
                                 <el-input clearable size="samll" v-model="activeAdd.activePersonal2"></el-input> 人
                             </p>-->
                        </el-form-item>

                        <el-form-item label="活动时间：" class="timeStyle" prop="begainTime">
                            <el-date-picker
                                    class="timer"
                                    v-model="activeAdd.begainTime"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    type="datetime"
                                    range-separator="至"
                                    placeholder="开始时间"
                                    :picker-options="pickerOptionsStart"
                            >
                            </el-date-picker>
                            -
                            <el-date-picker
                                    class="endTimer"
                                    v-model="activeAdd.endTime"
                                    type="datetime"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    range-separator="至"
                                    placeholder="结束时间"
                                    :picker-options="pickerOptionsEnd"
                            >
                            </el-date-picker>
                        </el-form-item>


                        <div style="display: flex;position: relative">
                            <span style="color:#f43b32;position: absolute;top: 10px;left: 36px">*</span>
                            <el-form-item label="转发海报：" prop="imageUrlHaiBao">
                                <div style="height: 150px;">
                                    <el-upload
                                            class="imgPosition"
                                            :action="activeUpload"
                                            :headers="headers"
                                            :data="uploadParams"
                                            :on-preview="handlePictureCardPreviewHaiBao"
                                            list-type=""
                                            :show-file-list="false"
                                            :on-success="handleAvatarSuccessHaiBao"
                                            :before-upload="beforeAvatarUploadHaiBao"
                                            :on-remove="handleRemove">
                                        <img v-if="imageUrlHaiBao" @mouseenter="showUpload" @mouseleave="hideUpload"
                                             :src="imageUrlHaiBao" width="168" height="168" alt="">
                                        <div class="reUpload" v-else></div>
                                        <div class="reUpload2 posIndex" @mouseenter="showUpload"
                                             @mouseleave="hideUpload" v-if="isShow">重新上传
                                        </div>
                                        <!--<img v-else src="../../../assets/images/no_img.jpg" width="200" alt="">-->
                                        <!--<img  v-if="img.imagePath" :src="BASE_URL.sourceBaseUrl + img.picPath">-->
                                    </el-upload>
                                </div>
                            </el-form-item>
                        </div>
                        <div style="display: flex;position: relative;top: 10px;">
                            <span style="color:#FF6419;position: absolute;top: 11px;left: 55px">*</span>
                            <el-form-item label="上架小程序：">
                                <el-checkbox-group v-model="putawayMiniProData">
                                    <el-checkbox v-for="item in putawayMiniProInfo"
                                                 :label="item.appId"
                                                 :key="item.appId">
                                        {{item.minProName}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                        </div>

<!--                        <el-form-item label="小程序：" prop="appId">-->
<!--                            <el-select v-model="activeAdd.appId" placeholder="请选择">-->
<!--                                <el-option-->
<!--                                        v-for="item in miniProInfo"-->
<!--                                        :key="item.appId"-->
<!--                                        :label="item.minProName"-->
<!--                                        :value="item.appId"-->
<!--                                        :size="miniProInfo.length"-->
<!--                                >-->
<!--                                </el-option>-->
<!--                            </el-select>-->
<!--                        </el-form-item>-->

                        <el-form-item label="活动规则：" prop="actRule">
                            <!-- <el-input type="textarea" v-model="activeAdd.actRule" maxlength="500" rows="5"></el-input> -->
                            <vue-editor
                                v-model="activeAdd.actRule"
                                class="editor">
                            </vue-editor>
                        </el-form-item>
                        <el-form-item label="新老用户限制规则：" prop="onlyAllowNew">
                            <el-radio v-model="activeAdd.onlyAllowNew" label="1">只能新用户参与</el-radio>
                            <el-radio v-model="activeAdd.onlyAllowNew" label="0">新老用户均可参与</el-radio>
                            <div class='astrict-box' v-show="activeAdd.onlyAllowNew == 0">
                                <div><span style='color: #FF6419'>*</span> 每人可帮好友砍价数</div>
                                <el-radio style='display:inline-block' v-model="lyAllowNew" label="0" @change="astrict">
                                    <el-input size='small' style='width: 70px' 
                                        v-model="activeAdd.helpCutPerAct" 
                                        type="number" 
                                        :disabled='disabled == 1'></el-input>
                                    好友/整个活动期间
                                </el-radio>
                                <el-radio style='display:inline-block; margin-left: 5px' v-model="lyAllowNew" label="1" @change="astrict">
                                    <el-input size='small' style='width: 70px' 
                                        v-model="activeAdd.helpCutPerDay" type="number" 
                                        :disabled='disabled == 0'></el-input>
                                    好友/每天
                                </el-radio>
                            </div>
                        </el-form-item>
                        <el-form-item label="配送方式：">
                            <el-radio-group v-model="activeAdd.shipType">
                                <el-radio :label="10">自提</el-radio>
                                <el-radio :label="20">快递</el-radio>
                            </el-radio-group>
                        </el-form-item>

                    </el-col>
                </el-row>
                <div class="buttonFooter">
                    <el-button size="small" @click="resetForm('ruleForm')" round>重置</el-button>
                    <el-button size="small" @click="saveData('ruleForm')" round type="primary">保存</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
    import qs from 'qs'
    import ElRadio from "element-ui/packages/radio/src/radio";
    import {formatDate} from '../../../filters/data';
    import axios from 'axios';
    import {VueEditor} from 'vue2-editor';
    import '@/assets/css/vueEditer.scss'
    // time = formatDate(new Date(time),'yyyy-MM-dd hh:mm:ss');
    export default {
        name: "activeadd",
        fixPrice: 1,
        components: {
            VueEditor
        },
        data() {
            return {
                putawayMiniProInfo: [],
                putawayMiniProData: [],
                lyAllowNew: '0',
                disabled: 0,
                miniProInfo: [],
                activeUpload: '',
                isShow: false,
                isShowProductImg: false,
                imageUrl: '',
                nav: [
                    {nav: "砍价活动列表", url: "/activelist"},
                    {nav: "新增活动", url: "/activeAdd"}
                ],
                uploadParams: {
                    platform: 'merchantManage',
                    module: 'model',
                    type: 'image'
                },
                headerToken: {Authorization: qs.parse(sessionStorage.getItem('loginUser')).token},
                imageUrlHaiBao: '',
                uploadImgHaiBaoId: '',
                activeAdd: {
                    putawayInfo: "",
                    appId: '',
                    productMinPrice: 0,
                    onlyAllowNew: '1',
                    actRule: '',
                    shipType:10
                },
                pickerOptionsStart: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7
                    }
                },
                pickerOptionsEnd: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7
                    }
                },
                rules: {
                    actRule: [
                        {required: true, message: '请填写活动规则', trigger: 'blur'},
                        {min: 0, max: 500, message: '长度在 500 个字符内', trigger: 'blur'}
                    ],
                    appId: [
                        {required: true, message: '请选择小程序', trigger: 'blur'},
                    ],
                    begainTime: [
                        {required: true, message: '请选择活动开始时间', trigger: 'blur'},
                        {validator: this.vaildbegainTime, trigger: 'blur'}
                    ],
                    endTime: [
                        {required: true, message: '请选择活动结束时间', trigger: 'blur'},
                        {validator: this.vaildbegainTime, trigger: 'blur'}
                    ],
                    actName: [
                        {required: true, message: '请输入活动名称', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 20 个字符内', trigger: 'blur'}
                    ],
                    productName: [
                        {required: true, message: '请输入活动商品名称', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 20 个字符内', trigger: 'blur'}
                    ],
                    productCount: [
                        {required: true, message: '商品实际库存', trigger: 'blur'},
                        {validator: this.checkProductCountNumber, trigger: 'blur'}
                    ],
                    productDisplayCount: [
                        {required: true, message: '商品显示库存', trigger: 'blur'},
                        {validator: this.checkProductCountNumber, trigger: 'blur'}
                    ],
                    activePrice: [
                        {required: true, message: '请输入商品价格', trigger: 'blur'}
                    ],
                    cutMethodPriceMax: [
                        {required: true, message: '好友砍价最高金额', trigger: 'blur'},
                        {validator: this.checkCutMethodPriceMax, trigger: 'blur'}
                    ],
                    cutMethodPriceMin: [
                        {required: true, message: '好友砍价最低金额', trigger: 'blur'},
                        {validator: this.checkCutMethodPriceMin, trigger: 'blur'}
                    ],
                    activeTime: [
                        {required: true, message: '请输选择活动时间', trigger: 'blur'}
                    ],
                    productOriginalPrice: [
                        {required: true, message: '请输入原价', trigger: 'blur'}
                    ],
                    // productDiscountPrice: [
                    //     {required: true, message: '请输入优惠价', trigger: 'blur'},
                    //     {validator: this.vaildPrice, trigger: 'blur'}
                    // ],
                    productMinPrice: [
                        {required: true, message: '请输入最低价', trigger: 'blur'},
                        {validator: this.vaildPrice, trigger: 'blur'}
                    ],
                    myCutPriceMax: [
                        {required: true, message: '自己砍价最高金额', trigger: 'blur'},
                        {validator: this.checkMyCutPriceMax, trigger: 'blur'}
                    ],
                    myCutPriceMin: [
                        {required: true, message: '自己砍价最低金额', trigger: 'blur'},
                        {validator: this.checkMyCutPriceMin, trigger: 'blur'}
                    ]
                }
            }
        },
        computed: {
            headers() {
                return { //上传图片时传token给后台
                    'Authorization': qs.parse(sessionStorage.getItem('loginUser')).token
                }
            }
        },

        methods: {
            getPutawayMiniProgram() {
                let companyId = sessionStorage.getItem('companyID');
                this.API.selectPutawayMiniProgram({
                    companyId: companyId
                }).then(res => {
                    this.putawayMiniProInfo = res.datalist;
                })
            },
            astrict() {
                console.log(this.lyAllowNew)
                if (this.lyAllowNew == 0) {
                    this.activeAdd.helpCutPerDay = undefined
                    this.disabled = 0
                }
                if (this.lyAllowNew == 1) {
                    this.activeAdd.helpCutPerAct = undefined
                    this.disabled = 1
                }
            },
            isValidDate(date) {
                return date instanceof Date && !isNaN(date.getTime())
            },
            vaildbegainTime(rule, value, callback) {
                if (this.isValidDate(new Date(this.activeAdd.begainTime)) && new Date(this.isValidDate(this.activeAdd.endTime))) {
                    if (new Date(this.activeAdd.begainTime).getTime() > new Date(this.activeAdd.endTime).getTime()) {
                        callback(new Error('活动开始时间不能大于活动结束时间'));
                    } else {
                        callback();
                    }
                } else {
                    callback();
                }
            },
            checkProductCountNumber(rule, value, callback) {
                let re = /^[0-9]+.?[0-9]*/;
                if (re.test(this.activeAdd.productCount)) {
                    //校验是否含有小数点
                    if (this.activeAdd.productCount.indexOf(".") > 0) {
                        callback(new Error('商品数量不能为小数'));
                    } else {
                        callback();
                    }
                } else {
                    callback(new Error('商品数量只能输入1-9的数字'));
                }
            },
            checkMyCutPriceMax(rule, value, callback) {
                if (Number(this.activeAdd.myCutPriceMax) < Number(this.activeAdd.myCutPriceMin)) {
                    callback(new Error('自己砍价最高金额不能低于自己砍价最低金额'));
                } else if (Number(this.activeAdd.myCutPriceMax) >= Number(this.activeAdd.productOriginalPrice)-(Number(this.activeAdd.productMinPrice))) {
                    callback(new Error('自己砍价金额不能高于原价-最低价'));
                } else {
                    callback();
                }
            },
            checkMyCutPriceMin(rule, value, callback){
                if (Number(this.activeAdd.myCutPriceMax) < Number(this.activeAdd.myCutPriceMin)){
                    callback(new Error('自己砍价最高金额不能低于自己砍价最低金额'));
                }else if (Number(this.activeAdd.myCutPriceMin) >= Number(this.activeAdd.productOriginalPrice)-(Number(this.activeAdd.productMinPrice))){
                    callback(new Error('自己砍价最低金额不能高于原价-最低价'));
                }else{
                    callback();
                }
            },
            checkCutMethodPriceMax(rule, value, callback) {
                if (Number(this.activeAdd.cutMethodPriceMax) < Number(this.activeAdd.cutMethodPriceMin)) {
                    callback(new Error('好友砍价最高金额不能低于好友砍价最低金额'));
                } else if (Number(this.activeAdd.cutMethodPriceMax) >= Number(this.activeAdd.productOriginalPrice)-(Number(this.activeAdd.productMinPrice))) {
                    callback(new Error('好友砍价最高金额不能高于原价-最低价'));
                } else {
                    callback();
                }
            },
            checkCutMethodPriceMin(rule, value, callback) {
                if (Number(this.activeAdd.cutMethodPriceMax) < Number(this.activeAdd.cutMethodPriceMin)) {
                    callback(new Error('好友砍价最高金额不能低于好友砍价最低金额'));
                } else if (Number(this.activeAdd.cutMethodPriceMin) >= Number(this.activeAdd.productOriginalPrice)-(Number(this.activeAdd.productMinPrice))) {
                    callback(new Error('好友砍价最低金额不能高于原价-最低价'));
                } else {
                    callback();
                }
            },
            vaildPrice(rule, value, callback) {
                if (Number(this.activeAdd.productOriginalPrice) < Number(this.activeAdd.productMinPrice)) {
                    callback(new Error('最低价不能高于原价'))
                } else {
                    callback();
                }
            },
            showUpload() {
                this.isShow = true
            },
            hideUpload() {
                this.isShow = false
            },
            showProductImg() {
                this.isShowProductImg = true
            },
            hideUploadProductImg() {
                this.isShowProductImg = false
            },
            handleAvatarSuccess(res, file) {
                this.imageUrl = this.BASE_URL.sourceBaseUrl + res.obj;
                this.activeAdd.productImg = res.obj;
                // this.uploadImgId = res.data.resId;
            },
            handleAvatarSuccessHaiBao(res, file) {
                this.imageUrlHaiBao = this.BASE_URL.sourceBaseUrl + res.obj;
                this.activeAdd.shareImg = res.obj;
                // this.uploadImgHaiBaoId = res.data.resId;
            },
            saveData(formName) {
                /*
                    actDetail (string, optional): 活动描述 ,
                    actImg (string, optional): 活动图片 ,
                    actName (string, optional): 砍价活动名称 ,
                    actRule (string, optional): 活动规则 ,
                    appId (string, optional): 微信appid ,
                    appName (string, optional): 小程序名称 ,
                    begainTime (string, optional): 活动开始时间 ,
                    companyId (integer, optional): 小程序所属企业id ,
                    companyName (string, optional): 小程序所属企业名称 ,
                    creator (string, optional): 创建人 ,
                    cutMethodInviteNumMax (integer, optional): 最多邀请好友数 ,
                    cutMethodInviteNumMin (integer, optional): 最少邀请好友数 ,
                    cutMethodPriceMax (number, optional): 好友砍价最高金额 ,
                    cutMethodPriceMin (number, optional): 好友砍价最低金额 ,
                    endTime (string, optional): 活动结束时间 ,
                    gmtCreate (string, optional): 创建时间 ,
                    gmtModified (string, optional): 修改时间 ,
                    id (string, optional): ID ,
                    isDeleted (integer, optional): 是否删除：0未删除、1已删除 ,
                    isEnable (integer, optional): 是否有效0:无效,1:有效 ,
                    modifier (string, optional): 修改人 ,
                    myCutPriceMax (number, optional): 自己砍价最高金额 ,
                    myCutPriceMin (number, optional): 自己砍价最低金额 ,
                    productCount (integer, optional): 产品数量 ,
                    productDiscountPrice (number, optional): 优惠价 ,
                    productImg (string, optional): 产品图片 ,
                    productMinPrice (number, optional): 底价 ,
                    productName (string, optional): 产品名称 ,
                    productOriginalPrice (number, optional): 原价 ,
                    productRemainCount (integer, optional): 产品剩余数量 ,
                    productVitualCount (integer, optional): 只记录虚拟扣除数量,不做逻辑处理(定时任务定时扣除) ,
                    registrationCount (integer, optional): 参与人数 ,
                    shareImg (string, optional): 转发图片 ,
                    sysReduceNum (integer, optional): 系统每小时扣减数量:(定时任务定时扣除,可当作参与人数与减少库存数)
                * */
                /* if (this.activeAdd.productImg == '' || this.activeAdd.productImg === undefined){
                     this.$message.warning("请上传商品图片");
                     return;
                 }
                 if (this.activeAdd.shareImg == '' || this.activeAdd.shareImg === undefined) {
                     this.$message.warning("请上传转发海报");
                     return;
                 }*/
                 console.log(this.activeAdd.actRule, '富文本')
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        //resolve();
                        
                        // this.activeAdd.onlyAllowNew = Number(this.activeAdd.onlyAllowNew)
                        // console.log(this.activeAdd.onlyAllowNew, 'sdsd')
                        if (this.activeAdd.onlyAllowNew == 0) {
                            console.log(this.activeAdd.helpCutPerAct)
                            this.activeAdd.helpCutPerAct = this.activeAdd.helpCutPerAct ? Number(this.activeAdd.helpCutPerAct):'';
                            this.activeAdd.helpCutPerDay = this.activeAdd.helpCutPerDay ? Number(this.activeAdd.helpCutPerDay) : '';
                        } else {
                            this.activeAdd.helpCutPerAct = undefined;
                            this.activeAdd.helpCutPerDay = undefined;
                        }
                        console.log(this.activeAdd)
                        this.activeAdd.begainTime = formatDate(new Date(this.activeAdd.begainTime), 'yyyy-MM-dd hh:mm:ss');
                        this.activeAdd.endTime = formatDate(new Date(this.activeAdd.endTime), 'yyyy-MM-dd hh:mm:ss');

                        let putaway = this.putawayMiniProData;
                        this.activeAdd.putawayInfo=putaway.join(",");
                        if(this.activeAdd.putawayInfo == null || this.activeAdd.putawayInfo == '') {
                            this.$message.warning("请选择上架的小程序");
                            return;
                        }

                        this.API.activeAdd(this.activeAdd).then(res => {
                            this.$message.success('活动新增成功')
                            if (res.success) {
                                this.$message.success(res.message);
                                this.$router.push("/activelist");
                            } else {
                                this.$message.error(res.message);
                            }

                        })
                    } else {
                        this.$message.warning("保存失败");
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.activeAdd.begainTime = '';
                this.activeAdd.endTime = '';
                this.activeAdd.actRule = '';
                this.imageUrl = '';
                this.imageUrlHaiBao = '';
            },
            beforeAvatarUploadHaiBao(file) {
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';
                const isGIF = file.type === 'image/gif';
                const isLt2M = file.size / 1024 / 1024 < 4;

                if (!isJPG && !isPNG && !isGIF) {
                    this.$message.error('上传图片只能是 GIF/JPG/PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 4M!');
                }
                return (isJPG || isPNG || isGIF) && isLt2M;
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';
                const isGIF = file.type === 'image/gif';
                const isLt2M = file.size / 1024 / 1024 < 4;

                if (!isJPG && !isPNG && !isGIF) {
                    this.$message.error('上传图片只能是 GIF/JPG/PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 4M!');
                }
                return (isJPG || isPNG || isGIF) && isLt2M;
            },
            handleRemove(file, fileList) {
                //console.log(file, fileList);
            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            handlePictureCardPreviewHaiBao(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            selectMiniProInfo() {
                this.API.getMiniProInfo().then(res => {
                    if (res.obj != '') {
                        this.miniProInfo = res.obj;
                        let actId = this.$route.params.id;
                        if (actId == 1) {
                            this.miniProInfo.forEach((item, index) => {
                                if (index == 0) {
                                    this.activeAdd.appId = item.appId;
                                }
                            })
                        }
                    }
                })
            },
            getActiveInfo(actId) {
                let form = new FormData();
                form.append('actId', actId);
                this.API.activeInfo(form).then(res => {
                    this.activeAdd = res.obj;
                    if (this.activeAdd.helpCutPerAct) {
                        this.lyAllowNew = "0";
                        this.disabled = 0;
                    }
                    if (this.activeAdd.helpCutPerDay) {
                        this.lyAllowNew = "1";
                        this.disabled = 1;
                    }
                    this.activeAdd.onlyAllowNew = res.obj.onlyAllowNew.toString()
                    this.activeAdd.productCount = this.activeAdd.productCount + '';
                    this.imageUrlHaiBao = this.BASE_URL.sourceBaseUrl + res.obj.shareImg;
                    this.imageUrl = this.BASE_URL.sourceBaseUrl + res.obj.productImg;
                    this.activeAdd.shipType==0  ? this.activeAdd.shipType=10 :''
                    this.activeAdd.productCount = this.activeAdd.productCount + '';
                    this.putawayMiniProData = this.activeAdd.putawayInfo.split(',');

                })
            }
        },
        created() {
            this.getPutawayMiniProgram();

            this.activeUpload = this.BASE_URL.systemUrl + `/v1/act/bargain/uploadActImg`
            //当前用户所在公司的小程序信息
            this.selectMiniProInfo();
            //校验当前是否是重新创建
            let actId = this.$route.params.id;
            if (actId != 1) {
                //重新创建入口
                this.getActiveInfo(actId);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .buttonFooter {
        border-top: solid 1px #e8e8e8;
        margin-top: 50px;
        padding: 40px;
        padding-left: 121px;
    }

    .posIndex {
        position: relative;
        z-index: 111;
    }

    .reUpload {
        color: #FF6419;
        width: 168px;
        height: 168px;
        border: solid 1px #e8e8e8;
        background: #fff url('../../../assets/images/icons/uploadPlug.png') no-repeat center center;
    }
    .astrict-box{
        border: 1px solid #e8e8e8;
        padding: 0 5px 20px;
    }
    .reUpload2 {
        color: #FF6419;
        width: 168px;
        height: 168px;
        border: solid 1px #e8e8e8;
        line-height: 218px;
        background: url('../../../assets/images/icons/uploadPlug.png') no-repeat center center;
    }

    .addActiveFrame {
        background: #fff;
        .el-row {
            padding-left: 63px;
            padding-right: 163px;
        }
        .headerTit {
            font-size: 20px;
            color: #666;
            border-bottom: solid 1px #e8e8e8;
            padding: 20px 0 10px 20px;
            margin-bottom: 20px;
        }
        .el-input {
            width: 400px;
        }
        .datetimes {
            width: 400px;
        }
        .el-button {
            width: 120px;
        }
        .el-radio{
            display: block;
            height: 30px;
            padding-top: 15px
        }
        .mulPrice {
            margin-bottom: 0;
            p {
               // margin-bottom: 15px;
            }
            .el-input {
                width: 120px;
            }
        }
        .myMod {
            margin-top: 20px;
            p {
                margin-bottom: 12px;
                margin-top: -41px;
            }
            .el-input {
                width: 150px;
                margin-top: -35px;
            }
        }
        .mod {
            p {
                margin-bottom: 12px;
            }
            .el-input {
                width: 150px;
            }
        }

        .timeStyle {
            .timer {
                 width: 200px;
             }
            .endTimer {
                width: 200px;
            }
        }

    }

    .imgPosition {
        position: relative;
        img {
            position: absolute;
            top: 0;
            left: 0;
            border-radius: 6px;
            z-index: 0;
        }
    }

</style>
