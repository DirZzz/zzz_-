package com.sandu.service.product.impl;

import com.sandu.api.product.model.ProductStyle;
import com.sandu.api.product.service.ProductStyleService;
import com.sandu.service.product.dao.ProductStyleDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sandu
 */
@Service("productStyleService")
public class ProductStyleServiceImpl implements ProductStyleService {
    @Autowired
    private ProductStyleDao productStyleDao;

    @Override
    public int saveProductStyle(ProductStyle productStyle) {
        return productStyleDao.save(productStyle);
    }

    @Override
    public int deleteProductStyleById(long id) {
        return productStyleDao.deleteById(id);
    }

    @Override
    public void updateProductStyle(ProductStyle productStyle) {
        productStyleDao.update(productStyle);
    }

    @Override
    public ProductStyle getProductStyleById(long id) {
        return productStyleDao.getById(id);
    }

    @Override
    public List<ProductStyle> listProductStyleIdAndName() {
        return productStyleDao.listProductStyleIdAndName();
    }

    @Override
    public Map<Integer, String> id2Name(List<Integer> collect) {
        if (CollectionUtils.isEmpty(collect)) {
            return Collections.emptyMap();
        }
        return productStyleDao.selectByIds(collect).stream().collect(Collectors.toMap(it -> it.getId().intValue(), ProductStyle::getStyleName));
    }

}
