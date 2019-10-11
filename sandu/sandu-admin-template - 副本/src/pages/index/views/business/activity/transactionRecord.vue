<template>
  <div class="transactionRecord-wrap">
    <el-form :inline="true" class="demo-form-inline">
      <el-row class="select-box">
        <el-row class="select-left-box">
          <el-row class="select-left">
            <el-form-item>
              <el-select clearable v-model="payStatus" placeholder="请选择支付状态">
                <el-option label="未支付" :value="0">未支付</el-option>
                <el-option label="已支付" :value="1">已支付</el-option>
                <el-option label="已关闭" :value="2">已关闭</el-option>
                <el-option label="支付失败" :value="3">支付失败</el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input v-model="companyName" clearable placeholder="请输入下单用户"></el-input>
            </el-form-item>
            <el-form-item class="creat-time-box">
              <el-date-picker
                v-model="createTimer"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                @change="dateChange"
              ></el-date-picker>
            </el-form-item>
          </el-row>
        </el-row>
        <div class="option-btn-box">
          <el-button
            round
            type="primary"
            style="margin-left:20px;"
            class="search-btn"
            @click="getSearchList"
          >搜索</el-button>
          <el-button round plain  style="margin-left:20px;" @click="resetList">重置</el-button>
        </div>
      </el-row>
    </el-form>
    <div class="table-wrap">
      <div class="table">
        <el-table v-loading="loading" :data="tableData" style="width: 100%">
          <el-table-column prop="orderCode" label="订单编号" align="center" width="180"></el-table-column>
          <el-table-column prop="companyName" label="下单用户" align="center" width="250">
            <template slot-scope="scope">
              <div class="companyName">{{scope.row.companyName?scope.row.companyName:'匿名用户'}}</div>
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
          <el-table-column label="操作" align="center" header-align="center" width="200">
            <template slot-scope="scope">
              <div class="option-btn">
                <el-button
                  type="text"
                  class="text-btn"
                  v-if="scope.row.payStatus==0&&scope.row.payType!=1"
                  @click="repay(scope.$index, scope.row)"
                >重新支付</el-button>
                <el-button
                  type="text"
                  class="text-btn"
                  v-if="scope.row.payStatus==0"
                  @click="cancelOrder(scope.$index, scope.row)"
                >取消订单</el-button>
                <el-button
                  class="text-btn"
                  type="text"
                  @click="orderDetail(scope.$index, scope.row)"
                >详情</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
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
      <div class="dialog-box">
        <el-dialog
          title="支付"
          width="30%"
          @close="closePaymentDialog"
          :close-on-click-modal="false"
          center
          :visible.sync="paymentDialog"
        >
          <div class="payment-dialog">
            <div v-if="payType==2">请使用微信扫码支付</div>
            <div v-if="payType==3">请使用支付宝扫码支付</div>
            <span class="price">￥{{totalMoney}}</span>
            <qrcode-vue :value="qrCodeUrl" :size="size" level="H"></qrcode-vue>
          </div>
        </el-dialog>
      </div>
      <!-- 详情 -->
      <el-dialog title="购买详情" :close-on-click-modal="false" center :visible.sync="detailDialog">
        <div class="detail-box">
          <div class="detail-table">
            <el-table :data="detailList" style="width: 100%">
              <el-table-column label="地区" width="180">
                <template slot-scope="scope">
                  <div>{{scope.row.provinceName}}{{scope.row.cityName}}{{scope.row.areaName}}</div>
                </template>
              </el-table-column>
              <el-table-column prop="shopName" label="门店" width="250"></el-table-column>
              <el-table-column prop="notPayNum" width="200" label="可购买用户信息数量"></el-table-column>
              <el-table-column prop="count" label="购买数量"></el-table-column>
            </el-table>
          </div>
          <div class="pagenation-box">
            <el-pagination
              @size-change="detailSizeChange"
              @current-change="detailCurrentChange"
              :current-page="detailPage.currentPage"
              :page-sizes="[10, 20, 30, 40,50]"
              :page-size="detailPage.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="detailPage.total"
            ></el-pagination>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import QrcodeVue from "qrcode.vue";
