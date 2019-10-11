<template>
  <div>
    <transition name="fade">
      <el-tabs
        style="background: #fff"
        class="allMargin"
        @tab-click="changeTabs"
        v-model="tabsSelect"
      >
        <el-tab-pane label="产品管理" name="first" v-if="isPer('product:view')">
          <section class="products">
            <el-form :inline="true" ref="formSearch">
              <el-row class="grid-content bg-purple-dark minWidth">
                <el-col :span="3">
                  <el-cascader
                    ref="productCates"
                    placeholder="请选择产品分类"
                    :props="props"
                    :options="productCategoryDrop"
                    filterable
                    v-model="query.productCategory"
                    @change="searchList"
                    change-on-select
                    clearable
                  >
                    <!--@change="selectedCategoryDrop"-->
                  </el-cascader>
                </el-col>
                <el-col :span="3">
                  <!--<el-select v-model="allotValue" placeholder="请选择分配情况" @change="getProductList" clearable
                             >
                    <el-option
                      v-for="(item,index) in allot"
                      :key="index"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>-->

                  <el-select
                    @change="searchList"
                    clearable
                    v-model="query.platformId"
                    placeholder="请选择上架情况"
                  >
                    <el-option
                      v-for="(item, index) in platformList"
                      :key="index"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                    <el-option value="0" label="未上架"></el-option>
                    <!-- <el-option value="ALL" label="渠道&线上"></el-option>
                    <el-option value="NONE" label="未分配"></el-option>-->
                  </el-select>
                </el-col>
                <el-col :span="3">
                  <el-select
                    v-model="isOpen"
                    @change="searchList"
                    clearable
                    placeholder="请选择公开状态"
                  >
                    <el-option
                      v-for="item in openDisclosed"
                      :key="item.name"
                      :label="item.name"
                      :value="item.isON"
                    ></el-option>
                  </el-select>
                </el-col>
                <el-col :span="3">
                  <el-select
                    v-model="isComplexParquet"
                    @change="searchList"
                    clearable
                    placeholder="是否一石多面"
                  >
                    <el-option label="是" value="1"></el-option>
                    <el-option label="否" value="0"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="3">
                  <el-input v-model="productInputName" placeholder="请输入产品名称"></el-input>
                </el-col>

                <el-col :span="3">
                  <el-input v-model="productInputCode" placeholder="请输入产品编号"></el-input>
                </el-col>

                <el-col :span="3">
                  <el-input v-model="productModelNumber" placeholder="请输入产品型号"></el-input>
                </el-col>
              </el-row>
              <el-row class="grid-content bg-purple-dark minWidth" style="padding-top: 0">
                  <el-col :span="3">
                    <el-select v-model="query.brandId" clearable placeholder="请选择品牌"  @change="searchList">
                      <el-option
                              v-for="item in brandList"
                              :key="item.id"
                              :label="item.name"
                              :value="item.id">
                      </el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="3">
                    <el-select v-model="query.isModelTexture" clearable placeholder="是否关联模型/贴图"  @change="searchList">
                      <el-option
                              v-for="item in connectModel"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value">
                      </el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="3">
                    <el-input v-model="query.sonProductCode" placeholder="请输入子产品编号"></el-input>
                  </el-col>
              </el-row>
              <el-row class="btnBox">
                <el-button round class="searchNavBtn" type="primary" @click="searchList">搜索</el-button>
                <el-button class="searchNavBtn" @click="resetInput" round>重置</el-button>
                <div class="btn-right">
                  <el-button
                          round
                          type="primary"
                          class="addProBtn"
                          plain
                          v-if="isPer('product:product_merge')"
                          @click="mergeGoods"
                  >产品合并</el-button>
                  <el-button
                    round
                    type="primary"
                    class="addProBtn1"
                    plain
                    v-if="isPer('product:add')"
                    @click="gotoAddProductTexture(true)"
                  >新增瓷砖贴图产品</el-button>
                  <el-button
                    round
                    type="primary"
                    class="addProBtn"
                    plain
                    v-if="isPer('product:add')"
                    @click="gotoAddProduct(true)"
                  >新增定制产品</el-button>
                  <el-button
                    round
                    type="primary"
                    class="addProBtn"
                    plain
                    v-if="isPer('product:add')"
                    @click="gotoAddProduct(false)"
                  >新增成品产品</el-button>
                </div>
              </el-row>
            </el-form>
            <div style="border-bottom: solid 15px #f0f0f0;"></div>
            <!--列表-->
            <div class="tableFilter">
              <ul>
                <li @click="batchShelf" class="share" v-if="isPer('product:exhibition')">
                  <span>批量上架</span>
                </li>
                <li @click="batchDelete" class="out" v-if="isPer('product:del')">
                  <span>批量删除</span>
                </li>
                <li @click="ProductSecrecy()" class="open" v-if="isPer('product:manage')">
                  <span>批量公开</span>
                </li>
                <li @click="ProductCancelSecrecy()" class="offOpen" v-if="isPer('product:manage')">
                  <span>批量取消公开</span>
                </li>
                <li @click="synchronous" class="refreshAuto" v-if="isPer('product:sync')">
                  <span>手动同步</span>
                </li>
                <li @click="syncGoods" class="syncGoods" v-if="isPer('product:sync')">
                  <span>同步商品</span>
                </li>
                <!--<li @click="mergeGoods" class="mergeGoods" v-if="isPer('product:sync')">-->
                  <!--<span>产品合并</span>-->
                <!--</li>-->
                <li @click="refreshList" class="refresh">
                  <span>刷新</span>
                </li>
              </ul>
            </div>
            <el-table
              :data="productList"
              style="width: 100%"
              v-loading="loading"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(255, 255, 255, 0.8)"
              @selection-change="handleSelectionChange"
              tooltip-effect="dark"
              :default-sort="{prop: 'gtmCreat', order: 'descending'}"
            >
              <el-table-column type="selection" label="全选" width="55" title="全选"></el-table-column>
              <el-table-column prop="code" label="编号" sortable></el-table-column>
              <el-table-column prop="picPath" label="产品缩略图">
                <template slot-scope="scope">
                  <div v-if="scope.row.picPath">
                    <img
                      style="cursor:pointer;object-fit: contain"
                      @click="ImgView(scope.row.picPath)"
                      :src="scope.row.picPath"
                      width="90"
                      height="90"
                    >
                  </div>
                  <div class="nonImg" v-else>暂无图片</div>
                </template>
              </el-table-column>
              <el-table-column prop="productName" label="名称" align="center"></el-table-column>
              <el-table-column prop="modelNumber" label="产品型号" align="center">
                <template slot-scope="scope">
                  <div v-if="scope.row.modelNumber">{{scope.row.modelNumber}}</div>
                  <div v-else>———</div>
                </template>
              </el-table-column>
              <el-table-column prop="brandName" label="品牌" align="center"></el-table-column>
              <el-table-column prop="categoryNames" label="产品分类" align="center">
                <template slot-scope="scope">
                  <p v-if="scope.row.categoryNames">{{scope.row.categoryNames.replace(/\//g, '>')}}</p>
                </template>
              </el-table-column>
              <el-table-column prop="modelCode" label="模型/贴图编号" align="center" width="150">
                <template slot-scope="scope">
                  <div v-if="scope.row.modelCode">{{scope.row.modelCode}}</div>
                  <div v-else>———</div>
                </template>
              </el-table-column>

              <el-table-column prop="putAwayPlatformNames" label="上架情况" align="center" width="150">
                <template slot-scope="scope">
                  <div
                    v-if="scope.row.putAwayPlatformNames"
                    v-for="(item,index) in scope.row.putAwayPlatformNames"
                    :key="index"
                  >
                    <p>{{item}}</p>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="gtmCreat" label="录入时间" width="120" align="center" sortable>
                <template slot-scope="scope">
                  <p
                    style="width: 80px; text-align: center"
                    v-if="scope.row.gtmCreat"
                  >{{scope.row.gtmCreat}}</p>
                  <p v-else>———</p>
                </template>
              </el-table-column>
              <el-table-column prop="secrecy" label="公开状态" align="center">
                <template slot-scope="scope">
                  <el-switch
                    class="switchStyle"
                    v-model="scope.row.secrecy"
                    active-color="#ff6419"
                    inactive-icon-class="inactive-icon-class"
                    active-icon-class="active-icon-class"
                    :active-value="0"
                    inactive-color="#ddd"
                    :inactive-value="1"
                    @change="changeSwitch(scope.row)"
                  ></el-switch>
                  <p>
                    {{scope.row.secrecy == 0 ? scope.row.statusSwitchTxt = '公开' : scope.row.statusSwitchTxt =
                    '未公开'}}
                  </p>
                </template>
              </el-table-column>
              <el-table-column label="操作" class="btnGroup" align="center" width="170">
                <template slot-scope="scope">
                  <!-- && scope.row.origin !== 'deliver' && scope.row.origin!='share'-->
                  <!--<el-button style="display: none" class="operatonBtn" size="mini"
                             v-if="isPer('solution:delivery')"
                             @click="handleDeliver(scope.row)">交付
                  </el-button>-->

                  <div>
                    <el-button
                      type="text"
                      size="mini"
                      style="margin-left: 0"
                      @click="copyProduct(scope.row)"
                      v-if="scope.row.mergeFlag==0"
                    >复制</el-button>
                    <el-button
                      type="text"
                      size="mini"
                      style="margin-left: 0"
                      v-if="isPer('product:view') && scope.row.isCreatedTexture==0 && scope.row.mergeFlag==0"
                      @click="showDetailProducts(scope.row)"
                    >详情</el-button>
                    <el-button
                      type="text"
                      size="mini"
                      style="margin-left: 0"
                      v-if="isPer('product:view') && scope.row.isCreatedTexture==1 && scope.row.mergeFlag==0"
                      @click="showDetailProductsTexture(scope.row)"
                    >详情</el-button>
                    <el-button
                      type="text"
                      size="mini"
                      style="margin-left: 0"
                      v-if="isPer('product:edit') && scope.row.isCreatedTexture==0 && scope.row.mergeFlag==0"
                      @click="goToEdit(scope.row)"
                    >编辑</el-button>
                    <el-button
                      type="text"
                      size="mini"
                      style="margin-left: 0"
                      v-if="isPer('product:edit') && scope.row.isCreatedTexture==1 && scope.row.mergeFlag==0"
                      @click="goToTextureEdit(scope.row)"
                    >编辑</el-button>
                  </div>
                  <!--<div>
                    <span class="operatonBtnTxt" v-if="isPer('solution:delivery')" @click="handleDeliver(scope.row)">公开</span>
                  </div>-->
                  <div>
                    <el-button
                      type="text"
                      size="mini"
                      style="margin-left: 0"
                      v-if="isPer('product:del') && scope.row.mergeFlag==0"
                      @click="handleDel(scope.row)"
                    >删除</el-button>
                    <el-button
                      type="text"
                      size="mini"
                      style="margin-left: 0"
                      v-if="isPer('product:exhibition')"
                      @click="singleShelf(scope.row)"
                    >上架管理</el-button>
                    <el-button
                            type="text"
                            size="mini"
                            style="margin-left: 0"
                            v-if="scope.row.mergeFlag==1"
                            @click="$router.push({ path:`/mergeProduct/${scope.row.id}`})"
                    >取消合并</el-button>
                  </div>
                  <!-- <el-button size="small" class="operatonBtn" v-if="isPer('product:manage')"
                              @click="batchSingle(scope.row)">分配
                  </el-button>-->
                </template>
              </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
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
            <!--分配界面-->
            <!--<div v-show="dialogAllot" class="dialogDiv">
              <div class="checkboxFrom">
                <h1>选择分配对象</h1>
                <div>
                  <el-checkbox-group v-model="checkAllot">
                    <el-checkbox label="2b" value="2b">渠道管理</el-checkbox>
                    <el-checkbox label="2c" value="2c">线上管理</el-checkbox>
                  </el-checkbox-group>
                </div>

                <p>
                  <el-button type="primary" @click="sureAllot" style="width: 80px" class="confirmButtonClass"
                             size="medium">
                    确定
                  </el-button>
                  <el-button @click="cancelAllot" style="width: 80px" class="cancelButtonClass" size="medium">取消
                  </el-button>
                </p>
              </div>
            </div>-->

            <el-dialog title="选择分配对象" :visible.sync="dialogAllot" width="20%" top="30vh" center>
              <div class="checkboxFrom">
                <div>
                  <el-checkbox-group v-model="checkAllot">
                    <el-checkbox label="2b" value="2b">渠道管理</el-checkbox>
                    <el-checkbox label="2c" value="2c">线上管理</el-checkbox>
                  </el-checkbox-group>
                </div>

                <p>
                  <el-button
                    type="primary"
                    @click="sureAllot"
                    style="width: 80px"
                    class="confirmButtonClass"
                    size="medium"
                  >确定</el-button>
                  <el-button
                    @click="cancelAllot"
                    style="width: 80px"
                    class="cancelButtonClass"
                    size="medium"
                  >取消</el-button>
                </p>
              </div>
            </el-dialog>
          </section>
        </el-tab-pane>
        <el-tab-pane label="产品组合管理" name="second" v-if="isPer('product:group:view')">
          <product-group></product-group>
        </el-tab-pane>
      </el-tabs>
    </transition>
    <el-dialog title="上下架管理" :visible.sync="shelfDialogVisible" width="540px" top="30vh">
      <div>
        <el-checkbox
          :indeterminate="isIndeterminate"
          v-model="checkAll"
          @change="handleCheckAllChange"
          class="checkAll"
        >全选</el-checkbox>
        <el-checkbox-group style="width:400px;margin-left: 40px;margin-bottom: 10px" v-model="shelf.targetPlatform">
          <el-checkbox
            style="width:120px;margin-left: 30px"
            v-for="(ds, index) in platformList"
            :key="index"
            :label="parseInt(ds.id)"
          >{{ds.name}}</el-checkbox>
        </el-checkbox-group>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button
          round
          type="primary"
          style="width: 80px"
          size="medium"
          class="confirmButtonClass"
          @click="shelfConfirm"
        >确 定</el-button>
        <el-button
          round
          type="info"
          style="width: 80px"
          size="medium"
          class="cancelButtonClass"
          @click="shelfDialogVisible = false"
        >取 消</el-button>
      </span>
    </el-dialog>

    <el-dialog title="选择交付企业" :visible.sync="deliverDialogVisible" width="25%" top="30vh" center>
      <el-input placeholder="请输入内容" prefix-icon="el-icon-search" v-model="deliverSearch"></el-input>
      <el-checkbox-group v-model="delivery.receiveCompanyIds">
        <div class="deliver-box">
          <el-checkbox style="display: none"></el-checkbox>
          <el-checkbox
            class="deliver-box-line"
            :disabled="c.delivered === 1"
            v-for="(c, index) in searchCompanys"
            :label="c.companyId"
            :key="c.companyId"
          >{{c.companyName}}</el-checkbox>
        </div>
      </el-checkbox-group>
      <span slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="handleDeliverConfirm">确 定</el-button>
        <el-button round type="info" @click="deliverDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <img-pre-view ref="sonParams"></img-pre-view>
  </div>
</template>
<script>
import upload from "../../../views/public/upload/upload";
import qs from "qs";
import productGroup from "../../productGroup/library/library";
import ImgPreView from "@/components/Img-preview";

export default {
  data() {
    return {
      connectModel:[
          { value:1,label:'是'},
          { value:0,label:'否'}
       ],
      brandList:[],
      checkAll: false,
      isIndeterminate: false,
      shelfDialogVisible: false,
      // 交付功能
      deliverDialogVisible: false,
      deliverSearch: "",
      delivery: {
        receiveCompanyIds: [],
        delivererId: qs.parse(sessionStorage.getItem("loginUser")).id,
        deliveryCompanyId: sessionStorage.getItem("companyID")
      },
      searchCompanys: [],
      // 交付功能-----结束
      tabsSelect: "",
      allotStatus: "", //  allotStatus
      loading: true,
      productModel: "",
      productInputCode: "",
      productModelNumber: "",
      productInputName: "",
      value1: true,
      inactiveValue: "",
      isOpen: "",
      isComplexParquet: "",
      productCates: "",
      total: 0,
      query: {
        page: 1,
        limit: 10,
        queryType: "library",
        companyId: sessionStorage.getItem("companyID"),
        productCategory: [],
        brandId:null,
        isModelTexture:null,
         sonProductCode:null
      },
      selectedIds: [],
      selectedType: "",
      openDisclosed: [{ name: "公开", isON: 0 }, { name: "未公开", isON: 1 }],
      rowid: [], // 单条数据分配时，获取id
      Switch: [],
      currentPage: 0, // 当前页
      pageSize: 5, // 页大小
      productCategoryDrop: [], // 获取分类下拉框
      props: {
        label: "name",
        value: "keyCode",
        children: "children"
      },
      allot: [
        { label: "渠道管理", value: "channel" },
        { label: "线上管理", value: "online" },
        { label: "未分配", value: "nonAllot" }
      ],
      allotValue: "",
      checkAllot: [],
      dialogAllot: false,
      showAddProducts: false,
      showEditProducts: false,
      productList: [],
      multipleSelection: [],
      dialogVisible: false,
      statusSwitch: "",
      submitBtnGreyBg: false, // 提交按钮为灰色
      platformList: [], // 上架平台列表
      shelf: {
        targetPlatform: []
      },
      productShelfParams: {
        // 产品上下架
        platformIds: "",
        productIds: []
      },
      singleProductId: []
    };
  },
  created() {
    //this.getShelfPlatformList()
    // this.tabsSelect = this.$store.state.activeName || 'first'
    // this.getProductList()
    // this.getProductCategory()
  },
  activated() {
    this.getShelfPlatformList();
    this.changeTabs();
    this.tabsSelect = this.$store.state.activeName || "first";
    console.log(this.$store.state.activeName, "this.activeName");
    this.getProductList();
    this.getProductCategory();
    this.getBrandDropList();
  },
  watch: {
    deliverSearch(val) {
      if (val) {
        this.searchCompanys = this.companys.filter(c =>
          c.companyName.includes(val)
        );
      } else {
        this.searchCompanys = this.companys;
      }
    }
  },
  methods: {
    getBrandDropList() {
        this.API.ProductBrandDrop({
            companyId: sessionStorage.getItem("companyID")
        }).then(res => {
            if (res.data) {
                this.brandList = res.data;
            }
        });
    },
    toOtherpage() {
      location.href = "./operation.html";
    },
    //图片预览事件
    ImgView(ImgURL) {
      this.$refs.sonParams.sonFun({ ImgURL: ImgURL, ImgShow: true });
    },
    isPer(per) {
      let permiss = qs.parse(sessionStorage.getItem("loginUser"));
      return this.isMainPer(per, permiss);
    },
    handleCheckAllChange(val) {
      if (val) {
        this.shelf.targetPlatform = [];
        this.platformList.map(res => {
          this.shelf.targetPlatform.push(parseInt(res.id));
        });
      } else {
        this.shelf.targetPlatform = [];
      }
    },
    mergeGoods(){
       this.$router.push(`/mergeProduct/0`);
    },
    copyProduct(row) {
        // 复制功能
        localStorage.setItem("breadcrumb3", "复制产品");
        sessionStorage.setItem(
            "addProductType",
            row.productBatchType == 2 ? "hard" : "soft"
        ); //区分软硬装
        sessionStorage.setItem("isConnectBtn", true);
      if(row.isCreatedTexture==0){ //
          this.$router.push(`/copyProduct/${row.id}`);
      }else if(row.isCreatedTexture==1){ //复制材质
          this.$router.push(`/copyProductTexture/${row.id}`);
      }
    },
    // 手动同步
    synchronous() {
      if (this.multipleSelection.length === 0) {
        this.$message.error("请选择需要同步的数据");
        return;
      }
      let ids = [];
      this.multipleSelection.map(function(value) {
        ids.push(value.id);
      });

      this.$confirm("确认手动同步数据吗?", "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        type: "warning",
        center: true
      }).then(res => {
        this.API.productSync({
          productIdsStr: ids.join(",")
        }).then();
        this.getProductList();
        setTimeout(() => {
          this.$message.success("数据同步成功");
        }, 2000);
      });
    },
    // 产品交付
    handleDeliver(row) {
      this.deliverDialogVisible = true;
      this.delivery.designPlanId = row.planId;

      if (row.containsSecrecyFlag === "0") {
        this.confirmDeliverMsg = "选定的方案包含未公开产品,确定要交付么?";
      }
      this.API.ListCompanysWithDelivered({
        companyId: sessionStorage.getItem("companyID"),
        planId: 88595
      }).then(res => {
        this.companys = res.data;
        this.disabledDeliverCompanyIds = this.companys
          .filter(it => it.delivered)
          .map(it => it.companyId);
        this.delivery.receiveCompanyIds = this.companys
          .filter(it => it.delivered)
          .map(it => it.companyId);
        this.searchCompanys = this.companys;
      });
    },
    // 确定交付公司
    handleDeliverConfirm() {
      if (
        this.delivery.receiveCompanyIds.filter(
          it => !this.disabledDeliverCompanyIds.includes(it)
        ).length <= 0
      ) {
        this.$message.error("请选择一个公司");
        return;
      }
      this.$confirm(this.confirmDeliverMsg, "提示", {
        type: "warning"
      }).then(() => {
        this.API.OneKeyPlanDeliver(this.delivery).then(resp => {
          this.deliverDialogVisible = false;
          this.page();
          this.$message({
            type: "success",
            message: "交付成功!"
          });
        });
      });
    },

    refreshList() {
      this.getProductList();
    },
    selectedCategoryDrop(value) {
      this.selectedType = value[value.length - 1];
    },
    gotoAddProduct(type) {
      this.$store.commit("breadcrumb4", "新增产品");
      sessionStorage.setItem("addProductType", type ? "hard" : "soft");
      this.$router.push("/AddProduct");
      sessionStorage.setItem("isConnectBtn", true);
    },
    gotoAddProductTexture(type) {
      this.$store.commit("breadcrumb4", "新增产品");
      sessionStorage.setItem("addProductType", type ? "hard" : "soft");
      this.$router.push("/AddProductTexture");
      sessionStorage.setItem("isConnectBtn", true);
    },
    // 取消分配
    cancelAllot() {
      this.dialogAllot = false;
    },
    changeTabs(val) {
      val ? (this.$store.state.activeName = val.name) : "";
    },
    showBatch() {
      //this.checkAllot = [...(row.status2b ? ['2b'] : []), ...(row.status2c ? ['2c'] : [])] // 分配回显状态
      this.checkAllot = []; // 批量分配时取消回现状态。
      if (this.multipleSelection.filter(item => !item.modelCode).length > 0) {
        this.$message.error("选择中记录中没有模型或贴图，不能做分配");
        return;
      }
      if (this.multipleSelection.length > 0) {
        this.dialogAllot = true;
      } else {
        this.$message.error("请选择一条或多条数据再做分配");
      }
    },
    // 获取上架平台列表
    getShelfPlatformList() {
      this.API.platformShelf({
        menuType: 0
      }).then(res => {
        if (res) {
          this.platformList = res.data;
        }
      });
    },
    // 产品批量上下架
    batchShelf() {
      if (this.multipleSelection.length === 0) {
        this.$message.error("请选择需要上架的产品");
        return;
      }
      if (this.multipleSelection.filter(item => !item.picPath).length > 0) {
        this.$message.error("选择中记录中没有产品图片，不能做上架");
        return;
      }
      // if(this.multipleSelection.filter(item => !item.modelCode).length > 0) {
      //     this.$message.error('选择中记录中没有模型或贴图，不能做上架')
      //     return
      // }
      this.shelf.targetPlatform = [];
      this.shelfDialogVisible = true;
      this.checkAll = false;
    },
    productBatchShelf() {
      // this.$confirm('确定要上架么?', '提示', {
      //     cancelButtonClass: 'cancelButtonClass',
      //     confirmButtonClass: 'confirmButtonClass',
      //     type: 'info',
      //     center: true
      // }).then(res => {
      let ids = [];
      if (this.multipleSelection) {
        this.multipleSelection.map(function(value) {
          ids.push(value.id);
        });
      }
      if (this.shelf.targetPlatform) {
        this.productShelfParams.platformIds = this.shelf.targetPlatform.join(
          ","
        );
      }
      this.productShelfParams.productIds = ids;
      this.API.contentLibraryProductShelf(this.productShelfParams).then(res => {
        this.shelfDialogVisible = false;
        this.getProductList();
        if (res.success == false) {
          this.$message.success(res.message);
        } else {
          this.$message.success("上架成功");
        }
      });
      //})
    },
    shelfConfirm() {
      if (this.multipleSelection.length > 0) {
        this.productBatchShelf();
      } else {
        this.singleShelfConfirm();
      }
    },
    // 取消公开
    ProductCancelSecrecy() {
      let ids = [];
      this.multipleSelection.map(function(value) {
        ids.push(value.id);
      });
      if (ids.length > 0) {
        this.$confirm("确定取消公开么", "提示", {
          cancelButtonClass: "cancelButtonClass",
          confirmButtonClass: "confirmButtonClass",
          confirmButtonText: "确定",
          type: "warning",
          center: true
        }).then(() => {
          this.API.ProductSecrecy({
            productIds: ids,
            productSecrecyStatus: 1
          }).then(res => {
            this.$message.success("已取消公开");
            this.getProductList();
          });
        });
      } else {
        this.$message.error("请选择需要批量取消公开的产品再做操作");
      }
    },
    // 公开
    ProductSecrecy() {
      let ids = [];
      this.multipleSelection.map(function(value) {
        ids.push(value.id);
      });
      if (ids.length > 0) {
        this.$confirm("确定公开选中产品？", "提示", {
          cancelButtonClass: "cancelButtonClass",
          confirmButtonClass: "confirmButtonClass",
          confirmButtonText: "确定",
          type: "warning",
          center: true
        }).then(() => {
          this.API.ProductSecrecy({
            productIds: ids,
            productSecrecyStatus: 0
          }).then(res => {
            this.$message.success("产品已公开");
            this.getProductList();
          });
        });
      } else {
        this.$message.error("请选择需要公开的产品再做操作");
      }
    },
    // 单条数据分配
    /*batchSingle (row) {
              if (!row.modelCode) {
                this.$message.error('没有模型或贴图，不能做分配')
                return
              }
              this.dialogAllot = true
              this.checkAllot = [...(row.status2b ? ['2b'] : []), ...(row.status2c ? ['2c'] : [])] // 分配回显状态
              this.rowid = row.id
            },*/
    // 单条数据上架  putAwayPlatformIds    putAwayPlatformNames
    singleShelf(row) {
      if (!row.picPath) {
        this.$message.error("没有产品图片，不能做上架");
        return;
      }
      // if (!row.modelCode) {
      //     this.$message.error('没有模型或贴图，不能做上架')
      //     return
      // }
      // this.shelf.targetPlatform = [...(row.putAwayPlatformIds ? row.putAwayPlatformIds : [])]
      this.shelf.targetPlatform = row.putAwayPlatformIds;

      this.singleProductId = [];
      this.shelfDialogVisible = true;
      this.singleProductId.push(row.id);
      this.platformList.length == this.shelf.targetPlatform.length
        ? (this.checkAll = true)
        : (this.checkAll = false); //全选
    },
    singleShelfConfirm() {
      // this.$confirm('确定要上架么?', '提示', {
      //     cancelButtonClass: 'cancelButtonClass',
      //     confirmButtonClass: 'confirmButtonClass',
      //     type: 'info',
      //     center: true
      // }).then((res) => {
      this.productShelfParams.productIds = [];
      if (this.shelf.targetPlatform) {
        this.productShelfParams.platformIds = this.shelf.targetPlatform.join(
          ","
        );
      }
      this.productShelfParams.productIds = this.singleProductId;
      this.API.contentLibraryProductShelf(this.productShelfParams).then(res => {
        this.shelfDialogVisible = false;
        this.getProductList();
        if (res.success == false) {
          this.$message.success(res.message);
        } else {
          this.$message.success("上架成功");
        }
      });
      //})
    },
    sureAllot() {
      if (this.multipleSelection.length > 0) {
        this.batchAllot(); // 批量分配
      } else {
        this.singleAllot(); // 单条数据分配，
      }
    },
    singleAllot() {
      this.API.BatchAllotProduct({
        productIds: [this.rowid],
        platformType: this.checkAllot.join(",")
      }).then(res => {
        this.dialogAllot = false;
        this.$message.success("分配成功");
        this.getProductList();
      });
    },
    batchAllot() {
      // 批量分配
      let ids = [];
      this.multipleSelection.map(value => {
        ids.push(value.id);
      });
      if (ids.length > 0) {
        this.$confirm("确定要分配么", "提示", {
          cancelButtonClass: "cancelButtonClass",
          confirmButtonClass: "confirmButtonClass",
          confirmButtonText: "确定",
          type: "warning",
          center: true
        }).then(() => {
          this.API.BatchAllotProduct({
            productIds: ids,
            platformType: this.checkAllot.join(",")
          }).then(res => {
            this.getProductList();
            this.$message.success("分配成功");
          });
          this.dialogAllot = false;
        });
      } else {
        this.$message.error("请选择一条或多条数据再做分配");
      }
    },
    resetInput() {
      this.productInputName = "";
      this.productInputCode = "";
      this.productModelNumber = "";
      this.allotValue = "";
      this.productCates = "";
      this.isOpen = "";
      this.query.platformId = "";
      this.isComplexParquet = "";
      this.query.brandId='';
      this.query.isModelTexture='';
      this.query.sonProductCode='';
      let obj = {};
      obj.stopPropagation = () => {};
      this.$refs.productCates.clearValue(obj);
    },
    getProductCategory() {
      // 下拉框产品分类
      this.API.ProductCategory().then(res => {
        this.productCategoryDrop = res.data;
      });
    },
    searchList() {
      this.query.page = 1;
      this.getProductList();
    },
    handleChange(value) {
      console.log(value);
      // this.getProductList();
    },
    getProductList() {
      this.loading = true;
      let pc = this.query.productCategory;
      let queryCondition = Object.assign({}, this.query, {
        // categoryCode: pc.length > 0 ? pc[pc.length - 1] : null,
        // productType: pc.length > 0 ? pc[pc.length - 1] : null,
        // Added by songjianming@sanduspace.cn on 2018/12/20
        // 产品库列表页面增加产品大类筛选(http://jira.3du-inc.net/browse/CMS-634)
        bigProductType: this.getProductTypeMark(pc, 1),
        productType: this.getProductTypeMark(pc, 2),
        allotState: this.allotValue,
        productName: this.productInputName,
        productCode: this.productInputCode,
        productModelNumber: this.productModelNumber,
        secrecy: this.isOpen,
        isComplexParquet: this.isComplexParquet
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

      this.API.ProductList(queryCondition).then(res => {
        // 产品列表
        if (res.code == 204 || res.code == 400 || res.code >= 500) {
          this.loading = false;
        }
        if (res.list) {
          this.loading = false;
          this.productList = res.list;
          this.total = res.total;
        } else {
          this.productList = [];
          this.total = 0;
        }
      });
    },
    getProductTypeMark(productTypes, index) {
      return productTypes && productTypes.length - 1 >= index
        ? productTypes[index]
        : null;
    },
    changeSwitch(row) {
      if (row) {
        if (row.secrecy) {
          row.statusSwitchTxt = "公开";
          this.API.ProductSecrecy({
            productIds: [row.id],
            productSecrecyStatus: 1
          }).then(res => {});
        } else {
          row.statusSwitchTxt = "未公开";
          this.API.ProductSecrecy({
            productIds: [row.id],
            productSecrecyStatus: 0
          }).then(res => {});
        }
      }
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    showDetailProducts(row) {
      if (row) {
        localStorage.setItem("breadcrumb3", "产品详情");
        this.$store.commit("breadcrumb3", "产品详情");
        sessionStorage.setItem(
          "addProductType",
          row.productBatchType == 2 ? "hard" : "soft"
        ); //productBatchType==2 硬装   1软装
        this.$router.push({ path: `/detailProduct/${row.id}` });
      }
    },
    showDetailProductsTexture(row) {
      if (row) {
        localStorage.setItem("breadcrumb3", "产品详情");
        this.$store.commit("breadcrumb3", "产品详情");
        sessionStorage.setItem(
          "addProductType",
          row.productBatchType == 2 ? "hard" : "soft"
        ); //productBatchType==2 硬装   1软装
        this.$router.push({ path: `/detailProductTexture/${row.id}` });
      }
    },
    // 跳到产品编辑页面
    goToEdit(res) {
      if (res) {
        localStorage.setItem("breadcrumb3", "产品编辑");
        sessionStorage.setItem(
          "addProductType",
          res.productBatchType == 2 ? "hard" : "soft"
        );
        this.$router.push(`/editProduct/${res.id}`);
        sessionStorage.setItem("isConnectBtn", false);
      }
    },
    goToTextureEdit(res) {
      if (res) {
        localStorage.setItem("breadcrumb3", "产品编辑");
        sessionStorage.setItem(
          "addProductType",
          res.productBatchType == 2 ? "hard" : "soft"
        );
        this.$router.push(`/editProductTexture/${res.id}`);
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 删除一条数据
    handleDel(row) {
      this.$confirm(
        "删除产品后，关联的模型或材质贴图会一起删除，确定要删除该产品?",
        "提示",
        {
          cancelButtonClass: "cancelButtonClass",
          confirmButtonClass: "confirmButtonClass",
          type: "warning",
          center: true
        }
      ).then(() => {
        this.API.DeleteLibraryProduct({
          productIds: row.id
        }).then(res => {
          if (res.success == false) {
            this.$message.success(res.message);
          } else {
            this.$message.success("删除成功");
          }
          this.getProductList();
        });
      });
    },
    batchDelete() {
      // 批量删除
      let ids = [];
      this.multipleSelection.map(function(value) {
        ids.push(value.id);
      });
      if (ids.length > 0) {
        this.$confirm(
          "删除产品后，关联的模型或材质贴图会一起删除，确认批量删除选中的产品吗?",
          "提示",
          {
            cancelButtonClass: "cancelButtonClass",
            confirmButtonClass: "confirmButtonClass",
            type: "warning",
            center: true
          }
        ).then(() => {
          this.API.DeleteLibraryProduct({
            productIds: ids.join(",")
          }).then(res => {
            if (res.success == false) {
              this.$message.success(res.message);
            } else {
              this.$message.success("删除成功");
            }
            this.getProductList();
          });
        });
      } else {
        this.$message.error("请选择一条或多条数据再删除");
      }
    },
    // 分页模块
    handleSizeChange: function(size) {
      // this.pageSize = size;
      console.log(size);
      this.query.limit = size;
      // sessionStorage.setItem('limit',size);
      this.getProductList();
    },
    handleCurrentChange: function(currentPage) {
      // this.currentPage = currentPage;
      console.log(currentPage);
      this.query.page = currentPage;
      // sessionStorage.setItem('currentPage',currentPage);
      this.loading = true;
      this.getProductList();
    },
    syncGoods() {
      if (this.multipleSelection.length === 0) {
        this.$message.error("请选择需要同步的数据");
        return;
      }
      let arr = [];
      this.multipleSelection.map(res => {
        arr.push(res.id);
      });
      console.log(this.multipleSelection);
      this.API.syncGoods(arr.join(",")).then(res => {
        if (res.success) {
          this.$message.success("同步商品成功");
        } else {
          this.$message.error("同步商品失败");
        }
      });
    }
  },
  components: {
    upload,
    productGroup,
    ImgPreView
  },
  computed: {},
  destroyed() {
    //this.resetInput();
  }
  // computed: mapState(['activeName'])
};
</script>

<style scoped lang="scss">
@import "../../../assets/css/mixin.scss";
.searchBtnFrame {
  border-top: solid 1px #ddd;
  margin-bottom: 15px;
  padding: 20px 0 20px 30px;
  .el-button {
    margin-right: 15px;
    width: 100px;
  }
}
.minWidth {
  padding-left: 30px !important;
  min-width: 600px;
}
.operatonBtnTxt {
  color: #ff6419;
  cursor: pointer;
  padding-right: 20px;
}
.dialogDiv {
  background: rgba(0, 0, 0, 0.3);
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 111;
}

.operatonBtn {
  font-size: 14px;
}

.checkboxFrom {
  border-radius: $borderRadius;
  h1 {
    padding: 10px;
    font-size: 14px;
    position: relative;
    border-bottom: solid 1px $greyBorder;
    span {
      position: absolute;
      right: 0;
    }
  }
  div {
    margin-bottom: 50px;
    text-align: center;
  }
  p {
    text-align: center;
  }
}
.deliver-box {
  margin-top: 20px;
  height: 200px;
  overflow-y: scroll;
}

.deliver-box-line {
  display: block;
}
.products {
  position: relative;
  margin-bottom: 10px;
  .el-row {
    margin-bottom: 10px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }

  .bg-purple-dark {
    background: $bg-color-white;
  }
  .btnBox {
    padding: 20px;
    border-top: 1px solid #e4e4e4;
    position: relative;
    display: table;
    text-align: center;
    width: 100%;
    .searchNavBtn {
      width: 100px;
      height: 32px;
      font-size: 12px;
      text-align: center;
      line-height: 8px;
    }
    .btn-right {
      position: absolute;
      right: 20px;
      top: 20px;
      .addProBtn {
        width: 120px;
        height: 32px;
        font-size: 12px;
        text-align: center;
        line-height: 8px;
      }
      .addProBtn1 {
        height: 32px;
        font-size: 12px;
        text-align: center;
        line-height: 8px;
      }
    }
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
    padding: 20px;
    .el-col {
      margin-right: 15px;
    }
  }
  .nonImg {
    background: #f2f2f2;
    height: 90px;
    width: 90px;
    display: inline-block;
    line-height: 90px;
    text-align: center;
    font-size: 12px;
    color: #999;
    border: solid 1px #e3e3e3;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .input_round {
    position: relative;
    margin-left: 15px;
    li {
      display: inline-block;
      margin-right: 15px;
      .widthBtn {
        width: 100px;
      }
      .search {
        width: 100px;
        color: $buttonOrange;
        border: solid 1px $btnOrange;
      }
    }
  }
  .tableFilter {
    height: 80px;
    font-size: 14px;
    line-height: 80px;
    padding: 0 30px;
    display: block;
    background: #fff;
    border-radius: 4px 4px;
    .share {
      height: 46px;
      line-height: 46px;
      padding-left: 25px;
      background: url("../../../assets/images/icons/allot.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/allot2.png") 0 center
          no-repeat;
      }
    }
    .out {
      height: 46px;
      line-height: 46px;
      padding-left: 25px;
      background: url("../../../assets/images/icons/delete.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/delete2.png") 0 center
          no-repeat;
      }
    }
    .open {
      height: 46px;
      line-height: 46px;
      padding-left: 25px;
      background: url("../../../assets/images/icons/secrecy.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/secrecy2.png") 0 center
          no-repeat;
      }
    }
    .offOpen {
      height: 46px;
      line-height: 46px;
      padding-left: 25px;
      background: url("../../../assets/images/icons/batchSecrecy.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/batchSecrecy2.png") 0
          center no-repeat;
      }
    }
    .refresh {
      height: 46px;
      line-height: 46px;
      padding-left: 25px;
      background: url("../../../assets/images/icons/reflesh.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/reflesh2.png") 0 center
          no-repeat;
      }
    }
    .refreshAuto {
      height: 46px;
      line-height: 46px;
      padding-left: 25px;
      background: url("../../../assets/images/icons/hand_01.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/hand_02.png") 0 center
          no-repeat;
      }
    }
    .syncGoods {
      height: 46px;
      line-height: 46px;
      padding-left: 25px;
      background: url("../../../assets/images/icons/tongbu_01.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/tongbu_02.png") 0 center
          no-repeat;
      }
    }
    .mergeGoods {
      height: 46px;
      line-height: 46px;
      padding-left: 25px;
      background: url("../../../assets/images/icons/list_icon_combine.png") 0 center
      no-repeat;
      &:hover {
       background: url("../../../assets/images/icons/list_icon_combine_active.png") 0 center
        no-repeat;
      }
    }
    li {
      display: inline-block;
      margin-right: 22px;

      color: #666;
      cursor: pointer;
      &:hover {
        color: $overButtonOrange;
      }
    }
  }

  .el-button--small {
    display: inline-block;
    padding: 6px 12px;
    margin: 10px 0 0 0;
  }
  .pagination {
    position: relative;
    bottom: 20px;
    margin: 20px 0;
    height: 80px;
    background: $bg-color-white;
    border-radius: 0 0 4px 4px;
    .block {
      margin-top: 24px;
      text-align: center;
    }
  }
}

