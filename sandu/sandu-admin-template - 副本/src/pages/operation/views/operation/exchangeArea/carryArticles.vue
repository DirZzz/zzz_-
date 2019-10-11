<template>
  <div>
    <!-- 列表页 -->
    <div>
      <div class="t_row">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>
            <a @click.stop="$router.go(-1)">文章管理</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <a>搬运文章</a>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <searchBar class="t_join" :searchs="searchs" :onSearch="getList" :pageData="pageData"></searchBar>
      <el-row class="t_row">
        <el-button type="primary" @click="carryArticles" v-loading="submitLoading">确认添加</el-button>
      </el-row>
      <el-row class="t_row t_join">
        <el-table
          :data="pageList"
          :header-cell-style="rowStyle"
          ref="refTable"
          @selection-change="handleTableCurrentChange"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column prop="title" label="标题" align="center">
            <template slot-scope="scope">{{ scope.row.title }}</template>
          </el-table-column>
          <el-table-column prop="type" label="类型" align="center">
            <template slot-scope="scope">{{ types[scope.row.type] }}</template>
          </el-table-column>
          <el-table-column prop="releaseAuthor" label="发布人" align="center">
            <template slot-scope="scope">{{ scope.row.releaseAuthor }}</template>
          </el-table-column>
          <el-table-column prop="shopName" label="店铺" align="center">
            <template slot-scope="scope">{{ scope.row.shopName }}</template>
          </el-table-column>
          <el-table-column prop="releaseTime" label="发布时间" align="center">
            <template slot-scope="scope">{{ scope.row.releaseTime || "-" }}</template>
          </el-table-column>
        </el-table>
        <el-row style="margin-top:20px;text-align:right">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="pageData.page"
            :page-sizes="[10, 20, 30, 40]"
            :page-size.sync="pageData.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total.sync="pageData.total"
          ></el-pagination>
        </el-row>
      </el-row>
    </div>
  </div>
</template>

<script>
import headers from "./../../content/headers";
import searchBar from "./../../userTagsMessage/searchBar";
export default {
  components: {
    headers,
    searchBar
  },
  data() {
    return {
      submitLoading: false,
      types: ["", "博文", "案例"],
      searchs: [
        {
          fields: "title",
          placeholder: "博文/案列标题",
          type: "text"
        },
        {
          fields: "shopName",
          placeholder: "店铺名称",
          type: "text"
        }
      ],
      pageData: { page: 1, limit: 10 },
      pageList: [],
      pageStatus: 0,
      carryDatas: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.API.getArticleANDProjectCaseList(this.pageData).then(res => {
        if (res.success) {
          this.pageList = res.datalist;
          this.pageData.total = res.totalCount;
        }
      });
    },
    carryArticles() {
      if (!this.carryDatas.length)
        this.$message({
          message: "请选择要搬运的文章！",
          type: "error"
        });
      else {
        if (!this.submitLoading) {
          this.submitLoading = true;
          this.API.transportArticles(this.carryDatas).then(res => {
            res.success ? this.getList() : "";
            this.$message({
              message: res.message,
              type: res.success ? "success" : "error"
            });
            this.submitLoading = false;
          });
        }
      }
    },
    handleTableCurrentChange(e) {
      this.carryDatas = e;
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
.avatar-uploader {
  text-align: left;
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 300px;
    height: 240px;
    line-height: 240px;
    text-align: center;
  }
  .avatar {
    width: 300px;
    height: auto;
    max-height: 800px;
    display: block;
  }
  .el-icon-plus {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    position: relative;
  }
  .el-icon-plus:hover {
    border-color: #409eff;
  }
  .el-upload {
    vertical-align: top;
  }
}
</style>
