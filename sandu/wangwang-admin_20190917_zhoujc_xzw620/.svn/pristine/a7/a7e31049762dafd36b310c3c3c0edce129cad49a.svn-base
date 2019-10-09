package com.sandu.api.solution.service;

import com.github.pagehelper.PageInfo;
import com.sandu.api.solution.input.UserAppointmentQuery;
import com.sandu.api.solution.model.MiniProUserAppointmentDetail;

import java.util.List;

public interface MiniProUserAppointmentDetailService {
    PageInfo<MiniProUserAppointmentDetail> getPageList(UserAppointmentQuery query);

    MiniProUserAppointmentDetail get(Long id);

    int updateRemakeAndStatusById(String remake, Integer status, Long id);

    List<MiniProUserAppointmentDetail> findList(UserAppointmentQuery query);

    int remove(Long id);

    int countByObtainId(Long id);
}
