package com.easy.dashboard.config;

import com.easy.dashboard.utils.WebSocketUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

public class UserHandler implements WebSocketHandler {
    /**
     *连接成功时调用此方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String para = Objects.requireNonNull(session.getUri()).getQuery();

        WebSocketUtils.addUser(session);
        //向管理员汇报当前在线人数
        WebSocketUtils.sendMessageToAdmin();
    }

    /**
     *收到消息时调用此方法
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    /**
     *连接异常时调用此方法
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("用户连接失败");
    }

    /**
     *关闭连接时调用此方法
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("用户退出连接");
        WebSocketUtils.removeUser(session);
        //向管理员汇报当前人数
        WebSocketUtils.sendMessageToAdmin();

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
