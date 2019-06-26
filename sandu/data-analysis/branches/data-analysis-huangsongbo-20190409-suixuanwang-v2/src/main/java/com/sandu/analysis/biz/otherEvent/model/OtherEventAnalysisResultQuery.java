package com.sandu.analysis.biz.otherEvent.model;

import java.util.Date;

import lombok.Data;

@Data
public class OtherEventAnalysisResultQuery {

    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
	
}
