<!--业主信息编辑-->
<template>
    <div class="interReferal">
        <header class="header">
            <p class="navtext">推广信息/业主基础信息/编辑</p>
            <h1 class="edittitle">内部推荐
                <div class="right">
                    <img src="../../assets/images/icon/icon_back.png" @click="toDecorateCustomerList" alt="返回">
                </div>
            </h1>
        </header>
        <div class="userinfo">
            <h1 class="title">客户信息</h1>
            <div class="textbox">
                <p class="textrow">
                    <span>姓名：{{customerInfo.userName}}</span>
                    <span>城市：{{customerInfo.cityName}}</span>
                    <span>小区：{{customerInfo.areaName}}</span>
                    <span>详细地址：{{customerInfo.address}}</span>
                </p>
                <p class="textrow">
                    <span>户型：{{customerInfo.houseType}}</span>
                    <span>面积(㎡)：{{customerInfo.houseAcreage}}</span>
                    <span>预算(元)：{{customerInfo.decorateBudgetStr}}</span>
                    <span>装修类型：{{customerInfo.decorateHouseTypeStr}}</span>
                    <span>装修方式：{{customerInfo.decorateTypeStr}}</span>
                    <span>倾向风格：{{customerInfo.decorateStyleStr}}</span>
                </p>
            </div>
        </div>
        <section>
            <div class="choosecomp">
                选择装企：<span class="comName" v-if="this.chooseCompanyName">{{this.chooseCompanyName }}  X</span>
            </div>
            <div class="searchbox">
             <span>
                 城市：
                 <!---->
                 <el-cascader
                         v-model="checkedCityOptions"
                         expand-trigger="hover"
                         :options="cityOptions"
                         :props="props"
                         :clearable=true
                         :filterable=true
                         @change="handleChange">
                 </el-cascader>

             </span>
                <span>
                 擅长风格：
                     <el-select v-model="searchOption.goodStyle" size="small" placeholder="请选择擅长风格">
                        <el-option v-for="item in styleOptions" :key="item.id" :label="item.name"
                                   :value="item.id">
                        </el-option>
                    </el-select>
             </span>
                <span>
                 装修费用：
                     <el-select v-model="searchOption.costRage" size="small" placeholder="请选择装修费用">
                        <el-option v-for="item in decorateBudgetOptions" :key="item.id" :label="item.name"
                                   :value="item.id">
                        </el-option>
                    </el-select>
             </span>
                <span>
                 装企名称：<el-input v-model="searchOption.companyName"></el-input>
             </span>
                <span>
                 <el-button size="medium" type="primary" @click="getInnerRecommendList">查询</el-button>
                    <!--<el-button size="medium" type="primary">保存</el-button>-->
                  <el-button size="medium" @click="resetSearchOption">重置</el-button>
             </span>
            </div>
            <div class="tableDataFrame">


                <el-table
                        @row-click="changeState"
                        @sort-change="getInnerRecommendList"
                        :data="dataList"
                        :header-cell-style="rowStyle"
                        style="padding-top:20px"
                        element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(255, 255, 255, 0.8)"
                        tooltip-effect="dark">

                    <el-table-column>
                        <template slot-scope="scope">
                            <el-checkbox v-model="scope.row.chooseState"></el-checkbox>
                        </template>
                    </el-table-column>
                    <el-table-column type="index" :index="indexMethod" label="序号" align="center"></el-table-column>
                    <el-table-column prop="companyName" label="装企名称" align="center"></el-table-column>
                    <el-table-column prop="cityName" label="城市" align="center"></el-table-column>
                    <el-table-column prop="goodStyleName[0]" label="擅长风格" align="center"></el-table-column>
                    <el-table-column prop="costRange" label="装修费用(元)" align="center"></el-table-column>
                    <el-table-column prop="dealNumber" label="成交量" align="center" sortable="custom"></el-table-column>
                    <el-table-column prop="praiseRates" label="好评率" align="center" sortable="custom"></el-table-column>
                    <el-table-column prop="overage" label="余额" align="center"></el-table-column>
                </el-table>
                <div class="pageCount">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page.sync="searchOption.page"
                            :page-sizes="[5, 10, 50, 100, 200,500]"
                            :page-size="searchOption.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="total">
                    </el-pagination>
                </div>
                <div class="footerButton">
                    <el-button type="primary" size="mini" @click="updateCustomerCompanyShop">保存</el-button>
                    <el-button size="mini" @click="toDecorateCustomerList">取消</el-button>
                </div>
            </div>
        </section>
    </div>
