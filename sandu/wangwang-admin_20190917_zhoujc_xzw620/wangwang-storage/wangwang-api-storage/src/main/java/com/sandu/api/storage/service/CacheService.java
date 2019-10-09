package com.sandu.api.storage.service;

import com.sandu.api.category.model.bo.CategoryTreeNode;

import java.util.List;
import java.util.Map;

public interface CacheService {

    List<CategoryTreeNode> listAllCategory();

    Map<String,String> resetCategoryCache();

    Integer getCountByNodeIdAndDetailType(Integer nodeId, Integer detailType);

    void putCountByNodeIdAndDetailsType(Integer id, Integer detailTypeView, Integer value);
}
