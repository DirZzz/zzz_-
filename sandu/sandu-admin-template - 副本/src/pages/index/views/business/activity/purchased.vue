<template>
  <div class="purchasabled-wrap">
    <div class="select-box">
      <div class="left-box">
        <div class="select-item">
          <area-select
            @provinceCode="getProvinceCode"
            @cityCode="getCityCode"
            @areaCode="getAreaCode"
            ref="areas"
          ></area-select>
        </div>
        <div class="select-item">
          <el-select clearable filterable v-model="shopId" placeholder="请选择门店">
            <el-option
              v-for="(item,index) in shop"
              :key="index"
              :label="item.name"
              :value="item.type"
            ></el-option>
          </el-select>
        </div>
      </div>
      <div class="option-btn">
        <div>
          <el-button
            round
            type="primary"
            size="medium"
            style="margin-left:20px;"
            @click="getSearchList"
          >搜索</el-button>
          <el-button round @click="resetList" plain>重置</el-button>
        </div>
        <el-button
          plain
          round
          type="primary"
          class="export-btn"
          :disabled="tableData.length==0?'disabled':false"
          @click="exportExcelList"
        >导出数据</el-button>
      </div>
    </div>
    <div class="table-page">
      <div class="desc">用户信息列表（共{{page.total}}条数据）</div>
      <div class="table-box">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column label="用户昵称">
            <template slot-scope="scope">
              <div>{{scope.row.nickName?scope.row.nickName:'匿名用户'}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="mobile" label="手机号"></el-table-column>
          <el-table-column prop="provinceCity" label="地区">
            <template slot-scope="scope">
              <div>{{scope.row.provinceCity&&scope.row.provinceCity!=' '?scope.row.provinceCity:'--'}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="brandName" label="品牌"></el-table-column>
          <el-table-column prop="shopName" label="报名门店"></el-table-column>
          <el-table-column prop="gmtCreate" label="预约时间"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="text" @click="dialogShow(scope.$index,scope.row,'remark')">备注</el-button>
              <el-button
                type="text"
                @click="dialogShow(scope.$index,scope.row,'setting')"
                v-if="scope.row.isPay == 0"
              >置为无效</el-button>
              <el-button
                type="text"
                @click="dialogShow(scope.$index,scope.row,'setting')"
                v-if="scope.row.isPay == 1"
              >置为有效</el-button>
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
    </div>
    <!-- 备注 -->
    <div class="dialog-box">
      <el-dialog
        title="备注"
        width="35%"
        :close-on-click-modal="false"
        center
        :visible.sync="remarkDialog"
      >
        <div>
          <div>历史信息：</div>
          <div class="remark-box">
            <div class="remark-list">
              <div class="remark-item" v-if="remarkList.remark">
                <div class="item-left">{{remarkList.remark}}</div>
                <div class="item-right">{{remarkList.gmtModified}}</div>
              </div>
            </div>
            <el-input placeholder="请输入您的备注信息" v-model="remarkMsg" clearable></el-input>
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" round class="active-btn" @click="remarkDialog = false">取消</el-button>
          <el-button type="primary" round class="active-btn" @click="updateRemark">确定</el-button>
        </div>
      </el-dialog>
    </div>
    <!--置为无效  -->
    <div class="dialog-box">
      <el-dialog
        :title="payText"
        width="35%"
        :close-on-click-modal="false"
        center
        :visible.sync="settingDialog"
      >
        <div class="pay-text">是否确定将此用户信息{{payText}}？</div>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" round class="active-btn" @click="settingDialog = false">取消</el-button>
          <el-button type="primary" round class="active-btn" @click="settingSave">确定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import areaSelect from "../../../components/areaSelect.vue";
export default {
  data() {
    return {
      tableData: [],
      shop: [],
      shopId: null,
      provinceCode: "",
      cityCode: "",
      areaCode: "",
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      remarkList: {},
      remarkDialog: false,
      remarkMsg: "",
      rowId: null,
      settingDialog: false,
      payText: ""
    };
  },
  components: {
    areaSelect
  },
  created() {
    this.getPurchasedList({
      activityId: this.$route.query.id,
      start: this.page.currentPage,
      limit: this.page.pageSize
    });
    setTimeout(() => {
      this.getShopIdName();
    }, 200);
  },
  methods: {
    exportExcelList() {
      let params = {
        activityId: this.$route.query.id,
        start: this.page.currentPage,
        limit: this.page.pageSize,
        shopId: this.shopId ? this.shopId : null,
        provinceCode: this.provinceCode ? this.provinceCode : null,
        cityCode: this.cityCode ? this.cityCode : null,
        areaCode: this.areaCode ? this.areaCode : null
      };
      this.API2.exportExcelList(params).then(res => {
        let url = window.URL.createObjectURL(res);
        let link = document.createElement("a");
        link.style.display = "none";
        link.href = url;
        link.setAttribute("download", "用户信息列表.xls");
        document.body.appendChild(link);
        link.click();
      });
    },
    getProvinceCode(val) {
      this.provinceCode = val;
    },
    getCityCode(val) {
      this.cityCode = val;
    },
    getAreaCode(val) {
      this.areaCode = val;
    },

    getShopIdName() {
      this.API2.getShopIdName({ activityId: this.$route.query.id }).then(
        res => {
          this.shop = res.obj;
        }
      );
    },
    getPurchasedList(params) {
      this.API2.getList(params).then(res => {
        if (res.success) {
          this.tableData = res.datalist;
          this.page.total = res.totalCount;
        }
      });
    },
    dialogShow(index, row, type) {
      this.rowId = row.id;
      console.log("id", this.rowId, row.id);
      this.rowIspay = row.isPay;
      if (type == "remark") {
        this.remarkDialog = true;
        this.getRemark();
      } else {
        this.settingDialog = true;
        if (row.isPay == 0) {
          this.payText = "置为无效";
        }
        if (row.isPay == 1) {
          this.payText = "置为有效";
        }
      }
    },
    settingSave() {
      let isPay = 1;
      switch (this.rowIspay) {
        case 0:
          isPay = 1;
          break;
        case 1:
          isPay = 0;
          break;
      }
      this.API2.changeIsUsed({ id: this.rowId, isPay: isPay }).then(res => {
        console.log(res);
        if (res.success) {
          this.$message.success("操作成功");
          this.settingDialog = false;
          this.getPurchasedList({
            activityId: this.$route.query.id,
            start: this.page.currentPage,
            limit: this.page.pageSize
          });
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    getSearchList() {
      let params = {
        activityId: this.$route.query.id,
        start: 1,
        limit: this.page.pageSize,
        shopId: this.shopId ? this.shopId : null,
        provinceCode: this.provinceCode ? this.provinceCode : null,
        cityCode: this.cityCode ? this.cityCode : null,
        areaCode: this.areaCode ? this.areaCode : null
      };
      this.getPurchasedList(params);
    },
    resetList() {
      this.shopId = "";
      this.provinceCode = "";
      this.cityCode = "";
      this.areaCode = "";
      this.page.currentPage = 1;
      this.$refs.areas.clearCode();
      this.$refs.areas.echoData(
        this.provinceCode,
        this.cityCode,
        this.areaCode
      );
      this.getPurchasedList({
        activityId: this.$route.query.id,
        start: this.page.currentPage,
        limit: this.page.pageSize
      });
    },
    getRemark() {
      this.API2.getRemark({ id: this.rowId }).then(res => {
        if (res.success) {
          this.remarkList = res.obj;
        }
      });
    },
    updateRemark() {
      if (this.remarkMsg) {
        this.API2.updateRemark({
          id: this.rowId,
          remark: this.remarkMsg
        }).then(res => {
          if (res.success) {
            this.remarkMsg = "";
            this.$message.success(res.message);
            this.getRemark();
            this.remarkDialog = false;
          }
        });
      } else {
        this.$message.warning("备注信息不能为空！");
      }
    },

    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getPurchasedList({
        activityId: this.$route.query.id,
        start: this.page.currentPage,
        limit: this.page.pageSize
      });
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.getPurchasedList({
        activityId: this.$route.query.id,
        start: this.page.currentPage,
        limit: this.page.pageSize
      });
    }
  }
};
</script>

<style lang='scss' scoped>
.purchasabled-wrap {
  min-height: 700px;
  padding-bottom: 50px;
  .el-tabs__header .is-top {
    background: #fff;
  }
  .is-round {
    padding: 10px 35px;
  }
  .select-box {
    display: flex;
    justify-content: space-between;
    flex-direction: column;
    border-bottom: 1px solid #e4e7ed;
    background: #fff;
    .left-box {
      display: flex;
      padding:20px 30px 30px 30px;
      border-bottom: 1px solid #ccc;
    }
  }
  .option-btn {
    display: flex;
    justify-content: center;
    padding: 20px 0;
    .export-btn {
      position: absolute;
      right: 30px;
    }
  }
  .desc {
    padding: 20px;
  }
  .pay-text {
    text-align: center;
  }
  .pagenation-box {
    display: flex;
    justify-content: center;
    padding: 30px 0;
  }
  .table-page {
    margin-top: 20px;
    background: #fff;
    .table-box {
      background: #fff;
      border: 2px solid #e0e0e0;
    }
  }

  .remark-box {
    padding: 20px 30px;
    .remark-list {
      color: #666;
      .remark-item {
        display: flex;
        justify-content: space-between;
        padding: 5px 0;
      }
    }
  }
}
</style>