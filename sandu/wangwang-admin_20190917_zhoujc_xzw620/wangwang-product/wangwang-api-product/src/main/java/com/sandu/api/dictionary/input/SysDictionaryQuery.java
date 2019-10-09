package com.sandu.api.dictionary.input;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SysDictionaryQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * where type = #{type}
	 */
	private String type;
	
	/**
	 * where att1 = #{att1}
	 */
	private String att1;
	
	/**
	 * where is_deleted = #{isDeleted}
	 */
	private Integer isDeleted;
	
}
