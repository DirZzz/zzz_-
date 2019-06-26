package com.sandu.analysis.biz.funnel.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class BigdataFunnalAnalyzeResultQuery implements Serializable {

	private static final long serialVersionUID = -6837318834212281061L;

    /**
     * 开始时间<p>
     * bigdata_funnal_analyze_result.start_time
     */
    private Date startTime;
    
    /**
     * 结束时间<p>
     * bigdata_funnal_analyze_result.end_time
     */
    private Date endTime;
    
    /**
     * 自定义漏斗id, 关联bigdata_funnal.id<p>
     * bigdata_funnal_analyze_result.funnel_id
     */
    private Long funnelId;
    
}
