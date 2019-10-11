<template>
  <div class="add-activity" v-if="pageStatus==0">
    <div class="main-body">
      <div class="breadcrumb-box">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/operation/order/settlement' }">结算管理</el-breadcrumb-item>
          <el-breadcrumb-item>
            {{pageType == "edit" ? "编辑" : (pageType == "detail" ? "详情" : "新增")}}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="form-box">
        <el-form
          ref="formData"
          :rules="rules"
          label-position="right"
          :model="formData"
          label-width="200px"
        >
          <el-row :gutter="60">
            <el-col :span="10">
              <el-form-item label="企业名称：" prop="companyId" label-width="130px">
                <div v-if="(pageType != 'detail' && settlementStatusDB != 1)">
                <el-select clearable filterable v-model="formData.companyId" placeholder="请选择企业名称" :disabled="isChooseOrder">
                  <el-option
                    v-for="item in companyList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
                </div>
                <div v-else>
                    {{formData.companyName}}
                </div>
              </el-form-item>
            </el-col>

            <el-col :span="10">
              <el-form-item label-width="130px" label="结算订单：" prop="orderIds">
                <div v-if="pageType != 'detail' && settlementStatusDB != 1">
                  <el-button  type="primary" class="submit-btn" @click="chooseOrderList()">选择订单</el-button>选中{{this.orderIdList.length}}个订单
                </div>
                <div v-else>
                  <el-button type="primary" class="submit-btn" @click="chooseOrderList(1)">查看订单</el-button>选中{{this.orderIdList.length}}个订单
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="60">
            <el-col :span="10">
              <el-form-item label-width="130px" label="结算总金额：">
                <!-- <div v-if="pageType != 'detail'">
                  <el-input class="name-input" v-model="formData.totalAmount"></el-input>
                </div> -->
                <!-- <div v-else>
                  {{formData.totalAmount}}
                </div> -->
                {{formData.totalAmount}}元
              </el-form-item>
            </el-col>

            <el-col :span="10">
              <el-form-item label="实际结算金额：" label-width="130px" prop="actualAmount">
                <div v-if="pageType != 'detail'">
                  <!-- <el-input class="name-input" v-model="formData.actualAmount" @keyup.native="discountAmountLimit" maxlength="10"></el-input> -->
                  <el-input class="name-input" v-model="formData.actualAmount" maxlength="10"></el-input>
                </div>
                <div v-else>
                  {{formData.actualAmount}}
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="60">
            <el-col :span="10">
              <el-form-item label="服务费：" label-width="130px" prop="serviceFee">
                <div v-if="pageType != 'detail'">
                  <!--<el-input class="name-input" v-model="formData.serviceFee" @keyup.native="discountAmountLimit2" maxlength="10"></el-input>-->
                  <el-input class="name-input" v-model="formData.serviceFee" maxlength="10"></el-input>
                </div>
                <div v-else>
                  {{formData.serviceFee}}
                </div>
              </el-form-item>
            </el-col>

            <el-col :span="10">
              <el-form-item label="责任人手机号：" label-width="130px" prop="phone" maxlength="20">
                <div v-if="pageType != 'detail'">
                  <el-input class="name-input" v-model="formData.phone" @keyup.native="discountAmountLimit3"></el-input>
                </div>
                <div v-else>
                    {{formData.phone}}
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label-width="130px" label="结算状态：" prop="settlementStatus">
            <div v-if="pageType != 'detail' && settlementStatusDB != 1">
            <el-select filterable v-model="formData.settlementStatus" placeholder>
              <el-option
                v-for="item in settlementStatusList"
                :key="item.type"
                :label="item.name"
                :value="item.type"
              ></el-option>
            </el-select>
            </div>
            <div v-else>
              {{formData.settlementStatusInfo}}
            </div>
          </el-form-item>

          <el-form-item label="备注：" label-width="130px">
            <div v-if="pageType != 'detail'">
              <el-input type = "textarea" :rows="6" style="width: 800px;" v-model="formData.remark" maxlength="200"></el-input>
            </div>
          <div v-else>
            {{formData.remark}}
          </div>
          </el-form-item>

          <el-form-item>
            <div class="btn-box">
              <el-button class="submit-btn" @click="cancelSubmit()">
                {{pageType == "detail" ? "关闭" : "取消"}}
                </el-button>
                <el-button v-if="pageType == 'edit'" type="primary" class="submit-btn" @click="submitFormValid(1)">确定</el-button>
                <el-button v-if="pageType != 'detail' && pageType != 'edit'" type="primary" class="submit-btn" @click="submitFormValid()">确定</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>

  <div v-else-if="pageStatus==1" class="main">
    <!--条件查询-->
    <div class="mainTop">
      <div class="breadcrumb-box">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/operation/order/settlement' }">结算管理</el-breadcrumb-item>
          <el-breadcrumb-item>
            <span style="color:#000000;cursor:pointer" @click="pageStatus=0">
              {{pageType == "edit" ? "编辑" : (pageType == "detail" ? "详情" : "新增")}}
            </span>
          </el-breadcrumb-item>
          <el-breadcrumb-item>选择订单</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="selRows" style="height: 50px;">
        <div class="selItems">
          <div class="inputs">
            <el-input
              placeholder="请输入订单号"
              maxlength="30"
              v-model="getListParams.orderCodeLike"
              clearable
            ></el-input>
          </div>
        </div>
        <div class="selItems">
          <div class="inputs">
            <el-input
              placeholder="请输入商品名称"
              maxlength="30"
              v-model="getListParams.productNameLike"
              clearable
            ></el-input>
          </div>
        </div>
        <div class="selItems">
          <div class="inputs">
            <el-input
              placeholder="请输入联系电话"
              maxlength="30"
              v-model="getListParams.mobileLike"
              clearable
            ></el-input>
          </div>
        </div>
        <div class="selItems">
          <div class="el-select">
            <el-select
              size="medium"
              v-model="getListParams.orderStatus"
              placeholder="请选择订单状态"
              :clearable="true"
              class="select"
            >
              <el-option
                v-for="item in orderStatusList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="selItems">
          <div class="el-select">
            <el-select
              size="medium"
              v-model="getListParams.orderType"
              placeholder="请选择订单类型"
              :clearable="true"
            >
              <el-option
                v-for="item in OrderTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
      </div>
      <div class="selRows">
        <div class="selItems">
          <div class="el-select">
            <el-date-picker
              v-model="createTimeStr"
              type="daterange"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="请选择下单时间"
              end-placeholder="请选择下单时间"
              style="width: 420px"
            ></el-date-picker>
          </div>
        </div>
        <div class="selItems">
          <div class="inputs">
            <el-input
              placeholder="分配的店铺"
              maxlength="30"
              v-model="getListParams.shopNameLike"
              clearable
            ></el-input>
          </div>
        </div>
        <div class="submitBtn" @click="getOrderList(2)">搜索</div>
        <div class="submitBtn" @click="resetAndSelect()">重置</div>
      </div>
    </div>

    <!-- 返回信息列表 -->
    <div class="mainBody">
      <section>
        <div class="listTitle">
          <div class="left">
            <el-button size="small" type="primary" @click="pageStatus=0">返回</el-button>
            <el-button v-if="pageType != 'detail' && settlementStatusDB != 1" @click="batchAdd()" size="small" type="primary">批量添加</el-button>
            <el-button v-if="pageType != 'detail' && settlementStatusDB != 1" @click="batchCancel()" size="small" type="primary">批量取消</el-button>
            共选择个{{this.orderIdList.length}}订单
          </div>
        </div>
        <div class="tableDataFrame">
          <el-table
            :data="orderList"
            :header-cell-style="rowStyle"
            style="padding-top:20px"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(255, 255, 255, 0.8)"
            tooltip-effect="dark"
            @selection-change="handleSelectionChange"
          >
            <el-table-column v-if="pageType != 'detail' && settlementStatusDB != 1" type="selection" width="30"></el-table-column>
            <el-table-column type="index" width="50px" label="序号"></el-table-column>
            <el-table-column label="订单号" width="150px">
              <template slot-scope="scope">{{scope.row.orderCode}}</template>
            </el-table-column>
            <!-- <el-table-column prop="feedbackTxt" label="商品名称" width="200px">
              <template slot-scope="scope">{{scope.row.productName}}</template>
            </el-table-column> -->
            <el-table-column label="支付状态" width="80px">
              <template slot-scope="scope">{{scope.row.payStatusInfo}}</template>
            </el-table-column>
            <el-table-column label="订单状态" width="80px">
              <template slot-scope="scope">{{scope.row.orderStatusInfo}}</template>
            </el-table-column>
            <el-table-column label="商品配送情况" width="120px">
              <template slot-scope="scope">{{scope.row.shippingStatusInfo}}</template>
            </el-table-column>
            <el-table-column label="订单价格" width="80px">
              <template slot-scope="scope">{{scope.row.orderAmount}}</template>
            </el-table-column>
            <el-table-column label="所属企业" width="150px">
              <template slot-scope="scope">{{scope.row.companyName}}</template>
            </el-table-column>
            <el-table-column label="分配店铺" width="150px">
              <template slot-scope="scope">{{scope.row.shopName}}</template>
            </el-table-column>
            <el-table-column label="订单类型" width="100px">
              <template slot-scope="scope">{{scope.row.orderTypeInfo}}</template>
            </el-table-column>
            <el-table-column label="收货人信息" width="250px">
              <template slot-scope="scope">
                {{scope.row.consigneeInfoName}}
                <br />
                {{scope.row.consigneeInfoMobile}}
                <br />
                {{scope.row.consigneeInfoAddress}}
              </template>
            </el-table-column>
            <el-table-column prop="feedbackTxt" label="下单时间" width="100px">
              <template slot-scope="scope">{{scope.row.createTimeInfo}}</template>
            </el-table-column>
            <el-table-column prop="feedbackTxt" label="发货方式" width="80px">
              <template slot-scope="scope">{{scope.row.shippingTypeInfo}}</template>
            </el-table-column>
            <el-table-column label="操作" width="150px">
              <template slot-scope="scope">
                <el-button type="text" @click="goToDetail(scope.row.id)">详情</el-button>
                
                <el-button
                  v-if="scope.row.checkedStatus == 0 && pageType != 'detail' && settlementStatusDB != 1"
                  type="text"
                  @click="addOrder(scope.row.id)"
                >添加结算</el-button>
                <el-button
                  v-if="scope.row.checkedStatus == 1 && pageType != 'detail' && settlementStatusDB != 1"
                  type="text"
                  @click="cancelOrder(scope.row.id)"
                >取消</el-button>
                <el-button
                  v-if="scope.row.checkedStatus == 1 && (pageType == 'detail' || settlementStatusDB == 1)"
                  type="text"
                >已添加</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pageCount">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="listPage"
              :page-sizes="[10, 20, 30, 40]"
              :page-size="getListParams.limit"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalCount"
            ></el-pagination>
          </div>
        </div>
      </section>
    </div>
  </div>

  <div v-else>
    <div class="reBack">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/operation/order/settlement' }">结算管理</el-breadcrumb-item>
            <el-breadcrumb-item>
                <span style="color:#000000;cursor:pointer" @click="pageStatus=0">
                  {{pageType == "edit" ? "编辑" : (pageType == "detail" ? "详情" : "新增")}}
                </span>
            </el-breadcrumb-item>
            <el-breadcrumb-item>
                <span style="color:#000000;cursor:pointer" @click="pageStatus=1">选择订单</span>
            </el-breadcrumb-item>
            <el-breadcrumb-item>订单详情</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
    <div class="orderDetailMsg">
      <form method="post" action="#" id="printJS-form">
        <table border="1" class="orderTotal" cellpadding="0" cellspacing="0">
          <thead>
            <tr>
              <th>订单号</th>
              <th>下单时间</th>
              <th>发货方式</th>
              <th>物流公司</th>
              <th>运单号</th>
              <th>商品数量</th>
              <th>订单总金额</th>
              <th>收件人姓名</th>
              <th>收货地址</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{orderDetail.orderCode}}</td>
              <td>{{orderDetail.orderTime}}</td>
              <td>{{shippingTypeStr[orderDetail.shippingType]}}</td>
              <td>
                <!--<el-input size="mini" style="width: 120px" v-model="orderDetail.logisticsCompany" placeholder="物流公司"> </el-input>-->
                {{orderDetail.logisticsCompany}}
              </td>
              <td>
                {{orderDetail.trackingNumber}}
                <!--<el-input size="mini" style="width: 120px" v-model="orderDetail.trackingNumber" placeholder="运单号"> </el-input>-->
              </td>
              <td>{{orderDetail.productCount}}</td>
              <td>{{orderDetail.orderAmount}}元</td>
              <td>{{orderDetail.consignee}}</td>
              <td>{{orderDetail.address}}, {{orderDetail.mobile}}</td>
            </tr>
          </tbody>
        </table>

        <table border="1" class="tableData" cellpadding="0" cellspacing="0">
          <thead>
            <tr>
              <th>商品</th>
              <th>规格</th>
              <th>数量</th>
              <th>单价</th>
              <th>订单来源</th>
              <th v-if="orderDetail.orderType != null">订单类型</th>
              <th>分配店铺</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in orderDetail.productRefList" :key="index">
              <td>{{product.productName}}</td>
              <td>
                <p
                  v-if="product.productColorName == product.productStyleName"
                >{{product.productStyleName}}</p>
                <p v-else>{{product.productColorName}} {{product.productStyleName}}</p>
              </td>
              <td>{{product.productCount}}</td>
              <td>{{product.productPrice}}</td>
              <td>{{orderDetail.orderSource == null ? "" : orderDetail.orderSource == 0 ? '企业小程序' : '随选网'}}</td>
              <td
                v-if="orderDetail.orderType != null"
              >{{orderDetail.orderType == 0 ? '普通订单' : '拼团订单'}}</td>
              <td>{{orderDetail.shopName}}</td>
            </tr>
          </tbody>
        </table>
        <div style="line-height:58px;padding-left:10px;border-bottom:solid 1px #dcdcdc;">
          用户备注信息:
          <span style="padding-left:20px">{{orderDetail.remark}}</span>
        </div>
      </form>
      <div class="printOrder">
        <!-- <el-button round @click.native="printMethods">打印</el-button> -->
        <el-button round @click="pageStatus=1">关闭</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        isChooseOrder: false,
        settlementStatusDB: 0,
        settlementOrderId: this.$route.params.id,
        pageType: this.$route.query.type,
        detailID: "",
        orderDetail: {},
        shippingTypeStr: ["", "快递", "上门自提"],
        pageStatus: 0,
        companyList: [],
        createTimeStr: undefined,
        // 列表数据
        orderList: [],
        listPage: 1,
        totalCount: 0,
        orderIdList: [],
        settlementStatusList: [
          { type: 0, name: "结算中" },
          { type: 1, name: "已结算" }
        ],
        /**
         * 结算单编辑/新增页面formData
         */
        formData: {
          id: undefined,
          companyId: undefined,
          companyName: undefined,
          orderIds: undefined,
          totalAmount: 0,
          actualAmount: undefined,
          serviceFee: undefined,
          phone: undefined,
          settlementStatus: 0,
          remark: undefined,
          settlementStatusInfo: undefined
        },
        orderStatusList: [
          {value: 0, label: "待付款"},
          {value: 7, label: "待确认"},
          {value: 1, label: "已确认"},
          {value: 2, label: "已取消"},
          /*{value: 3, label: "无效"},*/
          {value: 4, label: "交易完成"},
          /*{value: 5, label: "退货"},*/
          {value: 6, label: "待成团"},
          {value: 10, label: "结算中"},
          {value: 11, label: "已结算"}
        ],
        ShippingStatusList: [
          { value: 0, label: "未发货" },
          { value: 1, label: "已发货" },
          { value: 2, label: "已收货" },
          { value: 3, label: "退货" }
        ],
        OrderTypeList: [
          { value: 0, label: "普通订单" },
          { value: 1, label: "拼团订单" }
        ],
        getListParams: {
          selectType2: undefined,
          settlementOrderId: undefined,
          orderCodeLike: undefined,
          productNameLike: undefined,
          mobileLike: undefined,
          orderStatus: undefined,
          payStatus: undefined,
          shippingType: undefined,
          orderType: undefined,
          companyId: undefined,
          shopNameLike: undefined,
          createTimeStart: undefined,
          createTimeEnd: undefined,
          checkedOrderIds: undefined,
          selectType: 1,
          page: 1, // 页码
          limit: 10 // 每页条数
        },
        rules: {
          companyId: [
            {required: true, message: '请选择企业', trigger: 'blur'}
          ],
          orderIds: [
            {required: true, message: '请选择订单', trigger: 'blur'}
          ],
          phone: [
            {validator: this.validatePhone, trigger: 'blur'}
          ],
          actualAmount: [
            {validator: this.validateAmount, trigger: 'blur'}
          ],
          serviceFee: [
            {validator: this.validateAmount, trigger: 'blur'}
          ]
        }
      };
    },
    created() {
      console.error(this.settlementOrderId);
      console.error(this.pageType);
      this.getCompanyList();
      if (this.pageType == "edit" || this.pageType == "detail") {
        this.getSettlementOrderDetail(this.settlementOrderId);
      }
    },
    methods: {
      validatePhone(rule, value, callback) {
        if (value == undefined || value == "") {
          callback();
        }

          /* let isMobilePhone=/^([0-9]{3,4})?[0-9]{7,8}$/; */
          let isFixMob= /^0?1[3|4|5|6|7|8|9][0-9]\d{8}$/;
          let is400 = /^400[0-9]{0,7}[0-9]{1,7}$/;
          let isTelephone = /^0\d{2,3}?\d{7,8}$/;
          if(isFixMob.test(value) || /*isMobilePhone.test(value) ||*/ is400.test(value) || isTelephone.test(value)){
              callback();
          }
          else{
              callback(new Error('手机号格式不正确'));
          }
      },
      validateAmount(rule, value, callback) {
        if (value == undefined || value == "") {
          callback();
        }

        let isAmount = /(^[1-9](\d+)?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/;

          if(isAmount.test(value)){
              callback();
          }
          else{
              callback(new Error('金额格式不正确(最多保留两位小数的数字)'));
          }
      },
      discountAmountLimit3(){
        if(this.formData.phone || this.formData.phone == 0){
          //为了去除首位的0 如050
          this.formData.phone = this.formData.phone.replace(/\D/g,'');
          //转换成字符串才能有replace方法
          this.formData.phone = this.formData.phone + "";
        }
	    },
      discountAmountLimit2(){
        if(this.formData.serviceFee || this.formData.serviceFee == 0){
          //为了去除首位的0 如050
          this.formData.serviceFee = this.formData.serviceFee.replace(/\D/g,'') * 1;
          if(this.formData.serviceFee <0){
            this.formData.serviceFee = "0";
            this.$message.error('请输入正确的服务费');
          }
          //转换成字符串才能有replace方法
          this.formData.serviceFee = this.formData.serviceFee + "";
        }
	    },
      discountAmountLimit(){
        if(this.formData.actualAmount || this.formData.actualAmount == 0){
          //为了去除首位的0 如050
          this.formData.actualAmount = this.formData.actualAmount.replace(/\D/g,'') * 1;
          if(this.formData.actualAmount <0){
            this.formData.actualAmount = "0";
            this.$message.error('请输入正确的实际结算金额');
          }
          //转换成字符串才能有replace方法
          this.formData.actualAmount = this.formData.actualAmount + "";
        }
	    },
      /**
       * 处理总价格
       */
      handleActualAmount() {
        this.API.getActualAmount({orderIds: this.formData.orderIds}).then(res => {
          if (res.success) {
            this.formData.totalAmount = res.obj;
          } else {
            this.$message({
              type: "error",
              message: "获取总价失败"
            });
          }
        });
      },
      getSettlementOrderDetail(id) {
        this.API.getSettlementOrderDetail({id: id}).then(res => {
          if (res.success) {
            this.settlementStatusDB = res.obj.settlementStatus;
            this.formData.companyId = res.obj.companyId;
            this.formData.orderIds = res.obj.orderIds;
            this.formData.totalAmount = res.obj.totalAmount;
            this.formData.actualAmount = res.obj.actualAmount;
            this.formData.serviceFee = res.obj.serviceFee;
            this.formData.phone = res.obj.phone;
            this.formData.settlementStatus = res.obj.settlementStatus;
            this.formData.remark = res.obj.remark;
            this.formData.id = res.obj.id;
            this.formData.companyName = res.obj.companyName;
            this.formData.settlementStatusInfo = res.obj.settlementStatusInfo;
            this.getListParams.settlementOrderId = id;
            if (res.obj.orderIds != null && res.obj.orderIds != undefined) {
              this.orderIdList = res.obj.orderIds.split(",");
            }

            console.info("this.formData.orderIds = ", this.formData.orderIds);
            if (this.formData.orderIds != undefined && this.formData.orderIds.length > 0) {
              
              this.isChooseOrder = true;
            }

          } else {
            this.$message({
                type: "error",
                message: res.message
            });
          }
        });
      },
        getOrderDetail(id) {
            this.API.spGetOrderDetail({ id: id }).then(res => {
                if (res) {
                    this.orderDetail = res.obj;
                }
            });
        },
        goToDetail(id) {
            this.getOrderDetail(id);
            this.pageStatus = 2;
        },
        resetAndSelect() {
          this.reset();
          this.getOrderList();
        },
        reset() {
            this.getListParams.orderCodeLike = undefined;
            this.getListParams.productNameLike = undefined;
            this.getListParams.mobileLike = undefined;
            this.getListParams.orderStatus = undefined;
            this.getListParams.payStatus = undefined;
            this.getListParams.shippingType = undefined;
            this.getListParams.orderType = undefined;
            this.createTimeStr = undefined;
            this.getListParams.createTimeStart = undefined;
            this.getListParams.createTimeEnd = undefined;
            this.getListParams.companyId = undefined;
            this.getListParams.shopNameLike = undefined;
        },
        addOrder(id) {
          id = id.toString();
            if (!this.orderIdList.includes(id)) {
                this.orderIdList.push(id);
            }
            this.listPage = 1;
            this.getListParams.page = 1;
            this.getListParams.checkedOrderIds = this.orderIdList.toString();
            this.formData.orderIds = this.getListParams.checkedOrderIds;

            // 处理总价格
            this.handleActualAmount();

            this.isChooseOrder = true;

            this.getOrderList();
        },
        cancelOrder(id) {
          id = id.toString();
          console.error("cancelOrder(id), id = " + id);
            if (this.orderIdList.includes(id)) {
                console.error("this.orderIdList.includes(id)");
                for (var i = 0; i < this.orderIdList.length; i++) {
                  console.error("this.orderIdList[i].toString() = " + this.orderIdList[i].toString());
                    if (this.orderIdList[i].toString() == id.toString()) {
                        console.error("this.orderIdList[i], i = " + i);
                        this.orderIdList.splice(i, 1);
                        break;
                    }
                }
            }

            console.error("cancelOrder(id), this.orderIdList = " + this.orderIdList.toString());

            this.listPage = 1;
            this.getListParams.page = 1;
            this.getListParams.checkedOrderIds = this.orderIdList.toString();
            this.formData.orderIds = this.getListParams.checkedOrderIds;

            // 处理总价格
            this.handleActualAmount();

            this.getOrderList();
        },
        batchAdd() {
          if (this.idBox == undefined || this.idBox.length == 0) {
            this.$message({
              message: "请勾选订单",
              type: "info"
            });
            return;
          }

            var orderIdListTemp = this.orderIdList;
            this.idBox.forEach(function(v) {
              v = v.toString();
              !orderIdListTemp.includes(v) && orderIdListTemp.push(v);

            });

            this.getListParams.checkedOrderIds = this.orderIdList.toString();
            this.formData.orderIds = this.getListParams.checkedOrderIds;

            // 处理总价格
            this.handleActualAmount();

            this.isChooseOrder = true;

            this.getOrderList();
        },
        updateTotalAmount() {
          var totalAmount = 0;
          this.orderPriceList.forEach(function(item) {
            var amountItem = item.split("_")[1] * 1;
            totalAmount += amountItem;
          });
          this.formData.totalAmount = totalAmount;
          console.error("this.formData.totalAmount = " + this.formData.totalAmount);
        },
        batchCancel() {
          if (this.idBox == undefined || this.idBox.length == 0) {
            this.$message({
              message: "请勾选订单",
              type: "info"
            });
            return;
          }

            var orderIdListTemp = this.orderIdList;
            this.idBox.forEach(function(v) {
              v = v.toString();
                if (orderIdListTemp.includes(v)) {
                    for (var i = 0; i < orderIdListTemp.length; i++) {
                        if (orderIdListTemp[i] == v) {
                            orderIdListTemp.splice(i, 1);
                            break;
                        }
                    }
                }
            });

            this.getListParams.checkedOrderIds = this.orderIdList.toString();
            this.formData.orderIds = this.getListParams.checkedOrderIds;

            // 处理总价格
            this.handleActualAmount();

            this.getOrderList();
        },
    removeByValue(arr, val) {
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] == val) {
          arr.splice(i, 1);
          break;
        }
      }
    },
    handleSelectionChange(val) {
      this.idBox = [];
      val.map(res => {
        this.idBox.push(res.id);
      });
    },
    getOrderList(type) {
      console.error("type = " + type);
      this.orderList = [];
      this.totalCount = 0;

      if (1 == type) {
        this.getListParams.selectType2 = type;
      }
      if (2 == type) {
        // 重置page
        this.listPage = 1;
        this.getListParams.page = 1;
      }
      // else {
      //   this.getListParams.selectType2 = undefined;
      // }

      if (this.createTimeStr != undefined) {
        this.getListParams.createTimeStart = this.createTimeStr[0];
        this.getListParams.createTimeEnd = this.createTimeStr[1];
      } else {
        this.getListParams.createTimeStart = undefined;
        this.getListParams.createTimeEnd = undefined;
      }

      this.getListParams.checkedOrderIds = this.orderIdList.toString();

      this.API.getOrderList(this.getListParams).then(res => {
        if (res.success) {
          this.listPage = this.getListParams.page;
          this.orderList = res.datalist;
          this.totalCount = res.totalCount;
        }
      });
    },
    goToAdd() {
      this.pageStatus = 0;
    },
    rowStyle({ row, rowIndex }) {
      if (rowIndex === 0) {
        return {
          height: "30px!important",
          background: "#f5f5f5",
          color: "#666",
          lineHeight: "80px!important"
        };
      }
    },
    handleSizeChange: function(size) {
      //this.query.limit = size
      this.getListParams.limit = size;
      this.getListParams.page = 1;
      this.getOrderList();
    },
    handleCurrentChange: function(currentPage) {
      //this.query.page = currentPage;
      this.getListParams.page = currentPage;
      this.loading = true;
      this.getOrderList();
    },
    chooseOrderList(type) {
        // 检验必须选择了企业
        if (this.formData.companyId == null || this.formData.companyId == undefined) {
            this.$message({
                type: "error",
                message: "请先选择企业"
            });
            return;
        }

        this.getListParams.companyId = this.formData.companyId;
        this.pageStatus = 1;
        this.getOrderList(type);
    },
    getCompanyList() {
      this.API.belongCompanyList({ businessTypeListStr: 1 }).then(res => {
        this.companyList = res.datalist;
      });
    },
    submitFormValid(type) {
      this.$refs.formData.validate((valid) => {
        if (valid) {
          this.submitForm(type);
        }
      })
    },
    submitForm(type) {
      console.error("type = ", type);
      if (this.formData.id != undefined && this.formData.id > 0) {
        this.API.updateSettlementOrder(this.formData).then(res => {
          if (res.success) {
            this.$message({
              message: "更新成功",
              type: "success"
            });
            this.$router.push({path: "/operation/order/settlement", query: {nonuseCache : true, type: type}});
          } else {
            this.$message({
              message: res.message,
              type: "error"
            });
          }
        });
      } else {
        this.API.createSettlementOrder(this.formData).then(res => {
          if (res.success) {
            this.$message({
              message: "保存成功",
              type: "success"
            });
            // 不使用缓存, 但保持搜索条件
            this.$router.push({path: "/operation/order/settlement", query: {nonuseCache : true}});
          } else {
            this.$message({
              message: res.message,
              type: "error"
            });
          }
        });
      }
    },
    cancelSubmit() {
      // this.$router.push({path: "/operation/order/settlement", query: {nonuseCache : true}});
      this.$router.push({path: "/operation/order/settlement"});
    }
  }
};
</script>

