package com.sandu.service.solution.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sandu.api.solution.model.DesignRenderRoam;


/**   
 * @Title: DesignRenderRoamMapper.java 
 * @Package com.nork.render.dao
 * @Description:渲染漫游-720漫游Mapper
 * @createAuthor pandajun 
 * @CreateDate 2017-07-13 17:41:16
 * @version V1.0   
 */
@Repository
public interface DesignRenderRoamMapper {
	
    int insertSelective(DesignRenderRoam record);

    int updateByPrimaryKeySelective(DesignRenderRoam record);
  
    int deleteByPrimaryKey(Integer id);
        
    DesignRenderRoam selectByPrimaryKey(Integer id);
    
    List<DesignRenderRoam> selectList(DesignRenderRoam designRenderRoam);
    
    public DesignRenderRoam countByScreenShotId(DesignRenderRoam designRenderRoam);

    /**
     * 通过UUID查询一个漫游
     * @param uuid
     * @return
     */
	DesignRenderRoam selectByUUID(String uuid);

    /**
     * 通过截图ID查询一个720漫游
     * @param screenId
     * @return
     */
    DesignRenderRoam selectByScreenId(Integer screenId);

    /**
     * 通过screenId和fileKey 查找数据,过滤自动渲染产生的screenId一样的数据
     * @author: chenm
     * @date: 2019-07-23 19:33
     * @param screenId
     * @param fileKey
     * @return: java.util.List<com.nork.design.model.DesignRenderRoam>
     */
    List<DesignRenderRoam> selectByScreenIdAndFileKey(@Param("screenId") Integer screenId, @Param("fileKey")String fileKey);
}