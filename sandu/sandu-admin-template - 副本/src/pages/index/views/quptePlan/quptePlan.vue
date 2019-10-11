<template>
  <div>
    <!-- 列表页 -->
    <el-row v-if="pageStatus==0" v-loading="tbLoading">
      <!-- 切换空间 -->
      <div class="card nav" style="border-bottom: 1px solid #f5f5f5;">
        <span :class="tbParams.planType==2?'active':''" @click="switchTab(2)">全屋</span>
        <el-divider direction="vertical"></el-divider>
        <span :class="tbParams.planType==1?'active':''" @click="switchTab(1)">单空间</span>
      </div>
      <!-- 筛选栏 -->
      <div class="card join-top">
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
            <span>是否报价：</span>
            <el-select
              v-model="searchParams.quotedByCurrentCompany"
              size="small"
              placeholder="请选择"
              clearable
            >
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
          </div>
          <div class="search-item">
            <div class="btn" @click="tbParams.page=1;getPageList()">筛选</div>
            <div class="btn plain" @click="reset">重置</div>
          </div>
        </div>
      </div>
      <!-- 表格 -->
      <div class="card border">
        <div class="_720box">
          <div class="_720item" v-for="(item,index) in tbList" :key="index">
            <el-image class="image" fit="fill" :src="BASE_URL.sourceBaseUrl+item.picPath"></el-image>
            <el-button
              v-if="item.quotedByCurrentCompany"
              style="position:absolute;left:-14px;top:0;"
              type="primary"
              size="medium"
              round
            >已报价</el-button>
            <div class="bottom" style="line-height: 30px;height:64px">
              <div>
                <p style="font-weight:700">【{{item.styleName}}】{{item.planName}}</p>
                <p>【{{item.spaceName}}】</p>
              </div>
              <el-button type="primary" size="medium" plain round @click="toDetail(item)">报价</el-button>
            </div>
          </div>
          <div style="width:100%;text-align:center" v-if="tbList.length==0">暂无数据</div>
        </div>
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
      <el-breadcrumb class="app-breadcrumb" separator="|">
        <el-breadcrumb-item>
          <span style="color:#FF6419;cursor:pointer" @click="pageStatus=0;getPageList()">返回上一页</span>
        </el-breadcrumb-item>
        <el-breadcrumb class="app-breadcrumb" separator=">">
          <el-breadcrumb-item>
            <span style="color:#FF6419;cursor:pointer" @click="pageStatus=0;getPageList()">报价方案管理</span>
          </el-breadcrumb-item>
          <el-breadcrumb-item>查看方案</el-breadcrumb-item>
        </el-breadcrumb>
      </el-breadcrumb>
      <el-row class="card border">
        <span class="title">全屋方案信息</span>
      </el-row>
      <div class="card join-top">
        <el-form label-width="100px" style="color:#999">
          <el-form-item label="方案名称：">{{tbDetail.planName||"-"}}</el-form-item>
          <el-form-item label="房间类型：">{{tbDetail.spaceName||"-"}}</el-form-item>
          <el-form-item label="设计风格：">{{tbDetail.styleName||"-"}}</el-form-item>
          <el-form-item label="方案ID：">{{tbDetail.planId||"-"}}</el-form-item>
          <el-form-item label="方案描述：" style="max-width:1050px">
            <el-input
              type="textarea"
              show-word-limit
              :maxlength="500"
              placeholder="请输入方案描述"
              v-model="tbDetail.planDesc"
              v-if="planDescEdit"
            ></el-input>
            <span v-else>{{tbDetail.planDesc}}</span>
          </el-form-item>
        </el-form>
      </div>
      <el-row class="card join-top border">
        <span class="title">720效果图</span>
      </el-row>
      <div class="card join-top">
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
              <el-button
                type="primary"
                size="medium"
                plain
                round
                @click="openPlanProducts(item.planId)"
              >产品清单</el-button>
            </div>
            <div class="bottom">
              <div>
                <span>我的报价（元/㎡）</span>
                <el-input
                  :maxlength="4"
                  v-model="item.price"
                  style="width:150px"
                  oninput="this.value=this.value.replace(/\D/g,'')"
                ></el-input>
                <i class="el-icon-edit-outline ico" @click="update(item)"></i>
              </div>
            </div>
          </div>
          <div v-if="tbDetail.planDetails.length==0">暂无数据</div>
        </div>
      </div>
    </div>

    <!-- 产品清单 -->
    <el-dialog title="产品清单" :visible.sync="productVisible" width="700px" style="text-align:left;">
      <div class="product-dialog">
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
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      nav: [
        { nav: "装修报价", url: "/quptePlan/quptePlan" },
        { nav: "报价", url: "" }
      ],
      headerStyle: {
        backgroundColor: "#f5f5f5",
        textAlign: "center"
      },
      tabPosition: "jiaju",
      provinceAndCity: [],
      selectedOptions: [],
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
        quotedByCurrentCompany: undefined //是否公开报价 1：公开，0：不公开
      },
      tbParams: {
        page: 1,
        limit: 10,
        planType: 2, //方案类型 1:单空间，2：全屋
        companyId: sessionStorage.getItem("companyID")
      },
      fullTbParams: {
        page: 1,
        limit: 10
      },
      detailForFull: {}
    };
  },
  created() {
    //空间类型
    this.API2.getHouse().then(res => {
      if (res.data) this.spaceTypeList = res.data;
    });
    //风格
    this.API2.getStyle().then(res => {
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
      this.API2.getQuotedPrice({
        ...this.tbParams, // == 把对象的{}去掉
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
      console.log(row);
      this.API2.getQuotedPriceDetail({
        planId: row.planId,
        planType: this.tbParams.planType,
        companyId: this.tbParams.companyId
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
            this.API2.reViewPlanImges({
              planId: e.planId
            }).then(res => {
              this.$set(
                details.planDetails[i],
                "vr720Single",
                res.data.vr720Single
              );
            });
          });
        });
    },
    update(row) {
      if (
        typeof row.price != "number" &&
        (row.price == undefined ||
          row.price.length == 0 ||
          row.price.search(/[^\d]/g) != -1)
      ) {
        this.$message({
          message: "报价价格必须是正整数",
          type: "error"
        });
        return;
      }
      this.API2.quoted({
        planId: row.planId,
        price: row.price,
        companyId: this.tbParams.companyId
      }).then(res => {
        this.$message({
          message: res.message,
          type: res.success ? "success" : "error"
        });
      });
    },
    openPlanProducts(id) {
      this.API2.getPlanProducts({ planId: id }).then(res => {
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
    }
  }
};
</script>

<style lang="scss" scoped>
.card {
  margin-bottom: 20px;
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
    color: #ff6419;
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
      background-color: #ff6419;
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
    width: 490px;
    position: relative;
    margin: 10px 8px;
    overflow: hidden;
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
      height: 54px;
      background-color: #fafafa;
      display: inline-flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 10px;
      box-sizing: border-box;
    }
    .bottom ~ .bottom {
      border-top: 1px solid #dddddd;
    }
  }
}
.ico {
  vertical-align: middle;
  font-size: 25px;
  padding-left: 4px;
  cursor: pointer;
}
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 20px;
}
.el-breadcrumb__item:first-of-type {
  width: auto;
}
.product-dialog {
  max-height: 60vh;
  overflow-y: auto;
  overflow-x: hidden;
}
</style>
