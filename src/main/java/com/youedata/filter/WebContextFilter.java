package com.youedata.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt使用的跨域过滤器
 */
public class WebContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletResponse hsr = (HttpServletResponse)response;
        HttpServletRequest req = (HttpServletRequest) request;

        hsr.setHeader("Access-Control-Allow-Origin",req.getHeader("Origin"));
        hsr.setHeader("Access-Control-Allow-Credentials","true");
        hsr.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS,PATCH");
        hsr.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,accessToken,accountId,authorization,loginType");
        hsr.setHeader("Access-Control-Expose-Headers", "*");
        hsr.setHeader("Access-Control-Max-Age","3600");

        filterChain.doFilter(request, hsr);
    }

    @Override
    public void destroy() {

    }
}
