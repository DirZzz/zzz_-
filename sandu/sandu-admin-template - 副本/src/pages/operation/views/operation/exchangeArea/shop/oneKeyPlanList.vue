<template>
  <div>
    <el-row :gutter="24" class="searchCondition">
      <div style="height: 115px">
        <el-col :span="3">
          <el-select v-model="classProgramme" @change="GoPlan" clearable placeholder="请选择方案">
            <el-option value="oneProgramme" label="单个方案/组合方案"></el-option>
            <el-option value="allProgramme" label="全屋方案"></el-option>
          </el-select>
        </el-col>

        <el-col :span="3">
          <el-select
            v-model="query.isPublish"
            @change="page"
            filterable
            clearable
            placeholder="请选择发布状态"
            :disabled="true"
          >
            <el-option
              v-for="(item,index) in isPublish"
              :key="index"
              :label="item.name"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <contains-not-open-product @change="page" v-model="query.containsSecrecyFlag"></contains-not-open-product>
        </el-col>
        <el-col :span="3">
          <el-select
            v-model="query.origin"
            @change="page"
            filterable
            clearable
            placeholder="请选择方案来源"
          >
            <el-option value="deliver" label="企业交付"></el-option>
            <el-option value="diy" label="内部制作"></el-option>
            <el-option value="share" label="分享方案"></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select v-model="selectBrand" @change="page" filterable clearable placeholder="请选择品牌">
            <el-option
              v-for="(item,index) in brandDrop"
              :key="index"
              :label="item.name"
              :value="item.id"
            >{{item.name}}</el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-input v-model="query.planName" clearable placeholder="请输入方案名称"></el-input>
        </el-col>
        <el-col :span="3">
          <el-select v-model="query.spaceCommonName" @change="page" clearable placeholder="请选择空间类型">
            <el-option
              v-for="item in spaceCommonName"
              :key="item.id"
              :label="item.name"
              :value="item.name"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select @change="page" v-model="query.chargeType" clearable placeholder="请选择是否免费">
            <el-option value="0" label="是"></el-option>
            <el-option value="1" label="否"></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select
            @change="page"
            v-model="query.planGroupStyleId"
            filterable
            clearable
            popper-class="plan-style-css"
            placeholder="请选择方案风格"
          >
            <el-option
              v-for="item in planStyles"
              :key="item.groupId"
              :label="item.styleName"
              :value="item.groupId"
            ></el-option>
          </el-select>
        </el-col>
      </div>
      <div class="searchFrame">
        <el-button round class="UnifiedsearchBtn" type="primary" @click="page">搜索</el-button>
        <el-button
          round
          class="UnifiedsearchBtn"
          style="background:#f5f5f5;margin-left:20px;"
          @click="resetInput"
        >重置</el-button>
      </div>
    </el-row>
    <div class="operationButton" v-if="isRadio">
      <el-button type="text" class="outline" @click="handleCheckPlan">确认选中</el-button>
      <el-button type="text" class="refresh" @click="navigatorBack">返回</el-button>
    </div>
    <div class="operationButton" v-else>
      <el-button
        type="text"
        class="outline"
        v-if="isPer('biz:solution:exhibition')"
        @click="handleBatchPushaway"
      >批量发布</el-button>
      <el-button size="small" type="text" @click="refreshData" class="refresh">刷新</el-button>
    </div>
    <div class="planList">
      <el-table
        :data="list"
        style="width: 100%;"
        v-loading="loadingList"
        :highlight-current-row="isRadio"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.8)"
        ref="multipleTable"
        @selection-change="handleSelectionChange"
        @current-change="handleSelectionChange"
        tooltip-effect="dark"
      >
        <el-table-column
          v-if="!isRadio"
          type="selection"
          label="全选"
          :selectable="selectable"
          align="center"
          width="75"
          title="全选"
        ></el-table-column>
        <el-table-column prop="planCode" label="编号" width="180"></el-table-column>
        <el-table-column prop="picPath" label="缩略图" width="180">
          <template slot-scope="scope">
            <div v-if="scope.row.picPath">
              <img
                @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.picPath)"
                style="cursor:pointer"
                :src="BASE_URL.sourceBaseUrl+scope.row.picPath"
                width="80"
              >
            </div>
            <div class="nonImg" v-else>无缩略图</div>
          </template>
        </el-table-column>
        <el-table-column prop="planName" label="名称"></el-table-column>
        <el-table-column prop="spaceTypeName" label="空间类型"></el-table-column>
        <el-table-column prop="brandName" label="品牌">
          <template slot-scope="scope">
            <p v-if="scope.row.brandName" class="remarkOverEllipsis">{{scope.row.brandName}}</p>
            <p v-else>———</p>
          </template>
        </el-table-column>
        <el-table-column prop="designStyleName" label="风格"></el-table-column>
        <el-table-column prop="solutionType" label="方案类型"></el-table-column>
        <el-table-column prop="planPrice" label="版权费(度币)">
          <template slot-scope="scope">
            <p>{{scope.row.chargeType == 0?'免费' : scope.row.planPrice}}</p>
          </template>
        </el-table-column>
        <el-table-column prop="origin" align="center" label="来源">
          <template slot-scope="scope">
            <div v-if="scope.row.origin">
              <div v-if="scope.row.origin === 'diy'">内部制作</div>
              <div v-if="scope.row.origin === 'deliver'">企业交付</div>
              <div v-if="scope.row.origin === 'share'">分享方案</div>
              <div v-if="scope.row.origin === 'huxing'">内部制作</div>
            </div>
            <div v-else>内部制作</div>
          </template>
        </el-table-column>
        <el-table-column prop="isPublish" label="发布状态">
          <template slot-scope="scope">
            <span v-if="scope.row.companyShopDesignPlanisDeleted == 1">未发布</span>
            <span v-if="scope.row.companyShopDesignPlanisDeleted == 0">已发布</span>
          </template>
        </el-table-column>
        <el-table-column prop="isOpen" label="包含非公开产品" width="130">
          <template slot-scope="scope">
            <div style="text-align: center" class="question">
              {{scope.row.containsSecrecyFlag === "0" ? "是" : "否"}}
              <span
                v-if="scope.row.containsSecrecyFlag === '0'"
                @click="showSecrecyList(scope.row)"
                class="el-icon-question"
              ></span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="completeDate" label="入库时间" align="center" width="100" sortable>
          <template slot-scope="scope">
            <p style="width: 80px; text-align: center">{{scope.row.completeDate}}</p>
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
    <el-dialog title="未公开产品" :visible.sync="dialogOnekeySecercy">
      <el-table :data="onekeySecrecy" header-cell-class-name="hedStyle">
        <el-table-column prop="productCode" label="产品编号"></el-table-column>
        <el-table-column prop="productName" label="产品名称"></el-table-column>
      </el-table>
      <div class="pageFrame">
        <el-pagination
          @size-change="secrecySizeChange"
          @current-change="secrecyCurrentChange"
          :current-page="0"
          :page-sizes="[10, 50, 100, 200,500]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="secrecyTotal"
        ></el-pagination>
      </div>
    </el-dialog>
    <el-dialog title="设置版权费" :visible.sync="showPriceDialog" width="540px">
      <el-form style="width:440px;margin:0 auto;">
        <el-form-item label="设置收费方式：">
          <el-radio-group v-model="Priceform.isFree">
            <div style="margin:14px 0;">
              <el-radio label="免费"></el-radio>
            </div>

            <el-radio label="收费"></el-radio>
          </el-radio-group>
          <div v-if="Priceform.isFree=='收费'">
            <p style="margin: 10px 0 0 114px;">
              <span style="color:red;">*</span>度币：
              <el-input style="width:130px;"></el-input>
              <span class="huansuanImg"></span>
              人民币(元)：{{RMBChange}}
            </p>
          </div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" style="border-top:none;">
        <el-button
          round
          type="primary"
          style="width: 80px"
          size="medium"
          class="confirmButtonClass"
          @click="commitPrice"
        >确 定</el-button>
        <el-button
          round
          type="info"
          style="width: 80px"
          size="medium"
          class="cancelButtonClass"
          @click="showPriceDialog=false"
        >取 消</el-button>
      </span>
    </el-dialog>
    <img-pre-view ref="sonParams"></img-pre-view>
  </div>
