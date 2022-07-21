package com.easy.dashboard.controller;

import cn.hutool.core.date.DateUtil;
import com.easy.dashboard.config.WebSocketServer;
import com.easy.dashboard.model.EmailDTO;
import com.easy.dashboard.model.MessageBean;
import com.easy.dashboard.model.ResultObject;
import com.easy.dashboard.model.SendMsg;
import com.easy.dashboard.utils.EmailUtil;
import com.easy.dashboard.utils.JSON;
import com.easy.dashboard.utils.RedisUtil;
import com.easy.dashboard.utils.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
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
            sendEmail(param);
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

    @GetMapping("/api/getMsg")
    public ResultObject getMsg() {
        String key = DateUtil.format(new Date(), "yyyy-MM-dd");
        Object o = redisUtil.get(key);
        return ResultObject.success(o);
    }


    private final RedisUtil redisUtil;

    public void sendEmail(SendMsg sendMsg) {
        log.info("消息:{}", JSON.toJSONString(sendMsg));
        if ("test".equals(sendMsg.getTitle())) {
            if (sendMsg.getMessage().contains("信息网络部")) {
                log.info("消息来自信息网络部门...");
                String key = DateUtil.format(new Date(), "yyyy-MM-dd");
                if (redisUtil.get(key) != null) {
                    log.info("当天已经发过短信了,正在删除...");
                    redisUtil.del(key);
                }
                long time = 60 * 60;
                redisUtil.set(key, sendMsg.getMessage(), time);
            }
        }
    }
}
