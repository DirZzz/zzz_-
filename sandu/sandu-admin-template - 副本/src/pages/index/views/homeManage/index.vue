<template>
    <div class="homeManage">
        <div class="homeManage-header">首页管理</div>
        <div class="homeManage-content">
            <div class="homeManage-demo">
                <div class="homeManage-tip">提示：直接拖动模块并点击保存可进行排序</div>
                <div class="header-title"><img src="../../assets/images/nav.png" alt=""></div>
                <div class="homeManage-app">
                    <div class="banner" @click="setBanner" :class="{'borderActive':toggleCon.setBanner}">
                        <template v-if="moduleType[0].configDetails.length>0">
                            <el-carousel :interval="5000" arrow="never" class="el-carousel-box" height="205"
                                         style="width: 100%;height: 213px">
                                <el-carousel-item v-for="(item,index) in moduleType[0].configDetails" :key="index">
                                    <img :src="item.picAddress" alt="" class="bannerImg">
                                </el-carousel-item>
                            </el-carousel>
                        </template>
                        <img src="../../assets/images/zhanweitu.png" class="emptyImg" alt="" v-else>
                    </div>
                    <!--&lt;!&ndash;自定义导航栏&ndash;&gt;-->
                    <template>
                        <div class="navBox" @click="navSet" :class="{'borderActive':toggleCon.navSet}" v-if="navShow">
                            <div class="nav-item" v-for="(navItem,navIndex) in moduleType[1].configDetails"
                                 :key="navIndex" v-if="navItem.show">
                                <img :src="navItem.imageUrl" alt="">
                                <p>{{navItem.title}}</p>
                            </div>
                        </div>
                        <div class="navBox" @click="navSet" :class="{'borderActive':toggleCon.navSet}" v-else>
                            暂无导航栏
                        </div>
                    </template>
                    <!--首页弹框设置-->
                    <template>
                        <div class="setDialog" @click="setDialog" :class="{'borderActive':toggleCon.setDialog}">
                            <div class="setDialog-content">
                                <div class="setDialog-title">首页弹框设置</div>
                                <div class="setDialog-banner">
                                    <p>{{moduleType[2].title}}</p>
                                    <p>{{moduleType[2].secondTitle}}</p>
                                </div>
                                <div class="setDialog-phone">手机号</div>
                                <div class="setDialog-code">验证码 <span>获取验证码</span></div>
                                <div class="setDialog-confirm">确定</div>
                            </div>
                        </div>
                    </template>
                    <div class="sortableList" v-for="(item, index) in moduleType" :key="index"
                         v-dragging="{ item: item, list: moduleType, group: 'moduleType' }">
                        <!--企业基本信息-->
                        <div class="companyBaseInfo" @click="toggle(item,index)" v-if="item.type=='baseInfo'"
                             :class="{'borderActive':item.active}">
                            <div class="companyBaseInfo-item">
                                <div class="header-module-title"><i></i>{{item.title || '标题'}}<i
                                        class="ic_recommend_bg"></i></div>
                                <div class="companyBaseInfo-content">
                                    <div class="item"><span>企业名称：</span>{{item.secondTitle || '暂无'}}</div>
                                    <div class="item"><span>联系电话：</span>{{item.mobile || '暂无'}} <i class="ic_phone"></i>
                                    </div>
                                    <div class="item"
                                         style="position: relative;word-break: break-all;padding-right: 20px">
                                        <div><span>所在地区：</span>{{item.address || '暂无'}}</div>
                                        <i class="ic_address"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--推荐方案-->
                        <div class="recommendBox" @click="toggle(item,index)" v-if="item.type=='plan'">
                            <template v-if="isFirst(item.type,index) || item.isAdd">
                                <div class="recommendPlan" :class="{'borderActive':item.active}">
                                    <div class="recommendPlan-header">
                                        <div class="header-module-title"><i></i>{{item.title || '标题'}}<i
                                                class="ic_recommend_bg"></i></div>
                                        <span v-if="item.configDetails.length>0"><em>{{item.initialIndex || 1}}</em>/{{item.configDetails.length}}</span>
                                    </div>
                                    <div class="planList">
                                        <ul v-if="item.configDetails.length>0">
                                            <el-carousel :autoplay="false" class="el-carousel-box"
                                                         @change="changeLength" indicator-position="none" height="205"
                                                         style="width: 100%">
                                                <el-carousel-item v-for="(itemChild,indexChild) in item.configDetails"
                                                                  :key="indexChild">
                                                    <li>
                                                        <img :src="BASE_URL.sourceBaseUrl+itemChild.picAddress" alt="">
                                                        <div class="recommendPlan-bottom">
                                                            <div class="recommendPlan-bottom-text">
                                                                <p>{{itemChild.detailName}}</p>
                                                                <p>
                                                                    {{itemChild.designStyleName}}｜{{itemChild.spaceArea}}m²</p>
                                                            </div>
                                                            <div class="btn">装进我家<span></span></div>
                                                        </div>
                                                    </li>
                                                </el-carousel-item>
                                            </el-carousel>
                                            <!--<li v-for="(itemChild,indexChild) in item.configDetails"><img :src="BASE_URL.sourceBaseUrl+itemChild.picAddress" alt=""></li>-->
                                        </ul>
                                        <template v-else>
                                            <div class="empty-img">
                                                <img src="../../assets/images/zhanweitu.png" alt="">
                                            </div>
                                            <div class="recommendPlan-bottom">
                                                <div class="recommendPlan-bottom-text">
                                                    <p>方案名称</p>
                                                    <p>风格｜面积</p>
                                                </div>
                                                <div class="btn">装进我家<span></span></div>
                                            </div>
                                        </template>
                                    </div>
                                    <i class="ic_home_delete" v-if="item.active" @click="deleteModule(index)"></i>
                                </div>
                                <div class="addplanGoods" @click.stop="addModule('plan',index)">+添加方案模块</div>
                            </template>
                            <div class="addplanGoods" @click.stop="addModule('plan',index)" v-else>+添加方案模块</div>
                        </div>
                        <!--免费获取方案手机号-->
                        <div class="freeGetPhone" v-if="item.type=='obtainMobile'" @click="toggle(item,index)">
                            <div class="freeGetPhone-item" v-if="isFirst(item.type,index) || item.isAdd">
                                <div class="freeGetPhone-header" :class="{'borderActive':item.active}">
                                    <div class="header-module-title"><i></i>{{item.title || '标题'}}<i
                                            class="ic_recommend_bg"></i></div>
                                    <div class="freeGetPhone-content">
                                        <p>今日仅剩{{item.amount || '?'}}免费名额</p>
                                        <el-input v-model="input" placeholder="请输入你的姓名" class="freeInput"></el-input>
                                        <el-input v-model="input" placeholder="请输入你的手机号" class="freeInput"></el-input>
                                        <el-button type="primary" class="freeBtn">提交</el-button>
                                    </div>
                                </div>
                                <div class="addfreeGetPhone" @click.stop="addModule('obtainMobile',index)">
                                    +添加获取用户手机号模块
                                </div>
                                <i class="ic_home_delete" v-if="item.active" @click="deleteModule(index)"></i>
                            </div>
                            <div class="addfreeGetPhone" @click.stop="addModule('obtainMobile',index)" v-else>
                                +添加获取用户手机号模块
                            </div>
                        </div>
                        <div class="hotAct" v-if="item.type=='act'" @click="toggle(item,index)">
                            <template v-if="isFirst(item.type,index) || item.isAdd">
                                <div class="hotAct-content" :class="{'borderActive':item.active}">
                                    <div class="header-module-title"><i></i>{{item.title || '活动标题'}}<i
                                            class="ic_recommend_bg"></i></div>
                                    <ul>
                                        <template v-if="item.configDetails.length>0">
                                            <li v-for="(itemChild,iChild) in item.configDetails" :key="iChild">
                                                <img :src="itemChild.miniProImage" alt="" class="hotAct-img">
                                            </li>
                                        </template>
                                        <template v-else>
                                            <li>
                                                <div class="goods-img"><img src="../../assets/images/zhanweitu.png"
                                                                            alt=""></div>
                                            </li>
                                            <li>
                                                <div class="goods-img"><img src="../../assets/images/zhanweitu.png"
                                                                            alt=""></div>
                                            </li>
                                        </template>
                                    </ul>
                                </div>
                                <div class="addplanGoods" @click.stop="addModule('act',index)">+添加活动模块</div>
                                <i class="ic_home_delete" v-if="item.active" @click="deleteModule(index)"></i>
                            </template>
                            <div class="addplanGoods" @click.stop="addModule('act',index)" v-else>+添加活动模块</div>
                        </div>
                        <!--<img src="../../assets/images/zonghe_07.png" alt="" style="background-color: #fff" v-if="item.type=='act'">-->
                        <div class="recommend" @click="toggle(item,index)" :class="{'borderActive':item.active}"
                             v-if="item.type=='hotRecommend'">
                            <div class="recommend-item">
                                <div class="header-module-title"><i></i>{{item.title}}<i class="ic_recommend_bg"></i>
                                </div>
                                <ul v-if="item.configDetails.length>0">
                                    <li v-for="(itemChild,indexChild) in item.configDetails" :key="indexChild">
                                        <div class="goods-img"><img :src="BASE_URL.sourceBaseUrl+itemChild.picAddress"
                                                                    alt=""></div>
                                        <p class="goodsName">{{itemChild.spuName || itemChild.detailName}}</p>
                                        <p>￥{{itemChild.minPrice || itemChild.price || '来电询价'}}</p>
                                    </li>
                                </ul>
                                <div class="empty-img" v-else>
                                    <div class="empty-img-item">
                                        <div class="goods-img"><img src="../../assets/images/zhanweitu.png" alt="">
                                        </div>
                                        <p>商品标题 — —</p>
                                        <p>￥---</p>
                                    </div>
                                    <div class="empty-img-item">
                                        <div class="goods-img"><img src="../../assets/images/zhanweitu.png" alt="">
                                        </div>
                                        <p>商品标题 — —</p>
                                        <p>￥---</p>
                                    </div>
                                </div>
                            </div>
                            <i class="ic_home_delete" v-if="item.active" @click="deleteModule(index)"></i>
                        </div>
                        <div class="recommend" @click="toggle(item,index)" :class="{'borderActive':item.active}"
                             v-if="item.type=='newRecommend'">
                            <div class="recommend-item">
                                <div class="header-module-title"><i></i>新品推荐<i class="ic_recommend_bg"></i></div>
                                <ul v-if="item.configDetails.length>0">
                                    <li v-for="(itemChild,indexChild) in item.configDetails" :key="indexChild">
                                        <div class="goods-img"><img :src="BASE_URL.sourceBaseUrl+itemChild.picAddress"
                                                                    alt=""></div>
                                        <p class="goodsName">{{itemChild.spuName || itemChild.detailName}}</p>
                                        <p>￥{{itemChild.minPrice || itemChild.price || '来电询价'}}</p>
                                    </li>
                                </ul>
                                <div class="empty-img" v-else>
                                    <div class="empty-img-item">
                                        <div class="goods-img"><img src="../../assets/images/zhanweitu.png" alt="">
                                        </div>
                                        <p>商品标题 — —</p>
                                        <p>￥---</p>
                                    </div>
                                    <div class="empty-img-item">
                                        <div class="goods-img"><img src="../../assets/images/zhanweitu.png" alt="">
                                        </div>
                                        <p>商品标题 — —</p>
                                        <p>￥---</p>
                                    </div>
                                </div>
                            </div>
                            <i class="ic_home_delete" v-if="item.active" @click="deleteModule(index)"></i>
                        </div>
                        <div class="recommend" v-if="item.type=='moreGoods'">
                            <template v-if="isFirst(item.type,index) || item.isAdd">
                                <div class="recommend-item" :class="{'borderActive':item.active}"
                                     @click="toggle(item,index)">
                                    <div class="header-module-title"><i></i>{{item.title || '标题模块'}}<i
                                            class="ic_recommend_bg"></i></div>
                                    <ul v-if="item.configDetails.length>0">
                                        <li v-for="(itemChild,indexChild) in item.configDetails" :key="indexChild">
                                            <div class="goods-img"><img
                                                    :src="BASE_URL.sourceBaseUrl+itemChild.picAddress" alt=""></div>
                                            <p>{{itemChild.spuName || itemChild.detailName}}</p>
                                            <p>￥{{itemChild.minPrice || itemChild.price || '来电询价'}}</p>
                                        </li>
                                    </ul>
                                    <div class="empty-img" v-else>
                                        <div class="empty-img-item">
                                            <div class="goods-img"><img src="../../assets/images/zhanweitu.png" alt="">
                                            </div>
                                            <p>商品标题 — —</p>
                                            <p>￥---</p>
                                        </div>
                                        <div class="empty-img-item">
                                            <div class="goods-img"><img src="../../assets/images/zhanweitu.png" alt="">
                                            </div>
                                            <p>商品标题 — —</p>
                                            <p>￥---</p>
                                        </div>
                                    </div>
                                    <i class="ic_home_delete" v-if="item.active" @click="deleteModule(index)"></i>
                                </div>
                                <div class="addMoreGoods" @click.stop="addModule('moreGoods',index)">+添加商品模块</div>
                            </template>
                            <div class="addMoreGoods" @click.stop="addModule('moreGoods',index)" v-else>+添加商品模块</div>
                        </div>
                        <div class="brand-info" @click="toggle(item,index)" :class="{'borderActive':item.active}"
                             v-if="item.type=='company'">
                            <div class="brand-header">{{item.title}}</div>
                            <div class="brand-banner" v-html="item.richContext"></div>
                            <div class="brand-text" v-if="item.configDetails.length>0">
                                <div class="brand-text-header">品牌介绍</div>
                                <div class="brand-box">
                                    <div class="brand-type" :style="{marginLeft:`${removeX<0 ? 20 : 0}px`}">
                                        <ul :style="{ transform:`translateX(${removeX}px)`,width:`${totalWidth}px`}">
                                            <li v-for="(brand,i) in item.configDetails" :key="i" @click="brandTap(i)"
                                                :class="{'fontWeight':i==currentBrand}">{{brand.title}}
                                            </li>
                                        </ul>
                                    </div>
                                    <i class="ic_slide_left" @click="slideLeft" v-if="removeX<0"><─</i>
                                    <i @click="slideRight" v-if="item.configDetails.length>4">─></i>
                                </div>

                                <div class="brand-banner" v-html="item.configDetails[currentBrand].richContext"></div>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="content-bottom">
                    <img src="../../assets/images/tab.png" alt="">
                </div>
            </div>
            <!--banner设置-->
            <div class="dialog-module">
                <setBanner v-show="toggleCon.setBanner"></setBanner>
                <recommendPlan v-show="toggleCon.recommendPlan"></recommendPlan>
                <hotRecommend v-show="toggleCon.hotRecommend"></hotRecommend>
                <newRecommend v-show="toggleCon.newRecommend"></newRecommend>
                <moreRecommend v-show="toggleCon.moreRecommend"></moreRecommend>
                <brandInfo v-show="toggleCon.brandInfo"></brandInfo>
                <hotAct v-show="toggleCon.hotAct"></hotAct>
                <freePlan v-show="toggleCon.freePlan"></freePlan>
                <navSet v-show="toggleCon.navSet"></navSet>
                <setDialog v-show="toggleCon.setDialog"></setDialog>
                <baseInfo v-show="toggleCon.baseInfo"></baseInfo>
            </div>
        </div>
        <!--添加商品-->
        <addGoods></addGoods>
        <!--添加方案-->
        <addPlan></addPlan>
    </div>
