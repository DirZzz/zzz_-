<!--方案库 方案编辑页面-->
<template>
 <div class="PlanEdit">
     <!--智能方案 编辑页面-->
    <one-key-plan-edit v-if="!detailPlanType"></one-key-plan-edit>
     <!--全屋方案 编辑页面-->
    <fullhouse-plan-edit v-else></fullhouse-plan-edit>
 </div>
</template>

<script type="text/ecmascript-6">
import oneKeyPlanEdit from '@/views/contentLibrary/plan/components/oneKeyPlanEdit'
import fullhousePlanEdit from '@/views/contentLibrary/plan/components/fullhousePlanEdit'
export default {
 data() {
 return {

 }
 },
 components: {
   oneKeyPlanEdit,
   fullhousePlanEdit  
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
