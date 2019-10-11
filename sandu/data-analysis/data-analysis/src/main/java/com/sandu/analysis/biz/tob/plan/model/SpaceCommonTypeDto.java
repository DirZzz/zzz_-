package com.sandu.analysis.biz.tob.plan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Zhangwenjian
 * Date: 2019-06-14 18:50
 * Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpaceCommonTypeDto  implements Serializable {

    private static final long serialVersionUID = -158706242393271122L;

    /** 空间ID **/
    private Integer spaceCommonId;
    /** 空间类型 **/
    private Integer spaceCommonType;

}
