package com.easy.dashboard.controller;

import com.easy.dashboard.model.ResultObject;
import com.easy.dashboard.wechat.WeChatMsgMatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/wechat")
public class TencentController {
    @Resource
    WeChatMsgMatter weChatMsgMatter;

    @GetMapping("/pushMsg")
    @ResponseBody
    public ResultObject pushMsg(String msg,String openId ) {
        weChatMsgMatter.msg = msg;
        weChatMsgMatter.openId = openId;
        weChatMsgMatter.sendMessage();
        return ResultObject.success();
    }

    @GetMapping("/reset")
    @ResponseBody
    public String reset(String signature, String timestamp,String nonce, String echostr) {
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        return echostr;
    }
}
