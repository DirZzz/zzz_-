package com.sandu.service.goods.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sandu.api.dictionary.model.Dictionary;
import com.sandu.api.dictionary.service.DictionaryService;
import com.sandu.api.goods.input.GoodsDetailQuery;
import com.sandu.api.goods.input.GoodsSKUAdd;
import com.sandu.api.goods.input.GoodsSKUListAdd;
import com.sandu.api.goods.model.BaseGoodsSKU;
import com.sandu.api.goods.model.ConfigDetail;
import com.sandu.api.goods.model.MiniProgramDashboardConfig;
import com.sandu.api.goods.model.bo.BaseGoodsSKUBO;
import com.sandu.api.goods.model.bo.ProductAttributeBO;
import com.sandu.api.goods.model.bo.ProductSKUBO;
import com.sandu.api.goods.model.bo.PutAwayBO;
import com.sandu.api.goods.output.GoodsSKUExportVo;
import com.sandu.api.goods.output.GoodsSKUVO;
import com.sandu.api.goods.output.GoodsVO;
import com.sandu.api.goods.service.BaseGoodsSKUService;
import com.sandu.service.goods.dao.BaseGoodsSKUMapper;
import com.sandu.service.goods.dao.BaseGoodsSPUMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("baseGoodsSKUService")
public class BaseGoodsSKUServiceImpl implements BaseGoodsSKUService
{
    @Autowired
    private BaseGoodsSKUMapper baseGoodsSKUMapper;

    @Autowired
    private BaseGoodsSPUMapper baseGoodsSPUMapper;

    @Autowired
    private DictionaryService dictionaryService;

    private static Gson gson = new Gson();

    @Override
    public List<GoodsSKUVO> getGoodsSKUs(GoodsDetailQuery goodsDetailQuery)
    {
        //查询SKU表中的信息
        List<BaseGoodsSKUBO> baseGoodsSKUS = baseGoodsSKUMapper.selectBySPUId(goodsDetailQuery.getSpuId());
        //查询base_product表中的信息
        List<ProductSKUBO> productSKUS = baseGoodsSKUMapper.getProductSKUsBySPUId(goodsDetailQuery);
        List<GoodsSKUVO> goodsSKUVOS = new ArrayList<>();

        for (ProductSKUBO productSKUBO : productSKUS)
        {
            BaseGoodsSKUBO baseGoodsSKUBO = null;
            for (BaseGoodsSKUBO skuBo : baseGoodsSKUS)
            {
                if (skuBo.getProductId().equals(productSKUBO.getProductId()))
                {
                    baseGoodsSKUBO = skuBo;
                }
            }
            GoodsSKUVO goodsSKUVO = new GoodsSKUVO();
            goodsSKUVO.setModelNumber(productSKUBO.getProductModelNumber());
            goodsSKUVO.setSalePrice(productSKUBO.getSalePrice());
            goodsSKUVO.setProductId(productSKUBO.getProductId());
            goodsSKUVO.setDecorationPrice(productSKUBO.getDecorationPrice());
            goodsSKUVO.setValuationUnit("元/个");

            if (productSKUBO.getProductTypeValue() != null && productSKUBO.getProductSmallTypeValue() != null) {

                Dictionary dictionary = dictionaryService.getSmallProductTypeByValue(productSKUBO.getProductTypeValue(),productSKUBO.getProductSmallTypeValue());
                if (null != dictionary) {
                    List<Dictionary> productUnitPrices = dictionaryService.listByType("productUnitPrice");

                    if (null != productUnitPrices && productUnitPrices.size() > 0) {

                        for (Dictionary productUnitPrice : productUnitPrices) {

                            if (StringUtils.isNotBlank(productUnitPrice.getAtt3Info()) && productUnitPrice.getAtt3Info().contains(dictionary.getValuekey())) {
                                goodsSKUVO.setValuationUnit(productUnitPrice.getName());
                            }
                        }
                    }
                }
            }

            goodsSKUVO.setSpePic(baseGoodsSKUBO == null? productSKUBO.getPicPath() :
                    baseGoodsSKUBO.getSpecificationPic() == null? productSKUBO.getPicPath() : baseGoodsSKUBO.getSpecificationPic());
            goodsSKUVO.setSpePicId(baseGoodsSKUBO == null? productSKUBO.getPicId() :
                    baseGoodsSKUBO.getSpecificationPicId() == null? productSKUBO.getPicId() : baseGoodsSKUBO.getSpecificationPicId());
            goodsSKUVO.setInventory(baseGoodsSKUBO == null? 0 : baseGoodsSKUBO.getInventory());
            goodsSKUVO.setPrice(baseGoodsSKUBO == null? new BigDecimal(0) : baseGoodsSKUBO.getPrice());

            String attrValueIds = null;
            List<String> attributes = new ArrayList<>();
            List<String> tableHeads = new ArrayList<>();
            if(productSKUBO.getAttributes() == null || productSKUBO.getAttributes().size() == 0)
            {
                attrValueIds = "0";
                attributes.add("默认");
                tableHeads.add("默认");
            }else
            {
                for (ProductAttributeBO attributeBO : productSKUBO.getAttributes())
                {
                    attrValueIds = attrValueIds == null || "".equals(attrValueIds)?
                            attributeBO.getAttributeId().toString() : attrValueIds + "," + attributeBO.getAttributeId();
                    attributes.add(attributeBO.getValue());
                    tableHeads.add(attributeBO.getKey());
                }
            }
            goodsSKUVO.setTableHeads(tableHeads);
            goodsSKUVO.setAttributes(attributes);
            goodsSKUVO.setAttrValueIds(attrValueIds.toString());
            goodsSKUVOS.add(goodsSKUVO);
        }
        return goodsSKUVOS;
    }

