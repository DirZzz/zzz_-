import Vue from "vue";
import Router from "vue-router";
import Nav from "./components/nav/v_nav";

Vue.use(Router);
Vue.component("v-nav", Nav);
export default new Router({
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            console.log(savedPosition);
            return savedPosition;
        } else {
            return { x: 0, y: 0 };
        }
    },
    routes: [
        // {
        //   path: '/',
        //   name: 'home',
        //   component: Home,
        //   icon:require('&/assets/images/icon/icon-content.png'), //默认图标
        //   iconSel:require('&/assets/images/icon/icon-content2.png'),//选中图标
        //   permission:'company:member:manage', //权限设制
        //   leaf:true,   //是否有二级菜单 折叠展开
        //   routeIndex: 0,
        // },

        {
            path: "/",
            name: "运营管理",
            component: () =>
                import("./views/childIndex.vue"),
            icon: require("&/assets/images/icon/icon_01.png"),
            iconSel: require("&/assets/images/icon/icon_01_sct.png"),
            permission: "noPermission",
            routeIndex: 0,
            children: [{
                path: "/operation/exchangeArea",
                name: "互动区管理",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "topic:manage:view",
                children: [{
                    path: "/operation/exchangeArea/topic",
                    name: "话题管理",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/operation/exchangeArea/topic.vue"),
                    permission: "topic:topic:view"
                },
                {
                    path: "/operation/exchangeArea/comments",
                    name: "评论管理",
                    component: () =>
                        import("./views/operation/exchangeArea/comments.vue")
                },
                {
                    path: "/operation/exchangeArea/questionAnswer",
                    name: "问答管理",
                    component: () =>
                        import("./views/operation/exchangeArea/questionAnswer.vue"),
                    permission: "topic:askAndAnswer:view"
                },
                {
                    path: "/operation/exchangeArea/share",
                    name: "文章管理",
                    component: () =>
                        import("./views/operation/exchangeArea/share.vue"),
                    permission: "topic:share:view"
                },
                {
                    path: "/operation/exchangeArea/designer",
                    name: "设计改造管理",
                    component: () =>
                        import("./views/operation/exchangeArea/designer.vue"),
                    permission: "topic:designReform:view"
                },
                {
                    path: "/operation/exchangeArea/carryArticles",
                    name: "搬运文章",
                    component: () =>
                        import("./views/operation/exchangeArea/carryArticles.vue")
                },
                {
                    path: "/operation/exchangeArea/shop/select_Plan",
                    name: "搬运文章",
                    component: () =>
                        import("./views/operation/exchangeArea/shop/select_Plan.vue")
                },
                {
                    path: "/operation/exchangeArea/robotManage",
                    name: "机器人管理",
                    component: () =>
                        import("./views/operation/exchangeArea/robotManage.vue"),
                    permission: "topic:share:view"
                },
                ]
            },
            {
                path: "/operation/feedbackmangent",
                name: "反馈管理",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "noPermission",
                children: [{
                    path: "/operation/questionnaire/answerinfo",
                    name: "答卷信息",
                    component: () =>
                        import("./views/questionnaire/answerinfo/index.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/operation/questionnaire/userinfolist",
                    name: "用户信息列表",
                    component: () =>
                        import("./views/questionnaire/userinfolist/index.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/operation/questionnaire/answerconfig",
                    name: "问卷调查配置",
                    component: () =>
                        import("./views/questionnaire/answerconfig/index.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/operation/questionnaire/redpacketsstations",
                    name: "红包统计",
                    component: () =>
                        import("./views/questionnaire/redpacketsstations/index.vue"),
                    permission: "noPermission"
                }
                ]
            }/*,
             {
                path: "/operation/order",
                name: "订单管理",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "noPermission",
                children: [
                    {
                        path: "/operation/order/order",
                        name: "订单管理",
                        component: () =>
                            import("./views/order/order/list.vue"),
                        permission: "noPermission",
                        meta: {
                            keepAlive: true
                        }
                    },
                    {
                        path: "/operation/order/order/orderDetail/:id",
                        name: "详情",
                        component: () =>
                            import("./views/order/order/detail.vue")
                    },
                    {
                        path: "/operation/order/settlement",
                        name: "结算管理",
                        component: () =>
                            import("./views/order/settlement/list.vue"),
                        permission: "noPermission",
                        meta: {
                            keepAlive: true
                        }
                    },
                    {
                        path: "/operation/order/settlement/add",
                        name: "新增",
                        component: () =>
                            import("./views/order/settlement/add.vue")
                    },
                    {
                        path: "/operation/order/settlement/edit/:id",
                        name: "编辑",
                        component: () =>
                            import("./views/order/settlement/add.vue")
                    },
                    {
                        path: "/operation/order/settlement/detail/:id",
                        name: "详情",
                        component: () =>
                            import("./views/order/settlement/add.vue")
                    }
                ]
            } */
            ]
        },
        {
            path: "/content",
            name: "内容管理",
            component: () =>
                import("./views/childIndex.vue"),
            icon: require("&/assets/images/icon/icon_02.png"),
            iconSel: require("&/assets/images/icon/icon_02_sct.png"),
            permission: "noPermission",
            routeIndex: 0,
            children: [
                //供求信息
                {
                    path: "/basesupplydemand/list",
                    name: "供求信息管理",
                    component: () =>
                        import("./views/operation/basesupplydemand/list.vue"),
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    permission: "operation:basesupplydemand:view"
                },
                {
                    path: "/basesupplydemand/detail/:basesupplydemandId",
                    name: "详情",
                    component: () =>
                        import("./views/operation/basesupplydemand/detail.vue")
                },
                {
                    path: "/basesupplydemand/edit/:basesupplydemandId",
                    name: "编辑",
                    component: () =>
                        import("./views/operation/basesupplydemand/edit.vue")
                },
                {
                    path: "/storeManage/detail/:companyshopId",
                    name: "详情",
                    component: () =>
                        import("./views/operation/storeManage/detail.vue")
                },
                {
                    path: "/storeManage/edit/:companyshopId",
                    name: "编辑",
                    component: () =>
                        import("./views/operation/storeManage/edit.vue")
                },
                {
                    path: "/orderManage/edit/:orderManageId",
                    name: "编辑",
                    component: () =>
                        import("./views/operation/orderManage/edit.vue")
                },
                {
                    path: "/orderManage/add",
                    name: "新增",
                    component: () =>
                        import("./views/operation/orderManage/add.vue")
                },
                {
                    path: "/basesupplydemand/comment/list",
                    name: "评论管理",
                    component: () =>
                        import("./views/operation/basesupplydemand/comment/list.vue")
                },
                {
                    path: "/basesupplydemand/comment/detail",
                    name: "评论详情",
                    component: () =>
                        import("./views/operation/basesupplydemand/comment/detail.vue")
                },
                {
                    path: "/basesupplydemand/comment/edit",
                    name: "评论编辑",
                    component: () =>
                        import("./views/operation/basesupplydemand/comment/edit.vue")
                },
                //店铺管理
                {
                    path: "/storeManage/list",
                    name: "店铺管理",
                    component: () =>
                        import("./views/operation/storeManage/list.vue"),
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    permission: "operation:storeManage:view"
                },
                //最新动态管理
                {
                    path: "/dynamic/list",
                    name: "最新动态管理",
                    component: () =>
                        import("./views/operation/dynamic/list.vue"),
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    permission: "operation:dynamic:view"
                },
                //业主成交订单管理
                {
                    path: "/orderManage/list",
                    name: "成交订单管理",
                    component: () =>
                        import("./views/operation/orderManage/list.vue"),
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    permission: "operation:orderManage:view"
                },
                {
                    path: "/content/banner",
                    name: "Banner管理",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/content/index.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/content/indexPopups",
                    name: "首页弹窗管理",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/content/index.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/content/sharingActivities",
                    name: "活动分享管理",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/content/index.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/content/productList",
                    name: "商品列表管理",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/content/productList.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/content/productDetail/:id",
                    name: "商品列表管理详情",
                    component: () =>
                        import("./views/content/productDetail.vue"),
                },
            ]
        },
        {
            path: "/yewu",
            name: "业务管理",
            component: () =>
                import("./views/childIndex.vue"),
            icon: require("&/assets/images/icon/icon_02.png"),
            iconSel: require("&/assets/images/icon/icon_02_sct.png"),
            permission: "noPermission",
            routeIndex: 1,
            children: [{
                path: "/popularize",
                name: "推广信息",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "noPermission",
                routeIndex: 1,
                children: [{
                    path: "/ownerInfo",
                    name: "业主基础信息",
                    component: () =>
                        import("./views/popularize/ownerInfo.vue"),
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    permission: "decorate:ownerInfo:view",
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: "/decorCustomer",
                    name: "装修客户",
                    component: () =>
                        import("./views/popularize/decorCustomer.vue"),
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    permission: "decorate:decorateCustomer:view",
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: "/signedUp",
                    name: "已签约客户",
                    component: () =>
                        import("./views/popularize/signedUp.vue"),
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    permission: "decorate:signedUp:view"
                },
                {
                    path: "/refundment",
                    name: "退款管理",
                    component: () =>
                        import("./views/popularize/refundment.vue"),
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    permission: "decorate:refundment:view"
                },
                {
                    path: "/ownerInfoEdit",
                    name: "业主基础信息编辑",
                    component: () =>
                        import("./views/popularize/ownerInfoEdit.vue")
                },
                {
                    path: "/interReferral/:id",
                    name: "内部推荐页面",
                    component: () =>
                        import("./views/popularize/interReferral.vue")
                }
                ]
            },
            {
                path: "/feedback",
                name: "反馈管理",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "noPermission",
                routeIndex: 3,
                children: [{
                    path: "/feedback/sxw",
                    name: "随选网",
                    meta: { type: 16 },
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/feedback/feedback.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/feedback/app",
                    name: "移动端",
                    meta: { type: 1 },
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/feedback/feedback.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/feedback/qyx",
                    name: "企业小程序",
                    meta: { type: 14 },
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/feedback/feedback.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/feedback/sjht",
                    name: "商家管理后台",
                    meta: { type: 11 },
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/feedback/feedback.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/feedback/pub",
                    name: "通用版",
                    meta: { type: 2 },
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/feedback/feedback.vue"),
                    permission: "noPermission"
                }
                ]
            },
            {
                path: "/plan",
                name: "推荐方案管理",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "noPermission",
                routeIndex: 4,
                children: [{
                    path: "/plan/sxw",
                    name: "随选网",
                    component: () =>
                        import("./views/plan/plan.vue"),
                    permission: "noPermission"
                },
                {
                    path: "/plan/zxbj",
                    name: "装修报价",
                    component: () =>
                        import("./views/plan/quote.vue"),
                    permission: "noPermission"
                }
                ],
            }
            ]
        },
        {
            path: "/activity",
            name: "活动管理",
            component: () =>
                import("./views/childIndex.vue"),
            icon: require("&/assets/images/icon/icon_02.png"),
            iconSel: require("&/assets/images/icon/icon_02_sct.png"),
            permission: "noPermission",
            routeIndex: 2,
            children: [{
                path: "/activity",
                name: "运营活动",
                component: () =>
                    import("./views/activity/activity.vue"),
                icon: require("&/assets/images/icon/icon-content.png"),
                permission: "noPermission",
                iconSel: require("&/assets/images/icon/icon-content2.png")
            },
            //春节活动........
            {
                path: "/spring",
                name: "春节活动",
                component: () =>
                    import("./views/activity/spring.vue"),
                icon: require("&/assets/images/icon/icon-content.png"),
                permission: "noPermission",
                iconSel: require("&/assets/images/icon/icon-content2.png")
            },
            {
                path: "/wheel",
                name: "幸运大转盘活动",
                component: () =>
                    import("./views/activity/wheel.vue"),
                icon: require("&/assets/images/icon/icon-content.png"),
                permission: "noPermission",
                iconSel: require("&/assets/images/icon/icon-content2.png")
            },
            {
                path: "/addWheelAct",
                name: "新增转盘活动",
                component: () =>
                    import("./views/activity/addWheelAct.vue")
            },
            {
                path: "/editWheelAct",
                name: "编辑转盘活动",
                component: () =>
                    import("./views/activity/editWheelAct.vue")
            },
            {
                path: "/prizeManagement",
                name: "奖品管理",
                component: () =>
                    import("./views/activity/prizeManagement.vue")
            },
            {
                path: "/detailWheelAct",
                name: "详情转盘活动",
                component: () =>
                    import("./views/activity/detailWheelAct.vue")
            },
            {
                path: "/luckyDraw",
                name: "抽奖记录",
                component: () =>
                    import("./views/activity/luckyDraw.vue"),
                permission: "developing",
                iconSel: require("&/assets/images/icon/icon-content2.png")
            },
            {
                path: "/offlineActivity",
                name: "线下活动",
                component: () =>
                    import("./views/offlineActivity/offlineActivity.vue"),
                icon: require("&/assets/images/icon/icon-content.png"),
                permission: "noPermission",
                iconSel: require("&/assets/images/icon/icon-content2.png"),
            },
            {
                path: "/offlineActivity/addActivity",
                name: "新增活动",
                component: () =>
                    import("./views/offlineActivity/addActivity.vue"),


            },

            {
                path: "/offlineActivity/orderList",
                name: "订单列表",
                permission: "noPermission",
                icon: require("&/assets/images/icon/icon-content.png"),
                iconSel: require("&/assets/images/icon/icon-content2.png"),
                component: () =>
                    import("./views/offlineActivity/orderList.vue"),
            },
            {
                path: "/offlineActivity/orderDetail",
                name: "购买详情",
                component: () =>
                    import("./views/offlineActivity/orderDetail.vue"),
            },
            {
                path: "/offlineActivity/addStore",
                name: "添加门店",
                component: () =>
                    import("./views/offlineActivity/addStore.vue"),
            },
            ]
        },
        {
            path: "/userMessage",
            name: "用户管理",
            component: () =>
                import("./views/childIndex.vue"),
            icon: require("&/assets/images/icon/icon_02.png"),
            iconSel: require("&/assets/images/icon/icon_02_sct.png"),
            permission: "noPermission",
            routeIndex: 3,
            children: [{
                path: "/userTagsMessage",
                name: "用户标签管理",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "user:label:manage",
                routeIndex: 5,
                children: [{
                    path: "/userTagsMessage/configureUserTags",
                    name: "配置用户标签",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/userTagsMessage/configureUserTags.vue"),
                    permission: "user:label:view"
                },
                {
                    path: "/userTagsMessage/userTags",
                    name: "用户标签数据",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/userTagsMessage/userTags.vue"),
                    permission: "label:data:view"
                },
                {
                    path: "/userTagsMessage/userTagsStatistics",
                    name: "用户标签统计",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/userTagsMessage/userTagsStatistics.vue"),
                    permission: "user:label:data:view"
                }
                ]
            },
            {
                path: "/activityUserManage",
                name: "参与活动用户管理",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "noPermission",
                routeIndex: 5,
                children: [{
                    path: "/activityUserManage/activityUserInfo",
                    name: "参与活动用户信息",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/activityUserManage/activityUserManage.vue"),
                    permission: "noPermission",
                    meta: {
                        keepAlive: true
                    }
                }, {
                    path: "/activityUserManage/activityUserStatistics",
                    name: "参与活动用户统计",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/activityUserManage/activityUserStatistics.vue"),
                    permission: "noPermission"
                },]
            },
            {    //数据分析统计
                path: '/dataAnalysis',
                name: '数据分析统计',
                component: () => import('./views/childIndex.vue'),
                permission: 'noPermission',
                children: [
                    {
                        path: '/dataAnalysis/pageStatistics',
                        name: '页面统计',
                        component: () => import('./views/dataAnalysis/pageStatistics/index.vue'),
                        permission: 'noPermission',
                    },
                    {
                        path: '/dataAnalysis/elementStatistics',
                        name: '元素统计',
                        component: () => import('./views/dataAnalysis/elementStatistics/index.vue'),
                        permission: 'noPermission',
                    },
                    {
                        path: '/dataAnalysis/otherStatistics',
                        name: '其他行为统计',
                        component: () => import('./views/dataAnalysis/otherStatistics/index.vue'),
                        permission: 'noPermission',
                    },
                    {
                        path: '/dataAnalysis/pageStatistics/detail',
                        name: '页面详细分析',
                        component: () => import('./views/dataAnalysis/pageStatistics/detail.vue'),
                    },
                    {
                        path: '/dataAnalysis/elementStatistics/detail',
                        name: '元素详细分析',
                        component: () => import('./views/dataAnalysis/elementStatistics/detail.vue'),
                    },
                    {
                        path: '/dataAnalysis/otherStatistics/detail',
                        name: '其他详细分析',
                        component: () => import('./views/dataAnalysis/otherStatistics/detail.vue'),
                    },
                    {
                        path: '/dataAnalysis/retainedStatistics',
                        name: '留存统计',
                        component: () => import('./views/dataAnalysis/retainedStatistics/retainedStatistics.vue'),
                        permission: 'noPermission',
                    },
                    {
                        path: '/dataAnalysis/activeStatistics',
                        name: '活跃统计',
                        component: () => import('./views/dataAnalysis/activeStatistics/activeStatistics.vue'),
                        permission: 'noPermission',
                    },
                    {
                        path: '/dataAnalysis/statisticalAnalysis',
                        name: '统计分析',
                        component: () => import('./views/dataAnalysis/statisticalAnalysis/statisticalAnalysis.vue'),
                        permission: 'noPermission',
                    },
                    {
                        path: '/dataAnalysis/statisticalAnalysis/createrAnalysis',
                        name: '创建',
                        component: () => import('./views/dataAnalysis/statisticalAnalysis/createrAnalysis.vue'),
                    },
                    {
                        path: '/dataAnalysis/statisticalAnalysis/setAnalysis',
                        name: '编辑',
                        component: () => import('./views/dataAnalysis/statisticalAnalysis/setAnalysis.vue'),
                    },
                    {
                        path: '/dataAnalysis/statisticalAnalysis/analysisInfo',
                        name: '查看',
                        component: () => import('./views/dataAnalysis/statisticalAnalysis/analysisInfo.vue'),
                    }
                ]
            },
            {
                path: "/userAppointment",
                name: "用户预约管理",
                component: () =>
                    import("./views/childIndex.vue"),
                icon: require("&/assets/images/icon/icon_02.png"),
                iconSel: require("&/assets/images/icon/icon_02_sct.png"),
                permission: "noPermission",
                routeIndex: 5,
                children: [{
                    path: "/userAppointment/userAppointment",
                    name: "用户预约信息",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/userAppointment/userAppointment.vue"),
                    permission: "noPermission",
                    meta: {
                        keepAlive: true
                    }
                }, {
                    path: "/userAppointment/userAppointmentStatistics",
                    name: "用户预约数据统计",
                    icon: require("&/assets/images/icon/icon-content.png"),
                    iconSel: require("&/assets/images/icon/icon-content2.png"),
                    component: () =>
                        import("./views/userAppointment/userAppointmentStatistics.vue"),
                    permission: "noPermission"
                },]
            },
            ]
        },
        {
            path: "/operation/order",
            name: "订单管理",
            component: () =>
                import("./views/childIndex.vue"),
            icon: require("&/assets/images/icon/icon_02.png"),
            iconSel: require("&/assets/images/icon/icon_02_sct.png"),
            permission: "noPermission",
            children: [
                {
                    path: "/operation/order/order",
                    name: "订单管理",
                    component: () =>
                        import("./views/order/order/list.vue"),
                    permission: "noPermission",
                    /*meta: {
                        keepAlive: true
                    }*/
                },
                {
                    path: "/operation/order/order/orderDetail/:id",
                    name: "详情",
                    component: () =>
                        import("./views/order/order/detail.vue")
                },
                {
                    path: "/operation/order/settlement",
                    name: "结算管理",
                    component: () =>
                        import("./views/order/settlement/list.vue"),
                    permission: "noPermission",
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: "/operation/order/settlement/add",
                    name: "新增",
                    component: () =>
                        import("./views/order/settlement/add.vue")
                },
                {
                    path: "/operation/order/settlement/edit/:id",
                    name: "编辑",
                    component: () =>
                        import("./views/order/settlement/add.vue")
                },
                {
                    path: "/operation/order/settlement/detail/:id",
                    name: "详情",
                    component: () =>
                        import("./views/order/settlement/add.vue")
                }
            ]
        }
        /*{



                /*{
                    path: '/message',
                    name: '推送信息管理',
                    component: () => import('./views/childIndex.vue'),
                    icon: require('&/assets/images/icon/icon_03.png'),
                    iconSel: require('&/assets/images/icon/icon_03_sct.png'),
                    permission: 'noPermission',
                    routeIndex: 2,
                    children: [
                        {
                            path: '/systemMessage',
                            name: '推送消息管理',
                            component: () => import('./views/systemMessage/messageList.vue'),
                            permission: 'decorate:ownerInfo:view',
                        },
                        {
                            path: "/userTagsMessage/userTags",
                            name: "用户标签数据",
                            icon: require("&/assets/images/icon/icon-content.png"),
                            iconSel: require("&/assets/images/icon/icon-content2.png"),
                            component: () =>
                                import ("./views/userTagsMessage/userTags.vue"),
                            permission: "label:data:view"
                        },
                        {
                            path: "/userTagsMessage/userTagsStatistics",
                            name: "用户标签统计",
                            icon: require("&/assets/images/icon/icon-content.png"),
                            iconSel: require("&/assets/images/icon/icon-content2.png"),
                            component: () =>
                                import ("./views/userTagsMessage/userTagsStatistics.vue"),
                            permission: "user:label:data:view"
                        }
                    ]
                }]
            }

            /*{



                    /*{
                        path: '/message',
                        name: '推送信息管理',
                        component: () => import('./views/childIndex.vue'),
                        icon: require('&/assets/images/icon/icon_03.png'),
                        iconSel: require('&/assets/images/icon/icon_03_sct.png'),
                        permission: 'noPermission',
                        routeIndex: 2,
                        children: [
                            {
                                path: '/systemMessage',
                                name: '推送消息管理',
                                component: () => import('./views/systemMessage/messageList.vue'),
                                permission: 'decorate:ownerInfo:view',
                            },
                            {
                                path: '/messageDetail',
                                name: '推送消息详情',
                                component: () => import('./views/systemMessage/messageDetail.vue'),
                            },
                            {
                                path: '/messageAdd',
                                name: '推送消息新增',
                                component: () => import('./views/systemMessage/messageAdd.vue'),
                            },
                        ]
                    }*/

    ]

});
