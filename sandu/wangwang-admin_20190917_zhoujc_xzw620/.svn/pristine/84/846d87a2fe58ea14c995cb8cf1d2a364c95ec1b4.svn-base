package com.sandu.api.basesupplydemand.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * supply_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-10-20 10:46
 */
@Data
public class BasesupplydemandUpdate implements Serializable {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "信息类别(1:供应，2:需求)")
    private Integer type;

    @ApiModelProperty(value = "信息分类")
    private String supplyDemandCategoryId;

    @ApiModelProperty(value = "信息标题")
    private String title;

    @ApiModelProperty(value = "发布者")
    private String creator;

    @ApiModelProperty(value = "省编码")
    private String province;
    @ApiModelProperty(value = "市编码")
    private String city;
    @ApiModelProperty(value = "区编码")
    private String district;
    @ApiModelProperty(value = "街道编码")
    private String street;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "信息描述")
    private String description;

    @ApiModelProperty(value="动态时间")
    private Date gmtModified;

    @ApiModelProperty(value="级联id")
    private Integer[] categoryId;


    private String coverPicId;
    /**
     * 浏览次数
     */
    @Min(0)
    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;
    private Integer virtualViewCount;
    /**
     * 点赞次数
     */
    @Min(0)
    @ApiModelProperty(value = "点赞次数")
    private Integer likeCount;
    private Integer virtualLikeCount;

    private List<Integer> houseIds;
    private List<Integer> planIds;

    @ApiModelProperty(value = "是否为新数据")
    private Integer newDataFlag;

    //数据迁移标识 0: 不迁移 , 1.迁移
    private Integer moveDataFlag;
}
