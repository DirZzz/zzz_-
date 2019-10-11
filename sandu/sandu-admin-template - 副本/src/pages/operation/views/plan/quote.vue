<template>
  <div>
    <!-- 列表页 -->
    <el-row v-if="pageStatus==0" v-loading="tbLoading">
      <!-- 切换空间 -->
      <div class="card nav join-bottom" style="border-bottom: 1px solid #f5f5f5;">
        <span :class="tbParams.planType==1?'active':''" @click="switchTab(1)">单空间</span>
        <el-divider direction="vertical"></el-divider>
        <span :class="tbParams.planType==2?'active':''" @click="switchTab(2)">全屋</span>
      </div>
      <!-- 筛选栏 -->
      <div class="card">
        <div class="search-box">
          <div class="search-item">
            <span>方案ID：</span>
            <el-input
              v-model="searchParams.planId"
              maxlength="20"
              size="small"
              placeholder="请输入方案ID"
            ></el-input>
          </div>
          <div class="search-item">
            <span>方案名称：</span>
            <el-input
              v-model="searchParams.planName"
              maxlength="50"
              size="small"
              placeholder="请输入方案名称"
            ></el-input>
          </div>
          <div class="search-item">
            <span>空间类型：</span>
            <el-select
              v-model="searchParams.spaceFunctionId"
              size="small"
              placeholder="请选择"
              clearable
            >
              <el-option
                v-for="(item, index) in spaceTypeList"
                :key="index"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div class="search-item">
            <span>风格：</span>
            <el-select v-model="searchParams.styleValue" size="small" placeholder="请选择" clearable>
              <el-option
                v-for="(item, index) in styleList"
                :key="index"
                :label="item.styleName"
                :value="item.groupId"
              ></el-option>
            </el-select>
          </div>
          <div class="search-item">
            <span>公开报价：</span>
            <el-select
              v-model="searchParams.publicQuotedPrice"
              size="small"
              placeholder="请选择"
              clearable
            >
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
          </div>
          <!-- #409EFF -->
          <div class="search-item">
            <div class="btn" @click="tbParams.page=1;getPageList()">筛选</div>
            <div class="btn plain" @click="reset">重置</div>
          </div>
        </div>
      </div>
      <!-- 表格 -->
      <div class="card">
        <el-table
          empty-text="暂无数据"
          :data="tbList"
          :header-cell-style="headerStyle"
          style="width: 100%"
        >
          <el-table-column prop="planId" label="ID" width="80"></el-table-column>
          <el-table-column prop="picPath" label="缩略图" width="120">
            <template slot-scope="scope">
              <el-image
                v-if="scope.row.picPath"
                style="width:100px;height:80px"
                :src="BASE_URL.sourceBaseUrl + scope.row.picPath"
                fit="scale-down"
              ></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="planName" label="名称" align="center"></el-table-column>
          <el-table-column prop="styleName" label="风格" width="120" align="center"></el-table-column>
          <el-table-column prop="spaceName" label="房间类型" width="80"></el-table-column>
          <el-table-column prop="putShopDate" label="上架时间" width="150"></el-table-column>
          <el-table-column prop="companyName" label="所属企业" align="center"></el-table-column>
          <el-table-column label="浏览/点赞/收藏" width="120" align="center">
            <template slot-scope="scope">
              <span>
                {{tbList[scope.$index].countInfo.view+tbList[scope.$index].countInfo.virtualView}} /
                {{tbList[scope.$index].countInfo.like+tbList[scope.$index].countInfo.virtualLike}} /
                {{tbList[scope.$index].countInfo.favorite+tbList[scope.$index].countInfo.virtualFavorite}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="quotedPriceCountInCountry"
            label="全国总报价次数"
            width="80"
            align="center"
          >
            <template slot-scope="scope">{{scope.row.quotedPriceCountInCountry||"-"}}</template>
          </el-table-column>
          <el-table-column
            prop="quotePriceInShenZhen"
            label="深圳综合报价(元/m²)"
            width="110"
            align="center"
          >
            <template slot-scope="scope">{{scope.row.quotePriceInShenZhen||"-"}}</template>
          </el-table-column>
          <el-table-column prop="publicQuotedPriceFlag" label="是否公开报价" width="80">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.publicQuotedPriceFlag"
                :active-value="1"
                :inactive-value="0"
                @change="update(scope.row,{quotedPricePublicFlag:scope.row.publicQuotedPriceFlag?1:0})"
                active-color="#409EFF"
                inactive-color="#999"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="110">
            <template slot-scope="scope">
              <el-button type="text" @click="toDetail(scope.row)">查看方案</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row style="margin-top:20px;text-align:right">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="tbParams.page"
            :page-sizes="[10, 20, 30, 40]"
            :page-size.sync="tbParams.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total.sync="tbParams.total"
          ></el-pagination>
        </el-row>
      </div>
    </el-row>

    <!-- 详情页 -->
    <div v-if="pageStatus==1">
      <el-row class="card join-bottom">
        <el-breadcrumb separator=">">
          <el-breadcrumb-item>
            <a @click="pageStatus=0">报价方案管理</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>查看方案</el-breadcrumb-item>
        </el-breadcrumb>
      </el-row>
      <el-row class="card join-bottom border" style="padding-top:0">
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="title">查看方案</span>
          <el-button
            icon="el-icon-refresh"
            size="small"
            type="primary"
            plain
            @click="pageStatus=0"
          >返回</el-button>
        </div>
      </el-row>
      <div class="card">
        <el-form :inline="true" label-width="100px" style="color:#999">
          <el-form-item label="方案名称：">{{tbDetail.planName||"-"}}</el-form-item>
          <el-form-item label="房间类型：">{{tbDetail.spaceName||"-"}}</el-form-item>
          <el-form-item label="设计风格：">{{tbDetail.styleName||"-"}}</el-form-item>
          <el-form-item label="方案ID：">{{tbDetail.planId||"-"}}</el-form-item>
        </el-form>
        <el-form label-width="100px">
          <el-form-item label="方案描述：" style="max-width:1050px">
            <el-input
              type="textarea"
              show-word-limit
              :autosize="{ minRows: 3}"
              :maxlength="500"
              placeholder="请输入方案描述"
              v-model="tbDetail.planDesc"
              v-if="planDescEdit"
            ></el-input>
            <span v-else>{{tbDetail.planDesc}}</span>
          </el-form-item>
          <el-form-item label style="max-width:1050px;text-align:right">
            <el-button
              type="primary"
              size="small"
              @click="planDescEdit?update(tbDetail,{planDesc:tbDetail.planDesc}):planDescEdit=true"
            >{{planDescEdit?"保存":"编辑"}}</el-button>
            <el-button v-if="planDescEdit" size="small" @click="planDescEdit=false">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="card border">
        <h2 class="title">720效果图</h2>
        <!-- <div class="_720box" v-for="(item,index) in tbDetail.planDetails" :key="index"> -->
        <div class="_720box">
          <div class="_720item" v-for="(item,index) in tbDetail.planDetails" :key="index">
            <el-image class="image" fit="fill" :src="BASE_URL.sourceBaseUrl+item.picPath"></el-image>
            <a class="el-icon-view icon" :href="item.vr720Single" target="view_window"></a>
            <div class="bottom">
              <p>
                <span style="font-weight:700">【{{item.spaceName}}】</span>
                <span>{{item.planName}}</span>
              </p>
              <el-button type="primary" size="medium" @click="openPlanProducts(item.planId)">产品清单</el-button>
            </div>
          </div>
        </div>
      </div>
      <el-row class="card" v-if="detailForFull.list" v-loading="fullTbLoading">
        <el-form ref="form" label-width="100px">
          <el-form-item label="选择区域">
            <el-cascader
              @change="updateForFull({cityCode:selectedOptions[1]},0)"
              expand-trigger="hover"
              :options="provinceAndCity"
              v-model="selectedOptions"
            ></el-cascader>
          </el-form-item>
          <el-form-item label="综合报价：">
            <el-radio v-model="detailForFull.priceMode" :label="0" @change="priceModeChange">
              最低价
              <template v-if="detailForFull.priceMode==0">
                <span v-if="detailForFull.quotedPrice">({{detailForFull.quotedPrice}}元/㎡)</span>
                <span v-else>暂无最低价</span>
              </template>
            </el-radio>
            <el-radio v-model="detailForFull.priceMode" :label="1" @change="priceModeChange">
              均价
              <template v-if="detailForFull.priceMode==1">
                <span v-if="detailForFull.quotedPrice">({{detailForFull.quotedPrice}}元/㎡)</span>
                <span v-else>暂无均价</span>
              </template>
            </el-radio>
            <el-radio
              v-if="detailForFull.priceMode==2||detailForFull.quotedPrice"
              v-model="detailForFull.priceMode"
              :label="2"
              @change="detailForFull.quotedPrice=undefined"
              class="radioBox"
            >
              手动
              <el-input
                v-if="detailForFull.priceMode==2"
                type="number"
                style="width:100px"
                v-model="detailForFull.quotedPrice"
                size="small"
              ></el-input>
              <el-input v-else style="width:100px" size="small" disabled></el-input>
              <span>元/㎡</span>
              <i
                class="el-icon-edit ico"
                v-if="detailForFull.priceMode==2"
                @click="updateForFull(detailForFull)"
              ></i>
            </el-radio>
          </el-form-item>
        </el-form>
        <el-table
          v-loading="tbLoading"
          empty-text="暂无数据"
          :data="detailForFull.list"
          :header-cell-style="headerStyle"
          style="width: 100%"
        >
          <el-table-column type="index" label="编号" width="50"></el-table-column>
          <el-table-column prop="companyName" label="报价公司" align="center"></el-table-column>
          <el-table-column prop="shopName" label="企业店铺" align="center"></el-table-column>
          <el-table-column
            :prop="`prices[${i}].price`"
            v-for="(p,i) in detailForFull.list[0].prices"
            :key="i"
            :label="p.spaceName+'(元/m²)'"
            align="center"
          ></el-table-column>
          <el-table-column prop="avgPrice" label="最低报价(元/m²)" width="120" align="center"></el-table-column>
          <el-table-column prop="quotedPriceTime" label="报价时间" width="150" align="center"></el-table-column>
          <el-table-column prop="sort" label="排序" align="center" width="150">
            <template slot-scope="scope">
              <!-- <div v-if="!isEditSort[scope.row.index]">
                <span>{{ scope.row.sort }}</span>
                <img
                  class="bianji_ico"
                  src="../../assets/images/icon/icon_edit.png"
                  @click="$set(isEditSort,scope.row.index,true)"
                >
              </div>
              <div v-else style="display:flex;align-items:center">
                <input
                  v-model="scope.row.sort"
                  class="bianji_input"
                  maxlength="4"
                  oninput="this.value=this.value.replace(/[^0-9]/g,'')"
                >
                <el-button-group>
                  <el-button
                    class="bianji_button"
                    style="border-radius:0px;border-left:none"
                    @click="updateForFull({sort:scope.row.sort,companyId:scope.row.companyId});$set(isEditSort,scope.row.id,false)"
                  >
                    <img src="../../assets/images/icon/icon_ok.png">
                  </el-button>
                  <el-button class="bianji_button" @click="$set(isEditSort,scope.row.index,false)">
                    <img src="../../assets/images/icon/icon_close.png">
                  </el-button>
                </el-button-group>
              </div>-->
              <editBox
                :data="scope.row.sort"
                :editFn="updateForFull"
                editField="sort"
                :moreFnParams="{companyId:scope.row.companyId}"
                :maxlength="4"
                :regEx="/[^/d]g/"
              ></editBox>
            </template>
          </el-table-column>
          <el-table-column prop="isDisplay" label="是否显示" width="80" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.isDisplay"
                @change="updateForFull({isDisplay:scope.row.isDisplay,companyId:scope.row.companyId})"
                active-color="#409EFF"
                inactive-color="#999"
                :active-value="1"
                :inactive-value="-1"
              ></el-switch>
            </template>
          </el-table-column>
        </el-table>
        <el-row style="margin-top:20px;text-align:right">
          <el-pagination
            @size-change="handleSizeChange2"
            @current-change="handleCurrentChange2"
            :current-page.sync="fullTbParams.page"
            :page-sizes="[10, 20, 30, 40]"
            :page-size.sync="fullTbParams.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total.sync="fullTbParams.total"
          ></el-pagination>
        </el-row>
      </el-row>
      <div class="card" v-else>暂无报价公司报价！</div>
    </div>

    <!-- 产品清单 -->
    <el-dialog title="产品清单" :visible.sync="productVisible" width="800px" style="text-align:left;" :before-close="resetParams">
      <div class="product-dialog">
        <div class="productSearch">
          <div class="searchItem">
            <span>编号：</span>
            <el-input v-model="searchProductParams.productNumber" maxlength="20" size="small"></el-input>
          </div>
          <div class="searchItem">
            <span>产品名称：</span>
            <el-input v-model="searchProductParams.productName" maxlength="50" size="small"></el-input>
          </div>
          <div class="searchItem">
            <div class="btn" @click="searchProduct">搜索</div>
          </div>
        </div>
        <el-collapse v-model="tabPosition" accordion>
          <el-collapse-item title="软装" name="jiaju">
            <el-table :data="productObj.jiaju" border style="width:100%;text-align:center">
              <el-table-column prop="code" label="编号"></el-table-column>
              <el-table-column prop="name" label="产品名称"></el-table-column>
              <el-table-column prop="picPath" label="主图" width="120">
                <template slot-scope="scope">
                  <el-image
                    style="width:100px;height:80px"
                    :src="scope.row.picPath"
                    fit="scale-down"
                  ></el-image>
                </template>
              </el-table-column>
              <el-table-column prop="brandName" label="品牌"></el-table-column>
              <el-table-column prop="modelNumber" label="型号"></el-table-column>
              <el-table-column prop="decorationPrice" label="装修价格" align="center" width="200">
                <template slot-scope="scope">
                  <div class="priceEdit">
                    <editBox
                      :data="scope.row.decorationPrice"
                      :editFn="updatePrice"
                      editField="decorationPrice"
                      :moreFnParams="{obj:scope}"
                      :maxlength="10"
                      :inputType="'number'"
                      :regEx="/[^/d]g/"
                    ></editBox>
                    <div>{{scope.row.valuationUnit}}</div>
                  </div>
                </template>
                
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="硬装" name="jiancai">
            <el-table :data="productObj.jiancai" border style="width:100%;text-align:center">
              <el-table-column prop="code" label="编号"></el-table-column>
              <el-table-column prop="name" label="产品名称"></el-table-column>
              <el-table-column prop="picPath" label="主图" width="120">
                <template slot-scope="scope">
                  <el-image
                    style="width:100px;height:80px"
                    :src="scope.row.picPath"
                    fit="scale-down"
                  ></el-image>
                </template>
              </el-table-column>
              <el-table-column prop="brandName" label="品牌"></el-table-column>
              <el-table-column prop="modelNumber" label="型号"></el-table-column>
              <el-table-column prop="decorationPrice" label="装修价格" align="center" width="200">
                <template slot-scope="scope">
                  <div class="priceEdit">
                    <editBox
                      :data="scope.row.decorationPrice"
                      :editFn="updatePrice"
                      editField="decorationPrice"
                      :moreFnParams="{obj:scope}"
                      :maxlength="10"
                      :inputType="'number'"
                      :regEx="/[^/d]g/"
                    ></editBox>
                    <div>{{scope.row.valuationUnit}}</div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="电器" name="jiayongdianqi">
            <el-table :data="productObj.jiayongdianqi" border style="width:100%;text-align:center">
              <el-table-column prop="code" label="编号"></el-table-column>
              <el-table-column prop="name" label="产品名称"></el-table-column>
              <el-table-column prop="picPath" label="主图" width="120">
                <template slot-scope="scope">
                  <el-image
                    style="width:100px;height:80px"
                    :src="scope.row.picPath"
                    fit="scale-down"
                  ></el-image>
                </template>
              </el-table-column>
              <el-table-column prop="brandName" label="品牌"></el-table-column>
              <el-table-column prop="modelNumber" label="型号"></el-table-column>
              <el-table-column prop="decorationPrice" label="装修价格" align="center" width="200">
                <template slot-scope="scope">
                  <div class="priceEdit">
                    <editBox
                      :data="scope.row.decorationPrice"
                      :editFn="updatePrice"
                      editField="decorationPrice"
                      :moreFnParams="{obj:scope}"
                      :maxlength="10"
                      :inputType="'number'"
                      :regEx="/[^/d]g/"
                    ></editBox>
                    <div>{{scope.row.valuationUnit}}</div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import editBox from "&/components/common/editBox";
export default {
  components: { editBox },
  data() {
    return {
      headerStyle: {
        backgroundColor: "#f5f5f5",
        textAlign: "center"
      },
      isEditSort: {},
      tabPosition: "jiaju",
      provinceAndCity: [],
      selectedOptions: [],
      fullTbLoading: true,
      tbLoading: true,
      productVisible: false,
      productObj: {},
      spaceTypeList: [],
      styleList: [],
      tbList: [],
      tbDetail: {},
      planDescEdit: false,
      fullPrice: {},
      pageStatus: 0, //页面状态 0：列表，1：操作
      searchParams: {
        planId: undefined, //方案ID
        planName: undefined, //方案名称
        spaceFunctionId: undefined, //空间类型
        styleValue: undefined, //风格
        publicQuotedPrice: undefined //是否公开报价 1：公开，0：不公开
      },
      searchProductParams: {
        productNumber: undefined, //产品编号
        productName: undefined //产品名称
      },
      tbParams: {
        page: 1,
        limit: 10,
        total: 0,
        planType: 1 //方案类型 1:单空间，2：全屋
      },
      fullTbParams: {
        page: 1,
        limit: 10,
        total: 0
      },
      detailForFull: { priceMode: 0 },
      checkedPlanId: "" //当前产品清单planId
    };
  },
  created() {
    //空间类型
    this.API.getHouse().then(res => {
      if (res.data) this.spaceTypeList = res.data;
    });
    //风格
    this.API.getStyle().then(res => {
      if (res.data) this.styleList = res.data;
    });
    this.getPageList();
  },
  methods: {
    // 页签切换
    switchTab(type) {
      this.tbParams.planType = type;
      this.reset();
      this.getPageList();
    },
    // 重置搜索栏
    reset() {
      this.searchParams = {
        planId: undefined, //方案ID
        planName: undefined, //方案名称
        spaceFunctionId: undefined, //空间类型
        styleValue: undefined, //风格
        publicQuotedPrice: undefined //是否公开报价 1：公开，0：不公开
      };
      this.tbParams = Object.assign(this.tbParams, {
        page: 1,
        limit: 10
      });
    },
    getPageList() {
      this.tbLoading = true;
      this.API.getQuotedPrice({
        ...this.tbParams,
        ...this.searchParams
      })
        .then(res => {
          if (res && res.success) {
            this.tbList = res.datalist;
            this.tbParams.total = res.totalCount;
          } else
            this.$message({
              message: res.message || "数据异常",
              type: "error"
            });
        })
        .finally(() => (this.tbLoading = false));
    },
    toDetail(row) {
      this.provinceAndCity = [];
      this.selectedOptions = [];
      this.API.getQuotedPriceDetail({
        planId: row.planId,
        planType: this.tbParams.planType
      })
        .then(res => {
          if (res && res.success) {
            this.pageStatus = 1;
            this.tbDetail = res.obj;
            if (this.tbDetail.planDetails.length > 0) return this.tbDetail;
          } else
            this.$message({
              message: res.message || "数据异常",
              type: "error"
            });
        })
        .then(details => {
          // 720
          details.planDetails.forEach((e, i) => {
            this.API.reViewPlanImges({
              planId: e.planId
            }).then(res => {
              this.$set(
                details.planDetails[i],
                "vr720Single",
                res.data.vr720Single
              );
            });
          });
          // 省市下拉框
          this.API.getplanQuotedPriceArea({
            planId: details.planId,
            planType: this.tbParams.planType
          }).then(res => {
            if (res && res.success) {
              this.provinceAndCity = res.obj.map((e, i) => {
                if (i == 0 && this.selectedOptions.length == 0)
                  this.selectedOptions.push(e.code);
                if (e.name.indexOf("广东") != -1)
                  this.selectedOptions[0] = e.code;
                return {
                  label: e.name,
                  value: e.code,
                  children: e.children.map((esub, isub) => {
                    if (isub == 0 && this.selectedOptions.length == 1)
                      this.selectedOptions.push(esub.code);
                    if (esub.name.indexOf("深圳") != -1)
                      this.selectedOptions[1] = esub.code;
                    return { label: esub.name, value: esub.code };
                  })
                };
              });
              this.getCompanyPrice();
            } else {
              this.provinceAndCity = [];
            }
          });
        });
    },
    update(row, params) {
      this.tbLoading = true;
      this.API.updateQuotedPrice({
        planType: this.tbParams.planType,
        planId: row.planId,
        ...params
      })
        .then(res => {
          if (res && res.success) this.planDescEdit = false;
          this.$message({
            message: res.message,
            type: res.success ? "success" : "error"
          });
        })
        .finally(() => {
          this.getPageList();
        });
    },
    updateForFull(params, isT = true) {
      this.fullTbLoading = true;
      this.API.updateCompanyPrice({
        planType: this.tbParams.planType,
        planId: this.tbDetail.planId,
        priceMode: this.detailForFull.priceMode,
        cityCode: this.selectedOptions[1],
        ...params
      })
        .then(res => {
          if (isT)
            this.$message({
              message: res.message,
              type: res.success ? "success" : "error"
            });
        })
        .finally(() => {
          this.getCompanyPrice();
          this.getPageList();
        });
    },
    priceModeChange(val) {
      this.updateForFull({ priceMode: val }, 0);
    },
    openPlanProducts(id) {
      this.checkedPlanId = id;
      this.API.getPlanProducts({ planId: id }).then(res => {
        if (res && res.success) {
          
          this.productObj = res.obj;
          this.productVisible = true;
        }
      });
    },
    // 分页模块f
    handleSizeChange: function(size) {
      this.tbParams.limit = size;
      this.tbParams.page = 1;
      this.pageList = [];
      this.getPageList();
    },
    handleCurrentChange: function(currentPage) {
      this.tbParams.page = currentPage;
      this.getPageList();
    },
    handleSizeChange2: function(size) {
      this.fullTbParams.limit = size;
      this.fullTbParams.page = 1;
      this.pageList = [];
      this.getList();
    },
    handleCurrentChange2: function(currentPage) {
      this.fullTbParams.page = currentPage;
      this.getList();
    },
    // 报价列表
    getCompanyPrice() {
      this.API.getCompanyPrice({
        planId: this.tbDetail.planId,
        planType: this.tbParams.planType,
        provinceCode: this.selectedOptions[0],
        cityCode: this.selectedOptions[1],
        ...this.fullTbParams
      }).then(res => {
        if (res && res.success) {
          this.detailForFull = res.obj;
          this.fullTbParams.total = res.totalCount;
          for (let i = 0; i < this.tbDetail.planDetails.length; i++) {
            const d = this.tbDetail.planDetails[i];
            let filter = this.detailForFull.list[0].prices.filter(e => {
              return e.planId == d.planId;
            });
            if (filter.length) filter[0].spaceName = d.spaceName;
            else
              this.detailForFull.list[0].prices.push({
                spaceName: d.spaceName
              });
          }
        } else {
          this.detailForFull = {};
        }
        this.fullTbLoading = false;
      });
    },
    searchProduct() {
      //搜索产品
      this.API.getPlanProducts({
        planId: this.checkedPlanId,
        productCode: this.searchProductParams.productNumber,
        productName: this.searchProductParams.productName
      }).then(res => {
        if (res && res.success) {
          
          this.productObj = res.obj;
        }
      });
    },
    updatePrice(e){
      console.log(e)
      let params={
        productCode:e.obj.row.code,
        decorationPrice:e.decorationPrice
      }
      this.API.updateProductPrice(params).then(res=>{
        if(res.success){
          this.searchProduct();
          this.$message({
            message: '修改成功',
            type:"success" 
          });
        }else{
          this.$message({
            message: '修改失败',
            type:"error" 
          });
        }
      })
    },
    resetParams(){//关闭弹窗时重置搜索项
      this.searchProductParams={
        productNumber: undefined, //产品编号
        productName: undefined //产品名称
      }
      this.checkedPlanId="";
      this.productVisible=false;
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../../components/common/yangtian.scss";
.card {
  margin: 20px;
  background-color: #fff;
  border-radius: 5px;
  padding: 20px;
  text-align: left;
  .title {
    font-size: 16px;
    padding: 10px;
    font-weight: 700;
  }
}
.nav {
  span {
    font-weight: 900;
    cursor: pointer;
  }
  .active {
    color: #409eff;
  }
}
.border {
  border-bottom: 1px solid #e8e8e8;
}
.join-top {
  margin-top: -20px;
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
}
.search-box {
  font-size: 14px;
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  .search-item {
    height: 30px;
    line-height: 30px;
    margin: 0 20px 20px 0;
    display: flex;
    justify-content: space-between;
    span {
      width: 80px;
      text-align: right;
    }
    .el-input,
    .el-select {
      width: 150px;
    }
    .btn {
      font-weight: 700;
      width: 80px;
      height: 32px;
      line-height: 32px;
      border-radius: 16px;
      margin: 0 10px 20px 10px;
      text-align: center;
      color: #fff;
      background-color: #409eff;
      cursor: pointer;
    }
    .btn.plain {
      background-color: #f5f5f5;
      border: solid 1px #dddddd;
      color: #999999;
    }
  }
}
._720box {
  display: flex;
  flex-wrap: wrap;
  ._720item {
    width: 508px;
    height: 348px;
    position: relative;
    border-bottom: 1px solid #dddddd;
    margin: 10px 8px;
    .image {
      width: 100%;
      height: 292px;
    }
    .icon {
      width: 54px;
      height: 37px;
      background-color: #3333;
      color: #fff;
      position: absolute;
      top: 0;
      right: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 28px;
      cursor: pointer;
    }
    .bottom {
      font-size: 14px;
      width: 100%;
      height: 56px;
      background-color: #fafafa;
      display: inline-flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 10px;
    }
  }
}
.bianji_ico {
  width: 16px;
  height: 16px;
  vertical-align: middle;
  cursor: pointer;
}
.bianji_input {
  width: 50px;
  height: 32px;
  background-color: #f5f5f5;
  border-radius: 4px 0px 0px 4px;
  border: solid 1px #d9d9d9;
  padding: 0 6px;
}
.bianji_button {
  width: 32px;
  height: 32px;
  background-color: #fff;

  border: solid 1px #d9d9d9;
  padding: 0 6px;
}
.product-dialog {
  max-height: 60vh;
  overflow-y: auto;
  overflow-x: hidden;
  .productSearch {
    padding: 20px;
    font-size: 14px;
    display: flex;
    -webkit-box-pack: start;
    justify-content: space-between;
    flex-wrap: wrap;
    .searchItem {
      height: 30px;
      line-height: 30px;
      margin: 0 20px 20px 0;
      display: flex;
      justify-content: space-between;
      .el-input {
        width: 150px;
      }
      .btn {
        width: 60px;
        height: 30px;
        line-height: 30px;
        border-radius: 5px;
        margin: 0 20px 20px 0;
        text-align: center;
        color: #fff;
        background-color: #409eff;
        cursor: pointer;
      }
    }
  }
}
.priceEdit{
  display: flex;
  justify-content: center;
}
.ico {
  vertical-align: middle;
  font-size: 16px;
  margin-left: 4px;
  cursor: pointer;
  padding: 5px;
  border: 2px solid #3399ffdd;
  border-radius: 50%;
}
.radioBox {
  padding: 4px 10px;
  background-color: #fafafa;
  border-radius: 4px;
  border: solid 1px #f1f1f1;
}
.el-form--inline .el-form-item {
  margin-right: 30px;
}
</style>
<style lang="scss" scoped src='../../components/common/yangtian.scss'></style>
