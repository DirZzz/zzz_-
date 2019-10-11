<template>
  <el-row class="page">
    <el-row class="body" v-loading="loading">
      <!-- 页面标题 -->
      <div :class="`title ${isHasTabBar?'':'bottom_border'}`">
        <h1>{{pageData.title}}</h1>
        <span></span>
      </div>
      <!-- tab导航 -->
      <el-tabs v-if="isHasTabBar" v-model="pageData.tabIndex" @tab-click="init">
        <el-tab-pane
          v-for="(tab,index) in pageData.tabBar"
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
      <el-row
        class="huizong"
        v-if="pageData.summarizingLabel&&pageData.summarizingLabel[pageData.tabIndex]"
      >
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
      <div style="padding-bottom:30px;text-align:left;font-size: 14px;">
        <transition name="body">
          <line-chart v-if="!loading&&chartType=='Line'" :chart-data="pageData"></line-chart>
          <el-row v-if="!loading&&chartType=='Bar'">
            <span>{{pageData.barChartTitle[pageData.tabIndex]}}</span>
            <bar-chart :chart-data="pageData"></bar-chart>
          </el-row>
        </transition>
      </div>
      <!-- 标题 -->
      <div :class="`title`">
        <h1 style="font-size:20px">数据明细</h1>
        <el-button type="primary" @click="exportExcel">导出</el-button>
      </div>
      <!-- 表格 -->
      <el-row style="padding:10px">
        <el-table :data="pageData.detailList" border>
          <el-table-column
            :prop="key"
            :label="pageData.detailLabel[pageData.tabIndex][key]"
            v-for="key in getKey(pageData.detailLabel[pageData.tabIndex])"
            :key="key"
          >
            <template slot-scope="scope">
              <span>{{pageData.detailListKeyValue&&pageData.detailListKeyValue[key]?pageData.detailListKeyValue[key][scope.row[key]]:scope.row[key]}}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <!-- 分页 -->
      <el-pagination
        style="text-align:right"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageParams.page"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageParams.total"
      ></el-pagination>
    </el-row>
  </el-row>
</template>

<script>
import * as pageDataTool from "./pageDatas.js";
import filterBar from "./filterBar";
import LineChart from "./LineChart.js";
import BarChart from "./BarChart.js";
export default {
  components: {
    filterBar,
    LineChart,
    BarChart
  },
  computed: {
    pageIndex() {
      return this.$route.path[this.$route.path.length - 1];
    },
    pageData() {
      return this.pageDatas[this.pageIndex];
    },
    isHasTabBar() {
      return this.pageData.tabBar != undefined;
    },
    chartType() {
      return this.pageData.chartType[this.pageData.tabIndex];
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
    },
    provincesVal() {
      return this.pageDatas[2].filterBar[this.pageData.tabIndex][1].value;
    }
  },
  data() {
    return {
      pageDatas: pageDataTool.getPageDatas(
        this,
        sessionStorage.getItem("userId")
      ),
      loading: false
    };
  },
  watch: {
    pageIndex(nVal) {
      if (!this.loading) this.init();
    },
    provincesVal(nVal) {
      if (nVal)
        pageDataTool
          .requestData({
            url: "/v1/base/area/list",
            method: "post",
            params: {
              areaCode: nVal,
              userId: sessionStorage.getItem("userId")
            },
            basePath: "systemUrl",
            type: "json"
          })
          .then(res => {
            this.$set(
              this.pageDatas[2].filterBar[this.pageDatas[2].tabIndex][2],
              "value",
              null
            );
            this.pageDatas[2].filterBar[
              this.pageDatas[2].tabIndex
            ][2].options = res.datalist.map(se => {
              se.label = se.areaName;
              se.value = se.areaCode;
              return se;
            });
          });
      else {
        this.pageDatas[2].filterBar[this.pageDatas[2].tabIndex][2].options = [];
        this.pageDatas[2].filterBar[this.pageDatas[2].tabIndex][2].value = null;
      }
    }
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.loading = true;
      this.pageParams.page = 1;
      Promise.all([
        this.getSummarizingData(),
        this.getTableData(),
        this.getChartData()
      ]).finally(() => {
        this.loading = false;
      });
    },
    getSummarizingData() {
      if (
        this.pageData.requestSummarizingUrls &&
        this.pageData.requestSummarizingUrls[this.pageData.tabIndex]
      )
        return pageDataTool
          .requestData({
            url: this.pageData.requestSummarizingUrls[this.pageData.tabIndex],
            params: this.requestParams
          })
          .then(res => {
            this.pageData.summarizingData = [res.data];
          });
    },
    getTableData() {
      if (
        this.pageData.requestTableUrls &&
        this.pageData.requestTableUrls[this.pageData.tabIndex]
      )
        return pageDataTool
          .requestData({
            url: this.pageData.requestTableUrls[this.pageData.tabIndex],
            params: {
              ...this.pageParams,
              ...this.requestParams
            }
          })
          .then(res => {
            this.pageData.detailList = res.list || [];
            this.pageParams.total = res.total;
          });
    },
    getChartData() {
      if (
        this.pageData.requestChartUrls &&
        this.pageData.requestChartUrls[this.pageData.tabIndex]
      )
        return pageDataTool
          .requestData({
            url: this.pageData.requestChartUrls[this.pageData.tabIndex],
            params: {
              ...this.requestParams
            }
          })
          .then(res => {
            this.pageData.chartDatas = res.data || res.list || [];
          });
    },
    getKey(datas) {
      return Object.keys(datas).filter(e => e != "page" && e != "limit");
    },
    exportExcel() {
      pageDataTool
        .requestData({
          url: this.pageData.requestExportUrls[this.pageData.tabIndex],
          params: {
            ...this.requestParams,
            ...this.pageParams
          },
          type: "excel",
          method: "get"
        })
        .then(data => {
          var t = new Blob([data], {
              type: "application/vnd.ms-excel;charset=utf-8"
            }),
            n = URL.createObjectURL(t),
            a = document.createElement("a");
          (a.style.display = "none"),
            (a.href = n),
            a.setAttribute(
              "download",
              `${this.pageData.title}${
                this.isHasTabBar
                  ? "-" + this.pageData.tabBar[this.pageData.tabIndex]
                  : ""
              }.xls`
            ),
            document.body.appendChild(a),
            a.click();
        });
    },
    // 分页
    handleSizeChange(val) {
      this.pageParams.limit = val;
      this.getTableData();
    },
    handleCurrentChange(val) {
      this.pageParams.page = val;
      this.getTableData();
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
    transition: all 0.5s ease;
  }
  .body-enter,
  .body-leave-to {
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
      margin-bottom: 20px;
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
