package com.sandu.search.entity.elasticsearch.po.metadate;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in AM 10:39 2018/8/7 0007
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import com.sandu.search.common.tools.JsonUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.Strings;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Title: 基本产品元数据
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/8/7 0007AM 10:39
 */
@Data
public class BaseProductDataPo implements Serializable{

    private static final long serialVersionUID = -4015357918336965863L;

    private int productId; //产品ID
    private int spuId; //商品ID
    private int productIsDeleted;//产品是否被删除
    private BigDecimal price;//产品优惠价
    private String productCode;//'产品编码'
    private int brandId; //产品品牌
    private String productStyleIdInfo; //'产品风格'
    private String productSpec;//产品规格
    private int productTypeValue;//产品大类
    private int productSmallTypeValue;//产品小类
    private String productModelNumber;//产品型号
    private int putawayState;//产品状态('0-未上架;1-已上架;2-测试中;3-已发布;4-已下架;5-发布中')

    //装修报价
    private BigDecimal decorationPrice;
    //产品原价
    private BigDecimal salePrice;
    /**
     * 产品风格IDs
     */
    private List<Integer> productStyleIds;

    public void setProductStyleIdInfo(String productStyleIdInfo) {
        this.productStyleIdInfo = productStyleIdInfo;
        if (StringUtils.isNotBlank(productStyleIdInfo)) {
            try {
                ProductStyleBean productStyleBean = JsonUtil.fromJson(this.productStyleIdInfo, ProductStyleBean.class);
                if (!Strings.isNullOrEmpty(productStyleBean.getIsLeaf_0())) {
                    this.productStyleIds = Stream.of(productStyleBean.getIsLeaf_0().split(",")).map(Integer::new).collect(Collectors.toList());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Data
    private static class ProductStyleBean {
        private String isLeaf_0;
        private String isLeaf_1;
    }

    public static void main(String[] args) {
        ProductStyleBean bean = JsonUtil.fromJson("{\"isLeaf_0\":\"26\",\"isLeaf_1\":\"\"}", ProductStyleBean.class);
        System.out.println(bean);
    }

}
