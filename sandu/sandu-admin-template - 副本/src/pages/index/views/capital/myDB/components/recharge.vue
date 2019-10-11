<!--充值记录页面-->
<template>
    <div class="recharge">
        <main>
            <el-table
                    :data="tableData"
                    style="width: 100%">
               <!-- <el-table-column
                        prop="date"
                        label="编号"
                >
                </el-table-column>-->
                <el-table-column
                        prop="platformName"
                        label="平台"
                >
                </el-table-column>

                <el-table-column
                        prop="totalFee"
                        label="充值金额"
                        width="100px"
                >
                </el-table-column>
              <!-- <el-table-column
                        :render-header="DBtoRMB"
                >
                </el-table-column> -->
                <el-table-column
                        prop="totalFee"
                        label="充值度币"
                        width="100px"
                        :formatter="transformRMB"
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
                    :total="total">
            </el-pagination>
        </footer>
    </div>
</template>

<script type="text/ecmascript-6">
    export default {
        data() {
            return {
                tableData: [],
                total: 0,
                query:{
                    page : 1,
                    limit : 10
                }
            }
        },
        methods: {
            DBtoRMB(h, {column, $index}) {
                return h("div", {}, [
                    h("span", {
                        "class": {
                            "huansuanImg": true
                        }
                    }),
                h("el-tooltip", {
                        attrs: {
                            class: "huansuanImg",
                            effect: "dark",
                            content: "实收总金额 = 收款总金额 - 退款总金额",
                            placement: "bottom"
                        }
                    }, [
                        h("i", {})
                    ])

                ])
            },
            getUserRechargeList(currentPage) {
                console.log(currentPage)
                this.API.getUserRechargeRecord({
                    start : currentPage,
                    limit : this.query.limit
                }).then((res) => {
                    this.tableData = res.datalist;
                    this.total = res.totalCount;
                })

            },
            transformRMB(row, column, cellValue, index){
                 return row.totalFee * 10
            },
            page(){
                this.getUserRechargeList(this.query.page);
            }
        }, created() {
            this.getUserRechargeList(this.query.page);
        }
    }
</script>

<style scoped lang="scss">
    main {

    }

    footer {
        text-align: center;
        .el-pagination {
            padding: 30px 0;
        }
    }

</style>
