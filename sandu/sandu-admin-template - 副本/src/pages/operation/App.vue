<template>
  <div id="app">
      <div class="warper">
          <headers></headers>
          <div class="content">
              <leftSide></leftSide>
              <rightSide></rightSide>
          </div>
      </div>
  </div>
</template>

<script>
  import leftSide from './components/leftSide'
  import rightSide from './components/rightSide'
  import headers from './components/headers'
   export default {
       components:{
           leftSide,
           rightSide,
           headers
       }
   }
</script>

<style lang="scss">
@import './assets/css/common';
#app {

    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    width: 100%;
    height: 100%;
    background: #f0f2f5;
}
.warperBox{
    width: 100%;
    height: 100%;
}
.warper{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
}
.content{
  width: 100%;
  height: 100%;
  flex: 1;
  display: flex;
}
.selItems .el-input__inner{
    height: 32px;
    line-height: 32px;
}
.selItems .el-input__icon{
    line-height: 32px;
}
.page{
    width: 100%;
    height: 100%;
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
.formRows .el-form-item .el-form-item__label:before {
    content: "*";
    color: #f56c6c;
    margin-right: 4px;
}

#prizeManagement .el-input-number--mini{
    //width: 100px;
    position: relative;
    .el-input__inner{
        padding: 0;
    }
}
#prizeManagement .el-input-number--mini .el-input-number__decrease,
#prizeManagement .el-input-number--mini .el-input-number__increase{
    display: none;
}
.formBody .bfb .el-input-number--mini::before{
    content: '%';
    width: 20px;
    text-align: center;
    position: absolute;
    top: 0;
    font-size: 13px;
    color: #494949;
    right: -20px;
}
#prizeManagement .el-form-item--mini.el-form-item{
    margin-bottom: 10px;
}
#prizeManagement .el-upload{
    width: 68px;
    height: 68px;
    line-height: 68px;
    img{
        width: 68px;
        height: 68px;
        border-radius: 6px;
    }
}
.el-dropdown-menu__item.is-disabled{
    color: #3399ff;
    background-color: #ecf5ff;
}

</style>
