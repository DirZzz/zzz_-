<template>
  <div class="transactionRecord-wrap">
    <v-nav :nav="nav"></v-nav>
    <div class="transactionRecord-content">
      <el-tabs :value="tabValue" class="tab-box" @tab-click="tabClick">
        <el-tab-pane label="可购买数据" name="purchasable">
          <!-- <purchasable></purchasable> -->
        </el-tab-pane>
        <el-tab-pane label="已购买数据" name="purchased">
          <!-- <purchased></purchased> -->
        </el-tab-pane>
        <el-tab-pane label="交易记录" name="transactionRecord">
          <!-- <transaction-record></transaction-record> -->
        </el-tab-pane>
      </el-tabs>
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import purchasable from "./purchasable.vue";
import purchased from "./purchased.vue";
import transactionRecord from "./transactionRecord.vue";
export default {
  data() {
    return {
      loading: true,
      nav: [],
      label: "",
      tabValue: "purchasable",
      id: null,
      companyId: null
    };
  },
  created() {
    this.id =  localStorage.getItem('id');
    this.tabValue = localStorage.getItem('tabVale');
    this.companyId = localStorage.getItem('companyId');
    setTimeout(()=>{
      this.toTab(this.tabValue);
    },200)
    this.nav = [
      { nav: "活动列表", url: "/activity/activityList" },
      { nav: this.label, url: "" }
    ];
  },
  components: {
    purchasable,
    purchased,
    transactionRecord
  },
  methods: {
    tabClick(e) {
      this.toTab(e.name);
      localStorage.setItem('tabVale',e.name);
      this.nav = [
        { nav: "活动列表", url: "/activity/activityList" },
        { nav: this.label, url: "" }
      ];
    },
    toTab(tabName) {
      switch (tabName) {
        case "transactionRecord":
          this.label = "交易记录";
          this.$router.push({
            path: "/activity/transaction-record",
            query: {
              id: this.id,
              tabValue: this.value
            }
          });
          break;
        case "purchased":
          this.label = "已购买用户信息";
          this.$router.push({
            path: "/activity/purchased",
            query: {
              id: this.id,
              tabValue: this.value,
            }
          });
          break;
        case "purchasable":
          this.label = "可购买用户信息";
          this.$router.push({
            path: "/activity/purchasable",
            query: {
              id: this.id,
              tabValue: this.value,
              companyId: this.companyId
            }
          });
          break;
      }
    },
    
  },
  watch:{
      $route(to,from){
      this.tabValue = to.name
    }
  }
};
</script>
<style lang='scss' scoped>
.transactionRecord-content {
  .tab-box{
    background: #fff;
  }
  .option-btn {
    button {
      width: 70px;
      text-align: left;
    }
  }
}
</style>