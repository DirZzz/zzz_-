import {
  exportData,
  exportDataPost,
  fetch,
  formPost,
  formPostExportdata
} from '../utils/fetch'
import axios from 'axios'
//备注：同一个域名下的接口写在一个模板里 如：VUE_APP_BaseUrl 是基础的域名 统一放在index.js里
// 如果需要请求不同的域名下的接口 就新建一个js文件  文件名 命名规范为 请求域名的名字

let basePath = process.env
export default {
  loginOut(params) {
    let url = basePath.ssoUrl + '/v1/user/logout'
    return fetch('post', url, params)
  },
  login(params) {
    return axios.post(basePath.ssoUrl + '/v1/user/login', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  },
  getLoginUserAdminPlatform(params) {
    const url = basePath.ucUrl + `/v1/user/manage/getLoginUserAdminPlatform`
    return fetch('get', url, params)
  },
  //  区域选择接口
  areaList(params) {
    const url = basePath.systemUrl + `/v1/base/area/list`
    return fetch('post', url, params)
  },
  industryType(params) {
    //所属行业 与账号类型
    const url = basePath.baseUrl + `/v1/dictionary/with/${params.type}`
    return fetch('get', url, '')
  },
  industryType2(params) {
    //所属行业 与账号类型---经销商
    const url =
      basePath.systemUrl +
      `/v1/base/admin/franchiser/industry/list?pid=` +
      params.pid
    return fetch('get', url, '')
  },
  mfrsCategory(params) {
    //厂商产品可见范围
    const url = basePath.systemUrl + `/v1/pro/category/company/listAll`
    return fetch('get', url, params)
  },
  dealerCategory(params) {
    //经销商产品可见范围
    const url = basePath.systemUrl + `/v1/pro/category/franchiser/listAll`
    return fetch('get', url, params)
  },
  industryRange(params) {
    //产品可见范围回显
    const url = basePath.systemUrl + `/v1/pro/category/industry/list`
    return fetch('get', url, params)
  },
  // 关联功能列表
  getFunctionList(params) {
    const url = basePath.systemUrl + `/inner/setMeal/getFunctionList`
    return fetch('get', url, params)
  },

  // 保存功能列表
  saveFunction(params) {
    const url = basePath.systemUrl + `/inner/setMeal/saveFunction`
    return formPost('post', url, params)
  },
  // 套餐列表
  setMealList(params) {
    const url = basePath.systemUrl + `/inner/setMeal/list`
    return fetch('get', url, params)
  },
  // 新增套餐
  addMeal(params) {
    const url = basePath.systemUrl + `/inner/setMeal/add`
    return fetch('put', url, params)
  },
  //新增套餐用户类型
  getDictionaryInfo(params) {
    const url = basePath.systemUrl + `/inner/setMeal/getDictionaryInfo`
    return fetch('get', url, params)
  },
  //修改套餐
  updateMeal(params) {
    const url = basePath.systemUrl + `/inner/setMeal/update`
    return fetch('put', url, params)
  },
  //获取套餐详情
  getServiceDetail(params) {
    const url = basePath.systemUrl + `/inner/setMeal/getServiceDetail`
    return fetch('get', url, params)
  },
  //删除套餐
  deleteMeal(params) {
    const url = basePath.systemUrl + `/inner/setMeal/delete`
    return fetch('delete', url, params)
  },
  //改变套餐状态
  changeSetMealStatus(id, servicesStatus) {
    const url =
      basePath.systemUrl +
      `/inner/setMeal/changeSetMealStatus?id=${id}&servicesStatus=${servicesStatus}`
    return fetch('put', url)
  },
  //获取企业价格设置列表
  getCompanyPriceList(params) {
    const url = basePath.systemUrl + `/inner/setMeal/getCompanyPriceList`
    return fetch('get', url, params)
  },
  //添加企业价格
  addCompanyPrice(params) {
    const url = basePath.systemUrl + `/inner/setMeal/addCompanyPrice`
    return fetch('put', url, params)
  },
  //修改企业价格
  updateCompanyPrice(params) {
    const url = basePath.systemUrl + `/inner/setMeal/updateCompanyPrice`
    return fetch('put', url, params)
  },
  //获取企业价格详情
  getCompanyPriceDetail(params) {
    const url = basePath.systemUrl + `/inner/setMeal/getCompanyPriceDetail`
    return fetch('get', url, params)
  },
  //获取企业列表
  getCompanyList(params) {
    const url = basePath.systemUrl + `/inner/setMeal/getCompanyList`
    return fetch('get', url, params)
  },

  //删除企业价格
  deleteCompanyPrice(params) {
    const url = basePath.systemUrl + `/inner/setMeal/deleteCompanyPrice`
    return fetch('delete', url, params)
  },
  // 成员账号列表
  getServicesUserList(params) {
    const url = basePath.systemUrl + `/v1/services/user/list`
    return fetch('get', url, params)
  },

  /**  企业代购  start*/
  listCompanyPurchaseLog(params) {
    const url =
      basePath.systemUrl +
      `/service/purchase/inner/buy/getPageList/${params.pageNum}/${
        params.pageSize
      }`
    return fetch('get', url)
  },
  exportCompanyPurchaseAccounts(id) {
    const url =
      basePath.systemUrl + `/service/purchase/inner/buy/account/export/${id}`
    return exportData(url, {}, 'get')
  },
  listCompanyOptions() {
    const url = basePath.systemUrl + `/service/purchase/inner/buy/companys`
    return fetch('get', url)
  },
  listServiceOptions(param) {
    const url =
      basePath.systemUrl + `/service/purchase/inner/buy/getServicesList`
    return fetch('get', url, param)
  },
  listUserTypeOptions(param) {
    const url = basePath.systemUrl + `/service/purchase/inner/buy/getUserScopes`
    return fetch('get', url, param)
  },
  listServiceUseageOptions(param) {
    const url = basePath.systemUrl + `/service/purchase/inner/buy/getDuration`
    return fetch('get', url, param)
  },
  purchaseAccount(param) {
    const url = basePath.systemUrl + `/service/purchase/inner/buy/add`
    return fetch('post', url, param)
  },

  /**  企业代购  end*/
  //获取经销商列表
  listDealerManage(param) {
    const url = basePath.systemUrl + `/v1/base/admin/franchiser/list`
    return fetch('get', url, param)
  },
  //删除经销商
  doDeleteDealer(param) {
    const url = basePath.systemUrl + `/v1/base/admin/franchiser/delete`
    return fetch('delete', url, param)
  },
  //新增经销商
  addDealer(param) {
    const url = basePath.systemUrl + `/v1/base/admin/franchiser/add`
    return fetch('post', url, param)
  },
  //编辑经销商
  updateDealer(param) {
    const url = basePath.systemUrl + `/v1/base/admin/franchiser/update`
    return fetch('post', url, param)
  },

  //获取经销商详情
  getDealer(param) {
    const url = basePath.systemUrl + `/v1/base/admin/franchiser/get`
    return fetch('get', url, param)
  },
  //新增经销商--获取品牌列表
  cabrand(param) {
    const url = basePath.systemUrl + `/v1/base/brand/company/list`
    return fetch('get', url, param)
  },

  //用户列表--角色配置列表
  getUserRole(param) {
    const url = basePath.ucUrl + `/v1/user/manage/role/get`
    return fetch('get', url, param)
  },
  //用户列表--修改角色配置
  updateUserRole(param) {
    const url = basePath.ucUrl + `/v1/user/manage/role/update`
    return fetch('post', url, param)
  },

  /**  三度套餐续费/升级*/
  //续费
  innerRenewService(param) {
    const url = basePath.systemUrl + `/v1/services/inner/renew`
    return fetch('post', url, param)
  },

  //获取可选套餐列表
  selectListAllService(param) {
    const url = basePath.systemUrl + `/v1/services/choose/list`
    return fetch('get', url, param)
  },
  //套餐修改
  // 确认购买套餐
  confirmBuy(param) {
    const url = basePath.systemUrl + `/v1/services/pay/confirm`
    return fetch('post', url, param)
  },
  // 确认购买套餐
  confirmBuyTest(param) {
    const url = basePath.systemUrl + `/v1/services/inner/edit`
    return fetch('post', url, param)
  },

  /**  三度套餐续费/升级*/
  /*用户管理*/
  getUserList(param) {
    //用户列表接口
    const url = basePath.ucUrl + `/v1/user/manage/userList`
    return formPost('post', url, param)
  },
  getFranchiserCompany(param) {
    //用户列表经销商企业接口
    const url = basePath.ucUrl + `/v1/user/manage/getFranchiserCompany`
    return formPost('get', url, param)
  },
  belongCompanyList(param) {
    //所属企业列表接口
    const url = basePath.ucUrl + `/v1/user/manage/companyList`
    return fetch('get', url, '')
  },
  comapnyInner(param) {
    //公司列表接口
    const url = basePath.systemUrl + `/v1/comapny/inner`
    return fetch('get', url, param)
  },
  addComapnyInner(param) {
    //新增公司列表接口
    const url = basePath.systemUrl + `/v1/comapny/inner`
    return formPost('post', url, param)
  },
  deleteComapnyInner(param) {
    //删除公司列表接口
    const url = basePath.systemUrl + `/v1/comapny/inner/${param.id}`
    return fetch('delete', url, param)
  },
  editComapnyInner(param) {
    //编辑公司列表接口
    const url = basePath.systemUrl + `/v1/comapny/inner/${param.id}`
    return fetch('get', url, param)
  },
  saveComapnyInner(param) {
    //编辑保存公司列表接口
    const url = basePath.systemUrl + `/v1/comapny/inner/update`
    return formPost('post', url, param)
  },
  brandBusinessType(param) {
    //公司类型接口
    const url = `/v1/dictionary/with/brandBusinessType`
    return fetch('get', url, param)
  },
  userDel(param) {
    //用户列表删除接口
    const url = basePath.ucUrl + `/v1/user/manage/userDel`
    return formPost('post', url, param)
  },
  editUserInfo(param) {
    //用户列表编辑接口
    const url = basePath.ucUrl + `/v1/user/manage/userInfo`
    return formPost('post', url, param)
  },
  platformList(param) {
    //用户列表p平台列表接口
    const url = basePath.ucUrl + `/v1/user/manage/platform/list`
    return fetch('get', url, param)
  },
  platformSet(param) {
    //用户列表p平台设置接口
    const url = basePath.ucUrl + `/v1/user/manage/platform/get`
    return fetch('get', url, param)
  },
  rolePlatformList(param) {
    //用户列表p平台角色组所有list接口
    const url = basePath.ucUrl + `/v1/user/manage/role/group/list`
    return fetch('get', url, param)
  },
  rolePlatformSet(param) {
    //获取用户列表p平台角色组列表接口
    const url = basePath.ucUrl + `/v1/user/manage/role/group/get`
    return fetch('get', url, param)
  },
  rolePlatformUpdate(param) {
    //用户列表平台角色组更新接口
    const url = basePath.ucUrl + `/v1/user/manage/role/group/update`
    return fetch('post', url, param)
  },
  platformUpdate(param) {
    //用户列表平台设置更新接口
    const url = basePath.ucUrl + `/v1/user/manage/platform/update`
    return fetch('post', url, param)
  },
  getUserResource(param) {
    //用户列表用户来源接口
    const url = basePath.ucUrl + `/v1/user/manage/getUserResource`
    return fetch('get', url, param)
  },
  saveEditUser(param) {
    //用户列表编辑保存接口
    const url = basePath.ucUrl + `/v1/user/manage/editUser`
    return fetch('post', url, param)
  },
  getRandomPassWord(param) {
    const url = basePath.ucUrl + `/v1/user/manage/userRandomPassWord`
    return fetch('get', url, param)
  },
  addUser(param) {
    //用户列表新增用户接口
    const url = basePath.ucUrl + `/v1/user/manage/addUser`
    return fetch('post', url, param)
  },
  batchAddUser(param) {
    //用户列表批量新增用户接口
    const url = basePath.ucUrl + `/v1/user/manage/batchAddUser`
    return exportDataPost(url, param, 'post')
  },
  getCreatUserType(param) {
    //用户列表新增用户  用户类型接口
    const url = basePath.ucUrl + `/v1/user/manage/getCreatUserType`
    return formPost('post', url, param)
  },

  /**************************数据统计分析API Start*********************/
  //获取用户列表数据
  getUserAnalysisList2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/user/getList`
    return fetch('get', url, params)
  },
  getUserAnalysisAll2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/user/getAll`
    return fetch('get', url, params)
  },
  //获取用户汇总数据
  getUserAnalysisSum2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/user/getSum`
    return fetch('get', url, params)
  },
  //获取地区用户数据
  getAreaUserAnalysisList2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/user/getAreaList`
    return fetch('get', url, params)
  },

  //获取用户数量Top10的地区
  getAreaUserTop102(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/user/getAreaTop10`
    return fetch('get', url, params)
  },

  //热门方案
  getHotDesignList2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/single/design/getList`
    return fetch('get', url, params)
  },
  //获取热门方案Top10
  getHotDesignTop102(params) {
    const url =
      basePath.systemUrl + `/v1/data/analysis/single/design/getDesignTop10`
    return fetch('get', url, params)
  },

  //获取地区列表
  getAreaList2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/user/getCity`
    return fetch('get', url, params)
  },

  //获取方案使用数据
  getPlanAnalysisList2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/design/user/getList`
    return fetch('get', url, params)
  },

  getPlanAnalysisAll2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/design/user/getAll`
    return fetch('get', url, params)
  },

  //获取方案使用数据汇总
  getPlanAnalysisSum2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/design/user/getSum`
    return fetch('get', url, params)
  },

  //获取商品浏览数据
  getProductAnalysisList2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/product/view/getList`
    return fetch('get', url, params)
  },
  //获取商品浏览前十
  getProductTop10Analysis2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/product/view/getTop10`
    return fetch('get', url, params)
  },

  //获取产品替换数据
  getProductReplaceList2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/product/replace/getList`
    return fetch('get', url, params)
  },

  getProductReplaceAll2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/product/replace/getAll`
    return fetch('get', url, params)
  },
  //获取商品汇总
  getProductReplaceSum2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/product/replace/getSum`
    return fetch('get', url, params)
  },

  //获取用户来渠道（源数）据
  getSourceAnalysisList2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/channel/getList`
    return fetch('get', url, params)
  },

  getSourceAnalysisSum2(params) {
    const url = basePath.systemUrl + `/v1/data/analysis/channel/getSum`
    return fetch('get', url, params)
  },

  //获取小程序列表
  getMiniList(params) {
    const url = basePath.systemUrl + `/v1/mini/program/config/get`
    return fetch('get', url, params)
  },

  /**************************数据统计分析API end*********************/
  //推广信息接口
  proprietorInfo(params) {
    const url = basePath.shopApiUrl + `/v1/sandu/mini/proprietorInfo/list`
    return fetch('post', url, params)
  },
  exportReportList(params) {
    //导出excel
    const url =
      basePath.shopApiUrl + `/v1/sandu/mini/proprietorInfo/exportReportList`
    return formPost('post', url, params, 'blob')
  },
  deletepPoprietorInfo(params) {
    //删除
    const url = basePath.shopApiUrl + `/v1/sandu/mini/proprietorInfo/delete`
    return formPost('post', url, params)
  },
  updateProcess(params) {
    //批量修改为已处理
    const url =
      basePath.shopApiUrl + `/v1/sandu/mini/proprietorInfo/updateProcess`
    return formPost('post', url, params)
  },
  /**************************小程序管理后台API *********************/
  // 查看小程序列表
  getPagramList(params) {
    const url = basePath.systemUrl + `/v1/mini/back/getList`
    return fetch('get', url, params)
  },
  // 删除小程序列表对应行
  deletedMini(params) {
    const url = basePath.systemUrl + `/v1/mini/back/delete`
    return formPost('delete', url, params)
  },
  exportListExcel(params) {
    const url = basePath.systemUrl + `/v1/mini/back/exportExcel`
    return exportData(url, params, 'get')
  },
  getProgramInfo(params) {
    const url = basePath.systemUrl + `/v1/mini/back/getDetailInfo`
    return formPost('get', url, params)
  },
  programUpdate(params) {
    const url = basePath.systemUrl + `/v1/mini/back/update`
    return fetch('post', url, params)
  },
  addProgram(params) {
    const url = basePath.systemUrl + `/v1/mini/back/add`
    return fetch('post', url, params)
  },
  getMiniBackList(params) {
    const url = basePath.systemUrl + `/v1/mini/back/getMiniList`
    return formPost('get', url, params)
  },
  getTempList(params) {
    const url = basePath.systemUrl + `/v1/mini/back/getTempList`
    return formPost('get', url, params)
  },
  getCompList(params) {
    const url = basePath.systemUrl + `/v1/mini/back/getCompList`
    return formPost('get', url, params)
  },
  miniFileUpload(params) {
    const url = basePath.systemUrl + `/v1/mini/back/fileUpload`
    return formPost('post', url, params)
  },
  matchPackage(params) {
    // 匹配套餐
    const url = basePath.systemUrl + `/v1/services/matchPackage`
    return formPost('post', url, params)
  },
  batchModifyPassword(params) {
    // 批量修改密码
    const url = basePath.ucUrl + `/v1/user/manage/batchModifyPassword`
    return formPostExportdata('post', url, params)
  },
  batchModify(params) {
    // 批量修改用户套餐
    const url = basePath.ucUrl + `/v1/user/manage/batchModify`
    return formPost('post', url, params)
  },
  batchFreezeManage(params) {
    // 批量开启与批量暂停
    const url = basePath.ucUrl + `/v1/user/manage/freeze/manage`
    return fetch('post', url, params)
  },
  /************************** B端数据统计 *********************/
  // 用户类型 筛选栏数据
  getUserTypeList(params) {
    const url = basePath.baseUrl + `/v1/userStatistics/userTypeList`
    return fetch('get', url, params)
  },
  // 活跃用户和新增用户汇总接口
  getUserCountList(params) {
    const url = basePath.baseUrl + `/v1/userStatistics/userCountList`
    return formPost('post', url, params)
  },
  // 活跃用户和新增用户明细接口
  getUserListByUserStatistics(params) {
    const url = basePath.baseUrl + `/v1/userStatistics/userList`
    return formPost('post', url, params)
  },
  /*******************************主子账号设置********************************************/
  setMSType(params) {
    //
    const url = basePath.ucUrl + `/v1/user/manage/setMSType`
    return formPost('post', url, params)
  },
  companySonUserList(params) {
    //关联子账号左边
    const url = basePath.ucUrl + `/v1/user/manage/companySonUserList`
    return formPost('post', url, params)
  },
  masterSonUserList(params) {
    //关联子账号右边
    const url = basePath.ucUrl + `/v1/user/manage/masterSonUserList`
    return formPost('post', url, params)
  },
  getUserType(params) {
    //用户列表新增用户  用户类型接口
    const url = basePath.systemUrl + `/v1/sys/dictionary/type/list`
    return fetch('get', url, params)
  },
  manageQueryMaster(params) {
    //查看主账号接口
    const url = basePath.ucUrl + `/v1/user/manage/queryMaster`
    return formPost('post', url, params)
  },

  request(method, url, params, base = 'baseUrl', type = 'json') {
    switch (type) {
      case 'json':
        return fetch(method, basePath[base] + url, params)
      case 'formData':
        return formPost(method, basePath[base] + url, params)
      case 'excel':
        return exportData(basePath[base] + url, params, method)
    }
  }
}
