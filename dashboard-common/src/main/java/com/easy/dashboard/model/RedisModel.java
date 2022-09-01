package com.easy.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedisModel {
    private Long expireTime;
    private Object value;
    private String redisType;
    private String redisKey;
}
