<template>
  <div>
    <el-row :gutter="24" class="searchCondition">
      <div style="height: 60px;">
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
              v-for="(item,index) in shelfData"
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
        <!-- <el-col :span="3">
                    <el-input v-model="query.spaceCommonName" clearable placeholder="请输入空间类型"></el-input>
        </el-col>-->
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
          <el-select
            @change="page"
            v-model="query.planGroupStyleId"
            filterable
            clearable
            popper-class="plan-style-css"
            placeholder="请选择方案风格"
          >
            <!--<el-option-group-->
            <!--v-for="group in planStyles"-->
            <!--:key="group.styleName"-->
            <!--:label="group.styleName">-->
            <!--<el-option-->
            <!--v-for="item in group.styles"-->
            <!--:key="item.styleId"-->
            <!--:label="item.styleName"-->
            <!--:value="item.styleId">-->
            <!--</el-option>-->
            <!--</el-option-group>-->
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
        @click="handleBatchPushaway"
        v-if="isPer('biz:solution:exhibition')"
      >批量发布</el-button>
      <el-button size="small" type="text" @click="refreshData" class="refresh">刷新</el-button>
    </div>
    <div class="planList">
      <el-table
        :data="list"
        style="width: 100%;"
        ref="multipleTable"
        v-loading="channelPlanCommon"
        :highlight-current-row="isRadio"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.8)"
        @selection-change="handleSelectionChange"
        tooltip-effect="dark"
      >
        <el-table-column
          v-if="!isRadio"
          type="selection"
          label="全选"
          align="center"
          :selectable="selectable"
          width="75"
          title="全选"
        ></el-table-column>
        <el-table-column prop="planCode" label="编号" width="180"></el-table-column>
        <el-table-column prop="picPath" label="缩略图" width="180">
          <template slot-scope="scope">
            <div v-if="BASE_URL.sourceBaseUrl+scope.row.picPath">
              <img
                style="cursor:pointer"
                @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.picPath)"
                :src="BASE_URL.sourceBaseUrl+scope.row.picPath"
                width="80"
              >
            </div>
            <div v-else>无缩略图</div>
          </template>
        </el-table-column>
        <el-table-column prop="planName" label="名称"></el-table-column>
        <el-table-column prop="spaceTypeName" label="空间类型"></el-table-column>
        <el-table-column prop="brandName" label="品牌">
          <template slot-scope="scope">
            <p class="remarkOverEllipsis">{{scope.row.brandName}}</p>
          </template>
        </el-table-column>
        <el-table-column prop="designStyleName" label="风格"></el-table-column>

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

        <el-table-column label="发布状态" prop="isPublish">
          <template slot-scope="scope">
            <span v-if="scope.row.companyShopDesignPlanisDeleted == 1">未发布</span>
            <span v-if="scope.row.companyShopDesignPlanisDeleted == 0">已发布</span>
          </template>
        </el-table-column>
        <el-table-column prop="completeDate" label="入库时间" width="100" align="center" sortable>
          <template slot-scope="scope">
            <p style="width: 80px; text-align: center">{{scope.row.completeDate}}</p>
          </template>
        </el-table-column>
        <el-table-column prop="containsSecrecyFlag" label="包含非公开产品" width="130">
          <template slot-scope="scope">
            <div style="text-align: center">
              {{scope.row.containsSecrecyFlag === "0" ? "是" : "否"}}
              <span
                v-if="scope.row.containsSecrecyFlag === '0'"
                @click="showSecrecyList(scope.row)"
                class="el-icon-question"
              ></span>
            </div>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="operation" label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button
              type="text"
              class="operatonBtn"
              size="mini"
              style="padding:7px 0; text-align: center"
              @click="handleDetail(scope.$index, scope.row)"
            >详情</el-button>
            <el-button
              type="text"
              class="operatonBtn"
              style="padding:7px 0; "
              v-if="scope.row.companyShopDesignPlanisDeleted == 1"
              @click="publishPlan(scope.$index, scope.row)"
            >发布</el-button>
            <el-button
              type="text"
              class="operatonBtn"
              style="padding:7px;"
              v-if="scope.row.companyShopDesignPlanisDeleted == 0"
              @click="cancelPublishPlan(scope.$index, scope.row)"
            >取消发布</el-button>
          </template>
        </el-table-column>-->
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
    <!--<el-dialog
                title="选择上架对象"
                :visible.sync="shelfDialogVisible"
                width="20%"
                top="30vh"
                center>
            <el-checkbox-group class="center" v-model="shelf.isPublish">
                <el-checkbox v-for="(p,index) in shelfData" :key="index" :label="p.value">{{p.name}}</el-checkbox>
            </el-checkbox-group>
            <span slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="handleShelvesConfirm">确 定</el-button>
        <el-button round type="info" @click="shelfDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>-->
    <el-dialog title="未公开产品" :visible.sync="dialogCommonSecercy">
      <el-table :data="commonSecrecy" border>
        <el-table-column prop="productCode" label="产品编号"></el-table-column>
        <el-table-column prop="productName" label="产品名称"></el-table-column>
      </el-table>
    </el-dialog>
    <img-pre-view ref="sonParams"></img-pre-view>
  </div>
