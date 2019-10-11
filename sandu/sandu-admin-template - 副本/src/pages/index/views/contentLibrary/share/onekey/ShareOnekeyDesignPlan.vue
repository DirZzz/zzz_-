<template>
    <div>
        <div>
            <el-row :gutter="24" class="searchCondition">
                <el-col :span="3">
                    <el-select v-model="classProgramme" @change="GoPlan" placeholder="请选择方案类型">
                        <el-option value="oneProgramme" label="单个方案/组合方案"></el-option>
                        <el-option value="allProgramme" label="全屋方案"></el-option>
                        <el-option value="single" label="单个方案"></el-option>
                        <el-option value="group" label="组合方案"></el-option>
                    </el-select>
                </el-col>
                <!--<el-col :span="3" >
                  <el-select filterable clearable  @change="page"  v-model="query.solutionType" placeholder="请选择方案类型">
                    &lt;!&ndash;<el-option v-for="c in companys" :key="c.id" :value="c.id" :label="c.name"></el-option>&ndash;&gt;
                    <el-option label="单个方案">单个方案</el-option>
                    <el-option label="组合方案">组合方案</el-option>
                  </el-select>
                </el-col>-->
                <el-col :span="3">
                    <el-select filterable clearable @change="page" v-model="query.shareCompanyId" placeholder="请选择分享公司">
                        <el-option v-for="c in companys" :key="c.id" :value="c.id" :label="c.name"></el-option>
                    </el-select>
                </el-col>

                <el-col :span="3">
                    <el-select filterable clearable @change="page" v-model="query.spaceTypeId" placeholder="请选择空间类型">
                        <el-option v-for="c in spaceCommonName" :key="c.id" :value="c.id" :label="c.name"></el-option>
                    </el-select>
                </el-col>


                <el-col :span="3">
                    <el-select v-model="query.brandId" @change="page" filterable clearable placeholder="请选择品牌">
                        <el-option v-for="(item,index) in brandDrop"
                                   :key="index"
                                   :label="item.name"
                                   :value="item.id"
                        >{{item.name}}
                        </el-option>
                    </el-select>
                </el-col>

                <el-col :span="3">
                    <el-select filterable clearable @change="page" v-model="query.salePriceChargeType" placeholder="是否免费">
                        <el-option value="0" label="是">是</el-option>
                        <el-option value="1" label="否">否</el-option>
                    </el-select>
                </el-col>
                <el-col :span="3">
                    <el-input v-model="query.planName" placeholder="请输入设计师/方案名称/编号"></el-input>
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
                <!--<div>
                    当前收益（元）：{{currentCash}} &emsp;&emsp;
                    <el-button style="border: solid 1px #FF6419; color: #FF6419" round size="small" v-if="isViewButton"
                               @click="viewIncome">查看收益
                    </el-button>
                </div>-->
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
                    v-loading="planLoading"
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
                            <img style="cursor:pointer;" @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.picPath)"
                                 :src="BASE_URL.sourceBaseUrl+scope.row.picPath" width="80">
                        </div>
                        <!--<div v-else>
                          <img src="../../../../assets/images/no_img.jpg" width="80" height="80" alt="暂无图片">
                        </div>-->
                    </template>
                </el-table-column>
                <el-table-column prop="planId" label="方案ID" width="150"></el-table-column>
                <el-table-column
                        prop="planName"
                        label="名称">
                        <template slot-scope="scope">
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
                        <p v-else>———</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="designStyleName"
                        width="140"
                        label="风格"
                        align="center"
                        >
                    <template slot-scope="scope">
                        <p class="remarkOverEllipsis" v-if="scope.row.designStyleName">{{scope.row.designStyleName}}</p>
                        <p v-else>———</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="solutionType"
                        label="方案类型">
                </el-table-column>
                <el-table-column
                        width="140"
                        align="center"
                        prop="companyName"
                        label="公司">
                    <template slot-scope="scope">
                        <p v-if="scope.row.companyName">{{scope.row.companyName}}</p>
                        <p v-else>———</p>
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="salePrice"
                        label="售卖价格（次/度币）">
                    <template slot-scope="scope">
                        <p v-if="scope.row.salePriceChargeType == 0">———</p>
                        <p v-else-if="scope.row.salePrice">{{scope.row.salePrice.toFixed(2)}}</p>
                        <p v-else>———</p>
                    </template>
                </el-table-column>
                <!-- <el-table-column
                        align="center"
                        prop="useCount"
                        label="被使用次数">
                </el-table-column> -->
                <el-table-column
                        prop="completeDate"
                        label="分享时间"
                        width="120"
                        align="center"
                        >
                    <template slot-scope="scope">
                        <p style="text-align: center">{{scope.row.completeDate}}</p>
                    </template>
                </el-table-column>
                <el-table-column width="120" align="center"
                                 prop="operation"
                                 label="操作"
                >
                    <template slot-scope="scope">
                        <div class="table-td-right" style="padding-left:0;">
                            <el-button type="text" size="mini" style="margin: 0; padding: 0"
                                       v-if="isPer('solution:openshare:view')"
                                       @click="handleDetail(scope.$index, scope.row)">详情
                            </el-button>
                            <el-button type="text" size="mini" style="margin: 0; padding: 0"
                                       v-if="isPer('solution:openshare:use') && scope.row.whetherDisplay == 1"
                                       @click="handleCopy(scope.row)">使用
                            </el-button>
                            <!-- <div>
                              <el-button type="text" size="mini" v-if="scope.row.isSetPlanPrice == 1"
                                         @click="priceSet(scope.row)">
                                价格设置
                              </el-button>
                            </div> -->
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
                title="使用价格设置"
                :visible.sync="priceDialogVisible"
                width="540px"
                top="30vh"
                class="chargeMode"
        >
            <div>
                选择收费方式：
                <el-radio-group v-model="chargeMode" @change="clearCash">
                    <el-radio :label="1">按次收费</el-radio>
                    <el-radio :label="0">免费开放</el-radio>
                </el-radio-group>
            </div>
            <p v-if="chargeMode == 1">
                单次收费价格：
                <el-input v-model="cash" size="small" style="width: 200px"></el-input>
                (元/次)
            </p>
            <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="confirmSetPrice" round size="small">确 定</el-button>
          <el-button @click="priceDialogVisible = false" round size="small">取 消</el-button>
        </span>
        </el-dialog>

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
                <!--<img  width="200px" height="200px" style="background:#ccc;">-->
                <qrcode-vue :value="codeUrl" :size="size" level="H"></qrcode-vue>
                <p class="sandutext">三度云享家</p>
                <el-button round size="small" type="primary" v-on:click="refresh" plain>刷新二维码</el-button>
            </div>
        </el-dialog>
        <img-pre-view ref="sonParams"></img-pre-view>
    </div>
