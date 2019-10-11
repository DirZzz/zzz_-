import {exportData, fetch, formPost} from "../utils/fetch";
import axios from "axios";
//备注：同一个域名下的接口写在一个模板里 如：VUE_APP_BaseUrl 是基础的域名 统一放在index.js里
// 如果需要请求不同的域名下的接口 就新建一个js文件  文件名 命名规范为 请求域名的名字

let basePath = process.env;
let BUrl = basePath.baseUrl;
export default {
    login(params) {
        return axios.post(basePath.ssoUrl + "/v1/user/login", params, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        });
    },
    getLoginUserAdminPlatform(params) {
        const url = basePath.ucUrl + `/v1/user/manage/getLoginUserAdminPlatform`;
        return fetch("get", url, params);
    },
    loginOut(params) {
        let url = basePath.ssoUrl + "/v1/user/logout";
        return fetch("post", url, params);
    },
    // 发送请求
    sendRequest() {
        return fetch(...arguments);
    },
    // 签约客户列表接口
    signedUpList(params) {
        let url = BUrl + "/v1/decorateOrder/listContract";
        return fetch("get", url, params);
    },
    // 退款列表
    refundList(params) {
        let url = BUrl + "/v1/decorateOrder/listRefund";
        return fetch("get", url, params);
    },
    // 客户上传合同 /v1/decorateOrder/uploadContract
    uploadContractURL(params) {
        let url = BUrl + "/v1/decorateOrder/uploadContract";
        return fetch("put", url, params);
    },
    // 更改收款状态
    updateRefundStatus(params) {
        let url = BUrl + "/v1/decorateOrder/updateReceipt";
        return fetch("put", url, params);
    },
    // 审核通过，驳回 /v1/decorateOrder/updateRefund
    updateRefund(params) {
        let url = BUrl + "/v1/decorateOrder/updateRefund";
        return fetch("put", url, params);
    },

    //供求信息
    queryBasesupplydemand(params) {
        let url = BUrl + "/v1/basesupplydemand/list";
        return fetch("get", url, params);
    },
    getBasesupplydemandById(params) {
        let url = BUrl + `/v1/basesupplydemand/${params.basesupplydemandId}`;
        return fetch("get", url, params);
    },
    updateBasesupplydemand(params) {
        let url = BUrl + "/v1/basesupplydemand";
        return fetch("put", url, params);
    },
    patchDeletesupplydemand(params) {
        let url = BUrl + "/v1/basesupplydemand/del";
        return fetch("delete", url, params);
    },
    deleteBasesupplydemandById(params) {
        let url = BUrl + "/v1/basesupplydemand";
        return fetch("delete", url, params);
    },
    baseSupplyToTopById(params) {
        let url =
            BUrl +
            `/v1/basesupplydemand/toTop?basesupplydemandId=${
            params.basesupplydemandId
            }&topId=${params.topId}`;
        return fetch("put", url, params);
    },
    getCategoryList(params) {
        let url = BUrl + "/v1/basesupplydemand/getCategoryList";
        return fetch("get", url, params);
    },
    refreshSupplyById(params) {
        let url =
            BUrl +
            `/v1/basesupplydemand/toRefresh?basesupplydemandId=${
            params.basesupplydemandId
            }&topId=${params.topId}`;
        return fetch("put", url, params);
    },
    queryAllCategoryList(params) {
        let url = BUrl + "/v1/basesupplydemand/queryAllCategoryList";
        return fetch("get", url, params);
    },

    //店铺管理
    querycompanyshop(params) {
        let url = BUrl + "/v1/companyshop/list";
        return fetch("get", url, params);
    },
    getcompanyshopById(params) {
        let url = BUrl + `/v1/companyshop/${params.companyshopId}`;
        return fetch("get", url, params);
    },
    updatecompanyshop(params) {
        let url = BUrl + "/v1/companyshop";
        return fetch("put", url, params);
    },
    deletecompanyshopById(params) {
        let url = BUrl + "/v1/companyshop";
        return fetch("delete", url, params);
    },
    companyshopToTopById(params) {
        let url =
            BUrl +
            `/v1/companyshop/toTop?companyshopId=${params.companyshopId}&topId=${
            params.topId
            }`;
        return fetch("put", url, params);
    },
    getShopList(params) {
        let url = BUrl + "/v1/companyshop/getShopList";
        return fetch("get", url, params);
    },
    modifyStoreScore(params) {
        let url = BUrl + `/v1/companyshop/modifyEnableScore`;
        return fetch("get", url, params);
    },
    refreshStoreById(params) {
        let url =
            BUrl +
            `/v1/companyshop/toRefresh?companyshopId=${params.companyshopId}&topId=${
            params.topId
            }`;
        return fetch("put", url, params);
    },

    //最新动态管理
    queryDynamic(params) {
        let url = BUrl + "/v1/dynamic/list";
        return fetch("get", url, params);
    },
    getDynamicById(params) {
        let url = BUrl + `/v1/dynamic/${params.dynamicId}`;
        return fetch("get", url, params);
    },
    updateDynamic(params) {
        let url = BUrl + "/v1/dynamic";
        return fetch("put", url, params);
    },
    deleteDynamicById(params) {
        let url = BUrl + `/v1/dynamic?id=${params.id}&type=${params.type}`;
        return fetch("delete", url);
    },
    dynamicToTopById(params) {
        let url =
            BUrl +
            `/v1/dynamic/toTop?id=${params.id}&topId=${params.topId}&type=${
            params.type
            }`;
        return fetch("put", url, params);
    },
    refreshDynamicById(params) {
        let url =
            BUrl +
            `/v1/dynamic/toRefresh?id=${params.id}&topId=${params.topId}&type=${
            params.type
            }`;
        return fetch("put", url, params);
    },

    //业主成交订单管理
    queryOrderManage(params) {
        let url = BUrl + "/v1/orderManage/list";
        return fetch("get", url, params);
    },
    createOrderManage(params) {
        let url = BUrl + "/v1/orderManage";
        return fetch("post", url, params);
    },
    getOrderManageById(params) {
        let url = BUrl + `/v1/orderManage/${params.orderManageId}`;
        return fetch("get", url, params);
    },
    updateOrderManage(params) {
        let url = BUrl + "/v1/orderManage";
        return fetch("put", url, params);
    },
    deleteOrderManageById(params) {
        let url = BUrl + "/v1/orderManage";
        return fetch("delete", url, params);
    },
    getUserCategoryList(params) {
        let url = BUrl + "/v1/orderManage/getCategoryList";
        return fetch("get", url, params);
    },
    // 业主基础信息--获取需求类型
    getRequirementTypeList(params = {}) {
        let url = BUrl + "/v1/proprietor/info/getRequirementType";
        return fetch("get", url, params);
    },
    //删除业主
    deleteOwner(id) {
        let url = BUrl + `/v1/proprietor/info/${id}`;
        return fetch("delete", url);
    },
    // 业主基础信息--获取城市列表
    getCityList(params = {}) {
        let url = BUrl + "/v1/proprietor/info/getCityList";
        return fetch("get", url, params);
    },
    // 业主基础信息--获取客户类型列表
    getCustomerType(params = {}) {
        let url = BUrl + "/v1/proprietor/info/getCustomerType";
        return fetch("get", url, params);
    },
    // 业主基础信息-业主基础信息列表
    getCustomerInfoList(params) {
        let url = BUrl + "/v1/proprietor/info/list";
        return fetch("post", url, params);
    },
    // 业主基础信息-业主基础信息详情
    getCustomerInfo(params) {
        let url = BUrl + "/v1/proprietor/info/info";
        return fetch("get", url, params);
    },
    // 业主基础信息-保存基础信息
    updateCustomerInfo(params) {
        let url = BUrl + "/v1/proprietor/info/update";
        return fetch("post", url, params);
    },

    /**------抢单客户-------------*/
    // 获取客户列表
    listDecorateCustomer(param) {
        let url = BUrl + "/v1/decorateCustomer/list";
        return fetch("get", url, param);
    },
    // 获取字典数据
    initOptionsByType(type) {
        let url = BUrl + `/v1/dictionary/with/${type}`;
        return fetch("get", url);
    },
    //获取客户 详情
    getCustomerInfoWithInner(id) {
        let url = BUrl + `/v1/decorateCustomer/${id}`;
        return fetch("get", url);
    },
    //获取内部推荐装企列表
    getInnerRecommendList(param) {
        let url = BUrl + `/v1/company/shop/decorate`;
        return fetch("get", url, param);
    },
    //保存内部推荐
    updateCustomerCompanyShop(param) {
        let url =
            BUrl +
            `/v1/decorateCustomer/inner/recommend/${param.customerId}/${
            param.companyId
            }`;
        return fetch("get", url);
    },
    // 编辑客户
    updateDecorateCustomer(param) {
        let url = BUrl + `/v1/decorateCustomer`;
        return fetch("put", url, param);
    },
    //获取城市级联数据
    initCityTree() {
        let url = BUrl + `/v1/dictionary/area/tree`;
        return fetch("get", url);
    },
    //获取城市级联数据
    submitToSedKill(id) {
        let url = BUrl + `/v1/decorateCustomer/submit/sedkill/${id}`;
        return fetch("get", url);
    },
    //获取编辑客户签约企业下拉框
    listCompanyOptionWithCustomer(id) {
        let url = BUrl + `/v1/company/shop/decorate/${id}`;
        return fetch("get", url);
    },

    /**------抢单客户-------------*/

    //  区域选择接口
    areaList(params) {
        const url = basePath.systemUrl + `/v1/base/area/list`;
        return fetch("post", url, params);
    },
    //  创建活动
    createActivity(params) {
        const url = basePath.systemUrl + `/v1/act2/redpacket/create`;
        return formPost("post", url, params);
    },
    //  结束活动
    finishActivity(params) {
        const url = basePath.systemUrl + `/v1/act2/redpacket/finish`;
        return formPost("post", url, params);
    },
    //  活动列表
    getActivityList(params) {
        let url = basePath.systemUrl + `/v1/act2/redpacket/list`;
        return fetch("get", url, params);
    },

    // 运营管理后台 -> 运营管理 -> 订单管理 ->start
    getOrderList(params) {
        const url = basePath.orderUrl + `/v1/operation/order/getList`;
        return fetch("get", url, params);
    },
    exportOrderList(params) {
        const url = basePath.orderUrl + `/v1/operation/order/exportList`;
        return exportData("get", url, params);
    },
    spGetOrderDetail(params) {
        const url = basePath.orderUrl + `/v1/order/get/detail?id=${params.id}`;
        return fetch("get", url, params);
    },
    createSettlementOrder(params) {
        const url = basePath.orderUrl + `/v1/settlementOrder/create`;
        return fetch("post", url, params);
    },
    getSettlementList(params) {
        const url = basePath.orderUrl + `/v1/settlementOrder/settlementOrderList`;
        return fetch("get", url, params);
    },
    exportSettlementList(params) {
        const url = basePath.orderUrl + `/v1/settlementOrder/export`;
        return exportData("get", url, params);
    },
    getSettlementOrderDetail(params) {
        const url = basePath.orderUrl + "/v1/settlementOrder/getDetail";
        return fetch("get", url, params);
    },
    updateSettlementOrder(params) {
        const url = basePath.orderUrl + `/v1/settlementOrder/update`;
        return fetch("post", url, params);
    },
    deleteSettlementOrder(params) {
        const url = basePath.orderUrl + "/v1/settlementOrder/delete";
        return fetch("get", url, params);
    },
    getActualAmount(params) {
        const url = basePath.orderUrl + "/v1/operation/order/getActualAmount";
        return fetch("get", url, params);
    },
    // 运营管理后台 -> 运营管理 -> 订单管理 ->end

    /**----------客户反馈管理---------------**/
    //获取反馈列表
    showFeedbackList(params) {
        const url = basePath.systemUrl + `/v1/feedback/manage/feedbackList`;
        return fetch("get", url, params);
    },

    //反馈问题答复
    feedbackReply(params) {
        const url = basePath.systemUrl + `/v1/feedback/manage/feedbackReply`;
        return fetch("post", url, params);
    },

    //反馈问题导出
    feedbackExport(params) {
        const url = basePath.systemUrl + `/v1/feedback/manage/feedbackExport`;
        return fetch("get", url, params);
    },

    //反馈问题详情
    feedbackDetail(params) {
        const url = basePath.systemUrl + `/v1/feedback/manage/feedbackDetail`;
        return fetch("get", url, params);
    },

    //删除反馈问题
    feedbackDelete(params) {
        const url = basePath.systemUrl + `/v1/feedback/manage/feedbackDelete`;
        return fetch("delete", url, params);
    },

    //获取反馈企业
    feedbackCompany(params) {
        const url = basePath.systemUrl + `/v1/feedback/manage/feedbackCompany`;
        return fetch("delete", url, params);
    },
    //春节活动转盘获取
    getWheelList(params) {
        const url = BUrl + `/v1/springActivity/getWheelList`;
        return fetch("get", url, params);
    },

    //春节活动列表
    getSpringActivityList(params) {
        const url = BUrl + `/v1/springActivity/list`;
        return fetch("get", url, params);
    },

    //回显活动转盘信息
    getWheelInfo(params) {
        const url = BUrl + `/v1/springActivity/getWheelInfo`;
        return fetch("get", url, params);
    },

    addSpringActivity(params) {
        const url = BUrl + `/v1/springActivity`;
        return fetch("post", url, params);
    },

    exportSpringActivity(params) {
        const url = BUrl + `/v1/springActivity/export`;
        return exportData("get", url, params);
    },
    //获取反馈企业
    feedbackCompany(params) {
        const url = basePath.systemUrl + `/v1/feedback/manage/feedbackCompany`;
        return fetch("delete", url, params);
    },

    //春节活动转盘获取
    getWheelList(params) {
        const url = BUrl + `/v1/springActivity/getWheelList`;
        return fetch("get", url, params);
    },

    //大转盘活动
    luckywheelList(params) {
        //转盘列表
        const url = basePath.systemUrl + `/v1/act3/luckywheel/pageList`;
        return fetch("get", url, params);
    },
    luckywheelFinish(params) {
        //结束活动
        const url =
            basePath.systemUrl + `/v1/act3/luckywheel/finish?actId=${params.actId}`;
        return fetch("post", url, params);
    },
    luckywheelDelete(params) {
        //删除活动
        const url =
            basePath.systemUrl + `/v1/act3/luckywheel/delete?actId=${params.actId}`;
        return fetch("post", url, params);
    },
    getPageList(params) {
        //奖品列表
        const url = basePath.systemUrl + `/v1/act3/luckywheel/prize/pageList`;
        return fetch("get", url, params);
    },
    savePrize(params) {
        //修改奖品
        const url = basePath.systemUrl + `/v1/act3/luckywheel/prize/save`;
        return fetch("post", url, params);
    },
    luckywheelCreate(params) {
        //创建活动
        const url = basePath.systemUrl + `/v1/act3/luckywheel/create`;
        return fetch("post", url, params);
    },
    luckywheelModify(params) {
        //修改活动
        const url = basePath.systemUrl + `/v1/act3/luckywheel/modify`;
        return fetch("post", url, params);
    },
    getRecordPageList(params) {
        //修改活动
        console.log(params);
        const url =
            basePath.systemUrl + `/v1/act3/luckywheel/lottery/record/pageList`;
        return fetch("get", url, params);
    },

    luckywheelDetail(params) {
        //活动详情
        const url = basePath.systemUrl + `/v1/act3/luckywheel/getDetail`;
        return fetch("get", url, params);
    },
    /**----------内容管理---------------**/
    getBannerList(params) {
        //获取banner列表接口
        const url = basePath.systemUrl + `/v1/sxw/banner/getList`;
        return fetch("post", url, params);
    },
    getDialogList(params) {
        //获取弹窗列表接口
        const url = basePath.systemUrl + `/v1/sxw/dialog/list`;
        return fetch("post", url, params);
    },
    getShareList(params) {
        //获取活动列表接口
        const url = basePath.systemUrl + `/v1/sxw/share/list`;
        return fetch("post", url, params);
    },
    removeBanner(params) {
        //删除banner
        const url = basePath.systemUrl + `/v1/sxw/banner/del`;
        return fetch("post", url, params);
    },
    removeDialog(params) {
        //删除首页弹窗
        const url = basePath.systemUrl + `/v1/sxw/dialog/del`;
        return fetch("post", url, params);
    },
    removeShare(params) {
        //删除活动分享
        const url = basePath.systemUrl + `/v1/sxw/share/del`;
        return fetch("post", url, params);
    },
    addBanner(params) {
        //添加banner
        const url = basePath.systemUrl + `/v1/sxw/banner/add`;
        return fetch("post", url, params);
    },
    addDialog(params) {
        //添加首页弹窗
        const url = basePath.systemUrl + `/v1/sxw/dialog/add`;
        return fetch("post", url, params);
    },
    addShare(params) {
        //添加活动分享
        const url = basePath.systemUrl + `/v1/sxw/share/add`;
        return fetch("post", url, params);
    },
    updateBanner(params) {
        //更新banner
        const url = basePath.systemUrl + `/v1/sxw/banner/update`;
        return fetch("post", url, params);
    },
    updateDialog(params) {
        //更新首页弹窗
        const url = basePath.systemUrl + `/v1/sxw/dialog/update`;
        return fetch("post", url, params);
    },
    updateShare(params) {
        //更新活动分享
        const url = basePath.systemUrl + `/v1/sxw/share/update`;
        return fetch("post", url, params);
    },
    changeBanner(params) {
        //启动banner
        const url = basePath.systemUrl + `/v1/sxw/banner/changeIsEnable`;
        return fetch("post", url, params);
    },
    changeDialog(params) {
        //启动首页弹窗
        const url = basePath.systemUrl + `/v1/sxw/dialog/changeIsEnable`;
        return fetch("post", url, params);
    },
    // 评论管理
    demandList(params) {
        //评论列表
        const url = BUrl + `/reviews/demand`;
        return fetch("get", url, params);
    },
    demandTop(params) {
        //评论置顶
        const url = BUrl + `/reviews/top/${params.id}/${params.type}`;
        return fetch("get", url, "");
    },
    demandEdit(params) {
        //评论编辑
        const url = BUrl + `/reviews`;
        return fetch("put", url, params);
    },
    demandDetail(params) {
        //评论详情
        const url = BUrl + `/reviews/${params.id}`;
        return fetch("get", url, "");
    },
    demandDelete(params) {
        //评论删除
        const url = BUrl + `/reviews`;
        return fetch("delete", url, params);
    },
    /**----------推荐方案管理---------------**/
    // 单空间
    getStoreplan(params) {
        const url = BUrl + `/v1/storeplan/onekey/list`;
        return fetch("get", url, params);
    },
    // 全屋
    getStoreplanByfullHouse(params) {
        const url = BUrl + `/v1/storeplan/manager/fullHouse/plan`;
        return fetch("post", url, params);
    },
    getHouse(params) {
        //获取空间类型
        const url = BUrl + `/v1/dictionary/group/type/house`;
        return fetch("get", url, params);
    },
    getStyle(params) {
        //获取风格
        const url = BUrl + `/v1/product/base/style/plan/list`;
        return fetch("get", url, params);
    },
    StorePlanEdit(params) {
        //编辑
        const url = BUrl + `/v1/storeplan/manager/edit`;
        return fetch("post", url, params);
    },
    StorePlanEditByFullHouse(params) {
        //编辑
        const url = BUrl + `/v1/storeplan/manager/fullHouse/modify`;
        return fetch("post", url, params);
    },
    shopMessage(params) {
        //店铺信息
        const url =
            BUrl +
            `/v1/storeplan/manager/shop/list/${params.planId}/${params.planType}`;
        return fetch("get", url);
    },
    /**----------装修报价管理---------------**/
    //装修报价列表
    getQuotedPrice(params) {
        const url = basePath.systemUrl + `/v1/planQuotedPrice/manage/list`;
        return fetch("get", url, params);
    },
    //装修报价详情
    getQuotedPriceDetail(params) {
        const url = basePath.systemUrl + `/v1/planQuotedPrice/detail`;
        return fetch("get", url, params);
    },
    //装修报价 编辑
    updateQuotedPrice(params) {
        const url = basePath.systemUrl + `/v1/planQuotedPrice/plan/edit`;
        return fetch("post", url, params);
    },
    //全屋报价 列表
    getCompanyPrice(params) {
        const url = basePath.systemUrl + `/v1/planQuotedPrice/company/price/list`;
        return fetch("get", url, params);
    },
    //全屋报价 编辑
    updateCompanyPrice(params) {
        const url = basePath.systemUrl + `/v1/planQuotedPrice/company/price/edit`;
        return fetch("post", url, params);
    },
    //产品清单
    getPlanProducts(params) {
        const url =
            basePath.systemUrl + `/v1/planQuotedPrice/company/plan/products`;
        return fetch("get", url, params);
    },
    //省市下拉框（有报价的才显示）
    getplanQuotedPriceArea(params) {
        const url = basePath.systemUrl + `/v1/planQuotedPrice/area/list`;
        return fetch("get", url, params);
    },
    //方案详情 预览图片接口
    reViewPlanImges(params) {
        let url = basePath.baseUrl + `/v1/designplan/getRederPic`;
        return fetch("get", url, params);
    },
    /**----------内容管理---------------**/
    getUserTagsList(params) {
        //获取用户标签列表接口
        const url = basePath.systemUrl + `/v1/user/label/getList`;
        return fetch("get", url, params);
    },
    getUserTagsDetail(params) {
        //获取用户标签详情接口
        const url = basePath.systemUrl + `/v1/user/label/getDetail`;
        return fetch("get", url, params);
    },
    getDictionaryList(params) {
        // 获取数据字典中的列表
        let url = basePath.systemUrl + "/v1/sys/dictionary/type/list";
        return fetch("get", url, params);
    },
    updateDictionary(params) {
        // 更新数据字典
        let url = basePath.systemUrl + "/v1/sys/dictionary/update";
        return fetch("post", url, params);
    },
    addUserTags(params) {
        //新增用户标签接口
        const url = basePath.systemUrl + `/v1/user/label/add`;
        return fetch("post", url, params);
    },
    updateUserTags(params) {
        //编辑用户标签接口
        const url = basePath.systemUrl + `/v1/user/label/update`;
        return fetch("put", url, params);
    },
    deleteUserTags(params) {
        //编辑用户标签接口
        const url = basePath.systemUrl + `/v1/user/label/delete`;
        return fetch("get", url, params);
    },
    getUserTagList(params) {
        //查询用户标签列表
        const url = basePath.systemUrl + `/v1/user/behavior/aggregated/getList`;
        return fetch("get", url, params);
    },
    getUserLabelCount(params) {
        //统计某时间段的用户标签数据
        const url =
            basePath.systemUrl + `/v1/user/behavior/aggregated/getUserLabelCount`;
        return fetch("post", url, params);
    },
    getUserLifeCount(params) {
        //统计某时间段的用户生命周期数据
        const url =
            basePath.systemUrl + `/v1/user/behavior/aggregated/getUserLifeCount`;
        return fetch("post", url, params);
    },
    getLifeAndTag(params) {
        //统计某时间段的用户生命周期和标签数据
        const url =
            basePath.systemUrl + `/v1/user/behavior/aggregated/getLifeAndTag`;
        return fetch("get", url, params);
    },
    exportUserLabelExcel(params) {
        //导出Excel 用户标签
        const url = basePath.systemUrl + `/v1/user/behavior/aggregated/exportExcel`;
        return exportData("get", url, params);
    },
    exportUserLabelCountExcel(params) {
        //导出Excel 用户标签统计
        const url =
            basePath.systemUrl + `/v1/user/behavior/aggregated/getUserLabelExcel`;
        return exportData("get", url, params);
    },
    exportUserLifeCountExcel(params) {
        //导出Excel 生命周期统计
        const url =
            basePath.systemUrl + `/v1/user/behavior/aggregated/getUserLifeExcel`;
        return exportData("get", url, params);
    },
    exportLifeAndTagCountExcel(params) {
        //导出Excel 用户标签和生命周期统计
        const url =
            basePath.systemUrl + `/v1/user/behavior/aggregated/getLifeAndTagExcel`;
        return exportData("get", url, params);
    },
    /**----------互动区改版的---------------**/
    //上传图片
    upLoadImg(sureOrder) {
        const url = `/v1/storage/upload`;
        return fetch("post", url, sureOrder);
    },
    getInteractiveZoneTopicList(params) {
        if (params.page) params.start = params.page;
        //获取所有列表
        const url = basePath.coreUrl + `/v1/core/interactiveZoneTopic/list`;
        return fetch("get", url, params);
    },
    getInteractiveZoneTopicDetail(params) {
        //获取所有详情
        const url =
            basePath.coreUrl + `/v1/core/interactiveZoneTopic/sxw/topic/${params}`;
        return fetch("get", url);
    },
    getInteractiveZoneReplyDetail(params) {
        //获取所有评论列表
        const url = basePath.coreUrl + `/v1/core/interactiveZoneReply/list`;
        return fetch("get", url, params);
    },
    addInteractiveZoneReplyTopic(params) {
        //所有评论的编辑
        const url = basePath.coreUrl + `/v1/core/interactiveZoneReply/add`;
        return fetch("post", url, params);
    },
    addInteractiveZoneTopic(params) {
        //所有新增
        const url = basePath.coreUrl + `/v1/core/interactiveZoneTopic/add`;
        return fetch("post", url, params);
    },
    updateInteractiveZoneReplyTopic(params) {
        //所有评论的编辑
        const url = basePath.coreUrl + `/v1/core/interactiveZoneReply/update`;
        return fetch("post", url, params);
    },
    updateInteractiveZoneTopic(params) {
        //所有编辑
        const url = basePath.coreUrl + `/v1/core/interactiveZoneTopic/update`;
        return fetch("post", url, params);
    },
    getArticleANDProjectCaseList(params) {
        //获取店铺下的博文和案列
        const url =
            basePath.systemUrl +
            `/v1/company/shop/article/getArticleANDProjectCaseList`;
        return fetch("post", url, params);
    },
    transportArticles(params) {
        //搬运文章  这边是否用core需要问土强
        const url = basePath.coreUrl + `/v1/core/interactiveZoneTopic/transport`;
        return fetch("post", url, params);
    },
    getAllCompany(params) {
        //获取用户所属公司的列表
        const url = basePath.coreUrl + `/v1/core/company/getAllCompany`;
        return fetch("post", url, params);
    },

    // 店铺智能方案列表 /v1/storeplan/onekey/list
    shopIntelligentList(params) {
        let url = basePath.baseUrl + "/v1/storeplan/onekey/list";
        return fetch("get", url, params);
    },
    // 店铺普通方案列表
    shopCommonPlanList(params) {
        let url = basePath.baseUrl + "/v1/storeplan/common/list";
        return fetch("get", url, params);
    },
    // 产品品牌
    ProductBrandDrop(params) {
        let url = basePath.baseUrl + "/v1/brand/withCompany"; // let url = '/v1/brand/list'
        return fetch("get", url, params);
    },
    // 房屋类型
    FitTypeHose(params) {
        const url = basePath.baseUrl + "/v1/dictionary/group/type/house";
        return fetch("get", url, params);
    },
    listDesignPlanStyle() {
        const url = basePath.baseUrl + `/v1/product/base/style/plan/list`;
        return fetch("get", url, null);
    },
    // 店铺管理 方案管理  全屋方案列表 接口
    getShopFullhouseList(params) {
        let url = basePath.baseUrl + `/v1/storeplan/fullHouse/list`;
        return fetch("get", url, params);
    },
    setRelease(params) {
        //店铺管理发布
        let url = basePath.systemUrl + `/v1/company/shop/setRelease`;
        return fetch("post", url, params);
    },
    // 操作机器人数据
    opertionRobotData() {
        let [url = "getList", params = {}, method = "get"] = Array.from(arguments);
        url = basePath.coreUrl + `/v1/core/sysrobot/${url}`;
        return fetch(method, url, params);
    },
    /*****************************线下活动API*******************************/
    // 获取活动李贝奥
    getActivityList(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/getList`;
        return fetch("get", url, params);
    },
     // 删除活动
    deleteActivity(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/delete`;
        return fetch("delete", url, params);
    },
    // 获取活动详情
    getDetailInfo(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/getDetailInfo`;
        return fetch("get", url, params);
    },
    // 编辑活动
    updateInfo(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/update`;
        return fetch("post", url, params);
    },
    // 新增活动
    addActivity(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/add`;
        return fetch("post", url, params);
    },
    // 获取厂商列表
    getCompList(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/compList`;
        return fetch("get", url, params);
    },
    // 获取品牌列表
    getBrandList(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/brandList`;
        return fetch("get", url, params);
    },
     // 上传图片
    uploadPic(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/uploadPic`;
        return fetch("post", url, params);
    },
     // 获取门店列表
    shopList(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/shopList`;
        return fetch("post", url, params);
    },
    // 设置详情
    setInfo(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/setInfo`;
        return fetch("get", url, params);
    },
     // 设置详情
     set(params) {
        const url = basePath.systemUrl + `/v1/act4/underline/set`;
        return fetch("get", url, params);
    },
    // 省市区联动
    areaList(params) {
        const url = basePath.systemUrl + `/v1/base/area/list`;
        return fetch("post", url, params);
    },
    
    getShopListByIds(params) { // 查询门店信息
        const url = basePath.systemUrl + `/v1/act4/underline/getShopListByIds`;
        return fetch('post', url, params)
    },
    /********************************订单列表****************************************** */
    activityOrderList(params){
        const url = basePath.systemUrl + `/v1/activity/order/activityOrderList`;
        return fetch('get', url, params)
    },
    opActivityOrderDetail(params){
        const url = basePath.systemUrl + `/v1/activity/order/operationActivityOrderDetail`;
        return fetch('get', url, params)
    },
    activityOrderSetPay(params){
        const url = basePath.systemUrl + `/v1/activity/order/activityOrderSetPay`;
        return fetch('get', url, params)
    },
    cancelActivityOrder(params){
        const url = basePath.systemUrl + `/v1/activity/order/cancelActivityOrder`;
        return fetch('get', url, params)
    },
    generateActivityOrder(params){
        const url = basePath.systemUrl + `/v1/activity/order/generateActivityOrder`;
        return fetch('get', url, params)
    },
    // 支付回调接口
    orderPay(params){
        const url = basePath.systemUrl + `/v1/activity/order/`;
        return fetch('get', url, params)
    },
    orderPay(params){
        const url = basePath.systemUrl + `/v1/activity/order/`;
        return fetch('get', url, params)
    },
    notPay(params){
        const url = basePath.systemUrl + `/v1/activity/regist/notPay`;
        return fetch('get', url, params)
    },
  
    
    /**********************************参与活动管理******************************/
    getRegistList(params) {
        const url = basePath.systemUrl + `/v1/activity/regist/getList`;
        // const url = basePath.localUrl + `/v1/activity/regist/getList`;
        return fetch("post", url, params);
    },
    registExportExcel(params) {
        // const url = basePath.localUrl + `/v1/activity/regist/exportExcel`;
        const url = basePath.systemUrl + `/v1/activity/regist/exportExcel`;
        return exportData("post", url, params);
    },
    getRegistCompList(params) {
        const url = basePath.systemUrl + `/v1/activity/regist/compList`;
        return fetch("get", url, params);
    },
    getlocationList(params) {
        const url = basePath.systemUrl + `/v1/activity/regist/getActivityLocationList`;
        // const url = basePath.localUrl + `/v1/activity/regist/getActivityLocationList`;
        return fetch("get", url, params);
    },
    modifyTraceResult(params) {
        // const url = basePath.systemUrl + `/v1/activity/regist/getActivityLocationList`;
        const url = basePath.systemUrl + `/v1/activity/regist/modifyTraceResult`;
        return fetch("post", url, params);
    },
    getRegistBrandList(params) {
        const url = basePath.systemUrl + `/v1/activity/regist/brandList`;
        return fetch("get", url, params);
    },
    /**********************************参与活动统计******************************/
    // 活动数据统计
    getChartLineData(params) {
        const url = basePath.systemUrl + `/v1/activity/regist/getChartLineData`;
        return fetch("post", url, params);
    },
    // 活动数据导出
    exportChartLineData(params) {
        const url = basePath.systemUrl + `/v1/activity/regist/exportChartLineData`;
        return exportData("post", url, params);
    },
    // 用户预约数据统计
    obtainChartLineData(params) {
        const url = basePath.systemUrl + `/v1/appointment/obtainChartLineData`;
        return fetch("post", url, params);
    },
    // 用户预约数据统计导出
    exportChartLineDataByAppointment(params) {
        const url = basePath.systemUrl + `/v1/appointment/exportChartLineData`;
        return exportData("post", url, params);
    },

    // 问卷红包配置
    questionnairsave(params) {
        let url = basePath.systemUrl + `/v1/questionnair/save`;
        return fetch("post", url, params);
    },
    // 获取问卷红包配置详情
    getRedPackageDetailInfo(params) {
        let url = basePath.systemUrl + `/v1/questionnair/getRedPackageDetailInfo`;
        return fetch("get", url, params);
    },
    // 答卷信息
    questionnaireInfo(params) {
        let url = basePath.systemUrl + `/v1/questionnair/questionnaireInfo`;
        return fetch("get", url, params);
    },
    // 填空题内容分页
    contentlist(params) {
        let url = basePath.systemUrl + `/v1/questionnair/contentlist`;
        return fetch("get", url, params);
    },
    // 用户信息列表
    getansweruserlist(params) {
        let url = basePath.systemUrl + `/v1/questionnair/userlist`;
        return fetch("get", url, params);
    },
    // 用户信息导出
    getexportExcel(params) {
        let url = basePath.systemUrl + `/v1/questionnair/exportExcel`;
        return exportData("get", url, params);
    },
    // 查看用户所有答卷
    getNaireInfo(params) {
        let url = basePath.systemUrl + `/v1/questionnair/getNaireInfo`;
        return fetch("get", url, params);
    },
    // 红包统计
    countRedRecket(params) {
        let url = basePath.systemUrl + `/v1/questionnair/countRedRecket`;
        return fetch("get", url, params);
    },
    // 红包统计分页
    countRedRecketList(params) {
        let url = basePath.systemUrl + `/v1/questionnair/countRedRecketList`;
        return fetch("get", url, params);
    },
    //-----------------数据分析接口---------------------------------------------------------------------
    pageViewList(params) { //页面统计列表
        let url = basePath.systemUrl + `/v1/data/bigdata/pageViewResult/getList`;
        return fetch("post", url, params);
    },
    pageExportListExcel(params) { //页面统计页面导出
        let url = basePath.systemUrl + `/v1/data/bigdata/pageViewResult/exportListExcel`;
        return exportData("get", url, params);
    },
    pageDetailList(params) { //页面统计详细分析
        let url = basePath.systemUrl + `/v1/data/bigdata/pageViewResult/getDetailList`;
        return fetch("post", url, params);
    },
    pageExportDtailListExcel(params) { //页面统计详细分析导出
        let url = basePath.systemUrl + `/v1/data/bigdata/pageViewResult/exportDtailListExcel`;
        return exportData("get", url, params);
    },
    elementViewList(params) { //元素统计列表
        let url = basePath.systemUrl + `/v1/data/bigdata/buttonClickResult/getList`;
        return fetch("post", url, params);
    },
    elementExportListExcel(params) { //元素统计页面导出
        let url = basePath.systemUrl + `/v1/data/bigdata/buttonClickResult/exportListExcel`;
        return exportData("get", url, params);
    },
    elementDetailList(params) { //元素统计详细分析
        let url = basePath.systemUrl + `/v1/data/bigdata/buttonClickResult/getDetailList`;
        return fetch("post", url, params);
    },
    elementExportDtailListExcel(params) { //元素统计详细分析导出
        let url = basePath.systemUrl + `/v1/data/bigdata/buttonClickResult/exportDetailListExcel`;
        return exportData("get", url, params);
    },
    otherViewList(params) { //其他行为统计列表
        let url = basePath.systemUrl + `/v1/data/bigdata/otherEventAnalysisResult/getList`;
        return fetch("get", url, params);
    },
    otherExportListExcel(params) { //其他行为统计页面导出
        let url = basePath.systemUrl + `/v1/data/bigdata/otherEventAnalysisResult/exportExcelGetList`;
        return exportData("get", url, params);
    },
    otherDetailList(params) { //其他行为统计详细分析
        let url = basePath.systemUrl + `/v1/data/bigdata/otherEventAnalysisResult/getDetails`;
        return fetch("get", url, params);
    },
    otherExportDtailListExcel(params) { //其他行为统计详细分析导出
        let url = basePath.systemUrl + `/v1/data/bigdata/otherEventAnalysisResult/exportExcelGetDetails`;
        return exportData("get", url, params);
    },
    retainedViewList(params) { //留存统计列表
        let url = basePath.systemUrl + `/v1/data/bigdata/userRetentionResult/getList`;
        return fetch("post", url, params);
    },
    retainedExportListExcel(params) { //留存统计页面导出
        let url = basePath.systemUrl + `/v1/data/bigdata/userRetentionResult/exportListExcel`;
        return exportData("get", url, params);
    },
    activeViewList(params) { //活跃统计列表
        let url = basePath.systemUrl + `/v1/data/bigdata/userAnalysisResult/getList`;
        return fetch("post", url, params);
    },
    activeExportListExcel(params) { //活跃统计页面导出
        let url = basePath.systemUrl + `/v1/data/bigdata/userAnalysisResult/exportListExcel`;
        return exportData("get", url, params);
    },
    statisticViewList(params) { //统计分析列表
        let url = basePath.systemUrl + `/v1/data/bigdata/funnel/getList`;
        return fetch("get", url, params);
    },
    statisticViewDetail(params) { //统计分析详情列表
        let url = basePath.systemUrl + `/v1/data/bigdata/funnelDetail/getList`;
        return fetch("get", url, params);
    },
    statisticViewDelete(params) { //统计分析删除
        let url = basePath.systemUrl + `/v1/data/bigdata/funnel/delete`;
        return fetch("get", url, params);
    },
    statisticViewAnalysis(params) { //统计分析统计结果列表接口
        let url = basePath.systemUrl + `/v1/data/bigdata/funnelAnalysisResult/getList`;
        return fetch("get", url, params);
    },
    statisticExportListExcel(params) { //统计分析页面导出
        let url = basePath.systemUrl + `/v1/data/bigdata/funnelAnalysisResult/exportExcelGetList`;
        return exportData("get", url, params);
    },
    // ------------------------------------用户预约----------------------------------------------
    getAllShopList(params) {
        // let url =basePath.localUrl+`/v1/company/shop/findAllShop`;
        let url = basePath.systemUrl + `/v1/company/shop/findAllShop`;
        return fetch("get", url, params);
    },
    getAllShopTypeList(params) {
        let url = basePath.systemUrl + `/v1/company/shop/listByShopType`;
        return fetch("get", url, params);
    },
    obtinUserAppointmentList(params) {
        let url = basePath.systemUrl + `/v1/appointment/list`;
        return fetch("post", url, params);
    },
    modifyFollowUpResult(params) {
        let url = basePath.systemUrl + `/v1/appointment/modify`;
        return fetch("post", url, params);
    },
    exportListData(params) {
        let url = basePath.systemUrl + `/v1/appointment/export`;
        return exportData("post", url, params);
    },
    // 获取商品列表
    getGoodsList(sureOrder) {
        const url = basePath.baseUrl + `/v1/base/goods/list`;
        return fetch("get", url, sureOrder);
    },
    // 商品排序编辑
    goodSpuSortEdit(spuId, sort) {
        const url = basePath.baseUrl + `/v1/base/goods/modifyGoodsSort?id=${spuId}&sort=${sort}`;
        return fetch("get", url);
    },
    updateProductPrice(params){
        let url = basePath.systemUrl+`/v1/planQuotedPrice/productDecorationPriceEdit`;
        return fetch("get", url, params);
    },
    productCategory(params) { //商品分类
        // 获取分类下拉框
        let url = basePath.baseUrl + "/v1/dictionary/product/type";
        return fetch("get", url, params);
    },
    getCoverPicInfoList(params) { //设置封面图接口
        const url = basePath.baseUrl + `/v1/storeplan/getCoverPicInfoList`;
        return fetch("get", url,params);
    },
    modifyFullHousePlanCoverPics(params) { //设置封面图接口
        const url = basePath.baseUrl + `/v1/storeplan/modifyFullHousePlanCoverPics`;
        return fetch("get", url,params);
    },
    getGoodsEdit(params) {
        const url = `/v1/base/goods/edit`;
        return fetch("get",url,params)
    },
    userTypeList(params) {
        const url = `/v1/userStatistics/userTypeList`;
        return fetch("get",url,params)
    },
    belongCompanyList(param) {
        //所属企业列表接口
        const url = basePath.systemUrl + `/v1/comapny/inner/getCompanySpinner`;
        return fetch('get', url, param);
    },
};
