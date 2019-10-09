package com.sandu.cloud.activity.bargain.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sandu.cloud.activity.bargain.client.CompanyServiceClient;
import com.sandu.cloud.activity.bargain.dto.BargainAddDto;
import com.sandu.cloud.activity.bargain.dto.BargainAndRedDetailsDto;
import com.sandu.cloud.activity.bargain.dto.BargainDto;
import com.sandu.cloud.activity.bargain.dto.BargainUpdateDto;
import com.sandu.cloud.activity.bargain.exception.BargainBizException;
import com.sandu.cloud.activity.bargain.exception.BargainBizExceptionCode;
import com.sandu.cloud.activity.bargain.model.Bargain;
import com.sandu.cloud.activity.bargain.model.BargainRegistration;
import com.sandu.cloud.activity.bargain.service.BargainRegistrationService;
import com.sandu.cloud.activity.bargain.service.BargainService;
import com.sandu.cloud.common.login.LoginContext;
import com.sandu.cloud.common.util.UUIDUtil;
import com.sandu.cloud.common.vo.LoginUser;
import com.sandu.cloud.common.vo.PageResultDto;
import com.sandu.cloud.common.vo.ResponseEnvelope;
import com.sandu.cloud.company.model.Company;
import com.sandu.cloud.company.model.CompanyMiniProgramConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Api(tags = "ActBargain", description = "砍价活动")
@RestController
@RequestMapping("/v1/act/bargain")
@RefreshScope
@Slf4j
@Validated
public class BargainController{
	
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    @Autowired
    private BargainService bargainService;

    @Autowired
    private BargainRegistrationService bargainRegistrationService;

   
    @Autowired
    private CompanyServiceClient companyServiceClient;

    @Value("${wx.act.bargain.url}")
    private String bargainUrl;

    @Value("${upload.base.path}")
    private String rootPath;

    @Value("${wx.act.img.path}")
    private String actImgPath;
    
    @GetMapping(value = "/testConfig")
    public ResponseEnvelope testConfig(){
    	int a=1/0;
        log.info("测试动态刷新配置:"+bargainUrl);
        return ResponseEnvelope.ok(bargainUrl);
    }

