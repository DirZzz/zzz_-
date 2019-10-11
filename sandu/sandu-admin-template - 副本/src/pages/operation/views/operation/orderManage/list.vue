<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>订单管理</span>
            <!--<div class="rightButton">
                <el-button type="primary" size="mini" class="UnifiedsearchBtn" @click="toAdd" v-if="permission('operation:orderManage:add')">新增</el-button>
            </div>-->
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">
                <el-button type="primary" size="mini" class="UnifiedsearchBtn" @click="toAdd" v-if="permission('operation:orderManage:add')">新增</el-button> <el-input v-model="queryParam"  size="small" placeholder="搜索"></el-input>
            </el-row>
        </div>
        <div class="btnFrame clearfix">
            <p>
                <el-button round type="primary" size="small" @click="getListData">搜索</el-button>
                <el-button round  size="small" @click="resetForm">重置</el-button>
            </p>
            
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

                <el-table-column label="中介名称" header-align="center" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.intermediaryName }}
                    </template>
                </el-table-column>

                <el-table-column label="成交对象" header-align="center" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.tradePartner }}
                    </template>
                </el-table-column>
                    
                <el-table-column label="成交金额" header-align="center" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.tradePrice }}
                    </template>
                </el-table-column>
                               
                <el-table-column label="成交时间" header-align="center" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.tradeDate }}
                    </template>
                </el-table-column>

                <el-table-column
                        label="操作" header-align="center" align="center">
                    <template slot-scope="scope">
                        <!--<el-button type="text" @click="toDetail(scope.row.id)">详情</el-button>-->
                        <el-button type="text" @click="toEdit(scope.row.id)" v-if="permission('operation:orderManage:edit')">编辑</el-button>
                        <el-button type="text" @click="toDel(scope.row.id)" v-if="permission('operation:orderManage:delete')">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="query.page"
                        :page-sizes="[5,10, 50, 100, 200,500]"
                        :page-size="query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>

    export default {
        name: "coustomer",
        data() {
            return {
                total: 0, // 总页数
                mobile: '',
                customerStatus: '', // 客户状态
                queryParam:'',
                loading: true,
                streetShow: true,
                //区域选择-----------
                search: {
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },
                citySelect: {
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },

                cityCode: '',
                provide: [],
                provideSelect: [],
                citySelects: [],
                areaSelect: [],
                originSelect: [],

                //区域选择 结束------------
                tableData: [],
                listTableData: [],
                query: {
                    page: 1,
                    limit: 10,
                }
            }
        },
        created() {
            this.getListData();
        },
        methods: {
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '60px!important',background:'#f5f5f5',color: '#666'}
                }
            },
            getOriginCode(code) {
            },
            toAdd() {
                this.$router.push(`/orderManage/add`);
            },
            // 区域选择 结束---------- streetCodeName
            // 分类列表
            getListData() {
                let queryCondition = Object.assign({}, this.query, {
                    queryParam:this.queryParam
                })
                // 删除空参数
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                setTimeout(() => {
                    this.loading = false;

                    this.API.queryOrderManage(queryCondition).then(res => {
                     this.tableData = res.list;
                     this.total = res.total
                    });
                }, 1000);
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
            },

            resetForm() {
                this.citySelect = {}
                this.mobile = ''
                this.customerStatus = ''
                this.queryParam = ''
            },
            // 详情
            toDetail(objectId) {
                this.$router.push(`/orderManage/detail/${objectId}`);
            },
            toEdit(objectId)  {
                this.$router.push(`/orderManage/edit/${objectId}`);
            },
            toDel(objectId) {
                this.$confirm("此操作将删除内容，是否继续", "提示", {
                    confirmButtonText: "确定删除",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.API.deleteOrderManageById({
                        orderManageId:objectId
                    }).then(res => {
                        if (res.code === 200) {
                            this.getListData();
                            this.$message.success("删除成功！");
                        }
                    });
                }).catch(() => {
                    this.$message({type: "info", message: "已取消删除"});
                });
            }
        } //end methods
    }
</script>

<style scoped lang="scss">
    /*@import "../../assets/css/mixin.scss";*/
.customerFrame{
    margin: 20px;
}
    .changeColor {
        color: #ff2323;
    }

    .customerTitle {
        background: #fff;
        height: 58px;
        text-align: left;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        position: relative;
        border-bottom: solid 1px #ddd;
        span {
            display: inline-block;
            padding-left: 20px;
        }
        .rightButton{
            position: absolute;
            right:20px;
            top:-2px;
        }
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