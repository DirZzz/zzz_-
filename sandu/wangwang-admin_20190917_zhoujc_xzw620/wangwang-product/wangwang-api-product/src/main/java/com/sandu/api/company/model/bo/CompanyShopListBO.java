package com.sandu.api.company.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CompanyShopListBO implements Serializable {

	private Integer id;

	private Integer companyId;

	private String companyName;

	private String cityCode;
	private String cityName;


	private List<Integer> goodStyle;
	private List<String> goodStyleName;

	/**
	 * 花费
	 */
	private String costRange;

	/**
	 * 成交量
	 */
	private Integer dealNumber;

	/**
	 * 好评率
	 */
	private String praiseRates;

	/**
	 * 余额
	 */
	private Integer overage;

	private String categoryIds;

	private Double designFeeStarting;

	private Double designFeeEnding;

	private Integer companyPid;
	private Integer userId;
	private String shopCode;
	private String shopName;
	private Integer businessType;
	private String firstCategoryIds;
	private String provinceCode;
	private String areaCode;
	private String streetCode;
	private String longAreaCode;
	private String shopAddress;
	private String contactPhone;
	private String contactName;
	private Integer logoPicId;
	private String coverResIds;
	private Integer coverResType;
	private String releasePlatformValues;
	private Integer displayStatus;
	private Integer visitCount;
	private Double praiseRate;
	private String shopIntroduced;
	private String sysCode;
	private String creator;
	private Date gmtCreate;
	private String modifier;
	private Date gmtModified;
	private Integer isDeleted;
	private String remark;
	private Long introducedFileId;
	private String introducedPicIds;
	private Double longitude;
	private Double latitude;
	private Double decorationPriceStart;
	private Double decorationPriceEnd;
	private Integer decorationType;
	private Integer workingYears;
	private Integer isBlacklist;
	private Integer shopType;
	private String creatorAccount;
	private String creatorPhone;
	private String serviceArea;
	private String businessShopDecorationType;

	private Double automateScore;
}
