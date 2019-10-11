<template>
    <transition name="fade">
        <div class="decorCustomer">
            <header class="topBar">
                <div class="barTitle">
                    <span>装修客户查询</span>
                </div>
                <div class="searchOption">
                    <el-input
                            placeholder="请输入业主姓名"
                            v-model="searchOption.userName"
                            size="small" clearable style="width: 222px"
                            @blur="initTable"
                            clearable>
                    </el-input>
                    <el-input
                            placeholder="请输入业主联系电话"
                            v-model="searchOption.mobile"
                            size="small" clearable style="width: 222px"
                            @blur="initTable"
                            clearable>
                    </el-input>
                    <el-cascader
                            size="small"
                            v-model="checkedCityOptions"
                            expand-trigger="hover"
                            :options="cityOptions"
                            :props="props"
                            :clearable=true
                            :filterable=true
                            @change="handleChange">
                    </el-cascader>

                    <el-select v-model="searchOption.decorateType" @change="initTable" size="small" clearable
                               placeholder="请选择装修类型">
                        <el-option v-for="item in decorateTypeOptions"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                    </el-select>

                    <el-date-picker class="pickDate" v-model="searchOption.revisitTimeStr"
                                    type="date" size="small"
                                    value-format="yyyy-MM-dd"
                                    format="yyyy-MM-dd"
                                    @change="initTable"
                                    :picker-options="pickerOptions1" placeholder="选择日期下次回访日期">
                    </el-date-picker>

                </div>
                <div class="footer">
                    <span class="queryBtn" v-on:click="initTable">查询</span>
                    <span class="queryReset" @click="resetSearchParam">重置</span>
                </div>
            </header>
            <section>
                <div class="listTitle">客户信息
                    <div class="right">
                        <el-button @click="handleDownload" size="small" type="primary" :disabled="!multipleSelection.length">导出Excel</el-button>
                    </div>
                </div>
                <div class="tableDataFrame">
                    <el-table :data="tableDataList"
                              :header-cell-style="rowStyle"
                              @selection-change="handleSelectionChange"
                              style="padding-top:20px"
                              element-loading-text="拼命加载中"
                              element-loading-spinner="el-icon-loading"
                              element-loading-background="rgba(255, 255, 255, 0.8)"
                              tooltip-effect="dark">
                        <el-table-column type="selection"></el-table-column>
                        <el-table-column type="index" :index="initTableIndex" label="序号"></el-table-column>
                        <el-table-column label="名称">
                            <template slot-scope="scope" v-if="scope.row">
                                <el-popover
                                        placement="top-start"
                                        trigger="hover">
                                    <div v-if="scope.row.proprietorInfo">
                                        <li>姓名 : {{scope.row.proprietorInfo.userName}}</li>
                                        <li>城市 : {{scope.row.proprietorInfo.cityName}}</li>
                                        <li>小区 : {{scope.row.proprietorInfo.areaName}}</li>
                                        <li>详细地址 : {{scope.row.proprietorInfo.address}}</li>
                                        <li>户型面积 : {{scope.row.proprietorInfo.houseAcreage}} ㎡</li>
                                        <li>预算 : {{scope.row.proprietorInfo.decorateBudgetStr}}</li>
                                        <li>装修类型 : {{scope.row.proprietorInfo.decorateHouseTypeStr}}</li>
                                        <li>装修方式 : {{scope.row.proprietorInfo.decorateTypeStr}}</li>
                                        <li>倾向风格 : {{scope.row.proprietorInfo.decorateStyleStr}}</li>
                                    </div>
                                    <div slot="reference">
                                        <span>{{scope.row.userName}}</span>
                                    </div>
                                </el-popover>
                            </template>
                        </el-table-column>
                        <el-table-column prop="mobile" label="电话" width="150px"></el-table-column>
                        <el-table-column prop="cityName" label="城市"></el-table-column>
                        <el-table-column prop="companyName" label="客户预约企业"></el-table-column>
                        <el-table-column prop="firstSecKillCompanyName" label="第一家抢单企业"></el-table-column>
                        <el-table-column prop="secondSecKillCompanyName" label="第二家抢单企业"></el-table-column>

                        <el-table-column width="120px" label="派单报价装企及报价(10家)">
                            <template slot-scope="scope" v-if="scope.row.allPriceList.length">
                                <el-popover
                                        placement="top-start"
                                        trigger="hover"
                                >
                                    <ul class="listCompanyName">
                                        <li v-for="(item,index) in scope.row.allPriceList" :key="index">
                                            <span>{{item.name}}&emsp; {{item.price}}万</span>
                                        </li>
                                    </ul>
                                    <div class="liName" slot="reference">
                                        <p>{{scope.row.allPriceList[0].name}}
                                         &emsp;
                                        </p>
                                        <p>{{scope.row.allPriceList[0].price}}万</p>
                                        ...
                                    </div>
                                </el-popover>
                            </template>
                        </el-table-column>

                        <el-table-column prop="choosePriceList[0]" width="120px" label="派单选定装企及报价(3家)">
                            <template slot-scope="scope" v-if="scope.row.choosePriceList.length">

                                <el-popover
                                        placement="top-start"
                                        trigger="hover"
                                >
                                    <ul class="listCompanyName">
                                        <li v-for="(item,index) in scope.row.choosePriceList" :key="index">
                                            <span>{{item.name}}&emsp; {{item.price}}万</span>
                                        </li>
                                    </ul>
                                    <div class="liName" slot="reference">
                                        <p>{{scope.row.choosePriceList[0].name}}
                                        &emsp;
                                        </p>
                                        <p>{{scope.row.choosePriceList[0].price}}万</p>
                                        ...
                                    </div>
                                </el-popover>
                            </template>
                        </el-table-column>

                        <el-table-column prop="innerCompanyName" label="内部推荐企业"></el-table-column>
                        <el-table-column prop="decorateTypeName" label="装修类型"></el-table-column>
                        <el-table-column prop="revisitTime" label="下次回访日期" width="200px"></el-table-column>
                        <el-table-column prop="remark" label="备注"></el-table-column>
                        <el-table-column prop="name" label="操作" width="200px">
                            <template slot-scope="scope">
                                <el-button type="text" @click="showEditDialog(scope.row)" v-if="permission('decorate:decorateCustomer:edit')">编辑
                                </el-button>
                                <el-button type="text" @click="innerRecommend(scope.row.id)" v-if="permission('decorate:decorateCustomer:manage')">内部推荐
                                </el-button>
                                <el-button type="text" @click="submitToSedKill(scope.row.id)"
                                           v-if="scope.row.firstSecKillCompanyName && !scope.row.secondSecKillCompanyName&&permission('decorate:decorateCustomer:manage')">
                                    提交抢单
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pageCount">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page.sync="searchOption.page"
                                :page-sizes="[10, 50, 100, 200,500]"
                                :page-size="searchOption.limit"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="total">
                        </el-pagination>
                    </div>
                </div>
            </section>
            <el-dialog title="编辑" :visible.sync="dialogVisible" width="620px">
                <div class="diabox">
                    <div class="box">
                        <p class="text"><span class="star">*</span>装修进度：</p>
                        <el-select v-model="editParam.decorateType" size="small" placeholder="请选择装修进度" class="area"
                                   @change="getCompanyByCustomerId()">
                            <el-option v-for="item in decorateTypeOptions" :key="item.id" :label="item.name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                    </div>
                    <div class="box">
                        <p class="text desc">备注：</p>
                        <el-input type="textarea" v-model="editParam.remark" rows="6" class="area"></el-input>
                    </div>
                    <div class="box">
                        <p class="text">下次回访日期：</p>
                        <el-date-picker class="pickDate" v-model="editParam.revisitTime" type="date" size="small"
                                        value-format="yyyy-MM-dd"
                                        format="yyyy-MM-dd"
                                        :picker-options="pickerOptions1" placeholder="选择日期下次回访日期">
                        </el-date-picker>
                    </div>
                    <div class="box" v-if="editParam.decorateType && editParam.decorateType === 4">
                        <p class="text"><span class="star">*</span>已签约企业：</p>
                        <el-select v-model="editParam.contractCompany" size="small" placeholder="请输入已签约企业" class="area">
                            <el-option v-for="item in editCompanyOptions" :key="item.id" :label="item.companyName"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                        <!--<el-input v-model="editParam.contractCompany"></el-input>-->
                    </div>
                    <div class="box" v-if="editParam.decorateType && editParam.decorateType === 4">
                        <p class="text"><span class="star">*</span>签约价格(元)：</p>
                        <el-input v-model="editParam.contractPrice" placeholder="请输入签约价格"></el-input>
                    </div>
                </div>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false" size="medium">取 消</el-button>
                    <el-button type="primary" @click="updateCustomer" size="medium">确 定</el-button>
                </span>
            </el-dialog>
        </div>
    </transition>
