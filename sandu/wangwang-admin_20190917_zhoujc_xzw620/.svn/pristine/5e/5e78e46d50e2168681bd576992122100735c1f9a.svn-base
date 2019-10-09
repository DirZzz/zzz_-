package com.sandu.service.companyshop.impl.biz;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.sandu.api.company.model.bo.CompanyShopListBO;
import com.sandu.api.company.service.CompanyShopService;
import com.sandu.api.companyshop.input.CompanyshopAdd;
import com.sandu.api.companyshop.input.CompanyshopQuery;
import com.sandu.api.companyshop.input.CompanyshopUpdate;
import com.sandu.api.companyshop.model.Companyshop;
import com.sandu.api.companyshop.service.CompanyshopService;
import com.sandu.api.companyshop.service.biz.CompanyshopBizService;
import com.sandu.config.SchedulingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * store_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-Oct-22 16:51
 */
@Slf4j
@Service("companyshopBizService")
public class CompanyshopBizServiceImpl implements CompanyshopBizService {

    @Autowired
    private CompanyshopService companyshopService;

    @Resource(name = "companyShopServiceImpl")
    private CompanyShopService shopService;

//    @Autowired
    private SchedulingConfig schedulingConfig;

    @Override
    public int create(CompanyshopAdd input) {

        Companyshop companyshop = new Companyshop();
        BeanUtils.copyProperties(input, companyshop);

        return companyshopService.insert(companyshop);
    }

    @Override
    public int update(CompanyshopUpdate input) {
        Companyshop companyshop = new Companyshop();

        BeanUtils.copyProperties(input, companyshop);
        //转换原字段ID
//        companyshop.setId(input.getCompanyshopId());
        return companyshopService.update(companyshop);
    }

    @Override
    public int delete(String companyshopId) {
        if (Strings.isNullOrEmpty(companyshopId)) {
            return 0;
        }

        Set<Integer> companyshopIds = new HashSet<>();
        List<String> list = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(Strings.nullToEmpty(companyshopId));
        list.stream().forEach(id -> companyshopIds.add(Integer.valueOf(id)));

        if (companyshopIds.size() == 0) {
            return 0;
        }
        return companyshopService.delete(companyshopIds);
    }

    @Override
    public Companyshop getById(int companyshopId) {
        return companyshopService.getById(companyshopId);
    }

    @Override
    public PageInfo<Companyshop> query(CompanyshopQuery query) {

        return companyshopService.findAll(query);
    }

    @Override
    public int companyshopToTop(String shopId, String topId) {
        return companyshopService.companyshopToTop(shopId,topId);
    }

    @Override
    public int companyshopToRefresh(String companyshopId,String topId) {
        return companyshopService.companyshopToRefresh(companyshopId,topId);
    }

    @Override
    public int modifyEnableScore(Integer enableScore, Double handScore, Integer shopId) {
        return companyshopService.modifyEnableScore(enableScore,handScore,shopId);
    }

    @Override
    public void run() {
        List<CompanyShopListBO> handleList = shopService.findAll();

        List<CompanyShopListBO> updateList = new ArrayList<>(handleList.size());
        for (CompanyShopListBO shop : handleList){

            //计算店铺基本信息分数
            double baseScore = totalShopBaseScore(shop);

            //计算店铺发布方案分数
            double planScore = totalShopPlanScore(shop.getId());

            //计算案列分数
            double projectCaseScore = totalProjectCaseScore(shop.getId());

            //计算博文分数苏
            double shopArticleScore = totalShopArticleScore(shop);

            Double automateScore = KeepTwoDecimals(baseScore + planScore + projectCaseScore + shopArticleScore);

            shop.setAutomateScore(automateScore > 3.50 ? automateScore : 3.50);

            updateList.add(shop);
        }

        if (!CollectionUtils.isEmpty(updateList)) shopService.updateBatch(updateList);
    }

    private Double KeepTwoDecimals(double v) {
        DecimalFormat r=new DecimalFormat();
        r.applyPattern("#0.00");//保留小数位数，不足会补零
        return new Double(r.format(v)).doubleValue();
    }

    private double totalShopArticleScore(CompanyShopListBO shop) {
        //如果是经销商直接返回5分,不要问为什么,这个需求就是这么随便
        if (Objects.equals(shop.getBusinessType(),2)){
            return 5d * 0.1;
        }

        int totalCount = shopService.totalShopArticle(shop.getId());
        return obtainIntervalScore(totalCount,0.1);
    }

    private double totalProjectCaseScore(Integer shopId) {
        int totalCount = shopService.totalProjectCase(shopId);
        return obtainIntervalScore(totalCount,0.25);
    }

    private double obtainIntervalScore(int totalCount,double scale){
        double planScore = 0d;
        /**
         *       10个-20个=1分
         *
         *       20个-50个=2分
         *
         *       50个-100个=3分
         *
         *       100个-200个=4分
         *
         *       200个以上=5分
         */

        if (totalCount > 200){
            return (planScore + 5) * scale;
        }

        if (totalCount > 100 && totalCount <= 200){
            return (planScore + 4) * scale;
        }

        if (totalCount > 50 && totalCount <= 100){
            return (planScore + 3) * scale;
        }

        if (totalCount > 20 && totalCount <= 50){
            return (planScore + 2) * scale;
        }

        if (totalCount > 10 && totalCount <= 20){
            return (planScore + 1) * scale;
        }
        return 0;
    }

    private double totalShopPlanScore(Integer shopId) {
        int totalCount = shopService.totalShopPlan(shopId);
        return obtainIntervalScore(totalCount,0.4);
    }

    private double totalShopBaseScore(CompanyShopListBO shop) {
        double baseScore = 1d;
        if (Objects.equals(shop.getBusinessType(), 4) || Objects.equals(shop.getBusinessType(), 5)) {
            //判断店铺是否有擅长分格
            if (!StringUtils.isEmpty(shop.getCategoryIds())) {
                baseScore += 1d;
            }

        } else {
            baseScore += 1d;
        }

        if (Objects.equals(shop.getBusinessType(), 4)) {
            if (!StringUtils.isEmpty(shop.getDesignFeeStarting()) || !StringUtils.isEmpty(shop.getDesignFeeEnding())) {
                baseScore += 1d;
            }
        } else {
            baseScore += 1d;
        }

        if (null != shop.getLogoPicId()) {
            baseScore += 1d;
        }

        if (!StringUtils.isEmpty(shop.getCoverResIds())) {
            baseScore += 1d;
        }

        return baseScore * 0.25;
    }
}
