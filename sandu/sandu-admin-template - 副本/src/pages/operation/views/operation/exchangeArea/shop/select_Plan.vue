<template>
  <div>
    <div class="planLibrary tabClass">
      <el-tabs v-model="activeName" @tab-click="changeTabs">
        <el-tab-pane label="智能方案" name="first">
          <intelligent-plan v-if="!isShopPlanType" @getShopPlanType="getShopPlanType" :isRadio="true"></intelligent-plan>
          <shop-fullhouse-plan v-else @getShopPlanType="getShopPlanType" :isRadio="true"></shop-fullhouse-plan>
        </el-tab-pane>
        <el-tab-pane label="普通方案" name="second">
          <common-design-plan :isRadio="true"></common-design-plan>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import qs from "qs";
import intelligentPlan from "./oneKeyPlanList";
import CommonDesignPlan from "./common/CommonDesignPlan";
import shopFullhousePlan from "./shopPlancomponents/fullhouseShopPlan";
export default {
  components: {
    intelligentPlan,
    CommonDesignPlan,
    shopFullhousePlan
  },
  data() {
    return {
      isShopPlanType: false,
      shelfDialogVisible: false,
      shelf: {
        shelfStatus: []
      },
      activeName: "first"
    };
  },
  methods: {
    getShopPlanType(isShow) {
      this.isShopPlanType = isShow;
    },
    changeTabs(val) {
        sessionStorage.setItem("getTabName", val.label);
    //   this.activeName = val.label;
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../../../assets/css/mixin";

.planLibrary {
  box-sizing: padding-box;
  background: #fff;
  border-radius: $borderRadius;
}
.tabClass {
  margin-bottom: 20px;
}
.el-table th.hedStyle {
  height: 5px !important;
}
.el-tabs__item {
  height: 50px;
  line-height: 50px;
}
.el-tabs__nav {
  margin-left: 30px;
}
</style>
