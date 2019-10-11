<template>
    <div class="main">
        <!--条件查询-->
        <div class="mainTop">
            <div class="titles">结算单管理</div>
            <div class="selRows" style="height: 80px;">
                <div class="selItems">
                    <div class="inputs">
                        <el-input placeholder="请输入结算单号" maxlength="30" v-model="getListParams.settlementCode" clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems">
                    <div class="el-select">
                        <el-select size="medium" v-model="getListParams.companyId" placeholder="请选择所属企业" filterable clearable>
                            <el-option
                                v-for="item in companyList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="selItems">
                    <div class="el-select">
                        <el-select size="medium" v-model="getListParams.settlementStatus" placeholder="请选择结算状态" filterable clearable>
                            <el-option
                                v-for="item in settlementStatusList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="selItems">
                    <div class="inputs">
                        <el-input placeholder="请输入负责人电话" maxlength="30" v-model="getListParams.phone" clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems">
                    <div class="el-select">
                        <el-date-picker v-model="createTimeStr"
                            type="daterange"
                            format="yyyy 年 MM 月 dd 日"
                            value-format="yyyy-MM-dd"
                            range-separator="-"
                            start-placeholder="请选择结算时间"
                            end-placeholder="请选择结算时间" style="width: 420px">
                        </el-date-picker>
                    </div>
                </div>
                <div class="submitBtn" @click="getSettlementList(1)">搜索</div>
                <div class="submitBtn" @click="resetAndSelect()">重置</div>

            </div>
        </div>

        <!-- 返回信息列表 -->
        <div class="mainBody">
            <section>
                <div class="listTitle">
                    <div class="left">
                        <el-button @click="goToAdd()" size="small" type="primary">新增</el-button>
                        <el-button @click="exportSettlementList()" size="small" type="primary">导出Excel</el-button>
                    </div>
                </div>
                <div class="tableDataFrame">
                    <el-table :data="settlementList" :header-cell-style="rowStyle" style="padding-top:20px"
                        element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)"
                        tooltip-effect="dark">
                        <el-table-column type="index" width="50px" label="序号" align="center">
                        </el-table-column>
                        <el-table-column label="结算单号" width="200px" align="center">
                            <template slot-scope="scope">
                                <span style="color:#0066FF;cursor:pointer;font-weight:bold;" @click="goToDetail(scope.row.id)">
                                    {{scope.row.settlementCode}}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="企业名称" width="200px" align="center">
                            <template slot-scope="scope">{{scope.row.companyName}}</template>
                        </el-table-column>
                        <el-table-column label="结算状态" width="100px" align="center">
                            <template slot-scope="scope">{{scope.row.settlementStatusInfo}}</template>
                        </el-table-column>
                        <el-table-column label="总金额" width="100px" align="center">
                            <template slot-scope="scope">{{scope.row.totalAmount}}</template>
                        </el-table-column>
                        <el-table-column label="实际结算金额" width="120px" align="center">
                            <template slot-scope="scope">{{scope.row.actualAmount}}</template>
                        </el-table-column>
                        <el-table-column label="服务费" width="100px" align="center">
                            <template slot-scope="scope">{{scope.row.serviceFee}}</template>
                        </el-table-column>
                        <el-table-column label="结算时间" width="200px" align="center">
                            <template slot-scope="scope">{{scope.row.gmtModified}}</template>
                        </el-table-column>
                        <el-table-column label="操作人" width="200px" align="center">
                            <template slot-scope="scope">{{scope.row.modifier}}</template>
                        </el-table-column>
                        <el-table-column label="负责人电话" width="200px" align="center">
                            <template slot-scope="scope">{{scope.row.phone}}</template>
                        </el-table-column>
                        <el-table-column label="操作" width="150px" align="center">
                            <template slot-scope="scope">
                                <el-button type="text" @click="goToEdit(scope.row.id)">编辑</el-button>
                                <el-button v-if="scope.row.settlementStatus == 0" type="text" @click="deleteSettlementOrder(scope.row.id)">删除</el-button>
                            </template>
                        </el-table-column>
                        </el-table>
                        <div class="pageCount">
                            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listPage" :page-sizes="[10, 20, 30, 40]"
                                :page-size="getListParams.limit" layout="total, sizes, prev, pager, next, jumper" :total="totalCount">
                            </el-pagination>
                        </div>
                </div>
            </section>
        </div>
    </div>

