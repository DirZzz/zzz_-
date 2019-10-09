package com.sandu.service.basesupplydemand.impl.biz;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.sandu.api.area.service.BaseAreaService;
import com.sandu.api.basesupplydemand.input.BasesupplydemandAdd;
import com.sandu.api.basesupplydemand.input.BasesupplydemandQuery;
import com.sandu.api.basesupplydemand.input.BasesupplydemandUpdate;
import com.sandu.api.basesupplydemand.model.*;
import com.sandu.api.basesupplydemand.model.NodeDetails;
import com.sandu.api.basesupplydemand.service.BaseSupplydemandService;
import com.sandu.api.basesupplydemand.service.NodeInfoService;
import com.sandu.api.basesupplydemand.service.UserReviewsService;
import com.sandu.api.basesupplydemand.service.biz.BaseSupplydemandBizService;
import com.sandu.api.goods.model.ResPic;
import com.sandu.api.storage.service.CacheService;
import com.sandu.service.basesupplydemand.dao.BasesupplydemandMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.sandu.api.basesupplydemand.model.NodeInfoConstance.*;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * supply_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-Oct-20 10:46
 */
@Slf4j
@Service(value = "baseSupplydemandBizService")
public class BaseSupplydemandBizServiceImpl implements BaseSupplydemandBizService {

    @Autowired
    private BaseSupplydemandService baseSupplydemandService;

    @Autowired
    private BasesupplydemandMapper basesupplydemandMapper;

    @Autowired
    private NodeInfoService nodeInfoService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private BaseAreaService baseAreaService;

	@Autowired
	private UserReviewsService userReviewsService;

    @Override
    public int create(BasesupplydemandAdd input) {

        Basesupplydemand basesupplydemand = new Basesupplydemand();
        BeanUtils.copyProperties(input, basesupplydemand);

        return baseSupplydemandService.insert(basesupplydemand);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BasesupplydemandUpdate input) {
        Basesupplydemand basesupplydemand = new Basesupplydemand();

        BeanUtils.copyProperties(input, basesupplydemand);

        int result = baseSupplydemandService.update(basesupplydemand);
		// xxx : 编辑供求信息的浏览量、点赞数
        // create by zhoujc at  2019/2/1 10:22.
		List<NodeDetails> nodeDetails = nodeInfoService.listNodeDetails(input.getId(),
				NODE_TYPE_SUPPLY_DEMAND,
				Arrays.asList(DETAIL_TYPE_VIEW, DETAIL_TYPE_LIKE, DETAIL_TYPE_VIRTUAL_VIEW, DETAIL_TYPE_VIRTUAL_LIKE)
		);

		//create by qtq at 2019.3.29
		//互动区改版,旧数据迁移
		if (result > 0){
			if (Objects.equals(input.getMoveDataFlag(),1)){
				//需要搬迁数据到互动区
				moveToInteractive(input);
			}
		}

		nodeDetails.forEach(it -> {
			if (it.getDetailsType().equals(DETAIL_TYPE_VIEW)) {
				cacheService.putCountByNodeIdAndDetailsType(it.getNodeId(), DETAIL_TYPE_VIEW, input.getViewCount() == null ? 0 : input.getViewCount());
			}
			if (it.getDetailsType().equals(DETAIL_TYPE_LIKE)) {
				cacheService.putCountByNodeIdAndDetailsType(it.getNodeId(), DETAIL_TYPE_LIKE, input.getLikeCount() == null ? 0 : input.getLikeCount());
			}
			if (it.getDetailsType().equals(DETAIL_TYPE_VIRTUAL_VIEW)) {
				cacheService.putCountByNodeIdAndDetailsType(it.getNodeId(), DETAIL_TYPE_VIRTUAL_VIEW, input.getVirtualViewCount() == null ? 0 : input.getVirtualViewCount());
			}
			if (it.getDetailsType().equals(DETAIL_TYPE_VIRTUAL_LIKE)) {
				cacheService.putCountByNodeIdAndDetailsType(it.getNodeId(), DETAIL_TYPE_VIRTUAL_LIKE, input.getVirtualLikeCount() == null ? 0 : input.getVirtualLikeCount());
			}
		});
        // update view count
		nodeInfoService.updateNodeDetailsValue(input.getId(), NODE_TYPE_SUPPLY_DEMAND, DETAIL_TYPE_VIEW, input.getViewCount());
        // update like count
		nodeInfoService.updateNodeDetailsValue(input.getId(), NODE_TYPE_SUPPLY_DEMAND, DETAIL_TYPE_LIKE, input.getLikeCount());

		//处理方案、户型
		basesupplydemandMapper.deleteDemandInfoRel(basesupplydemand.getId(),
				input.getHouseIds() == null || input.getHouseIds().isEmpty() ? Collections.singletonList(0) : input.getHouseIds(),
				input.getPlanIds() == null || input.getPlanIds().isEmpty() ? Collections.singletonList(0) : input.getPlanIds());
        return result;
    }

