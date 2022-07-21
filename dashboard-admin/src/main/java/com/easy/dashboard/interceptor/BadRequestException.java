package com.easy.dashboard.interceptor;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String message;

    public BadRequestException() {
        super();
    }
    public BadRequestException(String code,String msg) {
        this.code = code;
        this.message = msg;
    }

    public BadRequestException(String msg) {
        this.code = "400";
        this.message = msg;
    }

    public BadRequestException(HttpStatus status, String msg){
        super(msg);
        this.code = status.value() + "";
    }
}
