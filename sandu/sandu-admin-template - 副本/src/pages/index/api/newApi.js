import { fetch, formPost, exportData, exportExcel } from "../utils/request";
const basePath = process.env;
export default {
  /**--------------------装修报价管理-------------------------**/
  showQuoted(params) {
    const url = basePath.systemUrl + `/v1/planQuotedPrice/company/shop/check`;
    return fetch(url, params, "get");
  },
  //装修报价列表
  getQuotedPrice(params) {
    const url = basePath.systemUrl + `/v1/planQuotedPrice/manage/list`;
    return fetch(url, params, "get");
  },
  //装修报价详情
  getQuotedPriceDetail(params) {
    const url = basePath.systemUrl + `/v1/planQuotedPrice/detail`;
    return fetch(url, params, "get");
  },
  //装修报价 编辑
  updateQuotedPrice(params) {
    const url = basePath.systemUrl + `/v1/planQuotedPrice/plan/edit`;
    return fetch(url, params, "get");
  },
  //全屋报价 列表
  getCompanyPrice(params) {
    const url = basePath.systemUrl + `/v1/planQuotedPrice/company/price/list`;
    return fetch(url, params, "get");
  },
  //全屋报价 编辑
  updateCompanyPrice(params) {
    const url = basePath.systemUrl + `/v1/planQuotedPrice/company/price/edit`;
    return fetch(url, params, "post");
  },
  //产品清单
  getPlanProducts(params) {
    const url =
      basePath.systemUrl + `/v1/planQuotedPrice/company/plan/products`;
    return fetch(url, params, "get");
  },
  //方案详情 预览图片接口
  reViewPlanImges(params) {
    let url = basePath.baseUrl + `/v1/designplan/getRederPic`;
    return fetch(url, params, "get");
  },
  getHouse(params) {
    //获取空间类型
    const url = basePath.baseUrl + `/v1/dictionary/group/type/house`;
    return fetch(url, params, "get");
  },
  getStyle(params) {
    //获取风格
    const url = basePath.baseUrl + `/v1/product/base/style/plan/list`;
    return fetch(url, params, "get");
  },
  //方案的报价接口
  quoted(params) {
    const url = basePath.systemUrl + `/v1/planQuotedPrice/company/quoted`;
    return fetch(url, params, "post");
  },
  obtainCompanyShop(params) {
    //增加获取公司下所有的店铺(用户数据接口中使用)
    const url = basePath.systemUrl + "/v1/company/shop/obtainCompanyShop";
    return fetch(url, params, "get");
  },
  /**--------------------方案库-------------------------**/
  // 设计师列表
  getDesignerList(params) {
    const url = basePath.baseUrl + "/v1/designplan/getDesignerList";
    return fetch(url, params, "get");
  },
  // 全屋设计师列表
  getDesignerListByFullHouse(params) {
    const url = basePath.baseUrl + "/v1/fullHouse/getFullHouseDesignerList";
    return fetch(url, params, "get");
  },
  // 导出智能单空间方案
  exportListExcelByOnekey(params) {
    const url = basePath.baseUrl + "/v1/designplan/onekey/exportListExcel";
    return exportData(url, params, "get");
  },
  // 导出智能全屋方案
  exportListExcelByFullHouse(params) {
    const url = basePath.baseUrl + "/v1/fullHouse/list/exporListFullHouse";
    return exportData(url, params, "get");
  },
  // 导出普通方案
  getDesignerListByCommon(params) {
    const url = basePath.baseUrl + "/v1/designplan/common/exportListExcel";
    return exportData(url, params, "get");
  },
  //登录首页
  overviewMiniProgram(params) {
      const url = basePath.systemUrl + "/panel/overview/miniProgram";
      return fetch(url, params, "get");
  },
  overviewNew(params) {
      const url = basePath.systemUrl + "/panel/overview/new";
      return fetch(url, params, "get");
  },
  overviewShop(params) {
      const url = basePath.systemUrl + "/panel/overview/shop";
      return fetch(url, params, "get");
  },
    /**--------------------砍价活动-------------------------**/
    //导出砍价活动中的用户数据
    exportExcelWxActBargainReg(params) {
        const url = basePath.systemUrl + "/v1/act/bargain/reg/exportExcelWxActBargainReg";
        return exportData(url, params, "get");
    },
    //返回砍价活动中统计数据内的数据明细数据
    getWxActBargainRegResultList(params) {
        const url = basePath.systemUrl + "/v1/act/bargain/reg/getWxActBargainRegResultList";
        return fetch(url, params, "get");
    },
    //导出砍价活动中统计数据内的数据明细数据
    exportExcelgetWxActBargainRegResultList(params) {
        const url = basePath.systemUrl + "/v1/act/bargain/reg/exportExcelgetWxActBargainRegResultList";
        return exportData(url, params, "get");
    },
    /**--------------------材质贴图库-------------------------**/
    // 批量公开/取消公开材质
    updateSecrecyFlag(params) {
        const url = basePath.baseUrl + "/v1/texture/updateSecrecyFlag";
        return fetch(url, params, "post");
    },
    //获取材质细分类
    getClassification(params) {
        const url = basePath.baseUrl + "/v1/dictionary/texture/classification";
        return fetch(url, params, "get");
    },
    //获取材质色系
    getColorSystem(params) {
        const url = basePath.baseUrl + "/v1/dictionary/texture/colorSystem";
        return fetch(url, params, "get");
    },
//-----------------------------------------------主子账号模块---------------------------------------------
    masterSonUserList(params) {
        const url = basePath.ucUrl + "/v1/user/manage/masterSonUserList";
        return formPost("post",url, params);
    },
    getUserType(params) { //用户列表新增用户  用户类型接口
        const url = basePath.systemUrl + `/v1/sys/dictionary/type/list`;
        return fetch(url, params,'get');
    },
    manageUserInfo(params) { //主子账号获取详情
        const url = basePath.ucUrl + `/v1/user/manage/userInfo`;
        return formPost("post",url, params);
    },
    manageEditUser(params) { //主子账号保存
        const url = basePath.ucUrl + `/v1/user/manage/editUser`;
        return formPost("post",url, params);
    },
    //合并产品
    mergeHardProduct(params) { //合并产品
        const url = basePath.baseUrl + `/v1/product/merge/hard`;
        return fetch(url, params,'post');
    },
    /*********************企业设置-活动接口************************* */

  merchantActivityOrderDetail(params) { //导航图标默认接口
    const url = basePath.systemUrl + `/v1/activity/order/merchantActivityOrderDetail`;
    return fetch(url, params, "get");
  },
  
  getMerchantActivityList(params) { //导航图标默认接口
    const url = basePath.systemUrl + `/v1/act4/underline/getMerchantActivityList`;
    return fetch(url, params, "get");
  },
  
  activityOrderList(params) {
    const url = basePath.systemUrl + `/v1/activity/order/activityOrderList`;
    return fetch(url, params, 'get');
  },
  notPay(params) {
    const url = basePath.systemUrl + `/v1/activity/regist/notPay`;
    return fetch(url, params, 'get');
  },
   // 删除活动
   deleteActivity(params) {
    const url = basePath.systemUrl + `/v1/act4/underline/delete`;
    return fetch( url, params,"delete");
  },
  getList(params) {
    const url = basePath.systemUrl + `/v1/activity/regist/getPurchaseUserList`;
    return fetch( url, params,"get");
  },
  getShopIdName(params) {
    const url = basePath.systemUrl + `/v1/activity/regist/getPurchaseShopList`;
    return fetch( url, params,"get");
  },
  getRemark(params) {
    const url = basePath.systemUrl + `/v1/activity/regist/getRemark`;
    return fetch( url, params,"get");
  },
  updateRemark(params) {
    const url = basePath.systemUrl + `/v1/activity/regist/updateRemark`;
    return fetch( url, params,"get");
  },
  changeIsUsed(params) {
    const url = basePath.systemUrl + `/v1/activity/regist/changeIsUsed`;
    return fetch( url, params,"get");
  },
  generateActivityOrder(params) {
    const url = basePath.systemUrl + `/v1/activity/order/generateActivityOrder`;
    return fetch( url, params,"post");
  },
  order(params) {
    const url = basePath.systemUrl + `/v1/activity/order/`;
    return fetch( url, params,"get");
  },
  cancelActivityOrder(params) {
    const url = basePath.systemUrl + `/v1/activity/order/cancelActivityOrder`;
    return fetch( url, params,"get");
  },
  exportExcelList(params) {
    const url = basePath.systemUrl + `/v1/activity/regist/exporttPurchaseUserExcel`;
    return exportData( url, params,"get");
  },
  getAreaList(params) {
    const url = basePath.systemUrl + `/v1/base/area/list`;
    return fetch( url, params,"post");
  },
  // 获取付款结果（轮询备用）
  getPayStatus(params) {
    const url = basePath.systemUrl + `/v1/activity/order/pay/status`;
    return fetch( url, params,"get");
  },
  // 获取用户是否为经销商
  judgeUserType(params) {
    const url = basePath.systemUrl + `/v1/act4/underline/judgeUserType`;
    return fetch( url, params,"get");
  },
  getApplyArea(params) {//方案库适用面积接口
      const url = basePath.baseUrl + `/v1/dictionary/with/${params.type}`;
      return fetch( url, '',"get");
  },
  getWithFullPlan(params) {//方案库全屋接口
      const url = basePath.baseUrl + `/v1/designplan/withFullPlan/singles`;
      return fetch( url, params,"get");
  },
 

  
};
