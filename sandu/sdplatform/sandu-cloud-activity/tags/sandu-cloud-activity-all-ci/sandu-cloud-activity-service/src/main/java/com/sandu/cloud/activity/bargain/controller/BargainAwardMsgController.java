package com.sandu.cloud.activity.bargain.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgAddDto;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgDto;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgQueryDto;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgUpdateDto;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgWebDto;
import com.sandu.cloud.activity.bargain.model.BargainAwardMsg;
import com.sandu.cloud.activity.bargain.service.BargainAwardMsgService;
import com.sandu.cloud.common.login.LoginContext;
import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.common.vo.PageResultDto;
import com.sandu.cloud.common.vo.ResponseEnvelope;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "WxActBargainAwardMsg", description = "领奖消息")
@RestController
@RequestMapping("/v1/act/bargain/awardmsg")
@Slf4j
@Validated
public class BargainAwardMsgController{
	
    @Autowired
    private BargainAwardMsgService bargainAwardMsgService;
    
    @ApiOperation(value = "领奖消息列表查询", response = ResponseEnvelope.class)
    @PostMapping("/getWxActBargainAwardMsgList")
    public ResponseEnvelope list(@Valid @RequestBody(required=false) BargainAwardMsgQueryDto query) {
        PageResultDto<BargainAwardMsgDto> pageResult = bargainAwardMsgService.pageList(query);
        return ResponseEnvelope.ok(pageResult);
       
    }

    
    @ApiOperation(value = "领奖消息列表查询(随机)", response = ResponseEnvelope.class)
    @GetMapping("/getWxActBargainAwardMsgRandomList")
    public ResponseEnvelope randomList(@NotNull(message = "活动Id不能为空") String actId) {
        List<BargainAwardMsgWebDto> list = bargainAwardMsgService.getMsgRandomList(actId);
        return ResponseEnvelope.ok("领奖消息列表查询完成",list);
        
    }


    @ApiOperation(value = "领奖消息修改", response = ResponseEnvelope.class)
    @PostMapping("/modifyWxActBargainAwardMsg")
    public ResponseEnvelope modify(@Valid @RequestBody(required=false) BargainAwardMsgUpdateDto wxActBargainAwardMsgUpdate
            ) {
        // 获取登录用户
        LoginUser loginUser = LoginContext.getLoginUser(); 
        //数据转换
        wxActBargainAwardMsgUpdate.setGmtModified(new Date());
        wxActBargainAwardMsgUpdate.setModifier(loginUser.getUsername());
        BargainAwardMsg msg = wxActBargainAwardMsgUpdate.dataTransfer(wxActBargainAwardMsgUpdate);
        
        int result = bargainAwardMsgService.modifyById(msg);
        if (result>0){
            return ResponseEnvelope.ok("修改成功");
        }
        return ResponseEnvelope.ok("修改失败");
         
    }
    
    @ApiOperation(value = "领奖消息删除", response = ResponseEnvelope.class)
    @PostMapping("/removeWxActBargainAwardMsg")
    public ResponseEnvelope remove(@NotNull(message="请传入领取Id") @RequestParam(required=false,name="awardMsgId") String awardMsgId) {
        // 获取登录用户
        LoginUser loginUser = LoginContext.getLoginUser();
        int result = bargainAwardMsgService.remove(awardMsgId,loginUser.getUsername());
        if (result>0){
            return ResponseEnvelope.ok("删除成功");
        }
        return ResponseEnvelope.ok("删除失败");
    }


    @ApiOperation(value = "领奖消息新增", response = ResponseEnvelope.class)
    @PostMapping("/createWxActBargainAwardMsg")
    public ResponseEnvelope create(@Valid @RequestBody(required=false) BargainAwardMsgAddDto awardMsgAdd) {
      
        log.info("领奖消息新增---入参：awardMsgAdd{}",awardMsgAdd);
        // 获取登录用户
        LoginUser loginUser = LoginContext.getLoginUser();

        //参数转化
        BargainAwardMsg msg=new BargainAwardMsg();
        String Id = UUID.randomUUID().toString().replace("-", "");
        msg.setId(Id);
        msg.setActId(awardMsgAdd.getActId());
        msg.setMessage(awardMsgAdd.getMessage());
        msg.setAppId(awardMsgAdd.getAppId());
        msg.setCreator(loginUser.getUsername());
        msg.setModifier(loginUser.getUsername());
        msg.setGmtCreate(new Date());
        msg.setGmtModified(new Date());
        msg.setIsDeleted(0);
        msg.setRegistrationId(loginUser.getId().toString());
         
        bargainAwardMsgService.create(msg);
        return ResponseEnvelope.ok("新增领奖消息成功!");
       
    }


}
