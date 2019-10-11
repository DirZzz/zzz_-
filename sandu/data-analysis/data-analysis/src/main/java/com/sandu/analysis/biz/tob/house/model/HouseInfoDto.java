package com.sandu.analysis.biz.tob.house.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 户型区域
 * @author xiaoxc
 * @data 2019/6/14 0014.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseInfoDto implements Serializable {

    private static final long serialVersionUID = 1263007065460601774L;
    /**
     * 户型Id
     */
    private Integer houseId;
    /**
     * 省编码
     */
    private String provinceCode;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 户型编码
     */
    private String houseCode;
    /**
     * 户型名称
     */
    private String houseName;
    /**
     * 小区名称
     */
    private String livingName;
    /**
     * 新增户型数量
     */
    private Integer newHouseCount;
    /**
     * 每小时(eg:2019-06-05 01、2019-06-05 02)
     */
    private String perHour;
    /**
     * 新增户型创建时间
     */
    private String createTime;


}
