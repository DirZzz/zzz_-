<template>
  <div>
    <template>
      <div v-if="pageStatus==0">
        <headers></headers>
        <searchBar class="t_join" :searchs="searchs" :onSearch="getList" :pageData="pageData"></searchBar>
        <el-row class="t_row" style="text-align:right">
          <el-button type="primary" @click="handleDownload">导出Excel</el-button>
        </el-row>
        <el-row class="t_row t_join">
          <el-table
            :data="pageList"
            :header-cell-style="rowStyle"
            ref="refTable"
            v-loading="loading"
          >
            <el-table-column prop="index" width="90" label="序号" align="center">
              <template slot-scope="scope">{{ scope.row.index }}</template>
            </el-table-column>
            <el-table-column
              prop="openId"
              label="openId"
              align="center"
              :show-overflow-tooltip="true"
            >
              <template slot-scope="scope">{{ scope.row.openId }}</template>
            </el-table-column>
            <el-table-column
              prop="nickName"
              label="昵称"
              align="center"
              :show-overflow-tooltip="true"
            >
              <template slot-scope="scope">{{ scope.row.nickName }}</template>
            </el-table-column>
            <el-table-column prop="mobile" label="用户手机号" align="center">
              <template slot-scope="scope">{{ scope.row.mobile||"-" }}</template>
            </el-table-column>
            <el-table-column prop="labelName" label="用户标签" :show-overflow-tooltip="true">
              <template slot-scope="scope">{{ scope.row.labelName }}</template>
            </el-table-column>
            <el-table-column prop="lifeTime" label="生命周期" align="center">
              <template slot-scope="scope">{{ scope.row.lifeTime }}</template>
            </el-table-column>
            <el-table-column
              prop="behavior"
              label="用户行为"
              align="center"
              :show-overflow-tooltip="true"
            >
              <template slot-scope="scope">{{ scope.row.behavior||"-" }}</template>
            </el-table-column>
            <el-table-column prop="address" label="地区" align="center">
              <template slot-scope="scope">{{ scope.row.address||"-" }}</template>
            </el-table-column>
            <el-table-column prop="curLoginTime" label="最近登录时间" align="center">
              <template slot-scope="scope">{{ scope.row.curLoginTime }}</template>
            </el-table-column>
            <el-table-column prop="id" label="操作" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="open(scope.row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-row style="margin-top:20px;text-align:right">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page.sync="pageData.page"
              :page-sizes="[10, 20, 30, 40,50]"
              :page-size.sync="pageData.limit"
              layout="total, sizes, prev, pager, next, jumper"
              :total.sync="pageData.total"
            ></el-pagination>
          </el-row>
        </el-row>
      </div>
      <div v-if="pageStatus==1">
        <div class="t_row">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item>
              <a @click.stop="pageStatus=0">用户标签数据</a>
            </el-breadcrumb-item>
            <el-breadcrumb-item>
              <a>详情</a>
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <el-form :model="detail" label-width="130px" class="t_row t_join">
          <el-form-item label="序号：">
            <span>{{detail.index}}</span>
          </el-form-item>
          <el-form-item label="openId：">
            <span>{{detail.openId}}</span>
          </el-form-item>
          <el-form-item label="昵称：">
            <span>{{detail.nickName}}</span>
          </el-form-item>
          <el-form-item label="用户手机号：">
            <span>{{detail.mobile||"-"}}</span>
          </el-form-item>
          <el-form-item label="用户标签：">
            <span>{{detail.labelName||"-"}}</span>
          </el-form-item>
          <el-form-item label="生命周期：">
            <span>{{detail.lifeTime||"-"}}</span>
          </el-form-item>
          <el-form-item label="用户行为：">
            <span style="letter-spacing:1px;">{{detail.behavior||"-"}}</span>
          </el-form-item>
          <el-form-item label="地区：">
            <span>{{detail.address||"-"}}</span>
          </el-form-item>
          <el-form-item label="最近登录时间：">
            <span>{{detail.curLoginTime||"-"}}</span>
          </el-form-item>
          <el-form-item>
            <el-button @click="pageStatus=0">关闭</el-button>
          </el-form-item>
        </el-form>
      </div>
    </template>
  </div>
</template>

<script>
import headers from "./../content/headers";
import searchBar from "./searchBar";
export default {
  components: {
    headers,
    searchBar
  },
  data() {
    return {
      loading: false,
      pageStatus: 0,
      pageData: { page: 1, limit: 10 },
      pageList: [],
      searchs: [
        {
          fields: "openId",
          placeholder: "openId",
          type: "text"
        },
        {
          fields: "nickName",
          placeholder: "昵称",
          type: "text"
        },
        {
          fields: "mobile",
          placeholder: "用户手机号码",
          type: "number"
        },
        {
          fields: "labelName",
          placeholder: "用户标签",
          options: [{ label: "不限", value: "" }, { value: "浏览型用户" }],
          type: "select"
        },
        {
          fields: "lifeTime",
          placeholder: "生命周期",
          options: [
            { label: "不限", value: "" },
            { value: "活跃期" },
            { value: "衰退期" },
            { value: "沉睡期" },
            { value: "流失期" },
            { value: "死亡期" }
          ],
          type: "select"
        }
      ],
      detail: {}
    };
  },
  computed: {},
  created() {
    this.API.getUserTagsList({ page: 1, limit: 99999 }).then(res => {
      if (res.success) {
        res.datalist.forEach(e => {
          this.searchs[3].options.push({ value: e.labelName });
        });
      }
    });
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      this.API.getUserTagList(this.pageData).then(res => {
        if (res.success) {
          this.pageList = res.datalist.map((e, i) => {
            e.index = (this.pageData.page - 1) * this.pageData.limit + i + 1;
            return e;
          });
          this.pageData.total = res.totalCount;
        } else {
          this.pageList = [];
          this.pageData.total = 0;
        }
        this.loading = false;
      });
    },
    // 导出excel 表
    handleDownload() {
      this.API.exportUserLabelExcel(this.pageData).then(res => {
        var t = new Blob([res], {
            type: "application/vnd.ms-excel;charset=utf-8"
          }),
          n = URL.createObjectURL(t),
          a = document.createElement("a");
        (a.style.display = "none"),
          (a.href = n),
          a.setAttribute("download", "userTags.xls"),
          document.body.appendChild(a),
          a.click();
      });
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => v[j]));
    },
    open(data) {
      this.pageStatus = 1;
      this.detail = data;
    },
    // 分页模块f
    handleSizeChange: function(size) {
      this.pageData.limit = size;
      this.pageData.page = 1;
      this.pageData.list = [];
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
</style>
