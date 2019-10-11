<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/packagelist/list' }">套餐列表</el-breadcrumb-item>
            <el-breadcrumb-item>成员账号</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="mainBodys">
            <el-form :inline="true" :model="formData" class="demo-form-inline">
              <el-form-item label="登录名">
                <el-input v-model="formData.loginName" clearable></el-input>
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="formData.mobile" clearable></el-input>
              </el-form-item>
              <el-form-item label="所属企业">
                <el-select v-model="formData.companyId" filterable clearable>
                    <el-option  v-for="item in companyList":key="item.id" :label="item.companyName" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="onSubmit" round >查询</el-button>
              </el-form-item>
              <el-form-item>
                <el-button @click="reSet" round >重置</el-button>
              </el-form-item>
            </el-form>
            <div class="lines"></div>
            <el-table :data="tableData" v-loading="loading" element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)"  style="width: 100%" align="center">
                <el-table-column label="序号" type="index" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="所属企业" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.companyName }}</template>
                </el-table-column>
                <el-table-column label="登录名" header-align="center" align="center">
                    <template slot-scope="scope"><span class="txts" @click="toLink('/packagelist/memberInfo',{serviceId:formData.serviceId,loginName:scope.row.loginName})">{{ scope.row.loginName }}</span></template>
                </el-table-column>        
                <el-table-column label="名称" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.nickName }}</template>
                </el-table-column>
                <el-table-column label="手机" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.mobile }}</template>
                </el-table-column>
                <el-table-column label="到期时间" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.expireTime }}</template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="formData.page"
                        :page-sizes="[10,20,30,50]"
                        :page-size="formData.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </page>
</template>

<script>
import {validateAlphone} from '@s/utils/validate'
import minixs from '@s/minixs/index';
export default {
  name: "packagelist",
  mixins:[minixs],
  data() {
    return {
      imgurl: process.env.sourceBaseUrl,
      formData:{
          loginName:'',
          mobile:'',
          companyId:'',
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
          this.formData.serviceId = this.$route.query.id;
          this.getListData();
          this.API.getCompanyList().then(res=>{
              this.companyList = res.obj
          })
      },
      onSubmit(){
          if(!this.formData.companyId&&!this.formData.loginName&&!this.formData.mobile){
              this.$message.warning('您没有输入查询内容哦！')
              return;
          }
          let mobile = this.formData.mobile;
          if(mobile&&!validateAlphone(mobile)){
              this.$message.warning('电话号码输入有误！')
              return;
          }
          this.formData.page = 1;
          this.getListData();
      },
      getListData(){
          this.loading = true
          let data = {};
          data.limit = this.formData.limit;
          data.serviceId = this.formData.serviceId;
          data.page = this.formData.page;
          this.formData.loginName?data.loginName = this.formData.loginName:'';
          this.formData.companyId?data.companyId = this.formData.companyId:'';
          this.formData.mobile?data.mobile = this.formData.mobile:'';
          this.API.getServicesUserList(data).then(res=>{
              this.loading = false;
              this.tableData = res.datalist;
              this.total = res.totalCount;
          })
      },
      reSet(){
            this.formData = {
                loginName:'',
                mobile:'',
                companyId:'',
                limit:10,
                serviceId:this.$route.query.id,
                page:1,
            },
          this.getListData();
      },
       // 分页模块
      handleSizeChange: function (size) {
          this.formData.limit = size
          this.getListData()
      },
      handleCurrentChange: function (currentPage) {
          this.formData.page = currentPage
          this.getListData()
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
    .txts{
        color: #409EFF;
        display: inline-block;
        width: 100%;
        cursor: pointer;
    }
}

</style>