	private void moveToInteractive(BasesupplydemandUpdate input) {
		Basesupplydemand damand = baseSupplydemandService.getById(input.getId());
		if (Objects.nonNull(damand)){
			Date now = new Date();
			InteractiveZoneTopic add = InteractiveZoneTopic.builder()
					.id(Long.valueOf(damand.getId() + ""))
					.title(damand.getTitle())
					.content(StringUtils.isNotBlank(damand.getContent()) ? damand.getContent() : damand.getDescription())
					.coverPicId(StringUtils.isNotBlank(damand.getCoverPicId()) ? Integer.parseInt(damand.getCoverPicId().split(",")[0]) : null)
					.picIds(damand.getCoverPicId())
					.blockType(1)
					.createUserId(damand.getCreatorId())
					.isDeleted(damand.getIsDeleted())
					.gmtCreate(now)
					.status(0)
					.gmtModified(now)
					.publishUserId(damand.getCreatorId())
					.publishTime(now)
					.isTop(0)
					.ordering(0)
					.lastReviewTime(now)
					.build();
			baseSupplydemandService.insertInteractiveZoneTopic(add);

			//迁移完成,删除旧数据
			baseSupplydemandService.deleteByList(Arrays.asList(input.getId()));

			//迁移评论数据
			List<UserReviews> list = userReviewsService.findBySupplyDemandIdList(input.getId());
			if (!CollectionUtils.isEmpty(list)){
				List<InteractiveZoneReply> replyList = list.stream()
						.map(item -> {
							return InteractiveZoneReply.builder()
									.blockType(1)
									.topicId(Long.valueOf(item.getBusinessId()))
									.content(item.getReviewsMsg())
									.picIds(item.getPicIds())
									.isTop(item.getIsTop())
									.planId(item.getPlanId())
									.planType(item.getPlanType())
									.houseId(item.getHouseId())
									.dataSource(1)
									.replyUserId(item.getUserId())
									.creator(item.getCreator())
									.gmtCreate(item.getGmtCreate())
									.modifier("数据迁移")
									.creator(basesupplydemandMapper.getNickName(item.getUserId()))
									.isDeleted(0)
									.build();
						})
						.collect(Collectors.toList());
				basesupplydemandMapper.batchInsertInteractiveZoneReply(replyList);
			}
		}
	}

	@Override
    public int delete(String basesupplydemandId) {
        if (Strings.isNullOrEmpty(basesupplydemandId)) {
            return 0;
        }

        Set<Integer> basesupplydemandIds = new HashSet<>();
        List<String> list = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(Strings.nullToEmpty(basesupplydemandId));
        list.stream().forEach(id -> basesupplydemandIds.add(Integer.valueOf(id)));

        if (basesupplydemandIds.size() == 0) {
            return 0;
        }
        return baseSupplydemandService.delete(basesupplydemandIds);
    }

