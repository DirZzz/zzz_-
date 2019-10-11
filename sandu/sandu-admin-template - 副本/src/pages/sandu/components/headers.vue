<template>
    <div class="headers clear">
        <div class="leftbox">
            <span class="operLogo"></span>
            <p class="opertext">三度管理后台</p>
            <el-dropdown placement="top-start" trigger="click">
                <span class="el-dropdown-link">
                    <span class="changePage"></span>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="(item,index) in platformList" @click.native="switchPlatform(item)" :key="index" :disabled="disabled(item)">{{item.platformName}}</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <div class="dropdown">
            <el-dropdown :hide-on-click="false">
                <div class="rightbox">
                    <p class="rightname">{{mobile}}</p>
                    <img src="../assets/images/icon/user.png" class="rightimg">
                </div>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                        <div class="exitbtn" @click="loginout">
                            <span class="exiticon"></span>
                            <span class="text">退出</span>
                        </div>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
    import qs from 'qs'
    import login from '@s/minixs/login'
    export default {
        name: "headers",
        mixins:[login],
        computed:{
            mobile(){
                return sessionStorage.getItem('mobile');
            },
            platformList(){
                return qs.parse(sessionStorage.getItem('platformList'))
            }
        },
        methods: {
            disabled(item){
                return item.platformCode==sessionStorage.getItem('platformCode');
            },
            loginout() {
                let loginUser = sessionStorage.getItem("loginUser");
                let userMsg = qs.parse(loginUser);
                this.API.loginOut({
                    Authorization: userMsg.token
                }).then(res => {
                    sessionStorage.setItem("loginUser", "");
                    sessionStorage.setItem("platformCode", "");
                    location.href="index.html"
                });
            },
            switchPlatform(item){
                if(item.platformCode=='merchantManage'){
                    sessionStorage.setItem('platformCode','merchantManage');
                    this.login('merchantManage');
                }else if(item.platformCode=='sanduManagerNew'){
                    sessionStorage.setItem('platformCode','sanduManagerNew');
                    this.login('sanduManagerNew');
                }else if(item.platformCode=='operationPlatform'){
                    sessionStorage.setItem('platformCode','operationPlatform');
                    this.login('operationPlatform');
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
  .headers{
      height:70px;
      line-height: 70px;
      text-align: center;
      background-color: $theme-color;
      color: #fff;
      .dropdown{
          float: right;
      }
      .leftbox{
          float: left;
          width: 180px;
          height: 70px;
          .operLogo{
              width: 23px;
              height: 25px;
              display: inline-block;
              background: url('../assets/images/icon/LOGO.png')  no-repeat;
              background-size: 23px 25px;
              vertical-align: middle;
          }
          .changePage{
              width: 16px;
              height: 14px;
              display: inline-block;
              background: url('../assets/images/icon/changePage.png')  no-repeat;
              background-size: 16px 14px;
              vertical-align: middle;
              cursor: pointer;
          }
          .opertext{
             font-size: 16px;
             color: white;  
             display: inline-block;
             margin: 0 10px;
          }

      }
      .rightbox{
          float: right;
          height: 70px;
          margin-right: 25px;
          .rightname{
              font-size:14px;
              color: white;
              display: inline-block;
              vertical-align: middle;
              margin-right: 10px;
          }
          .rightimg{
              width: 28px;
              height: 28px;
              vertical-align: middle;
              cursor: pointer;
          }
      }
  }
</style>