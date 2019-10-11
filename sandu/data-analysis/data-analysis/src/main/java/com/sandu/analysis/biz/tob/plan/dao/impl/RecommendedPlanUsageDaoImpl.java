package com.sandu.analysis.biz.tob.plan.dao.impl;

import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanUsageDao;
import com.sandu.analysis.biz.tob.plan.model.RecommendedPlanUsageDto;
import com.sandu.analysis.biz.tob.plan.model.SpaceCommonTypeDto;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.util.JdbcUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Zhangwenjian
 * Date: 2019-06-14 16:50
 * Desc:
 */
public class RecommendedPlanUsageDaoImpl implements RecommendedPlanUsageDao {

    @Override
    public List<RecommendedPlanUsageDto> selectPlanInfoByIds(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids) ){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for( Integer id : ids ){
            sb.append(id).append(",");
        }
        String params = sb.toString().substring(0, sb.length() - 1);
        String sql = "select id as planId,plan_name as planName,plan_code as planCode,recommended_type as planType,plan_source as planSource,design_style_id designStyleId,company_id as companyId,space_common_id as spaceCommonId " +
                "from app_online_30.design_plan_recommended where id in (" + params + ");";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        List<RecommendedPlanUsageDto> dtoList = null;
        try {
            dtoList = qr.query(sql,new BeanListHandler<RecommendedPlanUsageDto>(RecommendedPlanUsageDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    @Override
    public List<SpaceCommonTypeDto> selectSpaceTypeByIds(List<Integer> ids){
        if( CollectionUtils.isEmpty(ids) ){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for( Integer id : ids ){
            sb.append(id).append(",");
        }
        String params = sb.toString().substring(0, sb.length() - 1);
        String sql = "select id spaceCommonId, space_function_id spaceCommonType from app_online_30.space_common where id in  (" + params + ")";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        List<SpaceCommonTypeDto> list = null;
        try {
            list = qr.query(sql, new BeanListHandler<SpaceCommonTypeDto>(SpaceCommonTypeDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insertAfterDelete(List<RecommendedPlanUsageDto> dtos, LocalDate localDate){
        if( localDate == null || CollectionUtils.isEmpty(dtos) ){
            return ;
        }
        Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        delete(Utils.date2str(startTime), Utils.date2str(endTime));
        insert(dtos);
    }

    private void insert(List<RecommendedPlanUsageDto> dtos) {
        if( CollectionUtils.isEmpty(dtos) ){
            return ;
        }

        String sql = "insert into bigdata_recommended_plan_usage_amount_2b_day values (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[][] params = new Object[dtos.size()][];
        RecommendedPlanUsageDto dto = null;
        for( int i=0; i< dtos.size(); i++ ){
            dto = dtos.get(i);
            params[i] = new Object[]{
                    dto.getStartTime(), dto.getEndTime(), dto.getPlanId(), dto.getPlanName(), dto.getPlanCode(), dto.getPlanType(),
                    dto.getPlanSource(), dto.getDesignStyleId(), dto.getCompanyId(), dto.getSpaceCommonType(), dto.getPlanUsageAmountPc(),
                    dto.getPlanUsageAmountMobile2b(), dto.getCreator(), dto.getGmtCreate(), dto.getModifier(), dto.getGmtModified(),
                    dto.getIsDeleted(), dto.getRemark()
            };
        }

        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            qr.batch(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Long> select(String startTime, String endTime){
        if( StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime) ){
            return null;
        }
        String sql = "select id from bigdata_recommended_plan_usage_amount_2b_day where start_time = ? and end_time = ?";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        Object[] params = new Object[]{startTime, endTime};
        List<Long> ids = null;
        try {
            ids = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    private void delete(String startTime, String endTime){
        if( StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime) ){
            return ;
        }
        List<Long> ids = select(startTime, endTime);
        if( CollectionUtils.isEmpty(ids) ){
            return ;
        }
        delete(ids);
    }

    private void delete(List<Long> ids){
        if( CollectionUtils.isEmpty(ids) ){
            return ;
        }
        StringBuffer sb = new StringBuffer();
        for( Long id : ids ){
            sb.append(id).append(",");
        }
        String params = sb.substring(0, sb.length() - 1);
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String sql = "delete from bigdata_recommended_plan_usage_amount_2b_day where id in (" + params + ");";
            qr.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
