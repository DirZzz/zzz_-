package com.sandu.search.entity.elasticsearch.po.house;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 户型小区持久化对象
 *
 * @date 20180106
 * @auth pengxuangang
 */
@Data
public class HouseLivingPo implements Serializable {

    private static final long serialVersionUID = 7051398912924517917L;

    //小区ID
    private int livingId;
    //小区类型
    private String livingType;
    //小区编码
    private String livingCode;
    //小区名
    private String livingName;
    //小区地址
    private String livingAddress;
    //小区描述
    private String livingDesc;
    //小区面积ID
    private String livingAreaId;
    //小区建筑面积
    private String livingBuildArea;
    //小区覆盖面积
    private String livingCoverArea;
    //小区面积长编码
    private String livingAreaLongCode;
    //小区所在区编码
    private String areaCode;
    //小区下的户型数量
    private Integer totalHouse;
    //户型创建时间
    private Date gmtCreate;
    //市编码
    private String cityCode;
}
