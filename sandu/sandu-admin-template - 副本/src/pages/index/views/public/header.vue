<template>
  <div>
    <div class="main-header">
      <div class="sanduLogo">
        <b><img src="../../assets/LOGO.png" alt="" style="width: 29px; height: 32px"></b>
        <span>商家管理后台</span>
        <!--只有三度云想家才有切换功能-->
        <el-dropdown placement="top-start" trigger="click">
                <span class="el-dropdown-link">
                    <span class="changePage"></span>
                </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-for="(item,index) in platformList" @click.native="switchPlatform(item)" :key="index" :disabled="disabled(item)">{{item.platformName}}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </div>
      <p class="enterpriseLogo">
        <img :src="logoPic" alt="">
        <span>{{companyName}}
        </span>
      </p>
      <ul class="loginout">
        <li style="display: none">
          <span>当前平台</span>
          <el-select placeholder="请选择平台" @change="getPlatform(platName)" style="width:120px" size="mini" v-model="platName" clearable>
            <el-option v-for="(plat,index) in platfromlist" :key="index" :label="plat.name" :value="plat.value">{{plat.name}}</el-option>
          </el-select>
        </li>
         <li class="capital" v-if="isPer('check:myDubi:view','mySY') || isPer('check:myIncome:view','myDB')" @click="goCapital">
           <img  src="../../assets/images/icons/icon-zichan.png" alt=""> 我的资产
        </li>
        <li class="packageBuy" @click="gotoBuy">
          <img src="../../assets/images/icons/selectpackpage.png" alt=""> 套餐购买
        </li>
        <li class="feedback"  @click="showFeedback">
          <i>用户反馈</i>
          <span class="hdian" v-if="idR"></span>
        </li>
        <li>&nbsp; | &nbsp;</li>
        <li>{{userMobile ? userMobile : userName==true ? userName : loginName}}</li>
        <li>
          <el-dropdown :hide-on-click="false">
            <span class="el-dropdown-link">
              <img src="../../assets/images/icons/profle1.png" style="vertical-align: -8px;" width="28" alt="">
            </span>
            <el-dropdown-menu slot="dropdown">
              <div class="exitbtn" >
                <span class="setup"></span>
                <span class="text" @click="toPersonalSet">账号设置</span>
              </div>

              <!-- add by huangsongbo 2018.12.11 ->start -->
              <div class="exitbtn" >
                <span class="yinhangka"></span>
                <span class="text" @click="myBankCard">银行卡</span>
              </div>
              <!-- add by huangsongbo 2018.12.11 ->start -->

              <div class="exitbtn" v-show="isShowCompany"  @click="changecompany">
                <span class="qiye"></span>
                <span class="text">切换企业</span>
              </div>
              <div class="exitbtn" @click="loginout">
                <span class="exiticon"></span>
                <span class="text">退出</span>
              </div>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
      </ul>
    </div>
    <el-dialog :visible.sync="feedbackVisible" top="4vh" class="feedbackBottom">
      <iframe width="100%" frameborder="0" height="800" src="https://sanduspace.mikecrm.com/8kHFNCk">
      </iframe>
      <div class="bottomHide"></div>
    </el-dialog>
    <!--更新信息弹框-->
    <el-dialog :visible.sync="updatedia" :close-on-click-modal="false" :show-close="true" width="540px" top="300px" title="提示" class="updatedia">
      <div class="txtbox">
        你的信息已更新，请重新登录。
        <span class="Gobtn" @click="Gologin">去登录</span>
      </div>
    </el-dialog>

    <!--切换企业弹框-->
    <el-dialog :visible.sync="chosecompany" width="544px" top="200px" title="选择企业" class="chosecompany">
      <div class="wrap ">
        <div class="clear" style="width:485px;margin:0 auto;">
            <div  v-for="(item,index) in company_list" :key="index" :class="{'box':true,active:item.id==current}" @click="Act(index,item.id)">
          <img class="img" :src="item.logoPath">
          <p  class="text">{{item.name}}</p>
        </div>

        </div>
      </div>
        <div class="btnbox" >
          <span class="okbtn" @click="Swichcompany" >
             确定
          </span>
          </div>
    </el-dialog>

  </div>
