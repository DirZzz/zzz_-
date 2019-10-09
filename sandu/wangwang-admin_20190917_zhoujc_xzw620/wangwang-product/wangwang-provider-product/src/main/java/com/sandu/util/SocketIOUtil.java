package com.sandu.util;


import com.sandu.api.impush.model.PushMessageInfo;
import com.sandu.common.util.JsonObjectUtil;
import com.sandu.commons.gson.GsonUtil;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SocketIOUtil {

    private static Logger logger = LoggerFactory.getLogger(SocketIOUtil.class);

    /**
     * 支付回调通知事件
     */
    public static final String IM_PUSH_MSG_EVENT = "im_push_msg_event";

    /**
     * 消息推送类型
     */
    public static final String IM_PUSH_MSG_CODE = "PUSH_RENDER_MSG";
    
    /**
     * 消息推送类型(系统消息)
     */
    public static final String IM_PUSH_SYSTEM_MSG_CODE = "PUSH_SYSTEM_MSG";


    private static Socket socket;

    @Autowired
    public void socket(Socket socketBean){
        socket = socketBean;
    }

    /**
     * 向事件推送消息
     * @param event
     * @param pushMessageInfo
     */
    public static void sendEventMessage(String event, PushMessageInfo pushMessageInfo) {
        try {
            logger.info("[[[{}]]]send message start =>{} , pushMessageInfo =>{}", socket, event, pushMessageInfo);
            //获取socket对象
            String pushMessage = GsonUtil.toJson(pushMessageInfo);
            socket.emit(event, pushMessage);
            logger.info("send message end");
        } catch (Exception e) {
            logger.error("发送消息异常", e);
        }
    }

    /**
     * 构造通知内容
     * @param sessionId
     * @param msgCode
     * @param messageData
     * @return
     */
    public static PushMessageInfo buildMessageInfo(String sessionId, String msgCode, String messageData){
         PushMessageInfo messageInfo = new PushMessageInfo();
         messageInfo.setTargetSessionId(sessionId);
         messageInfo.setMsgCode(msgCode);
         messageInfo.setMsgContent(messageData);
         return  messageInfo;
    }

    /**
     * 处理通知消息
     * @param event
     * @param msgCode
     * @param sessionId
     * @param messageInfo
     */
    public static void handlerSendMessage(String event,String msgCode,String sessionId,String messageInfo){
        sendEventMessage(event,buildMessageInfo(sessionId,msgCode,messageInfo));
    }


    public static void main(String[] arg) {
        try {
            String SOCKETURL = "https://imwebsockettest.sanduspace.com";
            String uuid = UUID.randomUUID().toString().replace("-", "");
            SOCKETURL = SOCKETURL + "?userSessionId=merchantManage_" + uuid + "&appId=11";
            socket = IO.socket(SOCKETURL);
            Map<String, String> msg = new HashMap<>();
            msg.put("type", "fullHouseRender");
            msg.put("planId", "11111");
            socket.on(Socket.EVENT_CONNECT, args -> {
                logger.info("success  connect to socket");
                if (true) {
                    SocketIOUtil.handlerSendMessage(
                            "im_push_msg_event",
                            SocketIOUtil.IM_PUSH_SYSTEM_MSG_CODE + ":" + "fullHouseRenderNotify",
                            "049c3a07cb694ae3a0d6176e2dffa07a",
                            JsonObjectUtil.bean2Json(msg));


                }
            }).on("event", args -> {

            }).on(Socket.EVENT_DISCONNECT, args -> {

            });
            socket.connect();
        } catch (Exception e) {
            logger.error("连接socket失败", e);
        }

    }

}
