<template>
  <div>
    <!-- 智能方案详情页面 -->
    <pos-nav></pos-nav>
    <div class="planDetailFrame">
      <el-button round type="primary" @click="gotoAddProduct"><i class="el-icon-edit"></i> 编辑</el-button>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="基础信息" name="first">
          <div class="planLibBaseMsg">
            <el-col :span="12">
              <ul>
                <li><span>方案名称：</span> <b>{{planInfo.planName}}</b></li>
                <li><span>方案编号：</span> <b>{{planInfo.planCode}}</b></li>
                <li><span>来源：</span> <b>显示方案来源</b></li>
                <li><span>完成时间：</span> <b>{{planInfo.completeDate}}</b></li>
                <li><span>品牌：</span> <b>{{planInfo.brandName}}</b></li>
                <li><span>设计师：</span> <b>{{planInfo.designer}}</b></li>
                <li><span>方案类型：</span> <b>普通方案</b></li>
                <li><span>风格：</span> <b>{{planInfo.styleId}}</b></li>
                <li><span>方案描述：</span> <b class="remarks">{{planInfo.planDesc}}</b></li>
                <li><span>备注：</span> <b class="remarks">{{planInfo.remarks}}</b></li>
                <li><span>分配情况：</span> <b class="remarks">{{planInfo.distributionStatus}}</b></li>
              </ul>
            </el-col>
            <el-col :span="12">
              <div class="showImage"><img :src="planInfo.picPath" width="600" height="400" alt=""></div>
            </el-col>
          </div>
        </el-tab-pane>
        <el-tab-pane label="产品列表" name="second">
          <plan-product :products="planProductList.products"></plan-product>
          <!--分页组件-->
          <div class="pageFrame">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="planProductList.query.page"
              :page-sizes="[5, 10, 20]"
              :page-size="planProductList.query.limit"
              layout="total, sizes, prev, pager, next, jumper"
              :total="planProductList.total">
            </el-pagination>
          </div>
          <!-- 产品列表结束 -->
        </el-tab-pane>
        <el-tab-pane label="效果图" name="third">
          <el-row>
            <el-col :span="2">照片级效果图</el-col>
            <el-col :span="22">
              <el-col :span="6" v-for="(pic,index) in effectDiagram.pics" :key="index"><img :src="pic.picPath" width="200" height="150"/>
              </el-col>
            </el-col>
          </el-row>
          <br/>
          <el-row>
            <el-col :span="2">单点720全景图</el-col>
            <el-col :span="22">
              <el-col :span="6" v-for="(pic,index) in effectDiagram.single720Pic" :key="index"><img :src="pic.picPath" width="200"
                                                                               height="150"/></el-col>
            </el-col>
          </el-row>
          <br/>
          <el-row>
            <el-col :span="2">多点720全景图</el-col>
            <el-col :span="22">
              <el-col :span="6" v-for="(pic,index) in effectDiagram.multi720Pic" :key="index"><img :src="pic.picPath" width="200"
                                                                              height="150"/></el-col>
            </el-col>
          </el-row>
          <br/>
          <el-row>
            <el-col :span="2">漫游视频</el-col>
            <el-col :span="22">
              <el-col :span="6" v-for="(pic,index) in effectDiagram.videoPic" :key="index"><img :src="pic.picPath" width="200" height="150"/>
              </el-col>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  import ElCol from 'element-ui/packages/col/src/col'
  import PlanProduct from '../../../base/plan/PlanProducts.vue'

  export default {
    components: {
      PlanProduct,
      ElCol},
    data () {
      return {
        showHide: '',
        activeName: 'first', // tabs 默认激活项
        planId: 0,
        planInfo: {},
        effectDiagram: {},
        planProductList: {
          products: [],   // 智能方案列表
          total: 0,
          query: {
            page: 1,
            limit: 10
          }
        }
      }
    },
    methods: {
      handleClick (tab, event) {
      },
      handleSelectionChange (val) {
        this.multipleSelection = val
      },
      // 分页模块
      handleSizeChange: function (size) {
        this.planProductList.query.limit = size
        this.listPlanProducts()
      },
      toggleStatus (row) {
        const params = {
          planId: this.planId,
          productId: row.id,
          status: row.displayStatus
        }
        this.API.OnekeyPlanToggleProductDisplay(params).then(resp => {
          this.listPlanProducts()
        })
      },
      handleCurrentChange: function (currentPage) {
        this.planProductList.query.page = currentPage
        this.listPlanProducts()
      },
      getBaseInfo () {
        this.API.CommonDesignPlanBaseInfo(this.planId)
          .then((resp) => {
            this.planInfo = resp.data
          })
      },
      getEffectDiagram () {
        this.API.GetOneKeyEffectDiagram(this.planId).then(resp => {
          this.effectDiagram = resp.data
        })
      },
      listPlanProducts () {
        const params = {
          page: this.planProductList.query.page,
          limit: this.planProductList.query.limit,
          planId: this.planId
        }
        this.API.ListCommonPlanProducts(params).then(resp => {
          this.planProductList.products = resp.data
          this.planProductList.total = resp.total
        })
      },
      gotoAddProduct () {
        this.$router.push(`/plan/common/${this.planId}/edit`)
      }
    },
    created () {
      this.planId = this.$route.params.planId
      this.getBaseInfo()
      this.getEffectDiagram()
      this.listPlanProducts()
    }
  }
</script>

<style lang="scss" scoped>
  @import "../../../../assets/css/mixin";

  .planDetailFrame {
    padding: 20px;
    box-sizing: padding-box;
    background: #fff;
    border-radius: $borderRadius;
    .right {
      overflow: hidden;
      float: right;
    }
  }

  .pageFrame {
    background: #fff;
    height: 110px;
    padding-top: 40px;
    box-sizing: border-box;
    text-align: center;
  }

  .planLibBaseMsg {
    ul {
      min-width: 780px;
      padding-top: 20px;
      li {
        font-size: 14px;
        display: inline-block;
        margin-bottom: 46px;
        white-space: nowrap;
        span {
          display: inline-block;
          width: 80px;
          vertical-align: top;
        }
        b {
          display: inline-block;
          font-weight: normal;
          min-width: 280px;
          &.remarks {
            white-space: normal;
            max-width: 650px;
            min-width: 380px;
            word-wrap: break-word;
          }
        }
      }
    }
  }

  .productBaseInfo {
    height: 40px;
    line-height: 40px;
    background-color: #f6f6f6;
    border-left: solid 4px $leaveButtonStatus;
    color: $color666;
    font-size: 16px;
    padding-left: 10px;
    font-weight: bold;
    margin-top: 90px;
    margin-bottom: 30px;
    width: 100%;
  }

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }

  .showImage {
    /*img {*/
    /*width: 100%;*/
    /*}*/
  }

  .showClass {
    margin-bottom: 10px;
  }
</style>
