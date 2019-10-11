<template>
    <div class="shopOffline">
        <div class="shopOffline-header">
            <h1>线下门店</h1>
            <div class="header-screen">
                <el-input class="header-screen-input" size="medium" placeholder="请输入店铺名称" v-model="shopName" @change="listShopOffline('店铺名称')"></el-input>
                <el-select  class="screen-city-item" clearable  v-model="provinceCode" size="medium" placeholder="请选择省" @change="listShopOffline('省')">
                    <el-option
                            v-for="item in province"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
                <el-select class="screen-city-item" clearable  v-model="cityCode" size="medium" placeholder="请选择市" @change="listShopOffline('市')">
                    <el-option
                            v-for="item in city"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
                <el-select class="screen-city-item" clearable  v-model="districtCode" size="medium" placeholder="请选择区" @change="listShopOffline('区')">
                    <el-option
                            v-for="item in district"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
                <el-select class="screen-city-item" clearable  v-model="streetCode" size="medium" placeholder="请选择街道" @change="listShopOffline('街道')">
                    <el-option
                            v-for="item in street"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
            </div>
            <div class="header-seek">
                <el-button type="primary" round class="pri UnifiedsearchBtn" size="medium" @click="doQuery">搜索</el-button>
                <el-button  round class="seek UnifiedsearchBtn" size="medium" @click="listShopOffline('重置')">重置</el-button>
            </div>
        </div>
        <div class="shopOffline-content">
            <el-row :gutter="20">
                <el-col :span="6" v-for="(item,index) in shopData" :key="index">
                    <div class="shop-item">
                        <div class="shop-header">
                            <div><img :src="item.logoPicPath" width="120" height="120"></div>
                            <h3>{{item.shopName}}</h3>
                        </div>
                        <div class="shop-content">
                            <div>
                                <span>联系人：</span>
                                <span>{{item.contactName}}</span>
                            </div>
                             <div>
                                <span>联系人电话：</span>
                                <span>{{item.contactPhone}}</span>
                            </div>
                            <div>
                                <span>区域：</span>
                                <span :title="item.longAreaName">{{item.longAreaName}}</span>
                            </div>
                            <div>
                                <span>店铺地址：</span>
                                <span :title="item.shopAddress">{{item.shopAddress}}</span>
                            </div>
                        </div>
                        <div class="shop-action">
                            <span class="operation"  @click="claimShop(item.id)">认领</span>
                        </div>
                    </div>
                </el-col>
            </el-row>
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
                cancelBlacklistVisible:false,
                joinBacklistVisible:false,
                contactNameTop : -1,
                shopName: '', // 店铺名称
                province: [], // 省份
                provinceCode: '',
                city: [], // 市
                cityCode: '',
                district: [], // 区
                districtCode: '',
                street: [], // 街道
                streetCode: '',
                shopData: [], // 店铺数据表格
                totalCount: 0, // 店铺数据总条数
                page: 1, // 页码
                limit: 10, // 每页条数
                queryParams: {
                    provinceCode: undefined, // 区域省编码
                    cityCode: undefined, // 区域市编码
                    areaCode: undefined, // 区域区编码
                    streetCode: undefined, // 区域街道编码
                    shopName: undefined, // 店铺名称
                    page: 0, // 页码
                    limit: 10 // 每页条数
                }
            };
        },
        created () {
            // this.companyType = sessionStorage.getItem('companyType');
            this.doQuery();
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
            }
        },
        methods: {
            /*权限设置*/
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            /*跳转页面
            * type: 用于判断跳转到编辑，新增，详情页面，根据type不同显示相应的内容
            * */
            // goShop(type, scope) {
            //     sessionStorage.setItem('routerPath', this.$route.path);
            //     sessionStorage.setItem('routerTxt', type);
            //     if (type != '新增店铺') {
            //         sessionStorage.setItem('shopId', scope.row.shopId);
            //     }

            //     if (type === '工程案例') {
            //         this.$router.push('/business/projectCase');
            //     } else if(type === '方案管理') {
            //         this.$router.push('/business/shopIntelligent')
            //     }
            //     else if(type === '博文栏目'){
            //         this.$router.push('/business/blog');
            //     } else {
            //         this.$router.push('/business/editShop');
            //     }
            // },
            /*每页加载数据条数*/
            handleSizeChange(val) {
                this.queryParams.limit = val;
                this.limit = val;
                this.doQuery();
            },
            /*分页跳转*/
            handleCurrentChange(val) {
                this.page = val;
                this.queryParams.page = (val - 1) * this.queryParams.limit;
                this.doQuery();
            },
            /*条件搜索店铺数据*/
            listShopOffline(type) {
                if (type == '店铺名称') {
                    this.queryParams.shopName = this.shopName;
                }
                if (type == '省') {
                    this.queryParams.provinceCode = this.provinceCode;
                    this.cityCode = '';
                    this.districtCode = '';
                    this.streetCode = '';
                    this.city = [];
                    this.district = [];
                    this.street = [];
                    this.getAreaList(this.provinceCode, '市');
                }
                if (type == '市') {
                    this.queryParams.cityCode = this.cityCode;
                    this.districtCode = '';
                    this.streetCode = '';
                    this.district = [];
                    this.street = [];
                    this.getAreaList(this.cityCode, '区');
                }
                if (type == '区') {
                    this.queryParams.areaCode = this.districtCode;
                    this.streetCode = '';
                    this.street = [];
                    this.getAreaList(this.districtCode, '街道');
                }
                if (type == '街道') {
                    this.queryParams.streetCode = this.streetCode;
                }
                if (type == '重置') {
                    this.queryParams = {
                        provinceCode: undefined, // 区域省编码
                        cityCode: undefined, // 区域市编码
                        areaCode: undefined, // 区域区编码
                        streetCode: undefined, // 区域街道编码
                        shopName: undefined, // 店铺名称
                        page: 0, // 页码
                        limit: this.limit // 每页条数
                    }
                    this.shopName = '';
                    this.provinceCode = '';
                    this.cityCode = '';
                    this.districtCode = '';
                    this.streetCode = '';
                    this.doQuery();
                }
                else{
                    this.doQuery();
                }
            },
            /*请求店铺列表*/
            doQuery() {
                this.API.listShopOffline(this.queryParams).then((res) => {
                    this.shopData = res.list;
                    this.totalCount = res.total;
                });
            },
            /** 认领 */
            claimShop() {

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
            },
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../../assets/css/mixin";

    .shopOffline{
        .shopOffline-header{
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
        .shopOffline-content{
            margin-top: 20px;
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
            .shop-item{
                border: 1px solid #e8e8e8;
                padding: 10px;
                font-family: MicrosoftYaHei;
            }
            .shop-header {
                height: 150px;
                text-align: center;
                margin-top: 45px;
                div {
                    border-radius: 8px;
                    margin-bottom: 12px;
                }
                h3 {font-weight: 700; color: #333333;font-family: MicrosoftYaHei;}
            }
            .shop-content {
                width: 95%;
                margin: 28px auto;
                background: #fafafa;
                padding: 10px 0px;
                font-family: MicrosoftYaHei;
                font-weight: normal;
                font-stretch: normal;
                font-size: 14px;
                div {
                    margin: 5px 20px;
                    height: 25px;
                    line-height: 25px;
                }
                span {
                    display: -moz-inline-box;
                    display: inline-block;
                    overflow:hidden;
                    text-overflow:ellipsis;
                    white-space:nowrap;
                }
                span:first-child {
                    margin-left: 5px; 
                    color: #999999;
                }
                span:last-child {
                    font-size: 14px;
                    letter-spacing: 0px;
                    color: #333333;
                }
                div:last-child span:last-child{
                    width: 73%;
                }
            }
            .shop-action {
                margin: 25px 0px;
                text-align: center;
                font-weight: bold;
            }
        }
        .footer-paging{
            background-color: #ffffff;
            padding: 20px 40px;
            text-align: center;
        }
    }

</style>