import socketMixins from "../../../filters/socket.js";
var timer = null;
export default {
  mixins: [socketMixins],
  data() {
    return {
      pageName: "transactionRecord",
      loading: true,
      tableData: [],
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      detailList: [],
      detailDialog: false,
      detailPage: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      paymentDialog: false,
      payType: 1,
      totalMoney: 0,
      qrCodeUrl: "",
      size: 200,
      payFlag: true,
      companyName: "",
      payStatus: null,
      createTimer: "",
      orderCode: ""
    };
  },

  created() {
    this.getOrderList();
  },
  components: {
    QrcodeVue
  },
  methods: {
    getOrderList() {
      let params = {
        activityId: this.$route.query.id,
        limit: this.page.pageSize,
        start: this.page.currentPage
      };
      this.API2.activityOrderList(params).then(res => {
        console.log(res);
        if (res.success) {
          this.loading = false;
          this.page.total = res.totalCount;
          this.tableData = res.datalist;
        }
      });
    },
    getSearchList() {
      let params = {
        activityId: this.$route.query.id,
        start: 1,
        limit: this.page.pageSize,
        payStatus: this.payStatus != null ? this.payStatus : null,
        companyName: this.companyName ? this.companyName : null,
        startTime: this.createTimer ? this.createTimer[0] : null,
        endTime: this.createTimer ? this.createTimer[1] : null
      };
      this.API2.activityOrderList(params).then(res => {
        if (res.success) {
          this.loading = false;
          this.page.total = res.totalCount;
          this.tableData = res.datalist;
        }
      });
    },
    resetList() {
      this.payStatus = null;
      this.companyName = "";
      this.createTimer = [];
      this.page.currentPage = 1;
      this.getOrderList();
    },
    // 时间日期发生变化
    dateChange(date) {
      console.log(date);
    },
    handleClick() {},
    repay(index, row) {
      this.paymentDialog = true;
      this.qrCodeUrl = row.qrCodeUrl;
      this.totalMoney = row.totalMoney;
      this.payType = row.payType;
      this.orderCode = row.orderCode;
      try {
        this.initWebSocket();
      } catch (err) {
        this.getPayStatus();
      }
    },
    closePaymentDialog() {
      this.disconnect();
      if (timer) {
        clearInterval(timer);
      }
    },
    orderDetail(index, row) {
      this.detailDialog = true;
      this.merchantActivityOrderDetail(row.id);
    },
    // 轮询搜索订单支付状态（备用）
    getPayStatus() {
      timer = setInterval(() => {
        this.API2.getPayStatus({ orderNo: this.orderCode }).then(res => {
          console.log(res);
          if (res.orderStatus == "SUCCESS") {
            clearInterval(timer);
            this.paymentDialog = false;
            this.$message.success("支付成功！");
          }
          if (res.orderStatus == "FAILED") {
            clearInterval(timer);
            this.paymentDialog = false;
            this.$message.warning("支付失败！");
          }
        });
      }, 5000);
    },
    merchantActivityOrderDetail(id) {
      let params = {
        orderId: id,
        limit: this.detailPage.pageSize,
        start: this.detailPage.currentPage
      };
      this.API2.merchantActivityOrderDetail(params).then(res => {
        console.log(res);
        if (res.success) {
          this.detailList = res.datalist;
          this.detailPage.total = res.totalCount;
        }
      });
    },
    cancelOrder(index, row) {
      this.$confirm(`是否取消该订单？`, "取消订单", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.API2.cancelActivityOrder({ orderId: row.id }).then(res => {
          if (res.success) {
            this.$message.success("取消订单成功");
            this.getOrderList();
          }
        });
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getOrderList();
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.getOrderList();
    },
    detailSizeChange() {
      this.detailPage.pageSize = val;
      this.merchantActivityOrderDetail();
    },
    detailCurrentChange(val) {
      this.detailPage.currentPage = val;
      this.merchantActivityOrderDetail();
    }
  }
};
</script>
<style lang='scss' scoped>
.transactionRecord-content {
  .table-wrap {
    min-height: 700px;
    .text-btn {
      width: 80px;
    }
    .table {
      border: solid 2px #e0e0e0;
      margin-top: 20px;
      background: #fff;

      .pagenation-box {
        margin-top: 23px;
      }
      .companyName {
        color: #f56200;
      }
      .pagenation-box {
        display: flex;
        justify-content: center;
        padding-bottom: 23px;
      }
    }
    .detail-box {
      .pagenation-box {
        margin-top: 30px;
        display: flex;
        justify-content: center;
      }
    }
  }
  .detail-table {
    border: 1px solid #eee;
  }
  .payment-dialog {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .price {
      color: #ff6419;
      font-size: 24px;
      margin: 20px 0;
      font-weight: bold;
    }
  }
  .select-box {
    display: flex;
    flex-direction: column;
    background: #fff;
    .el-form-item {
      display: flex;
    }
    .option-btn-box {
      padding: 20px 42px;
      justify-content: center;
      display: flex;
      .is-round {
        padding: 10px 35px;
      }
    }
    .select-left-box {
      border-bottom: 1px solid #ccc;

      .select-left {
        display: flex;
        padding: 10px 30px 0px 30px;
      }
      .date-box {
        display: flex;
      }
    }
    // .el-input,.el-select{
    //   width: 150px;
    // }
    //  .el-input--prefix .el-input__inner{
    //    width: 200px;
    //  }
    .line {
      width: 50px;
      text-align: center;
    }
  }
}
</style>