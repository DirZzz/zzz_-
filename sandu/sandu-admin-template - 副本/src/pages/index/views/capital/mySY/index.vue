<!--页面注释-->
<template>
    <div class="mySY">
        <header>
            <h1 class="title">收益余额</h1>
            <p class="money"><span class="moneylogo"></span>{{incomeParams.currentDubi}}
            <el-tooltip class="item" 
            	placement="top">
            	<div slot="content">提现说明:<br/>①每次提现金额必须大于100块钱<br/>②每个企业每月提现不能超过2次<br/>③用户提现将在5个工作日内完成<br/>④收益为该企业在三度平台通过方案等途径获取的收益，可申请提现，可转出到我的度币进行使用</div>
              <span class="moneyhow"></span>
            </el-tooltip>
            </p>
            <p class="rightbox">
                <el-button v-if="isPer('check:withdraw:button')" type="text" size="medium" @click="drawings()">提现</el-button>
                <span v-if="isPer('check:withdraw:button') && isPer('check:transfer:button')" class="line">|</span>
                <el-button size="medium" v-if="isPer('check:transfer:button')" type="text" @click="showDialog('转出')">转出</el-button>
            </p>
            <div class="bottombox">
                <span>总收益：{{incomeParams.totalIncomeDubi}}度币</span><span>提现：{{incomeParams.withdrawDubi}}度币</span><span>转出：{{incomeParams.transferDubi}}度币</span><span>冻结：{{incomeParams.frozenDubi}}度币</span>
            </div>
        </header>
        <main>
            <el-tabs v-model="activeName" class="DBNav">
                <el-tab-pane v-if="showTab('check:myIncomeList:view','diposit')"  label="收益记录" name="income">
                    <in-come></in-come>
                </el-tab-pane>
                <el-tab-pane v-if="showTab('check:withdrawList:view','rollout')"  label="提现记录" name="diposit">
                    <di-posit ref="posit"></di-posit>
                </el-tab-pane>
                <el-tab-pane v-if="showTab('check:transferList:view','income')"  label="转出记录" name="rollout">
                    <roll-out ref="roll"></roll-out>
                </el-tab-pane>
            </el-tabs>
        </main>
        <el-dialog
                title="提现"
                :visible.sync="showIncome"
                width="600px"
                top="30vh"
                class="chargeMode">
        
        	<el-form :model = "incomeForm" :rules = "incomeFromRules" ref = "incomeForm">
	        	<el-form-item label="提现银行卡：" prop="bankcardInfoId">
		            <el-select placeholder="请选择" v-model="incomeForm.bankcardInfoId" filterable style = "width: 250px; margin-left: 25px">
			            <el-option v-for="item in bankCardInfoList" :label="item.info" :value="item.id">
				    	</el-option>
		            </el-select>
		            <span @click="createBankCard()" ><font color="#FF0000" style="cursor:pointer;">+添加银行卡</font></span>
		        </el-form-item>
		        
		        <el-form-item label="联系人手机号：" prop="mobile">
	                <el-input placeholder="请输入" v-model = "incomeForm.mobile" @keyup.native="number" style = "width: 250px; margin-left: 10px"></el-input>
	            </el-form-item>
	            
	            <el-form-item label="提现金额(度币)：" prop="priceNum">
		            <el-input v-model="incomeForm.priceNum" maxlength="10" @keyup.native="number" style = "width: 250px;"></el-input>
				    <span class="huansuanImg"></span>人民币(元)：{{DBChange/100}}
	            </el-form-item>
	            
	            <el-form-item label="提现方式：" prop="withdrawType">
		            <el-select style="margin-left: 40px; width: 250px;" v-model="incomeForm.withdrawType" placeholder="请选择提现方式">
		                <el-option value="0" label="个人"></el-option>
		                <el-option value="1" label="公司"></el-option>
		            </el-select>
	            </el-form-item>
	            
        	</el-form>
        	
            <span slot="footer" class="dialog-footer">
	            <el-button type="primary" round size="small" v-on:click="handlerWithdrawDubiAndVerity()">确 定</el-button>
	            <el-button @click="showIncome = false" round size="small">取 消</el-button>
	        </span>
        </el-dialog>

        <el-dialog title="新增银行卡" :visible.sync="showBankCard" width="600px" top="30vh">
	        <el-form :model = "bankCardForm" :rules = "rules" ref = "bankCardForm" class = "showBankCardFrom">
	            <el-form-item label="姓名:" class = "td1" prop="name">
	                <el-input placeholder="请输入" v-model="bankCardForm.name" class = "input" style = "margin-left: 30px;"></el-input>
	            </el-form-item>
	            <el-form-item label="银行卡号:" prop="cardNumber">
	                <el-input placeholder="请输入存储卡" v-model = "bankCardForm.cardNumber" class = "input2"></el-input>
	            </el-form-item>
	            <el-form-item label="所属银行:" prop="issuingBankValue">
	                <el-select placeholder="请选择" v-model="bankCardForm.issuingBankValue" class = "input">
	                    <el-option v-for="item in issuingBankList" :label="item.name" :value="item.value">
	                    </el-option>
	                </el-select>
	            </el-form-item>
	            <el-form-item label="所属支行:" prop="subBranchInfo">
	                <el-input placeholder="请输入" v-model = "bankCardForm.subBranchInfo" class = "input2"></el-input>
	            </el-form-item>
	        </el-form>
	        <span slot="footer" class="dialog-footer">
	            <el-button type="primary" round size="small" v-on:click="bankCardSaveAndVerify()">确 定</el-button>
	            <el-button @click="showBankCard = false" round size="small">取 消</el-button>
	        </span>
	    </el-dialog>
        
        <el-dialog
                title="转出"
                :visible.sync="showDiposit"
                width="540px"
                top="30vh"
                class="chargeMode">
            <p style="margin: 10px 0 0 40px;">转出金额(度币)：
                <el-input v-model="priceNum"  
                          maxlength="10"
                          @keyup.native="number"
                          style="width:130px;"></el-input>
            </p>
            <p style="margin: 10px 0 0 90px;">收款人：
                <el-select filterable style="margin-left:6px;" v-model="userId" placeholder="">
                    <el-option v-for="item in users" :key="item.id" :label="item.userName" :value="item.id">
                    </el-option>
                </el-select>
            </p>
            <span slot="footer" class="dialog-footer">
          <el-button type="primary" round size="small" v-on:click="handlerUserTransferDubi()">确 定</el-button>
          <el-button @click="showDiposit = false" round size="small">取 消</el-button>
        </span>
        </el-dialog>
        
        <el-dialog title="提示" :visible.sync="drawingsDialog" width="600px" top="30vh">
        	<div>{{drawingsMessage}}</div>
        	<div class = "font1">三度客服：0755-23895307</div>
        </el-dialog>
        
    </div>
