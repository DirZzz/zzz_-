package com.sandu.search.controller.living;

import com.sandu.search.entity.elasticsearch.po.house.HouseLivingPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.response.universal.UniversalSearchResultResponse;
import com.sandu.search.service.living.LivingSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/all/living/search")
public class LivingController {

    @Autowired
    private LivingSearchService livingSearchService;

    @RequestMapping("/list")
    public UniversalSearchResultResponse queryLivingList(@RequestParam(required = false,defaultValue = "1") Integer curPage,
                                                         @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                                                         String areaCode,String cityCode,
                                                         String livingName){

        if (StringUtils.isEmpty(livingName)){
            return new UniversalSearchResultResponse(false,"请输入您要搜索的小区名");
        }

        try{
            SearchObjectResponse searchObjectResponse = livingSearchService.searchLiving(curPage,pageSize,areaCode,livingName,cityCode);
            log.info("搜索小区结果:{}",searchObjectResponse);
            if (searchObjectResponse != null) {
//                List<HouseLivingPo> list = (List<HouseLivingPo>) searchObjectResponse.getHitResult();
                return new UniversalSearchResultResponse(true,"success to search",searchObjectResponse.getHitTotal(),searchObjectResponse.getHitResult());
            }
            return new UniversalSearchResultResponse(true,"not match result",0, Collections.EMPTY_LIST);
        }catch (Exception e){
            log.error("系统异常",e);
            return new UniversalSearchResultResponse(false,"搜索小区数据失败");
        }
    }
}
