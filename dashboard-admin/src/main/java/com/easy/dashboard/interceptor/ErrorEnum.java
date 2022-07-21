package com.easy.dashboard.interceptor;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    SUCCESS(200,"获取成功");
    private final int code;
    private final String msg;

    ErrorEnum(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }
}
