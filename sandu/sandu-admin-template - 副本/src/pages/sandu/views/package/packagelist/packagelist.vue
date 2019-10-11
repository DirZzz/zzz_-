<template>
    <div class="mains">
        <div class="mainBody">
            <div class="titles">
                <el-button round type="primary" @click="toLink('meal')" v-if="permission('service.manage.add')">新增</el-button>
            </div>
            <div class="tableList">
                
                <el-table
                        :header-cell-style="rowStyle"
                        :data="tableData"
                        v-loading="loading"
                        element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(255, 255, 255, 0.8)"
                        style="width: 100%"
                        align="center"
                        >
                    <el-table-column label="序号" type="index" header-align="center" align="center">
                    </el-table-column>
                    <el-table-column label="套餐编号" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.servicesCode }}</template>
                    </el-table-column>
                    <el-table-column label="套餐名称" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.servicesName }}</template>
                    </el-table-column>        
                    <el-table-column label="套餐描述" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.serviceDesc }}</template>
                    </el-table-column>

                    <el-table-column label="正式/试用" width="80px" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.servicesType?"试用":"正式" }}</template>
                    </el-table-column>
                    <el-table-column label="用户类型"  width="80px" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.scopeName }}</template>
                    </el-table-column>        
                    <el-table-column label="销售渠道" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.saleNames }}</template>
                    </el-table-column>

                    <el-table-column label="套餐状态" width="80px" header-align="center" align="center">
                        <template slot-scope="scope">{{ scope.row.servicesStatus=='1'?"启用":"禁用" }}</template>
                    </el-table-column>

                    <el-table-column label="操作" width="400px" align="center">
                        <template slot-scope="scope">
                            <el-button type="text" v-if="permission('service.manage.edit')" @click="toLink('editMeal',{id:scope.row.id})">编辑</el-button>
                            <el-button type="text" @click="toLink('relation',{id:scope.row.id})">关联功能</el-button>
                            <el-button type="text" v-if="!scope.row.servicesType&&permission('service.pricesetting.setting')" @click="toLink('setPriceList',{id:scope.row.id})">企业价格设置</el-button>
                            <el-button type="text" @click="toLink('member',{id:scope.row.id})" v-if="permission('service.manage.account')">成员账号</el-button>
                            <el-button type="text" @click="changeSetMealStatus(scope.row.id,scope.row.servicesStatus)" v-if="scope.row.servicesStatus=='1'&&permission('service.manage.edit')">禁用</el-button>
                            <el-button type="text" @click="changeSetMealStatus(scope.row.id,scope.row.servicesStatus)" v-if="scope.row.servicesStatus=='0'&&permission('service.manage.edit')">启用</el-button>
                            <el-button type="text" @click="deleteMeal(scope.row.id)" v-if="permission('service.manage.del')">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!--分页组件-->
                <div class="pageFrame">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page.sync="query.page"
                            :page-sizes="[10,20,30,50]"
                            :page-size="query.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="total">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import minixs from '@s/minixs/index';
export default {
    name: "packagelist",
    mixins:[minixs],
    data() {
        return {
            imgurl:process.env.sourceBaseUrl,
            total: 0, // 总页数
            tableData:[],
            loading: true,
            query: {
                page: 1,
                limit: 10,
            }
        }
    },
    activated() {
        this.getListData();
    },
    methods: {
        rowStyle({ row, rowIndex}) {
            if (rowIndex === 0) {
                return{height: '60px!important',background:'#f5f5f5',color: '#666'}
            }
        },
        getListData(){
            let data = {
              start: this.query.page,
              limit: this.query.limit,
          };
          
          this.API.setMealList(data).then((res)=>{
              if(res.success){
                  this.tableData = res.datalist;
                  this.total = res.totalCount;
                  this.loading = false;
              }
          })
        },
        deleteMeal(id){
            this.$confirm('确定删除该数据?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true
            }).then(() => {
                this.loading = true;
                this.API.deleteMeal({id:id,creator: sessionStorage.getItem('userId')}).then(res=>{
                    this.resFlag(res.success,'删除');
                })
            }).catch(() => {
                this.$message({type: 'info', message: '已取消删除'});
            });
            
        },
        changeSetMealStatus(id,type){
            this.loading = true;
            this.API.changeSetMealStatus(id,type).then(res=>{ this.resFlag(res.success,'操作');})
        },
        resFlag(flag,txt){
            if(flag){
                this.getListData();
                this.$message.success(txt+'成功！')
            }else{
                this.loading = false;
                this.$message.error(txt+'失败');
            }
        },
        // 分页模块
        handleSizeChange: function (size) {
            this.query.limit = size
            this.getListData()
        },
        handleCurrentChange: function (currentPage) {
            this.query.page = currentPage
            this.loading = true
            this.getListData()
        }
    } //end methods
}
</script>

<style scoped lang="scss">
.mains{
    width: 100%;
    height: 100%;
    padding: 20px;
    overflow: hidden;
    .mainBody{
        overflow-y: scroll;
        background: #fff;
        width: 100%;
        height: 100%;
        padding: 20px;
        padding-top: 80px;
        position: relative;
        .titles{
            text-align: left;
            position: absolute;
            top: 20px;
            left: 0;
            width: 100%;
            padding: 0 20px;
        }
        .tableList{
            width: 100%;
            height: 100%;
            .pageFrame{
                margin-top: 20px;
            }
        }
    }
    
}
</style>