<template>
  <div>
    <div class="reBack">
      <b>返回上一级 | 订单管理列表 ></b> 详情
    </div>
    <div class="orderContent">
      <div class="orderDetail" style="background: #fff;">
        <div class="orderTop clearfix">
          <div>
            <el-button type="text">打印</el-button>
          </div>
          <ul>
            <li>发货</li>
            <li>退货</li>
            <li>关闭订单</li>
          </ul>
        </div>
      </div>

      <div class="orderFrame">
        <!--订单详情-->
        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">订单详情</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>
        <div class="orderMsg">
          <table class="orderMsgTable" border="1" cellspacing="0" cellpadding="0">
            <thead>
              <tr>
                <th class="tdWidth180">订单号</th>
                <th class="tdWidth180">创建时间</th>
                <th class="tdWidth120">商品总金额</th>
                <th class="tdWidth120">配送费</th>
                <th class="tdWidth120">订金金额</th>
                <th class="tdWidth120">订单总金额</th>
                <th class="tdWidth120">订单状态</th>
                <th class="tdWidth120">支付状态</th>
                <th class="tdWidth280 orderInfoModify">
                  配送信息
                  <span>复制 修改</span>
                </th>
                <th class="tdWidth232">物流信息</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>12345678912346789</td>
                <td>2018-05-18 15:00:00</td>
                <td>￥10000</td>
                <td>￥10000</td>
                <td>￥10000</td>
                <td>￥10000</td>
                <td>已发货</td>
                <td>已支付</td>
                <td class="distriInfo">
                  <span>姓 名：lshclshc</span>
                  <span>电 话：13800013800</span>
                  <span>邮 编：518000</span>
                  <span>省 份：广东省</span>
                  <span>城 市：深圳市</span>
                  <span>区 域：福田区</span>
                  <span>地 址：福田区福华路幸福城福瑞大厦福居阁1688室</span>
                </td>
                <td class="distriInfo">
                  <span>物流公司：lshclshc</span>
                  <span>货运单号：13800013800</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!--订单明细-->
        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">订单明细</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>
        <div class="orderMsg">
          <table class="orderMsgTable" border="1" cellspacing="0" cellpadding="0">
            <thead>
              <tr>
                <th class="tdWidth50">序号</th>
                <th class="tdWidth140">商品编号</th>
                <th>商品</th>
                <th class="tdWidth140">商品规格</th>
                <th class="tdWidth140">数量</th>
                <th class="tdWidth140">单价</th>
                <th class="tdWidth140">合计</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td class="tdMiddle">序号</td>
                <td class="tdMiddle">商品编号</td>
                <td>
                  <div class="product clear">
                    <div>
                      <img src="../../../assets/images/no_img.jpg" width="80" height="80" alt>
                    </div>
                    <div>
                      <p>商品规格上海市普陀区金沙江路</p>
                    </div>
                  </div>
                  <div class="product clear">
                    <div>
                      <img src="../../../assets/images/no_img.jpg" width="80" height="80" alt>
                    </div>
                    <div>
                      <p>商品规格上海市普陀区金沙江路</p>
                    </div>
                  </div>
                </td>
                <td class="tdMiddle">商品规格</td>
                <td class="tdMiddle">数量</td>
                <td class="tdMiddle">单价</td>
                <td class="tdMiddle">合计</td>
              </tr>
              <tr>
                <td class="tdMiddle">序号</td>
                <td class="tdMiddle">商品编号</td>
                <td>
                  <div class="product clear">
                    <div>
                      <img src="../../../assets/images/no_img.jpg" width="80" height="80" alt>
                    </div>
                    <div>
                      <p>商品规格上海市普陀区金沙江路</p>
                    </div>
                  </div>
                </td>
                <td class="tdMiddle">商品规格</td>
                <td class="tdMiddle">数量</td>
                <td class="tdMiddle">单价</td>
                <td class="tdMiddle">合计</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!--支付信息-->
        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">支付信息</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>
        <div class="orderMsg">
          <table class="orderMsgTable" border="1" cellspacing="0" cellpadding="0">
            <thead>
              <tr>
                <th class="tdWidth232">订单号</th>
                <th class="tdWidth232">支付方式</th>
                <th class="tdWidth232">支付金额</th>
                <th class="tdWidth232">支付时间</th>
                <th class="tdWidth140">支付状态</th>
                <th>备注</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>123456789987456123</td>
                <td>微信支付</td>
                <td>15000</td>
                <td>2018-05-18 15:30:30</td>
                <td>支付成功</td>
                <td>备注相关信息显示在此框内。</td>
              </tr>
              <tr>
                <td>123456789987456123</td>
                <td>微信支付</td>
                <td>15000</td>
                <td>2018-05-18 15:30:30</td>
                <td>支付成功</td>
                <td>备注相关信息显示在此框内。</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!--订单日记-->
        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">订单日记</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>
        <div class="orderMsg">
          <table class="orderMsgTable" border="1" cellspacing="0" cellpadding="0">
            <thead>
              <tr>
                <th class="tdWidth232">序号</th>
                <th class="tdWidth232">操作人</th>
                <th class="tdWidth232">操作人类型</th>
                <th class="tdWidth232">操作时间</th>
                <th>日志</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>1</td>
                <td>admin</td>
                <td>管理员</td>
                <td>2018-05-18 15:30:30</td>
                <td>【创建订单】创建订单创建订单创建订单创建订单</td>
              </tr>
              <tr>
                <td>1</td>
                <td>admin</td>
                <td>管理员</td>
                <td>2018-05-18 15:30:30</td>
                <td>【创建订单】创建订单创建订单创建订单创建订单</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄"
        }
      ]
    };
  }
};
</script>

