package com.vector.server.config;

import com.vector.server.exception.AppleServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 精简返回给客户端的异常内容
 * @Title: ExceptionAdvice
 * @Package com.vector.server.config
 * @Author 芝士汉堡
 * @Date 2022/12/5 22:37
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler
    public String handleException(Exception e) {
        log.error("ExceptionAdvice: ", e);
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            return exception.getBindingResult().getFieldError().getDefaultMessage(); // 获取异常信息
        } else if (e instanceof AppleServerException) {
            AppleServerException exception = (AppleServerException) e;
            return exception.getMessage();
        } else if (e instanceof UnauthenticatedException) {
            return "不具备此权限";

        }
        return "后端执行异常";
    }

}
