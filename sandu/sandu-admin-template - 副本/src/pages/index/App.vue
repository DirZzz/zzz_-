<template>
  <div id="app" v-cloak>
     <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>

<script>
  import login from './views/login/login';
  export default {
    name: 'app',
      provide(){
        return {
            reload:this.reload
        }
      },
      data (){
        return {
            isRouterAlive :true
        }
      },
      methods:{
        reload(){
            this.isRouterAlive = false;
            this.$nextTick(function () {
                this.isRouterAlive = true
            })
        }
      },
    components: {
      login
    }
};
</script>

<style>
  .el-loading-spinner i {
    color: #FF6419;
    font-size: 42px;
  }
  [v-cloak] {
    display: none;
  }
#app {
  min-width: 1180px;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;

  overflow: hidden;
  overflow-y: scroll;
  background: #e4e4e4;
}
.noReds input{
    border-color: #ddd !important;
}
.right-aside{
  position: absolute;
  top:10px;
  left:0;
  min-width: 1000px;
  right: 10px;
}
.left-aside{
  position: fixed;
  top:0;
  left: 0;
  height: 100%;
  bottom:0;
  background: #454545;
  width: 238px;
  /*overflow: hidden;*/
  overflow-y: scroll;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity .4s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
/*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
.left-aside::-webkit-scrollbar{
  width: 10px;
  height: 16px;
  background-color: #f5f5f5;
}
/*定义滚动条的轨道，内阴影及圆角*/
.left-aside::-webkit-scrollbar-track{
  -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
  border-radius: 10px;
  background-color: #f5f5f5;
}
/*定义滑块，内阴影及圆角*/
.left-aside::-webkit-scrollbar-thumb{
  /*width: 10px;*/
  height: 20px;
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
  background-color: rgba(0,0,0,.4);
}
.left-aside::-webkit-scrollbar-thumb:hover{
  background-color: rgba(0,0,0,.6);
  cursor: pointer;
}
/*定义意见反馈查看大图时切换箭头样式*/
.showImgRows .el-carousel__arrow--right{
  width: 80px;
	height: 102px;
	background-color: #363636;
	border-radius: 20px 0px 0px 20px;
  font-size: 30px;
  right: 0;
}
.showImgRows .el-carousel__arrow--left{
  width: 80px;
	height: 102px;
	background-color: #363636;
	border-radius: 0 20px 20px 0;
  font-size: 30px;
  left: 0;
}
.el-dropdown-menu__item.is-disabled{
    color: #ff6419!important;
    background-color: #FFF0E8;
}
</style>
