<template>
  <div class="user-management">
    <div class="main-body">
      <div class="title">参与活动用户管理</div>
      <div class="usermanage-table-box">
        <div class="option-btn">
          <el-input placeholder="用户名称" label="用户名称" v-model="userName" style="width: 210px"></el-input>

          <el-input placeholder="手机号" label="手机号" v-model="mobile" style="width: 210px"></el-input>

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
          <el-select v-model="shopId" clearable filterable placeholder="报名门店">
            <el-option
              v-for="item in shopList"
              :key="item.id"
              :label="item.shopName"
              :value="item.id"
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
          <el-select
            v-model="companyId"
            clearable
            filterable
            @change="changeCompanyId"
            @clear="clearCompanyId"
            placeholder="请选择厂商"
          >
            <el-option
              v-for="item in comList"
              :key="item.type"
              :label="item.name"
              :value="item.type"
            ></el-option>
          </el-select>
          <el-select
            clearable
            filterable
            v-model="brandId"
            @clear="clearBrandId"
            placeholder="请选择品牌"
          >
            <el-option
              v-for="item in brandList"
              :key="item.type"
              :label="item.name"
              :value="item.type"
            ></el-option>
          </el-select>

          <el-select
            v-model="citySelect.provide"
            @change="getProvideCode(citySelect.provide)"
            clearable
            placeholder="请选择省"
          >
            <el-option
              v-for="(item, index) in provide"
              :key="item.areaCode"
              :label="item.areaName"
              :value="item.areaCode"
            ></el-option>
          </el-select>

          <el-select
            clearable
            v-model="citySelect.city"
            @change="getCityCode(citySelect.city)"
            placeholder="请选择市"
          >
            <el-option
              v-for="(item,index) in citySelects"
              :key="item.areaCode"
              :label="item.areaName"
              :value="item.areaCode"
            ></el-option>
          </el-select>
        </div>
      </div>
      <div class="btn">
        <el-button type="primary" @click="searchList">搜索</el-button>
        <el-button @click="reset">重置</el-button>
        <div class="option-btn-right">
          <el-button type="primary" @click="registExportExcel">导出数据</el-button>
        </div>
      </div>
      <div class="table">
        <el-table border v-loading="loading" :data="userList" style="width: 100%">
          <el-table-column prop="date" label="序号" align="center">
            <template slot-scope="scope">
              <div>{{(page.currentPage-1)*page.pageSize+(scope.$index+1)}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="activityName" label="活动名称" align="center"></el-table-column>
          <el-table-column prop="originPlatformStr" label="平台" align="center"></el-table-column>
          <el-table-column prop="activityLocation" label="位置" align="center"></el-table-column>
          <el-table-column prop="nickName" align="center" label="微信号" width="160">
            <template slot-scope="scope">
              <div
                class="nickName"
                :title="scope.row.nickName"
              >{{scope.row.nickName?scope.row.nickName:'匿名用户'}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="userName" label="用户昵称" align="center"></el-table-column>
          <el-table-column prop="mobile" label="手机号" align="center"></el-table-column>
          <el-table-column label="地区" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.provinceCity?scope.row.provinceCity:'未知'}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="companyName" label="厂商" align="center"></el-table-column>
          <el-table-column prop="brandName" label="品牌" align="center"></el-table-column>
          <el-table-column prop="shopName" label="报名门店" align="center"></el-table-column>
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
          <el-form :model="form" label-width="120px">
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
              <el-input type="textarea" maxlength="200" v-model="form.remark" style="width: 400px"></el-input>
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
      timeBox: [],
      shopId: "",
      shopList: [],
      appointmentTimeStart: "",
      appointmentTimeEnd: "",
      dialogFormVisible: false,
      formLabelWidth: "120px",
      userName: "",
      mobile: "",
      loading: true,
      comList: [],
      brandList: [],
      companyId: "",
      brandId: "",
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
      userList: [],
      originPlatformArr: [
        { platformName: "随选网", value: "selectDecoration" },
        { platformName: "报价小程序", value: "quotedDecoration" }
      ],
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      citySelects: [],
      provide: [],
      citySelect: {
        provide: "",
        city: ""
      }
    };
  },
  created() {
    this.getRegistList();
    this.getLocationList();
    this.getArea("0", "市");
  },
  activated() {
    this.getRegistBrandList();
    this.getRegistCompList();
    this.getShopList();
  },

  components: {},
  methods: {
    getShopList() {
      this.API.getAllShopList().then(res => {
        if (res.success) {
          this.shopList = res.obj;
        }
      });
    },
    submitForm() {
      let formData = new FormData();
      formData.append("id", this.form.id || "");
      formData.append("traceStatus", this.form.traceStatus || "");
      formData.append("traceResult", this.form.traceResult || "");
      formData.append(
        "remark",
        this.form.remark == undefined ? "" : this.form.remark
      );
      this.API.modifyTraceResult(formData).then(res => {
        if (res.success) {
          this.$message({
            message: res.message,
            type: "success"
          });
          this.dialogFormVisible = false;
          this.getRegistList();
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
    // 区域选择-------------此处省市区调用让人误导，肖总的锅
    getArea(code, type) {
      this.API.areaList({ areaCode: code }).then(res => {
        if (res) {
          if (type == "市") {
            this.provide = res.datalist;
          }
          if (type == "区") {
            this.citySelects = res.datalist;
          }
        }
      });
    },
    getProvideCode(code) {
      this.citySelect.city = "";
      this.citySelect.area = "";
      this.citySelect.origin = "";
      this.citySelects = [];
      this.areaSelect = [];
      this.originSelect = [];
      this.getArea(code, "区");
    },
    getCityCode(code) {
      // this.citySelect.area =''
      // this.citySelect.origin =''
      // this.areaSelect = []
      // this.originSelect = []
      // this.getArea(code,'市')
    },
    //获取位置信息列表
    getLocationList() {
      this.API.getlocationList().then(res => {
        if (res.success) {
          this.locationArr = res.obj;
        }
      });
    },
    reset() {
      this.userName = "";
      this.mobile = "";
      this.shopId = "";
      this.companyId = "";
      this.brandId = "";
      this.appointmentTimeStart = "";
      this.appointmentTimeEnd = "";
      this.originPlatform = "";
      this.location = "";
      this.traceResult = "";
      this.traceStatus = "";
      (this.citySelect.provide = ""),
        (this.citySelect.city = ""),
        (this.timeBox = []);
      this.searchList();
    },
    // 获取厂商列表
    getRegistCompList() {
      this.API.getRegistCompList().then(res => {
        if (res.success) {
          this.comList = res.obj;
        }
      });
    },
    // 获取品牌商列表
    getRegistBrandList() {
      this.API.getRegistBrandList({ companyId: this.companyId }).then(res => {
        console.log(res);
        if (res.success) {
          this.brandList = res.obj;
        }
      });
    },
    // 厂商id变化
    changeCompanyId() {
      if (this.companyId) {
        this.brandList = [];
        this.brandId = "";
      }
      this.getRegistBrandList();
    },
    // 品牌选择框被点击
    brandFocus() {
      //@focus="brandFocus"
      if (!this.companyId) {
        this.$message({
          message: "请先选择厂商",
          type: "warning"
        });
      }
      if (this.companyId && this.brandList.length == 0) {
        this.$message({
          message: "该厂商品牌为空，请重新选择厂商",
          type: "warning"
        });
      }
    },
    // 清除品牌id
    clearBrandId() {
      this.brandId = "";
    },
    // 清除厂商id
    clearCompanyId() {
      this.companyId = "";
      this.brandList = [];
      this.brandId = "";
    },
    // 获取列表
    getRegistList() {
      this.loading = true;
      let params = this.buildParameter();
      this.API.getRegistList(params).then(res => {
        if (res.success) {
          this.loading = false;
          this.userList = res.datalist;
          this.page.total = res.totalCount;
        }
      });
    },
    buildParameter() {
      let params = {
        limit: this.page.pageSize,
        start: this.page.currentPage
      };
      if (this.companyId) {
        params.companyId = this.companyId;
      }
      if (this.brandId) {
        params.brandId = this.brandId;
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
        params.activityEntry = this.location;
      }
      if (this.userName) {
        params.userName = this.userName;
      }
      if (this.mobile) {
        params.mobile = this.mobile;
      }
      if (this.citySelect.provide) {
        params.provinceCode = this.citySelect.provide;
      }
      if (this.citySelect.city) {
        params.cityCode = this.citySelect.city;
      }
      if (this.timeBox) {
        params.appointmentTimeStart = this.timeBox[0];
      }
      if (this.timeBox) {
        params.appointmentTimeEnd = this.timeBox[1];
      }
      if (this.shopId) {
        params.shopId = this.shopId;
      }
      return params;
    },
    // 每页条数改变
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getRegistList();
    },
    // 改变页码
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.getRegistList();
    },
    // 搜索
    searchList() {
      this.page.currentPage = 1;
      this.getRegistList();
    },
    // 数据导出
    registExportExcel() {
      let params = this.buildParameter();
      this.API.registExportExcel(params).then(res => {
        let url = window.URL.createObjectURL(res);
        let link = document.createElement("a");
        link.style.display = "none";
        link.href = url;
        link.setAttribute("download", "参与活动用户管理表.xls");
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
.searchCondition {
  padding-left: 30px;
  padding-bottom: 20px;
  .el-select,
  .el-input {
    width: 210px;
  }
  .el-col {
    width: 200px;
    margin-right: 15px;
    padding-top: 10px;
  }
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
        flex-wrap: wrap;
        .el-select,
        .el-input,
        .el-date-editor {
          margin-bottom: 15px;
          margin-right: 15px;
        }
      }
    }
    .table {
      margin-top: 30px;
    }
    .btn {
      text-align: center;
      position: relative;
      .option-btn-right {
        position: absolute;
        right: 20px;
        top: 0;
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
}
</style>
