package com.sandu.api.statisticsCompany.model;

import com.sandu.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyStatisticsDay extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -4054712969722009410L;

    private int id;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     *  结束时间
     */
    private String endTime;
    /**
     *  企业id
     */
    private Integer companyId;
    /**
     *  企业类型
     */
    private Integer companyType;
    /**
     *  企业名称
     */
    private String companyName="";
    /**
     *  品牌名称
     */
    private String brandName="";
    /**
     *  总活跃用户数
     */
    private int activeUserCount;
    /**
     *  新增用户数
     */
    private int newUserCount;
    /**
     * 开通账号人数
     */
    private int userAccountCount;
    /**
     * 使用率
     */
    private String userEffectiveRate="";
    /**
     *  创建者
     */
    private String creator;

    /**
     *  创建时间
     */
    private Date gmtCreate;

    /**
     *  修改人
     */
    private String modifier;

    /**
     *  修改时间
     */
    private Date gmtModified;

    /**
     *  逻辑删除字段(0:正常 1:已删除)
     */
    private Integer isDeleted;

    /**
     *  备注
     */
    private String remark;
    //筛选条件 时间
    private Integer time;
}
