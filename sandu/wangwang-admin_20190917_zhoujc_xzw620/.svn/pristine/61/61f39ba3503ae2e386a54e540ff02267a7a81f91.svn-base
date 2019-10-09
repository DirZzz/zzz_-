package com.sandu.api.basesupplydemand.output;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * supply_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-Oct-20 10:46
 */
@Data
public class BasesupplydemandVO implements Serializable {



    @ApiModelProperty(value = "id")
    private Integer id;
        
    @ApiModelProperty(value = "信息类别(1:供应，2:需求)")
    private Integer type;
        
    @ApiModelProperty(value = "信息分类")
    private String supplyDemandCategoryId;

    @ApiModelProperty(value = "分类名称")
    private String supplyDemandCategoryName;

    @ApiModelProperty(value = "省编码")
    private String province;
    @ApiModelProperty(value = "市编码")
    private String city;
    @ApiModelProperty(value = "区编码")
    private String district;
    @ApiModelProperty(value = "街道编码")
    private String street;
    @ApiModelProperty(value = "省编码")
    private String provinceName;
    @ApiModelProperty(value = "市编码")
    private String cityName;
    @ApiModelProperty(value = "区编码")
    private String districtName;
    @ApiModelProperty(value = "街道编码")
    private String streetName;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "封面图id")
    private String coverPicId;

    @ApiModelProperty(value = "封面图路径")
    private String coverPicPath;

    @ApiModelProperty(value = "封面图路径集")
    private List<String> coverPicPaths;

    @ApiModelProperty(value = "信息标题")
    private String title;
        
        
    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;

    @ApiModelProperty(value = "「推荐」")
    private Long recommendedTime;

    private Date recommendedDate;

    private Integer[] categoryId;


    @ApiModelProperty("浏览次数")
    private Integer viewCount;

    @ApiModelProperty("点赞 次数")
    private Integer likeCount;
    private Integer virtualViewCount;
    private Integer virtualLikeCount;

    @ApiModelProperty("方案名称")
    private String planName;

    @ApiModelProperty("户型名称")
    private String houseName;

    private Map<Integer, String> planInfo;
    private Map<Integer, String> houseInfo;

    @ApiModelProperty("是否为新数据")
    private Integer newDataFlag;
}
