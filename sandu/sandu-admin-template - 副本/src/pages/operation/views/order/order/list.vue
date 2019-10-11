<template>
    <div class="main">
        <!--条件查询-->
        <div class="mainTop">
            <div class="titles">订单管理</div>
            <div class="selRows" style="height: 50px;">
                <div class="selItems">
                    <div class="inputs">
                        <el-input placeholder="请输入订单号" maxlength="30" v-model="getListParams.orderCodeLike" clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems">
                    <div class="inputs">
                        <el-input placeholder="请输入商品名称" maxlength="30" v-model="getListParams.productNameLike" clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems">
                    <div class="inputs">
                        <el-input placeholder="请输入联系电话" maxlength="30" v-model="getListParams.mobileLike" clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems">
                    <div class="el-select">
                        <el-select size="medium" v-model="getListParams.orderStatus" placeholder="请选择订单状态" :clearable="true" class="select">
                            <el-option
                                v-for="item in orderStatusList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="selItems">
                    <div class="el-select">
                        <el-select size="medium" v-model="getListParams.payStatus" placeholder="请选择支付状态" :clearable="true">
                            <el-option
                                v-for="item in payStatusList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="selItems">
                    <div class="el-select">
                        <el-select size="medium" v-model="getListParams.shippingType" placeholder="请选择发货方式" :clearable="true">
                            <el-option
                                v-for="item in ShippingTypeList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                </div>
            </div>
            <div class="selRows">
                <div class="selItems">
                    <div class="el-select">
                        <el-select size="medium" v-model="getListParams.orderType" placeholder="请选择订单类型" :clearable="true">
                            <el-option
                                v-for="item in OrderTypeList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="selItems">
                    <div class="el-select">
                        <el-date-picker v-model="createTimeStr"
                            type="daterange"
                            format="yyyy 年 MM 月 dd 日"
                            value-format="yyyy-MM-dd"
                            range-separator="-"
                            start-placeholder="请选择下单时间"
                            end-placeholder="请选择下单时间" style="width: 420px">
                        </el-date-picker>
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
                    <div class="inputs">
                        <el-input placeholder="分配的店铺" maxlength="30" v-model="getListParams.shopNameLike" clearable>
                        </el-input>
                    </div>
                </div>
                <div class="submitBtn" @click="getOrderList(1)">搜索</div>
                <div class="submitBtn" @click="resetAndSelect()">重置</div>

            </div>
        </div>

        <!-- 返回信息列表 -->
        <div class="mainBody">
            <section>
                <div class="listTitle">
                    <div class="left">
                        <el-button @click="exportOrderList()" size="small" type="primary">导出Excel</el-button>
                    </div>
                </div>
                <div class="tableDataFrame">
                    <el-table :data="orderList" :header-cell-style="rowStyle" style="padding-top:20px"
                        element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)"
                        tooltip-effect="dark">
                        <el-table-column type="index" width="50px" label="序号">
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="订单号" width="200px">
                            <template slot-scope="scope">{{scope.row.orderCode}}</template>
                        </el-table-column>
                        <!-- <el-table-column prop="feedbackTxt" label="商品名称" width="200px">
                            <template slot-scope="scope">{{scope.row.productName}}</template>
                        </el-table-column> -->
                        <el-table-column prop="feedbackTxt" label="支付状态" width="80px">
                            <template slot-scope="scope">{{scope.row.payStatusInfo}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="订单状态" width="80px">
                            <template slot-scope="scope">{{scope.row.orderStatusInfo}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="商品配送情况" width="120px">
                            <template slot-scope="scope">{{scope.row.shippingStatusInfo}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="订单价格" width="80px">
                            <template slot-scope="scope">{{scope.row.orderAmount}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="所属企业" width="200px">
                            <template slot-scope="scope">{{scope.row.companyName}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="分配店铺" width="200px">
                            <template slot-scope="scope">{{scope.row.shopName}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="订单类型" width="80px">
                            <template slot-scope="scope">{{scope.row.orderTypeInfo}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="收货人信息" width="220px">
                            <template slot-scope="scope">{{scope.row.consigneeInfoName}}<br/>{{scope.row.consigneeInfoMobile}}<br/>{{scope.row.consigneeInfoAddress}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="下单时间" width="100px">
                            <template slot-scope="scope">{{scope.row.createTimeInfo}}</template>
                        </el-table-column>
                        <el-table-column prop="feedbackTxt" label="发货方式" width="80px">
                            <template slot-scope="scope">{{scope.row.shippingTypeInfo}}</template>
                        </el-table-column>
                        <el-table-column label="操作" width="100px">
                            <template slot-scope="scope">
                                <el-button type="text" @click="goToDetail(scope.row.id)">详情</el-button>
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
                // 查询条件 ->start
                // orderCodeLike: undefined,
                // productNameLike: undefined,
                // 查询条件 ->end
                createTimeStr: undefined,
                orderList: [],
                listPage:1,
                totalCount:0,
                companyList: [],
                orderStatusList: [
                    {value: 0, label: "待付款"},
                    {value: 7, label: "待确认"},
                    {value: 1, label: "已确认"},
                    {value: 2, label: "已取消"},
                    /*{value: 3, label: "无效"},*/
                    {value: 4, label: "交易完成"},
                    /*{value: 5, label: "退货"},*/
                    {value: 6, label: "待成团"},
                    {value: 10, label: "结算中"},
                    {value: 11, label: "已结算"}
                ],
                payStatusList: [
                    {value: 0, label: "待支付"},
                    /*{value: 1, label: "付款中"},*/
                    {value: 2, label: "已支付"}
                ],
                ShippingTypeList: [
                    {value: 1, label: "快递"},
                    {value: 2, label: "上门自提"}
                ],
                ShippingStatusList: [
                    {value: 0, label: "未发货"},
                    {value: 1, label: "已发货"},
                    {value: 2, label: "已收货"},
                    {value: 3, label: "退货"}
                ],
                OrderTypeList: [
                    {value: 0, label: "普通订单"},
                    {value: 1, label: "拼团订单"}
                ],
                getListParams: {
                    orderCodeLike: undefined,
                    productNameLike: undefined,
                    mobileLike: undefined,
                    orderStatus: undefined,
                    payStatus: undefined,
                    shippingType: undefined,
                    orderType: undefined,
                    companyId: undefined,
                    shopNameLike: undefined,
                    createTimeStart: undefined,
                    createTimeEnd: undefined,
                    page: 1, // 页码
                    limit: 10 // 每页条数
                }
            }
        },
        watch: {

        },
        components: {
            
        },
        created() {
            this.getOrderList();
            this.getCompanyList();
        },
        methods: {
            goToDetail(id) {
                // localStorage.setItem("breadcrumb3", "订单详情");
                this.$router.push({ path: `/operation/order/order/orderDetail/${id}` });
            },
            exportOrderList() {
                if (this.createTimeStr != undefined) {
                    this.getListParams.createTimeStart = this.createTimeStr[0];
                    this.getListParams.createTimeEnd = this.createTimeStr[1];
                }

                this.API.exportOrderList(this.getListParams).then(res => {
                    let url=window.URL.createObjectURL(res);
                    let link=document.createElement('a');
                    link.style.display="none";
                    link.href=url;
                    link.setAttribute('download','订单列表导出.xls');
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
                this.getOrderList()
            },
            handleCurrentChange: function (currentPage) {
                //this.query.page = currentPage;
                this.getListParams.page = currentPage;
                this.loading = true;
                this.getOrderList()
            },
            getOrderList(type) {
                console.error(type);
                if (1 == type) {
                    // 重置page
                    this.listPage = 1;
                    this.getListParams.page = 1;
                }

                this.orderList = [];
                this.totalCount = 0;

                if (this.createTimeStr != undefined) {
                    this.getListParams.createTimeStart = this.createTimeStr[0];
                    this.getListParams.createTimeEnd = this.createTimeStr[1];
                } else {
                    this.getListParams.createTimeStart = undefined;
                    this.getListParams.createTimeEnd = undefined;
                }

                this.API.getOrderList(this.getListParams).then(res => {
                    if (res.success) {
                        this.listPage = this.getListParams.page;
                        this.orderList = res.datalist;
                        this.totalCount = res.totalCount;
                    }
                })
            },
            resetAndSelect() {
                this.reset();
                this.getOrderList();
            },
            reset() {
                this.getListParams.orderCodeLike = undefined;
                this.getListParams.productNameLike = undefined;
                this.getListParams.mobileLike = undefined;
                this.getListParams.orderStatus = undefined;
                this.getListParams.payStatus = undefined;
                this.getListParams.shippingType = undefined;
                this.getListParams.orderType = undefined;
                this.createTimeStr = undefined;
                this.getListParams.createTimeStart = undefined;
                this.getListParams.createTimeEnd = undefined;
                this.getListParams.companyId = undefined;
                this.getListParams.shopNameLike = undefined;
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
</style>