</template>

<script>
    import {mapState, mapActions} from 'Vuex'
    import {filter, cloneDeep, map} from 'lodash'
    import setBanner from './conponents/setBanner'
    import recommendPlan from './conponents/recommendPlan'
    import hotRecommend from './conponents/hotRecommend'
    import newRecommend from './conponents/newRecommend'
    import moreRecommend from './conponents/moreRecommend'
    import brandInfo from './conponents/brandInfo'
    import hotAct from './conponents/hotAct'
    import addGoods from './conponents/addGoods'
    import addPlan from './conponents/addPlan'
    import freePlan from './conponents/freePlan'
    import navSet from './conponents/navSet'
    import setDialog from './conponents/setDialog'
    import baseInfo from './conponents/baseInfo'

    export default {
        name: "index",
        components: {
            setBanner,
            newRecommend,
            hotRecommend,
            addGoods,
            addPlan,
            recommendPlan,
            brandInfo,
            moreRecommend,
            hotAct,
            freePlan,
            navSet,
            setDialog,
            baseInfo
        },
        data() {
            return {
                input: '',
                pressDelay: 200,
                hideSortableGhost: true,
                toggleCon: {
                    setBanner: true,
                    recommendPlan: false,
                    brandInfo: false,
                    hotRecommend: false,
                    newRecommend: false,
                    moreRecommend: false,
                    hotAct: false,
                    freePlan: false,
                    navSet: false,
                    setDialog: false,
                    baseInfo: false,
                },
                currentBrand: 0,
                removeX: 0,
                //totalWidth:0
            }
        },
        created() {
            this.setInitdata(this);
        },
        mounted() {
            this.$dragging.$on('dragged', ({value}) => {
                if (value.group == 'moduleType') {
                    value.list.map((res, index) => {
                        res.active ? this.setTapIndex(index) : ''
                    });
                }
            })
        },
        computed: {
            ...mapState('homeManage', ['moreGoods', 'moduleType', 'actImgList', 'moduleTitle', 'companyData', 'bannerList', 'tapIndex']),
            navShow() {
                return this.moduleType[1].configDetails.some(res => {
                    return res.show
                })
            },
            totalWidth() {
                if (filter(this.moduleType, ['type', "company"])[0].configDetails.length > 0) {
                    return filter(this.moduleType, ['type', "company"])[0].configDetails.length * 80
                } else {
                    return '365'
                }
            }
        },
        methods: {
            ...mapActions('homeManage', ['setGoodsList', 'setInitdata', 'setPlansList', 'setSearchList', 'setMoreGoods', 'setSubmitMoreGoodsList', 'setSubmitPlansList', 'setTapIndex', 'setActList', 'setModuleTitle', 'setFreePlan', 'setSave', 'setSelectArr', 'setBaseInfo']),
            goodsLength(type, index) { //最后一个才显示
                let lastIndex = null;
                this.moduleType.map((item, index) => {
                    if (item.type == type) {
                        lastIndex = index;
                    }
                });
                return lastIndex == index;
            },
            slideRight() {
                if ((365 - filter(this.moduleType, ['type', "company"])[0].configDetails.length * 80) < this.removeX) {
                    this.removeX = this.removeX - 80;
                }
            },
            slideLeft() {
                if (this.removeX < 0) {
                    this.removeX = this.removeX + 80;
                }
            },
            changeLength(val) {
                this.moduleType[this.tapIndex].initialIndex = val + 1;
                this.$set(this.moduleType, this.tapIndex, this.moduleType[this.tapIndex]);
            },
            isFirst(type, index) { //是否是一个模块 && 这个模快是否有值
                if (filter(this.moduleType, ['type', type]).length > 1) {
                    return true
                } else {
                    let conf = type == 'obtainMobile' ? this.moduleType[index].uuid : this.moduleType[index].configDetails.length > 0;
                    if (conf) {
                        return true
                    } else {
                        return false
                    }
                }
            },
            brandTap(i) {
                this.currentBrand = i;
            },
            setBanner() { //固定
                this.setTapIndex(0);
                this.moduleType.map(res => {
                    res.active = false;
                })
                for (let key in this.toggleCon) {  //控制弹窗显示
                    this.toggleCon[key] = false
                }
                this.toggleCon.setBanner = true;

            },
            navSet() { //固定
                this.setTapIndex(1); //设置当前点击的是哪个
                this.moduleType.map(res => {
                    res.active = false;
                })
                for (let key in this.toggleCon) {  //控制弹窗显示
                    this.toggleCon[key] = false
                }
                this.toggleCon.navSet = true;
            },
            setDialog() { //固定
                this.setTapIndex(2);
                this.moduleType.map(res => {
                    res.active = false;
                })
                for (let key in this.toggleCon) {  //控制弹窗显示
                    this.toggleCon[key] = false
                }
                this.toggleCon.setDialog = true;
            },
            toggle(item, index) { //允许拖拽的模块
                console.log(index)
                console.log(item)
                this.setTapIndex(index);
                this.moduleType.map(res => {
                    res.active = false;
                })
                this.moduleType[index].active = true;
                this.$set(this.moduleType, index, this.moduleType[index]);
                for (let key in this.toggleCon) {  //控制弹窗显示
                    this.toggleCon[key] = false
                }

                switch (item.type) {
                    case 'plan':
                        setTimeout(() => {
                            this.toggleCon.recommendPlan = true;
                        }, 200)
                        this.setPlansList({vm: this, page: 1});
                        this.setSearchList({vm: this});
                        this.setSubmitPlansList({
                            type: 3,
                            title: this.moduleType[index].title,
                            list: cloneDeep(this.moduleType[index].configDetails)
                        });
                        break;
                    case 'obtainMobile':
                        setTimeout(() => {
                            this.toggleCon.freePlan = true;
                        }, 200)
                        this.setFreePlan(cloneDeep(this.moduleType[index]));
                        break;
                    case 'baseInfo':
                        setTimeout(() => {
                            this.toggleCon.baseInfo = true;
                        }, 200)
                        this.setBaseInfo(cloneDeep(this.moduleType[index]));
                        break;
                        break;
                    case 'act':
                        this.setActList(cloneDeep(this.moduleType[index].configDetails));
                        this.setSelectArr(map(this.moduleType[index].configDetails, 'value'));
                        setTimeout(() => {
                            this.toggleCon.hotAct = true;
                        }, 200)
                        break;
                    case 'newRecommend':
                        this.toggleCon.newRecommend = true;
                        this.setGoodsList({vm: this, page: 1, type: 1});
                        break;
                    case 'hotRecommend':
                        this.toggleCon.hotRecommend = true;
                        this.setGoodsList({vm: this, page: 1, type: 0});
                        break;
                    case 'moreGoods':
                        setTimeout(() => {
                            this.toggleCon.moreRecommend = true;
                        }, 200)
                        this.setGoodsList({vm: this, page: 1, type: 2});
                        this.setSubmitMoreGoodsList({res: cloneDeep(this.moduleType[index].configDetails), type: 3});//重新清空
                        break;
                    case 'company':
                        this.toggleCon.brandInfo = true;
                        break;
                }
                //标题切换
                this.setModuleTitle(this.moduleType[index].title);

            },
            addModule(type, index) {
                // console.log(index)
                //如果这个模块只有一个且是空的 添加时 把空模板显示出来
                if (type == 'obtainMobile') { //手机获取模块单独
                    this.moduleType[index].uuid ? this.moduleType.splice(index + 1, 0, {
                        type: type,
                        isAdd: true,
                        configDetails: []
                    }) : this.moduleType.splice(index, 1, {type: type, isAdd: true, configDetails: []});
                } else {
                    if (filter(this.moduleType, ['type', type]).length == 1 && this.moduleType[index].configDetails.length == 0) { //
                        this.moduleType[index].isAdd ? this.$message.error('新增的模块不能为空') : '';
                        this.moduleType.splice(index, 1, {type: type, isAdd: true, configDetails: []});
                    } else {
                        this.moduleType.splice(index + 1, 0, {type: type, isAdd: true, configDetails: []});
                    }
                }
                //新增模块时自动选中到下一个模块
                if (filter(this.moduleType, ['type', type]).length == 1 && this.moduleType[index].configDetails.length == 0) {
                    this.toggle({type: type}, index);
                } else {
                    this.toggle({type: type}, index + 1);
                }

            },
            deleteModule() {
                this.$confirm('此操作将删除该模块, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    for (let key in this.toggleCon) {  //控制弹窗显示
                        this.toggleCon[key] = false
                    }
                    this.setSave({vm: this, isDelete: true});
                }).catch(() => {
                });
            }
        }
    }
