<!--消费记录页面-->
<template>
    <div class="consume">
        <header>
            <el-select v-model="platformId" v-on:change="page" placeholder="平台" clearable>
                <el-option v-for="item in basePlatforms" :key="item.id" :label="item.platformName" :value="item.id">
                </el-option>
            </el-select>
            <el-select style="margin-left:6px;" v-on:change="page" v-model="searchFeeType" placeholder="消费项目" clearable>
                <el-option v-for="item in consumOptions" :key="item.optionvalue" :label="item.optionName" :value="item.optionvalue">
                </el-option>
            </el-select>
             <el-button type="primary" style="width:120px;" round size="medium " v-on:click="page">搜索</el-button>
             <el-button round class="searchBtn"  style="width:120px;" size="medium" @click="reSet">重置</el-button>
        </header>
        <main>
            <el-table
                    :data="tableData"
                    style="width: 100%">
                <el-table-column
                        prop="orderNo"
                        label="订单号"
                >
                </el-table-column>
                <el-table-column
                        prop="platformName"
                        label="平台"
                >
                </el-table-column>
                <!--:formatter="transformChinese"-->
                <el-table-column
                        prop="productName"
                        label="消费项目"
                >
                </el-table-column>
                <el-table-column
                        prop="totalFee"
                        label="消费度币"
                >
                </el-table-column>
                <el-table-column
                        prop="creator"
                        label="操作人"
                >
                </el-table-column>
                <el-table-column
                        prop="gmtCreate"
                        label="消费时间"
                >
                </el-table-column>
            </el-table>
        </main>
        <footer>
            <el-pagination

                    @current-change="page"
                    :current-page.sync="query.page"
                    :page-sizes="[10]"
                    :page-size="query.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="pageTotal">
            </el-pagination
                   >
        </footer>
    </div>
</template>

<script type="text/ecmascript-6">
    export default {
        data() {
            return {
                basePlatforms: [],
                consumOptions:[
                    {
                       optionName:"方案版权费",
                       optionvalue:'DESIGN_PLAN_COPYRIGHT'
                    },
                    {
                        optionName:"方案售卖",
                        optionvalue:'DESIGN_PLAN_SALE'
                    }
                ],
                platformId: '',
                searchFeeType:'',
                tableData: [],
                pageTotal:0,
                query:{
                    page:1,
                    limit:10
                }
            };
        },
        methods: {
            reSet(){
                this.platformId = '';
                this.searchFeeType = '';
                this.page();
            },
            transformChinese(row, column, cellValue, index){
                if (row.bizType === 'DESIGN_PLAN_SALE') {
                    return '方案售卖';
                }else if (row.bizType === 'DESIGN_PLAN_COPYRIGHT') {
                    return '方案版权费';
                }else{
                    return row.productName;
                }
            },
            getUserDubiExpensesRecordList(currentPage,platformId,searchFeeType) {
                //获取用户消费列表
                this.API.getUserDubiExpensesRecordList({
                    start:currentPage,
                    limit:this.query.limit,
                    platformId : platformId?platformId:undefined,
                    ExpenseType : searchFeeType?searchFeeType:undefined
                }).then((res) => {
                   this.tableData = res.datalist;
                   this.pageTotal = res.totalCount;
                })
            },
            page(){
                this.getUserDubiExpensesRecordList(this.query.page,this.platformId,this.searchFeeType);
            },
            getBasePlatformList(){
                this.API.getPlatforms().then((res) =>{
                    this.basePlatforms = res.obj;
                })
            }
        },
        created() {
            this.getUserDubiExpensesRecordList(this.query.page);
            this.getBasePlatformList();
        }
    };
</script>

<style scoped lang="scss">
    header {
        padding: 10px;
    }

    main {
        margin-top: 20px;
    }

    footer {
        text-align: center;
        .el-pagination {
            padding: 30px 0;
        }
    }
</style>
