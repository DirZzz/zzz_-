package com.sandu.cloud.activity.bargain.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandu.cloud.activity.bargain.dao.BargainDao;
import com.sandu.cloud.activity.bargain.exception.BargainBizException;
import com.sandu.cloud.activity.bargain.exception.BargainBizExceptionCode;
import com.sandu.cloud.activity.bargain.model.Bargain;
import com.sandu.cloud.activity.bargain.service.BargainService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BargainServiceImpl implements BargainService {

    @Autowired
    private BargainDao bargainDao;

    @Override
    public void create(Bargain bargain) {
        bargainDao.insert(bargain);
    }

    @Override
    public int modifyById(Bargain bargain) {
        return bargainDao.updateByPrimaryKeySelective(bargain);
    }

    @Override
    public int remove(String actId) {
    	Bargain bargain = new Bargain();
    	bargain.setId(actId);
    	bargain.setIsDeleted(1);
        return bargainDao.updateByPrimaryKeySelective(bargain);
    }

    @Override
    public Bargain get(String actId) {
        return bargainDao.selectByPrimaryKey(actId);
    }
    
    @Override
	public Bargain get(String actId, String appId) {
    	Bargain bargain = new Bargain();
    	bargain.setId(actId);
    	bargain.setAppId(appId);
    	bargain.setIsDeleted(0);
		return bargainDao.selectOne(bargain);
	}

	@Override
	public Integer getBargainStatus(Bargain actEntity) {
		Date now = new Date();
		if(actEntity!=null) {

			//已结束(过时,没库存,结束活动)
			if(actEntity.getProductRemainCount()==0
					||actEntity.getEndTime().compareTo(now)<=0
					|| actEntity.getIsEnable()==0) {
				return Bargain.STATUS_ENDED;
			}
			//未开始
			if(now.compareTo(actEntity.getBegainTime())<=0) {
				return Bargain.STATUS_UNBEGIN;
			}
			//进行中
			return Bargain.STATUS_ONGOING;
		}
		
		throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_NOT_EXIST);
	}

	@Override
	public Integer getBargainStatus(String actId,String appId) {
		Bargain actEntity = this.get(actId,appId);
		if(actEntity==null) {
			throw new BargainBizException(BargainBizExceptionCode.SD_ERR_BARGAIN_NOT_EXIST);
		}
		return this.getBargainStatus(actEntity);
	}

	@Override
	public boolean reduceProductInventory(String actId) {
		int flag = bargainDao.updateToReduceProductRemainCount(actId);
		if(flag>0) {
			return true;
		}
		return false;
	}

	@Override
	public void increaseParticipants(String actId) {
		bargainDao.updateToIncreaseRegistrationCount(actId);
	}


	@Override
	public List<Bargain> listAllEffectiveBargain(Date currentTime) {
		return bargainDao.selectAllEffectiveBargainAward(currentTime);
	}
	
	@Override
	public List<Bargain> listWillExpireList() {
		return bargainDao.selectWillExpireList();
	}

	@Override
	public void modifyVitualCount() {
		bargainDao.updateVitualCount();
	}


	@Override
	public List<Bargain> pageList(List<String> appids, Integer page, Integer limit) {
		return bargainDao.selectByAppids(appids,page,limit);
	}

    @Override
    public int countByAppids(List<String> appids) {
        return bargainDao.countByAppids(appids);
    }

    @Override
    public Bargain getByActName(String actName) {
    	Bargain bargain = new Bargain();
    	bargain.setIsDeleted(0);
    	bargain.setActName(actName);
        return bargainDao.selectOne(bargain);
    }

}
