package com.sandu.analysis.biz.tob.user.dao.impl;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.biz.tob.user.dao.UserStatisticsDao;
import com.sandu.analysis.biz.tob.user.model.UserStatistics2bDayDto;
import com.sandu.analysis.biz.tob.user.model.UserStatistics2bHourDto;
import com.sandu.analysis.biz.tob.user.model.UserInfoDto;
import com.sandu.analysis.biz.util.Utils;
import com.sandu.analysis.util.JdbcUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserStatisticsDaoImpl implements UserStatisticsDao {


    @Override
    public List<UserInfoDto> selectUserInfoByUUidList(List<String> uuidList) {
        if(CollectionUtils.isEmpty(uuidList)){
            return null;
        }
        List<UserInfoDto> userInfoDtos = null;
        StringBuffer stringBuffer = new StringBuffer();
        for (String uuid : uuidList){
            stringBuffer.append("'" + uuid + "',");
        }
        String uuidStr = stringBuffer.toString();
        uuidStr = uuidStr.substring(0,uuidStr.length() - 1);
        String sql = "SELECT  u.uuid,u.user_type AS userType,u.use_type AS useType,u.company_id AS companyId," +
                "u.province_code AS provinceCode,u.city_code AS cityCode,pa.area_name provinceName,ca.area_name AS cityName \n" +
                "FROM `app_online_30`.sys_user u \n" +
                "LEFT JOIN `app_online_30`.base_area pa ON pa.area_code = u.province_code \n" +
                "LEFT JOIN `app_online_30`.base_area ca ON ca.area_code = u.city_code \n" +
                "WHERE u.UUID IN (" + uuidStr + ")";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            userInfoDtos = qr.query(sql,new BeanListHandler<UserInfoDto>(UserInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userInfoDtos;
    }

    @Override
    public void insertAfterDeleteWithDay(List<UserStatistics2bDayDto> dayDtos, LocalDate localDate) {
        if( localDate == null || CollectionUtils.isEmpty(dayDtos) ){
            return ;
        }
        Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        deletedWithDay(Utils.date2str(startTime), Utils.date2str(endTime));
        insertWithDay(dayDtos);
    }

    @Override
    public void insertAfterDeleteWithHour(List<UserStatistics2bHourDto> hourDtos, LocalDate localDate) {
        if( localDate == null || CollectionUtils.isEmpty(hourDtos) ){
            return ;
        }
        Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        deletedWithHour(Utils.date2str(startTime), Utils.date2str(endTime));
        insertWithHour(hourDtos);
    }

    public void deletedWithDay(String startTime, String endTime) {
        List<Long> ids = this.searchIdsByDay(startTime,endTime);
        if(CollectionUtils.isEmpty(ids)) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (Long id : ids) {
            sb.append(id).append(",");
        }
        String params = sb.substring(0, sb.length() - 1);
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String sql = "update bigdata_user_statistics_2b_day set is_deleted = 1 where id in (" + params + ")";
            qr.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deletedWithHour(String startTime,String endTime) {
        List<Long> ids = this.searchIdsByHour(startTime,endTime);
        if(CollectionUtils.isEmpty(ids)) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        for( Long id : ids ){
            sb.append(id).append(",");
        }
        String params = sb.substring(0, sb.length() - 1);
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String sql = "update bigdata_user_statistics_2b_hour set is_deleted = 1 where id in (" + params+ ")";
            qr.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertWithDay(List<UserStatistics2bDayDto> dayDtos) {
        String sql = "insert into bigdata_user_statistics_2b_day "
                + "(start_time, end_time, new_user_count, active_user_count, user_type, use_type, "
                + "login_user_count_pc2b,login_user_count_mobile2b,login_user_count_merchantManage,"
                + "account_count,nonactivated_user_count,"
                +"    creator, gmt_create, modifier, gmt_modified, is_deleted, remark) "
                + "values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?,?,?)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // ==========设置参数 ->start
        Object[][] params = new Object[dayDtos.size()][];
        for (int index = 0; index < dayDtos.size(); index++) {
            UserStatistics2bDayDto dto = dayDtos.get(index);
            params[index] = new Object[] {
                    dto.getStartTime(), dto.getEndTime(),
                    dto.getNewUserCount(),dto.getActiveUserCount(), dto.getUserType(), dto.getUseType(),
                    dto.getLoginUserCountPC2B(),dto.getLoginUserCountMobile2B(),dto.getLoginUserCountMerchantManage(),
                    dto.getAccountCount(),dto.getNonactivatedUserCount(),
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

    public void insertWithHour(List<UserStatistics2bHourDto> hourDtos) {
        String sql = "insert into bigdata_user_statistics_2b_hour "
                + "(start_time, end_time, new_user_count, active_user_count, user_type, use_type, "
                + "login_user_count_pc2b,login_user_count_mobile2b,login_user_count_merchantManage,"
                +"    creator, gmt_create, modifier, gmt_modified, is_deleted, remark) "
                + "values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // ==========设置参数 ->start
        Object[][] params = new Object[hourDtos.size()][];
        for (int index = 0; index < hourDtos.size(); index++) {
            UserStatistics2bHourDto dto = hourDtos.get(index);
            params[index] = new Object[] {
                    dto.getStartTime(), dto.getEndTime(),
                    dto.getNewUserCount(),dto.getActiveUserCount(), dto.getUserType(), dto.getUseType(),
                    dto.getLoginUserCountPC2B(),dto.getLoginUserCountMobile2B(),dto.getLoginUserCountMerchantManage(),
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

    public List<Long> searchIdsByDay(String startTime, String endTime) {
        if(StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)){
            return null;
        }
        List<Long> list = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("select id from bigdata_user_statistics_2b_day where start_time = ? and end_time = ? and is_deleted =0");

        String sql = buffer.toString();
        Object[] params = new Object[] {startTime,endTime};

        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            list = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Long> searchIdsByHour(String startTime,String endTime) {
        if(StringUtils.isBlank(startTime)){
            return null;
        }
        List<Long> list = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("select id from bigdata_user_statistics_2b_hour where DATE_FORMAT(start_time, '%Y-%m-%d') = DATE_FORMAT(?, '%Y-%m-%d')  and is_deleted =0");

        String sql = buffer.toString();
        Object[] params = new Object[] {startTime};

        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            list = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    @Override
    public List<UserInfoDto> selectUserCountByDay(String endTime) {
        if(StringUtils.isBlank(endTime)){
            return null;
        }
        List<UserInfoDto> userInfoDtos = null;
        String sql = "SELECT COUNT(1) as accountCount,user_type as userType,use_type as useType\n" +
                "FROM `app_online_30`.sys_user  \n" +
                "WHERE platform_type = 2 AND is_deleted =0\n" +
                "AND gmt_create < '"+ endTime +"'\n" +
                "GROUP BY user_type,use_type";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            userInfoDtos = qr.query(sql,new BeanListHandler<UserInfoDto>(UserInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userInfoDtos;
    }

    @Override
    public List<UserInfoDto> selectNonactivatedUserCountByDay(String endTime) {
        if(StringUtils.isBlank(endTime)){
            return null;
        }
        List<UserInfoDto> userInfoDtos = null;
        String sql = "SELECT COUNT(1) as nonactivatedUserCount,user_type as userType,use_type as useType\n" +
                "FROM `app_online_30`.sys_user  \n" +
                "WHERE platform_type = 2  AND is_deleted =0\n" +
                "and gmt_create < '" + endTime + "' and \n" +
                "(is_login_before = 0 or first_login_time < '" + endTime + "')\n" +
                "GROUP BY user_type,use_type";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            userInfoDtos = qr.query(sql,new BeanListHandler<UserInfoDto>(UserInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userInfoDtos;
    }

    @Override
    public Long selectUserTotalByCompanyId(Integer companyId, String endTime) {
        if(companyId == null || StringUtils.isBlank(endTime)){
            return null;
        }
        String sql = "SELECT COUNT(1) userCount FROM `app_online_30`.sys_user WHERE \n" +
                "company_id = " +companyId +" \n" +
                "AND platform_type = 2 AND gmt_create < '" +endTime +"'\n" +
                "AND is_deleted =0";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        Long count = 0L;

        try {
            count = qr.query(sql,new  ScalarHandler<Long>());
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public List<UserInfoDto> selectNewUserCountWithHour(String beginTime, String endTime) {
        if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)){
            return null;
        }
        List<UserInfoDto> userInfoDtos = null;
        String sql = "SELECT DATE_FORMAT(gmt_create,'%Y-%m-%d %H:00:00') AS perHour,COUNT(1) AS newUserCount,user_type as userType,use_type as useType FROM `app_online_30`.sys_user \n" +
                "WHERE platform_type = 2\n" +
                "AND gmt_create >= '" + beginTime + "' AND gmt_create < '" + endTime + "'\n" +
                "AND is_deleted =0\n" +
                "GROUP BY DATE_FORMAT(gmt_create,'%Y-%m-%d %H:00:00'),user_type,use_type";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            userInfoDtos = qr.query(sql,new BeanListHandler<UserInfoDto>(UserInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfoDtos;
    }

    @Override
    public List<UserInfoDto> selectNewUserCountWithDay(String beginTime, String endTime) {
        if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)){
            return null;
        }
        List<UserInfoDto> userInfoDtos = null;
        String sql = "SELECT DATE_FORMAT(gmt_create,'%Y-%m-%d 00:00:00') AS perHour,gmt_create AS gmtCreate,COUNT(1) AS newUserCount,user_type as userType,use_type as useType  FROM `app_online_30`.sys_user \n" +
                "WHERE platform_type = 2\n" +
                "AND gmt_create >= '" + beginTime + "' AND gmt_create < '" + endTime + "'\n" +
                "AND is_deleted =0\n" +
                "GROUP BY DATE_FORMAT(gmt_create,'%Y-%m-%d'),user_type,use_type";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            userInfoDtos = qr.query(sql,new BeanListHandler<UserInfoDto>(UserInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfoDtos;
    }

    @Override
    public List<UserInfoDto> selectNewUserCountByCity(String beginTime, String endTime) {
        if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)){
            return null;
        }
        List<UserInfoDto> userInfoDtos = null;
        String sql = "SELECT DATE_FORMAT(u.gmt_create,'%Y-%m-%d 00:00:00') AS perHour,u.gmt_create AS gmtCreate,COUNT(1) AS newUserCount,\n" +
                "u.province_code AS provinceCode,u.city_code AS cityCode,pa.area_name provinceName,ca.area_name AS cityName\n" +
                "FROM `app_online_30`.sys_user AS u\n" +
                "INNER JOIN `app_online_30`.base_area pa ON pa.area_code = u.province_code\n" +
                "INNER JOIN `app_online_30`.base_area ca ON ca.area_code = u.city_code\n" +
                "WHERE u.platform_type = 2\n" +
                "AND u.gmt_create >= '" + beginTime + "' AND u.gmt_create < '" + endTime + "'\n" +
                "AND u.is_deleted =0\n" +
                "GROUP BY DATE_FORMAT(u.gmt_create,'%Y-%m-%d'),u.province_code,u.city_code";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            userInfoDtos = qr.query(sql,new BeanListHandler<UserInfoDto>(UserInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfoDtos;
    }

    @Override
    public List<String> selectNewUserUuidWithDay(String beginTime, String endTime) {
        if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)){
            return null;
        }
        List<String> list = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT uuid  FROM `app_online_30`.sys_user \n" +
                "                 WHERE platform_type = 2 \n" +
                "                 AND gmt_create >= ? AND gmt_create < ? \n" +
                "                 AND is_deleted =0  \n" +
                "                  AND UUID IS NOT NULL and UUID !=''");

        String sql = buffer.toString();
        Object[] params = new Object[] {beginTime,endTime};

        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            list = qr.query(sql, params, new ColumnListHandler<>("uuid")).stream().map(item ->item.toString()).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
