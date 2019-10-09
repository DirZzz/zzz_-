package com.sandu.service.solution.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sandu.api.company.model.BaseCompany;
import com.sandu.api.company.model.Company;
import com.sandu.api.company.model.RichContext;
import com.sandu.api.company.service.CompanyService;
import com.sandu.api.solution.model.ConfigDetail;
import com.sandu.api.solution.model.DesignPlanRecommended;
import com.sandu.api.solution.model.MiniProgramDashboard;
import com.sandu.api.solution.model.MiniProgramDashboardConfig;
import com.sandu.api.solution.service.DesignPlanRecommendedService;
import com.sandu.api.solution.service.DesignPlanService;
import com.sandu.api.solution.service.MiniProgramDashboardService;
import com.sandu.service.solution.dao.MiniProgramDashboardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright (c) http://www.sanduspace.cn. All rights reserved.
 *
 * @author :  Steve
 * @date : 2018/12/18
 * @since : sandu_yun_1.0
 */
@Slf4j
@Service("miniProgramDashboardService")
public class MiniProgramDashboardServiceImpl implements MiniProgramDashboardService{

    @Autowired
    private MiniProgramDashboardMapper miniProgramDashboardMapper;

    @Autowired
    private CompanyService companyService;

    @Value("${nav.Home_01.url}")
    private String homeImage01;

    @Value("${nav.Home_02.url}")
    private String homeImage02;

    @Value("${nav.Home_03.url}")
    private String homeImage03;

    @Value("${nav.Home_04.url}")
    private String homeImage04;
    
    @Autowired
    private DesignPlanRecommendedService designPlanRecommendedService;

    @Override
    public Long createMiniProgramDashboard(MiniProgramDashboard dashboard) {
        miniProgramDashboardMapper.createMiniProgramDashboard(dashboard);
        return dashboard.getId();
    }

    @Override
    public int updateMiniProgramDashboard(MiniProgramDashboard dashboard) {
        return miniProgramDashboardMapper.updateMiniProgramDashboard(dashboard);
    }

    @Override
    public MiniProgramDashboard getMiniProgramDashboardByAppId(String appId) {
        return miniProgramDashboardMapper.getMiniProgramDashboardByAppId(appId);
    }

    @Override
    public boolean transformDataFormat() {

        //handle company rich context
//        this.handleCompanyRichContextData();

        //handle miniProgram index config
        this.handleMiniProgramDashboardOldData();

        return Boolean.TRUE;
    }

    private void handleCompanyRichContextData() {
        //加载所有的企业富文本信息
        List<RichContext> richContextList = companyService.findAllCompanyRichContext();

        List<Long> companyIds = richContextList.stream().map(RichContext::getBusinessId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(companyIds)){
            log.warn("find company by richContext businessId size is 0");
            return;
        }

        List<Company> companyList = companyService.findByIds(companyIds);

        Map<Long, List<Company>> groupCompany = companyList.stream().collect(Collectors.groupingBy(Company::getId));

        List<Company> updateList = new ArrayList<>();
        //更新base_company表中的品牌介绍字段
        for (RichContext rich : richContextList) {
            List<Company> companys = groupCompany.get(rich.getBusinessId());
            if (!CollectionUtils.isEmpty(companys)){
                Company company = companys.get(0);
                company.setCompanyIntroduce(company.getCompanyIntroduce() + rich.getContext());
                updateList.add(company);
            }
        }
        //批量更新
        companyService.updateBatch(updateList);
    }

    private void handleMiniProgramDashboardOldData() {
        //获取所有的小程序首页配置
        List<MiniProgramDashboard> allList = miniProgramDashboardMapper.findAll();

        Gson gson = new Gson();
        log.info("began to handle old data num of size=>{}" + allList.size());
        for (MiniProgramDashboard mpd: allList) {

            String configJson = mpd.getConfig();
//            Map<String, MiniProgramDashboardConfig> mapConfig = gson.fromJson(configJson, new TypeToken<Map<String, MiniProgramDashboardConfig>>() {
//            }.getType());
//            List<MiniProgramDashboardConfig> transformList = this.mapToList(mapConfig);
//            //将公司模块加载到list中,放在最后
//            this.obtainCompanyModuleInfo(transformList,mpd.getAppId());
//            mpd.setConfig(gson.toJson(transformList,new TypeToken<List<MiniProgramDashboardConfig>>(){}.getType()));

            List<MiniProgramDashboardConfig> configList = gson.fromJson(configJson,new TypeToken<List<MiniProgramDashboardConfig>>(){}.getType());

            //删除对应模块
//            configList =  configList.stream().filter(conf-> !Objects.equals(conf.getType() ,"homePageDialog")).collect(Collectors.toList());
//              configList =  configList.stream().filter(conf-> !"baseInfo".equals(conf.getType())).collect(Collectors.toList());
            //新增首页弹框设置模块
//            this.homePageDialogInfo(configList);

            //新增企业基本信息
            this.companyBaseInfo(configList);

            mpd.setConfig(gson.toJson(configList,new TypeToken<List<MiniProgramDashboardConfig>>(){}.getType()));
        }
        log.info("end to build old data num of size=>{}" + allList.size());

        //执行批量更新
        miniProgramDashboardMapper.updateBatch(allList);
    }

