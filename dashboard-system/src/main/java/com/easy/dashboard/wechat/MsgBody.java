package com.easy.dashboard.wechat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MsgBody {
    /**
     * openId
     */
    private String touser;

    private String template_id;

    private MsgData data;



    @Setter
    @Getter
    public static class MsgInfo {
        private String value;
        private String color;
    }
    @Setter
    @Getter
    public static class MsgData {
        private MsgInfo remark;
        /**
         * 天气
         */
        private MsgInfo weather;
        /**
         * 恋爱时间
         */
        private MsgInfo loveTime;
        /**
         * 生日倒计时
         */
        private MsgInfo birthTime;
    }
}
