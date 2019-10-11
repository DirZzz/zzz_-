<template>
  <div style="margin:20px 20px 0">
    <el-tabs type="border-card" v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="用户标签数据" name="userTags">
        <div class="t_flex">
          <div>
            <span>请选择时间：</span>
            <el-date-picker
              v-model="pageData.daterange"
              type="daterange"
              range-separator="——"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              @change="dateChange"
            ></el-date-picker>
          </div>

          <el-button type="primary" @click="handleDownload">导出Excel</el-button>
        </div>
        <el-row style="padding-top:20px">
          <el-table :data="pageList1" :header-cell-style="rowStyle" v-loading="loading">
            <template v-for="(item,index) in pageList1" v-if="index==0">
              <el-table-column :key="index" label="时间" align="center">
                <template slot-scope="scope">{{ scope.row.key }}</template>
              </el-table-column>
              <el-table-column
                v-for="(subitem,subindex) in item.obj"
                :key="subitem.labelName"
                :label="subitem.labelName"
                align="center"
              >
                <template slot-scope="scope">{{ scope.row.obj[subindex].total }}</template>
              </el-table-column>
            </template>
          </el-table>
          <el-row style="margin-top:20px;text-align:right">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page.sync="pageData.page"
              :page-sizes="[10, 20, 30, 40,50]"
              :page-size.sync="pageData.limit"
              layout="total, sizes, prev, pager, next, jumper"
              :total.sync="pageData.total1"
            ></el-pagination>
          </el-row>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="生命周期数据" name="liftcycle">
        <div class="t_flex">
          <div>
            <span>请选择时间：</span>
            <el-date-picker
              v-model="pageData.daterange"
              type="daterange"
              range-separator="——"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              @change="dateChange"
            ></el-date-picker>
          </div>
          <el-button type="primary" @click="handleDownload">导出Excel</el-button>
        </div>
        <el-row style="padding-top:20px">
          <el-table :data="pageList2" :header-cell-style="rowStyle" v-loading="loading">
            <template v-for="(item,index) in pageList2" v-if="index==0">
              <el-table-column :key="index" label="时间" align="center">
                <template slot-scope="scope">{{ scope.row.key }}</template>
              </el-table-column>
              <el-table-column
                v-for="(subitem,subindex) in item.obj"
                :key="subitem.userLife"
                :label="subitem.userLife"
                align="center"
              >
                <template slot-scope="scope">{{ scope.row.obj[subindex].total }}</template>
              </el-table-column>
            </template>
          </el-table>
          <el-row style="margin-top:20px;text-align:right">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page.sync="pageData.page"
              :page-sizes="[10, 20, 30, 40,50]"
              :page-size.sync="pageData.limit"
              layout="total, sizes, prev, pager, next, jumper"
              :total.sync="pageData.total2"
            ></el-pagination>
          </el-row>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="生命周期与标签数据" name="liftcycleAndTag">
        <div class="t_flex">
          <div>
            <span>请选择时间：</span>
            <el-date-picker
              v-model="pageData.time"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择日期"
              @change="dateChange"
            ></el-date-picker>
          </div>
          <el-button type="primary" @click="handleDownload">导出Excel</el-button>
        </div>
        <el-row style="padding-top:20px">
          <el-table :data="pageList3" :header-cell-style="rowStyle" v-loading="loading">
            <template v-for="(item,index) in pageList3" v-if="index==0">
              <el-table-column :key="index" label="标签" align="center">
                <template slot-scope="scope">{{ scope.row.key }}</template>
              </el-table-column>
              <el-table-column
                v-for="(subitem,subindex) in item.obj"
                :key="subitem.lifeTime"
                :label="subitem.lifeTime"
                align="center"
              >
                <template slot-scope="scope">{{ scope.row.obj[subindex].count }}</template>
              </el-table-column>
            </template>
          </el-table>
          <el-row style="margin-top:20px;text-align:right">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page.sync="pageData.page"
              :page-sizes="[10, 20, 30, 40,50]"
              :page-size.sync="pageData.limit"
              layout="total, sizes, prev, pager, next, jumper"
              :total.sync="pageData.total3"
            ></el-pagination>
          </el-row>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeName: "userTags",
      pageData: {
        page: 1,
        limit: 10,
      },
      pageList1: [],
      pageList2: [],
      pageList3: [],
      loading: false
    };
  },
  created() {
    Date.prototype.Format = function(formatStr) {
      var str = formatStr;
      var Week = ["日", "一", "二", "三", "四", "五", "六"];
      str = str.replace(/yyyy|YYYY/, this.getFullYear());
      str = str.replace(
        /yy|YY/,
        this.getYear() % 100 > 9
          ? (this.getYear() % 100).toString()
          : "0" + (this.getYear() % 100)
      );
      str = str.replace(
        /MM/,
        this.getMonth() > 9
          ? (this.getMonth() + 1).toString()
          : "0" + (this.getMonth() + 1)
      );
      str = str.replace(/M/g, this.getMonth());
      str = str.replace(/w|W/g, Week[this.getDay()]);
      str = str.replace(
        /dd|DD/,
        this.getDate() > 9 ? this.getDate().toString() : "0" + this.getDate()
      );
      str = str.replace(/d|D/g, this.getDate());
      str = str.replace(
        /hh|HH/,
        this.getHours() > 9 ? this.getHours().toString() : "0" + this.getHours()
      );
      str = str.replace(/h|H/g, this.getHours());
      str = str.replace(
        /mm/,
        this.getMinutes() > 9
          ? this.getMinutes().toString()
          : "0" + this.getMinutes()
      );
      str = str.replace(/m/g, this.getMinutes());
      str = str.replace(
        /ss|SS/,
        this.getSeconds() > 9
          ? this.getSeconds().toString()
          : "0" + this.getSeconds()
      );
      str = str.replace(/s|S/g, this.getSeconds());
      return str;
    };
    this.getList();
  },
  methods: {
    getList(status) {
      this.loading = true;
      if (!this.pageData.time || !this.pageData.daterange.length) {
        this.$set(
          this.pageData,
          "time",
          new Date(+new Date() - 1000 * 60 * 60 * 24).Format("yyyy-MM-dd")
        );
        this.$set(this.pageData, "daterange", [
          new Date(+new Date() - 10000 * 60 * 60 * 24).Format("yyyy-MM-dd"),
          new Date(+new Date() - 1000 * 60 * 60 * 24).Format("yyyy-MM-dd")
        ]);
      }
      (() => {
        let fData = new FormData();
        fData.append("page", this.pageData.page);
        fData.append("limit", this.pageData.limit);
        if (this.pageData.daterange.length) {
          fData.append("dateBefore", this.pageData.daterange[0]);
          fData.append("dateAfter", this.pageData.daterange[1]);
        }
        switch (this.activeName) {
          case "userTags":
            this.API.getUserLabelCount(fData).then(res => {
              if (res.success) {
                this.pageList1 = res.obj.data;
                this.pageData.total1 = res.obj.total;
              } else {
                this.pageList1 = [];
                this.pageData.total1 = 0;
              }
              this.loading = false;
            });
            break;
          case "liftcycle":
            this.API.getUserLifeCount(fData).then(res => {
              if (res.success) {
                this.pageList2 = res.obj.data;
                this.pageData.total2 = res.obj.total;
              } else {
                this.pageList2 = [];
                this.pageData.total2 = 0;
              }
              this.loading = false;
            });
            break;
          case "liftcycleAndTag":
            this.API.getLifeAndTag(this.pageData).then(res => {
              if (!this.pageData.time) {
                let date = new Date(+new Date() - 1000 * 60 * 60 * 24);
                this.$set(this.pageData, "time", date.Format("yyyy-MM-dd"));
              }
              if (res.success) {
                this.pageList3 = res.datalist;
                this.pageData.total3 = res.totalCount;
              } else {
                this.pageList3 = [];
                this.pageData.total3 = 0;
              }
              this.loading = false;
            });
            break;
        }
      })();
    },
    handleClick(tab, event) {
      this.pageData = {
        page: 1,
        limit: 10
      };
      this.getList();
    },
    dateChange() {
      this.pageData.page = 1;
      this.getList();
    },
    // 导出excel 表
    handleDownload() {
      (() => {
        switch (this.activeName) {
          case "userTags":
            return this.API.exportUserLabelCountExcel(this.pageData);
          case "liftcycle":
            return this.API.exportUserLifeCountExcel(this.pageData);
          case "liftcycleAndTag":
            return this.API.exportLifeAndTagCountExcel(this.pageData);
        }
      })().then(res => {
        var t = new Blob([res], {
            type: "application/vnd.ms-excel;charset=utf-8"
          }),
          n = URL.createObjectURL(t),
          a = document.createElement("a");
        (a.style.display = "none"),
          (a.href = n),
          a.setAttribute("download", this.activeName + ".xls"),
          document.body.appendChild(a),
          a.click();
      });
    },
    // 分页模块f
    handleSizeChange: function(size) {
      this.pageData.limit = size;
      this.pageData.page = 1;
      this.pageList = [];
      this.getList();
    },
    handleCurrentChange: function(currentPage) {
      this.pageData.page = currentPage;
      this.getList();
    },
    rowStyle({ row, rowIndex }) {
      if (rowIndex === 0) {
        return {
          height: "30px!important",
          background: "#f5f5f5",
          color: "#666",
          lineHeight: "30px!important"
        };
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.t_row {
  margin: 20px 20px 0;
  padding: 20px 30px;
  background-color: #fff;
  text-align: left;
}
.t_join {
  margin-top: -20px;
}
.t_flex {
  display: flex;
  justify-content: space-between;
}
</style>
