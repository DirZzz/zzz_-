package com.sandu.analysis.biz.funnel.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigdataFunnalAnalyzeResultQuery other = (BigdataFunnalAnalyzeResultQuery) obj;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (funnelId == null) {
			if (other.funnelId != null)
				return false;
		} else if (!funnelId.equals(other.funnelId))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((funnelId == null) ? 0 : funnelId.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}
    
}
