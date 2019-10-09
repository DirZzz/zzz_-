package com.sandu.config;

import com.sandu.api.category.service.biz.CategoryBizService;
import com.sandu.api.company.model.bo.CompanyShopListBO;
import com.sandu.api.company.service.CompanyShopService;
import com.sandu.api.customer.service.CustomerAlotZoneService;
import com.sandu.api.decoratecustomer.service.biz.DecorateCustomerBizService;
import com.sandu.api.product.service.biz.ProductBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * wangwang-product
 *
 * @author Sandu
 * @datetime 2018/3/19 17:39
 */
@Configuration(value = "schedulingConfig")
@EnableScheduling
@Slf4j
@ConditionalOnProperty(name = "app.schedule.use.flag", havingValue = "true")
public class SchedulingConfig {


    @Autowired
    private ProductBizService productBizService;
    @Autowired
    private CustomerAlotZoneService customerAlotZoneService;
    @Autowired
    private CategoryBizService categoryBizService;

    @Autowired
    private DecorateCustomerBizService decorateCustomerBizService;

    @Value("${app.schedule.use.flag}")
    private boolean scheduleFlag;

    @Resource
    private CompanyShopService companyShopService;

    /**
     * 自动上架任务
     * 每5分钟执行一次
     */
//    @Scheduled(cron = "0 0/5 * * * ? ")
//    @Scheduled(initialDelay = 2 * 60 * 1000L, fixedDelay = 5 * 60 * 1000L)
    public void autoPutway() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#1 自动上架产品, StartTime: {}", LocalDateTime.now());
        productBizService.synProductToPlatform();
        log.info("#1 EndTime: {}", LocalDateTime.now());
    }

    /**
     * 同步产品运营分类缓存
     */
    @Scheduled(cron = "0 0 2 * * ? ")
    public void syncCategoryCache() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#1 同步产品运营分类缓存, StartTime: {}", LocalDateTime.now());
        categoryBizService.syncCache();
        log.info("#1 EndTime: {}", LocalDateTime.now());
    }

    /**
     * 同步三度后台已上架产品状态为已发布
     * 程序启动后，延迟5分钟开始执行， 以后每6分钟执行一次
     */
    @Scheduled(initialDelay = 7 * 60 * 1000L, fixedDelay = 6 * 60 * 1000L)
    public void updatedProductStateToRelease() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#2 同步三度后台已上架产品为已发布， StartTime: {}", LocalDateTime.now());
        productBizService.synProductPutawayState();
        log.info("#2 EndTime： {}", LocalDateTime.now());
    }

    /**
     * 修复无公司ID的产品
     * 程序启动后，延迟7分钟开始执行， 以后每9分钟执行一次
     */
    @Scheduled(initialDelay = 9 * 60 * 1000L, fixedDelay = 9 * 60 * 1000L)
    public void fixedCompanyIdByBrandId() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#3 同步三度后台无企业产品, StartTime: {}", LocalDateTime.now());
        productBizService.synProductCompanyInfoWithBrandId();
        log.info("#3 EndTime: {}", LocalDateTime.now());
    }

    /**
     * 初始化上架任务
     */
//    @Scheduled(cron = "0 30 12 * 3 ?")
//    @Scheduled(cron = "0 0/3 * * * ?")
    public void init() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#4 初始化上架任务, StartTime: {}", LocalDateTime.now());
        productBizService.initProductStatus();
        log.info("#4 EndTime: {}", LocalDateTime.now());
    }

    /**
     * 同步三度后台产品-SPU-SKU关系
     */
    /*@Scheduled(fixedDelay = 5 * 60 * 1000L)*/
    @Scheduled(cron = "0 0 1 * * ?")
    public void fixedProductToSpuToSku() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#5 调用修复三度后台创建产品关联spu-sku存储过程, StartTime: {}", LocalDateTime.now());
        productBizService.fixedProductToSpu();
        log.info("#5 EndTime: {}", LocalDateTime.now());
    }

    /**
     * 为开通了小程序管理的厂商下面的经销商根据区域自动生成匹配规则
     * 每天上午10点，下午2点，4点
     */
//    @Scheduled(cron = "0 0 10,14,16 * * ?")
    @Scheduled(fixedDelay = 10 * 60 * 1000L)
    public void autoGeneraRule() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#6 经销商生成规则开始{}", LocalDateTime.now());
        customerAlotZoneService.autoGeneraRule();
        log.info("#6 EndTime: {}", LocalDateTime.now());
    }

    /**
     * 根据客户所在区域与经销商企业所负责区域进行分配客户
     * 每天凌晨0点
     */
//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 0 0/1 * * ?")
//    @Scheduled(fixedDelay = 10 * 60 * 1000L)
    public void allotAreaRule() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#7 经销商分配客户开始{}", LocalDateTime.now());
        customerAlotZoneService.allotAreaRule();
        log.info("#7 EndTime: {}", LocalDateTime.now());
    }

    /**
     * 将用户放到客户表s
     * 每天凌晨0点
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void allotCustomer() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#8 将用户放到客户表开始{}", LocalDateTime.now());
        customerAlotZoneService.allotCustomer();
        log.info("#8 将用户放到客户表EndTime: {}", LocalDateTime.now());
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void dispatchDecorateOrder() {
        if (!scheduleFlag) {
            return;
        }
        log.info("#9 系统派单/抢单 start=====>{}", LocalDateTime.now());
        decorateCustomerBizService.dispatchDecoratePrice();
        log.info("#9 系统派单/抢单开始end=====>{}", LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void refreshCompanyShopScore(){

        List<CompanyShopListBO> handleList = companyShopService.findAll();

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

        if (!CollectionUtils.isEmpty(updateList)) companyShopService.updateBatch(updateList);
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

        int totalCount = companyShopService.totalShopArticle(shop.getId());
        return obtainIntervalScore(totalCount,0.1);
    }

    private double totalProjectCaseScore(Integer shopId) {
        int totalCount = companyShopService.totalProjectCase(shopId);
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
        int totalCount = companyShopService.totalShopPlan(shopId);
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
