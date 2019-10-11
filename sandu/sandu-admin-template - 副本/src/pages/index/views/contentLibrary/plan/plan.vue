<template>
    <div>
        <div class="planLibrary tabClass">
            <el-tabs v-model="activeName">
                <el-tab-pane label="智能方案" name="first">
                    <one-key-plan v-if="isFullHousePlan" @GetisFullHousePlan="GetisFullHousePlan"></one-key-plan>
                    <full-house-plan v-else @GetisFullHousePlan="GetisFullHousePlan"></full-house-plan>
                </el-tab-pane>
                <el-tab-pane label="普通方案" name="second">
                <common-design-plan ref="child"></common-design-plan>
            </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>
<script>
import CommonDesignPlan from "./common/CommonDesignPlan.vue";
import ContainsNotOpenProduct from "../../base/plan/ContainsNotOpenProduct.vue";
import qs from "qs";
import OneKeyPlan from "./components//OneKeyPlan";
import FullHousePlan from "./components//fullhousePlan";

const distributionStatus = [
    {
        code: "2b",
        label: "渠道管理"
    },
    {
        code: "2c",
        label: "线上管理"
    }
];
export default {
    inject: ["reload"],
    components: {
        ContainsNotOpenProduct,
        CommonDesignPlan,
        OneKeyPlan,
        FullHousePlan
    },
    data() {
        return {
            activeName: "first",
            isFullHousePlan: true
        };
    },
    methods: {
        GetisFullHousePlan(isShow) {
            console.log(isShow);
            this.isFullHousePlan = isShow;
        }
    }
};
</script>
<style lang="scss" scoped>
@import "../../../assets/css/mixin";
.planLibrary {
  box-sizing: padding-box;
  background: #fff;
  border-radius: $borderRadius;
}
</style>
