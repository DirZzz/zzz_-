package com.sandu.service.storage.impl;

import com.sandu.api.category.model.Category;
import com.sandu.api.category.model.bo.CategoryTreeNode;
import com.sandu.api.category.service.CategoryService;
import com.sandu.api.storage.service.CacheService;
import com.sandu.commons.gson.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    private final CategoryService categoryService;
    private final RedisTemplate redisTemplate;

    private final String CATEGORY_CACHE_KEY = "categoryMap";

    private static final String NODE_INFO_CACHE_KEY_PRE = "nn:";

    public static final String NODE_INFO_DATA_SYNCHRONIZED_SET = "syncSet";


    @Autowired
    public CacheServiceImpl(CategoryService categoryService, RedisTemplate redisTemplate) {
        this.categoryService = categoryService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<CategoryTreeNode> listAllCategory() {
        log.info("search category in cache");
        BoundHashOperations hashOperations = redisTemplate.boundHashOps(CATEGORY_CACHE_KEY);
        Map<String, String> categoryMap = hashOperations.entries();
        if (categoryMap.isEmpty()) {
            categoryMap = this.resetCategoryCache();
        }
        Map<Integer, List<CategoryTreeNode>> map = new HashMap<>(categoryMap.size());
        categoryMap.forEach((k, v) -> map.put(Integer.valueOf(k), Arrays.asList(GsonUtil.fromJson(v, CategoryTreeNode[].class))));
        map.entrySet().parallelStream().forEach(item ->
                item.getValue().forEach(node ->
                        node.setChildren(map.get(node.getId().intValue()))
                )
        );
        return Optional.ofNullable(map.get(1)).orElseGet(Collections::emptyList);
    }

    @Override
    public Map<String, String> resetCategoryCache() {
        log.info("reset category cache");
        redisTemplate.delete(CATEGORY_CACHE_KEY);
        BoundHashOperations hashOperations = redisTemplate.boundHashOps(CATEGORY_CACHE_KEY);
        // 不同分类可多选设置
        String multiSelectType = "dim,qiangm,cz,dals,db";
        List<Category> nodes = categoryService.listCategoryNodesByLevel(Category.LEVEL_FIRST, Category.LEVEL_FIFTH);
        Map<String, String> pid2Node = nodes.parallelStream().map(category -> {
            CategoryTreeNode tmp = new CategoryTreeNode();
            if (multiSelectType.indexOf(category.getCode()) != -1) {
                tmp.setIsMultiSelect("true");
            }
            BeanUtils.copyProperties(category, tmp);
            return tmp;
        }).collect(Collectors.groupingBy(item -> item.getPid().toString(),
                Collectors.collectingAndThen(Collectors.toList(), GsonUtil::toJson)));

        hashOperations.putAll(pid2Node);
        return pid2Node;
    }

    @Override
    public Integer getCountByNodeIdAndDetailType(Integer nodeId, Integer detailType) {
        int result = 0;
        String key = new StringBuilder().append(detailType).append(":").append(nodeId).toString();
        String s = fetchKey(nodeId, detailType);
        Object o = redisTemplate.boundHashOps(s).get(key);
        if (o == null) {
            this.putCountByNodeIdAndDetailsType(nodeId, detailType, 0);
            result = 0;
        } else {
            result = Integer.valueOf(o.toString());
        }

        return result;
    }

    @Override
    public void putCountByNodeIdAndDetailsType(Integer nodeId, Integer detailTypeView, Integer value) {
        // cache data
        String key = new StringBuilder().append(detailTypeView).append(":").append(nodeId).toString();
        redisTemplate.boundHashOps(fetchKey(nodeId, detailTypeView)).put(key, value + "");
        log.info("redis key:{}, hash_key:{}->hash_value:{}", fetchKey(nodeId, detailTypeView), key, value);

        // sync set
        redisTemplate.boundSetOps(NODE_INFO_DATA_SYNCHRONIZED_SET).add(0 + ":" + nodeId);
    }

    private static String fetchKey(Integer id, Integer detailTypeView) {
        String key = String.valueOf(detailTypeView) + ":" + id;
        byte[] bucketId = getBucketId(key.getBytes(), 16);
        return NODE_INFO_CACHE_KEY_PRE + new String(bucketId);
    }


    public static void main(String[] args) {
        String s = fetchKey(168185, 3);
        System.out.println(s);
    }

    /**
     * created by zhangchengda
     * 2018/11/9 11:28
     * 将Key转换为特定长度的BucketId
     *
     * @param key 键
     * @param bit 位数，例：假如大约有1亿数据，最接近1亿的是2的27次方，则bit=26
     * @return
     */
    private static byte[] getBucketId(byte[] key, int bit) {
        // 获取MD5散列值
        MessageDigest mdInst = null;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        mdInst.update(key);
        byte[] md5 = mdInst.digest();
        // byte数组存放BucketId
        byte[] bucketId = new byte[(bit - 1) / 7 + 1];
        // 01111111111...11111110b
        int a = (int) Math.pow(2, bit % 7) - 2;
        md5[bucketId.length - 1] = (byte) (md5[bucketId.length - 1] & a);
        System.arraycopy(md5, 0, bucketId, 0, bucketId.length);
        // 将非ASCII码转换为ASCII码(ASCII码在0~127之间）
        for (int i = 0; i < bucketId.length; i++) {
            if (bucketId[i] < 0) {
                bucketId[i] &= 127;
            }
        }
        return bucketId;
    }
}