</template>

<script type="text/ecmascript-6">
    export default {
        data() {
            return {

                id: this.$route.params.id,
                dataList: [
                    {
                        name: "lshc",
                        phone: "12345678900",
                        city: "深圳",
                        type: 1
                    },
                    {
                        name: "lshc",
                        phone: "12345678900",
                        city: "深圳",
                        type: 1
                    }
                ],
                chooseCompanyId: 0,
                cityOptions: [],
                checkedCityOptions: [],
                props: {
                    value: 'id',
                    label: 'name',
                    children: 'children'
                },
                chooseCompanyName: '',
                styleOptions: [],
                decorateBudgetOptions: [],
                customerInfo: {
                    address: null,
                    areaCode: null,
                    areaName: null,
                    cityName: null,
                    decorateBudget: null,
                    decorateBudgetStr: null,
                    decorateHouseType: null,
                    decorateHouseTypeStr: null,
                    decorateStyle: null,
                    decorateStyleStr: null,
                    decorateType: null,
                    decorateTypeStr: null,
                    houseAcreage: null,
                    houseType: null,
                    id: null,
                    remark: null,
                    revisitTime: null,
                    userName: null
                },
                searchOption: {
                    goodStyle: null,
                    costRage: null,
                    cityCode: null,
                    companyName: null,
                    houseAcreage: null,
                    page: 1,
                    limit: 10,
                    orderBy: 'dealNumber',
                    orderMethod: 'desc',
                },
                total: 0,
            };
        },
        created() {
            this.getCustomerInfoWithInner()
            this.initOptions()
        },
        methods: {
            getInnerRecommendList(column) {
                console.log(column)
                this.searchOption.houseAcreage = this.customerInfo.houseAcreage
                if (column) {
                    this.searchOption.orderBy = column.prop
                    this.searchOption.orderMethod = column.order === 'ascending' ? 'asc' : 'desc'
                }
                this.API.getInnerRecommendList(this.searchOption).then(res => {
                        if (res) {
                            this.total = res.total
                            this.dataList = res.list
                            if (this.dataList.length) {
                                this.dataList.map(it => {
                                    it.praiseRates = it.praiseRates.slice(0, 3)
                                })
                            }

                        }
                    }
                )
            },
            getCustomerInfoWithInner() {
                this.API.getCustomerInfoWithInner(this.id).then(res => {
                        if (res) {
                            this.customerInfo = res.data
                            this.searchOption.houseAcreage = this.customerInfo.houseAcreage
                            this.getInnerRecommendList()
                        }
                    }
                )
            },
            rowStyle({row, rowIndex}) {
                if (rowIndex === 0) {
                    return {
                        height: "30px!important",
                        background: "#f5f5f5",
                        color: "#666",
                        lineHeight: "80px!important",
                    };
                }
            },
            initOptions() {
                this.API.initOptionsByType('decorateBudget').then(res => {
                        if (!res) return;
                    this.decorateBudgetOptions = res.data
                    }
                )
                this.API.initCityTree().then(res => {
                    if (!res) return;
                    this.cityOptions = res.data
                })
                this.API.initOptionsByType('goodStyle').then(res => {
                    if (!res) return;
                    this.styleOptions = res.data
                })
            },
            resetSearchOption() {
                for (let searchOptionKey in this.searchOption) {
                    if (searchOptionKey === 'page' || searchOptionKey === 'limit') {
                        continue
                    }
                    this.checkedCityOptions = []
                    this.searchOption[searchOptionKey] = undefined
                }
                this.getInnerRecommendList()
            },
            //内部推荐
            updateCustomerCompanyShop() {
                let tmp = this.dataList.find(it => it.chooseState)
                if (tmp) {
                    this.API.updateCustomerCompanyShop({
                        customerId: this.id,
                        companyId: tmp.companyId
                    }).then(res => {
                        if (!res) return;
                        if (res.success) {
                            this.$message.success(res.message)
                        } else {
                            this.$message.error(res.message)
                        }
                        this.$router.push('/decorCustomer');
                    })
                } else {
                    this.$message.error("请选择装企 ")
                }
            },
            // 分页模块
            handleSizeChange: function (size) {
                this.searchOption.limit = size
                this.getCustomerInfoWithInner()
            },
            handleCurrentChange: function (currentPage) {
                this.searchOption.page = currentPage
                this.getCustomerInfoWithInner()
            },
            handleChange(val) {
                this.checkedCityOptions = val
                this.searchOption.cityCode = val[val.length - 1]

            },
            indexMethod(index) {
                return index + 1;
            },
            toDecorateCustomerList() {
                this.$router.push('/decorCustomer')
            },
            changeState(item) {
                item.chooseState = !item.chooseState
                if (item.chooseState) {
                    this.chooseCompanyName = item.companyName
                } else {
                    this.chooseCompanyName = ''
                }
                for (let index in this.dataList) {
                    if (this.dataList[index].companyId === item.companyId) {
                        this.dataList[index].chooseState = item.chooseState
                        this.$set(this.dataList, index, this.dataList[index])
                    } else {
                        this.dataList[index].chooseState = false
                    }
                }
            },
        }
    }
