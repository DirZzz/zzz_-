<template>
    <div class="mains">
        <div class="mainBody">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/packagelist/list' }">套餐列表</el-breadcrumb-item>
                <el-breadcrumb-item>企业价格设置列表</el-breadcrumb-item>
            </el-breadcrumb>
            <div class="h20"></div>
            <el-form :inline="true" :model="formData" class="demo-form-inline">
              <el-form-item label="企业编码">
                <el-input v-model="formData.companyCode" placeholder="企业编码"></el-input>
              </el-form-item>
              <el-form-item label="企业名称">
                <el-input v-model="formData.companyName" placeholder="企业名称"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="onSubmit" round>查询</el-button>
              </el-form-item>
            </el-form>
            <div class="lines"></div>
            <div class="btns" v-if="permission('service.pricesetting.add')"><el-button type="primary" round @click="toLink2('setPrice')">设置企业价格</el-button></div>
            <div class="tableList">
                <el-table :header-cell-style="rowStyle" :data="tableData" v-loading="loading" element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)"  style="width: 100%" align="center">
                    <el-table-column label="序号" type="index" header-align="center" align="center">
                    </el-table-column>
                    <el-table-column label="企业编码" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.companyCode }}</template>
                    </el-table-column>
                    <el-table-column label="企业名称" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.companyName }}</template>
                    </el-table-column>        
                    <el-table-column label="企业价格内容" width="500px" header-align="center" align="center">
                        <template slot-scope="scope">
                        <div v-for="v in scope.row.prices">
                            {{v.price}}/{{v.duration}}{{v.priceUnit=="0"?"年":(v.priceUnit=="1"?"月":"天")}} (赠送天数：{{v.giveDuration}},赠送度币：{{v.sanduCurrency}},免费渲染时长：{{v.freeRenderDuration}}个月)
                        </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="200px" align="center">
                        <template slot-scope="scope">
                            <el-button type="text" @click="toLink2('editPrice',scope.row.companyId)"  v-if="permission('service.pricesetting.update')">修改</el-button>
                            <el-button type="text" @click="deleteCompanyPrice(scope.row.companyId)" v-if="permission('service.pricesetting.del')">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!--分页组件-->
                <div class="pageFrame">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page.sync="formData.start"
                            :page-sizes="[10,20,30,50]"
                            :page-size="formData.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="total">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  name: "packagelist",
  data() {
    return {
      imgurl: process.env.sourceBaseUrl,
      tableData:[],
      total: 0,
      loading: true,
      formData:{
        companyCode:'',
        id:'',
        companyName:'',
        start:1,
        limit:10,
      }
    };
  },
  created() {
      this.init();
    
  },
  methods: {
      init(){
          this.formData.id = this.$route.query.id;
          this.getListData();
      },
      rowStyle({ row, rowIndex}) {
          if (rowIndex === 0) {
              return{height: '60px!important',background:'#f5f5f5',color: '#666'}
          }
      },
      getListData(){
          this.API.getCompanyPriceList(this.formData).then((res) => {
              this.loading = false;
              this.total = res.totalCount;
              this.tableData = res.datalist;
          });
      },
      deleteCompanyPrice(cId){
            this.$confirm('确定删除该数据?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true
            }).then(() => {
                this.loading = true;
                this.API.deleteCompanyPrice({companyId:cId,servicesId:this.formData.id}).then(res=>{
                    this.loading = false;
                    res.success?this.$message.success('删除成功！'):this.$message.error('删除失败！');
                    this.getListData();
                })
            }).catch(() => {
                this.$message({type: 'info', message: '已取消删除'});
            });
          
      },
      toLink2(url,cid,name){
          let query = {id: this.formData.id}
          cid?query.companyId = cid:'';
          this.$router.push({path: url, query: query});
          
      },
      onSubmit(){
        //   if(!this.formData.companyCode&&!this.formData.companyName){
        //       this.$message.warning('您没有输入查询内容哦！')
        //       return;
        //   }
          this.loading = true;
          this.formData.start = 1;
          this.getListData();
      },
      // 分页模块
      handleSizeChange: function (size) {
          this.formData.limit = size
          this.getListData()
      },
      handleCurrentChange: function (currentPage) {
          this.formData.start = currentPage
          this.loading = true
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
.mains {
  width: 100%;
  height: 100%;
  padding: 20px;
  padding-right: 15px;
  overflow: hidden;
  .mainBody {
    overflow-y: scroll;
    background: #fff;
    width: 100%;
    height: 100%;
    padding: 20px;
    text-align: left;
    .btns{
      margin: 20px auto;
    }
    .pageFrame{
      margin-top: 20px;
      text-align: center;
    }
  }
}
</style>