<template>
    <div class="companyBaseMsg">
        <div class="companyBaseMsg-header">
            <h1>店铺管理</h1>
            <div class="header-screen">
                <el-input class="header-screen-input" size="medium" placeholder="请输入店铺编码" v-model="shopCode" @change="seekShopList('店铺编码')"></el-input>
                <el-input class="header-screen-input" size="medium" placeholder="请输入店铺名称" v-model="shopName" @change="seekShopList('店铺名称')"></el-input>
                <el-input class="header-screen-input" size="medium" placeholder="请输入联系人姓名" v-model="contactsName"  @change="seekShopList('联系人姓名')"></el-input>
                <el-input class="header-screen-input" size="medium" placeholder="请输入联系人电话" v-model="contactsPhone" @change="seekShopList('联系人电话')"></el-input>
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
                <el-select size="medium" v-if="companyType ==1" v-model="selectBacklist" class="screen-city-item"
                           @change="seekShopList(selectBacklist)" placeholder="是否加入黑名单"
                           :clearable=true >
                    <el-option label="是" :value="1">是</el-option>
                    <el-option label="否" :value="0" >否</el-option>
                </el-select>
            </div>
            <div class="header-seek">
                <el-button type="primary" round class="pri UnifiedsearchBtn" size="medium" @click="shopList">搜索</el-button>
                <el-button  round class="seek UnifiedsearchBtn" size="medium" @click="seekShopList('重置')">重置</el-button>
                <el-button type="primary"
                           round
                           plain
                           class="add-shop UnifiedsearchBtn"
                           size="medium"
                           @click="goShop('新增店铺')"
                           v-if="companyType != 1 && isPer('shop:edit')">新增店铺</el-button>
                <el-button type="primary"
                           round
                           plain
                           class="add-company-shop UnifiedsearchBtn"
                           size="medium"
                           @click="goShop('新增企业店铺')"
                           v-if="companyType != 1 && isPer('shop:addMainShop')">新增企业店铺</el-button>
            </div>
        </div>
        <div class="companyBaseMsg-content">
            <el-table
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    :data="shopData"
                    style="width: 100%">

                <el-table-column prop="shopCode" label="店铺编码">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.shopCode}}</span>
                    </template>
                </el-table-column>

                <el-table-column prop="shopName" label="店铺名称">
                    <template slot-scope="scope">
                        <!--<span class="shop-name">{{scope.row.shopName}}</span>-->
                        <el-tooltip
                                effect="dark"
                                :content="scope.row.shopName"
                                placement="top-start"
                                :enterable="false"
                                :visible-arrow="false">
                            <span class="shop-name" @click="goShop('店铺详情', scope)">{{scope.row.shopName}}</span>
                        </el-tooltip>
                        <img src="https://show.sanduspace.com/windowsPc/goods/picture/2018/12/19/17/339762_20181219173238000.png" v-if="scope.row.shopType == 1">
                    </template>
                </el-table-column>

                <el-table-column prop="contactName" label="店铺评分" :render-header="renderHeader" width="130">
                    <template slot-scope="scope">
                        <el-tooltip
                                :content="scope.row.enableScore == 10 ? scope.row.automateScore+'':''+scope.row.handScore"
                                :enterable="false">
                            <span class="operation-name">{{scope.row.enableScore == 10 ? scope.row.automateScore : scope.row.handScore }}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>

                <el-table-column prop="contactName" label="联系人姓名">
                    <template slot-scope="scope">
                        <el-tooltip
                                effect="dark"
                                :content="scope.row.contactName"
                                placement="top-start"
                                :enterable="false"
                                :visible-arrow="false">
                            <span class="operation-name">{{scope.row.contactName}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>

                <el-table-column prop="isBacklist" label="是否加入黑名单" v-if="companyType == 1">
                    <template slot-scope="scope">
                        <span>{{scope.row.isBlacklist == 0 ? '否':'是'}}</span>
                    </template>
                </el-table-column>

                <el-table-column prop="contactPhone" label="联系人电话">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.contactPhone}}</span>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="contactPhone"
                        label="创建人账号">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.creatorAccount}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="contactPhone"
                        label="创建人手机号">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.creatorPhone}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="areaName" label="区域">

                    <template slot-scope="scope">
                        <el-tooltip
                                effect="dark"
                                :content="scope.row.areaName.split(',')|unique5"
                                placement="top-start"
                                :enterable="false"
                                :visible-arrow="false">
                             <span class="operation-name" >
                                {{scope.row.areaName.split(',') | unique5}}
                            </span>
                         </el-tooltip>
                    <!-- <span v-for="(item, index) in scope.row.areaName.split(',') " :key="index">
                             <span class="operation-name" v-show="item != scope.row.areaName.split(',')[index + 1]">{{item}}</span>
                             <el-tooltip
                                effect="dark"
                                :content="scope.row.areaName.split(',')"
                                placement="top-start"
                                :enterable="false"
                                :visible-arrow="false">

                        </el-tooltip>

                        </span>-->
                    </template>
                </el-table-column>

               <!-- <el-table-column v-if="adminUser" width="200" prop="releasePlatformName" label="发布">
                    <template slot-scope="scope">
                        <span class="operation-name" v-if="scope.row.releasePlatformName">{{scope.row.releasePlatformName}}</span>
                        <span class="operation-name" v-else>————</span>
                    </template>
                </el-table-column>-->
                <el-table-column prop="releaseState" label="发布" align="center">
                    <template slot-scope="scope">
                        <el-switch v-if="adminUser && isPer('shop:edit')" disabled class="switchStyle" v-model="scope.row.releaseStatus" active-color="#E4E7ED"
                                   inactive-icon-class="inactive-icon-class" :disabled="false"
                                   active-icon-class="active-icon-class" :active-value="0"
                                   inactive-color="#ff6419" :inactive-value="1" @change="changeSwitch(scope.row)">
                        </el-switch>
                        <p>{{scope.row.releaseStatus === 0 ? scope.row.statusSwitchTxt = '未发布' :
                            scope.row.statusSwitchTxt ='已发布'}}</p>
                    </template>
                </el-table-column>

                <el-table-column class="bowen" width="320" prop="operation"  label="操作 ">
                    <template slot-scope="scope" >
                        <div v-if="adminUser">
                            <span class="operation" v-if="isPer('shop:edit')" @click="goShop('编辑店铺', scope)">编辑</span>
                            <span class="operation" @click="goShop('方案管理', scope)">方案管理</span>
                            <span class="operation" v-if="isPer('shop:del')" @click="deleteShop(scope)">删除</span>
                            <span class="operation" v-if="sysUserType != 3" @click="goShop('博文栏目',scope)" >博文栏目</span>
                            <span class="operation"  v-if="companyType != 1" @click="goShop('工程案例', scope)">工程案例</span>
                        </div>
                        <div v-if="userType && isPer('shop:blacklist')">
                            <span class="operation" @click="blacklist(scope)"
                                  v-if="scope.row.isBlacklist == 0 && companyType == 1">加入黑名单</span>
                            <span class="operation" @click="cancelBlacklist(scope)"
                                  v-if="scope.row.isBlacklist == 1 && companyType == 1">取消黑名单</span>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="footer-paging">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="start"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="10"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalCount">
            </el-pagination>
        </div>
        <el-dialog title="加入黑名单" width="450px" :visible.sync="joinBacklistVisible">
            <div>确定将该<span style="color: #FF6419">“{{shopNameTmp}}”</span>加入黑名单？</div>
            <span slot="footer" class="dialog-footer">
                <el-button size="mini" round type="primary" @click="addBlacklist">确 定</el-button>
                <el-button size="mini" round @click="joinBacklistVisible = false">取 消</el-button>
              </span>
        </el-dialog>

        <el-dialog title="取消黑名单" width="450px" :visible.sync="cancelBlacklistVisible">
            <div>确定将该<span style="color: #FF6419">“{{shopNameTmp}}”</span>解除黑名单？</div>
            <span slot="footer" class="dialog-footer">
                <el-button size="mini" round type="primary" @click="sureCancelBlacklist">确 定</el-button>
                <el-button size="mini" round @click="cancelBlacklistVisible = false">取 消</el-button>
              </span>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs';

    export default {
        data () {
            return {
                sysUserType :'',
                shopNameTmp:'',
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
                start: 1, // 页码
                limit: 10, // 每页条数
                getShopList: {
                    // companyId: sessionStorage.getItem('companyID'), // 企业Id
                    companyId: sessionStorage.getItem('businessAdministrationId'),
                    companyType: undefined, // 企业类型
                    provinceCode: undefined, // 区域省编码
                    cityCode: undefined, // 区域市编码
                    areaCode: undefined, // 区域区编码
                    streetCode: undefined, // 区域街道编码
                    contactName: undefined, // 联系人姓名
                    contactPhone: undefined, // 联系人电话
                    shopCode: undefined, // 店铺编码
                    shopName: undefined, // 店铺名称
                    isBlacklist:undefined,// 黑名单
                    releaseStatus:undefined,//发布状态
                    start: 0, // 页码
                    limit: 10 // 每页条数
                }
            };
        },
        // props:['messages'],
        created () {
            this.companyType = sessionStorage.getItem('companyType');
            this.sysUserType = qs.parse(sessionStorage.getItem('loginUser')).userType;
            console.log('created shop res');
            this.shopList();
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
                if(userType == 2) {
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
            //显示加入黑名单弹框
            blacklist(val){
                this.getShopId = val.row.shopId;
                this.shopNameTmp = val.row.shopName
                this.joinBacklistVisible = true
            },
            cancelBlacklist(val){
                this.getShopId = val.row.shopId;
                this.shopNameTmp = val.row.shopName
                this.cancelBlacklistVisible = true
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
            /*跳转页面
            * type: 用于判断跳转到编辑，新增，详情页面，根据type不同显示相应的内容
            * */
            goShop(type, scope) {
                sessionStorage.setItem('routerPath', this.$route.path);
                sessionStorage.setItem('routerTxt', type);
                if (type != '新增店铺' && type != '新增企业店铺') {
                    sessionStorage.setItem('shopId', scope.row.shopId);
                }
                if (type === '工程案例') {
                    this.$router.push({path:'/business/projectCase',query:{shopType:scope.row.shopType}});
                } else if(type === '方案管理') {
                    this.$router.push({path:'/business/shopIntelligent',query:{shopType:scope.row.shopType}})
                } else if(type === '博文栏目'){
                    this.$router.push({path:'/business/blog',query:{shopType:scope.row.shopType}});
                } else if(type === '新增店铺') {
                    if (type==='新增店铺') {
                        this.API.shopIsExit().then(res => {
                            if (res) {
                                if (res.success) {
                                    this.$alert('你已经拥有一个店铺了，无法再添加更多店铺','提示',{
                                        confirmButtonText: '确定',
                                    });
                                }else {
                                    this.$router.push({path:'/business/editShop', query:{shopType:2}});
                                }
                            }
                        });
                    }else{
                        this.$router.push({path:'/business/editShop', query:{shopType:2}});
                    }
                } else if (type === '新增企业店铺'){
                    this.checkCompanyShop();
                } else {
                    this.$router.push({path:'/business/editShop', query:{shopType:scope.row.shopType}});
                }
            },
            checkCompanyShop() {
                this.API.checkCompanyShop({companyId: sessionStorage.getItem('businessAdministrationId')}).then(res => {
                    if (res.success) {
                        this.$router.push({path:'/business/editShop', query:{shopType:1}});
                    }else{
                        this.$message({
                            type: 'error',
                            message: res.message
                        });
                    }
                })
            },
            /*删除操作*/
            deleteShop(scope) {
                this.$confirm('此操作将删除当前店铺信息，是否继续','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.shopDelete({shopId: scope.row.shopId}).then(res => {
                        if (res) {
                            if (res.success) {
                                this.$message({
                                    type: 'success',
                                    message: '删除成功!'
                                });
                                this.shopData.splice(scope.$index, 1);
                                this.shopList();
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
                this.start = val;
                this.getShopList.start = (val - 1) * this.getShopList.limit;
                this.shopList();
            },
            /*条件搜索店铺数据*/
            seekShopList(type) {
                if (type == '店铺编码') {
                    this.getShopList.shopCode = this.shopCode;
                }
                if (type == '店铺名称') {
                    this.getShopList.shopName = this.shopName;
                }
                if (type == '联系人姓名') {
                    this.getShopList.contactName = this.contactsName;
                }
                if (type == '联系人电话') {
                    this.getShopList.contactPhone = this.contactsPhone;
                }
                if (type == '省') {
                    this.getShopList.provinceCode = this.provinceCode;
                    this.cityCode = '';
                    this.districtCode = '';
                    this.streetCode = '';
                    this.city = [];
                    this.district = [];
                    this.street = [];
                    this.getAreaList(this.provinceCode, '市');
                }
                if (type == '市') {
                    this.getShopList.cityCode = this.cityCode;
                    this.districtCode = '';
                    this.streetCode = '';
                    this.district = [];
                    this.street = [];
                    this.getAreaList(this.cityCode, '区');
                }
                if (type == '区') {
                    this.getShopList.areaCode = this.districtCode;
                    this.streetCode = '';
                    this.street = [];
                    this.getAreaList(this.districtCode, '街道');
                }
                if (type == '街道') {
                    this.getShopList.streetCode = this.streetCode;
                }
                if (type == '重置') {
                    console.log(this.getShopList);
                    this.getShopList = {
                        companyId: sessionStorage.getItem('businessAdministrationId'),
                        companyType: undefined, // 企业类型
                        provinceCode: undefined, // 区域省编码
                        cityCode: undefined, // 区域市编码
                        areaCode: undefined, // 区域区编码
                        streetCode: undefined, // 区域街道编码
                        contactName: undefined, // 联系人姓名
                        contactPhone: undefined, // 联系人电话
                        shopCode: undefined, // 店铺编码
                        shopName: undefined, // 店铺名称
                        start: 0, // 页码
                        limit: this.limit // 每页条数
                    }
                    this.selectBacklist='',
                    this.shopCode = '';
                    this.shopName = '';
                    this.contactsName = '';
                    this.contactsPhone = '';
                    this.provinceCode = '';
                    this.cityCode = '';
                    this.districtCode = '';
                    this.streetCode = '';
                    this.shopList();
                }
                if(type == 1){
                    this.getShopList.isBlacklist = 1;
                    this.shopList();
                }
                else if (type == 0){
                    this.getShopList.isBlacklist = 0;
                    this.shopList();
                }
                else{
                    this.shopList();
                }
                this.getShopList.start = 0;
            },
            /*请求店铺列表*/
            shopList() {
                this.API.shopList(this.getShopList).then((res) => {
                    console.log(res,'shop res');
                    this.shopData = res.datalist;
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
            },
            /*店铺发布与取消发布*/
            changeSwitch(row) {
                if (row.releaseStatus) {
                    row.statusSwitchTxt = "未发布";
                    this.API.shopRelease({
                        shopId: row.shopId,
                        isRelease: 1
                    }).then(res => {
                        this.$message.success("发布成功!");
                    });
                } else {
                    row.statusSwitchTxt = "已发布";
                    this.API.shopRelease({
                        shopId: row.shopId,
                        isRelease: 0
                    }).then(res => {
                        this.$message.success("取消发布成功!");
                    });
                }
            },
            renderHeader (h,{column}) {
                return h(
                    'div',{
                    style:'display:flex;margin:0 auto'
                    },
                    [
                    h('p', column.label),
                    h('prompt-message', {
                        props: {messages: ["[ 评分说明 ]","提高评分可以，店铺可以获得更大的曝光机会","1.发布更多的方案，案例可提高评分","2.完善店铺信息可提高评分"]}
                    })
                    ]
                );
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
                .add-company-shop{
                    float: right;
                    padding: 0px 14px;
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
        
    }

</style>

