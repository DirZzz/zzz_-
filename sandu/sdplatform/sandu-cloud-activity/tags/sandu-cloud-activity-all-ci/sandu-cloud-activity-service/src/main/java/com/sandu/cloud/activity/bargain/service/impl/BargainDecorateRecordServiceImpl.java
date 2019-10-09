package com.sandu.cloud.activity.bargain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.activity.bargain.dao.BargainDecorateRecordDao;
import com.sandu.cloud.activity.bargain.model.BargainDecorateRecord;
import com.sandu.cloud.activity.bargain.service.BargainDecorateRecordService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BargainDecorateRecordServiceImpl implements BargainDecorateRecordService {

    @Autowired
    private BargainDecorateRecordDao bargainDecorateRecordDao;

	@Override
	public void create(BargainDecorateRecord decorateRecord) {
		bargainDecorateRecordDao.insert(decorateRecord);
	}

	@Override
	public int modifyById(BargainDecorateRecord decorateRecord) {
		return bargainDecorateRecordDao.updateByPrimaryKeySelective(decorateRecord);
	}

	@Override
	public int remove(String decorateRecordId) {
		BargainDecorateRecord decorateRecord = new BargainDecorateRecord();
		decorateRecord.setIsDeleted(1);
		decorateRecord.setId(decorateRecordId);
		return bargainDecorateRecordDao.updateByPrimaryKeySelective(decorateRecord);
	}

	@Override
	public BargainDecorateRecord get(String decorateRecordId) {
		return bargainDecorateRecordDao.selectByPrimaryKey(decorateRecordId);
	}

	@Override
	public BargainDecorateRecord getByRegId(String regId) {
		BargainDecorateRecord decorateRecord = new BargainDecorateRecord();
		decorateRecord.setIsDeleted(0);
		decorateRecord.setRegistrationId(regId);
		return bargainDecorateRecordDao.selectOne(decorateRecord);
	}
}
