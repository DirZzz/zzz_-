<template>
    <div class="companyBaseMsg">
        <div class="companyBaseMsg-header">
            <h1>客户预约信息</h1>
            <div class="header-screen">
                <el-select  class="screen-city-item" clearable  v-model="provinceCode" size="medium" placeholder="请选择省" @change="seekShopList('省')">
                    <el-option
                            v-for="item in province"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
                <el-select class="screen-city-item" clearable  v-model="cityCode" size="medium" placeholder="请选择市" @change="seekShopList('市')">
                    <el-option
                            v-for="item in city"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
                <el-select size="medium" v-model="process" class="screen-city-item"
                           @change="seekShopList(process)" placeholder="请选择处理结果"
                           :clearable=true
                >
                    <el-option label="已处理" :value="1">已处理</el-option>
                    <el-option label="未处理" :value="0" >未处理</el-option>
                </el-select>
                <el-input class="header-screen-input" size="medium" placeholder="请输入联系人电话" v-model="mobile" @change="seekShopList(mobile)"></el-input>
            </div>
            <div class="header-seek">
                <el-button type="primary" round class="pri UnifiedsearchBtn" size="medium" @click="search">搜索</el-button>
                <el-button  round class="seek UnifiedsearchBtn" size="medium" @click="seekShopList('重置')">重置</el-button>
            </div>
        </div>
        <div class="content-title">
            <p class="title">预约信息列表</p>
            <span class="operation" @click="exportExcel">导出EXCEL</span>
        </div>
        <div class="companyBaseMsg-content">
            <el-table
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    :row-class-name="tableRowClassName"
                    :data="proprietorInfoData"
                    style="width: 100%">
                <el-table-column
                        label="序号"
                        >
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.index}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="servicesType"
                        label="预约类型">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.servicesType == 1 ? '验房预约' : 
                            scope.row.servicesType == 2 ? '量房预约' : 
                            scope.row.servicesType == 3 ? '设计预约' :
                            scope.row.servicesType == 4 ? '店铺预约' : ''}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="userName"
                        label="姓名"
                        >
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.userName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="mobile"
                        label="手机号码"
                >
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.mobile}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="shopName"
                        label="预约对象">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.shopName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="gmtCreated"
                        label="时间">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.gmtCreated}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="process"
                        label="处理结果">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.process == 1 ? '已处理' : '未处理'}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    class="bowen"
                        width="320"
                        prop="operation"
                        label="操作 ">
                    <template slot-scope="scope" >
                        <div v-if="adminUser">
                            <span class="operation" @click="updateProcess(scope.row.id)">修改处理结果</span>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="footer-paging">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="page"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="10"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalCount">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import qs from 'qs';
    export default {
        data () {
            return {
                getShopId:'',
                selectBacklist:'',
                cancelBlacklistVisible:false,
                joinBacklistVisible:false,
                contactNameTop : -1,
                shopCode: '', // 店铺编码
                shopName: '', // 店铺名称
                contactsName: '', // 联系人姓名
                contactsPhone: '', // 联系人电话
                province: [], // 省份
                provinceCode: '',
                city: [], // 市
                cityCode: '',
                district: [], // 区
                districtCode: '',
                street: [], // 街道
                companyType: 0, // 企业类型
                streetCode: '',
                shopData: [], // 店铺数据表格
                totalCount: 0, // 店铺数据总条数
                page: 1,
                start: 0, // 页码
                limit: 10, // 每页条数
                process: '',
                mobile: '',
                proprietorInfoData:[]
            };
        },
        created () {
            this.companyType = sessionStorage.getItem('companyType');
            this.proprietorInfoList();
            this.getAreaList(0, '省');
        },
        filters: {
           unique5(arr){
            var x = new Set(arr);
            return [...x].join('');
            }
        },
        computed: {
          adminUser(){
              if (this.companyType == 1 ){
                  if(sessionStorage.getItem('amdinUser')==1) {
                      return true;
                  }else{
                      return false;
                  }
              }else{
                  return true;
              }
          },
            userType(){
                let userType = qs.parse(sessionStorage.getItem('loginUser')).userType;
                if(userType !== 3) {
                    return true;
                }else{
                    return false;
                }
            },
            queryParam(){
                return {
                    companyId: sessionStorage.getItem('businessAdministrationId'),
                    start: this.start, // 页码
                    limit: this.limit, // 每页条数
                    provinceCode: this.provinceCode == '' ? null : this.provinceCode,
                    cityCode: this.cityCode == '' ? null : this.cityCode,
                    process: this.process == '' ? null : this.process,
                    mobile: this.mobile == '' ? null : this.mobile
                }
            }
        },
        methods: {
            /*权限设置*/
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            search() {
                this.start = 0;
                this.page = 1;
                this.proprietorInfoList();
            },
            exportExcel() {
                this.API.exportProprietorExcel(this.queryParam);
            },
            tableRowClassName({row, rowIndex}) {
                row.index = rowIndex + 1;
            },
            updateProcess(id) {
                let idList = [id];
                this.API.updateProcess(idList).then(res => {
                    this.proprietorInfoList();
                })
            },
            /*每页加载数据条数*/
            handleSizeChange(val) {
                this.limit = val;
                this.proprietorInfoList();
            },
            /*分页跳转*/
            handleCurrentChange(val) {
                this.start = (val - 1) * this.limit;
                this.proprietorInfoList();
            },
            /*条件搜索店铺数据*/
            seekShopList(type) {
                if (type == '省') {
                    this.cityCode = '';
                    this.city = [];
                    this.getAreaList(this.provinceCode, '市');
                }
                if (type == '重置') {
                    this.process = '';
                    this.mobile = '';
                    this.provinceCode = '';
                    this.cityCode = '';
                    this.city = [];
                    this.proprietorInfoList();
                }
            },
            /*请求店铺列表*/
            proprietorInfoList() {
                this.API.getProprietorInfoList(this.queryParam).then((res) => {
                    this.proprietorInfoData = res.datalist;
                    this.totalCount = res.totalCount;
                });
            },
            /*省市区联动*/
            getAreaList(areaCode, type) {
                this.API.areaList({areaCode:areaCode}).then((res) => {
                    if (res.datalist) {
                        if (type == '省') {
                            this.province = res.datalist;
                        }
                        if (type == '市') {
                            this.city = res.datalist;
                        }
                        if (type == '区') {
                            this.district = res.datalist;
                        }
                        if (type == '街道') {
                            this.street = res.datalist;
                        }
                    }
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/css/mixin";

    .companyBaseMsg{
        .companyBaseMsg-header{
            width: 100%;
            background-color: #ffffff;
            h1{
                width: 100%;
                height: 58px;
                font-size: 18px;
                line-height: 58px;
                padding-left: 20px;
                box-sizing: border-box;
                border-bottom: 1px solid #dddddd;
            }
            .header-screen{
                padding: 20px 10px 0 10px;
                border-bottom: 1px solid #dddddd;
                .header-screen-input{
                    width: 180px;
                    padding: 0 10px 20px 10px;
                }
                .screen-city-item{
                    width: 130px;
                    padding: 0 10px 20px 10px;
                }
                .screen-city{
                    line-height: 36px;
                }
            }
            .header-seek{
                padding: 20px 30px;
                text-align: center;
                .seek{
                    width: 100px;
                    background: #f5f5f5 ;
                }
                .pri{
                    width: 100px;
                    border: solid 1px #dddddd;
                }
                .add-shop{
                    float: right;
                }
            }
        }
        .content-title{
            background-color: white;
            margin-top: 20px;
            height: 50px;
            .title{
                float: left;
                padding: 14px 27px;
                font-size: 17px;
                font-weight: 500;
            }
            .operation{
                color: #ff6419;
                cursor:pointer;
                padding: 15px 17px;
                float: right;
                font-size: 15px;
            }
            .operation-splite{
                float: right;
                padding: 15px 0px;
            }
        }
        .companyBaseMsg-content{
            margin-top: 2px;
            padding: 20px;
            background-color: #ffffff;
            .name-class{
                color: red;
            }
            .operation{
                color: #ff6419;
                cursor:pointer;
                padding-left:10px;
            }
            .shop-name{
                width: 100%;
                border: none;
                color: #ff6419;
                cursor:pointer;
                overflow: hidden;
                text-overflow:ellipsis;
                white-space: nowrap;
            }
            .operation-name{
                color: #333333;
                overflow: hidden;
                text-overflow:ellipsis;
                white-space: nowrap;

            }
        }
        .footer-paging{
            background-color: #ffffff;
            padding: 20px 40px;
            text-align: center;
        }
    }

</style>

