package com.easy.dashboard.utils;

import com.easy.dashboard.model.MessageBean;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketUtils {
    /**
     * 用来存放普通用户Session
     */
    private static final CopyOnWriteArraySet<WebSocketSession> usersSessionSet = new CopyOnWriteArraySet<>();
    /**
     * 用来存放管理员Session
     */
    private static final CopyOnWriteArraySet<WebSocketSession> adminSessionSet = new CopyOnWriteArraySet<>();

    /**
     * 添加管理员
     * @param socketSession
     */
    public static synchronized void addAdmin(WebSocketSession socketSession){
        adminSessionSet.add(socketSession);
    }

    /**
     * 添加普通用户
     * @param socketSession
     */
    public static synchronized void addUser(WebSocketSession socketSession){
        usersSessionSet.add(socketSession);
    }

    /**
     * 删除管理员
     * @param webSocketSession
     */
    public static synchronized void removeAdmin(WebSocketSession webSocketSession){
        adminSessionSet.remove(webSocketSession);
    }

    /**
     * 删除普通用户
     * @param webSocketSession
     */
    public static synchronized void removeUser(WebSocketSession webSocketSession){
        usersSessionSet.remove(webSocketSession);
    }

    /**
     * 获取管理员在线人数
     * @return
     */
    public static synchronized int getAdminOnlineCount(){
        return adminSessionSet.size();
    }

    /**
     * 获取普通用户在线人数
     * @return
     */
    public static synchronized int getUserOnlineCount(){
        return usersSessionSet.size();
    }

    /**
     * 发送消息给管理员
     */
    public static void sendMessageToAdmin(){
        adminSessionSet.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(new TextMessage("在线人数为："+getUserOnlineCount()));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("发送消息给管理员失败："+e.getLocalizedMessage());
            }
        });
    }

    /**
     * 发送消息给管理员
     */
    public static void sendMessageToAdmin(String msg){
        adminSessionSet.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("发送消息给管理员失败："+e.getLocalizedMessage());
            }
        });
    }


    /**
     * 发送消息给所有用户
     * @param msg
     */

    public static void sendMessageToUser(String msg){
        usersSessionSet.forEach(usersSessionSet->{
            try {
                usersSessionSet.sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("消息发送失败");
            }
        });
    }

    /**
     * 发送给某个用户
     */

    public static void sendMessageToUserForSingle(String msg){
        MessageBean messageBean = JSON.parseObject(msg,MessageBean.class);
        usersSessionSet.forEach(webSocketSession -> {
            String[] path = webSocketSession.getUri().getQuery().split("&");
            HashMap<String,String> map = new HashMap<>();
            for (String s : path) {
                String[] para = s.split("=");
                map.put(para[0], para[1]);
            }
            if (map.get("id").equals(messageBean.getTargetId())){
                try {
                    webSocketSession.sendMessage(new TextMessage(msg));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("发送消息给用户失败："+e.getLocalizedMessage());
                }
            }

        });
    }

    /**
     * 发送给某个渠道
     */

    public static void sendMessageToUserForChannel(String msg){
        MessageBean messageBean = JSON.parseObject(msg,MessageBean.class);
        usersSessionSet.forEach(webSocketSession -> {
            String[] path = webSocketSession.getUri().getQuery().split("&");
            HashMap<String,String> map = new HashMap<>();
            for (String s : path) {
                String[] para = s.split("=");
                map.put(para[0], para[1]);
            }
            if (Integer.parseInt(map.get("channel")) == messageBean.getChannel()){
                try {
                    webSocketSession.sendMessage(new TextMessage(msg));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("发送消息给用户失败："+e.getLocalizedMessage());
                }
            }

        });
    }


    /**
     * 通过条件发送信息
     */

    public static void sendMessageToUserForCondition(String msg){
        MessageBean messageBean = JSON.parseObject(msg,MessageBean.class);
        usersSessionSet.forEach(webSocketSession -> {
            //读取前端连接时的地址，从地址中获取条件参数，并保存到hashmap中去
            String[] path = webSocketSession.getUri().getQuery().split("&");
            HashMap<String,String> map = new HashMap<>();
            for (int i=0;i<path.length;i++){
                String[] para= path[i].split("=");
                map.put(para[0],para[1]);
            }
            //判断渠道是否符合条件参数，符合则发送消息
            if (Integer.parseInt(map.get("channel")) == messageBean.getChannel() && Integer.parseInt(map.get("age"))>messageBean.getMinYear() && Integer.valueOf(map.get("age"))<messageBean.getMaxYear()){
                try {
                    webSocketSession.sendMessage(new TextMessage(msg));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("发送消息给用户失败："+e.getLocalizedMessage());
                }
            }

        });
    }
}
