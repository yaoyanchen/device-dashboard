package com.easy.dashboard.wechat;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import com.easy.dashboard.utils.RedisUtil;
import com.easy.dashboard.utils.StringUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class WeChatMsgMatter {
    public String msg;
    public String openId;
    @Resource
    RedisUtil redisUtil;
    @Resource
    IWechatService wechatService;

    @Resource
    @Qualifier("threadPoolTaskExecutor")
    ThreadPoolTaskExecutor executorService;
    @Scheduled(cron ="30 08 * * * *")
    public void sendMessage() {
        String accessToken = (String) redisUtil.get("token");
        if (accessToken == null) {
            accessToken = wechatService.getAccessToken("wxcea2d58888d232ff", "807a35c52eddc1bd34064b59871afa1f");
            redisUtil.set("token", accessToken, 6000);
        }
        Console.log("token: " + accessToken);
        if (StringUtil.isEmpty(msg)) {
            msg = wechatService.getMsg("X4GzvLI6NRO89FBu");
        }
        Console.log("消息:" + msg);
        String weatherInfo = wechatService.getWeatherInfo("32169678", "Jl58nhiN", "杭州");
        WeatherInfo weatherObject = JSON.parseObject(weatherInfo, WeatherInfo.class);
        if (StringUtil.isEmpty(openId)) {
            openId = "on1ra6R6zwO5iwa0vKRHm819xyR8";
        }
        String templateId = "GLQoPBor8bV-p1zq993onlq5jpnWnu67ceTtLiEbewU";

        MsgBody body = new MsgBody();
        body.setTouser(openId.trim());
        body.setTemplate_id(templateId.trim());

        String finalMsg = msg;
        Date dateTimeNow = DateUtil.date();
        MsgBody.MsgData data = new MsgBody.MsgData();

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    //消息
                    MsgBody.MsgInfo remark = new MsgBody.MsgInfo();
                    remark.setValue(finalMsg);
                    remark.setColor("#f00");
                    data.setRemark(remark);
                }, executorService),
                CompletableFuture.runAsync(() -> {
                    //确定关系时间
                    String loveDate = "2021-02-12";
                    Date startDate = DateUtil.parseDate(loveDate);

                    long betweenDay = DateUtil.between(startDate, dateTimeNow, DateUnit.DAY);
                    MsgBody.MsgInfo loveInfo = new MsgBody.MsgInfo();
                    loveInfo.setValue(betweenDay + "天");
                    loveInfo.setColor("#000");
                    data.setLoveTime(loveInfo);
                }, executorService),
                CompletableFuture.runAsync(() -> {
                    //生日倒计时
                    String strBirthTime = "2022-09-16";
                    Date birthTime = DateUtil.parseDate(strBirthTime);
                    long betweenDayTwo = DateUtil.between(dateTimeNow, birthTime, DateUnit.DAY);
                    MsgBody.MsgInfo birthdayInfo = new MsgBody.MsgInfo();
                    birthdayInfo.setValue(betweenDayTwo + "天\n");
                    birthdayInfo.setColor("#000");
                    data.setBirthTime(birthdayInfo);
                }, executorService),
                CompletableFuture.runAsync(() -> {
                    //天气
                    MsgBody.MsgInfo weatherMsgInfo = new MsgBody.MsgInfo();
                    StringBuilder sb = new StringBuilder();
                    sb.append("城市:")
                            .append(weatherObject.getCity())
                            .append(",天气:")
                            .append(weatherObject.getWea())
                            .append(",最高气温:")
                            .append(weatherObject.getTem_day());
                    if (Integer.parseInt(weatherObject.getTem_day()) > 36) {
                        sb.append("，白天气温太高啦，外出记得带遮阳伞和一个小风扇~~~~~");
                    }
                    if(Integer.parseInt(weatherObject.getTem_night()) < 25) {
                        sb.append("，晚上气温有点凉哦，外出记得披一件外套~~~~");
                    }
                    weatherMsgInfo.setValue(sb.toString());
                    weatherMsgInfo.setColor("#000");
                    data.setWeather(weatherMsgInfo);
                }, executorService)
        ).join();
        body.setData(data);

        String finalAccessToken = accessToken;
        executorService.execute(() -> {
            wechatService.sendMsg(finalAccessToken, body);
        });
    }
}
