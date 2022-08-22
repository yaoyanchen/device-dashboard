package com.easy.dashboard.wechat;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class WeChatService implements IWechatService {
    @Override
    public String getWeatherInfo(String appId, String appSecret, String city) {
        return HttpUtil.createGet("https://www.yiketianqi.com/free/day?appid=" + appId + "&appsecret=" + appSecret + "&unescape=1&city=" + city)
                .execute().body();
    }

    @Override
    public String getAccessToken(String appId, String appSecret) {
        String body = HttpUtil.createGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret).execute().body();
        return JSON.parseObject(body).getString("access_token");
    }

    @Override
    public String getMsg(String token) {
        String body = HttpUtil.createGet("https://v2.alapi.cn/api/qinghua?token=" + token)
                .execute().body();
        JSONObject object = JSON.parseObject(body);
        if (object.getInteger("code") == 200 && "success".equals(object.getString("msg"))) {
            JSONObject dataObj = object.getJSONObject("data");
            return dataObj.getString("content");
        }
        return null;
    }

    @Override
    public void sendMsg(String token, MsgBody msgBody) {
        String body = JSON.toJSONString(msgBody);
        Console.log(body);
        String s = HttpUtil.createPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token)
                .body(body)
                .execute().body();
        Console.log("发送消息返回:"+s);
    }
}
