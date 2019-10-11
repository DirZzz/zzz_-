<template>
    <transition name="fade">
        <div class="ownerInfo-box">
            <header class="topBar">
                <div class="barTitle">
                    <span>业主信息查询</span>
                    <div class="right">
                        <span>导入Excel</span>
                    </div>
                </div>
                <div class="searchOption">
                    <el-input
                            placeholder="请输入业主姓名"
                            v-model="searchOption.userName"
                            size="small" clearable
                            @blur="getCustomerInfoList"
                            clearable>
                    </el-input>
                    <el-input
                            placeholder="请输入业主联系电话"
                            v-model="searchOption.mobile"
                            size="small" clearable
                            @blur="getCustomerInfoList"
                            clearable>
                    </el-input>
                    <el-select v-model="searchOption.decorateStyle" size="small" placeholder="请选择擅长风格"
                               @change="getCustomerInfoList" clearable>
                        <el-option v-for="item in styleOptions"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                    </el-select>

                    <el-select v-model="searchOption.decorateBudget" size="small" placeholder="请选择装修费用"
                               @change="getCustomerInfoList" clearable>
                        <el-option v-for="item in decorateBudgetOptions"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                    </el-select>

                    <el-select v-model="searchOption.decorateHouseType" @change="getCustomerInfoList" size="small"
                               placeholder="请选择装修类型" clearable>
                        <el-option v-for="item in testArray"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="searchOption.decorateType" @change="getCustomerInfoList" size="small"
                               placeholder="请选择装修方式" clearable>
                        <el-option
                                v-for="item in decorateTypeOptions"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="searchOption.hasDesign" @change="getCustomerInfoList" size="small"
                               placeholder="请选择是否有设计方案" clearable>
                        <el-option
                                v-for="item in [{value:true,label:'有设计方案'},{value:false,label:'无设计方案'}]"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>

                    <el-select v-model="searchOption.type" @change="getCustomerInfoList" size="small"
                               placeholder="请选择需求类型" clearable>
                        <el-option
                                prop=""
                                v-for="item in requirementTypeOptions"
                                :key="item.type"
                                :label="item.typeName"
                                :value="item.type">
                        </el-option>
                    </el-select>

                    <el-cascader v-model="selectProvinceCity" @change="getCustomerInfoList" size="small"
                                 placeholder="请选择城市"
                                 :options="cityOptions"
                                 expand-trigger='hover'
                                 filterable
                                 clearable>
                    </el-cascader>

                    <el-select v-model="searchOption.customerTypeValue" @change="getCustomerInfoList" size="small"
                               placeholder="请选择客户类型" clearable>
                        <el-option
                                v-for="item in customerTypeOptions"
                                :key="item.customerTypeValue"
                                :label="item.customerType"
                                :value="item.customerTypeValue">
                        </el-option>
                    </el-select>

                    <el-date-picker
                            class="pickDate"
                            v-model="searchOption.nextTime"
                            type="date"
                            size="small"
                            value-format="yyyy-MM-dd 00:00:00"
                            placeholder="选择下次回访日期"
                            :picker-options="pickerOptions1"
                    >
                    </el-date-picker>
                    <!-- <el-button type="text" @click="toNowDay">今天</el-button> -->
                </div>
                <div class="footer">
                    <span class="queryBtn" @click="getCustomerInfoList">查询</span>
                    <span class="queryReset" @click="resetFrom">重置</span>
                    <span class="right">
                        <el-button @click="handleDownload" size="mini" type="primary"
                                   :disabled="!customerInfoListOptions.length">导出Excel
                        </el-button>
                    </span>
                </div>

            </header>
            <section>

                <div class="ownerInfo" v-if="customerInfoListOptions!==''" :key="index"
                     v-for="(item,index) in customerInfoListOptions">
                    <ul>
                        <li>{{index + 1}}</li>
                        <li>
                            <el-popover ref="popover" placement="top-start" trigger="hover"
                                        @show="show(item.requirementType)" v-if="item.showPopover">
                                <div v-if="item.requirementType === '店铺预约'">预约店铺名称：{{item.companyName}}</div>
                                <div slot="reference" v-if="item.requirementType === '店铺预约'">
                                    需求类型：{{item.requirementType}}
                                </div>
                            </el-popover>
                            <div v-else>需求类型：{{item.requirementType}}</div>
                        </li>
                        <!--<li>需求类型：{{item.requirementType}}</li>-->
                        <li>客户登记时间：{{item.initTime}}</li>
                        <li>联系电话：{{item.mobile}}</li>
                        <li>客户类型：{{item.customerType}}</li>
                        <li>上次回访日期：{{item.lastTime}}</li>
                        <li>下次回访日期：{{item.nextTime}}</li>
                        <span class="btn edit" @click='gotoEdit(item.id)'
                              v-if="permission('decorate:ownerInfo:edit')">编辑</span>

                        <span class="btn delete" @click='deleteOwner(item.id)'
                              v-if="item.deleteFunc && permission('decorate:ownerInfo:edit')">删除</span>
                    </ul>
                    <div class="text">
                        <p>姓名：{{item.userName}}</p>
                        <p>城市：{{item.provinceCity}}</p>
                        <p>小区：{{item.houseEstate}}</p>
                        <p>详细地址：{{item.address}}</p>
                        <p>户型：{{item.house}}</p>
                        <p>面积(㎡)：{{item.houseAcreage}}</p>
                        <p>预算(元)：{{item.decorateBudget}}</p>
                        <p>装修类型：{{item.decorateType}}</p>
                        <p>倾向风格：{{item.goodStyle}}</p>
                        <p>装修方式：{{item.decorateHouseType}}</p>
                        <p>设计方案：<span style="cursor:pointer;color:#3399ff;"
                                      @click="ImgView(BASE_URL+item.designPlanPic)" v-if="item.designPlanPic">查看</span>
                            <span v-else>暂无方案图</span>
                        </p>
                        <p style="display: block">备注：{{item.remark}}</p>
                    </div>
                </div>
                <div v-if="customerInfoListOptions==''" class="nonData">
                    暂无数据
                </div>

                <div class="pageCount">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page.sync="query.page"
                            :page-sizes="[10, 50, 100, 200,500]"
                            :page-size="query.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="total">
                    </el-pagination>
                </div>
            </section>
            <img-view ref="sonParams"></img-view>
        </div>
    </transition>