</template>

<script>
import ContainsNotOpenProduct from "./ContainsNotOpenProduct";
import qs from "qs";
import ImgPreView from "@/components/Img-preview";
const isPublish = [
  {
    value: 0,
    name: "未发布"
  },
  {
    value: 1,
    name: "已发布"
  }
];
export default {
  name: "one-key-plan-list",
  components: {
    ContainsNotOpenProduct,
    ImgPreView
  },
  props: ["isRadio"],
  data() {
    return {
      RMBChange: 0,
      Priceform: {
        isFree: "免费"
      },
      showPriceDialog: false,
      classProgramme: "oneProgramme",
      selectBrand: "",
      brandDrop: [],
      productPlanId: 0,
      secrecyTotal: 0,
      loadingList: true,
      names: [],
      deliverDialogVisible: false,
      total: 0,
      shelfDialogVisible: false,
      shelf: {
        isPublish: []
      },
      query: {
        page: 0,
        limit: 10,
        planStyleId: null,
        planGroupStyleId: null,
        chargeType: null,
        isPublish:1,
        companyId: sessionStorage.getItem("companyID")
      },
      publishSend: {
        companyShopDesignPlanAddList: [],
        isContainsId: 0,
        isDeleted: 0,
        shopType: this.$route.query.shopType
      },
      list: [],
      sels: null,
      companys: [],
      batch: false,
      onekeySecrecy: [],
      dialogOnekeySecercy: false,
      confirmShelfMsg: "确定要上架么?",
      isPublish: isPublish,
      spaceCommonName: [],
      planStyles: [
        {
          styleName: "",
          styles: [
            {
              styleId: 0,
              styleName: ""
            }
          ]
        }
      ]
    };
  },
  methods: {
    commitPrice() {
      this.showPriceDialog = false;
      this.$message({
        type: "success",
        message: "设置成功!"
      });
      console.log("确定提交价格方式");
    },
    //价格弹框
    showPice() {
      this.showPriceDialog = true;
    },
    ImgView(ImgURL) {
      this.$refs.sonParams.sonFun({ ImgURL: ImgURL, ImgShow: true });
    },
    GoPlan() {
      //false 为智能方案 true 为全屋方案
      this.$emit("getShopPlanType", true);
    },
    isPer(per) {
      let permiss = qs.parse(sessionStorage.getItem("loginUser"));
      return this.isMainPer(per, permiss);
    },
    // 获取品牌列表
    getBrandDropList() {
      this.API.ProductBrandDrop({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        this.brandDrop = res.data;
      });
    },
    refreshData() {
      this.$nextTick(function() {
        this.loadingList = true;
        this.page();
      });
    },
    resetInput() {
      this.query = {
        page: 0,
        limit: 10,
        companyId: sessionStorage.getItem("companyID")
      };
      this.selectBrand = "";
      this.page();
    },
    showSecrecyList(row) {
      if (row) {
        this.dialogOnekeySecercy = true;
        this.productPlanId = row.planId;
        this.ProductSecrecyList();
      }
    },
    ProductSecrecyList() {
      this.API.ListOneKeyPlanProducts({
        planId: this.productPlanId,
        secrecy: 0,
        page: this.pages,
        limit: this.limits
      }).then(res => {
        this.onekeySecrecyLoading = false;
        this.onekeySecrecy = res.data;
        this.secrecyTotal = res.total;
      });
    },
    page() {
      let queryCondition = Object.assign({}, this.query, {
        brandId: this.selectBrand,
        shopId: sessionStorage.getItem("shopId"),
        shopType: this.$route.query.shopType
      });
      for (let key in queryCondition) {
        if (
          queryCondition[key] === "" ||
          queryCondition[key] === undefined ||
          queryCondition[key] === null
        ) {
          delete queryCondition[key];
        }
        if (key === "productCategory") delete queryCondition[key];
      }
      this.API.shopIntelligentList(queryCondition).then(resp => {
        if (resp.code == 204 || resp.code >= 500) {
          this.loadingList = false;
        }
        if (resp.data) {
          this.loadingList = false;
          this.list = resp.data.list.map(it => {
            if (it.isPublish) {
              return Object.assign({}, it, {
                isPublish: it.isPublish.split(",")
              });
            }
            return it;
          });
          console.log(this.list);
          this.total = resp.data.total;
        } else {
          this.loadingList = false;
          this.list = [];
          this.total = 0;
        }
      });
    },
    handleSelectionChange(sels) {
      this.sels = sels;
      /*if (sels) {
                    sels.forEach(row => {
                        if(rows.companyShopDesignPlanisDeleted == 0) {
                            this.$refs.multipleTable.clearSelection(row);
                        } else {
                            this.$refs.multipleTable.toggleRowSelection(row);
                        }
                    });
                }*/
    },
    selectable(row, index) {
      if (row.companyShopDesignPlanisDeleted === 0) {
        return false;
      } else {
        return true;
      }
    },
    // 空间类型
    getSpaceCommonName() {
      this.API.FitTypeHose().then(res => {
        this.spaceCommonName = res.data;
        console.log(res, "FitTypeHose");
      });
    },
    handleDetail(index, row) {
      localStorage.setItem("breadcrumb3", "方案详情");
      this.$store.commit("breadcrumb3", "方案详情");
      sessionStorage.setItem("shopPlanType", "0");
      this.$router.push(`/shopIntelligent/${row.planId}`);
    },
    publishPlan(index, row) {
      // 方案发布
      this.$confirm("确认发布方案吗？", "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        center: true,
        type: "warning"
      }).then(res => {
        this.publishSend.companyShopDesignPlanAddList = [];
        this.shelf.planId = row.planId;
        if (row.companyShopDesignPlanId == 0) {
          this.publishSend.isContainsId = 0;
          this.publishSend.isDeleted = 0;
        } else {
          this.publishSend.isContainsId = 1;
          this.publishSend.isDeleted = 0;
        }
        this.publishSend.companyShopDesignPlanAddList.push({
          id: row.companyShopDesignPlanId,
          planId: row.planId,
          planRecommendedType: row.isOpen,
          shopId: sessionStorage.getItem("shopId")
        });

        this.API.shopPublishPlan(this.publishSend).then(res => {
          this.page();
          this.$message.success("发布成功");
        });
      });

      /*this.batch = false*/
    },
    cancelPublishPlan(index, row) {
      this.$confirm("确认取消发布方案吗？", "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        center: true,
        type: "warning"
      }).then(res => {
        this.shelf.planId = row.planId;
        if (row.companyShopDesignPlanId != 0) {
          this.publishSend.isContainsId = 1;
          this.publishSend.isDeleted = 1;
        }
        this.publishSend.companyShopDesignPlanAddList = [];
        this.publishSend.companyShopDesignPlanAddList.push({
          id: row.companyShopDesignPlanId,
          planId: row.planId,
          planRecommendedType: row.isOpen,
          shopId: sessionStorage.getItem("shopId")
        });

        this.API.shopPublishPlan(this.publishSend).then(res => {
          this.page();
          this.$message.success("取消发布成功");
        });
      });
    },
    // 未公开产品分页模块
    secrecySizeChange: function(size) {
      this.limits = size;
      this.ProductSecrecyList();
    },
    secrecyCurrentChange: function(currentPage) {
      this.pages = currentPage;
      this.ProductSecrecyList();
    },
    // 分页事件
    handleSizeChange: function(size) {
      this.query.limit = size;
      this.page();
    },
    handleCurrentChange: function(currentPage) {
      this.loadingList = true;
      this.query.page = currentPage;
      this.page();
    },
    // 批量发布
    handleBatchPushaway() {
      if (!this.sels || this.sels.length <= 0) {
        this.$message.error("请选择一条记录");
        return;
      }

      this.$confirm("确认批量发布方案吗？", "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        center: true,
        type: "warning"
      }).then(res => {
        this.publishSend.companyShopDesignPlanAddList = [];
        this.sels.map(res => {
          if (res.companyShopDesignPlanId == 0) {
            this.publishSend.isContainsId = 0;
            this.publishSend.isDeleted = 0;
          } else {
            this.publishSend.isContainsId = 1;
            this.publishSend.isDeleted = 0;
          }

          this.publishSend.companyShopDesignPlanAddList.push({
            id: res.companyShopDesignPlanId,
            planId: res.planId,
            planRecommendedType: res.isOpen,
            shopId: sessionStorage.getItem("shopId")
          });
        });

        this.API.shopPublishPlan(this.publishSend).then(res => {
          this.page();
          this.$message.success("发布成功");
        });
      });
    },
    handleCheckPlan() {
      if (!this.sels) {
        this.$message.error("请选择一条记录");
        return;
      }
      this.sels.planType = 1;
      this.$store.commit("SET_checkPlan", this.sels);
      this.navigatorBack();
    },
    navigatorBack() {
      this.$router.go(-1);
    },
    initPlanStyle() {
      this.API.listDesignPlanStyle().then(resp => {
        this.planStyles = resp.data || [];
      });
    }
    /*
              getPlatformList () {
                 this.API.PlatFormList({platformType: '2b'}).then(resp => {
                   this.platforms = resp.data
                 })
               }
              */
  },
  created() {
    this.page();
    this.getBrandDropList();
    //空间类型
    this.getSpaceCommonName();
    this.initPlanStyle();
  }
};
</script>

