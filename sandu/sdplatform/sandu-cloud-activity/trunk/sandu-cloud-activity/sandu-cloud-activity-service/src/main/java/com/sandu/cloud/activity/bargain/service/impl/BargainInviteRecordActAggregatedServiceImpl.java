package com.sandu.cloud.activity.bargain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.activity.bargain.dao.BargainInviteRecordActAggregatedDao;
import com.sandu.cloud.activity.bargain.model.BargainInviteRecordActAggregated;
import com.sandu.cloud.activity.bargain.service.BargainInviteRecordActAggregatedService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BargainInviteRecordActAggregatedServiceImpl implements BargainInviteRecordActAggregatedService {

    @Autowired
    private BargainInviteRecordActAggregatedDao inviteRecordActAggregatedDao;

	@Override
	public void create(BargainInviteRecordActAggregated entity) {
		inviteRecordActAggregatedDao.insert(entity);
	}

	@Override
	public int modifyById(BargainInviteRecordActAggregated entity) {
		return inviteRecordActAggregatedDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public BargainInviteRecordActAggregated get(String id) {
		return inviteRecordActAggregatedDao.selectByPrimaryKey(id);
	}

	@Override
	public boolean isCut(String actId, String openId) {
		BargainInviteRecordActAggregated entity = this.get(actId, openId);
		if(entity!=null) {
			return true;
		}
		return false;
	}

	@Override
	public int getInviteCutCount(String actId, String openId) {
		BargainInviteRecordActAggregated entity = this.get(actId, openId);
		if(entity!=null) {
			return entity.getInviteCutCount();
		}
		return 0;
	}
	
	@Override
	public BargainInviteRecordActAggregated get(String actId, String openId) {
		BargainInviteRecordActAggregated inviteRecordActAggregated = new BargainInviteRecordActAggregated();
		inviteRecordActAggregated.setActId(actId);
		inviteRecordActAggregated.setOpenId(openId);
		return inviteRecordActAggregatedDao.selectOne(inviteRecordActAggregated);
	}

	@Override
	public void increaseInviteCutCount(String actId, String openId) {
		BargainInviteRecordActAggregated entity = this.get(actId, openId);
		BargainInviteRecordActAggregated updEntity = new BargainInviteRecordActAggregated();
		updEntity.setInviteCutCount(entity.getInviteCutCount()+1);
		updEntity.setId(entity.getId());
		this.modifyById(updEntity);
	}

	
	

	
}
