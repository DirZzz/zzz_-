package com.sandu.cloud.activity.bargain.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.cloud.activity.bargain.assembler.BargainAwardMsgAssembler;
import com.sandu.cloud.activity.bargain.dao.BargainAwardMsgDao;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgDto;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgQueryDto;
import com.sandu.cloud.activity.bargain.dto.BargainAwardMsgWebDto;
import com.sandu.cloud.activity.bargain.model.BargainAwardMsg;
import com.sandu.cloud.activity.bargain.service.BargainAwardMsgService;
import com.sandu.cloud.common.vo.PageResultDto;

import lombok.extern.slf4j.Slf4j;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * awardMsg
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-Nov-20 14:33
 */
@Slf4j
@Service
public class BargainAwardMsgServiceImpl implements BargainAwardMsgService {

    @Autowired
    private BargainAwardMsgDao bargainAwardMsgDao;

	@Override
	public void create(BargainAwardMsg wxActBargainAwardMsg) {
		bargainAwardMsgDao.insert(wxActBargainAwardMsg);
	}

	@Override
	public int modifyById(BargainAwardMsg bargainAwardMsg) {
		return bargainAwardMsgDao.updateByPrimaryKeySelective(bargainAwardMsg);
	}

	@Override
	public int remove(String awardMsgId,String modifier) {
		BargainAwardMsg obj = new BargainAwardMsg();
		obj.setIsDeleted(1);
		obj.setModifier(modifier);
		obj.setGmtModified(new Date());
		obj.setId(awardMsgId);
		return bargainAwardMsgDao.updateByPrimaryKeySelective(obj);
	}

	@Override
	public BargainAwardMsg get(String awardMsgId) {
		// TODO Auto-generated method stub
		return bargainAwardMsgDao.selectByPrimaryKey(awardMsgId);
	}




	@Override
	public List<BargainAwardMsg> getListByActId(BargainAwardMsgQueryDto query) {
		BargainAwardMsg obj = new BargainAwardMsg();
		obj.setIsDeleted(0);
		obj.setActId(query.getActId());
		return bargainAwardMsgDao.select(obj);
	}

	@Override
	public int getCountByActId(BargainAwardMsgQueryDto query) {
		BargainAwardMsg obj = new BargainAwardMsg();
		obj.setIsDeleted(0);
		obj.setActId(query.getActId());
		return bargainAwardMsgDao.selectCount(obj);
	}
	
	@Override
	public PageResultDto<BargainAwardMsgDto> pageList(BargainAwardMsgQueryDto query) {
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		List<BargainAwardMsg> list = this.getListByActId(query);
		List<BargainAwardMsgDto> listDto = new ArrayList<BargainAwardMsgDto>();
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				listDto.add(BargainAwardMsgAssembler.assemble(list.get(i)));
			}
		}		
		PageInfo<BargainAwardMsgDto> pageInfo = new PageInfo<>(listDto);
		return new PageResultDto<BargainAwardMsgDto>(pageInfo.getList(),pageInfo.getTotal());
	}

	@Override
	public List<BargainAwardMsgWebDto> getMsgRandomList(String actId) {
		return bargainAwardMsgDao.selectMsgRandomList(actId);
	}

	


}
