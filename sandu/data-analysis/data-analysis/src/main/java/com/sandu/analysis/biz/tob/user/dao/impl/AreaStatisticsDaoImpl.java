package com.sandu.analysis.biz.tob.user.dao.impl;

import com.sandu.analysis.biz.tob.user.dao.AreaStatisticsDao;
import com.sandu.analysis.biz.tob.user.model.AreaInfoDto;
import com.sandu.analysis.biz.tob.user.model.AreaStatistics2bDayDto;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.util.JdbcUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AreaStatisticsDaoImpl implements AreaStatisticsDao {

    @Override
    public void insertAfterDelete(List<AreaStatistics2bDayDto> dtos, LocalDate localDate) {
        if (localDate == null || CollectionUtils.isEmpty(dtos)) {
            return;
        }
        Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        delete(Utils.date2str(startTime), Utils.date2str(endTime));
        insert(dtos);
    }

    public void insert(List<AreaStatistics2bDayDto> dtos) {
        String sql = "insert into bigdata_area_statistics_2b_day  \n" +
                "(start_time, end_time, \n" +
                "province_code,province_name,city_code,city_name,\n" +
                "new_user_count, active_user_count,\n" +
                "creator, gmt_create, modifier, gmt_modified, is_deleted, remark)  \n" +
                "values  \n" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ;\n";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // ==========设置参数 ->start
        Object[][] params = new Object[dtos.size()][];
        for (int index = 0; index < dtos.size(); index++) {
            AreaStatistics2bDayDto dto = dtos.get(index);
            params[index] = new Object[] {
                    dto.getStartTime(), dto.getEndTime(),
                    dto.getProvinceCode(),dto.getProvinceName(),dto.getCityCode(),dto.getCityName(),
                    dto.getNewUserCount(),dto.getActiveUserCount(),
                    dto.getCreator(),simpleDateFormat.format(dto.getGmtCreate()), dto.getModifier(), simpleDateFormat.format(dto.getGmtModified()),
                    dto.getIsDeleted(), dto.getRemark()
            };
        }
        // ==========设置参数 ->end

        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            qr.batch(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return;
        }
        List<Long> ids = select(startTime, endTime);
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        delete(ids);
    }

    private List<Long> select(String startTime, String endTime){
        if( StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime) ){
            return null;
        }
        String sql = "select id from bigdata_area_statistics_2b_day where start_time = ? and end_time = ?";
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
            String sql = "update bigdata_area_statistics_2b_day set is_deleted = 1  where id in (" + params + ");";
            qr.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<AreaInfoDto> selectAreaInfoByAreaNames(Set<String> areaNameSet,Integer levelId) {

        if(CollectionUtils.isEmpty(areaNameSet) || levelId == null){
            return null;
        }
        StringBuffer areaNameSb = new StringBuffer();
        for (String name : areaNameSet) {
            areaNameSb.append("'" + name + "'").append(",");
        }
        String areaNameParams = areaNameSb.substring(0, areaNameSb.length() - 1);

        List<AreaInfoDto> dtoList = null;
        String sql = "SELECT area_code AS areaCode,area_name AS areaName FROM `app_online_30`.base_area\n" +
                " WHERE \n" +
                " level_id = "+ levelId +"\n" +
                " AND area_name IN (\n" + areaNameParams + ") and is_deleted =0";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            dtoList = qr.query(sql,new BeanListHandler<AreaInfoDto>(AreaInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    @Override
    public List<AreaInfoDto> selectProvinceInfoByAreaNames(Set<String> areaNameSet) {
        return this.selectAreaInfoByAreaNames(areaNameSet,1);
    }

    @Override
    public List<AreaInfoDto> selectCityInfoByAreaNames(Set<String> areaNameSet) {
        return this.selectAreaInfoByAreaNames(areaNameSet,2);
    }
}
