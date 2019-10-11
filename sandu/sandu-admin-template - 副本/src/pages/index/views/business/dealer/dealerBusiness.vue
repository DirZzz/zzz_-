<template>
    <div>
        <div class="dealerSearchArea">
            <h1>经销商企业管理</h1>
            <el-row :gutter="24">
                <el-col :span="3">
                    <el-input v-model="search.franchiserName" clearable placeholder="请输入经销商企业名称"></el-input>
                </el-col>
                <el-col :span="3">
                    <el-input v-model="search.contactName" clearable placeholder="请输入联系人姓名"></el-input>
                </el-col>
                <el-col :span="3">
                    <el-input v-model="search.phone" clearable placeholder="请输入联系人电话"></el-input>
                </el-col>
                <el-col :span="15">
                    <el-select v-model="citySelect.provide"
                               @clear= "resetprovide"
                               @change="getprovinceCode(citySelect.provide)"
                               clearable
                               placeholder="请选择省">
                        <el-option
                                v-for="(item, index) in provide"
                                :key="index"
                                :label="item.areaName"
                                :value="item.areaName">
                        </el-option>
                    </el-select> <span class="splitColor">—</span>

                    <el-select clearable v-model="citySelect.city"
                               @clear= "resetcity"
                               @change="getCityCode(citySelect.city)"
                               placeholder="请选择市">
                        <el-option
                                v-for="(item,index) in citySelects"
                                :key="index"
                                :label="item.areaName"
                                :value="item.areaName">
                        </el-option>
                    </el-select> <span class="splitColor">—</span>

                    <el-select clearable @clear= "resetarea" v-model="citySelect.area" @change="getAreaCode(citySelect.area)" placeholder="请选择区域">
                        <el-option
                                v-for="(item,index) in areaSelect"
                                :key="index"
                                :label="item.areaName"
                                :value="item.areaName">
                        </el-option>
                    </el-select> <span class="splitColor">—</span>

                    <el-select clearable @clear= "resetorigin" v-model="citySelect.origin" @change="getOriginCode(citySelect.origin)" placeholder="请选择街道">
                        <el-option
                                v-for="(item,index) in originSelect"
                                :key="index"
                                :label="item.areaName"
                                :value="item.areaName">
                        </el-option>
                    </el-select>
                </el-col>

            </el-row>
            <p class="clearfix" style="display: table;text-align: center;width: 100%;padding:20px">
                <el-button round class="confirmBtn UnifiedsearchBtn"  type="primary" @click="getDealerList">搜 索</el-button>
                <el-button round class="cancelBtnBg button UnifiedsearchBtn" @click="resetSearch">重 置</el-button>
                <el-button round class="addDealer UnifiedsearchBtn" plain  type="primary" style="width:110px;" @click="gotoAdd" v-if="isPer('dealer:add')">新增经销商</el-button>
            </p>
        </div>

        <div class="listTable"> <!--:header-cell-style="rowClass"-->
            <el-table
                    :data="dealerData"
                    style="width: 100%"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    tooltip-effect="dark"
            >
                <el-table-column type="index" label="编号" width="60">
                </el-table-column>
                <el-table-column prop="franchiserName" label="经销商企业名称">
                    <template slot-scope="scope">
                        <div class="franchiserName" @click="gotoDetail(scope.row)" title="点击查看详情">{{scope.row.franchiserName}}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="contactName" label="联系人姓名">
                    <template slot-scope="scope">
                        <div class="contactNameWidth">
                        <span v-if="scope.row.contactName">{{scope.row.contactName}}</span>
                        <span v-else>———</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="phone" label="联系人电话">
                </el-table-column>
                <el-table-column prop="brandName" label="品牌">
                </el-table-column>
                <el-table-column prop="areaNames" label="区域">

                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" @click="gotoDealerEdit(scope.row)" v-if="isPer('dealer:edit')">编辑</el-button>
<!--
                        <el-button type="text" @click="gotoDetail(scope.row)"  v-if="isPer('dealer:view')">详情</el-button>
