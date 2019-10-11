<template>
    <div class="record">
        <el-table
                :data="recordList"
                style="width: 100%"
                v-loading="loading"
                element-loading-text="拼命加载中"
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(255, 255, 255, 0.8)"
                tooltip-effect="dark"
        >
            <el-table-column
                    prop="orderNo"
                    label="订单编号">
            </el-table-column>
            <el-table-column
                    label="套餐名称"
                    prop="servicesName">
            </el-table-column>
            <el-table-column
                    label="套餐时长"
                    prop="purchaseUseTime">
            </el-table-column>
            <el-table-column
                    label="数量"
                    prop="purchaseAmount">
            </el-table-column>
            <el-table-column
                    label="金额"
                    prop="purchaseMomey">
            </el-table-column>
            <el-table-column
                    label="支付方式"
                    prop="payType">
                <template slot-scope="scope">
                    <span v-if="scope.row.payType == 0">支付宝</span>
                    <span v-if="scope.row.payType == 1">微信</span>
                    <span v-if="scope.row.payType == 2">其他</span>
                </template>
            </el-table-column>
            <el-table-column
                    label="购买结果"
                    prop="purchaseStatus">
                <template slot-scope="scope">
                    <span v-if="scope.row.purchaseStatus == 0">待支付</span>
                    <span v-if="scope.row.purchaseStatus == 1">支付成功</span>
                    <span v-if="scope.row.purchaseStatus == 2">支付失败</span>
                </template>
            </el-table-column>
            <el-table-column
                    label="购买时间"
                    prop="purchaseTime"
            >
            </el-table-column>
            <el-table-column
                    label="操作人员"
                    prop="creator"
            >
            </el-table-column>
        </el-table>
        <!--分页组件-->
        <div class="pageFrame">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="query.page"
                    :page-sizes="[10, 50, 100, 200,500]"
                    :page-size="query.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: "record",
        data() {
            return {
                recordList: [],
                total: 0,
                loading: true,
                query: {
                    page: 1,
                    limit: 10,
                    companyId: sessionStorage.getItem('companyID')
                },
            }
        },
        created() {
            this.listRecords()
        },
        methods: {

            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size
                this.listRecords()
            },
            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage
                this.loading = true
                this.listRecords()
            },
            listRecords() {
                this.API.listRecordsApi(this.query).then(res => {
                    if (res) {
                        this.loading = false
                        this.recordList = res.datalist
                        this.total = res.totalCount
                    }
                })
            }
        }
    }
</script>

<style scoped lang="scss">
    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .record {
        background: #fff;
    }
</style>