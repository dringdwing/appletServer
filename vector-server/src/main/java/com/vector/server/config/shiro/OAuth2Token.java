package com.vector.server.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description: 把令牌封装成认证对象
 * @Title: OAuth2Token
 * @Package com.vector.server.config.shiro
 * @Author 芝士汉堡
 * @Date 2022/12/4 11:56
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
