<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <div class="shop">
            <!--弹框-->
            <el-dialog :title="'添加'+workStyle" :visible.sync="workStyleShow" class="pop-up" width="540px">
                <el-input size="medium" class="workStyleShowSeek" v-model="workStyleShowSeek"></el-input>
                <span @click="jobType">搜索</span>
                <p>选择分类：
                    <span @click="checkWorkStyleList = []">重置</span>
                </p>
                <div class="pop-up-content">
                    <el-checkbox-group v-model="checkWorkStyleList">
                        <el-row>
                            <el-col :span="8" v-for="(item, index) in workStyleList" :key="index">
                                <el-checkbox :label="item.value">{{item.name}}</el-checkbox>
                            </el-col>
                        </el-row>
                    </el-checkbox-group>
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button round size="mini" type="primary" @click="confirmWorkStyle">确 定</el-button>
                    <el-button round size="mini" @click="cancelWorkStyle">取 消</el-button>
                </div>
            </el-dialog>
            <!--弹框-->
            <h1 class="headline">新增</h1>
            <span class="cut-off-rule"></span>
            <p class="required-hint" v-if="!isShopDetails">
                <span>*</span>为必填项</p>
            <el-row :gutter="24" class="splineFrame">
                <el-col :span="1" class="productBaseInfo">店铺信息</el-col>
                <el-col :span="23" class="rightLine"><hr></el-col>
            </el-row>
            
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                    <el-form-item label="店铺名称：" prop="name">
                        <el-input v-model="ruleForm.name" placeholder="请输入" class="shop-name" size="medium" maxlength="50"></el-input>
                    </el-form-item>
                    <el-form-item label="联系人姓名：" prop="contactsName">
                        <el-input v-model="ruleForm.contactsName" placeholder="请输入" class="contacts" size="medium" maxlength="50"></el-input>
                    </el-form-item>
                    <el-form-item label="联系人电话：" prop="contactsPhone">
                        <el-input v-model="ruleForm.contactsPhone" placeholder="请输入" class="contacts" size="medium" maxlength="13"></el-input>
                    </el-form-item>
                    <el-form-item label="区域：" class="area" prop="provinceCode">
                        <span>*</span>
                        <el-select clearable v-model="ruleForm.provinceCode" size="medium" placeholder="请选择省" @change="area('省')">
                            <el-option v-for="item in province" :key="item.areaCode" :label="item.areaName" :value="item.areaCode">
                            </el-option>
                        </el-select>
                        -
                        <el-select clearable v-model="ruleForm.cityCode" size="medium" placeholder="请选择市" @change="area('市')">
                            <el-option v-for="item in city" :key="item.areaCode" :label="item.areaName" :value="item.areaCode">
                            </el-option>
                        </el-select>
                        -
                        <el-select clearable v-model="ruleForm.districtCode" size="medium" placeholder="请选择区" @change="area('区')">
                            <el-option v-for="item in district" :key="item.areaCode" :label="item.areaName" :value="item.areaCode">
                            </el-option>
                        </el-select>
                        -
                        <el-select clearable class="noReds" v-model="ruleForm.streetCode" size="medium" placeholder="请选择街道" @change="area('街道')">
                            <el-option v-for="item in street" :key="item.areaCode" :label="item.areaName" :value="item.areaCode">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="详细地址：" prop="site">
                        <el-input v-model="ruleForm.site" placeholder="请输入" class="shop-name" size="medium" maxlength="100"></el-input>
                    </el-form-item>
                    

                    <el-form-item label="LOGO设置：" class="logo">
                        <el-upload class="avatar-uploader"
                                   :action="upLogoImgUrl"
                                   :headers="token"
                                   :data="upLogoParam"
                                   :show-file-list="false"
                                   :on-success="logoSuccess"
                                   :before-upload="upload">
                            <div class="add-img"></div>
                        </el-upload>
                        <div class="picClass">
                            <img :src="logoImgUrl" v-if="logoImgUrl" class="logo-img" @mouseover="onmouse(true)" @mouseout="onmouse(false)">
                            <img v-if="logoImgUrl != '' && imgShow" src="../../../assets/images/icons/delete3.png" class="delete-shop-cover-img" @click="deleteLogo()" @mouseover="onmouse(true)" @mouseout="onmouse(false)">
                        </div>
                        <p class="img-text">文件格式GIF，JPG，JPEG，PNG文件大小4M以内，尺寸1:1</p>
                    </el-form-item>

                    <el-form-item label="店铺封面：" class="shop-cover">
                        
                        <el-upload class="avatar-uploader"
                                   :action="upCoverImgUrl"
                                   :headers="token"
                                   :data="upCoverParam"
                                   :show-file-list="false"
                                   :on-success="shopSuccess"
                                   :before-upload="upload">
                            <div class="add-img"></div>
                        </el-upload>

                        <div class="img-list">
                            <div class="img-list-item" v-for="(item, index) in coverImgUrl" :key="index" @mouseover="coverImgUrlIndex = index" @mouseout="coverImgUrlIndex = -1">
                                <span v-show="coverImgUrlIndex == index" @click="deleteImg(index)"></span>
                                <img :src="item" class="shop-cover-img">
                            </div>
                        </div>
                        <p class="img-text">文件格式GIF，JPG，JPEG，PNG文件大小4M以内，尺寸16:9</p>
                    </el-form-item>

                    <el-form-item label="门店介绍：">
                        <vue-editor useCustomImageHandler  @image-added="handleImageAdded" v-model="ruleForm.introduce" :editorOptions="editorSettings" class="editor"></vue-editor>
                    </el-form-item>
                </el-form>
                <div class="footer">
                    <el-button type="primary" round class="reset" @click="resetForm('ruleForm')">重置</el-button>
                    <el-button type="primary" round class="save" @click="submitForm('ruleForm')">保存</el-button>
                </div>
            
            
            <!--富文本html区域-->
            <div v-show="false" v-html="ruleForm.introduce" ref="editorContent"></div>
            <!--富文本html区域-->
        </div>
    </div>
