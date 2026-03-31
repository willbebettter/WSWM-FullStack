package wswm.utils;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}") // 路径，建议带上用户标识
// 注意：如果需要注入 Spring 的 Bean（如 Service），需要加上此注解，并可能需要静态注入工具类
public class WebSocketServer {
    // 存储连接的会话，实际项目中建议使用 Redis 或专门的连接池管理
    public static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public static void message(String message) {
        Session session = sessions.get("666");
        sendMessage(session, message);
    }

    public static void message2(String message) {
        Session session = sessions.get("888");
        sendMessage(session, message);
    }

    public static void message3(String message) {
        Session session = sessions.get("999");
        sendMessage(session, message);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        sessions.put(userId, session);
        session.getUserProperties().put("userId", userId);
    }

    /**
     * 收到客户端消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // 1. 先处理心跳
            if ("pong".equals(message)) {
                log.info("接收到前端回应的pong");
                return; // 结束
            }

            // 2. 获取发送者ID
            String senderId = (String) session.getUserProperties().get("userId");

            // 3. 判断是否是特殊处理的用户（999）
            if ("999".equals(senderId)) {
                // 尝试解析JSON，但要防止异常
                try {
                    JSONObject jsonObject = JSONObject.parseObject(message);
                    String targetUser = jsonObject.getString("name"); // 目标用户ID
                    String content = jsonObject.getString("msg");

                    Session targetSession = sessions.get(targetUser);
                    if (targetSession != null && targetSession.isOpen()) {
                        // 直接发送纯文本内容
                        targetSession.getBasicRemote().sendText(content);
                        log.info("消息转发成功: {} -> {}", senderId, targetUser);
                    }
                } catch (JSONException e) {
                    // 如果不是JSON，当作普通消息处理，或者忽略
                    log.warn("用户999发送了非JSON消息: {}", message);
                    // 可以选择广播给所有人，或者直接返回错误
                }
            } else {
                // 4. 普通用户逻辑（例如：收到"web"指令）
                if ("web".equals(message)) {
                    // 处理web指令...
                    return;
                }
                // 默认：当作普通聊天消息处理，广播或转发
                log.info("普通用户消息: {}", message);
            }

        } catch (Exception e) {
            log.error("处理消息时发生未知错误", e);
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        sessions.remove(userId);
    }

    /**
     * 发生错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("发生错误");
        error.printStackTrace();
    }

    // =============== 工具方法 ===============

    // 发送给所有人（广播）
    public static void broadcast(String message) {
        sessions.values().forEach(session -> {
            // 检查会话是否还打开着
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // 发送给指定用户
    public static void sendMessageToUser(String userId, String message) {
        Session session = sessions.get(userId);
        if (session != null) {
            sendMessage(session, message);
        }
    }

    // 封装发送消息逻辑
    private static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}