    @Override
    public Integer updateGoodsSKU(GoodsSKUListAdd goodsSKUListAdd)
    {
        Integer number = 0;
        this.updateDashboardConfig(goodsSKUListAdd);

        for (GoodsSKUAdd goodsSKUAdd : goodsSKUListAdd.getGoodsSKUAddList())
        {
            Date currentDate = new Date();
            BaseGoodsSKU baseGoodsSKU = new BaseGoodsSKU();
            baseGoodsSKU.setProductId(goodsSKUAdd.getProductId());
            baseGoodsSKU.setInventory(goodsSKUAdd.getRepertory());
            baseGoodsSKU.setPrice(goodsSKUAdd.getCost());
            baseGoodsSKU.setSpecificationPicId(goodsSKUAdd.getSpePictureId());
            baseGoodsSKU.setGmtModified(currentDate);
            baseGoodsSKU.setModifier(goodsSKUListAdd.getUserId().toString());

            int i = baseGoodsSKUMapper.updateByProductId(baseGoodsSKU);

            if(i == 0)
            {
                baseGoodsSKU.setSpuId(goodsSKUListAdd.getSpuId());
                baseGoodsSKU.setCreator(goodsSKUListAdd.getUserId().toString());
                baseGoodsSKU.setGmtCreate(currentDate);
                baseGoodsSKU.setAttributeIds(goodsSKUAdd.getAttrValueIds());
                baseGoodsSKU.setIsDeleted(0);
                i = baseGoodsSKUMapper.insertSelective(baseGoodsSKU);
            }
            number += i;
        }
        return number;
    }

