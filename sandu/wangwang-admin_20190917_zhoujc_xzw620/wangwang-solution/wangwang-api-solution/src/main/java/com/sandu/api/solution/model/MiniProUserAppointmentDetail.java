package com.sandu.api.solution.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MiniProUserAppointmentDetail implements Serializable {

    private Long id;

    private Long miniProObtainMobileDetailId;

    private String userName;

    private String mobile;

    private Date appointmentTime;

    private String appointmentTitle;

    private String remake;

    private Integer status;

    private String creator;

    private Date gmtCreate;

    private String modifier;

    private Date gmtModified;

    private Integer isDeleted;

    private String state;
}
