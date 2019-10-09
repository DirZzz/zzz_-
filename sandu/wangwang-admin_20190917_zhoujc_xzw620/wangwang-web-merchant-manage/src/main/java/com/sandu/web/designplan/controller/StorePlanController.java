package com.sandu.web.designplan.controller;

import com.github.pagehelper.PageInfo;
import com.sandu.api.company.service.CompanyShopService;
import com.sandu.api.queue.SyncMessage;
import com.sandu.api.queue.service.QueueService;
import com.sandu.api.shop.model.CompanyShop;
import com.sandu.api.solution.input.CompanyShopDesignPlanVo;
import com.sandu.api.solution.input.DesignPlanRecommendedQuery;
import com.sandu.api.solution.input.FullHouseDesignPlanQuery;
import com.sandu.api.solution.input.ManagerSXWPlanUpdate;
import com.sandu.api.solution.model.DesignPlanRecommended;
import com.sandu.api.solution.model.bo.DesignPlanBO;
import com.sandu.api.solution.model.bo.FullHouseDesignPlanBO;
import com.sandu.api.solution.output.DesignPlanVO;
import com.sandu.api.solution.output.FullHouseDesignPlanCoverPicInfoDTO;
import com.sandu.api.solution.output.FullHouseDesignPlanVO;
import com.sandu.api.solution.output.FullHousePlanVO;
import com.sandu.api.solution.service.DesignPlanRecommendedService;
import com.sandu.api.solution.service.FullHouseDesignPlanService;
import com.sandu.api.solution.service.biz.DesignPlanBizService;
import com.sandu.api.user.model.LoginUser;
import com.sandu.common.BaseController;
import com.sandu.common.LoginContext;
import com.sandu.common.ReturnData;
import com.sandu.commons.ListCopyUtils;
import com.sandu.constant.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sandu.constant.ResponseEnum.*;
import static com.sandu.util.Commoner.isEmpty;
import static com.sandu.util.Commoner.isNotEmpty;

/**
 * @Author: YuXingchi
 * @Description:
 * @Date: Created in 15:33 2018/8/8
 */

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/v1/storeplan")
@Api(tags = "storePlanManage", description = "店铺方案管理")
@Slf4j
public class StorePlanController extends BaseController {

    @Resource
    private DesignPlanRecommendedService designPlanRecommendedService;

    @Resource
    private DesignPlanBizService designPlanBizService;

    @Resource
    private FullHouseDesignPlanService fullHouseDesignPlanService;

    @Resource
    private QueueService queueService;

    @Autowired
    private CompanyShopService companyShopService;


    @ApiOperation(value = "一键方案列表", response = DesignPlanVO.class)
    @GetMapping("/onekey/list")
    public ReturnData listOneKeyDesignPlan(DesignPlanRecommendedQuery query) {
        PageInfo<DesignPlanBO> datas = designPlanBizService.onkeyPlanStoreList(query);
        if (isEmpty(datas.getList())) {
            return ReturnData.builder().code(NOT_CONTENT).message("没有数据了");
        }
        List<DesignPlanVO> vos = datas.getList().stream().map(it -> new DesignPlanVO()).collect(Collectors.toList());
        ListCopyUtils.copyListProps(datas.getList(), vos);
        PageInfo<DesignPlanVO> voPages = new PageInfo<>(vos);
        voPages.setTotal(datas.getTotal());
        return ReturnData.builder().data(voPages).code(SUCCESS).message("成功");
    }

    @ApiOperation(value = "普通方案列表", response = DesignPlanVO.class)
    @GetMapping("/common/list")
    public ReturnData listCommonDesignPlan(DesignPlanRecommendedQuery query) {
        PageInfo<DesignPlanBO> datas = designPlanBizService.commonPlanStoreList(query);
        if (isEmpty(datas.getList())) {
            return ReturnData.builder().code(NOT_CONTENT).message("没有数据了");
        }
        List<DesignPlanVO> vos = datas.getList().stream().map(it -> new DesignPlanVO()).collect(Collectors.toList());
        ListCopyUtils.copyListProps(datas.getList(), vos);
        PageInfo<DesignPlanVO> voPages = new PageInfo<>(vos);
        voPages.setTotal(datas.getTotal());
        return ReturnData.builder().data(voPages).code(SUCCESS).message("成功");
    }

