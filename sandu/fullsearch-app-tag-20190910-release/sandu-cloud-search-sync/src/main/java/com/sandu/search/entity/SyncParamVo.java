package com.sandu.search.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 远程调用参数vo
 * @auth xiaoxc
 * @data 2019/03/27
 */
@Data
public class SyncParamVo implements Serializable {

    private static final long serialVersionUID = -1L;

    //IDS
    private String ids;
    //业务类型
    private String businessType;
    //操作类型(1:add, 2:delete, 3:update)
    private int action;
    //模块
    private int module;
}
