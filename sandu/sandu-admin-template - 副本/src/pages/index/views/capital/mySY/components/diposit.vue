<!--提现记录-->
<template>
    <div class="diposit">
        <header>
            <el-row class="row">
                <el-col :span="3">
                    <el-select style="margin-left:6px;" v-model="withdrawType" placeholder="提现方式">
                        <el-option value="0" label="个人"></el-option>
                        <el-option value="1" label="公司"></el-option>
                    </el-select>
                </el-col>

                <el-col :span="3">
                    <el-input v-model="applyUser"  @keyup.native="number" placeholder="输入您要查找的操作人"></el-input>
                </el-col>

                <el-col :span="3">
                    <el-select style="margin-left:6px;" v-model="withdrawState" placeholder="提现状态">
                        <el-option value="0" label="提现中"></el-option>
                        <el-option value="1" label="已提现"></el-option>
                    </el-select>
                </el-col>

                <el-button type="primary"  round size="medium " v-on:click="handlerSearchClick">搜索</el-button>
                <el-button round class="searchBtn" size="small" @click="handleClearQuery">重置</el-button>
                <el-button class="excelButton" @click="exportExcel()">导出</el-button>
            </el-row>
        </header>
        <main>
            <el-table
                    :data="tableData"
                    style="width: 100%">
                <!--<el-table-column
                        prop="date"
                        label="编号"
                >
                </el-table-column>-->
                <el-table-column
                        prop="applyDubi"
                        label="涉及度币"
                >
                </el-table-column>
                <el-table-column
                        prop="realWithdrawAmount"
                        label="实际提现金额">
                </el-table-column>
                <el-table-column
                        prop="withdrawType"
                        label="提现方式"
                        :formatter="transformWithdrawType"
                >
                </el-table-column>
                <el-table-column
                        prop="withdrawStatus"
                        label="状态"
                        :formatter="transformStatus"
                >
                </el-table-column>
                <el-table-column
                        prop="revceiveUserName"
                        label="收款人"
                >
                </el-table-column>
                <el-table-column
                        prop="applyUserName"
                        label="申请人"
                >
                </el-table-column>shopIntelligent
                <el-table-column
                        prop="applyTime"
                        label="申请时间"
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
                options: [
                    {
                        value: "平台",
                        label: "平台"
                    }
                ],
                value: "",
                tableData: [],
                total: 0,
                query: {
                    page: 1,
                    limit: 10
                },
                applyUser: '', //操作人
                withdrawType: '', //提现方式
                withdrawState: '' // 提现状态
            }
        },
        methods: {
            exportExcel(){
                this.API.exportIncomeExcel({
                    exportType: 'withdrawRecordList',
                    withdrawType: this.withdrawType,
                    applyUser: this.applyUser,
                    withdrawState: this.withdrawState,
                    companyId: sessionStorage.getItem("companyID")
                });
            },
             number() {
                    this.applyUser = this.applyUser.replace(/\s+/g,"");
            },
            getCompanyWithdrawListTwo(currentPage, type, user, state) {
                let companyId = sessionStorage.getItem("companyID");
                let formData = new FormData();
                formData.append("companyId", companyId);
                formData.append("start", currentPage);
                formData.append("limit", this.query.limit);
                console.log(user);
                if (typeof (type) != "undefined") {
                    formData.append("withdrawType", type);
                }
                if (typeof (user) != "undefined" && user != '') {
                    formData.append("applyUser", user);
                }
                if (typeof (state) != "undefined") {
                    formData.append("withdrawStatus", state);
                }
                this.API.getCompanyWithdrawRecordList(formData).then((res) => {
                    this.tableData = res.datalist;
                    this.total = res.totalCount;
                })
            },
            page() {
                this.getCompanyWithdrawListTwo(this.query.page);
            },
            transformWithdrawType(row, column, cellValue, index) {
                return row.withdrawType == 0 ? '个人' : '公司';
            },
            transformStatus(row, column, cellValue, index) {
                return row.withdrawStatus == 0 ? '提现中' : '已提现'
            },
            handlerSearchClick() {
                console.log(this.applyUser);
                this.getCompanyWithdrawListTwo(this.query.page,this.withdrawType,this.applyUser,this.withdrawState);
            },
            handleClearQuery(){
                this.withdrawType = '';
                this.applyUser = '';
                this.withdrawState = '';
                this.getCompanyWithdrawListTwo(this.query.page);
            }
        },
        created() {
            this.getCompanyWithdrawListTwo(this.query.page);
        }
    }
</script>

<style scoped lang="scss">
    header {
        padding: 10px;
        .el-button {
            width: 120px;
            font-size: 14px;
            margin-left: 10px;
        }
        .excelButton{
            float: right;
            margin-right: 30px;
            height: 36px;
            width: 70px;
        }
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
    .row{
        .el-col{
            margin-left:10px;
        }
    }
</style>