</template>
<script>
import ContainsNotOpenProduct from "../ContainsNotOpenProduct.vue";
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
  components: { ContainsNotOpenProduct, ImgPreView },
  name: "CommonDesignPlan",
  props: ["isRadio"],
  data() {
    return {
      spaceCommonName: [],
      selectBrand: "", // 选择的品牌
      channelPlanCommon: true,
      deliverDialogVisible: false,
      activeName: "first",
      total: 0,
      shelfDialogVisible: false,
      // platforms: [],
      delivery: {
        receiveCompanyId: []
      },
      shelf: {
        isPublish: []
      },
      deliverSearch: "",
      query: {
        page: 1,
        limit: 10,
        planStyleId: null,
        planGroupStyleId: null,
        isPublish:1,
        companyId: sessionStorage.getItem("companyID")
      },
      publishSend: {
        companyShopDesignPlanAddList: [],
        isContainsId: 0,
        isDeleted: 0
      },
      brandDrop: [],
      list: [],
      sels: [],
      companys: [],
      dialogCommonSecercy: false,
      commonSecrecy: [],
      batch: false,
      isPublish: isPublish,
      confirmShelfMsg: "确定要上架么?",
      shelfData: [
        {
          value: 0,
          name: "未发布"
        },
        {
          value: 1,
          name: "已发布"
        }
      ],
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
    //图片预览事件
    ImgView(ImgURL) {
      this.$refs.sonParams.sonFun({ ImgURL: ImgURL, ImgShow: true });
    },
    // 空间类型
    getSpaceCommonName() {
      this.API.FitTypeHose().then(res => {
        this.spaceCommonName = res.data;
        console.log(res, "FitTypeHose");
      });
    },
    isPer(per) {
      let permiss = qs.parse(sessionStorage.getItem("loginUser"));
      return this.isMainPer(per, permiss);
    },
    selectable(row, index) {
      if (row.companyShopDesignPlanisDeleted === 0) {
        return false;
      } else {
        return true;
      }
    },
    // 获取品牌列表
    getBrandDropList() {
      this.API.ProductBrandDrop({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        this.brandDrop = res.data;
      });
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
    showSecrecyList(row) {
      if (row) {
        this.dialogCommonSecercy = true;
        this.API.ListCommonPlanProducts({
          planId: row.planId
        }).then(res => {
          this.commonSecrecy = res.data;
        });
      }
    },
    refreshData() {
      this.$nextTick(function() {
        this.channelPlanCommon = true;
        this.page();
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
      this.API.shopCommonPlanList(queryCondition).then(resp => {
        if (resp.code == 204) {
          this.channelPlanCommon = false;
        }
        if (resp.data) {
          this.channelPlanCommon = false;
          this.list = resp.data.list;
          this.total = resp.data.total;
        } else {
          this.channelPlanCommon = false;
          this.list = [];
          this.total = 0;
        }
      });
    },
    handleSelectionChange(sels) {
      this.sels = sels;
    },
    handleDetail(index, row) {
      localStorage.setItem("breadcrumb3", "方案详情");
      this.$store.commit("breadcrumb3", "方案详情");
      sessionStorage.setItem("shopPlanType", "0");
      this.$router.push(`/shopIntelligent/${row.planId}`);
    },
    handleShelves(index, row) {
      // 方案上架 打开弹窗
      this.shelf.planId = row.planId;
      this.shelfDialogVisible = true;
      this.batch = false;
      this.shelf.isPublish = row.isPublish ? [row.isPublish] : [];
      if (row.containsSecrecyFlag === "0") {
        this.confirmShelfMsg = "选定的方案包含未公开产品,确定要上架么?";
      }
    },
    handleShelvesConfirm() {
      // 确定上架到某个平台
      this.$confirm(this.confirmShelfMsg, "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        type: "warning",
        center: true
      }).then(() => {
        if (this.batch) {
          const params = this.sels.map(sel => {
            return {
              planId: sel.planId,
              isPublish: this.shelf.isPublish
            };
          });
          this.API.ChannelCommonPlanBatchPushaway(params).then(resp => {
            this.$message({
              message: "上架成功",
              type: "success"
            });
            this.shelfDialogVisible = false;
            this.page();
          });
        } else {
          this.API.ChannelCommonPlanPushaway(this.shelf).then(resp => {
            this.$message({
              message: "上架成功",
              type: "success"
            });
            this.shelfDialogVisible = false;
            this.page();
          });
        }
      });
    },
    handleDeliver() {
      this.deliverDialogVisible = true;
      this.API.ListCompany().then(resp => {
        this.companys = resp.data;
      });
    },
    handleDeliverConfirm() {},
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        center: true,
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        type: "warning"
      })
        .then(index => {
          this.API.CommonDesignPlanDelete(row.planId).then(resp => {
            this.page();
            this.$message({
              type: "success",
              message: "删除成功!"
            });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
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

    batchDelete() {
      const planIds = this.sels.map(sel => sel.planId).join(",");
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        center: true,
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        type: "warning"
      })
        .then(index => {
          this.API.CommonDesignPlanBatchDelete(planIds).then(resp => {
            this.page();
            this.$message({
              type: "success",
              message: "删除成功!"
            });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    // 分页事件
    handleSizeChange: function(size) {
      this.query.limit = size;
      this.page();
    },
    handleCurrentChange: function(currentPage) {
      this.channelPlanCommon = true;
      this.query.page = currentPage;
      this.page();
    },
    /*
            getPlatFormList () {
              this.API.PlatFormList({platformType: '2b'}).then(resp => {
                this.platforms = resp.data
                console.log(resp.data)
              })
            },
            */
    handleBatchPushaway() {
      if (this.sels.length <= 0) {
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
  },
  created() {
    this.page();
    this.getBrandDropList();
    //空间类型
    this.getSpaceCommonName();
    //      this.getPlatFormList()
    this.initPlanStyle();
  }
};
</script>
<style lang="scss" scoped>
@import "../../../../../assets/css/mixin";

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
}

.center {
  text-align: center;
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
  padding: 20px;
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
    color: $buttonOrange;
  }
}

.operationButton {
  padding: 20px 30px;
  margin-top: 10px;
  text-align: left;
  @extend .planLibrary;
  .outline {
    // background: url('../../../../assets/images/icons/allot.png') 0 center no-repeat;
    &:hover {
      // background: url('../../../../assets/images/icons/allot2.png') 0 center no-repeat;
    }
  }
  .refresh {
    margin-left: 0;
    // background: url('../../../../assets/images/icons/reflesh.png') 0 center no-repeat;
    &:hover {
      // background: url('../../../../assets/images/icons/reflesh2.png') 0 center no-repeat;
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

.operatonBtn {
  font-size: 14px;
  width: 80px;
  margin-bottom: 5px;
}

.pageFrame {
  height: 110px;
  padding-top: 40px;
  box-sizing: border-box;
  text-align: center;
}
.plan-style-css {
  .el-select-dropdown__item {
    color: #606266 !important;
  }
}
</style>
<style>
.el-table th {
  height: 60px;
  background: #f3f3f3;
  text-align: center;
}
</style>
