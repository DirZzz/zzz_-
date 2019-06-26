package com.sandu.analysis.biz.page.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * CopyRight (c) 2017 Sandu Technology Inc.<p>
 * 
 * 按钮点击事件统计
 * 
 * @author huangsongbo
 * 2019-05-06 16:03:13.906
 */

@Data
public class ButtonClickResultDO implements Serializable {

	private static final long serialVersionUID = -8581772102023720513L;

	/**
     * bigdata_button_click_result.id
     */
    private Long id;

    /**
     * 当前页面属性(路径)<p>
     * bigdata_button_click_result.current_page_property
     */
    private String currentPageProperty;

    /**
     * 按钮标识<p>
     * bigdata_button_click_result.button_property
     */
    private String buttonProperty;

    /**
     * 按钮点击次数<p>
     * bigdata_button_click_result.pv
     */
    private Integer pv;

    /**
     * 按钮点击人数<p>
     * bigdata_button_click_result.uv
     */
    private Integer uv;

    /**
     * 开始时间<p>
     * bigdata_button_click_result.start_time
     */
    private Date startTime;

    /**
     * 结束时间<p>
     * bigdata_button_click_result.end_time
     */
    private Date endTime;

    /**
     * 应用app_id<p>
     * bigdata_button_click_result.app_id
     */
    private String appId;

    /**
     * 渠道<p>
     * bigdata_button_click_result.channel
     */
    private String channel;

    /**
     * 创建者<p>
     * bigdata_button_click_result.creator
     */
    private String creator;

    /**
     * 创建时间<p>
     * bigdata_button_click_result.gmt_create
     */
    private Date gmtCreate;

    /**
     * 更新者<p>
     * bigdata_button_click_result.modifier
     */
    private String modifier;

    /**
     * 修改时间<p>
     * bigdata_button_click_result.gmt_modified
     */
    private Date gmtModified;

    /**
     * 逻辑删除字段, 0=正常, 1=已删除<p>
     * bigdata_button_click_result.is_deleted
     */
    private Integer isDeleted;

    /**
     * 备注<p>
     * bigdata_button_click_result.remark
     */
    private String remark;

}