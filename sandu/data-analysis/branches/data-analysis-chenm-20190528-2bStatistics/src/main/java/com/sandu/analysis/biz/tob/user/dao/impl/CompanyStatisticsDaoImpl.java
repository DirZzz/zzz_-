package com.sandu.analysis.biz.tob.user.dao.impl;

import com.sandu.analysis.biz.tob.user.dao.CompanyStatisticsDao;
import com.sandu.analysis.biz.tob.user.model.CompanyInfoDto;
import com.sandu.analysis.biz.tob.user.model.CompanyStatistics2bDayDto;
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
import java.util.stream.Collectors;

public class CompanyStatisticsDaoImpl implements CompanyStatisticsDao {

    @Override
    public List<CompanyInfoDto> selectCompanyInfoListByIdList(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        List<CompanyInfoDto> companyInfoDtoList = null;
        StringBuffer stringBuffer = new StringBuffer();
        for (Integer id : idList) {
            stringBuffer.append(id + ",");
        }
        String idStr = stringBuffer.toString();
        idStr = idStr.substring(0, idStr.length() - 1);

        String sql = "SELECT c.id,c.company_name AS companyName ,c.business_type AS companyType,GROUP_CONCAT(b.`brand_name`) AS brandName FROM `app_online_30`.base_company AS c LEFT JOIN `app_online_30`.base_brand b \n" +
                "ON c.id = b.`company_id`\n" +
                "where c.id IN (" + idStr + ")  GROUP BY c.id";
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        try {
            companyInfoDtoList = qr.query(sql, new BeanListHandler<CompanyInfoDto>(CompanyInfoDto.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companyInfoDtoList;
    }

    @Override
    public void insertAfterDelete(List<CompanyStatistics2bDayDto> dtos, LocalDate localDate) {
        if (localDate == null || CollectionUtils.isEmpty(dtos)) {
            return;
        }
        Date startTime = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        delete(Utils.date2str(startTime), Utils.date2str(endTime));
        insert(dtos);
    }

    public void insert(List<CompanyStatistics2bDayDto> dtos) {
        String sql = "insert into bigdata_company_statistics_2b_day  \n" +
                "(start_time, end_time, \n" +
                "company_type,company_id,company_name,brand_name,\n" +
                " active_user_count, user_account_count,user_effective_rate,\n" +
                "creator, gmt_create, modifier, gmt_modified, is_deleted, remark)  \n" +
                "values  \n" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?) ;";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // ==========设置参数 ->start
        Object[][] params = new Object[dtos.size()][];
        for (int index = 0; index < dtos.size(); index++) {
            CompanyStatistics2bDayDto dto = dtos.get(index);
            params[index] = new Object[]{
                    dto.getStartTime(), dto.getEndTime(),
                    dto.getCompanyType(), dto.getCompanyId(), dto.getCompanyName(), dto.getBrandName(),
                    dto.getActiveUserCount(), dto.getUserAccountCount(), dto.getUserEffectiveRate(),
                    dto.getCreator(), simpleDateFormat.format(dto.getGmtCreate()), dto.getModifier(), simpleDateFormat.format(dto.getGmtModified()),
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
        String sql = "select id from bigdata_company_statistics_2b_day where start_time = ? and end_time = ?";
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
            String sql = "update bigdata_company_statistics_2b_day set is_deleted = 1  where id in (" + params + ");";
            qr.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
