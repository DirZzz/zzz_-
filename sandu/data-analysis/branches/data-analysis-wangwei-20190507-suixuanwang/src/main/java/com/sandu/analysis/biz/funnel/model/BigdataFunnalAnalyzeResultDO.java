package com.sandu.analysis.biz.funnel.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * CopyRight (c) 2017 Sandu Technology Inc.<p>
 * 
 * 漏斗统计数据结果表
 * 
 * @author huangsongbo
 * 2019-04-19 11:46:09.629
 */

@Data
public class BigdataFunnalAnalyzeResultDO implements Serializable {

	private static final long serialVersionUID = 9018075963493987499L;

	/**
     * bigdata_funnal_analyze_result.id
     */
    private Long id;

    /**
     * 开始时间<p>
     * bigdata_funnal_analyze_result.start_time
     */
    private Date startTime;

    /**
     * 结束时间<p>
     * bigdata_funnal_analyze_result.end_time
     */
    private Date endTime;

    /**
     * 自定义漏斗id, 关联bigdata_funnal.id<p>
     * bigdata_funnal_analyze_result.funnel_id
     */
    private Long funnelId;

    /**
     * 漏斗事件节点id, 关联bigdata_funnal_detail.id<p>
     * bigdata_funnal_analyze_result.node_id
     */
    private Long nodeId;

    /**
     * 漏斗节点名称<p>
     * bigdata_funnal_analyze_result.node_name
     */
    private String nodeName;

    /**
     * 漏斗节点uv<p>
     * bigdata_funnal_analyze_result.node_uv
     */
    private Integer nodeUv;

    /**
     * 非正常pv, 这里统计的是一个流程的uv<p>
     * bigdata_funnal_analyze_result.node_pv
     */
    private Integer nodePv;

    /**
     * 节点顺序, eg: 1>2>3...<p>
     * bigdata_funnal_analyze_result.node_seq
     */
    private Integer nodeSeq;

    /**
     * 创建者<p>
     * bigdata_funnal_analyze_result.creator
     */
    private String creator;

    /**
     * 创建时间<p>
     * bigdata_funnal_analyze_result.gmt_create
     */
    private Date gmtCreate;

    /**
     * 更新者<p>
     * bigdata_funnal_analyze_result.modifier
     */
    private String modifier;

    /**
     * 修改时间, 自动更新<p>
     * bigdata_funnal_analyze_result.gmt_modified
     */
    private Date gmtModified;

    /**
     * 逻辑删除字段, 0=正常, 1=已删除<p>
     * bigdata_funnal_analyze_result.is_deleted
     */
    private Integer isDeleted;

    /**
     * 备注<p>
     * bigdata_funnal_analyze_result.remark
     */
    private String remark;

}