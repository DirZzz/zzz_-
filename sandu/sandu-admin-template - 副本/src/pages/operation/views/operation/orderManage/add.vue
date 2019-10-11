<template>
    <div class="addFrame">
        <transition name="fade">
            <div class="addProduct">
                <v-nav :nav="nav"></v-nav>
                <div class="headTitle">新增</div>
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
                                <el-select v-model="ruleForm.intermediaryId" filterable placeholder="请选择">
                                    <el-option
                                    v-for="(item,index) in categoryList"
                                    :key="index"
                                    :label="item.userName"
                                    :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item label="成交对象：" prop="tradePartner">
                                <el-input maxlength="50" class="formWidth" v-model.trim="ruleForm.tradePartner" placeholder="请输入内容"></el-input>
                            </el-form-item>

                            <el-form-item label="成交金额：" prop="tradePrice">
                                <el-input  maxlength="8" class="formWidth" v-model.trim="ruleForm.tradePrice"  placeholder="请输入内容"></el-input>
                            </el-form-item>

                            <el-form-item label="成交时间：" prop="tradeDate">
                                <el-date-picker
                                        maxlength="50"
                                        v-model="ruleForm.tradeDate"
                                        type="datetime"
                                        placeholder="选择日期时间">
                                </el-date-picker>
                            </el-form-item>
                        </el-row>
                        <el-form-item>
                            <el-button type="primary" size="mini" round @click="submitForm('ruleForm')">录入
                            </el-button>
                            <el-button round size="mini" @click="resetForm('ruleForm')">取消</el-button>
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
                    {nav: '业主成交订单管理', url: '/template'},
                    {nav: '新增', url: ''}
                ],
                ruleForm: { // 验证表单
        },
            brandSelect: [
                {id: 1, name: '大自然'},
                {id: 2, name: '圣象地板'},
                {id: 3, name: '简一'},
            ],
                // 验证表单规则
                rules:{
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
        created() {
            this.categoryDroplist();
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.nav[0].url = from.path
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
                console.log(formName)
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let formDatas = this.ruleForm;
                        for (let key in formDatas) {
                            if (formDatas[key] == '' || formDatas[key] == undefined || formDatas[key] == null) {
                                delete formDatas[key]
                            }
                        }
                        let time = formDatas.tradeDate.valueOf();
                        time = formatDate(new Date(time),'yyyy-MM-dd hh:mm:ss');
                        let query = {
                            intermediaryId:formDatas.intermediaryId,
                            intermediaryName:formDatas.intermediaryName,
                            tradePartner:formDatas.tradePartner,
                            tradePrice:formDatas.tradePrice,
                            tradeDate:time
                        }
                        let jsonData = JSON.stringify(query)
                        this.$confirm('确定录入内容?', '提示', {
                            confirmButtonText: '确定',
                            type: 'info',
                            cancelButtonClass: 'cancelButtonClass',
                            confirmButtonClass: 'confirmButtonClass',
                            center: true
                        }).then(() => {
                            this.API.createOrderManage(query).then(() => {
                                this.$message.success('产品录入成功');
                                this.$router.push("/orderManage/list");
                            });
                        })
                    }else{
                        return false
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.$router.push("/orderManage/list");
            }
        }
    }
</script>

<style lang="scss" scoped>
    /*@import "../../assets/css/mixin";*/
    .addFrame{
        margin: 20px;
    }
    .headTitle {
        border-bottom: solid 1px #ddd;
        font-size: 20px;
        font-weight: normal;
        color: #666666;
        padding-bottom: 20px;
    }

    .addProduct {
        text-align: left;
        background: #fff;
        box-sizing: padding-box;
        padding: 20px;
        margin-bottom: 20px;
        border-radius: 5px;
        .productBaseItem{
            .el-input,.el-select{
                width: 250px;
            }
        }
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

