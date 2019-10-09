package com.sandu.api.solution.input;

import com.sandu.base.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * DesignPlanRecommendedQuery class
 *
 * @author bvvy
 * @date 2018/04/02
 */
@Data
public class DesignPlanRecommendedQuery extends BaseQuery implements Serializable {


    @ApiModelProperty("方案ID")
    private Integer id;

    @ApiModelProperty("来源")
    @Length(max = 30,message = "最长 {max}")
    private String origin;

    @ApiModelProperty("是否公开")
    @Min(value = 0, message = "公开参数不正确")
    @Max(value = 1,message = "公开参数不合法")
    private Integer isOpen;

    @ApiModelProperty("方案名称")
    @Length(max = 50,message = "方案名称最长{max}")
    private String planName;

    @ApiModelProperty("分配情况")
    @Length(max = 20,message = "分配情况最长{max}")
    private String distributionStatus;

    @ApiModelProperty("包含为空开产品")
    @Length(max = 10,message = "包含为空开产品最长{max}")
    private String containsSecrecyFlag;

    @ApiModelProperty("上架平台")
    @Length(max = 20,message = "上架平台最长{max}")
    private String platformId;
    
    private String platformBussinessType;
    
    @ApiModelProperty(value = "公司id",required = true)
    @Min(value = 1, message = "公司id不正确")
    @NotNull(message = "公司id不能为空")
    private Integer companyId;


    @ApiModelProperty("上架状态")
    @Length(max = 20,message = "上架状态最长{max}")
    private String shelfStatus;

    @ApiModelProperty("交付状态 Y ,N")
    @Length(max = 1)
    private String deliverStatus;


    @ApiModelProperty(value = "品牌,后台设置进去",hidden = true)
    private List<Integer> brandIds;

    @ApiModelProperty(value = "推介类型,后台设置进去",hidden = true)
    private Integer recommendedType;

    @ApiModelProperty(value = "空间类型名称")
    @Length(max = 30,message = "空间类型不能超过{max}")
    private String spaceCommonName;

    @ApiModelProperty("对应空间类型id")
    private Integer spaceTypeId;

    @ApiModelProperty("该店铺方案是否发布0未发布 1已发布")
    private Integer isPublish;

    @ApiModelProperty("方案品牌")
    private String brandName;

    @ApiModelProperty("方案品牌id")
    private Integer brandId;

    @ApiModelProperty("店铺ID")
    private Integer shopId;

    @ApiModelProperty("方案是否公开:0.未公开,1.公开")
    private Integer openState;

    @ApiModelProperty("方案售卖价格是否收费")
    private Integer salePriceChargeType;

    @ApiModelProperty("方案版权费是否免费")
    private Integer chargeType;

    @ApiModelProperty("方案风格ID")
    private Integer planStyleId;

    private String planGroupStyleId;
    private List<Integer> listGroupStyleId;

    ////-- create by zhoujc at  2019/2/22 10:00.
    //-- 运营管理 > 推荐方案管理字段

    @ApiModelProperty("运营管理标识")
    private Boolean managerSXW;

    @ApiModelProperty("运营管理 > 方案ID")
    private Integer planId;

    @ApiModelProperty("运营管理 > 首页展示")
    private Integer showInSXWIndexFlag;

    @ApiModelProperty("运营管理 > 推荐")
    private Integer showInSXWFlag;

    @ApiModelProperty("运营管理 > 店铺信息")
    private Integer shopIn720Page;

    @ApiModelProperty("运营管理 > 店铺名称")
    private String shopName;
    @ApiModelProperty("运营管理 > 企业名称")
    private String companyName;

    //-- update by wangw on 2019/5/30 to CMS-744
    @ApiModelProperty("上架开始时间")
    private String startTime;
    @ApiModelProperty("上架结束时间")
    private String endTime;
    @ApiModelProperty("设计师id")
    private String designerUserId;

    @ApiModelProperty("查询类型：group:组合方案、single:单空间方案")
    @Pattern(regexp = "(single|group)")
    private String queryType;

    @ApiModelProperty("适用面积")
    private String applyAreaValue;


}
