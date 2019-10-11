<template>
    <page>
        <div class="mainBodys">
            <div class="titles">
                <el-button type="primary" v-if="permission('service.buy.add')" round @click="toLink('add')">新增</el-button>
                <!--<el-button type="primary" round @click="this.getListData">search</el-button>-->
            </div>
            <el-table :data="tableData" v-loading="loading" element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)"  style="width: 100%" align="center">
                <el-table-column label="序号" type="index" header-align="center" align="center"></el-table-column>
                <el-table-column label="企业编码" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.companyCode }}</template>
                </el-table-column>
                <el-table-column label="企业名称" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.companyName }}</template>
                </el-table-column>        
                <el-table-column label="企业类型" width="100px" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.companyType }}</template>
                </el-table-column>
                <el-table-column label="购买套餐" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.setmealName }}</template>
                </el-table-column>
                <el-table-column label="数量" width="80px" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.stemealNum|tofix }}</template>
                </el-table-column>
                <el-table-column label="金额" width="80px" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.setmealPrice|tofix }}</template>
                </el-table-column>
                <el-table-column label="操作人" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.modifier }}</template>
                </el-table-column>        
                <el-table-column label="备注" min-width="150px" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.remark }}</template>
                </el-table-column>
                <el-table-column label="购买时间" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.gmtCreate }}</template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" @click="exportAccounts(scope.row.id)" v-if="permission('service.buy.export')">导出账号</el-button>
                    </template>
                </el-table-column>
                
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="formData.pageNum"
                        :page-sizes="[10,20,30,50]"
                        :page-size="formData.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </page>
</template>

<script>
    import minixs from '@s/minixs/index';

    export default {
  name: "packagelist",
  mixins:[minixs],
  data() {
    return {
      imgurl: process.env.sourceBaseUrl,
      formData: {
        logName: "",
        phone: "",
        company: "",
        pageSize: 10,
        pageNum: 1
      },
        exportUrl: '',
      total: 0,
      loading: true,
      tableData: []
    };
  },
  created() {
    this.getListData();
  },
  methods: {
    onSubmit() {
      console.log(this.formData);
    },
      exportAccounts(id) {
          this.API.exportCompanyPurchaseAccounts(id)
              .then((result) => {
                  console.log(result)
                  if (result && result.size > 0) {
                      let url = window.URL.createObjectURL(result)
                      let link = document.createElement('a')
                      link.style.display = 'none'
                      link.href = url
                      link.setAttribute('download', '企业代购账号信息导出.xls')
                      document.body.appendChild(link)
                      link.click()
                  } else {
                      this.loadingTwo.close()
                      this.$message.error("没有数据需要导出!")
                  }
              })
          console.log(this.exportUrl)
      },
    getListData() {
        this.API.listCompanyPurchaseLog(this.formData)
            .then(res => {
                if (res.success) {
                    this.tableData = res.datalist;
                    this.total = res.totalCount;
                }
            })
      setTimeout(() => {
        this.loading = false;
      }, 1000);
    },
    // 分页模块
    handleSizeChange: function(size) {
      this.formData.pageSize = size;
      this.getListData();
    },
    handleCurrentChange: function(currentPage) {
      this.formData.pageNum = currentPage;
      this.loading = true;
      this.getListData();
    },

  }
};
</script>

<style scoped lang="scss">
.h20 {
  height: 20px;
}
.lines {
  border-bottom: 1px solid #eaeaea;
}
.mainBodys {
  width: 100%;
  position: relative;
  .pageFrame {
    margin-top: 20px;
    text-align: center;
  }
  .titles {
    margin-bottom: 20px;
    text-align: left;
  }
}
</style>