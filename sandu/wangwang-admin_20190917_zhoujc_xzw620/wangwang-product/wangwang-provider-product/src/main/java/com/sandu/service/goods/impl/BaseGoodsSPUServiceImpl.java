package com.sandu.service.goods.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sandu.api.goods.input.GoodsListQuery;
import com.sandu.api.goods.input.GoodsSPUAdd;
import com.sandu.api.goods.model.BaseGoodsSPU;
import com.sandu.api.goods.model.ConfigDetail;
import com.sandu.api.goods.model.MiniProgramDashboardConfig;
import com.sandu.api.goods.model.ResPic;
import com.sandu.api.goods.model.bo.PutAwayBO;
import com.sandu.api.goods.model.po.GoodsListQueryPO;
import com.sandu.api.goods.output.GoodsDetailVO;
import com.sandu.api.goods.output.GoodsTypeVO;
import com.sandu.api.goods.output.GoodsVO;
import com.sandu.api.goods.output.PicVO;
import com.sandu.api.goods.service.BaseGoodsSPUService;
import com.sandu.api.goods.service.PicService;
import com.sandu.common.ReturnData;
import com.sandu.constant.ResponseEnum;
import com.sandu.service.goods.dao.BaseGoodsSKUMapper;
import com.sandu.service.goods.dao.BaseGoodsSPUMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("baseGoodsSPUService")
public class BaseGoodsSPUServiceImpl implements BaseGoodsSPUService {
    @Autowired
    private BaseGoodsSPUMapper baseGoodsSPUMapper;
    @Autowired
    private BaseGoodsSKUMapper baseGoodsSKUMapper;
    @Autowired
    private PicService picService;

    private static Gson gson = new Gson();

