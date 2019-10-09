package com.sandu.cloud.design.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.design.dao.DesignPlanDao;
import com.sandu.cloud.design.model.DesignPlan;
import com.sandu.cloud.design.service.DesignPlanService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DesignPlanServiceImpl implements DesignPlanService {

    @Autowired
    private DesignPlanDao designPlanDao;

  
    @Override
    public DesignPlan get(long id) {
        return designPlanDao.selectByPrimaryKey(id);
    }
    
    
    
}
