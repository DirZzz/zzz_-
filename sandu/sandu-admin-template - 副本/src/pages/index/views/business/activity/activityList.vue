<template>
  <div class="activityList">
    <div class="activityList-header">
      <h1>活动列表</h1>
      <div class="activityList-content">
        <div class="table">
          <el-table stripe :data="tableData" style="width: 100%">
            <el-table-column  label="活动名称" width="180">
               <template slot-scope="scope">
                 <div class="activity-name">{{scope.row.activityName}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="活动图片">
              <template slot-scope="scope">
                 <img class="coverPic" :src="sourceBaseUrl+scope.row.coverPicId"  v-if="scope.row.coverPicId" alt />
                 <div v-else>--</div>
              </template>
            </el-table-column>
            <el-table-column prop="brandName" align="center" label="参加活动品牌"></el-table-column>
            <el-table-column label="活动有效期" align="center" width="180">
              <template slot-scope="scope">
                <div>{{scope.row.startTime}} - {{scope.row.endTime}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="gmtCreate" align="center" label="添加活动时间"></el-table-column>
            <el-table-column prop="trueNum" label="已报名人数"></el-table-column>
            <el-table-column align="center" label="可购买/总数">
              <template slot-scope="scope">
                <div v-if="scope.row.trueNum != 0">{{scope.row.notPayNum}}/{{scope.row.trueNum}}</div>
                <div v-else>0</div>
              </template>
            </el-table-column>
                <el-table-column prop="status" align="center" label="活动状态"></el-table-column>
            <el-table-column  label="操作" align="center" width="280">
              <template slot-scope="scope">
                <div class="option-btn">
                  <span @click="tabOption(scope.$index,scope.row,'purchasable')">购买用户信息</span>
                  <span @click="tabOption(scope.$index,scope.row,'purchased')">用户信息</span>
                  <span @click="tabOption(scope.$index,scope.row,'transactionRecord')">交易记录</span>
                  <span @click="handleDelete(scope.$index,scope.row)" v-if="distributor">删除</span>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <div class="navigation-box">
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
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      sourceBaseUrl:'',
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      distributor:true
    };
  },
  created() {
    this.getMerchantActivityList();
    this.judgeUserType();
     let basePath = process.env;
    this.sourceBaseUrl = basePath.sourceBaseUrl;
  },
  components: {},
  methods: {
    // 判断是否为经销商，经销商不可以删除活动
    judgeUserType(){
      this.API2.judgeUserType().then(res=>this.distributor = res.success)
    },
    getMerchantActivityList() {
      let params = {
        limit: this.page.pageSize,
        start: this.page.currentPage
      };
      this.API2.getMerchantActivityList(params).then(res => {
        if (res.success) {
           res.datalist.forEach(item => {
            item.startTime = item.startTime.split(" ")[0].replace(/-/g,'.');
            item.endTime = item.endTime.split(" ")[0].replace(/-/g,'.');
            item.gmtCreate = item.gmtCreate.split(" ")[0].replace(/-/g,'.');
          });
          this.tableData = res.datalist;
          this.page.total = res.totalCount;
        }
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getMerchantActivityList();
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.getMerchantActivityList();
    },
    handleDelete(index,row){
      let params = {
        id: row.id
      };
      this.$confirm(`是否确认删除，删除后参与活动的用户信息不可恢复`, "删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.API2.deleteActivity(params).then(res => {
          if (res.success) {
            this.$message.success('删除成功');
            this.getMerchantActivityList();
          }
        });
      });
    },
    tabOption(index,row,value){
      localStorage.setItem('id',row.id);
      localStorage.setItem('tabVale',value);
      localStorage.setItem('companyId',row.companyId);
      this.$router.push('/activity/tab');
    }
  }
};
</script>
<style lang='scss' scoped>
.activityList {
  &-header {
    width: 100%;
    background-color: #ffffff;
    h1 {
      width: 100%;
      height: 58px;
      font-size: 18px;
      line-height: 58px;
      padding-left: 20px;
      box-sizing: border-box;
      border-bottom: 1px solid #dddddd;
    }
    .activityList-content {
      background: #fff;
      min-height: 860px;
      .table {
        .activity-name{
          color:#f56200
        }
        .option-btn {
          color: #f56200;
          font-size: 12px;
          padding-left: 40px;
          span {
            width: 90px;
            text-align: left;
            display: inline-block;
            cursor: pointer;
          }
        }
      }
      .navigation-box {
        display: flex;
        justify-content: center;
        margin-top: 30px;
      }
    }
    .coverPic{
      object-fit: cover;
      width: 80px;
      height: 80px;
    }
  }
}
</style>