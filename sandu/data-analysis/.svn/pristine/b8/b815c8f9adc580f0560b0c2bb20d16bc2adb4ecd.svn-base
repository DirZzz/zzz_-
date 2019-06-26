package com.sandu.analysis.biz.user.dao.impl;

import com.sandu.analysis.biz.user.dao.UserAnalysisResultDao;
import com.sandu.analysis.biz.user.dao.model.UserAnalysisResultDto;
import com.sandu.analysis.util.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserAnalysisResultImpl implements UserAnalysisResultDao {
    private String className = this.getClass().getName();
    @Override
    public void insert(List<UserAnalysisResultDto> dtoList) {
        String functionName = className + ".insert";

        if(dtoList == null || dtoList.size() == 0) {
            System.out.println("warn, function = " + functionName + ", message = (dtoList == null || dtoList.size() == 0) = true");
            return;
        }
        String sql = "insert into bigdata_user_analysis_result "
                + "(start_time, end_time, new_user_count, active_user_count, type, app_id, channel, \n" +
                "    creator, gmt_create, modifier, gmt_modified, is_deleted, remark) "
                + "values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // ==========设置参数 ->start
        Object[][] params = new Object[dtoList.size()][];
        for (int index = 0; index < dtoList.size(); index++) {
            UserAnalysisResultDto dto = dtoList.get(index);
            params[index] = new Object[] {
                    simpleDateFormat.format(dto.getStartTime()), simpleDateFormat.format(dto.getEndTime()),
                    dto.getNewUserCount(),dto.getActiveUserCount(), dto.getType(), dto.getAppId(), dto.getChannel(),
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
}
