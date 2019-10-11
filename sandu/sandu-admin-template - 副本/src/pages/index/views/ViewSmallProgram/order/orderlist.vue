<template>
  <div>
    <el-tabs v-model="activeName" class="searchBg">
      <el-tab-pane label="订单汇总" disabled name="first">配置管理</el-tab-pane>
      <el-tab-pane label="订单列表" name="second">
        <div style="margin-top: 25px">
          <el-form class="orderListSearch">
            <el-row :gutter="24">
              <el-col :span="3">
                <el-input placeholder="请输入订单号" maxlength="30" clearable v-model="orderNumber"></el-input>
              </el-col>
              <el-col :span="3">
                <el-input placeholder="请输入收件人名称" maxlength="10" clearable v-model="consignee"></el-input>
              </el-col>
              <el-col :span="3">
                <el-select
                  v-model="orderSourceValue"
                  clearable
                  @change="getOrderList"
                  placeholder="请选择订单来源"
                >
                  <el-option
                    v-for="item in orderSource"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-col>
              <el-col :span="3">
                <el-input v-model="allotShopValue" clearable placeholder="请输入分配的店铺"></el-input>
              </el-col>
              <el-col :span="3">
                <el-select size="medium" v-model="orderState" placeholder="订单状态" :clearable="true">
                  <el-option label="待付款" :value="0"></el-option>
                  <el-option label="待确认" :value="7"></el-option>
                  <el-option label="已确认" :value="1"></el-option>
                  <el-option label="已取消" :value="2"></el-option>
                  <el-option label="交易完成" :value="4"></el-option>
                  <el-option label="待成团" :value="6"></el-option>
                  <el-option label="结算中" :value="10"></el-option>
                  <el-option label="已结算" :value="11"></el-option>
                </el-select>
              </el-col>
              <el-col :span="3">
                <el-select
                  size="medium"
                  v-model="shippingState"
                  placeholder="商品配送情况"
                  :clearable="true"
                >
                  <el-option label="未发货" :value="0"></el-option>
                  <el-option label="已发货" :value="1"></el-option>
                  <el-option label="已收货" :value="2"></el-option>
                </el-select>
              </el-col>
              <el-col :span="3">
                <el-select size="medium" v-model="payState" placeholder="支付状态" :clearable="true">
                  <el-option label="待支付" :value="0"></el-option>
                  <el-option label="已支付" :value="2"></el-option>
                </el-select>
              </el-col>
              <el-col :span="3">
                <el-select size="medium" v-model="orderType" placeholder="订单类型" :clearable="true">
                  <el-option label="普通订单" :value="0"></el-option>
                  <el-option label="拼团订单" :value="1"></el-option>
                </el-select>
              </el-col>
            </el-row>
            <el-row style="margin-top:10px">
              <el-col :span="3">
                <el-select
                  size="medium"
                  v-model="shippingType"
                  placeholder="发货类型"
                  :clearable="true"
                >
                  <el-option label="快递" :value="1"></el-option>
                  <el-option label="上门自提" :value="2"></el-option>
                </el-select>
              </el-col>
            </el-row>
            <el-row :gutter="24" class="buttonSplitLine">
              <el-button
                round
                type="primary"
                class="searchBtn grey UnifiedsearchBtn"
                @click="search"
              >搜索</el-button>
              <el-button round class="searchBtn grey reset UnifiedsearchBtn" @click="resetList">重置</el-button>
            </el-row>
          </el-form>
          <el-button
            round
            type="primary"
            class="addProBtn"
            plain
            @click.stop="exportCustomerData"
          >导出</el-button>
        </div>
        <div class="order">
          <div>
            <el-table
              :data="tableData"
              v-loading="loading"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(255, 255, 255, 0.8)"
              style="width: 100%"
            >
              <el-table-column type="index" label="序号" width="60"></el-table-column>
              <el-table-column prop="orderCode" label="订单编号" width="180"></el-table-column>
              <el-table-column prop="orderStatus" label="订单状态" width="80">
                <!--订单状态(1:代付款,2:待确认,3:代发货,4:代签收,5:交易完成 ) ,-->
                <template slot-scope="scope">
                  <span v-if="scope.row.orderStatus === 0 && scope.row.payStatus !=2 ">待付款</span>
                  <span v-if="scope.row.orderStatus === 0 && scope.row.payStatus === 2">待确认</span>
                  <span v-if="scope.row.orderStatus === 4">交易完成</span>
                  <span v-if="scope.row.orderStatus === 6">待成团</span>
                  <span v-if="scope.row.orderStatus === 2">已取消</span>
                  <span v-if="scope.row.orderStatus === 10">结算中</span>
                  <span v-if="scope.row.orderStatus === 11">已结算</span>
                  <span
                    v-if="scope.row.orderStatus === 1 && scope.row.shippingStatus === 0 && scope.row.payStatus === 2"
                  >待发货</span>
                  <span
                    v-if="scope.row.orderStatus === 1 && scope.row.shippingStatus === 1 && scope.row.payStatus === 2"
                  >待签收</span>
                </template>
              </el-table-column>
              <el-table-column prop="shippingStatus" label="商品配送情况" width="150">
                <!--商品配送情况(0:未发货,1:已发货,2:已签收,4:退货)-->
                <template slot-scope="scope">
                  <span v-if="scope.row.shippingStatus === 0">未发货</span>
                  <span v-if="scope.row.shippingStatus === 1">已发货</span>
                  <span v-if="scope.row.shippingStatus === 2">已签收</span>
                </template>
              </el-table-column>
              <el-table-column prop="payStatus" label="支付状态" width="100">
                <template slot-scope="scope">
                  <span v-if="scope.row.payStatus === 0">待支付</span>
                  <span v-if="scope.row.payStatus === 1">未支付</span>
                  <span v-if="scope.row.payStatus === 2">已支付</span>
                </template>
              </el-table-column>
              <el-table-column prop="orderSource" label="订单来源">
                <template slot-scope="scope">
                  <div v-if="scope.row.orderSource === 0">企业小程序</div>
                  <div v-if="scope.row.orderSource === 1">随选网</div>
                </template>
              </el-table-column>

              <el-table-column prop="orderType" label="订单类型" width="80">
                <template slot-scope="scope">
                  <div v-if="scope.row.orderType === 0">普通订单</div>
                  <div v-else-if="scope.row.orderType === 1">拼团订单</div>
                </template>
              </el-table-column>

              <el-table-column prop="consignee" label="收货人" width="100"></el-table-column>
              <el-table-column prop="shopName" label="分配店铺">
                <template slot-scope="scope">
                  <div>{{scope.row.shopName}}</div>
                </template>
              </el-table-column>
              <el-table-column prop="address" label="收货人地址" align="left"></el-table-column>
              <el-table-column prop="shippingType" label="发货方式" width="120">
                <template slot-scope="scope">{{shippingTypeStr[scope.row.shippingType]}}</template>
              </el-table-column>
              <el-table-column prop="orderAmount" label="订单价格" width="120"></el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    @click="gotoDetail(scope.row.id)"
                    v-if="isPer('miniapp:order:view')"
                    type="text"
                  >详情</el-button>
                  <span v-if="scope.row.companyBusinessType ==2">
                    <span v-if="scope.row.orderStatus === 0 && scope.row.payStatus === 2">
                      <el-button size="mini" type="text" @click="isSureOrder(scope.row)">确认</el-button>
                    </span>
                    <span
                      v-if="scope.row.orderStatus === 1 && scope.row.shippingStatus ===0 && scope.row.payStatus === 2"
                    >
                      <el-button size="mini" type="text" @click="sendGoods(scope.row)">发货</el-button>
                    </span>
                    <span
                      v-if="scope.row.orderStatus === 1 && scope.row.shippingStatus === 1 && scope.row.payStatus === 2"
                    >
                      <el-button size="mini" type="text" @click="finished(scope.row)">完成</el-button>
                    </span>
                    <span v-if="scope.row.orderStatus === 4 && !scope.row.shippingStatus ===2">
                      <el-button size="mini" type="text" @click="finished(scope.row)">完成</el-button>
                    </span>
                  </span>
                  <span v-else>
                    <span v-if="scope.row.companyId == scope.row.franchiserId">
                      <span v-if="scope.row.orderStatus === 0 && scope.row.payStatus === 2">
                        <el-button size="mini" type="text" @click="isSureOrder(scope.row)">确认</el-button>
                      </span>
                      <span
                        v-if="scope.row.orderStatus === 1 && scope.row.shippingStatus ===0 && scope.row.payStatus === 2"
                      >
                        <el-button size="mini" type="text" @click="sendGoods(scope.row)">发货</el-button>
                      </span>
                      <span
                        v-if="scope.row.orderStatus === 1 && scope.row.shippingStatus === 1 && scope.row.payStatus === 2"
                      >
                        <el-button size="mini" type="text" @click="finished(scope.row)">完成</el-button>
                      </span>
                      <span v-if="scope.row.orderStatus === 4 && !scope.row.shippingStatus ===2">
                        <el-button size="mini" type="text" @click="finished(scope.row)">完成</el-button>
                      </span>
                    </span>
                    <el-button
                      v-if="scope.row.isShow==1"
                      size="mini"
                      type="text"
                      @click="allocation(scope.row)"
                    >分配</el-button>
                  </span>
                </template>
              </el-table-column>
            </el-table>
            <div class="pageFrame">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="query.page"
                :page-sizes="[10, 50, 100, 200,500]"
                :page-size="query.limit"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
              ></el-pagination>
            </div>
          </div>

          <!--确认对话框-->
          <el-dialog title="确认订单" :visible.sync="sureDialogTableVisible">
            <el-table :data="sureProduct">
              <el-table-column property="productName" label="商品"></el-table-column>
              <el-table-column label="收货人">
                <template slot-scope="scope">{{reveice}}</template>
              </el-table-column>
              <el-table-column label="发货方式">{{shippingTypeStr[orderDetailMsg.shippingType]}}</el-table-column>
              <el-table-column property="productStyleName" label="规格"></el-table-column>
              <el-table-column property="productPrice" label="价格"></el-table-column>
              <el-table-column property="productCount" label="数量"></el-table-column>
            </el-table>
            <div class="receiveAddress">收货地址：{{address}} ，{{mobile}}</div>
            <div class="sureButton">
              <el-button type="primary" round @click="sureOrder" size="medium">确认</el-button>
            </div>
          </el-dialog>

          <!--发货对话框-->
          <el-dialog title="发货" :visible.sync="sendVisible">
            <el-table :data="sureProduct" :header-cell-style="rowStyle">
              <el-table-column property="productName" label="商品"></el-table-column>
              <el-table-column label="收货人">
                <template slot-scope="scope">{{reveice}}</template>
              </el-table-column>
              <el-table-column property="productStyleName" label="规格"></el-table-column>
              <el-table-column property="productPrice" label="价格"></el-table-column>
              <el-table-column property="productCount" label="数量"></el-table-column>
            </el-table>
            <div class="receiveAddress">订单来源：{{orderDetailMsg.orderSource == 0 ? '企业小程序' : '随选网'}}</div>
            <div class="receiveAddress">分配店铺：{{orderDetailMsg.shopName}}</div>
            <div class="receiveAddress">收货地址：{{address}} , {{mobile}}</div>
            <div class="receiveAddress">
              收货方式：
              <el-radio v-model="orderDetailMsg.shippingType" :label="1">快递</el-radio>
              <el-radio v-model="orderDetailMsg.shippingType" :label="2">上门自取</el-radio>

              <p style="color: #FF6419" v-if="shouhuo">请输入收货方式</p>
            </div>
            <el-row :gutter="24" class="wuliu" v-if="orderDetailMsg.shippingType!=2">
              <el-col :span="12">
                <el-input
                  size="medium"
                  @change="changewu"
                  v-model="logisticsCompany"
                  placeholder="请输入物流公司名称"
                ></el-input>
                <p style="color: #FF6419" v-if="wuliu">请输入物流公司名称</p>
              </el-col>
              <el-col :span="12">
                <el-input size="medium" v-model="trackingNumber" placeholder="请输入货运单号"></el-input>
                <span style="color: #FF6419" v-if="orderNum">请输入货运单号</span>
                <span style="color: #FF6419" v-if="orderNumLen">运单号长度大于6位-30位字符</span>
              </el-col>
            </el-row>
            <div
              style="color:#ff641e; margin-top: 25px"
              v-if="orderDetailMsg.shippingType!=2"
            >* 请仔细填写物流公司及快递单号</div>
            <div class="sureButton">
              <el-button type="primary" round @click="sendOrder()" size="medium">确定</el-button>
            </div>
          </el-dialog>

          <!--完成订单-->
          <el-dialog title="完成订单" :visible.sync="finishedVisible">
            <el-table :data="sureProduct">
              <el-table-column property="productName" label="商品"></el-table-column>
              <el-table-column label="收货人">
                <template slot-scope="scope">{{reveice}}</template>
              </el-table-column>
              <el-table-column property="productStyleName" label="规格"></el-table-column>
              <el-table-column property="productPrice" label="价格"></el-table-column>
              <el-table-column property="productCount" label="数量"></el-table-column>
            </el-table>
            <div class="receiveAddress">收货地址：{{address}} , {{mobile}}，{{consignee}}</div>
            <div class="receiveAddress">
              收货方式：
              <el-radio v-model="orderDetailMsg.shippingType" :disabled="true" :label="1">快递</el-radio>
              <el-radio v-model="orderDetailMsg.shippingType" :disabled="true" :label="2">上门自取</el-radio>
            </div>
            <el-row :gutter="24" class="wuliu" v-if="orderDetailMsg.shippingType!=2">
              <el-col :span="12">物流公司:{{orderDetailMsg.logisticsCompany}}</el-col>
              <el-col :span="12">货运单号:{{orderDetailMsg.trackingNumber}}</el-col>
            </el-row>
            <div class="sureButton">
              <el-button type="primary" round @click="finishedMsg" size="medium">完成</el-button>
            </div>
          </el-dialog>

          <!--分配对话框-->
          <el-dialog title="分配" width="551px" :visible.sync="allocationDialog">
            <div style="text-align:center;">
              选择店铺：
              <el-select v-model="shopId" placeholder="请选择店铺" filterable>
                <el-option
                  v-for="item in shopList"
                  :key="item.shopId"
                  :label="item.shopName"
                  :value="item.shopId"
                ></el-option>
              </el-select>
            </div>
            <div class="sureButton" style="display:flex;justify-content:center;">
              <el-button type="info" plain round @click="allocationDialog=!allocationDialog" size="medium">取消</el-button>
              <el-button type="primary" round @click="allocationSubmit" size="medium">确认</el-button>
            </div>
          </el-dialog>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import qs from "qs";

