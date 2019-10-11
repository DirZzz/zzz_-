<template>
  <div>
    <v-nav :nav="nav"></v-nav>
    <div class="activeUserData">
      <header class="headerT">
        <h1>用户数据</h1>
      </header>
      <div>
        <div class="searchFrame">
          <el-input v-model="wxId" size="small" clearable placeholder="微信ID"></el-input>
          <el-input v-model="wxNick" size="small" clearable placeholder="微信昵称"></el-input>
          <el-input v-model="phone" size="small" clearable placeholder="手机号"></el-input>
          <!--10砍价成功  20已发货-->
          <el-select
            size="small"
            v-model="isStatus"
            clearable
            @change="getUserData"
            placeholder="配送状态"
          >
            <el-option
              :value="item.value"
              v-for="(item,index) in statusList"
              :label="item.name"
              :key="index"
            >{{item.name}}</el-option>
          </el-select>

          <el-select
            size="small"
            v-model="payStatus"
            clearable
            @change="getUserData"
            placeholder="支付状态"
          >
            <el-option
              :value="item.value"
              v-for="(item,index) in payList"
              :label="item.name"
              :key="index"
            >{{item.name}}</el-option>
          </el-select>
          <el-select
            size="small"
            v-if="appId === 'wx42e6b214e6cdaed3'"
            v-model="isDecHome"
            clearable
            @change="getUserData"
            placeholder="是否装进我家"
          >
            <!--0未体验  10已体验-->
            <el-option value="10" label="已体验">已体验</el-option>
            <el-option value="0" label="未体验">未体验</el-option>
          </el-select>

          <el-select
            size="small"
            v-model="distributionShop"
            clearable
            filterable
            placeholder="分配店铺"
            @change="distributionChange"
          >
            <el-option
              v-for="item in distributionList"
              :key="item.id"
              :label="item.shopName"
              :value="item.id"
            ></el-option>
          </el-select>
          <div class="search-btn">
            <el-button type="primary" round size="small" @click="searchUserData">搜索</el-button>
            <el-button round size="small" @click="searchReset">重置</el-button>
          </div>
        </div>
      </div>
      <el-dialog
        title="发货"
        width="540px"
        class="sendDialog"
        :close-on-click-modal="false"
        :visible.sync="sendProductVisible"
      >
        <p>
          快递公司：
          <el-input size="small" v-model="sendCompany"></el-input>
        </p>
        <p>
          运&nbsp;单&nbsp; 号：
          <el-input size="small" v-model="sendNo"></el-input>
        </p>
        <span slot="footer">
          <el-button size="small" round type="primary" @click="sureSend">确定</el-button>
          <el-button size="small" round @click="sendProductVisible = false">取消</el-button>
        </span>
      </el-dialog>
      <el-dialog
        title="编辑运单"
        width="540px"
        class="sendDialog"
        :close-on-click-modal="false"
        :visible.sync="editProductVisible"
      >
        <p>
          快递公司：
          <el-input size="small" v-model="sendCompany"></el-input>
        </p>
        <p>
          运&nbsp;单&nbsp; 号：
          <el-input size="small" v-model="sendNo"></el-input>
        </p>
        <span slot="footer">
          <el-button size="small" round type="primary" @click="editSureSend">确定</el-button>
          <el-button size="small" round @click="editProductVisible = false">取消</el-button>
        </span>
      </el-dialog>
    </div>
    <div class="activeUserData">
      <div class="exportBar">
        <el-button round plain type="primary" @click="exportBtn">导出</el-button>
      </div>
      <div class="listData">
        <el-table
          :data="userListData"
          style="width: 100%"
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(255, 255, 255, 0.8)"
          tooltip-effect="dark"
        >
          <el-table-column label width="55"></el-table-column>
          <el-table-column prop="openId" label="微信信息" width="380px">
            <template slot-scope="scope">
              <div class="wxInfo">
                <div>
                  <img :src="scope.row.headPic" width="46" height="46" alt>
                </div>
                <div>
                  <p>微信昵称:{{scope.row.nickname}}</p>
                  <p>微信ID:{{scope.row.openId}}</p>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="gmtCreate" label="参与时间"></el-table-column>
          <el-table-column prop="inviteCutCount" label="砍价人数"></el-table-column>
          <el-table-column prop="inviteCutPriceSum" label="砍价金额"></el-table-column>
          <el-table-column
            v-if="appId === 'wx42e6b214e6cdaed3'"
            prop="decorateStatus"
            label="是否体验装进我家"
          >
            <template slot-scope="scope">{{scope.row.decorateStatus == 0 ? '未体验' : '已体验'}}</template>
          </el-table-column>
          <el-table-column prop="status" label="配送状态">
            <template slot-scope="scope">
              <p v-if="scope.row.completeStatus == 10 && scope.row.shipmentStatus==0">砍价成功</p>
              <p v-if="scope.row.completeStatus == 10 && scope.row.shipmentStatus==10">已发货</p>
              <p v-if="scope.row.completeStatus == 10 && scope.row.shipmentStatus==20">已自提</p>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="支付状态">
            <template slot-scope="scope">
              <p
                v-if="scope.row.payStatus == 10"
              >{{ (scope.row.completeStatus == 10 && scope.row.productMinPrice==0 && scope.row.mobile) ? '支付成功' : '未支付' }}</p>
              <p v-if="scope.row.payStatus == 20">支付成功</p>
              <p v-if="scope.row.payStatus == 30">支付失败</p>
            </template>
          </el-table-column>
          <el-table-column label="支付金额（元）">
            <template slot-scope="scope">
              <span>{{scope.row.payStatus == 20 ? scope.row.productMinPrice : ''}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="shopName" label="分配店铺"></el-table-column>
          <el-table-column prop="address" width="300px" label="收货地址">
            <template slot-scope="scope">
              <!-- <div class="receiver">-->
              <p>收货人：{{scope.row.receiver}}</p>
              <p>联系电话：{{scope.row.mobile}}</p>
              <p>收货地址：{{scope.row.address}}</p>
              <!-- </div>-->
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <!--shipType 10 自提 20快递-->
              <el-button
                type="text"
                @click="confirm(scope.row)"
                v-if="scope.row.status == 50 && scope.row.shipType==10"
              >完成</el-button>
              <el-button
                type="text"
                @click="confirm(scope.row)"
                v-if="scope.row.status == 10 && scope.row.shipType==10 && scope.row.mobile"
              >完成</el-button>
              <!--<el-button type="text" v-if="scope.row.status == 30" disabled>已自提</el-button>-->
              <el-button
                type="text"
                @click="editSend(scope.row)"
                v-if="scope.row.status == 50 && scope.row.shipType==20"
              >发货</el-button>
              <el-button
                type="text"
                @click="send(scope.row)"
                v-if="scope.row.status == 10 && scope.row.shipType==20 && scope.row.mobile"
              >发货</el-button>
              <el-button type="text" @click="editSend(scope.row)" v-if="scope.row.status == 20">编辑运单</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pageFrame">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="query.pageNum"
            :page-sizes="[3,10, 50, 100, 200,500]"
            :page-size="query.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          ></el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "active-user-data",
  data() {
    return {
      wxNick: "",
      wxId: "",
      phone: "",
      isStatus: "",
      isDecHome: "", // 是否装时我家
      sendCompany: "", // 快递公司
      sendNo: "", // 运单号
      userDataId: "",
      userListData: [],
      nav: [
        { nav: "砍价活动列表", url: "/activelist" },
        { nav: "用户数据", url: "" }
      ],
      getRegId: "",
      total: 0,
      loading: false,
      query: {
        pageSize: 10,
        pageNum: 1
      },
      sendProductVisible: false,
      editProductVisible: false,
      appId: "",
      distributionShop: "",
      distributionList: [],
      statusList: [
        { value: 10, name: "砍价完成" },
        { value: 20, name: "已发货" },
        { value: 30, name: "已自提" }
      ],
      payList: [
        { value: 10, name: "待支付" },
        { value: 20, name: "支付成功" },
        { value: 30, name: "支付失败" }
      ],
      payStatus: "",
      shipType: ""
    };
  },
  created() {
    this.userDataId = this.$route.params.id;
    this.appId = this.$route.params.appId;
    this.getUserData();
    this.API2.obtainCompanyShop({
      companyId: sessionStorage.getItem("companyID")
    }).then(res => {
      if (res.success) {
        this.distributionList = res.obj;
      } else {
        this.$message.error(res.message);
      }
    });
  },
  methods: {
    exportBtn() {
      // 删除多余参数
      let queryCondition = Object.assign({}, this.query, {
        actId: this.userDataId,
        openId: this.wxId,
        nickname: this.wxNick,
        status: this.isStatus, //状态 10砍价成功  20已发货
        decorateStatus: this.isDecHome, //是否体验装进我家,0未体验  10已体验
        payStatus: this.payStatus,
        mobile: this.phone
      });
      for (let key in queryCondition) {
        if (
          (queryCondition[key] === "" ||
            queryCondition[key] === undefined ||
            queryCondition[key] === null) &&
          queryCondition[key] !== 0
        ) {
          delete queryCondition[key];
        }
        if (key === "productCategory") delete queryCondition[key];
      }
      this.API2.exportExcelWxActBargainReg(queryCondition).then(data => {
        var t = new Blob([data], {
            type: "application/vnd.ms-excel;charset=utf-8"
          }),
          n = URL.createObjectURL(t),
          a = document.createElement("a");
        (a.style.display = "none"),
          (a.href = n),
          a.setAttribute("download", "用户数据.xls"),
          document.body.appendChild(a),
          a.click();
      });
    },
    searchUserData() {
      this.query.pageNum = 1;
      this.getUserData();
    },
    searchReset() {
      this.wxNick = "";
      this.wxId = "";
      this.isStatus = "";
      this.isDecHome = "";
      this.payStatus = "";
      this.distributionShop = "";
      this.phone = "";
      this.getUserData();
    },
    send(item) {
      this.getRegId = item.regId;
      this.sendProductVisible = true;
      this.sendCompany = "";
      this.sendNo = "";
      this.shipType = item.shipType;
    },
    sureSend() {
      let params = new FormData();
      params.append("regId", this.getRegId);
      params.append("carrier", this.sendCompany);
      params.append("shipmentNo", this.sendNo);
      params.append("shipType", this.shipType);
      this.API.activeSend(params).then(res => {
        this.sendProductVisible = false;
        this.$message.success(res.message);
        this.getUserData();
      });
    },
    // 编辑货运单号
    editSend(item, type) {
      this.getRegId = item.regId;
      this.shipType = item.shipType;
      console.log(item);
      if (type != 1) {
        this.editProductVisible = true;
      }
      // 获取详情
      this.API.activeSendDetail({
        regId: this.getRegId,
        shipType: item.shipType
      }).then(res => {
        if (type != 1) {
          this.sendCompany = res.obj.carrier;
          this.sendNo = res.obj.shipmentNo;
        } else {
          this.getUserData();
        }
      });
    },
    confirm(item) {
      this.$confirm("是否确认完成", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = new FormData();
          params.append("regId", item.regId);
          params.append("shipType", item.shipType);
          this.API.activeSend(params).then(res => {
            this.$message.success(res.message);
            this.getUserData();
          });
        })
        .catch(() => {});
    },
    editSureSend() {
      let params = new FormData();
      params.append("regId", this.getRegId);
      params.append("carrier", this.sendCompany);
      params.append("shipmentNo", this.sendNo);
      params.append("shipType", this.shipType);
      this.API.activeSend(params).then(res => {
        this.editProductVisible = false;
        this.$message.success(res.message);
        this.getUserData();
      });
    },
    getUserData() {
      // 删除多余参数
      let queryCondition = Object.assign({}, this.query, {
        actId: this.userDataId,
        openId: this.wxId,
        nickname: this.wxNick,
        status: this.isStatus, //状态 10砍价成功  20已发货
        decorateStatus: this.isDecHome, //是否体验装进我家,0未体验  10已体验
        payStatus: this.payStatus,
        mobile: this.phone
      });
      for (let key in queryCondition) {
        if (
          (queryCondition[key] === "" ||
            queryCondition[key] === undefined ||
            queryCondition[key] === null) &&
          queryCondition[key] !== 0
        ) {
          delete queryCondition[key];
        }
        if (key === "productCategory") delete queryCondition[key];
      }

      this.API.activeUserData(queryCondition).then(res => {
        console.log(res, "userdata");
        this.loading = false;
        this.userListData = res.datalist;
        this.total = res.totalCount;
      });
    },
    distributionChange(val) {
      this.query.shopId = val;
      this.getUserData();
      //console.log(val)
    },
    // 分页模块
    handleSizeChange: function(size) {
      this.query.pageSize = size;
      this.getUserData();
    },
    handleCurrentChange: function(currentPage) {
      this.query.pageNum = currentPage;
      this.loading = true;
      this.getUserData();
    }
  }
};
</script>

<style lang="scss" scoped>
.headerT {
  padding: 20px;
  border-bottom: solid 1px #e8e8e8;
  color: #666;
  h1 {
    font-size: 20px;
  }
}
.receiver {
  text-align: left;
}
.wxInfo {
  div {
    float: left;
    text-align: left;
    img {
      border-radius: 30px;
      background: #f5f5f5;
      border: none;
      margin-right: 10px;
    }
  }
}
.sendDialog {
  p {
    margin-bottom: 15px;
  }
  .el-select,
  .el-input {
    width: 250px;
  }
}
.searchFrame {
  margin: 20px;
  .el-input,
  .el-select {
    margin-right: 10px;
    width: 200px;
  }
  .el-button {
    width: 120px;
    margin-left: 30px;
  }
}

.activeUserData {
  background: #fff;
  overflow: hidden;
  margin-top: 20px;
  .exportBar {
    margin: 20px;
  }
}
.listData {
  margin: 20px;
}
.search-btn {
  margin-top: 20px;
  text-align: center;
}
.pageFrame {
  background: #fff;
  height: 110px;
  padding-top: 40px;
  box-sizing: border-box;
  text-align: center;
}
</style>
