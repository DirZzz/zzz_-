package com.sandu.api.statistics.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: chenqiang
 * @create: 2019-05-27 14:09
 */
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 4185210014576558452L;

    private Integer id;

    private Integer userType;

    private Integer businessAdministrationId;

    private Integer companyId;
}
