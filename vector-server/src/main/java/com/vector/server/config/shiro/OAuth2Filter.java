package com.vector.server.config.shiro;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @Title: OAuth2Filter
 * @Package com.vector.server.config.shiro
 * @Author 芝士汉堡
 * @Date 2022/12/4 13:55
 */
@Component
@Scope("prototype")
public class OAuth2Filter extends AuthenticatingFilter {
    @Value("${applet.jwt.cache-expire}")
    private int cachedExpire;

    @Resource
    private ThreadLocalToken threadLocalToken;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 创建令牌
     * 客户端每次发的请求会被此拦截器拦截，然后从请求头中获取token，然后封装成toekn对象（令牌）
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = getRequestToken(req);
        if (StrUtil.isBlank(token)) {
            return null;
        }

        return new OAuth2Token(token);
    }

    /**
     * 拦截请求
     * 判断那种请求被shiro处理
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 经过isAccessAllowed判断后，true，就会执行onAccessDenied方法,有如下功能
     * 从请求头获取token ->  判断token是否过期  ->  是否刷新令牌字符串
     * ->  刷新令牌字符串 ->  保存令牌字符串到redis、媒介类
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Methods", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Origin", "true");
        threadLocalToken.clear();
        String token = getRequestToken(req);
        if (StrUtil.isBlank(token)) {
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            resp.getWriter().print("无效的令牌");
            return false;
        }
        try {
            jwtUtil.verifyToken(token);
        } catch (TokenExpiredException e) {
            /*刷新令牌*/
            if (redisTemplate.hasKey(token)) {
                redisTemplate.delete(token);
                int userId = jwtUtil.getUserId(token);
                token = jwtUtil.createToken(userId);
                redisTemplate.opsForValue().set(token, userId+"", cachedExpire, TimeUnit.DAYS);
                threadLocalToken.setToken(token);
            }else {
                /*客户端令牌过期 服务端缓存的令牌也么有 需要用户冲i想你登陆*/
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
                resp.getWriter().print("令牌过期");
                return false;
            }
        } catch (JWTDecodeException e) {
            /*无效令牌的处理*/
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            resp.getWriter().print("无效的令牌");
            return false;
        }
        /*执行realm实现认证和授权*/
        boolean bool = executeLogin(request, response);
        /*如果为false 则会执行onLoginFailure*/
        return bool;
    }

    /**
     * 在执行执行realm实现认证和授权时 判定用户没有登录或登录失败会执行此方法
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Methods", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Origin", "true");
        resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
        try {
            resp.getWriter().print(e.getMessage());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return false;
    }

    /**
     * 从请求头里获得令牌的操作
     */
    private String getRequestToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        return token;
    }

}