</template>
<script>
import qs from "qs";
import myVue from "@/filters/bus";
import commonMethods from '@/filters/minixs';
export default {
   mixins: [commonMethods],
  name: "my-header",
  data() {
    return {
      isShowCompany:false,
      company_list:[],
      current:0,
      chosecompany: false,
      updatedia: false,
      feedbackVisible: false,
      companyName: "",
      platName: "",
      nickName: "",
        idR:false,
      // userName:'',
      loginName: "",
      logoPic: "",
        userMobile: sessionStorage.getItem('mobile'),
      platfromlist: [
        { name: "商家后台", value: "one", path: "/index" },
        { name: "小程序", value: "two", path: "/order" }
      ]
    };
  },
  computed: {
    userName() {
      return sessionStorage.getItem("userNames");
    },
    CompanyId(){
      return sessionStorage.getItem("companyID")
    },
      platformList(){
          return qs.parse(sessionStorage.getItem('platformList'))
      }
    // userMobile(){
    //     return sessionStorage.getItem('mobile')
    // }
  },
  watch:{
      'userMobile':function () {


          return sessionStorage.getItem('mobile')
      }
  },
    created(){
        this.CompanyList();
    },
  mounted() {
    this.getCompanyLogo();
    this.getUserMsg();
    let platform = sessionStorage.getItem("platform");
    this.platName = platform;
  },
  methods: {
     isPer(per) {
          let permiss = qs.parse(sessionStorage.getItem('loginUser'))
          return this.isMainPer(per, permiss)
     },
      disabled(item){
          return item.platformCode==sessionStorage.getItem('platformCode');
      },
      switchPlatform(item){
          if(item.platformCode=='merchantManage'){
              sessionStorage.setItem('platformCode','merchantManage');
              this.login('','merchantManage');
          }else if(item.platformCode=='sanduManagerNew'){
              sessionStorage.setItem('platformCode','sanduManagerNew');
              this.login('','sanduManagerNew');
          }else if(item.platformCode=='operationPlatform'){
              sessionStorage.setItem('platformCode','operationPlatform');
              this.login('','operationPlatform');
          }
      },
     goCapital(){
        this.$router.push('/capital')
     },
      toPersonalSet(){
          this.$router.push('/personal/accountSet')
      },

    // add by huangsongbo 2018.12.10 跳转到银行卡页面 ->start
    myBankCard(){
      this.$router.push('/capital/myBankCard')
    },
    // add by huangsongbo 2018.12.10 跳转到银行卡页面 ->end

    Gologin(){
      this.$router.push("/");
    },
    Swichcompany(){
      this.login(this.current);
    },
    CompanyList(){
      let form = new FormData();
      form.append('account',sessionStorage.getItem('mobile'));
      form.append('password',sessionStorage.getItem('pass'));
      this.API.getcompanylist(form).then(res => {
        console.log(res.datalist);
        this.company_list = res.datalist;
        if(this.company_list){
            if(this.company_list.length>1){
                this.isShowCompany = true;
            }
            for(let i=0;i<this.company_list.length;i++){
                this.company_list[i].logoPath = process.env.sourceBaseUrl+ this.company_list[i].logoPath;
            }
        }
      })
      this.current = this.CompanyId;

    },
    Act(index,id){
       this.current = id;
       console.log(this.current,this.CompanyId);
    },
    changecompany(){
      this.chosecompany = true;
      this.CompanyList();
    },
    gotoBuy() {
      this.$router.push("/packagelist");
    },
    getPlatform(val) {
      sessionStorage.setItem("platform", val); // 平台选择
      // this.$router.go(0) // 刷新页面
      // this.$router.push('/index')
      if (val == "two") {
        this.$router.push("/order");
      }
      if (val == "one") {
        this.$router.push("/index");
      }
    },
    showFeedback() {
      this.$router.push("/feedback");
    },
    getUserMsg() {
      let usermsg = qs.parse(sessionStorage.getItem("loginUser"));
      this.nickName = usermsg.nickName;
      console.log(usermsg);
      this.loginName = usermsg.loginName;
      // this.userName = sessionStorage.getItem('userNames');
      //this.userMobile = usermsg.mobile;
    },
    getCompanyLogo() {
      this.API.getCompanyDetail({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        if (res) {
          this.companyName = res.data.name;
          this.logoPic = res.data.logo;
        }
      });
    },
    loginout() {
      let loginUser = sessionStorage.getItem("loginUser");
      let userMsg = qs.parse(loginUser);
      this.API.loginOut({
        Authorization: userMsg.token
      }).then(res => {
        this.$router.replace("/");
        // this.$router.go(0)
        sessionStorage.setItem("loginUser", "");
        sessionStorage.setItem("platformCode", "");
        loginUser = "";
        // setTimeout(()=>{
        //     window.location.reload();
        // },500)
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.chosecompany {
  .wrap {
    max-height: 400px;
    overflow: scroll;
    overflow-x:hidden;
    .box {
      float: left;
      position: relative;
      width: 235px;
      height: 90px;
      border: 1px solid #cccccc;
      margin-top: 10px;
      display: flex;
      align-items: center;
      &:first-child,&:nth-child(2) {
        margin-top: 0;
      }

      &:nth-child(2n) {
        margin-left: 10px;
      }
      .img{
        flex-basis: 50px;
        height: 50px;
        background: #ccc;
        margin-left: 20px;
      }
      .text {
        margin-left: 12px;
        flex: 1;
        font-size: 14px;
        line-height: 14px;
         display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
      }
    }
    .active{
      border: 1px solid #ff6419;
      ::after{
        content: '';
        width: 24px;
        height: 24px;
        display: inline-block;
        position: absolute;
        bottom: -1px;
        right: -1px;
        background: url('../../assets/images/icons/ic_choose.png') no-repeat;
      }
    }
  }
  .btnbox{
    text-align: center;
    margin-top: 30px;
    .okbtn{
      cursor: pointer;
      width: 200px;
      height: 36px;
      color: white;
      background: #ff6419;
      display: inline-block;
      text-align: center;
      line-height: 36px;
      border-radius: 18px;
      user-select: none;
    }
  }
}

.updatedia {
  .txtbox {
    text-align: center;
    .Gobtn {
      width: 90px;
      height: 30px;
      line-height: 30px;
      color: #ffffff;
      border-radius: 15px;
      background: #ff6419;
      display: inline-block;
      text-align: center;
      user-select: none;
      cursor: pointer;
    }
  }
}
.feedbackBottom {
  position: relative;
  .bottomHide {
    background: #fff;
    position: absolute;
    bottom: 27px;
    height: 60px;
    width: 96%;
  }
}
.exitbtn {
  width: 120px;
  height: 40px;
  line-height: 40px;
  cursor: pointer;
  &:hover {
    color: #ff6419;
    .exiticon,
    .qiye,
    .yinhangka,
    .setup {
      margin-left: 18px;
      width: 16px;
      height: 16px;
      display: inline-block;
      vertical-align: text-bottom;
    }
    .exiticon {
      background: url("../../assets/images/icons/tuichu2.png") no-repeat;
    }
    .qiye {
      background: url("../../assets/images/icons/qiye2.png") no-repeat;
    }
    .setup {
      background: url("../../assets/images/icons/setup2.png") no-repeat;
    }
    .yinhangka {
      background: url("../../assets/images/icons/yinhangka01.png") no-repeat;
    }
  }
  .text {
    margin-left: 8px;
  }
  .exiticon,
  .qiye,
  .yinhangka,
  .setup {
    margin-left: 18px;
    width: 16px;
    height: 16px;
    display: inline-block;
    vertical-align: text-bottom;
  }
  .exiticon {
    background: url("../../assets/images/icons/tuichu.png") no-repeat;
  }
  .qiye {
    background: url("../../assets/images/icons/qiye.png") no-repeat;
  }
  .setup {
    background: url("../../assets/images/icons/setup.png") no-repeat;
  }
  .yinhangka {
    background: url("../../assets/images/icons/yinhangka02.png") no-repeat;
  }
}
.feedback {
  position: relative;
  &:hover {
    color: #ff6419;
  }
  span.hdian{
    position: absolute;
    border-radius: 50%;
    width: 7px;
    height: 7px;
    background: #f00;
    top:18px;
    right: -10px;
  }
  i {
    display: inline-block;
    background: url("../../assets/images/icons/feedback.png") no-repeat;
    height: 26px;
    line-height: 26px;
    padding-left: 36px;
    font-style: normal;
    &:hover {
      background: url("../../assets/images/icons/feedback2.png")
        no-repeat;
      cursor: pointer;
    }
  }
}
.capital{
  cursor: pointer;
  font-size: 15px;
  margin-right: 25px;
  &:hover {
    color: #ff6419;
  }
  img {
    font-size: 18px;
    font-weight: bold;
    color: #ff6419;
    vertical-align: -4px;
  }
}
.packageBuy {
  cursor: pointer;
  font-size: 15px;
  margin-right: 25px;
  &:hover {
    color: #ff6419;
  }
  img {
    font-size: 18px;
    font-weight: bold;
    color: #ff6419;
    vertical-align: -8px;
  }
}
.main-header {
  position: fixed;
  z-index: 11;
  top: 0;
  left: 0;
  right: 1%;
  padding: 0;
  height: 60px;
  line-height: 60px;
  background: #fff;
  text-align: left;
  &:after {
    content: "";
    width: 46px;
    position: absolute;
    right: -30px;
    background: #fff;
    height: 60px;
    z-index: -2;
  }
}
.main-header2 {
  position: fixed;
  z-index: 4;
  top: 0;
  left: 0;
  right: 0;
  border: solid red 1px;
  padding: 0;
  height: 60px;
  line-height: 60px;
  background: #fff;
  text-align: left;
}
.enterpriseLogo {
  height: 60px;
  margin-left: 260px;
  text-align: center;
  display: inline-block;
  span {
    display: inline-block;
    margin-right: 15px;
    vertical-align: top;
    font-size: 16px;
  }
  img {
    height: 50px;
    margin-top: 5px;
  }
}
.sanduLogo {
  background: #ff6419;
  position: absolute;
  left: 0;
  height: 60px;
  text-align: center;
  width: 238px;
  top: 0;
  right: 20px;
  font-size: 22px;
  color: #fff;
.changePage{
          width: 16px;
          height: 14px;
          display: inline-block;
          background: url('../../assets/images/icons/changepage.png')  no-repeat;
          background-size: 16px 14px;
          vertical-align: middle;
          cursor: pointer;
      }
  b {
    display: inline-block;
    vertical-align: -10px;
  }
  span {
    display: inline-block;
    vertical-align: middle;
  }
  img {
    width: 30px;
    margin-right: 10px;
  }
}

.loginout {
  position: absolute;
  right: 20px;
  top: 0;
  color: #666;
  li {
    display: inline-block;
  }
}
</style>
