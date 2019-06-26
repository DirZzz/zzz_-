package com.sandu.analysis.biz.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class UserAnalysisResultQuery {
    private Date startTime;

    private Integer type;
}
