package com.sandu.web.company.controller;

import com.github.pagehelper.PageInfo;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sandu.api.company.input.CompanyIntroduceUpdate;
import com.sandu.api.company.input.CompanyNew;
import com.sandu.api.company.input.CompanyShopQuery;
import com.sandu.api.company.input.CompanyUpdate;
import com.sandu.api.company.model.Company;
import com.sandu.api.company.model.CompanyMiniProgramConfig;
import com.sandu.api.company.model.RichContext;
import com.sandu.api.company.model.bo.CompanyBO;
import com.sandu.api.company.model.bo.CompanyIntroduceBO;
import com.sandu.api.company.model.bo.CompanyShopListBO;
import com.sandu.api.company.output.CompanyIntroduceVO;
import com.sandu.api.company.output.CompanyShopListVO;
import com.sandu.api.company.output.CompanyVO;
import com.sandu.api.company.service.CompanyService;
import com.sandu.api.company.service.biz.CompanyBizService;
import com.sandu.api.dictionary.model.Dictionary;
import com.sandu.api.dictionary.service.DictionaryService;
import com.sandu.api.solution.model.ConfigDetail;
import com.sandu.api.solution.model.MiniProObtainMobileDetail;
import com.sandu.api.solution.model.MiniProgramDashboard;
import com.sandu.api.solution.model.MiniProgramDashboardConfig;
import com.sandu.api.solution.service.DesignPlanRecommendedService;
import com.sandu.api.solution.service.MiniProObtainMobileDetailService;
import com.sandu.api.solution.service.MiniProUserAppointmentDetailService;
import com.sandu.api.solution.service.MiniProgramDashboardService;
import com.sandu.common.BaseController;
import com.sandu.common.LoginContext;
import com.sandu.common.LoginUser;
import com.sandu.common.ReturnData;
import com.sandu.constant.ResponseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sandu
 */
@Api(description = "企业", tags = "company")
@RestController
@RequestMapping("/v1/company")
@Log4j
public class CompanyController extends BaseController {

    private final Gson gson = new Gson();

    @Resource
    private CompanyService companyService;

    @Resource
    private CompanyBizService companyBizService;

    @Resource
    private MiniProgramDashboardService miniProgramDashboardService;

    @Resource
    private DesignPlanRecommendedService designPlanRecommendedService;

    @Resource
    private DictionaryService dictionaryService;

    @Resource
    private MiniProObtainMobileDetailService miniProObtainMobileDetailService;

    @Resource
    private MiniProUserAppointmentDetailService miniProUserAppointmentDetailService;

    @GetMapping("/{companyId}/info")
    @ApiOperation(value = "获取企业详情", response = CompanyVO.class)
    public ReturnData getCompanyById(@PathVariable("companyId") Integer companyId) {
        ReturnData data = ReturnData.builder();
        CompanyBO bo = companyBizService.getCompanyInfoById(companyId);
        if (bo == null) {
            return data.success(false).code(ResponseEnum.NOT_CONTENT).message("未查询到相关数据...");
        }
        CompanyVO vo = new CompanyVO();
        vo.setBrands(bo.getBrandNames());
        vo.setQq(bo.getCompanyCustomerQq());
        vo.setDomain(bo.getCompanyDomainName());
        vo.setId(bo.getId());
        vo.setLogo(bo.getLogoPath());
        vo.setName(bo.getCompanyName());
        vo.setCategoryNames(bo.getCategoryNames());
        return data.data(vo).code(ResponseEnum.SUCCESS).success(true);
    }

    @PutMapping("")
    @ApiOperation(value = "更新企业信息")
    public ReturnData updateCompany(
            @Valid @RequestBody CompanyUpdate companyUpdate, BindingResult validResult) {
        ReturnData data = ReturnData.builder();
        if (validResult.hasErrors()) {
            return processValidError(validResult, data);
        }
        boolean flag = companyBizService.updateCompanyInfo(companyUpdate);
        if (flag) {
            return data.success(true).code(ResponseEnum.SUCCESS);
        } else {
            return data.success(false).code(ResponseEnum.ERROR).message("更新失败...");
        }
    }