    /**
     * 将公司模块封装到list中
     * @param transformList
     * @param appId
     */
    private void obtainCompanyModuleInfo(List<MiniProgramDashboardConfig> transformList, String appId) {
        Long companyId = companyService.findConpanyIdByAppId(appId);

        if (null != companyId){
            Company company = companyService.getCompanyById(companyId);
            if (null != company){
                MiniProgramDashboardConfig config = new MiniProgramDashboardConfig();
                config.setType("company");
                config.setIsShowHome(1);
                config.setTitle(company.getIntroduceTitle());
                config.setRichContext(company.getCompanyIntroduce());
                transformList.add(config);
            }
        }
    }

    /**
     * 将首页弹框模块封装到list中
     * @param transformList
     */
    private void homePageDialogInfo(List<MiniProgramDashboardConfig> transformList) {
        MiniProgramDashboardConfig config = new MiniProgramDashboardConfig();
        config.setType("dialog");
        config.setIsShowHome(1);
        config.setTitle("绑定手机号");
        config.setSecondTitle("免费出装修效果图");
        config.setConfigDetails(Collections.EMPTY_LIST);
        transformList.add(2,config);
    }


    /**
     * 将企业基本信息模块封装到list中
     * @param transformList
     */
    private void companyBaseInfo(List<MiniProgramDashboardConfig> transformList) {
        MiniProgramDashboardConfig config = new MiniProgramDashboardConfig();
        config.setType("baseInfo");
        config.setConfigDetails(Collections.EMPTY_LIST);
        config.setTitle("");//标题
        config.setIsShowHome(0);//是否显示
        config.setSecondTitle("");//企业名称
        config.setMobile("");//联系电话
        config.setAddress("");//所在地区
        transformList.add(3,config);
    }

    /**
     * 转换数据结构 map -> list
     * @param mapConfig
     * @return
     */
    private List<MiniProgramDashboardConfig> mapToList(Map<String, MiniProgramDashboardConfig> mapConfig) {
        
        List<MiniProgramDashboardConfig> tansformList = new ArrayList<>();

        if (checkElementNotNull("bana",mapConfig)){
            this.setTypeAndAddToList("bana","banner",tansformList,mapConfig);
        }else{
            this.addMissModule("banner",tansformList);
        }

        this.setNavModule(tansformList);

        if (checkElementNotNull("plan",mapConfig)){
            this.setTypeAndAddToList("plan","plan",tansformList,mapConfig);
        }else{
            this.addMissModule("plan",tansformList);
        }

        //添加方案模块
        this.addMissModule("obtainMobile",tansformList);

        //活动模块
        this.addMissModule("act",tansformList);

        if (checkElementNotNull("baokuan",mapConfig)){
            this.setTypeAndAddToList("baokuan","hotRecommend",tansformList,mapConfig);
        }else{
            this.addMissModule("hotRecommend",tansformList);
        }

        if (checkElementNotNull("newkuan",mapConfig)){
            this.setTypeAndAddToList("newkuan","newRecommend",tansformList,mapConfig);
        }else{
            this.addMissModule("newRecommend",tansformList);
        }

        if (checkElementNotNull("moreGoods",mapConfig)){
            this.setTypeAndAddToList("moreGoods","moreGoods",tansformList,mapConfig);
        }else{
            this.addMissModule("moreGoods",tansformList);
        }
        return tansformList;
    }

    private void addMissModule(String title, List<MiniProgramDashboardConfig> transformList) {
        //如果不存在banner模块,新增一个banner模块
        MiniProgramDashboardConfig config = new MiniProgramDashboardConfig();
        config.setType(title);
        config.setConfigDetails(Collections.EMPTY_LIST);
        transformList.add(config);
    }

    private void setNavModule(List<MiniProgramDashboardConfig> tansformList) {
        //插入nva
        MiniProgramDashboardConfig navConfig = new MiniProgramDashboardConfig();
        navConfig.setType("nav");
        List<ConfigDetail> configDetails = new ArrayList<>();
        this.buildNavConfigDetail("品牌介绍",true,homeImage01,configDetails,1);
        this.buildNavConfigDetail("线下门店",true,homeImage02,configDetails,2);
        this.buildNavConfigDetail("领券中心",true,homeImage03,configDetails,3);
        this.buildNavConfigDetail("推荐方案",true,homeImage04,configDetails,4);
        navConfig.setConfigDetails(configDetails);
        tansformList.add(navConfig);
    }