</template>
<script>
    import qs from 'qs';
    import ImgPreView from '@/components/Img-preview';
    import QrcodeVue from 'qrcode.vue';
    import editPlan from '@/filters/edit-plan';

    export default {
        name: 'ShareOnekeyDesignPlan',
        inject:['reload'],
        mixins: [editPlan],
        data() {
            return {
                size:200,
                timer:null,
                priceNum: '',
                showPayDialog: false,
                Priceform: {
                    payType: '微信'
                },
                DBChange: 0,
                showRecharge: false,
                showGoMoney: false,
                brandDrop: [],
                planLoading: true,
                priceDialogVisible: false,
                cash: 100.00,
                isViewButton: false,
                chargeMode: 1,
                currentCash: 0,
                total: 0,
                query: {
                    page: 0,
                    limit: 10,
                    planStyleId: null,
                    planGroupStyleId: null,
                    companyId: sessionStorage.getItem('companyID'),
                    id:null,
                },
                list: [],
                sels: [],
                spaceCommonName: [],
                companys: [],
                classProgramme: sessionStorage.getItem('shareClassProgramme') || "oneProgramme",
                isHasImg: false,
                codeUrl: '',
                orderNo: '',
                planStyles: [{
                    styleName: '',
                    styles: [
                        {
                            styleId: 0,
                            styleName: ''
                        }
                    ]}
                ]
            }
        },
        components: {
            ImgPreView,
            QrcodeVue
        },
         beforeDestroy(){
            clearInterval(this.timer);
            this.timer = null;
        },
        methods: {
            ClearTimer(){
                 clearInterval(this.timer);
                 this.timer = null;
                 this.showPayDialog = false;
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
                    let obj = JSON.parse(res.obj.payParam);
                    this.codeUrl = obj.qrCodeUrl;
                    this.orderNo = res.obj.orderNo;
                    this.timer = setInterval(this.RechargeCheckOut, 3000);
                })
                this.showPayDialog = true;
                this.showRecharge = false
            },
            refresh() {
                this.goPay();
            },
            number() {
                this.priceNum = this.priceNum.replace(/[^\ .\d]/g, '');
                this.priceNum = this.priceNum.replace('.', '');
                this.priceNum = this.priceNum.replace(/\s+/g,"");
                this.DBChange = this.priceNum * 10;
            },
            //品牌列表
            getBrandDropList() {
                this.API.AllBrandList({
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    this.brandDrop = res.data
                })
            },
            // 空间类型
            getSpaceCommonName() {
                this.API.FitTypeHose().then((res) => {
                    this.spaceCommonName = res.data
                })
            },
            viewIncome() {
                this.$router.push('/share/income')
            },
            GoPlan() {
                // localStorage.setItem('fullhouseSharePlanTyle','3');
                if(this.classProgramme=='allProgramme'){ //全屋方案
                    this.$emit('GetisFullHouseSharePlan', false);
                }else {
                    this.classProgramme!='oneProgramme' ? this.query.queryType=this.classProgramme : delete this.query.queryType;
                    this.query.page=1;
                    this.page();
                }

            },
            //图片预览事件
            ImgView(ImgURL) {
                this.$refs.sonParams.sonFun({ImgURL: ImgURL, ImgShow: true});
            },
            isPer(per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            refreshData() {
                this.$nextTick(function () {
                    this.planLoading = true
                    this.page()
                })
            },
            page() {
                this.API.OnekeyShareDesignPlanList(this.query).then((resp) => {
                    this.planLoading = false
                    //console.log(resp.data.flag, 'flag')
                    //this.isViewButton = resp.data.flag
                    /*if(resp.data.totalIncome) {
                        this.currentCash = resp.data.totalIncome.toFixed(2)
                    }*/
                    this.list = resp.list
                    this.total = resp.total
                })
            },
            handleSelectionChange(sels) {
                this.sels = sels
            },
            handleDetail(index, row) {
                localStorage.setItem('breadcrumb3', '方案详情')
                this.$store.commit('breadcrumb3', '方案详情')
                localStorage.setItem('fullhouseSharePlanTyle', '0');
                this.$router.push(`/plan/share/${row.planId}`)
            },
            // 分页事件
            handleSizeChange: function (size) {
                this.query.limit = size
                this.page()
            },
            handleCurrentChange: function (currentPage) {
                this.planLoading = true
                this.query.page = currentPage
                this.page()
            },
            getCompany() {
                this.API.ListCompany().then(resp => {
                    this.companys = resp.data
                })
            },
            handleClearQuery() {
                this.query = {
                    page: 1,
                    limit: 10,
                    companyId: sessionStorage.getItem('companyID')
                };
                this.page()
            },
            priceSet(row) {
                console.log(row, 'siw')
                this.cash = row.planPrice
                this.planId = row.planId
                this.chargeMode = row.chargeType == '0' ? 0 : 1
                this.priceDialogVisible = true
            },
            clearCash() {
                if (this.chargeMode == 0) {
                    this.cash = 0
                }
            },
            confirmSetPrice() {
                let formData = new FormData();
                formData.append("planId", this.planId);
                formData.append("planPrice", this.cash);
                formData.append("chargeType", this.chargeMode);
                if (this.cash <= 0 && this.chargeMode == 1) {
                    this.$message.error('请输入价格')
                    return
                }

                this.API.oneKeySharePlanPriceSet(formData).then((res) => {
                    this.page()
                    this.priceDialogVisible = false
                    this.$message.success('价格设置成功')
                })
            },
            async handleCopy(row) {
                this.$confirm('确定要使用该方案?', '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    type: 'info',
                    center: true
                }).then(() => {
                    //if (row.salePriceChargeType == 0) {//不用支付的条件
                    if(row.salePriceChargeType == 0){
                        // let params = {
                        //     sourceDesignPlanIds: [row.planId],
                        //     targetCompanyId: sessionStorage.getItem('companyID')
                        // }
                        // this.API.ShareDesignPlanCopy(params).then(res => {
                        //     this.$message.success('使用成功')
                        //     this.page()
                        // })
                       this.designPlanCopy(row);
                    } else {//需要支付
                        this.$confirm(`使用该方案，你将支付${row.salePrice}度币,确定使用该方案?`, '提示', {
                            cancelButtonClass: 'cancelButtonClass',
                            confirmButtonClass: 'confirmButtonClass',
                            type: 'info',
                            center: true
                        }).then(() => {
                            let checkDibi = '';
                            this.API.getUserDubiInfo().then((res) => {
                                checkDibi = res.obj.balanceAmount >= row.salePrice;
                                if (checkDibi) {//支付度币
                                    this.handlerPayDesignPlanFee(row);
                                } else {//需要充值
                                    this.showGoMoney = true;
                                }
                            })
                        })
                    }
                })
            },
            designPlanCopy(row){
                let params = {
                    sourceDesignPlanIds: [row.planId],
                    targetCompanyId: sessionStorage.getItem('companyID')
                }
                this.API.ShareDesignPlanCopy(params).then(res => {
                    // this.$message.success('使用成功')
                    // this.page()

                    // Modified by songjianming@sanduspace.cn on 2018/12/20
                    // 商家后台使用共享方案提示信息优化(http://jira.3du-inc.net/browse/CMS-667)
                    let _this = this;
                    this.goEditPlanView(row, res, function() {
                        // 接口返回的复制好的新方案ID
                        let ids = res.data;
                        localStorage.setItem('breadcrumb3', '方案编辑');
                        localStorage.setItem('isNowPlanType', 'false');
                        localStorage.setItem('detailPlanType', '0');
                        sessionStorage.setItem('mainPlanId',ids);
                        _this.$router.push(`/plan/onekey/${ids}/edit`);
                    }, _this.page);
                });
            },
            handlerPayDesignPlanFee(row){
                let param = {
                    planIds: row.planId,
                    planType: 1,
                    buyType: 0
                }
                this.API.batchDesignPlanSaleFee(param).then((res) => {
                    if (res.status) {
                        //this.$message.success(res.message);
                        this.designPlanCopy(row);
                    } else {
                        this.$message.error(res.message);
                    }
                })
            },
            handlerBatchPayDesignPlanFee(planIds){
                let param = {
                    planIds: planIds,
                    planType: 1,
                    buyType: 0
                }
                this.API.batchDesignPlanSaleFee(param).then((res) => {
                    if (res.status) {
                        //this.$message.success(res.message);
                        //this.designPlanCopy(row);
                        this.designPlanBatchCopy(this.sels);
                    } else {
                        this.$message.error(res.message);
                    }
                })
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
            handleBatchCopy() {
                if (this.sels.length === 0) {
                    this.$message.error("请选择一个方案")
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
                    // if (isPrices) {//需要支付
                     if(isPrices){
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
                                    this.handlerBatchPayDesignPlanFee(planIds);
                                } else {
                                    this.showGoMoney = true;
                                }
                            })
                        })
                    } else {//不需要支付
                        // let params = {
                        //     sourceDesignPlanIds: this.sels.map(it => it.planId),
                        //     targetCompanyId: sessionStorage.getItem('companyID')
                        // }
                        // this.API.ShareDesignPlanCopy(params).then(res => {
                        //     this.page()
                        //     this.$message.success('使用成功')
                        // })

                        // 凉凉~~上面的bug谁写的~
                        this.designPlanBatchCopy(this.sels);
                    }
                })
            },
            designPlanBatchCopy(sels){
                let params = {
                    sourceDesignPlanIds: sels.map(it => it.planId),
                    targetCompanyId: sessionStorage.getItem('companyID')
                }
                this.API.ShareDesignPlanCopy(params).then(res => {
                    // this.page()
                    // this.$message.success('使用成功')

                    // Modified by songjianming@sanduspace.cn on 2018/12/20
                    // 商家后台使用共享方案提示信息优化(http://jira.3du-inc.net/browse/CMS-667)
                    let _this = this;
                    this.goEditPlanView(null, res, function() {
                        _this.$router.push(`/plan`);
                    }, _this.page);
                })
            },
            initPlanStyle() {
                this.API.listDesignPlanStyle().then((resp) => {
                    this.planStyles = resp.data || [];
                });
            }
        },
        created() {
            this.page();
            this.getSpaceCommonName();
            this.getCompany();
            this.getBrandDropList();
            this.initPlanStyle();
        }
    }
