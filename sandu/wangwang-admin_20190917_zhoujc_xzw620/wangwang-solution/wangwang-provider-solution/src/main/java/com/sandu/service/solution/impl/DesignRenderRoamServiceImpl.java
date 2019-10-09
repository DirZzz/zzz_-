package com.sandu.service.solution.impl;

import javax.annotation.Resource;

import com.sandu.common.util.collections.CustomerListUtils;
import org.springframework.stereotype.Service;

import com.sandu.api.solution.model.DesignRenderRoam;
import com.sandu.api.solution.service.DesignRenderRoamService;
import com.sandu.service.solution.dao.DesignRenderRoamMapper;

import java.util.List;

@Service("designRenderRoamService")
public class DesignRenderRoamServiceImpl implements DesignRenderRoamService {

	@Resource
	private DesignRenderRoamMapper designRenderRoamMapper;
	
	@Override
	public int add(DesignRenderRoam designRenderRoam) {
		designRenderRoamMapper.insertSelective(designRenderRoam);
		return designRenderRoam.getId();
	}
	
	@Override
	public DesignRenderRoam selectByScreenShotId(Integer id) {
		return designRenderRoamMapper.selectByScreenId(id);
	}

	@Override
	public DesignRenderRoam getByScreenIdAndFileKey(Integer screenId, String fileKey) {
		List<DesignRenderRoam> roamList = designRenderRoamMapper.selectByScreenIdAndFileKey(screenId,fileKey);
		if(CustomerListUtils.isNotEmpty(roamList)){
			return roamList.get(0);
		}
		return null;
	}

}
