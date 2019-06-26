package com.sandu.analysis.biz.page.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * CopyRight (c) 2017 Sandu Technology Inc.<p>
 * 
 * 页面访问事件统计
 * 
 * @author huangsongbo
 * 2019-05-06 16:03:13.891
 */

@Data
public class PageViewResultDO implements Serializable {

	private static final long serialVersionUID = -1341739796287650459L;

	/**
     * bigdata_page_view_result.id
     */
    private Long id;

    /**
     * 页面属性(路径)<p>
     * bigdata_page_view_result.page_property
     */
    private String pageProperty;

    /**
     * 页面访问次数<p>
     * bigdata_page_view_result.pv
     */
    private Integer pv;

    /**
     * 页面访问人数<p>
     * bigdata_page_view_result.uv
     */
    private Integer uv;

    /**
     * 页面平均停留时长(单位s)<p>
     * bigdata_page_view_result.time_on_page
     */
    private Integer timeOnPage;

    /**
     * 开始时间<p>
     * bigdata_page_view_result.start_time
     */
    private Date startTime;

    /**
     * 结束时间<p>
     * bigdata_page_view_result.end_time
     */
    private Date endTime;

    /**
     * 应用app_id<p>
     * bigdata_page_view_result.app_id
     */
    private String appId;

    /**
     * 渠道<p>
     * bigdata_page_view_result.channel
     */
    private String channel;

    /**
     * 创建者<p>
     * bigdata_page_view_result.creator
     */
    private String creator;

    /**
     * 创建时间<p>
     * bigdata_page_view_result.gmt_create
     */
    private Date gmtCreate;

    /**
     * 更新者<p>
     * bigdata_page_view_result.modifier
     */
    private String modifier;

    /**
     * 修改时间<p>
     * bigdata_page_view_result.gmt_modified
     */
    private Date gmtModified;

    /**
     * 逻辑删除字段, 0=正常, 1=已删除<p>
     * bigdata_page_view_result.is_deleted
     */
    private Integer isDeleted;

    /**
     * 备注<p>
     * bigdata_page_view_result.remark
     */
    private String remark;

}