<template>
  <div class="purchasable-wrap">
    <div class="title-box">
      <div class="title">请选择购买的数据</div>
      <div v-if="tableData.length>0">每条数据单价{{unitPrice}}元/条</div>
    </div>
    <div class="table">
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        @select="select"
        @select-all="selectAll"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection"></el-table-column>
        <el-table-column label="地区">
          <template
            slot-scope="scope"
          >{{scope.row.provinceName}}{{scope.row.cityName}}{{scope.row.areaName}}</template>
        </el-table-column>
        <el-table-column width="200" label="门店">
          <template slot-scope="scope">
            <div class="desc">{{scope.row.shopName}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="count" label="可购买用户信息数量" show-overflow-tooltip></el-table-column>
        <el-table-column label="购买数量" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <div class="input-edit">
              <input
                type="number"
                placeholder="请输入"
                @change="numChange(scope.$index,scope.row.count,scope.row.num)"
                v-model="scope.row.num"
              />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="bottom-box">
      <div class="table-bottom-btn">
        <div>已选择{{selectLen}}条，共￥{{totalPrice}}</div>
        <el-button
          round
          type="primary"
          @click="buyUserMsg"
          :disabled="tableData.length==0?'disabled':false"
        >购买用户完整信息</el-button>
      </div>
      <div class="pagination-box">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5,10, 20, 30, 40,50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        ></el-pagination>
      </div>
    </div>
    <!-- 选择支付方式 -->
    <div class="dialog-box">
      <el-dialog
        title="选择支付方式"
        width="35%"
        :close-on-click-modal="false"
        center
        :visible.sync="payDialog"
      >
        <div class="dialog-content">
          <div class="total-box">
            <span class="label">应付金额：</span>
            <span class="price">￥{{totalPrice}}</span>
          </div>
          <div class="choose-box">
            <div>请选择支付方式：</div>
            <el-radio-group v-model="payType">
              <el-radio :label="2">
                <span>微信支付</span>
                <!-- <div>
                  <img src class="code" alt />
                  <div>
                    微信支付二维码（倒计时：
                    <span class="desc">56</span>s）
                  </div>
                </div>-->
              </el-radio>
              <el-radio :label="3">支付宝支付</el-radio>
              <el-radio :label="1">线下支付（请联系三度空间客服：029-189781616进行购买）</el-radio>
            </el-radio-group>
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" size="medium" @click="paySave" round>确定</el-button>
        </div>
      </el-dialog>
    </div>
    <!-- 支付 -->
    <div class="dialog-box">
      <el-dialog
        title="支付"
        width="30%"
        @close="closePaymentDialog"
        :close-on-click-modal="false"
        center
        :visible.sync="paymentDialog"
      >
        <div class="payment-dialog" v-if="qrCodeUrl">
          <div v-if="payType==2">请使用微信扫码支付</div>
          <div v-if="payType==3">请使用支付宝扫码支付</div>
          <span class="price">￥{{totalMoney}}</span>
          <qrcode-vue :value="qrCodeUrl" :size="size" level="H"></qrcode-vue>
          <!-- <img :src="qrCodeUrl" class="code" alt /> -->
        </div>
      </el-dialog>
    </div>

    <!-- 支付成功 -->
    <div class="dialog-box success-dialog">
      <el-dialog
        title="支付结果"
        width="35%"
        :close-on-click-modal="false"
        center
        :visible.sync="payResultDialog"
      >
        <div class="dialog-content">
          <img src="../../../assets/images/duihao.png" class="icon" alt />
          <div class="desc">支付成功！</div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button class="active-btn" size="medium" round @click="toPurchasable">查看用户信息</el-button>
        </div>
      </el-dialog>
    </div>
    <!-- 支付失败 -->
    <div class="dialog-box fail-dialog">
      <el-dialog
        title="支付失败"
        width="35%"
        :close-on-click-modal="false"
        center
        :visible.sync="payFailDialog"
      >
        <div class="dialog-content">
          <img src="../../../assets/images/cuowu_close.png" class="icon" alt />
          <div class="fail-text">
            <div>您未支付成功</div>
            <div>有疑问请联系客服：0919-1212312</div>
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button class="active-btn" size="medium" round>重新发起支付</el-button>
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
      pageName: "purchasable",
      size: 200,
      tableData: [],
      payDialog: false,
      payType: 1,
      payResultDialog: false,
      payFailDialog: false,
      paymentDialog: false,
      selectLen: 0,
      totalPrice: 0,
      unitPrice: 1,
      numSum: 0,
      multiSelection: [],
      buyDetail: [],
      qrCodeUrl: "",
      selectLen: 0,
      totalMoney: 0,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      selectionList: [],
      arr: [],
      orderNo: "",
      timer: null
    };
  },
  components: {
    QrcodeVue
  },
  created() {
    setTimeout(() => {
      this.getNotPayList();
    }, 200);
  },
  methods: {
    // 获取可购买数据列表
    getNotPayList() {
      this.API2.notPay({
        activityId: this.$route.query.id,
        limit: this.pageSize,
        start: this.currentPage
      }).then(res => {
        if (res.success) {
          let list = res.datalist;
          this.tableData = list;
          this.total = res.totalCount;
          if (list) this.unitPrice = list[0].price;
        }
      });
    },
    buyUserMsg() {
      let result = this.selectionList.every(item =>{console.log(item);return (item.shopId||item.shopId == 0) && item.num == true});
      console.log(result)
      if(!result){
         this.$message.warning('请输入要购买的数量！');
         return;
      }
      if (this.selectLen > 0) {
        this.payDialog = true;
      } else {
        this.$message.warning("请勾选要购买的数据！");
      }
      
    },
    // 选择支付方式前往支付
    paySave() {
      this.payDialog = false;
      if (this.payType != 1) {
        this.totalMoney = this.totalPrice;
      }
      this.buyDetail = this.selectionList.map(item => {
        return {
          shopId: item.shopId,
          count: parseInt(item.num)
        };
      });
      // 防止多次点击购买数据
      this.generateActivityOrder();
    },

    // 关闭支付扫码弹窗，断开socket链接。
    closePaymentDialog() {
      if (this.msg.orderStatus != "SUCCESS") {
        this.$message.warning("未支付成功，可前往交易记录里重新支付！");
        if (timer) {
          clearInterval(timer);
        }
      }
      this.getNotPayList();
      this.disconnect();
    },
    // 查看已购买用户信息
    toPurchasable() {
      this.$router.push({
        path: "/activity/purchased",
        query: {
          id: this.$route.query.id,
          tabValue: "purchased"
        }
      });
    },
    // 生成订单
    generateActivityOrder() {
      let params = {
        activityId: this.$route.query.id,
        companyId: this.$route.query.companyId,
        payType: this.payType,
        payCount: this.numSum,
        totalMoney: this.totalPrice,
        payMoney: this.totalPrice,
        details: this.buyDetail,
        price: this.unitPrice
      };
      this.API2.generateActivityOrder(params).then(res => {
        if (res.success) {
          this.getNotPayList();
          let qrCode = JSON.parse(res.obj.payResponse);
          this.qrCodeUrl = qrCode.qrCodeUrl;
          this.orderNo = res.obj.orderNo; // 订单编号
          try {
            if (this.payType != 1) {
              this.paymentDialog = true;
              try {
                this.initWebSocket();
              } catch (err) {
                this.getPayStatus();
              }
            }
          } catch (err) {
            // 报错情况下：
            if (qrCode.status) {
              if (this.payType != 1) {
                this.paymentDialog = true;
              } else {
                this.$message.success("操作成功，可在交易记录中查看订单详情");
              }
            } else {
              this.$message.error(qrCode.message);
            }
          }
        } else {
          this.$message.error(res.message);
          this.getNotPayList();
        }
      });
    },
    // 轮询搜索订单支付状态（备用）
    getPayStatus() {
      timer = setInterval(() => {
        this.API2.getPayStatus({ orderNo: this.orderNo }).then(res => {
          console.log(res);
          if (res.orderStatus == "SUCCESS") {
            clearInterval(timer);
            this.paymentDialog = false;
            this.payResultDialog = true;
          }
          if (res.orderStatus == "FAILED") {
            clearInterval(timer);
            this.paymentDialog = false;
            this.payFailDialog = true;
          }
        });
      }, 5000);
    },
    // 单个勾选
    select(selection, row) {
      if (!row.num && selection.length != 0) {
        row.num = row.count;
      }
      let result = this.selectionList.some(item => {
        if (item.shopId == row.shopId) {
          return true;
        }
      });
      if (result) {
        this.selectionList.forEach((item, index) => {
          if (item.shopId == row.shopId) {
            this.selectionList.splice(index, 1);
          }
        });
      } else {
        this.selectionList.push(row);
      }
    },
    // 勾选全部
    selectAll(selection) {
      if (selection.length > 0) {
        selection.forEach(item => {
          if (!item.num) {
            item.num = parseInt(item.count);
          }
        });
        let arr = [];
        Object.assign(arr, this.selectionList);
        let idArr = arr.map(item => {
          return item.shopId;
        });
        selection.forEach((item, index) => {
          if (!idArr.includes(item.shopId)) {
            this.selectionList.push(item);
          }
        });
      } else {
        this.selectionList = [];
      }
      console.log("this.selectionList", this.selectionList);
    },
    handleSelectionChange(selection) {},
    // 表格里输入的值发生变化
    numChange(index, max, value) {
      value = parseInt(value);

      // 如果输入的数字为0，则强制转换
      if (value == 0) {
        this.tableData[index].num = 1;
      }
      let shopId = this.tableData[index].shopId;
      if (value > max) {
        this.$message.warning(`输入的购买数量不可大于${max}！`);
        value = max;
        this.tableData[index].num = max;
      }
      console.log(max, value);
      this.$set(this.tableData, index, this.tableData[index]);
      // 输入框改变数值后，总价发生变化
      this.numSum = 0;
      this.selectionList.forEach(item => {
        if (item.shopId == shopId) {
          item.num = value;
        }
        this.numSum += parseInt(item.num);
      });
      this.totalPrice = this.numSum * this.unitPrice;
      this.totalPrice = this.totalPrice.toFixed(2);
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getNotPayList();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getNotPayList();
    }
  },
  watch: {
    // 监听所勾选的数据，计算总价及勾选条数。
    selectionList() {
      this.selectLen = this.selectionList.length;
      this.numSum = 0;
      this.selectionList.forEach(item => {
        this.numSum += parseInt(item.num);
      });
      this.totalPrice = this.numSum * this.unitPrice;
      this.totalPrice = this.totalPrice.toFixed(2);
    }
  },
  updated() {
    // 回显所勾选数据
    this.$nextTick(() => {
      this.selectionList.forEach(item => {
        this.tableData.forEach((tItem, index) => {
          if (item.shopId == tItem.shopId) {
            tItem.num = item.num;
            this.$refs.multipleTable.toggleRowSelection(tItem, true);
          }
        });
      });
    });
  }
};
</script>
<style lang='scss' scoped>
.purchasable-wrap {
  background: #fff;
  min-height: 700px;
  .title-box {
    padding: 0 20px;
    color: #333333;
    .title {
      padding: 20px 0;
      font-size: 16px;
    }
  }
  .table {
    border: solid 2px #e0e0e0;
  }
  .el-radio {
    padding: 15px 30px;
    display: block;
  }
  .code {
    width: 200px;
    height: 200px;
    display: block;
    object-fit: contain;
    margin-bottom: 20px;
  }
  .desc {
    color: #f56200;
    text-align: center;
  }
  .fail-text {
    font-size: #666;
    text-align: center;
    padding-top: 10px;
  }
  .active-btn {
    background-color: #fef0e7;
    border: solid 1px #f56200;
    color: #f56200;
  }
  .el-dialog--center .el-dialog__body {
    padding: 0;
  }
  .choose-box {
    padding: 20px;
  }
  .dialog-content {
    .icon {
      margin: auto;
      display: list-item;
    }
  }
  .total-box {
    font-size: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #666666;
    padding-bottom: 15px;
    border-bottom: 1px solid #eee;
    .price {
      color: #f56200;
      font-size: 24px;
      font-weight: bold;
    }
  }
  .payment-dialog {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .price {
      color: #ff6419;
      font-size: 24px;
      font-weight: bold;
      margin: 20px 0;
    }
  }
  .table-bottom-btn {
    display: flex;
    align-items: center;
    padding: 0 30px;
    button {
      margin-left: 15px;
    }
  }
}
</style>
<style lang="scss">
.purchasable-wrap {
  .input-edit {
    input {
      width: 80px;
      height: 36px;
      line-height: 36px;
      text-align: center;
      border-radius: 5px;
    }
    .el-input {
      width: 80px;
    }
    .el-input__inner {
      border: none !important;
      text-align: center;
    }
  }
  .bottom-box {
    display: flex;
    justify-content: space-between;
    padding: 30px;
  }
  .fail-dialog {
    .el-dialog--center .el-dialog__body {
      padding: 21px 30px;
    }
  }
}
</style>
