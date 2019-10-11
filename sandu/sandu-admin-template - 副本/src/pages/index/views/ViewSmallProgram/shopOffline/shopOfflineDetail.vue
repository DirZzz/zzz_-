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
            <h1 class="headline">详情</h1>
            <span class="cut-off-rule"></span>
            <p class="required-hint" v-if="!isShopDetails">
            <el-row :gutter="24" class="splineFrame">
                <el-col :span="1" class="productBaseInfo">店铺信息</el-col>
                <el-col :span="23" class="rightLine"><hr></el-col>
            </el-row>
            
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                    <el-form-item label="店铺名称：" prop="shopName">
                        {{ruleForm.shopName}}
                    </el-form-item>
                    <el-form-item label="联系人姓名：" prop="contactName">
                         {{ruleForm.contactName}}
                    </el-form-item>
                    <el-form-item label="联系人电话：" prop="contactPhone">
                        {{ruleForm.contactPhone}}
                    </el-form-item>
                    <el-form-item label="区域：" prop="provinceCode">
                        {{ruleForm.longAreaName}}
                    </el-form-item>
                    <el-form-item label="详细地址：" prop="shopAddress">
                        {{ruleForm.shopAddress}}
                    </el-form-item>
                    

                    <el-form-item label="LOGO设置：" class="logo">
                        <img :src="logoImgUrl" v-if="logoImgUrl" class="logo-img">
                        <span v-else class="shop-cover-active">暂无图片</span>
                    </el-form-item>

                     <el-form-item label="店铺封面：" class="coverPic">
                        <div class="cover-img-list" v-if="ruleForm.coverPicPath!=''">
                        <img :src="item" alt="" class="cover-img-url" v-for="item in ruleForm.coverPicPath" :key="index">
                        </div>
                        <span v-else class="shop-cover-active">暂无图片</span>
                    </el-form-item>

                    <el-form-item label="店铺介绍：" class="shop-datails-item">
                       <div v-html="ruleForm.shopIntroduced" class="shop-item"></div>
                    </el-form-item>
                </el-form>
            
            
            <!--富文本html区域-->
            <div v-show="false" v-html="ruleForm.introduce" ref="editorContent"></div>
            <!--富文本html区域-->
        </div>
    </div>
</template>

<script>
    import {Quill, VueEditor} from "vue2-editor";
    import {ImageDrop} from "quill-image-drop-module";
    import ImageResize from "quill-image-resize-module";
    import "@/assets/css/vueEditer.scss";
    import myMap from "@/components/map";

    Quill.register("modules/imageDrop", ImageDrop);
    Quill.register("modules/imageResize", ImageResize);
    export default {
        name: "add-shop",
        components: {
            VueEditor,
            myMap
        },
        data() {
            return {
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
                    { nav: "线下门店详情", }
                ],
                editorSettings: {
                    modules: {
                        imageDrop: true,
                        imageResize: {}
                    }
                },
                ruleForm: {
                    id:"",
                    shopName: "",
                    contactName: "",
                    contactPhone: "",
                    provinceCode: "",
                    cityCode: "",
                    districtCode: "",
                    shopAddress:"",
                    streetCode: "",
                },
                rules: {
                    // 表单验证
                    // shopName: [
                    //     { required: true, message: "请输入店铺名称", trigger: "blur" },
                    //     { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
                    // ],
                    // shopAddress: [
                    //     { required: true, message: "请输入详细地址", trigger: "blur" },
                    //     {
                    //         min: 1,
                    //         max: 100,
                    //         message: "长度在 1 到 100 个字符",
                    //         trigger: "blur"
                    //     }
                    // ],
                    // contactName: [
                    //     { required: true, message: "请输入联系人姓名", trigger: "blur" },
                    //     { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
                    // ],
                    // contactPhone: [
                    //     { required: true, message: "请输入联系人电话", trigger: "blur" },
                    //     { validator: this.validPhone, trigger: "blur" }
                    // ],
                    // provinceCode: [
                    //     { required: true, message: "请选择区域", trigger: "blur" },
                    //     { validator: this.validArea, trigger: "blur" }
                    // ]
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
            this.getAreaList(0, "省");
            this.getShopOfflineDetail(this.$route.params.id);
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
            getShopOfflineDetail(index){
                this.API.getShopOfflineDetail({
                    id: this.$route.params.id
                }).then(res => {
                        this.ruleForm = res.data;
                        this.ruleForm.provinceCode = res.data.provinceCode;
                        this.logoImgUrl=res.data.logoPicPath;
                    this.getAreaList(res.data.provinceCode, "市");
                    this.ruleForm.cityCode = res.data.cityCode;
                    this.getAreaList(res.data.cityCode, "区");
                    this.ruleForm.districtCode = res.data.areaCode;
                    if (res.data.areaCode) {
                        this.getAreaList(res.data.areaCode, "街道");
                        this.ruleForm.streetCode = res.data.streetCode;
                    }
                });
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
                        let obj = {
                            id: this.$route.params.id,
                            companyId: sessionStorage.getItem("businessAdministrationId"),
                            modifier:sessionStorage.getItem("userId"),
                            shopName: this.ruleForm.shopName, //店铺名称
                            contactName: this.ruleForm.contactName, // 联系人姓名
                            contactPhone: this.ruleForm.contactPhone, // 联系人电话
                            shopAddress: this.ruleForm.shopAddress, // 详细地址
                            provinceCode: this.ruleForm.provinceCode, // 省
                            cityCode: this.ruleForm.cityCode, // 市
                            areaCode: this.ruleForm.districtCode, // 区
                            streetCode: this.ruleForm.streetCode, // 街道
                            logoPicId: this.logoImgUrlId != "" ? this.logoImgUrlId : undefined, // 店铺logo图片Id
                        };

                        this.$confirm("确定修改?", "提示", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        }).then(() => {
                                    this.API.shopOfflineUpdate(obj).then(res => {
                                        if (res.success) {
                                            this.$message({
                                                type: "success",
                                                message: res.message
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
                    this.area("省");
            },
            /*删除图片*/
            // deleteImg(index) {
            //     this.logoImgUrl.splice(index, 1);
            //     this.logoImgUrlId.splice(index, 1);
            // },
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
                if (
                    this.ruleForm.provinceCode.length == 0 ||
                    this.ruleForm.cityCode.length == 0 ||
                    this.ruleForm.districtCode.length == 0 ||
                    (this.street.length > 0 && this.ruleForm.streetCode.length == 0)
                ) {
                    callback(new Error("请选择完整的区域"));
                } else {
                    callback();
                }
            },
            /*电话号码验证*/
            validPhone(rule, value, callback) {
                let mobileRegex = /^1[34578]\d{9}$/;
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

    .cover-img-url {
                        display: inline-block;
                        width: 180px;
                        height: 90px;
                        margin-right: 10px;
                    }

    //  .el-dialog__wrapper{
    //      .el-dialog{
    //          .el-dialog__header{
    //              background: red!important;
    //          }
    //      }
    //  }
</style>