<style lang="scss" scoped>
.orderDetail {
  background: #fff;
  .orderTop {
    border-bottom: solid 1px #ddd;
    height: 58px;
    margin-top: 15px;
    div {
      margin-top: 10px;
      float: left;
      margin-left: 15px;
    }
    ul {
      float: right;
      li {
        margin-top: 24px;
        display: inline-block;
        height: 14px;
        line-height: 14px;
        padding: 0 15px;
        border-right: solid 1px #ddd;
        &:last-of-type {
          border-right: none;
        }
      }
    }
  }
}
.orderContent {
  background: #fff;
}
.tdWidth180 {
  width: 180px;
}
.tdWidth120 {
  width: 120px;
}
.tdWidth280 {
  width: 280px;
}
.tdWidth232 {
  width: 232px;
}
.tdWidth50 {
  width: 50px;
}
.tdWidth140 {
  width: 140px;
}
.orderFrame {
  padding: 20px;
}
.tdMiddle {
  vertical-align: middle !important;
}
.product {
  border-top: solid 1px #ddd;
  &:nth-of-type(1) {
    border-top: none;
  }

  div {
    float: left;
    p {
      display: table-cell;
      max-width: 450px;
      color: #333;
      min-width: 200px;
      height: 80px;
      vertical-align: middle;
      word-break: break-all;
      word-wrap: break-word;
      line-height: 22px;
      span {
        color: #999;
      }
    }
    &:last-of-type {
      margin-left: 5px;
    }
  }

  img {
    margin: 5px;
  }
}
.clear {
  clear: both;
}
.orderMsg {
  background: #fff;
  table {
    margin-top: 15px;
    border-collapse: collapse;
    border: none;
    width: 100%;
    text-align: left;
    thead {
      tr {
        background: #fafafa;
        height: 30px;
        border: none;
        th {
          font-weight: normal;
          color: #999;
          padding-left: 10px;
        }
      }
    }
    tbody {
      td {
        vertical-align: top;
        min-height: 40px;
        padding: 10px 0 10px 10px;
      }
    }
  }
}
.orderInfoModify {
  position: relative;
  span {
    position: absolute;
    right: 10px;
    color: #ff6419;
    cursor: pointer;
  }
}

.distriInfo {
  span {
    display: block;
    margin-top: 5px;
    padding-left: 45px;
    text-indent: -45px;
  }
}

.reBack {
  b {
    color: #ff6419;
  }
}
</style>