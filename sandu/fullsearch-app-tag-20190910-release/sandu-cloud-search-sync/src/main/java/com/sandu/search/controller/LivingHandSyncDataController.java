package com.sandu.search.controller;

import com.sandu.search.initialize.LivingIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/sync/living")
@Slf4j
public class LivingHandSyncDataController {

    @Autowired
    private LivingIndex livingIndex;

    @PostMapping
    public String syncUpdateLivingList(@RequestBody List<Integer> livingIds){
        if (CollectionUtils.isEmpty(livingIds)) return "parameter is empty !";
        try {
            log.info("begin to sync living es data,livingIds:{}",livingIds);
            livingIndex.indexLivingData(livingIds);
            log.info("end to sync living es data");
            return "success to sync living data to es";
        } catch (Exception e) {
            log.error("手动同步小区信息异常",e);
            return "error to hand living data to es";
        }
    }
}
