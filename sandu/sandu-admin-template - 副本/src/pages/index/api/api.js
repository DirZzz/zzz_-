import {fetch,formPost,exportData} from '../utils/request'
import axios from "axios";
const basePath = process.env;

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
        return fetch(url, params, "get");
    },
    loginOut(params) {
        return fetch(basePath.ssoUrl + "/v1/user/logout", params, "post");
    },
    /*---------------水刀模板 start--------------------*/
    waterKnifeList(params) {
        let url = "/v1/basewaterjet/list";
        return fetch(url, params, "get");
    },
    addWaterKnifeList(params) {
        let url = "/v1/basewaterjet";
        return fetch(url, params, "post");
    },
    editWaterKnifeList(params) {
        let url = "/v1/basewaterjet";
        return fetch(url, params, "put");
    },
    infoWaterKnife(params) {
        let url = "/v1/basewaterjet";
        return fetch(url, params, "get");
    },
    getBrandList(params) {
        let url = "/v1/basewaterjet/brandlist";
        return fetch(url, params, "get");
    },
    getTemplateShape(params) {
        let url = basePath.systemUrl + "/v1/sys/dictionary/type/list";
        return fetch(url, params, "get");
    },
    upperandlowerstatus(params) {
        let url = "/v1/basewaterjet/upperandlowerstatus";
        return fetch(url, params, "put");
    },
    deleteBasewaterjet(params) {
        let url = "/v1/basewaterjet";
        return fetch(url, params, "delete");
    },
    /*---------------水刀模板 end--------------------*/
    AddProduct(params) {
        // 增加产品
        let url = "/v1/product";
        return fetch(url, params, "post");
    },
    AddProductTexture(params) {
        // 增加产品
        let url = "/v1/productTextureCombo";
        return fetch(url, params, "post");
    },
    modelNumberRecommand(params) {
        let url = `/v1/product/modelNumber/recommend`;
        return fetch(url, params, "get");
    },
    isModel(params) {
        let url = "/v1/dictionary/prop/type/check";
        return fetch(url, params, "get");
    },
    AddProductCategory(params) {
        // 增加产品
        let url = "/v1/category/tree";
        return fetch(url, params, "get");
    },
    UpdateProduct(params) {
        // 更新产品
        let url = `/v1/product/`;
        return fetch(url, params, "put");
    },
    UpdateProductLibrary(params) {
        // 内容库产品更新
        let url = `/v1/product/library`;
        return fetch(url, params, "put");
    },
    UpdateProductTexture(params) {
        // 内容库产品更新
        let url = `/v1/productTextureCombo/library`;
        return fetch(url, params, "put");
    },
    newUpdateProductLibrary(params) {
        // 硬装内容库产品更新
        let url = `/v1/product/library/hard`;
        return fetch(url, params, "put");
    },
    UpdateProductChannel(params) {
        // 渠道产品更新
        let url = `/v1/product/channel`;
        return fetch(url, params, "put");
    },
    UpdateProductOnline(params) {
        // 线上产品更新
        let url = `/v1/product/online`;
        return fetch(url, params, "put");
    },
    ProductList(params) {
        // 产品列表
        let url = `/v1/product/list/library`; // ?page=0&limit=200&queryType=${params.queryType}&categoryCode=${params.categoryCode}&allotStatus=${params.allotStatus}&productName=${params.productName}&secrecy=${params.secrecy}&productCode=${params.productCode}`;
        return fetch(url, params, "get");
    },
    ProductTOAllot(params) {
        // 分配产品到渠道
        let url = "/v1/product/allot";
        return fetch(url, params, "put");
    },
    QueryProductWidthCate(params) {
        // 根据分类查询产品
        let url = "/v1/product/findWithCategory/";
        return fetch(url, params, "get");
    },
    ProductDetail(params) {
        // 产品详情
        // let url = `/v1/product/${params.id}/info`;
        let url = `/v1/product/${params.platformType}/${params.productId}/info`;
        return fetch(url, params, "get");
    },
    QueryProduct(params) {
        // 产品过滤
        let url = "/v1/product/query";
        return fetch(url, params, "get");
    },
    DeleteProductById(params) {
        // 删除产品(ID)
        let url = `/v1/product/library?productIds=${params.productIds}`;
        return fetch(url, params, "delete");
    },
    DeleteChannelProduct(params) {
        // 渠道产品删除
        let url = `/v1/product/channel`;
        return fetch(url, params, "delete");
    },
    DeleteLibraryProduct(params) {
        // 内容库产品删除
        let url = `/v1/product/library`;
        return fetch(url, params, "delete");
    },
    DeleteOnlineProduct(params) {
        // 线上产品删除
        let url = `/v1/product/online`;
        return fetch(url, params, "delete");
    },
    QueryProductById(params) {
        // 根据产品ID查询产品
        let url = `/v1/product/${params.productId}/info`;
        return fetch(url, params, "get");
    },
    ProductCategory(params) {
        // 获取分类下拉框
        let url = "/v1/dictionary/product/type";
        return fetch(url, params, "get");
    },
    ProductSearch(params) {
        // 获取产品运营分类下拉框
        let url = "/v1/category/tree/search";
        return fetch(url, params, "get");
    },
    BatchAllotProduct(params) {
        // 批量分配
        let url = "/v1/product/allot";
        return fetch(url, params, "put");
    },
    ProductSecrecy(params) {
        // 公开或取消公开
        let url = "/v1/product/secrecy";
        return fetch(url, params, "put");
    },
    // 方案公开
    planSecrecy(params) {
        let url = "/v1/designplan/secrecy";
        return fetch(url, params, "put");
    },
    // 产品品牌
    ProductBrandDrop(params) {
        let url = "/v1/brand/withCompany"; // let url = '/v1/brand/list'
        return fetch(url, params, "get");
    },
    AllBrandList(params) {
        let url = "/v1/brand/list"; // let url = '/v1/brand/list'
        return fetch(url, params, "get");
    },
    // 产品系列
    ProductSeriesDrop(params) {
        let url = "/v1/product/series/list";
        return fetch(url, params, "get");
    },
    // 产品手动同步
    productSync(params) {
        let url = "/v1/product/index/sync";
        return fetch(url, params, "get");
    },
    // 产品款式
    ProductStyleDrop(params) {
        let url = "/v1/product/style/list";
        return fetch(url, params, "get");
    },
    // 产品材质
    ProductTextureDrop(params) {
        let url = "/v1/texture/textures";
        return fetch(url, params, "get");
    },
    textureImport(params) {
        //贴图模板xls
        let url = "/v1/texture/import";
        return fetch(url, params, "post");
    },
    shopImport(params) {
        //店铺xls
        let url = "/v1/companyshopoffline/import";
        return fetch(url, params, "post");
    },
    BaseProductStyleDrop(params) {
        let url = "/v1/product/base/style/list";
        return fetch(url, params, "get");
    },
    // 产品风格
    BasePlanStyleDrop(params) {
        let url = `/v1/product/base/style/planList/${params}`;
        return fetch(url, "", "get");
    },
    // 数据字典获取产品售价单位
    DisctionaryUnit(params) {
        let url = "/v1/dictionary/sale/unit/list";
        return fetch(url, params, "get");
    },
    OneKeyDesignPlanList(params) {
        // ---------------------------智能方案开始
        let url = basePath.baseUrl + "/v1/designplan/onekey/list";
        return fetch(url, params, "get");
    },
    // 方案富文本编辑器
    planTextarea(params) {
        let url = basePath.baseUrl + "/v1/designplan/config";
        return fetch(url, params, "put");
    },
    // 获取方案富文本编辑详情信息
    getPlanTextarea(params) {
        let url = basePath.baseUrl + `/v1/designplan/config/${params.id}`;
        return fetch(url, params, "get");
    },
    ChannelOneKeyDesignPlanList(params) {
        let url = "/v1/designplan/onekey/channel/list";
        return fetch(url, params, "get");
    },
    OnlineOneKeyDesignPlanList(params) {
        let url = basePath.baseUrl + "/v1/designplan/onekey/online/list";
        return fetch(url, params, "get");
    },
    OneKeyDesignPlanBaseInfo(planId) {
        // /v1/designplan/comboDetail/{planId}/{companyId}
        let url =
            basePath.baseUrl +
            `/v1/designplan/${planId}/${sessionStorage.getItem("companyID")}`;
        return fetch(url, {}, "get");
    },

    OneKeyDesignPlanName(params) {
        // /v1/designplan/comboDetail/{planId}/{companyId}
        let url =
            basePath.baseUrl +
            `/v1/designplan/comboDetail/${params.planId}/${sessionStorage.getItem(
        "companyID"
      )}`;
        return fetch(url, params, "get");
    },

    OneKeyDesignPlanEdit(params) {
        const url = basePath.baseUrl + `/v1/designplan`;
        return fetch(url, params, "put");
    },
    OneKeyDesignPlanDelete(planId) {
        const url = `/v1/designplan`;
        return fetch(url, { planIds: planId }, "delete");
    },
    OneKeyDesignPlanBatchDelete(planIds) {
        const url = `/v1/designplan/`;
        return fetch(url, { planIds: planIds }, "delete");
    },
    GetOneKeyEffectDiagram(planId) {
        const url = `/v1/designplan/${planId}/diagram`;
        return fetch(url, {}, "get");
    },
    DesignPlanStyles() {
        const url = `/v1/dictionary/style/list`;
        return fetch(url, {}, "get");
    },
    ListOneKeyPlanProducts(params) {
        const url = `/v1/designplan/products`;
        return fetch(url, params, "get");
    },
    OnekeyPlanToggleProductDisplay(params) {
        const url = `/v1/designplan/toggle/display`;
        return fetch(url, params, "post");
    },
    OneKeyPlanAllot(params) {
        const url = `/v1/designplan/allot`;
        return fetch(url, params, "post");
    },
    OneKeyPlanBatchAllot(params) {
        const url = `/v1/designplan/allot/batch`;
        return fetch(url, params, "post");
    },
    OneKeyPlanDeliver(params) {
        const url = "/v1/designplan/deliver";
        return fetch(url, params, "post");
    },
    allDelivery(params) {
        const url = "/v1/designplan/deliver/batch";
        return fetch(url, params, "post");
    },
    // 上传材质球文件
    UploadTextureFile(params) {
        const url = "/v1/model/biz/upload";
        return fetch(url, params, "put");
    },
    ModelDeliver(params) {
        const url = "/v1/model/deliver/single";
        return fetch(url, params, "put");
    },
    BatchModelDeliver(params) {
        const url = "/v1/model/deliver/batch";
        return fetch(url, params, "put");
    },
    OnekeyDeliveredCompanys(params) {
        const url = `/v1/designplan/delivery/${params.companyId}/${
      params.planId
    }/delivered`;
        return fetch(url, {}, "get");
    },
    ListCompanysWithDelivered(params) {
        const url = `/v1/designplan/${params.companyId}/${
      params.planId
    }/with/delivered`;
        return fetch(url, {}, "get");
    },
    ListModelCompanysWithDelivered(params) {
        const url = `/v1/model/${params.companyId}/${
      params.modelId
    }/with/delivered`;
        return fetch(url, {}, "get");
    },
    CommonPlanDeliver(params) {
        const url = "/v1/designplan/deliver";
        return fetch(url, params, "post");
    },
    ListDeliveredCompanys(planId, query) {
        const url = `/v1/designplan/delivered/${planId}/log`;
        return fetch(url, query, "get");
    },
    CommonDesignPlanList(params) {
        // ---------------------------普通方案开始
        let url = "/v1/designplan/common/list";
        return fetch(url, params, "get");
    },
    ChannelCommonDesignPlanList(params) {
        let url = "/v1/designplan/common/channel/list";
        return fetch(url, params, "get");
    },
    CommonDesignPlanBaseInfo(planId) {
        let url = `/v1/designplan/${planId}`;
        return fetch(url, {}, "get");
    },
    CommonDesignPlanEdit(params) {
        const url = `/v1/designplan`;
        return fetch(url, params, "put");
    },
    CommonDesignPlanDelete(planId) {
        const url = `/v1/designplan/`;
        return fetch(url, { planIds: planId }, "delete");
    },
    CommonDesignPlanBatchDelete(planIds) {
        const url = `/v1/designplan/`;
        return fetch(url, { planIds: planIds }, "delete");
    },
    GetCommonEffectDiagram(planId) {
        const url = `/v1/designplan/${planId}/diagram`;
        return fetch(url, {}, "get");
    },
    ListCommonPlanProducts(params) {
        const url = `/v1/designplan/products`;
        return fetch(url, params, "get");
    },
    CommonPlanToggleProductDisplay(params) {
        const url = `/v1/designplan/toggle/display`;
        return fetch(url, params, "post");
    },
    CommonPlanAllot(params) {
        const url = `/v1/designplan/allot`;
        return fetch(url, params, "post");
    },
    CommonPlanBatchAllot(params) {
        const url = `/v1/designplan/allot/batch`;
        return fetch(url, params, "post");
    },
    ChannelCommonPlanPushaway(params) {
        const url = "/v1/designplan/channel/pushaway";
        return fetch(url, params, "post");
    },
    ChannelCommonPlanBatchPushaway(params) {
        const url = "/v1/designplan/channel/pushaway/batch";
        return fetch(url, params, "post");
    },
    OnekeyShareDesignPlanList(params) {
        // ---------------------------分享方案开始
        // let url = '/v1/designplan/onekey/share/list'
        let url = basePath.baseUrl + "/v1/designplan/onekey/share/list";
        return fetch(url, params, "get");
    },
    // 方案价格设置
    oneKeySharePlanPriceSet(params) {
        let url = basePath.baseUrl + "/v1/designplan/onekey/share/editPlanPrice";
        return fetch(url, params, "post");
    },
    // 企业收益
    companyIncome(params) {
        let url = basePath.baseUrl + "/v1/designplan/company/income/list";
        return fetch(url, params, "post");
    },
    ShareDesignPlanCopy(params) {
        let url = "/v1/designplan/share/copy";
        return fetch(url, params, "post");
    },
    CommonShareDesignPlanList(params) {
        let url = "/v1/designplan/common/share/list";
        return fetch(url, params, "get");
    },
    ShareDesignPlanBaseInfo(planId) {
        let url = `/v1/designplan/${planId}`;
        return fetch(url, {}, "get");
    },
    // 渠道管理接口 -------------------------------------
    ChannelProductList(params) {
        const url = `/v1/product/list/channel`;
        return fetch(url, params, "get");
    },
    // 渠道智能方案上下架
    ChannelOneKeyPlanPushaway(params) {
        const url = `/v1/designplan/channel/pushaway`;
        return fetch(url, params, "post");
    },
    ChannelOneKeyPlanBatchPushaway(params) {
        const url = `/v1/designplan/channel/pushaway/batch`;
        return fetch(url, params, "post");
    },
    OnlineOneKeyPlanPushaway(params) {
        const url = `/v1/designplan/online/pushaway`;
        return fetch(url, params, "post");
    },
    OnlineOneKeyPlanBatchPushaway(params) {
        const url = `/v1/designplan/online/pushaway/batch`;
        return fetch(url, params, "post");
    },
    // 产品上下架 渠道与线上  区别：渠道
    // ProductPut (params) {
    //   /*
    //     platformIds (string, optional): 线上产品上下架操作时，需传的平台ID,多选以逗号分隔 ,
    //     platformType (string): 渠道类型:渠道上下架:2b,线上上下架:2c ,
    //     productIds (Array[integer]): 产品id集合 ,
    //     putStatus (integer, optional): 渠道产品上下架操作时，上下架状态:1上架/2下架
    //    */
    //   const url = `/v1/product/put`
    //   return fetch(url, params, 'put')
    // },
    // 渠道产品上下架
    ProductPutChannel(params) {
        const url = `/v1/product/put/channel`;
        return fetch(url, params, "put");
    },
    // 线上产品上下架
    ProductPutOnline(params) {
        const url = `/v1/product/put/online`;
        return fetch(url, params, "put");
    },
    // 线上管理 接口
    OnlineProductList(params) {
        const url = `/v1/product/list/online`;
        return fetch(url, params, "get");
    },
    // 平台列表
    PlatFormList(params) {
        const url = `/v1/platform/list`;
        return fetch(url, params, "get");
    },
    ListCompany(params) {
        // ------------------------------------------------公司
        const url = `/v1/company/list`;
        return fetch(url, params, "get");
    },
    userDetail(params) {
        const url = `/v1/user/${params.userId}`;
        return fetch(url, params, "get");
    },
    //  手机号验证是否已经被使用
    MobileCheck(params) {
        const url = `/v1/user/check`;
        return fetch(url, params, "get");
    },
    // 编辑用户信息
    UserInfoEdit(params) {
        const url = `/v1/user`;
        return fetch(url, params, "post");
    },
    ListModel(params) {
        // -------------------------------------------------模型
        for (let key in params) {
            if (
                params[key] === "" ||
                params[key] === undefined ||
                params[key] === null
            ) {
                delete params[key];
            }
        }
        const url = `/v1/model/list`;
        return fetch(url, params, "get");
    },
    GetModel(modelId) {
        const url = `/v1/model/${modelId}`;
        return fetch(url, {}, "get");
    },
    DeleteModel(modelIds) {
        const url = `/v1/model`;
        return fetch(url, { ids: modelIds }, "delete");
    },
    EditModel(params) {
        const url = `/v1/model`;
        return fetch(url, params, "put");
    },
    AddModel(params) {
        const url = "/v1/model/biz";
        return fetch(url, params, "post");
    },
    ListTexture(params) {
        // -------------------------------------------------模型
        const url = `/v1/texture/list`;
        return fetch(url, params, "get");
    },
    GetTexture(textureId) {
        const url = `/v1/texture/${textureId}`;
        return fetch(url, {}, "get");
    },
    DeleteTexture(textureIds) {
        const url = `/v1/texture`;
        return fetch(url, { ids: textureIds }, "delete");
    },
    EditTexture(params) {
        const url = `/v1/texture`;
        return fetch(url, params, "put");
    },
    AddTexture(params) {
        const url = `/v1/texture`;
        return fetch(url, params, "post");
    },
    // ----------------------------------用户企业信息----------------------------
    UpdateCompanyInfo(params) {
        // 更新企业信息
        const url = `/v1/company`;
        return fetch(url, params, "put");
    },
    getCompanyIDBrandID(params) {
        // 根据用户获取企业ID/品牌ID
        const url = `/v1/user/companyInfo`;
        return fetch(url, params, "get");
    },
    getCompanyDetail(params) {
        const url = `/v1/company/${params.companyId}/info`;
        return fetch(url, params, "get");
    },
    ProductProps(params) {
        // 获取产品属性
        const url = `/v1/product/props`;
        return fetch(url, params, "get");
    },
    modelTexture(params) {
        // 校验分类类型为模型还是贴图
        const url = `/v1/category/type`;
        return fetch(url, params, "get");
    },
    // 模型-贴图详情
    modelTextureDetail(params) {
        // 校验分类类型为模型还是贴图
        const url = `/v1/model/area/info`;
        return fetch(url, params, "get");
    },
    // 材质表面属性
    textureAttr(params) {
        const url = `/v1/dictionary/texture/attr`;
        return fetch(url, params, "get");
    },
    // 材质材料
    textureCaiLiao(params) {
        const url = `/v1/dictionary/texture/textures`;
        return fetch(url, params, "get");
    },
    // 效果图操作
    SetOnekeyPlanDefaultPic(params) {
        const url = "/v1/designplan/pic/default";
        return fetch(url, params, "post");
    },
    BanEffectPic(picId) {
        const url = "/v1/renderpic/pic/ban";
        return fetch(url, picId, "post");
    },
    DeleteEffectPic(picId) {
        const url = "/v1/renderpic";
        return fetch(url, { picId }, "delete");
    },
    getBrandName(params) {
        const url = `/v1/brand/withCompany`;
        return fetch(url, params, "get");
    },
    // 产品组合
    // 内容库产品组合列表
    getLibraryProductGroupList(params) {
        const url = "/v1/group/library";
        return fetch(url, params, "get");
    },
    // 产品组合类型
    ProductGroupType(params) {
        const url = "/v1/dictionary/group/type/group";
        return fetch(url, params, "get");
    },
    // 产品组合分配
    ProductGroupAllot(params) {
        const url = "/v1/group/allot";
        return fetch(url, params, "put");
    },
    // 产品组合详情
    ProductGroupDetail(params) {
        const url = `/v1/group/${params.platformType}/${params.groupId}/info`;
        return fetch(url, {}, "get");
    },
    // 渠道组合列表
    getChannelProductGroupList(params) {
        const url = "/v1/group/channel";
        return fetch(url, params, "get");
    },
    // 线上组合列表
    getOnlineProductGroupList(params) {
        const url = "/v1/group/online";
        return fetch(url, params, "get");
    },
    // 适用房间面积
    FitRoomArea(params) {
        const url = `/v1/dictionary/group/area?${params.value}`;
        return fetch(url, params, "get");
    },
    // 房屋类型
    FitTypeHose(params) {
        const url = "/v1/dictionary/group/type/house";
        return fetch(url, params, "get");
    },
    // 产品组合内容库编辑
    ProductGroupEdit(params) {
        const url = "/v1/group/edit/library";
        return fetch(url, params, "put");
    },
    // 产品组合上下架
    // ProductGroupPutaway (params) {
    //   const url = '/v1/group/putAway'
    //   return fetch(url, params, 'put')
    // },
    // 渠道产品组合上下架
    ProductGroupPutawayChannel(params) {
        const url = "/v1/group/putAway/channel";
        return fetch(url, params, "put");
    },
    // 线上产品组合上下架
    ProductGroupPutawayOnline(params) {
        const url = "/v1/group/putAway/online";
        return fetch(url, params, "put");
    },
    // 产品库产品组合删除
    ProductGroupDelete(params) {
        const url = "/v1/group/library";
        return fetch(url, params, "delete");
    },
    // 产品组合上下架
    productGroupShelf(params) {
        const url = "/v1/group/putAway";
        return fetch(url, params, "put");
    },
    // 产品库产品组合公开操作
    ProductGroupSecrecy(params) {
        const url = "/v1/group/secrecy";
        return fetch(url, params, "put");
    },
    // 平台上下架管理
    platformShelf(params) {
        const url = "/v1/platform/listByType";
        return fetch(url, params, "get");
    },
    // 内容库方案上下架
    contentLibraryPlanShelf(params) {
        const url = "/v1/designplan/library/push";
        return fetch(url, params, "put");
    },
    // 内容库产品上下架
    contentLibraryProductShelf(params) {
        const url = "/v1/product/put/library";
        return fetch(url, params, "put");
    },
    // 线上渠道编辑
    // ProductGroupOnlineChannelEdit (params) {
    //   const url = '/v1/group/edit/manager/online'
    //   return fetch(url, params, 'put')
    // },
    //   线上编辑
    ProductGroupOnlineEdit(params) {
        const url = "/v1/group/edit/manager/online";
        return fetch(url, params, "put");
    },
    // 渠道编辑
    ProductGroupChannelEdit(params) {
        const url = "/v1/group/edit/manager/channel";
        return fetch(url, params, "put");
    },
    // 获取图片验证码
    imgSecurityCode(params) {
        const url = basePath.ucUrl + `/v1/center/getCode`;
        return fetch(url, params, "get");
    },
    // 获取短信验证码
    phoneSecurityCode(params) {
        const url = basePath.ucUrl + `/v1/center/getSmsCode`;
        return fetch(url, params, "get");
    },
    // 修改密码
    setPassword(params) {
        const url = basePath.ucUrl + `/v1/center/forgetPwd`;
        return fetch(url, params, "post");
    },
    // 判断手机是否被注册
    isPhone(params) {
        const url = basePath.ucUrl + `/v1/center/checkPhone`;
        return fetch(url, params, "get");
    },
    // 保存个人设置
    setEditUser(params) {
        const url = basePath.ucUrl + `/v1/center/editUser`;
        return fetch(url, params, "post");
    },
    getUserDetailInfo(params) {
        const url = basePath.ucUrl + `/v2/user/center/getUserInfo2b`;
        return fetch(url, params, "get");
    },
    // 个人中心密码修改检查 /v2/user/center/checkUserOldPassword
    userOldPwdCheck(params) {
        const url = basePath.ucUrl + "/v2/user/center/checkUserOldPassword";
        return fetch(url, params, "post");
    },
    // 个人中心密码修改
    userPassworUpdate(params) {
        const url =
            basePath.ucUrl + "/v2/user/center/updatePasswordByMerchantManage";
        return fetch(url, params, "post");
    },
    // 个人中心修改手机号
    userMobileUpdate(params) {
        const url = basePath.ucUrl + "/v2/user/center/modify2BUserMobile";
        return fetch(url, params, "post");
    },
    //  判断是否为多企业经销商账号
    isMultipleCompany(params) {
        const url = basePath.ucUrl + "/v1/center/isMultipleFranchiserAccount";
        return fetch(url, params, "post");
    },
    // 保存账户安全信息
    setUpdatePhone(params) {
        const url = basePath.ucUrl + `/v1/center/updatePhone`;
        return fetch(url, params, "post");
    },
    // 验证图片验证码
    setCheckCode(params) {
        const url = basePath.ucUrl + `/v1/center/checkCode`;
        return fetch(url, params, "get");
    },
    getEditUser(params) {
        const url = basePath.ucUrl + `/v1/center/getUserInfo`;
        return fetch(url, params, "get");
    },
    // -------------------------- 小程序后台接口-------------------------------------
    spOrderList(params) {
        const url = basePath.orderUrl + "/v1/order/list";
        return fetch(url, params, "get");
    },
    //订单导出
    exportOrderApi(params) {
        const url = basePath.orderUrl + `/v1/order/export`;
        return exportData(url, params, "get");
    },
    spGetOrderDetail(params) {
        const url = basePath.orderUrl + `/v1/order/get/detail?id=${params.id}`;
        return fetch(url, params, "get");
    },
    spChangeOrderStatus(sureOrder) {
        const url =
            basePath.orderUrl +
            `/v1/order/change/order/status?type=${sureOrder.type}&id=${sureOrder.id}`;
        return fetch(url, sureOrder, "put");
    },
    spSendGoods(params) {
        const url = basePath.orderUrl + `/v1/order/dispatch`;
        return fetch(url, params, "put");
    },
    // 小程序首页设置
    spIndexBannerList(params) {
        delete params.userId;
        const url = basePath.systemUrl + "/v1/banner/manage/miniprogram/get/list";
        return fetch(url, params, "get");
    },
    spIndexBannerDelete(params) {
        delete params.userId;
        const url = basePath.systemUrl + "/v1/banner/manage/miniprogram/delete";
        return fetch(url, params, "delete");
    },
    // 小程序首页设置 新增广告
    spIndexBannerAdd(params) {
        delete params.userId;
        const url = basePath.systemUrl + "/v1/banner/manage/miniprogram/pic/add";
        return fetch(url, params, "post");
    },
    // 小程序首页设置 修改广告图片信息
    spIndexBannerUpdate(params) {
        delete params.userId;
        const url = basePath.systemUrl + "/v1/banner/manage/miniprogram/pic/update";
        return fetch(url, params, "put");
    },
    // 小程序首页设置 修改广告跳转路径
    spIndexBannerUpdateUrl(params) {
        delete params.userId;
        const url =
            basePath.systemUrl + "/v1/banner/manage/miniprogram/skip/update";
        return fetch(url, params, "put");
    },
    // 小程序基本设置 修改企业资质名称
    spIndexBannerUpdateName(params) {
        delete params.userId;
        const url =
            basePath.systemUrl + "/v1/banner/manage/miniprogram/name/update";
        return fetch(url, params, "put");
    },
    spShopBannerList(params) {
        delete params.userId;
        const url =
            basePath.systemUrl + "/v1/banner/manage/miniprogram/shop/bannerList";
        return fetch(url, params, "get");
    },
    // 小程序后台 品牌介绍更新，
    upadateBrandIntro(params) {
        const url = "/v1/company/update/introduce";
        return fetch(url, params, "put");
    },
    getBrandIntroDetail(params) {
        const url = `/v1/company/get/introduce`;
        return fetch(url, params, "get");
    },
    // 客户管理接口
    getCustomerList(params) {
        delete params.userId;
        const url = basePath.baseUrl + "/v1/customer/list";
        return fetch(url, params, "get");
    },
    //我的客户管理接口
    ourDistributorList(params) {
        delete params.userId;
        const url = basePath.baseUrl + "/v1/customer/distributor/list";
        return fetch(url, params, "get");
    },
    // 客户管理经销商列表
    getCustomerDealerList(params) {
        delete params.userId;
        const url = basePath.baseUrl + "/v1/customer/dealer/list";
        return fetch(url, params, "get");
    },
    // 重新分配
    reAllot(params) {
        delete params.userId;
        const url = basePath.baseUrl + "/v1/customer/reallocate";
        return fetch(url, params, "post");
    },
    customerDetail(params) {
        const url = basePath.baseUrl + `/v1/customer/info/${params.id}`;
        return fetch(url, params, "get");
    },
    // 跟进人
    getFollowIn(params) {
        const url = basePath.baseUrl + "/v1/customer/companyUser/list";
        return fetch(url, params, "get");
    },
    // 跟进详情 /v1/customer/followUp/{id}
    getFollowDetail(params) {
        const url = basePath.baseUrl + `/v1/customer/followUp/${params.id}`;
        return fetch(url, params, "get");
    },
    // 跟进提交更新
    updateFollowInfo(params) {
        const url = basePath.baseUrl + `/v1/customer/followIn/`;
        return fetch(url, params, "post");
    },
    // 经销商企业下拉框 客户管理-厂商列表-重新分配下拉框
    getComapnyIn(params) {
        const url = basePath.baseUrl + "/v1/customer/reallocateList";
        return fetch(url, params, "get");
    },
    // 微官网轮播图接口------------------------------------------
    // 删除轮播广告
    wgBannerDelete(params) {
        const url = basePath.systemUrl + "/v1/banner/manage/weiguanwang/delete";
        return fetch(url, params, "delete");
    },
    // 查询首页广告列表
    wgBannerList(params) {
        const url = basePath.systemUrl + "/v1/banner/manage/weiguanwang/get/list";
        return fetch(url, params, "get");
    },
    // 新增广告信息
    wgBannerAdd(params) {
        const url = basePath.systemUrl + "/v1/banner/manage/weiguanwang/pic/add";
        return fetch(url, params, "post");
    },
    // 修改广告图片信息
    wgBannerUpdate(params) {
        const url = basePath.systemUrl + "/v1/banner/manage/weiguanwang/pic/update";
        return fetch(url, params, "post");
    },
    // 修改广告跳转路径
    wgBannerUpdateUrl(params) {
        const url =
            basePath.systemUrl + "/v1/banner/manage/weiguanwang/skip/update";
        return fetch(url, params, "put");
    },
    // 企业管理------------------------------------------
    // 企业管理信息详情
    companyMessageDetial(params) {
        const url = basePath.systemUrl + `/v1/base/manufacturer/details`;
        return fetch(url, params, "get");
    },

    // 获取商品列表
    getGoodsList(sureOrder) {
        const url = `/v1/base/goods/list`;
        return fetch(url, sureOrder, "get");
    },

    // 经销商企业管理接口--------------------------------------
    dealerList(params) {
        const url = basePath.systemUrl + "/v1/base/franchiser/list";
        return fetch(url, params, "post");
    },
    // 经销商企业编辑
    dealerEdit(params) {
        const url = basePath.systemUrl + "/v1/base/franchiser";

        return fetch(url, params, "put");
    },
    // 经销商企业新增
    dealerAdd(params) {
        const url = basePath.systemUrl + "/v1/base/franchiser";
        return fetch(url, params, "post");
    },
    // 经销商企业详情
    dealerDetail(params) {
        const url = basePath.systemUrl + "/v1/base/franchiser/details";
        return fetch(url, params, "get");
    },
    // 经销商分类接口
    dealerCategory(params) {
        const url = basePath.systemUrl + "/v1/pro/category/franchiser/listAll";
        return fetch(url, params, "get");
    },
    // 基础品牌
    baseBrand(params) {
        const url = basePath.systemUrl + "/v1/base/brand/company/list";
        return fetch(url, params, "get");
    },
    //  区域选择接口
    areaList(params) {
        const url = basePath.systemUrl + `/v1/base/area/list`;
        return fetch(url, params, "post");
    },
    // 删除经销商
    dealerDelete(params) {
        const url = basePath.systemUrl + `/v1/base/franchiser`;
        return fetch(url, params, "delete");
    },
    // 企业可见范围分类接口
    companyCategory(params) {
        const url = basePath.systemUrl + "/v1/pro/category/company/listAll";
        return fetch(url, params, "get");
    },
    // 企业厂商 -- 企业管理
    manuFacturerDetail(params) {
        const url = basePath.systemUrl + `/v1/base/manufacturer/details`;
        return fetch(url, params, "get");
    },
    // 企业非厂商编辑
    nonFactoryUpate(params) {
        const url = basePath.systemUrl + `/v1/base/manufacturer/non/update`;
        return fetch(url, params, "put");
    },

    // 更新企业厂商信息
    updateFacturerMsg(params) {
        const url = basePath.systemUrl + `/v1/base/manufacturer/`;
        return fetch(url, params, "put");
    },
    defaultShow(params) {
        const url =
            basePath.systemUrl +
            `/v1/base/franchiser/cabrand?companyId=${params.companyId}&msgId=1`;
        return fetch(url, params, "post");
    },
    // 用户类型
    dealerUserType(params) {
        delete params.userId;
        const url = basePath.systemUrl + `/v1/sys/eiu/user`;
        return fetch(url, params, "get");
    },
    // 品牌馆------------------------------------------
    brandShopDetial(params) {
        const url = basePath.systemUrl + `/v1/company/shop/get`;
        return fetch(url, params, "put");
    },
    // -------------------------lshc-----end-------------------------
    // 商品分类
    getGoodsType(sureOrder) {
        const url = `/v1/base/goods/type`;
        return fetch(url, sureOrder, "get");
    },
    // 商品上架
    postDownPutaway(sureOrder) {
        const url = `/v1/base/goods/putaway`;
        return fetch(url, sureOrder, "post");
    },
    // 商品下架
    postUpPutaway(sureOrder) {
        const url = `/v1/base/goods/offshelves`;
        return fetch(url, sureOrder, "post");
    },
    // 商品编辑
    getGoodsEdit(sureOrder) {
        const url = `/v1/base/goods/edit`;
        return fetch(url, sureOrder, "get");
    },
    // 保存商品spu信息
    updateSpu(sureOrder) {
        const url = `/v1/base/goods/updateSpu`;
        return fetch(url, sureOrder, "put");
    },
    // 保存商品sku信息
    updateSku(sureOrder) {
        const url = `/v1/base/goods/updateSku`;
        return fetch(url, sureOrder, "put");
    },
    /************************店铺管理*********************/
    //获取企业类型
    getCompanyType(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/getCompanyType`;
        return fetch(url, sureOrder, "post");
    },
    // 店铺信息列表
    shopList(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/list`;
        // const url =  basePath.systemUrl+`/v1/company/shop/list`;
        return fetch(url, sureOrder, "post");
    },
    // 店铺加入黑名单
    // （0:未被拉黑；1:被拉黑）
    setBlacklist(params) {
        const url = basePath.systemUrl + `/v1/company/shop/setBlacklist`;
        return fetch(url, params, "post");
    },
    //店铺信息删除
    shopDelete(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/remove`;
        return fetch(url, sureOrder, "delete");
    },
    //店铺新增
    shopAdd(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/save`;
        return fetch(url, sureOrder, "post");
    },
    //店铺发布
    shopRelease(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/setRelease`;
        return fetch(url, sureOrder, "post");
    },
    //店铺详情
    shopDatails(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/get`;
        // const url =  basePath.systemUrl+`/v1/company/shop/get`;
        return fetch(url, sureOrder, "get");
    },
    //用户是否已经创建店铺
    shopIsExit(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/isShopAlreadyExit`;
        return fetch(url, sureOrder, "get");
    },
    //店铺编辑
    shopEdit(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/update`;
        return fetch(url, sureOrder, "put");
    },
    //新增线下门店
    shopOfflineAdd(sureOrder) {
        const url = basePath.baseUrl + `/v1/companyshopoffline`;
        return fetch(url, sureOrder, "post");
    },
    //更新线下门店
    shopOfflineUpdate(sureOrder) {
        const url = basePath.baseUrl + `/v1/companyshopoffline`;
        return fetch(url, sureOrder, "put");
    },
    //获取门店详情
    getShopOfflineDetail(params) {
        const url = basePath.baseUrl + `/v1/companyshopoffline/${params.id}`;
        return fetch(url, "", "get");
    },
    //线下门店列表
    shopOfflineList(sureOrder) {
        const url = basePath.baseUrl + `/v1/companyshopoffline/list`;
        // const url =  basePath.systemUrl+`/v1/company/shop/list`;
        return fetch(url, sureOrder, "get");
    },
    //删除线下门店
    shopOfflineDelete(sureOrder) {
        const url = basePath.baseUrl + `/v1/companyshopoffline`;
        // const url =  basePath.systemUrl+`/v1/company/shop/list`;
        return fetch(url, sureOrder, "delete");
    },
    //发布线下门店
    shopOfflineRelease(sureOrder) {
        const url =
            basePath.baseUrl +
            `/v1/companyshopoffline/toRelease?id=${sureOrder.id}&isRelease=${
        sureOrder.isRelease
      }`;
        // const url =  basePath.systemUrl+`/v1/company/shop/list`;
        return fetch(url, sureOrder, "put");
    },
    //工作种类
    jobType(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/dictionary/type/list`;
        return fetch(url, sureOrder, "get");
    },
    //上传图片
    upLoadImg(sureOrder) {
        const url = `/v1/storage/upload`;
        return fetch(url, sureOrder, "post");
    },
    // 店铺智能方案列表 /v1/storeplan/onekey/list
    shopIntelligentList(params) {
        let url = basePath.baseUrl + "/v1/storeplan/onekey/list";
        return fetch(url, params, "get");
    },
    // 店铺普通方案列表
    shopCommonPlanList(params) {
        let url = basePath.baseUrl + "/v1/storeplan/common/list";
        return fetch(url, params, "get");
    },
    // 品牌 /v1/brand/list
    shopBrandDropList(params) {
        let url = basePath.baseUrl + "/v1/brand/list";
        return fetch(url, params, "get");
    },
    shopPlanDetail(params) {
        let url =
            basePath.baseUrl + `/v1/storeplan/${params.planId}/${params.companyId}`;
        return fetch(url, params, "get");
    },
    // 发布方案
    shopPublishPlan(params) {
        let url = basePath.baseUrl + `/v1/storeplan/plan/publishPlan`;
        return fetch(url, params, "post");
    },
    /********************************工程案例************************************/
    // 工程案例列表
    projectCaseList(sureOrder) {
        const url = basePath.systemUrl + `/v1/shop/case/list`;
        return fetch(url, sureOrder, "post");
    },
    //工程案例发布
    projectCasePublish(sureOrder) {
        const url = basePath.systemUrl + `/v1/shop/case/updateReleaseStauts`;
        return fetch(url, sureOrder, "post");
    },
    //工程案例删除
    projectCaseDelete(sureOrder) {
        const url = basePath.systemUrl + `/v1/shop/case/remove`;
        return fetch(url, sureOrder, "delete");
    },
    //工程案例上传图片
    projectCaseUpLoadImg(sureOrder) {
        const url = basePath.systemUrl + `/v1/shop/case/img/upload`;
        return fetch(url, sureOrder, "post");
    },
    //工程案例保存
    addProjectCase(sureOrder) {
        const url = basePath.systemUrl + `/v1/shop/case/save`;
        return fetch(url, sureOrder, "post");
    },
    //工程案例编辑
    editProjectCase(sureOrder) {
        const url = basePath.systemUrl + `/v1/shop/case/update`;
        return fetch(url, sureOrder, "put");
    },
    //工程案例详情
    projectCaseDatails(sureOrder) {
        const url = basePath.systemUrl + `/v1/shop/case/get`;
        return fetch(url, sureOrder, "get");
    },
    /**********************品牌馆***************************/
    //品牌馆数据请求
    getBrand(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/brandPavilion`;
        return fetch(url, sureOrder, "get");
    },
    //品牌馆数据更新
    updateBrand(sureOrder) {
        const url = basePath.systemUrl + `/v1/company/shop/updateDisplayStatus`;
        return fetch(url, sureOrder, "post");
    },
    /**********************账号管理***************************/
    //内部账号列表
    getInteriorAccount(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/eiu/user/list`;
        return fetch(url, sureOrder, "post");
    },
    //内部账号删除
    interiorAccountDelete(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/eiu/user`;
        return fetch(url, sureOrder, "delete");
    },
    // 内部账号详情
    getInteriorDatails(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/eiu/user`;
        return fetch(url, sureOrder, "get");
    },
    //内部账号电话号码验证
    getInteriorPhone(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/eiu/user/checkphone?${sureOrder}`;
        return fetch(url, "", "post");
    },
    //内部用户编辑提交
    postInteriorEdit(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/eiu/user`;
        return fetch(url, sureOrder, "put");
    },
    // 用户类型
    getUserType(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/dictionary/user/list`;
        return fetch(url, sureOrder, "get");
    },
    // 套餐购买用户Scope类型
    getAllUserType(sureOrder) {
        const url =
            basePath.systemUrl +
            `/v1/sys/dictionary/user/scope/list/${sureOrder.companyId}`;
        return fetch(url, "", "get");
    },
    //经销商账号列表
    getCompanyAccount(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/fa/user/list`;
        return fetch(url, sureOrder, "post");
    },
    //经销商账号删除
    getCompanyDelete(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/fa/user`;
        return fetch(url, sureOrder, "delete");
    },
    //经销商企业
    getDealerList(sureOrder) {
        const url = basePath.systemUrl + `/v1/base/manufacturer/franchiser/list`;
        return fetch(url, sureOrder, "get");
    },
    //经销商账号详情
    getCompanyDatails(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/fa/user`;
        return fetch(url, sureOrder, "get");
    },
    //经销商账号电话号码验证
    getCompanyPhone(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/fa/user/checkphone?${sureOrder}`;
        return fetch(url, "", "post");
    },
    //经销商账号编辑保存
    postCompanyEdit(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/fa/user`;
        return fetch(url, sureOrder, "put");
    },
    //POST /v1/sys/fa/user/cf/confirm经销商用户确认经销商企业
    postCompanyConfirm(sureOrder) {
        const url = basePath.systemUrl + `/v1/sys/fa/user/cf/confirm?${sureOrder}`;
        return fetch(url, "", "post");
    },
    updatamodel() {
        const url = basePath.baseUrl + `/v1/model/editor/file`;
        return fetch(url, "", "post");
    },
    /*新增模块 优惠券------------------------------------------------------------------*/
    //获取商品推荐列表
    getGoodsRecommendList(product) {
        const url = basePath.shopApiUrl + "/v1/sandu/mini/goodsrecommend/getList";
        //alert(url);
        return fetch(url, product, "get");
    },
    //添加商品推荐列表
    addGoodsRecommendList(params) {
        const url = basePath.shopApiUrl + "/v1/sandu/mini/goodsrecommend/batchAdd";
        //alert(url);
        return fetch(url, params, "post");
    },
    //删除商品推荐列表
    deleteGoodsRecommendList(params) {
        const url =
            basePath.shopApiUrl + "/v1/sandu/mini/goodsrecommend/batchDelete";
        return fetch(url, params, "post");
    },
    //保存优惠券列表
    addCoupon(params) {
        const url = basePath.shopApiUrl + "/v1/sandu/mini/activity/save";
        return fetch(url, params, "post");
    },
    //获取优惠券详情
    getCouponDeatil(id) {
        const url = basePath.shopApiUrl + "/v1/sandu/mini/activity/getCoupon";
        return fetch(url, id, "get");
    },
    //获取优惠券关联的商品列表
    getCouponPro(params) {
        const url =
            basePath.shopApiUrl + "/v1/sandu/mini/activity/getCouponProductList";
        return fetch(url, params, "get");
    },
    //所有优惠券
    getCouponList(params) {
        const url = basePath.shopApiUrl + "/v1/sandu/mini/activity/getCouponList";
        return fetch(url, params, "get");
    },
    //更改优惠券状态
    updateCouponStatus(params) {
        const url = basePath.shopApiUrl + "/v1/sandu/mini/activity/updateState";
        return fetch(url, params, "get");
    },
    //删除优惠券
    delCoupon(params) {
        const url = basePath.shopApiUrl + "/v1/sandu/mini/activity/deleteCoupon";
        return fetch(url, params, "get");
    },
    //获取可选用的商品列表
    getAvailableGoodsList(params) {
        const url = basePath.shopApiUrl + "/v1/sandu/mini/goods/getAvailableList";
        return fetch(url, params, "get");
    },

    //获取可选套餐列表
    selectListAllService(params) {
        const url = basePath.systemUrl + `/v1/services/choose/list`;
        return fetch(url, params, "get");
    },
    //获取已购套餐列表
    listServiceWithBuy(params) {
        const url = basePath.systemUrl + `/v1/services/purchase/list`;
        return fetch(url, params, "get");
    },
    //获取已购套餐列表
    listRecordsApi(params) {
        const url = basePath.systemUrl + `/v1/services/paylog/list`;
        return fetch(url, params, "get");
    },
    //导出已购套餐
    exportExcelApi(params) {
        const url = basePath.systemUrl + `/v1/services/file/${params.companyId}`;
        return fetch(url, params, "get");
    },
    // 可购买套餐
    mayBuy(params) {
        const url = basePath.systemUrl + `/choose/list/${params.companyId}`;
        return fetch(url, params, "get");
    },
    funcDetail(params) {
        const url =
            basePath.systemUrl + `/v1/services/func/details/${params.serviceId}`;
        return fetch(url, params, "get");
    },
    // 确认购买套餐
    confirmBuy(params) {
        const url = basePath.systemUrl + `/v1/services/pay/confirm`;
        return fetch(url, params, "post");
    },
    // 确认购买套餐
    confirmBuyTest(params) {
        const url = basePath.systemUrl + `/v1/services/pay/confirm/test`;
        return fetch(url, params, "post");
    },
    // 查询购买套餐详情
    packageDetail(params) {
        const url =
            basePath.systemUrl +
            `/v1/services/buyInfo/${params.userId}/${params.saleChannel}/${
        params.serviceId
      }`;
        return fetch(url, params, "get");
    },
    //选择套餐
    chooseList(params) {
        const url =
            basePath.systemUrl +
            `/v1/services/chooseList/${params.userId}/${params.saleChannel}/${
        params.pageNo
      }/${params.pageSize}`;
        return fetch(url, "", "get");
    },
    //套餐续费
    renewals(params) {
        const url =
            basePath.systemUrl +
            `/v1/services/renewals/${params.userId}/${params.saleChannel}`;
        return fetch(url, "", "get");
    },
    //套餐升级续费
    updagrade(params) {
        const url =
            basePath.systemUrl +
            `/v1/services/updagrade/${params.userId}/${params.saleChannel}/${
        params.serviceId
      }`;
    return fetch(url, "", "get");
  },
  //订单修改店铺
  updateShopByPay(params) {
    const url = basePath.orderUrl + `/v1/order/updateShopId`;
    return fetch(url, params, "get");
  },
  //确认订单
  confirmPay(params) {
    const url = basePath.systemUrl + `/v1/services/pay/confirm`;
    return fetch(url, params, "post");
  },
  selectPackage(params) {
    const url =
      basePath.systemUrl + `/v1/services/payResponse/${params.orderNo}`;
    return fetch(url, "", "get");
  },

    //特卖商品 弹框列表
    spesellList(params) {
        const url = basePath.baseUrl + "/v1/special/goods/getSpecialGoodsList";
        return fetch(url, params, "get");
    },
    //特卖商品 主页列表
    sepsellmainlist(params) {
        const url = basePath.baseUrl + "/v1/special/goods/getMainPageGoodsList";
        return fetch(url, params, "get");
    },

    //删除 添加 特卖商品列表
    spesellupdatelist(params) {
        const url =
            basePath.baseUrl +
            `/v1/special/goods/updateMainPageState/${params.spuId}/${params.state}`;
        return fetch(url, params, "put");
    },
    //预售商品 弹框列表
    preselllist(params) {
        const url = basePath.baseUrl + "/v1/presell/goods/getNewGoodsList";
        return fetch(url, params, "get");
    },
    //预售商品 主页列表
    presellmainlist(params) {
        const url = basePath.baseUrl + "/v1/presell/goods/getMainPageGoodsList";
        return fetch(url, params, "get");
    },
    //删除 添加 预售商品列表
    presellupdatelist(params) {
        const url =
            basePath.baseUrl +
            `/v1/presell/goods/updateMainPageState/${params.spuId}/${params.state}`;
        return fetch(url, params, "put");
    },

    //取到企业经销商列表
    getcompanylist(params) {
        const url = basePath.ssoUrl + "/v1/user/getFranchiserCompanyList";
        return fetch(url, params, "post");
    },

    //选择企业
    switchcompany(params) {
        const url = basePath.ssoUrl + "/v1/user/switchCompany";
        return fetch(url, params, "post");
    },
    // 客户管理模块
    //客户管理规则列表
    allotRule(params) {
        const url = basePath.baseUrl + "/v1/customer/AlotZoneList/allotRule";
        return fetch(url, params, "get");
    },

    //客户列表导出校验
    exportCustomerCheckApi(params) {
        const url = basePath.baseUrl + `/v1/customer/exportCheck`;
        return fetch(url, params, "get");
    },

    //客户列表导出
    exportCustomerApi(params) {
        const url = basePath.baseUrl + `/v1/customer/export`;
        return exportData(url, params, "get");
    },

    //厂商获取经销商列表
    companyList(params) {
        const url =
            basePath.baseUrl + `/v1/customer/companyList/factory/${params.companyId}`;
        return fetch(url, "", "get");
    },
    cancelCustomer(params) {
        //取消分配
        const url = basePath.baseUrl + `/v1/customer`;
        return fetch(url, params, "delete");
    },
    customerRule(params) {
        //根据经销商id获取分配规则
        const url = basePath.baseUrl + `/v1/customer/${params.companyId}`;
        return fetch(url, params, "get");
    },
    companyListRule(params) {
        //根据经销商是否已经分配
        console.log(params.companyId);
        const url =
            basePath.baseUrl +
            `/v1/customer/companyList/${params.companyId ? params.companyId : "-1"}`;
        return fetch(url, "", "get");
    },
    addCompany(params) {
        //新增经销商分配规则
        const url = basePath.baseUrl + `/v1/customer/company`;
        return fetch(url, params, "post");
    },
    //获取 企业 所属行业列表
    getindustrylist(params) {
        const url = basePath.systemUrl + "/v1/sys/dictionary/type/list";
        return fetch(url, params, "get");
    },
    //通过 企业行业 显示 企业可见范围
    industrytoextend(params) {
        const url = basePath.systemUrl + "/v1/pro/category/industry/list";
        return fetch(url, params, "get");
    },

    //获取  经销商 所属行业列表
    getchiserindustrylist(params) {
        const url = basePath.systemUrl + "/v1/base/franchiser/franchiser/category";
        return fetch(url, params, "get");
    },

    //模糊查询   经销商 所属行业列表
    searchchiserindustrylist(params) {
        const url =
            basePath.systemUrl + "/v1/base/franchiser/franchiser/category/list";
        return fetch(url, params, "get");
    },
    //新增的经销商 行业回显
    addindustry(params) {
        const url = basePath.systemUrl + "/v1/base/franchiser/getFactoryIndustrys";
        return fetch(url, params, "get");
    },

    //获取博文首页列表
    getbloglist(params) {
        const url = basePath.systemUrl + "/v1/company/shop/article/list";
        return fetch(url, params, "post");
    },
    //新增博文
    addblog(params) {
        const url = basePath.systemUrl + "/v1/company/shop/article/save";
        return fetch(url, params, "post");
    },
    //发布店铺博文接口
    publishblog(params) {
        const url = basePath.systemUrl + "/v1/company/shop/article/release";
        return fetch(url, params, "put");
    },
    //删除店铺博文接口
    deleteblog(params) {
        const url = basePath.systemUrl + "/v1/company/shop/article/remove";
        return fetch(url, params, "delete");
    },
    //博文详情接口
    detailblog(params) {
        const url = basePath.systemUrl + "/v1/company/shop/article/get";
        return fetch(url, params, "get");
    },
    //编辑保存博文接口
    updateblog(params) {
        const url = basePath.systemUrl + "/v1/company/shop/article/update";
        return fetch(url, params, "put");
    },
    //博客图片上传
    blogpicUp(params) {
        const url = basePath.systemUrl + "/v1/company/shop/article/img/upload";
        return fetch(url, params, "post");
    },
    //获取全屋方案列表
    getfullhouselist(params) {
        // const url = basePath.baseUrl + '/v1/fullHouse/list';
        const url = basePath.baseUrl + "/v1/fullHouse/list";
        return fetch(url, params, "get");
    },
    //全屋方案公开状态
    fullhousestate(params) {
        const url = basePath.baseUrl + "/v1/fullHouse/secrecy";
        return fetch(url, params, "put");
    },
    //全屋方案 单个交付接口
    Onefullhousedeliver(params) {
        const url = basePath.baseUrl + "/v1/fullHouse/deliver";
        return fetch(url, params, "post");
    },
    //全屋方案 批量交付接口
    Allfullhousedeliver(params) {
        const url = basePath.baseUrl + "/v1/fullHouse/deliver/batch";
        return fetch(url, params, "post");
    },
    //全屋方案 上下架接口
    contentLibraryfullhouseShelf(params) {
        const url = basePath.baseUrl + "/v1/fullHouse/push";
        return fetch(url, params, "put");
    },
    //全屋查找单个 方案 详情接口
    getdetailplan(params) {
        // let url = basePath.baseUrl + `/v1/fullHouse/${params.planId}/${params.companyId}`;
        let url =
            basePath.baseUrl + `/v1/fullHouse/${params.planId}/${params.companyId}`;
        return fetch(url, {}, "get");
    },
    //全屋方案 编辑接口
    fullhouseEdite(params) {
        // let url = basePath.baseUrl + `/v1/fullHouse`;
        let url = basePath.baseUrl + `/v1/fullHouse`;
        return fetch(url, params, "put");
    },
    //全屋方案 风格列表接口
    getfullhouseStyleList(params) {
        let url = basePath.baseUrl + `/v1/fullHouse/styleList`;
        return fetch(url, params, "get");
    },
    //全屋方案  删除方案接口
    deletefullhouse(params) {
        let url = basePath.baseUrl + `/v1/fullHouse`;
        return fetch(url, params, "delete");
    },
    //全屋方案 分享列表 接口
    fullhouseSharePlanList(params) {
        let url = basePath.baseUrl + `/v1/fullHouse/share/list`;
        return fetch(url, params, "get");
    },
    //全屋方案 分享列表 使用按钮 拷贝接口
    fullhouseSharePlanCopy(params) {
        let url = basePath.baseUrl + `/v1/fullHouse/share/copy`;
        return fetch(url, params, "post");
    },
    //全屋方案 设计理念 富文本编辑 保存接口
    fullhouseEiditorSave(params) {
        let url = basePath.baseUrl + `/v1/fullHouse/config`;
        return fetch(url, params, "put");
    },
    //全屋方案 获取富文本编辑器 详情接口
    getfullhouseEditorInfo(params) {
        let url = basePath.baseUrl + `/v1/fullHouse/config/${params.id}`;
        return fetch(url, params, "get");
    },
    // 全屋方案组合 /v1/fullHouse/detail/{planId}/{companyId}
    wholeGroup(params) {
        let url =
            basePath.baseUrl +
            `/v1/fullHouse/detail/${params.planId}/${params.companyId}`;
        return fetch(url, params, "get");
    },
    // 店铺管理 方案管理  全屋方案列表 接口
    getShopFullhouseList(params) {
        let url = basePath.baseUrl + `/v1/storeplan/fullHouse/list`;
        return fetch(url, params, "get");
    },
    // 店铺管理 方案管理  发布 取消发布 全屋方案 接口
    shopFullhousePublish(params) {
        let url = basePath.baseUrl + `/v1/storeplan/fullHouse/publish`;
        return fetch(url, params, "post");
    },
    //店铺管理 方案管理 根据 id查找方案详情
    shopFullhouseDetail(params) {
        let url =
            basePath.baseUrl +
            `/v1/storeplan/fullHouse/${params.planId}/${params.companyId}`;

        return fetch(url, params, "get");
    },
    // 装修报价类型 /v1/dictionary/plan/decorate/type
    decoreatePrice(params) {
        let url = basePath.baseUrl + `/v1/dictionary/plan/decorate/type`;
        return fetch(url, params, "get");
    },
    //设置全屋方案售卖价格
    setFullHouseSalePrice(params) {
        let url = basePath.baseUrl + `/v1/fullHouse/editSalePrice`;
        return fetch(url, params, "post");
    },
    //设置全屋方案版权费用
    setFullHousePlanPrice(params) {
        let url = basePath.baseUrl + `/v1/fullHouse/editPlanPrice`;
        return fetch(url, params, "post");
    },
    //设置一键方案售卖价格
    setOneKeySalePrice(params) {
        let url = basePath.baseUrl + `/v1/designplan/onekey/share/editSalePrice`;
        return fetch(url, params, "post");
    },
    //设置一键方案版权费用
    setOneKeyPlanPrice(params) {
        let url = basePath.baseUrl + `/v1/designplan/onekey/share/editPlanPrice`;
        return fetch(url, params, "post");
    },
    //获取用户度币信息
    getUserDubiInfo(params) {
        let url = basePath.ucUrl + `/v1/dubi/getUserDubiInfo`;
        return fetch(url, params, "get");
    },
    //获取用户消费列表
    getUserDubiExpensesRecordList(params) {
        let url = basePath.ucUrl + `/v1/dubi/getUserDubiExpensesRecordList`;
        return fetch(url, params, "get");
    },
    //获取所有平台数据
    getPlatforms(params) {
        let url = basePath.ucUrl + `/v1/dubi/getPlatforms`;
        return fetch(url, params, "get");
    },
    //获取公司总收益
    getCompanyIncomeAggregated(params) {
        let url = basePath.ucUrl + `/v1/income/getCompanyAggregated`;
        return fetch(url, params, "get");
    },
    //获取收益记录列表
    getCompanyIncomeList(params) {
        let url = basePath.ucUrl + `/v1/income/companyIncomeList`;
        return fetch(url, params, "post");
    },
    //获取用户充值记录
    getUserRechargeRecord(params) {
        let url = basePath.ucUrl + `/v1/dubi/getUserRechargeRecord`;
        return fetch(url, params, "get");
    },
    //获取公司提现记录列表
    getCompanyWithdrawRecordList(params) {
        let url = basePath.ucUrl + `/v1/income/withdrawRecordList`;
        return fetch(url, params, "post");
    },
    //获取公司转出记录列表
    getCompanyTransferList(params) {
        let url = basePath.ucUrl + `/v1/income/transferRecordList`;
        return fetch(url, params, "post");
    },
    //用户提现度币
    UserWithdrawDubi(params) {
        let url = basePath.ucUrl + `/v1/income/withdrawDubi`;
        return fetch(url, params, "post");
    },
    //用户转出度币
    userTransferDubi(params) {
        let url = basePath.ucUrl + `/v1/income/transferDubi`;
        return fetch(url, params, "post");
    },
    //获取公司下的所有用户
    getCompanyUsers(params) {
        let url = basePath.ucUrl + `/v1/center/getUsers`;
        return fetch(url, params, "get");
    },
    //度币充值
    rechargeDubi(params) {
        let url = basePath.systemUrl + `/v1/merchantManagePay/rechargeDubi`;
        return fetch(url, params, "post");
    },
    //度币支付方案售卖费用
    batchDesignPlanSaleFee(params) {
        let url = basePath.systemUrl + `/v1/merchantManagePay/batchFee`;
        return fetch(url, params, "post");
    },
    //度币充值轮询接口
    rechargeCheckOut(params) {
        let url = basePath.systemUrl + `/v1/merchantManagePay/getOrder`;
        return fetch(url, params, "get");
    },
    //方案详情 预览图片接口
    reViewPlanImges(params) {
        let url = basePath.baseUrl + `/v1/designplan/getRederPic`;
        return fetch(url, params, "get");
    },
    // 砍价活动接口
    // 活动列表 basePath.systemUrl +
    activeList(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/getWxActBargainList`;
        //let url = basePath.testSystemUrl+`/v1/act/bargain/getWxActBargainList`
        return fetch(url, params, "post");
    },
    // 活动结束 /v1/act/bargain/finishWxActBargain
    activeTimeOut(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/finishWxActBargain`;
        return fetch(url, params, "post");
    },
    // 新增活动
    activeAdd(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/createWxActBargain`;
        return fetch(url, params, "post");
    },
    editActiveInfo(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/modifyWxActBargain`;
        return fetch(url, params, "post");
    },
    // 删除活动
    activeDelete(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/removeWxActBargain`;
        return fetch(url, params, "post");
    },
    //获取活动详情
    activeInfo(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/getWxActBargainDetails`;
        return fetch(url, params, "post");
    },
    // 用户数据
    activeUserData(params) {
        let url =
            basePath.systemUrl + `/v1/act/bargain/reg/getRegAnalyseResultList`;
        return fetch(url, params, "get");
    },
    // 发货编辑发货单
    activeSend(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/reg/modifyShipmentNo`;
        return fetch(url, params, "post");
    },
    // 发货单详情
    activeSendDetail(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/reg/getShipmentInfo`;
        return fetch(url, params, "get");
    },
    // 图表显示数据
    statisticalDataShow(params) {
        let url =
            basePath.systemUrl + `/v1/act/bargain/reg/getRegDashBoardResultList`;
        return fetch(url, params, "get");
    },
    //获取当前用户所在企业的小程信息
    getMiniProInfo(params) {
        let url = basePath.systemUrl + `/v1/act/bargain/getWxMiniProgramList`;
        return fetch(url, params, "post");
    },
    //获取领奖消息接口
    getActMsgList(params) {
        let url =
            basePath.systemUrl +
            `/v1/act/bargain/awardmsg/getWxActBargainAwardMsgList`;
        return fetch(url, params, "post");
    },
    //保存消息
    saveActMSg(params) {
        let url =
            basePath.systemUrl +
            `/v1/act/bargain/awardmsg/createWxActBargainAwardMsg`;
        return fetch(url, params, "post");
    },
    //修改消息
    editActMsg(params) {
        let url =
            basePath.systemUrl +
            `/v1/act/bargain/awardmsg/modifyWxActBargainAwardMsg`;
        //let url = basePath.testSystemUrl + `/v1/act/bargain/awardmsg/modifyWxActBargainAwardMsg`
        return fetch(url, params, "post");
    },
    //删除消息
    removeActMsg(params) {
        let url =
            basePath.systemUrl +
            `/v1/act/bargain/awardmsg/removeWxActBargainAwardMsg`;
        return fetch(url, params, "post");
    },
    //设置领取人数
    AddPersonals(params) {
        /*return axios.post(basePath.testSystemUrl + `/v1/act/bargain/setWxActBargainAwardMsg`, params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })*/
        let url = basePath.systemUrl + `/v1/act/bargain/setWxActBargainAwardMsg`;
        return fetch(url, params, "post");
    },
    // 线下门店列表
    listShopOffline(params) {
        const url = basePath.baseUrl + `/v1/companyshopoffline/business/list`;
        // const url = `http://localhost:32001/v1/companyshopoffline/business/list`;
        return fetch(url, params, "get");
    },
    getShopOffline(params) {
        const url = basePath.baseUrl + `/v1/companyshopoffline/get/` + params;
        // const url = `http://localhost:32001/v1/companyshopoffline/get/` + params;
        return fetch(url, params, "get");
    },
    claimShop(params) {
        const url = basePath.baseUrl + `/v1/companyshopoffline/claimShop/` + params;
        // const url = `http://localhost:32001/v1/companyshopoffline/claimShop/` + params;
        return fetch(url, params, "post");
    },

    getCompanyUser(params) {
        const url = basePath.systemUrl + `/v1/company/shop/companyUser`;
        return fetch(url, params, "get");
    },
    getProprietorInfoList(params) {
        const url = basePath.systemUrl + `/v1/proprietor/getInfoList`;
        return fetch(url, params, "get");
    },
    updateProcess(params) {
        const url = basePath.systemUrl + `/v1/proprietor/updateProcess`;
        return fetch(url, params, "post");
    },
    //拼团活动列表
    groupPurchaseActivityList(params) {
        const url = basePath.systemUrl + `/v1/group/purchase/list`;
        // const url = `http://localhost:32001/v1/companyshopoffline/business/list`;
        return fetch(url, params, "get");
    },
    //改变活动状态
    changeActivityStatus(sureOrder) {
        const url =
            basePath.systemUrl +
            `/v1/group/purchase/changeActivityStatus?id=${
        sureOrder.activityId
      }&activityStatus=${sureOrder.activityStatus}`;
        // const url =  basePath.systemUrl+`/v1/company/shop/list`;
        return fetch(url, sureOrder, "put");
    },
    //删除拼团活动
    deleteActivity(sureOrder) {
        const url = basePath.systemUrl + `/v1/group/purchase`;
        // const url =  basePath.systemUrl+`/v1/company/shop/list`;
        return fetch(url, sureOrder, "delete");
    },
    //拼团活动详情
    getActivityDetail(params) {
        const url =
            basePath.systemUrl + `/v1/group/purchase/getActivityDetail/${params.id}`;
        return fetch(url, "", "get");
    },
    //新增拼团
    addAssemActivity(params) {
        const url = basePath.systemUrl + `/v1/group/purchase`;
        return fetch(url, params, "post");
    },
    //获取上架小程序信息
    selectPutawayMiniProgram(params) {
        const url = basePath.systemUrl + `/v1/base/companyMiniProgramConfig/getMinProName`;
        return fetch(url, params, "get");
    },
    //修改拼团
    updateAssemActivity(params) {
        const url = basePath.systemUrl + `/v1/group/purchase`;
        return fetch(url, params, "put");
    },
    //查看开团信息
    getActivityOpen(params) {
        const url = basePath.systemUrl + `/v1/group/purchase/getActivityOpenDetail`;
        return fetch(url, params, "get");
    },
    getOpenDetailByOpenId(params) {
        const url = basePath.systemUrl + `/v1/group/purchase/getOpenDetailByOpenId`;
        return fetch(url, params, "get");
    },
    //获取sku信息
    getSkuDetail(params) {
        const url = basePath.systemUrl + `/v1/group/purchase/getActivitySku`;
        return fetch(url, params, "get");
    },
    //新增活动sku信息
    saveActivitySku(params) {
        const url = basePath.systemUrl + `/v1/group/purchase/saveActivitySku`;
        return fetch(url, params, "post");
    },
    //修改活动sku信息
    updateActivitySku(params) {
        const url = basePath.systemUrl + `/v1/group/purchase/updateActivitySku`;
        return fetch(url, params, "put");
    },
    //获取拼团sku信息
    getGroupGoodsDetail(params) {
        const url =
            basePath.systemUrl +
            `/v1/group/purchase/getActivitySkuDetail?purchaseActivityId=${
        params.purchaseActivityId
      }&spuId=${params.spuId}`;
        return fetch(url, "", "get");
    },
    checkCompanyShop(params) {
        const url = basePath.systemUrl + `/v1/company/shop/checkCompanyShop`;
        return fetch(url, params, "get");
    },
    exportProprietorExcel(params) {
        params.url = basePath.systemUrl + `/v1/proprietor/exportExcel`;
        this.exportExcel(params);
    },
    exportIncomeExcel(params) {
        // params.url = basePath.ucUrl + `/v1/income/export`;
        params.url = basePath.ucUrl + `/v1/income/export`;
        this.exportExcel(params);
    },
    exportExcel(params) {
        const url = params.url;
        params.url = null;
        axios({
            method: "get",
            url: url,
            params: params,
            data: params,
            withCredentials: true,
            responseType: "blob"
        }).then(
            res => {
                if (res && res.status == 200) {
                    let blob = new Blob([res.data], {
                        type: "application/vnd.ms-excel;charset=utf-8"
                    });
                    let url = URL.createObjectURL(blob);
                    let link = document.createElement("a");
                    link.style.display = "none";
                    link.href = url;
                    link.setAttribute("download", "excel.xls");
                    document.body.appendChild(link);
                    link.click();
                } else {
                    Message({ type: "error", message: "系统异常" });
                }
            },
            err => {
                Message({ type: "error", message: "系统异常" });
            }
        );
    },
    /**
     * 获取数据字典列表
     *
     * @author huangsongbo
     * @param type
     */
    getIssuingBankList(type) {
        var params = { type: type };
        let url = basePath.ucUrl + "/v1/sys/dictionary/getList";
        return fetch(url, params, "get");
    },
    /**
     * 保存银行卡
     *
     * @author huangsongbo
     * @param type
     */
    bankCardSave(params) {
        let url = basePath.ucUrl + "/v1/bankcard/create";
        return fetch(url, params, "post");
    },
    /**
     * 获取银行卡信息列表
     *
     * @author huangsongbo
     */
    getBankCardInfoList() {
        let params = {};
        let url = basePath.ucUrl + "/v1/bankcard/list";
        return fetch(url, params, "get");
    },
    /**
     * 删除指定银行卡信息
     *
     * @author huangsongbo
     */
    bankCardInfoDelete(id) {
        let params = { id: id };
        let url = basePath.ucUrl + "/v1/bankcard/delete";
        return fetch(url, params, "get");
    },
    /**
     * 是否允许提现
     *
     * @author huangsongbo
     */
    isAllowDrawings() {
        let params = {};
        let url = basePath.ucUrl + "/v1/income/isAllowDrawings";
        return fetch(url, params, "get");
    },
    /**
     * 检查是否还能创建银行卡
     * @author huangsongbo
     */
    getIsAllowCreate() {
        let params = {};
        let url = basePath.ucUrl + "/v1/bankcard/getIsAllowCreate";
        return fetch(url, params, "get");
    },

    /**----------客户问题反馈---------------**/
    //新增问题反馈
    feedbackAdd(params) {
        let url = basePath.systemUrl + `/v1/feedback/web/feedbackAdd`;
        return fetch(url, params, "post");
    },

    //我的反馈列表
    myFeedbackList(params) {
        let url = basePath.systemUrl + `/v1/feedback/web/admin/api/feedbackList`;
        return fetch(url, params, "get");
    },

    //是否存在未读回复
    isReadFeedback(params) {
        let url = basePath.systemUrl + `/v1/feedback/web/is/read/feedback`;
        return fetch(url, params, "get");
    },

    //反馈评价
    feedbackEstimate(params) {
        let url = basePath.systemUrl + `/v1/feedback/web/feedbackEstimate`;
        return fetch(url, params, "post");
    },
  productCeilingApplyArea() {
      const url =`/v1/dictionary/with/productCeilingApplyArea`;
      return fetch(url, null, 'get');
  },
  //电子统计信息列表接口
  getAllUserCardOfCompany(params,type) {
      if(type){
          params.curPage = undefined;
          params.pageSize = undefined; 
      }
      const url =basePath.coreUrl+`/v1/core/userCard/getAllUserCardOfCompany`;
      return fetch(url, params, 'post');
  },
  //电子统计信息公司接口
  getAllCompany() {
      //http://192.168.3.98:28008
      const url =basePath.coreUrl+`/v1/core/company/getAllCompany`;
      return fetch(url, '', 'get');
  },
  //导出电子统计信息接口
  excelExport(params) {
      //http://192.168.3.98:28008
      params.curPage = undefined;
      params.pageSize = undefined;
      const url = `http://192.168.3.98:28008/v1/core/userCard/excelExport`;
      return fetch(url, params, 'post');
  },
  //活动列表
  homeActiveList(params) {
      const url =`/v1/company/activeList`;
      return fetch(url, params, 'get');
  },
    
    //获取用户手机号码
    getUserMobile(params) {
        let url = basePath.systemUrl + `/v1/feedback/web/getUserMobile`;
        return fetch(url, params, "get");
    },
    dashboardconfig(params) {
        //首页管理banner 新品 爆款  http://192.168.2.11:32001
        const url = `/v1/company/dashboardconfig/${params.companyId}`;
        return fetch(url, "", "get");
    },
    createorupdate(params) {
        //保存首页管理banner 新品 爆款
        const url = `/v1/company/createorupdate/dashboard`;
        return fetch(url, params, "post");
    },
    listDesignPlanStyle() {
        const url = `/v1/product/base/style/plan/list`;
        return fetch(url, null, "get");
    },

    /**************************数据统计分析API Start*********************/
    //获取用户列表数据
    getUserAnalysisList(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/user/getList`;
        return fetch(url, params, "get");
    },
    getUserAnalysisAll(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/user/getAll`;
        return fetch(url, params, "get");
    },

    //获取用户汇总数据
    getUserAnalysisSum(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/user/getSum`;
        return fetch(url, params, "get");
    },
    //获取地区用户数据
    getAreaUserAnalysisList(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/user/getAreaList`;
        return fetch(url, params, "get");
    },
    //获取用户前十的地区
    getAreaUserTop10(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/user/getAreaTop10`;
        return fetch(url, params, "get");
    },

    //热门方案
    getHotDesignList(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/single/design/getList`;
        return fetch(url, params, "get");
    },
    //获取热门方案Top10
    getHotDesignTop10(params) {
        let url =
            basePath.systemUrl + `/v1/data/analysis/single/design/getDesignTop10`;
        return fetch(url, params, "get");
    },

    //获取方案使用数据
    getPlanAnalysisList(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/design/user/getList`;
        return fetch(url, params, "get");
    },

    getPlanAnalysisAll(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/design/user/getAll`;
        return fetch(url, params, "get");
    },

    //获取方案使用数据汇总
    getPlanAnalysisSum(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/design/user/getSum`;
        return fetch(url, params, "get");
    },

    //获取地区列表
    getAreaList(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/user/getCity`;
        return fetch(url, params, "get");
    },

    //获取商品浏览数据
    getProductAnalysisList(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/product/view/getList`;
        return fetch(url, params, "get");
    },
    //获取商品浏览前十
    getProductTop10Analysis(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/product/view/getTop10`;
        return fetch(url, params, "get");
    },

    //获取产品替换数据
    getProductReplaceList(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/product/replace/getList`;
        return fetch(url, params, "get");
    },

    getProductReplaceAll(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/product/replace/getAll`;
        return fetch(url, params, "get");
    },
    //获取商品汇总
    getProductReplaceSum(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/product/replace/getSum`;
        return fetch(url, params, "get");
    },

    //获取用户来渠道（源数）据
    getSourceAnalysisList(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/channel/getList`;
        return fetch(url, params, "get");
    },

    getSourceAnalysisSum(params) {
        let url = basePath.systemUrl + `/v1/data/analysis/channel/getSum`;
        return fetch(url, params, 'get');
    },
    syncGoods(params) {
        let url = `/v1/product/sync/goods/${params}`;
        return fetch(url, {}, 'get');
    },
    /**************************数据统计分析API end*********************/
    // 预约信息相关接口
    getAppointmentList(params) {
        let url = `/v1/appointment/list`;
        return fetch(url, params, "post");
    },
    // 更新编辑预约信息相关接口
    updateAppointment(params) {
        let url = `/v1/appointment/update`;
        return formPost('post', url, params);
    },
    // 删除预约信息相关接口
    removeAppointment(params) {
        let url = `/v1/appointment/remove`;
        return fetch(url, params, "get");
    },
    // 导出预约信息相关接口
    exportAppointment(params) {
        let url = `/v1/appointment/export`;
        return exportData(url, params, "post");
    },
    obtainDefaultNAVPicPath(params) { //导航图标默认接口
        let url = `/v1/company/obtainDefaultNAVPicPath`;
        return fetch(url, params, "get");
    },
    exportListExcelByGoods(params){//商品导出excel
        let url = `/v1/base/goods/exportGoodsSKUsData`
        return exportData(url, params, "get");
    }

}