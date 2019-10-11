<!--业主信息编辑-->
<template>
    <div class="ownerInfoEdit">
        <header class="header">
            <!-- <p class="navtext">推广信息/业主基础信息/编辑</p> -->
            <v-nav :nav="nav" class="navtext"></v-nav>
            <h1 class="edittitle">
                <span>编辑</span>
                <div class="right">
                    <img src="../../assets/images/icon/icon_back.png" @click="toback()" style="cursor:pointer;">
                </div>
            </h1>
        </header>
        <el-form class="form" label-width="200px" :model="formOptions" :rules="rules" ref="ruleForm">
            <div class="form-item-left ">
                <p class="neededtext"><span class="star">*</span>为必填项</p>
                <el-form-item label=" 需求类型：">
                    {{formOptions.requirementType}}
                </el-form-item>
                <el-form-item label="姓名：" prop="userName">
                    <el-input v-model="formOptions.userName"></el-input>
                </el-form-item>
                <el-form-item label="联系电话：" prop="mobile">
                    <el-input v-model="formOptions.mobile"></el-input>
                </el-form-item>
                <el-form-item label="城市：" prop="provinceCity">
                    <el-cascader v-model="provinceCity" size="small" placeholder="请选择城市"
                                 :options="provinceCityList"
                                 expand-trigger='hover'
                                 filterable
                                 clearable>
                    </el-cascader>
                </el-form-item>
                <el-form-item label="小区：">
                    <el-input v-model="formOptions.houseEstate"></el-input>
                </el-form-item>
                <el-form-item label="详细地址：">
                    <el-input v-model="formOptions.address"></el-input>
                </el-form-item>
                <el-form-item label="户型：">
                    <el-select v-model="formOptions.bedroomNum" style="width: 103px" size="small">
                        <el-option
                                v-for="n in 7"
                                :key="n -1"
                                :label="n - 1"
                                :value="n - 1">
                        </el-option>
                    </el-select>
                    室

                    <el-select v-model="formOptions.livingRoomNum" style="width: 103px" size="small">
                        <el-option
                                v-for="n in 7"
                                :key="n -1"
                                :label="n - 1"
                                :value="n - 1">
                        </el-option>
                    </el-select>
                    厅

                    <el-select v-model="formOptions.toiletNum" style="width: 103px" size="small">
                        <el-option
                                v-for="n in 7"
                                :key="n -1"
                                :label="n - 1"
                                :value="n - 1">
                        </el-option>
                    </el-select>
                    卫
                </el-form-item>
                <el-form-item label="面积(㎡)：" prop="houseAcreage">
                    <el-input v-model="formOptions.houseAcreage"></el-input>
                </el-form-item>
                <el-form-item label="装修方式：">
                    <el-select v-model="formOptions.decorateTypeValue" size="small" placeholder="请选择需求类型" clearable>
                        <el-option
                                v-for="item in decorateTypeList"
                                :key="item.decorateTypeValue"
                                :label="item.decorateType"
                                :value="item.decorateTypeValue">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="装修预算(元)：">
                    <el-select v-model="formOptions.decorateBudgetValue" size="small" placeholder="请选择需求类型"
                               :disabled="editFunc"
                               clearable>
                        <el-option
                                v-for="item in decorateBudgetList"
                                :key="item.decorateBudgetValue"
                                :label="item.decorateBudget"
                                :value="item.decorateBudgetValue">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="倾向风格：">
                    <el-select v-model="formOptions.goodStyleValue" size="small" placeholder="请选择需求类型" clearable>
                        <el-option
                                v-for="item in goodStyleList"
                                :key="item.goodStyleValue"
                                :label="item.goodStyle"
                                :value="item.goodStyleValue">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="装修类型：">
                    <el-select v-model="formOptions.decorateHouseTypeKey" size="small" placeholder="请选择需求类型" clearable>
                        <el-option
                                v-for="item in decorateHouseTypeList"
                                :key="item.decorateHouseTypeKey"
                                :label="item.decorateHouseType"
                                :value="item.decorateHouseTypeKey">
                        </el-option>
                    </el-select>
                </el-form-item>
            </div>
            <div class="form-item-right">
                <el-form-item label="设计方案：" style="margin-top:34px;">
                    <img width="372px" v-if="formOptions.designPlanPic" height="220px" alt="无图片"
                         :src="imgurl+formOptions.designPlanPic">
                    <div v-else class="noimg">无图</div>
                </el-form-item>
                <el-form-item label="选定装修公司：">
                    {{formOptions.companyName}}
                </el-form-item>
                <el-form-item label="客户类型：">
                    <el-select v-model="formOptions.customerTypeValue" size="small" placeholder="请选择需求类型"
                               clearable :disabled="editFunc">
                        <el-option
                                v-for="item in customerTypeList"
                                :key="item.customerTypeValue"
                                :label="item.customerType"
                                :value="item.customerTypeValue">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注：">
                    <el-input type="textarea" rows="8" class="area" v-model="formOptions.remark"></el-input>
                </el-form-item>
                <el-form-item label="下次回访日期：">
                    <el-date-picker
                            class="pickDate"
                            v-model="formOptions.nextTime"
                            type="date"
                            size="small"
                            value-format="yyyy-MM-dd 00:00:00"
                            placeholder="选择日期下次回访日期">
                    </el-date-picker>
                </el-form-item>
            </div>
            <el-form-item class="boxbottom" label-width="0">
                <el-button type="primary" size="medium" @click="submitCustomerInfo('ruleForm')">保存</el-button>
                <el-button size="medium" @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script type="text/ecmascript-6">
    export default {
        data() {
            return {
                imgurl: process.env.sourceBaseUrl,
                nav: [
                    {nav: '推广信息', url: '/ownerInfo'},
                    {nav: '业主基础信息', url: '/ownerInfo'},
                    {nav: '编辑', url: ''}
                ],
                formOptions: {},
                provinceCityList: [],
                submitFormOptions: {},
                provinceCity: [],
                pickerOptions1: {},
                pickdate: {},
                decorateTypeList: [],
                decorateBudgetList: [],
                goodStyleList: [],
                decorateHouseTypeList: [],
                customerTypeList: [],
                editFunc: true,
                rules: {
                    userName: [
                        {required: true, message: '请输入用户名称', trigger: 'blur'},
                        {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
                    ],
                    mobile: [
                        {required: true, message: '请输入联系电话', trigger: 'blur'},
                        {min: 3, max: 11, message: '长度在 11 个字符', trigger: 'blur'}
                    ],
                    /* provinceCity: [
                         { required: true, message: '请选择城市', trigger: 'blur' }
                     ],*/
                    houseAcreage: [
                        {
                            validator: (rule, value, callback) => {
                                console.log(this.formOptions.customerTypeValue)
                                if (this.formOptions.customerTypeValue === 1 || this.formOptions.customerTypeValue === 2) {
                                    if (!value) {
                                        callback(new Error('请输入面积'));
                                    }
                                    else if (value && value.length > 5) {
                                        callback(new Error('长度在 1 到 5 个字符'))
                                    } else {
                                        callback();
                                    }
                                }
                                else {
                                    callback();
                                }
                            }, trigger: 'blur',
                        }
                    ],
                }
            }
        },
        methods: {
            toback() {
                this.$router.push('/ownerInfo')
            },
            resetForm(formName) {
                /*this.provinceCity = []
                this.formOptions={}
                this.$refs[formName].resetFields()*/
                this.getCustomerInfo(this.$route.query.id);
            },
            submitCustomerInfo(formName) {
                console.log("-----------")
                this.formOptions.provinceCode = this.provinceCity[0];
                this.formOptions.cityCode = this.provinceCity[1];
                // 不要注释掉！！！
                if (this.formOptions.nextTime) {
                    this.formOptions.nextTime = this.formOptions.nextTime + ' 00:00:00';
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.API.updateCustomerInfo(this.formOptions).then((res) => {
                            if (res.success) {
                                this.$message.success('提交成功');
                                this.$router.push('/ownerInfo');
                            } else {
                                this.$message.warning(res.message);
                            }
                        })
                    } else {
                        return false;
                    }
                });
            },
            getCustomerInfo: function (id) {
                this.API.getCustomerInfo({'id': id})
                    .then(res => {

                        this.formOptions = res.data.proprietorInfoDetailVo;
                        this.decorateTypeList = res.data.decorateTypeList;
                        this.decorateBudgetList = res.data.decorateBudgetList;
                        this.goodStyleList = res.data.goodStyleList;
                        this.decorateHouseTypeList = res.data.decorateHouseTypeList;
                        this.customerTypeList = res.data.customerTypeList;
                        //alert(res.data.proprietorInfoDetailVo.provinceCode)
                        //  if (res.data.ProprietorInfoDetailVo == undefined){
                        //     //this.provinceCity.splice(0, 0, res.data.ProprietorInfoDetailVo.provinceCode);
                        //    // this.provinceCity=''
                        //  }else{
                        //     this.provinceCity.splice(0, 0, res.data.ProprietorInfoDetailVo.provinceCode);
                        //  }
                        //  if (res.data.ProprietorInfoDetailVo == undefined){
                        //    // this.provinceCity.splice(1, 0, res.data.ProprietorInfoDetailVo.cityCode);
                        //     this.provinceCity=''
                        //  }else{
                        //     this.provinceCity.splice(1, 0, res.data.ProprietorInfoDetailVo.cityCode);
                        //  }
                        let newOptions = [];
                        let list = res.data.provinceList;
                        for (let i = 0; i < list.length; i++) {
                            let a = {
                                'value': list[i].provinceCode,
                                'label': list[i].province,
                                'children': []
                            }
                            newOptions.splice(i, 0, a);
                            for (let j = 0; j < list[i].cityList.length; j++) {
                                let b = {
                                    'value': list[i].cityList[j].cityCode,
                                    'label': list[i].cityList[j].city
                                }
                                newOptions[i].children.splice(j, 0, b);
                            }
                        }
                        ;
                        console.log()
                        if (this.formOptions.provinceCode) {
                            this.provinceCity.splice(0, 0, this.formOptions.provinceCode)
                            this.provinceCity.splice(1, 0, this.formOptions.cityCode)
                        } else {

                            this.provinceCity.splice(0, 0, '440000')
                            this.provinceCity.splice(1, 0, '440300')
                        }

                        this.provinceCityList = newOptions;
                        this.editFunc = this.formOptions.customerTypeValue && (this.formOptions.customerTypeValue === 1 || this.formOptions.customerTypeValue === 2)
                    })
            }
        },
        created() {
            this.getCustomerInfo(this.$route.query.id);
        }
    }
