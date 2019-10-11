<template>
    <div class="messageFrame">
        <header class="topBar">
            <div class="barTitle">
                <span>推送信息管理</span>
            </div>
            <div class="searchOption">
                <el-select clearable  size="small" placeholder="请选择平台">
                    <el-option>
                    </el-option>
                </el-select>

                <el-select clearable  size="small" placeholder="请选择推送对象">
                    <el-option>
                    </el-option>
                </el-select>
            </div>
            <div class="footer">
                <span class="queryBtn">查询</span>
                <span class="queryReset">重置</span>
            </div>
        </header>
        <section class="messageList">
            <div class="listTitle">客户信息
                <div class="right">
                    <el-button size="small" type="primary">新增消息</el-button>
                </div>
            </div>
            <div class="tableDataFrame">
                <el-table
                        :data="messageTableList"
                        width="100%"
                        v-loading="loading"
                        :header-cell-style="rowStyle"
                        style="padding-top:20px"
                        element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(255, 255, 255, 0.8)"
                        tooltip-effect="dark"
                >
                    <el-table-column prop="listIndex" width="50px" label="序号"> </el-table-column>
                    <el-table-column prop="listIndex" label="平台"> </el-table-column>
                    <el-table-column prop="listIndex" label="推送对象"> </el-table-column>
                    <el-table-column prop="listIndex" label="推送方式"> </el-table-column>
                    <el-table-column prop="listIndex" label="推送时间"> </el-table-column>
                    <el-table-column prop="listIndex" label="消息标题"> </el-table-column>
                    <el-table-column prop="listIndex" label="消息内容"> </el-table-column>
                    <el-table-column prop="listIndex" label="消息图片"> </el-table-column>
                    <el-table-column prop="listIndex" label="操作">
                        <template slot-scope="scope">
                            <el-button type="text">编辑</el-button>
                            <el-button type="text">详情</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="pageCount">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="query.page"
                        :page-sizes="[10, 50, 100, 200,500]"
                        :page-size="query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </section>
    </div>
</template>

<script>
    export default {
        name: "messagelist",
        data(){
            return{
                loading:true,
                messageTableList:[],
                query:{
                    limit:1,
                    page:10
                },
                total:0
            }
        },
        methods:{
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '30px!important',background:'#f5f5f5',color: '#666',lineHeight:'80px!important'}
                }
            },
            getMessageList(){
                setTimeout(() => {
                    this.loading = false
                },1000)
            },
            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size
                this.getMessageList()
            },

            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage
                this.loading = true
                this.getMessageList()
            },
        },
        created(){
            this.getMessageList()
        },
    }
</script>

<style lang="scss" scoped>
    @import "../../assets/css/common.scss";
    .messageFrame{
        margin: 20px;
        text-align: left;
    }
    .topBar{
        background: #fff;
        div{
            text-align: left;
        }
        .footer{
            margin-top: 20px;
            border-top: solid 1px #e8e8e8;
            text-align: center;
            padding:20px  0 ;
            display: block;
        }
        .barTitle{
            border-bottom: solid 1px #e8e8e8;
            padding-left: 32px;
            font-size: 16px;
            color: #333;
            height: 56px;
            line-height: 56px;
            position: relative;
        }
    }
    .searchOption{
        .el-select, .pickDate{
            margin-top: 20px;
            margin-left: 32px;
            margin-right: 10px;
        }
    }
    .queryBtn{
        height: 30px;
        width:62px;
        display: inline-block;
        text-align: center;
        line-height: 30px;
        border-radius: 4px;
        color: #fff;
        background: $theme-color;
        cursor: pointer;
        font-size: 14px;
        margin-left: 40px;
        margin-right: 10px;
    }
    .queryReset{
        height: 30px;
        width:62px;
        font-size: 14px;
        display: inline-block;
        text-align: center;
        line-height: 30px;
        border-radius: 4px;
        border:solid 1px #d9d9d9;
        color: #999;
        cursor: pointer;
    }
    .messageList{
        background: #fff;
    }
    .exportExcel{
        position: absolute;
        right: 32px;
        top: 0;
        span{
            display: inline-block;
            text-align: center;
            font-size: 14px;
            width: 82px;
            height: 32px;
            line-height: 32px;
            background-color: $theme-color;
            color: #fff;
            border-radius: 4px;
        }
    }
    .listTitle{
        font-size: 16px;
        color: #333;
        height: 56px;
        text-align: left;
        line-height: 56px;
        background: #fff;
        margin-top: 20px;
        border-bottom: solid 1px #e8e8e8;
        padding-left: 32px;
        position: relative;
        .right{
            @extend .exportExcel
        }
    }
    .tableDataFrame{
        padding:0 20px;
        background: #fff;
    }
</style>