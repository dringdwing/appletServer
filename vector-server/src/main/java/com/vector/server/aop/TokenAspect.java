package com.vector.server.aop;


import cn.hutool.core.util.StrUtil;
import com.vector.server.config.shiro.ThreadLocalToken;
import com.vector.server.util.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 利用切面类向客户端返回新令牌 拦截所有的Web方法返回值判断 是否刷新生成新令牌
 *                  检查ThreadLocal中是否保存令牌 把新令牌绑定到R对象中
 * @Title: TokenAspect
 * @Package com.vector.server.aop
 * @Author 芝士汉堡
 * @Date 2022/12/5 9:53
 */
@Aspect
@Component
public class TokenAspect {

    @Resource
    private ThreadLocalToken threadLocalToken;

    @Pointcut("execution(public * com.vector.server.controller.*.*(..))")
    public void aspect() {
    }

    @Around("aspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        R r = (R)point.proceed();
        /*检查是否有新生成的令牌*/
        String token = threadLocalToken.getToken();
        if (!StrUtil.isEmpty(token)) {
            r.put("token", token);
            threadLocalToken.clear();
        }
        return r;
    }
}
