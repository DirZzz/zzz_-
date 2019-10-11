<template>
  <div class="add-store-wrap">
    <!-- <div class="title">添加门店</div> -->
    <div class="add-store-select">
      <div class="select-title">数据筛选</div>
      <div class="select-content">
        <div class="select-box">
          <div class="select-item">
            <div class="label">手机号</div>
            <el-input clearable v-model="addStore.mobile" placeholder="请输入手机号"></el-input>
          </div>
          <div class="select-item">
            <div class="label">店铺名称</div>
            <el-input clearable v-model="addStore.shopName" placeholder="请输入店铺名"></el-input>
          </div>
          <div class="select-item">
            <div class="label">地区</div>
            <el-row>
              <area-select
                ref="areas"
                @provinceCode="getProvinceCode"
                @cityCode="getCityCode"
                @areaCode="getAreaCode"
              ></area-select>
            </el-row>
          </div>
        </div>
        <div class="getlist-btn">
          <el-button type="primary" size="medium" @click="searchShopList">查询</el-button>
          <el-button type="primary" size="medium" @click="resetList">重置</el-button>
        </div>
      </div>
    </div>
    <div class="table">
      <el-table
        ref="multipleTable"
        border
        v-loading="loading"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        @select="handleSelect"
        @select-all="handleSelectAll"
      >
        <el-table-column type="selection" align="center" label="全选"></el-table-column>
        <el-table-column prop="shopName" align="center" label="店铺名称"></el-table-column>
        <el-table-column prop="contactName" align="center" label="联系人"></el-table-column>
        <el-table-column prop="contactPhone" align="center" label="手机号"></el-table-column>
        <el-table-column align="center" label="店铺logo">
          <template slot-scope="scope">
            <img class="shop-logo" :src="sourceBaseUrl+scope.row.picPath" alt />
          </template>
        </el-table-column>
        <el-table-column prop="address" align="center" label="区域">
          <template slot-scope="scope">
            <div>{{scope.row.provinceName}}{{scope.row.cityName}}{{scope.row.areaName}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="shopAddress" align="center" label="详细地址"></el-table-column>
      </el-table>
      <div class="pagenation-box">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.currentPage"
          :page-sizes="[5,10, 20, 30, 40,50]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
        ></el-pagination>
      </div>
      <div class="option-btn">
        <el-button type="primary" class="btn" @click="addStoreSava">确定</el-button>
        <el-button type="primary" class="btn" @click="cancelDialog">取消</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import areaSelect from "../../components/areaSelect.vue";
export default {
  props: {
    companyId: 0
  },
  data() {
    return {
      sourceBaseUrl: "",
      loading: true,
      addStore: {
        mobile: "",
        shopName: "",
        provinceCode: "",
        cityCode: "",
        areaCode: ""
      },
      selectionList: [],
      tableData: [],
      multipleSelection: [],
      page: {
        currentPage: 1,
        pageSize: 5,
        total: 0
      }
    };
  },
  components: {
    areaSelect
  },
  created() {
    let basePath = process.env;
    this.sourceBaseUrl = basePath.sourceBaseUrl;
    this.getShopList({
      companyId: this.companyId,
      limit: this.page.pageSize,
      start: this.page.currentPage
    });
  },
  methods: {
    getProvinceCode(val) {
      this.addStore.provinceCode = val;
    },
    getCityCode(val) {
      this.addStore.cityCode = val;
    },
    getAreaCode(val) {
      this.addStore.areaCode = val;
    },
    resetList() {
      let vm = this;
      this.addStore = {
        mobile: "",
        shopName: "",
        provinceCode: "",
        cityCode: "",
        areaCode: ""
      };
      this.page.pageSize = 5;
      this.page.currentPage = 1;
      this.$refs.areas.echoData(vm.provinceCode, vm.cityCode, vm.areaCode);
      this.getShopList({
        companyId: this.companyId,
        limit: this.page.pageSize,
        start: this.page.currentPage
      });
    },
    searchShopList(){
     if(!(this.addStore.mobile&&this.addStore.shopName&&this.addStore.provinceCode&&this.addStore.cityCode&&this.addStore.areaCode)){
        this.page.currentPage = 1;
      }
      this.searchList();
    },
    searchList() {
      let params = {
        companyId: this.companyId,
        start: this.page.currentPage,
        limit: this.page.pageSize,
        mobile:this.addStore.mobile?this.addStore.mobile:null,
        shopName:this.addStore.shopName?this.addStore.shopName:null,
        provinceCode:this.addStore.provinceCode?this.addStore.provinceCode:null,
        cityCode:this.addStore.cityCode?this.addStore.cityCode:null,
        areaCode:this.addStore.areaCode?this.addStore.areaCode:null,
      };
      this.getShopList(params);
    },
    getShopList(params) {
      this.API.shopList(params).then(res => {
        (this.loading = false), (this.tableData = res.datalist);
        this.page.total = res.totalCount;
      });
    },
    handleSelect(selection, row) {
      if (this.selectionList.includes(row.id)) {
        this.selectionList.forEach((item, index) => {
          if (item == row.id) {
            this.selectionList.splice(index, 1);
          }
        });
      } else {
        this.selectionList.push(row.id);
      }
    },
    handleSelectAll(selection) {
      this.multipleSelection = selection;
      if (selection.length > 0) {
        this.multipleSelection.forEach(item => {
          this.selectionList.push(item.id);
        });
      } else {
        this.selectionList = [];
      }
    },

    handleSizeChange(val) {
      this.page.pageSize = val;
      this.searchList();
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.searchList();
    },
    addStoreSava() {
      this.$emit("addStoreList", this.selectionList);
      // this.$refs.multipleTable.clearSelection();
    },
    cancelDialog() {
      this.$emit("cancelPage", null);
    }
  }
};
</script>
<style lang='scss' scoped>
.add-store-wrap {
  background: #fff;
  color: #333;
  .title {
    text-align: left;
    margin: 30px 0;
    width: 100%;
    font-size: 18px;
    height: 55px;
    line-height: 55px;
    border-bottom: 1px solid #eaeaea;
  }
  .add-store-select {
    border: 1px solid #eee;
    .select-title {
      padding: 10px;
      background: #f8f8f8;
      color: #888;
      font-size: 16px;
      text-align: left;
      border-bottom: 1px solid #eee;
    }
    .select-content {
      padding: 10px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .select-box {
        display: flex;
        justify-content: space-between;
        .select-item {
          display: flex;
          align-items: center;
          margin-left: 30px;
          &:first-child {
            margin-left: 0;
          }
          .label {
            width: 60px;
            text-align: left;
          }
          .el-input {
            width: 135px;
          }
          .el-select {
            width: 80px;
          }
        }
      }
    }
  }
  .table {
    margin-top: 30px;
  }
  .pagenation-box {
    margin-top: 30px;
    display: flex;
    justify-content: center;
  }
  .shop-logo {
    height: 100px;
    max-width: 150px;
  }
  .option-btn {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 50px;
    .btn {
      margin-right: 30px;
    }
  }
  .getlist-btn {
    display: flex;
    button {
      margin-left: 15px;
    }
  }
}
</style>