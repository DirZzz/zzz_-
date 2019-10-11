<template>
    <div class="updatePassword">
        <el-form label-width="110px"> <!---->
            <el-form-item label=""  v-if="isShowSelect">
                <el-radio-group v-model="isAllCompany" class="mw">
                    <el-radio :label="1">仅修改当前企业</el-radio>
                    <el-radio :label="2">修改关联的所有企业</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="新手机号：" class="starTip"> <!-- @blur="validMobile"-->
                <b class="b1">*</b>
                <el-input type="text" placeholder="请输入手机号码"  @blur="validPhone" v-model="mobilePhone" class="account"></el-input>
                <span v-if="phoneOk" class="el-icon-success" style="color:#FF6419;"></span>
                <p v-if="phoneError" style="color:red;">输入的手机号格式错误，请重新输入</p>
                <p v-if="isPhoneNull" style="color:red;">请输入手机号</p>
            </el-form-item>
            <el-form-item label="验证码：" class="starTip">
                <b class="b2">*</b>
                <span><el-input @blur="setVerify" class="valiTxt" v-model="userValCode" placeholder="请输入验证码"></el-input></span>
                <span><img :src="verifyImg" class="verify-img" @click="getImgSecurity"></span>
                <span class="isVerify el-icon-success" v-show="isVerifyShow"></span>
                <p v-if="isValiCodeNull" style="color:red;">请输入验证码</p>
                <p v-if="isValiCodeError" style="color:red;">图片验证码输入有误!</p>
            </el-form-item>
            <el-form-item label="短信验证码：" class="starTip">
                <b class="b3">*</b>
                <el-input type="text" placeholder="请输入短信验证码" @blur="valiMsg" v-model="phoneCode" class="valiTxt"></el-input> <el-button class="valCodeTxt" :disabled="mobilePhone.length < 11 || userValCode.length < 4 || timeout" @click="verifyCode">{{verifyCodeTxt}}</el-button>
                <p v-if="isMsgNull" style="color:red;">请输入短信验证码</p>
            </el-form-item>
        </el-form>
        <div class="btnFrame">
            <el-button round type="primary" :disabled="!mobilePhone.length || !userValCode.length || !phoneCode.length" @click="updateMobile">提交</el-button>
        </div>
        <el-dialog title="提示" width="364px" :visible.sync="dialogTableVisible">
            该手机号已绑定其他经销商账号，确认是否合并？
            <div slot="footer" class="dialog-footer">
                <el-button round size="mini" @click="dialogTableVisible = false">取 消</el-button>
                <el-button round size="mini" type="primary" @click="mergeCompany">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs'
    // import commonMethods from '@/filters/minixs';
    export default {
       // mixins: [commonMethods],
        inject: ['reload'],
        data(){
            return{
                isValiCodeError: false,
                isMsgNull:false,
                isPhoneNull: false,
                isValiCodeNull:false,
                timeout: false,
                phoneCode:'',
                mergeTip:'',
                isShowSelect:'',
                getValCode: false,
                dialogTableVisible:false,
                isAllCompany: 1,
                userValCode: '',
                verifyValCode: '',
                verifyImg:'',
                verifyShow: false,
                isVerifyShow: false,
                mobilePhone:'',
                verifyCodeTxt: '获取验证码',
                phoneOk:false,
                phoneError:false
            }
        },
        created () {
            this.getUserInfo()
            this.getImgSecurity()
            this.multipleCompany()
        },
        methods:{
            updateMobile(){

                if(this.isShowSelect){
                    // isShowSelect 是否是多企业经销商
                    // this.validMobile()
                    let form = new FormData();
                    form.append('mobile',this.mobilePhone); // 手机号
                    form.append('phoneCode',this.phoneCode); //验证码
                    form.append('comfirmFlag',0); // 0 不合并，1是合并，
                    form.append('enterpriseFlag',this.isAllCompany); // 是否是多企业经销商， 1仅修改当前企业;2表示修改所有关联企业
                    this.API.userMobileUpdate(form).then((res) => {
                        if(res.exceptionCode == 10010212) {
                            //this.dialogTableVisible = true;
                            this.$confirm('该手机号已绑定其他经销商账号，确认是否合并？', '提示', {
                                cancelButtonClass: 'cancelButtonClass',
                                confirmButtonClass: 'confirmButtonClass',
                                type: 'warning',
                                center: true
                            }).then((res) => {
                                let form = new FormData();
                                form.append('mobile',this.mobilePhone); // 手机号
                                form.append('phoneCode',this.phoneCode); //验证码
                                form.append('comfirmFlag',1); // 0 不合并，1是合并，
                                form.append('enterpriseFlag',this.isAllCompany); // 是否是多企业经销商， 1仅修改当前企业;2表示修改所有关联企业

                                this.API.userMobileUpdate(form).then((res) => {
                                    // this.dialogTableVisible = false
                                    //this.$message.success(res.message)
                                    if(res.success) {
                                        //this.$message.success(res.message)
                                        let usermsg = {...qs.parse(sessionStorage.getItem("loginUser")),mobile:this.mobilePhone};
                                        sessionStorage.setItem('loginUser',qs.stringify(usermsg))
                                        sessionStorage.setItem('mobile',this.mobilePhone)
                                        this.reload()
                                    }
                                })
                            })
                        } else {

                            this.$confirm('确认修改手机号码？', '提示', {
                                cancelButtonClass: 'cancelButtonClass',
                                confirmButtonClass: 'confirmButtonClass',
                                type: 'warning',
                                center: true
                            }).then(() => {
                                let form = new FormData();
                                form.append('mobile',this.mobilePhone); // 手机号
                                form.append('phoneCode',this.phoneCode); //验证码
                                form.append('comfirmFlag',0); // 0 不合并，1是合并，
                                form.append('enterpriseFlag',this.isAllCompany); // 是否是多企业经销商， 1仅修改当前企业;2表示修改所有关联企业

                                this.API.userMobileUpdate(form).then((res) => {
                                    // this.dialogTableVisible = false
                                    //this.$message.success(res.message)
                                    if(res.success) {
                                        let usermsg = {...qs.parse(sessionStorage.getItem("loginUser")),mobile:this.mobilePhone};
                                        sessionStorage.setItem('loginUser',qs.stringify(usermsg))
                                        sessionStorage.setItem('mobile',this.mobilePhone)
                                        this.$message.success(res.message)
                                        this.reload()
                                    }
                                })
                            })

                        }

                    })

                } else {
                    // 非多企业，不合并
                    this.$confirm('确认修改手机号码？', '提示', {
                        cancelButtonClass: 'cancelButtonClass',
                        confirmButtonClass: 'confirmButtonClass',
                        type: 'warning',
                        center: true
                    }).then((res) => {

                        let form = new FormData();
                        form.append('mobile',this.mobilePhone); // 手机号
                        form.append('phoneCode',this.phoneCode); //验证码
                        form.append('comfirmFlag',0); // 0 不合并，1是合并，
                        form.append('enterpriseFlag',1); // 1仅修改当前企业;2表示修改所有关联企业
                        this.API.userMobileUpdate(form).then((res) => {
                            // this.dialogTableVisible = false
                            if(res.success) {
                                //this.$message.success(res.message)
                                //this.dialogTableVisible = true
                                let usermsg = {...qs.parse(sessionStorage.getItem("loginUser")),mobile:this.mobilePhone};
                                sessionStorage.setItem('loginUser',qs.stringify(usermsg))
                                sessionStorage.setItem('mobile',this.mobilePhone);
                                this.reload()
                            }
                            if(res.exceptionCode == 10010212) {
                                //this.dialogTableVisible = true;
                                this.$confirm('该手机号已绑定其他经销商账号，确认是否合并？', '提示', {
                                    cancelButtonClass: 'cancelButtonClass',
                                    confirmButtonClass: 'confirmButtonClass',
                                    type: 'warning',
                                    center: true
                                }).then((res) => {
                                    let form = new FormData();
                                    form.append('mobile',this.mobilePhone); // 手机号
                                    form.append('phoneCode',this.phoneCode); //验证码
                                    form.append('comfirmFlag',1); // 0 不合并，1是合并，
                                    form.append('enterpriseFlag',this.isAllCompany); // 是否是多企业经销商， 1仅修改当前企业;2表示修改所有关联企业

                                    this.API.userMobileUpdate(form).then((res) => {
                                        // this.dialogTableVisible = false
                                        //this.$message.success(res.message)
                                        if(res.success) {
                                            this.$message.success(res.message)
                                            let usermsg = {...qs.parse(sessionStorage.getItem("loginUser")),mobile:this.mobilePhone};
                                            sessionStorage.setItem('loginUser',qs.stringify(usermsg))
                                            sessionStorage.setItem('mobile',this.mobilePhone)
                                            this.reload()
                                        }
                                    })
                                })
                            }
                        })
                    })
                }

            },
            // 合并企业
            mergeCompany() {
                let form = new FormData();
                form.append('mobile',this.mobilePhone); // 手机号
                form.append('phoneCode',this.phoneCode); //验证码
                form.append('comfirmFlag',1); // 0 不合并，1是合并，
                form.append('enterpriseFlag',this.isAllCompany); // 1仅修改当前企业;2表示修改所有关联企业
                this.API.userMobileUpdate(form).then((res) => {
                    // this.dialogTableVisible = false
                    this.$message.success(res.message)
                    if(res.success) {
                        this.$message.success(res.message)
                        let usermsg = {...qs.parse(sessionStorage.getItem("loginUser")),mobile:this.mobilePhone};
                        sessionStorage.setItem('loginUser',qs.stringify(usermsg))
                        sessionStorage.setItem('mobile',this.mobilePhone)
                    }
                })
            },
            getUserInfo () {
                this.loginUser = qs.parse(sessionStorage.getItem('loginUser'))
            },
            // 验证图片验证码
            setVerify () {
                if(!this.userValCode) {
                    this.isValiCodeError = false
                    this.isValiCodeNull = true
                    return
                } else {
                    this.isValiCodeNull = false
                }
                this.API.setCheckCode({
                    sysCode: 'merchantManage',
                    userId: this.loginUser.id,
                    imgCode: this.userValCode.toUpperCase()
                }).then((res) => {
                    console.log(res, 'code vali')
                    if (res.message === '图片验证码输入有误!') {
                        // this.$message.error(res.message)
                        this.isValiCodeError = true
                    } else {
                        this.isValiCodeError = false
                    }
                })
            },
            // 获取图片验证码
            getImgSecurity () {
                this.verifyImg = ''
                this.API.imgSecurityCode({
                    userId: this.loginUser.id,
                    sysCode: 'merchantManage'
                }).then((res) => {
                    this.verifyImg = this.BASE_URL.ucUrl + '/v1/center/getCode?userId=' + this.loginUser.id + '&sysCode=merchantManage'
                })
            },
            // 判断是否为多企业经销商
            multipleCompany(){
                this.API.isMultipleCompany({
                    token: sessionStorage.getItem('token')
                }).then((res) => {
                    console.log(res, 'isMultipleCompany')
                    if(res) {
                        if(res.obj ==0) {
                            this.isShowSelect = false
                        } else {
                            this.isShowSelect = true
                        }
                    }
                })
            },
            // 验证
            valiMsg(){
                if(!this.phoneCode) {
                    this.isMsgNull = true
                    return
                } else {
                    this.isMsgNull = false
                }
            },
            // 手机验证
            validPhone() {
                let mobileRegex = /^1[345678]\d{9}$/;
                if(!this.mobilePhone) {
                    this.isPhoneNull = true
                    this.phoneError = false
                } else {
                    this.isPhoneNull = false
                    if (!mobileRegex.test(this.mobilePhone)) { //
                        this.phoneError = true
                        this.phoneOk = false
                    } else {
                        this.phoneError = false
                        this.phoneOk = true
                    }

                }
            },
            // 用户信息验证

            /*userMobileCheck () {
                let reg = /^1[0-9]{10}$/
                let phoneNumber = reg.test(this.mobilePhone)
                if (phoneNumber) {
                    this.API.isPhone({
                        phoneNumber: this.mobilePhone
                    }).then((res) => {
                        if (res.message === '此手机号已经被注册') {
                            this.$message.error(res.message)
                        } else {
                            this.$message.success(res.message)
                        }
                    })
                } else {
                    this.$message.error('请输入正确的手机号')
                }
            },
            validMobile () {
                this.userMobileCheck()
            },*/

            // 获取短信验证码
            verifyCode () {
                this.API.phoneSecurityCode({
                    phoneNumber: this.mobilePhone,
                    functionType: 2
                }).then((res) => {
                    if (res.message === '手机号不能为空') {
                        this.$message.error(res.message)
                        //this.isValiCodeNull = true
                    } else {
                        let i = 60
                        let g = setInterval(() => {
                            i--
                            this.verifyCodeTxt = i + 's后重新发送'
                            this.timeout = true
                            if (i < 0) {
                                clearInterval(g)
                                this.verifyCodeTxt = '获取验证码'
                                this.timeout = false
                            }
                        }, 1000)
                    }
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
    .starTip{
        position: relative;
        .b1{
            position: absolute;
            font-weight: normal;
            color: red;
            left: -90px;
        }
        .b2{
            position: absolute;
            font-weight: normal;
            color: red;
            left: -75px;
        }
        .b3{
            position: absolute;
            font-weight: normal;
            color: red;
            left: -105px;
        }

    }
    .valCodeTxt{
         min-width: 110px;
    }
    .verify-img{
        width: 110px;
        height: 39px;
        border-radius: 6px;
        vertical-align: -15px;
    }
    .mw{
        .el-radio{
            margin-right: 15px;
        }
    }
    .account{
        width: 321px;
    }
    .updatePassword{
        padding: 20px;
        background: #fff;
        min-height: 750px;
    }
    .valiTxt{
        width: 211px;
    }
    .btnFrame{
        padding-top: 25px;
        border-top: solid 1px #ddd;
        .el-button{
            width: 133px;
            height: 40px;
            margin-left: 121px;
        }
    }
</style>