<style lang='scss'>
.add-activity {
  padding: 30px;
  .main-body {
    min-height: 860px;
    background: #fff;
    .title,
    .breadcrumb-box {
      font-size: 18px;
      padding: 15px;
      border-bottom: 1px solid #eee;
      text-align: left;
    }
    .el-row {
      text-align: left;
    }
    .el-breadcrumb__inner a,
    .el-breadcrumb__inner.is-link {
      font-size: 18px;
      color: #222;
      font-weight: normal;
    }
    .el-breadcrumb__item:last-child .el-breadcrumb__inner {
      font-size: 18px;
      color: #222;
      font-weight: normal;
    }
    .form-box {
      margin: 0 auto;
      margin-top: 30px;
      width: 80%;
      .el-input,
      .el-select {
        width: 200px;
      }
      .name-input {
        width: 250px;
      }
      .el-date-editor {
        width: 400px;
      }
      .el-form-item {
        display: flex;
        justify-content: start;
        align-items: center;
      }
      .el-form-item__label {
        font-size: 16px;
      }
      .el-form-item__content {
        font-size: 16px;
      }
      .choose-brand {
        margin-left: 80px;
      }
      .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
      }
      .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
        object-fit: cover;
      }
      .avatar {
        width: 178px;
        height: 178px;
        display: block;
        object-fit: cover;
      }

      .btn-normal-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 262px;
        height: 60px;
        .avatar {
          width: 262px;
          height: 60px;
          display: block;
          object-fit: cover;
        }

        i {
          width: 262px;
          height: 60px;
          line-height: 50px;
          text-align: center;
          display: inline-block;
        }
      }
    }
    .el-form-item__content {
      line-height: 60px;
    }
    .submit-btn {
      margin-left: 30px;
    }
    .star {
      position: relative;
      .el-form-item__label {
        padding-left: 8px;
      }
      &::before {
        content: "*";
        color: #f56c6c;
        margin-right: 4px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        // left: -2px;
        left: 47px;
        width: 14px;
        height: 14px;
      }
    }
  }
  .store-list {
    display: flex;
    align-items: center;
    button {
      margin-left: 30px;
    }
  }
  .shop-logo {
    height: 50px;
    max-width: 80px;
    object-fit: contain;
  }
  .add-areaselect {
    display: inline-block;
    cursor: pointer;
    color: #3399ff;
    width: 70px;
    text-align: left;
  }
  .btn-box {
    padding: 30px 0;
  }
}

