package com.sandu.cloud.activity.bargain.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.activity.bargain.dto.BargainInviteRecordDto;
import com.sandu.cloud.activity.bargain.model.BargainDecorateRecord;
import com.sandu.cloud.activity.bargain.model.BargainInviteRecord;
import com.sandu.cloud.activity.bargain.service.BargainDecorateRecordService;
import com.sandu.cloud.activity.bargain.service.BargainInviteRecordService;
import com.sandu.cloud.common.vo.PageResultDto;
import com.sandu.cloud.common.vo.ResponseEnvelope;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "ActBargain", description = "砍价活动")
@RestController
@RequestMapping("/v1/act/bargain/reg/invite")
@Validated
public class BargainInviteRecordController {
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
    @Autowired
    private BargainInviteRecordService bargainInviteRecordService;
    
    @Autowired
    private BargainDecorateRecordService bargainDecorateRecordService;
    
    @ApiOperation(value = "获取砍价详情列表", response = ResponseEnvelope.class)
    @GetMapping("/getInviteRecordList")
    public ResponseEnvelope pageList(@NotNull(message = "任务id不能为空!") @RequestParam(value="regId") String regId,
			@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
    	
		PageResultDto<BargainInviteRecord> pageList = bargainInviteRecordService.pageList(regId,pageNum,pageSize);
		PageResultDto<BargainInviteRecordDto> retPageList = buildPageResult(pageList);
		
		//如果是首页,则加上装进我家的记录
		if(pageNum == 1) {
			BargainDecorateRecord decorateRecord =  bargainDecorateRecordService.getByRegId(regId);
			if(decorateRecord!=null) {
				//WxActBargainInviteRecordVO temp = new WxActBargainInviteRecordVO("装进我家("+decorateRecord.getHouseName()+")",decorateRecord.getHeadPic(),decorateRecord.getCutPrice(),sdf.format(decorateRecord.getGmtCreate()));
				BargainInviteRecordDto temp = new BargainInviteRecordDto("装进我家",decorateRecord.getHeadPic(),decorateRecord.getCutPrice(),sdf.format(decorateRecord.getGmtCreate()));
				retPageList.getList().add(0,temp);
			}
		}
		return ResponseEnvelope.ok("ok",retPageList);
    	
    }
    
    private PageResultDto<BargainInviteRecordDto> buildPageResult(PageResultDto<BargainInviteRecord> pageList){
    	PageResultDto<BargainInviteRecordDto> retList = new PageResultDto<BargainInviteRecordDto>();
    	List<BargainInviteRecordDto> list = new ArrayList<BargainInviteRecordDto>();
    	if(pageList!=null && pageList.getList()!=null&& pageList.getList().size()>0) {
    		for(BargainInviteRecord record : pageList.getList()) {
    			list.add(new BargainInviteRecordDto(record.getNickname(),record.getHeadPic(),record.getCutPrice(),sdf.format(record.getGmtCreate())));
    		}
    	}
    	retList.setTotal(pageList.getTotal());
    	retList.setList(list);
    	return retList;    	
    }

}