.addProducts,
.editProducts,
.DetailProducts {
  position: absolute;
  left: -20px;
  top: -20px;
  padding: 20px;
  min-width: 100%;
  height: 100%;
  background: #e4e4e4;
  z-index: 99;
  .titel {
    line-height: 80px;
  }
  .breadcrumb {
    width: 100%;
    height: 80px;
    text-indent: 20px;
    background: #fff;
    line-height: 80px;
  }
  .orangeBtn {
    width: 133px;
    margin: 20px 20px 0 0;
    color: #fff;
    background: $font-color-orange;
    &:hover {
      background: #ff7a39;
    }
    &:active {
      background: #fa580a;
    }
    .iconBack {
    }
  }
  .contentWrap {
    width: 97.6%;
    height: 100%;
    margin-top: 10px;
    padding: 20px;
    background: #fff;
    .titel {
      font-size: 20px;
      line-height: 36px;
      font-weight: bold;
    }
    .hr {
      width: 97.6%;
      padding: 0 20px;
      border-bottom: 1px solid #eee;
    }
    .describe {
      margin-top: 40px;
      font-size: 14px;
      i {
        color: red;
        margin-right: 10px;
        vertical-align: middle;
      }
    }
    .mapsInfo {
      margin-top: 10px;
      margin-bottom: 30px;
      height: 40px;
      background: #f6f6f6;
      line-height: 40px;
      i {
        display: inline-block;
        width: 6px;
        height: 40px;
        background: red;
        vertical-align: middle;
      }
      span {
        font-size: 16px;
        font-weight: bold;
        margin-left: 10px;
      }
    }
    .input-item {
      display: inline-block;
    }
    .size-w,
    .size-h {
      display: inline-block;
    }
    .mapsSelect {
      display: block;
    }
    .desc {
      max-width: 886px;
      height: 130px;
      textarea {
        background: #f6f6f6;
      }
    }
    .blackBtn {
      color: $bg-color-white;
      background: $greyButton;
    }
  }
}

.pageFrame {
  background: #fff;
  height: 110px;
  padding-top: 40px;
  box-sizing: border-box;
  text-align: center;
}

.inactive-icon-class {
  background: #5daf34;
  border: solid 1px #ddd !important;
}

.active-icon-class {
  background: #5daf34;
  border: solid 1px #ddd !important;
}
.checkAll {
  padding-left: 70px;
  margin-bottom: 10px;
}
</style>
<style>
.el-table td {
  padding-left: 20px;
}

.el-table th {
  height: 60px;
  background: #f3f3f3;
  padding-left: 20px;
  text-align: center;
}
</style>
