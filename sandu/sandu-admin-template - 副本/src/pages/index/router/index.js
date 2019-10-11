import Vue from "vue";
import Router from "vue-router";
import posNav from "../views/rightContent/top/posNav";
import Nav from "../views/public/nav/v_nav.vue";
const _import = require("./_import_" + process.env.NODE_ENV);
Vue.component("pos-nav", posNav);
Vue.component("v-nav", Nav);
Vue.use(Router);
Vue.use(posNav);
Vue.use(Nav);
export const constantRouterMap = [
  {
    path: "/",
    name: "login",
    component: _import("login/login")
  },
  {
    // title:'404',
    path: "/404",
    name: "404",
    component: _import("HelloWorld2")
  }
];
let parentsNameOne = [
  // 父级菜单
  {
      name: "survey",
      title: "概况",
      permission: "company:view:panel",
      isView: true,
      icon: require("@/assets/images/icons/icon-content.png"),
      iconSel: require("@/assets/images/icons/icon-content2.png"),
      children: []
  },
  {
    name: "content",
    title: "内容库",
    permission: "platform:factory",
    isView: true,
    icon: require("@/assets/images/icons/icon-content.png"),
    iconSel: require("@/assets/images/icons/icon-content2.png"),
    children: []
  },
  /*{
        name: 'channel',
        title: '渠道管理',
        permission: 'platform:2b',
        isView: true,
        icon: require('@/assets/images/icons/icon-channel.png'),
        children:[]
    },
    {
        name: 'online',
        title: '线上管理',
        permission: 'platform:2c',
        isView: true,
        icon: require('@/assets/images/icons/icon-online.png'),
        children:[]
    },*/
  {
    // name: 'personal',
    name: "business",
    title: "企业设置",
    permission: "company:view,dealer:view,shop:view",
    //permission: true,
    isView: true,
    icon: require("@/assets/images/icons/icon-set.png"),
    children: []
  },
  {
    name: "smallProgram",
    title: "小程序后台",
    permission: "platform:miniapp",
    isView: true,
    icon: require("@/assets/images/icons/icon_miniapp.png"),
    children: []
  },
  {
    name: "miniAnalysis",
    title: "小程序数据统计",
    permission: "platform:analysis",
    isView: true,
    icon: require("@/assets/images/icons/icon_miniapp.png"),
    children: []
  },
  {
    name: "brandHouse",
    title: "品牌馆",
    permission: "brandhall:view",
    isView: sessionStorage.getItem("companyType") == 1 ? true : false,
    icon: require("@/assets/images/icons/icon_brand.png"),
    children: []
  },
  {
    name: "zqMiniProgram",
    title: "装企小程序",
    permission: "platform:zqMiniProgram",
    isView: true,
    icon: require("@/assets/images/icons/icon_miniapp.png"),
    children: []
  },
];
let parentsNameTwo = [
  {
    name: "product",
    title: "商品管理",
    permission: "",
    isView: true,
    icon: require("@/assets/images/icons/icon-content.png"),
    children: []
  },
  {
    name: "order",
    title: "订单管理",
    isView: true,
    icon: require("@/assets/images/icons/icon-content.png"),
    children: []
  }
];
export const OneParentsName = parentsNameOne;
export const twoParentsName = parentsNameTwo;
export const constantRouterMapOne = [
  {
    // 如果有子菜单，放在上级菜单的下面。parentName 与name 同名，视为上下级关系
    path: "/index",
    name: "index",
    component: _import("index"),
    children: [
      // 方案库--------------------------------------------方案库-----------------------------------------方案库--------
        {
            title: "概况",
            path: "/survey",
            permission: "company:view:panel",
            name: "survey",
            parentName: "survey",
            parentCname: "概况",
            isShow: false,
            isView: true,
            component: _import("survey/index")
        },
        {
        title: "方案库",
        path: "/plan",
        permission: "solution:view",
        name: "plan1",
        isShow: true,
        isView: true,
        active: false,
        meta: {
          keepAlive: true
        },
        parentName: "content",
        parentCname: "内容库",
        component: _import("contentLibrary/plan/plan")
      },
      // 共享方案库有子菜单 /share/income
      {
        title: "共享方案库",
        name: "sharePlan",
        path: "/share/plan",
        permission: "solution:openshare:view",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        active: false,
        parentName: "content",
        parentCname: "内容库",
        component: _import("contentLibrary/share/SharePlan")
      },
      {
        title: "查看收益",
        name: "shareIncome",
        path: "/share/income",
        permission: "solution:openshare:view",
        isShow: false,
        isView: false,
        meta: {
          keepAlive: true
        },
        active: false,
        parentName: "content",
        parentCname: "内容库",
        component: _import("contentLibrary/share/income/income")
      },
      // 共享方案库的子菜单
      // {
      //     title: '共享方案库的子菜单',
      //     path: '/plan',
      //     permission: 'solution:view',
      //     name: 'bbb_plan',
      //     isShow: true,
      //     parentName: 'sharePlan',
      //     parentCname: "内容库",
      //     component: _import('contentLibrary/plan/plan'),
      // },
      {
        title: "共享方案详情",
        path: "/plan/share/:planId",
        name: "sharePlanDetail",
        permission: "",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/share/sharePlanDetail")
      },
      {
        title: "方案详情",
        path: "/plan/onekey/:planId",
        permission: "solution:view",
        name: "planDeital",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/plan/planDetail")
      },

      // 产品库方案编辑
      {
        title: "智能方案编辑",
        path: "/plan/onekey/:planId/edit",
        permission: "solution:view",
        name: "OneKeyPlanEdit",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/plan/planEdit")
      },
      {
        title: "普通方案编辑",
        path: "/plan/common/:planId/edit",
        name: "普通方案编辑",
        permission: "solution:edit",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/plan/common/CommonPlanEdit")
      },
      {
        title: "普通方案详情",
        path: "/plan/common/:planId",
        name: "CommonPlanDetail",
        permission: "solution:view",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/plan/common/CommonPlanDetail")
      },
      //********************************报价方案*********************************** */
      {
        path: "/quptePlan/quptePlan",
        title: "装修报价",
        permission: "product:quptePlan",
        name: "quptePlan",
        isShow: true,
        isView: true,
        parentName: "content",
        parentCname: "内容库",
        component: _import("quptePlan/quptePlan")
      },
      // 产品库--------------------------------------------产品库-----------------------------------------产品库--------
      {
        title: "产品库",
        path: "/product",
        permission: "product:view",
        name: "product123",
        parentName: "content",
        parentCname: "内容库",
        isShow: true,
        isView: true,
        active: false,
        meta: {
          keepAlive: true
        },
        component: _import("contentLibrary/product/product")
      },
      {
          title: "合并产品",
          path: "/mergeProduct/:id",
          permission: "product:add",
          name: "mergeProduct",
          parentName: "content",
          parentCname: "内容库",
          isShow: false,
          isView: true,
          component: _import("contentLibrary/product/mergeProduct")
      },
      {
        title: "新增产品",
        path: "/addProduct",
        permission: "product:add",
        name: "addProduct",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/product/addProduct")
      },
      {
        title: "新增瓷砖贴图",
        path: "/addProductTexture",
        permission: "product:add",
        name: "addProductTexture",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/product/addProductTexture")
      },
      {
        title: "复制产品",
        path: "/copyProduct/:productId",
        permission: "product:add",
        name: "copyProduct",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/product/copyProduct")
      },
      {
        title: "产品编辑",
        path: "/editProduct/:proID",
        name: "editProduct",
        permission: "product:edit",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/product/editProduct")
      },
      {
        title: "产品编辑",
        path: "/editProductTexture/:proID",
        name: "editProductTexture",
        permission: "product:edit",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/product/editProductTexture")
      },
      {
        title: '产品复制',
        path: '/copyProductTexture/:proID',
        name: 'copyProductTexture',
        permission: 'product:edit',
        parentName: 'content',
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import('contentLibrary/product/copyProductTexture')
      },
      {
        title: "产品详情",
        path: "/detailProduct/:proID",
        name: "detailProduct",
        permission: "product:view",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/product/detailProduct")
      },
      {
        title: "产品详情",
        path: "/detailProductTexture/:proID",
        name: "detailProductTexture",
        permission: "product:view",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/product/detailProductTexture")
      },
      // 产品库产品组合
      {
        title: "产品组合编辑",
        path: "/productGroupEdit/:proID",
        name: "productGroupEdit",
        permission: "product:group:edit",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("productGroup/library/editProduct")
      },
      {
        title: "产品组合详情",
        path: "/productGroupDetail/:proID",
        name: "productGroupDetail",
        permission: "product:group:view",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("productGroup/library/productGroupDetail")
      },
      // 线上产品组合
      {
        title: "线上产品组合详情",
        path: "/onlineProductGroupDetail/:proID",
        name: "onlineProductGroupDetail",
        permission: "client:product:group:view",
        parentName: "content",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("productGroup/online/detailProduct")
      },
      {
        path: "/online/onlineProductGroupEdit/:proID",
        name: "onlineProductGroupEdit",
        permission: "client:product:group:edit",
        parentName: "online",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("productGroup/online/editProduct")
      },

      // 模型库--------------------------------------------模型库-----------------------------------------模型库--------
      {
        title: "模型库",
        path: "/model",
        permission: "model:view",
        name: "model",
        parentName: "content",
        parentCname: "内容库",
        meta: {
          keepAlive: true
        },
        isShow: true,
        isView: true,
        component: _import("contentLibrary/model/model")
      },
      {
        title: "模型添加",
        path: "/model/add",
        permission: "model:add",
        name: "addModel",
        parentName: "model",
        parentCname: "内容库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/model/ModelAdd")
      },
      {
        title: "模型详情",
        path: "/model/:modelId",
        name: "modelDetail",
        permission: "model:view",
        isShow: false,
        isView: true,
        parentName: "model",
        parentCname: "内容库",
        component: _import("contentLibrary/model/modelDetail")
      },
      {
        title: "模型编辑",
        path: "/model/:modelId/edit",
        name: "modelDetail",
        permission: "model:view",
        isShow: false,
        isView: true,
        parentName: "model",
        parentCname: "内容库",
        component: _import("contentLibrary/model/ModelEdit")
      },
      //  材质贴图库
      {
        title: "材质贴图库",
        permission: "texture:view",
        name: "texture",
        path: "/texture",
        parentName: "content",
        parentCname: "模型库",
        meta: {
          keepAlive: true
        },
        isShow: true,
        isView: true,
        component: _import("contentLibrary/texture/Texture")
      },
      {
        title: "材质贴图增加",
        path: "/texture/add",
        name: "AddTexture",
        permission: "texture:add",
        parentName: "content",
        parentCname: "材质贴图库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/texture/AddTexture")
      },
      {
        title: "材质贴图库编辑",
        path: "/texture/:textureId/edit",
        name: "UpdateTexture",
        permission: "texture:edit",
        parentName: "content",
        parentCname: "材质贴图库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/texture/UpdateTexture")
      },
      {
        title: "材质贴图详情",
        path: "/texture/:textureId",
        name: "TextureDetail",
        permission: "texture:edit",
        parentName: "content",
        parentCname: "材质贴图库",
        isShow: false,
        isView: true,
        component: _import("contentLibrary/texture/TextureDetail")
      },
      /*-------------------------------水刀模板 start--------------------------------------*/
      // {
      //     title: '水刀模板管理',
      //     name: 'waterKnife',
      //     path: '/content/waterKnife',
      //     permission: 'waterKnife:manage',
      //     isShow: true,
      //     isView: true,
      //     meta:{
      //         keepAlive:true
      //     },
      //     active: false,
      //     parentName: 'content',
      //     parentCname: "内容库",
      //     component: _import('contentLibrary/waterKnife/waterKnifeList')
      // },
      {
        title: "水刀模板新增",
        name: "waterKnife",
        path: "/waterKnife/waterKnifeAdd",
        permission: "waterKnife:add",
        isShow: false,
        isView: true,
        active: false,
        parentName: "content",
        parentCname: "内容库",
        component: _import("contentLibrary/waterKnife/waterKnifeAdd")
      },
      {
        title: "水刀模板编辑",
        name: "waterKnife",
        path: "/waterKnife/waterKnifeEdit/:id",
        permission: "waterKnife:edit",
        isShow: false,
        isView: true,
        active: false,
        parentName: "content",
        parentCname: "内容库",
        component: _import("contentLibrary/waterKnife/waterKnifeEdit")
      },
      {
        title: "水刀模板详情",
        name: "waterKnife",
        path: "/waterKnife/waterKnifeDetail/:id",
        permission: "waterKnife:view",
        isShow: false,
        isView: true,
        active: false,
        parentName: "content",
        parentCname: "内容库",
        component: _import("contentLibrary/waterKnife/waterKnifeDetail")
      },
      /*-------------------------------水刀模板 end--------------------------------------*/
      //我的资产
      {
        path: "/capital",
        title: "我的资产",
        permission: "",
        name: "capital",
        isShow: false,
        isView: true,
        parentName: "index",
        parentCname: "我的资产",
        component: _import("capital/index")
      },
      //我的银行卡 crate by huangsongbo 2018.12.10
      {
        path: "/capital/myBankCard",
        title: "我的银行卡",
        permission: "",
        name: "capital",
        isShow: false,
        isView: true,
        parentName: "index",
        parentCname: "我的银行卡",
        component: _import("capital/myBankCard/index")
      },
      // 套餐购买--------------------------------------
      {
        path: "/package",
        title: "套餐购买",
        permission: "",
        name: "package",
        isShow: false,
        isView: true,
        parentName: "index",
        parentCname: "套餐购买",
        component: _import("packageBuy/selectPackage"),
        children: [
          {
            path: "/packagelist",
            title: "可选套餐",
            permission: "",
            name: "packagelist",
            isShow: false,
            isView: true,
            parentName: "package",
            parentCname: "可选套餐",
            component: _import("packageBuy/packagelist/selectlist")
          },
          {
            path: "/record",
            title: "消费记录",
            permission: "",
            name: "record",
            isShow: false,
            isView: true,
            parentName: "package",
            parentCname: "套餐购买",
            component: _import("packageBuy/packagelist/record")
          },
          {
            path: "/buylist",
            title: "已购套餐",
            permission: "",
            name: "buylist",
            isShow: false,
            isView: true,
            parentName: "package",
            parentCname: "套餐购买",
            component: _import("packageBuy/packagelist/buylist")
          }
        ]
      },

      {
        title: "渠道产品组合编辑",
        path: `/channel/productGroupEdit/:proID`,
        name: "channelProductGroupEdit",
        permission: "biz:product:group:edit",
        isShow: false,
        isView: true,
        parentName: "channel",
        parentCname: "渠道管理",
        component: _import("productGroup/channel/editProduct")
      },
      {
        path: `/channel/productGroupDetail/:proID`,
        name: "channelProductGroupDetail",
        permission: "biz:product:group:edit",
        isShow: false,
        isView: true,
        parentName: "channel",
        parentCname: "渠道管理",
        component: _import("productGroup/channel/detailProduct")
      },
      // 企业设置------------------------------------------企业设置--------------------------------------企业设置--------
      /*{
                path: '/companyBaseInfo',
                title: '基础信息1',
                permission: 'solution:view',
                parentName: 'companyBaseInfo',
                parentCname: "用户管理",
                name: 'companyBaseInfo',
                isShow: true,
                component: _import('set/business/companyBaseMsg')
            },*/

      {
        path: "/companyBaseInfo",
        title: "基础信息1",
        permission: "solution:view",
        parentName: "companyBaseInfo",
        parentCname: "用户管理",
        name: "companyBaseInfo",
        isShow: true,
        isView: true,
        component: _import("set/business/companyBaseMsg")
      },

      // 个人设置---------------------------------------------------------
      {
        title: "个人设置",
        name: "personal",
        path: "/personal",
        permission: "",
        parentName: "personal",
        parentCname: "用户管理",
        isShow: true,
        isView: true,
        component: _import("set/person/person")
      },
      {
        title: "账号设置",
        name: "accountSet",
        path: "/personal/accountSet",
        permission: "",
        parentName: "accountSet",
        parentCname: "用户管理",
        isShow: true,
        isView: true,
        component: _import("set/person/accountSet"),
        children: [
          {
            title: "个人设置",
            name: "personalSet",
            path: "/personal/personalSet",
            permission: "",
            parentName: "accountSet",
            parentCname: "账号设置",
            isShow: true,
            isView: true,
            component: _import("set/person/personalSet")
          },
          {
            title: "修改密码",
            name: "updatePassword",
            path: "/personal/updatePassword",
            permission: "",
            parentName: "accountSet",
            parentCname: "账号设置",
            isShow: true,
            isView: true,
            component: _import("set/person/updatePassword")
          },
          {
            title: "修改手机号",
            name: "updateMobile",
            path: "/personal/updateMobile",
            permission: "",
            parentName: "accountSet",
            parentCname: "账号设置",
            isShow: true,
            isView: true,
            component: _import("set/person/updateMobile")
          }
        ]
      },
      //意见反馈开始-----------------
      {
        path: "/feedback",
        title: "意见反馈",
        permission: "coupon:edit",
        name: "feedback",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "意见反馈",
        component: _import("feedback/feedback")
      },
      {
        path: "/myFeedback",
        title: "我的反馈",
        permission: "coupon:edit",
        name: "myFeedback",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "我的反馈",
        component: _import("feedback/myFeedback")
      },
      //意见反馈结束-----------------
      /*{
                title: '企业设置',
                name: 'business',
                path: '/set/business/companyBaseMsg',
                permission: '',
                parentName: 'business',
                parentCname: "企业设置",
                isShow: true,

                component: _import('set/business/companyBaseMsg')
            },*/
      {
        title: "企业管理",
        name: "businessAdmin",
        path: "/business/company",
        permission: "company:view",
        parentName: "business",
        parentCname: "企业设置",
        isShow: true,
        isView: true,
        component: _import("business/company/company")
      },
      {
        title: "经销商企业管理",
        name: "saleBusinessAdmin",
        path: "/business/dealerBusiness",
        permission: "dealer:view",
        parentName: "business",
        parentCname: "企业设置",
        isShow: true,
        isView: true,
        component: _import("business/dealer/dealerBusiness")
      },
      {
        title: "活动",
        name: "activityList",
        path: "/activity/activityList",
        permission: "company:activity_view",
        parentName: "business",
        parentCname: "企业设置",
        isShow: true,
        isView: true,
        component: _import("business/activity/activityList")
      },
      {
        title: "厂商活动vue",
        name: "tabVue",
        path: "/activity/tab",
        permission: "company:view",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: false,
        component: _import("business/activity/tab"),
        children:[
          {
            title: "交易记录",
            name: "transactionRecord",
            path: "/activity/transaction-record",
            permission: "company:view",
            parentName: "business",
            parentCname: "企业设置",
            isShow: false,
            isView: false,
            component: _import("business/activity/transactionRecord")
          },
          {
            title: "可购买数据",
            name: "purchasable",
            path: "/activity/purchasable",
            permission: "company:view",
            parentName: "business",
            parentCname: "企业设置",
            isShow: false,
            isView: false,
            component: _import("business/activity/purchasable")
          },
          {
            title: "已购买数据",
            name: "purchased",
            path: "/activity/purchased",
            permission: "company:view",
            parentName: "business",
            parentCname: "企业设置",
            isShow: false,
            isView: false,
            component: _import("business/activity/purchased")
          }
        ]
      },
     
      {
        title: "经销商企业编辑",
        name: "dealerEdit",
        path: "/business/dealerEdit/:bid",
        permission: "dealer:edit",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/dealer/dealerEdit")
      },
      {
        title: "新增经销商企业 ",
        name: "dealerAdd",
        path: "/business/dealerAdd",
        permission: "dealer:add",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/dealer/dealerAdd")
      },

      {
        title: "经销商企业详情 ",
        name: "dealerDetail",
        path: "/business/dealerDetail/:bid",
        permission: "dealer:view",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/dealer/dealerDetail")
      },
      {
        title: "店铺管理",
        name: "shop",
        path: "/business/shop",
        permission: "shop:view",
        parentName: "business",
        parentCname: "企业设置",
        isShow: true,
        isView: true,
        component: _import("business/shop/shop")
      },
      {
        title: "选择方案",
        name: "shop",
        path: "/business/shop/select_Plan",
        permission: "",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/shop/select_Plan")
      },
      {
        title: "线下门店",
        name: "shopOffline",
        path: "/business/shopOffline",
        permission: "shopOffline:dealer:view",
        parentName: "business",
        parentCname: "企业设置",
        meta: {
          keepAlive: true
        },
        isShow: true,
        isView: true,
        component: _import("business/shopOffline/list")
      },
      {
        path: "/shopOffline/shopOfflineDetail/:shopOfflineId",
        title: "门店信息",
        permission: "shopOffline:dealer:view",
        name: "shopOfflineDetail",
        isShow: false,
        isView: true,
        parentName: "business",
        parentCname: "线下门店",
        component: _import("business/shopOffline/shopOfflineDetail")
      },
      // shopIntelligent
      {
        title: "店铺管理方案",
        name: "shopPlan",
        path: "/business/shopIntelligent",
        permission: "shop:view",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/shop/shopIntelligent")
      },
      // 店铺方案详情
      {
        title: "方案详情",
        path: "/shopIntelligent/:planId",
        name: "shopIntelligent",
        permission: "",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/shop/shopPlanDetail")
      },
      {
        title: "新增店铺",
        name: "editShop",
        path: "/business/editShop",
        permission: "",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/shop/editShop")
      },
      {
        title: "工程案例",
        name: "projectCase",
        path: "/business/projectCase",
        permission: "",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/shop/project_case")
      },
      {
        title: "博客栏目",
        name: "blog",
        path: "/business/blog",
        permission: "",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/shop/blog")
      },
      {
        title: "博文操作",
        name: "blogOperation",
        path: "/business/blogOperation/:type",
        permission: "",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/shop/blogOperation")
      },
      {
        title: "新增工程案例",
        name: "addProjectCase",
        path: "/business/addProjectCase",
        permission: "",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/shop/add_project_case")
      },
      {
        title: "账号管理",
        name: "account",
        path: "/business/account",
        permission: "company:user:view,dealer:user:view",
        parentName: "business",
        parentCname: "企业设置",
        isShow: true,
        isView: true,
        component: _import("business/account/account")
      },
      {
        title: "编辑账号",
        name: "editAccount",
        path: "/business/editAccount",
        permission: "",
        parentName: "business",
        parentCname: "企业设置",
        isShow: false,
        isView: true,
        component: _import("business/account/editAccount")
      },
      {
          title: "主子账号管理",
          name: "mainAccountSet",
          path: "/business/mainAccountSet",
          permission: "mainAccountSet",
          parentName: "business",
          parentCname: "企业设置",
          isShow: true,
          isView: true,
          component: _import("business/mainAccountSet/index")
      },
      {
          title: "主子账号管理编辑",
          name: "mainAccountSet",
          path: "/editMainAccount/:id",
          permission: "company:view",
          parentName: "business",
          parentCname: "企业设置",
          isShow: false,
          isView: true,
          component: _import("business/mainAccountSet/editMainAccount")
      },
      // 小程序后台-------------------------------------------
      //首页自定义管理
      {
        path: "/homeManage",
        title: "首页管理",
        permission: "miniapp:homePage:edit", //miniapp:homePage:edit
        name: "homeManage",
        isShow: true,
        isView: true,
        parentName: "smallProgram",
        parentCname: "小程序后台",
        component: _import("homeManage/index")
      },
      // 电子名片-------------------------------------------
      {
        path: "/vcard",
        title: "电子名片",
        permission: "zqMiniProgram:userCard:select",
        name: "vcard",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "zqMiniProgram",
        parentCname: "电子名片",
        component: _import("vcard/vcard")
      },
      {
        path: "/baseInfoIndex",
        title: "基本信息",
        permission: "zqMiniProgram:baseInfo:view",
        name: "baseInfoIndex",
        isShow: true,
        isView: true,
        parentName: "zqMiniProgram",
        parentCname: "小程序后台",
        component: _import("ViewSmallProgram/baseInfo/baseInfoIndex"),
        children: [
          {
            path: "/activity",
            title: "活动广告",
            permission: "zqMiniProgram:baseInfo:view",
            name: "activity",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("ViewSmallProgram/baseInfo/activity")
          },
          {
            path: "/friendBrand",
            title: "合作品牌",
            permission: "zqMiniProgram:baseInfo:view",
            name: "friendBrand",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("ViewSmallProgram/baseInfo/friendBrand")
          },
          {
            path: "/qualification",
            title: "企业资质",
            permission: "zqMiniProgram:baseInfo:view",
            name: "qualification",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("ViewSmallProgram/baseInfo/qualification")
          }
        ]
      },
      {
        path: "/proprietorInfo",
        title: "客户预约信息",
        permission: "zqMiniProgram:proprietorInfo:view",
        name: "proprietorInfo",
        isShow: true,
        isView: true,
        parentName: "zqMiniProgram",
        parentCname: "小程序后台",
        component: _import("ViewSmallProgram/customer/proprietorInfo")
      },
      // 订单管理--------------------------------------------

      {
        path: "/indexpage",
        title: "首页设置",
        permission: "miniapp:home:view",
        name: "defaultSet",
        isShow: true,
        isView: true,
        parentName: "smallProgram",
        parentCname: "小程序后台",
        component: _import("ViewSmallProgram/main/indexpage"),
        children: [
          {
            path: "/indexPageSet",
            title: "首页设置",
            permission: "miniapp:home:view",
            name: "indexSet",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("ViewSmallProgram/main/indexSet")
          },
          {
            path: "/brandIntro",
            title: "品牌介绍",
            permission: "miniapp:home:brand:edit",
            name: "brandIntro",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("ViewSmallProgram/main/brandIntro")
          },
          {
            path: "/spesell",
            title: "特卖商品",
            permission: "miniapp:home:brand:edit",
            name: "spesell",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("ViewSmallProgram/main/spesell")
          },
          {
            path: "/presell",
            title: "新品预售",
            permission: "miniapp:home:brand:edit",
            name: "spesell",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("ViewSmallProgram/main/presell")
          }
        ]
      },

      //线下门店管理
      {
        path: "/shopOffline",
        title: "线下门店管理",
        permission: "shopOffline:view",
        name: "shopOfflineManage",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "smallProgram",
        parentCname: "线下门店管理",
        component: _import("ViewSmallProgram/shopOffline/list")
      },
      {
        path: "/viewSmallProgram/addShopOffline",
        title: "新增线下门店",
        permission: "shopOffline:add",
        name: "shopOfflineAdd",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "线下门店管理",
        component: _import("ViewSmallProgram/shopOffline/addShopOffline")
      },
      {
        path: "/viewSmallProgram/editShopOffline/:id",
        title: "编辑线下门店",
        permission: "shopOffline:edit",
        name: "shopOfflineEdit",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "线下门店管理",
        component: _import("ViewSmallProgram/shopOffline/editShopOffline")
      },
      {
        path: "/viewSmallProgram/ShopOfflineDetail/:id",
        title: "线下门店详情",
        permission: "shopOffline:view",
        name: "shopOfflineDetail",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "线下门店管理",
        component: _import("ViewSmallProgram/shopOffline/shopOfflineDetail")
      },
      // 小程序后台商品管理
      {
        path: "/goods",
        title: "商品管理",
        permission: "miniapp:goods:view",
        name: "goods",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "smallProgram",
        parentCname: "商品管理",
        component: _import("ViewSmallProgram/goods/goods")
      },
      {
        path: "/goodsEdit",
        title: "商品编辑",
        permission: "miniapp:goods:view",
        name: "goods",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "商品管理",
        component: _import("ViewSmallProgram/goods/goodsEdit")
      },
      // {
      //     path: '/order',
      //     title: '订单管理',
      //     permission: 'miniapp:order:view',
      //     name: 'order',
      //     isShow: false,
      //     isView: true,
      //     meta:{
      //         keepAlive:true
      //     },
      //     parentName: 'smallProgram',
      //     parentCname: "订单管理1",
      //     component: _import('ViewSmallProgram/order/order'),
      // },
      {
        path: "/orderDetail/:id",
        title: "订单详情",
        permission: "miniapp:order:view",
        name: "detail",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "订单管理2",
        component: _import("ViewSmallProgram/order/detailMsg")
      },
      {
        path: "/orderlist",
        title: "订单管理",
        permission: "miniapp:order:view",
        name: "order",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "smallProgram",
        parentCname: "订单列表",
        component: _import("ViewSmallProgram/order/orderlist")
      },
      // 活动页面
      {
        path: "/activelist",
        title: "砍价活动",
        permission: "miniapp:bargain:view",
        name: "active",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "smallProgram",
        parentCname: "活动列表",
        component: _import("ViewSmallProgram/activity/activelist")
      },
      {
        path: "/activeAdd/:id",
        title: "新增活动",
        permission: "miniapp:bargain:add",
        name: "active",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "活动新增",
        component: _import("ViewSmallProgram/activity/activeAdd")
      },
      {
        path: "/activeEdit/:id",
        title: "修改活动",
        permission: "miniapp:bargain:edit",
        name: "active",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "活动新增",
        component: _import("ViewSmallProgram/activity/activeEdit")
      },
      {
        path: "/activeUserData/:id/:appId",
        title: "用户数据",
        permission: "miniapp:userData:view",
        name: "activeUserData",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "活动新增",
        component: _import("ViewSmallProgram/activity/activeUserData")
      },
      {
        path: "/statisticalData/:id",
        title: "统计数据",
        permission: "miniapp:countData:view",
        name: "statisticalData",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "活动新增",
        component: _import(
          "ViewSmallProgram/activity/activeUserStatisticalData"
        )
      },

      //拼团活动
      {
        path: "/assemActivity",
        title: "拼团活动",
        permission: "groupPurchaseActivity:view",
        name: "assemActivityManage",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "smallProgram",
        parentCname: "拼团活动",
        component: _import("ViewSmallProgram/assemActivity/list")
      },
      {
        path: "/assemActivity/add",
        title: "拼团活动新增",
        permission: "groupPurchaseActivity:add",
        name: "assemActivityAdd",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "拼团活动新增",
        component: _import("ViewSmallProgram/assemActivity/add")
      },
      {
        path: "/assemActivity/edit/:id",
        title: "拼团活动编辑",
        permission: "groupPurchaseActivity:edit",
        name: "assemActivityEdit",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "拼团活动编辑",
        component: _import("ViewSmallProgram/assemActivity/edit")
      },
      {
        path: "/assemActivity/basicInfo/:id",
        title: "拼团活动基本信息",
        permission: "groupPurchaseActivity:view",
        name: "assemActivityBaseInfo",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "拼团活动基本信息",
        component: _import("ViewSmallProgram/assemActivity/basicInfo")
      },
      {
        path: "/assemActivity/data/:id",
        title: "拼团活动数据",
        permission: "groupPurchaseActivity:view",
        name: "assemActivityBaseInfo",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "拼团活动数据",
        component: _import("ViewSmallProgram/assemActivity/data")
      },
      /*{
                path: '/chart/:id',
                title: '统计数据',
                permission: 'miniapp:order:view',
                name: 'userChart',
                isShow: false,
                isView: true,
                parentName: 'smallProgram',
                parentCname: "活动新增",
                component: _import('ViewSmallProgram/activity/userChart'),
            },*/
      {
        path: "/mininet",
        title: "微官网",
        permission: "miniapp:home:view",
        name: "mininet",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "微官网",
        component: _import("ViewSmallProgram/main/mininet")
      },
      // 业主管理
      {
        path: "/customer",
        title: "业主管理",
        permission: "customer:factory:view",
        name: "customer",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "smallProgram",
        parentCname: "业主管理",
        component: _import("ViewSmallProgram/customer/customer")
      },
      {
        path: "/ourCustomer",
        title: "我的业主",
        permission: "customer:agent:view",
        name: "ourcustomer",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "smallProgram",
        parentCname: "我的业主",
        component: _import("ViewSmallProgram/customer/ourCustomer")
      },
      {
        path: `/customerDetail/:id`,
        title: "业主详情",
        permission: "miniapp:home:view",
        name: "ourcustomer",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "我的业主",
        component: _import("ViewSmallProgram/customer/customerDetail")
      },
      {
        path: "/rules",
        title: "设置分配规则",
        permission: "customer:rule:view",
        name: "customer",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "业主管理",
        component: _import("ViewSmallProgram/customer/rules")
      },
      {
        path: "/addRule",
        title: "新增规则",
        permission: "customer:rule:add",
        name: "customer",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "业主管理",
        component: _import("ViewSmallProgram/customer/addRule")
      },
      {
        path: "/rulesDetail/:channelCompanyId",
        title: "业主跟进人详情",
        permission: "customer:rule:view",
        name: "customer",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "业主管理",
        component: _import("ViewSmallProgram/customer/rulesDetail")
      },
      // 品牌馆-------------------------------------------
      {
        path: "/brandHouse",
        title: "品牌馆",
        permission: "brandhall:view",
        name: "brandHouse1",
        isShow: true,
        isView: sessionStorage.getItem("companyType") == 1 ? true : false,
        parentName: "brandHouse",
        parentCname: "品牌馆",
        component: _import("brand/brand")
      },

      /*新增模块优惠券-----------------------------------------------------------*/
      //小程序商品推荐
      // {
      //     path: '/recommends',
      //     title: '商品推荐',
      //     permission: 'goods:recommend:view',
      //     name: 'recommends',
      //     isShow: true,
      //     isView:true,
      //     parentName: 'smallProgram',
      //     parentCname: "商品推荐",
      //     component: _import('activity/product/recommends'),
      // },
      // 小程序后台优惠券管理
      {
        path: "/coupons",
        title: "优惠券管理",
        permission: "coupon:view",
        name: "youhui",
        isShow: true,
        isView: true,
        parentName: "smallProgram",
        parentCname: "优惠券管理",
        component: _import("activity/coupon/coupons")
      },
      {
        path: "/ordercumerinfo",
        title: "预约客户信息",
        permission: "coupon:view",
        name: "yuyue",
        isShow: true,
        isView: true,
        parentName: "smallProgram",
        parentCname: "预约客户信息",
        component: _import("ordercumerinfo/index")
      },
      {
        path: "/couponAdd",
        title: "优惠券添加",
        permission: "coupon:add",
        name: "couponEdit",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "优惠券管理",
        component: _import("activity/coupon/couponAdd")
      },
      {
        path: "/couponEdit/:id",
        title: "优惠券编辑",
        permission: "coupon:edit",
        name: "couponEdit",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "优惠券管理",
        component: _import("activity/coupon/couponEdit")
      },
      // 电子名片-------------------------------------------
      {
        path: "/vcard",
        title: "电子名片",
        permission: "miniapp:usercard:select",
        name: "vcard",
        isShow: true,
        isView: true,
        meta: {
          keepAlive: true
        },
        parentName: "smallProgram",
        parentCname: "电子名片",
        component: _import("vcard/vcard")
      },
      //----------------------------------------------积分商城----------------------------------------------------
      {
        path: "/mall",
        title: "积分商城",
        redirect: "/imall/imallOrder/collect",
        permission: "points:mall:order:view",
        name: "mall",
        isShow: true,
        isView: true,
        parentName: "smallProgram",
        parentCname: "积分商城",
        component: _import("imall/imall"),
        children: [
          {
            path: "/imall/gift/index",
            title: "积分订单汇总",
            permission: "points:mall:order:view",
            name: "gift",
            isShow: false,
            isView: true,
            meta: {
              keepAlive: true
            },
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("imall/gift/index")
          },
          {
            path: "/imall/imallSlide/index",
            title: "礼品订单列表",
            permission: "points:mall:order:view",
            name: "imallSlide",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("imall/imallSlide/index")
          },
          {
            path: "/imall/imallOrder/collect",
            title: "礼品订单列表",
            permission: "points:mall:order:view",
            name: "imallOrderCollect",
            isShow: false,
            isView: true,
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("imall/imallOrder/collect")
          },
          {
            path: "/imall/imallOrder/orderList/:status",
            title: "礼品订单列表",
            permission: "points:mall",
            name: "orderList",
            isShow: false,
            isView: true,
            meta: {
              keepAlive: true
            },
            parentName: "defaultSet",
            parentCname: "小程序后台",
            component: _import("imall/imallOrder/orderList")
          }
        ]
      },
      {
        path: "/imall/gift/create",
        title: "新增礼品",
        permission: "points:mall",
        name: "indexSet",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "小程序后台",
        component: _import("imall/gift/create")
      },
      {
        path: "/imall/gift/edit/:id",
        title: "编辑礼品",
        permission: "points:mall:gift:edit",
        name: "indexSet",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "小程序后台",
        component: _import("imall/gift/edit")
      },
      {
        path: "/imall/imallOrder/orderDetail/:id",
        title: "礼品订单列表详情",
        permission: "points:mall:order:view",
        name: "indexSet",
        isShow: false,
        isView: true,
        parentName: "defaultSet",
        parentCname: "小程序后台",
        component: _import("imall/imallOrder/orderDetail")
      },

      /**add by WangHaiLin 未添加权限 **/
      //小程序数据统计
      {
        path: "/userAnalysis",
        title: "用户统计",
        permission: "platform:analysis",
        name: "userAnalysis",
        isShow: true,
        isView: true,
        parentName: "miniAnalysis",
        parentCname: "小程序数据统计",
        component: _import("miniAnalysis/userAnalysis")
      },
      {
        path: "/areaAnalysis",
        title: "区域数据",
        permission: "platform:analysis",
        name: "areaAnalysis",
        isShow: true,
        isView: true,
        parentName: "miniAnalysis",
        parentCname: "小程序数据统计",
        component: _import("miniAnalysis/areaAnalysis")
      },
      {
        path: "/sourceAnalysis",
        title: "用户来源",
        permission: "platform:analysis",
        name: "sourceAnalysis",
        isShow: true,
        isView: true,
        parentName: "miniAnalysis",
        parentCname: "小程序数据统计",
        component: _import("miniAnalysis/sourceAnalysis")
      },
      {
        path: "/replaceAnalysis",
        title: "产品数据",
        permission: "platform:analysis",
        name: "replaceAnalysis",
        isShow: true,
        isView: true,
        parentName: "miniAnalysis",
        parentCname: "小程序数据统计",
        component: _import("miniAnalysis/productReplace")
      },
      {
        path: "/productAnalysis",
        title: "商品数据",
        permission: "platform:analysis",
        name: "productAnalysis",
        isShow: true,
        isView: true,
        parentName: "miniAnalysis",
        parentCname: "小程序数据统计",
        component: _import("miniAnalysis/productAnalysis")
      },
      {
        path: "/planAnalysis",
        title: "方案数据",
        permission: "platform:analysis",
        name: "planAnalysis",
        isShow: true,
        isView: true,
        parentName: "miniAnalysis",
        parentCname: "小程序数据统计",
        component: _import("miniAnalysis/planAnalysis")
      }
    ]
  },
  {
    title: "内容",
    name: "content",
    path: "/app",
    permission: "solution:view",
    active: false,
    isView: true,
    component: _import("main/main")
  }
];
export const constantRouterMaptwo = [
  {
    path: "/index",
    name: "index",
    component: _import("index"),
    children: [
      // 订单--------------------------------------------
      // {
      //     path: '/order',
      //     title: '订单管理',
      //         permission: 'miniapp:order:view',
      //     name: 'order',
      //     isShow: false,
      //     isView: true,
      //     parentName: 'order',
      //     parentCname: "订单管理1",
      //     component: _import('ViewSmallProgram/order/order'),
      // },
      {
        path: "/orderDetail",
        title: "订单详情",
        permission: "miniapp:order:view",
        name: "detail",
        isShow: false,
        isView: true,
        parentName: "order",
        parentCname: "订单管理2",
        component: _import("ViewSmallProgram/order/detailMsg")
      },
      {
        path: "/orderlist",
        title: "订单管理3",
        permission: "miniapp:order:view",
        name: "order",
        isShow: true,
        isView: true,
        parentName: "order",
        parentCname: "订单列表",
        component: _import("ViewSmallProgram/order/orderlist")
      },
      /*新增模块优惠券-----------------------------------------------------------*/
      //小程序商品推荐
      {
        path: "/recommends",
        title: "商品推荐",
        permission: "goods:recommend:view",
        name: "recommends",
        isShow: true,
        isView: true,
        parentName: "smallProgram",
        parentCname: "商品推荐",
        component: _import("activity/product/recommends")
      },
      // 小程序后台优惠券管理
      {
        path: "/coupons",
        title: "优惠券管理",
        permission: "coupon:view",
        name: "goods",
        isShow: true,
        isView: true,
        parentName: "smallProgram",
        parentCname: "优惠券管理",
        component: _import("activity/coupon/coupons")
      },
      {
        path: "/couponAdd",
        title: "优惠券添加",
        permission: "coupon:add",
        name: "couponEdit",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "优惠券管理",
        component: _import("activity/coupon/couponAdd")
      },
      {
        path: "/couponEdit/:couponId",
        title: "优惠券编辑",
        permission: "coupon:edit",
        name: "couponEdit",
        isShow: false,
        isView: true,
        parentName: "smallProgram",
        parentCname: "优惠券管理",
        component: _import("activity/coupon/couponEdit")
      }
    ]
  },
  {
    path: "/app",
    title: "内容",
    permission: "solution:view",
    name: "content",
    active: false,
    component: _import("main/main")
  }
];

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRouterMap
});


//报错 NavigationDuplicated 添加这下面一段代码，就可以解决报错
const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}
