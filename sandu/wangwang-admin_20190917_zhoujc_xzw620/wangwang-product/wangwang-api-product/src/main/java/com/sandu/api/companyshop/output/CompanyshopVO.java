package com.sandu.api.companyshop.output;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * store_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-Oct-22 16:51
 */
@Data
public class CompanyshopVO implements Serializable {

    @ApiModelProperty(value = "id")
    private Integer id;

    private String shopName;

    private Integer logoPicId;

    private String logoPicPath;

    private String coverResIds;

    private Integer companyId;

    private String companyName;

    private Integer businessType;

    private String businessTypeName;

    private Date gmtModified;

    /** 「推荐」 */
    private Long recommendedTime;

    /** 「推荐」 */
    private Date recommendedDate;

    private List<String> coverPicPaths;

    private List<String> coverResPaths;

    private String creatorAccount;

    private String creatorPhone;

    private Integer isRelease;

    /**
     * 手动评分
     */
    private Double handScore;

    /**
     * 自动评分
     */
    private Double automateScore;

    /**
     * 启用评分 : 10,自动;20,手动
     */
    private Integer enableScore;

    /**
     * 区域信息名称
     */
    private String areaInfoName;

    /**
     * 用户类型
     */
    private String userTypeStr;
}
