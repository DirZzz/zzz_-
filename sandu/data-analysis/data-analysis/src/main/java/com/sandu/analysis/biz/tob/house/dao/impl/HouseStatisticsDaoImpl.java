package com.sandu.analysis.biz.tob.house.dao.impl;

import com.sandu.analysis.biz.tob.house.constant.HouseEventConstants;
import com.sandu.analysis.biz.tob.house.dao.HouseStatisticsDao;
import com.sandu.analysis.biz.tob.house.model.HouseInfoDto;
import com.sandu.analysis.biz.tob.house.model.HouseStatisticsDayDto;
import com.sandu.analysis.biz.tob.house.model.HouseStatisticsHourDto;
import com.sandu.analysis.biz.tob.house.model.HouseUsageAmountStatisticsDayDto;
import com.sandu.analysis.util.JdbcUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 户型数据统计数据实现层
 * @author xiaoxc
 * @data 2019/6/14 0014.
 */
public class HouseStatisticsDaoImpl implements HouseStatisticsDao {
    private String className = this.getClass().getName();

    @Override
    public List<HouseInfoDto> selectHouseArea(List<String> houseIdList) {
        String functionName = className + ".selectHouseArea";

        if(CollectionUtils.isEmpty(houseIdList)){
            System.out.println("error:" + functionName + " houseIdList is empty!");
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (String houseId : houseIdList){
            builder.append("'").append(houseId).append("',");
        }
        String houseIds = builder.toString().substring(0, (builder.length() - 1));

        String sql = "select a.id as houseId,c1.area_code as provinceCode, c1.area_name as provinceName,c2.area_code as cityCode, c2.area_name as cityName, a.house_common_code as houseName, a.house_code houseCode,b.living_name as livingName "
                + "from app_online_30.base_house a "
                + "LEFT JOIN app_online_30.base_living b ON b.id = a.living_id and b.is_deleted = 0 "
                + "LEFT JOIN app_online_30.base_area c1 ON c1.area_code = SUBSTRING(SUBSTRING_INDEX(b.area_long_code,'.',2),2) "
                + "LEFT JOIN app_online_30.base_area c2 ON c2.area_code = SUBSTRING(SUBSTRING_INDEX(b.area_long_code,'.',3),LENGTH(SUBSTRING_INDEX(b.area_long_code,'.',2)) + 2) "
                + "where a.id in (" + houseIds + ")";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());

        List<HouseInfoDto> houseInfoDtoList = null;
        try {
            houseInfoDtoList = qr.query(sql, new BeanListHandler<HouseInfoDto>(HouseInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return houseInfoDtoList;
    }

    @Override
    public List<HouseInfoDto> selectNewHouseCount(String startTime, String endTime, String type) {
        String functionName = className + ".selectNewHouseAmount";

        if(StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)){
            System.out.println("error:" + functionName + " startTime or endTime is empty!");
            return null;
        }

        String sql = null;
        if (Objects.equals(type, HouseEventConstants.HOUSE_DAY_TYPE)) {
            sql = "select c1.area_code as provinceCode, c1.area_name as provinceName,c2.area_code as cityCode, c2.area_name as cityName, count(a.id) as newHouseCount "
                    + "from app_online_30.base_house a "
                    + "LEFT JOIN app_online_30.base_living b ON b.id = a.living_id and b.is_deleted = 0 "
                    + "LEFT JOIN app_online_30.base_area c1 ON c1.area_code = SUBSTRING(SUBSTRING_INDEX(b.area_long_code,'.',2),2) "
                    + "LEFT JOIN app_online_30.base_area c2 ON c2.area_code = SUBSTRING(SUBSTRING_INDEX(b.area_long_code,'.',3),LENGTH(SUBSTRING_INDEX(b.area_long_code,'.',2)) + 2) "
                    + "where a.gmt_create BETWEEN '" + startTime + "' AND '" + endTime + "'"
                    + "group by c1.area_code,c2.area_code";
        } else if (Objects.equals(type, HouseEventConstants.HOUSE_HOUR_TYPE)) {
            sql = "select DATE_FORMAT(a.gmt_create,'%Y-%m-%d %h') as perHour,a.gmt_create as createTime, c1.area_code as provinceCode, c1.area_name as provinceName,c2.area_code as cityCode, c2.area_name as cityName, count(a.id) as newHouseCount "
                    + "from app_online_30.base_house a "
                    + "LEFT JOIN app_online_30.base_living b ON b.id = a.living_id and b.is_deleted = 0 "
                    + "LEFT JOIN app_online_30.base_area c1 ON c1.area_code = SUBSTRING(SUBSTRING_INDEX(b.area_long_code,'.',2),2) "
                    + "LEFT JOIN app_online_30.base_area c2 ON c2.area_code = SUBSTRING(SUBSTRING_INDEX(b.area_long_code,'.',3),LENGTH(SUBSTRING_INDEX(b.area_long_code,'.',2)) + 2) "
                    + "where a.gmt_create BETWEEN '" + startTime + "' AND '" + endTime + "'"
                    + "group by DATE_FORMAT(a.gmt_create,'%Y-%m-%d %h'),c1.area_code,c2.area_code";
        }

        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());

        List<HouseInfoDto> houseInfoDtoList = null;
        try {
            houseInfoDtoList = qr.query(sql, new BeanListHandler<HouseInfoDto>(HouseInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return houseInfoDtoList;
    }

    @Override
    public void insertByDay(List<HouseStatisticsDayDto> dtoList, String startTime, String endTime) {
        String functionName = className + ".insertByDay";

        if(dtoList == null || dtoList.size() == 0) {
            System.out.println("warn, function = " + functionName + ", message = (dtoList == null || dtoList.size() == 0) = true");
            return;
        }
        //新增之前先删数据
        delete(startTime, endTime, "bigdata_house_statistics_2b_day");
        insertByDay(dtoList);

    }

    private void insertByDay(List<HouseStatisticsDayDto> dtoList) {
        String sql = "insert into bigdata_house_statistics_2b_day "
                + "(start_time, end_time, new_house_count, use_house_count, province_code, province_name, city_code, \n" +
                "  city_name, creator, gmt_create, modifier, gmt_modified, is_deleted, remark) "
                + "values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // ==========设置参数 ->start
        Object[][] params = new Object[dtoList.size()][];
        for (int index = 0; index < dtoList.size(); index++) {
            HouseStatisticsDayDto dto = dtoList.get(index);
            params[index] = new Object[] {
                    dto.getStartTime(), dto.getEndTime(),
                    dto.getNewHouseCount(),dto.getUseHouseCount(),
                    dto.getProvinceCode(), dto.getProvinceName(),
                    dto.getCityCode(), dto.getCityName(),
                    dto.getCreator(),dto.getGmtCreate(),
                    dto.getModifier(), dto.getGmtModified(),
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

    @Override
    public void insertByHour(List<HouseStatisticsHourDto> dtoList, String startTime, String endTime) {
        String functionName = className + ".insertByHour";
        if(dtoList == null || dtoList.size() == 0) {
            System.out.println("warn, function = " + functionName + ", message = (dtoList == null || dtoList.size() == 0) = true");
            return;
        }

        //新增之前先删数据
        delete(startTime, endTime, "bigdata_house_statistics_2b_hour");
        insertByHour(dtoList);
    }

    private void insertByHour(List<HouseStatisticsHourDto> dtoList) {

        String sql = "insert into bigdata_house_statistics_2b_hour "
                + "(start_time, end_time, new_house_count, use_house_count, province_code, province_name, city_code, \n" +
                "  city_name, creator, gmt_create, modifier, gmt_modified, is_deleted, remark) "
                + "values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // ==========设置参数 ->start
        Object[][] params = new Object[dtoList.size()][];
        for (int index = 0; index < dtoList.size(); index++) {
            HouseStatisticsHourDto dto = dtoList.get(index);
            params[index] = new Object[] {
                    dto.getStartTime(), dto.getEndTime(),
                    dto.getNewHouseCount(),dto.getUseHouseCount(),
                    dto.getProvinceCode(), dto.getProvinceName(),
                    dto.getCityCode(), dto.getCityName(),
                    dto.getCreator(),dto.getGmtCreate(),
                    dto.getModifier(), dto.getGmtModified(),
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

    @Override
    public void insertHouseUsageAmountByDay(List<HouseUsageAmountStatisticsDayDto> dtoList, String startTime, String endTime) {
        String functionName = className + ".insertHouseUsageAmountByDay";

        if(dtoList == null || dtoList.size() == 0) {
            System.out.println("warn, function = " + functionName + ", message = (dtoList == null || dtoList.size() == 0) = true");
            return;
        }
        //新增之前先删数据
        delete(startTime, endTime, "bigdata_house_usage_amount_2b_day");
        insertHouseUsageAmountByDay(dtoList);
    }

    private void insertHouseUsageAmountByDay(List<HouseUsageAmountStatisticsDayDto> dtoList) {
        String sql = "insert into bigdata_house_usage_amount_2b_day "
                + "(start_time, end_time, house_id, house_name, house_code, living_name, house_usage_amount, province_code, \n" +
                "  province_name, city_code, city_name, creator, gmt_create, modifier, gmt_modified, is_deleted, remark) "
                + "values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // ==========设置参数 ->start
        Object[][] params = new Object[dtoList.size()][];
        for (int index = 0; index < dtoList.size(); index++) {
            HouseUsageAmountStatisticsDayDto dto = dtoList.get(index);
            params[index] = new Object[] {
                    dto.getStartTime(), dto.getEndTime(),
                    dto.getHouseId(),dto.getHouseName(),
                    dto.getHouseCode(),dto.getLivingName(),
                    dto.getHouseUsageAmount(),
                    dto.getProvinceCode(), dto.getProvinceName(),
                    dto.getCityCode(), dto.getCityName(),
                    dto.getCreator(),dto.getGmtCreate(),
                    dto.getModifier(), dto.getGmtModified(),
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

    @Override
    public void delete(String startTime, String endTime, String tableName) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime) || StringUtils.isBlank(tableName)) {
            return ;
        }
        List<Long> ids = select(startTime, endTime, tableName);
        if (CollectionUtils.isEmpty(ids)) {
            return ;
        }
        delete(ids, tableName);
    }

    private List<Long> select(String startTime, String endTime, String tableName){
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            return null;
        }
        String sql = null;
        Object[] params = new Object[]{startTime, endTime};
        if (Objects.equals(tableName, "bigdata_house_statistics_2b_hour")) {
            sql = "select id from " + tableName + " where DATE_FORMAT(start_time,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d')";
            params = new Object[]{startTime};
        } else {
            sql = "select id from " + tableName + " where start_time = ? and end_time = ?";
        }
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());

        List<Long> ids = null;
        try {
            ids = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    private void delete(List<Long> ids, String tableName){
        if(CollectionUtils.isEmpty(ids)) {
            return ;
        }
        StringBuffer sb = new StringBuffer();
        for (Long id : ids) {
            sb.append(id).append(",");
        }
        String params = sb.substring(0, sb.length() - 1);
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String sql = "delete from " + tableName + " where id in (" + params + ");";
            qr.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