<style lang="scss" scoped>
@import "../../../../assets/css/mixin";
.searchFrame {
  padding-top: 20px;
  padding-left: 30px;
  margin-left: -20px;
  border-top: solid 1px #ddd;
  text-align: center;
  .el-button {
    width: 100px;
  }
}
.searchCondition {
  padding-left: 30px;
  border-bottom: solid #e4e4e4 15px;
  /*padding-top: 15px;*/
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
.center {
  text-align: center;
}
.question {
  &:hover {
    color: $btnOrange;
  }
}
.operatonBtn {
  display: inline-block;
  padding-left: 0;
  font-size: 14px;
  width: 80px;
  margin-bottom: 5px;
}
.planLibrary {
  box-sizing: padding-box;
  background: #fff;
  border-radius: $borderRadius;
}

.deliver-box {
  margin-top: 20px;
  height: 200px;
  overflow-y: scroll;
}

.deliver-box-line {
  display: block;
}

.tabClass {
  margin-bottom: 20px;
}

.inputPlanKey {
  .el-input {
    width: 200px;
    margin-right: 10px;
  }
  .el-button {
    width: 130px;
    border: solid 1px $btnOrange;
    margin-left: 25px;
    margin-right: 20px;
    color: $buttonOrange;
  }
}

.operationButton {
  padding: 20px 30px;
  margin-top: 10px;
  text-align: left;
  @extend .planLibrary;
  .outline {
    // background: url("../../../assets/images/icons/allot.png") 0 center no-repeat;
    &:hover {
      // background: url("../../../assets/images/icons/allot2.png") 0 center        no-repeat;
    }
  }
  .refresh {
    // background: url("../../../assets/images/icons/reflesh.png") 0 center      no-repeat;
    &:hover {
      // background: url("../../../assets/images/icons/reflesh2.png") 0 center        no-repeat;
    }
  }
  button {
    padding-left: 25px;
    color: #666;
    font-size: 14px;
    margin-right: 10px;
    &:hover {
      color: $overButtonOrange;
    }
  }
}

.planList {
  border-top: solid 1px #eee;
  @extend .planLibrary;
}

.pageFrame {
  height: 110px;
  padding-top: 40px;
  box-sizing: border-box;
  text-align: center;
}

.nonImg {
  background: #f2f2f2;
  height: 80px;
  display: inline-block;
  width: 80px;
  line-height: 80px;
  text-align: center;
  font-size: 12px;
  color: #999;
  border: solid 1px #e3e3e3;
}
.el-button + .el-button {
  margin-left: 0;
}
.plan-style-css {
  .el-select-dropdown__item {
    color: #606266 !important;
  }
}
</style>
<style>
.el-table th.hedStyle {
  height: 5px !important;
}
.el-tabs__item {
  height: 50px;
  line-height: 50px;
}
.el-tabs__nav {
  margin-left: 30px;
}
.huansuanImg {
  width: 12px;
  height: 14px;
  /* background: url("../../../assets/images/icons/icon-huansuan.png") no-repeat; */
  background-size: 12px 14px;
  display: inline-block;
  margin: 0 10px;
  vertical-align: middle;
}
</style>
