package com.sandu.service.basesupplydemand.impl.biz;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Sandu
 * @ClassName NodeDetails
 * @date 2018/11/6
 */
@Data
public class NodeDetails implements Serializable {
	private Integer nodeId;
	private Integer contendId;
	private Integer nodeType;
	private Integer detailsType;
	private Integer value;

}
