package com.easy.dashboard.config;

import com.easy.dashboard.interceptor.BadRequestException;
import com.easy.dashboard.model.ResultObject;
import com.easy.dashboard.model.SendMsg;
import com.easy.dashboard.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@ServerEndpoint("/websocket/{userName}")
@Component
@Slf4j
public class WebSocketServer {
    private static final CopyOnWriteArrayList<WebSocketServer> sWebSocketServers = new CopyOnWriteArrayList<>();
    private Session mSession;
    private String userName;
    private static int onlineUserCount = 0;

    public static ResultObject kickout(String userName) {
        for (WebSocketServer server : sWebSocketServers) {
            if (server.userName.equals(userName)) {
                try {
                    sWebSocketServers.remove(server);
                    server.mSession.close();
                } catch (IOException e) {
                    log.info(e.toString());
                    return ResultObject.error("关闭连接失败");
                }
                break;
            }
        }
        return ResultObject.success("踢出成功");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        mSession = session;
        int size = (int) sWebSocketServers.stream().filter(
                a -> a.userName.equals(userName)).count();
        if (size > 0) {
            throw new BadRequestException("用户已接入,请勿重复接入");
        }
        sWebSocketServers.add(this); // 将回话保存
        System.out.println(userName + "上线啦");
        //log.info("-->onOpen new connect vmcNo is " + userName);
        this.userName = userName;
        addOnlineUser();
    }

    @OnClose
    public void onClose(@PathParam("userName") String userName) {
        sWebSocketServers.remove(this);
        log.info(userName + "下线了");
        removeOnlineUser();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("-->onMessage " + message);
// 这里选择的是让其他客户端都知道消息，类似于转发的聊天室，可根据使用场景使用
        for (WebSocketServer socketServer : sWebSocketServers) {
            socketServer.sendMessage("i have rcv you message");
        }
    }

    /**
     * 对外发送消息
     *
     * @param message
     */
    public boolean sendMessage(String message) {
        try {
            mSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.info(e.toString());
            return false;
        }
        return true;
    }

    /**
     * 对某个机器发送消息
     *
     * @param message
     * @param userName   用户名
     * @return true, 返回发送的消息, false，返回failed字符串
     */
    public static String sendMessage(String message, String userName) {
        boolean success = false;
        for (WebSocketServer server : sWebSocketServers) {
            if (server.userName.equals(userName)) {
                success = server.sendMessage(message);
                break;
            }
        }
        return success ? message : "failed";
    }

    /**
     * 对所有机器发送消息,不包括自己,并且验证该机器是否存在
     *
     * @param message
     * @param userName   用户名
     * @return 返回验证的信息
     */
    public static String sendMessageValidation(String message, String userName) {
        boolean success = false;
        for (WebSocketServer server : sWebSocketServers) {
            if (server.userName.equals(userName)) {
                success = server.sendMessage(message);
                break;
            }
        }
        return success ? message : JSON.toJSONString(new SendMsg(500,"发送失败", "该机器不在线",userName));
    }

    public static int getOnlineUserCount() {
        return onlineUserCount;
    }

    //获取在线用户
    public static Set<String> getOnlineUser(){
        Set<String> list = new HashSet<>();
        for (WebSocketServer server : sWebSocketServers) {
            list.add(server.userName);
        }
        return list;
    }


    private synchronized void addOnlineUser() {
        onlineUserCount ++ ;
    }

    private synchronized void removeOnlineUser() {
        onlineUserCount -- ;
    }
}
