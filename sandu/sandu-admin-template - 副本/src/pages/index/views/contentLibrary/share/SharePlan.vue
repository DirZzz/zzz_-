<template>
  <div>
    <div class="planLibrary tabClass">
      <el-tabs v-model="activeName">
        <el-tab-pane label="智能方案" name="first">
          <share-onekey-design-plan v-if="isFullHouseSharePlan" @GetisFullHouseSharePlan="GetisFullHouseSharePlan"></share-onekey-design-plan>
          <fullhouse-share-plan v-else   @GetisFullHouseSharePlan="GetisFullHouseSharePlan"></fullhouse-share-plan>
        </el-tab-pane>
        <el-tab-pane label="普通方案" name="second">
          <share-common-design-plan></share-common-design-plan>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
  import ShareCommonDesignPlan from './common/ShareCommonDesignPlan.vue'
  import ContainsNotOpenProduct from '../../base/plan/ContainsNotOpenProduct.vue'
  import ShareOnekeyDesignPlan from './onekey/ShareOnekeyDesignPlan.vue'
  import fullhouseSharePlan from './onekey/fullhouseSharePlan.vue'
  export default {
    components: {
      ShareCommonDesignPlan,
      ContainsNotOpenProduct,
      ShareOnekeyDesignPlan,
      fullhouseSharePlan
    },
    data () {
      return {
        activeName: 'first',
        isFullHouseSharePlan:true
      }
    },
    methods: {
      GetisFullHouseSharePlan(isShow){
        console.log(isShow);
        this.isFullHouseSharePlan = isShow;
      }
    }
  }
</script>
<style lang="scss" scoped>


  .planLibrary {
    box-sizing: padding-box;
    background: #fff;
  }

  .tabClass {
    // padding: 20px;
    margin-bottom: 20px;
  }
</style>
<style>

  .el-tabs__nav {
    margin-left: 30px;
  }

  .el-tabs__item {
    height: 50px;
    line-height: 50px;
  }
</style>
