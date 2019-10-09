package com.sandu.api.solution.service;

import com.sandu.api.solution.model.DesignRenderRoam;

import java.util.List;

/**
 * @Author: YuXingchi
 * @Description:
 * @Date: Created in 17:36 2018/5/17
 */
public interface DesignRenderRoamService {

	/**
	 * 新增数据
	 * @param designRenderRoam
	 * @return  int 
	 */
	public int add(DesignRenderRoam designRenderRoam);
	
	/**
	 * 根据主键获取id信息 
	 * @param id
	 * @return
	 */
	public DesignRenderRoam selectByScreenShotId(Integer id);

	/**
	 * 通过screenId和fileKey 查找数据,过滤自动渲染产生的screenId一样的数据
	 * @author: chenm
	 * @date: 2019-07-23 19:33
	 * @param screenId
	 * @param fileKey
	 * @return: java.util.List<com.nork.design.model.DesignRenderRoam>
	 */
	DesignRenderRoam getByScreenIdAndFileKey(Integer screenId,String fileKey);
}
