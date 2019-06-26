package com.sandu.analysis.biz.tob.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserStatistics2bDayDto
 * @Description B端用户统计结果(每天)
 * @Author chenm
 * @Date 2019/5/30 19:49
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatistics2bDayDto implements Serializable {
    private static final long serialVersionUID = -4054712969722009410L;

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
     *  总活跃用户数
     */
    private Integer activeUserCount;
    /**
     *  新增用户数
     */
    private Integer newUserCount;

    /**
     *  用户类型
     */
    private Integer userType;

    /**
     *  账号使用类型(0:试用 1:正式)
     */
    private Integer useType;

    /**
     *  登录pc端用户数
     */
    private Integer loginUserCountPC2B;

    /**
     *  登录B端移动端用户数
     */
    private Integer loginUserCountMobile2B;

    /**
     *  登录商家后台用户数
     */
    private Integer loginUserCountMerchantManage;
    /**
     * 累积用户数
     */
    private Integer accountCount;
    /**
     * 未激活用户数
     */
    private Integer nonactivatedUserCount;
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