</script>

<style scoped lang="scss">
    .noimg {
        height: 220px;
        width: 370px;
        background: #f5f5f5;
        border: solid 1px #e8e8e8;
        text-align: center;
        line-height: 220px;
        font-size: 30px;
        color: #999;
    }

    .right {
        position: absolute;
        right: 32px;
        top: 15px;
    }

    .ownerInfoEdit {
        .header {
            width: 100%;
            height: 100px;
            background: white;
            .navtext {
                text-align: left;
                color: #999999;
                font-size: 14px;
                padding: 20px 0 0 32px;
            }
            .edittitle {
                text-align: left;
                font-size: 18px;
                color: #333333;
                padding: 20px 0 0 32px;
                position: relative;
                .right {
                    position: absolute;
                    right: 32px;
                    top: 15px;
                }
            }
        }
        .form {
            margin: 20px;
            background: white;
            padding: 10px 0;
            .el-input {
                width: 60%;
            }
            .form-item-right {
                width: 50%;
                display: inline-block;
                float: right;
                text-align: left;
                .area {
                    width: 60%;
                }
                .el-input, .el-select, .el-cascader {
                    width: 372px;
                }
            }
            .form-item-left {
                width: 50%;
                display: inline-block;
                .el-input, .el-select, .el-cascader {
                    width: 372px;
                }
                .neededtext {
                    text-align: left;
                    color: #999999;
                    font-size: 14px;
                    text-indent: 120px;
                    margin: 10px 0;
                    .star {
                        color: red;
                    }
                }
                .el-form-item {
                    text-align: left;
                }
            }
            .boxbottom {
                width: 90%;
                margin: 0 auto;
                border-top: 1px solid #e8e8e8;
                padding: 40px 0;
            }
        }
    }
</style>
