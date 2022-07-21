package com.easy.dashboard.controller;

import com.easy.dashboard.config.WebSocketServer;
import com.easy.dashboard.model.MessageBean;
import com.easy.dashboard.model.ResultObject;
import com.easy.dashboard.model.SendMsg;
import com.easy.dashboard.utils.JSON;
import com.easy.dashboard.utils.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class NotificationController {
    /**
     * 群发消息
     * @param messageBean 记录后台管理页面传过来的内容
     * @return
     */
    @RequestMapping(value = "/api/notification/sendAll",method = RequestMethod.POST  , produces = "application/json;charset=UTF-8")
    public String sendAll(@RequestBody MessageBean messageBean){
        if (messageBean != null){
            String backInfo = JSON.toJSONString(messageBean);
            //发送消息前端设备
            WebSocketUtils.sendMessageToUser(backInfo);
        }else {
            //告知管理员消息发送失败
            WebSocketUtils.sendMessageToAdmin("发送失败");
        }
        return "{}";
    }

    @RequestMapping("/api/notification/push/{userName}")
    public String push(@PathVariable("userName") String userName, HttpServletRequest request){
        String message = request.getParameter("message");
        String title = request.getParameter("title");
        if (message != null){
            //发送消息前端设备
            SendMsg sendMsg = new SendMsg(0,message,title,userName);

            String sendMessage = JSON.toJSONString(sendMsg);
            return WebSocketServer.sendMessage(sendMessage, userName);
        }else {
            //告知管理员消息发送失败
            WebSocketUtils.sendMessageToAdmin("发送失败");
        }
        return "{}";
    }

    @PostMapping("/api/notification/push")
    public String push(@RequestBody SendMsg sendMsg){

        if (sendMsg.getMessage() != null && sendMsg.getUserName() != null){
            SendMsg param = new SendMsg(0,sendMsg.getTitle(),sendMsg.getMessage(),sendMsg.getUserName());
            String sendMessage = JSON.toJSONString(param);
            log.info("{}给{}发送消息,内容是{}",param.getTitle(),param.getUserName(),param.getMessage());
            return WebSocketServer.sendMessageValidation(sendMessage,sendMsg.getUserName());
        }else {
            //告知管理员消息发送失败
            WebSocketUtils.sendMessageToAdmin("发送失败");
        }
        return "{}";
    }

    @RequestMapping("/api/notification/getOnlineUser")
    public Set<String> getOnlineUser(){
        return WebSocketServer.getOnlineUser();
    }

    @RequestMapping("/api/notification/kickout/{userName}")
    public ResultObject kickout(@PathVariable("userName") String userName){
        return WebSocketServer.kickout(userName);
    }
}