    @ApiOperation(value = "根据id获取一键方案", response = DesignPlanVO.class)
    @GetMapping("/{planId}/{companyId}")
    public ReturnData getDesignPlan(@PathVariable long planId, @PathVariable Integer companyId) {
        List<DesignPlanBO> designPlanBOs = designPlanRecommendedService.getBaseInfos(planId, companyId);
        if (designPlanBOs != null && designPlanBOs.size() > 0) {
            List<DesignPlanVO> vos = designPlanBOs.stream().map(it -> new DesignPlanVO()).collect(Collectors.toList());
            ListCopyUtils.copyListProps(designPlanBOs, vos);
            return ReturnData.builder().data(vos).code(SUCCESS).message("成功");
        }
        return ReturnData.builder().code(NOT_CONTENT).message("没有数据了");
    }

    @ApiOperation(value = "发布或取消发布一键方案或普通方案")
    @PostMapping("/plan/publishPlan")
    public ReturnData publishDesignPlan(@RequestBody CompanyShopDesignPlanVo companyShopDesignPlanVo) {
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
//        loginUser = new LoginUser();
//        loginUser.setId(-1);
        if (null == loginUser || loginUser.getId() == 0) {
            return ReturnData.builder().code(UNAUTHORIZED).message("请先登录");
        }
        if (null == companyShopDesignPlanVo || companyShopDesignPlanVo.getCompanyShopDesignPlanAddList().isEmpty()) {
            return ReturnData.builder().code(PARAM_ERROR).message("参数缺失");
        }

        List<Integer> planIds = new ArrayList<>();
        companyShopDesignPlanVo.getCompanyShopDesignPlanAddList().forEach(list ->
                planIds.add(list.getPlanId())
        );

        Integer count = 0;
        //取消发布或发布
        if (companyShopDesignPlanVo.getIsContainsId() == 1 && companyShopDesignPlanVo.getIsDeleted() == 1) {
            count = designPlanRecommendedService.updateCompanyShopDesignPlan(companyShopDesignPlanVo.getCompanyShopDesignPlanAddList(),
                    loginUser.getName(), companyShopDesignPlanVo.getIsDeleted(),companyShopDesignPlanVo.getShopType());


            // xxx : 对于没有上架到如何店铺的方案，制空shop_in_720_page
            // create by zhoujc at  2019/3/9 16:33.
            Map<Integer, String> id2PushShopIds = designPlanRecommendedService.getPushStateByPlanIds(planIds);
            List<Integer> pushDownIds = id2PushShopIds.entrySet().stream()
                    .peek(en -> {
                        DesignPlanRecommended plan = designPlanRecommendedService.getDesignPlanRecommended(en.getKey().longValue());

                        DesignPlanRecommended tmp = new DesignPlanRecommended();
                        tmp.setId(en.getKey().longValue());
						if (StringUtils.isEmpty(en.getValue())) {
							tmp.setShowInSXWFlag(0);
							tmp.setShopIn720Page(0);
						}
                        if (plan.getShopIn720Page() != 0 && !en.getValue().contains(plan.getShopIn720Page() + "")) {
							tmp.setShopIn720Page(0);
						}
                        designPlanRecommendedService.save(tmp);
                    })
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            designPlanRecommendedService.deleteDesignPlanRecommendedSuperiorByPlanIds(pushDownIds, 0);


            sycMessageDoSend(SyncMessage.ACTION_DELETE, planIds,
                    SyncMessage.PLAN_TYPE_RECOMMENDED);
            if (null != count && count > 0) {
                return ReturnData.builder().data(count).code(SUCCESS).message("取消发布");
            }
            //新增发布
        } else if (companyShopDesignPlanVo.getIsDeleted() == 0 && companyShopDesignPlanVo.getIsContainsId() == 0) {
            count = designPlanRecommendedService.addCompanyShopDesignPlan(companyShopDesignPlanVo.getCompanyShopDesignPlanAddList(),
                    loginUser.getName(),companyShopDesignPlanVo.getShopType());

            Integer shopId = companyShopDesignPlanVo.getCompanyShopDesignPlanAddList().get(0).getShopId();
            planIds.forEach(
                    it -> {
                        DesignPlanRecommended designPlanRecommended = designPlanRecommendedService.getDesignPlanRecommended(it);
                        if (designPlanRecommended.getShopIn720Page() == 0) {
                            Integer id = designPlanRecommendedService.getFirstShopId(designPlanRecommended.getId());
                            if (id == null) {
                                id = shopId;
                            }
                            designPlanRecommended.setShopIn720Page(id);
                        }
                        designPlanRecommendedService.save(designPlanRecommended);
                    }
            );
            sycMessageDoSend(SyncMessage.ACTION_ADD, planIds,
                    SyncMessage.PLAN_TYPE_RECOMMENDED);
            if (null != count && count > 0) {
                return ReturnData.builder().data(count).code(SUCCESS).message("成功发布");
            }
        } else if (companyShopDesignPlanVo.getIsContainsId() == 1 && companyShopDesignPlanVo.getIsDeleted() == 0) {
            count = designPlanRecommendedService.addCompanyShopDesignPlan(companyShopDesignPlanVo.getCompanyShopDesignPlanAddList(),
                    loginUser.getName(),companyShopDesignPlanVo.getShopType());
            sycMessageDoSend(SyncMessage.ACTION_ADD, planIds,
                    SyncMessage.PLAN_TYPE_RECOMMENDED);
            if (null != count && count > 0) {
                return ReturnData.builder().data(count).code(SUCCESS).message("成功发布");
            }
        }

        return ReturnData.builder().code(NOT_CONTENT).message("没有数据了");
    }