</template>

<script type="text/ecmascript-6">
    import inCome from './components/income';
    import diPosit from './components/diposit';
    import rollOut from './components/rollOut';
    import qs from 'qs';

    export default {
        data() {
        	var checkCardNumber = (rule, value, callback) => {
                var regex = /^([1-9]{1})(\d{15}|\d{18})$/;
                value = value.replace(/\s+/g, "");
                if (regex.test(value)) {
                    callback();
                } else {
                    callback(new Error("银行卡格式不正确"));
                }
            };
            return {
            	drawingsDialog: false,
            	drawingsMessage: "",
            	priceNum: "",
            	incomeForm: {
            		bankcardInfoId: "",
            		mobile: "",
            		priceNum: "",
            		withdrawType: ""
            	},
            	issuingBankList: [],
            	rules: {
                    issuingBankValue: [
                        {required: true, message: "请选择所属银行", trigger: "change"}
                    ],
                    name: [
                        {required: true, message: "请输入姓名", trigger: "blur"},
                        {max: 20, message: "长度不能大于20", trigger: "blur"}
                    ],
                    cardNumber: [
                        {required: true, message: "请输入银行卡号", trigger: "blur"},
                        {validator: checkCardNumber, trigger: 'blur'}
                    ],
                    subBranchInfo: [
                        {required: true, message: "请输入所属支行", trigger: "blur"},
                        {max: 100, message: "长度不能大于100", trigger: "blur"}
                    ]
                },
                incomeFromRules: {
                	bankcardInfoId: [
                		{required: true, message: "请选择银行卡", trigger: "change"}
                	],
                	mobile: [
                		{required: true, message: "请输入手机号", trigger: "blur"},
                		{validator: this.validPhone, trigger: "blur"}
                	],
                	priceNum: [
                		{required: true, message: "请输入度币", trigger: "blur"},
                		{validator: function(rule,value,callback) {
                			if(value < 1000) {
                				callback(new Error("请输入大于1000的度币"));
                			}else{
                				callback();
                			}
                			}, trigger: "blur"}
                	],
                	withdrawType: [
                		{required: true, message: "请选择提现方式", trigger: "change"},
                	]
                },
            	showBankCard: false,
            	bankCardForm: {
                    issuingBankValue: "",
                    name: "",
                    cardNumber: "",
                    subBranchInfo: ""
                },
                activeName: 'income',
                showIncome: false,
                showDiposit: false,
                DBChange: 0,
                options: [
                    {
                        value: "平台",
                        label: "平台"
                    }
                ],
                value: "",
                incomeParams: {
                    currentDubi: 0,
                    frozenDubi: 0,
                    totalIncomeDubi: 0,
                    transferDubi: 0,
                    withdrawDubi: 0
                },
                withdrawType: '0',
                users: [],
                userId: '',
                bankCardInfoList: []
            }
        },
        methods: {
            validPhone(rule, value, callback) {
                let mobileRegex = /^1[3456789]\d{9}$/;
                if (mobileRegex.test(value)) {
                    callback();
                } else {
                    callback(new Error('手机号码格式不正确！'))
                }
            },
        	getIssuingBankList(type) {
                this.API.getIssuingBankList(type).then((res) =>{
                    this.issuingBankList = res.obj;
                })
            },
        	// 清楚新建银行卡表格内容
            bankCardFormClear() {
                this.$refs["bankCardForm"].resetFields();
                this.bankCardForm.issuingBankValue = "";
                this.bankCardForm.name = "";
                this.bankCardForm.cardNumber = "";
                this.bankCardForm.subBranchInfo = "";
            },
        	// 保存银行卡+参数校验
            bankCardSaveAndVerify() {
                this.$refs["bankCardForm"].validate((valid) => {
                    if(valid){
                        this.bankCardSave();
                    }else{
                        
                    }
                });
            },
            // 保存银行卡
            bankCardSave() {
            	let jsonData = {
            		"issuingBankValue": this.bankCardForm.issuingBankValue,
            		"name": this.bankCardForm.name,
            		"cardNumber": this.bankCardForm.cardNumber,
            		"subBranchInfo": this.bankCardForm.subBranchInfo
            	}
                this.API.bankCardSave(jsonData).then((res) =>{
                    if(res.success) {
                        this.$message.success('创建成功');
                        this.showBankCard = false;
                        this.bankCardFormClear();
                        this.getBankCardInfoList();
                    } else {
                        this.$message.error(res.message);
                    }
                })
            },
        	createBankCard() {
                this.showBankCard = true;
            },
            showTab(permission,currentTab){
                let per = this.isPer(permission);
                if (!per){
                    this.activeName = currentTab
                }
                return per
            },
            isPer(per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            number() {
                this.incomeForm.priceNum = this.incomeForm.priceNum.replace(/[^\ .\d]/g, '');
                this.incomeForm.priceNum = this.incomeForm.priceNum.replace('.', '');
                this.incomeForm.priceNum = this.incomeForm.priceNum.replace(/\s+/g,"");
                this.incomeForm.mobile = this.incomeForm.mobile.replace(/[^\ .\d]/g, '');
                this.incomeForm.mobile = this.incomeForm.mobile.replace('.', '');
                this.incomeForm.mobile = this.incomeForm.mobile.replace(/\s+/g,"");
                this.DBChange = this.incomeForm.priceNum * 10;
            },
            // 点击提现按钮
            drawings() {
            	// 判断本月还有没有提现机会
                this.API.isAllowDrawings().then((res) => {
                    if (res.success) {
                        if(res.obj.allow){
                        	this.showDialog("提现");
                        }else{
                        	// this.$message.error(res.obj.message);
                        	this.drawingsMessage = res.obj.message;
                        	this.drawingsDialog = true;
                        }
                    } else {
                        this.$message.error(res.message);
                    }
                });
            },
            showDialog(type) {
                switch (type) {
                    case '提现':
                        this.showIncome = true;
                        this.incomeForm.priceNum = '';
                        this.DBChange = '';
                        break;
                    case '转出':
                        this.priceNum = '';
                        this.showDiposit = true;
                        //获取公司下的用户
                        break;
                }
            },
            getCompanyIncomeAggregated() {
                let companyId = sessionStorage.getItem("companyID");
                this.API.getCompanyIncomeAggregated({
                    companyId: companyId
                }).then((res) => {
                	if(res.obj[0] == null || res.obj[0] == undefined) {
                		
                	}else {
                		this.incomeParams = res.obj[0];
                	}
                    /*console.log("------this.incomeParams: " + this.incomeParams);*/
                })
            },
            handlerWithdrawDubiAndVerity() {
                this.$refs["incomeForm"].validate((valid) => {
                    if(valid){
                        this.handlerWithdrawDubi();
                    }
                });
            },
            handlerWithdrawDubi() {
                //提现度币
                
                if(this.incomeForm.priceNum == 0){
                    if(this.priceNum.charAt(0)<=0){
                    this.$message.warning('请输入您要提现的度币');
                    return;
                    }
                }
                if (this.incomeForm.priceNum == '' || this.incomeForm.priceNum < 1000) {
                    this.$message.warning('请输入大于1000的度币');
                    return
                }
                let companyId = sessionStorage.getItem("companyID");
                let formData = new FormData();
                formData.append("companyId", companyId);
                formData.append("dubi", this.incomeForm.priceNum);
                formData.append("withdrawType", this.incomeForm.withdrawType);
                formData.append("mobile", this.incomeForm.mobile);
                formData.append("bankcardInfoId", this.incomeForm.bankcardInfoId);
                this.API.UserWithdrawDubi(formData).then((res) => {
                    if (res.success) {
                        this.$message.success(res.message);
                        this.getCompanyIncomeAggregated();
                        this.$refs.posit.getCompanyWithdrawListTwo(1);
                        this.incomeFormClear();
                    } else {
                        this.$message.error(res.message);
                    }
                    this.showIncome = false;
                });
            },
            incomeFormClear() {
            	this.$refs["incomeForm"].resetFields();
            	this.incomeForm.bankcardInfoId = "";
            	this.incomeForm.mobile = "";
            	this.incomeForm.priceNum = "";
            	this.incomeForm.withdrawType = "";
            },
            handlerUserTransferDubi() {
               //转出度币
                if(this.priceNum == 0){
                    if(this.priceNum.charAt(0)<=0){
                        this.$message.warning('请输入您要转出的度币');
                        return;
                    }
                }
                if (this.priceNum == '' || this.priceNum.charAt(0) <= 0) {
                    this.$message.warning('请输入大于0的金额');
                    return
                }

                if (this.userId == 0 || this.userId == ''){
                    this.$message.warning("请选择收款人");
                    return;
                }

                let companyId = sessionStorage.getItem("companyID");
                let formData = new FormData();
                formData.append("companyId", companyId);
                formData.append("dubi", this.priceNum);
                formData.append("userId", this.userId);
                this.API.userTransferDubi(formData).then((res) => {
                    if (res.success) {
                        this.$message.success(res.message);
                        this.$refs.roll.getTransferList(1);
                    } else {
                        this.$message.error(res.message);
                    }
                    this.showDiposit = false;
                    this.getCompanyIncomeAggregated();
                })
            },
            getUserList() {
                //获取公司下的所有用户
                let companyId = sessionStorage.getItem("companyID");
                this.API.getCompanyUsers({
                    companyId: companyId
                }).then((res) => {
                    this.users = res.obj;
                    this.users.map(item=>{
                        item.userName='用户名:'+item.nickName+' | '+'电话:'+item.mobile;
                    })
                })
            },
            // 获取银行卡信息列表
            getBankCardInfoList() {
                this.API.getBankCardInfoList().then((res) =>{
                    if(res.success) {
                        this.bankCardInfoList = res.datalist;
                    } else {
                        this.$message.error(res.message);
                    }
                })
            }
        },
        components: {
            inCome,
            diPosit,
            rollOut
        },
        created() {
            this.getUserList();
            this.getCompanyIncomeAggregated();
            this.getBankCardInfoList();
            this.getIssuingBankList("issuingBank");
        }
    }
</script>

<style scoped lang="scss">
.showBankCardFrom {
    tr {
        height: 60px;
    }
    .el-form-item {
        width: 600px;
        // text-align: right;
    }
    .input {
        width: 200px;
        background-color: #fafafa;
        border-radius: 2px;
        /*border: solid 1px #dddddd;*/
    }
    .input2 {
        width: 400px;
        background-color: #fafafa;
        border-radius: 2px;
        /*border: solid 1px #dddddd;*/
    }
    .td1 {
        // margin-left: 30px;
    }
}
    header {
        background: #fffaf8;
        height: 116px;
        line-height: 76px;
        font-weight: bold;
        .bottombox {
            height: 40px;
            background: #fff2ed;
            line-height: 40px;
            > span {
                display: inline-block;
                margin-left: 25px;
                color: #333333;
                font-size: 14px;
                font-weight: normal;
            }
        }
        .rightbox {
            display: inline-block;
            .el-button {
                font-size: 16px;
                font-weight: bold;
            }
            .line {
                color: #ff6419;
                font-size: 16px;
                margin: 0 10px;
            }
        }
        .title {
            font-size: 18px;
            color: #333333;
            font-weight: bold;
            display: inline-block;
            margin: 0 10px 0 20px;
        }
        .money {
            font-size: 18px;
            color: #ff6419;
            display: inline-block;
            .moneylogo, .moneyhow {
                display: inline-block;
                vertical-align: middle;
                width: 18px;
                height: 18px;
                background-size: 18px 18px;
                margin: 0 6px;
            }
            .moneylogo {
                background: url('../../../assets/images/icons/icon-dubi.png') no-repeat;
            }
            .moneyhow {
                background: url('../../../assets/images/icons/icon-shuoming.png') no-repeat;
                margin-right: 40px;
            }
        }
    }

    .huansuanImg {
        width: 12px;
        height: 14px;
        background: url('../../../assets/images/icons/icon-huansuan.png') no-repeat;
        background-size: 12px 14px;
        display: inline-block;
        margin: 0 10px;
        vertical-align: middle;
    }
    .font1 {
		height: 20px;
		font-family: MicrosoftYaHei;
		font-size: 12px;
		font-weight: normal;
		font-stretch: normal;
		letter-spacing: 0px;
		color: #999999;
    	margin-top: 15px;
    }
</style>
