package com.sandu.analysis.biz.tob.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto implements Serializable{
    private static final long serialVersionUID = 2857806454640497345L;
    /** 用户类型 **/
    private Integer userType;
    /** 用户使用类型 使用类型（0：试用、1：正式） **/
    private Integer useType;
    /** 用户 UUid**/
    private String uuid;
    /** 企业id **/
    private Integer companyId;
    /** 省编码 **/
    private String provinceCode;
    /**省名称**/
    private String provinceName;
    /** 市编码 **/
    private String cityCode;
    /** 市名称 **/
    private String cityName;

    /**
     * 累积用户数
     */
    private Integer accountCount;
    /**
     * 未激活用户数
     */
    private Integer nonactivatedUserCount;
    /**
     *  新增用户数
     */
    private Integer newUserCount;

    /**
     * 每小时(eg:2019-06-05 01、2019-06-05 02)
     */
    private String perHour;

    /**
     * 创建时间
     */
    private String gmtCreate;
}
