package com.sandu.api.basesupplydemand.input;

import com.sandu.common.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * supply_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-10-20 10:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BasesupplydemandQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "信息分类")

    private String supplyDemandCategoryId;


    @ApiModelProperty(value="查询参数")
    private String queryParam;

    @ApiModelProperty(value = "最早发帖时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd:HH-mm-ss")
    private Date startTime;

    @ApiModelProperty(value = "最晚发帖时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd:HH-mm-ss")
    private Date endTime;

    @ApiModelProperty(value = "查询参数,1:供应，2:需求")
    private String type;
}