</template>

<script>
    export default {
        data() {
            return {
                diaType: false,
                query: {},
                total: 0,
                props: {
                    value: 'id',
                    label: 'name',
                    children: 'children'
                },
                dialogVisible: false,
                checkedCityOptions: [],
                cityOptions: [],
                decorateTypeOptions: [],
                decorateBudgetOptions: [],
                multipleSelection:[],
                searchOption: {
                    page: 0,
                    limit: 10,
                    cityCode: undefined,
                    decorateType: undefined,
                    revisitTime: undefined,
                    revisitTimeStr: undefined,
                    hasDesign: undefined,
                    userName: undefined,
                    mobile: undefined,
                },
                editCompanyOptions: [],
                pickerOptions1: {
                    shortcuts: [
                        {
                            text: "今天",
                            onClick(picker) {
                                picker.$emit("pick", new Date());
                            }
                        },
                        {
                            text: "明天",
                            onClick(picker) {
                                const date = new Date();
                                date.setTime(date.getTime() + 3600 * 1000 * 24);
                                picker.$emit("pick", date);
                            }
                        },
                        {
                            text: "后天",
                            onClick(picker) {
                                const date = new Date();
                                date.setTime(date.getTime() + 3600 * 1000 * 24 * 2);
                                picker.$emit("pick", date);
                            }
                        }
                    ]
                },
                tableDataList: [],
                editParam: {
                    customerId: undefined,
                    revisitTime: undefined,
                    decorateType: undefined,
                    remark: undefined,
                    contractCompany: undefined,
                    contractPrice: undefined
                },
            };
        },
        activated() {
            //alert(11)
            this.initOptions()
            this.initTable()
        },
        computed: {},
        methods: {
            handleSelectionChange (val) {
                this.multipleSelection = val
            },
            // 导出excel 表
            handleDownload() {
                require.ensure([], () => {
                    const { export_json_to_excel } = require('@/vendor/Export2Excel')
                    const tHeader = ['序号', '名称', '联系电话', '城市', '客户预约企业', '第一家抢单企业', '第二家抢单企业', '派单报价装企及报价(10家11)', '派单选定装企及报价(3家)', '内部推荐企业', '装修类型', '下次回访日期', '备注']
                    const filterVal = ['listIndex', 'userName', 'mobile', 'cityName', 'companyName', 'firstSecKillCompanyName', 'secondSecKillCompanyName', 'allPriceStr', 'choosePriceStr', 'innerCompanyName', 'decorateTypeName', 'revisitTime', 'remark']
                    const list = this.multipleSelection
                    const data = this.formatJson(filterVal, list)
                    export_json_to_excel(tHeader, data, '装修客户文件')
                })
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },
            //-------------------------------------------

            innerRecommend(id) {
                this.$router.push(`/interReferral/${id}`)
            },
            initOptions() {
                this.API.initOptionsByType('customerDecorateType').then(res => {
                    if(res){
                        this.decorateTypeOptions = res.data
                    }
                })
                this.API.initCityTree().then(res => {
                    if (!res) return;
                    this.cityOptions = res.data
                })
            },
            async showEditDialog(row) {
                // console.log(row.id, this.editParam.customerId, this.editParam.decorateType)
                if (row.id !== this.editParam.customerId) {
                    this.editCompanyOptions = [];
                    this.editParam.customerId = row.id;
                    this.editParam.decorateType = row.decorateType;
                    await this.getCompanyByCustomerId()
                }
                this.dialogVisible = true;
                this.editParam.customerId = row.id;
                this.editParam.remark = row.remark;
                this.editParam.decorateType = row.decorateType;
                this.editParam.contractPrice = row.contractPrice;
                this.editParam.contractCompany = row.contractCompany;
                this.editParam.revisitTime = row.revisitTime;

            },
            submitToSedKill(id) {
                this.API.submitToSedKill(id).then(res => {
                    if (res) {
                        if (res.success) {
                            this.$message.success(res.message)
                        } else {
                            this.$message.error(res.message)
                        }
                    }
                })
            },
            rowStyle({row, rowIndex}) {
                if (rowIndex === 0) {
                    return {
                        height: "30px!important",
                        background: "#f5f5f5",
                        color: "#666",
                        lineHeight: "80px!important"
                    };
                }
            },
            // 分页模块
            handleSizeChange: function (size) {
                this.searchOption.limit = size
                this.initTable()
            },
            handleCurrentChange: function (currentPage) {
                this.searchOption.page = currentPage
                this.initTable()
            },
            resetSearchParam() {
                for (let field in this.searchOption) {
                    if (field === 'page' || field === 'limit') {
                        continue
                    }
                    this.searchOption[field] = null;
                }
                this.checkedCityOptions = []
                this.initTable()
            },
            updateCustomer() {
                if (!this.editParam.decorateType) {
                    this.$message.error("请选择装修进度")
                    return
                }
                if (this.editParam.decorateType === 4 && !this.editParam.contractCompany) {
                    this.$message.error("请选择已签约企业")
                    return
                }
                if (this.editParam.decorateType === 4 && !this.editParam.contractPrice) {
                    this.$message.error("请选择签约价格")
                    return
                }
                this.API.updateDecorateCustomer(this.editParam).then(res => {
                    if (!res) return;
                    for (let field in this.editParam) {
                        this.editParam[field] = undefined;
                    }
                    this.dialogVisible = false
                    this.initTable()
                })
            },
            handleChange(val) {
                this.checkedCityOptions = val
                this.searchOption.cityCode = val[val.length - 1]
                this.initTable()
            },
            initTableIndex(index) {
                return index + 1
            },
            getCompanyByCustomerId() {
                return new Promise((resolve => {
                    if (this.editParam.decorateType && this.editParam.decorateType === 4) {
                        this.API.listCompanyOptionWithCustomer(this.editParam.customerId).then(res => {
                            if (res) {
                                this.editCompanyOptions = res.data;
                                resolve();
                            }
                        })
                    } else {
                        resolve();
                    }
                }))

            },
            initTable() {
                this.API.listDecorateCustomer(this.searchOption).then(res => {
                    if (res) {
                        this.tableDataList = res.list ? res.list : []
                        this.total = res.total
                        this.tableDataList.forEach(it => {
                            if (it.contractPrice) {
                                it.contractPrice = parseFloat(it.contractPrice).toFixed(2)
                            }
                            if (it.allPriceList.length) {
                                it.allPriceStr = it.allPriceList.map(price => price.name + '  ' + price.price + '万')
                            }
                            if (it.choosePriceList.length) {
                                it.choosePriceStr = it.allPriceList.map(price => price.name + '  ' + price.price + '万')
                            }
                        })
                        window.scrollTop = 0;
                    }

                })
            }
        }
    };
