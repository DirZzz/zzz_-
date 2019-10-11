<template>
	<div class="coupon-page ">
		<div><pos-nav></pos-nav></div>
		<div class="add-wrap" v-loading="isLoading">
			<header>
	      <h1>新增优惠券</h1>
	  	</header>
	  	<p class="head-tip"><span>*</span>为必填项</p>
	  	<div class="productBaseInfo">优惠券信息</div>
			<el-form ref="couponForm" :model="form" label-width="120px" :rules="rules">
				<el-row :gutter="12">
			    <el-col :span="6" class="hint">
		      	<el-form-item label="优惠券名称：" prop="couponName">
			    		<el-input v-model="form.couponName"  :maxlength="10" placeholder="请输入10个汉字以内" ></el-input>
	  				</el-form-item>
			    </el-col>
			 	</el-row>
			 	
			 	<!--<el-form-item label="限购信息：" prop="discountMode" style="height: 77px;">-->
		 		<el-form-item label="优惠信息：" prop="discountMode">
			    <el-radio-group v-model="form.discountMode" @change="discountChange">
			    	<div class="radio-line">
			    		<el-radio :label="1" class="limitPrice">指定金额<div class="limit-box" v-show="form.discountMode==1"><el-input v-model="form.discountAmount" type="text" min="0" placeholder=""  @keyup.native="discountAmountLimit" class="ipt-small"></el-input>元</div>	
			    		</el-radio>	
			    	</div>
			      <el-radio :label="2" class="limitDiscount">折扣<div class="limit-box" v-show="form.discountMode==2"> <el-input v-model="form.rebateFactor" placeholder="" @keyup.native="rebateLimit" class="ipt-small"></el-input> 折</div>
			      </el-radio>
			    </el-radio-group>
			  </el-form-item>
			 	
			 	<el-form-item label="使用门槛：" prop="minPriceType">
			    <el-radio-group v-model="form.minPriceType">
			    	<div class="radio-line">
			      	<el-radio :label="1">不限制</el-radio>
			      </div>
			      <el-radio :label="2" value="form.minPrice">
			      	满 <el-input v-model="form.orderAmount" placeholder="" type="text" min="0"  @keyup.native="orderAmountLimit" class="ipt-small"></el-input> 元使用
			      </el-radio>
			    </el-radio-group>
			  </el-form-item>
			  
			  <el-form-item label="有效期：" prop="effectiveDateMode">
			    <el-radio-group v-model="form.effectiveDateMode" class="effectiveDate">
			    	<div class="radio-line">
			      	<el-radio :label="1" >固定日期
			      	</el-radio>
			      	<div class="time-item" v-show="form.effectiveDateMode==1">
			      		开始时间<el-date-picker v-model="form.strStartTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择时间" default-time="12:00:00" class="time-picker" :picker-options="pickerOptionsStart" @change="changeEnd"></el-date-picker>
		      		</div>
		      		<div class="time-item" v-show="form.effectiveDateMode==1">
		      			结束时间<el-date-picker v-model="form.strEndTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择时间" default-time="12:00:00" class="time-picker" :picker-options="pickerOptionsEnd" @change="changeStart"></el-date-picker>
		      		</div>
		      	</div>
			      <el-radio :label="2" >领到券当日开始 <el-input v-model="form.effectiveDays" placeholder="" type="text" min="0" @keyup.native="effDayLimit" class="ipt-small"></el-input> 天内
			      </el-radio>
			    </el-radio-group>
			  </el-form-item>
			  
			  <el-form-item label="发放总量：" prop="qty">
	    		<el-input v-model="form.qty" type="text" min="0" class="ipt-small" @keyup.native="qtyLimit"></el-input> 张  <span style="color:#ccc">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0表示不限发放数量</span>
				</el-form-item>
				
				<!--<el-form-item label="每人限量领取：" prop="maxReceiveQty">-->
	    		<!--<el-input v-model="form.maxReceiveQty" type="text" min="0" class="ipt-small"  @keyup.native="receiveQtyLimit"></el-input> 张  <span style="color:#ccc">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0表示不限制</span>-->
				<!--</el-form-item>-->
			  
			  <el-form-item label="优惠券类型：" class="coupon-type" prop="couponType">
			    <el-radio-group v-model="form.couponType">
			    	<div class="radio-line">
			      	<el-radio :label="2">普通优惠券</el-radio>
			      </div>
			      <el-radio :label="1">新人优惠券
			      </el-radio>
			      <p class="form-tip">新人优惠券最多创建两张</p>
			    </el-radio-group>
			  </el-form-item>

			 	<el-row :gutter="12">
			    <el-col :span="6" class="hint">
		      	<el-form-item label="使用范围说明：" prop="scopeDesc" class="scope-desc">
			    		<el-input v-model="form.scopeDesc" placeholder="请输入10个汉字以内" :maxlength="10"></el-input>
	  				</el-form-item>
			    </el-col>
			 	</el-row>
			 	
			 	<el-form-item label="" class="coupon-scope">
			    <el-radio-group v-model="form.productScopeType">
			    	<div class="radio-line">
			      	<el-radio :label="1">全店通用</el-radio>
			      </div>
			      <el-radio :label="2">指定商品
			      </el-radio>
			      <p class="pro-add" v-show="form.productScopeType==2"><el-button type="text" @click="showProDia">+ 添加商品</el-button></p>
			    </el-radio-group>
			  </el-form-item>
			  
			  <!--<div class="product-table product-container" v-show="form.productScopeType==2">
			  	<el-table
			    :data="proCheckedList"
			    border
			    style="width: 100%">-->
			  <div class="product-table product-container" v-show="form.productScopeType==2">
			  	<el-table
			    :data="proShowChecked"
			    border
			    style="width: 100%">  
				    <el-table-column
							      label="序号"
							      type="index"
							      width="100">
				    </el-table-column>
				    <el-table-column
				      prop="spuCode"
				      label="商品编号"
				      width="150">
				    </el-table-column>
				    <el-table-column
				      prop="proInfo"
				      label="商品" width="280">
				      <template slot-scope="scope">
				      	<div class="product-info">
				      		<!--<img v-if="scope.row.pic" :src="BASE_URL.sourceBaseUrl+scope.row.pic" width="40" height="40" class="propic"/>
			      			<div v-else class="dia-nopic">暂无<br>图片</div>-->
				      		<!--{{scope.row.spuName}}-->
				      		<img v-if="scope.row.pic" :src="BASE_URL.sourceBaseUrl+scope.row.pic" width="40" height="40" class="propic"/>
			      				<div v-else class="dia-nopic">暂无<br>图片</div>
				      		<el-tooltip :content="scope.row.spuName" placement="bottom" effect="light">
                    <span>{{scope.row.spuName}}</span>
                  </el-tooltip>
				      	</div>
							</template>
				    </el-table-column>
				    <el-table-column
				      prop="operate"
				      label="操作" width="80">
				      <template slot-scope="scope">
				        <el-button @click="removeProduct(scope.row, proCheckedList)" type="text" size="small">移除</el-button>
				      </template>
				    </el-table-column>
				  </el-table>
			  	<div class="pro-pagination" v-show="proCheckedList.length>0">
				    <el-pagination
				      @current-change="handleCheckedChange"
				      :current-page.sync="checkedPage"
				      :page-size="6"
				      :current-page="checkedPage"
				      layout="total,prev, pager, next, jumper"
				      :total="proCheckedCount">
				    </el-pagination>
				  </div>
			  </div>

				<el-form-item label="上架小程序：" prop="putawayMiniPro">
					<el-checkbox-group v-model="form.putawayMiniPro">
						<el-checkbox v-for="item in putawayMiniProInfo"
									 :label="item.appId"
									 :key="item.appId">
							{{item.minProName}}
						</el-checkbox>
					</el-checkbox-group>
				</el-form-item>

			  <div class="form-operate">
			  	<el-button type="primary" @click="submitForm('couponForm')" class="confirmBtn marRight" round>保存</el-button>
					<!--<el-button @click="resetForm('couponForm')" round class="cancelBtnBg">重置</el-button>-->
	        <el-button @click="toBack" round class="cancelBtnBg">取消</el-button>
			  </div>
			</el-form>
			
			<!--选择商品弹窗-->
			<el-dialog title="选择商品" :visible.sync="dialogTableVisible">
				<div class="pro-dialog">
					<div class="prokey-wrap">
						<el-input v-model="proName" placeholder="请输入商品名称" class="prokey-ipt"></el-input>
						<el-input v-model="proCode" placeholder="请输入商品编号" class="prokey-ipt"></el-input>
						<!--<span class="prokey-sear"  style="cursor:pointer;">搜索</span>-->
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
				      type="selection"
				      width="55"
				      >  
				    </el-table-column>
				    <!--<el-table-column
				      prop="tableIndex"
				      label="序号"
				      width="100">
				    </el-table-column>-->
				    <el-table-column
				      prop="spuCode"
				      label="商品编号"
				      width="150">
				    </el-table-column>
				    <el-table-column
				      prop="proInfo"
				      label="商品"  width="280">
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
				    <el-table-column label="商品分类"  width="160">
                <template slot-scope="scope">
                    <span>{{scope.row.bigType}}</span>
                    <span>></span>
                    <span>{{scope.row.smallType}}</span>
                </template>
            </el-table-column>
				    <el-table-column
				      prop="operate"
				      label="操作" width="80">
				      <template slot-scope="scope">
				        <el-button @click="addCurrentItem(scope.row)" type="text" size="small">添加</el-button>
				      </template>
				    </el-table-column>
				  </el-table>
				  <div class="pageination"  v-show="proAll.length>0">
				    <el-pagination
				      @current-change="handleDiaChange"
				      :page-size="5"
				      layout="total,prev, pager, next, jumper"
				      :current-page.sync="diaPage"
				      :total="proTotal">
				    </el-pagination>
				  </div>



				  <div class="dia-operate">
				  	<el-button slot="label" round size="small" class="el-button--primary" @click="addProduct">添加</el-button>
				  	<el-button slot="label" round size="small" class="el-button--primary btn-cancel" @click="dialogTableVisible=false">取消</el-button>
				  </div>
				</div>
			</el-dialog>
		</div>

	</div>
	

