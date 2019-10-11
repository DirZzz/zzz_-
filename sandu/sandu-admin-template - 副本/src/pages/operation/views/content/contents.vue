<template>
  <div class="table">
    <el-table
      :data="pageData.list"
      :header-cell-style="rowStyle"
      v-loading="loading"
      :ref="refTable"
    >
      <template v-if="$store.state.content.type == 'banner'">
        <el-table-column prop="name" label="banner名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column prop="url" min-width="150px" label="跳转路径" align="center">
          <template slot-scope="scope">{{ scope.row.url }}</template>
        </el-table-column>
        <el-table-column prop="bannerPicPath" label="banner图片" align="center">
          <template slot-scope="scope">
            <img
              :src="BASE_URL.sourceBaseUrl+scope.row.bannerPicPath"
              style="width:150px;height:80px"
            >
          </template>
        </el-table-column>
        <el-table-column prop="ordering" label="排序" align="center">
          <template slot-scope="scope">{{ scope.row.ordering||0 }}</template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">{{ scope.row.status==0?"关闭":"启用" }}</template>
        </el-table-column>
        <el-table-column prop="gmtModified" label="更新时间" align="center">
          <template slot-scope="scope">{{ scope.row.gmtModified }}</template>
        </el-table-column>
        <el-table-column prop="id" label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="togglePopups(scope.row)">编辑</el-button>
            <el-button type="text" v-if="scope.row.status==0" @click="open(scope.row.id)">启用</el-button>
            <el-button type="text" style="color:#c0c4cc" v-else @click="open(scope.row.id)">关闭</el-button>
            <el-button type="text" v-if="scope.row.status==0" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </template>
      <template v-else-if="$store.state.content.type == 'indexPopups'">
        <el-table-column prop="dialogName" label="弹窗名称" align="center">
          <template slot-scope="scope">{{ scope.row.dialogName }}</template>
        </el-table-column>
        <el-table-column prop="url" min-width="150px" label="弹窗链接" align="center">
          <template slot-scope="scope">{{ scope.row.url }}</template>
        </el-table-column>
        <el-table-column prop="picPath" label="弹窗图片" align="center">
          <template slot-scope="scope">
            <img :src="BASE_URL.sourceBaseUrl+scope.row.picPath" style="width:150px;height:80px">
          </template>
        </el-table-column>
        <el-table-column label="平台" align="center">
          <template slot-scope="scope">{{ scope.row.showInMiniProName }}</template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">{{ scope.row.isEnable==0?"关闭":"启用" }}</template>
        </el-table-column>
        <el-table-column prop="gmtModified" label="更新时间" align="center">
          <template slot-scope="scope">{{ scope.row.gmtModified }}</template>
        </el-table-column>
        <el-table-column prop="id" label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="togglePopups(scope.row)">编辑</el-button>
            <el-button type="text" v-if="scope.row.isEnable==0" @click="open(scope.row.id)">启用</el-button>
            <el-button type="text" v-else @click="open(scope.row.id)">关闭</el-button>
            <el-button type="text" v-if="scope.row.isEnable==0" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </template>
      <template v-else-if="$store.state.content.type == 'sharingActivities'">
        <el-table-column prop="bannerName" label="对应banner" align="center" key="1">
          <template slot-scope="scope">{{ scope.row.bannerName }}</template>
        </el-table-column>
        <el-table-column prop="shareText" label="分享文案" align="center" key="2">
          <template slot-scope="scope">{{ scope.row.shareText }}</template>
        </el-table-column>
        <el-table-column prop="picPath" label="分享图片" align="center" key="3">
          <template slot-scope="scope">
            <img :src="BASE_URL.sourceBaseUrl+scope.row.picPath" style="width:150px;height:80px">
          </template>
        </el-table-column>
        <el-table-column prop="gmtModified" label="更新时间" align="center" key="4">
          <template slot-scope="scope">{{ scope.row.gmtModified }}</template>
        </el-table-column>
        <el-table-column prop="id" label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="togglePopups(scope.row)">编辑</el-button>
            <el-button type="text" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </template>
    </el-table>
    <div class="tablePagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="pageData.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size.sync="pageData.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total.sync="pageData.total"
      ></el-pagination>
    </div>
  </div>
</template>
 
<script>
export default {
  data() {
    return {
      pageData: { page: 1, limit: 10, list: [] },
      loading: false,
      refTable: "refTable"
    };
  },
  watch: {
    $route: function(_new, _old) {
      this.pageData = { page: 1, limit: 10, list: [] };
      this.getList();
    },
    "$store.state.content.triggerGetBannerList": function(_new, _old) {
      this.getList();
    }
  },
  created() {
    this.getList();
  },
  methods: {
    // 打开/关闭弹窗
    togglePopups(data) {
      this.$store.commit("content_togglePopups");
      this.$store.commit("content_setIsAdd", false);
      data.picId =
        data.resBannerPicId ||
        data.dialogImageResPicId ||
        data.shareImgResPicId;
      this.$store.commit("content_setPopupData", Object.assign({}, data));
    },
    // 获取列表数据
    getList() {
      this.loading = true;
      let fData = new FormData();
      fData.append("positionCode", "xz:home:banner:interior");
      fData.append("page", this.pageData.page);
      fData.append("limit", this.pageData.limit);
      (() => {
        switch (this.$store.state.content.type) {
          case "banner":
            return this.API.getBannerList(fData);
          case "indexPopups":
            return this.API.getDialogList(fData);
          case "sharingActivities":
            return this.API.getShareList(fData);
        }
      })().then(res => {
        this.pageData.total = res.totalCount;
        this.pageData.list = res.datalist;
        this.loading = false;
      });
    },
    // 启用
    open(id) {
      this.loading = true;
      (() => {
        let fData = new FormData();
        switch (this.$store.state.content.type) {
          case "banner":
            fData.append("bannerId", id);
            return this.API.changeBanner(fData);
          case "indexPopups":
            fData.append("dialogId", id);
            return this.API.changeDialog(fData);
        }
      })().then(res => {
        this.$message({
          message: res.message,
          type: res.success ? "success" : "error"
        });
        this.loading = false;
        this.getList();
      });
    },
    // 删除
    remove(id) {
      this.$confirm("是否继续执行删除操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.loading = true;
        (() => {
          let fData = new FormData();
          switch (this.$store.state.content.type) {
            case "banner":
              fData.append("bannerId", id);
              return this.API.removeBanner(fData);
            case "indexPopups":
              fData.append("dialogId", id);
              return this.API.removeDialog(fData);
            case "sharingActivities":
              fData.append("id", id);
              return this.API.removeShare(fData);
          }
        })().then(res => {
          this.$message({
            message: res.message,
            type: res.success ? "success" : "error"
          });
          if (this.pageData.list.length == 1) --this.pageData.page;
          this.loading = false;
          this.getList();
        });
      });
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
.table {
  margin: 0 20px 20px;
  padding: 20px;
  background-color: #fff;
}
.tablePagination {
  padding-top: 20px;
  text-align: right;
}
</style>
