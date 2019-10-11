import Vue from 'vue'
import Router from 'vue-router'
import Nav from './components/nav/v_nav'

Vue.use(Router)
Vue.component('v-nav', Nav)
export default new Router({
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            console.log(savedPosition)
            return savedPosition
        } else {
            return { x: 0, y: 0 }
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
            path: '/package',
            name: '套餐管理',
            component: () => import('./views/childIndex.vue'),
            icon: require('&/assets/images/icon/icon_01.png'),
            iconSel: require('&/assets/images/icon/icon_01_sct.png'),
            permission: 'root.service.manage',
            children: [
                //套餐列表
                {
                    path: '/packagelist/list',
                    name: '套餐列表',
                    component: () => import('./views/package/packagelist/packagelist.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                    permission: 'service.manage.view',
                    meta: {
                        keepAlive: true
                    }

                },
                {
                    path: '/packagelist/meal',
                    name: '套餐列表',
                    component: () => import('./views/package/packagelist/addMeal.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },
                {
                    path: '/packagelist/editMeal',
                    name: '套餐列表',
                    component: () => import('./views/package/packagelist/editMeal.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },
                {
                    path: '/packagelist/setPriceList',
                    name: '套餐列表',
                    component: () => import('./views/package/packagelist/setPriceList.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },
                {
                    path: '/packagelist/setPrice',
                    name: '套餐列表',
                    component: () => import('./views/package/packagelist/setPrice.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },
                {
                    path: '/packagelist/relation',
                    name: '套餐列表',
                    component: () => import('./views/package/packagelist/relation.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },
                {
                    path: '/packagelist/editPrice',
                    name: '套餐列表',
                    component: () => import('./views/package/packagelist/editPrice.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },
                {
                    path: '/packagelist/member',
                    name: '成员账号',
                    component: () => import('./views/package/packagelist/member.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },
                {
                    path: '/packagelist/memberInfo',
                    name: '成员账号详情',
                    component: () => import('./views/package/packagelist/memberInfo.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },

                //企业代购
                {
                    path: '/buying/list',
                    name: '企业代购',
                    component: () => import('./views/package/buying/buying.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                    permission: 'service.buy.view',
                },
                {
                    path: '/buying/add',
                    name: '新增企业代购',
                    component: () => import('./views/package/buying/addBuying.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    iconSel: require('&/assets/images/icon/icon-content2.png'),
                },


            ]
        },
        //用户管理
        {
            path: '/userManage',
            name: '用户管理',
            component: () => import('./views/childIndex.vue'),
            icon: require('&/assets/images/icon/icon_01.png'),
            iconSel: require('&/assets/images/icon/icon_01_sct.png'),
            permission: 'root.user.manage',
            children: [
                //用户列表
                {
                    path: '/userManage/userList',
                    name: '用户列表',
                    component: () => import('./views/userManage/userList/index'),
                    permission: 'user.manage.view',
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: '/userManage/info',
                    name: '详情页',
                    component: () => import('./views/userManage/userList/userInfo'),
                },
                {
                    path: '/userManage/mfrsManage',
                    name: '厂商列表',
                    component: () => import('./views/userManage/mfrsManage/index'),
                    permission: 'manufacturer.manage.view',
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: '/userManage/companyList',
                    name: '公司列表',
                    component: () => import('./views/userManage/companyList/index'),
                    permission: 'company.manage.view',
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: '/userManage/edit',
                    name: '编辑',//用户列表
                    component: () => import('./views/userManage/userList/listEdit'),
                },
                {
                    path: '/userManage/platformSet',
                    name: '平台设置',
                    component: () => import('./views/userManage/userList/platformSet'),
                },
                {
                    path: '/userManage/userSetGroup',
                    name: '配置角色组',
                    component: () => import('./views/userManage/userList/userSetGroup'),
                },
                {
                    path: '/userManage/mfrsManage/userManage',
                    name: '用户管理', //厂商用户管理
                    component: () => import('./views/userManage/mfrsManage/userManage'),
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: '/userManage/mfrsManage/dealerManage/:parentCompanyId',
                    name: '经销商企业',
                    component: () => import('./views/userManage/mfrsManage/dealerManage'),
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: '/userManage/mfrsManage/mfrsAdd',
                    name: '新增',//厂商管理
                    component: () => import('./views/userManage/mfrsManage/mfrsAdd'),
                },
                {
                    path: '/userManage/mfrsManage/mfrsEdit',
                    name: '编辑',//厂商管理
                    component: () => import('./views/userManage/mfrsManage/mfrsEdit'),
                },
                {
                    path: '/userManage/mfrsManage/mfrsInfo',
                    name: '编辑',//厂商管理
                    component: () => import('./views/userManage/mfrsManage/mfrsInfo'),
                },
                {
                    path: '/userManage/companyList/companyAdd',
                    name: '新增',//公司列表
                    component: () => import('./views/userManage/companyList/companyAdd'),
                },
                {
                    path: '/userManage/companyList/companyEdit',
                    name: '编辑',//公司列表
                    component: () => import('./views/userManage/companyList/companyEdit'),
                },
                {
                    path: '/userManage/companyList/companyInfo',
                    name: '公司列表',
                    component: () => import('./views/userManage/companyList/companyInfo'),
                },
                {
                    path: '/userManage/companyList/userManage',
                    name: '用户管理',//公司列表
                    component: () => import('./views/userManage/companyList/userManage'),
                    meta: {
                        keepAlive: true
                    }
                },
                {
                    path: '/userManage/mfrsManage/dealerAdd',
                    name: '新增',//厂商列表 经销商企业
                    component: () => import('./views/userManage/mfrsManage/dealerAdd'),
                },
                {
                    path: '/userManage/mfrsManage/dealerEdit',
                    name: '编辑',//厂商列表 经销商企业
                    component: () => import('./views/userManage/mfrsManage/dealerEdit'),
                },
                {
                    path: '/userManage/mfrsManage/dealerInfo',
                    name: '编辑',//厂商列表 经销商企业
                    component: () => import('./views/userManage/mfrsManage/dealerInfo'),
                },
                {
                    path: '/userManage/setUser',
                    name: '配置角色',//用户列表 配置角色
                    component: () => import('./views/userManage/userList/setUser')
                }
            ]
        },
        //企业小程序数据分析
        {
            path: '/miniAnalysis',
            name: '小程序数据统计',
            component: () => import('./views/childIndex.vue'),
            icon: require('&/assets/images/icon/icon_01.png'),
            iconSel: require('&/assets/images/icon/icon_01_sct.png'),
            permission: 'platform:analysis',
            routeIndex: 4,
            children: [
                /*{
                    path: '/miniAnalysis/setup',
                    name: '系统设置',
                    component: () => import('./views/dataAnalysis/systemSteUp.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    permission: 'noPermission',
                    iconSel: require('&/assets/images/icon/icon-content2.png')
                },*/
                {
                    path: '/miniAnalysis/user',
                    name: '用户统计',
                    component: () => import('./views/dataAnalysis/userAnalysis.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    permission: 'platform:analysis',
                    iconSel: require('&/assets/images/icon/icon-content2.png')
                },
                {
                    path: '/miniAnalysis/area',
                    name: '区域数据',
                    component: () => import('./views/dataAnalysis/areaAnalysis.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    permission: 'platform:analysis',
                    iconSel: require('&/assets/images/icon/icon-content2.png')
                },
                {
                    path: '/miniAnalysis/source',
                    name: '用户来源',
                    component: () => import('./views/dataAnalysis/sourceAnalysis.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    permission: 'platform:analysis',
                    iconSel: require('&/assets/images/icon/icon-content2.png')
                },
                {
                    path: '/miniAnalysis/replace',
                    name: '产品数据',
                    component: () => import('./views/dataAnalysis/productReplace.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    permission: 'platform:analysis',
                    iconSel: require('&/assets/images/icon/icon-content2.png')
                },
                {
                    path: '/miniAnalysis/product',
                    name: '商品数据',
                    component: () => import('./views/dataAnalysis/productAnalysis.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    permission: 'platform:analysis',
                    iconSel: require('&/assets/images/icon/icon-content2.png')
                },
                {
                    path: '/miniAnalysis/plan',
                    name: '方案数据',
                    component: () => import('./views/dataAnalysis/planAnalysis.vue'),
                    icon: require('&/assets/images/icon/icon-content.png'),
                    permission: 'platform:analysis',
                    iconSel: require('&/assets/images/icon/icon-content2.png')
                }
            ]
        },
        // 推广信息模块
        {
            path: '/promotionInfo',
            name: '推广信息',
            component: () => import('./views/childIndex.vue'),
            icon: require('&/assets/images/icon/icon_01.png'),
            iconSel: require('&/assets/images/icon/icon_01_sct.png'),
            permission: 'proprietor:list:view',
            children: [
                {
                    path: '/promotionInfo/ownerInfo',
                    name: '业主信息',
                    component: () => import('./views/promotionInfo/ownerInfo/index'),
                    permission: 'proprietor:list:view',
                },
                {
                    path: '/promotionInfo/designerInfo',
                    name: '设计师入驻信息',
                    component: () => import('./views/promotionInfo/designerInfo/index'),
                    permission: 'proprietor:list:view',
                },
                {
                    path: '/promotionInfo/decorationInfo',
                    name: '设计装修公司入驻信息',
                    component: () => import('./views/promotionInfo/decorationInfo/index'),
                    permission: 'proprietor:list:view',
                },
                {
                    path: '/promotionInfo/smallProgramInfo',
                    name: '小程序意向企业/经销商',
                    component: () => import('./views/promotionInfo/smallProgramInfo/index'),
                    permission: 'proprietor:list:view',
                },
            ]
        },
        // 小程序管理后台
        {
            path: '/programManage',
            name: '小程序管理后台',
            component: () => import('./views/childIndex.vue'),
            icon: require('&/assets/images/icon/icon_01.png'),
            iconSel: require('&/assets/images/icon/icon_01_sct.png'),
            permission: 'noPermission',
            children: [
                {
                    path: '/programManage/programManage',
                    name: '小程序管理后台',
                    component: () => import('./views/programManage/programManage.vue'),
                    permission: 'noPermission',
                },
                {
                    path: '/programManage/programDetail',
                    name: '详情',
                    component: () => import('./views/programManage/programDetail.vue'),
                },
            ]
        },
        // B端数据统计
        {
            path: '/businessDataStatistics',
            name: 'B端数据统计',
            component: () => import('./views/childIndex.vue'),
            icon: require('&/assets/images/icon/icon_01.png'),
            iconSel: require('&/assets/images/icon/icon_01_sct.png'),
            permission: 'noPermission',
            children: [
                {
                    path: '/businessDataStatistics/dataStatistics/0',
                    name: '用户数据统计',
                    component: () => import('./views/businessDataStatistics/dataStatistics.vue'),
                    permission: 'noPermission',
                },
                {
                    path: '/businessDataStatistics/dataStatistics/1',
                    name: '企业数据统计',
                    component: () => import('./views/businessDataStatistics/dataStatistics.vue'),
                    permission: 'noPermission',
                },
                {
                    path: '/businessDataStatistics/dataStatistics/2',
                    name: '户型数据统计',
                    component: () => import('./views/businessDataStatistics/dataStatistics.vue'),
                    permission: 'noPermission',
                },
                {
                    path: '/businessDataStatistics/dataStatistics/3',
                    name: '方案数据统计',
                    component: () => import('./views/businessDataStatistics/dataStatistics.vue'),
                    permission: 'noPermission',
                },
                {
                    path: '/businessDataStatistics/dataStatistics/4',
                    name: '区域数据统计',
                    component: () => import('./views/businessDataStatistics/dataStatistics.vue'),
                    permission: 'noPermission',
                },
            ]
        },
    ]
})
