package com.sandu.api.statistics.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: chenqiang
 * @create: 2019-06-03 11:19
 */
@Data
public class PicDto implements Serializable {
    private static final long serialVersionUID = 940794843278779471L;

    private Integer id;

    private String smallPicInfo;

    private String picIds;

    private Integer coverPicId;

    private Integer houseCoverId;
}
