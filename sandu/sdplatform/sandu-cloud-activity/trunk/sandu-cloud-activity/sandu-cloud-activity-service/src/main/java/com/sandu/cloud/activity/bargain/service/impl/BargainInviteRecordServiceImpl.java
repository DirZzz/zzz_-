package com.sandu.cloud.activity.bargain.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.cloud.activity.bargain.dao.BargainInviteRecordDao;
import com.sandu.cloud.activity.bargain.model.BargainInviteRecord;
import com.sandu.cloud.activity.bargain.service.BargainInviteRecordService;
import com.sandu.cloud.common.vo.PageResultDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BargainInviteRecordServiceImpl implements BargainInviteRecordService {

    @Autowired
    private BargainInviteRecordDao inviterecordDao;

	@Override
	public void create(BargainInviteRecord inviteRecord) {
		inviterecordDao.insert(inviteRecord);
	}

	@Override
	public int modifyById(BargainInviteRecord inviteRecord) {
		return inviterecordDao.updateByPrimaryKeySelective(inviteRecord);
	}

	@Override
	public int remove(String inviteRecordId) {
		BargainInviteRecord inviteRecord = new BargainInviteRecord();
		inviteRecord.setIsDeleted(0);
		inviteRecord.setId(inviteRecordId);
		return inviterecordDao.updateByPrimaryKeySelective(inviteRecord);
	}

	@Override
	public BargainInviteRecord get(String inviteRecordId) {
		return inviterecordDao.selectByPrimaryKey(inviteRecordId);
	}

	@Override
	public List<BargainInviteRecord> list(BargainInviteRecord queryEntity) {
		return inviterecordDao.select(queryEntity);
	}

	@Override
	public BargainInviteRecord get(String registrationId, String openId) {
		BargainInviteRecord queryEntity = new BargainInviteRecord();
		queryEntity.setIsDeleted(0);
		queryEntity.setOpenId(openId);
		queryEntity.setRegistrationId(registrationId);
		List<BargainInviteRecord> list = this.list(queryEntity);
		if(list==null || list.size()==0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public boolean isCut(String registrationId, String openId) {
		if(this.get(registrationId,openId)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public PageResultDto<BargainInviteRecord> pageList(String regId, Integer pageNum,
			Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		BargainInviteRecord queryEntity = new BargainInviteRecord();
		queryEntity.setIsDeleted(0);
		queryEntity.setRegistrationId(regId);
		List<BargainInviteRecord> list = this.list(queryEntity);
		PageInfo<BargainInviteRecord> pageInfo = new PageInfo<BargainInviteRecord>(list);
		return new PageResultDto<BargainInviteRecord>(pageInfo.getList(),pageInfo.getTotal());
	}
	
	
	@Override
	public String getCutFriends(String regId) {
		if(StringUtils.isBlank(regId)) {
			return null;
		}
		BargainInviteRecord queryEntity = new BargainInviteRecord();
		queryEntity.setIsDeleted(0);
		queryEntity.setRegistrationId(regId);
		List<BargainInviteRecord> list = this.list(queryEntity);
		String cutFriendsStr = "";
		if(list!=null && list.size()>0) {
			int i=1;
			for(BargainInviteRecord inviteRecord:list) {
				if(StringUtils.isNotBlank(inviteRecord.getNickname())) {
					cutFriendsStr += inviteRecord.getNickname()+",";
					i++;
				}
				if(i>=15) {
					break;
				}
			}
		}
		if(cutFriendsStr.length()>0) {
			cutFriendsStr = cutFriendsStr.substring(0, cutFriendsStr.length()-1);
		}
		return cutFriendsStr;
	}
}
