<template>
  <div class="orderlist">
    <div class="main-body">
      <div class="title">购买详情</div>
      <div class="table">
        <div class="table-label">购买数据</div>
        <el-table v-loading="loading" border :data="tableData" stripe style="width: 100%">
          <el-table-column prop="nickName" label="用户昵称" width="180" align="center"></el-table-column>
          <el-table-column prop="mobile" label="手机号"  align="center"></el-table-column>
          <el-table-column prop="address" label="地区" width="200" align="center">
            <template slot-scope="scope">
              {{scope.row.provinceName}}{{scope.row.cityName}}{{scope.row.areaName}}
            </template>
          </el-table-column>
          <el-table-column prop="brandName" label="品牌" align="center"></el-table-column>
          <el-table-column prop="shopName" label="报名门店" align="center"></el-table-column>
          <el-table-column prop="gmtCreate"  label="预约时间" align="center"></el-table-column>
        </el-table>
      </div>
      <div class="pagenation-box">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.currentPage"
          :page-sizes="[10, 20, 30, 40,50]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      tableData: [],
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      }
    };
  },
  components: {},
  created() {
    this.getOpActivityOrderDetail();
  },
  methods: {
    getOpActivityOrderDetail() {
      let params = {
      orderId: this.$route.query.orderId,
      limit: this.page.pageSize,
      start: this.page.currentPage
    };
      this.API.opActivityOrderDetail(params).then(res => {
        console.log(res);
        if (res.success) {
          this.loading = false;
          this.page.total = res.totalCount;
          this.tableData = res.datalist;
        }
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getOpActivityOrderDetail();
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
     this.getOpActivityOrderDetail();
    }
  }
};
</script>
<style lang='scss' scoped>
.orderlist {
  padding: 30px;
  .main-body {
    min-height: 860px;
    background: #fff;
    .title {
      font-size: 18px;
      color: #222;
      padding: 15px 30px;
      text-align: left;
      border-bottom: 1px solid #eee;
    }
    .table {
      padding: 0 30px;
      .table-label {
        padding: 30px 0;
        text-align: left;
      }
    }
    .pagenation-box{
      margin-top: 30px;
    }
  }
}
</style>