div {
  box-sizing: border-box;
}

.reds {
  color: #ff4956 !important;
}

.grees {
  color: #67c23a !important;
}

.blues {
  color: #3399ff !important;
}

@media screen and (max-width: 1620px) {
  .selItems {
    margin-right: 30px !important;
    .inputs {
      width: 180px !important;
    }
  }
  .phone {
    width: 242px !important;
  }
  .wxnum,
  .companys,
  .results {
    width: 256px !important;
  }
  .state {
    width: 228px !important;
  }
}

@media screen and (max-width: 1480px) {
  .selItems {
    margin-right: 20px !important;
    .inputs {
      width: 160px !important;
      font-size: 13px;
    }
  }
  .phone {
    width: 222px !important;
  }
  .wxnum,
  .companys,
  .results {
    width: 236px !important;
  }
  .state {
    width: 208px !important;
  }
}

@media screen and (max-width: 1390px) {
  .selRows {
    padding: 24px 10px !important;
  }
  .selItems {
    margin-right: 10px !important;
  }
}

.main {
  width: 100%;
  height: 100%;
  padding: 20px;
  font-size: 16px;
  color: #333333;
  .mainTop {
    width: 100%;
    background-color: #ffffff;
    border-radius: 2px;
    margin-bottom: 20px;
    .titles {
      height: 55px;
      border-bottom: 1px solid #eaeaea;
      padding: 0 30px;
      line-height: 55px;
      text-align: left;
    }
    .selRows {
      padding: 24px 30px;
      height: 80px;
      .submitBtn {
        height: 32px;
        line-height: 32px;
        box-sizing: border-box;
        background-color: #3399ff;
        border-radius: 4px;
        width: 64px;
        float: left;
        color: #fff;
        font-size: 14px;
        cursor: pointer;
        margin-left: 20px;
      }
      .selItems {
        height: 32px;
        line-height: 32px;
        box-sizing: border-box;
        margin-right: 30px;
        position: relative;
        float: left;
        .label {
          font-size: 14px;
          height: 32px;
          position: absolute;
          left: 0;
          top: 0;
        }
        .inputs {
          display: block;
          width: 200px;
          height: 32px;
          line-height: 32px;
          box-sizing: border-box;
        }
        .el-select {
          height: 32px;
          line-height: 32px;
        }
      }
      .wxnum {
        .inputs {
          background-color: #ffffff;
          border-radius: 4px;
          input {
            display: block;
            width: 100%;
            height: 100%;
            font-size: 14px;
          }
        }
      }
      .phone {
        width: 262px;
        padding-left: 62px;
        label {
          width: 62px;
        }
        .inputs {
          background-color: #ffffff;
          border-radius: 4px;
          input {
            display: block;
            width: 100%;
            height: 100%;
            font-size: 14px;
          }
        }
      }
      .state {
        width: 248px;
        padding-left: 48px;
        label {
          width: 48px;
        }
      }
      .wxnum,
      .companys,
      .results {
        width: 276px;
        padding-left: 76px;
        label {
          width: 76px;
        }
      }
    }
    margin-bottom: 20px;
  }
  .mainBody {
    width: 100%;
    .tableDataFrame {
      color: #666;
    }
    .userinfoDiv {
      width: 100%;
      height: 44px;
      position: relative;
      padding-left: 54px;
      box-sizing: border-box;
      img {
        position: absolute;
        width: 44px;
        height: 44px;
        border-radius: 50%;
        top: 0;
        left: 0;
        z-index: 1;
      }
      p {
        font-size: 14px;
        line-height: 1;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        &:first-child {
          margin-bottom: 10px;
        }
      }
    }
  }
  .formRows {
    width: 100%;
    height: 100%;
    position: fixed;
    left: 0;
    top: 0;
    background: rgba(0, 0, 0, 0.7);
    z-index: 9999;
    .formBody {
      width: 620px;
      padding: 54px 0 52px;
      background: #fff;
      box-sizing: border-box;
      border-radius: 4px;
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      .titles {
        width: 100%;
        height: 54px;
        line-height: 54px;
        box-sizing: border-box;
        padding: 0 24px;
        font-size: 16px;
        color: #333333;
        position: absolute;
        text-align: left;
        top: 0;
        left: 0;
        border-bottom: 1px solid #eaeaea;
        i {
          position: absolute;
          width: 54px;
          height: 54px;
          line-height: 54px;
          text-align: center;
          right: 0;
          color: #8c8c8c;
          font-size: 12px;
          top: 0;
          cursor: pointer;
        }
      }
      .bodys {
        width: 100%;
        padding: 30px;
        font-size: 14px;
        text-align: left;
        .userRows {
          width: 100%;
          height: 80px;
          background-color: #f5f5f5;
          padding: 20px;
          padding-left: 80px;
          position: relative;
          margin-bottom: 20px;
          img {
            position: absolute;
            width: 48px;
            height: 48px;
            border-radius: 50%;
            left: 20px;
            top: 16px;
          }
          div {
            width: 50%;
            float: left;
            height: 100%;
            p {
              width: 100%;
              line-height: 1;
              text-align: left;
              &:first-child {
                margin-bottom: 7px;
              }
            }
          }
        }
        .txtTitles {
          line-height: 1;
          position: relative;
          margin-bottom: 16px;
          span {
            position: absolute;
            top: 0;
            right: 0;
          }
        }
        .feedbackRows {
          width: 100%;
          padding: 20px;
          background-color: #f5f5f5;
          margin-bottom: 20px;
          .txts {
            line-height: 20px;
            display: block;
            width: 70px;
            text-align: left;
            word-break: break-all;
          }
          .items {
            width: 100%;
            position: relative;
            padding-left: 76px;
            margin-bottom: 17px;
            color: #333;
            font-size: 14px;
            min-height: 14px;
            &:last-child {
              margin: 0;
            }
            span {
              text-align: right;
              display: block;
              width: 70px;
              position: absolute;
              left: 0;
              top: 0;
              color: #666666;
            }
            .imgRows {
              height: 106px;
              .imgItems {
                width: 106px;
                height: 106px;
                float: left;
                margin-right: 6px;
                &:last-child {
                  margin: 0;
                }
                position: relative;
                &:hover .zzcs {
                  opacity: 1;
                }
                img {
                  width: 100%;
                  height: 100%;
                }
                .zzcs {
                  width: 100%;
                  height: 100%;
                  position: absolute;
                  left: 0;
                  top: 0;
                  background: rgba(0, 0, 0, 0.7);
                  opacity: 0;
                  transition-duration: 0.3s;
                  .btns {
                    width: 72px;
                    height: 24px;
                    text-align: center;
                    line-height: 24px;
                    cursor: pointer;
                    color: #fff;
                    font-size: 12px;
                    position: absolute;
                    left: 50%;
                    top: 50%;
                    margin-left: -36px;
                    margin-top: -12px;
                    background-color: #3399ff;
                    border-radius: 12px;
                  }
                }
              }
            }
          }
        }
        .jieguo {
          margin-bottom: 16px;
          height: 14px;
          line-height: 1;
          p {
            width: 50%;
            float: left;
          }
        }
        .he156 {
          height: 156px;
        }
        .fkui {
          width: 100%;
          padding-left: 80px;
          position: relative;
          min-height: 24px;
          line-height: 24px;
          span.txs {
            position: absolute;
            left: 0;
            top: 0;
            color: #666666;
          }
          .rowTxs {
            line-height: 20px;
            word-wrap: break-word;
          }
          .rowInp {
            width: 100%;
            height: 100%;
            background-color: #ffffff;
            border-radius: 4px;
            border: solid 1px #d9d9d9;
            padding: 0 10px;
            padding-bottom: 22px;
            textarea {
              max-width: 100%;
              max-height: 100%;
              min-width: 100%;
              min-height: 100%;
              line-height: 24px;
              border: none;
              font-size: 14px;
              color: #333;
              background: rgba(0, 0, 0, 0);
              resize: none;
            }
            .counts {
              position: absolute;
              right: 10px;
              bottom: 10px;
              font-size: 12px;
              line-height: 1;
              color: #999999;
            }
          }
        }
      }
      .bnts {
        width: 100%;
        text-align: right;
        height: 52px;
        line-height: 52px;
        position: absolute;
        left: 0;
        bottom: 0;
        border-top: 1px solid #eaeaea;
        padding-right: 20px;
      }
    }
  }
  .delView {
    width: 100%;
    height: 100%;
    position: fixed;
    left: 0;
    top: 0;
    background: rgba(0, 0, 0, 0.7);
    z-index: 9999;
    .delRows {
      width: 620px;
      padding: 54px 0 52px;
      background: #fff;
      border-radius: 4px;
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      .conts {
        padding: 40px 72px;
        font-size: 14px;
        line-height: 1;
        color: #333333;
      }
      .titles {
        width: 100%;
        height: 54px;
        line-height: 54px;
        box-sizing: border-box;
        padding: 0 24px;
        font-size: 16px;
        color: #333333;
        position: absolute;
        text-align: left;
        top: 0;
        left: 0;
        border-bottom: 1px solid #eaeaea;
        i {
          position: absolute;
          width: 54px;
          height: 54px;
          line-height: 54px;
          text-align: center;
          right: 0;
          color: #8c8c8c;
          font-size: 12px;
          top: 0;
          cursor: pointer;
        }
      }
      .bnts {
        width: 100%;
        text-align: right;
        height: 52px;
        line-height: 52px;
        position: absolute;
        left: 0;
        bottom: 0;
        border-top: 1px solid #eaeaea;
        padding-right: 20px;
      }
    }
  }
}

