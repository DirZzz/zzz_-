<template>
  <div class="tooltip">
    <el-tooltip effect="light" placement="bottom">
      <div slot="content" class="content">
        <p v-for="item in messages" :key="item">
          {{item}}
        </p>
      </div>
      <span class="el_icon_question" style="width:18px;height:18px;line-height:18px;display:inline-block;color:#999;text-align:center;padding:0 3px 0 2px;font-size:15px;border-radius:50%;border: 1px solid #999;box-sizing:border-box;margin-left:10px;">?</span>
    </el-tooltip>
  </div>
</template>

<script>
  export default {
    props:['messages']
  };
</script>
<style lang="scss" scoped>
    .el-table__header-wrapper .tooltip{
      padding: 0px 0px;
      text-overflow: initial;
    }
    .content > p:first-child{
      font-weight: bold;
      margin-bottom: 14px;
    }
</style>

