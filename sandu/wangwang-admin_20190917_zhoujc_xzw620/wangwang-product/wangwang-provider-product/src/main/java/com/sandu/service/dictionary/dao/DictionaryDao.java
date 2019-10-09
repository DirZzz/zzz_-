package com.sandu.service.dictionary.dao;

import com.sandu.api.dictionary.input.SysDictionaryQuery;
import com.sandu.api.dictionary.model.Dictionary;
import com.sandu.api.dictionary.output.DictionaryTypeListVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sandu
 */
@Repository
public interface DictionaryDao {

    int insertSelective(Dictionary record);

    int updateByPrimaryKeySelective(Dictionary record);

    int deleteByPrimaryKey(Integer id);

    Dictionary selectByPrimaryKey(Integer id);

    List<Dictionary> listByType(@Param("types") List<String> types);

    Dictionary getByTypeAndValue(@Param("type") String type, @Param("value") Integer value);

    Dictionary getSmallProductTypeByValue(@Param("prodcutTypeValue") Integer prodcutTypeValue, @Param("productSmallTypeValue") Integer productSmallTypeValue);

    List<Dictionary> getProductTypeValueAndSmallBySmallValueKey(@Param("valueKey") String valueKey);

    Dictionary getByValueKey(@Param("smallTypeCode") String smallTypeCode);

    List<Dictionary> getByValueKeys(List<String> strings);

    List<Dictionary> getByTypeAndName(@Param("type") String type, @Param("name") String name);

    List<Dictionary> listByAloneType(@Param("type") String type);

    List<DictionaryTypeListVO> selectListByTypeOrValues(@Param("industry") String industry,@Param("integerList") List<Integer> integerList);

    List<Dictionary> getUserTypeList(@Param("type") String type);

    @Select("select c.type,c.valuekey,c.value,c.name,p.name as  att3 from sys_dictionary p\n" +
            "inner join sys_dictionary c  on p.valuekey = c.type\n" +
            "where p.type = 'houseType'")
    List<Dictionary> listApplySpaceAreas();

	List<Dictionary> selectDictionaryByQuery(SysDictionaryQuery query);
	
}
