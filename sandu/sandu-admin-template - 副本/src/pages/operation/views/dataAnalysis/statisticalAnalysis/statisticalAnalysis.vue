<template>
    <page class="pageStatistics">
        <!--<div class="pageStatistics-header">-->
            <!--<el-button type="primary" round @click="toLink('/dataAnalysis/statisticalAnalysis/createrAnalysis')">创建</el-button>-->
        <!--</div>-->
        <div class="pageStatistics-header">
            <div class="pageStatistics-item">
                <span>小程序：</span>
                <el-select v-model="appletType" clearable placeholder="请选择" @change="userAppletTypeChange">
                    <el-option 
                        v-for="item in userAppletTypeOrigin"
                        :key="item.type"
                        :label="item.name"
                        :value="item.name"
                    >   
                    </el-option>
                </el-select>
            </div>
        </div>
        <div class="pageStatistics-list">
            <el-table
                    :data="tableData"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%"
                    align="center"
            >
                <el-table-column label="编号" type="index" :index="indexMeds" align="center">
                </el-table-column>
                <el-table-column label="标题" header-align="center" align="center" prop="funnelName">
                </el-table-column>
                <el-table-column label="创建时间" header-align="center" align="center" prop="gmtCreateInfo">
                </el-table-column>
                <el-table-column label="操作" header-align="center" align="center" prop="id">
                    <template slot-scope="scope">
                        <el-button type="success" round size="mini" @click="toLink('/dataAnalysis/statisticalAnalysis/analysisInfo',{id:scope.row.id})">查看</el-button>
                        <el-button size="mini" type="warning" @click="toLink('/dataAnalysis/statisticalAnalysis/createrAnalysis',{id:scope.row.id})" round>详情</el-button>
                        <el-button size="mini" type="danger" round @click="delFun(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="currentPage"
                        :page-size="limit"
                        :page-sizes="[10, 20, 30, 40, 50]"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </page>
</template>

<script>
    export default {
        name: "elementStatistics",
        data(){
            return{
                currentPage:1,
                limit:10,
                radio:'1',
                total:0,
                pickTime:'',
                query:{
                    size:10,
                    page:1,
                },
                tableData:[],
                userSource:'',
                userOrigin:[],
                loading:false,
                appletType:'随选网',
                userAppletTypeOrigin:[
                    { name:'随选网',type:'wx42e6b214e6cdaed3'},
                    { name:'随选网精准装修报价',type:'wxf1113bc672fe7112'},
                ],
            }
        },
        created(){
            this.pageList();
            
        },
        methods:{
            pageList(){
                this.loading=true;
                this.API.statisticViewList({
                    appId:this.appletType==='随选网'?'wx42e6b214e6cdaed3':'wxf1113bc672fe7112',
                    page:this.currentPage,
                    limit:this.limit
                }).then(res=>{
                    if(res.success){
                        this.loading=false;
                        this.tableData=res.datalist;
                        this.total=res.totalCount;
                        //console.log(this.option.xAxis.data)
                    }else {
                        this.$message(res.message);
                    }
                })
            },
            indexMeds(index){
                return (this.currentPage-1)*this.limit+index+1
            },
            handleSizeChange(val){
                this.limit = val;
                this.pageList();
            },
            userAppletTypeChange(){
                this.pageList();
            },
            handleCurrentChange(val){
                this.currentPage = val;
                this.pageList();
            },
            toLink(path,query){
                this.$router.push({path:path,query:query});
            },
            delFun(id){
                this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.statisticViewDelete({id:id}).then(res=>{
                        if(res.success){
                            this.$message.success('删除成功');
                            this.pageList();
                        }else {
                            this.$message.error(res.message)
                        }
                    })
                })
            },
        }
    }
</script>

<style scoped lang="scss">
    .pageStatistics{
        .pageStatistics-header{
            display: flex;
            margin-bottom: 30px;
        }
        .pagination{
            text-align: right;
            margin-top: 20px;
        }
    }
</style>