</template>

<script>
    import ImgView from '&/components/Img-view'
    import { mapGetters } from 'Vuex'
    export default {
        data() {
            return {
                showPopover: true,
                BASE_URL: process.env.sourceBaseUrl,
                query: {
                    page: 1,
                    limit: 10
                },
                total: 0,
                requirementTypeOptions: [],
                cityOptions: [{}],
                customerTypeOptions: [],
                customerInfoListOptions: [],
                styleTypeOptions: [],
                searchOption: {},
                pickdate: '',
                pickerOptions1: {
                    shortcuts: [{
                        text: '今天',
                        onClick(picker) {
                            picker.$emit('pick', new Date());
                        }
                    }, {
                        text: '明天',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24);
                            picker.$emit('pick', date);
                        }
                    }, {
                        text: '后天',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24 * 2);
                            picker.$emit('pick', date);
                        }
                    }]
                },
                dataList: [],
                selectProvinceCity: [],
                styleOptions: [],
                decorateBudgetOptions: [],
                decorateTypeOptions: [],
                testArray: [{id: 0, name: '新房装修'}, {id: 1, name: '旧房改造'}],
            }
        },
        components: {
            ImgView
        },
        computed:{
            ...mapGetters(['getRightSide'])
        },
        activated() {
            this.getRequirementTypeList();
            this.getCustomerType();
            this.getCityList();
            this.getCustomerInfoList();
            this.initOptions();
        },
        methods: {
            show(text) {
                console.log(text)
                if (text == '店铺预约') {
                    this.showPopover = true;
                } else {
                    this.showPopover = false;
                }
            },
            //图片预览事件
            ImgView(ImgURL) {
                this.$refs.sonParams.sonFun({ImgURL: ImgURL, ImgShow: true});
            },
            resetFrom() {
                for (let field in this.searchOption) {
                    if (field === 'page' || field === 'limit') {
                        continue
                    }
                    this.searchOption[field] = undefined;
                }
                this.selectProvinceCity = []
                this.getCustomerInfoList();

            },

            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size;
                this.getCustomerInfoList();
            },
            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage;
                this.loading = true;
                this.getCustomerInfoList();
            },
            gotoEdit(id) {
                this.$router.push({path: '/ownerInfoEdit?', query: {'id': id}});
            },
            deleteOwner(id) {
                this.API.deleteOwner(id)
                    .then(res => {
                        if (res) {
                            this.customerInfoListOptions = this.customerInfoListOptions.filter(it => it.id != id)
                            if (res.success) {
                                this.$message.success(res.message)
                            }
                        }

                    })
            },
            getRequirementTypeList: function () {
                this.API.getRequirementTypeList()
                    .then(res => {
                        this.requirementTypeOptions = res.list;
                    })
            },
            getCityList: function () {
                this.API.getCityList()
                    .then(res => {
                        let newOptions = [];
                        for (let i = 0; i < res.list.length; i++) {
                            let a = {
                                'value': res.list[i].provinceCode,
                                'label': res.list[i].province,
                                'children': []
                            }
                            newOptions.splice(i, 0, a);
                            for (let j = 0; j < res.list[i].cityList.length; j++) {
                                let b = {
                                    'value': res.list[i].cityList[j].cityCode,
                                    'label': res.list[i].cityList[j].city
                                }
                                newOptions[i].children.splice(j, 0, b);
                            }
                        }
                        ;
                        this.cityOptions = newOptions;
                    })
            },
            emptyParamToUndefine: function (param) {
                for (let field in param) {
                    if (param[field] === '') {
                        param[field] = undefined
                    }
                }
            },
            getCustomerInfoList: function () {
                this.searchOption.cityCode = this.selectProvinceCity[1];
                this.searchOption.page = this.query.page;
                this.searchOption.pageSize = this.query.limit;
                this.emptyParamToUndefine(this.searchOption);
                this.API.getCustomerInfoList(this.searchOption)
                    .then(res => {
                        if (res.success == false) {
                            this.customerInfoListOptions = '';
                            this.total = 0;
                            return
                        }
                        this.customerInfoListOptions = res.list;
                        this.customerInfoListOptions.map(item => {
                            if (item.requirementType == '店铺预约') {
                                item.showPopover = true;
                            } else {
                                item.showPopover = false;
                            }
                        })
                        this.total = res.total;
                        console.log(this.getRightSide)
                        this.getRightSide.scrollTop=0;
                    })
            },
            getCustomerType: function () {
                this.API.getCustomerType()
                    .then(res => {
                        this.customerTypeOptions = res.list;
                    })
            },
            initOptions() {
                this.API.initOptionsByType('decorateBudget').then(res => {
                    if (!res) return;
                    this.decorateBudgetOptions = res.data
                })
                this.API.initOptionsByType('goodStyle').then(res => {
                    if (!res) return;
                    this.styleOptions = res.data
                })
                this.API.initOptionsByType('decorateType').then(res => {
                    if (!res) return;
                    this.decorateTypeOptions = res.data
                })
            },
            // 导出excel 表
            handleDownload() {
                require.ensure([], () => {
                    const {export_json_to_excel} = require('@/vendor/Export2Excel')
                    const tHeader = ['需求类型', '客户登记时间', '联系电话', '客户类型', '上次回访日期', '下次回访日期', '姓名', '城市', '小区', '详细地址', '户型', '面积(㎡)', '预算(元)', '装修类型', '倾向风格', '装修方式', '设计方案', '备注']
                    const filterVal = ['requirementType', 'initTime', 'mobile', 'customerType', 'lastTime', 'nextTime', 'userName', 'provinceCity', 'houseEstate', 'address', 'house', 'houseAcreage', 'decorateBudget', 'decorateType', 'goodStyle', 'decorateHouseType', 'designPlanPic', 'remark']
                    const list = this.customerInfoListOptions
                    const data = list.map(v => filterVal.map(j => v[j]))
                    export_json_to_excel(tHeader, data, '业主信息导出')
                })
            },

        }


    }
