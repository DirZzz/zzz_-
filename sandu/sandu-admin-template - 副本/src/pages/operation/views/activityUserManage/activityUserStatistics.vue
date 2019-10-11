<template>
  <div class="user-management">
    <div class="main-body">
      <!-- 标题 -->
      <div class="title">参与活动用户统计</div>
      <!-- 筛选栏 -->
      <el-form :inline="true" class="filterBar">
        <el-form-item label="时间区间">
          <el-date-picker
            v-model="filterDatas.date"
            type="datetimerange"
            :default-time="['00:00:00']"
            :value-format="'yyyy-MM-dd HH:mm:ss'"
            @change="initData"
            clearable
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="平台">
          <el-select v-model="filterDatas.originPlatform" clearable @change="initData">
            <el-option label="随选网" value="selectDecoration"></el-option>
            <el-option label="装修小程序" value="quotedDecoration"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动名称">
          <el-input v-model="filterDatas.activityName" @input="initData"></el-input>
        </el-form-item>
        <el-form-item label="位置">
          <el-select v-model="filterDatas.activityEntry" clearable filterable @change="initData">
            <el-option
              v-for="item in locationArr"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- 汇总栏 -->
      <el-row class="huizong">
        <span>汇总：</span>
        <el-row class="table">
          <el-row>总人数</el-row>
          <el-row>{{aggregate}}</el-row>
        </el-row>
      </el-row>
      <!-- 折线图表 -->
      <transition name="body">
        <div class="body">
          <line-chart v-if="!loading&&chartDatas" :chart-data="chartDatas"></line-chart>
        </div>
      </transition>
      <!-- 数据明细表格 -->
      <div class="tableBar">
        <h1>数据明细</h1>
        <el-button type="primary" @click="exportExcel">导出Excel</el-button>
      </div>
      <el-row style="padding:15px">
        <el-table :data="tableDatas" border :header-cell-style="headerStyle">
          <el-table-column
            label="序号"
            type="index"
            width="100px"
            align="center"
            :index="indexMethod"
          ></el-table-column>
          <el-table-column label="时间" prop="dateTime" align="center"></el-table-column>
          <el-table-column label="人数" prop="total" align="center"></el-table-column>
        </el-table>
      </el-row>
      <!-- 分页 -->
      <el-pagination
        style="text-align:right;padding:15px"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageParams.start"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageParams.total"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import LineChart from "./LineChart.js";
export default {
  components: {
    LineChart
  },
  data() {
    return {
      loading: false,
      headerStyle: {
        backgroundColor: "#f5f5f5",
        textAlign: "center"
      },
      locationArr: [],
      filterDatas: {
        date: undefined,
        appointmentTimeStart: undefined, //开始区间
        appointmentTimeEnd: undefined, //结束区间
        activityEntry: undefined, //位置
        originPlatform: undefined, // 平台 selectDecoration:随选网|quotedDecoration:装修小程序
        activityName: undefined //活动名称
      },
      pageParams: {
        start: 1,
        limit: 10,
        total: undefined
      },
      tableDatas: [],
      aggregate: undefined,
      chartDatas: undefined
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.initDatePrototype();
      var myDate = new Date();
      this.filterDatas.appointmentTimeStart = new Date(
        myDate.getTime() - 7 * 24 * 60 * 60 * 1000
      ).Format("yyyy-MM-dd HH:mm:ss");
      this.filterDatas.appointmentTimeEnd = myDate.Format(
        "yyyy-MM-dd HH:mm:ss"
      );
      this.filterDatas.date = [
        this.filterDatas.appointmentTimeStart,
        this.filterDatas.appointmentTimeEnd
      ];
      this.getLocationList();
      this.getData();
    },
    initData() {
      this.pageParams.start = 1;
      this.getData();
    },
    getData() {
      [
        this.filterDatas.appointmentTimeStart,
        this.filterDatas.appointmentTimeEnd
      ] = this.filterDatas.date;
      this.loading = true;
      this.API.getChartLineData({
        ...this.filterDatas,
        ...this.pageParams
      })
        .then(res => {
          if (res.success) {
            this.tableDatas = res.obj.data;
            this.chartDatas = res.obj.chartLineData;
            this.aggregate = res.obj.aggregate;
            this.pageParams.total = res.obj.totalCount;
          } else {
            this.$message.error(res.message);
          }
        })
        .finally(() => (this.loading = false));
    },
    exportExcel() {
      this.API.exportChartLineData(this.filterDatas).then(data => {
        var t = new Blob([data], {
            type: "application/vnd.ms-excel;charset=utf-8"
          }),
          n = URL.createObjectURL(t),
          a = document.createElement("a");
        (a.style.display = "none"),
          (a.href = n),
          a.setAttribute("download", `参与活动用户统计数据.xls`),
          document.body.appendChild(a),
          a.click();
      });
    },
    // 分页
    handleSizeChange(val) {
      this.pageParams.limit = val;
      this.getData();
    },
    handleCurrentChange(val) {
      this.pageParams.start = val;
      this.getData();
    },
    indexMethod(index) {
      return (this.pageParams.start - 1) * this.pageParams.limit + index + 1;
    },

    getLocationList() {
      this.API.getlocationList().then(res => {
        if (res.success) {
          this.locationArr = res.obj;
        }
      });
    },
    initDatePrototype() {
      Date.prototype.Format = function(fmt) {
        // author: meizz
        var o = {
          "M+": this.getMonth() + 1, // 月份
          "d+": this.getDate(), // 日
          "H+": this.getHours(), // 小时
          "m+": this.getMinutes(), // 分
          "s+": this.getSeconds(), // 秒
          "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
          S: this.getMilliseconds() // 毫秒
        };
        if (/(y+)/.test(fmt))
          fmt = fmt.replace(
            RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length)
          );
        for (var k in o)
          if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(
              RegExp.$1,
              RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length)
            );
        return fmt;
      };
    }
  }
};
</script>


<style lang="scss" scoped>
.user-management {
  padding: 30px;
  .main-body {
    min-height: 860px;
    background: #fff;
    .title {
      font-size: 18px;
      padding: 15px;
      border-bottom: 1px solid #eee;
      text-align: left;
    }
    .filterBar {
      padding: 15px;
      text-align: left;
    }
    .huizong {
      display: flex;
      font-size: 14px;
      margin: 0 0 30px 15px;
      align-items: center;
      .table {
        margin-left: 20px;
        width: 301px;
        display: flex;
        line-height: 42px;
        border: 1px solid #aaa;
        justify-content: space-evenly;
        * {
          width: 150px;
        }
        * ~ * {
          border-left: 1px solid #aaa;
        }
      }
    }
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
    .tableBar {
      padding: 15px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
</style>
