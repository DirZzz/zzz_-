<template>
    <div class="buylist">
        <el-button  class="exportCount"  type="text" @click="handleDownload" :disabled="isSelectData">导出账号</el-button>
        <el-table
                :data="serviceList"
                style="width: 100%"
                v-loading="loading"
                element-loading-text="拼命加载中"
                element-loading-spinner="el-icon-loading"
                @selection-change="handleSelectionChange"
                element-loading-background="rgba(255, 255, 255, 0.8)"
                tooltip-effect="dark"
        >
            <el-table-column
                    type="selection"
                    label="全选"
                    align="center"
                    width="75" title="全选">
            </el-table-column>
            <el-table-column
                    prop="servicesCode"
                    label="序号">
            </el-table-column>
            <el-table-column
                    label="套餐名称"
                    prop="servicesName">
            </el-table-column>
            <el-table-column
                    label="描述"
                    prop="serviceDesc">
            </el-table-column>
            <el-table-column
                    label="有效时长"
                    prop="duration">
            </el-table-column>
            <el-table-column
                    label="使用状态"
                    prop="status">
                <template slot-scope="scope">
                    <div v-if="scope.row.status">
                        <!--0-未使用;1-使用-->
                        {{scope.row.status == 1 ? '已使用':'未使用'}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    label="使用账号"
                    prop="account">
                <template slot-scope="scope">
                    <div v-if="scope.row.status">
                        <!--0-未使用;1-使用-->
                        {{scope.row.status == 1 ? scope.row.account:''}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    label="登录名"
                    prop="account">
            </el-table-column>
            <el-table-column
                    label="密码"
                    prop="password"
            >
            </el-table-column>
            <el-table-column
                    label="用户类型"
                    prop="userTypeName">
            </el-table-column>
            <el-table-column
                    label="时间"
                    prop="gmtCreate"
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
        name: "buylist",
        data() {
            return {
                serviceList: [],
                total: 0,
                isSelectData: true,
                multipleSelection: [],
                loading: true,
                query: {
                    page: 1,
                    limit: 10,
                    companyId: sessionStorage.getItem('companyID')
                }
            }
        },
        created() {
            this.listServiceWithBeBuy()
        },
        methods: {
            handleSelectionChange (val) {
                console.log(val, 'value')
                if(val.length > 0) {
                    this.isSelectData = false
                    this.multipleSelection = val
                } else {
                    this.isSelectData = true
                }
            },
            // 导出excel 表
            handleDownload() {
                require.ensure([], () => {
                    const { export_json_to_excel } = require('@/vendor/Export2Excel')
                    const tHeader = ['序号', '套餐名称', '描述','有效时长','使用状态','使用账号','登录名', '密码','用户类型','时间']
                    const filterVal = ['servicesCode', 'servicesName', 'serviceDesc','duration','status','account', 'account','password','userTypeName','gmtCreate']
                    const list = this.multipleSelection
                    const data = this.formatJson(filterVal, list)
                    export_json_to_excel(tHeader, data, '已购套餐excel文件')
                })
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },
            // 分页模块
            handleSizeChange: function (size) {
                // this.pageSize = size;
                this.query.limit = size
                this.listServiceWithBeBuy()
            },
            handleCurrentChange: function (currentPage) {
                // this.currentPage = currentPage;
                this.query.page = currentPage
                this.loading = true
                this.listServiceWithBeBuy()
            },
            listServiceWithBeBuy() {
                this.API.listServiceWithBuy(this.query).then(res => {
                    if(res) {
                        this.loading = false
                        this.serviceList = res.datalist
                        this.total = res.totalCount
                    }
                })
            },
            exportExcel(){
                this.API.exportExcelApi()
            }
        }
    }
</script>

<style lang="scss" scoped>
    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
    .buylist {
        background: #fff;
    }

    .exportCount {
        padding: 15px;
    }
</style>