    @Override
    public Basesupplydemand getById(int basesupplydemandId) {
        Basesupplydemand result = baseSupplydemandService.getById(basesupplydemandId);
		if (result == null) {
			return result;
		}

		//设置地区名称
		List<String> areaCodes = Arrays.asList(result.getProvince(), result.getCity(), result.getDistrict(), result.getStreet());
		Map<String, String> code2Name = baseAreaService.code2Name(new HashSet<>(areaCodes));
		if (StringUtils.isNotEmpty(result.getProvince())) {
			result.setProvinceName(code2Name.get(result.getProvince()));
		}
		if (StringUtils.isNotEmpty(result.getCity())) {
			result.setCityName(code2Name.get(result.getCity()));
		}
		if (StringUtils.isNotEmpty(result.getDistrict())) {
			result.setDistrictName(code2Name.get(result.getDistrict()));
		}
		if (StringUtils.isNotEmpty(result.getStreet())) {
			result.setStreetName(code2Name.get(result.getStreet()));
		}

		// xxx : CMS-711, 供求信息中添加户型、方案、点赞数、浏览量
        // create by zhoujc at  2019/2/1 9:56.
        //点赞数、浏览量
		List<com.sandu.api.basesupplydemand.model.NodeDetails> details = nodeInfoService.listNodeDetails(result.getId(),
                NODE_TYPE_SUPPLY_DEMAND,
				Arrays.asList(DETAIL_TYPE_VIEW, DETAIL_TYPE_LIKE, DETAIL_TYPE_VIRTUAL_VIEW, DETAIL_TYPE_VIRTUAL_LIKE));
        for (NodeDetails detail : details) {
			result.setViewCount(result.getViewNum());
			if (detail.getDetailsType().equals(DETAIL_TYPE_LIKE)) {
				result.setLikeCount(detail.getValue());
			}
			if (detail.getDetailsType().equals(DETAIL_TYPE_VIRTUAL_VIEW)) {
				result.setVirtualViewCount(detail.getValue());
			}
			if (detail.getDetailsType().equals(DETAIL_TYPE_VIRTUAL_LIKE)) {
				result.setVirtualLikeCount(detail.getValue());
			}
        }


		List<DemandInfoRel> infoRels = basesupplydemandMapper.getDemandRelInfo(basesupplydemandId);
		if (infoRels == null) {
			return result;
		}

		result.setHouseInfo(new HashMap<>());
		result.setPlanInfo(new HashMap<>());
		for (DemandInfoRel infoRel : infoRels) {
			//方案名称
			if (infoRel.getPlanId() != null) {
				result.getPlanInfo().put(infoRel.getPlanId(), fetchPlanNameByIdAndType(infoRel.getPlanId(), infoRel.getPlanType()));
			}
//			result.setPlanName(fetchPlanNameByIdAndType(infoRel.getPlanId(), infoRel.getPlanType()));
			// 户型名称
			if (infoRel.getHouseId() != null) {
				result.getHouseInfo().put(infoRel.getHouseId(), fetchHouseNameDetails(infoRel.getHouseId()));
			}
//			result.setHouseName(fetchHouseNameDetails(infoRel.getHouseId()));
		}
		return result;
	}

	@Override
	public String fetchPlanNameByIdAndType(@NotNull Integer planId, @NotNull Integer planType) {
		return basesupplydemandMapper.fetchPlanNameByIdAndType(planId, planType);
	}

	@Override
	public List<ResPic> getResPicByIds(List<Long> picIds) {
		if (picIds == null || picIds.isEmpty()) {
			return Collections.emptyList();
		}
		return basesupplydemandMapper.getResPicByIds(picIds);
	}

	@Override
	public String fetchHouseNameDetails(@NotNull Integer houseId) {
		BaseHouse house = basesupplydemandMapper.getBaseHouseByHouseId(houseId);
		if (house == null) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(Strings.nullToEmpty(house.getHouseCommonCode()));
		//处理户型类型（几室几厅）
		List<SpaceCommon> spaceDetails = basesupplydemandMapper.listSpaceDetailsByHouseId(house.getId());
		if (spaceDetails.isEmpty()) {
			return result.toString();
		}
		Map<Short, List<SpaceCommon>> collect = spaceDetails.stream().collect(Collectors.groupingBy(SpaceCommon::getOrigin));
		List<SpaceCommon> newSpace = collect.get(Short.valueOf(1 + ""));
		if (newSpace == null) {
			newSpace = collect.get(Short.valueOf(0 + ""));
		}
		String typeName = newSpace.stream()
				.filter(it -> StringUtils.isNotEmpty(it.getSpaceName()))
				.map(it -> it.getSpaceNum() + it.getSpaceName()).collect(Collectors.joining(""));
		result.append("-").append(typeName).append("-").append(house.getTotalArea());
		return result.toString();
	}

	@Override
    public PageInfo<Basesupplydemand> query(BasesupplydemandQuery query) {
        return baseSupplydemandService.findAll(query);
    }

    @Override
    public int baseSupplyToTop(String basesupplydemandId, String topId) {
        return baseSupplydemandService.baseSupplyToTop(basesupplydemandId, topId);
    }

    @Override
    public int baseSupplyToRefresh(String basesupplydemandId, String topId) {
        return baseSupplydemandService.baseSupplyToRefresh(basesupplydemandId, topId);
    }

    @Override
    public int deleteByList(List<Integer> ids) {
        return baseSupplydemandService.deleteByList(ids);
    }
}