</script>

<style scoped lang="scss">
    @import "../../assets/css/common.scss";

    .ownerInfo-box {
        padding: 20px;
    }

    .nonData {
        padding-top: 30px;
        font-size: 14px;
        color: #666;
        width: 100%;
        height: 20px;
        background: #fff;
        margin-top: 20px;
    }

    .topBar {
        min-width: 1450px;
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
            position: relative;
            .right {
                position: absolute;
                right: 15px;
            }
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
                display: none;
                top: 0;
                span {
                    display: inline-block;
                    text-align: center;
                    font-size: 14px;
                    width: 82px;
                    cursor: pointer;
                    height: 32px;
                    line-height: 32px;
                    background-color: #3399ff;
                    color: #fff;
                    border-radius: 4px;
                }
            }
        }
    }

    .searchOption {
        padding-left: 32px;
        .el-select, .pickDate, .el-input, .el-cascader {
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
        //min-width: 1450px;
        margin-top: 20px;
        border-radius: 4px;
        height: 214px;
        margin-bottom: 20px;
        text-align: left;
        ul {
            height: 64px;
            border-bottom: solid 1px #e8e8e8;
            position: relative;
            background: #fff;
            li {
                display: inline-block;
                margin-right: 35px;
                height: 64px;
                line-height: 64px;
                font-size: 16px;
                color: #333;
                position: relative;
                &:first-of-type {
                    width: 50px;
                    text-align: center;
                    margin-right: 0;
                }
            }
            .btn {
                position: absolute;
                top: 16px;
                width: 64px;
                height: 32px;
                line-height: 32px;
                color: #3a8ee6;
                cursor: pointer;
                text-align: center;
                background-color: #ffffff;
                border-radius: 4px;
                font-size: 14px;
                border: solid 1px #3399ff;
                right: 32px;
            }
            .delete {
                right: 110px;
            }
        }
        p {
            display: inline-block;
            margin-left: 109px;
            margin-bottom: 24px;
            color: #999;
            min-width: 120px;
            font-size: 14px;
        }
        .text {
            background: #fff;
            height: 150px;
            padding-top: 32px;
        }
    }
</style>