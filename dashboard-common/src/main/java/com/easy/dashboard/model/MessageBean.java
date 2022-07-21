package com.easy.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageBean {
    private String title;//推送标题
    private String content;//推送内容
    private String imageUrl;//推送图片地址
    private int type;//推送类型
    private String targetId;//推送目标id
    private int minYear;//推送限定的最小接收年龄
    private int maxYear;//推送限定的最大接收年龄
    private int channel = -1;//推送渠道
}