    @ApiOperation(value = "查询砍价活动列表", response = ResponseEnvelope.class)
    @PostMapping("/getWxActBargainList")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header",value = "token",required = true,dataType = "String",name = "Authorization"),
        @ApiImplicitParam(paramType = "query",required = true,dataType = "Integer",name="page"),
        @ApiImplicitParam(paramType = "query",required = true,dataType = "Integer",name="limit")
    })
    public ResponseEnvelope<PageResultDto> pageList(@RequestParam(defaultValue="1") Integer page,@RequestParam(defaultValue="10") Integer limit) {
        
		page = (page - 1) * limit;
		List<CompanyMiniProgramConfig> configs = this.getBaseCompanyMiniProConfigInfo();
		if (Objects.nonNull(configs) || !configs.isEmpty()) {
			List<String> appids = configs.stream().map(CompanyMiniProgramConfig::getAppId)
					.collect(Collectors.toList());
			log.info("company appids =>{}" + (appids == null ? 0 : appids.toString()));
			// 根据小程序appids去查询wx_act_bargain砍价活动表,返回企业小程序配置的活动列表
			int total = bargainService.countByAppids(appids);
			List<BargainDto> vos = null;
			if (total > 0) {
				List<Bargain> wxActBargains = bargainService.pageList(appids, page, limit);
				// 复制链接按钮:需要配置一个链接(前端给),到时返回列表的复制链接按钮使用.
				vos = this.transformVO(wxActBargains);
				// 统计参与人数与成功砍价人数
				this.countJoinANDSuccess(vos);
			}
			
			return ResponseEnvelope.ok(new PageResultDto(vos,total));
		}
		return ResponseEnvelope.ok(new PageResultDto(Collections.EMPTY_LIST,0));
        
    }

    private void countJoinANDSuccess(List<BargainDto> vos) {
        List<String> ids = vos.stream().map(BargainDto::getId).collect(Collectors.toList());
        List<BargainRegistration> wxActBargains =  bargainRegistrationService.list(ids);
        vos.stream().forEach(vo ->{
            //设置活动参与人数
            List<BargainRegistration> wxActBargainRegistrations = wxActBargains.stream().filter(wx -> {
                return Objects.equals(wx.getActId(), vo.getId());
            }).collect(Collectors.toList());

            //统计砍价成功人数
            long bargainSuccessfulPeoples = wxActBargainRegistrations.stream().filter(item ->{
                return Objects.equals(item.getCompleteStatus(),10) && Objects.equals(item.getExceptionStatus(),0);
            }).count();

            vo.setActJoinPeoples(wxActBargainRegistrations.size());
            vo.setBargainSuccessfulPeoples(new Long(bargainSuccessfulPeoples).intValue());
        });
    }

    private List<CompanyMiniProgramConfig> getBaseCompanyMiniProConfigInfo() {
    	LoginUser loginUser = LoginContext.getLoginUser();
        ////根据登录用户获到用户所属企业有几个小程序
        return companyServiceClient.getCompanyMiniProgramConfigs(loginUser.getCompanyId());

    }

    private List<BargainDto> transformVO(List<Bargain> wxActBargains) {
        if (Objects.nonNull(wxActBargains) && !wxActBargains.isEmpty()) {
            wxActBargains.forEach(item -> item.setCopyUrl(bargainUrl + "?actId=" + item.getId()));
            //转换VO
            List<BargainDto> vos = wxActBargains.stream().map(model -> {
                model.setActStatus(bargainService.getBargainStatus(model));
                BargainDto vo = BargainDto.builder().build();
                BeanUtils.copyProperties(model, vo);
                return vo;
            }).collect(Collectors.toList());
            return vos;
        }
        return Collections.EMPTY_LIST;
    }

    @ApiOperation(value = "获取用户所属企业小程序列表", response = ResponseEnvelope.class)
    @PostMapping("/getWxMiniProgramList")
    @ApiImplicitParam(paramType = "header",value = "token",required = true,dataType = "String",name = "Authorization")
    public ResponseEnvelope getWxMiniProgramList() {
        //根据登录用户获到用户所属企业sys_user(companyId)
        //根据companyId查询表base_company_mini_program_config获取微信小程序列表
        //表base_company_mini_program_config需要加小程序名称字段
        List<CompanyMiniProgramConfig> baseCompanyMiniProConfigInfo = this.getBaseCompanyMiniProConfigInfo();
        return ResponseEnvelope.ok(baseCompanyMiniProConfigInfo);
       
    }

    @ApiOperation(value = "上传活动图片",response = ResponseEnvelope.class)
    @PostMapping(value = "/uploadActImg")
    public ResponseEnvelope uploadActImg(@NotNull(message="请选择要上传的图片") MultipartFile file){
        Object imgPath = this.executeUploadFile(file);
        return ResponseEnvelope.ok(imgPath);
    }

    private String executeUploadFile(MultipartFile file){
        StringBuffer sb = new StringBuffer();
        sb.append(rootPath);
        sb.append(actImgPath);
        //sb.append("C:\\Users\\Sandu\\Desktop\\压缩");
        File dir = new File(sb.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String name = file.getOriginalFilename();
        //上传文件名
        String filename = StringUtils.substringBeforeLast(name, ".");
        //后缀
        String suffix = StringUtils.substringAfterLast(name, ".");
        //大小
        Long size = file.getSize();
        long millis = System.currentTimeMillis();
        sb.append("/");
        sb.append(millis);
        sb.append(".");
        sb.append(suffix);

        //上传文件
        String filePath = sb.toString();
        // 1、生成缩略图，并写入磁盘
        File targetFile = new File(filePath);
        try {
            Thumbnails.of(file.getInputStream()).size(750, 750).toFile(targetFile);
        } catch (IOException e) {
        	log.error("生成缩略图异常:", e);
            //发生异常,上传原图
            try {
				file.transferTo(targetFile);
			} catch (Exception ex) {
				log.error("上传原图异常:", e);
				throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_UPLOAD_IMAGE_ERROR);
			} 
        }

        //file.transferTo(targetFile);
        log.info("文件上传的路劲"+filePath);

        StringBuffer sBuffer = new StringBuffer(actImgPath);
        sBuffer.append("/");
        sBuffer.append(millis);
        sBuffer.append(".");
        sBuffer.append(suffix);
        String savePath = sBuffer.toString();
        log.error("数据库文件存储路径"+savePath);

        return savePath;
    }
    
    
    @ApiOperation(value = "创建砍价活动", response = ResponseEnvelope.class)
    @PostMapping("/create")
    public ResponseEnvelope create(@RequestBody(required=false) @Valid BargainAddDto wxActBargainAdd) {
        
        if (wxActBargainAdd.getMyCutPriceMax() < wxActBargainAdd.getMyCutPriceMin()){
        	throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_MY_CUT_PRICE_MAX_PRICE_MUST_BIGGER_THAN_MIN_PRICE);
        }

        if (wxActBargainAdd.getCutMethodPriceMax() < wxActBargainAdd.getCutMethodPriceMin()){
        	throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_CUT_PRICE_MAX_PRICE_MUST_BIGGER_THAN_MIN_PRICE);
        }

        if (wxActBargainAdd.getEndTime().compareTo(wxActBargainAdd.getBegainTime()) < 0){
        	throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_END_DATE_MUST_AFTER_BEGIN_DATE);
        }

        if (wxActBargainAdd.getProductCount() <= 0){
        	throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_STOCK_CAN_NOT_BE_NULL);
        }
        
        if (wxActBargainAdd.getProductDisplayCount()<wxActBargainAdd.getProductCount()){
        	throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_DISPLAY_STOCK_MUST_BIGGER_THAN_REAL_STOCK);
        }
        
        if(wxActBargainAdd.getOnlyAllowNew()==0) {
        	if(wxActBargainAdd.getHelpCutPerDay()==null && wxActBargainAdd.getHelpCutPerAct()==null) {
        		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_DAY_MUST_NOT_BE_NULL);
        	}
        	if(wxActBargainAdd.getHelpCutPerAct()!=null && wxActBargainAdd.getHelpCutPerAct()<=0 ) {
        		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_ACT_MUST_NOT_BE_NULL);
        	}else if(wxActBargainAdd.getHelpCutPerAct()==null) {
        		if(wxActBargainAdd.getHelpCutPerDay()!=null && wxActBargainAdd.getHelpCutPerDay()<=0) {
        			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_DAY_MUST_NOT_BE_NULL2);
            	}
        	} else {
        		wxActBargainAdd.setHelpCutPerDay(null);
        	}
        	
        }

