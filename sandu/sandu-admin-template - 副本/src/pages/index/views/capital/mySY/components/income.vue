<!--收益记录-->
<template>
    <div class="income">
        <header>
            <el-row class="row">
                <el-col :span="3">
                    <el-select v-model="platformId" v-on:change="page" placeholder="平台" clearable>
                        <el-option v-for="item in basePlatforms" :key="item.id" :label="item.platformName" :value="item.id">
                        </el-option>
                    </el-select>
                </el-col>
                <el-col :span="3">
                    <el-input style="width:220px;" v-model="planCode"  @keyup.native="number('planCode')" placeholder="输入您要查找的方案编码" v-on:change="page" clearable></el-input>
                </el-col>
                <el-col :span="3">
                    <el-input v-model="planCreator" style="width:220px;margin-left:20px;" placeholder="输入您要查找的方案创建人"  @keyup.native="number('planCreator')" v-on:change="page" clearable></el-input>
                </el-col>
                <el-button type="primary" style="width:120px;margin-left:60px;" round size="medium " v-on:click="page">搜索</el-button>
                <el-button round class="searchBtn"  style="width:120px;" size="medium" @click="reSet">重置</el-button>
                <el-button class="excelButton" @click="exportExcel()">导出</el-button>
            </el-row>
        </header>
        <main>
            <el-table
                    :data="tableData"
                    style="width: 100%"
            >
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
                        prop="planCode"
                        label="方案编码">
                </el-table-column>
                <el-table-column
                        prop="planCreator"
                        label="方案创建者"
                        :formatter="formatterName"
                >
                </el-table-column>
                <el-table-column
                        prop="buyerName"
                        label="购买用户"
                >
                </el-table-column>
                <el-table-column
                        prop="buyType"
                        label="购买途径"
                        :formatter="transformData"
                >
                </el-table-column>
                <el-table-column
                        prop="payDubi"
                        label="涉及度币"
                >
                </el-table-column>
                <el-table-column
                        prop="payTime"
                        label="购买时间"
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
                    :total="query.total">
            </el-pagination>
        </footer>
    </div>
</template>

<script type="text/ecmascript-6">
    export default {
        data() {
            return {
                tableData: [],
                query:{
                    page:1,
                    limit:10,
                    total:0
                },
                basePlatforms: [],
                platformId : '',
                planCode:'',
                planCreator:''
            }
        },
        methods: {
            exportExcel(){
                this.API.exportIncomeExcel({
                    exportType: 'companyIncomeList',
                    platformId: this.platformId,
                    planCode: this.planCode,
                    planCreator: this.planCreator,
                    companyId: sessionStorage.getItem("companyID")
                });
            },
            formatterName(row, column, cellValue, index){
                if (row.planCreator === undefined){
                    return '----'
                } else{
                    return row.planCreator;
                }
            },
             number(params) {
                 console.log(params)
                if(params=='planCode'){
                    this.planCode = this.planCode.replace(/\s+/g,"");
                }else if(params=='planCreator'){
                    this.planCreator = this.planCreator.replace(/\s+/g,"");
                }
            },
            reSet(){
                this.platformId='';
                this.planCode = '';
                this.planCreator = '';
                this.page();
            },
            getCompanyIncomeList(currentPage,platformId,planCode,planCreator) {
                let companyId = sessionStorage.getItem("companyID");
                let formData = new FormData();
                formData.append("companyId", companyId);
                formData.append("start",currentPage);
                formData.append("limit",this.query.limit);
                if (typeof (platformId) != "undefined"){
                    formData.append("platformId",platformId)
                }
                if (typeof (planCode) != "undefined") {
                    formData.append("planCode",planCode)
                }
                if (typeof (planCreator) != "undefined") {
                    formData.append("planCreator",planCreator)
                }
                this.API.getCompanyIncomeList(formData).then((res) => {
                    console.log(res.datalist);
                    this.tableData = res.datalist;
                    this.query.total = res.totalCount;
                })
            },
            transformData(row, column, cellValue, index) {
                if (row.buyType == 0) {
                    return '售卖方案'
                } else {
                    return '版权方案'
                }
            },
            selectPlatforms(){
                this.API.getPlatforms().then((res) =>{
                    this.basePlatforms = res.obj;
                })
            },
            page(){
                this.getCompanyIncomeList(this.query.page,this.platformId,this.planCode,this.planCreator);
            },
            queryBasePlatformlist(){
                this.API.getPlatforms().then((res) =>{
                    this.basePlatforms = res.obj;
                })
            }/*,
            handleClearQuery(){
                this.basePlatforms = [];
                this.planCode = '';
                this.queryBasePlatformlist();
                this.getCompanyIncomeList(this.query.page);
            }*/
        }, created() {
            this.getCompanyIncomeList(this.query.page);
            this.queryBasePlatformlist();
        }
    }
</script>

<style scoped lang="scss">
    header{
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
    .row{
        .el-col{
            margin-left:10px;
        }
    }
    .excelButton{
        float: right;
        margin-right: 30px;
        height: 36px;
    }
</style>