    @ApiOperation(value = "全屋方案列表", response = FullHousePlanVO.class)
    @GetMapping("/fullHouse/list")
    public ReturnData listFullHousePlan(FullHouseDesignPlanQuery query) {
        PageInfo<FullHouseDesignPlanBO> datas = fullHouseDesignPlanService.selectStoreFullHousePlan(query);

        if (isEmpty(datas.getList())) {
            return ReturnData.builder().code(NOT_CONTENT).message("没有数据了");
        }
        List<FullHouseDesignPlanVO> vos = datas.getList().stream().map(it -> new FullHouseDesignPlanVO()).collect(Collectors.toList());
        ListCopyUtils.copyListProps(datas.getList(), vos);
        List<FullHousePlanVO> results = new ArrayList<>(vos.size());
        for (FullHouseDesignPlanVO fullHouseVO : vos) {
            FullHousePlanVO vo = convertBean(fullHouseVO);
            results.add(vo);
        }
        PageInfo<FullHousePlanVO> voPages = new PageInfo<>(results);
        voPages.setTotal(datas.getTotal());
        return ReturnData.builder().data(voPages).code(SUCCESS).message("成功");
    }

    @ApiOperation(value = "根据id获取全屋方案", response = FullHousePlanVO.class)
    @GetMapping("/fullHouse/{planId}/{companyId}")
    public ReturnData getFullHousePlan(@PathVariable Integer planId, @PathVariable Integer companyId) {
        FullHouseDesignPlanBO designPlanBO = fullHouseDesignPlanService.getBaseInfo(planId, companyId);
        if (isNotEmpty(designPlanBO)) {
            FullHouseDesignPlanVO vo = new FullHouseDesignPlanVO();
            BeanUtils.copyProperties(designPlanBO, vo);
            FullHousePlanVO planVo = convertBean(vo);
            return ReturnData.builder().data(planVo).code(SUCCESS);
        }
        return ReturnData.builder().code(NOT_FOUND);
    }

    @ApiOperation(value = "根据全屋方案id获取组合方案信息", response = DesignPlanVO.class)
    @GetMapping("/detail/{planId}/{companyId}")
    public ReturnData getDetailDesignPlan(@PathVariable Integer planId, @PathVariable Integer companyId) {
        List<DesignPlanBO> designPlanBOList = fullHouseDesignPlanService.getDetailDesignPlan(planId, companyId);
        if (designPlanBOList != null && designPlanBOList.size() > 0) {
            List<DesignPlanVO> vos = designPlanBOList.stream().map(it -> new DesignPlanVO()).collect(Collectors.toList());
            ListCopyUtils.copyListProps(designPlanBOList, vos);
            return ReturnData.builder().data(vos).code(SUCCESS);
        }
        return ReturnData.builder().code(NOT_FOUND);
    }

    @ApiOperation(value = "发布或取消发布全屋方案")
    @PostMapping("/fullHouse/publish")
    public ReturnData publishFullHousePlan(@RequestBody CompanyShopDesignPlanVo companyShopDesignPlanVo) {

        List<Integer> planIds = new ArrayList<>();
        companyShopDesignPlanVo.getCompanyShopDesignPlanAddList().forEach(list ->
                planIds.add(list.getPlanId())
        );

        Integer count;

        try {
            if (companyShopDesignPlanVo.getIsContainsId() == 1) {
                //取消发布
                count = fullHouseDesignPlanService.cancelPublish(companyShopDesignPlanVo.getCompanyShopDesignPlanAddList());
                sycMessageDoSend(SyncMessage.ACTION_DELETE, planIds,
                        SyncMessage.PLAN_TYPE_FULLHOUSE);
            } else {
                //发布
                count = fullHouseDesignPlanService.publish(companyShopDesignPlanVo.getCompanyShopDesignPlanAddList());
                sycMessageDoSend(SyncMessage.ACTION_ADD, planIds,
                        SyncMessage.PLAN_TYPE_FULLHOUSE);
            }
            if (count > 0) {
                return ReturnData.builder().code(SUCCESS).message("成功");
            }
        } catch (Exception e) {
            log.error("发布异常",e);
        }
        return ReturnData.builder().code(ERROR).message("失败");
    }


