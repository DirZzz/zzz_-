package com.sandu.api.solution.output;

import com.sandu.api.solution.model.bo.DesignPlanDeliverInfoBO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DesignPlanExcelDto implements Serializable{
    private Long planId;

    private String picPath;

    private byte[] picBytes;

    @ApiModelProperty("方案编号")
    private String planCode;//编码

    @ApiModelProperty("方案名称")
    private String planName;//名称

    @ApiModelProperty("对应空间类型名称")
    private String spaceTypeName;//空间类型

    @ApiModelProperty("品牌名称")
    private String brandName;//品牌

    @ApiModelProperty("设计风格名称")
    private String designStyleName;//风格

    @ApiModelProperty("组合方案/单个方案")
    private String solutionType;//方案类型

    @ApiModelProperty("设计师")
    private String designer;//设计师

    @ApiModelProperty("上架时间")
    private Date putawayTime;//上架时间
    private String putawayTimeStr;

    @ApiModelProperty("来源")
    private String origin;//来源

    @ApiModelProperty(value = "方案企业(名称)")
    private String deliverName;//方案企业(名称)

    @ApiModelProperty("完成时间")
    private Date completeDate;//入库时间
    private String completeDateStr;//入库时间

    @ApiModelProperty(value = "方案交付的企业和品牌")
    private List<DesignPlanDeliverInfoBO> delivers;

    //全屋方案
    @ApiModelProperty("方案来源名称")
    private String sourceName;
    @ApiModelProperty("设计师名称")
    private String userName;
    @ApiModelProperty("方案风格名称")
    private String planStyleName;
    @ApiModelProperty("创建时间")
    private Date gmtCreate;
}
