package com.sandu.service.solution.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.solution.input.UserAppointmentQuery;
import com.sandu.api.solution.model.MiniProUserAppointmentDetail;
import com.sandu.api.solution.service.MiniProObtainMobileDetailService;
import com.sandu.api.solution.service.MiniProUserAppointmentDetailService;
import com.sandu.service.solution.dao.MiniProUserAppointmentDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service("miniProUserAppointmentDetailService")
public class MiniProUserAppointmentDetailServiceImpl implements MiniProUserAppointmentDetailService {

    @Autowired
    private MiniProUserAppointmentDetailMapper miniProUserAppointmentDetailMapper;

    @Autowired
    private MiniProObtainMobileDetailService miniProObtainMobileDetailService;

    @Override
    public PageInfo<MiniProUserAppointmentDetail> getPageList(UserAppointmentQuery query) {

        List<Long> ids = this.fecthObtainIds(query.getCompanyId());
        if (CollectionUtils.isEmpty(ids)){
            return new PageInfo<>(Collections.EMPTY_LIST);
        }
        query.setObtainIds(ids);
        PageHelper.startPage(query.getPage() == null ? 0 : query.getPage(),query.getLimit() == null ? 10 : query.getLimit());
        List<MiniProUserAppointmentDetail> list = miniProUserAppointmentDetailMapper.findList(query);
        return new PageInfo<>(list);
    }

    private List<Long> fecthObtainIds(Integer companyId) {
        List<Long> ids =  miniProObtainMobileDetailService.findObtainDetailIdsByCompanyId(companyId);
        return ids;
    }

    @Override
    public MiniProUserAppointmentDetail get(Long id) {
        return miniProUserAppointmentDetailMapper.findById(id);
    }

    @Override
    public int updateRemakeAndStatusById(String remake, Integer status, Long id) {
        return miniProUserAppointmentDetailMapper.updateRemakeAndStatusById(remake, status, id);
    }

    @Override
    public List<MiniProUserAppointmentDetail> findList(UserAppointmentQuery query) {
        List<Long> ids = this.fecthObtainIds(query.getCompanyId());
        if (CollectionUtils.isEmpty(ids)){
            return Collections.EMPTY_LIST;
        }
        query.setObtainIds(ids);
        return miniProUserAppointmentDetailMapper.findList(query);
    }



    @Override
    public int remove(Long id) {
        return miniProUserAppointmentDetailMapper.delete(id);
    }

    @Override
    public int countByObtainId(Long obtainId) {
        return miniProUserAppointmentDetailMapper.countByObtainId(obtainId);
    }
}