    // @PostMapping("")
    public ReturnData saveCompany(@Valid CompanyNew companyNew, BindingResult validResult) {
        ReturnData data = ReturnData.builder();
        if (validResult.hasErrors()) {
            return processValidError(validResult, data);
        }
        int i = companyService.saveCompany(new Company());
        if (i > 0) {
            return data.code(ResponseEnum.SUCCESS).success(true);
        }
        return data.success(false).code(ResponseEnum.ERROR).message("保存失败");
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有公司", response = CompanyVO.class)
    public ReturnData listCompany(Integer bizType) {
        List<Company> companies = companyService.findAll(bizType);
        List<CompanyVO> vos =
                companies
                        .stream()
                        .map(
                                company -> {
                                    CompanyVO companyVO = new CompanyVO();
                                    companyVO.setName(company.getCompanyName());
                                    companyVO.setId(company.getId());
                                    return companyVO;
                                })
                        .collect(Collectors.toList());
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(vos);
    }

    @GetMapping("/get/introduce")
    @ApiOperation(value = "获取品牌介绍", response = CompanyIntroduceVO.class)
    public ReturnData getCompanyIntroduce(Integer companyId) {
        CompanyIntroduceVO vo = new CompanyIntroduceVO();
        CompanyIntroduceBO bo = companyService.getCompanyIntroduce(companyId);
        RichContext search = new RichContext();
        search.setBusinessId(Long.valueOf(companyId));
        search.setBusinessType(Integer.valueOf(0));
        RichContext context = companyService.getRichContext(search);
        if (bo == null) {
            return ReturnData.builder().code(ResponseEnum.ERROR).message("获取品牌介绍失败");
        }
        if (context != null) {
            vo.setRichContext(context.getContext());
        }
        vo.setCompanyId(bo.getId());
        vo.setPicPath(bo.getCompanyPicPath());
        vo.setIntroduce(bo.getCompanyIntroduce());
        vo.setPicId(bo.getPicId());
        vo.setIntroduceTitle(bo.getIntroduceTitle());
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(vo);
    }

    @PutMapping("/update/introduce")
    @ApiOperation(value = "更新品牌介绍")
    public ReturnData updateCompanyIntroduce(
            @Validated @RequestBody CompanyIntroduceUpdate companyIntroduceUpdate,
            BindingResult bindingResult) {
        StringBuilder errMsg = new StringBuilder();
    /*if (introduce == null || "".equals(introduce))
        return ReturnData.builder().code(ResponseEnum.ERROR).message("传入参数为空");
    Gson gson = new Gson();
    CompanyIntroduceUpdate companyIntroduceUpdate = gson.fromJson(introduce,CompanyIntroduceUpdate.class);
    Set<ConstraintViolation<CompanyIntroduceUpdate>> results =
            Validation.buildDefaultValidatorFactory().getValidator().validate(companyIntroduceUpdate);
    if (results != null && results.size() > 0)
    {
        Iterator<ConstraintViolation<CompanyIntroduceUpdate>> iterator = results.iterator();
        for (;iterator.hasNext();)
        {
            ConstraintViolation<CompanyIntroduceUpdate> val = iterator.next();
            errMsg.append(val.getMessage());
        }
        return ReturnData.builder().code(ResponseEnum.ERROR).message(errMsg.toString());
    }*/
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError err : errors) {
                errMsg.append(err.getDefaultMessage() + "; ");
            }
            return ReturnData.builder().code(ResponseEnum.ERROR).message(errMsg.toString());
        }
        Integer num = companyService.updateCompanyIntroduceById(companyIntroduceUpdate);
        String context = companyIntroduceUpdate.getRichContext();
        //因为前段没有一个删除富文本的操作，想要删除只能把富文本设置为空串，所以加为null判断就好
        if (null != context) {
            RichContext richContextModel = new RichContext();
            richContextModel.setBusinessId(Long.valueOf(companyIntroduceUpdate.getCompanyId()));
            richContextModel.setBusinessType(Integer.valueOf(0));
            richContextModel.setContext(context);
            companyService.saveOrUpdateCompanyRichContext(richContextModel);
        }
        if (num == null || num == 0) {
            return ReturnData.builder().code(ResponseEnum.ERROR).message("更新品牌介绍失败");
        }
        return ReturnData.builder().code(ResponseEnum.SUCCESS).message("更新成功");
    }

    @GetMapping("shop/decorate")
    @ApiOperation(value = "获取派单内部推荐企业列表", response = CompanyShopListVO.class)
    public ReturnData listCompanyShop(
            @Validated CompanyShopQuery query,
            BindingResult bindingResult) {
        ReturnData data = ReturnData.builder();
        if (bindingResult.hasErrors()) {
            return processValidError(bindingResult, data);
        }

        PageInfo<CompanyShopListBO> list = companyBizService.listCompanyShop(query);

        List<CompanyShopListVO> result = list.getList().stream().map(it -> {
            CompanyShopListVO vo = new CompanyShopListVO();
            BeanUtils.copyProperties(it, vo);
            return vo;
        }).collect(Collectors.toList());
        return ReturnData.builder().code(ResponseEnum.SUCCESS).list(result).total(list.getTotal());
    }

    @GetMapping("shop/decorate/{id}")
    @ApiOperation(value = "获取客户以签约企业", response = CompanyShopListVO.class)
    public ReturnData listCompanyWithCustomerId(@PathVariable Integer id) {
        List<Company> data = companyBizService.listCompanyForCustomerOrder(id);
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(data);
    }


    @PostMapping("/createorupdate/dashboard")
    @ApiOperation(value = "Create or update the MINI-PROGRAM of dashboard config.")
    public ReturnData createOrUpdateMiniProgramDashboard(
            @RequestBody MiniProgramDashboard dashboard,
            BindingResult bindingResult) {
        StringBuilder errMsg = new StringBuilder();
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError err : errors) {
                errMsg.append(err.getDefaultMessage() + "; ");
            }
            return ReturnData.builder().code(ResponseEnum.ERROR).message(errMsg.toString());
        }

        try {
            CompanyMiniProgramConfig miniProgramConfig = companyService.getCompanyMiniConfigByCompanyId(dashboard.getCompanyId());
            if (miniProgramConfig == null || StringUtils.isEmpty(miniProgramConfig.getAppId())) {
                return ReturnData.builder().code(ResponseEnum.SUCCESS).message("获取小程序信息失败!");
            }
            dashboard.setAppId(miniProgramConfig.getAppId());
            LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
//        Map<String, MiniProgramDashboardConfig> configMap = dashboard.getConfigMap();
//        String configJson = gson.toJson(configMap, new TypeToken<Map<String, MiniProgramDashboardConfig>>() {
//        }.getType());
            List<MiniProgramDashboardConfig> list = dashboard.getConfigList();
            String configJson = gson.toJson(list);
            dashboard.setConfig(configJson);
            MiniProgramDashboard checkExists = miniProgramDashboardService.getMiniProgramDashboardByAppId(dashboard.getAppId());
            if (loginUser != null) {
                dashboard.setCreator(loginUser.getLoginName());
                dashboard.setModifier(loginUser.getLoginName());
            }

            /**
             * 处理获取用户手机号模块
             */
            this.handleObtainMobileModule(list,dashboard.getAppId(),loginUser);

            if (checkExists != null) {
                miniProgramDashboardService.updateMiniProgramDashboard(dashboard);
            } else {
                miniProgramDashboardService.createMiniProgramDashboard(dashboard);
            }
            return ReturnData.builder().code(ResponseEnum.SUCCESS).message("更新成功");
        } catch (Exception e) {
            log.error("update error",e);
            return ReturnData.builder().code(ResponseEnum.ERROR).message("更新异常");
        }
    }

    private void handleObtainMobileModule(List<MiniProgramDashboardConfig> list, String appid, LoginUser loginUser) {

        //获取该程序下所有的免费详情
        List<MiniProObtainMobileDetail> obtainMobileDetails = miniProObtainMobileDetailService.listByAppId(appid);

        List<MiniProObtainMobileDetail> insertList = new ArrayList<>();
        List<MiniProObtainMobileDetail> updateList = new ArrayList<>();
        for (MiniProgramDashboardConfig config : list){
            if ("obtainMobile".equals(config.getType())){
                if (StringUtils.isNotBlank(config.getUuid())){
                    //存在获取用户手机号的,
                    Optional<MiniProObtainMobileDetail> first = obtainMobileDetails.stream().filter(item -> Objects.equals(item.getUuid(), config.getUuid())).findFirst();
                    if (!CollectionUtils.isEmpty(obtainMobileDetails)
                            && first.isPresent()){
                        //如果已经存在,则做更新操作
                        //判断数据是否修改
                        first.ifPresent(detail ->{
                            //判断是否发生了变化
                            if (!Objects.equals(detail.getTitle(),config.getTitle()) && !Objects.equals(detail.getFreeNum(),config.getAmount())){
                                //两个数据都改变了
                                updateList.add(buildMiniProObtainMobileDetail(config,appid,loginUser,detail));
                            }else if (!Objects.equals(detail.getTitle(),config.getTitle())){
                                updateList.add(buildMiniProObtainMobileDetail(config,appid,loginUser,detail));
                            }else if (!Objects.equals(detail.getFreeNum(),config.getAmount())){
                                updateList.add(buildMiniProObtainMobileDetail(config,appid,loginUser,detail));
                            }
                        });
                    }else{
                        insertList.add(buildMiniProObtainMobileDetail(config,appid,loginUser,null));
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(insertList)) miniProObtainMobileDetailService.batchInsert(insertList);

        if (!CollectionUtils.isEmpty(updateList)) miniProObtainMobileDetailService.updateBatch(updateList);
    }

    private MiniProObtainMobileDetail buildMiniProObtainMobileDetail(MiniProgramDashboardConfig config, String appid, LoginUser loginUser, MiniProObtainMobileDetail detail) {
        Date now = new Date();
        int count = 0;
        if (null != detail && Objects.equals(1,detail.getIsLimit())){
            //计算已经领取的数量
            count = miniProUserAppointmentDetailService.countByObtainId(detail.getId());
            if (Objects.nonNull(config.getAmount())  && config.getAmount() - count <= 0){
                count = config.getAmount();
            }
        }
        return MiniProObtainMobileDetail.builder()
                .appId(appid)
                .freeNum(config.getAmount())
                .curRemainNum(Objects.isNull(config.getAmount()) ? null : config.getAmount() - count)
                .uuid(config.getUuid())
                .creator(null != loginUser  ? loginUser.getLoginName() : "system")
                .modifier(null != loginUser  ? loginUser.getLoginName() : "system")
                .gmtCreate(now)
                .gmtModified(now)
                .isLimit(Objects.isNull(config.getAmount()) ? 0 : 1)
                .isDeleted(0)
                .title(config.getTitle())
                .build();
    }

    @GetMapping("/dashboardconfig/{companyId}")
    @ApiOperation(value = "Get the dashboard config by appid", response = MiniProgramDashboard.class)
    public ReturnData getMiniProgramDashboardConfig(@PathVariable Integer companyId) {
        try {
            CompanyMiniProgramConfig miniProgramConfig = companyService.getCompanyMiniConfigByCompanyId(companyId);
            if (miniProgramConfig == null || StringUtils.isEmpty(miniProgramConfig.getAppId())) {
                return ReturnData.builder().code(ResponseEnum.SUCCESS).message("获取小程序信息失败!");
            }
            MiniProgramDashboard data = miniProgramDashboardService.getMiniProgramDashboardByAppId(miniProgramConfig.getAppId());
            // if(data == null) {
            //     return ReturnData.builder().code(ResponseEnum.SUCCESS).message("未获取到配置信息");
            // }
            List<MiniProgramDashboardConfig> resultList = null;
            if (null != data) {
                String configJson = data.getConfig();
                resultList = gson.fromJson(configJson,new TypeToken<List<MiniProgramDashboardConfig>>(){}.getType());
                MiniProgramDashboardConfig plan = null;
                for (MiniProgramDashboardConfig  config : resultList){
                    if (Objects.equals("plan",config.getType())){
                        plan = new MiniProgramDashboardConfig();
                        plan.setConfigDetails(config.getConfigDetails());
                    }
                }
                if (plan != null) {
                    List<ConfigDetail> plans = plan.getConfigDetails();
                    if (!CollectionUtils.isEmpty(plans)) {
                        Iterator<ConfigDetail> iterator = plans.iterator();
                        while (iterator.hasNext()) {
                            ConfigDetail detail=  iterator.next();
                            if (Objects.nonNull(detail.getId())){
                                if (designPlanRecommendedService.checkDesignPlanIsDeleted(detail.getId()) == 1) {
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }
    //            data.setConfigMap(list);
            } else {
    //            data = new MiniProgramDashboard();
                //小程序为首次配置,需要重新配置,塞数据
                resultList = miniProgramDashboardService.completionMiniProIndexData();
            }
//            data.setConfig(null);
//
//            CompanyIntroduceVO vo = new CompanyIntroduceVO();
//            CompanyIntroduceBO bo = companyService.getCompanyIntroduce(companyId);
//            RichContext search = new RichContext();
//            search.setBusinessId(Long.valueOf(companyId));
//            search.setBusinessType(Integer.valueOf(0));
//            RichContext context = companyService.getRichContext(search);
//            if (bo == null) {
//                return ReturnData.builder().code(ResponseEnum.ERROR).message("获取品牌介绍失败");
//            }
//            if (context != null) {
//                vo.setRichContext(context.getContext());
//            }
//            vo.setCompanyId(bo.getId());
//            vo.setPicPath(bo.getCompanyPicPath());
//            vo.setIntroduce(bo.getCompanyIntroduce());
//            vo.setPicId(bo.getPicId());
//            vo.setIntroduceTitle(bo.getIntroduceTitle());
//            data.setCompanyIntroduceVO(vo);
//
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("list",resultList);
            resultMap.put("appId",miniProgramConfig.getAppId());

            log.info("result list "+resultList);
            return ReturnData.builder().code(ResponseEnum.SUCCESS).data(resultMap);
        } catch (Exception e) {
            log.error("获取详情异常",e);
            return ReturnData.builder().code(ResponseEnum.ERROR).message("获取详情异常");
        }
    }

    @ApiOperation(value = "转换旧数据格式")
    @GetMapping(value = "/transformData")
    public ReturnData transformDataFormat(){
        try {
            boolean flag = miniProgramDashboardService.transformDataFormat();
            if (flag) {
                return ReturnData.builder().success(Boolean.TRUE).message("success to transform data");
            }
            return ReturnData.builder().success(Boolean.FALSE).message("fail to transform data");
        } catch (Exception e) {
            log.error("转换数据异常",e);
            return ReturnData.builder().success(Boolean.FALSE).message("error");
        }
    }

    /**
     * 修复数据
     * @return
     */
    @GetMapping(value = "/restore")
    public ReturnData restoreMiniProIndexData(){
        try {
            miniProgramDashboardService.restoreMiniProIndexData();
            return ReturnData.builder().success(Boolean.TRUE).message("success to restore data");
        } catch (Exception e) {
            log.error("修复数据异常",e);
            return ReturnData.builder().success(Boolean.FALSE).message("error");
        }
    }

    @GetMapping(value = "/activeList")
    public ReturnData obtainActiveList(){
        List<Dictionary> list = dictionaryService.listByType("activeList");
        List<Map<String,Object>> resultList = new ArrayList<>();
        for (Dictionary sd : list){
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("name",sd.getName());
            resultMap.put("value",sd.getValue());
            resultMap.put("miniProImage",sd.getAtt1());
            resultMap.put("adminImage",sd.getAtt2());
            resultList.add(resultMap);
        }
        return ReturnData.builder().code(ResponseEnum.SUCCESS).success(Boolean.TRUE).data(resultList);
    }

    @GetMapping(value = "/obtainDefaultNAVPicPath")
    public ReturnData obtainDefaultNAVPicPath(){
        Map<String,Object> resultMap = miniProgramDashboardService.obtainDefaultNAVPicPath();
        return ReturnData.builder().success(Boolean.TRUE).data(resultMap);
    }
}
