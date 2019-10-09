package com.sandu.service.user.dao;

import com.sandu.api.user.input.UserMasterSonSearch;
import com.sandu.api.user.model.UserMasterSonRef;
import com.sandu.api.user.output.UserMasterSonVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMasterSonRefDao {

    /**
     * 删除
     *
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增
     *
     */
    int insertSelective(UserMasterSonRef record);

    /**
     * 查询
     *
     */
    UserMasterSonRef selectByPrimaryKey(Integer id);

    /**
     * 修改
     *
     */
    int updateByPrimaryKeySelective(UserMasterSonRef record);


    List<UserMasterSonVO> selectMasterSonUserList(UserMasterSonSearch userMasterSonSearch);

    Integer deleteMasterSonRef(@Param("masterUserId") Integer masterUserId,@Param("sonUserId") Integer sonUserId);

    Integer bacthInsert(@Param("userMasterSonRefList") List<UserMasterSonRef> userMasterSonRefList);
}