    @PostMapping("manager/edit")
    public ReturnData managerPlanInSXW(@RequestBody ManagerSXWPlanUpdate update) {
        ReturnData<Boolean> booleanReturnData = this.doResult(designPlanBizService::editPlanForManager, update);
        if (booleanReturnData.getSuccess()) {
            sycMessageDoSend(SyncMessage.ACTION_UPDATE, Collections.singletonList(update.getPlanId()),
                    SyncMessage.PLAN_TYPE_RECOMMENDED);
        }
        return booleanReturnData;
    }

    @GetMapping("manager/shop/list/{planId}/{planType}")
    public ReturnData listShopByPlanId(@PathVariable("planId") Integer planId,@PathVariable("planType") Integer planType) {
        // xxx : 获取此方案发布的所有店铺
        // create by zhoujc at  2019/2/25 9:52.
        try {
            List<CompanyShop> companyShops = companyShopService.listCompanyByPlanId(planId, planType);
            return ReturnData.builder().success(true).list(companyShops);
        } catch (Exception e) {
            log.error("获取方案店铺名称列表异常",e);
        }
        return ReturnData.builder().success(false).message("获取方案店铺名称列表异常");
        //return this.doResult(companyShopService::listCompanyByPlanId, planId,planType);

    }

    @PostMapping(value = "/manager/fullHouse/plan")
    public ReturnData managerSxwFullHousePlan(@RequestBody FullHouseDesignPlanQuery query){

        try {
            PageInfo<DesignPlanBO> pageInfo =  fullHouseDesignPlanService.findManagerSXWFullHouseDesign(query);
            return ReturnData.builder().success(true).total(pageInfo.getTotal()).data(pageInfo.getList());
        } catch (Exception e) {
            log.error("获取随选网全屋方案管理列表异常",e);
            return ReturnData.builder().success(false).message("获取随选网全屋方案管理列表异常");
        }
    }


    @PostMapping(value = "/manager/fullHouse/modify")
    public ReturnData managerFullHouseDesignPlanModify(@RequestBody ManagerSXWPlanUpdate update) {
        if (null == update) {
            return ReturnData.builder().success(false).message("param planId is not null!!!");
        }

        try {
            boolean modifyFlag = fullHouseDesignPlanService.modifyPlanForManager(update);
            if (modifyFlag) {
                if (modifyFlag) {
                    sycMessageDoSend(SyncMessage.ACTION_UPDATE, Collections.singletonList(update.getPlanId()),
                            SyncMessage.PLAN_TYPE_FULLHOUSE);
                }
                return ReturnData.builder().success(true).message("修改成功");
            }
        } catch (IllegalStateException ill) {
            return ReturnData.builder().success(false).message(ill.getMessage());
        } catch (Exception e) {
            log.error("修改异常", e);
        }
        return ReturnData.builder().success(false).message("修改失败");
    }

    /**
     * <p>获取全屋推荐方案的所有效果图
     * <p>备注 by huangsongbo 2019.08.30:
     * <p>此接口不能适应于现在的业务, 现在的业务需要取每个推荐方案封面图(此接口取得是所有照片级缩略图) + 空间类型名称(此接口没有)
     * <p>替代此方法的方法: {@link StorePlanController#getCoverPicInfo}
     * 
     * @author qiutuqiang
     * @param fullHousePlanId
     * @return
     */
    @Deprecated
    @GetMapping(value = "/subPhotoLevelCoverPics")
    public Object getSubPhotoLevelCoverPics(Integer fullHousePlanId){
        try {
            Assert.notNull(fullHousePlanId,"fullHousePlanId is not null");
            return ReturnData.builder().success(true).data(fullHouseDesignPlanService.fetchSubPlanPhotoLevelCoverPics(fullHousePlanId));
        }catch (IllegalArgumentException ill){
            return ReturnData.builder().success(false).message(ill.getMessage());
        }catch (Exception e) {
            return ReturnData.builder().success(false).message("获取照片级封面图异常");
        }
    }

