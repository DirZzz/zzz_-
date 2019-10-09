package com.sandu.api.basesupplydemand.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InteractiveZoneTopic implements Serializable {
    private Long id;

    private String title;

    private String content;

    private String picIds;

    private Integer blockType;

    private Integer coverPicId;

    private Integer shareType;

    private Integer livingRoomNum;

    private Integer bedroomNum;

    private Float houseArea;

    private Float houseCost;

    private String houseStyle;

    private Integer planId;
    
    private Integer planType;

    private Integer houseId;

    private Integer planCoverId;

    private Integer houseCoverId;

    private Integer articleSource;

    private Integer status;

    private Integer articleId;

    private Integer projectCaseId;

    private Integer shopId;

    private Integer isTop;

    private Integer ordering;

    private Integer publishUserId;

    private Date publishTime;

    private Integer dataSource;

    private String author;

    private Integer createUserId;

    private String creator;

    private Date gmtCreate;

    private String modifier;

    private Date gmtModified;

    private Integer isDeleted;

    private String jsonContent;

    private Integer isOld;

    private Date lastReviewTime;

}