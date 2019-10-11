package com.sandu.analysis.biz.funnel.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.sandu.analysis.biz.funnel.dao.FunnelDao;
import com.sandu.analysis.biz.funnel.model.BigdataFunnelAnalysisResultDO;
import com.sandu.analysis.biz.funnel.model.BigdataFunnelAnalysisResultQuery;
import com.sandu.analysis.biz.funnel.model.BigdataFunnelDetailBO;
import com.sandu.analysis.util.JdbcUtils;

public class FunnelDaoImpl implements FunnelDao {

	@Override
	public void insertBeforeDelete(List<BigdataFunnelAnalysisResultDO> list) {
		if(list == null || list.size() == 0) {
			System.out.println("warn, function = FunnelDao.insertBeforeDelete, message = (list == null || list.size() == 0) = true");
			return;
		}
		
		Set<BigdataFunnelAnalysisResultQuery> bigdataFunnelAnalyzeResultQuerySet = new HashSet<BigdataFunnelAnalysisResultQuery>();
		for(BigdataFunnelAnalysisResultDO item : list) {
			BigdataFunnelAnalysisResultQuery bigdataFunnelAnalyzeResultQuery = new BigdataFunnelAnalysisResultQuery();
			bigdataFunnelAnalyzeResultQuery.setEndTime(item.getEndTime());
			bigdataFunnelAnalyzeResultQuery.setFunnelId(item.getFunnelId());
			bigdataFunnelAnalyzeResultQuery.setStartTime(item.getStartTime());
			bigdataFunnelAnalyzeResultQuerySet.add(bigdataFunnelAnalyzeResultQuery);
		}
		
		// 逻辑删除已存在的数据(startTime相等 && endTime相等 && funnel_id相等)
		this.delete(bigdataFunnelAnalyzeResultQuerySet);
		
		this.insert(list);
		
	}

	private void delete(Set<BigdataFunnelAnalysisResultQuery> querySet) {
		if(querySet == null || querySet.size() == 0) {
			System.out.println("warn, function = FunnelDao.delete, message = (querySet == null || querySet.size() == 0) = true");
			return;
		}
		querySet.forEach(item -> this.delete(item));
	}

	private void delete(BigdataFunnelAnalysisResultQuery query) {
		if(query == null || query.getEndTime() == null || query.getFunnelId() == null || query.getStartTime() == null) {
			System.out.println("warn, function = FunnelDao.delete, message = (query == null || query.getEndTime() == null || query.getFunnelId() == null || query.getStartTime() == null) = true");
			System.out.println("info, function = FunnelDao.delete, message = query = " + query);
			System.out.println("info, function = FunnelDao.delete, message = query.getEndTime() = " + query.getEndTime());
			System.out.println("info, function = FunnelDao.delete, message = query.getFunnelId() = " + query.getFunnelId());
			System.out.println("info, function = FunnelDao.delete, message = query.getStartTime() = " + query.getStartTime());
			return;
		}
		
		List<Long> idList = this.select(query);
		
		this.delete(idList);
	}

	private void delete(List<Long> idList) {
		if(idList == null || idList.size() == 0) {
			System.out.println("warn, function = FunnelDao.delete, (idList == null || idList.size() == 0) = true");
			return;
		}
		
		try {
			QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "update bigdata_funnel_analysis_result set is_deleted = 1 where id = ?";
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

	@SuppressWarnings("deprecation")
	@Override
	public List<Long> select(BigdataFunnelAnalysisResultQuery query) {
		if(query == null || query.getEndTime() == null || query.getFunnelId() == null || query.getStartTime() == null) {
			System.out.println("warn, function = FunnelDao.select, (query == null || query.getEndTime() == null || query.getFunnelId() == null || query.getStartTime() == null) = true");
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<Long> list = null;
		try {
			QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
			/*String sql = "select id from bigdata_funnel_analysis_result "
					+ "where funnel_id = " + query.getFunnelId() 
					+ " and start_time = \"" + simpleDateFormat.format(query.getStartTime()) 
					+ "\" and end_time = \"" + simpleDateFormat.format(query.getEndTime()) + "\"";
			list = qr.query(sql, new BeanListHandler<Long>(Long.class));*/
			
			String sql = "select id from bigdata_funnel_analysis_result where funnel_id = ? and start_time = ? and end_time = ?";
			Object[] params = new Object[] {query.getFunnelId(), simpleDateFormat.format(query.getStartTime()), simpleDateFormat.format(query.getEndTime())};
			list = qr.query(sql, params, new ColumnListHandler<>("id")).stream().map(item -> Long.valueOf(item.toString())).collect(Collectors.toList());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	private void insert(List<BigdataFunnelAnalysisResultDO> list) {
		if(list == null || list.size() == 0) {
			System.out.println("warn, function = FunnelDao.delete, (list == null || list.size() == 0) = true");
			return;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
			String sql = "insert into bigdata_funnel_analysis_result (start_time, end_time, " + 
					"funnel_id, node_id, node_name, " + 
					"node_uv, node_pv, node_seq, " + 
					"creator, gmt_create, modifier, " + 
					"gmt_modified, is_deleted, remark, channel, node_npv, node_nuv" + 
					") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			Object params[][] = new Object[list.size()][];
			for (int i = 0; i < list.size(); i++) {
				BigdataFunnelAnalysisResultDO bigdataFunnelAnalyzeResultDO = list.get(i);
				params[i] = new Object[] {
						simpleDateFormat.format(bigdataFunnelAnalyzeResultDO.getStartTime()), simpleDateFormat.format(bigdataFunnelAnalyzeResultDO.getEndTime()), 
						bigdataFunnelAnalyzeResultDO.getFunnelId(), bigdataFunnelAnalyzeResultDO.getNodeId(),
						bigdataFunnelAnalyzeResultDO.getNodeName(), bigdataFunnelAnalyzeResultDO.getNodeUv(), 
						bigdataFunnelAnalyzeResultDO.getNodePv(), bigdataFunnelAnalyzeResultDO.getNodeSeq(), 
						bigdataFunnelAnalyzeResultDO.getCreator(), simpleDateFormat.format(bigdataFunnelAnalyzeResultDO.getGmtCreate()), 
						bigdataFunnelAnalyzeResultDO.getModifier(), simpleDateFormat.format(bigdataFunnelAnalyzeResultDO.getGmtModified()), 
						bigdataFunnelAnalyzeResultDO.getIsDeleted(), bigdataFunnelAnalyzeResultDO.getRemark(),
						bigdataFunnelAnalyzeResultDO.getChannel(), bigdataFunnelAnalyzeResultDO.getNodeNpv(),
						bigdataFunnelAnalyzeResultDO.getNodeNuv()
						};
			}
			qr.batch(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<BigdataFunnelDetailBO> selectFromBigdataFunnelDetailBOwhereEffective() {
		String sql = "select bfd.id,bfd.node_name,bfd.node_seq,bfd.node_event_type,bfd.node_event_property,bfd.funnel_id,bf.app_id "
				+ "from bigdata_funnel bf left join bigdata_funnel_detail bfd on bfd.funnel_id = bf.id "
				+ "where bf.status = 0 and bf.is_deleted = 0 and bfd.is_deleted = 0";
		
		List<BigdataFunnelDetailBO> list = null;
		try {
			QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
			BeanProcessor bean = new GenerousBeanProcessor();
			RowProcessor processor = new BasicRowProcessor(bean);
			list = qr.query(sql, new BeanListHandler<BigdataFunnelDetailBO>(BigdataFunnelDetailBO.class, processor));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