</script>

<style scoped lang="scss">
    @import "../../assets/css/common.scss";

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

    .interReferal {
        .header {
            width: 100%;
            height: 100px;
            background: white;
            .navtext {
                text-align: left;
                color: #999999;
                font-size: 14px;
                padding: 20px 0 0 32px;
            }
            .edittitle {
                text-align: left;
                font-size: 18px;
                color: #333333;
                padding: 20px 0 0 32px;
                position: relative;
                .right {
                    @extend .exportExcel
                }
            }
        }
        .userinfo {
            background: white;
            height: 160px;
            margin: 20px;
            border-radius: 4px;
            .title {
                height: 55px;
                line-height: 55px;
                font-size: 16px;
                text-align: left;
                padding-left: 32px;
                border-bottom: 1px solid #e8e8e8;
                box-sizing: border-box;
            }
            .textbox {
                height: 105px;
                .textrow {
                    text-align: left;
                    margin-left: 80px;
                    margin-top: 24px;
                    > span {
                        display: inline-block;
                        min-width: 240px;
                        color: #333333;
                        font-size: 14px;
                    }
                }
            }
        }
        section {
            margin: 20px;
            border-radius: 4px;;
            background: white;
            .choosecomp {
                height: 56px;
                line-height: 56px;
                font-size: 16px;
                color: #333333;
                text-align: left;
                text-indent: 32px;
                border-bottom: 1px solid #e8e8e8;
                box-sizing: border-box;
                .comName {
                    border: 1px solid #d9d9d9;
                    box-sizing: border-box;
                    font-size: 14px;
                    color: #666666;
                    padding: 4px;
                    border-radius: 4px;
                    margin-left: 10px;
                }
            }
            .searchbox {
                text-align: left;
                padding: 24px 0;
                .el-input {
                    width: 200px;
                }
                > span {
                    display: inline-block;
                    margin-left: 32px;
                }
            }
            .tableDataFrame {
                padding: 0 20px;
            }
        }
    }
</style>
