<template>
    <div class="companyBaseMsg">
        <div class="companyBaseMsg-header">
            <h1>线下门店管理</h1>
            <div class="header-screen">
                <el-input class="header-screen-input" size="medium" placeholder="请输入店铺名称" v-model="shopName" @change="seekShopList('店铺名称')"></el-input>
                <el-select size="medium" v-model="isRelease" class="screen-city-item"
                           @change="seekShopList(isRelease)" placeholder="请选择发布状态"
                           :clearable=true
                >
                    <el-option label="是" :value="1">是</el-option>
                    <el-option label="否" :value="2" >否</el-option>
                </el-select>
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
                <el-select class="screen-city-item" clearable  v-model="districtCode" size="medium" placeholder="请选择区" @change="seekShopList('区')">
                    <el-option
                            v-for="item in district"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
                <el-select class="screen-city-item" clearable  v-model="streetCode" size="medium" placeholder="请选择街道" @change="seekShopList('街道')">
                    <el-option
                            v-for="item in street"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
            </div>
            <div class="header-seek">
                <el-button type="primary" round class="pri UnifiedsearchBtn" size="medium" @click="shopList">搜索</el-button>
                <el-button  round class="seek UnifiedsearchBtn" size="medium" @click="seekShopList('重置')">重置</el-button>
                <el-button type="primary"
                           round
                           plain
                           class="orangeBtn UnifiedsearchBtn"
                           style="width: 110px;float:right;margin-left: 20px"
                           size="small"
                           @click="addShop()"
                           v-if="isPer('shopOffline:add')"
                           >新增线下门店</el-button>
                <el-button round class="orangeBtn UnifiedsearchBtn" v-if="isPer('shopOffline:add')" plain type="primary" style="width: 140px;float:right;margin-left: 20px"
                         @click="handleBatchAdd">批量导入线下门店
              </el-button>          
            </div>
        </div>
        <div class="companyBaseMsg-content">
            <el-table
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    :data="shopData"
                    style="width: 100%">
                <el-table-column
                        prop="shopName"
                        label="店铺名称">
                    <template slot-scope="scope">
                        <!--<span class="shop-name">{{scope.row.shopName}}</span>-->
                        <el-tooltip
                                effect="dark"
                                :content="scope.row.shopName"
                                placement="top-page"
                                :enterable="false"
                                :visible-arrow="false">
                            <span class="shop-name" @click="toShopDetail(scope.row.id)">{{scope.row.shopName}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="logoUrl"
                        label="店铺logo"
                        >
                        <template slot-scope="scope" v-if="scope.row.logoPicPath != null">
                         <img width="100px" height="60px;" :src="scope.row.logoPicPath" />
                        </template>
                </el-table-column>
                <el-table-column
                        prop="contactName"
                        label="联系人姓名"

                        >
                    <template slot-scope="scope">
                        <el-tooltip
                                effect="dark"
                                :content="scope.row.contactName"
                                placement="top-page"
                                :enterable="false"
                                :visible-arrow="false">
                            <span class="operation-name">{{scope.row.contactName}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                

                <el-table-column
                        prop="contactPhone"
                        label="联系人电话">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.contactPhone}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="longAreaName"
                        label="区域">
                    <template slot-scope="scope">
                        <el-tooltip
                                effect="dark"
                                :content="scope.row.longAreaName"
                                placement="top-start"
                                :enterable="false"
                                :visible-arrow="false">
                             <span class="operation-name" >
                                {{scope.row.longAreaName}}
                            </span>
                         </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="address"
                        label="店铺地址">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.shopAddress}}</span>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="release"
                        label="发布状态">
                    <template slot-scope="scope">
                        <span class="operation-name">{{ scope.row.isRelease == 0 ? '未发布':'已发布'}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    class="bowen"
                        width="320"
                        prop="operation"
                        label="操作 ">
                    <template slot-scope="scope" >
                            <span class="operation"  @click="editShop(scope.row.id)" v-if="isPer('shopOffline:edit')">编辑</span>
                            <span class="operation"  @click="toShopDetail(scope.row.id)">详情</span>
                            <span class="operation"  @click="toRelease(scope.row.id,scope.row.isRelease > 0?0:1)" v-if="isPer('shopOffline:release')">
                                {{ scope.row.isRelease == 0 ? '发布':'取消发布'}}
                            </span>
                            <span class="operation"  @click="deleteShop(scope)" v-if="isPer('shopOffline:delete')">删除</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="footer-paging">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="page"
                    :page-sizes="[5, 10, 20, 30, 40]"
                    :page-size="10"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalCount">
            </el-pagination>
        </div>

        <!--批量新增线下门店-->
        <batchAdd ref="batchAdd"></batchAdd>
        <!--导入文件加载过程-->
        <fileLoading ref="fileLoading"></fileLoading>    

    </div>
</template>

<script>
    import qs from 'qs';
    import batchAdd from './components/batchAdd'
    import fileLoading from './components/fileLoading'
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
                isRelease:'',//是否发布
                shopData: [], // 店铺数据表格
                totalCount: 0, // 店铺数据总条数
                page: 1, // 页码
                limit: 10, // 每页条数
                getShopList: {
                    // companyId: sessionStorage.getItem('companyID'), // 企业Id
                    companyId: sessionStorage.getItem('businessAdministrationId'),
                    provinceCode: undefined, // 区域省编码
                    cityCode: undefined, // 区域市编码
                    areaCode: undefined, // 区域区编码
                    streetCode: undefined, // 区域街道编码
                    shopName: undefined, // 店铺名称
                    isRelease:undefined,//是否发布
                    page: 1, // 页码
                    limit: 10 // 每页条数
                }
            };
        },
        activated () {
            //console.log(888888888888888888)
            this.companyType = sessionStorage.getItem('companyType');
            this.shopList();
            this.getAreaList(0, '省');
        },
        filters: {
           unique5(arr){
            var x = new Set(arr);
            return [...x].join('');
            }  
        },
        components: {
            batchAdd,
            fileLoading
        },
        computed: {
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
             handleBatchAdd(){  //批量新增贴图
                this.$refs.batchAdd.isShow(true);
            },
            //显示加入黑名单弹框
            blacklist(val){
                this.getShopId = val.row.shopId;
                this.shopName = val.row.shopName
                this.joinBacklistVisible = true
            },
              // 是否确定加入黑名单
            addBlacklist(){
                this.API.setBlacklist({
                    shopId:this.getShopId,
                    isBlacklist:1
                }).then((res) => {
                    this.joinBacklistVisible = false
                    this.shopList()
                    this.$message.success('店铺已加入黑名单')
                })
            },
            sureCancelBlacklist(){
                this.API.setBlacklist({
                    shopId:this.getShopId,
                    isBlacklist:0
                }).then((res) => {
                    this.cancelBlacklistVisible = false
                    this.shopList()
                    this.$message.success('店铺已解除黑名单')
                })
            },
            addShop(){
                this.$router.push('/viewSmallProgram/addShopOffline');
            },
            editShop(index){
                this.$router.push(`/viewSmallProgram/editShopOffline/${index}`);
            },
            toRelease(index,release) {
                this.API.shopOfflineRelease({
                       id:index,
                       isRelease:release
                   }).then(res => {
                        if (res.code === 200) {
                            this.shopList();
                            this.$message.success("操作成功！");
                        }
                    });
            },
            /*跳转页面
            * type: 用于判断跳转到编辑，新增，详情页面，根据type不同显示相应的内容
            * */
            goShop(type, scope) {
                sessionStorage.setItem('routerPath', this.$route.path);
                sessionStorage.setItem('routerTxt', type);
                if (type != '新增店铺') {
                    sessionStorage.setItem('shopId', scope.row.shopId);
                }

                if (type === '工程案例') {
                    this.$router.push('/business/projectCase');
                } else if(type === '方案管理') {
                    this.$router.push('/business/shopIntelligent')
                }
                else if(type === '博文栏目'){
                    this.$router.push('/business/blog');
                } else {
                    this.$router.push('/business/editShop');
                }
            },
            toShopDetail(index) {
                this.$router.push(`/viewSmallProgram/ShopOfflineDetail/${index}`);
            },
            /*删除操作*/
            deleteShop(scope) {
                this.$confirm('此操作将删除当前店铺信息，是否继续','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.shopOfflineDelete({companyshopofflineId: scope.row.id}).then(res => {
                        if (res.code == 200) {
                            if (res.code == 200) {
                                this.shopList();
                                this.$message({
                                    type: 'success',
                                    message: "删除成功"
                                });
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: res.message
                                });
                            }
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            /*每页加载数据条数*/
            handleSizeChange(val) {
                this.getShopList.limit = val;
                this.limit = val;
                this.shopList();
            },
            /*分页跳转*/
            handleCurrentChange(val) {
                this.page = val;
                this.getShopList.page = val;
                this.shopList();
            },
            /*条件搜索店铺数据*/
            seekShopList(type) {
                if (type == '店铺名称') {
                    this.getShopList.shopName = this.shopName;
                    this.getShopList.page = 0;
                    this.page =0;
                }
                if (type == '省') {
                    this.cityCode = '';
                    this.districtCode = '';
                    this.streetCode = '';
                    this.city = [];
                    this.district = [];
                    this.street = [];
                    this.getAreaList(this.provinceCode, '市');
                    this.getShopList = {
                        companyId: sessionStorage.getItem('businessAdministrationId'),
                        provinceCode: this.provinceCode, // 区域省编码
                        cityCode: undefined, // 区域市编码
                        areaCode: undefined, // 区域区编码
                        streetCode: undefined, // 区域街道编码
                        shopName: undefined, // 店铺名称
                        isRelease:this.isRelease,
                        page: 0, // 页码
                        limit: this.limit // 每页条数
                    }
                    this.shopList();
                }
                if (type == '市') {
                    this.districtCode = '';
                    this.streetCode = '';
                    this.district = [];
                    this.street = [];
                    this.getAreaList(this.cityCode, '区');
                    this.getShopList = {
                        companyId: sessionStorage.getItem('businessAdministrationId'),
                        provinceCode: this.provinceCode, // 区域省编码
                        cityCode: this.cityCode, // 区域市编码
                        areaCode: undefined, // 区域区编码
                        streetCode: undefined, // 区域街道编码
                        shopName: undefined, // 店铺名称
                        isRelease:this.isRelease,
                        page: 0, // 页码
                        limit: this.limit // 每页条数
                    }
                    this.shopList();
                }
                if (type == '区') {
                    this.streetCode = '';
                    this.street = [];
                    this.getAreaList(this.districtCode, '街道');
                    this.getShopList = {
                        companyId: sessionStorage.getItem('businessAdministrationId'),
                        provinceCode: this.provinceCode, // 区域省编码
                        cityCode: this.cityCode, // 区域市编码
                        areaCode: this.districtCode, // 区域区编码
                        streetCode: undefined, // 区域街道编码
                        shopName: undefined, // 店铺名称
                        isRelease:this.isRelease,
                        page: 0, // 页码
                        limit: this.limit // 每页条数
                    }
                    this.shopList();
                }
                if (type == '街道') {
                    this.getShopList.streetCode = this.streetCode;
                }
                if (type == '重置') {
                    console.log(this.getShopList);
                    this.getShopList = {
                        companyId: sessionStorage.getItem('businessAdministrationId'),
                        provinceCode: undefined, // 区域省编码
                        cityCode: undefined, // 区域市编码
                        areaCode: undefined, // 区域区编码
                        streetCode: undefined, // 区域街道编码
                        shopName: undefined, // 店铺名称
                        isRelease:undefined,
                        page: 0, // 页码
                        limit: this.limit // 每页条数
                    }
                    this.isRelease = '';
                    this.shopName = '';
                    this.provinceCode = '';
                    this.cityCode = '';
                    this.districtCode = '';
                    this.streetCode = '';
                    this.shopList();
                }
                if(type == 0) {
                    this.getShopList.isRelease = undefined;
                }
                if(type == 1){
                    this.getShopList.isRelease = 1;
                    this.page =0;
                    this.getShopList.page = 0;
                    this.shopList();
                }
                else if (type == 2){
                    this.getShopList.isRelease = 0;
                    this.page =0;
                    this.getShopList.page = 0;
                    this.shopList();
                }
                else{
                    this.shopList();
                }
            },
            /*请求店铺列表*/
            shopList() {
                this.API.shopOfflineList(this.getShopList).then((res) => {
                    this.shopData = res.list;
                    this.totalCount = res.total;
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
            },
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
        .companyBaseMsg-content{
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
        }
        .footer-paging{
            background-color: #ffffff;
            padding: 20px 40px;
            text-align: center;
        }
       .orangeBtn {
           width:120px;
           height:32px;
           font-size:12px;
           text-align:center;
           line-height: 8px;
      }
    }

</style>

