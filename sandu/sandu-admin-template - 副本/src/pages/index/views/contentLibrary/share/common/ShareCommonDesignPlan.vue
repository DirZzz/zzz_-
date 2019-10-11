<template>
  <div>
    <!--<div>
      <el-row :gutter="24" class="searchCondition">
        <el-col :span="3">
          <el-select filterable  clearable  @change="page" v-model="query.shareCompanyId" placeholder="请选择分享公司">
            <el-option v-for="c in companys" :key="c.id" :value="c.id" :label="c.name"></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-input v-model="query.planName" placeholder="请输入方案名称"></el-input>
        </el-col>

        <el-col :span="1" class="marRight">
          <el-button round class="searchBtn " @click="page">搜索</el-button>
        </el-col>
        <el-col :span="1">
          <el-button round class="searchBtn" @click="handleClearQuery">重置</el-button>
        </el-col>
      </el-row>
    </div>-->
    <div>
      <el-row :gutter="24" class="searchCondition">
        <el-col :span="3" >
          <el-select filterable clearable  @change="page"  v-model="query.shareCompanyId" placeholder="请选择分享公司">
            <el-option v-for="c in companys" :key="c.id" :value="c.id" :label="c.name"></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
                        <el-select v-model="query.brandId" @change="page" filterable clearable placeholder="请选择品牌">
                            <el-option v-for="(item,index) in brandDrop"
                                        :key="index"
                                        :label="item.name"
                                        :value="item.id"
                            >{{item.name}}</el-option>
                        </el-select>
                    </el-col>
        <el-col :span="3">
          <el-input v-model="query.planName" placeholder="请输入设计师/方案名称/编号"></el-input>
        </el-col>

        <el-col :span="3">
          <el-select filterable clearable @change="page" v-model="query.salePriceChargeType" placeholder="是否免费">
            <el-option value="0" label="是">是</el-option>
            <el-option value="1" label="否">否</el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select @change="page" v-model="query.planGroupStyleId" filterable clearable popper-class="plan-style-css" placeholder="请选择方案风格">
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
                    :value="item.groupId">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="3"><el-input v-model="query.id" placeholder="请输入方案ID"></el-input></el-col>
      </el-row>

      <el-row class="searchBtnFrame clearfix">
        <div>
          <el-button round type="primary" size="small" @click="page">搜索</el-button>
          <el-button round class="searchBtn" size="small" @click="handleClearQuery">重置</el-button>
        </div>
      </el-row>
    </div>
    <div class="operationButton">
      <el-button type="text" class="allocation" @click="handleBatchCopy">批量使用</el-button>
      <el-button size="small" type="text" @click="refreshData" class="refresh">刷新</el-button>
    </div>
    <div class="planList">
      <el-table
        :data="list"
        style="width: 100%;"
        ref="multipleTable"
        v-loading="channelPlanCommon"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.8)"
        @selection-change="handleSelectionChange"
        tooltip-effect="dark"
        :default-sort="{prop: 'date', order: 'descending'}"
      >
        <el-table-column
          type="selection"
          label="全选"
          align="center"
          width="75" title="全选">
        </el-table-column>
        <!--<el-table-column-->
          <!--prop="planCode"-->
          <!--label="编号"-->
          <!--width="180">-->
        <!--</el-table-column>-->


        <el-table-column
          prop="picPath"
          label="方案缩略图"
          width="180">
          <template slot-scope="scope">
            <div v-if="scope.row.picPath">
              <img style="cursor:pointer;" @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.picPath)" :src="BASE_URL.sourceBaseUrl+scope.row.picPath" width="80">
            </div>
            <div v-else>
              <img src="../../../../assets/images/no_img.jpg" width="80" height="80" alt="">
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="planId" label="方案ID" width="150" sortable></el-table-column>
        <el-table-column
          prop="planName"
          label="名称">
          <template slot-scope="scope">
            <!--<img v-if="scope.row.isChanged == 0" :src="require('../../../../assets/images/fuben.png')" />-->
            <span @click="handleDetail(scope.$index, scope.row)" style='color: #FF6419;cursor:pointer;'>{{scope.row.planName}}</span>
          </template>
        </el-table-column>
        <el-table-column
                prop="spaceTypeName"
                label="空间类型">
        </el-table-column>
        <el-table-column
          prop="brandName"
          label="品牌">
          <template slot-scope="scope">
            <p class="remarkOverEllipsis" v-if="scope.row.brandName">{{scope.row.brandName}}</p>
            <p v-else class="remarkOverEllipsis">———</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="designStyleName"
          label="风格">
        </el-table-column>

        <el-table-column
          align="center"
          prop="companyName"
          label="分享公司">
          <template slot-scope="scope">
            <p v-if="scope.row.companyName">{{scope.row.companyName}}</p>
            <p v-else>———</p>
          </template>
        </el-table-column>

        <el-table-column
                align="center"
                prop="salePriceChargeType"
                label="是否免费">
          <template slot-scope="scope">
            <p v-if="scope.row.salePriceChargeType == 1">收费</p>
            <p v-else>免费</p>
          </template>
        </el-table-column>

        <el-table-column
                align="center"
                prop="salePrice"
                label="售卖价格（次/度币）">
          <template slot-scope="scope">
            <p v-if="scope.row.salePriceChargeType == 1">{{scope.row.salePrice}}</p>
            <p v-else>---</p>
          </template>
        </el-table-column>

        <el-table-column
          prop="completeDate"
          label="入库时间"
          width="100"
          align="center"
          sortable>
          <template slot-scope="scope">
            <p style="width: 80px; text-align: center">{{scope.row.completeDate}}</p>
          </template>
        </el-table-column>
        <el-table-column width="190" align="center"
                         prop="operation"
                         label="操作"
        >
          <template slot-scope="scope">
            <div class="table-td-right">
              <el-button class="operatonBtn" type="text" v-if="isPer('solution:openshare:view')"
                         @click="handleDetail(scope.$index, scope.row)">详情
              </el-button>

              <el-button class="operatonBtn" type="text" v-if="isPer('solution:openshare:use')"
                         @click="handleCopy(scope.row)">使用
              </el-button>

            </div>
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
          :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog
            title="提示"
            :visible.sync="showGoMoney"
            width="540px"
            top="30vh"
            class="chargeMode"
    >
      <p style="margin-left:20px;">当前余额不足，无法进行支付。
        <el-button round size="small" type="primary" @click="showRecharge=true,showGoMoney=false">去充值
        </el-button>
      </p>
    </el-dialog>


    <el-dialog
            title="充值"
            :visible.sync="showRecharge"
            width="540px"
            top="30vh"
            class="chargeMode"
    >
      <p style="margin: 10px 0 0 40px;">充值金额(元)：
        <el-input v-model="priceNum"  
                  maxlength="10"
                  @keyup.native="number"
                  style="width:130px;"></el-input>
        <span class="huansuanImg"></span>度币：{{DBChange}}
      </p>
      <div>
        <el-form>
          <el-form-item label="支付方式：" style="margin:10px 40px;">
            <el-radio-group v-model="Priceform.payType" style="margin:0;">
              <el-radio label="微信"></el-radio>
              <el-radio label="支付宝" style="margin-left:80px!important;"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" round size="small" @click="goPay">确 定</el-button>
          <el-button @click="showRecharge = false" round size="small">取 消</el-button>
        </span>
    </el-dialog>



    <el-dialog
            title="支付"
            :visible.sync="showPayDialog"
            :before-close="ClearTimer"
            width="540px"
            top="30vh"
            class="chargeMode"
    >
      <div class="paybox">
        <p class="paytext" v-if="Priceform.payType=='微信'">请使用微信扫码支付</p>
        <p class="paytext" v-else>请使用支付宝扫码支付</p>
        <p class="paynum">￥{{priceNum}}.00</p>
        <qrcode-vue :value="codeUrl" :size="size" level="H"></qrcode-vue>
        <p class="sandutext">三度云享家</p>
        <el-button round size="small" type="primary" plain>刷新二维码</el-button>
      </div>
    </el-dialog>

    <img-pre-view ref="sonParams"></img-pre-view>
  </div>
