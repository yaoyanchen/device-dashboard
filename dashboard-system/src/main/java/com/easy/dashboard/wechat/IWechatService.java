package com.easy.dashboard.wechat;

public interface IWechatService {
    /**
     * 获取天气信息
     * @param appId .
     * @param appSecret .
     * @return .
     */
    String getWeatherInfo(String appId,String appSecret,String city);
    /**
     * 获取微信公众号的token
     * @param appId .
     * @param appSecret .
     * @return token
     */
    String getAccessToken(String appId,String appSecret);

    /**
     * 获取情话信息
     * @param token .
     * @return .
     */
    String getMsg(String token);

    /**
     * 推送消息
     * @param msgBody 数据
     */
    void sendMsg(String token,MsgBody msgBody);

}