    /**
     * 获取一个全屋推荐方案下所有的推荐方案的封面图(供管理员选择封面图在随选网显示)
     * 
     * @author huangsongbo 2019.08.30
     * @param fullHousePlanId full_house_design_plan.id
     * @return List<FullHouseDesignPlanCoverPicInfoDTO>
     */
    @GetMapping(value = "/getCoverPicInfoList")
    public Object getCoverPicInfoList(Long fullHousePlanId) {
    	String errorMessage = "获取封面图列表失败";
    	
    	try {
    		Assert.notNull(fullHousePlanId, "参数\"fullHousePlanId\"不能为空");
    		List<FullHouseDesignPlanCoverPicInfoDTO> returnList = fullHouseDesignPlanService.getFullHouseDesignPlanCoverPicInfoDTO(fullHousePlanId);
    		return ReturnData.builder().success(true).total(returnList == null ? 0L : returnList.size()).list(returnList);
    	} catch (Exception e) {
    		return ReturnData.builder().success(false).message(e.getMessage() + ", " + errorMessage);
		}
    }
    
    @GetMapping(value = "/modifyFullHousePlanCoverPics")
    public Object modifyFullHousePlanCoverPics(Integer fullHousePlanId,String coverPicIds){
        try {
            Assert.notNull(fullHousePlanId,"fullHousePlanId is not null");
            Assert.hasLength(coverPicIds,"coverPicIds is not null");
            fullHouseDesignPlanService.modifyFullHousePlanCoverPics(fullHousePlanId,coverPicIds);
            return ReturnData.builder().success(true).message("修改成功!");
        }catch (IllegalArgumentException ill){
            return ReturnData.builder().success(false).message(ill.getMessage());
        }catch (Exception e) {
            return ReturnData.builder().success(false).message("修改全屋方案封面图异常");
        }
    }

    private <P, R> ReturnData<R> doResult(Function<P, R> func, P param) {
        ReturnData result = ReturnData.builder().success(true);
        try {
            R apply = func.apply(param);
            if (apply instanceof List) {
                List list = (List) apply;
                result.list(list);
            } else {
                result.data(apply);
            }
            return result;
        } catch (IllegalStateException biz) {
            return ReturnData.builder().success(false).message(biz.getMessage());
        } catch (Exception e) {
            log.error("系统错误", e.getMessage());
            return ReturnData.builder().success(false).message("系统错误");
        }
    }

    /**
     * 数据转换
     *
     * @return
     */
    private FullHousePlanVO convertBean(FullHouseDesignPlanVO fullHouseVO) {
        FullHousePlanVO vo = new FullHousePlanVO();
        vo.setPlanId(fullHouseVO.getId());
        vo.setUuid(fullHouseVO.getUuid());//全屋方案返回UUid
        vo.setPlanCode(fullHouseVO.getPlanCode());
        vo.setPlanName(fullHouseVO.getPlanName());
        vo.setIsPublish(fullHouseVO.getIsPublish());
        vo.setPicId(fullHouseVO.getPlanPicId());
        vo.setPicPath(fullHouseVO.getPlanPicPath());
        vo.setBrandId(fullHouseVO.getBrandId());
        vo.setBrandName(fullHouseVO.getBrandName());
        vo.setCompanyId(fullHouseVO.getCompanyId());
        vo.setDeliverStatus(fullHouseVO.getDeliverStatus());
        vo.setSourceType(fullHouseVO.getSourceType());
        vo.setOrigin(fullHouseVO.getSourceName());
        vo.setDesignStyleId(fullHouseVO.getPlanStyleId());
        vo.setDesignStyleName(fullHouseVO.getPlanStyleName());
        vo.setCompleteDate(fullHouseVO.getGmtCreate());
        vo.setDesignerId(fullHouseVO.getUserId());
        vo.setDesigner(fullHouseVO.getUserName());
        vo.setRemark(fullHouseVO.getRemark());
        vo.setPlanDesc(fullHouseVO.getPlanDescribe());
        vo.setDecoratePriceInfoList(fullHouseVO.getDecoratePriceInfoList());
        vo.setChargeType(fullHouseVO.getChargeType());
        vo.setPlanPrice(fullHouseVO.getPlanPrice());
        return vo;
    }

    private void sycMessageDoSend(Integer messageAction, List<Integer> ids, Integer planType) {
        List<Map> content = ids.stream().map(item -> {
            HashMap<String, Integer> tmp = new HashMap<>(1);
            tmp.put("id", item);
            tmp.put("planTableType", planType);
            return tmp;
        }).collect(Collectors.toList());
        SyncMessage message = new SyncMessage();
        message.setAction(messageAction);
        message.setMessageId("P-" + System.currentTimeMillis());
        message.setModule(SyncMessage.MODULE_SOLUTION_RECOMMEND);
        message.setPlatformType(Constants.PLATFORM_CODE_MERCHANT_MANAGE);
        message.setObject(content);
        queueService.send(message);
    }

}