</template>
<script>
  import ContainsNotOpenProduct from '../../../base/plan/ContainsNotOpenProduct.vue';
  import QrcodeVue from 'qrcode.vue';
  import qs from 'qs'
  import ImgPreView from '@/components/Img-preview'
  import editPlan from '@/filters/edit-plan';

  export default {
    components: {ContainsNotOpenProduct,ImgPreView,QrcodeVue},
    name: 'ShareCommonDesignPlan',
    inject:['reload'],
    mixins: [editPlan],
    data () {
      return {
        DBChange:0,
        size:200,
        showGoMoney :false,
        showRecharge:false,
        showPayDialog:false,
        productPlanId: 0,
        channelPlanCommon: true,
        activeName: 'first',
        total: 0,
        query: {
          page: 0,
          limit: 10,
          planStyleId: null,
          planGroupStyleId: null,
          companyId: sessionStorage.getItem('companyID')
        },
        list: [],
        brandDrop:[],
        sels: [],
        companys: [],
        searchCompanys: [],
          codeUrl : '',
          orderNo : '',
          priceNum : '',
          Priceform:{
              payType : '微信'
          },
          rowCopy: null,
          planStyles: [{
              styleName: '',
              styles: [
                  {
                      styleId: 0,
                      styleName: ''
                  }
              ]}
          ],
      }
    },
  actived(){
      this.$nextTick(()=>{
         this.refreshData();
      })
  },
    methods: {
        ClearTimer(){
            clearInterval(this.timer);
            this.timer = null;
            this.showPayDialog = false;
        },
        number() {
            this.priceNum = this.priceNum.replace(/[^\ .\d]/g, '');
            this.priceNum = this.priceNum.replace('.', '');
            this.DBChange = this.priceNum * 10;
        },
        goPay() {
            if (this.priceNum == '' || this.priceNum <= 0) {
                this.$message.warning('请输入大于0的金额');
                return
            }
            let formData = new FormData();
            formData.append("money", this.priceNum);
            formData.append("payType", this.Priceform.payType == '微信' ? 0 : 1);
            this.API.rechargeDubi(formData).then((res) => {
                clearInterval(this.timer);
                this.timer = null;
                console.log(res);
                let obj = JSON.parse(res.obj.payParam)
                this.codeUrl = obj.qrCodeUrl;
                this.orderNo = res.obj.orderNo;
                this.timer = setInterval(this.RechargeCheckOut, 5000);
            })
            this.showPayDialog = true;
            this.showRecharge = false
        },
        RechargeCheckOut(){
              this.API.rechargeCheckOut({
                        orderNo: this.orderNo
                    }).then(res=>{
                       if(res.obj){
                           this.$message.success('充值成功!');
                           clearInterval(this.timer);
                            this.timer = null;
                            this.showPayDialog = false;
                            this.refreshData();
                       }else{
                           console.log(res);
                       }
                    })
            },
      //品牌列表
     getBrandDropList() {
                this.API.AllBrandList({
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    this.brandDrop = res.data
                })
            },

          //图片预览事件
      ImgView(ImgURL){
       this.$refs.sonParams.sonFun({ImgURL:ImgURL,ImgShow:true});
     },
      isPer (per) {
        let permiss = qs.parse(sessionStorage.getItem('loginUser'))
        return this.isMainPer(per, permiss)
      },
      refreshData () {
        this.$nextTick(function () {
          this.channelPlanCommon = true;
          this.page()
        })
      },
      page () {
        this.API.CommonShareDesignPlanList(this.query).then((resp) => {
          if (resp.code == 200 || resp.code >= 500 || resp.code == 400) {
            this.channelPlanCommon = false
          }
          if (resp) {
            this.list = resp.list
            this.total = resp.total
            this.channelPlanCommon = false
          } else {
            this.list = []
            this.total = 0
            this.channelPlanCommon = false
          }
        })
      },
      handleSelectionChange (sels) {
        this.sels = sels
      },
      handleDetail (index, row) {
        this.$store.commit('breadcrumb3', '方案详情')
         localStorage.setItem('fullhouseSharePlanTyle','0');
        this.$router.push(`/plan/share/${row.planId}`)
      },
      // 分页事件
      handleSizeChange: function (size) {
        this.query.limit = size
        this.page()
      },
      handleCurrentChange: function (currentPage) {
        this.query.page = currentPage
        this.page()
      },
      handleClearQuery () {
        this.query = {
          page: 0,
          limit: 10,
          companyId: sessionStorage.getItem('companyID')
        }
        this.page()
      },
      getCompany () {
        this.API.ListCompany().then(resp => {
          this.companys = resp.data
        })
      },
      handleCopy (row) {
        this.$confirm('确定要使用该方案?', '提示', {
          cancelButtonClass: 'cancelButtonClass',
          confirmButtonClass: 'confirmButtonClass',
          type: 'info',
          center: true
        }).then(() => {
         // if (row.salePriceChargeType == 1){
            if(false){
               //方案需要收费
               this.$confirm(`使用该方案，你将支付${row.salePrice}度币,确定使用该方案?`, '提示', {
                   cancelButtonClass: 'cancelButtonClass',
                   confirmButtonClass: 'confirmButtonClass',
                   type: 'info',
                   center: true
               }).then(() => {
                   let checkDubi = '';
                   this.API.getUserDubiInfo().then((res) => {
                       checkDubi = res.obj.balanceAmount >= row.salePrice;
                       if (checkDubi) {
                           //支付度币
                           this.rowCopy = row;
                           this.payDesignPlanSalePrice(row.planId,0);
                       } else {
                           this.showGoMoney = true;
                       }
                   })
               })
           } else{
               //不需要支付方案售卖价格,直接拷贝方案
               this.rowCopy = row;
               this.handlerCopyDesignPlan(row.planId);
           }
        })
      },
      payDesignPlanSalePrice(planId,type){
          let param = {
              planIds: planId,
              planType: 1,
              buyType: 0
          }
          this.API.batchDesignPlanSaleFee(param).then((res) => {
              if (res.status) {
                  //支付度币成功后靠背方案
                  if (type == 0){
                      //单方案使用
                      this.handlerCopyDesignPlan(planId);
                  }else{
                      this.handlerBatchCopyDesignPlan();
                  }
              } else {
                  this.$message.error(res.message);
              }
          })
      },
      handlerCopyDesignPlan(planId){
          let params = {
              sourceDesignPlanIds: [planId],
              targetCompanyId: sessionStorage.getItem('companyID')
          }
          this.API.ShareDesignPlanCopy(params).then(res => {
              // this.$message.success('使用成功');
              // this.page()

              // Modified by songjianming@sanduspace.cn on 2018/12/20
              // 商家后台使用共享方案提示信息优化(http://jira.3du-inc.net/browse/CMS-667)
              let _this = this;
              this.goEditPlanView(this.rowCopy, res, function() {
                  // 接口返回的复制好的新方案ID
                  let ids = res.data;
                  localStorage.setItem('breadcrumb3', '方案编辑');
                  localStorage.setItem('isNowPlanType', 'false');
                  localStorage.setItem('detailPlanType', '0');
                  sessionStorage.setItem('mainPlanId', planId);
                  _this.$router.push(`/plan/onekey/${ids}/edit`);
              }, _this.page);

          })
      },
        handlerBatchCopyDesignPlan(){
            let params = {
                sourceDesignPlanIds: this.sels.map(it => it.planId),
                targetCompanyId: sessionStorage.getItem('companyID')
            }
            this.API.ShareDesignPlanCopy(params).then(res => {
                // this.page()
                // this.$message.success('使用成功');

                // Modified by songjianming@sanduspace.cn on 2018/12/20
                // 商家后台使用共享方案提示信息优化(http://jira.3du-inc.net/browse/CMS-667)
                let _this = this;
                this.goEditPlanView(null, res, function() {
                    // 接口返回的复制好的新方案ID
                    _this.$router.push(`/plan`)
                }, _this.page);
            })
        },
      handleBatchCopy () {
        if(this.sels.length === 0) {
          this.$message.error('请选择一个方案')
          return;
        }

          let isPrices = false;
          let planPriceTotal = 0;
          let planIds = '';
          this.sels.map(item => {
              if (item.salePriceChargeType == 1) {
                  planIds += item.planId + ',';
                  isPrices = true;
                  planPriceTotal += item.salePrice
              }
          })

        this.$confirm('确定要使用该方案?', '提示', {
          cancelButtonClass: 'cancelButtonClass',
          confirmButtonClass: 'confirmButtonClass',
          type: 'info',
          center: true
        }).then(() => {
          //  if (isPrices){
            if(false){
                //需要支付方案售卖价格
                this.$confirm(`使用该方案，你将支付${planPriceTotal.toFixed(2)}度币,确定使用该方案?`, '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    type: 'info',
                    center: true
                }).then(() => {
                    let checkDubi = '';
                    this.API.getUserDubiInfo().then((res) => {
                        checkDubi = res.obj.balanceAmount >= planPriceTotal;
                        if (checkDubi) {
                            //支付度币
                            this.payDesignPlanSalePrice(planIds,1);
                        } else {
                            this.showGoMoney = true;
                        }
                    })
                })
            } else{
                /*let params = {
                    sourceDesignPlanIds: this.sels.map(it => it.planId),
                    targetCompanyId: sessionStorage.getItem('companyID')
                }
                this.API.ShareDesignPlanCopy(params).then(res => {
                    this.page()
                    this.$message.success('使用成功');
                })*/
                this.handlerBatchCopyDesignPlan();
            }
        })
      },
      initPlanStyle() {
          this.API.listDesignPlanStyle().then((resp) => {
              this.planStyles = resp.data || [];
          });
      },
    },
    created () {
      this.getCompany();
      this.page();
      this.getBrandDropList();
      this.initPlanStyle();
    }
  }
