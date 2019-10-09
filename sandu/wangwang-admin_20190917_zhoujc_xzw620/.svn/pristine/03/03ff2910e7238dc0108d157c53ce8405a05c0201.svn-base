package com.sandu.api.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOperationRecord implements Serializable {

    private Integer id;

    private Integer productId;

    private String originalPrice;

    private String curPrice;

    private Date operationTime;

    private Integer operationUserId;

    private Integer isDeleted;

    private String remark;
}
