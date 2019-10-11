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
.page{
    width: 100%;
    height: 100%;
}
.formRows .el-form-item__content{
    text-align: left;
}
.formRows [class*=el-col-].line {
    text-align: center;
}
.formRows .el-cascader{
    width: 100%;
}
.lines{
    .el-select .el-input {
        width: 60px;
    }
    .input-with-select .el-input-group__prepend {
        background-color: #fff;
    }
}
::-webkit-scrollbar{
    display: none;
}
.el-col,
.el-form-item{
    position: relative;
}
.magin30 .el-form-item--small.el-form-item{
    margin-bottom: 25px;
}
.el-dropdown-menu__item.is-disabled{
    color: #3399ff;
    background-color: #ecf5ff;
}
.Loadings{
    width: 500px;
    height: 350px;
    left: 50%;
    top: 50%;
    margin-left: -175px;
    margin-top: -250px;
}
.disabledTxt{ 
    .el-select{
        display: inline-block !important;
        width: auto !important;
    }
    .el-input.is-disabled{ 
        .el-input__inner{
            background-color: rgba(0,0,0,0);
            border:none;
            color: #494949;
            cursor: text;
            padding: 0;
        }
        .el-input__suffix{
            display: none;
        }
    }
    .el-textarea.is-disabled{
        .el-textarea__inner{
            background-color: rgba(0,0,0,0);
            border:none;
            color: #494949;
            cursor: text;
            padding: 0;
        }
    } 
}
.formRows .lines .el-input-number--small{
    width: 100% !important;
}

</style>
