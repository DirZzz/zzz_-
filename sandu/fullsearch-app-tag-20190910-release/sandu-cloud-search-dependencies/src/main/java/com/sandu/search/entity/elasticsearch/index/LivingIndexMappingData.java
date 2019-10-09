package com.sandu.search.entity.elasticsearch.index;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LivingIndexMappingData implements Serializable {

    //小区id
    private Integer livingId;

    //小区名
    private String livingName;

    //小区编码
    private String areaCode;

    //小区下的户型数量
    private Integer totalHouse;

    //户型创建时间
    private Date gmtCreate;

    //市编码
    private String cityCode;
}
