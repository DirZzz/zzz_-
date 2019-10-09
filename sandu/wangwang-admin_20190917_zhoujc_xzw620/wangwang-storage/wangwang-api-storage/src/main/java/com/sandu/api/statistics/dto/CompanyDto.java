package com.sandu.api.statistics.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: chenqiang
 * @create: 2019-05-27 15:01
 */
@Data
public class CompanyDto implements Serializable {
    private static final long serialVersionUID = 1089969962294825190L;

    private Integer id;

    private Integer pid;

    private Integer businessType;

    private String brandIds;
}