.decorCustomer {
  padding: 20px;
}

.contractContent {
  padding-top: 30px;
}

.rejectReson {
  width: 372px;
}

.leftTitle {
  vertical-align: top;
}

.pageCount {
  text-align: right;
  background: #fff;
  padding: 20px;
}

.listTitle {
  font-size: 16px;
  color: #333;
  height: 56px;
  text-align: left;
  line-height: 56px;
  background: #fff;
  margin-top: 20px;
  border-bottom: solid 1px #e8e8e8;
  padding-left: 32px;
  position: relative;
  .right {
    @extend .exportExcel;
  }
}

.exportExcel {
  position: absolute;
  right: 32px;
  top: 0;
  span {
    display: inline-block;
    text-align: center;
    font-size: 14px;
    width: 82px;
    height: 32px;
    line-height: 32px;
    background-color: $theme-color;
    color: #fff;
    border-radius: 4px;
  }
}

.tableDataFrame {
  padding: 0 20px;
  background: #fff;
}

.topBar {
  background: #fff;
  div {
    text-align: left;
  }
  .footer {
    margin-top: 20px;
    border-top: solid 1px #e8e8e8;
    text-align: center;
    padding: 20px 0;
    display: block;
  }
  .barTitle {
    border-bottom: solid 1px #e8e8e8;
    padding-left: 32px;
    font-size: 16px;
    color: #333;
    height: 56px;
    line-height: 56px;
    position: relative;
    .right {
      position: absolute;
      right: 32px;
      top: 0;
      span {
        display: inline-block;
        text-align: center;
        font-size: 14px;
        width: 82px;
        height: 32px;
        line-height: 32px;
        background-color: $theme-color;
        color: #fff;
        border-radius: 4px;
      }
    }
  }
}