export default {
  data() {
    return {
      shopId: "",
      allocationDialog: false,
      orderState: undefined,
      payState: undefined,
      shippingState: undefined,
      orderType: undefined,
      shippingType: undefined,
      shippingTypeStr: ["", "快递", "上门自提"],
      orderSource: [
        {
          value: "0",
          label: "企业小程序"
        },
        {
          value: "1",
          label: "随选网"
        }
      ],
      orderSourceValue: "",

      allotShop: [
        {
          value: "ta",
          label: "天安数码城店铺"
        },
        {
          value: "ta2",
          label: "天安数码城店铺2"
        }
      ],
      allotShopValue: "",

      wuliu: false,
      shouhuo: false,
      orderNum: false,
      orderNumLen: false,
      sureDialogTableVisible: false,
      finishedVisible: false,
      orderNumber: undefined,
      consignee: undefined,
      sendVisible: false,
      activeName: "second",
      selectDate: "",
      loading: true,
      OrderStatus: "",
      tableData: [],
      shopList: [],
      mobile: "",
      logisticsCompany: "",
      trackingNumber: "",
      total: 0,
      productName: [],
      address: "",
      orderID: "",
      sureProduct: [],
      orderDetailMsg: {
        logisticsCompany: "",
        trackingNumber: "",
        shippingType: 0
      },
      query: {
        page: 1,
        limit: 10
      },
      reveice: ""
    };
  },
  activated() {
    this.getOrderList();
    this.API.shopList({
      companyId: sessionStorage.getItem("companyID"),
      userId: sessionStorage.getItem("userId"),
      limit: 99999
    }).then(res => {
      if (res.success) this.shopList = res.datalist;
    });
  },
  methods: {
    // 确定分配
    allocationSubmit() {
      if (!this.shopId) {
        this.$message.error("请选择店铺");
        return false;
      }
      this.API.updateShopByPay({ id: this.orderID, shopId: this.shopId }).then(
        res => {
          this.$message({
            message: `分配${res.success ? "成功" : "失败"}`,
            type: res.success ? "success" : "error"
          });
          if (res.success) this.allocationDialog = !this.allocationDialog;
        }
      );
    },
    // 打开分配对话框
    allocation(data) {
      this.allocationDialog = !this.allocationDialog;
      this.shopId = "";
      this.orderID = data.id;
    },
    exportCustomerData() {
      let queryCondition = Object.assign(
        {},
        {
          orderCode: this.orderNumber,
          consignee: this.consignee,
          orderSource: this.orderSourceValue,
          shopName: this.allotShopValue,
          companyId: sessionStorage.getItem("companyID"),
          userId: sessionStorage.getItem("userId"),
          orderStatus: this.orderState,
          orderType: this.orderType,
          shippingType: this.shippingType,
          payStatus: this.payState,
          shippingStatus: this.shippingState
          // exportType:0
        }
      );
      for (let key in queryCondition) {
        if (
          queryCondition[key] === "" ||
          queryCondition[key] === undefined ||
          queryCondition[key] === null
        ) {
          delete queryCondition[key];
        }
      }
      console.log(queryCondition);
      this.loadingTwo = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      this.API.exportOrderApi(queryCondition).then(result => {
        console.log(result);
        if (result && result.size > 0) {
          this.loadingTwo.close();
          let url = window.URL.createObjectURL(result);
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", "订单信息导出.xls");
          document.body.appendChild(link);
          link.click();
        } else {
          this.loadingTwo.close();
          this.$message.error("没有数据需要导出!");
        }
      });
    },
    rowStyle({ row, rowIndex }) {
      if (rowIndex === 0) {
        return {
          height: "40px!important",
          padding: "0!important",
          fontWeight: "normal",
          background: "#f5f5f5",
          color: "#666"
        };
      }
    },
    isPer(per) {
      let permiss = qs.parse(sessionStorage.getItem("loginUser"));
      return this.isMainPer(per, permiss);
    },
    checkZmOrNum(zmnum) {
      var zmnumReg = /^[0-9a-zA-Z]*$/;
      if (zmnum != "" && !zmnumReg.test(zmnum)) {
        alert("只能输入是字母或者数字,请重新输入");
        return false;
      }
    },
    changewu() {
      if (this.logisticsCompany) {
        this.wuliu = false;
      }
    },
    // 搜索订单
    search() {
      this.query.page = 1;
      this.loading = true;
      this.getOrderList();
    },
    resetList() {
      this.loading = true;
      this.orderNumber = "";
      this.consignee = "";
      this.allotShopValue = "";
      this.orderSourceValue = "";
      this.orderState = "";
      this.orderType = "";
      this.payState = "";
      this.shippingState = "";
      this.shippingType = "";
      this.getOrderList();
    },
    // 确认订单
    isSureOrder(row) {
      console.log(row, "row sure");
      if (row.payStatus === 0 || row.payStatus === 1) {
        this.$message.error("您的订单未支付");
        return;
      }
      this.reveice = row.consignee;
      this.sureDialogTableVisible = true;
      this.orderInfo(row);
    },
    orderInfo(row) {
      this.address = row.address;
      this.orderID = row.id;
      this.API.spGetOrderDetail({ id: this.orderID }).then(res => {
        if (res) {
          this.orderDetailMsg = res.obj;
          this.mobile = res.obj.mobile;
          this.sureProduct = res.obj.productRefList;
          console.log(this.sureProduct, "this.sureProduct");
        }
      });
    },
    sureOrder() {
      this.$confirm("确认订单", "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        confirmButtonText: "确定",
        type: "info",
        center: true
      }).then(() => {
        this.API.spChangeOrderStatus({
          type: "affirmOrder",
          id: this.orderID
        }).then(res => {
          if (res) {
            this.sureDialogTableVisible = false;
            this.OrderStatus = res.data;
            this.getOrderList();
            this.$message.success("订单已确认");
          }
        });
      });
    },
    // 发货信息
    sendGoods(row) {
      this.logisticsCompany = "";
      this.trackingNumber = "";
      this.reveice = row.consignee;
      this.sendVisible = true;
      this.orderInfo(row);
    },
    // 发货
    sendOrder() {
      if (!this.orderDetailMsg.shippingType) this.shouhuo = true;
      else this.shouhuo = false;

      if (this.orderDetailMsg.shippingType != 2) {
        if (this.logisticsCompany === "" || this.logisticsCompany === null) {
          this.wuliu = true;
          return;
        }

        if (this.logisticsCompany.length > 0) {
          this.wuliu = false;
        }

        if (this.trackingNumber === "" || this.trackingNumber === null) {
          this.orderNum = true;
          return;
        }

        if (this.trackingNumber.length < 6) {
          this.orderNumLen = true;
          return;
        } else {
          this.orderNumLen = false;
        }
      }

      this.$confirm("确认订单发货", "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        confirmButtonText: "确定",
        type: "info",
        center: true
      }).then(() => {
        this.API.spSendGoods({
          type: "affirmOrder",
          shippingStatus: 1,
          shippingType: this.orderDetailMsg.shippingType,
          logisticsCompany:
            this.orderDetailMsg.shippingType == 2
              ? undefined
              : this.logisticsCompany || undefined,
          trackingNumber:
            this.orderDetailMsg.shippingType == 2
              ? undefined
              : this.trackingNumber || undefined,
          id: this.orderID
        }).then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          }
          if (res) {
            this.sendVisible = false;
            this.getOrderList();
            this.$message.success("订单已发货");
          }
        });
      });
    },
    // 完成
    finished(row) {
      this.finishedVisible = true;
      this.orderInfo(row);
      this.reveice = row.consignee;
      console.log(row, "finished");
    },
    finishedMsg() {
      this.$confirm("确认完成订单", "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        confirmButtonText: "完成",
        type: "info",
        center: true
      }).then(() => {
        this.API.spChangeOrderStatus({
          type: "finishOrder",
          id: this.orderID
        }).then(res => {
          if (res) {
            this.finishedVisible = false;
            this.$message.success("订单已完成");
            this.getOrderList();
          }
        });
      });
    },
    gotoDetail(id) {
      localStorage.setItem("breadcrumb3", "订单详情");
      this.$router.push({ path: `/orderDetail/${id}` });
    },
    viewDetail(row) {
      this.$router.push("");
    },
    //获取订单列表
    getOrderList() {
      let queryCondition = Object.assign({}, this.query, {
        orderCode: this.orderNumber,
        consignee: this.consignee,
        orderSource: this.orderSourceValue,
        shopName: this.allotShopValue,
        companyId: sessionStorage.getItem("companyID"),
        orderStatus: this.orderState,
        orderType: this.orderType,
        payStatus: this.payState,
        shippingStatus: this.shippingState,
        shippingType: this.shippingType
      });
      for (let key in queryCondition) {
        if (
          queryCondition[key] === "" ||
          queryCondition[key] === undefined ||
          queryCondition[key] === null
        ) {
          delete queryCondition[key];
        }
      }
      this.API.spOrderList(queryCondition).then(res => {
        console.log(res, "res");
        this.loading = true;
        if (
          res.code == 204 ||
          res.code == 400 ||
          res.code >= 500 ||
          res.code == 404
        ) {
          this.loading = false;
        }

        if (res) {
          this.loading = false;
          this.tableData = res.datalist;
          console.log(res.datalist, "f res.datalist");
          for (let i = 0; i < res.datalist.length; i++) {
            if (res.datalist[i].productNames.length) {
              this.productName = res.datalist[i].productNames;
            }
          }
          this.total = res.totalCount;
        }
      });
    },
    // 分页模块
    handleSizeChange: function(size) {
      // this.pageSize = size;
      this.query.limit = size;
      this.getOrderList();
    },
    handleCurrentChange: function(currentPage) {
      // this.currentPage = currentPage;
      this.query.page = currentPage;
      this.loading = true;
      this.getOrderList();
    }
  },
  mounted() {
    this.getOrderList();
  }
};
</script>

