<template>
	<div class="coupon-page">
		<v-nav :nav="nav"></v-nav>
		<div class="add-wrap" v-loading="isLoading">
			<header>
	      <h1>编辑</h1>
	  	</header>
	  	<p class="head-tip">&emsp;&emsp;&emsp;<span>*</span>为必填项</p>
			<el-form ref="couponForm" :model="form" label-width="130px" :rules="rules">
                <el-row :gutter="12">
			    <el-col :span="6" class="hint">
		      	<el-form-item label="活动状态：">
			    		{{form.activityStatus==0?'草稿中':form.activityStatus==1?'未开始':
                            form.activityStatus==2?'进行中':form.activityStatus==3?'已结束':form.activityStatus==4?'已失效':''}}
	  				</el-form-item>
			    </el-col>
			 	</el-row>

				<el-row :gutter="12">
			    <el-col :span="6" class="hint">
		      	<el-form-item label="活动名称：" prop="activityName">
			    		<el-input v-model="form.activityName" :maxlength="10" placeholder="请输入10个汉字以内" ></el-input>
	  				</el-form-item>
			    </el-col>
			 	</el-row>

			 	<el-form-item  prop="activityTime">
					 <span slot="label"> <span style="color:red;">*</span> 活动时间：</span>
			      	<el-date-picker
                                style="width: 200px"
                                v-model="form.activityStartTime"
                                format="yyyy-MM-dd HH:mm:ss"
                                type="datetime"
                                range-separator="至"
                                placeholder="开始时间"
                                v-if="form.activityStatus == 0"
                                >
                        </el-date-picker>
                        <span v-if="form.activityStatus != 0">{{form.activityStartTime}}</span>
                        &emsp;至&emsp;
                        <el-date-picker
                                style="width: 200px"
                                v-model="form.activityEndTime"
                                type="datetime"
                                format="yyyy-MM-dd HH:mm:ss"
                                range-separator="至"
                                placeholder="结束时间">
                        </el-date-picker>
			  </el-form-item>
			 	
			 	<el-form-item label="参团人数：" prop="totalNumber">
	    		<el-input v-model="form.totalNumber" type="text" class="ipt-small" v-if="form.activityStatus == 0"></el-input> 
                 <span v-if="form.activityStatus != 0">{{form.totalNumber}}</span>
                人
				</el-form-item>

                  <el-form-item label="拼团有效期：" prop="effectiveDateMode">

					 <span slot="label"> <span style="color:red;">*</span> 拼团有效期
					  <el-tooltip class="item" effect="light" content="若设置1日，用户开团后，需要在1日内完成，超时则拼团失败" placement="bottom">
                        <div class="tip">？</div>
                    </el-tooltip>：
				 </span>
				  <el-input v-model="form.groupValidDay" type="text" class="ipt-small" v-if="form.activityStatus == 0"  @blur="validDay"></el-input>
                  <span v-if="form.activityStatus != 0">{{form.groupValidDay}}</span>
                  &emsp;天&emsp;
                    <el-select v-model="form.groupValidHour" clearable placeholder="请选择" size="mini" style="width:115px" v-if="form.activityStatus == 0">
                        <el-option
                        v-for="item in 23"
                        :key="item"
                        :label="item"
                        :value="item">
                        </el-option>
                    </el-select>
                    <span v-if="form.activityStatus != 0 && form.groupValidHour != 0">{{form.groupValidHour}}</span>
                    <span v-if="(form.activityStatus != 0 && form.groupValidHour != 0) || form.activityStatus == 0">&emsp;小时&emsp;</span>
                    <el-select v-model="form.groupValidMinute" clearable placeholder="请选择" size="mini" style="width:115px" v-if="form.activityStatus == 0">
                        <el-option
                        v-for="item in 59"
                        :key="item"
                        :label="item"
                        :value="item">
                        </el-option>
                    </el-select>
                    <span v-if="form.activityStatus != 0  && form.groupValidMinute != 0">{{form.groupValidMinute}}</span>
                    <span v-if="(form.activityStatus != 0 && form.groupValidMinute != 0) || form.activityStatus == 0">&emsp;分</span>
			  </el-form-item>

			 
                <el-form-item  class="logo">
					<span slot="label"> <span style="color:red;">*</span> 活动商品：</span>
						<div style="display:inline-block">
						<el-button type="text" @click="showProDia" v-if="form.activityStatus==0">更改</el-button>&emsp;&emsp;
						<!-- <el-button type="text" @click="showProDia" v-if="form.activityStatus==0">同步商品信息</el-button> -->
						</div>
					</el-form-item> 

				<div class="product-table product-container" v-show="this.spuId!=''">

                        <div class="product-info" style="background-color:#f3f3f3">
							<img :src="BASE_URL.sourceBaseUrl+form.productPath" width="40" height="40" class="propic" v-if="productImgURL ==''"/>
							<span v-if="productImgURL ==''">{{form.productText}}</span>
							<img :src="productImgURL" width="40" height="40" class="propic" v-if="productImgURL"/>
							<span v-if="productImgURL">{{productText}}</span>
				    </div>
					<p	style="margin:5px;font-weight:bold">拼团价：</p>
					<el-table 
					:data="skuInfo"
					border
					style="width:100%">		
						<el-table-column
						    v-for="(item ,index) in attributeNames"
							:key="index"
							:prop="item"
							:label="item"
							v-if="attributeNames">
							<template slot-scope="scope">
								<div v-if="scope.row.attributeValueNames">
									{{scope.row.attributeValueNames[index]}}
								</div>
							</template>
						</el-table-column>

						<el-table-column
						label="优惠价（元）"
						>
						<template slot-scope="scope">
							{{scope.row.price?scope.row.price:'———'}}
						</template>
						</el-table-column>

						<el-table-column
						label="拼团价（元）"
						>
						<template slot-scope="scope">
							<el-input type="text"  v-model="scope.row.activityPrice" v-if="form.activityStatus==0" maxlength="7"></el-input>
							<span v-if="form.activityStatus!=0">{{scope.row.activityPrice}}</span>
						</template>
						</el-table-column>

						<el-table-column
						prop="inventory"
						label="库存"
						>
						<template slot-scope="scope">
							<span v-if="scope.row.inventory">{{scope.row.inventory}}</span>
							<span v-else>{{scope.row.qty}}</span>
						</template>
						</el-table-column>

					</el-table>
				</div>

                <el-form-item label="商品限购：" prop="purchaseLimitAmount">
	    		<el-input v-model="form.purchaseLimitAmount" type="text" class="ipt-small" v-if="form.activityStatus == 0"></el-input> 
                <span v-if="form.activityStatus != 0">{{form.purchaseLimitAmount}}</span>
                件/人
				</el-form-item>

                
		      	<el-form-item label="团购设置：" prop="scopeDesc" class="scope-desc">
                        <template>
                        <!-- `checked` 为 true 或 false -->
                        <el-checkbox v-model="gatherFlag" class="switch">开启凑团</el-checkbox>
                        </template>
                        <p style="display:block" class="hint-switch">开启凑团后，对于未参团的买家，商品详情页会展示未成团的团列表，买家可以直接任选一个参团，提升参团率。</p>
                        <template>
                        <!-- `checked` 为 true 或 false -->
                        <el-checkbox v-model="virtualFlag" class="switch">开启模拟成团</el-checkbox>
                        </template>
                        <p class="hint-switch">开启凑团后，拼团有效期内人数未满的团，系统将会模拟“匿名买家”凑满人数，使该团成团。你只需要对已付款参团的真实买家发货。建议合理开启，以提高成团率。<span style="color:blue;cursor:pointer" @click="isShowPic()"><团详情示例></span></p>
                        <template>
                        <!-- `checked` 为 true 或 false -->
                        <el-checkbox v-model="couponFlag" class="switch">可叠加使用优惠券</el-checkbox>
                        </template>
	  				</el-form-item>
				<div style="display: flex;position: relative">
				<span style="color:#FF6419;position: absolute;top: 10px;left: 25px">*</span>
				<el-form-item label="上架小程序：">
					<el-checkbox-group v-model="putawayMiniProData">
						<el-checkbox v-for="item in putawayMiniProInfo"
									 :label="item.appId"
									 :key="item.appId">
							{{item.minProName}}
						</el-checkbox>
					</el-checkbox-group>
				</el-form-item>
				</div>

			  <div class="form-operate">
			  	<el-button @click="toBack" round class="cancelBtnBg">取消</el-button>&emsp;&nbsp;
                <el-button type="primary" v-if="form.activityStatus == 0" @click="submitForm('couponForm','release')" class="confirmBtn marRight" round>直接发布</el-button>
			  	<el-button type="primary" @click="submitForm('couponForm')" class="confirmBtn marRight" round>保存</el-button>
			  </div>
			</el-form>
			
			<div class="exhibitPic" v-if="this.exhibitPic">
				<img src="../../../assets/images/shili.png"/>
			</div>

			<!--选择商品弹窗-->
			<el-dialog title="选择商品" :visible.sync="dialogTableVisible" width="1000px">
				<div class="pro-dialog">
					<div class="prokey-wrap">
						<el-input v-model="proName" placeholder="请输入商品名称" class="prokey-ipt"></el-input>
						<!--<span class="prokey-sear" @click="getGoodsList" style="cursor:pointer;">搜索</span>
						<span class="prokey-sear" @click="resetGoodsList" style="cursor:pointer;">重置</span>-->
						<button data-v-e1eef154="" type="button" class="el-button searchBtn  el-button--default is-round" @click="getGoodsList"><span>搜索</span></button>
						<button data-v-e1eef154="" type="button" class="el-button searchBtn  el-button--default is-round" @click="resetGoodsList"><span>重置</span></button>
						
					</div>
				  <el-table
			    :data="proAll"
			    border
			    style="width: 100%"
			    height="512"
			    ref="proAllEle"
			    v-loading="proAllLoding"
			    @selection-change="handleSelectionChange"
			    :row-key="getRowId"
			    :cell-class-name="returnRowClass">
			    	<!--<el-table-column
				      type="selection"
				      width="55"
				      :reserve-selection="true">-->
				    
				    <el-table-column
				      prop="tableIndex"
				      label="序号"
					  type="index">
				    </el-table-column>
				    
				    <el-table-column
				      prop="proInfo"
				      label="商品" >
				      <template slot-scope="scope">
				      	<div class="product-info">
				      		<img v-if="scope.row.pic" :src="BASE_URL.sourceBaseUrl+scope.row.pic" width="40" height="40" class="propic"/>
			      			<div v-else class="dia-nopic">暂无<br>图片</div>
				      		<el-tooltip :content="scope.row.spuName" placement="bottom" effect="light">
                    <span>{{scope.row.spuName}}</span>
                  </el-tooltip>
				      	</div>
							</template>
				    </el-table-column>

					<el-table-column label="价格"  >
                <template slot-scope="scope">
                    <span>{{scope.row.minPrice}}</span>
                </template>
            </el-table-column>

			<el-table-column label="库存" >
                <template slot-scope="scope">
                    <span>{{scope.row.totalInventory}}</span>
                </template>
            </el-table-column>

				    <!-- <el-table-column label="创建时间"  width="160">
                <template slot-scope="scope">
                    <span>{{2018/1/1}}</span>
                </template>
            </el-table-column> -->

				    <el-table-column
				      prop="operate"
				      label="操作" >
				      <template slot-scope="scope">
				        <el-button @click="addCurrentItem(scope.row)" type="text" size="small" v-if="scope.row.minPrice!=0 && scope.row.minPrice !=undefined
											&& scope.row.totalInventory!=0	&& scope.row.totalInventory!=undefined">添加</el-button>
							<span v-else>不可添加</span>
				      </template>
				    </el-table-column>
				  </el-table>
				  <div class="pageination" v-show="proAll.length>0">
				    <el-pagination
				      @current-change="handleDiaChange"
				      :page-size="5"
				      layout="total,prev, pager, next, jumper"
				      :current-page.sync="diaPage"
				      :total="proTotal">
				    </el-pagination>
				  </div>
				</div>
			</el-dialog>
		</div>

	</div>	
	