.searchOption {
  padding-left: 32px;
  .el-select,
  .pickDate,
  .el-input,
  .el-cascader {
    width: 220px;
    margin-top: 20px;
    margin-right: 10px;
  }
}

.queryBtn {
  height: 30px;
  width: 62px;
  display: inline-block;
  text-align: center;
  line-height: 30px;
  border-radius: 4px;
  color: #fff;
  background: #3a8ee6;
  cursor: pointer;
  font-size: 14px;
  margin-left: 40px;
  margin-right: 10px;
}

.queryReset {
  height: 30px;
  width: 62px;
  font-size: 14px;
  display: inline-block;
  text-align: center;
  line-height: 30px;
  border-radius: 4px;
  border: solid 1px #d9d9d9;
  color: #999;
  cursor: pointer;
}

.ownerInfo {
  margin-top: 20px;
  background: #fff;
  border-radius: 4px;
  height: 214px;
  text-align: left;
  ul {
    height: 64px;
    line-height: 64px;
    border-bottom: solid 1px #e8e8e8;
    li {
      display: inline-block;
      margin-left: 50px;
      margin-right: 50px;
    }
  }
  div {
    background: #fff;
    height: 150px;
  }
}

.ordermsg {
  background: #fafafa;
  color: #666;
  padding: 10px;
  margin: 0 !important;
  li {
    padding-right: 10px;
    width: 13%;
    display: inline-block;
    white-space: normal;
  }
  p {
    height: 34px;
    white-space: nowrap;
  }
}
.orderTotal {
  background: #fafafa;
  border-collapse: collapse;
  border: none;
  width: 100%;
  thead {
    border: solid 1px #dcdcdc;
    th {
      background: #fafafa;
      height: 40px;
      text-align: center;
      vertical-align: middle;
      padding-left: 10px;
    }
  }
  tbody {
    td {
      background: #fafafa;
      border: solid 1px #dcdcdc;
      height: 60px;
      vertical-align: middle;
      padding-left: 6px;
    }
  }
}
.tableData {
  margin-top: 15px;
  background: #fafafa;
  border-collapse: collapse;
  border: none;
  width: 100%;
  height: 50px;
  text-align: left;
  font-weight: normal;
  thead {
    border: solid 1px #dcdcdc;
  }
  th {
    height: 40px;
    text-align: center;
    vertical-align: middle;
    padding-left: 10px;
    font-weight: normal;
    color: #666;
    border: none;
    &:nth-of-type(2) {
      padding-left: 10px;
    }
  }
  tbody {
    background: #fff;
    td {
      border: none;
      border-bottom: solid 1px #dcdcdc;
      height: 76px;
      text-align: center;
      vertical-align: middle;
      padding-left: 10px;
    }
  }
}

.reBack {
  margin-top: 15px;
  margin-left: 20px;
  margin-bottom: 15px;
  float: left;
  b {
    color: #ff6419;
  }
}
.orderDetailMsg {
  background: #fff;
  padding: 20px;
}
.printOrder {
  margin-top: 25px;
  .el-button {
    border: solid #ff6419 1px;
    color: #ff6419;
    width: 130px;
  }
}
</style>