<template>
  <div class="user-management">
    <div class="main-body">
      <div class="title">用户预约信息</div>
      <div class="usermanage-table-box">
        <div class="option-btn">
          <div class="option-btn-left">
            <el-input placeholder="用户名称" v-model="userName" style="width: 160px"></el-input>
            <el-input placeholder="手机号" v-model="mobile" style="width: 160px"></el-input>
            <el-select v-model="shopId" clearable filterable placeholder="预约店铺">
              <el-option
                v-for="item in shopList"
                :key="item.value"
                :label="item.shopName"
                :value="item.id"
              ></el-option>
            </el-select>
            <el-select v-model="companyId" clearable filterable placeholder="预约企业">
              <el-option
                v-for="item in comList"
                :key="item.type"
                :label="item.name"
                :value="item.type"
              ></el-option>
            </el-select>
            <el-date-picker
              v-model="timeBox"
              type="datetimerange"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              align="right"
            ></el-date-picker>
            <el-select v-model="originPlatform" clearable filterable placeholder="平台">
              <el-option
                v-for="item in originPlatformArr"
                :key="item.value"
                :label="item.platformName"
                :value="item.value"
              ></el-option>
            </el-select>
            <el-select v-model="location" clearable filterable placeholder="位置">
              <el-option
                v-for="item in locationArr"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              ></el-option>
            </el-select>
            <el-select v-model="traceResult" clearable filterable placeholder="跟进结果">
              <el-option
                v-for="item in traceResultArr"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <el-select v-model="traceStatus" clearable filterable placeholder="跟进状态">
              <el-option
                v-for="item in traceStatusArr"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="btn">
          <el-button type="primary" @click="searchList">搜索</el-button>
          <el-button @click="reset">重置</el-button>
          <div class="option-btn-right">
            <el-button type="primary" @click="exportListData">导出数据</el-button>
          </div>
        </div>
        <div class="table">
          <el-table border v-loading="loading" :data="userAppointmentList" style="width: 100%">
            <el-table-column prop="date" label="序号" align="center">
              <template slot-scope="scope">
                <div>{{(page.currentPage-1)*page.pageSize+(scope.$index+1)}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="originPlatformStr" label="平台" align="center"></el-table-column>
            <el-table-column prop="appointmentPositionStr" label="位置" align="center"></el-table-column>
            <el-table-column prop="userName" label="用户昵称" align="center"></el-table-column>
            <el-table-column prop="mobile" label="手机号" align="center"></el-table-column>
            <el-table-column prop="shopName" label="预约店铺" align="center"></el-table-column>
            <el-table-column prop="shopTypeStr" label="店铺类型" align="center"></el-table-column>
            <el-table-column prop="companyName" label="预约企业" align="center"></el-table-column>
            <el-table-column prop="gmtCreate" label="预约时间" align="center"></el-table-column>
            <el-table-column prop="traceStatusStr" label="跟进状态" align="center"></el-table-column>
            <el-table-column prop="traceResultStr" label="跟进结果" align="center"></el-table-column>
            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <el-button type="text" @click="followUp(scope.row)">跟进</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-box">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="page.currentPage"
              :page-sizes="[10, 20, 30,40, 50]"
              :page-size="page.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="page.total"
            ></el-pagination>
          </div>
          <el-dialog title="跟进" :visible.sync="dialogFormVisible" :center="true">
            <el-form label-width="120px" :model="form">
              <el-form-item label="跟进状态:">
                <el-select v-model="form.traceStatus" clearable placeholder="请选择跟进状态">
                  <el-option
                    v-for="item in traceStatusArr"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="跟进结果:">
                <el-select v-model="form.traceResult" clearable placeholder="请选择跟进结果">
                  <el-option
                    v-for="item in traceResultArr"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="备注:">
                <el-input
                  type="textarea"
                  maxlength="200"
                  v-model="form.remark"
                  style="width: 400px"
                ></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="submitForm()">确 定</el-button>
            </div>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        traceResult: "",
        traceStatus: "",
        remark: "",
        id: ""
      },
      companyId: "",
      appointmentTimeStart: "",
      appointmentTimeEnd: "",
      dialogFormVisible: false,
      formLabelWidth: "120px",
      userName: "",
      mobile: "",
      loading: true,
      shopList: [],
      timeBox: [],
      shopId: "",
      shopType: "",
      shopTypeList: [],
      comList: [],
      value: "",
      originPlatform: "", //平台
      location: "", //位置
      traceResult: "",
      traceResultArr: [
        { label: "有效", value: 20 },
        { label: "无效", value: 10 }
      ],
      traceStatus: "",
      traceStatusArr: [
        { label: "待跟进", value: 10 },
        { label: "跟进中", value: 20 },
        { label: "已跟进", value: 30 }
      ],
      locationArr: [],
      userAppointmentList: [],
      originPlatformArr: [
        { platformName: "随选网", value: "selectDecoration" },
        { platformName: "报价小程序", value: "quotedDecoration" }
      ],
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      }
    };
  },
  created() {
    // this.obtainShopType("shopType");
    this.getUserAppointmentList();
    this.getLocationList("reservationPosition");
  },
  activated() {
    this.findShopList();
    this.getRegistCompList();
  },
  components: {},
  methods: {
    // 获取厂商列表
    getRegistCompList() {
      this.API.getRegistCompList().then(res => {
        if (res.success) {
          this.comList = res.obj;
        }
      });
    },
    getUserAppointmentList() {
      this.loading = true;
      let params = this.buildParameter();
      this.API.obtinUserAppointmentList(params).then(res => {
        this.loading = false;
        this.userAppointmentList = res.datalist;
        this.page.total = res.totalCount;
      });
    },
    findShopList() {
      this.API.getAllShopList().then(res => {
        if (res.success) {
          this.shopList = res.obj;
        }
      });
    },
    obtainShopType(type) {
      this.API.getAllShopTypeList({ type: type }).then(res => {
        if (res.success) {
          this.shopTypeList = res.obj;
        }
      });
    },
    reset() {
      this.userName = "";
      this.mobile = "";
      this.shopId = "";
      this.companyId = "";
      this.appointmentTimeStart = "";
      this.appointmentTimeEnd = "";
      this.originPlatform = "";
      this.location = "";
      this.traceResult = "";
      this.traceStatus = "";
      this.timeBox = [];
      this.searchList();
    },
    submitForm() {
      let params = {
        id: this.form.id,
        traceStatus: this.form.traceStatus || "",
        traceResult: this.form.traceResult || "",
        remark: this.form.remark == undefined ? "" : this.form.remark
      };
      this.API.modifyFollowUpResult(params).then(res => {
        if (res.success) {
          this.$message({
            message: res.message,
            type: "success"
          });
          this.dialogFormVisible = false;
          this.getUserAppointmentList();
        } else {
          this.$message({
            message: res.message,
            type: "warning"
          });
          this.dialogFormVisible = false;
        }
      });
    },
    followUp(item) {
      this.form.id = item.id;
      this.form.traceStatus = item.traceStatus;
      this.form.traceResult = item.traceResult;
      this.form.remark = item.remark;
      this.dialogFormVisible = true;
    },
    //获取位置信息列表
    getLocationList(type) {
      this.API.getAllShopTypeList({ type: type }).then(res => {
        if (res.success) {
          this.locationArr = res.obj;
        }
      });
    },
    // 每页条数改变
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getUserAppointmentList();
    },
    // 改变页码
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.getUserAppointmentList();
    },
    // 搜索
    searchList() {
      this.page.currentPage = 1;
      this.getUserAppointmentList();
    },
    buildParameter() {
      let params = {
        limit: this.page.pageSize,
        start: this.page.currentPage
      };

      if (this.shopId) {
        params.shopId = this.shopId;
      }
      if (this.shopType) {
        params.shopType = this.shopType;
      }
      if (this.originPlatform) {
        params.originPlatform = this.originPlatform;
      }
      if (this.traceStatus) {
        params.traceStatus = this.traceStatus;
      }
      if (this.traceResult) {
        params.traceResult = this.traceResult;
      }
      if (this.location) {
        params.appointmentPosition = this.location;
      }
      if (this.userName) {
        params.userName = this.userName;
      }
      if (this.mobile) {
        params.mobile = this.mobile;
      }
      if (this.timeBox) {
        params.appointmentTimeStart = this.timeBox[0];
      }
      if (this.timeBox) {
        params.appointmentTimeEnd = this.timeBox[1];
      }
      if (this.companyId) {
        params.companyId = this.companyId;
      }
      return params;
    },
    // 数据导出
    exportListData() {
      let params = this.buildParameter();
      this.API.exportListData(params).then(res => {
        let url = window.URL.createObjectURL(res);
        let link = document.createElement("a");
        link.style.display = "none";
        link.href = url;
        link.setAttribute("download", "用户预约数据源.xls");
        document.body.appendChild(link);
        link.click();
      });
    }
  }
};
</script>
<style lang='scss' scoped>
.submit-btn {
  margin: 80px 0 50px 50px;
}
.user-management {
  padding: 30px;
  .main-body {
    min-height: 860px;
    background: #fff;
    .title {
      font-size: 18px;
      padding: 15px;
      border-bottom: 1px solid #eee;
      text-align: left;
    }
    .usermanage-table-box {
      padding: 30px;
      .option-btn {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .option-btn-left {
          display: flex;
          flex-wrap: wrap;
          .el-select,
          .el-input,
          .el-date-editor {
            margin-right: 15px;
            margin-bottom: 15px;
          }
        }
      }
      .btn {
        text-align: center;
        position: relative;
        margin-top: 20px;
        .option-btn-right {
          position: absolute;
          right: 0;
          top: 0;
        }
      }
      .table {
        margin-top: 30px;
      }
    }
  }
  .pagination-box {
    margin-top: 30px;
  }
  .nickName {
    width: 150px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .dialog-footer {
    text-align: center;
  }
}
</style>
