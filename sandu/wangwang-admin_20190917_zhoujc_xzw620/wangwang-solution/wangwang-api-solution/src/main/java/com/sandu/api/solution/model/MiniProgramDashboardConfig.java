package com.sandu.api.solution.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) http://www.sanduspace.cn. All rights reserved.
 *
 * @author :  Steve
 * @date : 2018/12/18
 * @since : sandu_yun_1.0
 */
@Data
public class MiniProgramDashboardConfig implements Serializable{

    private Long id;

    private String title;

    private List<ConfigDetail> configDetails;

    private String type;

    private List<MiniProgramDashboardConfig> configList;

    private Integer isShowHome;

    private String richContext;

    private Integer amount;

    /**
     * 手机号模块标识
     */
    private String uuid;

    private String secondTitle;

    private String mobile;

    //地区
    private String address;
}