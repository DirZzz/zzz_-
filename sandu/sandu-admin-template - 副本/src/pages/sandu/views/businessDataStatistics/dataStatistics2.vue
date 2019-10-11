<template>
  <el-row class="page">
    <el-row class="body">
      <!-- 页面标题 -->
      <transition name="body">
        <!-- <el-button @click="show=!show">切换</el-button> -->
      </transition>
      <div :class="`title ${isHasTabBar?'':'bottom_border'}`">
        <h1>{{pageDatas[pageIndex].title}}</h1>
        <span></span>
      </div>
      <!-- tab导航 -->
      <el-tabs v-if="isHasTabBar" v-model="pageData.tabIndex">
        <el-tab-pane
          v-for="(tab,index) in pageDatas[pageIndex].tabBar"
          :key="index"
          :label="tab"
          :name="index.toString()"
        ></el-tab-pane>
      </el-tabs>
      <!-- 筛选栏 -->
      <filterBar
        :searchs="pageData.filterBar[pageData.tabIndex]"
        :pageParams="requestParams"
        :onSearch="init"
      ></filterBar>
      <!-- 汇总表格 -->
      <el-row class="huizong">
        <span>汇总：</span>
        <el-row class="table">
          <el-table :data="pageData.summarizingData" border>
            <el-table-column
              :prop="key"
              :label="pageData.summarizingLabel[pageData.tabIndex][key]"
              v-for="key in getKey(pageData.summarizingLabel[pageData.tabIndex])"
              :key="key"
            ></el-table-column>
          </el-table>
        </el-row>
      </el-row>
      <!-- 图表 -->
      <vue-chart :chart-data="pageData.detailList" charttype="Line" :height="500"></vue-chart>
      <!-- 标题 -->
      <div :class="`title`">
        <h1 style="font-size:20px">数据明细</h1>
        <el-button type="primary">导出</el-button>
      </div>
      <!-- 表格 -->
      <el-row style="padding:10px">
        <el-table :data="pageData.detailList" border>
          <el-table-column
            :prop="key"
            :label="pageData.detailLabel[pageData.tabIndex][key]"
            v-for="key in getKey(pageData.detailLabel[pageData.tabIndex])"
            :key="key"
          ></el-table-column>
        </el-table>
      </el-row>
      <!-- 分页 -->
      <el-pagination
        style="text-align:right"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageData.pageParams[pageData.tabIndex].page"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageData.pageParams[pageData.tabIndex].total"
      ></el-pagination>
    </el-row>
  </el-row>
</template>

<script>
import * as pageDataTool from "./pageDatas.js";
import filterBar from "./filterBar";
import VueChart from "./VueChart.js";
export default {
  components: {
    filterBar,
    VueChart
  },
  computed: {
    pageIndex() {
      return this.$route.path[this.$route.path.length - 1];
    },
    pageData() {
      return this.pageDatas[this.pageIndex];
    },
    isHasTabBar() {
      return this.pageData.hasOwnProperty("tabBar");
    },
    requestParams() {
      let requestParams = {};
      this.pageData.filterBar[this.pageData.tabIndex].forEach(e => {
        if (e.value != undefined) requestParams[e.fields] = e.value;
      });
      return requestParams;
    },
    pageParams() {
      return this.pageData.pageParams[this.pageData.tabIndex];
    }
  },
  data() {
    return {
      pageDatas: pageDataTool.getPageDatas(this)
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.getSummarizingData();
      this.getTableData();
    },
    getSummarizingData() {
      pageDataTool
        .requestData(this, "getUserCountList", this.requestParams)
        .then(res => {
          this.pageData.summarizingData = [res.data];
        });
    },
    getTableData() {
      pageDataTool
        .requestData(this, "getUserListByUserStatistics", {
          ...this.pageParams,
          ...this.requestParams
        })
        .then(res => {
          this.pageData.detailList = res.list || [];
          this.pageParams.total = res.total;
        });
    },
    getKey(datas) {
      return Object.keys(datas).filter(e => e != "page" && e != "limit");
    },
    // 分页
    handleSizeChange(val) {
      this.pageParams.limit = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.pageParams.page = val;
      this.init();
    }
  }
};
</script>

<style lang="scss" scoped>
.page {
  padding: 20px;
  .body {
    padding: 20px;
    background-color: #fff;
  }
  .body-enter-active {
    transition: all 0.3s ease;
  }
  .body-leave-active {
    transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
  }
  .body-enter, .body-leave-to
    /* .body-leave-active for below version 2.1.8 */ {
    transform: translateX(10px);
    opacity: 0;
  }
  .title {
    display: flex;
    justify-content: space-between;
    h1 {
      font-size: 21px;
      font-weight: 600;
      line-height: 30px;
      padding-bottom: 10px;
    }
    &.bottom_border {
      border-bottom: #e4e7ed solid 2px;
    }
  }
  .huizong {
    text-align: left;
    font-size: 14px;
    margin: 50px 0 30px 0;
    .table {
      width: 60%;
      min-width: 600px;
      margin: 10px;
    }
  }
}
</style>
