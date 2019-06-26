package com.sandu.analysis.biz.user.dao.impl;

import com.sandu.analysis.biz.user.dao.UserAnalysisResultDao;
import com.sandu.analysis.biz.user.model.UserAnalysisResultDto;
import com.sandu.analysis.biz.user.model.UserAnalysisResultQuery;
import com.sandu.analysis.util.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public void insertBeforeDelete(List<UserAnalysisResultDto> userAnalysisResultDtoList) {
        String functionName = className + ".insertBeforeDelete";

        if(userAnalysisResultDtoList == null || userAnalysisResultDtoList.size() == 0) {
            System.out.println("warn, function = " + functionName + ", message = (userAnalysisResultDtoList == null || userAnalysisResultDtoList.size() == 0) = true");
            return;
        }

        Set<UserAnalysisResultQuery> userAnalysisResultQuerySet = new HashSet<UserAnalysisResultQuery>();
        for(UserAnalysisResultDto item : userAnalysisResultDtoList) {
            UserAnalysisResultQuery query = new UserAnalysisResultQuery();
            query.setType(item.getType());
            query.setStartTime(item.getStartTime());
            userAnalysisResultQuerySet.add(query);
        }

        // 逻辑删除已存在的数据(startTime相等 && type相等)
        this.delete(userAnalysisResultQuerySet);

        this.insert(userAnalysisResultDtoList);
    }

    private void delete(Set<UserAnalysisResultQuery> querySet) {
        String functionName = className + ".delete";

        if(querySet == null || querySet.size() == 0) {
            System.out.println("warn, function = " + functionName + ", message = (querySet == null || querySet.size() == 0) = true");
            return;
        }
        querySet.forEach(item -> this.delete(item));
    }

    private void delete(UserAnalysisResultQuery query) {
        String functionName = className + ".delete";

        if(query == null || query.getType() == null || query.getStartTime() == null) {
            System.out.println("warn, function = " + functionName + ", message = (query == null || query.getType() == null || query.getStartTime() == null) = true");
            System.out.println("info, function = " + functionName + ", message = query = " + query);
            System.out.println("info, function = " + functionName + ", message = query.getType() = " + query.getType());
            System.out.println("info, function = " + functionName + ", message = query.getStartTime() = " + query.getStartTime());
            return;
        }

        List<Long> idList = this.select(query);
        this.delete(idList);
    }

    //执行逻辑删除
    private void delete(List<Long> idList) {
        String functionName = className + ".delete";
        if(idList == null || idList.size() == 0) {
            System.out.println("warn, function = " + functionName + ", (idList == null || idList.size() == 0) = true");
            return;
        }

        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String sql = "update bigdata_user_analysis_result set is_deleted = 1 where id = ?";
            Object params[][] = new Object[idList.size()][];
            for (int i = 0; i < idList.size(); i++) {
                Long id = idList.get(i);
                params[i] = new Object[] {id};
            }
            qr.batch(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //获取需要删除的数据
    @SuppressWarnings("deprecation")
    private List<Long> select(UserAnalysisResultQuery query) {
        String functionName = className + ".select";

        if(query == null || query.getType() == null || query.getStartTime() == null) {
            System.out.println("warn, function = " + functionName + ", (query == null || query.getType() == null || query.getStartTime() == null) = true");
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Long> list = null;
        String sql = "select id from bigdata_user_analysis_result where start_time = ? and type = ?";
        Object[] params = new Object[] {simpleDateFormat.format(query.getStartTime()), query.getType()};
        System.out.println("sql = " + sql);
        System.out.println("params = " + params);
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            list = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