<style lang="scss" scoped>
.productName {
  text-align: left;
  line-height: 24px;
}

.receiveAddress {
  margin-top: 30px;
  margin-bottom: 20px;
}

.sureButton {
  text-align: right;
  margin-top: 20px;
  border-top: solid 1px #ddd;
  padding-top: 20px;
  .el-button {
    width: 130px;
  }
}

.pageFrame {
  background: #fff;
  height: 110px;
  padding-top: 40px;
  box-sizing: border-box;
  text-align: center;
}

.orderlistTop {
  background: #fff;
  padding: 20px;
}

.buttonSplitLine {
  border-top: solid 1px #ddd;
  margin: 25px 0;
  padding-top: 20px;
  text-align: center;
  .reset {
    background: #f5f5f5;
  }
}

.searchBg {
  background: #fff;
}

.orderListSearch {
  margin: 15px;
}

.buttonSplitLine {
  border-top: solid 1px #ddd;
  margin: 25px 0;
  padding-top: 20px;
}
.addProBtn {
  margin-left: 20px;
  margin-bottom: 20px;
}
</style>
<style lang="scss">
.searchBg {
  .el-tabs__nav {
    margin-left: 15px;
  }

  .el-radio-button__inner {
    background: #fafafa;
    border: none;
  }

  .el-radio-button__orig-radio:checked + .el-radio-button__inner {
    background: #fff;
    border: none;
    color: #666;
    -webkit-box-shadow: none;
    box-shadow: none;
  }
}
</style>