<!--共享方案库详情页面-->
<template>
 <div class="sharePlanDetail">
     <!--智能方案 详情-->
        <one-key-share-plan-detail v-if="isFullShare"></one-key-share-plan-detail>
     <!--全屋方案 详情-->
     <fullhouse-share-plan-detail v-else></fullhouse-share-plan-detail>
 </div>
</template>

<script type="text/ecmascript-6">
import oneKeySharePlanDetail from './components/oneKeySharePlanDetail'
import fullhouseSharePlanDetail from './components/fullhouseSharePlanDetail'
export default {
 data() {
 return {

 }
 },
 components: {
   oneKeySharePlanDetail,
   fullhouseSharePlanDetail
 },
 methods: {

 },
     computed: {
      isFullShare(){
        if(localStorage.getItem('fullhouseSharePlanTyle')==3){
          return false
        }else{
          return true
        }
      }
    },
}
</script>

<style scoped lang="scss">
</style>
