package com.sandu.search.service.living;

import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;

public interface LivingSearchService {
    SearchObjectResponse searchLiving(Integer curPage, Integer pageSize, String areaCode, String livingName, String cityCode);
}
