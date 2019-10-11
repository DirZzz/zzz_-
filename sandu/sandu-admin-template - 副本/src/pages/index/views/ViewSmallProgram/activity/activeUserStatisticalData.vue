<template>
  <div>
    <v-nav :nav="nav"></v-nav>
    <div class="activeUserData">
      <header class="headerT">
        <h1>统计数据</h1>
      </header>
      <div class="dataShow">
        时间区间:
        <el-date-picker
          v-model="beginTime"
          type="datetime"
          @change="getData(),getWxActBargainRegResultList()"
          placeholder="选择开始日期时间"
          :picker-options="pickerOptions(0)"
          :clearable="false"
          :editable="false"
        ></el-date-picker>
        <el-date-picker
          v-model="endTime"
          type="datetime"
          @change="getData(),getWxActBargainRegResultList()"
          placeholder="选择结束日期时间"
          :picker-options="pickerOptions(1)"
          :clearable="false"
          :editable="false"
        ></el-date-picker>

        <line-chart
          :labels="labels"
          :reglist="regList"
          :regsuccesslist="regSuccessList"
          :cutlist="cutList"
          ref="vueChart"
        ></line-chart>
      </div>
      <header class="headerT">
        <h1>数据明细</h1>
        <el-button type="primany" class="exportBtn" @click="exportBtn">导出</el-button>
      </header>
      <el-row style="padding:20px;margin-bottom:20px">
        <el-table
          :data="userListData"
          style="width: 100%"
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(255, 255, 255, 0.8)"
          tooltip-effect="dark"
        >
          <el-table-column prop="time" label="时间"></el-table-column>
          <el-table-column prop="regNum" label="参与人数"></el-table-column>
          <el-table-column prop="cutNum" label="砍价人数"></el-table-column>
          <el-table-column prop="regSuccessNum" label="砍价成功人数"></el-table-column>
        </el-table>
        <div style="text-align: center;">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="query.page"
            :page-sizes="[10, 50, 100, 200,500]"
            :page-size="query.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="query.total"
          ></el-pagination>
        </div>
      </el-row>
    </div>
  </div>
</template>

<script>
import { formatDate } from "../../../filters/data";
import lineChart from "./component/statisticalData";

