package com.sandu.search.storage.product;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.GroupPlatformRelPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 组合平台元数据存储
 *
 * @author xiaoxc
 * @date 2019-03-29
 */
@Slf4j
@Component
public class GroupPlatformMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "组合平台元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public GroupPlatformMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> groupPlatformRelMap = null;

    /**
     * 切换存储模式
     */
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            groupPlatformRelMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "组合平台存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    /**
     * 获取Map数据方法兼容
     */
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            return groupPlatformRelMap.get(keyName);
        }
        return null;
    }

    /**
     * 更新数据
     */
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取组合平台元数据存储....");

        //组合平台元数据
        List<GroupPlatformRelPo> groupPlatformRelPoList;
        try {
            groupPlatformRelPoList = metaDataService.queryAllPlatformGroupMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取组合平台元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取组合平台元数据失败,List<CompanyShopPlanPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取组合平台元数据完成,总条数:{}", (null == groupPlatformRelPoList ? 0 : groupPlatformRelPoList.size()));

        if (null == groupPlatformRelPoList || 0 >= groupPlatformRelPoList.size()) {
            log.error(CLASS_LOG_PREFIX + "获取组合平台元数据为空，请检查数据...");
            return;
        }

        //Map对象
        Map<String, List<GroupPlatformRelPo>> groupPlatformRelPoMap = new HashMap<>();
        //转换为Map元数据
        for (GroupPlatformRelPo groupPlatformRelPo : groupPlatformRelPoList) {
            if (null != groupPlatformRelPo) {
                //组合平台ID
                String groupId = String.valueOf(groupPlatformRelPo.getGroupId());
                List<GroupPlatformRelPo> groupPlatformRelPos = new ArrayList<>(8);
                groupPlatformRelPos.add(groupPlatformRelPo);
                if (groupPlatformRelPo.getGroupId() > 0) {
                    if (groupPlatformRelPoMap.containsKey(groupId)) {
                        groupPlatformRelPos.addAll(groupPlatformRelPoMap.get(groupId));
                    }
                    groupPlatformRelPoMap.put(groupId, groupPlatformRelPos);
                }
            }
        }
        log.info(CLASS_LOG_PREFIX + "格式化组合平台元数据完成.");

        //转换map
        Map<String, String> groupPlatformJsonMap = new HashMap<>(groupPlatformRelPoMap.size());
        groupPlatformRelPoMap.forEach((k,v) -> groupPlatformJsonMap.put(k, JsonUtil.toJson(v)));

        //装载缓存
        redisService.del(RedisConstant.GROUP_PLATFORM_REL_MAP_DATA);
        redisService.addMapCompatible(RedisConstant.GROUP_PLATFORM_REL_MAP_DATA, groupPlatformJsonMap);
        log.info(CLASS_LOG_PREFIX + "装载缓存组合平台元数据完成.");

    }

    /**
     * 根据组合ID查询平台信息
     *
     * @param groupId
     * @return
     */
    public List<GroupPlatformRelPo> getGroupPlatformById(Integer groupId) {

        if (null == groupId || 0 == groupId) {
            return null;
        }

        String productPlatformRelStr = getMap(RedisConstant.GROUP_PLATFORM_REL_MAP_DATA, groupId + "");
        if (StringUtils.isEmpty(productPlatformRelStr)) {
            return null;
        }

        return JsonUtil.fromJson(productPlatformRelStr, new TypeToken<List<GroupPlatformRelPo>>() {}.getType());
    }


}
