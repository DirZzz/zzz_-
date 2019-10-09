package com.sandu.service.basesupplydemand.impl.biz;

import java.util.List;

/**
 * @author Sandu
 * @ClassName NodeInfoServiceImpl
 * @date 2018/11/6
 */
public interface NodeInfoService {
	List<NodeDetails> listNodeDetails(Integer contendId, Integer nodeType, List<Integer> detailTypes);

	int updateNodeDetailsValue(Integer contentId, int nodeTypeSupplyDemand, int detailsType, Integer value);
}
