package com.vector.server.config.shiro;

import org.springframework.stereotype.Component;

/**
 * @description: 创建存储令牌的媒介类
 * @Title: ThreadLocalToken
 * @Package com.vector.server.config.shiro
 * @Author 芝士汉堡
 * @Date 2022/12/4 13:10
 */

@Component
public class ThreadLocalToken {
    private ThreadLocal<String> local = new ThreadLocal<>();

    public void setToken(String token) {
        local.set(token);
    }

    public String getToken() {
        return (String) local.get();
    }

    public void clear() {
        local.remove();
    }

}