<!--店铺管理 方案详情页面-->
<template>
 <div class="page">
     <!--一键方案 详情-->
     <shop-onekey-plan-detail v-if="!ShopPlanType"></shop-onekey-plan-detail>
      <!--全屋方案 详情-->
      <shop-fullhouse-plan-detail v-else></shop-fullhouse-plan-detail>
 </div>
</template>

<script type="text/ecmascript-6">
import shopOnekeyPlanDetail from './OneKeyPlanDetail'
import shopFullhousePlanDetail from './shopPlancomponents/fullhouseShopPlanDetail'
export default {
 data() {
 return {
  
 }
 },
 methods: {

 },
 components: {
    shopOnekeyPlanDetail,
    shopFullhousePlanDetail
 },
 computed: {
     ShopPlanType(){
         if( sessionStorage.getItem('shopPlanType')==3){
             return true;
         }else{
             return false;
         }
     }
 }
}
</script>

<style scoped lang="scss">
</style>
