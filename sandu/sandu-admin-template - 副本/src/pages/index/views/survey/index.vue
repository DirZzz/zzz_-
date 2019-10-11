<template>
    <div class="survey">
        <div class="survey-container">
            <div class="survey-item" @click="$router.push('/personal/personalSet')">
                <img :src="BASE_URL.sourceBaseUrl+userData.picPath" alt="">
                <span>欢迎您，{{userData.userName}}</span>
            </div>
            <div class="survey-shop">
                <div class="survey-shop-item">
                    <div class="shop-header">最新店铺数据</div>
                    <div class="shop-content">
                        <div class="shop-content-item">
                            <template v-if="shopData.type!=0">
                                <div class="left">
                                    <i class="ic_look"></i>
                                    <p v-if="userType==2">企业小程序店铺总浏览量</p>
                                    <p v-else>随选网店铺总浏览量</p>
                                </div>
                                <div class="right">
                                    <i class="ic_arrow"></i>
                                    <span>{{shopData.viewCount || 0}}</span>
                                </div>
                            </template>
                            <template v-else>
                                <div class="no-goods" v-if="userType==2">
                                    <div v-if="isPer('brandhall:view')">暂无店铺信息，去<span @click="$router.push('/brandHouse')">添加</span>品牌馆</div>
                                    <div v-else>暂无店铺信息</div>
                                </div>
                                <div class="no-goods" v-else>
                                    <div v-if="isPer('shop:edit')">暂无店铺信息，去<span @click="$router.push('/business/shop')">添加</span>店铺</div>
                                    <div v-else>暂无店铺信息</div>
                                </div>
                            </template>
                        </div>
                    </div>
                </div>
                <div class="survey-shop-item">
                    <div class="shop-header">小程序数据</div>
                    <div class="shop-content">
                        <template v-if="miniProgramData.hasMiniProgramFlag==1">
                            <div class="shop-content-item">
                                <div class="left">
                                    <i class="ic_user"></i>
                                    <p>昨日活跃用户数</p>
                                </div>
                                <div class="right">
                                    <i class="ic_arrow"></i>
                                    <span>{{miniProgramData.yesterdayCount}}</span>
                                </div>
                            </div>
                            <div class="shop-content-item">
                                <div class="left">
                                    <i class="ic_user"></i>
                                    <p>昨日新增用户数</p>
                                </div>
                                <div class="right">
                                    <i class="ic_arrow"></i>
                                    <span>{{miniProgramData.newCount}}</span>
                                </div>
                            </div>
                            <div class="shop-content-item">
                                <div class="left">
                                    <i class="ic_user"></i>
                                    <p>总用户数量</p>
                                </div>
                                <div class="right">
                                    <i class="ic_arrow"></i>
                                    <span>{{miniProgramData.totalCount}}</span>
                                </div>
                            </div>
                        </template>
                        <template v-else>
                            <div class="no-goods" style="text-align: center">暂无小程序信息，了解更多小程序信息请点击 <span
                                    @click="toPath('http://www.sanduspace.com/')">小程序介绍</span></div>
                        </template>
                    </div>
                </div>
            </div>
            <!--近期更新-->
            <div class="survey-shop">
                <div class="survey-shop-item" v-if="userType!=3">
                    <div class="shop-header">近期更新</div>
                    <div class="shop-content">
                        <div class="shop-content-item" v-if="userType==2||userType==14">
                            <template v-if="newData.hasProduct==1">
                                <div class="left">
                                    <i class="ic_commodity"></i>
                                    <p>7日内商品上新数</p>
                                </div>
                                <div class="right">
                                    <i class="ic_arrow"></i>
                                    <span>{{newData.productCount}}</span>
                                </div>
                            </template>
                            <template v-else>
                                <div class="no-goods" v-if="isPer('product:view')">暂无产品信息，去<span @click="$router.push('/product')">添加</span>产品</div>
                                <div class="no-goods" v-else>暂无产品信息</div>
                            </template>
                        </div>
                        <div class="shop-content-item">
                            <template v-if="newData.hasPlan==1">
                                <div class="left">
                                    <i class="ic_program"></i>
                                    <p>7日内方案上新数</p>
                                </div>
                                <div class="right">
                                    <i class="ic_arrow"></i>
                                    <span>{{newData.planCount}}</span>
                                </div>
                            </template>
                            <template v-else>
                                <div class="no-goods" v-if="isPer('solution:view')">暂无方案信息，去<span @click="$router.push('/plan')">添加</span>方案</div>
                                <div class="no-goods" v-else>暂无方案信息</div>
                            </template>
                        </div>
                    </div>
                </div>
                <div class="survey-shop-item"></div>
            </div>
            <!--常用功能-->
            <div class="survey-shop">
                <div class="survey-shop-item">
                    <div class="shop-header">常用功能</div>
                    <div class="shop-container">
                        <div class="shop-content-item cursor" v-for="(item,key) in iconArr" :key="key"
                             @click="$router.push(item.path)" v-if="isPer(item.permission) &&isPer(item.parentPermission)">
                            <img :src="item.url" alt="">
                            <p>{{item.name}}</p>
                        </div>
                    </div>
                </div>
            </div>
            <!--更多服务-->
            <div class="survey-shop">
                <div class="survey-shop-item">
                    <div class="shop-header">更多服务</div>
                    <div class="shop-container">
                        <div class="shop-content-item cursor" v-for="(item,key) in moreIconArr" :key="key"
                             @click="toPath(item.path)">
                            <img :src="item.url" alt="">
                            <p>{{item.name}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        name: "index",
        data() {
            return {
                iconArr: [
                    {url: require('./images/shop_icon.png'), name: '店铺管理', path: '/business/shop',permission:'shop:view',parentPermission:true},
                    {url: require('./images/Program_icon.png'), name: '方案管理', path: '/plan',permission:'solution:view',parentPermission:'platform:factory'},
                    {url: require('./images/product_icon.png'), name: '产品管理', path: '/product',permission:'product:view',parentPermission:'platform:factory'},
                    {url: require('./images/Order_icon.png'), name: '订单管理', path: '/orderlist',permission:'miniapp:order:view',parentPermission:'platform:miniapp'},
                    {url: require('./images/commodity_icon.png'), name: '商品管理', path: '/goods',permission:'miniapp:goods:view',parentPermission:'platform:miniapp'},
                    {url: require('./images/applets_icon.png'), name: '小程序管理', path: '/homeManage',permission:'miniapp:homePage:edit',parentPermission:true},
                ],
                moreIconArr: [
                    {url: require('./images/PC_icon.png'), name: '三度云享家PC版', path: 'http://www.sanduspace.com/'},
                    {url: require('./images/app_icon.png'), name: '三度云享家APP', path: 'http://www.sanduspace.com/'},
                    {url: require('./images/enterprise_icon.png'), name: '企业小程序', path: 'http://www.sanduspace.com/'},
                    {url: require('./images/Store_icon.png'), name: '门店小程序', path: 'http://www.sanduspace.com/'},
                    {
                        url: require('./images/Installed enterprise_icon.png'),
                        name: '装企小程序',
                        path: 'http://www.sanduspace.com/'
                    },
                ],
                miniProgramData: '',
                shopData: '',
                newData: '',
                userData: '',
            }
        },
        computed:{
            userType(){
                return sessionStorage.getItem('userType')
            }
        },
        created() {
            this.getMiniProgram();
            this.getNew();
            this.getShop();
            this.getUserInfo();
        },
        methods: {
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            getUserInfo() {
                this.API.getUserDetailInfo().then(res => {
                    if (res.success) {
                        this.userData = res.obj;
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            getMiniProgram() {
                this.API2.overviewMiniProgram().then(res => {
                    if (res.success) {
                        this.miniProgramData = res.obj;
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            getNew() {
                this.API2.overviewNew().then(res => {
                    if (res.success) {
                        this.newData = res.obj;
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            getShop() {
                this.API2.overviewShop().then(res => {
                    if (res.success) {
                        this.shopData = res.obj;
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            toPath(path) {
                window.open(path, "_blank");
            }
        }
    }
</script>

<style scoped lang="scss">
    .survey {
        .survey-container {
            .survey-item {
                width: 100%;
                height: 90px;
                background-color: #ffffff;
                box-shadow: 0px 2px 2px 0px rgba(58, 22, 4, 0.06);
                display: flex;
                align-items: center;
                cursor: pointer;
                img {
                    width: 58px;
                    height: 58px;
                    background-color: #f0f0f0;
                    border-radius: 100%;
                    margin-left: 20px;
                }
                span {
                    font-size: 18px;
                    color: #333333;
                    margin-left: 20px;
                }
            }
            .survey-shop {
                display: flex;
                flex-wrap: wrap;
                margin-top: 20px;
                .survey-shop-item {
                    flex: 1;
                    box-sizing: border-box;
                    .shop-header {
                        color: #333333;
                        font-size: 16px;
                        padding: 20px 0;
                        text-indent: 20px;
                        border-bottom: 1px solid #f5f5f5;
                        font-weight: bold;
                        background-color: #fff;
                    }
                    .shop-content {
                        display: flex;
                        height: 160px;
                        justify-content: center;
                        align-items: center;
                        background-color: #fff;
                        .shop-content-item {
                            flex: 1;
                            display: flex;
                            justify-content: center;
                            .left {
                                i{
                                    display: block;
                                    width: 40px;
                                    height: 40px;
                                }
                                .ic_look {
                                    background: no-repeat center url("./images/tab_liulan_nor.png");
                                    background-size: 100%;
                                }
                                .ic_user {
                                    background: no-repeat center url("./images/tab_user_nor.png");
                                    background-size: 100%;
                                }
                                .ic_commodity {
                                    background: no-repeat center url("./images/tab_commodity_nor.png");
                                    background-size: 100%;
                                }
                                .ic_program {
                                    background: no-repeat center url("./images/tab_Program_nor.png");
                                    background-size: 100%;
                                }

                                p {
                                    color: #666666;
                                    font-size: 16px;
                                    line-height: 32px;
                                    margin-top: 5px;
                                }
                            }
                            .right {
                                margin-left: 5px;
                                i {
                                    display: inline-block;
                                    width: 23px;
                                    height: 17px;
                                    background: no-repeat center url("./images/tab_arrow_nor.png");
                                    background-size: 100%;
                                    vertical-align: middle;
                                }
                                span {
                                    font-size: 45px;
                                    color: #333333;
                                    //font-weight: bold;
                                    margin-left: 5px;
                                }
                            }
                        }
                        .no-goods {
                            font-size: 16px;
                            span {
                                color: #FF6419;
                                font-weight: bold;
                                cursor: pointer
                            }
                        }
                        .cursor {
                            cursor: pointer;
                        }
                    }
                    .shop-container {
                        display: flex;
                        padding: 20px 0;
                        background-color: #fff;
                        .shop-content-item {
                            display: flex;
                            align-items: center;
                            margin-left: 60px;
                            img {
                                width: 85px;
                                height: 85px;
                            }
                            p {
                                color: #666666;
                            }
                        }
                        .cursor {
                            cursor: pointer;
                        }
                    }
                }
                .survey-shop-item:nth-child(2n) {
                    margin-left: 20px;
                }
            }
        }
    }
</style>