package com.sandu.analysis.biz.otherEvent.model;

import java.util.Date;

import lombok.Data;

@Data
public class OtherEventAnalysisResultDO {

    /**
     * bigdata_other_event_analysis_result.id
     */
    private Long id;

    /**
     * 事件标识<p>
     * bigdata_other_event_analysis_result.event_code
     */
    private String eventCode;

    /**
     * 事件执行次数<p>
     * bigdata_other_event_analysis_result.pv
     */
    private Integer pv;

    /**
     * 事件执行人数<p>
     * bigdata_other_event_analysis_result.uv
     */
    private Integer uv;

    /**
     * 开始时间<p>
     * bigdata_other_event_analysis_result.start_time
     */
    private Date startTime;

    /**
     * 结束时间<p>
     * bigdata_other_event_analysis_result.end_time
     */
    private Date endTime;

    /**
     * 应用app_id<p>
     * bigdata_other_event_analysis_result.app_id
     */
    private String appId;

    /**
     * 渠道<p>
     * bigdata_other_event_analysis_result.channel
     */
    private String channel;

    /**
     * 创建者<p>
     * bigdata_other_event_analysis_result.creator
     */
    private String creator;

    /**
     * 创建时间<p>
     * bigdata_other_event_analysis_result.gmt_create
     */
    private Date gmtCreate;

    /**
     * 更新者<p>
     * bigdata_other_event_analysis_result.modifier
     */
    private String modifier;

    /**
     * 修改时间<p>
     * bigdata_other_event_analysis_result.gmt_modified
     */
    private Date gmtModified;

    /**
     * 逻辑删除字段, 0=正常, 1=已删除<p>
     * bigdata_other_event_analysis_result.is_deleted
     */
    private Integer isDeleted;

    /**
     * 备注<p>
     * bigdata_other_event_analysis_result.remark
     */
    private String remark;
	
}
