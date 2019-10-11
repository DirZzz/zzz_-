<!--页面注释-->
<template>
    <div>
        <div class = "title">
            <header>
                <h1>银行卡</h1>
                <el-button class = "button" type="primary" round @click="createBankCardAndVerfiy()">新建银行卡</el-button>
            </header>
            <main>
            </main>
            <el-dialog title="新增银行卡" :visible.sync="showBankCard" width="600px" top="30vh">
                <el-form :model = "bankCardForm" :rules = "rules" ref = "bankCardForm" class = "showBankCardFrom">
                    <el-form-item label="姓名:" class = "td1" prop="name">
                        <el-input placeholder="请输入" v-model="bankCardForm.name" class = "input" style = "margin-left: 30px;"></el-input>
                    </el-form-item>
                    <el-form-item label="银行卡号:" prop="cardNumber">
                        <el-input placeholder="请输入存储卡" v-model = "bankCardForm.cardNumber" class = "input2"></el-input>
                    </el-form-item>
                    <el-form-item label="所属银行:" prop="issuingBankValue">
                        <el-select placeholder="请选择" v-model="bankCardForm.issuingBankValue" class = "input" filterable>
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
        </div>
        <div class="bankList">
            <div class="items" v-for = "item in bankCardInfoList">
                <img src="./yinhangka.png" alt="">
                <span class="bankName">{{item.issuingBank}}</span>
                <span class="bankType">{{item.cardType}}</span>
                <span class="bankNum">{{item.cardNumberHide}}</span>
                <span class="bankBtn" @click = "bankCardInfoDelete(item.id)">删除</span>
            </div>
        </div>
    </div>
</template>

<script type="text/ecmascript-6">
    export default {
        watch: {

        },
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
                showBankCard: false,
                issuingBankList: [],
                bankCardForm: {
                    issuingBankValue: "",
                    name: "",
                    cardNumber: "",
                    subBranchInfo: ""
                },
                bankCardInfoList: [],
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
                }
            }
        },
        methods: {
        	createBankCardAndVerfiy() {
                this.API.getIsAllowCreate().then((res) =>{
                    if(res.success) {
                    	if(res.obj.flag){
                    		this.createBankCard();
                    	}else{
                    		this.$message.error(res.obj.message);
                    	}
                    } else {
                        this.$message.error(res.message);
                    }
                })
        	},
            createBankCard() {
                this.showBankCard = true;
            },
            getIssuingBankList(type) {
                this.API.getIssuingBankList(type).then((res) =>{
                    this.issuingBankList = res.obj;
                })
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
            // 清楚新建银行卡表格内容
            bankCardFormClear() {
                this.$refs["bankCardForm"].resetFields();
                this.bankCardForm.issuingBankValue = "";
                this.bankCardForm.name = "";
                this.bankCardForm.cardNumber = "";
                this.bankCardForm.subBranchInfo = "";
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
            },
            bankCardInfoDelete(id){
                this.$confirm('此操作将删除当前银行卡信息, 是否继续?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.bankCardInfoDelete(id).then((res) =>{
                    if(res.success) {
                        this.$message.success("删除成功");
                        this.getBankCardInfoList();
                    } else {
                        this.$message.error(res.message);
                    }
                })
                }).catch(() => {
                    this.$message.info("已取消删除");
                });
            }
        },
        components: {

        },
        created() {
            this.getIssuingBankList("issuingBank");
            this.getBankCardInfoList();
        },
        beforeDestroy(){
            
        }
    }
</script>

<style scoped lang="scss">
.title {
	height: 76px;
	background-color: #ffffff;
    h1 {
        width: 54px;
        height: 18px;
        font-family: MicrosoftYaHei;
        font-size: 18px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #333333;
        float: left;
        margin-top: 29px;
        margin-left: 21px;
    }
    .button {
        height: 36px;
        background-color: #ff6419;
        border-radius: 18px;
        /*margin-right: 20px;*/
        text-align: center;
        float: right;
        margin-top: 20px;
        margin-right: 20px;
    }
}
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
.bankList{
    width: 100%;
    box-sizing: border-box;
    padding: 20px;
    padding-right: 0;
    background: #fff;
    border-top: 1px solid #ccc;
    min-height: 884px;
    .items{
        width: 385px;
        margin-right: 15px;
        height: 221px;
        border-radius: 10px;
        overflow: hidden;
        position: relative;
        float: left;
        font-size: 0;
        margin-bottom: 23px;
        box-shadow: 0 10px 20px rgba(255, 130, 63, .2);
        img{
            display: block;
            height: 100%;
        }
        span{
            position: absolute;
            left: 23px;
            display: inline-block;
            color: #fff;
            line-height: 1;
        }
        .bankName{
            top: 28px;
            font-size: 18px;
        }
        .bankType{
            top: 51px;
            font-size: 14px;
        }
        .bankNum{
            bottom: 55px;
            font-size: 36px;
        }
        .bankBtn{
            top: 17px;
            left: 321px;
            font-size: 14px;
            width: 48px;
            height: 24px;
            cursor: pointer;
            line-height: 24px;
            background-color: #e2e2e2;
            border-radius: 4px;
            text-align: center;
            color: #333333;
        }
    }
}
</style>
