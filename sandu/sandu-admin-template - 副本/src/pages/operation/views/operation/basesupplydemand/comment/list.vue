<template>
    <div class="comment">
        <div class="nav">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/basesupplydemand/list' }">供求信息管理</el-breadcrumb-item>
                <el-breadcrumb-item>评论管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">
                <el-input v-model="params.content" placeholder="请输入评论" clearable></el-input>
                <el-date-picker
                        @change="pickChange"
                        v-model="pickTime"
                        type="datetimerange"
                        range-separator="至"
                        value-format="yyyy-MM-dd:HH-mm-ss"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
            </el-row>
        </div>
        <div class="btnFrame clearfix">

            <p>
                <el-button round type="primary" size="small" @click="batchDelete" class="batchDelete">批量删除</el-button>
                <el-button round type="primary" size="small" @click="search">搜索</el-button>
                <el-button round  size="small" @click="reset">重置</el-button>
            </p>

        </div>
        <div class="tableList">
            <el-table
                    :header-cell-style="rowStyle"
                    :data="tableData"
                    v-loading="loading"
                    @selection-change="handleSelectionChange"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%"
                    align="center"
            >
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column label="序号" type="index" align="center"></el-table-column>

                <el-table-column label="评论内容" align="center">
                    <template slot-scope="scope">
                        <span class="commentName" @click="$router.push({path:'/basesupplydemand/comment/detail',query:{reviewsId:scope.row.id,businessId:$route.query.businessId}})">{{scope.row.reviewsMsg}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="发布者" prop="creator" align="center"></el-table-column>
                <el-table-column label="评论时间" prop="gmtCreate" align="center"></el-table-column>

                <el-table-column label="点赞数" prop="likeCount" align="center"></el-table-column>
                <el-table-column
                        label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" @click="$router.push({path:'/basesupplydemand/comment/edit',query:{reviewsId:scope.row.id,businessId:$route.query.businessId}})">编辑</el-button>
                        <el-button type="text" @click="toTop(scope.row)">{{scope.row.isTop==0 ? '置顶' : '取消置顶'}}</el-button>
                        <el-button type="text" @click="toDel(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="params.page"
                        :page-sizes="[5,10, 50, 100, 200,500]"
                        :page-size="params.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>

    export default {
        name: "comment",
        data() {
            return {
                loading: true,
                tableData:[],
                total:0,
                params:{
                    page:1,
                    limit:10,
                    businessId:this.$route.query.businessId,
                    content:'',
                    startTime:'',
                    endTime:'',
                },
                pickTime:'',
                selectArr:''
            }
        },
        created() {
           this.commentList()
        },
        methods: {
            handleSelectionChange(val){
                this.selectArr=val;
            },
            pickChange(val){
                val ? this.params.startTime=val[0] : this.params.startTime='';
                val ? this.params.endTime=val[1] : this.params.endTime='';
            },
            commentList(){
                for(let p in this.params){
                    this.params[p]=="" ? delete this.params[p] : ''
                }
                this.API.demandList(this.params).then(res=>{
                    this.loading=false;
                    if(res.success){
                        this.tableData=res.list;
                        this.total=res.total;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '60px!important',background:'#f5f5f5',color: '#666'}
                }
            },
            // 分页模块
            handleSizeChange(val) {
                this.params.page=1;
                this.params.limit=val;
                this.commentList()
            },
            handleCurrentChange(val) {
                this.params.page=val;
                this.commentList()
            },
            batchDelete(){
                let arr=[];
                this.selectArr && this.selectArr.map(res=>{
                    arr.push(res.id)
                });
                if(this.selectArr.length>0){
                    this.$confirm('此操作将永久删除该评论, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.API.demandDelete({ids:arr.join(',')}).then(res=>{
                            if(res.success){
                                this.$message.success("删除成功");
                                this.commentList()
                            }else {
                                this.$message.error(res.message);
                            }
                        })
                    }).catch();
                }else {
                    this.$message.error('至少选择一条评论');
                }

            },
            search(){
                this.params.page=1;
                this.commentList()
            },
            reset(){
                this.params.page=1;
                this.params.content='';
                this.params.startTime='';
                this.params.endTime='';
                this.pickTime='';
                this.commentList()
            },
            toTop(item){
                this.API.demandTop({id:item.id,type:item.isTop==0 ? 1 : 0}).then(res=>{
                    if(res.success){
                        this.$message.success("成功");
                        this.commentList();
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            toDel(id){
                this.API.demandDelete({ids:id}).then(res=>{
                    if(res.success){
                        this.$message.success("删除成功");
                        this.tableData=this.tableData.filter(item=>{return item.id!=id});
                    }else {
                        this.$message.error(res.message);
                    }
                })
            }
        } //end methods
    }
</script>

<style scoped lang="scss">
    .comment{
        margin: 20px;
    }
    .changeColor {
        color: #ff2323;
    }
    .commentName{
        color: #409EFF;
        cursor: pointer;
    }
    .batchDelete{
        position: absolute;
        left: 16px;
    }
    .nav {
        background: #fff;
        height: 58px;
        text-align: left;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        position: relative;
        border-bottom: solid 1px #ddd;
        padding-left: 16px;
    }

    .searchFrame {
        text-align: left;
        background: #fff;
        padding: 20px;
    }

    .areaStyle {
        margin-top: 20px;
        .el-select {
            width: 216px;
            margin-right: 10px;
        }
    }

    .selectSearchCond {
        .el-select, .el-input {
            width: 216px;
            margin-right: 10px;
        }
    }

    .btnFrame {
        padding: 10px 0;
        text-align: center;
        border-top: solid 1px #ddd;
        background: #fff;
        font-size: 18px;
        color: #333;
        p {
            text-align: center;
            position: relative;
        }

    }

    .tableList {
        margin-top: 20px;
        padding: 20px;
        background: #fff;
    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
</style>
<style lang="scss">
    .comment{
        .nav{
            .el-breadcrumb{
                line-height: 58px;
            }
        }
    }
</style>