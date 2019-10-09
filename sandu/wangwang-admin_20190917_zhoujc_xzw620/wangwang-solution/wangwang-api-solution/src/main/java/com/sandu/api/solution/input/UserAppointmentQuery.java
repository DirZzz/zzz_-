package com.sandu.api.solution.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserAppointmentQuery implements Serializable {

    private Integer page;

    private Integer limit;

    private String userName;

    private  String mobile;

    private String title;

    private Integer status;

    private Date appointmentTimeStart;

    private Date appointmentTimeEnd;

    private Integer companyId;

    private List<Long> obtainIds;
}