</script>
<style lang="scss" scoped>
    @import "../../../../assets/css/mixin";

    .searchCondition {
        padding-left: 30px;
        /*padding-top: 15px;*/
        .el-row {
            margin-bottom: 20px;
        }

        .el-col {
            width: 250px;
            .el-input, .el-select {
                width: 230px;
                margin: 10px 10px 0 0;
            }
        }
    }

    .searchBtnFrame {
        display: table;
        border-bottom: solid 15px #f0f0f0;
        text-align: center;
        margin-top: 25px;
        border-top: solid 1px #ddd;
        padding: 20px 30px;
        box-sizing: border-box;
        width: 100%;
        position: relative;
        div:nth-of-type(1) {
            /*float: left;*/

        }
        div:nth-of-type(2) {
            position: absolute;
            top: 20px;
            right: 20px;
        }
    }

    .center {
        text-align: center;
    }

    .chargeMode {
        div {
            margin-bottom: 20px;
            .el-radio {
                margin-right: 15px;
            }
        }
    }

    .operatonBtn {
        display: inline-block;
        margin-bottom: 8px;
        font-size: 14px;
        margin-left: 0 !important;
    }

    .planLibrary {
        box-sizing: padding-box;
        background: #fff;
        border-radius: $borderRadius;
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
        .refresh {
            background: url('../../../../assets/images/icons/reflesh.png') 0 center no-repeat;
            &:hover {
                background: url('../../../../assets/images/icons/reflesh2.png') 0 center no-repeat;
            }
        }
        button {
            color: #666;
            font-size: 14px;
            margin-right: 10px;
            padding-left: 25px;
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

    .huansuanImg {
        width: 12px;
        height: 14px;
        background: url('../../../../assets/images/icons/icon-huansuan.png') no-repeat;
        background-size: 12px 14px;
        display: inline-block;
        margin: 0 10px;
        vertical-align: middle;
    }

    .paybox {
        text-align: center;
        .paytext {
            color: #333333;
            font-size: 14px;
        }
        .paynum {
            color: #ff6419;
            font-size: 20px;
            margin: 20px 0;
        }
        .sandutext {
            color: #999999;
            font-size: 12px;
            margin-bottom: 20px;
        }
    }
    .plan-style-css {
        .el-select-dropdown__item {
            color: #606266 !important;
        }
    }
</style>
