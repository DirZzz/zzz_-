<!--页面注释-->
<template>
 <div class="capital">
   <el-tabs v-model="activeName"   class="headerNAV" @tab-click="handleClick">
    <el-tab-pane v-if="showTab('check:myDubi:view','mySY')" label="我的度币" name="myDB">
        <my-d-b></my-d-b>
    </el-tab-pane>
    <el-tab-pane v-if="showTab('check:myIncome:view','myDB')" label="我的收益"  name="mySY">
      <my-s-y></my-s-y>
    </el-tab-pane>
  </el-tabs>
 </div>
</template>

<script type="text/ecmascript-6">
import myDB from './myDB';
import mySY from './mySY';
import qs from 'qs';
export default {
 data() {
 return {
   activeName: 'myDB'
 }
 },
 components: {
   myDB,
   mySY
 },
 methods: {
     showTab(permission,currentTab){
         let per = this.isPer(permission);
         if (!per){
             this.activeName = currentTab
         }
         return per
     },
     isPer(per) {
         let permiss = qs.parse(sessionStorage.getItem('loginUser'))
         return this.isMainPer(per, permiss)
     },
   handleClick(tab, event) {
        console.log(tab, event);
      }
 }
}
</script>

<style scoped lang="scss">
.capital{
   .headerNAV{
       background: white;
       .el-tab-pane{
           padding: 20px;
       }

   }
}
</style>
