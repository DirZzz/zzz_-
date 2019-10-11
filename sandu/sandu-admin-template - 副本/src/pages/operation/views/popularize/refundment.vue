<template>
  <div class="refundment-vue">
    <transition name="fade">
      <div class="decorCustomer">
        <header class="topBar">
          <div class="barTitle">
            <span>退款管理</span>
          </div>
          <div class="searchOption">
            <el-input
              placeholder="请输入业主姓名"
              v-model="searchOption.userName"
              size="small"
              clearable
              style="width: 222px"
              @blur="getRefundList"
            ></el-input>
            <el-input
              placeholder="请输入业主联系电话"
              v-model="searchOption.mobile"
              size="small"
              clearable
              style="width: 222px"
              @blur="getRefundList"
            ></el-input>
            <el-select
              v-model="searchFinanceReceiptsStatus"
              @change="getRefundList"
              clearable
              size="small"
              placeholder="请选择退款状态"
            >
              <el-option
                v-for="item in financeReceiptsStatus"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>

            <el-input
              placeholder="请输入退款装企"
              v-model="companyName"
              size="small"
              clearable
              style="width: 222px"
            ></el-input>
          </div>
          <div class="footer">
            <span class="queryBtn" @click="getRefundList">查询</span>
            <span class="queryReset" @click="resetFrom">重置</span>
          </div>
        </header>
        <section>
          <div class="listTitle">
            客户信息
            <div class="right">
              <el-button
                @click="handleDownload"
                size="small"
                type="primary"
                :disabled="!multipleSelection.length"
              >导出Excel</el-button>
            </div>
          </div>
          <div class="tableDataFrame">
            <el-table
              :data="tableDataList"
              :header-cell-style="rowStyle"
              @selection-change="handleSelectionChange"
              style="padding-top:20px"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(255, 255, 255, 0.8)"
              tooltip-effect="dark"
            >
              <el-table-column type="selection"></el-table-column>
              <el-table-column prop="listIndex" width="50px" label="序号"></el-table-column>
              <el-table-column prop="userName" label="名称"></el-table-column>
              <el-table-column prop="mobile" label="联系电话"></el-table-column>
              <el-table-column prop="cityName" label="城市"></el-table-column>
              <el-table-column prop="companyName" label="退款装修公司"></el-table-column>
              <el-table-column prop="refundStatusStr" label="退款状态"></el-table-column>
              <el-table-column prop="refundReason" label="退款原因"></el-table-column>
              <el-table-column prop="refundRejectReason" label="驳回原因"></el-table-column>
              <el-table-column label="操作" width="200px">
                <template slot-scope="scope" v-if="scope.row.refundStatusStr == '待审核'">
                  <el-button
                    type="text"
                    @click="passThrough(scope.row.id)"
                    v-if="permission('decorate:refundment:manage')"
                  >审核通过</el-button>
                  <el-button
                    type="text"
                    @click="rejectDialog(scope.row.id)"
                    v-if="permission('decorate:refundment:manage')"
                  >驳回</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pageCount">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page.sync="query.page"
                :page-sizes="[10, 50, 100, 200,500]"
                :page-size="query.limit"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
              ></el-pagination>
            </div>
          </div>
        </section>

        <el-dialog
          style="text-align: left;"
          title="审核通过"
          :visible.sync="passThroughDialog"
          width="620px"
        >
          <div class="contractContent">请确认是否符合退款条件？</div>
          <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="passThroughDialog = false">取 消</el-button>
            <el-button size="small" type="primary" @click="passThroughSure">确 定</el-button>
          </div>
        </el-dialog>

        <el-dialog
          style="text-align: left;"
          title="驳回"
          :visible.sync="rejectVisibleDialog"
          width="620px"
        >
          <div class="contractContent">
            <span class="leftTitle">
              <font color="red" class="leftTitle">*</font>驳回原因：
            </span>
            <el-input
              type="textarea"
              :rows="5"
              placeholder="请输入原因"
              v-model="rejectTxt"
              class="rejectReson"
            ></el-input>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="rejectVisibleDialog = false">取 消</el-button>
            <el-button size="small" type="primary" @click="rejectSure">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  data() {
    return {
      total: 0,
      multipleSelection: [],
      rowId: "",
      companyName: "",
      rejectTxt: "",
      passThroughDialog: false,
      rejectVisibleDialog: false,
      searchOption: { userName: "", mobile: "" },
      searchFinanceReceiptsStatus: "",
      financeReceiptsStatus: [
        { value: 0, label: "待审核" },
        { value: 1, label: "已退款" },
        { value: 2, label: "驳回" }
      ],

      tableDataList: [],
      query: {
        page: 1,
        limit: 10
      }
    };
  },
  methods: {
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
    passThrough(id) {
      this.rowId = id;
      this.passThroughDialog = true;
    },
    passThroughSure() {
      /*this.$confirm('确定审核通过?', '提示', {
                    confirmButtonText: '确定',
                    type: 'info',
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    center: true
                }).then(() => {

                })*/

      this.API.updateRefund({
        id: this.rowId,
        operateType: 0
      }).then(res => {
        this.getRefundList();
        this.passThroughDialog = false;
        this.$message.success("审核通过");
      });
    },
    rejectDialog(id) {
      this.rowId = id;
      this.rejectVisibleDialog = true;
    },
    rejectSure() {
      /*this.$confirm('确定驳回?', '提示', {
                    confirmButtonText: '确定',
                    type: 'info',
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    center: true
                }).then(() => {

                })*/
      if (!this.rejectTxt) {
        this.$message.error("请输入驳回原因!");
        return;
      }
      this.API.updateRefund({
        id: this.rowId,
        operateType: 1,
        refundRejectReason: this.rejectTxt
      }).then(res => {
        this.rejectVisibleDialog = false;
        this.getRefundList();
        this.$message.success("驳回成功");
      });
    },
    // 分页模块
    handleSizeChange: function(size) {
      this.query.limit = size;
      this.getRefundList();
    },
    resetFrom() {
      this.companyName = "";
      this.searchFinanceReceiptsStatus = "";
      this.searchOption.userName = "";
      this.searchOption.mobile = "";
      this.getRefundList();
    },
    handleCurrentChange: function(currentPage) {
      this.query.page = currentPage;
      this.loading = true;
      this.getRefundList();
    },
    // 导出excel 表
    handleDownload() {
      require.ensure([], () => {
        const { export_json_to_excel } = require("@/vendor/Export2Excel");
        const tHeader = [
          "序号",
          "名称",
          "联系电话",
          "城市",
          "退款装修公司",
          "退款状态",
          "退款原因",
          "驳回原因"
        ];
        const filterVal = [
          "listIndex",
          "userName",
          "mobile",
          "cityName",
          "companyName",
          "refundStatusStr",
          "refundReason",
          "refundRejectReason"
        ];
        const list = this.multipleSelection;
        const data = this.formatJson(filterVal, list);
        export_json_to_excel(tHeader, data, "退款管理文件");
      });
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => v[j]));
    },
    //-------------------------------------------
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 客户列表
    getRefundList() {
      let formDatas = Object.assign({}, this.query, {
        companyName: this.companyName,
        refundStatus: this.searchFinanceReceiptsStatus,
        companyId: "",
        userName: this.searchOption.userName,
        mobile: this.searchOption.mobile
      });

      for (let key in formDatas) {
        if (formDatas[key] !== 0 && formDatas[key] == "") {
          delete formDatas[key];
        }
      }

      this.API.refundList(formDatas).then(res => {
        if (res.list) {
          res.list.map((item, index) => {
            item.listIndex = index + 1;
          });
          this.total = res.total;
          this.tableDataList = res.list;
        } else {
          this.total = 0;
          this.tableDataList = [];
        }
      });
    }
  },
  created() {
    this.getRefundList();
  }
};
</script>

<style scoped lang="scss">
@import "../../assets/css/common.scss";
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
</style>
<style lang="scss">
.refundment-vue {
  .el-dialog__header {
    border-bottom: solid 1px #e8e8e8;
  }
  .el-dialog__footer {
    border-top: solid 1px #e8e8e8;
  }
  .el-dialog__body {
    padding: 0 20px 20px 77px;
    color: #606266;
    font-size: 14px;
  }
}
</style>