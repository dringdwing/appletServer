package com.vector.server.exception;

import lombok.Data;

/**
 * @description: 自定义异常类
 * @Title: AppleServerException
 * @Package com.vector.server.exception
 * @Author 芝士汉堡
 * @Date 2022/12/4 5:20
 */
@Data
public class AppleServerException extends RuntimeException{
    private String msg;
    private Integer code = 500;

    public AppleServerException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public AppleServerException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public AppleServerException( String msg, Integer code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    public AppleServerException( String msg, Integer code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
