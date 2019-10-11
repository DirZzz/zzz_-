package com.sandu.analysis.biz.tob.plan.dao.impl;

import com.sandu.analysis.biz.tob.plan.dao.PlanAnalysisResultDao;
import com.sandu.analysis.biz.tob.plan.model.PlanAnalysisResultDto;
import com.sandu.analysis.biz.tob.plan.model.PlanAnalysisResultQuery;
import com.sandu.analysis.util.JdbcUtils;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class PlanAnalysisResultDaoImpl implements PlanAnalysisResultDao {

	private String className = this.getClass().getName();

    @Override
    public void insertByDay(List<PlanAnalysisResultDto> list) {
		String functionName = className + ".insert";
		
		if(list == null || list.size() == 0) {
			System.out.println("warn, function = " + functionName + ", message = (list == null || list.size() == 0) = true");
			return;
		}
		String sql = "insert into bigdata_recommended_plan_statistics_2b_day "
                + "(start_time, end_time, new_plan_count, use_plan_count, plan_type, "
                + "design_style_id, space_common_type, company_id, plan_source, "
                + "is_deleted, creator, gmt_create, MODIFIER, gmt_modified, remark)"
                + " values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // ==========设置参数 ->start
        Object[][] params = new Object[list.size()][];
        for (int index = 0; index < list.size(); index++) {
            PlanAnalysisResultDto planAnalysisResultDto = list.get(index);
            params[index] = new Object[] {
                    simpleDateFormat.format(planAnalysisResultDto.getStartTime()), simpleDateFormat.format(planAnalysisResultDto.getEndTime()), planAnalysisResultDto.getNewPlanCount(),
                    planAnalysisResultDto.getUsePlanCount(), planAnalysisResultDto.getPlanType(), planAnalysisResultDto.getDesignStyleId(),
                    planAnalysisResultDto.getSpaceCommonType(), planAnalysisResultDto.getCompanyId(), planAnalysisResultDto.getPlanSource(), planAnalysisResultDto.getIsDeleted(),
                    planAnalysisResultDto.getCreator(), simpleDateFormat.format(planAnalysisResultDto.getGmtCreate()), planAnalysisResultDto.getModifier(),simpleDateFormat.format(planAnalysisResultDto.getGmtModified()),planAnalysisResultDto.getRemark()
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
    public void deleteByDay(PlanAnalysisResultQuery query) {
        List<Long> ids = this.searchIdsByDay(query);
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String sql = "update bigdata_recommended_plan_statistics_2b_day set is_deleted = 1 where id = ?";
            Object params[][] = new Object[ids.size()][];
            for (int i = 0; i < ids.size(); i++) {
                Long id = ids.get(i);
                params[i] = new Object[] {id};
            }
            qr.batch(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Long> searchIdsByDay(PlanAnalysisResultQuery query) {
        //TODO:异常判断与处理
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Long> list = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("select id from bigdata_recommended_plan_statistics_2b_day where start_time = ? and end_time = ? and is_deleted =0");

        String sql = buffer.toString();
        Object[] params = new Object[] {simpleDateFormat.format(query.getStartTime()),simpleDateFormat.format(query.getEndTime())};

        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            list = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }



    @Override
    public void insertByHour(List<PlanAnalysisResultDto> list) {
        String functionName = className + ".insert";

        if(list == null || list.size() == 0) {
            System.out.println("warn, function = " + functionName + ", message = (list == null || list.size() == 0) = true");
            return;
        }
        String sql = "insert into bigdata_recommended_plan_statistics_2b_hour "
                + "(start_time, end_time, new_plan_count, use_plan_count, plan_type, "
                + "design_style_id, space_common_type, company_id, plan_source, "
                + "is_deleted, creator, gmt_create, MODIFIER, gmt_modified, remark)"
                + " values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // ==========设置参数 ->start
        Object[][] params = new Object[list.size()][];
        for (int index = 0; index < list.size(); index++) {
            PlanAnalysisResultDto planAnalysisResultDto = list.get(index);
            params[index] = new Object[] {
                    simpleDateFormat.format(planAnalysisResultDto.getStartTime()), simpleDateFormat.format(planAnalysisResultDto.getEndTime()), planAnalysisResultDto.getNewPlanCount(),
                    planAnalysisResultDto.getUsePlanCount(), planAnalysisResultDto.getPlanType(), planAnalysisResultDto.getDesignStyleId(),
                    planAnalysisResultDto.getSpaceCommonType(), planAnalysisResultDto.getCompanyId(), planAnalysisResultDto.getPlanSource(), planAnalysisResultDto.getIsDeleted(),
                    planAnalysisResultDto.getCreator(), simpleDateFormat.format(planAnalysisResultDto.getGmtCreate()), planAnalysisResultDto.getModifier(),simpleDateFormat.format(planAnalysisResultDto.getGmtModified()),planAnalysisResultDto.getRemark()
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
    public void deleteByHour(PlanAnalysisResultQuery query) {
        List<Long> ids = this.searchIdsByHour(query);
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String sql = "update bigdata_recommended_plan_statistics_2b_hour set is_deleted = 1 where id = ?";
            Object params[][] = new Object[ids.size()][];
            for (int i = 0; i < ids.size(); i++) {
                Long id = ids.get(i);
                params[i] = new Object[] {id};
            }
            qr.batch(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Long> searchIdsByHour(PlanAnalysisResultQuery query) {
        //TODO:异常判断与处理
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Long> list = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("select id from bigdata_recommended_plan_statistics_2b_hour where start_time = ? and end_time = ? and is_deleted =0");

        String sql = buffer.toString();
        Object[] params = new Object[] {simpleDateFormat.format(query.getStartTime()),simpleDateFormat.format(query.getEndTime())};

        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            list = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


}
