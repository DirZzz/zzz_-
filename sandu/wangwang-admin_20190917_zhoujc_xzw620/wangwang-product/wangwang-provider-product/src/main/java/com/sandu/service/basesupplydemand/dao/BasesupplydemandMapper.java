package com.sandu.service.basesupplydemand.dao;

import com.sandu.api.basesupplydemand.input.BasesupplydemandQuery;
import com.sandu.api.basesupplydemand.model.*;
import com.sandu.api.goods.model.ResPic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * supply_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-Oct-20 10:46
 */
@Repository
public interface BasesupplydemandMapper {
    int insert(Basesupplydemand basesupplydemand);

    int updateByPrimaryKey(Basesupplydemand basesupplydemand);

    int deleteByPrimaryKey(@Param("basesupplydemandIds") Set<Integer> basesupplydemandIds);

    Basesupplydemand selectByPrimaryKey(@Param("basesupplydemandId") int basesupplydemandId);

    List<Basesupplydemand> findAll(BasesupplydemandQuery query);

    /**
     * 置顶
     * @param basesupplydemandId
     * @param topId
     * @return
     */
    int baseSupplyToTop(@Param("id") String basesupplydemandId, @Param("topId") String topId);

    /**
     * 刷新
     * @param basesupplydemandId
     * @return
     */
    int baseSupplyToRefresh(@Param("basesupplydemandId") String basesupplydemandId);


    int deleteByIds(@Param("ids") List<Integer> ids);

    List<NodeDetails> listNodeDetails(@Param("contentIds") List<Integer> contendId, @Param("nodeType") Integer nodeType, @Param("detailTypes") List<Integer> detailTypes);

    String fetchPlanNameByDemandId(@Param("id") Integer id);

    String fetchHouseNameByDemandId(@Param("id") Integer id);

    int updateNodeDetailsValue(@Param("contentId") Integer contentId, @Param("nodeType") int nodeType, @Param("detailsType") int detailsType, @Param("value") Integer value);

    BaseHouse getBaseHouseByDemandId(Integer id);

    List<SpaceCommon> listSpaceDetailsByHouseId(long houseId);

    String fetchPlanNameByIdAndType(@Param("id") Integer id, @Param("type") Integer type);

    List<DemandInfoRel> getDemandRelInfo(@Param("basesupplydemandId") int basesupplydemandId);

    BaseHouse getBaseHouseByHouseId(Integer houseId);

    @Insert("insert INTO node_info (id,\n" +
            "                                     uuid,\n" +
            "                                     content_id,\n" +
            "                                     node_type,\n" +
            "                                     creator,\n" +
            "                                     gmt_create,\n" +
            "                                     modifier,\n" +
            "                                     gmt_modified,\n" +
            "                                     is_deleted,\n" +
            "                                     remark)\n" +
            "VALUES (null,\n" +
            "        uuid(),\n" +
            "        #{contendId},\n" +
            "        #{nodeType},\n" +
            "        'system',\n" +
            "        now(),\n" +
            "        'system',\n" +
            "        now(),\n" +
            "        0,\n" +
            "        '自动生成节点');")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertNodeInfo(@Param("contendId") Integer contendId, @Param("nodeType") Integer nodeType);

    @Insert("INSERT INTO node_info_detail (id,\n" +
            "                                            uuid,\n" +
            "                                            node_id,\n" +
            "                                            detail_type,\n" +
            "                                            value,\n" +
            "                                            creator,\n" +
            "                                            gmt_create,\n" +
            "                                            modifier,\n" +
            "                                            gmt_modified,\n" +
            "                                            is_deleted,\n" +
            "                                            remark)\n" +
            "VALUES (null,\n" +
            "        uuid(),\n" +
            "        #{nodeId},\n" +
            "        #{detailType},\n" +
            "        #{value},\n" +
            "        'system',\n" +
            "        now(),\n" +
            "        'system',\n" +
            "        now(),\n" +
            "        0,\n" +
            "        '');")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertNOdeInfoDetail(Integer nodeId, Integer detailType, int value);

    void deleteDemandInfoRel(@Param("id") Integer id, @Param("houseIds") List<Integer> houseIds, @Param("planIds") List<Integer> planIds);

    List<NodeDetails> listNodeInfoByContentIdsAndNodeType(@Param("contentIds") List<Integer> contendIds, @Param("nodeType") Integer nodeType);

    List<ResPic> getResPicByIds(@Param("picIds") List<Long> picIds);

    void insertInteractiveZoneTopic(InteractiveZoneTopic add);

    void batchInsertInteractiveZoneReply(@Param("list")List<InteractiveZoneReply> list);

    String getNickName(Integer userId);
}
