<template>
  <div class="personalFrame">
    <!--遮罩层-->
    <div class="shade-box" v-show="!edit"></div>
    <div class="shade-box-mask" v-show="shadeMask"></div>
    <!--遮罩层-->
    <!--弹框-->
    <div class="compile-box" v-show="!edit">
      <div class="compile-header">修改个人信息 <span class="delete" @click="cancelEdit"></span></div>
      <div class="compile-content">
        <div class="nickname">昵称：<input type="text" v-model="nickname" @blur="nicknameMobile"></div>
        <p v-show="nameHint" class="nameHint">{{nicknameHint}}</p>
        <div class="gender">性别：
          <span class="boy"><input type="checkbox" value="男" ref="boy" @click="checkedBoy"></span>男
          <span class="woman"><input type="checkbox" value="女" ref="woman" @click="checkedWoman"></span>女
        </div>
        <p v-show="checkedNameShow" class="checkedName">请选择性别</p>
        <div class="nickname">邮箱：<input type="text" v-model="email"  @blur="emailMobile"></div>
        <p v-show="mailboxHint" class="mailboxHint">请输入正确的邮箱</p>
        <div class="btn">
          <button class="yes" @click="saveEdit">保存</button>
          <button class="no" @click="cancelEdit">取消</button>
        </div>
      </div>
    </div>
    <!--弹框-->
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="个人设置" class="userMsg" name="first">
        <el-row :gutter="24">
          <el-col class="headShow" :span="11">
            <img src="../../../assets/images/headImg.png" class="headImg" alt="">
            <el-button v-if="!edit" round size="mini" class="el-button--primary"> 重新上传</el-button>
          </el-col>
          <el-col :span="10" style="margin-top: 20px">
            <ul>
              <li v-if="getUserBaseInfo.nickName"><span>登录名：</span> <span>{{getUserBaseInfo.nickName}}</span></li>
              <li><span>手机号：</span> <span>{{getUserBaseInfo.mobile}}</span></li>
              <li><span>昵  称：</span> <span>{{getUserBaseInfo.userName}}</span>
                <!--<el-input v-if="!edit" v-model="getUserBaseInfo.nick"></el-input>-->
              </li>
              <li>
                <span>性别：</span> <span>{{getUserBaseInfo.sex == 1 ? '男' : '女' }}</span>
                <!--<span v-if="!edit">-->
                  <!--<el-radio-group v-model="getUserBaseInfo.sex" @change="selectGender">-->
                    <!--<el-radio v-model="radio" label="1">男</el-radio>-->
                    <!--<el-radio v-model="radio" label="2">女</el-radio>-->
                  <!--</el-radio-group>-->
                <!--</span>-->
              </li>
              <li>
                <span>邮箱：</span>
                <span>{{getUserBaseInfo.email}}</span>
                <!--<span><el-input v-if="!edit" v-model="getUserBaseInfo.email" @blur="emailMobile"></el-input></span>-->
                <span class="isVerify el-icon-success" v-show="okEmail"></span>
                <span class="isOk" v-show="isEmail">{{emailTxt}}</span>
              </li>
            </ul>
          </el-col>
          <el-col :span="2">
            <el-button round size="mini" @click="editUserMsg" v-if="isPer('company:edit')" class="el-icon-edit el-button--primary" style="margin-top: 20px"> 编辑</el-button>
          </el-col>
        </el-row>
        <!--<div class="saveBtn">-->
          <!--<el-button v-if="!edit" round size="small" @click="cancelEdit" class="el-button&#45;&#45;primary">取消</el-button>-->
          <!--<el-button v-if="!edit" round size="small" @click="saveEdit" class="el-button&#45;&#45;primary">保存</el-button>-->
        <!--</div>-->
      </el-tab-pane>
      <el-tab-pane label="账户安全" class="userMsg" name="second">
        <el-row :gutter="24">
          <el-col class="headShow" :span="11">
            <img src="../../../assets/images/headImg.png" class="headImg" alt="">
            <el-button v-if="!edit" round size="mini" class="el-button--primary"> 重新上传</el-button>
          </el-col>
          <el-col :span="10">
            <ul>
              <!--<li><span>用户名：</span> <span>{{getUserBaseInfo.username}}</span></li>-->
              <li>
                <span>手机号：</span>
                <span>
                <el-input @blur="validMobile" v-model="mobilePhone"placeholder="请输入手机号"></el-input>
                </span>
                <span class="green el-icon-success" v-if="isRight"></span>
                <span class="isOk" v-if="isUsed">{{phoneTip}}</span>
              </li>
              <li class="verify-code">
                <span class="verifyTxt">验证码：</span>
                <span><el-input @blur="setVerify" class="verify" v-model="userValCode" placeholder="请输入验证码"></el-input></span>
                <span><img :src="verifyImg" class="verify-img" @click="getImgSecurity"></span>
                <span class="isVerify el-icon-success" v-show="isVerifyShow"></span>
              </li>
              <span class="isOk" v-show="verifyShow">{{verifyTxt}}</span>
              <li class="verify-code">
                <span class="verifyTxt">短信码：</span>
                <span><el-input class="verify" v-model="verifyValCode" placeholder="请输入验证码"></el-input></span>
                <span>
                  <el-button :disabled="disabled" @click="verifyCode" class="el-button--primary verify-phone">{{verifyCodeTxt}}</el-button>
                </span>
              </li>
              <!--<li>-->
                <!--<span>新密码：</span>-->
                <!--<span>-->
                  <!--<el-input type="password" v-model="newPassword" placeholder="请输入新密码"></el-input>-->
                <!--</span>-->
              <!--</li>-->
            </ul>
          </el-col>
          <el-col :span="2">
            <el-button round size="mini" @click="userEditUserMsg" class="el-icon-edit el-button--primary"> 编辑</el-button>
          </el-col>
        </el-row>
        <div class="saveBtn">
          <el-button v-if="!userEdit" round size="small" @click="userCancelEdit" class="el-button--primary">取消</el-button>
          <el-button v-if="!userEdit" round size="small" @click="userSaveEdit" class="el-button--primary">保存</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>

       <!--套餐升级继费对话框-->
      <!--<dialog-buy :showUpdate="packageBuyDialog" :showTxt="showTxt" :days="lastDays" @closeMd="closeMd"></dialog-buy>-->
      <!--// 账号过期提醒对话框-->
      <!--<package-timeout-tip></package-timeout-tip>-->
      <!--&lt;!&ndash;二维码弹窗&ndash;&gt;-->
      <!--&lt;!&ndash;<pay-code :showCode="payCodeShow"></pay-code>&ndash;&gt;-->
      <!--&lt;!&ndash;套餐购买&ndash;&gt;-->
      <!--<packagePurchase :dialogVisible="packagePurchase"></packagePurchase>-->
      <!--&lt;!&ndash;选择套餐&ndash;&gt;-->
      <!--<choosePackage @showMd="showMd"></choosePackage>-->
      <!--&lt;!&ndash;套餐过期弹窗&ndash;&gt;-->
      <!--<packageTimeout :tryoutPackage="tryoutPackage"></packageTimeout>-->

         <!--登录是套餐提醒弹窗-->

    <loginPackageTimeout :title="tryoutPackage" :text="packageText"></loginPackageTimeout>
  </div>
