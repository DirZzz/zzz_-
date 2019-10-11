<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/packagelist/list' }">套餐列表</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/packagelist/member?id='+formData.serviceId }">成员账号</el-breadcrumb-item>
            <el-breadcrumb-item>成员账号详情</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="mainBodys">
            <el-form status-icon label-width="300px" class="demo-ruleForm">
                <el-form-item label="所属企业：">{{tableData.companyName}}</el-form-item>
                <el-form-item label="登录名：">{{tableData.loginName}}</el-form-item>
                <el-form-item label="名称：">{{tableData.nickName}}</el-form-item>
                <el-form-item label="手机：">{{tableData.mobile}}</el-form-item>
                <el-form-item label="到期时间：">{{tableData.expireTime}}</el-form-item>
            </el-form>
        </div>
    </page>
</template>

<script>
import {validateAlphone} from '@s/utils/validate'
export default {
  name: "packagelist",
  data() {
    return {
      imgurl: process.env.sourceBaseUrl,
      formData:{
          loginName:'',
          limit:10,
          serviceId:'',
          page:1,
      },
      companyList:[],
      total: 0,
      loading: false,
      tableData:[],
    };
  },
  created() {
      this.init();
  },
  methods: {
      init(){
          this.formData.serviceId = this.$route.query.serviceId;
          this.formData.loginName = this.$route.query.loginName;
          this.getListData();
      },
      onSubmit(){
          this.getListData();
      },
      getListData(){
          this.API.getServicesUserList(this.formData).then(res=>{
              this.tableData = res.datalist[0];
          })
      },
  } 
};
</script>

<style scoped lang="scss">
.h20{
  height: 20px;
}
.lines{
  border-bottom: 1px solid #eaeaea;
}
.mainBodys {
    width: 100%;
    padding: 20px 0 0;
    position: relative;
    .pageFrame{
      margin-top: 20px;
      text-align: center;
    }
}

</style>