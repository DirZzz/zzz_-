<template>
    <div class="orderFrames">
        <!--<v-nav :nav="nav"></v-nav>-->
        <transition name="fade">
            <div class="addProduct">
                <div class="headTitle">编辑</div>
                <p class="ptop"><span>*</span>为必填项</p>

                <div>
                    <el-row :gutter="24" class="splineFrame">
                        <el-col :span="2" class="productBaseInfo">基本信息</el-col>
                        <el-col :span="22" class="rightLine">
                            <hr>
                        </el-col>
                    </el-row>
                </div>

                <div class="productBaseItem">
                    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                        <el-row class="formFrameWidth">
                            <!--<el-col :span="12">-->
                            <!--</el-col>-->
                        
                        <el-form-item label="中介名称：" prop="intermediaryId">
                            <el-select v-model="ruleForm.intermediaryId" placeholder="请选择">
                                <el-option
                                        v-for="(item,index) in categoryList"
                                        :key="index"
                                        :label="item.userName"
                                         :value="item.id">
                                </el-option>
                            </el-select>
                            </el-form-item>
                            
                        <el-form-item label="成交对象：" prop="tradePartner">
                                <el-input class="formWidth" clearable v-model.trim="ruleForm.tradePartner" placeholder="请输入内容"></el-input>
                            </el-form-item>

                            <el-form-item label="成交金额：" prop="tradePrice">
                                <el-input class="formWidth" maxlength="8" clearable v-model.trim="ruleForm.tradePrice" placeholder="请输入内容"></el-input>
                            </el-form-item>

                            <el-form-item label="成交时间：" prop="tradeDate">
                                <el-date-picker
                                        v-model="ruleForm.tradeDate"
                                        type="datetime"
                                        placeholder="选择日期时间">
                                </el-date-picker>
                            </el-form-item>



                        </el-row>

                        <div class="splitLine"></div>
                        <el-form-item>
                            <el-button type="primary" class="confirmBtn" round @click="submitForm('ruleForm')">确定
                            </el-button>
                            <el-button round class="cancelBtnBg marRight" @click="resetForm('ruleForm')">取消</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </transition>
    </div>
</template>

<script type="es6">
    import {formatDate} from '&/utils/date';

    export default {
        data() {
            return {
                categoryList: [

                ],
                nav: [
                    {nav: '产品库', url: '/template'},
                    {nav: '编辑产品', url: ''}
                ],
                ruleForm: {},
                brandSelect: [
                    {id: 1, name: '大自然'},
                    {id: 2, name: '圣象地板'},
                    {id: 3, name: '简一'},
                ],
                // 验证表单规则
                rules: {
                    // productNameStr: [
                    //     {required: true, message: '请输入产品名称', trigger: 'blur'},
                    //     {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}
                    // ]
                    tradePartner:[
                        {required: true, message: '请输入成交对象', trigger: 'blur'},
                    ],
                    intermediaryId:[
                        {required: true, message: '请输入中介名称', trigger: 'blur'},
                    ],
                    tradePrice:[
                        {required: true, message: '请输入成交金额', trigger: 'blur'},
                        {validator: this.checkNum, trigger: 'blur'}
                    ],
                    tradeDate:[
                        {required: true, message: '请选择成交时间', trigger: 'blur'},
                    ],
                }
            }
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.nav[0].url = from.path
            })
        },
        created() {
            this.categoryDroplist();
            this.API.getOrderManageById({
                orderManageId:this.$route.params.orderManageId
            }).then(res=>{
                this.ruleForm = res.data;
            })
        },
        methods: {
            /*金额验证*/
            checkNum(rule, value, callback) {
                let regu = /^[1-9]\d*$/;
                var patrn = /^\d+(\.\d+)?$/;
                if (regu.test(value)||patrn.test(value)) {
                    //
                    callback();
                } else {
                    callback(new Error("请输入正数"));
                }
            },
            categoryDroplist(){
                this.API.getUserCategoryList().then((res) => {
                    this.categoryList = res.data
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let formDatas = this.ruleForm;
                        for (let key in formDatas) {
                            if (formDatas[key] == '' || formDatas[key] == undefined || formDatas[key] == null) {
                                delete formDatas[key]
                            }
                        }
                        let jsonData = JSON.stringify(formDatas)
                        this.$confirm('确定修改内容?', '提示', {
                            confirmButtonText: '确定',
                            type: 'info',
                            cancelButtonClass: 'cancelButtonClass',
                            confirmButtonClass: 'confirmButtonClass',
                            center: true
                        }).then(() => {
                            // this.$message.success('修改成功');
                            let time = formDatas.tradeDate.valueOf();
                            time = formatDate(new Date(time),'yyyy-MM-dd hh:mm:ss');
                            let query={
                                id:formDatas.id,
                                intermediaryId:formDatas.intermediaryId,
                                tradePartner:formDatas.tradePartner,
                                tradePrice:formDatas.tradePrice,
                                tradeDate:time
                            }
                            //提交数据
                            this.API.updateOrderManage(query).then(()=>{
                                this.$message({ message: "成功更新内容", type: "success" });
                                this.$router.push("/orderManage/list");
                            })
                        })
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();

                this.$message({ message: "您取消了此次编辑", type: "warning" });
                this.$router.push("/orderManage/list");
            }
        }
    }
</script>

<style lang="scss" scoped>
    /*@import "../../assets/css/mixin";*/
    .el-input,.el-select{
      width: 220px;
    }
    .orderFrames{
        margin: 20px;
    }
    .headTitle {
        border-bottom: solid 1px #ddd;
        font-size: 20px;
        text-align: left;
        font-weight: normal;
        color: #666666;
        padding-bottom: 20px;
    }

    .addProduct {
        background: #fff;
        box-sizing: padding-box;
        padding: 20px;
        margin-bottom: 20px;
        border-radius: 5px;
        text-align: left;
        .ptop {
            height: 30px;
            line-height: 30px;
            padding-top: 10px;
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

    }
</style>