//            WxActBargain wx = wxActBargainService.getWxActBargainByActName(wxActBargainAdd.getActName());
//            if (Objects.nonNull(wx)){
//                //同一小程序下名称不能重复
//                if(Objects.equals(wx.getAppId(),wxActBargainAdd.getAppId())){
//                    return new ResponseEnvelope(false,"活动名称已占用");
//                }
//            }
        LoginUser loginUser = LoginContext.getLoginUser();
        Bargain wxActBargain = this.buildWxActBargain(wxActBargainAdd,loginUser);
        bargainService.create(wxActBargain);
        return ResponseEnvelope.ok();
        
    }

    private Bargain buildWxActBargain(BargainAddDto wxActBargainAdd, LoginUser loginUser) {
        Bargain wxActBargain = new Bargain();
        addSystemFiled(wxActBargainAdd,loginUser);
        addCompanyInfo(wxActBargainAdd);
        BeanUtils.copyProperties(wxActBargainAdd, wxActBargain);
        return wxActBargain;
    }

    private void addCompanyInfo(BargainAddDto wxActBargainAdd) {
        //获取小程序名称
        CompanyMiniProgramConfig config = companyServiceClient.getCompanyMiniProConfig(wxActBargainAdd.getAppId());
        //获取小程序所属企业名称
        Company company = companyServiceClient.get(config.getCompanyId());
        wxActBargainAdd.setAppName(config.getMinProName());
        wxActBargainAdd.setCompanyId(config.getCompanyId().intValue());
        wxActBargainAdd.setCompanyName(company.getCompanyName());
    }

    private void addSystemFiled(BargainAddDto wxActBargainAdd, LoginUser loginUser) {
        Date now = new Date();
        wxActBargainAdd.setCreator(loginUser.getNickName());
        wxActBargainAdd.setModifier(loginUser.getNickName());
        wxActBargainAdd.setGmtCreate(now);
        wxActBargainAdd.setGmtModified(now);
        wxActBargainAdd.setId(UUIDUtil.getUUID());
        wxActBargainAdd.setIsEnable(1);
        wxActBargainAdd.setProductRemainCount(wxActBargainAdd.getProductCount());
        wxActBargainAdd.setProductVitualCount(0);
        wxActBargainAdd.setIsDeleted(0);
        wxActBargainAdd.setRegistrationCount(0);
        if (Objects.isNull(wxActBargainAdd.getSysReduceNum())){
            wxActBargainAdd.setSysReduceNum(0);
        }
    }

    @ApiOperation(value = "修改砍价活动", response = ResponseEnvelope.class)
    @PostMapping("/modifyWxActBargain")
    public ResponseEnvelope modify(@RequestBody(required=false) @Valid BargainUpdateDto wxActBargainUpdate) {
        
        Bargain wxActBargain = bargainService.get(wxActBargainUpdate.getId());
        Integer actStatus = bargainService.getBargainStatus(wxActBargain);
        if (actStatus == Bargain.STATUS_UNBEGIN){
        	if(wxActBargainUpdate.getOnlyAllowNew()==0) {
        		  
            	if(wxActBargainUpdate.getHelpCutPerDay()==null && wxActBargainUpdate.getHelpCutPerAct()==null) {
            		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_DAY_MUST_NOT_BE_NULL);
            	}
            	if(wxActBargainUpdate.getHelpCutPerAct()!=null && wxActBargainUpdate.getHelpCutPerAct()<=0 ) {
            		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_ACT_MUST_NOT_BE_NULL);
              	}else if(wxActBargainUpdate.getHelpCutPerAct()==null) {
              		if(wxActBargainUpdate.getHelpCutPerDay()!=null && wxActBargainUpdate.getHelpCutPerDay()<=0) {
              			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_DAY_MUST_NOT_BE_NULL2);
                  	}
              	}
            	
            	if(wxActBargainUpdate.getHelpCutPerAct()!=null && wxActBargainUpdate.getHelpCutPerAct()>0) {
            		wxActBargainUpdate.setHelpCutPerDay(0);
            	}else if(wxActBargainUpdate.getHelpCutPerDay()!=null && wxActBargainUpdate.getHelpCutPerDay()>0) {
            		wxActBargainUpdate.setHelpCutPerAct(0);
            	}
            }else {
            	wxActBargainUpdate.setHelpCutPerAct(0);
            	wxActBargainUpdate.setHelpCutPerDay(0);
            }
            //活动还没有开始,可以更新
            Bargain updateWxActBargain = new Bargain();
            BeanUtils.copyProperties(wxActBargainUpdate, updateWxActBargain);
            bargainService.modifyById(updateWxActBargain);
            return ResponseEnvelope.ok();
        }else if (actStatus == Bargain.STATUS_ONGOING){
        	
        	if(wxActBargainUpdate.getOnlyAllowNew()==0) {
            	if(wxActBargainUpdate.getHelpCutPerAct()==null && wxActBargainUpdate.getHelpCutPerDay()==null) {
            		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_DAY_MUST_NOT_BE_NULL);
            	}
            	if(wxActBargainUpdate.getHelpCutPerAct()!=null && wxActBargainUpdate.getHelpCutPerAct()<=0 ) {
            		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_ACT_MUST_NOT_BE_NULL);
              	}else if(wxActBargainUpdate.getHelpCutPerAct()==null) {
              		if(wxActBargainUpdate.getHelpCutPerDay()!=null && wxActBargainUpdate.getHelpCutPerDay()<=0) {
              			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HELP_CUT_PER_DAY_MUST_NOT_BE_NULL2);
                  	}
              	}
            	
            	if(wxActBargainUpdate.getHelpCutPerAct()!=null && wxActBargainUpdate.getHelpCutPerAct()>0) {
            		wxActBargainUpdate.setHelpCutPerDay(0);
            	}else if(wxActBargainUpdate.getHelpCutPerDay()!= null && wxActBargainUpdate.getHelpCutPerDay()>0) {
            		wxActBargainUpdate.setHelpCutPerAct(0);
            	}
            }else {
            	wxActBargainUpdate.setHelpCutPerAct(0);
            	wxActBargainUpdate.setHelpCutPerDay(0);
            }
        	Bargain updateWxActBargain = new Bargain();
        	updateWxActBargain.setMyCutPriceMax(wxActBargainUpdate.getMyCutPriceMax());
        	updateWxActBargain.setMyCutPriceMin(wxActBargainUpdate.getMyCutPriceMin());
        	updateWxActBargain.setCutMethodPriceMax(wxActBargainUpdate.getCutMethodPriceMax());
        	updateWxActBargain.setCutMethodPriceMin(wxActBargainUpdate.getCutMethodPriceMin());
        	updateWxActBargain.setOnlyAllowNew(wxActBargainUpdate.getOnlyAllowNew());
        	updateWxActBargain.setHelpCutPerAct(wxActBargainUpdate.getHelpCutPerAct());
        	updateWxActBargain.setHelpCutPerDay(wxActBargainUpdate.getHelpCutPerDay());
        	updateWxActBargain.setId(wxActBargainUpdate.getId());
        	bargainService.modifyById(updateWxActBargain);
            return ResponseEnvelope.ok();
        }else{
        	throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_HAS_FINISH);
        }
        
    }


    @ApiOperation(value = "获取活动详情接口", response = ResponseEnvelope.class)
    @PostMapping("/getWxActBargainDetails")
    public ResponseEnvelope get(@NotEmpty(message="活动id不能为空") String actId) {       
        //根据actId 查询wx_act_bargain砍价活动表
        BargainDto vo = null;
        Bargain wxActBargain = bargainService.get(actId);
        if (Objects.nonNull(wxActBargain)){
            vo = BargainDto.builder().build();
            transfromVO(wxActBargain,vo);
            if(vo.getHelpCutPerAct()!=null && vo.getHelpCutPerAct()==0) {
            	vo.setHelpCutPerAct(null);
            }
            if(vo.getHelpCutPerDay()!=null && vo.getHelpCutPerDay()==0) {
            	vo.setHelpCutPerDay(null);
            }
        }
        return ResponseEnvelope.ok(vo);        
    }

    public void transfromVO(Object currentObj,Object targetObj){
        BeanUtils.copyProperties(currentObj,targetObj);
    }

    @ApiOperation(value = "结束活动", response = ResponseEnvelope.class)
    @PostMapping("/finishWxActBargain")
    public ResponseEnvelope finish(@NotEmpty(message="活动id不能为空") String actId) {
        //update wx_act_bargain is_enable = 0
        Bargain update = this.buildUpdateObjecet(actId);
        update.setIsEnable(0);
        bargainService.modifyById(update);
        return ResponseEnvelope.ok();
    }


    private Bargain buildUpdateObjecet(String actId) {
    	LoginUser loginUser = LoginContext.getLoginUser();
        Date now = new Date();
        Bargain update = new Bargain();
        update.setId(actId);
        update.setModifier(loginUser.getNickName());
        update.setGmtModified(now);
        return update;
    }

    @ApiOperation(value = "删除活动", response = ResponseEnvelope.class)
    @PostMapping("/removeWxActBargain")
    public ResponseEnvelope remove(@NotEmpty(message="活动id不能为空") String actId) {
        //如果活动正在进行,提示请先结束活动再删除
	    Bargain wxActBargain = bargainService.get(actId);
	    if (Objects.isNull(wxActBargain)){
	        throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_NOT_EXIST);
	    }
	    //If(is_enable&&(endTime<currentTime||库存<=0))
	    //update wx_act_bargain is_deleted = 1 where id =actId
	    if (Objects.equals(bargainService.getBargainStatus(wxActBargain),Bargain.STATUS_ENDED)
	                                            ||
	        Objects.equals(bargainService.getBargainStatus(wxActBargain),Bargain.STATUS_UNBEGIN)
	    ){
	        //满足删除的活动的条件
	        Bargain update = buildUpdateObjecet(actId);
	        update.setIsDeleted(1);
	        bargainService.modifyById(update);
	        return ResponseEnvelope.ok();
	    }else{
	    	throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_END_FIRST_BEFORE_DELETE);
	    }
        
    }

    @ApiOperation(value = "设置虚拟库存扣除参数", response = ResponseEnvelope.class)
    @PostMapping("/setWxActBargainAwardMsg")
    public ResponseEnvelope setBargainAwardMsg (@NotEmpty(message="活动id不能为空") String actId, @NotEmpty(message="扣除数量不能为空") Integer sysDecreaseNum) {
        //update wx_act_bargain set is_ sys_decrease_num =? where id=actId
       
		LoginUser loginUser = LoginContext.getLoginUser();
        //查询待修改的活动详情
        Bargain wxActBargain = bargainService.get(actId);
        //虚拟扣除参数要小于库存剩余数量
        if (sysDecreaseNum>wxActBargain.getProductRemainCount()){
        	throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_DECREASE_NUM_CAN_NOT_BIGGER_THAN_REMAIN_STOCK);
        }
        //处理要修改的数据
        wxActBargain.setSysReduceNum(sysDecreaseNum);
        wxActBargain.setModifier(loginUser.getUsername());
        wxActBargain.setGmtModified(new Date());
        int result = bargainService.modifyById(wxActBargain);
        if (result>0){
            return ResponseEnvelope.ok();
        }
        throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_OPT_ERROR);
    }

    @ApiOperation(value = "获取活动详情接口", response = ResponseEnvelope.class)
    @GetMapping("/getActAndRegDetails")
    public ResponseEnvelope getWxActBargainAndRegistrationDetails(@NotEmpty(message="活动id不能为空") String actId,String regId) {
		LoginUser loginUser = LoginContext.getLoginUser();
		//返回砍价金额
		Bargain actEntity = bargainService.get(actId,loginUser.getAppId());
		if(actEntity==null) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_NOT_EXIST);
		}
		BargainRegistration regEntity = null;
		if(StringUtils.isNotBlank(regId)) {
			regEntity = bargainRegistrationService.get(regId);
		}
		BargainAndRedDetailsDto vo = this.buildResultVo(actEntity, regEntity);
		return ResponseEnvelope.ok(vo);
    }

    private BargainAndRedDetailsDto buildResultVo(Bargain actEntity,BargainRegistration regEntity) {
    	BargainAndRedDetailsDto vo = new BargainAndRedDetailsDto();
    	vo.setActName(actEntity.getActName());
    	vo.setActRule(actEntity.getActRule());
    	vo.setProductImg(actEntity.getProductImg());
    	vo.setProductDiscountPrice(actEntity.getProductDiscountPrice());
    	vo.setProductOriginalPrice(actEntity.getProductOriginalPrice());
    	vo.setShareImg(actEntity.getShareImg());
    	vo.setBegainTime(dateFormat.format(actEntity.getBegainTime()));
    	vo.setEndTime(dateFormat.format(actEntity.getEndTime()));
    	//倒计时:由服务端统一处理,客户端的当前时间可能不一样.返回一个部的秒数,给前端倒计时就可以了.
    	Long actRemainTime = (actEntity.getEndTime().getTime()-new Date().getTime())/1000;
    	actRemainTime = actRemainTime<0?0:actRemainTime;
    	vo.setActRemainTime(actRemainTime);
    	//参与人数=真实参加人数(registrationCount)+每小时递增的
    	vo.setRegistrationCount(actEntity.getRegistrationCount()+actEntity.getProductVitualCount());
    	if(Bargain.STATUS_ENDED==bargainService.getBargainStatus(actEntity)) {
    		vo.setProductRemainCount(0);
    		vo.setActRemainTime(0L);
    	}else {
    		vo.setProductRemainCount(actEntity.getProductDisplayCount());
    	}
    	
    	vo.setProductName(actEntity.getProductName());
    	//任务已开始
    	if(regEntity!=null) {
    		vo.setProductRemainPrice(regEntity.getProductRemainPrice());
    	}
    	//任务未开始
    	else {
    		vo.setProductRemainPrice(actEntity.getProductDiscountPrice()-actEntity.getProductMinPrice());
    	}
    	vo.setProductMinPrice(actEntity.getProductMinPrice());
    	return vo;
    }


}