</template>

<script>
    import "@/assets/css/vueEditer.scss";
    import myMap from "@/components/map";
    import VueEditor from '@/components/vueEditor/vueEditor'
    import Quill from "quill";
    import { ImageDrop } from "quill-image-drop-module";
    import ImageResize from "quill-image-resize-module";
    import {container, ImageExtend,QuillWatch} from 'quill-image-extend-module'

    Quill.register("modules/imageDrop", ImageDrop);
    Quill.register("modules/imageResize", ImageResize);
    Quill.register('modules/ImageExtend', ImageExtend)
    export default {
        name: "add-shop",
        components: {
            VueEditor,
            myMap
        },
        data() {
            return {
                imgShow:false,
                getPoint:{
                    longitude:'',//经度
                    latitude:''////纬度
                },
                upLogoParam:{
                    fileKey:'company.shop.logoPic',
                    platform: 'web',
                    module: 'product',
                    type: 'image'
                },
                upCoverParam:{
                    fileKey:'company.shop.coverPic',
                    platform: 'web',
                    module: 'product',
                    type: 'image'
                },
                showMixNum: false,
                showMaxNum: false,
                mixPrice: "",
                maxPrice: "",
                isInit: sessionStorage.getItem("routerTxt") == "新增店铺" ? true : false,
                isShopDetails: false,
                headerTxt: "",
                token: { Authorization: sessionStorage.getItem("token") },
                nav: [
                    { nav: "线下门店管理", url: "/shopOffline" },
                    { nav: "新增线下门店", }
                ],
                editorSettings: {
                    modules: {
                        imageDrop: false,
                        imageResize: {},
                        ImageExtend:{
                            loading: false,
                            name:'file',
                            change: (xhr, formData) => {
                                xhr.setRequestHeader('Authorization',sessionStorage.getItem('token'));
                                formData.append("type", "image");
                                formData.append("platform", "web");
                                formData.append("module", "product");
                                formData.append("fileKey", "company.shop.casePic");
                            },
                            action: 'upload-error',   //拖拽功能 去掉
                            start: (value) => {

                            },
                            response: (res) =>{
                                return res.data.url
                            }
                        }
                    }
                },
                ruleForm: {
                    name: "",
                    contactsName: "",
                    contactsPhone: "",
                    provinceCode: "",
                    cityCode: "",
                    districtCode: "",
                    streetCode: "",
                    introduce: "", // 富文本编辑内容
                    introducedPicIds: [], // 富文本图片ID
                    introducedPicImg: [], //富文本图片信息
                },
                rules: {
                    // 表单验证
                    name: [
                        { required: true, message: "请输入店铺名称", trigger: "blur" },
                        { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
                    ],
                    site: [
                        { required: true, message: "请输入详细地址", trigger: "blur" },
                        {
                            min: 1,
                            max: 100,
                            message: "长度在 1 到 100 个字符",
                            trigger: "blur"
                        }
                    ],
                    contactsName: [
                        { required: true, message: "请输入联系人姓名", trigger: "blur" },
                        { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
                    ],
                    contactsPhone: [
                        { required: true, message: "请输入联系人电话", trigger: "blur" },
                        { validator: this.validPhone, trigger: "blur" }
                    ],
                    provinceCode: [
                        { required: true, message: "请选择省/市/区", trigger: "blur" },
                        { validator: this.validArea, trigger: "blur" },
                        { required: true, message: "请选择省/市/区", trigger: "change" },
                        { validator: this.validArea, trigger: "change" },
                    ]
                },
                province: [], // 省
                city: [], // 市
                district: [], // 区
                street: [], // 街道
                provinceName: "",
                districtName: "",
                streetName: "",
                cityName: "",
                townName: "",
                workStyleShow: false,
                workStyleShowSeek: "", // 搜索
                showWorkStyleList: [], // 需要显示在页面的工作类型列表
                checkWorkStyleList: [], // 已选工作类型列表
                checkWorkStyleLists: [], // 预留已选工作类型列表，用于编辑页面选择工作类型点击取消后所选的类型的还原
                workStyleList: [], // 工作类型列表
                workStyle: "", //工作种类名称
                workStyleCode: "", // 请求工作类型参数
                upLogoImgUrl: "", // 上传Logo图片url
                upCoverImgUrl: "", // 上传封面图图片url
                logoImgUrl: "", // logo图片url
                logoImgUrlId: "", // logo图片id
                coverImgUrl: [], // 封面图片url
                coverImgUrlIndex: -1,
                coverImgUrlId: [], // 封面图片id
                publishChannel: [], // 发布渠道列表
                shopDatailsList: {} // 店铺详情
            };
        },
        created() {
            this.companyType = sessionStorage.getItem('companyType');
            this.upLogoImgUrl=this.BASE_URL.uploadUrl;
            this.upCoverImgUrl=this.BASE_URL.uploadUrl;
            this.getAreaList(0, "省");
        },
        computed: {
            showPrice(){
                if(sessionStorage.getItem('companyType')==6||sessionStorage.getItem('companyType')==4) {
                    return true;
                }else{
                    return false;
                }
            }
        },
        methods: {
            /*富文本上传图片*/
            async handleImageAdded(file,fileName, Editor, cursorLocation, resetUploader) {
                if (!this.upload(file)) {
                    return;
                }
                let formData = new FormData();
                formData.append("fileKey", 'company.shop.introducedPic');
                formData.append("file ", file,fileName);
                formData.append("module ", 'product');
                formData.append("type", "image");
                formData.append("platform", "web");
                await this.API.upLoadImg(formData).then(res => {
                    if (res.code==200) {
                        res.obj= {};
                        res.obj.id = res.data.resId;
                        res.obj.picPath = res.data.url;
                        let url = res.data.url; // Get url from response
                        if (res.data.resId) {
                            this.ruleForm.introducedPicImg.push(res.obj);
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
            /*获取富文本内存在的图片的id*/
            acquireIntroducedPicId() {
                let img = this.$refs.editorContent.getElementsByTagName("img");
                let id = [];
                if (img.length > 0) {
                    for (let i = 0; i < img.length; i++) {
                        for (let j = 0; j < this.ruleForm.introducedPicImg.length; j++) {
                            if (decodeURI(img[i].src.substring(this.BASE_URL.sourceBaseUrl.length)) == decodeURI(this.ruleForm.introducedPicImg[j].picPath)
                            ) {
                                id.push(this.ruleForm.introducedPicImg[j].id);
                            }
                        }
                    }
                }
                this.ruleForm.introducedPicIds = id;
            },
            onmouse(flag){
                this.imgShow = flag;
            },
            deleteLogo(){
                this.logoImgUrl="",
                this.logoImgUrlId = ""
            },
            kkk(e){
                console.log(e)
            },
            getPointXY(pox){ //获取经纬度
                this.getPoint.longitude=pox.lng;
                this.getPoint.latitude=pox.lat;
            },
            /*确定保存选择的工作类型*/
            confirmWorkStyle() {
                let arr = [];
                for (let i = 0; i < this.checkWorkStyleList.length; i++) {
                    for (let j = 0; j < this.workStyleList.length; j++) {
                        if (this.workStyleList[j].value == this.checkWorkStyleList[i]) {
                            arr.push(this.workStyleList[j].name);
                        }
                    }
                }
                this.showWorkStyleList = arr;
                this.checkWorkStyleLists = this.checkWorkStyleList;
                this.workStyleShow = false;
            },
            /*取消选择的工作类型*/
            cancelWorkStyle() {
                this.workStyleShow = false;
                this.checkWorkStyleList = this.checkWorkStyleLists;
            },
            /*保存， 根据不同的入口，保存调对应的接口*/
            submitForm(formName) {
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.acquireIntroducedPicId();
                        let obj = {
                            companyId: sessionStorage.getItem("businessAdministrationId"),
                            creator:sessionStorage.getItem("userId"),
                            shopName: this.ruleForm.name, //店铺名称
                            contactName: this.ruleForm.contactsName, // 联系人姓名
                            contactPhone: this.ruleForm.contactsPhone, // 联系人电话
                            shopAddress: this.ruleForm.site, // 详细地址
                            provinceCode: this.ruleForm.provinceCode, // 省
                            cityCode: this.ruleForm.cityCode, // 市
                            areaCode: this.ruleForm.districtCode, // 区
                            streetCode: this.ruleForm.streetCode==-1?'':this.ruleForm.streetCode, // 街道
                            shopIntroduced:  this.ruleForm.introduce.replace(/[\n\r]/g,''), //富文本
                            logoPicId: this.logoImgUrlId != "" ? this.logoImgUrlId : undefined, // 店铺logo图片Id
                            coverPicId:
                                this.coverImgUrlId.length > 0
                                    ? this.coverImgUrlId.join(",")
                                    : undefined, // 店铺封面图片Id
                        };

                        this.$confirm("确定录入该门店?", "提示", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        }).then(() => {
                                    this.API.shopOfflineAdd(obj).then(res => {
                                        if (res.code == 200) {
                                            this.$message({
                                                type: "success",
                                                message: "创建成功"
                                            });
                                            this.$router.push("/shopOffline");
                                        } else {
                                            this.$message({
                                                type: "error",
                                                message: res.message
                                            });
                                        }
                                    });
                            })
                            .catch(() => {
                                this.$message({
                                    type: "info",
                                    message: "已取消该操作"
                                });
                            });
                    } else {
                        return false;
                    }
                });
            },
            /*重置*/
            resetForm(formName) {
                    this.$refs[formName].resetFields();
                    this.ruleForm.introduce = "";
                    this.ruleForm.checkList = [];
                    this.checkWorkStyleList = [];
                    this.showWorkStyleList = [];
                    this.logoImgUrl = "";
                    this.site="",
                    this.logoImgUrlId = "";
                    this.coverImgUrl = [];
                    this.coverImgUrlId = [];
                    this.area("省");
            },
            /*删除图片*/
            deleteImg(index) {
                this.coverImgUrl.splice(index, 1);
                this.coverImgUrlId.splice(index, 1);
            },
            /*删除工作类型*/
            deleteWorkStyle(index) {
                for (let i = 0; i < this.workStyleList.length; i++) {
                    if (this.workStyleList[i].name == this.showWorkStyleList[index]) {
                        for (let j = 0; j < this.checkWorkStyleList.length; j++) {
                            if (this.workStyleList[i].value == this.checkWorkStyleList[j]) {
                                this.checkWorkStyleList.splice(j, 1);
                                this.showWorkStyleList.splice(index, 1);
                                return;
                            }
                        }
                    }
                }
            },
            /*选择区域校验*/
            validArea(rule, value, callback) {
                // console.log(this.street,'this.street');
                // console.log(typeof(this.ruleForm.districtCode), 'this.ruleForm.districtCode');
                if (this.ruleForm.cityCode.length == 0 && this.ruleForm.districtCode.length == 0) {
                    callback(new Error("请选择市/区"));
                } else if(this.ruleForm.districtCode.length == 0) {
                    callback(new Error("请选择区"));
                } else {
                    callback();
                }
            },
            /*电话号码验证*/
            validPhone(rule, value, callback) {
                let mobileRegex = /^1[3456789]\d{9}$/;
                let phoneRegex = /^0(\d{2,4})([-]{0,1})\d{3,8}$/;
                let fix400 = /^[48]00[-]{0,1}\d{6,7}$/; // /^400[0-9]{7}/
                if (
                    fix400.test(value) ||
                    phoneRegex.test(value) ||
                    mobileRegex.test(value)
                ) {
                    //
                    callback();
                } else {
                    callback(new Error("电话号码格式不正确！"));
                }
            },
            /*上传LOGO成功回调*/
            logoSuccess(res, file) {
                if (res.code==200) {
                    this.logoImgUrl = res.data.url;
                    this.logoImgUrlId = res.data.resId;
                } else {
                    this.$message({
                        type: "error",
                        message: res.message
                    });
                }
            },
            /*上传店铺封面成功回调*/
            shopSuccess(res, file) {
                if (this.coverImgUrl.length > 5) {
                    this.$message({
                        type: "error",
                        message: "最多上传6张封面图"
                    });
                    return;
                }
                if (res.code==200) {
                    console.log('jahjahah')
                    this.coverImgUrl.push(res.data.url);
                    this.coverImgUrlId.push(res.data.resId);
                } else {
                    this.$message({
                        type: "error",
                        message: res.message
                    });
                }
            },
            
            /*上传图片前验证图片回调*/
            upload(file) {
                const isJPG =
                    file.name.split(".")[1].toUpperCase() === "GIF" ||
                    "JPG" ||
                    "JPEG" ||
                    "PNG";
                const isLt4M = file.size / 1024 / 1024 < 4;
                if (!isJPG) {
                    this.$message.error("文件格式GIF，JPG，JPEG，PNG!");
                    return false;
                } else if (!isLt4M) {
                    this.$message.error("文件大小4M以内!");
                    return false;
                } else {
                    return true;
                }
            },
            /*发布类型*/
            releaseType() {
                this.API.jobType({
                    type: "shopReleasePlatform"
                }).then(res => {
                    if (res.datalist) {
                        this.publishChannel = res.datalist;
                        if (sessionStorage.getItem("companyType") != 2) {
                            for (let i = 0; i < res.datalist.length; i++) {
                                if (res.datalist[i].value == 1) {
                                    this.publishChannel.splice(i, 1);
                                }
                            }
                        }
                    }
                });
            },
            /*工作种类*/
            jobType() {
                let obj = {
                    type: this.workStyleCode,
                    name:
                        this.workStyleShowSeek.length > 0 ? this.workStyleShowSeek : undefined
                };
                if (this.workStyleCode == "") {
                    return;
                }
                this.API.jobType(obj).then(res => {
                    if (res.datalist) {
                        this.workStyleList = res.datalist;
                    }
                });
            },
            /*判断企业类型
                    * sessionStorage.getItem('companyType')保存企业类型
                    * 1：厂商，2：经销商，3：门店，4：设计公司，5：装修公式，6：设计师，7：工长，8：中介
                    * this.workStyleCode： 请求工作类型参数，
                    * 设计公司，装修公司，设计师为:  goodStyle;
                    * 工长为: constructionType；
                    * */
            
            /*回显编辑店铺*/
            editShop() {
                this.API.shopDatails({
                    shopId: sessionStorage.getItem("shopId")
                }).then(res => {
                    if(res.obj.designFeeStarting){
                        this.mixPrice = res.obj.designFeeStarting;
                        this.maxPrice = res.obj.designFeeEnding ;
                    }
                    this.ruleForm.name = res.obj.shopName;
                    this.ruleForm.contactsName = res.obj.contactName;
                    this.ruleForm.contactsPhone = res.obj.contactPhone;
                    this.ruleForm.provinceCode = res.obj.provinceCode;
                    this.getAreaList(res.obj.provinceCode, "市");
                    this.ruleForm.cityCode = res.obj.cityCode;
                    this.getAreaList(res.obj.cityCode, "区");
                    this.ruleForm.districtCode = res.obj.areaCode;
                    if (res.obj.areaCode) {
                        this.getAreaList(res.obj.areaCode, "街道");
                        this.ruleForm.streetCode = res.obj.streetCode;
                    }
                    if (res.obj.shopIntroduced) {
                        this.ruleForm.introduce = res.obj.shopIntroduced;
                    }
                    this.ruleForm.site = res.obj.shopAddress;
                    for (let i = 0; i < res.obj.categoryIds.split(",").length; i++) {
                        this.checkWorkStyleList.push(
                            Number(res.obj.categoryIds.split(",")[i])
                        );
                        this.checkWorkStyleLists.push(
                            Number(res.obj.categoryIds.split(",")[i])
                        );
                    }
                    if (res.obj.categoryName) {
                        this.showWorkStyleList = res.obj.categoryName.split(",");
                    }
                    if (res.obj.coverList) {
                        let arr = [],
                            arrId = [];
                        for (let i = 0; i < res.obj.coverList.length; i++) {
                            arr.push(res.obj.coverList[i].resPath.indexOf('http')>-1 ? res.obj.coverList[i].resPath : this.BASE_URL.sourceBaseUrl+res.obj.coverList[i].resPath);
                            arrId.push(res.obj.coverList[i].resId);
                        }
                        this.coverImgUrl = arr;
                        this.coverImgUrlId = arrId;
                    }
                    for (
                        let i = 0;
                        i < res.obj.releasePlatformValues.split(",").length;
                        i++
                    ) {
                        this.ruleForm.checkList.push(
                            Number(res.obj.releasePlatformValues.split(",")[i])
                        );
                    }
                    this.logoImgUrl =this.BASE_URL.sourceBaseUrl + res.obj.logoUrl;
                    this.logoImgUrlId =res.obj.logoId;
                });
            },
            /*店铺详情*/
            shopDatails() {
                this.isShopDetails = true;
                this.API.shopDatails({
                    shopId: sessionStorage.getItem("shopId")
                }).then(res => {
                    console.log(res);
                    if (res.obj) {
                        this.shopDatailsList = res.obj;
                        if (res.obj.categoryName && res.obj.categoryName.indexOf(",") > -1) {
                            this.showWorkStyleList = res.obj.categoryName.split(",");
                        }
                    }
                });
            },
            /*省市区联动
                    * type：用于判断操作什么数据
                    * */
            area(type) {
                console.log(this.ruleForm.provinceCode);
                if (type == "省") {
                    this.ruleForm.cityCode = "";
                    this.ruleForm.districtCode = "";
                    this.ruleForm.streetCode = "";
                    this.city = [];
                    this.district = [];
                    this.street = [];
                    this.getAreaList(this.ruleForm.provinceCode, "市");
                    this.province.map(res => {
                        if (this.ruleForm.provinceCode == res.areaCode) {
                            this.provinceName = res.areaName;
                            //this.$refs.myMap.searchPosition(res.areaName);
                        }
                    });
                }
                if (type == "市") {
                    this.ruleForm.districtCode = "";
                    this.ruleForm.streetCode = "";
                    this.district = [];
                    this.street = [];
                    this.getAreaList(this.ruleForm.cityCode, "区");
                    this.city.map(res => {
                        if (this.ruleForm.cityCode == res.areaCode) {
                            this.districtName = res.areaName;
                            //this.$refs.myMap.searchPosition(this.provinceName + res.areaName);
                        }
                    });
                }
                if (type == "区") {
                    this.ruleForm.streetCode = "";
                    this.street = [];
                    this.getAreaList(this.ruleForm.districtCode, "街道");
                    this.district.map(res => {
                        if (this.ruleForm.districtCode == res.areaCode) {
                            this.streetName = res.areaName;
                            // this.$refs.myMap.searchPosition(
                            //   this.provinceName + this.districtName + res.areaName
                            // );
                        }
                    });
                }
                if (type == "街道") {
                    this.street.map(res => {
                        if (this.ruleForm.streetCode == res.areaCode) {
                            this.townName = res.areaName;
                        }
                    });
                }
            },
            /*请求省市区接口
                    * areaCode： 请求参数省市区code
                    * type：用于判断请求什么数据
                    * */
            getAreaList(areaCode, type) {
                this.API.areaList({ areaCode: areaCode }).then(res => {
                    if (res.datalist) {
                        if (type == "省") {
                            this.province = res.datalist;
                        }
                        if (type == "市") {
                            this.city = res.datalist;
                        }
                        if (type == "区") {
                            this.district = res.datalist;
                        }
                        if (type == "街道") {
                            res.datalist.unshift({areaCode:-1,areaName:'暂不选择'})
                            this.street = res.datalist;
                        }
                    }
                });
            }
        }
    };
</script>

<style lang="scss" scoped>
    @import "../../../assets/css/mixin";
    .shop {
        padding: 20px 40px 20px 40px;
        background-color: #ffffff;

        .nav {
            width: 100%;
            height: 15px;
            margin-bottom: 10px;
        }
        .headline {
            width: 100%;
            font-size: 18px;
            color: #333333;
        }
        .cut-off-rule {
            display: block;
            width: 100%;
            height: 1px;
            background-color: #eeeeee;
            margin: 10px 0 40px;
        }
        .required-hint {
            width: 100%;
            font-size: 14px;
            color: #666666;
            span {
                color: #ff3838;
            }
        }
        .headline2 {
            width: 100%;
            height: 40px;
            line-height: 40px;
            padding-left: 10px;
            font-size: 16px;
            margin: 10px 0 30px;
            color: #333333;
            border-left: 7px solid #ff6419;
            background-color: #f6f6f6;
        }

        .demo-ruleForm {
            overflow: hidden;
            padding-left: 20px;
            .publish {
                color: #333333;
            }
            .editor {
                width: 850px;
            }
            .el-form-item {
                margin-bottom: 20px;
            }
            .shop-name {
                width: 500px;
            }
            .contacts {
                width: 320px;
            }
            .area {
                position: relative;
                color: #999999;
                span {
                    position: absolute;
                    top: 0;
                    left: -63px;
                    color: #ff6419;
                }
                .el-select {
                    width: 150px;
                }
            }
            .work-style {
                p {
                    color: #ff6419;
                    cursor: pointer;
                }
                div {
                    display: flex;
                    justify-content: left;
                    font-size: 14px;
                    color: #333333;
                    flex-wrap: wrap;
                    margin-bottom: 10px;
                    .work-style-item {
                        display: flex;
                        justify-content: space-between;
                        min-width: 94px;
                        height: 30px;
                        line-height: 30px;
                        background-color: #fafafa;
                        border-radius: 4px;
                        border: solid 1px #dddddd;
                        padding: 0 10px;
                        margin-right: 10px;
                        a {
                            display: inline-block;
                            width: 18px;
                            height: 18px;
                            line-height: 18px;
                            text-align: center;
                            border-radius: 50%;
                            margin-top: 6px;
                            background-color: #ffffff;
                            border: solid 1px #dddddd;
                            cursor: pointer;
                        }
                    }
                }
            }
            .logo,
            .shop-cover {
                .logo-img {
                    width: 90px;
                    height: 90px;
                    border-radius: 4px;
                    margin-right: 20px;
                    display: inline-block;
                    vertical-align: bottom;
                }
                .img-list {
                    display: inline-block;
                    .img-list-item {
                        display: inline-block;
                        position: relative;
                        span {
                            position: absolute;
                            top: 36px;
                            left: 82px;
                            display: block;
                            width: 18px;
                            height: 16px;
                            background: url("../../../assets/images/icons/delete3.png");
                        }
                        .shop-cover-img {
                            width: 180px;
                            height: 90px;
                            border-radius: 4px;
                            margin-right: 10px;
                            vertical-align: bottom;
                        }
                    }
                }
                .img-text {
                    color: #999999;
                }
                .add-img {
                    width: 22px;
                    height: 22px;
                    margin: 34px 34px;
                    background: url("../../../assets/images/icons/uploadPlug.png") no-repeat
                    0 0;
                }
                .avatar-uploader {
                    display: inline-block;
                    vertical-align: bottom;
                    width: 90px;
                    height: 90px;
                    background-color: #ffffff;
                    border-radius: 4px;
                    border: solid 1px #dddddd;
                    margin-right: 20px;
                }
            }
        }
        .footer {
            height: 80px;
            border-top: 1px solid #eeeeee;
            padding-left: 80px;
            .el-button {
                width: 130px;
                height: 40px;
                margin-top: 40px;
                margin-right: 40px;
            }
            .reset {
                background-color: #454545;
                border: solid 1px #454545;
            }
        }

        .pop-up {
            .workStyleShowSeek {
                width: 400px;
                margin: 0 20px 20px 20px;
            }
            span {
                color: #ff6419;
                cursor: pointer;
            }
            p {
                padding-left: 20px;
                margin-bottom: 10px;
            }
            .pop-up-content {
                width: 440px;
                height: 340px;
                padding: 10px;
                margin-left: 20px;
                border: solid 1px #dddddd;
            }
            .dialog-footer {
                // border-top: 1px solid #dddddd;
                height: 56px;
                line-height: 56px;
            }
        }
        /********************店铺详情*********************/
        .shop-datails {
            padding: 0 20px;
            margin-bottom: 20px;
            font-size: 14px;
            .shop-datails-item {
                min-height: 30px;
                line-height: 30px;
                margin-bottom: 20px;
                display: flex;
                justify-content: left;
                .shop-item {
                    word-wrap: break-word;
                    word-break: break-all;
                }
                p {
                    min-width: 100px;
                    text-align: right;
                    margin-right: 20px;
                }
                .logoImgUrl {
                    display: inline-block;
                    width: 90px;
                    height: 90px;
                }
                .shop-logo-active {
                    display: inline-block;
                    width: 90px;
                    height: 90px;
                    border: 1px solid #dddddd;
                    line-height: 90px;
                    text-align: center;
                }
                .cover-img-list {
                    .cover-img-url {
                        display: inline-block;
                        width: 180px;
                        height: 90px;
                        margin-right: 10px;
                    }
                }
                .shop-cover-active {
                    display: inline-block;
                    width: 180px;
                    height: 90px;
                    border: 1px solid #dddddd;
                    line-height: 90px;
                    text-align: center;
                }
                div {
                    display: flex;
                    justify-content: left;
                    font-size: 14px;
                    color: #333333;
                    flex-wrap: wrap;
                    margin-bottom: 10px;
                    .work-style-item {
                        display: flex;
                        justify-content: space-around;
                        height: 30px;
                        line-height: 30px;
                        background-color: #fafafa;
                        border-radius: 4px;
                        border: solid 1px #dddddd;
                        padding: 0 10px;
                        margin-right: 10px;
                        a {
                            width: 7px;
                            height: 7px;
                            background-color: #999999;
                            border-radius: 50%;
                            margin-top: 12px;
                            margin-right: 10px;
                        }
                    }
                }
            }
        }
    }

    .picClass {
        width: 90px;
        height: 90px;
        border-radius: 4px;
        margin-right: 20px;
        display: inline-block;
        vertical-align: bottom;
        position: relative;
    }
    .delete-shop-cover-img{
        position: absolute;
        top: 37px;
        left: 36px;
        cursor: pointer;
    }

    //  .el-dialog__wrapper{
    //      .el-dialog{
    //          .el-dialog__header{
    //              background: red!important;
    //          }
    //      }
    //  }
</style>