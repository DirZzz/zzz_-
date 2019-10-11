package com.sandu.analysis.biz.tob.plan.dao.impl;

import com.sandu.analysis.biz.tob.plan.dao.RecommendedPlanStatisticsHourDao;
import com.sandu.analysis.biz.tob.plan.model.RecommendedPlanStatisticsHourDto;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Zhangwenjian
 * Date: 2019-06-15 19:55
 * Desc:
 */
public class RecommendedPlanStatisticsHourDaoImpl implements RecommendedPlanStatisticsHourDao {

    @Override
    public void insertAfterDelete(List<RecommendedPlanStatisticsHourDto> dtos, LocalDate localDate){
        if( localDate == null || CollectionUtils.isEmpty(dtos) ){
            return ;
        }
        Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        delete(Utils.date2str(startTime), Utils.date2str(endTime));
        insert(dtos);
    }

    @Override
    public List<RecommendedPlanStatisticsHourDto> selectNewPlanCount(LocalDate localDate){
        if( localDate == null ){
            return null;
        }

        Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String sql = "select DATE_FORMAT(a.gmt_create,'%Y-%m-%d %H') as perHour," +
                        "b.space_function_id as spaceCommonType," +
                        "a.recommended_type as planType," +
                        "a.design_style_id as designStyleId," +
                        "a.company_id as companyId," +
                        "a.plan_source as planSource," +
                        "count(a.id) as newPlanCount," +
                        "a.gmt_create as gmtCreate" +
                        " from app_online_30.design_plan_recommended a, app_online_30.space_common b" +
                        " where a.space_common_id = b.id " +
                        " and a.gmt_create BETWEEN '" + Utils.date2str(startTime) + "' AND '" + Utils.date2str(endTime) + "'" +
                        " group by DATE_FORMAT(a.gmt_create,'%Y-%m-%d %H'),b.space_function_id,a.recommended_type,a.design_style_id,a.company_id,a.plan_source;";

        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        List<RecommendedPlanStatisticsHourDto> list = new ArrayList<>();
        try {
            list = qr.query(sql, new BeanListHandler<RecommendedPlanStatisticsHourDto>(RecommendedPlanStatisticsHourDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Long> select(String startTime, String endTime){
        if( StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime) ){
            return null;
        }
        String sql = "select id from bigdata_recommended_plan_statistics_2b_hour where DATE_FORMAT(start_time, '%Y-%m-%d') = DATE_FORMAT(?, '%Y-%m-%d')";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        Object[] params = new Object[]{startTime};
        List<Long> ids = null;
        try {
            ids = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public void delete(String startTime, String endTime){
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
            String sql = "delete from bigdata_recommended_plan_statistics_2b_hour where id in (" + params + ")";
            qr.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert(List<RecommendedPlanStatisticsHourDto> dtos) {
        if( CollectionUtils.isEmpty(dtos) ){
            return ;
        }

        String sql = "insert into bigdata_recommended_plan_statistics_2b_hour values (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[][] params = new Object[dtos.size()][];
        RecommendedPlanStatisticsHourDto dto = null;
        for( int i=0; i< dtos.size(); i++ ){
            dto = dtos.get(i);
            params[i] = new Object[]{
                    dto.getStartTime(), dto.getEndTime(), dto.getNewPlanCount(), dto.getPcUsePlanCount(), dto.getMobileUsePlanCount(),
                    dto.getPlanType(), dto.getDesignStyleId(), dto.getSpaceCommonType(), dto.getCompanyId(), dto.getPlanSource(),
                    dto.getCreator(), dto.getGmtCreate(), dto.getModifier(), dto.getGmtModified(),
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
}
