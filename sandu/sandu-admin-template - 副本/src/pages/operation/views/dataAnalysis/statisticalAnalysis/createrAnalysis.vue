<template>
    <page class="pageStatistics">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-position='left' label-width="60px" class="demo-ruleForm formRow">
            <div class="pageStatistics-header">
                <el-button type="primary" round @click="toLink('/dataAnalysis/statisticalAnalysis')">返回</el-button>
                <!--<el-button type="primary" class="subBtn" round @click="submitForm('ruleForm')">保存</el-button>-->
            </div>
            <!--<el-form-item label="标题" prop="title">-->
                <!--<el-input v-model="ruleForm.title" :maxlength="50"></el-input>-->
            <!--</el-form-item>-->
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
                    <el-table-column label="编号" type="index" align="center">
                    </el-table-column>
                    <el-table-column prop="eventType" label="类型" header-align="center" align="center"></el-table-column>
                    <!--<el-table-column label="前置页面" header-align="center" align="center"></el-table-column>-->
                    <!--<el-table-column label="前置页面名称" header-align="center" align="center"></el-table-column>-->
                    <el-table-column prop="currentPage" label="当前页面" header-align="center" align="center"></el-table-column>
                    <el-table-column prop="currentPageName" label="当前页面名称" header-align="center" align="center"></el-table-column>
                    <el-table-column prop="buttonProperty" label="元素" header-align="center" align="center"></el-table-column>
                    <el-table-column prop="buttonName" label="元素名称" header-align="center" align="center"></el-table-column>
                    <!--<el-table-column label="点击次数" header-align="center" align="center"></el-table-column>-->
                    <!--<el-table-column label="点击人数" header-align="center" align="center"></el-table-column>-->
                    <!--<el-table-column label="操作" header-align="center" align="center">-->
                        <!--<template slot-scope="scope">-->
                            <!--<el-button type="success" round>上移</el-button><el-button type="warning" round>下移</el-button><el-button type="danger" round>移除</el-button>-->
                        <!--</template>-->
                    <!--</el-table-column>-->
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
                <!--<div class="addRow">-->
                    <!--<el-button type="primary" @click="addData" icon="el-icon-circle-plus">添加</el-button>-->
                <!--</div>-->
            </div>
        </el-form>
        <!--<div class="zzc" v-if="showZzc">-->
            <!--<div class="zzcRows">-->
                <!--<i class="el-icon-circle-close close" @click="addData"></i>-->
                <!--<el-tabs type="border-card" v-model="activeName" @tab-click="handleClick">-->
                    <!--<el-tab-pane label="添加页面" name="page">-->
                        <!--<el-table-->
                            <!--:data="tableData2"-->
                            <!--v-loading="loading2"-->
                            <!--@selection-change="handleSelectionChange2"-->
                            <!--element-loading-text="拼命加载中"-->
                            <!--element-loading-spinner="el-icon-loading"-->
                            <!--element-loading-background="rgba(255, 255, 255, 0.8)"-->
                            <!--style="width: 100%"-->
                            <!--align="center"-->
                        <!--&gt;-->
                            <!--<el-table-column label="编号" type="index" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="前置页面" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="前置页面名称" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="当前页面" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="当前页面名称" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="操作" header-align="center" align="center">-->
                                <!--<template slot-scope="scope">-->
                                    <!--<el-button type="success" round>添加</el-button><el-button type="danger" round>移除</el-button>-->
                                <!--</template>-->
                            <!--</el-table-column>-->
                        <!--</el-table>-->
                    <!--</el-tab-pane>-->
                    <!--<el-tab-pane label="添加元素" name="element">-->
                        <!--<el-table-->
                            <!--:data="tableData2"-->
                            <!--v-loading="loading2"-->
                            <!--@selection-change="handleSelectionChange2"-->
                            <!--element-loading-text="拼命加载中"-->
                            <!--element-loading-spinner="el-icon-loading"-->
                            <!--element-loading-background="rgba(255, 255, 255, 0.8)"-->
                            <!--style="width: 100%"-->
                            <!--align="center"-->
                        <!--&gt;-->
                            <!--<el-table-column label="编号" type="index" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="前置页面" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="前置页面名称" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="当前页面" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="当前页面名称" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="元素" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="元素名称" header-align="center" align="center">-->
                            <!--</el-table-column>-->
                            <!--<el-table-column label="操作" header-align="center" align="center">-->
                                <!--<template slot-scope="scope">-->
                                    <!--<el-button type="success" round>添加</el-button><el-button type="danger" round>移除</el-button>-->
                                <!--</template>-->
                            <!--</el-table-column>-->
                        <!--</el-table>-->
                    <!--</el-tab-pane>-->
                <!--</el-tabs>-->
            <!--</div>-->
        <!--</div>-->
    </page>
</template>

<script>
    export default {
        name: "elementStatistics",
        data(){
            return{
                ruleForm:{},
                tableData:[],
                showZzc:false,
                addType:0,
                activeName:'page',
                rules:{
                    title: [
                        { required: true, message: '请输入标题', trigger: 'blur' },
                        { required: true, message: '请输入标题', trigger: 'change' },
                    ],
                },
                tableData2:[],
                loading2:false,
                loading:false,
                currentPage:1,
                limit:10,
                total:0
            }
        },
        created(){
            this.pageList();
        },
        methods:{
            pageList(){
                this.loading=true;
                this.API.statisticViewDetail({
                    page:this.currentPage,
                    limit:this.limit,
                    funnelId:this.$route.query.id
                }).then(res=>{
                    if(res.success){
                        this.loading=false;
                        this.tableData=res.datalist;
                        this.total=res.totalCount;
                    }else {
                        this.$message(res.message);
                    }
                })
            },
            handleSizeChange(val){
                this.limit=val;
                this.pageList();
            },
            handleCurrentChange(val){
                this.currentPage=val;
                this.pageList();
            },
            toLink(path,query){
                this.$router.push({path:path,query:query});
            }
        }
    }
</script>

<style scoped lang="scss">
.demo-ruleForm{
    .el-form-item{
        width: 400px;
    }
    .pageStatistics-header{
        height: 40px;
        margin-bottom: 30px;
        button{
            float: left;
        }
        .subBtn{
            float: right;
        }
    }
    .addRow{
        width: 100%;
        height: 40px;
        margin-top: 10px;
    }
    .pageStatistics-list{
        margin: 10px auto;
    }
    .pagination{
        text-align: right;
        margin-top: 20px;
    }
}
.page{
    position: relative;
}
.zzc{
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 66;
    background: rgba(0,0,0,0.7);
    .zzcRows::-webkit-scrollbar{
        width: 0;
    }
    .zzcRows{
        position: absolute;
        width: 80%;
        left: 10%;
        top: 10%;
        max-height: 600px;
        overflow-y: scroll;
        border-radius: 6px;
        .close{
            position: absolute;
            right: 10px;
            top: 10px;
            font-size: 20px;
            cursor: pointer;
            z-index: 667;
        }
    }
}

</style>