<template>
    <div class="edit-account">
        <v-nav :nav="nav"></v-nav>
        <div class="edit-account-content">
            <h1>{{headerName}}</h1>
            <p class="required-hint" v-if="!isDatails"><span>*</span>为必填项</p>
            <h2 class="headline2">账号信息</h2>
            <div class="form" v-if="!isDatails">
                <el-form ref="ruleForm" :rules="rules" :model="ruleForm" label-width="110px" >
                    <div class="form-left">
                        <el-form-item label="账号：" >
                            <el-input size="medium" v-model="ruleForm.account" placeholder="请输入账号" disabled></el-input>
                        </el-form-item>
                        <el-form-item label="密码：">
                            <el-input  type="password" size="medium" v-model="ruleForm.pwd" placeholder="请输入密码"></el-input>
                        </el-form-item>
                        <el-form-item label="确认密码：" prop="verifyPwd">
                            <el-input  type="password" size="medium" v-model="ruleForm.verifyPwd" placeholder="请输入确认密码"></el-input>
                        </el-form-item>
                        <el-form-item label="昵称：" >
                            <el-input  size="medium" v-model="ruleForm.nickName" placeholder="请输入昵称"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号：" >
                            <el-input  size="medium" v-model="ruleForm.phone" placeholder="请输入手机号"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱：" >
                            <el-input  size="medium"  @change="verifyEmail" v-model="ruleForm.email" placeholder="请输入邮箱"></el-input>
                            <p class="verify-email" v-show="showEmail">请输入正确的邮箱</p>
                        </el-form-item>
                         <el-form-item label="服务激活时间：" >
                            <p>{{ruleForm.firstLoginTime}}</p>
                        </el-form-item>
                         <el-form-item label="区域：" style="min-width:1200px;" >
                              <el-col :span="3">
                                <el-select v-model="citySelect.provide"
                                        @clear= "resetprovide"
                                        @change="getprovinceCode(citySelect.provide)"
                                        clearable
                                        placeholder="请选择省">
                                    <el-option
                                            v-for="(item, index) in provide"
                                            :key="index"
                                            :label="item.areaName"
                                            :value="item.areaName">
                                    </el-option>
                                </el-select>
                             </el-col>

                              <el-col :span="3" class="adress" >
                                <el-select clearable v-model="citySelect.city"
                                        @clear= "resetcity"
                                        @change="getCityCode(citySelect.city)"
                                        placeholder="请选择市">
                                    <el-option
                                            v-for="(item,index) in citySelects"
                                            :key="index"
                                            :label="item.areaName"
                                            :value="item.areaName">
                                    </el-option>
                                </el-select>
                            </el-col>

                             <el-col :span="3" class="adress">
                                <el-select clearable @clear= "resetarea" v-model="citySelect.area" @change="getAreaCode(citySelect.area)" placeholder="请选择区域">
                                    <el-option
                                            v-for="(item,index) in areaSelect"
                                            :key="index"
                                            :label="item.areaName"
                                            :value="item.areaName">
                                    </el-option>
                                </el-select>
                            </el-col>

                            <el-col :span="3" class="adress">
                                <el-select clearable @clear= "resetorigin" v-model="citySelect.origin" @change="getOriginCode(citySelect.origin)" placeholder="请选择街道">
                                    <el-option
                                            v-for="(item,index) in originSelect"
                                            :key="index"
                                            :label="item.areaName"
                                            :value="item.areaName">
                                    </el-option>
                                </el-select>
                            </el-col>

                        </el-form-item>
                         <el-form-item label="详细地址：" >
                            <el-input v-model="detailadress" size="medium" maxlength="50"  placeholder="请输入，允许50个字"></el-input>
                        </el-form-item>
                    </div>
                    <div class="form-right">
                        <el-form-item label="LOGO设置：" class="logo">
                            <div class="logo-item">
                                <img :src="BASE_URL.sourceBaseUrl + ruleForm.logoImgUrl" v-if="ruleForm.logoImgUrl" class="logo-img">
                                <div v-else class="headPicPath">暂无头像</div>
                                <span v-else class="logo-active"></span>
                                文件格式GIF，JPG，JPEG，PNG文件大小4M以内，尺寸1:1
                            </div>
                            <el-upload
                                    class="avatar-uploader"
                                    :action="uploadUrl"
                                    :headers="token"
                                    :show-file-list="false"
                                    :on-success="imgSuccess"
                                    :before-upload="imgUpload">
                                重新上传
                            </el-upload>
                        </el-form-item>
                        <el-form-item label="经销商企业：" v-if="isCompanyAaccount">
                            <el-select v-model="ruleForm.dealerTypeCode" filterable  placeholder="请选择经销商企业">
                                <el-option
                                        v-for="item in dealersCompany"
                                        :key="item.id"
                                        :label="item.compnayName"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                            <div class="add-dealer" @click="goAddDealer">新增经销商</div>
                        </el-form-item>
                        <el-form-item label="用户类型：">
                            <span v-if="headerName == '经销商账号编辑'">{{ruleForm.userTypeCode == 3? '经销商': ''}}</span>
                            <el-select
                                    v-if="headerName == '内部账号编辑'"
                                    v-model="ruleForm.userTypeCode"
                                    placeholder="请选择用户类型">
                                <el-option
                                        v-for="item in userType"
                                        :key="item.value"
                                        :label="item.name"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </div>
                    <div class="footer">
                        <el-form-item>
                            <el-button round class="cancel" @click="cancel">取消</el-button>
                            <el-button round class="preserve" @click="verifyPhone()">保存</el-button>
                        </el-form-item>
                    </div>
                </el-form>
            </div>
            <div class="datails" v-if="isDatails">
                <div class="datails-left">
                    <div class="datails-left-item"><p>账号：</p><span>{{datails.account}}</span></div>
                    <div class="datails-left-item"><p>密码：</p><span>***************</span></div>
                    <div class="datails-left-item"><p>昵称：</p><span>{{datails.nickName}}</span></div>
                    <div class="datails-left-item"><p>手机号：</p><span>{{datails.mobile}}</span></div>
                    <div class="datails-left-item"><p>邮箱：</p><span>{{datails.email}}</span></div>
                    <div class="datails-left-item"><p>服务激活时间</p><span>{{datails.firstLoginTime}}</span></div>
                    <div class="datails-left-item"><p>区域：</p><span>
                    {{datails.provinceCodeName}}{{datails.cityCodeName}}{{datails.areaCodeName}}{{datails.streetCodeName}}
                        </span></div>
                    <div class="datails-left-item"><p>详细地址：</p><span>{{datails.address}}</span></div>
                </div>
                <div class="datails-right">
                    <div class="datails-right-item">
                        <p>头像：</p>
                        <img v-if="datails.headPicPath" :src="BASE_URL.sourceBaseUrl + datails.headPicPath" alt="">
                        <div v-else class="headPicPath">暂无头像</div>
                    </div>
                    <div class="datails-right-item" v-if="datails.companyName"><p>经销商企业：</p> <span>{{datails.companyName}}</span></div>
                    <div class="datails-right-item"><p>用户类型：</p> <span></span>{{datails.userTypeStr}}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import qs from 'qs';
    export default {
        name: "edit-account",
        data() {
            return {
                detailadress:'',//详细地址
                areaSelect:[],
                cityCode: '',
                originSelect:[],
                search:{
                    franchiserName: '',
                    phone: '',
                    contactName: '',
                    provinceCode: '',
                    cityCode: '',
                    areaCode: '',
                    originCode: ''
                },
                citySelects:[],
                provide:[],
                citySelect:{
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },
                nav:[
                    {nav: '账号管理', url: '/business/account'},
                    {nav: '账号编辑', url: '/business/editAccount'}
                ],
                isDatails: false,
                showEmail: false,
                isCompanyAaccount: true,
                headerName: '编辑内部账号',
                ruleForm: { // 表单数据
                    account: '',
                    pwd: '',
                    verifyPwd: '',
                    nickName: '',
                    phone: '',
                    email: '',
                    logoImgUrlId: '',
                    logoImgUrl: '',
                    userTypeCode: '',
                    dealerTypeCode: '',
                    firstLoginTime:''
                },
                rules: {
                    verifyPwd:[
                        // { required: true, message: '输入密码不一致', trigger: 'blur' },
                        {validator: this.validPwd, trigger: 'blur'}
                    ]
                },
                dealersCompany: [], // 企业经销商列表
                userType: [], // 用户类型
                uploadUrl: '', // 上传头像url
                token: {Authorization: sessionStorage.getItem('token')}, // 上传头像token
                datails: { // 详情信息
                    account: '', // 账号
                    companyName: '', // 经销商企业
                    headPicPath: '', // 头像
                    email: '', //邮箱
                    mobile: '', // 手机号
                    nickName: '', // 昵称
                    password: '', // 密码
                    userType: '', // 用户类型
                    firstLoginTime:'',//服务激活时间
                    areaadress:'',
                    detailadress:'',//详细地址
                    userTypeStr:'',
                }
            };
        },
        created() {
            // this.headerTxt = sessionStorage.getItem('routerTxt');
            this.nav[1].nav = sessionStorage.getItem('routerTxt');
            this.getDealerList();
            if (sessionStorage.getItem('routerTxt') == '账号详情') {
                this.isDatails = true;
                this.getDatails();
            }
            if (sessionStorage.getItem('routerTxt') == '账号编辑') {
                this.isDatails = false;
                this.getEdit();
            }
             this.getAreaList();
        },
        methods: {
             getAreaList (){
                this.API.areaList({areaCode:0}).then((res => {
                    if(res) {
                        this.provide = res.datalist
                    }
                }))
            },
            getprovinceCode(code) {
                   this.resetprovide();
                this.provide.map( (item) => {
                    if(item.areaName == code)
                    {
                        this.cityCode = item.areaCode
                        this.search.provinceCode = item.areaCode
                    }
                })
                this.API.areaList({areaCode:this.cityCode}).then((res => {
                    if(res) {
                        this.citySelects = res.datalist
                        if(!this.citySelects.map((item) => item.areaName).includes(this.citySelect.city)) {
                            this.citySelect.city = ''
                            this.citySelect.area = ''
                            this.citySelect.origin = ''
                        }

                        //this.citySelect.city = this.citySelects[0].areaName
                        //this.getCityCode(this.citySelects[0].areaName)
                    }
                }))
            },

                getCityCode (code){
                    this.citySelects.map( (item) => {
                        if(item.areaName == code)
                        {
                            this.cityCode = item.areaCode
                            this.search.cityCode = item.areaCode
                        }
                    })

                    this.API.areaList({areaCode:this.cityCode}).then((res => {
                        if(res) {
                            this.areaSelect = res.datalist
                            if(!this.areaSelect.map((item) => item.areaName).includes(this.citySelect.area)) {
                                this.citySelect.area = ''
                                this.citySelect.origin = ''
                            }
                           /* this.citySelect.area = this.areaSelect[0].areaName
                            this.getAreaCode(this.areaSelect[0].areaName)
                            this.citySelect.origin = this.originSelect[0].areaName*/
                        }
                }))
                    this.areaSelect =[];  //清空当前区的列表
                    this.originSelect = [];  //清空街道列表
                    this.search.areaCode=''; //清空区的编码
                    this.search.originCode=''; //清空街道编码
            },
            getAreaCode (code){
                this.areaSelect.map( (item) => {
                    if(item.areaName == code)
                    {
                        this.cityCode = item.areaCode
                        this.search.areaCode = item.areaCode
                    }
                })

                this.API.areaList({areaCode:this.cityCode}).then((res => {
                    if(res) {
                        if(!this.originSelect.map((item) => item.areaName).includes(this.citySelect.origin)) {
                            this.citySelect.origin = ''
                        }
                        this.originSelect = res.datalist
                        //this.getOriginCode (this.areaSelect[0].areaName)
                        //this.citySelect.origin = this.originSelect[0].areaName
                    }
                }))
                    this.originSelect = [];  //清空街道列表
                    this.search.originCode=''; //清空街道编码
            },
            getOriginCode (code){
                this.originSelect.map( (item) => {
                    if(item.areaName == code)
                    {
                        this.cityCode = item.areaCode
                        this.search.originCode = item.areaCode
                    }
                })
                this.API.areaList({areaCode:this.cityCode}).then((res => {
                    if(res) {
                        this.originSelect = res.datalist
                    }
                }))
            },


             resetprovide(){
                 this.search.provinceCode = '';
                 this.citySelect.city='';
                 this.cityCode='';
                this.search.provinceCode = ''
                this.citySelects = []
                this.resetcity()
                this.resetarea()
                this.resetorigin()
            },
            resetcity(){
                console.log(1)
                this.search.cityCode = ''
                this.citySelects = [];
                  this.citySelect.area = '';
                   this.areaSelect =[];  //清空当前区的列表
                    this.originSelect = [];  //清空街道列表
                    this.search.areaCode=''; //清空区的编码
                    this.search.originCode=''; //清空街道编码
            },
            resetarea(){
                this.search.areaCode = ''
                this.areaSelect =[];
                 this.originSelect = [];  //清空街道列表
                    this.search.originCode=''; //清空街道编码
                this.resetorigin();
            },
            resetorigin(){
                this.search.originCode = ''
                this.originSelect = []
            },
            /*跳转新增经销商*/
            goAddDealer(){
                this.$router.push('/business/dealerBusiness');
            },
            /*验证确认密码*/
            validPwd(rule, value, callback) {
                let MobileRegex = this.ruleForm.pwd;
                if (MobileRegex != value) {
                    callback(new Error('输入密码不一致！'))
                } else {
                    callback();
                }
            },
            /*验证邮箱*/
            verifyEmail() {
                let reg = /^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
                if (this.ruleForm.email && this.ruleForm.email.length > 0 && !reg.test(this.ruleForm.email)) {
                    this.showEmail = true;
                } else {
                    this.showEmail = false;
                }
            },
            /*上传图片成功*/
            imgSuccess(res, file) {
                this.ruleForm.logoImgUrl = res.obj.picPath;
                this.ruleForm.logoImgUrlId = res.obj.id;
            },
            /*上传图片验证*/
            imgUpload(file) {
                const isJPG = file.name.split('.')[1].toUpperCase() === 'GIF' || 'JPG' || 'JPEG' || 'PNG' ;
                const isLt500K = file.size / 1024 < 500;
                if (!isJPG) {
                    this.$message.error('文件格式GIF，JPG，JPEG，PNG!');
                }
                if (!isLt500K) {
                    this.$message.error('文件大小500K以内!');
                }
            },
            /*取消*/
            cancel() {
                this.$router.push(sessionStorage.getItem('routerPath'));
            },
            /*验证手机号码后保存*/
            verifyPhone() {
                if (!this.ruleForm.phone) {
                    this.saveSubmit('ruleForm');
                    return;
                }
                if (sessionStorage.getItem('isInterior') == '内部账号') {
                    let i = 'mobile=' + this.ruleForm.phone.replace(/\s+/g, "")
                        + '&oType=edit'
                        + '&userType=' + this.ruleForm.userTypeCode
                        + '&id=' + sessionStorage.getItem('userId');

                    this.API.getInteriorPhone(i).then((res) => {
                        if (res.success) {
                            this.saveSubmit('ruleForm');
                        } else {
                            this.$message({
                                type: 'error',
                                message: res.message
                            });
                            return false;
                        }
                    });
                }
                if (sessionStorage.getItem('isInterior') == '经销商账号') {
                    let i = 'mobile=' + this.ruleForm.phone.replace(/\s+/g, "")
                        + '&oType=edit'
                        + '&id=' + sessionStorage.getItem('userId')
                        + '&companyId=' + sessionStorage.getItem('companyID');
                    this.API.getCompanyPhone(i).then((res) => {
                        if (res.success) {
                            this.saveSubmit('ruleForm')
                        } else {
                            this.$message({
                                type: 'error',
                                message: res.message
                            });
                            return false;
                        }
                    });
                }
            },
            /*保存*/
            saveSubmit(ruleForm) {
                console.log(this.$refs[ruleForm]);
                this.$refs[ruleForm].validate((valid) => {
                    if (valid) {
                        if (this.showEmail) {
                            this.$message({
                                type: 'info',
                                message: '请填写正确的邮箱'
                            });
                            return;
                        }
                        let pwd = this.ruleForm.pwd.replace(/\s|\xA0/g,"");
                        let passwordNew = this.ruleForm.verifyPwd.replace(/\s|\xA0/g,"");
                        let email =  this.ruleForm.email ? this.ruleForm.email.replace(/\s|\xA0/g,"") : undefined;
                        let obj = {
                            id: sessionStorage.getItem('userId'),
                            userName: this.ruleForm.account,
                            password: pwd.length > 0 ? pwd : undefined,
                            passwordNew: passwordNew.length > 0 ? passwordNew : undefined,
                            nickName: this.ruleForm.nickName,
                            mobile: this.ruleForm.phone,
                            email: (email && email.length) > 0 ? email : undefined,
                            headPicId: this.ruleForm.logoImgUrlId,
                            userType: this.ruleForm.userTypeCode,
                            franchiserId: this.ruleForm.dealerTypeCode,
                            logoBool: false,
                            firstLoginTime:this.ruleForm.firstLoginTime,//服务激活时间
                            address:this.detailadress,//详细地址
                            provinceCode:this.search.provinceCode, //省的编码
                            cityCode:this.search.cityCode, //市的编码
                            areaCode:this.search.areaCode, //区的编码
                            streetCode:this.search.originCode, //街道编码

                        };
                        this.$confirm('此操作将修改当前账号数据, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            this.postInteriorEdit(obj);
                        }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消保存'
                            });
                        });
                    } else {
                        this.$message({
                            message: '输入密码不一致',
                            type: 'warning'
                        });
                    }
                });
            },
            /*保存编辑内容请求，更具不同的入口请求对应的接口
            * obj：请求参数
            * */
            postInteriorEdit(obj) {
                if (sessionStorage.getItem('isInterior') == '内部账号') {
                    this.API.postInteriorEdit(obj).then((res) => {
                        if (res.success) {
                            this.$message({
                                message: res.message,
                                type: 'success'
                            });
                            this.$router.push(sessionStorage.getItem('routerPath'));
                        } else {
                            this.$message({
                                message: res.message,
                                type: 'error'
                            });
                        }
                    });
                }
                if (sessionStorage.getItem('isInterior') == '经销商账号') {
                    this.API.postCompanyEdit(obj).then((res) => {
                        if (res.success) {
                            this.$message({
                                message: res.message,
                                type: 'success'
                            });
                            this.$router.push(sessionStorage.getItem('routerPath'));
                        } else {
                            this.$message({
                                message: res.message,
                                type: 'error'
                            });
                        }
                    });
                }
            },
            /*账号编辑，根据入口判断分别请求内部账号，经销商账号的接口*/
            getEdit() {
                if (sessionStorage.getItem('isInterior') == '内部账号') {
                    this.headerName = '内部账号编辑';
                    this.nav[1].nav = '内部账号编辑';
                    this.isCompanyAaccount= false;
                    this.uploadUrl = this.BASE_URL.systemUrl + '/v1/sys/eiu/user/img/upload';
                    this.API.getInteriorDatails({
                        id: sessionStorage.getItem('userId')
                    }).then((res) => {
                        if (!res.success) {
                            this.$message({
                                type: 'error',
                                message: res.message
                            });
                        }
                        this.ruleForm.account = res.obj.account;
                        this.ruleForm.nickName = res.obj.nickName;
                        this.ruleForm.phone = res.obj.mobile;
                        this.ruleForm.email = res.obj.email;
                        this.ruleForm.dealerTypeCode = undefined;
                        this.ruleForm.logoImgUrl = res.obj.headPicPath;
                        this.ruleForm.logoImgUrlId = res.obj.headPicId;
                        this.ruleForm.firstLoginTime = res.obj.firstLoginTime;//首次服务激活时间
                        this.isUserType = res.obj.userType;
                        this.getUserType(res.obj.userType);
                        this.detailadress = res.obj.address;//详细地址回显
                        this.businessType = res.obj.businessType;//公司类型记录
                        if(res.obj.provinceCode){//判断省
                           this.citySelect.provide = res.obj.provinceCodeName;
                           this.search.provinceCode = res.obj.provinceCode;
                           this.cityCode = res.obj.cityCode;
                           this.getprovinceCode(this.citySelect.provide);
                        }
                        if(res.obj.cityCode){//判断市
                            this.citySelect.city = res.obj.cityCodeName;
                            this.search.cityCode  = res.obj.cityCode;
                            this.cityCode = res.obj.cityCode;
                            this.getCityCode(this.citySelect.provide);
                        }
                         if(res.obj.areaCode){//判断县
                           this.citySelect.area = res.obj.areaCodeName;
                           this.search.areaCode = res.obj.areaCode;
                         }
                         if(res.obj.streetCode){//判断街道
                             this.citySelect.origin = res.obj.streetCodeName;
                           this.search.originCode = res.obj.streetCode;
                         }
                    });
                }
                if (sessionStorage.getItem('isInterior') == '经销商账号') {
                    this.headerName = '经销商账号编辑';
                    this.nav[1].nav = '经销商账号编辑';
                    this.isCompanyAaccount= true;
                    this.uploadUrl = this.BASE_URL.systemUrl + '/v1/sys/fa/user/img/upload';
                    this.API.getCompanyDatails({
                        id: sessionStorage.getItem('userId')
                    }).then((res) => {
                        if (!res.success) {
                            this.$message({
                                type: 'error',
                                message: res.message
                            });
                        }
                        this.ruleForm.account = res.obj.account;
                        this.ruleForm.nickName = res.obj.nickName;
                        this.ruleForm.phone = res.obj.mobile;
                        this.ruleForm.email = res.obj.email;
                        res.obj.franchiserId ? this.ruleForm.dealerTypeCode = Number(res.obj.franchiserId) : '';
                        this.ruleForm.userTypeCode = res.obj.userType;
                        this.ruleForm.logoImgUrl = res.obj.headPicPath;
                        this.ruleForm.logoImgUrlId = res.obj.headPicId;
                        this.ruleForm.firstLoginTime = res.obj.firstLoginTime;
                       this.detailadress = res.obj.address;//详细地址回显
                        if(res.obj.provinceCode){//判断省
                           this.citySelect.provide = res.obj.provinceCodeName;
                           this.search.provinceCode = res.obj.provinceCode;
                           this.cityCode = res.obj.cityCode;
                           this.getprovinceCode(this.citySelect.provide);
                        }
                        if(res.obj.cityCode){//判断市
                            this.citySelect.city = res.obj.cityCodeName;
                            this.search.cityCode  = res.obj.cityCode;
                            this.cityCode = res.obj.cityCode;
                            this.getCityCode(this.citySelect.provide);
                        }
                         if(res.obj.areaCode){//判断县
                           this.citySelect.area = res.obj.areaCodeName;
                           this.search.areaCode = res.obj.areaCode;
                         }
                         if(res.obj.streetCode){//判断街道
                             this.citySelect.origin = res.obj.streetCodeName;
                           this.search.originCode = res.obj.streetCode;
                         }
                    });

                }
            },
            /*企业类型*/
            getDealerList() {
                this.API.getDealerList({companyId: sessionStorage.getItem('companyID')}).then((res) => {
                    if (res.datalist) {
                        this.dealersCompany = res.datalist;
                    }
                });
            },
            /*用户类型*/
            getUserType(userType) {
                this.API.getUserType({companyId: sessionStorage.getItem('businessAdministrationId')}).then(res => {
                    if (res.success) {
                        this.userType = res.datalist;
                        for (let i = 0; i < this.userType.length; i++) {
                            if (this.userType[i].value == userType) {
                                this.ruleForm.userTypeCode = userType;
                                return;
                            }
                        }
                    }
                });
            },
            /*账号详情，根据不同的入口，调用对应的接口*/
            getDatails() {
                if (sessionStorage.getItem('isInterior') == '内部账号') {
                    this.headerName = '内部账号详情';
                    this.API.getInteriorDatails({
                        id: sessionStorage.getItem('userId')
                    }).then((res) => {
                        this.datails = res.obj;
                    });
                }
                if (sessionStorage.getItem('isInterior') == '经销商账号') {
                    this.headerName = '经销商账号详情';
                    this.API.getCompanyDatails({
                        id: sessionStorage.getItem('userId')
                    }).then((res) => {
                        this.datails = res.obj;
                    });
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/css/mixin";

    .edit-account-content{
        background-color: #ffffff;
        padding: 40px 20px 20px 20px;
        h1{
            font-size: 20px;
            height: 35px;
            border-bottom: 1px solid #dddddd;
            margin-bottom: 40px;
        }
        .required-hint{
            width: 100%;
            font-size: 14px;
            color: #666666;
            span{
                color: #ff3838;
            }
        }
        .headline2{
            height: 40px;
            line-height: 40px;
            padding-left: 10px;
            font-size: 16px;
            margin: 10px 0 30px;
            border-left: 7px solid #ff6419;
            background-color: #f6f6f6;
        }
        .form{
            .el-form{
                display: flex;
                justify-content: space-between;
                flex-wrap: wrap;
                .form-left{
                    width: 50%;
                    .verify-email{
                        position: absolute;
                        line-height: 14px;
                        color: #ff6419;
                    }
                    .el-input{
                        width: 320px;
                    }
                    .el-form-item{
                        margin-bottom: 20px;
                    }
                    .adress{
                        margin-left: 20px;
                    }
                    .validEmail{
                        position: absolute;
                        top: 40px;
                        height: 14px;
                        color: #f56c6c;
                        line-height: 14px;
                    }
                }
                .form-right{
                    width: 50%;
                    .add-dealer{
                        display: inline-block;
                        width: 125px;
                        height: 40px;
                        line-height: 40px;
                        text-align: center;
                        background-color: #ffdbc9;
                        border-radius: 4px;
                        margin-left: 10px;
                        color: #ff6419;
                        cursor:pointer;
                    }
                    .logo{
                        .logo-item{
                            display: flex;
                            justify-content: left;
                            color: #999999;
                            .logo-img{
                                width: 90px;
                                height: 90px;
                                margin-right: 20px;
                            }
                            .logo-active{
                                width: 90px;
                                height: 90px;
                                background-color: #ffffff;
                                border-radius: 4px;
                                border: solid 1px #dddddd;
                                margin-right: 20px;
                            }
                        }
                        .avatar-uploader{
                            color: #ff6419;
                        }
                    }
                }
            }
        }
        .footer{
            width: 100%;
            border-top: 1px solid #dddddd;
            line-height: 80px;
            .el-form-item{
                width: 100%;
                margin-bottom: 0;
                margin-top: 20px;
                .el-button{
                    width: 130px;
                    height: 40px;
                    border: none;
                    color: #fff;
                    font-size: 16px;
                    font-weight: bold;
                }
                .preserve{
                    background-color: #ff6419;
                }
                .cancel{
                    background-color: #454545;
                }
            }
        }
        .datails{
            display: flex;
            justify-content: left;
            margin-bottom: 300px;
            .datails-left{
                width: 50%;
                .datails-left-item{
                    height: 36px;
                    line-height: 36px;
                    p{
                        display: inline-block;
                        width: 110px;
                        text-align: right;
                        margin-right: 10px;
                    }
                }
            }
            .datails-right{
                width: 50%;
                .datails-right-item{
                    display: flex;
                    justify-content: left;
                    line-height: 36px;
                    p{
                        width: 110px;
                        text-align: right;
                        margin-right: 10px;
                    }
                    img{
                        width: 90px;
                        height: 90px;
                    }
                }
            }
        }
        .headPicPath{
            width: 90px;
            height: 90px;
            line-height: 90px;
            text-align: center;
            background-color: #f6f6f6;
            margin-right: 20px;
        }
    }

</style>