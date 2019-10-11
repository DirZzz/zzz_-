<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/userManage/mfrsManage' }">厂商管理</el-breadcrumb-item>
            <el-breadcrumb-item>详情</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="mfrs-content">
            <el-form :label-position="labelPosition" label-width="160px" :model="formParam">
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="企业名称:" prop="companyName">{{formParam.companyName}}</el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="所属行业:">{{industryTypeName}}</el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-form-item label="企业可见产品范围:" prop="selectTitle">{{formParam.selectTitle}}</el-form-item>
                </el-row>
                <el-row>
                    <el-form-item label="区域:">{{(formParam.provinceCodeName+formParam.cityCodeName+formParam.areaCodeName+formParam.streetCodeName)||''}}</el-form-item>
                </el-row>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="客服QQ:">{{formParam.companyCustomerQq}}</el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="合同号:">{{formParam.contractNumber}}</el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="企业网站:">{{formParam.companyUrl}}</el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="企业LOGO:">
                            <img v-if="uploadData.url" :src="uploadData.url" class="avatar logoImg">
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="联系人姓名:">{{formParam.contactName}}</el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="联系人电话:">{{formParam.phone}}</el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="经营范围:">{{formParam.businessScope}}</el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="企业标识:">{{formParam.companyIdentify}}</el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="企业详细地址:">{{formParam.companyAddress}}</el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="品牌网站:">{{formParam.companyDomainName}}</el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="方案公开提成率:">{{formParam.withdrawRate}}</el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="方案是否审核:">{{formParam.isExamine?'否':'是'}}</el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="开通小程序客户管理:">{{formParam.isManage?'否':'是'}}</el-form-item>
                <el-form-item label="企业介绍:">{{formParam.companyDesc}}</el-form-item>
                <el-form-item label="备注:">{{formParam.remark}}</el-form-item>
            </el-form>
        </div>
    </page>
</template>

<script>
import {validateAlphone,validateURL,validateUnmber} from '@s/utils/validate'
    export default {
        name: "mfrsAdd",
        data(){
            return{
                labelPosition: 'right',
                uploadData:{},
                industryTypeName:'',
                formParam: {
                    id:this.$route.query.id,
                    selectTitle:'',
                    companyIndustrys:'',// 所属行业
                    productVisibilityRange:'',//企业可见产品范围
                    companyName: '',//企业名称
                    companyCustomerQq: '',//客服QQ
                    provinceCode:'',
                    cityCode:'',
                    areaCode:'',
                    streetCode:'',
                    contactName:'',//联系人姓名
                    contractNumber:'',//合同号
                    contractEffectiveTime:'',//合同生效时间
                    companyUrl:'',//企业网站
                    phone:'',//联系电话
                    businessScope:'',//经营范围
                    companyIdentify:'',//企业标识
                    companyAddress:'',//企业地址
                    companyDomainName:'',//公司品牌网站域名
                    isManage:'0',// 是否开通小程序管理
                    withdrawRate:'',//  方案提成率:0-1之间的小数，小数位后最多5位
                    isExamine:'',//方案是否审核
                    companyDesc:'',//企业介绍
                    remark:'', //备注
                    companyLogo:'', //logo
                    businessType:1
                },
            }
        },
        created(){
            this.API.editComapnyInner({id:this.$route.query.id}).then(res=>{
                if(res.success){
                    for (let key in res.data){
                        this.formParam[key]=res.data[key];
                    }
                    this.uploadData.url=this.BASE_URL.sourceBaseUrl+res.data.companyLogoUrl;
                    this.formParam.isManage=res.data.isManage.toString();
                    let arr=[];
                    res.data.categoryListVOList.map((item,sun)=>{ arr.push(item.categoryName)});
                    this.formParam.selectTitle=arr.join(',');
                    
                    if(res.data.companyIndustrys){
                        let arr2 = res.data.companyIndustrys.split(',');
                        this.API.industryType({type:'industry'}).then(ress=>{
                            let arr = [];
                            ress.data.map(v=>{ arr2.map(h=>{ if(v.id==h) arr.push(v.name);}) })
                            this.industryTypeName = arr.join(',');
                        })
                    }
                }else {
                    this.$message.error(res.message);
                }
            })
        },
        methods:{
        }
    }
</script>

<style scoped lang="scss">
    .mfrs-content{
        margin-top: 20px;
    }
    .btn{
        text-align: center;
    }
    .logoImg{
        width: 68px;
        height: 68px;
        border-radius: 4px;
    }
</style>