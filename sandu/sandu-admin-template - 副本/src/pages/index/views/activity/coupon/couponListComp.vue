<template>
		<div class="coupon-component">
            <div class="header-screen">
                <el-input class="header-screen-input" size="medium" placeholder="请输入优惠券名称"
                          v-model="couponName"
                          @change="conditionalSearchList('优惠券名称')">
                </el-input>

                <el-select size="medium" v-model="couponStatus" class="screen-city-item" placeholder="状态"
                           :clearable=true
                           @change="conditionalSearchList('状态')">
                    <el-option label="未开始" :value="1">未开始</el-option>
                    <el-option label="进行中" :value="2">进行中</el-option>
                    <el-option label="已失效" :value="3">已失效</el-option>
                </el-select>

                <el-select v-model="putawayMiniPro"
                           multiple placeholder="上架小程序"
                           @change="conditionalSearchList('小程序')">
                    <el-option
                            v-for="item in putawayMiniProInfo"
                            :key="item.appId"
                            :label="item.minProName"
                            :value="item.appId">
                    </el-option>
                </el-select>
            </div>
            <div class="box-padding">
                <el-button type="primary" round class="pri UnifiedsearchBtn" size="medium" @click="getCouponList">搜索</el-button>
                <el-button  round class="seek UnifiedsearchBtn" size="medium" @click="resetContent">重置</el-button>
                <el-button class="addCoupon" @click="toAddCoupon" type="primary" size="small" round plain>新建优惠券</el-button>
            </div>
      	<div class="coupon">
            <el-table
              :data="tableData"
              v-loading="loading"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(255, 255, 255, 0.8)"
              style="width: 100%">
                <el-table-column type="couponName" prop="couponName" label="优惠券名称" >
                	<template slot-scope="scope">
                   <span :class="{'font-bold':true,'newPerson':scope.row.couponType==1} ">{{scope.row.couponName}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="discountAmount" label="价值" >
                	<template slot-scope="scope">
                			<span v-if="scope.row.discountMode==1" class="small-icon">
                				{{scope.row.discountAmount}}元 
                			</span>
                			<span v-else class="small-icon">
                				{{scope.row.rebateFactor}}折 
                			</span>
                    	<span v-if="scope.row.productScopeType==1">全场通用</span>
                    	<span v-else>部分商品</span>
                  </template>
                </el-table-column>
                <el-table-column prop="maxReceiveQty" label="领取限制" >
                	<template slot-scope="scope">
                      <div v-if="scope.row.maxReceiveQty==0">
                      	<p>不限制</p>
                      	<p class="cl-gray" v-if="scope.row. availableStock==-1">库存：不限量</p>
                      	<p class="cl-gray" v-if="scope.row. availableStock>=0">库存：{{scope.row.availableStock}}张</p>
                      </div>
                      <div v-else>
                      	<p>每人限领{{scope.row.maxReceiveQty}}张</p>
                      	<p class="cl-gray" v-if="scope.row. availableStock==-1">库存：不限量</p>
                      	<p class="cl-gray" v-if="scope.row. availableStock>=0">库存：{{scope.row.availableStock}}张</p>
                      </div>
                  </template>
                </el-table-column>
                <el-table-column prop="couponTime" label="有效时间" min-width="120">
                	<template slot-scope="scope">
                      <div v-if="scope.row.effectiveDateMode==1">
                      	<p><span class="cl-gray">开始时间：</span>{{getDate(scope.row.startTime)}}</p>
                      	<p><span class="cl-gray">结束时间：</span>{{getDate(scope.row.endTime)}}</p>
                      </div>
                      <div v-else>
                      	<p>领取之后的{{scope.row.effectiveDays}}天内</p>
                      </div>
                  </template>
                </el-table-column>
                
                <el-table-column prop="userReceiveCount" label="领取人次"></el-table-column>
                <el-table-column prop="receiveQty" label="领取次数"></el-table-column>
                <el-table-column prop="usedQty" label="已使用"></el-table-column>
                
                <el-table-column prop="couponPercent" label="领取率/使用率"  >
                	<template slot-scope="scope">
                      <div class="">
                      	<p><span class="cl-gray">领取率：</span>{{scope.row.receiveRate}}</p>
                      	<p><span class="cl-gray">使用率：</span>{{scope.row.usedRate}}</p>
                      </div>
                  </template>
                	
                </el-table-column>

                <el-table-column prop="putawayMinProName" label="上架小程序">
                    <template slot-scope="scope">
                        <div v-if="scope.row.putawayMinProName">
                            <p
                                    v-for="(item,index) in scope.row.putawayMinProName.split(',')"
                                    :key="index"
                            >{{item}}</p>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="couponState" label="状态" >
                    <!--订单状态(未开始 进行中 已失效 ) ,-->
                    <template slot-scope="scope">
                        <span v-if="scope.row.couponState == 1">未开始</span>
                        <span v-if="scope.row.couponState == 2">进行中</span>
                        <span v-if="scope.row.couponState == 3">已失效</span>
                    </template>
                </el-table-column>

                <el-table-column label="操作">
                    <template slot-scope="scope">
                      <span v-if="scope.row.couponState == 2" class="mr-20">
                          <el-button size="mini" type="text" @click="toEditCoupon(scope.row.id)">编辑</el-button>
                          </span>
                          <span  v-if="scope.row.couponState == 2" class="mr-20">
                              <el-button size="mini" type="text" @click="stopCoupon(scope.row)">停止</el-button>
                              </span>
                              <span  v-if="scope.row.couponState == 1" class="mr-20">
                                  <el-button size="mini" type="text" @click="startCoupon(scope.row)">启用</el-button></span>
<!--                                  <span v-if="scope.row.couponState != 2" class="mr-20">-->
<!--                                      <el-button size="mini" type="text" @click="toEditCoupon(scope.row.id)">编辑</el-button>-->
<!--                                  </span>-->
                                   <span v-if="scope.row.couponState == 3" class="mr-20">
                                      <el-button size="mini" type="text" @click="startCoupon(scope.row)">重新启用</el-button>
                                  </span>
                                  <span v-if="scope.row.couponState == 1 ||scope.row.couponState == 3" class="mr-20">
                                      <el-button size="mini" type="text" @click="delCoupon(scope.row)">删除</el-button>
                                      </span>

                                                                            
                    </template>
                </el-table-column>
            </el-table>
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="goodsListArguments.pageNo"
                        :page-sizes="[10, 50, 100, 200,500]"
                        :page-size="goodsListArguments.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        
        <!--删除对话框-->
        <el-dialog title="删除优惠券" :visible.sync="delDialog" width="540px" top="30vh">
        	<div class="dia-single">确定删除此优惠券?</div>
            <div class="sureButton">
                <el-button type="primary" round @click="doDelCoupon" size="small" class="el-button--primary">是</el-button>
                <el-button type="primary" round @click="delDialog = false" size="small" class="el-button--primary btn-cancel">否</el-button>
            </div>
        </el-dialog>
        
        <!--停用对话框-->
        <el-dialog title="停用优惠券" :visible.sync="stopDialog" width="540px" top="30vh">
        	<div class="dia-single">是否将此优惠券设置成结束状态?</div>
            <div class="sureButton">
                <el-button type="primary" round @click="doStopCoupon" size="small" class="el-button--primary">是</el-button>
                <el-button type="primary" round @click="stopDialog = false" size="small" class="el-button--primary btn-cancel">否</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script type="text/javascript">
	
</script>
<script>
    import qs from 'qs'
    export default {
    		props:["couponState","tab"],
        data() {
            return {
                putawayMiniProInfo: [],
                putawayMiniPro: [],
                couponName:'',
                couponStatus: '',
                selectDate: '',
                loading: true,
                OrderStatus: '',
                tableData: [],
                total: 0,
                goodsListArguments:{
                	pageNo:1,
                	pageSize:10,
                	couponState:null,//1:未开始 2:进行中 3:已失效
                    companyId: sessionStorage.getItem('companyID'),// 企业ID
                    couponName:null,
                    putawayInfo:null
                },
                activeRow:{},
                delDialog:false,
                stopDialog:false
            }
        },
        created() {
            this.loading = false;
        },
        methods: {
            getPutawayMiniProgram() {
                let companyId = sessionStorage.getItem('companyID');
                this.API.selectPutawayMiniProgram({
                    companyId: companyId
                }).then(res => {
                    this.putawayMiniProInfo = res.datalist;
                })
            },
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
          	getDate(timeStamp){
          		if(!timeStamp)
          			return '';
		      		let dDate = new Date(timeStamp);
		      		let YYYY = dDate.getFullYear(),
		      				MM = (dDate.getMonth()+1) > 9?(dDate.getMonth()+1):"0"+(dDate.getMonth()+1),
		      				dd = dDate.getDate() > 9 ?dDate.getDate():"0"+dDate.getDate(),
		      				HH = dDate.getHours() > 9 ?dDate.getHours():"0"+dDate.getHours(),
		      				mm = dDate.getMinutes() > 9 ?dDate.getMinutes():"0"+dDate.getMinutes(),
		      				ss = dDate.getSeconds() > 9 ?dDate.getSeconds():"0"+dDate.getSeconds();
		      		let str = YYYY+"-"+MM+"-"+dd+" "+HH+":"+mm+":"+ss;
		      		return str;
		      	},
            delCoupon(row){
            	this.delDialog = true;
            	this.activeRow = row;
            },
            doDelCoupon(){
            	this.delDialog = false;
            	let couponId = this.activeRow.id;
            	if(!couponId&&couponId!=0)
            		return;
            	this.loading = true;
            	this.API.delCoupon({couponId:couponId}).then(res => {
            			this.loading = true;
		              if(res.code==200){
		              	this.$message.success('删除成功');
		              	this.goodsListArguments.pageNo=1;
		              	this.getCouponList();
		              }
		            })
            },
            stopCoupon(row){
            	this.stopDialog =true;
            	this.activeRow = row;
            },
            toEditCoupon(id){
            	localStorage.setItem('breadcrumb3', '编辑');
            	sessionStorage.setItem("pathTitle","全部优惠券列表");
            	this.$router.push(`/couponEdit/${id}`);
            },
            toAddCoupon () {
                localStorage.setItem('breadcrumb3', '新增');
                sessionStorage.setItem("pathTitle","全部优惠券列表")
                this.$router.push('/couponAdd');
            },
            doStopCoupon(){
            	var This = this;
            	this.stopDialog =false;
            	var id = this.activeRow.id;
            	if(!id)
            		return;
							 	this.API.updateCouponStatus({couponId:id,state:3}).then(res => {
											this.loading= false;
                      if(res.code==200){
                      	this.$message.success(res.message);
                      	setTimeout(function(){
                      		This.getCouponList();
                      	},500)
                      }
                })
            },
            startCoupon(row){
            	if(!row)
            		return;
            	this.activeRow = row;
            	this.doStartCoupon();
            },
            doStartCoupon(){
            	var This = this;
            	var id = this.activeRow.id;
            	if(!id)
            		return;
							 	this.API.updateCouponStatus({couponId:id,state:2}).then(res => {
											this.loading= false;
                      if(res.code==200){
                      	if(res.message=="新人券最多只能启用2张")
                      		this.$message.error(res.message);
                      	else
                      		this.$message.success(res.message);
                      	setTimeout(function(){
                      		This.getCouponList();
                      	},800)
                      }
                })
            },
            /*条件搜索数据*/
            conditionalSearchList(type) {
                if (type=='小程序') {
                    this.goodsListArguments.putawayInfo = this.putawayMiniPro.join(",");
                    this.goodsListArguments.pageNo = 1;
                    this.goodsListArguments.pageSize = 10;
                }
                if (type == '优惠券名称') {
                    this.goodsListArguments.couponName = this.couponName;
                    this.goodsListArguments.pageNo = 1;
                    this.goodsListArguments.pageSize = 10;
                }
                if(type == '状态') {
                    this.goodsListArguments.couponState = this.couponStatus;
                    this.goodsListArguments.pageNo = 1;
                    this.goodsListArguments.pageSize = 10;
                }
                this.getCouponList();
            },
            getCouponList(){
            	this.loading = true;
            	let goodsListArguments = this.goodsListArguments;
                goodsListArguments.couponName = this.couponName;
                goodsListArguments.couponState = this.couponStatus;
                goodsListArguments.putawayInfo = this.putawayMiniPro.join(",");
                this.API.getCouponList(goodsListArguments).then(res => {
                    this.loading= false;
                      if(res.code==200){
//                    	this.goodsListArguments.pageNo++;
                      	this.total = res.data.count;
                      	this.tableData = res.data.list;
                      }
                })
            },
            viewDetail(row) {
                this.$router.push('')
            },
            // 分页模块
            handleSizeChange: function (size) {
                this.goodsListArguments.pageSize = size;
                this.goodsListArguments.pageNo = 1;
                this.getCouponList()
            },
            handleCurrentChange: function (currentPage) {
                this.goodsListArguments.pageNo = currentPage;
                this.getCouponList();
            },
            resetContent() {
                this.goodsListArguments = {
                    pageNo: this.goodsListArguments.pageNo,
                    pageSize: this.goodsListArguments.pageSize,
                    couponState: null,
                    companyId: this.goodsListArguments.companyId,
                    couponName: null,
                    putawayInfo: null
                };
                this.couponStatus = '';
                this.couponName = '';
                this.putawayMiniPro= [];
                this.getCouponList();
            }
        },
        mounted() {
            this.getPutawayMiniProgram();
            this.goodsListArguments.couponState = this.couponState;
            if(this.couponState==this.tab)
            	this.getCouponList();
            var This = this;
            this.$watch('couponState', function(newVal, oldVal){
				       this.goodsListArguments.couponState = this.couponState;
				       if(this.couponState==this.tab)
            		this.getCouponList();
				    }); 
        }
    }
</script>

<style lang="scss" scoped>
	@import "../../../assets/css/mixin";
	.productName {
        text-align: left;
        line-height: 24px;
    }

    .receiveAddress {
        margin-top: 30px;
        margin-bottom: 20px;
    }

    .sureButton {
        text-align: right;
        border-top: solid 1px #ddd;
        padding-top: 20px;
        /*.el-button {
            width: 130px;
        }*/
    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .grey {
        background: #f5f5f5;
        &:hover {
            background: #FF6419;
            color: #fff !important;
        }
    }

    .orderlistTop {
        background: #fff;
        padding: 20px;
    }

    .buttonSplitLine {
        border-top: solid 1px #ddd;
        margin: 25px 0;
        padding-top: 20px;
        .el-button {
            color: #FF6419;
        }
    }

    .searchBg {
        background: #fff;
    }

    .orderListSearch {
        margin: 15px;
    }

    .buttonSplitLine {
        border-top: solid 1px #ddd;
        margin: 25px 0;
        padding-top: 20px;
        .el-button {
            color: #FF6419;
        }
    }
	
	
	.searchBg{
		background-color: #fff;
	}
    .el-tabs__nav {
        margin-left: 15px;
        line-height: 58px!important;
    	padding-left: 21px!important;
    }

    .el-radio-button__inner {
        background: #fafafa;
        border: none;
    }

    .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        background: #fff;
        border: none;
        color: #666;
        -webkit-box-shadow: none;
        box-shadow: none;
    }
    
    /*优惠券*/
    .btn-cancel{
			background-color: #FCDCCF;
	    color: $btnOrange;
	    border: 1px solid #FCDCCF;
		}
    .coupon-component{
    	width: 100%;
    	.btn-cancel{
				background-color: #FCDCCF;
		    color: $btnOrange;
		    border: 1px solid #FCDCCF;
			}
			.dia-single{
				padding-left: 40px;
			}
			.coupon-stock{
				
			}
    }
    .cl-gray{
    	color:#bbb;
    }
    .small-icon{
    	display: inline-block;
	    vertical-align: middle;
	    background: #ff2323;
	    width: 38px;
	    height: 18px;
	    line-height: 18px !important;
	    margin-right: 6px;
	    color: #fff;
	    border-radius: 6px 0px 6px 0px;
    }
    .font-bold{
    	font-weight: bold;
       
    }
    .newPerson{
         position: relative;
       &::before{
            content: '';
         width: 60px;
        height: 16px;
        display: inline-block;
        vertical-align: text-bottom;
        margin-right: 6px;
        background: url('../../../assets/images/icons/xinren_.png') no-repeat;
        background-size: 60px 16px;
        position: absolute;
        left: -66px;
        top: 1px;
        }
    }
    .mr-20{
    	margin-right: 20px;
    }
</style>
<style 	lang="scss" >

    .coupon {
        margin-top: 10px;
        padding: 20px;
        background-color: #fff;
    }

	/*特殊的样式*/
    .header-screen {
        padding: 20px 10px 0 10px;
        border-bottom: 1px solid #dddddd;

        .header-screen-input {
            width: 180px;
            padding: 0 10px 20px 10px;
        }

        .screen-city-item {
            width: 130px;
            padding: 0 10px 20px 10px;
        }
    }

    .box-padding{
        padding: 20px 30px;
        text-align: center;

        .seek {
            width: 100px;
            background: #f5f5f5;
        }

        .pri {
            width: 100px;
            border: solid 1px #dddddd;
        }

        .addCoupon {
            float: right;
        }
    }

	.coupon-component{
		.el-dialog__header{
			background-color:#fafafa;
			font-family:'Microsoft YaHei';
		}
		.dia-single{
			/*padding-top: 20px;*/
			line-height: 74px;
		}
		.el-dialog__body{
			padding: 0;
		}
		.sureButton{
			padding:13px 10px;
			>button{
				width: 60px;
				height: 30px;
			}
		}
	}
</style>
