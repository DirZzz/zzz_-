<template>
  <div class="orderlist">
    <div class="main-body">
      <div class="title">订单列表</div>
      <div class="order-select">
        <div class="select-title">数据筛选</div>
        <div class="select-content">
          <div class="select-box">
            <div class="select-item">
              <div class="label">支付状态：</div>
              <el-select v-model="payStatus" clearable filterable placeholder="请选择支付状态">
                <el-option label="未支付" :value="0">未支付</el-option>
                <el-option label="已支付" :value="1">已支付</el-option>
                <el-option label="已关闭" :value="2">已关闭</el-option>
                <el-option label="支付失败" :value="3">支付失败</el-option>
              </el-select>
            </div>
            <div class="select-item">
              <div class="label">下单用户：</div>
              <el-input clearable v-model="companyName" placeholder="请输入下单用户"></el-input>
            </div>

            <div class="select-item">
              <div class="label">下单时间：</div>
              <el-date-picker
                clearable
                v-model="date"
                type="datetimerange"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="dateChange"
              ></el-date-picker>
            </div>
          </div>
          <el-button type="primary" size="medium" @click="searchWordList">查询</el-button>
          <el-button type="primary" size="medium" @click="resetList" style="margin-left:15px;">重置</el-button>
        </div>
      </div>
      <div class="order-table">
        <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%">
          <el-table-column prop="orderCode" label="订单编号" align="center" width="180"></el-table-column>
          <el-table-column prop="companyName" label="下单用户" align="center" width="250">
            <template slot-scope="scope">
              <div>{{scope.row.companyName?scope.row.companyName:'匿名用户'}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="payCount" label="购买数量" align="center"></el-table-column>
          <el-table-column prop="price" label="单价" align="center"></el-table-column>
          <el-table-column prop="totalMoney" label="总金额" align="center"></el-table-column>
          <el-table-column label="支付状态" align="center">
            <template slot-scope="scope" align="center">
              <div v-if="scope.row.payStatus==0">未支付</div>
              <div v-if="scope.row.payStatus==1">已支付</div>
              <div v-if="scope.row.payStatus==2">已关闭</div>
              <div v-if="scope.row.payStatus==3">支付失败</div>
            </template>
          </el-table-column>
          <el-table-column prop="payType" label="支付方式" align="center">
            <template slot-scope="scope" align="center">
              <div v-if="scope.row.payType==1">线下支付</div>
              <div v-if="scope.row.payType==2">线上支付--微信支付</div>
              <div v-if="scope.row.payType==3">线上支付-支付宝支付</div>
            </template>
          </el-table-column>
          <el-table-column prop="payMoney" label="实付金额" align="center"></el-table-column>
          <el-table-column prop="gmtCreate" label="下单时间" width="200" align="center"></el-table-column>
          <el-table-column label="操作" align="center" width="180">
            <template slot-scope="scope">
              <el-button type="text" v-if="scope.row.payStatus == 0" @click="payment(scope.$index, scope.row)">置为已付款</el-button>
              <el-button type="text" @click="orderDetail(scope.$index, scope.row)">详情</el-button>
            </template>
          </el-table-column>
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
    <!-- 置为已付款弹窗 -->
    <div class="dialog-box">
      <el-dialog
        title="置为已付款弹窗"
        :close-on-click-modal="false"
        width="30%"
        center
        :visible.sync="paymentDialog"
      >
        <el-form :inline="true">
          <el-form-item label="支付方式：">
            <el-select v-model="payType" clearable filterable placeholder="请选择支付方式">
              <el-option label="线下支付" :value="1">线下支付</el-option>
              <el-option label="线上支付--微信支付" :value="2">线上支付--微信支付</el-option>
              <el-option label="线上支付--支付宝支付" :value="3">线上支付--支付宝支付</el-option>
            </el-select>
          </el-form-item>
          <el-row>
            <el-form-item label="支付时间：">
              <div class="pay-time">{{nowTime}}</div>
            </el-form-item>
          </el-row>
          <el-form-item label="支付金额：" style="margin-left: -15px;">
            <el-input v-model="payMoney" type="number" min="0.01"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelPayment">取 消</el-button>
          <el-button type="primary" @click="paymentSave">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      companyName:null,
      payStatus: null,
      date:null,
      startTime: null,
      endTime: null,
      paymentDialog: false,
      time: null,
      pickerOptions: {
        // disabledDate(time) {
        //   return time.getTime() <= new Date().getTime() - 1000 * 3600 * 24;
        // }
      },
      tableData: [],
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      payType: 1,
      payMoney: null,
      orderId: null // 用户点击置为已付款对应的行id
    };
  },
  created() {
    this.getOrderList({
      limit: this.page.pageSize,
      start: this.page.currentPage
    });
  },
  watch: {
    payMoney(newVal, oldVal) {
      if (this.payMoney % 1 != 0) {
        setTimeout(() => {
          this.payMoney = parseFloat(this.payMoney).toFixed(2);
        }, 500);
      }
    }
  },
  components: {},
  methods: {
    getOrderList(params) {
      this.API.activityOrderList(params).then(res => {
        if (res.success) {
          this.loading = false;
          this.page.total = res.totalCount;
          this.tableData = res.datalist;
        }
      });
    },
    searchWordList() {
      if(!(this.companyName&&this.startTime&&this.endTime)&&this.payStatus==''){
        this.page.currentPage = 1;
      }
      this.searchList();
    },
    searchList(){
      let params = {
        limit: this.page.pageSize,
        start: this.page.currentPage,
        payStatus: this.payStatus != null ? this.payStatus : null,
        companyName: this.companyName ? this.companyName : null,
        startTime: this.startTime ? this.startTime : null,
        endTime: this.endTime ? this.endTime : null
      };
     
      this.getOrderList(params);
    },
    resetList(){
      this.payStatus = null;
      this.date = null;
      this.startTime = null;
      this.endTime = null;
      this.companyName = null;
      this.page.currentPage = 1;
      this.getOrderList({
        limit: this.page.pageSize,
        start: this.page.currentPage
      });
    },
    dateChange(date) {
      if (date) {
        this.startTime = date[0];
        this.endTime = date[1];
      } else {
        this.startTime = "";
        this.endTime = "";
      }
    },
    payment(index, row) {
      this.paymentDialog = true;
      this.orderId = row.id;
    },
    cancelPayment(){
        this.paymentDialog = false;
        this.payMoney = '';
    },
    orderDetail(index, row) {
      this.$router.push({
        path: "/offlineActivity/orderDetail",
        query: { orderId: row.id }
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
     this.searchList();
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.searchList();
    },
    paymentSave() {
      if(!this.payMoney||this.payMoney<=0){
          this.$message.warning("请输入支付金额");
          return;
      }
      if(!this.payType){
          this.$message.warning("请选择支付方式");
          return;
      }
      if (this.payMoney && this.payType) {
        let params = {
          orderId: this.orderId,
          payMoney: this.payMoney,
          payType: this.payType
        };
        this.API.activityOrderSetPay(params).then(res => {
          if (res.success) {
            this.paymentDialog = false;
            this.$message.success("置为已付款成功");
            this.getOrderList({
              limit: this.page.pageSize,
              start: this.page.currentPage
            });
          }
        });
      }
    }
  },
  computed: {
    nowTime() {
      var date = new Date();
      var seperator1 = "-";
      var seperator2 = ":";
      var month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      var strDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var currentdate =
        date.getFullYear() +
        seperator1 +
        month +
        seperator1 +
        strDate +
        " " +
        date.getHours() +
        seperator2 +
        date.getMinutes();
      this.time = currentdate;
      return currentdate;
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
    .order-select {
      padding: 30px;
      .select-title {
        padding: 10px;
        background: #f8f8f8;
        color: #888;
        font-size: 16px;
        text-align: left;
        border-bottom: 1px solid #eee;
      }
      .select-content {
        padding: 30px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border: 1px solid #eee;
        .select-box {
          width: 90%;
          display: flex;
          overflow: hidden;
          .select-item {
            display: flex;
            align-items: center;
            margin-left: 30px;
            &:first-child {
              margin-left: 0;
            }
            .label {
              width: 100px;
              text-align: left;
            }
            .el-input {
              width: 150px;
            }
            .el-select {
              width: 150px;
            }
          }
        }
      }
    }
    .order-table {
      padding: 30px;
    }
    .pagenation-box {
      margin-top: 30px;
    }
  }
  .dialog-box {
    form {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      .pay-time {
        width: 218px;
      }
    }
  }
}
</style>