    private void buildNavConfigDetail(String title, boolean b, String imageUrl, List<ConfigDetail> configDetails,Integer navValue) {
        ConfigDetail configDetail = new ConfigDetail();
        configDetail.setTitle(title);
        configDetail.setShow(b);
        configDetail.setImageUrl(imageUrl);
        configDetail.setNavValue(navValue);
        configDetails.add(configDetail);
    }

    /**
     * 设置模块类型,并添加到结果集中
     * @param moduleName
     * @param type
     * @param tansformList
     * @param mapConfig
     */
    private void setTypeAndAddToList(String moduleName, String type, List<MiniProgramDashboardConfig> tansformList, Map<String, MiniProgramDashboardConfig> mapConfig) {
        MiniProgramDashboardConfig module = mapConfig.get(moduleName);
        if (null != module){
            //更多模块需要特殊处理
            if ("moreGoods".equals(moduleName)){
                if (!CollectionUtils.isEmpty(module.getConfigList())){
                    for (MiniProgramDashboardConfig config : module.getConfigList()){
                        config.setType(type);
                        tansformList.add(config);
                    }
                    return;
                }else{
                   module.setType(type);
                   module.setConfigDetails(Collections.EMPTY_LIST);
                   tansformList.add(module);
                   return;
                }
            }

            if ("plan".equals(moduleName)){
                //如果是方案的,需要加载方案的面积、名称、风格
                List<ConfigDetail> configDetails = module.getConfigDetails();
                if (!CollectionUtils.isEmpty(configDetails)){
                    for(ConfigDetail detail : configDetails){
                        DesignPlanRecommended planRecommended = designPlanRecommendedService.findRecommendedInfoById(detail.getId());
                        detail.setDetailName(planRecommended.getPlanName());
                        detail.setDesignStyleName(planRecommended.getDesignStyleName());
                        detail.setSpaceArea(planRecommended.getApplySpaceAreas());
                    }
                }
            }
            module.setType(type);
            tansformList.add(module);
        }
    }

    /**
     * check element not null
     * @param moduleName
     * @param mapConfig
     * @return
     */
    private boolean checkElementNotNull(String moduleName, Map<String, MiniProgramDashboardConfig> mapConfig) {
        return Objects.nonNull(mapConfig.get(moduleName));
    }

    @Override
    public Map<String, Object> obtainDefaultNAVPicPath() {
        Map<String,Object> map = new HashMap<>();
        map.put("homeImage01",homeImage01);
        map.put("homeImage02",homeImage02);
        map.put("homeImage03",homeImage03);
        map.put("homeImage04",homeImage04);
        return map;
    }

    @Override
    public List<MiniProgramDashboardConfig> completionMiniProIndexData() {
        List<MiniProgramDashboardConfig> tansformList = new ArrayList<>();
        this.addMissModule("banner",tansformList);
        this.setNavModule(tansformList);
        homePageDialogInfo(tansformList);
        companyBaseInfo(tansformList);
        this.addMissModule("plan",tansformList);
        //添加方案模块
        this.addMissModule("obtainMobile",tansformList);
        //活动模块
        this.addMissModule("act",tansformList);
        this.addMissModule("baokuan",tansformList);
        this.addMissModule("newkuan",tansformList);
        this.addMissModule("moreGoods",tansformList);
//        this.addMissModule("baseInfo",tansformList);
        this.addMissModule("company",tansformList);

        return tansformList;
    }

    @Override
    public void restoreMiniProIndexData() {
        //获取所有的小程序首页配置
        List<MiniProgramDashboard> allList = miniProgramDashboardMapper.findAll();
        Gson gson = new Gson();
        log.info("began to handle old data num of size=>{}" + allList.size());
        for (MiniProgramDashboard mpd : allList) {

            String configJson = mpd.getConfig();

            List<MiniProgramDashboardConfig> configList = gson.fromJson(configJson, new TypeToken<List<MiniProgramDashboardConfig>>() {
            }.getType());

            //删除多余的baseInfo模块
            Iterator<MiniProgramDashboardConfig> iterator = configList.iterator();
            int index = 1;
            while (iterator.hasNext()) {
                if (Objects.equals(iterator.next().getType(), "baseInfo")) {
                    if (index > 1) {
                        iterator.remove();
                    }
                    index++;
                }
            }

            mpd.setConfig(gson.toJson(configList, new TypeToken<List<MiniProgramDashboardConfig>>() {
            }.getType()));
        }
        log.info("end to build old data num of size=>{}" + allList.size());

        //执行批量更新
        miniProgramDashboardMapper.updateBatch(allList);
    }

}
