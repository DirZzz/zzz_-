package com.sandu.search.storage.product;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.util.Utils;
import com.sandu.search.entity.elasticsearch.po.metadate.UsedProductsPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品使用记录元数据存储（当前一个月产品统计数量）
 *
 * @author xiaoxc
 * @date 2019-01-10
 */
@Slf4j
@Component
public class UsedProductsMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "产品使用记录元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public UsedProductsMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> usedProductCountMap = null;

    // 切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            usedProductCountMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "产品使用次数存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
        //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PRODUCT_USAGE_COUNT_DATA.equals(mapName)) {
                return usedProductCountMap.get(keyName);
            }
        }
        return null;
    }


    //更新数据
    public void updateData() {
        //产品使用次数元数据
        List<UsedProductsPo> usedProductsPoList;
        try {
            //获取数据
            log.info(CLASS_LOG_PREFIX + "开始获取上个月产品使用次数元数据....");
            String startTime = Utils.getMonthBefore(new Date(),1, 0);
            String endTime = Utils.getMonthBefore(new Date(),1, 1);
            usedProductsPoList = metaDataService.queryLastMonthUsedProductData(startTime, endTime);
            log.info(CLASS_LOG_PREFIX + "获取上个月产品使用次数元数完成,总条数:{}", (null == usedProductsPoList ? 0 : usedProductsPoList.size()));
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取上个月产品使用次数元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取上个月产品使用次数元数据失败,List<UsedProductsPo> is null.MetaDataException:" + e);
        }

        //转换Map
        if (null != usedProductsPoList && 0 != usedProductsPoList.size()) {
            Map<String, String> productUsageCountMap = new HashMap<>();
            usedProductsPoList.forEach(usedProductsPo -> {
                //产品ID
                Integer productId = usedProductsPo.getProductId();
                //使用次数
                int productUsageCount = usedProductsPo.getLastMonthProductUsageCount();
                if (null != productId) {
                    //用户产品使用次数集合
                    productUsageCountMap.put(productId + "", productUsageCount + "");
                }
            });

            log.info(CLASS_LOG_PREFIX + "格式化上个月产品使用次数元数据完成....");
            //装载缓存
            redisService.addMapCompatible(RedisConstant.LASTMONTH_USEDPRODUCT_COUNT_DATA, productUsageCountMap);
            log.info(CLASS_LOG_PREFIX + "上个月产品使用次数元数据装载缓存完成....");

            //内存模式
            if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
                usedProductCountMap = productUsageCountMap;
                log.info(CLASS_LOG_PREFIX + "产品使用次数元数据装载内存完成....");
            }
        }
    }

    /**
     * 获取上个月产品使用次数元数据Map
     *
     * @return
     */
    public Integer getUsedProductCountMetaDataMap(Integer productId) {

        if (null == productId || 0 >= productId) {
            return 0;
        }

        String mapStr = getMap(RedisConstant.LASTMONTH_USEDPRODUCT_COUNT_DATA, productId + "");
        if (StringUtils.isEmpty(mapStr)) {
            return 0;
        }

        return Integer.valueOf(mapStr);
    }
}
