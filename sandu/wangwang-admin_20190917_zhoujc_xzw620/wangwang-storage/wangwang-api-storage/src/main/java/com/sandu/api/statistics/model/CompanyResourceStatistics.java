package com.sandu.api.statistics.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CompanyResourceStatistics implements Serializable {
    private static final long serialVersionUID = -5241321828410558390L;

    /**
     *
     */
    private Integer id;

    /**
     * 企业id
     */
    private Integer companyId;

    /**
     * 方案渲染资源
     */
    private String planRender;

    /**
     * 方案配置资源
     */
    private String planConfig;

    /**
     * 共享方案渲染资源
     */
    private String sharePlanRender;

    /**
     * 共享图方案配置资源
     */
    private String sharePlanConfig;

    /**
     * 企业材质资源
     */
    private String texture;

    /**
     * 贴图产品资源
     */
    private String stickChartProduct;

    /**
     * 模型产品 模型资源
     */
    private String productModel;

    /**
     * 模型产品 图片资源
     */
    private String productPic;

    /**
     * 店铺资源
     */
    private String shop;

    /**
     * 店铺博文资源
     */
    private String shopBowen;

    /**
     * 店铺工程案例资源
     */
    private String shopEngineering;

    /**
     * 门店资源
     */
    private String store;

    /**
     * 账号资源
     */
    private String user;

    /**
     * 企业资源
     */
    private String company;

    /**
     * 时间
     */
    private Integer date;

    /**
     * 天
     */
    private Integer day;

    /**
     * 周
     */
    private Integer week;

    /**
     * 月
     */
    private Integer month;

    /**
     * 年
     */
    private Integer year;

    /**
     * 系统编码
     */
    private String sysCode;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 是否删除(0:未删除、1:删除)
     */
    private Integer isDeleted;

    /**
     * 备注
     */
    private String remark;
}