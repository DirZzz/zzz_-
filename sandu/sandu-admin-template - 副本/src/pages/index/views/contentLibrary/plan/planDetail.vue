<!--方案库详情页面-->
<template>
 <div class="planDetail">
     <!--智能方案详情页面-->
   <one-key-plan-detail v-if="!detailPlanType"></one-key-plan-detail>
      <!--全屋方案详情页面-->
    <fullhouse-plan-detail v-else></fullhouse-plan-detail>
 </div>
</template>

<script type="text/ecmascript-6">
import oneKeyPlanDetail from '@/views/contentLibrary/plan/components/oneKeyPlanDetail.vue'
import fullhousePlanDetail from '@/views/contentLibrary/plan/components/fullhousePlanDetail.vue'
export default {
 data() {
 return {

 }
 },
 components: {
     oneKeyPlanDetail,
     fullhousePlanDetail
 },
 methods: {

 },
computed: {
    detailPlanType(){
        if(localStorage.getItem('isNowPlanType')=='true'){
            return  true;
        }else{
            return false;
        }
    }  
},
}
</script>

<style scoped lang="scss">
</style>
