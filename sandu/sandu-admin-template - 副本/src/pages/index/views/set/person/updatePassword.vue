<template>
    <div class="updatePassword">
        <el-form label-width="110px">
            <el-form-item label="旧 密 码：" class="starTip">
                <span>*</span>
                <el-input type="password" minlength="6"  maxlength="20" @blur="pwdCheck" v-model="oldPassword" class="account"></el-input>
                <b v-if="isPwdRight" style="font-weight: normal; color: #FF6419"> <i class="el-icon-success"></i> 原密码正确</b>
                <b v-if="isPwdError" style="font-weight: normal; color: #FF6419">原密码不正确，请重新输入</b>
            </el-form-item>
            <el-form-item label="新 密 码：" class="starTip">
                <span>*</span>
                <el-input type="password" minlength="6" maxlength="20" v-model="newPassword"  class="account"></el-input> <b style="font-weight:normal; color: #999">6~20个字母、数字和任意组合</b>
            </el-form-item>
            <el-form-item label="确认密码：" class="starTip">
                <span>*</span>
                <el-input type="password" minlength="6" maxlength="20" v-model="newPassword2" class="account"></el-input>
            </el-form-item>
        </el-form>
        <div class="btnFrame">
            <el-button round type="primary" @click="updatePwd" :disabled="!oldPassword.length || newPassword.length < 6 || newPassword2.length < 6">提交</el-button>
        </div>
    </div>
</template>

<script>
    import md5 from 'js-md5'
    export default {
        data(){
            return{
                oldPassword:'',
                newPassword:'',
                newPassword2:'',
                isPwdRight:false,
                isPwdError:false
            }
        },
        methods:{
            encrypt(pwd) {
                const salt = 'WeB';
                const result = md5(salt + pwd);
                return md5(result);
            },
            pwdCheck(){
                if(!this.oldPassword) {
                    this.$message.error('请输入旧密码')
                    return
                }
                let form = new FormData();
                form.append('oldPassword',this.encrypt(this.oldPassword));
                this.API.userOldPwdCheck(form).then((res) => {
                    if(res.obj == 1) {
                        this.isPwdRight = true
                        this.isPwdError = false
                    } else {
                        this.isPwdError = true
                        this.isPwdRight = false
                    }
                })
            },
            updatePwd(){
                let form = new FormData();
                form.append('oldPassword',this.encrypt(this.oldPassword));
                form.append('newPassword',this.encrypt(this.newPassword));

                if(!this.newPassword2) {
                    this.$message.error('请输入确认密码')
                    return
                }
                if(this.newPassword != this.newPassword2) {
                    this.$message.error('两次输入密码不正确，请重新输入')
                    return
                }
                this.API.userPassworUpdate(form).then((res) => {
                    console.log(res,'update')
                    if(res.obj == 0) {
                        this.$message.error(res.message)
                    } else {
                        this.$message.success(res.message)
                    }
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    .starTip{
        position: relative;
        span{
            position: absolute;
            color: red;
            left: -90px;
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