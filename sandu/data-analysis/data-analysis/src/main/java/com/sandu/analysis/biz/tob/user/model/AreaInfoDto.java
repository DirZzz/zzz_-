package com.sandu.analysis.biz.tob.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaInfoDto {

    /**
     *  地区编码
     */
    private String areaCode;
    /**
     *  地区名称
     */
    private String areaName;

}