export default {
  name: "userdata",
  data() {
    return {
      value4: "",
      selectTime: "",
      regList: [], // 砍价人数
      regSuccessList: [], // 砍价成功人数
      cutList: [], // 砍价人数
      labels: [], // 时间段
      beginTime: "", //  开始时间
      endTime: "", //  结束时间
      minTime: "", //  开始时间
      maxTime: "", //  结束时间
      getDataShow: {},
      regid: "",
      nav: [
        { nav: "砍价活动列表", url: "/activelist" },
        { nav: "统计数据", url: "" }
      ],
      loading: false,
      userListData: [],
      query: {
        page: 1,
        limit: 10,
        total: 0
      }
    };
  },
  components: {
    lineChart
  },
  methods: {
    exportBtn() {
      this.API2.exportExcelgetWxActBargainRegResultList({
        actId: this.regid,
        beginTime: this.beginTime
          ? formatDate(new Date(this.beginTime), "yyyy-MM-dd hh:mm:ss")
          : undefined,
        endTime: this.endTime
          ? formatDate(new Date(this.endTime), "yyyy-MM-dd hh:mm:ss")
          : undefined,
        ...this.query
      }).then(data => {
        var t = new Blob([data], {
            type: "application/vnd.ms-excel;charset=utf-8"
          }),
          n = URL.createObjectURL(t),
          a = document.createElement("a");
        (a.style.display = "none"),
          (a.href = n),
          a.setAttribute("download", "统计数据明细.xls"),
          document.body.appendChild(a),
          a.click();
      });
    },
    getWxActBargainRegResultList() {
      this.loading = true;
      this.API2.getWxActBargainRegResultList({
        actId: this.regid,
        beginTime: this.beginTime
          ? formatDate(new Date(this.beginTime), "yyyy-MM-dd hh:mm:ss")
          : undefined,
        endTime: this.endTime
          ? formatDate(new Date(this.endTime), "yyyy-MM-dd hh:mm:ss")
          : undefined,
        ...this.query
      })
        .then(res => {
          if (res.success) {
            this.userListData = res.datalist;
            this.query.total = res.totalCount;
          } else {
            this.$message.error(res.message);
            this.userListData = res.datalist;
            this.query.total = 0;
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 分页模块
    handleSizeChange: function(size) {
      this.query.limit = size;
      this.getWxActBargainRegResultList();
    },
    handleCurrentChange: function(currentPage) {
      this.query.page = currentPage;
      this.loading = true;
      this.getWxActBargainRegResultList();
    },
    TimeStyle(time) {
      time.valueOf();
      time = formatDate(new Date(time), "yyyy-MM-dd hh:mm:ss");
      return time;
    },
    handlerData(lables, obj) {
      let set = new Set(this.labels);
      //处理数据
      let jiaoji = [...new Set([...lables].filter(x => set.has(x)))];
      let array = [];
      this.labels.map((item, index) => {
        let flag = jiaoji.some((it, index) => {
          return it == item;
        });
        if (flag) {
          array.push(index);
        }
      });
      let result = [];
      let count = 0;
      for (let i = 0; i < this.labels.length; i++) {
        if (
          array.some((s, it) => {
            return s == i;
          })
        ) {
          result.push(obj[count]);
          count++;
        } else {
          result.push(0);
        }
      }
      return result;
    },
    getData() {
      this.regList = [];
      this.cutList = [];
      this.regSuccessList = [];
      this.API.statisticalDataShow({
        actId: this.regid,
        beginTime: this.beginTime
          ? formatDate(new Date(this.beginTime), "yyyy-MM-dd hh:mm:ss")
          : undefined,
        endTime: this.endTime
          ? formatDate(new Date(this.endTime), "yyyy-MM-dd hh:mm:ss")
          : undefined
      }).then(res => {
        let unixDb = new Date(this.beginTime).getTime();
        let unixDe = new Date(this.endTime).getTime();
        let str = "";
        for (let k = unixDb; k <= unixDe; ) {
          str += new Date(parseInt(k)).format() + ",";
          k = k + 1 * 60 * 60 * 1000;
        }
        this.labels = [...new Set(str.split(","))];
        let labels = [];
        res.obj.regList.map((item, index) => {
          this.regList.push(Number(item.num));
          labels.push(item.time);
        });
        this.regList = this.handlerData(labels, this.regList);
        labels = [];
        res.obj.regSuccessList.map((item, index) => {
          this.regSuccessList.push(Number(item.num));
          labels.push(item.time);
        });
        this.regSuccessList = this.handlerData(labels, this.regSuccessList);
        labels = [];
        res.obj.cutList.map((item, index) => {
          this.cutList.push(Number(item.num));
          labels.push(item.time);
        });
        this.cutList = this.handlerData(labels, this.cutList);
        /*this.labels = [...new Set(str.split(','))]*/
        this.$refs.vueChart.componentsInit(
          this.regList,
          this.cutList,
          this.regSuccessList
        );
      });
    },
    // 搜索栏时间选择限制
    pickerOptions() {
      let self = this;
      let ops = {
        disabledDate(time) {
          return (
            time.getTime() + 1000 * 60 * 60 * 24 <=
              new Date(self.minTime).getTime() ||
            time.getTime() > new Date(self.maxTime).getTime()
          );
        }
      };
      return ops;
    }
  },
  created() {
    this.regid = this.$route.params.id;
    //获取活动详情
    let form = new FormData();
    form.append("actId", this.regid);
    this.API.activeInfo(form)
      .then(res => {
        this.beginTime = res.obj.begainTime;
        this.minTime = res.obj.begainTime;
        this.endTime = res.obj.endTime;
        this.maxTime = res.obj.endTime;
      })
      .finally(() => {
        this.getData();
        this.getWxActBargainRegResultList();
      });
  }
};

Date.prototype.format = function() {
  let s = "";
  s += this.getFullYear() + "-"; // 获取年份。
  if (this.getMonth() + 1 >= 10) {
    // 获取月份。
    s += this.getMonth() + 1 + "-";
  } else {
    s += "0" + (this.getMonth() + 1) + "-";
  }
  if (this.getDate() >= 10) {
    // 获取日。
    s += this.getDate() + " ";
  } else {
    s += "0" + this.getDate() + " ";
  }
  if (this.getHours() >= 10) {
    // 获取时。
    s += this.getHours();
  } else {
    s += "0" + this.getHours();
  }
  return s; // 返回日期。
};

function getAllDate(begin, end) {
  let unixDb = begin.getTime();
  let unixDe = end.getTime();
  let str = "";
  for (let k = unixDb; k <= unixDe; ) {
    str += new Date(parseInt(k)).format() + ",";
    k = k + 1 * 60 * 60 * 1000;
  }

  return str;
}
</script>

<style lang="scss" scoped>
.headerT {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  border-bottom: solid 1px #e8e8e8;
  color: #666;
  h1 {
    font-size: 20px;
  }
  .exportBtn {
    width: 110px;
    height: 36px;
    background-color: #ffd5c1;
    border-radius: 18px;
    border: solid 1px #ff6419;
    color: #ff6419;
    font-weight: 700;
  }
}

.activeUserData {
  background: #fff;
}

.dataShow {
  padding: 20px 40px;
}
</style>
