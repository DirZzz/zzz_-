package com.sandu.analysis.biz.usersRetention.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserRetentionResultDO implements Serializable {

	private static final long serialVersionUID = 5704777121927377928L;

	/**
     * bigdata_user_retention_result.id
     */
    private Long id;

    /**
     * 开始时间<p>
     * bigdata_user_retention_result.start_time
     */
    private Date startTime;

    /**
     * 结束时间<p>
     * bigdata_user_retention_result.end_time
     */
    private Date endTime;

    /**
     * 新增人数<p>
     * bigdata_user_retention_result.new_user_count
     */
    private Integer newUserCount;

    /**
     * 平均访问页面数<p>
     * bigdata_user_retention_result.average_pv
     */
    private Integer averagePv;

    /**
     * 次日留存用户数<p>
     * bigdata_user_retention_result.one_day_retention_count
     */
    private Integer oneDayRetentionCount;

    /**
     * 3日留存用户数<p>
     * bigdata_user_retention_result.three_day_retention_count
     */
    private Integer threeDayRetentionCount;

    /**
     * 7日留存用户数<p>
     * bigdata_user_retention_result.seven_day_retention_count
     */
    private Integer sevenDayRetentionCount;

    /**
     * 30日留存用户数<p>
     * bigdata_user_retention_result.thirty_day_retention_count
     */
    private Integer thirtyDayRetentionCount;

    /**
     * 应用app_id<p>
     * bigdata_user_retention_result.app_id
     */
    private String appId;

    /**
     * 渠道<p>
     * bigdata_user_retention_result.channel
     */
    private String channel;

    /**
     * 创建者<p>
     * bigdata_user_retention_result.creator
     */
    private String creator;

    /**
     * 创建时间<p>
     * bigdata_user_retention_result.gmt_create
     */
    private Date gmtCreate;

    /**
     * 更新者<p>
     * bigdata_user_retention_result.modifier
     */
    private String modifier;

    /**
     * 修改时间<p>
     * bigdata_user_retention_result.gmt_modified
     */
    private Date gmtModified;

    /**
     * 逻辑删除字段, 0=正常, 1=已删除<p>
     * bigdata_user_retention_result.is_deleted
     */
    private Integer isDeleted;

    /**
     * 备注<p>
     * bigdata_user_retention_result.remark
     */
    private String remark;
    
}
