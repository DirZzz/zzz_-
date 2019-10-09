package com.sandu.api.solution.output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sandu.api.solution.constant.SysDictionaryConstants;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FullHouseDesignPlanCoverPicInfoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * <p>图片id
	 * <p>res_render_pic.id/design_plan_recommended.cover_pic_id
	 */
	private Long picId;
	
	/**
	 * <p>图片路径
	 * <p>eg: /AA/c_basedesign_recommended/2019/07/26/14/design/designPlanRecommended/render/small/297668_1564123450206.jpg
	 */
	private String picPath;
	
	/**
	 * eg: 客餐厅照片集效果图
	 */
	private String picTypeInfo;
	
	/**
	 * 0 = 未选中, 1 = 已选中
	 */
	private Integer checkStatus;
	
	public FullHouseDesignPlanCoverPicInfoDTO(FullHouseDesignPlanCoverPicInfoDO dobj, List<Long> checkedPicIdList) {
		if (dobj == null) {
			return;
		}
		if (checkedPicIdList == null) {
			checkedPicIdList = new ArrayList<Long>();
		}
		this.picId = dobj.getPicId();
		this.picPath = dobj.getPicPath();
		this.picTypeInfo = SysDictionaryConstants.SPACE_TYPE_MAP.get(dobj.getSpaceTypeValue()) + "照片级效果图";
		
		// ==========选中状态处理 ->start
		if (checkedPicIdList.contains(dobj.getPicId())) {
			this.checkStatus = 1;
		} else {
			this.checkStatus = 0;
		}
		// ==========选中状态处理 ->end
		
	}
	
}
