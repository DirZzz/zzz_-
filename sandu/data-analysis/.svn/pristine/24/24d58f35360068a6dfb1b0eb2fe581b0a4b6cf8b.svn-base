package com.sandu.analysis.biz.tob.plan.dao.impl;

import com.sandu.analysis.biz.constant.CommonConstants;
import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanDao;
import com.sandu.analysis.biz.tob.plan.model.RecommendedPlanDto;
import com.sandu.analysis.util.JdbcUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class RecommendedPlanDaoImpl implements RecommendedPlanDao {
    String className = this.getClass().getName();

    @SuppressWarnings("deprecation")
    @Override
    public List<RecommendedPlanDto> selectNewRecommendedPlan(LocalDateTime startTime, LocalDateTime endTime) {
        String functionName = className + ".selectRecommendedPlan";

        if(startTime == null || endTime == null) {
            System.out.println("warn, function = " + functionName + ", message = (startTime == null || endTime == null) = true");
            return null;
        }

        String sql = "SELECT " +
                "a.id  id," +
                "a.recommended_type  recommendedType," +
                "a.design_recommended_style_id  styleId," +
                "b.space_function_id  spaceType, " +
                "a.company_id  companyId, " +
                "a.plan_source  planSource, " +
                "count(1)  newPlanCount " +
                "FROM app_online_30.design_plan_recommended a, app_online_30.space_common b " +
                "WHERE a.space_common_id = b.id " +
                "and a.is_deleted = 0 " +
                "and a.gmt_create BETWEEN ? and ? " +
                "GROUP BY " +
                "a.recommended_type, " +
                "a.design_recommended_style_id, " +
                "b.space_function_id, " +
                "a.company_id, " +
                "a.plan_source";
        Object[] params = new Object[] {
                startTime.format(CommonConstants.DATE_TIME_FORMATTER_DEFAULT), endTime.format(CommonConstants.DATE_TIME_FORMATTER_DEFAULT)
        };
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        BeanProcessor bean = new GenerousBeanProcessor();
        RowProcessor processor = new BasicRowProcessor(bean);
        List<RecommendedPlanDto> recommendedPlanList = null;
        try {
            recommendedPlanList = qr.query(sql, params, new BeanListHandler<RecommendedPlanDto>(RecommendedPlanDto.class, processor));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommendedPlanList;
    }


    @Override
    public List<RecommendedPlanDto> selectRecommendedPlanList(List<String> planIdList) {
        if(CollectionUtils.isEmpty(planIdList)){
            return null;
        }
        List<RecommendedPlanDto> recommendedPlanDtos = null;
        StringBuffer stringBuffer = new StringBuffer();
        for (String planId : planIdList){
            stringBuffer.append( planId + ",");
        }
        String planIdStr = stringBuffer.toString();
        planIdStr = planIdStr.substring(0,planIdStr.length() - 1);

        String sql = "SELECT " +
                "a.id id, " +
                "a.recommended_type  recommendedType," +
                "a.design_recommended_style_id  styleId," +
                "b.space_function_id  spaceType, " +
                "a.company_id  companyId, " +
                "a.plan_source  planSource " +
                "FROM app_online_30.design_plan_recommended a, app_online_30.space_common b " +
                "WHERE a.space_common_id = b.id and a.is_deleted = 0 and a.id in (" + planIdStr + ")";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            recommendedPlanDtos = qr.query(sql,new BeanListHandler<RecommendedPlanDto>(RecommendedPlanDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommendedPlanDtos;
    }
}
