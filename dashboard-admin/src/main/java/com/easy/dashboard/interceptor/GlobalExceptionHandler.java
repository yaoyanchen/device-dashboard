package com.easy.dashboard.interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import com.easy.dashboard.model.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseBody
    public ResultObject badRequest(HttpServletRequest req,BadRequestException e) {
        log.warn("业务异常:{}",e.getMessage());
        return ResultObject.error(Integer.parseInt(e.getCode()),e.getMessage());
    }

    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public ResultObject notLoginHandler(HttpServletRequest req,NotLoginException e) {
        log.error("sa-token异常:{}",e.getMessage());
        return ResultObject.error(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultObject exceptionHandler(HttpServletRequest req,Exception e) {
        log.error("系统异常:{}",e.getMessage());
        return ResultObject.error(e.getMessage());
    }
}
