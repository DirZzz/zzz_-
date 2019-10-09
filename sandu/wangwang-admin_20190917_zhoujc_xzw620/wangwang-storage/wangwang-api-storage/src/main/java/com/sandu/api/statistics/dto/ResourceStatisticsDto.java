package com.sandu.api.statistics.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chenqiang
 * @create: 2019-05-23 16:06
 */
@Component
@Data
public class ResourceStatisticsDto implements Serializable {
    private static final long serialVersionUID = 4562694799678183542L;

    public static final int LIMIT_SIZE = 200;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "企业id")
    private Integer companyId;

    @ApiModelProperty(value = "经销商id")
    private Integer claimCompanyId;

    @ApiModelProperty(value = "店铺类型")
    private Integer shopType;

    @ApiModelProperty(value = "店铺图片 Id集合")
    private List<Integer> picIdList;

    @ApiModelProperty(value = "店铺文件 Id集合")
    private List<Integer> fileIdList;

    @ApiModelProperty(value = "品牌idLIst")
    private List<Integer> brandIdList;

    @ApiModelProperty(value = "品牌ids")
    private String brandIds;

    @ApiModelProperty(value = "模型idList")
    private List<Integer> modelIdList;

    @ApiModelProperty(value = "是否模型产品")
    private Integer isNotModel;

    private Integer start;

    private Integer size;
}