</template>

<script>
	
	// import ElButton from '../../../../node_modules/element-ui/packages/button/src/button.vue'
	import qs from 'qs'
	
	export default {
	    data() {
	      return {
			  putawayMiniProInfo:[],
			  form: {
	          couponName: '',
	          qty:'0',//0代表不限制
	          discountMode:1,//优惠方式
	          effectiveDateMode:1,
	          effectiveDays:'',//有效天数
	          discountAmount:'',//优惠金额
	          rebateFactor:'',//折扣系数
	          orderAmount:'',//最低使用门槛
	          minPriceType:1,//最低使用门槛类型   1无限制 2 有金额  //这个值只是开发使用 不用传到后台
	          strStartTime:'',
	          strEndTime:'',
	          productScopeType:1,//1全店商品 2部分商品
	          couponType:2,
	          productIds:'',//商品ID
	          companyId:sessionStorage.getItem('companyID'),
	          maxReceiveQty:0,
			  putawayMiniPro: [],
			  putawayInfo: ""
	        },
	        proCode:'',
	        proName:'',
	        proAllLoding:true,
	        goodsListArguments: { // 请求商品列表参数
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
          },
	        rules: {
	            couponName: [
	                {required: true, message: '请输入优惠券名称', trigger: 'blur'},
	                {min: 1, max: 10, message: '长度在 10个字符内', trigger: 'blur'}
	            ],
	            couponPrice: [
	                {required: true, message: '优惠券面值', trigger: 'blur'},
	                {min: 1, max: 40, message: '长度在 40 个字符内', trigger: 'blur'}
	            ],
	            discountMode:[
	            	 {required: true, message: '请选择优惠信息', trigger: 'blur'},
	            	 {pattern: /^[0-9]/, message: '只能输入正整数' }
	            ],
	            minPriceType:[
	            	 {required: true, message: '请输入使用门槛', trigger: 'blur'},
	            	 {pattern: /^[0-9]/, message: '只能输入正整数' }
	            ],
	            effectiveDateMode:[
	            	 {required: true, message: '请设置优惠券使用日期', trigger: 'blur'}
	            ],
	            qty:[
	            	 {required: true, message: '请输入发放总量', trigger: 'blur'},
	            	 {pattern: /^[0-9]/, message: '只能输入正整数' }
	            ],
	            maxReceiveQty:[
	            	{required: true, message: '请输入每人限领数量', trigger: 'blur'},
	            	{pattern: /^[0-9]/, message: '只能输入正整数' }
	            ],
	            couponType:[
	            	 {required: true, message: '请选择优惠券类型', trigger: 'blur'}
	            ],
	            scopeDesc:[
	            	 {required: true, message: '请输入优惠券使用范围', trigger: 'blur'}
	            ],
				putawayMiniPro: [
					{required: true, message: '请选择上架的小程序', trigger: 'blur'},
				]
	        },
            currentIndex:1,
	        checkedPage:1,
	        diaPage:1,
        	dialogTableVisible:false,
        	proKey:"",
        	proTotal:0,
        	proAll: [],
        	proCheckedList:[],
        	proShowChecked:[],//显示的选中页的内容
        	proCheckedCount:0,
        	showChecked:[],
        	currDialogChecked:[],//输入框选中的内容,
        	isLoading:false,
        	dialogObj:null,//保存表格对象用来清除多选项
        	pickerOptionsStart:{
        		disabledDate(time) {
					      return time.getTime() < Date.now() - 8.64e7 
					   }
        	},
        	pickerOptionsEnd:{
        		disabledDate(time) {
				      return time.getTime() < Date.now() - 8.64e7
				   }
        	}
	      }
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
	    	discountAmountLimit(){
	    		if(this.form.discountAmount || this.form.discountAmount === 0){
	    			//为了去除首位的0 如050
	    			this.form.discountAmount = this.form.discountAmount.replace(/\D/g,'') * 1;
	    			if(this.form.discountAmount <0){
		    			this.form.discountAmount = "0"
		    			this.$message.error('请输入正确优惠金额')
		    		}
	    			//转换成字符串才能有replace方法
	    			this.form.discountAmount = this.form.discountAmount + "";
	    		}
	    	},
	    	orderAmountLimit(){
	    		if(this.form.orderAmount || this.form.orderAmount ===0){
	    			this.form.orderAmount=this.form.orderAmount.replace(/\D/g,'') * 1;
	    			if(this.form.orderAmount < 0 ){
		    			this.form.orderAmount= "0"
		    			this.$message.error('使用门槛必须大于等于0')
		    		}
	    			this.form.orderAmount = this.form.orderAmount + "";
	    		}
	    	},
            discountChange(val){
                this.currentIndex=val;
            },
	    	rebateLimit(){
	    		if(!this.form.rebateFactor)
	    			return;
	    		this.form.rebateFactor = this.form.rebateFactor.replace(/[^\.\d]/g,'');
	    		if(this.form.rebateFactor>=10){
	    			this.$message.error('折扣系数必须小于10')
	    		}
	    	},
	    	effDayLimit(){
	    		if(this.form.effectiveDays || this.form.effectiveDays ===0){
	    			this.form.effectiveDays=this.form.effectiveDays.replace(/\D/g,'') * 1;
	    			if(this.form.effectiveDays<0){
		    			this.form.effectiveDays="0"
		    			this.$message.error('请输入正确的有效天数')
		    		}
	    			this.form.effectiveDays = this.form.effectiveDays + "";
	    		}
	    	},
	    	qtyLimit(){
	    		if(this.form.qty || this.form.qty ===0){
	    			this.form.qty=this.form.qty.replace(/\D/g,'') * 1;
	    			this.form.qty = this.form.qty + "";
	    		}
	    	},
	    	receiveQtyLimit(){
	    		if(this.form.maxReceiveQty || this.form.maxReceiveQty ===0){
	    			this.form.maxReceiveQty=this.form.maxReceiveQty.replace(/\D/g,'') * 1;
	    			this.form.maxReceiveQty = this.form.maxReceiveQty + "";
	    		}
	    	},
	    	getRowId(row){
      		return row.id;
      	},
      	returnRowClass(row){
      		return "tableRow"
      	},
      	changeEnd(){
      		if(this.form.strEndTime && this.form.strStartTime){
      			var dateEnd = new Date(this.form.strEndTime);
      			var dateStart = new Date(this.form.strStartTime);
      			if(dateStart.getTime()-dateEnd.getTime()>=0){
      				this.$message.error("开始时间必须早于结束时间")
      			}
      		}
      		this.pickerOptionsEnd = Object.assign({},this.pickerOptionsEnd,{
	          disabledDate: (time) => {
	            var strStartTime = new Date(this.form.strStartTime);
            	return time.getTime() < strStartTime.getTime()- 8.64e7;
            }
	        })
      	},
      	changeStart(){
      		if(this.form.strEndTime && this.form.strStartTime){
      			var dateEnd = new Date(this.form.strEndTime);
      			var dateStart = new Date(this.form.strStartTime);
      			if(dateStart.getTime()-dateEnd.getTime()>=0){
      				this.$message.error("开始时间必须早于结束时间")
      			}
      		}
      		this.pickerOptionsStart = Object.assign({},this.pickerOptionsStart,{
	          disabledDate: (time) => {
	            var strEndTime = new Date(this.form.strEndTime);
            	return time.getTime() > strEndTime.getTime() || time.getTime() < Date.now() - 8.64e7;
            }
	        })
      	},
	      submitForm(formName) {
      	 this.$refs[formName].validate(valid=>{
      	 	if(valid){
      	 		//获取商品ID
		      	var productIds = '';
		      	var ids=[];
		      	this.proCheckedList.forEach(item=>{
		      		ids.push(item.id)
		      	})
		      	productIds=ids.join(',');
		      	var formData = this.form;
                this.currentIndex==1 ? this.form.rebateFactor=0 : '';
                this.currentIndex==2 ? this.form.discountAmount=0 : '';
		      	if(formData.minPriceType==1){
		      		formData.orderAmount = 0;
		      	}else if(formData.discountMode==1){
		      		if(!formData.orderAmount && formData.orderAmount!=0){
		      			this.$message.error('请输入最低使用门槛')
		      			return;
		      		}else if(formData.orderAmount * 1<=formData.discountAmount * 1){
		      			console.log(formData.orderAmount)
		      			this.$message.error('最低使用门槛必须大于优惠金额')
		      			return;
		      		}
		      	}
		      		
		      	if(formData.discountMode==1){
//		      		if(parseFloat(formData.discountAmount)<=0 || formData.discountAmount==''){
//		      			this.$message.error('请输入限购金额')
//		      			return;
//		      		}
		      		if(formData.discountAmount==''){
		      			this.$message.error('请输入优惠金额')
		      			return;
		      		}
		      	}else{
		      		if(parseFloat(formData.rebateFactor)<=0 || formData.rebateFactor==''){
		      			this.$message.error('折扣系数必须大于0')
		      			return;
		      		}
		      		if(parseFloat(formData.rebateFactor)>=10 ){
		      			this.$message.error('折扣系数必须小于10')
		      			return;
		      		}
		      		formData.rebateFactor = parseFloat(formData.rebateFactor);
		      	}

		      	if(formData.effectiveDateMode==1){
		      		if(!formData.strStartTime){
		      			this.$message.error('请输入开始日期');
		      			return;
		      		}else if(!formData.strEndTime){
		      			this.$message.error('请输入结束日期');
		      			return;
		      		}
		      		if(this.form.strEndTime && this.form.strStartTime){
		      			var dateEnd = new Date(this.form.strEndTime);
		      			var dateStart = new Date(this.form.strStartTime);
		      			if(dateStart.getTime()-dateEnd.getTime()>=0){
		      				this.$message.error("开始时间必须早于结束时间");
		      				return;
		      			}
		      		}
		      	}else{
		      		if(parseFloat(formData.effectiveDays)<=0 || formData.effectiveDays==''){
		      			this.$message.error('请输入正确的有效日期');
		      			return;
		      		}
		      	}
		      	
		      	if(formData.productScopeType==2){
		      		if(this.proCheckedList.length==0){
		      			this.$message.error('至少选择一个商品');
		      			return;
		      		}
		      		formData.productIds = productIds
		      	}
		      	
		      	formData.couponName = formData.couponName.trim();
		      	formData.scopeDesc = formData.scopeDesc.trim();

				let putawayMiniPro = formData.putawayMiniPro;
				formData.putawayInfo=putawayMiniPro.join(",");
				console.log(formData.putawayInfo);

		      	if(this.isLoading == true)
		      		return;
		      	this.isLoading = true;
		      	this.API.addCoupon(formData).then((res) => {
		      		this.isLoading = false;
		      		if(res && res.code==200){
		      			this.$message.success('保存成功');
		      			this.$router.push("/coupons");
		      		}else{
		      			this.$message.success(res.message || "保存失败 请稍后再试");
		      		}
		      	})
      	 	}else{
      	 		this.$message.error('请输入正确的优惠券数据')
      	 	}
      	 })
	      },
	      resetForm (formName) {
            this.form = {
	          couponName: '',
	          qty:'0',//0代表不限制
	          discountMode:1,//优惠方式
	          effectiveDateMode:1,
	          effectiveDays:'',//有效天数
	          discountAmount:'',//优惠金额
	          rebateFactor:'',//折扣系数
	          orderAmount:0,//最低使用门槛
	          minPriceType:1,//最低使用门槛类型   1无限制 2 有金额  //这个值只是开发使用 不用传到后台
	          strStartTime:'',
	          strEndTime:'',
	          productScopeType:1,//1全店商品 2部分商品
	          couponType:2,
	          productIds:[],//商品ID
	          companyId:sessionStorage.getItem('companyID')
	        }
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
        toBack(){
        	this.$router.push("/coupons");
        },
	      removeProduct(data, rows) {
	      	var targetId = data.id;
	      	for(var i = 0 ; i < rows.length;i++){
	      		if(rows[i].id == targetId){
	      				rows.splice(i, 1);
	      				break;
	      		}
	      	}
	      	this.proCheckedCount--;
	      	//重新分配显示内容
	      	var checkedPage =(this.checkedPage-1)*6;
	      	this.proShowChecked = this.proCheckedList.slice(checkedPage,checkedPage+6);
	      },
	      handleSelectionChange(val){
	      	this.currDialogChecked = val;
	      	this.dialogObj = this;
	      },
	      addCurrentItem(row){
	      	this.$refs.proAllEle.toggleRowSelection(row,true);
	      	this.addProduct();
	      },
	      addProduct(){
	      	var currDialogChecked = this.currDialogChecked;
	      	
	      	if(currDialogChecked.length==0){
	      		this.$message.error('请至少选择一个商品');
	      		return;
	      	}
	      	for(var i = 0 ; i < this.proCheckedList.length;i++){
	      		for(var j = 0 ; j < currDialogChecked.length;j++){
	      			if(this.proCheckedList[i].id==currDialogChecked[j].id){
//	      				this.$message.error('编号为'+currDialogChecked[j].spuCode+'的商品已添加 请勿重复添加');
//	      				return;
									currDialogChecked.splice(j,1);
									j--;
	      			}
	      		}
	      	}
					
	      	this.proCheckedList = this.proCheckedList.concat(currDialogChecked);
	      	this.proCheckedCount = this.proCheckedList.length;
	      	
	      	this.dialogTableVisible = false;
	      	this.checkedPage=1;
	      	this.proShowChecked = this.proCheckedList.slice(0,6);
	      },
	      showProDia(){
	      	this.form.productScopeType = 2;
	      	this.dialogTableVisible = true;
	      	this.doShowChecked();	
	      },
	      doShowChecked(){
	      	var This = this;
	      	var aProduct = this.proAll;
	      	var aChecked = this.proCheckedList;
	      	var aDiaCheckedList = [];
	      	aProduct.forEach(function(proItem,proIndex){
	      		aChecked.forEach(function(checkItem,checkIndex){
	      			if(proItem.id==checkItem.id){
	      				aDiaCheckedList.push(proItem);
	      			}
	      		})
	      	})
	      	//要加个定时器才能获取到This.$refs.proAllEle对象
	      	setTimeout(function(){
	      		This.$refs.proAllEle.clearSelection();
	      		if (aDiaCheckedList.length>0) {
		          aDiaCheckedList.forEach(row => {
		            This.$refs.proAllEle.toggleRowSelection(row,true);
		          });
		        }
	      	},0)
	      },
       	handleSizeChange(val) {
	      },
       	handleCheckedChange(val) {
       		var checkedPage = (val-1) * 6;
       		this.proShowChecked = this.proCheckedList.slice(checkedPage,checkedPage+6);
       		this.dialogObj = this;
		    },
		    handleDiaChange(val){
		    	this.goodsListArguments.page = val;
		      this.getGoodsList();
		    },
        getGoodsList(){
        	this.goodsListArguments.name= this.proName;
        	this.goodsListArguments.code= this.proCode;
        	this.proAllLoding = true;
        	this.API.getGoodsList(this.goodsListArguments).then((res) => {
        			this.proAllLoding = false;
        			if(res.code==200){
        				for(var i = 0 ;i < res.list.length;i++){
	        				res.list[i].tableIndex = (this.goodsListArguments.page-1) * 5 +i+1;
	        			}
	              this.proAll = res.list;
	              this.proTotal = res.total;
	              this.doShowChecked();
        			}else if(res.code==204){
        				this.proAll = [];
        				this.proTotal = 0;
        			} 
          });
        },
       
     },
	    mounted(){
			this.getPutawayMiniProgram();
			this.getGoodsList();
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
	
</style>
<style>
	/*搞特殊的样式*/
	.coupon-page .add-wrap .el-input__inner{
		height: 36px;
	}
	.coupon-page .el-pagination__editor.el-input .el-input__inner{
		height: 28px;
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
	.coupon-page .add-wrap .effectiveDate .radio-line{
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
	.coupon-page .add-wrap  .el-radio__input.is-checked + .el-radio__label{
		color: #606266;
	}
	.coupon-page .add-wrap .time-item{
		color: #606266;
	}
</style>