<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item v-if="!userMethodType" :to="{ path: '/userManage/userList' }">用户列表</el-breadcrumb-item>
            <el-breadcrumb-item v-if="userMethodType=='vendor'" :to="{ path: '/userManage/mfrsManage' }">厂商列表</el-breadcrumb-item>
            <el-breadcrumb-item v-if="userMethodType=='franchiser'" :to="{ path: '/userManage/mfrsManage' }">厂商列表</el-breadcrumb-item>
            <el-breadcrumb-item v-if="userMethodType=='company'" :to="{ path: '/userManage/companyList' }">公司列表</el-breadcrumb-item>
            <el-breadcrumb-item v-if="userMethodType=='vendor'" :to="{ path: '/userManage/mfrsManage/userManage?id='+userId+'&businessType='+businessType+'&tabType=first'+'&page='+page }">用户管理</el-breadcrumb-item>
            <el-breadcrumb-item v-if="userMethodType=='franchiser'" :to="{ path: '/userManage/mfrsManage/userManage?id='+userId+'&businessType='+businessType+'&tabType=second'+'&page='+page }">用户管理</el-breadcrumb-item>
            <el-breadcrumb-item v-if="userMethodType=='company'" :to="{ path: '/userManage/companyList/userManage?id='+userId+'&businessType='+businessType+'&page='+page }">用户管理</el-breadcrumb-item>
            <el-breadcrumb-item>详情</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="edit-content disabledTxt">
            <el-form :label-position="labelPosition" label-width="160px" :model="formParam">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="登录名：">{{formParam.nickName}}</el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="手机号：">{{formParam.mobile}}</el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="昵称：">{{formParam.userName}}</el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="用户类型：">{{userTypeName}}</el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="邮箱：">{{formParam.email}}</el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="用户来源：">{{userSourceName}}</el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="区域：">{{addressName||''}}</el-form-item>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="首次登录时间:">{{formParam.firstLoginTime}}</el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="到期时间:">{{formParam.failureTime}}</el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="设置套餐:">{{serviceInfo.servicesName}}</el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="有效时长:">{{vaildTime}}</el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="备注：">{{formParam.remark}}</el-form-item>
                <el-form-item label="暂停使用时间：">{{formParam.accountFreezeTime}}</el-form-item>
                <el-form-item label="开启使用时间：">{{formParam.accountEnableTime}}</el-form-item>
            </el-form>
        </div>
        <packageEdit ref="packageEdit"></packageEdit>
    </page>
</template>

<script>
import {validateAlphone,validateEmail} from '@s/utils/validate'
    import md5 from 'js-md5';
    import packageEdit from '@s/components/userList/packageEdit'
    import minixs from '@s/minixs/index'
    export default {
        name: "listEdit",
        mixins:[minixs],
        components:{
            packageEdit
        },
        data(){
            return{
                input:'',
                userId:'',
                userMethodType:'',
                businessType:'',
                addressName:'',
                userTypeName:'',
                userSourceName:'',
                labelPosition: 'right',
                vaildTime:'',
                page:1,
                formParam: {}
            }
        },
        created(){
            this.page = this.$route.query.page;
            this.userMethodType = this.userMethod
            this.userId = this.$route.query.pId
            this.businessType = this.$route.query.businessType?this.$route.query.businessType:'';
            this.API.editUserInfo({userId:this.$route.query.id}).then(res=>{
                 if(res.success){
                     this.formParam = res.obj.userInfo;
                     this.userTypeName = res.obj.userTypeName;
                     this.userSourceName = res.obj.userResourceName;
                     this.addressName = res.obj.addressName;
                     this.vaildTime = res.obj.vaildTime
                     if(res.obj.serviceInfo){
                         this.setServiceInfo(res.obj.serviceInfo);
                     }
                 }
            })
        },
        methods:{
           userOriginChange(val){
                this.formParam.userSource=val;
            },
        }
    }
</script>

<style scoped lang="scss">
   .edit-content{
       margin-top: 30px;
       text-align: left;
   }
    .btn{
        margin-top: 40px;
        text-align: center;
    }
    .textarea{
        width: 610px;
        vertical-align: top;
    }
</style>