</template>

<script>
    import qs from 'qs'
    import dialogBuy from '../../packageUpdate/dialogBuy'
    import payCode from '../../packageUpdate/payCode'
    import packageTimeoutTip from '../../packageUpdate/dialogTimeoutPackage'
    import packageTimeout from '../../packageUpdate/packageTimeout'
    import packagePurchase from '../../packageUpdate/packagePurchase'
    import choosePackage from '../../packageUpdate/choosePackage'
    import loginPackageTimeout from '../../packageUpdate/loginPackageTimeout'

    export default {
      inject:['reload'],
     
    data () {
      return {
          lastDays: '',
          payCodeShow: false, // 支付二维码对话框
          packageBuyDialog: false, // 套餐升级继费对话框
          packageTimeoutTip: false, // 套餐过期提醒对话框
          showTimeoutTip: false,
          packageTimeoutModule: false,
          packagePurchase: false, // 套餐购买
          showTxt:false, // 提示剩余天数文本
          tryoutPackage:'', //试用套餐或正式套餐
          packageText:'',
          gender: '',
          mobilePhone: '',
          userValCode: '',
          verifyValCode: '',
          newPassword: '',
          activeName: 'first',
          radio: '1',
          userNameInput: '',
          edit: true,
          userEdit: true,
          mail: '',
          isUsed: false,
          isRight: false,
          phoneTip: '',
          getUserBaseInfo: {},
          loginUser: {},
          verifyImg: '',
          verifyCodeTxt: '获取验证码',
          disabled: false,
          verifyTxt: '',
          verifyShow: false,
          isVerifyShow: false,
          isEmail: false,
          emailTxt: '',
          okEmail: false,
          nameHint: false,
          mailboxHint: false,
          email: '',
          nickname: '',
          checkedName: '',
          nicknameHint: '昵称1~20个字符',
          checkedNameShow: false,
          isRefresh: false,
          shadeMask: false,
      }
    },
    created () {
      this.getUserInfo()
      this.getUserDetail()
      this.getImgSecurity()
      
    },
      components:{
          dialogBuy,
          packageTimeoutTip,
          payCode,
          packagePurchase,
          choosePackage,
          packageTimeout,
          loginPackageTimeout
      },
    mounted () {
        if (sessionStorage.getItem('isRefresh') == '' || sessionStorage.getItem('isRefresh') == null) {
            this.$router.go(0);
            sessionStorage.setItem('isRefresh', 'false');
        }
        
        this.getUserDetail()
        this.getImgSecurity()
        this.packageTimeout()
        this.packageLowFive()
        this.isTryOut()
    },

    methods: {

        // 套餐判断
        isTryOut(){
            this.tryout = sessionStorage.getItem('serviceType')
        },
        /*
        serviceType => 0.正式套餐  1.试用套餐
        maturityFlag =>{}套餐到期标识 1.false 不过期 2.true 过期
        tipsFlag =true =>{}表明套餐只剩下小于等于5天 false =>{} 表示套餐大于5天
        RemainingDays  -->当tipsFlag =true时这个值才有 套餐剩余最后5天的时候才会有

        sessionStorage.getItem('maturityFlag') //套餐到期标识
        sessionStorage.getItem('tipsFlag') //表明套餐只剩下小于等于5天
        sessionStorage.getItem('RemainingDays') // 套餐剩余最后5天的时候才会有
        */
         // 套餐到期标识
         packageTimeout(){
             //1.账号是否到期
             // if(sessionStorage.getItem('exceptionCode')=='10010040'){  //账号过期
             //     this.$store.dispatch('dialog',{isShow:true,name:'loginPackageTimeout'});
             //     this.tryoutPackage='您的账号有效期已到';
             // }
             if(sessionStorage.getItem('leftTime')&&parseInt(sessionStorage.getItem('leftTime'))<=5){
                 this.$store.dispatch('dialog',{isShow:true,name:'loginPackageTimeout'});
                 this.tryoutPackage=`您的账号还剩${sessionStorage.getItem('leftTime')}天到期`;
                 this.packageText=`可联系账号提供者进行延期`;
                 return
             }
             //2.套餐是否到期
             if(sessionStorage.getItem('serviceType') == '0'&&sessionStorage.getItem('maturityFlag') == 'true') { //正式套餐 过期
                 setTimeout(()=>{
                     this.$store.dispatch('dialog',{isShow:true,name:'loginPackageTimeout'});
                 },1000)
                 this.tryoutPackage=`您的正式套餐“${sessionStorage.getItem('serviceName ')}”已到期`;
                 this.packageText=`可联系账号提供者进行续费`;
                 this.shadeMask=true;
                 return
             }else if(sessionStorage.getItem('serviceType') == '0' && sessionStorage.getItem('tipsFlag') == 'true'){//正式套餐 不过期 只剩下小于等于5天
                 setTimeout(()=>{
                     this.$store.dispatch('dialog',{isShow:true,name:'loginPackageTimeout'});
                 },1000)
                 this.tryoutPackage=`您的正式套餐“${sessionStorage.getItem('serviceName')}”还有${sessionStorage.getItem('remainingDays')}天到期`;
                 this.packageText=`可联系账号提供者进行续费`;
                 return
             }else if(sessionStorage.getItem('serviceType') == '1' && sessionStorage.getItem('maturityFlag') == 'true'){//试用套餐 过期
                 setTimeout(()=>{
                     this.$store.dispatch('dialog',{isShow:true,name:'loginPackageTimeout'});
                 },1000)
                 console.log(11111)
                 this.tryoutPackage=`您的试用套餐“${sessionStorage.getItem('serviceName')}”已到期`;
                 this.packageText=`可联系三度进行延期或直接购买正式套餐`;
                 this.shadeMask=true;
                 return
             }else if(sessionStorage.getItem('serviceType') == '1' && sessionStorage.getItem('tipsFlag') == 'true'){//试用套餐 不过期 只剩下小于等于5天
                 setTimeout(()=>{
                     this.$store.dispatch('dialog',{isShow:true,name:'loginPackageTimeout'});
                 },1000)
                 console.log(sessionStorage.getItem('serviceName'))
                 this.tryoutPackage=`您的试用套餐“${sessionStorage.getItem('serviceName')}”还有${sessionStorage.getItem('remainingDays')}天到期`;
                 this.packageText=`可联系三度进行延期或直接购买正式套餐`;
                 return
             }
         },

        // 套餐只剩下小于等于5天
        packageLowFive(){
             this.lastDays = sessionStorage.getItem('remainingDays')
            if(sessionStorage.getItem('remainingDays') < 5) {
                this.packageBuyDialog = true
            }
        },
        closeMd(type){
             if(type=='update'){ //套餐升级
                 this.packageBuyDialog=false;
                 setTimeout(()=> {
                     this.choosePackage=true;
                 },500)
             }else if(type=='buy'){ //套餐购买
                 this.packageBuyDialog=false;
                 setTimeout(()=> {
                     this.packagePurchase=true;
                     this.$store.dispatch('goOnPackageAction',this)
                 },500)
             }

        },
        showMd(){
            this.choosePackage=false;
             setTimeout(()=> {
                 this.packagePurchase=true;
             },500)

        },
        // 保存个人设置
      saveEdit () {
        if (this.nickname.length > 0 && this.email.length > 0 && !this.nameHint && !this.mailboxHint && this.checkedName != '') {
          this.API.setEditUser({
            nick: this.nickname,
            sex: this.checkedName,
            email: this.email,
            userId: this.loginUser.id
          }).then((res) => {
            this.edit = true
          })
        }
        if (this.nickname.length === 0) {
          this.nameHint = true;
        }
        if (this.checkedName === '') {
          this.checkedNameShow = true;
        }
        if (this.email.length === 0) {
          this.mailboxHint = true;
        }
      },
      // 昵称验证
      nicknameMobile () {
        let txt = this.nickname.replace(/\s+/g, '');
        if (txt.length === 0 || txt.length > 20) {
          this.nameHint = true
        } else {
          this.nameHint = false
        }
      },
      checkedBoy () {
        this.$refs.woman.checked = false
        this.checkedName = 1
        this.checkedNameShow = false
      },
      checkedWoman () {
        this.$refs.boy.checked = false
        this.checkedName = 2
        this.checkedNameShow = false
      },
      // 验证邮箱
      emailMobile () {
        let reg = new RegExp('^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$')
        if (!this.edit) {
          if (!reg.test(this.email)) {
            this.mailboxHint = true
          } else {
            this.mailboxHint = false
          }
        } else {
          if (!reg.test(this.getUserBaseInfo.email)) {
            this.emailTxt = '请填写正确邮箱'
            this.isEmail = true
            this.okEmail = false
          } else if (reg.test(this.getUserBaseInfo.email)) {
            this.okEmail = true
            this.isEmail = false
          }
        }
      },
      // 验证图片验证码
      setVerify () {
        this.API.setCheckCode({
          sysCode: 'merchantManage',
          userId: this.loginUser.id,
          imgCode: this.userValCode.toUpperCase()
        }).then((res) => {
          if (res.message === '图片验证码输入有误!' || res.message === '验证码不能为空!') {
            this.verifyTxt = res.message
            this.verifyShow = true
            this.isVerifyShow = false
          }
          if (res.message === '验证成功!') {
            this.isVerifyShow = true
            this.verifyShow = false
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
      // 获取短信验证码
      verifyCode () {
        if (this.isRight && this.isVerifyShow) {
          this.API.phoneSecurityCode({
            phoneNumber: this.mobilePhone
            // phoneNumber: this.getUserBaseInfo.username
          }).then((res) => {
            if (res.message === '手机号不能为空') {
              this.isUsed = true
              this.isRight = false
              this.phoneTip = res.message
            } else {
              let i = 60
              let g = setInterval(() => {
                i--
                this.verifyCodeTxt = i + 's'
                this.disabled = true
                if (i < 0) {
                  clearInterval(g)
                  this.verifyCodeTxt = '获取验证码'
                  this.disabled = false
                }
              }, 1000)
            }
          })
        }
      },
      isPer (per) {
        let permiss = qs.parse(sessionStorage.getItem('loginUser'))
        return this.isMainPer(per, permiss)
      },
      selectGender (val) {
      },
      handleClick (tab, event) {
      },
      userMobileCheck () {
        let reg = /^1[0-9]{10}$/
        let phoneNumber = reg.test(this.mobilePhone)
        if (phoneNumber) {
          this.API.isPhone({
            phoneNumber: this.mobilePhone
          }).then((res) => {
            if (res.message === '此手机号已经被注册') {
              this.isUsed = true
              this.isRight = false
              this.phoneTip = '此手机号已经被注册'
            } else {
              this.isUsed = false
              this.isRight = true
            }
          })
        } else {
          this.isUsed = true
          this.isRight = false
          this.phoneTip = '请输入正确的手机号'
        }
      },
      validMobile () {
        this.userMobileCheck()
      },
      editUserMsg () {
        this.edit = false
      },
      userEditUserMsg () {
        this.userEdit = false
      },
      cancelEdit () {
        this.edit = true
        this.checkedNameShow = false
        this.nameHint = false
        this.mailboxHint = false
      },
      userCancelEdit () {
        this.userEdit = true
      },
      // 保存账户安全修改信息
      userSaveEdit () {
        this.API.setUpdatePhone({
          phoneCode: this.verifyValCode,
          imgCode: this.userValCode.toUpperCase(),
          sysCode: 'merchantManage',
          userId: this.loginUser.id,
          phone: this.mobilePhone
        }).then((res) => {
          this.userEdit = true
          this.activeName = 'first'
        })
      },
      // 保存个人设置
      // saveEdit () {
      //   if (this.okEmail) {
      //     this.API.setEditUser({
      //       nick: this.getUserBaseInfo.nick,
      //       sex: this.getUserBaseInfo.sex,
      //       email: this.getUserBaseInfo.email,
      //       userId: this.loginUser.id
      //     }).then((res) => {
      //       this.edit = true
      //     })
      //   }
        /* this.edit = true */
        // this.API.UserInfoEdit({
        //   email: this.getUserBaseInfo.email, // loginUser (string): Email ,
        //   id: this.getUserBaseInfo.id, // (integer, optional),
        //   nick: this.getUserBaseInfo.nick, // (string): 昵称 ,
        //   phone: this.getUserBaseInfo.phone, // (string, optional): 手机号 ,
        //   sex: this.getUserBaseInfo.sex, // (integer): 性别:1男,2女 ,
        //   userName: this.getUserBaseInfo.username // (string, optional): 用户名
        // }).then((res) => {
        //   this.edit = true
        //   localStorage.setItem('loginUser', qs.stringify(this.loginUser));
        //   // const loginUser = qs.parse(localStorage.getItem('loginUser'))
        // })
      // },
      getUserInfo () {
        this.loginUser = qs.parse(sessionStorage.getItem('loginUser'))
      },

      getUserDetail () {
        // this.API.userDetail({
        //   userId: this.loginUser.id
        // }).then((res) => {
        //   this.getUserBaseInfo = res.data
        //   console.log(this.getUserBaseInfo, 'user')
        // })
        this.API.getEditUser({
          userId: this.loginUser.id
        }).then((res) => {
          if (res.message === 'ok') {
            this.getUserBaseInfo = res.obj
            sessionStorage.setItem('userNames', res.obj.userName)
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .personalFrame {
    min-height: 800px;
    background: #fff;
    padding: 20px;
    box-sizing: padding-box;
  }

  .shade-box{
    position: fixed;
    top: 0;
    left: 0;
    z-index: 99;
    width: 100%;
    height: 100%;
    background-color: black;
    opacity: 0.2;
  }
.shade-box-mask{
  position: fixed;
  top: 0;
  left: 0;
  z-index: 2000;
  width: 100%;
  height: 100%;
  background-color: white;
  opacity: 0.4;
}
  .compile-box{
    position: fixed;
    top: 50%;
    left: 50%;
    z-index: 100;
    transform: translate(-50%, -50%);
    width: 420px;
    height: 348px;
    background-color: #fff;
    .compile-header{
      width: 100%;
      height: 50px;
      padding: 0 19px 0 19px;
      border-bottom: 1px solid #ddd;
      box-sizing: border-box;
      color: #666666;
      font-size: 14px;
      line-height: 50px;
      display: flex;
      justify-content: space-between;
      .delete{
        width: 17px;
        height: 16px;
        margin-top: 17px;
        background: url('../../../assets/images/icons/delete4.png') 0 center no-repeat;
      }
    }
    .compile-content{
      position: relative;
      font-size: 14px;
      color: #666;
      height: 173px;
      padding: 0 23px 0 23px;
      margin-top: 30px;
      .btn{
        width: 100%;
        height: 40px;
        margin-top: 44px;
        display: flex;
        font-size: 16px;
        color: #fff;
        font-weight: bold;
        justify-content: space-around;
        .yes{
          width: 130px;
          height: 40px;
          border-radius: 20px;
          text-align: center;
          line-height: 40px;
          background-color: #ff6419;
        }
        .no{
          width: 130px;
          height: 40px;
          border-radius: 20px;
          text-align: center;
          line-height: 40px;
          background-color: #454545;
        }
      }
      .nameHint{
        position: absolute;
        top: 40px;
        left: 80px;
        width: 100%;
        box-sizing: border-box;
        color: #ff3030
      }
      .checkedName{
        position: absolute;
        top: 80px;
        left: 80px;
        width: 100%;
        box-sizing: border-box;
        color: #ff3030
      }
      .mailboxHint{
        position: absolute;
        top: 150px;
        left: 80px;
        width: 100%;
        box-sizing: border-box;
        color: #ff3030
      }
      .nickname{
        line-height: 40px;
        margin-bottom: 24px;
        height: 40px;
        display: flex;
        justify-content: space-between;
        input{
          width: 321px;
          height: 100%;
          font-size: 14px;
          color: #666;
          box-sizing: border-box;
          border: 1px solid #ddd;
          padding: 0 12px 0 12px;
          border-radius: 4px;
        }
      }
      .gender{
        width: 100%;
        height: 15px;
        line-height: 15px;
        margin-top: 15px;
        margin-bottom: 30px;
        display: flex;
        justify-content: flex-start;
        .boy{
          height: 15px;
          margin: 0 10px 0 15px;
        }
        .woman{
          height: 15px;
          margin: 0 10px 0 40px;
        }
        input{
          width: 15px;
          height: 15px;
        }
      }
    }
  }

  .isOk {
    color: red;
    padding-left: 58px;
    padding-top: 10px;
    display: block;
  }

  .green {
    color: #5daf34;
  }

  .isVerify{
    color: #5daf34;
    margin: 12px 0 0 5px;
  }

  .userMsg {
    .el-input {
      width: 250px;
      height: 36px;
    }
    .verifyTxt{
      line-height: 36px;
    }
    .verify-code{
      display: flex;
      justify-content: flex-start;
    }
    .verify{
      margin-left: 4px;
      width: 130px;
    }
    .verify-phone{
      margin-left: 10px;
      width: 110px;
      height: 39px;
    }
    .verify-img{
      margin-left: 10px;
      width: 110px;
      height: 39px;
    }
    .headShow {
      position: relative;
      border-right: solid 1px #e3e3e3;
      .el-button {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
      }
    }
    .headImg {
      width: 180px;
      margin: 40px;
      border: none;
      background: #fff;
    }
    .el-col:nth-of-type(3) {
      margin-top: 16px;
    }
    ul {
      padding-bottom: 35px;
      padding-left: 40px;
      font: {
        color: #666;
        size: 14px
      }
      li {
        padding-top: 20px;
      }
    }
  }

  .saveBtn {
    border-top: solid 1px #e3e3e3;
    text-align: center;
    padding: 40px 0 20px 0;
    .el-button {
      border: none;
      height: 40px;
      width: 130px;
      font-size: 14px;
      &:nth-of-type(1) {
        background: #454545;
      }
    }
  }
</style>
<style lang="scss">
  .personalFrame{
    .el-tabs__content{
      border-bottom: 1px solid #ddd;
    }
    .el-tabs__header{
      margin: 0!important;
    }
  }

</style>
