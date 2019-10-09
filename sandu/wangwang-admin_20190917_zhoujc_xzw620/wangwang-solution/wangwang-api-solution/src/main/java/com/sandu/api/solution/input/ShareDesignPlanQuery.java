package com.sandu.api.solution.input;

import com.sandu.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
/**
 * ShareDesignPlanQuery class
 *
 * @author bvvy
 * @date 2018/04/02
 */
@Data
@ApiModel("共享方案搜索条件")
public class ShareDesignPlanQuery extends BaseQuery implements Serializable{

    @ApiModelProperty("方案ID")
    private Integer id;

    @Length(max = 30,message = "名称最长{max}")
    @ApiModelProperty("方案名称")
    private String planName;

    @ApiModelProperty("分享方案企业")
    @Min(value = 1,message = "分享企业id不能小于{value}")
    private Integer shareCompanyId;

    @ApiModelProperty("品牌id")
    private String brandId;

    @NotNull(message = "公司id不能为空")
    @Min(value = 1, message = "公司id不能小于{value}")
    @ApiModelProperty(value = "公司id", required = true)
    private Integer companyId;

    @ApiModelProperty(hidden = true)
    private List<Integer> peerCompanyIds;

    @ApiModelProperty(hidden = true)
    private Integer designPlanType;

    private Integer spaceTypeId;

    @ApiModelProperty("方案编号")
    private String planCode;

    @ApiModelProperty("设计师名称")
    private String designer;

    @ApiModelProperty(value = "方案收费类型:0.免费,1.收费")
    private Integer chargeType;

    @ApiModelProperty("模糊筛选:0.方案编号,1.方案名称,2.设计师名称")
    private String planSearch;

    private Long userId;

    @ApiModelProperty("方案售卖是免费: 0.免费,1.收费")
    private Integer salePriceChargeType;

    @ApiModelProperty("方案风格")
    private Integer planStyleId;

    private String planGroupStyleId;
    private List<Integer> listGroupStyleId;

    @ApiModelProperty("查询类型：group:组合方案、single:单空间方案")
    @Pattern(regexp = "(single|group)")
    private String queryType;

//    @ApiModelProperty("适用面积")
//    private String applyAreaValue;
}
