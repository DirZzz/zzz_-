<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>同城店铺</span>
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">

                <el-select v-model="customerStatus" @change="getListData" clearable placeholder="动态类型">
                    <el-option v-for="(item,index) in dynamicList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"
                    ></el-option>
                </el-select>

                <el-input v-model="queryParam" placeholder="动态标题"></el-input>
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

                <el-table-column label="动态标题" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.dynamicName }}</template>
                </el-table-column>

                <el-table-column label="类型" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.dynamicType }}</template>
                </el-table-column>
                    
                <el-table-column label="缩略图" header-align="center" align="center">
                    <template slot-scope="scope" v-if="scope.row.picPath != null">
                         <img width="120px" height="80px;" :src="imgurl+scope.row.picPath" />
                    </template>
                </el-table-column>

                <el-table-column label="动态时间" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.gmtModified }}</template>
                </el-table-column>

                <!-- <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" @click="toDel(scope.row.id,scope.row.dynamicType)">删除</el-button>
                        <el-button type="text" @click="toTop(scope.row.id,scope.row.recommendedTime > 0?0:1,scope.row.dynamicType)">{{ scope.row.recommendedTime == 0 ? '置顶':'取消置顶'}}</el-button>
                        <el-button type="text" @click="toReflresh(scope.row.id,scope.row.recommendedTime > 0?0:1,scope.row.dynamicType)">刷新</el-button>
                    </template>
                </el-table-column> -->
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
                imgurl:process.env.sourceBaseUrl,
                total: 0, // 总页数
                mobile: '',
                customerStatus: '', // 客户状态
                queryParam:'',
                dynamicList: [
                    {value:0, name:'供需'},
                    {value: 1, name: '店铺'}
                ],
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
            // 区域选择 结束---------- streetCodeName
            // 分类列表
            getListData() {
                let queryCondition = Object.assign({}, this.query, {
                    type:this.customerStatus,
                    dynamicName:this.queryParam
                })
                // 删除空参数
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                setTimeout(() => {
                    this.loading = false;

                    this.API.queryDynamic(queryCondition).then(res => {
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
            toReflresh(objectId,topId,type) {
                this.API.refreshDynamicById({
                    id:objectId,
                    topId:topId,
                    type:type
                }).then(res => {
                        if (res.code === 200) {
                            this.getListData();
                            this.$message.success("刷新成功！");
                        }
                    });
            },
            // 详情
            toDetail(objectId) {
                this.$router.push(`/dynamic/detail/${objectId}`);
            },
            toEdit(objectId)  {
                this.$router.push(`/dynamic/edit/${objectId}`);
            },
            deletList(objectId,type){
                this.API.deleteDynamicById({
                        id:objectId,
                        type:type
                    }).then(res => {
                        if (res.code === 200) {
                            this.getListData();
                            this.$message.success("删除成功！");
                        }
                    });

            },
            toDel(objectId,type) {
                this.$confirm("此操作将删除内容，是否继续", "提示", {
                    confirmButtonText: "确定删除",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                   this.deletList(objectId,type);
                }).catch(() => {
                    this.$message({type: "info", message: "已取消删除"});
                });
            },
            toTop(objectId,topId,type) {
                   this.API.dynamicToTopById({
                       id:objectId,
                       topId:topId,
                       type:type
                   }).then(res => {
                        if (res.code === 200) {
                            this.getListData();
                            this.$message.success("操作成功！");
                        }
                    });
            }
        } //end methods
    }
</script>

<style scoped lang="scss">
    /*@import "../../assets/css/mixin.scss";*/

    .changeColor {
        color: #ff2323;
    }
.customerFrame{
    margin: 20px;
}
    .customerTitle {
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        text-align: left;
        color: #333;
        border-bottom: solid 1px #ddd;
        span {
            display: inline-block;
            padding-left: 20px;
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
        padding: 20px;
        margin-top: 20px;
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