</template>

<script>
    export default {
        data() {
            return {
                settlementOrderDetail: [],
                pageTypeValue: 0,
                createTimeStr: undefined,
                settlementList: [],
                listPage:1,
                totalCount:0,
                companyList: [],
                settlementStatusList: [
                    {value: 0, label: "结算中"},
                    {value: 1, label: "已结算"},
                ],
                getListParams: {
                    orderCodeLike: undefined,
                    settlementCode: undefined,
                    companyId: undefined,
                    phone: undefined,
                    settlementStatus: undefined,
                    createTimeStart: undefined,
                    createTimeEnd: undefined,
                    startTime: undefined,
                    endTime: undefined,
                    page: 1, // 页码
                    limit: 10 // 每页条数
                }
            }
        },
        watch: {

        },
        components: {
            
        },
        activated() {
            console.error("activated");
            console.error("this.$route.query.nonuseCache = ", this.$route.query.nonuseCache);
            console.error("this.$route.query.type = ", this.$route.query.type);
            if (this.$route.query.nonuseCache) {
                // 保持搜索条件的刷新
                if (this.$route.query.type != 1) {
                this.reset();
                this.listPage = 1;
                this.getListParams.page = 1;
                }
                this.getSettlementList();
            }
        },
        created() {
            // console.error("start");
            this.getSettlementList();
            this.getCompanyList();
        },
        methods: {
            deleteSettlementOrder(id) {
                console.error("delete(id), id = " + id);
                this.$confirm('确认删除选中的结算单吗?', '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    type: 'warning',
                    center: true
		        }).then(() => {
                    this.API.deleteSettlementOrder({id : id}).then((res) => {
                            this.$message.success('删除成功')
                            this.getSettlementList(2);
                    })
		        })
            },
            goToEdit(id) {
                this.$router.push({path: "/operation/order/settlement/edit/" + id, query: {type : "edit"}});
            },
            /**
             * 进入结算单详情
             */
            goToDetail(id) {
                console.error("id = " + id);
                this.$router.push({path: "/operation/order/settlement/detail/" + id, query: {type : "detail"}});
            },
            goToAdd() {
                this.$router.push({ path: `/operation/order/settlement/add` });
            },
            exportSettlementList() {
                if (this.createTimeStr != undefined) {
                    this.getListParams.startTime = this.createTimeStr[0];
                    this.getListParams.endTime = this.createTimeStr[1];
                }

                this.API.exportSettlementList(this.getListParams).then(res => {
                    let url=window.URL.createObjectURL(res);
                    let link=document.createElement('a');
                    link.style.display="none";
                    link.href=url;
                    link.setAttribute('download','结算单列表导出.xls');
                    document.body.appendChild(link);
                    link.click();
                });
            },
            getCompanyList() {
                this.API.belongCompanyList().then(res => {
                    this.companyList = res.datalist;
                });
            },
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '30px!important',background:'#f5f5f5',color: '#666',lineHeight:'80px!important'}
                }
            },
            handleSizeChange: function (size) {
                //this.query.limit = size
                this.getListParams.limit = size;
                this.getListParams.page = 1;
                this.getSettlementList()
            },
            handleCurrentChange: function (currentPage) {
                //this.query.page = currentPage;
                this.getListParams.page = currentPage;
                this.loading = true;
                this.getSettlementList()
            },
            getSettlementList(type) {

                if (1 == type) {
                    // 重置page
                    this.listPage = 1;
                    this.getListParams.page = 1;
                }
                if (2 == type) {
                    this.reset();
                    // 重置page
                    this.listPage = 1;
                    this.getListParams.page = 1;
                }

                this.settlementList = [];
                this.totalCount = 0;

                if (this.createTimeStr != undefined) {
                    this.getListParams.startTime = this.createTimeStr[0];
                    this.getListParams.endTime = this.createTimeStr[1];
                } else {
                    this.getListParams.startTime = undefined;
                    this.getListParams.endTime = undefined;
                }

                this.API.getSettlementList(this.getListParams).then(res => {
                    if (res.success) {
                        this.listPage = this.getListParams.page;
                        this.settlementList = res.datalist;
                        this.totalCount = res.totalCount;
                    }
                })
            },
            resetAndSelect() {
                this.reset();
                this.getSettlementList();
            },
            reset() {
                this.createTimeStr = undefined;
                this.getListParams.settlementCode = undefined;
                this.getListParams.companyId = undefined;
                this.getListParams.phone = undefined;
                this.getListParams.settlementStatus = undefined;
                this.getListParams.createTimeStart = undefined;
                this.getListParams.createTimeEnd = undefined;
                this.getListParams.startTime = undefined;
                this.getListParams.endTime = undefined;
            }
        },

    }

