package com.sandu.service.basesupplydemand.impl.biz;

import com.sandu.api.basesupplydemand.model.NodeDetails;
import com.sandu.api.basesupplydemand.service.NodeInfoService;
import com.sandu.api.storage.service.CacheService;
import com.sandu.service.basesupplydemand.dao.BasesupplydemandMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sandu
 * @ClassName NodeInfoServiceImpl
 * @date 2018/11/6
 */

@Service
public class NodeInfoServiceImpl implements NodeInfoService {
	@Autowired
	private BasesupplydemandMapper basesupplydemandMapper;

	@Autowired
	private CacheService cacheService;

	@Override
	public List<NodeDetails> listNodeDetails(
			@NotNull Integer contendId,
			@NotNull Integer nodeType,
			@NotNull List<Integer> detailTypes) {
		return this.listNodeDetails(Collections.singletonList(contendId), nodeType, detailTypes);
	}

	@Override
	public List<NodeDetails> listNodeDetails(List<Integer> contendIds, Integer nodeType, List<Integer> detailTypes) {
		if (detailTypes == null || detailTypes.isEmpty() || contendIds == null || contendIds.isEmpty()) {
			return Collections.emptyList();
		}


//		List<NodeDetails> nodeDetails = basesupplydemandMapper.listNodeDetails(contendIds, nodeType, Collections.emptyList());
		List<NodeDetails> nodeDetails = basesupplydemandMapper.listNodeInfoByContentIdsAndNodeType(contendIds, nodeType);

		List<Integer> existContentId = nodeDetails.stream().map(NodeDetails::getContentId).collect(Collectors.toList());
		contendIds.forEach(it -> {
			if (!existContentId.contains(it)) {
				Integer nodeId = basesupplydemandMapper.insertNodeInfo(it, nodeType);
				NodeDetails tmp = new NodeDetails();
				tmp.setContentId(it);
				tmp.setNodeId(nodeId);
				nodeDetails.add(tmp);
			}
		});

		List<NodeDetails> result = new LinkedList<>();
		for (NodeDetails it : nodeDetails) {
			for (Integer detailType : detailTypes) {
				NodeDetails tmp = new NodeDetails();
				BeanUtils.copyProperties(it, tmp);
				tmp.setDetailsType(detailType);
				result.add(tmp);
			}
		}

		return result.stream()
				.peek(detail -> detail.setValue(cacheService.getCountByNodeIdAndDetailType(detail.getNodeId(), detail.getDetailsType())))
				.collect(Collectors.toList());

	}

	@Override
	public int updateNodeDetailsValue(Integer contentId, int nodeTypeSupplyDemand, int detailsType, Integer value) {
		List<NodeDetails> nodeDetails = this.listNodeDetails(contentId, nodeTypeSupplyDemand, Collections.singletonList(detailsType));
		nodeDetails.forEach(it -> cacheService.putCountByNodeIdAndDetailsType(it.getNodeId(), it.getDetailsType(), value));
		return nodeDetails.size();
	}
}
