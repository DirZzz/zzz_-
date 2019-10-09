package com.sandu.service.solution.dao;

import com.sandu.api.solution.input.UserAppointmentQuery;
import com.sandu.api.solution.model.MiniProUserAppointmentDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiniProUserAppointmentDetailMapper {
    List<MiniProUserAppointmentDetail> findList(UserAppointmentQuery query);

    MiniProUserAppointmentDetail findById(Long id);

    int updateRemakeAndStatusById(@Param("remake") String remake, @Param("status") Integer status, @Param("id")Long id);

    int delete(Long id);

    int countByObtainId(Long obtainId);
}
