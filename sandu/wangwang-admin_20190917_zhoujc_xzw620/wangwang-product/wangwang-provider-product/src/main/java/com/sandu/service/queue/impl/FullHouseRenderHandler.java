package com.sandu.service.queue.impl;

import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.sandu.api.user.model.SysUser;
import com.sandu.api.user.service.SysUserService;
import com.sandu.common.util.JsonObjectUtil;
import com.sandu.commons.ResponseEnvelope;
import com.sandu.util.SocketIOUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sandu
 * @ClassName FullHouseRenderHandler
 * @date 2019/9/18-19:42$
 */
@Component()
@EnableCaching(proxyTargetClass = true)
@Slf4j
public class FullHouseRenderHandler implements DeliverCallback {
	private final Gson gson = GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays().create();


	@Autowired
	private SysUserService userService;

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${app.notiry.url.sendFullRenderUrl}")
	private String sendFullRenderUrl;


	/**
	 * Called when a <code><b>basic.deliver</b></code> is received for this consumer.
	 *
	 * @param consumerTag the <i>consumer tag</i> associated with the consumer
	 * @param message     the delivered message
	 * @throws IOException if the consumer encounters an I/O error while processing the message
	 */
	@Override
	public void handle(String consumerTag, Delivery message) throws IOException {
		String msg = new String(message.getBody(), StandardCharsets.UTF_8);
		UserFullPlanRendDTO it = gson.fromJson(msg, UserFullPlanRendDTO.class);
		log.info("consumer msg:{}", it);
		//wx template notify by http
		String url = sendFullRenderUrl + "?userId=" + it.getUserId() + "&planId=" + it.getPlanId();
		try {
			ResponseEnvelope response = restTemplate.getForObject(url, ResponseEnvelope.class);
			if (!response.isSuccess()) {
				log.error("发送消息模版失败，url : {}, response :{}", url, JsonObjectUtil.bean2Json(response));
			}
			log.info("发送消息模版成功，url : {}, response :{}", url, JsonObjectUtil.bean2Json(response));
		} catch (Exception e) {
			log.error("send error : {}", e.getMessage());
		}

		//webSocket notify
		try {
			SysUser user = userService.get(it.getUserId());
			if (user == null) {
				log.error("get user null, userId:{}", it.getUserId());
			} else {
				Map<String, String> data = new HashMap<>();
				data.put("msgOrigin", "fullHouseRenderNotify");
				data.put("planId", it.getPlanId() + "");
				SocketIOUtil.handlerSendMessage(
						SocketIOUtil.IM_PUSH_MSG_EVENT,
						SocketIOUtil.IM_PUSH_SYSTEM_MSG_CODE + ":" + "fullHouseRenderNotify",
						user.getUuid(),
						JsonObjectUtil.bean2Json(data));
			}
		} catch (Exception e) {
			log.error("socket notify  error : {}", e.getMessage());
		}

	}


	@Data
	private class UserFullPlanRendDTO {
		private Integer planId;
		private Integer userId;
	}

}