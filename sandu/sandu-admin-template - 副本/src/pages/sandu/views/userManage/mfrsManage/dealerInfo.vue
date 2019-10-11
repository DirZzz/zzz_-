<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/userManage/mfrsManage' }">厂商管理</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/userManage/mfrsManage/dealerManage/'+formLabelAlign.pid }">经销商企业</el-breadcrumb-item>
            <el-breadcrumb-item>详情</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="dealerAdd-content disabledTxt">
            <el-form :label-position="labelPosition" ref="form" label-width="240px" :model="formLabelAlign">
                
                <el-row>
                    <el-col :span="10">
                        <el-form-item label="经销商名称:">
                            <el-input clearable v-model="formLabelAlign.companyName" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item label="所属品牌:">
                            <el-select v-model="brandIds" placeholder="" @change="brandIdsFunc" disabled>
                                <el-option
                                        v-for="item in cabrand"
                                        :key="item.brandId"
                                        :label="item.brandName"
                                        :value="item.brandId">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="10">
                        <el-form-item label="所属行业:">
                            {{formLabelAlign.industryNames}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="企业可见产品范围:">
                            <p class="moreThree">{{produceRangeTitle}}</p>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="10">
                        <el-form-item label="联系人姓名:">
                            <el-input clearable v-model="formLabelAlign.contactName" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item label="联系电话:">
                            <el-input clearable v-model="formLabelAlign.phone" :maxlength="11" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-form-item label="区域:">
                          {{formLabelAlign.companyArea}}
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-col :span="10">
                        <el-form-item label="经营范围:">
                            <el-input clearable v-model="formLabelAlign.businessScope" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item label="邮箱:">
                            <el-input clearable v-model="formLabelAlign.email" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="10">
                        <el-form-item label="企业详细地址:">
                            <el-input clearable v-model="formLabelAlign.companyAddress" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item label="厂商授权经销商可开通移动端数量:">
                            <el-input clearable v-model="formLabelAlign.mobileCount" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="10">
                        <el-form-item label="厂商授权经销商可开通PC端数量:">
                            <el-input clearable v-model="formLabelAlign.pcCount" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item label="方案是否审核:">
                            <el-select v-model="formLabelAlign.isExamine" placeholder="" disabled>
                                <el-option
                                        v-for="item in isExamine"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="企业介绍:" v-if="formLabelAlign.companyDesc">
                    <el-input
                            type="textarea"
                            :rows="4"
                            disabled
                            placeholder=""
                            
                            v-model="formLabelAlign.companyDesc">
                    </el-input>
                </el-form-item>
                <el-form-item label="备注:" v-if="formLabelAlign.remark">
                    <el-input
                            type="textarea"
                            :rows="4"
                            disabled
                            placeholder=""
                            v-model="formLabelAlign.remark">
                    </el-input>
                </el-form-item>
            </el-form>
        </div>
    </page>
</template>

<script>
    import minixs from '@s/minixs/index'
    import {validateAlphone,validateEmail,validateUnmber} from '@s/utils/validate'
    
    export default {
        name: "dealerAdd",
        mixins:[minixs],
        data(){
            return{
                labelPosition: 'right',
                radio:'1',
                textarea:'',
                options:[],
                cabrand:[],
                produceRangeTitle:'',
                isExamine:[{
                    label:'否',
                    value: 0
                },
                {
                    label:'是',
                    value: 1
                }],
                brandIds:'',
                value:'',
                formLabelAlign: {}
            }
        },
        created(){
            this.init();
        },
        methods: {
            init(){
                this.formLabelAlign.pid  = this.$route.query.pid;
                this.formLabelAlign.id  = this.$route.query.id;
                this.getCabrand();
                this.getDealer();
            },
            getDealer(){
                this.API.getDealer({companyId:this.$route.query.id}).then(res=>{
                    if(res.success){
                        this.formLabelAlign = res.obj
                        this.formLabelAlign.pid  = this.$route.query.pid;
                        this.formLabelAlign.id  = this.$route.query.id;
                        this.$refs.belongIndustry.echoData(res.obj.companyIndustrys);
                        this.$refs.areaSelect.echoData(res.obj.provinceCode,res.obj.cityCode,res.obj.areaCode,res.obj.streetCode);
                        this.formLabelAlign.productVisibilityRange = res.obj.productVisibilityRange.join(',');
                        this.formLabelAlign.companyIndustrys = res.obj.companyIndustrys.join(',');
                        this.brandIds = res.obj.brandIds[0];
                        this.produceRangeTitle = res.obj.productVisibilityRangeNames;
                    }
                })
            },
            getCabrand(){
                this.API.cabrand({companyId: this.$route.query.pid}).then(res=>{
                    if(res.success){
                        this.cabrand = res.datalist
                    }
                })
            },
            provinceChange(code){
                this.formLabelAlign.provinceCode=code;
            },
            cityChange(code){
                this.formLabelAlign.cityCode=code;
            },
            areaChange(code){
                this.formLabelAlign.areaCode=code;
            },
            streetChange(code){
                this.formLabelAlign.streetCode=code;
            },
            industryChange(obj){
                this.produceRangeTitle = obj.range.categoryNames;
                this.formLabelAlign.productVisibilityRange = obj.range.categoryIds.join(',');
                this.formLabelAlign.companyIndustrys = obj.industry.join(',');
            },
            produceRange(obj){
                this.produceRangeTitle = obj.range.categoryNames;
                this.formLabelAlign.productVisibilityRange = obj.range.categoryIds.join(',');
            },
            brandIdsFunc(code){
                this.formLabelAlign.brandIds = code;
            },
        },
    }
</script>

<style scoped lang="scss">
    .dealerAdd-content{
        margin-top: 20px;
    }
    .btn{
        text-align: center;
    }
    .moreThree{
        width: 100%;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 3;
        line-height: 1.5;
        color: #494949;
        overflow: hidden;
    }
</style>