<template>
  <div>
    <div class="reBack">
      <b>
        <router-link to="/orderlist" style="color: #FF6419">返回上一级 | 订单管理列表</router-link>>
      </b> 详情
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
            用户备注信息: <span style="padding-left:20px">{{orderDetail.remark}}</span>
        </div>
      </form>
      <div class="printOrder" v-if="isPer('miniapp:order:export')">
        <el-button round @click.native="printMethods">打印</el-button>
      </div>
    </div>
  </div>
</template>

<script>
"use strict";
import qs from "qs";
import printJS from "print-js";
import { tools } from "@/utils/tools";
export default {
  data() {
    return {
      detailID: "",
      orderDetail: {},
      shippingTypeStr: ["", "快递", "上门自提"],
    };
  },
  methods: {
    isPer(per) {
      let permiss = qs.parse(sessionStorage.getItem("loginUser"));
      return this.isMainPer(per, permiss);
    },
    printMethods() {
      window.printJS({
        printable: "printJS-form",
        type: "html",
        font_size: "12px",
        header: "订单详细"
      });
    },
    getOrderDetail(id) {
      this.API.spGetOrderDetail({ id: id }).then(res => {
        if (res) {
          this.orderDetail = res.obj;
        }
      });
    }
  },

  created() {
    let id = this.$route.params.id;
    this.getOrderDetail(id);
  }
};
</script>

<style lang="scss" scoped>
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
      text-align: left;
      padding-left: 10px;
    }
  }
  tbody {
    td {
      background: #fafafa;
      border: solid 1px #dcdcdc;
      height: 60px;
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
    text-align: left;
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
      text-align: left;
      padding-left: 10px;
    }
  }
}

.reBack {
  margin-bottom: 15px;
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