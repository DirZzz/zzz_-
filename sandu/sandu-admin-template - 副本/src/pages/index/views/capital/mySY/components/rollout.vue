<!--转出记录-->
<template>
    <div class="rollout">
        <header>
            <el-row>
                <el-col :span="3">
                    <el-input v-model="tranferUser"  @keyup.native="number" placeholder="操作人"></el-input>
                </el-col>
                <el-button type="primary" v-on:click="handlerSearchClick" round size="medium ">搜索</el-button>
                <el-button round class="searchBtn"  style="width:120px;" size="medium" @click="reSet">重置</el-button>
                <el-button class="excelButton" @click="exportExcel()">导出</el-button>
            </el-row>
        </header>
        <main>
            <el-table
                    :data="tableData"
                    style="width: 100%">
                <el-table-column
                        prop="transferDubi"
                        label="转出度币"
                >
                </el-table-column>
                <el-table-column
                        prop="transferStatus"
                        label="状态"
                        :formatter="transformState"
                >
                </el-table-column>
                <el-table-column
                        prop="receiveUserName"
                        label="收款方">
                </el-table-column>
                <el-table-column
                        prop="transferTime"
                        label="操作时间"
                >
                </el-table-column>
                <el-table-column
                        prop="transferUserName"
                        label="操作人"
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
                value: "",
                tableData: [],
                total : 0,
                tranferUser : '',
                query:{
                    page:1,
                    limit : 10
                }
            }
        },
        methods: {
            exportExcel(){
                this.API.exportIncomeExcel({
                    exportType: 'transferRecordList',
                    transferUserName: this.tranferUser,
                    companyId: sessionStorage.getItem("companyID")
                });
            },
             number() {
                    this.tranferUser = this.tranferUser.replace(/\s+/g,"");   
            },
            reSet(){
                this.tranferUser = '';
                this.page();
            },
            getTransferList(currentPage,tranferUser) {
                let companyId = sessionStorage.getItem("companyID");
                let formData = new FormData();
                formData.append("companyId", companyId);
                formData.append("start", currentPage);
                formData.append("limit", this.query.limit);
                if (typeof (tranferUser) != "undefined") {
                    formData.append("transferUserName", tranferUser);
                }
                this.API.getCompanyTransferList(formData).then((res) => {
                    this.tableData = res.datalist;
                    this.total = res.totalCount;
                })
            },
            transformState(row, column, cellValue, index){
               return row.transferStatus == 0? '失败' : '成功'
            },
            page(){
                this.getTransferList(this.query.page);
            },
            handlerSearchClick(){
                this.getTransferList(this.query.page,this.tranferUser);
            }
        }, created() {
            this.getTransferList(this.query.page);
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
</style>