    @Override
    public List<GoodsSKUExportVo> getGoodsSKUsExportData(PutAwayBO putAwayBO) {
        GoodsDetailQuery goodsDetailQuery = new GoodsDetailQuery();
        goodsDetailQuery.setCompanyId(putAwayBO.getCompanyId());
        List<GoodsSKUExportVo> list = new ArrayList<>();
        List<GoodsVO> goodsVOList = baseGoodsSPUMapper.getGoodsListByIds(putAwayBO.getIds());
        goodsVOList.forEach(vo -> {
            if (vo.getModelId() != null && vo.getModelId() > 0) {
                if (StringUtils.isNotBlank(vo.getMaterialIds()) && !"-1".equals(vo.getMaterialIds())) {
                    vo.setModelOrMaterial("有模型/贴图");
                }else {
                    vo.setModelOrMaterial("有模型");
                }
            }else {
                if (StringUtils.isNotBlank(vo.getMaterialIds()) && !"-1".equals(vo.getMaterialIds())) {
                    vo.setModelOrMaterial("有贴图");
                }else {
                    vo.setModelOrMaterial("无模型/贴图");
                }
            }
            // xxx : CMSBUG-1619 列表只显示“是”与“否”
            // create by zhoujc at  2019/4/24 11:01.
            if ((vo.getModelId() == null || vo.getModelId() <= 0) && (StringUtils.isBlank(vo.getMaterialIds()) || "-1".equals(vo.getMaterialIds()))) {
                vo.setModelOrMaterial("否");
            } else {
                vo.setModelOrMaterial("是");

            }
            goodsDetailQuery.setSpuId(vo.getId());
            List<GoodsSKUVO> goodsSKUs = this.getGoodsSKUs(goodsDetailQuery);
            if (null != goodsSKUs && goodsSKUs.size()>0){
                for (GoodsSKUVO skuVo : goodsSKUs){
                    GoodsSKUExportVo dataVo = new GoodsSKUExportVo();
                    BeanUtils.copyProperties(skuVo, dataVo);
                    BeanUtils.copyProperties(vo, dataVo);
                    StringBuilder sb = new StringBuilder();
                    sb.append(vo.getSmallType());
                    sb.append("~");
                    sb.append(vo.getBigType());
                    dataVo.setBigsmallType(sb.toString());
                    dataVo.setIsPutaway(vo.getIsPutaway() == 0 ? "未上架" : "上架");
                    /*if (null != dataVo.getProductDesc() && !dataVo.getProductDesc().equals("")){
                        String productDesc = dataVo.getProductDesc().replace("<p>", "").replace("</p>", "");
                        dataVo.setProductDesc(productDesc);
                    }*/
                    list.add(dataVo);
                }
            }

        });
        return list;
    }

    private boolean updateDashboardConfig(GoodsSKUListAdd goodsSKUListAdd){
        if (StringUtils.isNotBlank(goodsSKUListAdd.getAppId())) {
            String dashboardConfig = baseGoodsSKUMapper.getDashBoardConfigByAppId(goodsSKUListAdd.getAppId());
            if (StringUtils.isNotBlank(dashboardConfig)) {
                List<GoodsSKUAdd> list = goodsSKUListAdd.getGoodsSKUAddList()
                        .stream()
                        .filter(o -> o.getCost().compareTo(new BigDecimal("0")) > 0)
                        .collect(Collectors.toList());
                Double minPrice = (list == null || list.size() == 0) ? 0 : list
                        .stream()
                        .min(Comparator.comparing(GoodsSKUAdd::getCost))
                        .get()
                        .getCost()
                        .doubleValue();

//                Map<String, MiniProgramDashboardConfig> mapConfig = gson.fromJson(dashboardConfig, new TypeToken<Map<String, MiniProgramDashboardConfig>>() {
//                }.getType());
                List<MiniProgramDashboardConfig> configs = gson.fromJson(dashboardConfig, new TypeToken<List<MiniProgramDashboardConfig>>() {
                }.getType());
                for (MiniProgramDashboardConfig config : configs){
                    if ("hotRecommend".equals(config.getType())){
                        this.updateGoodsConfig(config, goodsSKUListAdd.getSpuId(), minPrice);
                    }

                    if ("newRecommend".equals(config.getType())){
                        this.updateGoodsConfig(config, goodsSKUListAdd.getSpuId(), minPrice);
                    }



                }
                //this.updateGoodsConfig(mapConfig.get("baokuan"), goodsSKUListAdd.getSpuId(), minPrice);
                //this.updateGoodsConfig(mapConfig.get("newkuan"), goodsSKUListAdd.getSpuId(), minPrice);
                baseGoodsSKUMapper.updateDashBoardConfigByAppId(goodsSKUListAdd.getAppId(), gson.toJson(configs));
                return true;
            }
        }
        return false;
    }

    private void updateGoodsConfig(MiniProgramDashboardConfig config, Integer spuId, Double minPrice){
        if (config != null) {
            List<ConfigDetail> detailList = config.getConfigDetails();
            if (detailList != null) {
                detailList.forEach(detail -> {
                    if (spuId.equals(detail.getId().intValue())) {
                        detail.setPrice(minPrice);
                    }
                });
            }
        }
    }
}
