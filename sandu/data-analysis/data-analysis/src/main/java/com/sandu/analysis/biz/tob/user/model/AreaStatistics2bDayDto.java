package com.sandu.analysis.biz.tob.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName AreaStatistics2bDayDto
 * @Description B端省市统计结果(每天)
 * @Author chenm
 * @Date 2019/5/30 19:49
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaStatistics2bDayDto implements Serializable {

    private static final long serialVersionUID = 908531684534994067L;
    private Integer id;
    /**
     * 开始时间
     */
    private String startTime;

    /**
     *  结束时间
     */
    private String endTime;
    /**
     *  省编码
     */
    private String provinceCode;
    /**
     *  省名称
     */
    private String provinceName;
    /**
     *  市编码
     */
    private String cityCode;
    /**
     *  市名称
     */
    private String cityName;
    /**
     *  总活跃用户数
     */
    private Integer activeUserCount;
    /**
     *  新增用户数
     */
    private Integer newUserCount;
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


}