    @Override
    public BaseGoodsSPU selectByPrimaryKey(Integer id) {
        return baseGoodsSPUMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<GoodsVO> getGoodsList(GoodsListQuery goodsListQuery) {
        // 创建查询对象
        GoodsListQueryPO goodsListQueryPO = new GoodsListQueryPO();
        // 处理查询条件，商品名称、商品编号、产品型号两端增加通配符%，使用like模糊查询
        if (goodsListQuery.getName() != null && !"".equals(goodsListQuery.getName().trim())) {
            goodsListQueryPO.setSpuName("%" + goodsListQuery.getName().trim() + "%");
        }
        if (goodsListQuery.getCode() != null && !"".equals(goodsListQuery.getCode().trim())) {
            goodsListQueryPO.setSpuCode("%" + goodsListQuery.getCode().trim() + "%");
        }
        if (goodsListQuery.getProductModelNumber() != null && !"".equals(goodsListQuery.getProductModelNumber().trim())) {
            goodsListQueryPO.setProductModelNumber("%" + goodsListQuery.getProductModelNumber().trim() + "%");
        }
        // 设置查询条件到查询对象中
        goodsListQueryPO.setTypeCode(goodsListQuery.getTypeCode());
        goodsListQueryPO.setChildTypeCode(goodsListQuery.getChildTypeCode());
        goodsListQueryPO.setCompanyId(goodsListQuery.getCompanyId());
        goodsListQueryPO.setIsPresell(goodsListQuery.getPresell());
        goodsListQueryPO.setIsPutaway(goodsListQuery.getPutaway());
        goodsListQueryPO.setHasModelOrMaterial(goodsListQuery.getHasModelOrMaterial());
        goodsListQueryPO.setCompanyNameLike(goodsListQuery.getCompanyName());
        goodsListQueryPO.setSelectMode(goodsListQuery.getSelectMode());
        // 处理分页参数
        if (goodsListQuery.getLimit() != null) {
            goodsListQueryPO.setLimit(goodsListQuery.getLimit());
        }
        if (goodsListQuery.getPage() != null) {
            goodsListQueryPO.setStart((goodsListQuery.getPage() - 1) * goodsListQuery.getLimit());
        }
        List<GoodsVO> goodsVOList = baseGoodsSPUMapper.getGoodsList(goodsListQueryPO);
        goodsVOList.forEach(vo -> {
            if (vo.getModelId() != null && vo.getModelId() > 0) {
                if (StringUtils.isNotBlank(vo.getMaterialIds()) && !"-1".equals(vo.getMaterialIds())) {
                    vo.setModelOrMaterial("有模型/贴图");
                }else {
                    vo.setModelOrMaterial("有模型");
                }
            }else {
                if (StringUtils.isNotBlank(vo.getMaterialIds()) && !"-1".equals(vo.getMaterialIds())) {
                    vo.setModelOrMaterial("有贴图");
                }else {
                    vo.setModelOrMaterial("无模型/贴图");
                }
            }
            // xxx : CMSBUG-1619 列表只显示“是”与“否”
            // create by zhoujc at  2019/4/24 11:01.
            if ((vo.getModelId() == null || vo.getModelId() <= 0) && (StringUtils.isBlank(vo.getMaterialIds()) || "-1".equals(vo.getMaterialIds()))) {
                vo.setModelOrMaterial("否");
            } else {
                vo.setModelOrMaterial("是");

            }
        });
        return goodsVOList;
    }

    @Override
    public Integer goodsPutawayUp(PutAwayBO putAwayBO) {
        Integer number = baseGoodsSPUMapper.goodsPutawayUp(putAwayBO.getIds());
        return number;
    }

    @Transactional
    @Override
    public Integer goodsPutawayDown(List<Integer> ids, String appId) {
        Integer number = baseGoodsSPUMapper.goodsPutawayDown(ids);
        this.deletedDashboardConfig(ids, appId);
        return number;
    }

    @Override
    public GoodsDetailVO getGoodsInfo(Integer id) {
        GoodsDetailVO goodsDetail = baseGoodsSPUMapper.getGoodsDetail(id);
        // 处理商品图片列表
        // 主缩略图不展示，默认pic_ids的第一张图为主缩略图的原图
        List<Integer> idList = new ArrayList();
        if (goodsDetail != null) {
            String picIds = goodsDetail.getPicIds();
            // 拆分pic_ids
            if (picIds != null && !picIds.equals("")) {
                String[] picIdArr = picIds.split(",");

                for (String str : picIdArr) {
                    idList.add(Integer.parseInt(str));
                }
            }
            // 获取图片
            List<PicVO> picList = picService.getPicsByIds(idList);
            // 图片按pic_ids的顺序展示
            List<PicVO> picListSort = new ArrayList<>();
            if (picList != null){
                for (int i = 0; i < idList.size(); i++) {
                    for (PicVO picVO : picList) {
                        if (i == 0){
                            if (picVO.getPicId().equals(idList.get(i))){
                                // 第一张图为主缩略图的原图
                                goodsDetail.setMainPic(picVO);
                                goodsDetail.setMainPicId(idList.get(i));
                                break;
                            }
                        }else {
                            if (picVO.getPicId().equals(idList.get(i))){
                                // 其余图片按顺序展示
                                picListSort.add(picVO);
                                break;
                            }
                        }
                    }
                }
            }
            goodsDetail.setPicList(picListSort);
            return goodsDetail;
        } else {
            return null;
        }
    }

    @Override
    public ReturnData updateGoods(GoodsSPUAdd goodsSPUAdd) {
        if (goodsSPUAdd.getDescribe() != null && !"".equals(goodsSPUAdd.getDescribe())){
            if (goodsSPUAdd.getDescribe().getBytes().length > 65535){
                return ReturnData.builder().message("商品描述字数过多").code(ResponseEnum.ERROR);
            }
        }
        if (baseGoodsSPUMapper.updateSpu(goodsSPUAdd) == 0) {
            return ReturnData.builder().message("保存失败").code(ResponseEnum.ERROR);
        } else {
            this.updateDashboardConfig(goodsSPUAdd);
            if (baseGoodsSPUMapper.updateSpuSaleInfo(goodsSPUAdd) == 0) {
                goodsSPUAdd.setCreator(goodsSPUAdd.getModifier());
                goodsSPUAdd.setGmtCreate(goodsSPUAdd.getGmtModified());
                goodsSPUAdd.setIsDeleted(0);
                baseGoodsSPUMapper.insertSpuSaleInfo(goodsSPUAdd);
            }
        }
        return ReturnData.builder().message("保存成功").code(ResponseEnum.SUCCESS);
    }

    @Override
    public List<GoodsTypeVO> getGoodsType(Integer companyId) {
        List<GoodsTypeVO> goodsTypeVOList = baseGoodsSPUMapper.getGoodsType(companyId);
        return goodsTypeVOList;
    }

    @Override
    public Integer totalCount(GoodsListQuery goodsListQuery) {
        GoodsListQueryPO goodsListQueryPO = new GoodsListQueryPO();
        if (goodsListQuery.getName() != null && !"".equals(goodsListQuery.getName().trim())) {
            goodsListQueryPO.setSpuName("%" + goodsListQuery.getName().trim() + "%");
        }
        if (goodsListQuery.getCode() != null && !"".equals(goodsListQuery.getCode().trim())) {
            goodsListQueryPO.setSpuCode("%" + goodsListQuery.getCode().trim() + "%");
        }
        if (goodsListQuery.getProductModelNumber() != null && !"".equals(goodsListQuery.getProductModelNumber().trim())) {
            goodsListQueryPO.setProductModelNumber("%" + goodsListQuery.getProductModelNumber().trim() + "%");
        }
        goodsListQueryPO.setTypeCode(goodsListQuery.getTypeCode());
        goodsListQueryPO.setChildTypeCode(goodsListQuery.getChildTypeCode());
        goodsListQueryPO.setCompanyId(goodsListQuery.getCompanyId());
        goodsListQueryPO.setIsPresell(goodsListQuery.getPresell());
        goodsListQueryPO.setIsPutaway(goodsListQuery.getPutaway());
        goodsListQueryPO.setHasModelOrMaterial(goodsListQuery.getHasModelOrMaterial());
        goodsListQueryPO.setCompanyNameLike(goodsListQuery.getCompanyName());
        goodsListQueryPO.setSelectMode(goodsListQuery.getSelectMode());
        return baseGoodsSPUMapper.getTotalCount(goodsListQueryPO);
    }

    @Override
    public BaseGoodsSPU get(Integer id) {
        return baseGoodsSPUMapper.selectByPrimaryKey(id);
    }

    @Override
    public int modifyGoodsSort(Integer id, Integer sort) {
        return baseGoodsSPUMapper.modifyGoodsSort(id,sort);
    }

    @Override
    public int sortEdit(Integer spuId, Integer sort) {
        BaseGoodsSPU spu = new BaseGoodsSPU();
        spu.setId(spuId);
        spu.setSort(sort);
        return baseGoodsSPUMapper.updateByPrimaryKeySelective(spu);
    }

    private boolean deletedDashboardConfig(List<Integer> spuIds, String appId) {
        if (StringUtils.isNotBlank(appId)) {
            String dashboardConfig = baseGoodsSKUMapper.getDashBoardConfigByAppId(appId);
            if (StringUtils.isNotBlank(dashboardConfig)) {
//                Map<String, MiniProgramDashboardConfig> mapConfig = gson.fromJson(dashboardConfig,
//                        new TypeToken<Map<String, MiniProgramDashboardConfig>>() {}.getType());
                List<MiniProgramDashboardConfig> configs = gson.fromJson(dashboardConfig, new com.google.common.reflect.TypeToken<List<MiniProgramDashboardConfig>>() {
                }.getType());
                Set<String> idSet = new HashSet<>();
                spuIds.forEach(id -> idSet.add(id + ""));
                for(MiniProgramDashboardConfig config : configs){
                    if ("hotRecommend".equals(config.getType())){
                        this.deleteGoodsConfig(config, idSet);
                    }

                    if ("newRecommend".equals(config.getType())){
                        this.deleteGoodsConfig(config,idSet);
                    }

                    if ("moreGoods".equals(config.getType())){
                        this.deleteGoodsConfig(config,idSet);
                    }
                }
//                this.deleteGoodsConfig(mapConfig.get("baokuan"), idSet);
//                this.deleteGoodsConfig(mapConfig.get("newkuan"), idSet);
//                MiniProgramDashboardConfig more = mapConfig.get("moreGoods");
//                if (more != null && more.getConfigList() != null && more.getConfigList().size() > 0) {
//                    more.getConfigList().stream().forEach(config -> this.deleteGoodsConfig(config, idSet));
//                }
                baseGoodsSKUMapper.updateDashBoardConfigByAppId(appId, gson.toJson(configs));
                return true;
            }
        }
        return false;
    }

    private boolean updateDashboardConfig(GoodsSPUAdd goodsSPUAdd){
        if (StringUtils.isNotBlank(goodsSPUAdd.getAppId())) {
            ResPic pic = new ResPic();
            if (goodsSPUAdd.getMainPicId() != null) {
                pic = picService.get(goodsSPUAdd.getMainPicId());
            }
            String picPath = pic.getPicPath();
            String name = goodsSPUAdd.getName();
            String dashboardConfig = baseGoodsSKUMapper.getDashBoardConfigByAppId(goodsSPUAdd.getAppId());
            if (StringUtils.isNotBlank(dashboardConfig)) {
//                Map<String, MiniProgramDashboardConfig> mapConfig = gson.fromJson(dashboardConfig,
//                        new TypeToken<Map<String, MiniProgramDashboardConfig>>() {}.getType());
                List<MiniProgramDashboardConfig> mapConfig = gson.fromJson(dashboardConfig,new TypeToken<List<MiniProgramDashboardConfig>>(){}.getType());
                mapConfig.forEach(config ->{
                    if (Objects.equals("hotRecommend",config.getType())){
                        this.updateGoodsConfig(config, goodsSPUAdd.getSpuId(), picPath, name);
                    }

                    if (Objects.equals("newRecommend",config.getType())){
                        this.updateGoodsConfig(config, goodsSPUAdd.getSpuId(), picPath, name);
                    }

                    if (Objects.equals("moreGoods",config.getType())){
                        this.updateGoodsConfig(config, goodsSPUAdd.getSpuId(), picPath, name);
                    }
                });
//                this.updateGoodsConfig(mapConfig.get("baokuan"), goodsSPUAdd.getSpuId(), picPath, name);
//                this.updateGoodsConfig(mapConfig.get("newkuan"), goodsSPUAdd.getSpuId(), picPath, name);
//                MiniProgramDashboardConfig more = mapConfig.get("moreGoods");
//                if (more != null && more.getConfigList() != null && more.getConfigList().size() > 0) {
//                    more.getConfigList().stream().forEach(config -> this.updateGoodsConfig(config, goodsSPUAdd.getSpuId(), picPath, name));
//                }
                baseGoodsSKUMapper.updateDashBoardConfigByAppId(goodsSPUAdd.getAppId(), gson.toJson(mapConfig));
                return true;
            }
        }
        return false;
    }

    private void updateGoodsConfig(MiniProgramDashboardConfig config, Integer spuId, String picPath, String spuName){
        if (config != null) {
            List<ConfigDetail> detailList = config.getConfigDetails();
            if (detailList != null) {
                detailList.forEach(detail -> {
                    if (spuId.equals(detail.getId().intValue())) {
                        detail.setPicAddress(picPath);
                        detail.setDetailName(spuName);
                    }
                });
            }
        }
    }

    private void deleteGoodsConfig(MiniProgramDashboardConfig config, Set<String> set){
        if (config != null) {
            List<ConfigDetail> detailList = config.getConfigDetails();
            if (detailList != null) {
                Iterator<ConfigDetail> iterator = detailList.iterator();
                while (iterator.hasNext()) {
                    ConfigDetail detail = iterator.next();
                    if (set.contains(detail.getId() + "")) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}