</script>

<style scoped lang="scss">
    div {
        box-sizing: border-box;
    }
    
    .reds {
        color: #ff4956 !important;
    }
    
    .grees {
        color: #67c23a !important;
    }
    
    .blues {
        color: #3399ff !important;
    }
    
    @media screen and (max-width: 1620px) {
        .selItems {
            margin-right: 30px !important;
            .inputs {
                width: 180px !important;
            }
        }
        .phone {
            width: 242px !important;
        }
        .wxnum,
        .companys,
        .results {
            width: 256px !important;
        }
        .state {
            width: 228px !important;
        }
    }
    
    @media screen and (max-width: 1480px) {
        .selItems {
            margin-right: 20px !important;
            .inputs {
                width: 160px !important;
                font-size: 13px;
            }
        }
        .phone {
            width: 222px !important;
        }
        .wxnum,
        .companys,
        .results {
            width: 236px !important;
        }
        .state {
            width: 208px !important;
        }
    }
    
    @media screen and (max-width: 1390px) {
        .selRows {
            padding: 24px 10px!important;
        }
        .selItems {
            margin-right: 10px !important;
        }
    }
    
    .main {
        width: 100%;
        height: 100%;
        padding: 20px;
        font-size: 16px;
        color: #333333;
        .mainTop {
            width: 100%;
            background-color: #ffffff;
            border-radius: 2px;
            margin-bottom: 20px;
            .titles {
                height: 55px;
                border-bottom: 1px solid #eaeaea;
                padding: 0 30px;
                line-height: 55px;
                text-align: left;
            }
            .selRows {
                padding: 24px 30px;
                height: 80px;
                .submitBtn {
                    height: 32px;
                    line-height: 32px;
                    box-sizing: border-box;
                    background-color: #3399ff;
                    border-radius: 4px;
                    width: 64px;
                    float: left;
                    color: #fff;
                    font-size: 14px;
                    cursor: pointer;
                    margin-left: 20px;
                }
                .selItems {
                    height: 32px;
                    line-height: 32px;
                    box-sizing: border-box;
                    margin-right: 30px;
                    position: relative;
                    float: left;
                    .label {
                        font-size: 14px;
                        height: 32px;
                        position: absolute;
                        left: 0;
                        top: 0;
                    }
                    .inputs {
                        display: block;
                        width: 200px;
                        height: 32px;
                        line-height: 32px;
                        box-sizing: border-box;
                    }
                    .el-select {
                        height: 32px;
                        line-height: 32px;
                    }
                }
                .wxnum {
                    .inputs {
                        background-color: #ffffff;
                        border-radius: 4px;
                        input {
                            display: block;
                            width: 100%;
                            height: 100%;
                            font-size: 14px;
                        }
                    }
                }
                .phone {
                    width: 262px;
                    padding-left: 62px;
                    label {
                        width: 62px;
                    }
                    .inputs {
                        background-color: #ffffff;
                        border-radius: 4px;
                        input {
                            display: block;
                            width: 100%;
                            height: 100%;
                            font-size: 14px;
                        }
                    }
                }
                .state {
                    width: 248px;
                    padding-left: 48px;
                    label {
                        width: 48px;
                    }
                }
                .wxnum,
                .companys,
                .results {
                    width: 276px;
                    padding-left: 76px;
                    label {
                        width: 76px;
                    }
                }
            }
            margin-bottom: 20px;
        }
        .mainBody {
            width: 100%;
            .tableDataFrame {
                color: #666;
            }
            .userinfoDiv {
                width: 100%;
                height: 44px;
                position: relative;
                padding-left: 54px;
                box-sizing: border-box;
                img {
                    position: absolute;
                    width: 44px;
                    height: 44px;
                    border-radius: 50%;
                    top: 0;
                    left: 0;
                    z-index: 1;
                }
                p {
                    font-size: 14px;
                    line-height: 1;
                    text-overflow: ellipsis;
                    overflow: hidden;
                    white-space: nowrap;
                    &:first-child {
                        margin-bottom: 10px;
                    }
                }
            }
        }
        .formRows {
            width: 100%;
            height: 100%;
            position: fixed;
            left: 0;
            top: 0;
            background: rgba(0, 0, 0, 0.7);
            z-index: 9999;
            .formBody {
                width: 620px;
                padding: 54px 0 52px;
                background: #fff;
                box-sizing: border-box;
                border-radius: 4px;
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
                .titles {
                    width: 100%;
                    height: 54px;
                    line-height: 54px;
                    box-sizing: border-box;
                    padding: 0 24px;
                    font-size: 16px;
                    color: #333333;
                    position: absolute;
                    text-align: left;
                    top: 0;
                    left: 0;
                    border-bottom: 1px solid #eaeaea;
                    i {
                        position: absolute;
                        width: 54px;
                        height: 54px;
                        line-height: 54px;
                        text-align: center;
                        right: 0;
                        color: #8c8c8c;
                        font-size: 12px;
                        top: 0;
                        cursor: pointer;
                    }
                }
                .bodys {
                    width: 100%;
                    padding: 30px;
                    font-size: 14px;
                    text-align: left;
                    .userRows {
                        width: 100%;
                        height: 80px;
                        background-color: #f5f5f5;
                        padding: 20px;
                        padding-left: 80px;
                        position: relative;
                        margin-bottom: 20px;
                        img {
                            position: absolute;
                            width: 48px;
                            height: 48px;
                            border-radius: 50%;
                            left: 20px;
                            top: 16px;
                        }
                        div {
                            width: 50%;
                            float: left;
                            height: 100%;
                            p {
                                width: 100%;
                                line-height: 1;
                                text-align: left;
                                &:first-child {
                                    margin-bottom: 7px;
                                }
                            }
                        }
                    }
                    .txtTitles {
                        line-height: 1;
                        position: relative;
                        margin-bottom: 16px;
                        span {
                            position: absolute;
                            top: 0;
                            right: 0;
                        }
                    }
                    .feedbackRows {
                        width: 100%;
                        padding: 20px;
                        background-color: #f5f5f5;
                        margin-bottom: 20px;
                        .txts {
                            line-height: 20px;
                            display: block;
                            width: 70px;
                            text-align: left;
                            word-break: break-all;
                        }
                        .items {
                            width: 100%;
                            position: relative;
                            padding-left: 76px;
                            margin-bottom: 17px;
                            color: #333;
                            font-size: 14px;
                            min-height: 14px;
                            &:last-child {
                                margin: 0;
                            }
                            span {
                                text-align: right;
                                display: block;
                                width: 70px;
                                position: absolute;
                                left: 0;
                                top: 0;
                                color: #666666;
                            }
                            .imgRows {
                                height: 106px;
                                .imgItems {
                                    width: 106px;
                                    height: 106px;
                                    float: left;
                                    margin-right: 6px;
                                    &:last-child {
                                        margin: 0;
                                    }
                                    position: relative;
                                    &:hover .zzcs {
                                        opacity: 1;
                                    }
                                    img {
                                        width: 100%;
                                        height: 100%;
                                    }
                                    .zzcs {
                                        width: 100%;
                                        height: 100%;
                                        position: absolute;
                                        left: 0;
                                        top: 0;
                                        background: rgba(0, 0, 0, 0.7);
                                        opacity: 0;
                                        transition-duration: .3s;
                                        .btns {
                                            width: 72px;
                                            height: 24px;
                                            text-align: center;
                                            line-height: 24px;
                                            cursor: pointer;
                                            color: #fff;
                                            font-size: 12px;
                                            position: absolute;
                                            left: 50%;
                                            top: 50%;
                                            margin-left: -36px;
                                            margin-top: -12px;
                                            background-color: #3399ff;
                                            border-radius: 12px;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    .jieguo {
                        margin-bottom: 16px;
                        height: 14px;
                        line-height: 1;
                        p {
                            width: 50%;
                            float: left;
                        }
                    }
                    .he156 {
                        height: 156px;
                    }
                    .fkui {
                        width: 100%;
                        padding-left: 80px;
                        position: relative;
                        min-height: 24px;
                        line-height: 24px;
                        span.txs {
                            position: absolute;
                            left: 0;
                            top: 0;
                            color: #666666;
                        }
                        .rowTxs {
                            line-height: 20px;
                            word-wrap: break-word;
                        }
                        .rowInp {
                            width: 100%;
                            height: 100%;
                            background-color: #ffffff;
                            border-radius: 4px;
                            border: solid 1px #d9d9d9;
                            padding: 0 10px;
                            padding-bottom: 22px;
                            textarea {
                                max-width: 100%;
                                max-height: 100%;
                                min-width: 100%;
                                min-height: 100%;
                                line-height: 24px;
                                border: none;
                                font-size: 14px;
                                color: #333;
                                background: rgba(0, 0, 0, 0);
                                resize: none;
                            }
                            .counts {
                                position: absolute;
                                right: 10px;
                                bottom: 10px;
                                font-size: 12px;
                                line-height: 1;
                                color: #999999;
                            }
                        }
                    }
                }
                .bnts {
                    width: 100%;
                    text-align: right;
                    height: 52px;
                    line-height: 52px;
                    position: absolute;
                    left: 0;
                    bottom: 0;
                    border-top: 1px solid #eaeaea;
                    padding-right: 20px;
                }
            }
        }
        .delView {
            width: 100%;
            height: 100%;
            position: fixed;
            left: 0;
            top: 0;
            background: rgba(0, 0, 0, 0.7);
            z-index: 9999;
            .delRows {
                width: 620px;
                padding: 54px 0 52px;
                background: #fff;
                border-radius: 4px;
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
                .conts {
                    padding: 40px 72px;
                    font-size: 14px;
                    line-height: 1;
                    color: #333333;
                }
                .titles {
                    width: 100%;
                    height: 54px;
                    line-height: 54px;
                    box-sizing: border-box;
                    padding: 0 24px;
                    font-size: 16px;
                    color: #333333;
                    position: absolute;
                    text-align: left;
                    top: 0;
                    left: 0;
                    border-bottom: 1px solid #eaeaea;
                    i {
                        position: absolute;
                        width: 54px;
                        height: 54px;
                        line-height: 54px;
                        text-align: center;
                        right: 0;
                        color: #8c8c8c;
                        font-size: 12px;
                        top: 0;
                        cursor: pointer;
                    }
                }
                .bnts {
                    width: 100%;
                    text-align: right;
                    height: 52px;
                    line-height: 52px;
                    position: absolute;
                    left: 0;
                    bottom: 0;
                    border-top: 1px solid #eaeaea;
                    padding-right: 20px;
                }
            }
        }
    }
    
    .decorCustomer {
        padding: 20px;
    }
    
    .contractContent {
        padding-top: 30px;
    }
    
    .rejectReson {
        width: 372px;
    }
    
    .leftTitle {
        vertical-align: top;
    }
    
    .pageCount {
        text-align: right;
        background: #fff;
        padding: 20px;
    }
    
    .listTitle {
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
        .right {
            @extend .exportExcel
        }
    }
    
    .exportExcel {
        position: absolute;
        right: 32px;
        top: 0;
        span {
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
    
    .tableDataFrame {
        padding: 0 20px;
        background: #fff;
    }
    
    .topBar {
        background: #fff;
        div {
            text-align: left;
        }
        .footer {
            margin-top: 20px;
            border-top: solid 1px #e8e8e8;
            text-align: center;
            padding: 20px 0;
            display: block;
        }
        .barTitle {
            border-bottom: solid 1px #e8e8e8;
            padding-left: 32px;
            font-size: 16px;
            color: #333;
            height: 56px;
            line-height: 56px;
            position: relative;
            .right {
                position: absolute;
                right: 32px;
                top: 0;
                span {
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
        }
    }
    
    .searchOption {
        padding-left: 32px;
        .el-select,
        .pickDate,
        .el-input,
        .el-cascader {
            width: 220px;
            margin-top: 20px;
            margin-right: 10px;
        }
    }
    
    .queryBtn {
        height: 30px;
        width: 62px;
        display: inline-block;
        text-align: center;
        line-height: 30px;
        border-radius: 4px;
        color: #fff;
        background: #3a8ee6;
        cursor: pointer;
        font-size: 14px;
        margin-left: 40px;
        margin-right: 10px;
    }
    
    .queryReset {
        height: 30px;
        width: 62px;
        font-size: 14px;
        display: inline-block;
        text-align: center;
        line-height: 30px;
        border-radius: 4px;
        border: solid 1px #d9d9d9;
        color: #999;
        cursor: pointer;
    }
    
    .ownerInfo {
        margin-top: 20px;
        background: #fff;
        border-radius: 4px;
        height: 214px;
        text-align: left;
        ul {
            height: 64px;
            line-height: 64px;
            border-bottom: solid 1px #e8e8e8;
            li {
                display: inline-block;
                margin-left: 50px;
                margin-right: 50px;
            }
        }
        div {
            background: #fff;
            height: 150px;
        }
    }

.add-activity {
  padding: 30px;
  .main-body {
    min-height: 860px;
    background: #fff;
    .title,
    .breadcrumb-box {
      font-size: 18px;
      padding: 15px;
      border-bottom: 1px solid #eee;
      text-align: left;
    }
    .el-row {
      text-align: left;
    }
    .el-breadcrumb__inner a,
    .el-breadcrumb__inner.is-link {
      font-size: 18px;
      color: #222;
      font-weight: normal;
    }
    .el-breadcrumb__item:last-child .el-breadcrumb__inner {
      font-size: 18px;
      color: #222;
      font-weight: normal;
    }
    .form-box {
      margin: 0 auto;
      margin-top: 30px;
      width: 80%;
      .el-input,
      .el-select {
        width: 200px;
      }
      .name-input {
        width: 250px;
      }
      .el-date-editor {
        width: 400px;
      }
      .el-form-item {
        display: flex;
        justify-content: start;
        align-items: center;
      }
      .el-form-item__label {
        font-size: 16px;
      }
      .el-form-item__content {
        font-size: 16px;
      }
      .choose-brand {
        margin-left: 80px;
      }
      .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
      }
      .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
        object-fit: cover;
      }
      .avatar {
        width: 178px;
        height: 178px;
        display: block;
        object-fit: cover;
      }

      .btn-normal-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 262px;
        height: 60px;
        .avatar {
          width: 262px;
          height: 60px;
          display: block;
          object-fit: cover;
        }

        i {
          width: 262px;
          height: 60px;
          line-height: 50px;
          text-align: center;
          display: inline-block;
        }
      }
    }
    .el-form-item__content {
      line-height: 60px;
    }
    .submit-btn {
      margin-left: 30px;
    }
    .star {
      position: relative;
      .el-form-item__label {
        padding-left: 8px;
      }
      &::before {
        content: "*";
        color: #f56c6c;
        margin-right: 4px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        // left: -2px;
        left: 47px;
        width: 14px;
        height: 14px;
      }
    }
  }
  .store-list {
    display: flex;
    align-items: center;
    button {
      margin-left: 30px;
    }
  }
  .shop-logo {
    height: 50px;
    max-width: 80px;
    object-fit: contain;
  }
  .add-areaselect {
    display: inline-block;
    cursor: pointer;
    color: #3399ff;
    width: 70px;
    text-align: left;
  }
  .btn-box {
    padding: 30px 0;
  }
}
</style>