package com.sandu.api.statisticsUser.model;

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
public class UserStatistics  extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -4054712969722009410L;

    private int id;
    /**
     * 开始时间
     */
    private String startTime="";
    /**
     *  结束时间
     */
    private String endTime;
    /**
     *  总活跃用户数
     */
    private int activeUserCount;
    /**
     *  新增用户数
     */
    private int newUserCount;
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
    private int loginUserCountPC2B;
    /**
     *  登录B端移动端用户数
     */
    private int loginUserCountMobile2B;
    /**
     *  登录商家后台用户数
     */
    private int loginUserCountMerchantManage;
    /**
     * 累积用户数
     */
    private int accountCount;
    /**
     * 未激活用户数
     */
    private int nonactivatedUserCount;
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
    //活跃用户总数
    private int activeUserTotal;
    //新增用户总数
    private int newUserTotal;
    //登录pc端用户总数
    private int loginPc2bTotal;
    //登录移动端用户总数
    private int loginMobile2bTotal;
    //登录商家后台用户总数
    private int loginMerchantManageTotal;
    //累计总用户总数
    private int accountTotal;
    //未激活用户总数
    private int nonactivatedTotal;
    //类型：1新增 0活跃
    private Integer type;

    private Integer day;
}