</script>

<style scoped lang="scss">
    .header-module-title {
        color: #ff6419;
        font-weight: bold;
        text-indent: 16px;
        font-size: 16px;
        padding: 20px 0;
        background-color: #fff;
        i {
            display: inline-block;
            width: 20px;
            height: 20px;
            background: no-repeat center url("../../assets/images/icons/icon_tuijian.png");
            background-size: 100%;
            vertical-align: top;
            margin-right: 8px;
        }
        .ic_recommend_bg {
            transform: rotate(180deg);
            margin-left: 8px;
            vertical-align: middle;
        }
    }

    .homeManage {
        width: 100%;
        //height: 100%;
        background-color: #fff;
        .ic_home_delete {
            display: inline-block;
            position: absolute;
            right: 0;
            top: 0;
            width: 32px;
            height: 32px;
            background: no-repeat center url("../../assets/images/ic_home_delete.png");
            background-size: 100%;
            cursor: pointer;
        }
        .homeManage-header {
            height: 44px;
            line-height: 44px;
            font-size: 16px;
            text-indent: 16px;
            border-bottom: 1px solid #f0f0f0;
        }

        .homeManage-content {
            width: 96%;
            height: 100%;
            background-color: #f0f0f0;
            margin: 24px auto 0 auto;
            display: flex;
            .borderActive {
                border: dashed 1.3px #295ee4;
            }
            .dialog-module {
                flex: 1;
                position: relative;
            }
            .homeManage-demo {
                width: 420px;
                height: 100%;
                background-color: #fff;
                margin-top: 26px;
                margin-left: 74px;
                .homeManage-tip {
                    background-color: #f0f0f0;
                    line-height: 48px;
                    color: #ff2323;
                }
                .homeManage-app {
                    width: 400px;
                    height: 660px;
                    margin: 0 auto;
                    overflow-y: scroll;
                    overflow-x: hidden;
                    background-color: #f0f0f0;
                    .banner {
                        width: 100%;
                        height: 213px;
                        position: relative;
                        background-color: #ebebeb;
                        cursor: pointer;
                        box-sizing: border-box;
                        .emptyImg {
                            position: absolute;
                            top: 50%;
                            left: 50%;
                            transform: translate(-50%, -50%);
                        }
                        .bannerImg {
                            width: 100%;
                            height: 213px;
                        }
                        &:hover {
                            border: dashed 1.3px #295ee4;
                        }
                    }
                    .navBox {
                        display: flex;
                        height: 106px;
                        background-color: #fff;
                        align-items: center;
                        justify-content: center;
                        margin-top: 1px;
                        &:hover {
                            border: dashed 1.3px #295ee4;
                        }
                        .nav-item {
                            flex: 1;
                            text-align: center;
                            img {
                                width: 56px;
                                height: 56px;
                                border-radius: 100%;
                            }
                        }
                    }
                    .setDialog {
                        .setDialog-content {
                            background-color: #fff;
                            padding-bottom: 20px;
                            &:hover {
                                border: dashed 1.3px #295ee4;
                            }
                            .setDialog-title {
                                padding: 20px 0 20px 0;
                                box-sizing: border-box;
                                position: relative;
                                font-size: 16px;
                                color: #333;
                                font-weight: bold;
                                text-indent: 16px;
                            }
                            .setDialog-banner {
                                width: 366px;
                                height: 150px;
                                margin: 0 auto;
                                background: no-repeat center url("../../assets/images/setDialog_bg.png");
                                background-size: 100%;
                                text-align: center;
                                p:nth-of-type(1) {
                                    height: 36px;
                                    font-size: 32px;
                                    font-stretch: normal;
                                    line-height: 42px;
                                    letter-spacing: 0;
                                    color: #ffffff;
                                    font-weight: bold;
                                    text-align: center;
                                    padding-top: 36px;
                                }
                                p:nth-of-type(2) {
                                    display: inline-block;
                                    max-width: 280px;
                                    height: 30px;
                                    background-color: #ffefc0;
                                    border-radius: 14px;
                                    margin: 15px auto 0 auto;
                                    color: #d12000;
                                    font-size: 16px;
                                    text-align: center;
                                    line-height: 30px;
                                    padding: 0 16px;
                                }
                            }
                            .setDialog-phone {
                                width: 326px;
                                height: 53px;
                                background-color: #f5f5f5;
                                border-radius: 26px;
                                text-indent: 20px;
                                color: #999999;
                                font-size: 16px;
                                line-height: 53px;
                                margin: 20px auto;
                            }
                            .setDialog-confirm {
                                width: 326px;
                                height: 54px;
                                background-color: #ff6419;
                                border-radius: 26px;
                                color: #ffffff;
                                line-height: 54px;
                                text-align: center;
                                font-size: 18px;
                                margin: 0 auto;
                            }
                            .setDialog-code {
                                @extend .setDialog-phone;
                                position: relative;
                                span {
                                    color: #ff6419;
                                    font-size: 16px;
                                    position: absolute;
                                    right: 20px;
                                }
                            }
                        }
                    }
                    .hotAct {
                        background-color: #fff;
                        margin-top: 5px;
                        margin-bottom: 8px;
                        position: relative;
                        .hotAct-content {
                            &:hover {
                                border: dashed 1.3px #295ee4;
                            }
                        }
                        .hotAct-img {
                            width: 180px;
                            height: 133px;
                        }
                        ul {
                            display: flex;
                            flex-wrap: wrap;
                            padding: 0 10px;
                            box-sizing: border-box;
                            li {
                                width: 50%;
                                padding-bottom: 20px;

                                .goods-img {
                                    width: 180px;
                                    height: 133px;
                                    background-color: #ebebeb;
                                    position: relative;
                                    margin: 0 auto;
                                    border-radius: 4px;
                                    img {
                                        position: absolute;
                                        top: 50%;
                                        left: 50%;
                                        transform: translate(-50%, -50%);
                                    }
                                }
                            }
                            .changeWidth {
                                width: 100%;
                            }
                        }
                    }
                    .freeGetPhone {
                        position: relative;
                        .freeGetPhone-item {
                            .freeGetPhone-header {
                                &:hover {
                                    border: dashed 1.3px #295ee4;
                                }
                            }
                            .freeGetPhone-content {
                                background-color: #f3f3f3;
                                padding: 25px 0;
                                text-align: center;
                                p {
                                    text-align: center;
                                }
                                .freeInput {
                                    width: 245px;
                                    margin-top: 10px;
                                }
                                .freeBtn {
                                    width: 245px;
                                    margin-top: 10px;
                                }
                            }
                        }
                        .addfreeGetPhone {
                            height: 44px;
                            line-height: 44px;
                            text-align: center;
                            color: #ff6419;
                            background-color: #fff;
                            border: 1px solid #ff6419;
                            cursor: pointer;
                        }
                    }
                    .companyBaseInfo {
                        padding-bottom: 20px;
                        background-color: #fff;
                        &:hover {
                            border: dashed 1.3px #295ee4;
                        }
                        .companyBaseInfo-content {
                            color: #333;
                            font-size: 16px;
                            border: 1px solid #f5f5f5;
                            box-shadow: 1px 1px 1px 1px #f5f5f5;
                            padding: 20px;
                            width: 90%;
                            margin: 0 auto;
                            box-sizing: border-box;
                            span {
                                font-weight: bold;
                            }
                            .item {
                                margin-bottom: 15px;
                                .ic_phone {
                                    display: inline-block;
                                    width: 20px;
                                    height: 20px;
                                    background: no-repeat center url("../../assets/images/icons/phone_xiangqing@3x.png");
                                    background-size: 100%;
                                    vertical-align: top;
                                    margin-left: 5px;
                                }
                                .ic_address {
                                    position: absolute;
                                    right: 0;
                                    top: 0;
                                    display: inline-block;
                                    width: 16px;
                                    height: 22px;
                                    background: no-repeat center url("../../assets/images/icons/location@3x.png");
                                    background-size: 100%;
                                    vertical-align: middle;
                                    margin-left: 5px;
                                }
                            }
                        }
                    }
                    .recommendPlan {
                        background-color: #fff;
                        margin-top: 5px;
                        position: relative;
                        &:hover {
                            border: dashed 1.3px #295ee4;
                        }
                        .recommendPlan-header {
                            padding: 20px 0 20px 0;
                            box-sizing: border-box;
                            position: relative;
                            img {
                                width: 160px;
                                height: 20px;
                            }
                            span {
                                position: absolute;
                                right: 10px;
                                top: 0;
                                color: #999;
                                font-size: 14px;
                                line-height: 68px;
                                em {
                                    font-style: normal;
                                    color: #333;
                                    font-weight: bold;
                                    font-size: 16px;
                                }
                            }
                        }
                        .recommendPlan-bottom {
                            padding: 0 20px;
                            position: relative;
                            .recommendPlan-bottom-text {
                                margin-top: 20px;
                                p {
                                    color: #333333;
                                    font-size: 15px;
                                }
                                p:nth-of-type(2) {
                                    font-size: 12px;
                                    line-height: 28px;
                                }

                            }
                            .btn {
                                position: absolute;
                                top: 0;
                                right: 30px;
                                width: 100px;
                                height: 36px;
                                color: #fff;
                                font-size: 16px;
                                text-align: center;
                                line-height: 36px;
                                background-color: #ff6419;
                                border-radius: 20px;
                                &:after {
                                    display: inline-block;
                                    content: '';
                                    width: 0;
                                    height: 0;
                                    border-top: 8px solid transparent;
                                    border-bottom: 8px solid transparent;
                                    border-left: 10px solid #fff;
                                    position: relative;
                                    top: 2.5px;
                                    left: 3px;
                                }
                            }
                        }
                        .planList {
                            width: 100%;
                            height: 285px;
                            position: relative;
                            overflow: hidden;
                            &::-webkit-scrollbar {
                                display: none
                            }
                            .empty-img {
                                position: relative;
                                width: 375px;
                                height: 205px;
                                background-color: #eeeeee;
                                margin-left: 10px;
                                img {
                                    position: absolute;
                                    left: 50%;
                                    top: 50%;
                                    transform: translate(-50%, -50%);
                                }

                            }
                            ul {
                                position: absolute;
                                width: 100%;
                                left: 0;
                                display: flex;
                                flex-wrap: nowrap;
                                li {
                                    position: relative;
                                    margin-left: 10px;
                                    img {
                                        height: 205px;
                                        width: 375px;
                                    }
                                }
                            }
                        }

                    }
                    .moreGoods {
                        position: relative;
                    }
                    .recommend {
                        background-color: #fff;
                        cursor: pointer;
                        position: relative;
                        .recommend-item {
                            &:hover {
                                border: dashed 1.3px #295ee4;
                            }
                        }

                        ul {
                            display: flex;
                            flex-wrap: wrap;

                            li {
                                width: 50%;
                                padding-bottom: 20px;
                                .goods-img {
                                    width: 179px;
                                    height: 179px;
                                    background-color: #ebebeb;
                                    position: relative;
                                    margin: 0 auto;
                                    img {
                                        width: 179px;
                                        height: 179px;
                                    }
                                }
                                p {
                                    text-indent: 14px;
                                    line-height: 24px;
                                }
                                .goodsName {
                                    white-space: nowrap;
                                    text-overflow: ellipsis;
                                    overflow: hidden;
                                }
                            }

                        }
                        .empty-img {
                            display: flex;
                            flex-wrap: wrap;
                            .empty-img-item {
                                width: 50%;
                                padding-bottom: 20px;
                                .goods-img {
                                    width: 179px;
                                    height: 179px;
                                    background-color: #ebebeb;
                                    position: relative;
                                    margin: 0 auto;
                                }
                                p {
                                    text-indent: 14px;
                                }
                            }
                            img {
                                position: absolute;
                                top: 50%;
                                left: 50%;
                                transform: translate(-50%, -50%);
                            }
                        }
                    }
                    .recommend-header {
                        height: 44px;
                        line-height: 44px;
                        text-indent: 16px;
                        color: #333333;
                        font-size: 16px;
                        padding-top: 20px;
                        font-weight: bold;
                    }
                    .addMoreGoods, .addplanGoods {
                        height: 44px;
                        line-height: 44px;
                        text-align: center;
                        color: #ff6419;
                        background-color: #fff;
                        border: 1px solid #ff6419;
                        cursor: pointer;
                        &:active {
                            opacity: .6;
                        }
                    }
                    .brand-info {
                        //margin-top: 10px;
                        background-color: #fff;
                        cursor: pointer;
                        box-sizing: border-box;
                        &:hover {
                            border: dashed 1.3px #295ee4;
                        }
                        .brand-header {
                            line-height: 48px;
                            height: 48px;
                            text-indent: 16px;
                            font-size: 16px;
                            color: #333;
                            font-weight: bold;
                        }
                        .brand-banner {
                            position: relative;
                            z-index: 1;
                            white-space: pre-wrap;
                        }
                        .brand-text {
                            //margin-top: -30px;
                            padding-top: 20px;
                            position: relative;
                            z-index: 9;
                            .brand-box {
                                position: relative;
                                i {
                                    display: inline-block;
                                    width: 22px;
                                    position: absolute;
                                    right: 0;
                                    top: 6px;
                                    color: #ff6419;
                                    font-weight: bold;
                                }
                                .ic_slide_left {
                                    position: absolute;
                                    left: 0;
                                    top: 6px;
                                    color: #ff6419;
                                    font-weight: bold;
                                }
                            }
                            .brand-text-header {
                                text-indent: 16px;
                                color: #333333;
                                font-size: 16px;
                                font-weight: bold;
                            }
                            .brand-type {
                                height: 36px;
                                box-sizing: border-box;
                                margin: 10px 20px 0 0;
                                overflow: hidden;
                                position: relative;
                                ul {
                                    height: 36px;
                                    display: flex;
                                    flex-wrap: nowrap;
                                    transition: all 0.5s;
                                    overflow: hidden;
                                    li {
                                        line-height: 32px;
                                        height: 32px;
                                        min-width: 80px;
                                        text-align: center;
                                    }
                                    .fontWeight {
                                        font-weight: bold;
                                        border-bottom: 3px solid #ff6419;
                                    }
                                }

                            }

                            p {
                                padding: 24px;
                                font-size: 12px;
                                text-align: justify;
                            }
                        }
                        .brand-more {
                            width: 368px;
                            height: 172px;
                            background-color: #fafafa;
                            color: #999999;
                            font-size: 14px;
                            line-height: 172px;
                            text-align: center;
                            margin: 10px auto 0 auto;
                        }
                    }
                }
                .content-bottom {
                    background-color: #fff;
                }
            }

            .homeManage-app::-webkit-scrollbar { /*滚动条整体样式*/
                width: 5px;
                background-color: #ebebeb;
            }
            .homeManage-app::-webkit-scrollbar-thumb { /*滚动条里面小方块*/
                background-color: #ff6419;
                border: solid 1px #ff6419;
            }
        }
    }
</style>
<style lang="scss">
    .homeManage {
        .brand-banner {
            img {
                width: 100%;
                height: 100%;
            }
            p {
                //text-align: justify;
            }
            .ql-align-center {
                text-align: center;
            }
            .ql-align-right {
                text-align: right;
            }
            .ql-indent-1 {
                text-indent: 3em;
            }
            .ql-indent-2 {
                text-indent: 6em;
            }
            .ql-indent-3 {
                text-indent: 9em;
            }
            .ql-indent-4 {
                text-indent: 12em;
            }
        }
        .banner {
            .el-carousel__container {
                height: 213px;
            }
        }
        .planList {
            .el-carousel__container {
                height: 285px;
            }
        }
    }
</style>