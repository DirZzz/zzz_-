package com.sandu.cloud.activity.bargain.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.activity.bargain.dao.BargainInviteRecordActDayAggregatedDao;
import com.sandu.cloud.activity.bargain.model.BargainInviteRecordActDayAggregated;
import com.sandu.cloud.activity.bargain.service.BargainInviteRecordActDayAggregatedService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BargainInviteRecordActDayAggregatedServiceImpl implements BargainInviteRecordActDayAggregatedService {

    @Autowired
    private BargainInviteRecordActDayAggregatedDao inviteRecordActDayAggregatedDao;

	@Override
	public void create(BargainInviteRecordActDayAggregated entity) {
		inviteRecordActDayAggregatedDao.insert(entity);
	}

	@Override
	public int modifyById(BargainInviteRecordActDayAggregated entity) {
		return inviteRecordActDayAggregatedDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public BargainInviteRecordActDayAggregated get(String id) {
		return inviteRecordActDayAggregatedDao.selectByPrimaryKey(id);
	}
	
	@Override
	public BargainInviteRecordActDayAggregated get(String actId, String openId,Date date) {
		BargainInviteRecordActDayAggregated query = new BargainInviteRecordActDayAggregated();
		query.setActId(actId);
		query.setOpenId(openId);
		query.setInviteDate(date);
		return inviteRecordActDayAggregatedDao.selectOne(query);
	}

	@Override
	public int getCurrentDateInviteCutCount(String actId, String openId) {
		BargainInviteRecordActDayAggregated entity = this.get(actId, openId,new Date());
		if(entity!=null) {
			return entity.getInviteCutCount();
		}
		return 0;
	}

	@Override
	public boolean isCutCurrentDay(String actId, String openId) {
		BargainInviteRecordActDayAggregated entity = this.get(actId, openId,new Date());
		if(entity!=null) {
			return true;
		}
		return false;
	}

	@Override
	public void increaseCurrentDayInviteCutCount(String actId, String openId) {
		BargainInviteRecordActDayAggregated entity = this.get(actId, openId,new Date());
		BargainInviteRecordActDayAggregated updEntity = new BargainInviteRecordActDayAggregated();
		updEntity.setInviteCutCount(entity.getInviteCutCount()+1);
		updEntity.setId(entity.getId());
		this.modifyById(updEntity);
	}
}
