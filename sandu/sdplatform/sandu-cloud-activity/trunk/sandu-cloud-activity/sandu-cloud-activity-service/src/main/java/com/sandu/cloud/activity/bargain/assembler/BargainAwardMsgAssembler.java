package com.sandu.cloud.activity.bargain.assembler;

import org.springframework.beans.BeanUtils;

import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgDto;
import com.sandu.cloud.activity.bargain.model.BargainAwardMsg;

public class BargainAwardMsgAssembler {

	public static BargainAwardMsgDto assemble(BargainAwardMsg bargainAwardMsg) {
		BargainAwardMsgDto vo = new BargainAwardMsgDto();
		BeanUtils.copyProperties(bargainAwardMsg, vo);
		return vo;
	}
}
