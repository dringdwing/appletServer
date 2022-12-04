package com.vector.server.config.xss;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import java.io.IOException;

/**
 * @description: 将拦截下的请求封装为XssHttpServletRequestWrapper对象
 * @Title: XssFilter
 * @Package com.vector.server.config.xss
 * @Author 芝士汉堡
 * @Date 2022/12/4 8:47
 */
@WebFilter(filterName = "xssFilter", urlPatterns = "/*")
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {
    }

}

