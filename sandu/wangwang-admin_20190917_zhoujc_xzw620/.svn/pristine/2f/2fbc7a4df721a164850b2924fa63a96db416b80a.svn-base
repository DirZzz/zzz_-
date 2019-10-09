package com.sandu.service.product.impl;

import com.sandu.api.product.model.BaseProductCountertops;
import com.sandu.api.product.service.BaseProductCountertopsService;
import com.sandu.service.product.dao.BaseProductCountertopsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * <p>
 * pc-admin-draw
 *
 * @author songjianming@sanduspace.cn
 * <p>
 * 2018年6月12日
 */

@Service
public class BaseProductCountertopsServiceImpl implements BaseProductCountertopsService {

	@Autowired
	BaseProductCountertopsMapper baseProductCountertopsMapper;

	@Override
	public List<BaseProductCountertops> listProCtopsByProductId(Long productId) {
		if (productId == null) {
			return Collections.emptyList();
		}

		return baseProductCountertopsMapper.listProCtopsByProductId(productId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addProCtops(List<BaseProductCountertops> proCtops) {
		if (proCtops == null || proCtops.isEmpty()) {
			return -1;
		}

		int updateCount = 0;
		for (BaseProductCountertops proCtop : proCtops) {
			updateCount += baseProductCountertopsMapper.insertSelective(proCtop);
		}

		return updateCount;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateProCtops(List<BaseProductCountertops> proCtops) {
		if (proCtops == null || proCtops.isEmpty()) {
			return -1;
		}

		int updateCount = 0;
		for (BaseProductCountertops proCtop : proCtops) {
			updateCount += baseProductCountertopsMapper.updateByPrimaryKeySelective(proCtop);
		}

		return updateCount;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteProCtops(List<Integer> proCtops) {
		if (proCtops == null || proCtops.isEmpty()) {
			return -1;
		}

		// remove null object
		proCtops.removeIf(Objects::isNull);
		return baseProductCountertopsMapper.deleteProCtops(proCtops);
	}
}
