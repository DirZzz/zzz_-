package com.sandu.analysis.biz.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaSparkContext;

import com.sandu.analysis.biz.constant.AnalysisConstants;
import com.sandu.analysis.constant.ConfigConstants;

public class UserUtils {

	/**
	 * 获取指定日期的新增用户List
	 * 
	 * @author huangsongbo 2019.07.11
	 * @param localDate
	 * @return
	 */
	public static List<String> getNewUserUuidList(LocalDate localDate, JavaSparkContext javaSparkContext) {
		String newUserHdfsFileDir = AnalysisConstants.HDFS_DOMAIN + AnalysisConstants.HDFS_NEWUSERIDENTITY_DIR+ localDate.format(DateTimeFormatter.ofPattern("yyyyMM/dd/"));
		List<String> newUserUuidList = new ArrayList<String>();
		
		if (!ConfigConstants.RUN_ENV_LOCAL.equals(AnalysisConstants.RUN_ENV) && !HadoopUtils.getIsExist(newUserHdfsFileDir)) {
			
		} else {
			// 0		1			2
			// uuid	appId	channel
			newUserUuidList = javaSparkContext.textFile(newUserHdfsFileDir)
			.map(str -> {
				String[] strs = str.split(AnalysisConstants.SPLIT_REGEX);
				return strs[0];
			})
			.distinct()
			.collect();
		}
		
		return newUserUuidList;
	}
	
}