-->
                        <el-button type="text" @click="deleteData(scope.row)" v-if="isPer('dealer:del')">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="query.start"
                        :page-sizes="[5, 10, 50, 100, 200,500]"
                        :page-size="query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="totalCount">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        data(){
            return {
                search:{
                    franchiserName: '',
                    phone: '',
                    contactName: '',
                    provinceCode: '',
                    cityCode: '',
                    areaCode: '',
                    originCode: ''
                },
                input: '',
                value: '',
                citySelect:{
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },
                cityCode: '',
                provide:[],

                provideSelect:[],
                citySelects:[],
                areaSelect: [],
                originSelect: [],

                totalCount: 0,
                loading: false,
                dealerData: [],
                query:{
                    start:1,
                    limit:10
                }
            }
        },
        methods:{
            rowClass({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return {borderTop:'solid 1px #ddd', borderBottom:'solid 1px #ddd'}// {color: 'red'}//'color:green'
                } else {
                    return ''
                }
            },
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            getDealerList() {
                this.loading = true
                let queryCondition = Object.assign({}, this.query, this.search,{
                   companyId: sessionStorage.getItem('companyID')
                })
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                this.API.dealerList(queryCondition).then( (res) => {
                   if(res) {
                       this.loading = false
                       this.dealerData = res.datalist
                       this.totalCount = res.totalCount
                   }
                })
            },
            // 分页模块
            handleSizeChange: function (size) {
                // this.pageSize = size;
                this.query.limit = size
                this.getDealerList()
            },
            handleCurrentChange: function (currentPage) {
                // this.currentPage = currentPage;
                this.query.start = currentPage
                this.loading = true
                this.getDealerList()
            },
            resetprovide(){
                this.search.provinceCode = ''
                this.citySelects = []
                this.resetcity()
                this.resetarea()
                this.resetorigin()
            },
            resetcity(){
                this.search.cityCode = ''
                this.citySelects = []
                this.resetarea()
                this.resetorigin()
            },
            resetarea(){
                this.search.areaCode = ''
                this.areaSelect =[]
                this.resetorigin()
            },
            resetorigin(){
                this.search.originCode = ''
                this.originSelect = []
            },
            gotoDetail (row) {
                this.$router.push(`/business/dealerDetail/${row.id}`)
            },
            gotoAdd () {
                this.$router.push('/business/dealerAdd')
            },
            resetSearch () {
                this.citySelect.provide = ''
                this.citySelect.city = ''
                this.citySelect.origin = ''
                this.citySelect.area = ''
                this.search = {}
                this.search.franchiserName = ''
                this.search.contactName = ''
                this.search.phone = ''
                this.getDealerList()
            },
            gotoDealerEdit (row) {
              // this.$router.replace(`/editProduct/${index.id}`)
              this.$router.replace(`/business/dealerEdit/${row.id}`)
            },
            deleteData(row){
                this.$confirm(`确定要删除【${row.franchiserName}】经销商企业吗`, '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    confirmButtonText: '确定',
                    type: 'warning',
                    center: true
                }).then(() => {
                    this.API.dealerDelete({fanchiserId: row.id}).then(res => {
                        if(res) {
                            this.getDealerList()
                            this.$message.success('已删除')
                        }
                    })
                })


            },
            getAreaList (){
                this.API.areaList({areaCode:0}).then((res => {
                    if(res) {
                        this.provide = res.datalist
                    }
                }))
            },
            getprovinceCode(code) {
                this.provide.map( (item) => {
                    if(item.areaName == code)
                    {
                        this.cityCode = item.areaCode
                        this.search.provinceCode = item.areaCode
                    }
                })

                this.API.areaList({areaCode:this.cityCode}).then((res => {
                    if(res) {
                        this.citySelects = res.datalist
                        if(!this.citySelects.map((item) => item.areaName).includes(this.citySelect.city)) {
                            this.citySelect.city = ''
                            this.citySelect.area = ''
                            this.citySelect.origin = ''
                        }

                        //this.citySelect.city = this.citySelects[0].areaName
                        //this.getCityCode(this.citySelects[0].areaName)
                    }
                }))

            },

                getCityCode (code){
                    this.citySelects.map( (item) => {
                        if(item.areaName == code)
                        {
                            this.cityCode = item.areaCode
                            this.search.cityCode = item.areaCode
                        }
                    })

                    this.API.areaList({areaCode:this.cityCode}).then((res => {
                        if(res) {
                            this.areaSelect = res.datalist
                            if(!this.areaSelect.map((item) => item.areaName).includes(this.citySelect.area)) {
                                this.citySelect.area = ''
                                this.citySelect.origin = ''
                            }
                           /* this.citySelect.area = this.areaSelect[0].areaName
                            this.getAreaCode(this.areaSelect[0].areaName)
                            this.citySelect.origin = this.originSelect[0].areaName*/
                        }
                }))

            },
            getAreaCode (code){
                this.areaSelect.map( (item) => {
                    if(item.areaName == code)
                    {
                        this.cityCode = item.areaCode
                        this.search.areaCode = item.areaCode
                    }
                })

                this.API.areaList({areaCode:this.cityCode}).then((res => {
                    if(res) {
                        if(!this.originSelect.map((item) => item.areaName).includes(this.citySelect.origin)) {
                            this.citySelect.origin = ''
                        }
                        this.originSelect = res.datalist
                        //this.getOriginCode (this.areaSelect[0].areaName)
                        //this.citySelect.origin = this.originSelect[0].areaName
                    }
                }))

            },
            getOriginCode (code){
                this.originSelect.map( (item) => {
                    if(item.areaName == code)
                    {
                        this.cityCode = item.areaCode
                        this.search.originCode = item.areaCode
                    }
                })
                this.API.areaList({areaCode:this.cityCode}).then((res => {
                    if(res) {
                        this.originSelect = res.datalist
                    }
                }))
            },
            isGoEdit() {
                if ( sessionStorage.getItem('businessAdministrationId') != sessionStorage.getItem('companyID')) {
                    this.$router.replace(`/business/dealerEdit/${sessionStorage.getItem('businessAdministrationId')}`);
                }
            }
        },

        created() {
            this.isGoEdit()
            this.getDealerList()
            this.getAreaList()
        }
    }
</script>

<style scoped lang="scss">
    .splitColor{
        color: #999;
    }
    .listTable{
        border-top: solid 15px #f0f0f0;
        padding: 20px;
        background: #fff;
    }
    .franchiserName{
        color:#FF6419;
        cursor: pointer;
        text-align: center;
        width: 90%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 1;
    }
    .contactNameWidth{
        text-align: center;
        width: 90%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 1;
    }
    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
   .dealerSearchArea{
       background: #fff;
       h1{
           height: 58px;
           line-height: 58px;
           padding-left: 21px;
           font-size: 19px;
           border-bottom: solid 1px #ddd;
       }
       .el-row{
           padding:20px
       }
       p{
           padding:20px;
           border-top: solid 1px #ddd;
           position: relative;
       }
       .button{
           background: #f5f5f5;
       }
       .addDealer{
           position: absolute;
           right: 60px;
       }
   }
</style>