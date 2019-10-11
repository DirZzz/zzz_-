<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/userManage/companyList' }">公司列表</el-breadcrumb-item>
            <el-breadcrumb-item>详情</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="company-content">
            <el-form :label-position="labelPosition" label-width="160px" :model="formParam"  ref="ruleForm">
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="企业编码:">
                            {{formParam.companyCode}}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="8">
                        <el-form-item label="企业名称:">
                            {{formParam.companyName}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="客服QQ:">
                            {{formParam.companyCustomerQq}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-form-item label="区域:">
                        {{(formParam.provinceCodeName+formParam.cityCodeName+formParam.areaCodeName+formParam.streetCodeName)||''}}
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="合同号:">
                            {{formParam.contractNumber}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="合同生效时间:">
                            {{formParam.contractEffectiveTime}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="企业网站:">
                            {{formParam.companyUrl}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="企业LOGO:">
                            <img :src="uploadData.url" class="avatar" v-if="uploadData.url">
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="联系人姓名:">
                            {{formParam.contactName}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="联系人电话:">
                            {{formParam.phone}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="经营范围:">
                            {{formParam.businessScope}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="企业标识:">
                            {{formParam.companyIdentify}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="企业详细地址:">
                            {{formParam.companyAddress}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="品牌网站:">
                            {{formParam.companyAddress}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="方案公开提成率:">
                            {{formParam.withdrawRate}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="方案是否审核:">
                            {{formParam.isExamine?'否':'是'}}
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="公司类型:">
                            {{formParam.businessTypeName}}
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="合作企业:">
                            {{formParam.cooperationCompany}}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="企业介绍:">
                    {{formParam.companyDesc}}
                </el-form-item>
                <el-form-item label="备注:">
                    {{formParam.remark}}
                </el-form-item>
            </el-form>
        </div>

    </page>
</template>

<script>
import {validateAlphone,validateURL,validateUnmber} from '@s/utils/validate'
    export default {
        name: "companyAdd",
        data(){
            return{
                labelPosition: 'right',
                uploadData:{},
                businessType:'',
                formParam: {
                    id:this.$route.query.id,
                    companyName: '',//企业名称
                    companyCustomerQq: '',//客服QQ
                    provinceCode:'',
                    cityCode:'',
                    areaCode:'',
                    streetCode:'',
                    contractNumber:'',//合同号
                    contractEffectiveTime:'',//合同生效时间
                    companyUrl:'',//企业网站
                    phone:'',//联系电话
                    businessScope:'',//经营范围
                    companyIdentify:'',//企业标识
                    companyAddress:'',//企业地址
                    companyDomainName:'',//公司品牌网站域名
                    isManage:'',// 是否开通小程序管理
                    withdrawRate:'',//  方案提成率:0-1之间的小数，小数位后最多5位
                    isExamine:'',//方案是否审核
                    companyDesc:'',//企业介绍
                    remark:'', //备注
                    companyLogo:'', //logo
                    businessType:''
                },
            }
        },
        created(){
           this.API.editComapnyInner({id:this.$route.query.id}).then(res=>{
               if(res.success){
                   for (let key in res.data){
                       this.formParam[key]=res.data[key];
                   }
                   this.uploadData.url=res.data.companyLogoUrl?(this.BASE_URL.sourceBaseUrl+res.data.companyLogoUrl):'';

               }else {
                   this.$message.error(res.message);
               }
           })
        },
        methods:{
            successUpload(res){
                this.uploadData=res.data;
                this.formParam.companyLogo=res.data.resId;
            },
        }
    }
</script>

<style scoped lang="scss">
    .company-content{
        margin-top: 20px;
    }
    .btn{
        text-align: center;
    }
</style>