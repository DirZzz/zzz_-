package com.sandu.api.product.output;

import com.sandu.api.category.model.bo.CategoryTreeNode;
import com.sandu.api.product.model.ProCtopsBO;
import com.sandu.api.product.model.bo.CeilingCrossSectionDataDTO;
import com.sandu.api.product.model.bo.MergeProductBO;
import com.sandu.api.product.model.bo.PicInfo;
import com.sandu.api.product.model.bo.ProductPropBO;
import com.sandu.api.resmodel.model.bo.HardProductModelBO;
import com.sandu.api.resmodel.model.bo.ModelAreaBO;
import com.sandu.api.resmodel.output.ResModelVO;
import com.sandu.api.restexture.model.bo.ProductTextureVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sandu
 */
@Data
public class ProductVO implements Serializable {
    @ApiModelProperty(value = "产品id")
    private Long id;
    @ApiModelProperty(value = "产品编码")
    private String code;
    @ApiModelProperty(value = "产品名称")
    private String name;
    @ApiModelProperty(value = "产品型号")
    private String modelNumber;
    @ApiModelProperty(value = "品牌ID")
    private Integer brandId;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "所属分类编码" ,hidden = true)
    private List<String> categoryCodes;
    @ApiModelProperty(value = "所属分类编码名称",hidden = true)
    private String categoryCodeNames;
    @ApiModelProperty(value = "产品风格")
    private List<Integer> baseStyleId;
    @ApiModelProperty(value = "产品风格名称")
    private List<String> baseStyleName;
    @ApiModelProperty(value = "产品款式")
    private Integer modelingId;
    @ApiModelProperty(value = "产品款式名称")
    private String modelingName;
    @ApiModelProperty(value = "产品系列")
    private Integer seriesId;
    @ApiModelProperty(value = "产品系列名称")
    private String seriesName;
    @ApiModelProperty(value = "建议售价")
    private Double advicePrice;
    @ApiModelProperty(value = "售价")
    private Double price;
    @ApiModelProperty(value = "售价单位")
    private Integer saleUnitId;
    @ApiModelProperty(value = "售价单位名称")
    private String saleUnitName;
    @ApiModelProperty(value = "产品描述")
    private String desc;
    @ApiModelProperty(value = "产品缩略图图片")
    private String defaultPicPath;
    @ApiModelProperty(value = "产品缩略图ID")
    private String defaultPicId;
    @ApiModelProperty(value = "产品所有图片ID,以逗号分隔")
    private String picIds;
    @ApiModelProperty(value = "产品属性")
    private List<ProductPropBO> props;

    @ApiModelProperty(value = "图片信息")
    private List<PicInfo> picInfos;

    @ApiModelProperty(value = "模型产品的贴图详情")
    private List<ModelAreaBO> modelTextureInfo;

    @ApiModelProperty(value = "贴图产品贴图详情")
    private ProductTextureVO texture;

    @ApiModelProperty(value = "线上分配状态(1:已分配/0:未分配)")
    /** 线上分配状态(1:已分配/0:未分配) */
    private Integer onlineAllotStatus;

    @ApiModelProperty(value = "渠道分配状态(1:已分配/0:未分配)")
    /** 渠道分配状态(1:已分配/0:未分配) */
    private Integer channelAllotStatus;

    @ApiModelProperty(value = "判断产品类型/true为模型,false为贴图")
    private boolean checkIsModel;

    @ApiModelProperty(value = "模型信息")
    private ResModelVO model;

    @ApiModelProperty(value = "产品宽")
    private String width;

    @ApiModelProperty(value = "产品长")
    private String length;

    @ApiModelProperty(value = "产品高")
    private String height;

    @ApiModelProperty(value = "产品分类信息")
    private List<CategoryTreeNode> categoryNodes;

    @ApiModelProperty(value = "产品属性")
    private String attrNames;

    @ApiModelProperty(value = "小类编码")
    private String smallType;

    /**
     * 硬装产品模型信息
     * Map<productId,ModelInfo> </>
     */
    private List<HardProductModelBO> hardProductModelInfo;

    @ApiModelProperty(value = "产品规格 ")
    private String spec;

    @ApiModelProperty("是否单独创建")
    private Integer isCreatedTexture;

    @ApiModelProperty("材质信息")
    private String materialPicIds;

    private Integer isComplexParquet;
    /**
     * 床沿尺寸
     * */
    private double bedsideDimensions;
    private Integer isBedsideDimensions = 0;

    /**
     * 装修单价
     */
    private String decorationPrice;

    /**
     * 计价单位
     */
    private String valuationUnit;

    private ProCtopsBO proCtopsBO;

    private List<CeilingCrossSectionDataDTO> ceilingCrossSectionDataDTOList;

    /**
     * 合并产品
     */
    private List<MergeProductBO> mergeProducts;

    /**
     * 是否有特效(0:无,1:有)
     * @author xiaoxc
     * @date 2019.08.26
     */
    private Integer isSpecialEffects;
}