</script>
<style lang="scss" scoped>
  @import "../../../../assets/css/mixin";

  .operatonBtn {
    display: inline-block;
    margin-bottom: 8px;
    margin-left: 0 !important;
    font-size: 14px;
  }

  .center {
    text-align: center;
  }

  .searchCondition {
    padding-left: 30px;
    padding-top: 15px;
    .el-row{
      margin-bottom: 20px;
    }

    .el-col{
      width: 250px;
      .el-input,.el-select{
        width: 230px;
      }
    }
  }
  .searchBtnFrame{
    border-bottom: solid 15px #E4E4E4;
    margin-top: 25px;
    border-top: solid 1px #ddd;
    padding:20px 30px;
    div:nth-of-type(1) {

      .el-button {
        width: 100px;
      }
    }
    div:nth-of-type(2){

    }
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
      margin-left: 15px;
      color: $buttonOrange;
    }
  }

  .operationButton {
    padding: 20px 30px;
    @extend .planLibrary;
    .allocation {
      background: url('../../../../assets/images/icons/allot.png') 0 center no-repeat;
      &:hover {
        background: url('../../../../assets/images/icons/allot2.png') 0 center no-repeat;
      }
    }
    .delete {
      background: url('../../../../assets/images/icons/delete.png') 0 center no-repeat;
      &:hover {
        background: url('../../../../assets/images/icons/delete2.png') 0 center no-repeat;
      }
    }
    .refresh {
      background: url('../../../../assets/images/icons/reflesh.png') 0 center no-repeat;
      &:hover {
        background: url('../../../../assets/images/icons/reflesh2.png') 0 center no-repeat;
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
  .plan-style-css {
    .el-select-dropdown__item {
      color: #606266 !important;
    }
  }
</style>
<style>
  .el-table th {
    height: 60px;
    background: #f3f3f3;text-align: center
  }
</style>