</script>

<style scoped lang="scss">
    @import "../../assets/css/common.scss";
    .decorCustomer {
        padding: 20px
    }
    .liName{
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        p{
            line-height: 20px;
        }
        &:hover{
            color: $theme-color;
            cursor: pointer;
        }
    }
    .listCompanyName{
        span{
            display: inline-block;
            margin-right:10px;
            font-size: 12px;
        }
        li{
            position: relative;
            padding-left: 15px;
            &:before{
                content: '';
                position: absolute;
                height: 6px;
                left: 0;
                top: 6px;
                width: 6px;
                border-radius: 3px;
                background: $theme-color;
            }
        }
    }
    .diabox {
        text-align: left;
        .box {
            margin: 10px 0;
            .text {
                display: inline-block;
                width: 130px;
                text-align: right;
            }
            .star {
                color: red;
            }
            .el-input, .area {
                width: 60%;
            }
            .desc {
                vertical-align: top;
            }
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
            background-color:$theme-color;
            color: #fff;
            border-radius: 4px;
        }
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

    .tableDataFrame {
        padding: 0 20px;
        background: #fff;
    }

    .topBar {
        background: #fff;
        div {
            text-align: left;
        }
        .footer{
            margin-top: 20px;
            border-top: solid 1px #e8e8e8;
            text-align: center;
            padding:20px  0 ;
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
        .el-select, .pickDate, .el-input ,.el-cascader{
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
        background: $theme-color;
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
        .dialog-footer {
            color: red;
        }
    }
</style>
