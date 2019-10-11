<template>
  <div class="pos">
    <ul>
      <li class="left">
        <el-breadcrumb class="nav-top" separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">当前位置：</el-breadcrumb-item>
          <el-breadcrumb-item>活动管理</el-breadcrumb-item>
          <el-breadcrumb-item>活动列表</el-breadcrumb-item>
          <el-breadcrumb-item>活动详情</el-breadcrumb-item>
        </el-breadcrumb>
      </li>
      <li class="right"><b><i class="el-icon-back"></i> 返回</b></li>
    </ul>
  </div>
</template>
<style lang="scss" scoped>
  @import "../../../assets/css/mixin.scss";
  .pos
  {
    margin-bottom: 12px;
    min-height: 80px;
    background: #fff;
    border-radius: 6px;
    box-shadow: 0 0 8px rgba(0,0,0,0.1);
    .nav-top {
      margin-top: 35px;
      font-weight: normal;
    }

    ul{
      display: flex;
      -webkit-justify-content: space-between;
      justify-content: space-between;
      font-size: 13px;
      color:#626262;
      li {
        padding:0 20px;
        line-height: 80px;
        font-weight: bold;
        b{
          display: inline-block;
          background: #ff6419;
          height: 40px;
          line-height: 40px;
          padding:0 20px;
          color:#fff;
          border-radius: 20px;
        }
      }
    }
  }
</style>
