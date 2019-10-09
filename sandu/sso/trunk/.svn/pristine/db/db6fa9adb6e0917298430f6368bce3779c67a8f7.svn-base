package com.sandu.api.user.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sandu.api.user.model.LoginUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Sandu
 * @ClassName AccountFreezeManage
 * @date 2019/6/19-14:23$
 */

@Data
public class AccountFreezeManage implements Serializable {
	public final static String SERVICE_ACTION_LOG_MSG_ENABLE = "设置用户账号(Id:%s)启用日期为：%s";
	public final static String SERVICE_ACTION_LOG_MSG_FREEZE = "冻结用户账号(Id:%s),停用日期为：%s";

	@NotEmpty
	private List<Integer> userIds;

	public void setDate(Date date) {
		if (date != null) {
			date.setHours(0);
		}
		this.date = date;
	}

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;

	@NotNull
	@Pattern(regexp = "(freeze|enable)")
	private String state;

	@ApiModelProperty(hidden = true)
	private LoginUser loginUser;

}