</template>

<script>
//	import posSecond from '../../../rightContent/top/posNav2'
// 	import ElButton from '../../../../node_modules/element-ui/packages/button/src/button.vue'
    import qs from 'qs'
    import {formatDate} from '../../../filters/data';
//	Vue.component('pos-second', posSecond)
//	Vue.use(posSecond)
	
	export default {
	    data() {
	      return {
			  putawayMiniProInfo:[],
			  putawayMiniProData: [],
			  exhibitPic:false,
			  productImgURL:'',
                productText:'',
			  skuInfo:[],
			  attributeNames:'',
			  attributeValueNames:'',
			  activityId:'',
              spuId: "",
              groupValidHour:'',
              groupValidMinute:'',
              gatherFlag:'',
              virtualFlag:'',
              couponFlag:'',
	        form: {

	          activityId:'',
	          activityEndTime: "",
			  activityName: "",
			  activityStatus:"",
              activityStartTime:"",
              couponFlag: "",
              modifier: sessionStorage.getItem('userId'),
              gatherFlag:"",
              groupValidDay: "",
              groupValidHour: "",
              groupValidMinute: "",
              purchaseLimitAmount: "",
              spuId: "",
              totalNumber: "",
			  virtualFlag: "",
			  productPath:"",
			  putawayInfo:""
			},
			skuForm: {
				purchaseActivityId : "",
				spuId : "",
				skuIds : [],
				activityPrices :[],
				creator : sessionStorage.getItem('userId'),
				qtys:[],
			},
			nav: [
                    { nav: "拼团活动列表", url: "/assemActivity" },
                    { nav: "编辑", }
                ],
            currentIndex:1, //是否选择的是当前的
	        proCode:'',
	        proName:'',
	        proAllLoding:true,
	        goodsListArguments: { // 请求商品列表参数
            typeCode: undefined, // 商品类别code
            childTypeCode: undefined, //商品小类code
            putaway: 1, // 上架情况：1表示已上架，0表示未上架
            presell: undefined, // 是否预售：1表示预售商品，0表示非预售商品
            name: undefined, // 商品名称
            code: undefined, // 商品编号
            companyId: sessionStorage.getItem('companyID'), // 企业ID
            page: 1, // 当前页数
            start: undefined,
            limit: 5, // 每页显示商品数
          },
          couponPro:{
          	list:[],
          	page:1,
          	total:0,
          	loading:false
          },
	        rules: {
	            activityName: [
	                {required: true, message: '请输入活动名称', trigger: 'blur'},
	                {min: 1, max: 10, message: '长度在 10个字符内', trigger: 'blur'}
				],
				totalNumber: [
					{required: true, message: '请输入参团人数', trigger: 'blur'},
					{validator:this.checkJoinNum,trigger:'blur'}
				],
				purchaseLimitAmount: [
					{required: true, message: '请输入限购数量', trigger: 'blur'},
					{validator:this.checkLimitAmount,trigger:'blur'}
				],
				// putawayMiniProData: [
				// 	{required: true, message: '请输上架的小程序', trigger: 'blur'}
				// ]
	            // minPriceType:[
	            // 	 {required: true, message: '请输入使用门槛', trigger: 'blur'}
	            // ],
	            // effectiveDateMode:[
	            // 	 {required: true, message: '请设置优惠券使用日期', trigger: 'blur'}
	            // ],
	            // qty:[
	            // 	 {required: true, message: '请输入发放总量', trigger: 'blur'},
	            // 	 {pattern: /^[0-9]/, message: '只能输入正整数' }
	            // ],
	            // maxReceiveQty:[
	            // 	{required: true, message: '请输入每人限领数量', trigger: 'blur'},
	            // 	{pattern: /^[0-9]/, message: '只能输入正整数' }
	            // ],
	            // couponType:[
	            // 	 {required: true, message: '请选择优惠券类型', trigger: 'blur'}
	            // ],
	            // scopeDesc:[
	            // 	 {required: true, message: '请输入优惠券使用范围', trigger: 'blur'}
	            // ]
	        },
	        pickerOptionsStart:{
        		disabledDate(time) {
								return time.getTime() < Date.now() - 8.64e7;
					   }
        	},
        	pickerOptionsEnd:{
        		disabledDate(time) {
				      return time.getTime() < Date.now() - 8.64e7
				   }
        	},
	        checkedPage:1,
	        diaPage:1,
        	dialogTableVisible:false,
        	proKey:"",
        	proTotal:0,
        	proAll: [],
        	proCheckedShow:[],
        	proCheckedList:[],
        	proCheckedCount:0,
        	currDialogChecked:[],//输入框选中的内容,
        	isLoading:false,
        	dialogObj:null//保存表格对象用来清除多选项
	      }
	    },
	    methods: {
			checkLimitAmount(rule,value,callback){
				let reg = /^[1-9][0-9]*$/;
				if(this.form.purchaseLimitAmount != '' && !reg.test(this.form.purchaseLimitAmount)) {
					callback(new Error("请输入正确的限购数量"))
				}else{
					callback();
				}
			},
			checkJoinNum(rule,value,callback){
				let reg = /^[1-9][0-9]*$/;
				if(this.form.totalNumber != '' && !reg.test(this.form.totalNumber)) {
					callback(new Error("请输入正确的参团人数"))
				}else{
					if(this.form.totalNumber < 2) {
						callback(new Error("参团人数不能少于2人"))
					}else{
						callback();
					}
				}
			},
			validDay() {
				let reg = /^[0-9]*$/;
				if(this.form.groupValidDay != '' && !reg.test(this.form.groupValidDay)) {
					this.$message.error("请输入正确的有效天数")
				}
				
			},
	    	getRowId(row){
      		return row.id;
		  },
		  isShowPic(){
			  if(this.exhibitPic){
				  this.exhibitPic = false;
			  }else{
				  this.exhibitPic = true;
			  }
		  },
      	returnRowClass(row){
      		return "tableRow"
      	},
      	changeEnd(){
      		if(this.form.activityEndTime && this.form.activityStartTime){
      			var dateEnd = new Date(this.form.activityEndTime);
      			var dateStart = new Date(this.form.activityStartTime);
      			if(dateStart.getTime()-dateEnd.getTime()>=0){
      				this.$message.error("开始时间必须早于结束时间")
      			}
      		}
      		this.pickerOptionsEnd = Object.assign({},this.pickerOptionsEnd,{
	          disabledDate: (time) => {
	            var strStartTime = new Date(this.form.activityStartTime);
            	return time.getTime() < strStartTime.getTime()- 8.64e7;
            }
	        })
      	},
      	changeStart(){
      		this.pickerOptionsStart = Object.assign({},this.pickerOptionsStart,{
	          disabledDate: (time) => {
	            var strEndTime = new Date(this.form.strEndTime);
            	return time.getTime() > strEndTime.getTime()|| time.getTime() < Date.now() - 8.64e7;
            }
	        })
      	},

		  saveActivitySku(index){
			  this.skuInfo.map(res => {
				  this.skuForm.spuId =res.spuId
				  this.skuForm.skuIds.push(res.skuId)
				  this.skuForm.activityPrices.push(res.activityPrice)
				  this.skuForm.purchaseActivityId = index
				  if(res.inventory){
					  this.skuForm.qtys.push(res.inventory)
				  }else{
					  this.skuForm.qtys.push(res.qty)
				  }
			  })
			  this.API.updateActivitySku(
				this.skuForm  
			  ).then((res) => {
				  if(res.success == true) {
					  this.$message.success('保存成功');
		      		this.$router.push("/assemActivity");
				  }
			  })
		  },

	      submitForm(formName,param) {
      	 this.$refs[formName].validate(valid=>{
      	 	if(valid){
      	 		//获取商品ID
		      	var productIds = '';
		      	var ids=[];
		      	
		      	this.proCheckedList.forEach(item=>{
		      		ids.push(item.goodsId)
		      	})
		      	productIds=ids.join(',');
		      	
                  var formData = this.form;

				  let putaway = this.putawayMiniProData;
				  formData.putawayInfo=putaway.join(",");
				  if(formData.putawayInfo == null || formData.putawayInfo == '') {
					this.$message.error("请选择上架的小程序");
					return;
				  }
                  formData.gatherFlag=this.gatherFlag?'1':'0';
				  formData.virtualFlag=this.virtualFlag?'1':'0';
				  formData.couponFlag=this.couponFlag?'1':'0';

				  if(!formData.activityStartTime){
		      			this.$message.error('请输入开始日期');
		      			return;
		      		}else if(!formData.activityEndTime){
		      			this.$message.error('请输入结束日期');
		      			return;
					  }

				  formData.activityStartTime = formatDate(new Date(formData.activityStartTime),'yyyy-MM-dd hh:mm:ss');
                  formData.activityEndTime = formatDate(new Date(formData.activityEndTime),'yyyy-MM-dd hh:mm:ss');
				  formData.modifier = sessionStorage.getItem('userId');
				  formData.spuId = this.spuId;
				  
                  
                this.currentIndex==1 ? this.form.rebateFactor=0 : '';
                this.currentIndex==2 ? this.form.discountAmount=0 : '';
					  
				  //延迟一分钟左右...
				  // if(new Date(formData.activityStartTime)-new Date() < -60000){
					//  console.log( new Date(formData.activityStartTime)-new Date());
					// 	  this.$message.error("开始时间必须大于当前时间");
		      		// 		return;
					//   }

		      	if(formData.activityStartTime && formData.activityEndTime){
		      			var dateEnd = new Date(this.form.activityEndTime);
						  var dateStart = new Date(this.form.activityStartTime);
		      			if(dateStart.getTime()-dateEnd.getTime()>=0){
		      				this.$message.error("开始时间必须早于结束时间");
		      				return;
		      			}
					  }

			 var currTime = Number((formData.groupValidDay*24+formData.groupValidHour)*3600) + Number(formData.groupValidMinute*60);					  
		      	
		      		if(this.spuId==''){
		      			this.$message.error('至少选择一个商品');
		      			return;
				  }
				  
				  let totalAllInventory = 0;
				  
					let flag = false;
					let priceFlag = true;
				  this.skuInfo.map(res => {
					  let reg = /^\d+(\.\d{0,2})?$/;
					  if(!reg.test(res.activityPrice)) {
						  priceFlag = false
					  }
					  else if(res.activityPrice - res.price > 0) {
						  priceFlag = false
					  }
					  else if(res.activityPrice !=0 ){
						  flag = true;
						  	if(res.inventory){
								totalAllInventory+=res.inventory
							}else{
								totalAllInventory+=res.qty
							}
						  
					  }
				  })
				  if(!priceFlag){
					  this.$message.error('请为商品设置合理的拼团价');
		      			return;
				  }

					if(!flag) {
						this.$message.error('至少选择为一个商品设置拼团价');
		      			return;
					}
					
					if(totalAllInventory < this.form.totalNumber){
						this.$message.error('参团人数要小于或等于商品库存数');
		      			return;
					}
					
					let reg = /^[0-9]*$/;
					if(!reg.test(this.form.groupValidDay)) {
						this.$message.error("请输入正确的有效天数")
						return
					}else if(this.form.groupValidDay == 0 && this.form.groupValidHour == 0 && this.form.groupValidMinute == 0) {
						this.$message.error("请输入有效期时间")
						return
					}

					//校验活动时间和有效天数
					let mss = new Date(this.form.activityEndTime)-new Date(this.form.activityStartTime);
					var days = parseInt(mss / (1000 * 60 * 60 * 24));
					var hours = parseInt((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
					var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
				  	if(days < this.form.groupValidDay){
						this.$message.error('拼团有效期不能大于活动时间');
		      			return;
					  }
					if(days == this.form.groupValidDay) {
						if(hours < this.form.groupValidHour){
							this.$message.error('拼团有效期不能大于活动时间');
		      				return;	
						}else if(hours == this.form.groupValidHour){
							if(minutes <= this.form.groupValidMinute){
								this.$message.error('拼团有效期不能大于活动时间');
		      					return;
							}
						}
					}
				
				  
		      	if(this.isLoading ==true)
					  return;
					  
					  if(param == 'release') {
					  formData.activityStatus = 1;
                  }
		      	
                  this.isLoading = true;
		      	this.API.updateAssemActivity(formData).then((res) => {
		      		this.isLoading = false;
		      		if(res.success == true){
						this.saveActivitySku(this.activityId);
		      			this.$message.success('保存成功');
		      			this.$router.push("/assemActivity");
		      		}else{
		      			this.$message.success(res.message || "保存失败 请稍后再试");
		      		}
		      	})
      	 	}else{
      	 		this.$message.error('请输入正确有效的数据')
      	 	}
      	 })
	      },
	      toBack(){
        	this.$router.push("/assemActivity");
        },
	      removeProduct(data, rows) {
	      	var targetId = data.id;
	      	for(var i = 0 ; i < rows.length;i++){
	      		if(rows[i].id == targetId){
	      				rows.splice(i, 1);
	      				this.proCheckedCount--;
	      				break;
	      		}
	      	}
	      	var checkedPage = (this.checkedPage-1) * 6;
	      	this.proCheckedShow = this.proCheckedList.slice(checkedPage,checkedPage+6)
	      },
	      handleSelectionChange(val){
	      	this.currDialogChecked = val;
	      	this.dialogObj = this;
	      },
	      addCurrentItem(row){
			  this.skuInfo=[];
	      	this.$refs.proAllEle.toggleRowSelection(row,true);
	      	this.addProduct(row);
	      },
	      addProduct(row){
			  var currDialogChecked = row;
			  this.productImgURL = this.BASE_URL.sourceBaseUrl+currDialogChecked.pic
			  this.productText = currDialogChecked.spuName
			  this.spuId = currDialogChecked.id
			  this.getSkuDetail(this.spuId);
			//   this.getSkuDetail(45750);
	      	
	      	this.proCheckedList = this.proCheckedList.concat(currDialogChecked);
	      	this.proCheckedCount = this.proCheckedList.length;
	      	
	      	this.dialogTableVisible = false;
	      	this.checkedPage=1;
       		this.proCheckedShow= this.proCheckedList.slice(0,6)
		  },
		   getSkuDetail(index){
			  this.API.getSkuDetail({
				  spuId : index
			  }).then((res) => {
				  this.skuInfo = res.obj;
				  this.attributeNames = [];
				  if(res.obj[0].attributeNames != null){
					this.attributeNames = res.obj[0].attributeNames.split(',');
				  }
				  //this.attributeValueNames = 
				  this.attributeValueNames=[];
				  res.obj.map(item=>{
					  item.activityPrice = '';
					  if(item.attributeValueNames!=null){
						item.attributeValueNames =  item.attributeValueNames.split(',');  
					  }
					return item
				  })
			  })
		  },
	      showProDia(){
	      	this.form.productScopeType = 2;
	      	this.dialogTableVisible = true;
			  var This = this;
	      	this.doShowChecked();		
	      },
	      doShowChecked(){
	      	var This =this;
	      	var aProduct = this.proAll;
	      	var aChecked = this.proCheckedList;
	      	var aDiaCheckedList = [];
	      	aProduct.forEach(function(proItem,proIndex){
	      		aChecked.forEach(function(checkItem,checkIndex){
	      				aDiaCheckedList.push(proItem);
	      		})
			  })
	      	//加个定时器才能获取到This.$refs.proAllEle对象
	      	setTimeout(function(){
	      		if(This.$refs.proAllEle){
	      			This.$refs.proAllEle.clearSelection();
		      		if (aDiaCheckedList.length>0) {
			          aDiaCheckedList.forEach(row => {
			            This.$refs.proAllEle.toggleRowSelection(row,true);
			          });
			        }
	      		}
	      	},0)
	      },
       	handleCheckedChange(val) {
       		this.checkedPage = val;
       		var checkedPage = (val-1) * 6;
       		this.proCheckedShow= this.proCheckedList.slice(checkedPage,checkedPage+6);
		    },
		    handleDiaChange(val){
		    	this.goodsListArguments.page = val;
		      this.getGoodsList();
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
      		let str = YYYY+"-"+MM+"-"+dd +" "+HH+":"+mm+":"+ss;
      		return str;
      	},
		getActivityDetail(index){
			this.API.getActivityDetail({
					id: this.$route.params.id
				}).then(res => {
					this.form = res.obj;
					this.couponFlag = res.obj.couponFlag==0?false:true;
					this.virtualFlag = res.obj.virtualFlag==0?false:true;
					this.gatherFlag = res.obj.gatherFlag==0?false:true;
					this.spuId = res.obj.spuId;
					this.activityId = res.obj.activityId;

					this.putawayMiniProData = res.obj.putawayInfo.split(',');

					this.getGroupGoodsDetail();
				});
		},
        getGoodsList(){
        	this.goodsListArguments.name= this.proName;
        	this.goodsListArguments.code= this.proCode;
        	this.proAllLoding = true;
        	this.API.getGoodsList(this.goodsListArguments).then((res) => {
        			this.proAllLoding = false;
        			if(res && res.code==200){
        				for(var i = 0 ;i < res.list.length;i++){
	        				res.list[i].tableIndex = (this.goodsListArguments.page-1) * 5 +i+1;
	        			}
	              this.proAll = res.list;
				  this.proTotal = res.total;
	              this.doShowChecked();
	              
        			}else if(res && res.code==204){
        				this.proAll = [];
        				this.proTotal = 0;
        			} 
          });
        },
         resetGoodsList(){
      		this.proName = '';
      		this.proCode = '';
      		this.goodsListArguments =  { // 请求商品列表参数
            typeCode: undefined, // 商品类别code
            childTypeCode: undefined, //商品小类code
            putaway: undefined, // 上架情况：1表示已上架，0表示未上架
            presell: undefined, // 是否预售：1表示预售商品，0表示非预售商品
            name: undefined, // 商品名称
            code: undefined, // 商品编号
            companyId: sessionStorage.getItem('companyID'), // 企业ID
            page: 1, // 当前页数
            start: undefined,
            limit: 5, // 每页显示商品数、
            scopeDesc:''
         	};
         	this.diaPage =1;
    			this.getGoodsList();
      	},
        getCouponProduct(){
        	this.couponPro.loading = true;
        	let reqData = {
        		couponId:this.form.id
//      		pageNo:this.checkedPage,
//      		pageSize:1000
        	}
        	this.API.getCouponPro(reqData).then((res) => {
        			this.couponPro.loading = false
        			if(res && res.code==200 &&res.data &&res.data.list){
        				
	              this.proCheckedList = res.data.list;
	              this.couponPro.list = res.data.list;
	              
	              var checkedPage = (this.checkedPage-1) * 6;
	      				this.proCheckedShow= this.proCheckedList.slice(checkedPage,checkedPage+6);
	              this.proCheckedCount = res.data.count;
	              
        			}else if(res && !res.data){
        				this.proCheckedList = [];
        				this.proCheckedCount= 0;
        			} 
          });
        },
		getGroupGoodsDetail() {
			this.API.getGroupGoodsDetail(
					{
						purchaseActivityId : this.activityId,
                        spuId : this.spuId
                    }
                ).then(res => {
					this.skuInfo = res.obj;
                    if(res.obj[0].attributeNames != null){
					this.attributeNames = res.obj[0].attributeNames.split(',');
				  }
                    this.attributeValueNames=[];
				    res.obj.map(item=>{
						if(item.attributeValueNames!=null){
						item.attributeValueNames =  item.attributeValueNames.split(',');  
					  }
					return item
				  })
                })
            },
			getPutawayMiniProgram() {
				let companyId = sessionStorage.getItem('companyID');
				this.API.selectPutawayMiniProgram({
					companyId: companyId
				}).then(res => {
					this.putawayMiniProInfo = res.datalist;
				});
			},
     	},
     	created(){
     		
     	},
    	mounted(){
    		let id = this.$route.params.id;
      		this.getActivityDetail(id);
	    	this.getGoodsList();
			this.getPutawayMiniProgram();
	    }
	  }
</script>
<style lang="scss" scoped>
  @import "../../../assets/css/mixin";
  .add-wrap{
  	background-color: #fff;
  	box-sizing: padding-box;
    padding: 20px;
    border-radius: $borderRadius;
    header {
      h1 {
        font-weight: bold;
        font-size: 20px;
        padding-bottom: 15px;
        color: $greyOverButton;
        border-bottom: solid 1px $greyBorderEEE;
      }
    }
    .hint {
      position: relative;
      .hint-txt {
        position: absolute;
        top: 15px;
        left: 5px;
        color: #ff6419
      }
    }
    .head-tip{
      height: 30px;
      line-height: 30px;
      padding-top: 30px;
      font-size: 14px;
      color: #666;
      span {
        height: 20px;
        line-height: 20px;
        display: inline-block;
        padding-right: 5px;
        color: red;
        font-weight: bold;
        font-size: 20px;
        vertical-align: -6px;
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
      margin-bottom: 30px;
      margin-top: 20px;
      margin-left: 0;
    }
    .radio-line{
    	padding-bottom:10px;
    }
    .ipt-small{
    	width: 110px;
    	margin: 0 4px;
    }
    .el-radio-group{
    	padding-top: 13px;
    }
    .form-tip{
    	color:#ccc;
    	font-size:12px;
    	line-height:30px;
    	padding-top: 7px;
    }
    .coupon-type{
    	.form-tip{
	    padding-left:23px
	    }
    }
    .coupon-scope{
    	margin-bottom: 0;
    }
    .time-item{
    	&:nth-of-type(1){
    		padding-top:15px;
    	}
    	/*color:#FF6419;*/
    	padding-bottom:10px;
    	padding-left:23px
    }
    .time-picker{
    	width:200px;
    	margin-left:5px;
    }
    .limit-box{
    	display: inline-block;
    	margin: 0 5px;
    }
    .radio-high{
			padding-bottom: 17px!important;
		}
    .form-operate{
    	margin-top: 20px;
    	padding: 40px 0 0 120px;
    	border-top: 1px solid #ccc;
    }
  }
  .el-radio__input.is-checked+.el-radio__label {
	  color: #606266!important
	}
	.product-table{
		width: 611px;
		/*width: 402px;删除了商品列表的管理*/
		padding-left: 120px;
		>.pro-pagination{
			padding: 16px 0 10px;
			text-align: center;
		}
	}
	.pro-add{
		line-height: 40px;
    color: #FF6419;
    padding-left: 24px;
    padding-top:4px;
	}
	.product-info{
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		text-align: left;
		width: 100%;
		img{
			vertical-align: middle;
			width:70px;
			height: 70px;
		}
	}
	.pro-dialog{
		.pageination{
			text-align: center;
			padding: 30px 0 6px;
		}
		.prokey-wrap{
			padding: 0 0 20px 0;
			>.prokey-ipt{
				width: 150px;
				margin-right: 20px;
			}
			>.prokey-sear{
				color: $btnOrange;
			}
		}
		.dia-operate{
			text-align: right;
			border-top: 1px solid #ccc;
			padding:13px 24px;
			margin-top: 20px;
			>.btn-cancel{
				background-color: #FCDCCF;
		    color: $btnOrange;
		    border: 1px solid #FCDCCF;
			}
		}
		
	}
	.el-radio.is-checked.limitPrice{
  	margin-top: -13px;
  }
  .el-radio.is-checked.limitDiscount{
  	margin-top: -2px;
  }
  .el-radio__input.is-checked + .el-radio__label {
	    color: #606266;
	}
	.dia-nopic{
   	display: inline-block;
    width: 70px;
    height: 70px;
    vertical-align: middle;
    color: #fff;
    background-color: #ccc;
    line-height: 26px;
    padding: 13px;
    font-size: 22px;
	}
	.time-item{
		font-size: 14px;
	}
	.exhibitPic{
		position: fixed;
		right:28px;
		top: 100px;
	}
	.tip{
        display: inline-block;
        width: 16px;
        height: 16px;
        color: gray;
        font-size: 14px;
        line-height: 16px;
        text-align: center;
        background-color:white;
        border-radius: 100%;
		margin-left: 0;
		border:1px solid gray;
        cursor: pointer;
	}
	.product-table{
		width: 611px;
		padding-left: 120px;
		>.pro-pagination{
			padding: 16px 0 10px;
			text-align: center;
		}
	}
</style>
<style lang="scss">
	/*搞特殊的样式*/
	.coupon-page .add-wrap .el-input__inner{
		height: 36px;
	}
	.coupon-page .add-wrap .radio-line{
		height: 36px;
		padding-bottom: 0;
	}
	.coupon-page .add-wrap .el-radio.is-checked.limitDiscount{
		margin-top: 0;
		height: 36px;
		line-height: 36px;
	}
	.coupon-page .add-wrap .scope-desc.el-form-item{
		margin-bottom: 3px;
	}
	.coupon-page .add-wrap .el-form-item{
		margin-bottom: 20px;
	}
	.coupon-page .add-wrap .el-form-item__label{
		line-height: 36px;
	}
	.coupon-page .add-wrap .el-radio-group{
		padding-top: 0;
	}
	.coupon-page .hint-switch{
		width: 1200px;
		height: 13px;
		font-family: MicrosoftYaHei;
		font-size: 14px;
		font-weight: normal;
		font-stretch: normal;
		letter-spacing: 0px;
		color: #999999;
		height: auto;
	}
	.coupon-page .switch{
		width: 1409px;
		height: 36px;
		background-color: #ffffff;
		height: auto;
	}
	.coupon-page .add-wrap .el-radio{
		height: 36px;
		line-height: 36px;
	}
	.coupon-page .add-wrap .time-item:nth-of-type(1){
		margin-top: 0;
	}
	.coupon-page .add-wrap .el-radio.is-checked.limitPrice{
		margin-top: 0;
	}
	.coupon-page .add-wrap .form-tip{
		padding-top: 0;
	}
	.coupon-page .add-wrap .pro-add{
		padding-top: 0;
		line-height: 16px;
		padding-bottom:20px;
	}
	.coupon-page .add-wrap .pro-add>.el-button--text{
		padding: 0;
	}
	.coupon-page .add-wrap .el-dialog{
		width: 766px;
	}
	.coupon-page .tableRow{
		height: 90px;
	}
	.coupon-page .add-wrap .el-table td{
		padding: 8px 0;
	}
	.coupon-page .add-wrap .el-dialog__body{
		padding: 30px 20px 0;
	}
	.coupon-page .add-wrap .el-form-item__content{
		color: #606266;
	}
</style>