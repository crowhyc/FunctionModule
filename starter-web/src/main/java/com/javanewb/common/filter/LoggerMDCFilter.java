package com.javanewb.common.filter;

import com.javanewb.common.configuration.util.UUIDUtil;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 日志过滤器，埋点处理
 *
 * @author Dean.Hwang
 */
public class LoggerMDCFilter implements Filter {
    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String URI_KEY = "URI";
    public static final String TIME = "TIME";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // 唯一标识埋点，设置线程唯一标识，方便查找日志，组合方式 时间戳@请求埋点@线程号
        MDC.put(IDENTIFIER, UUIDUtil.getUUID());
        // uri_key
        MDC.put(URI_KEY, req.getRequestURI());
        MDC.put(TIME, Long.toString(System.currentTimeMillis()));
        chain.doFilter(request, response);
    }

    /**
     * 销毁MDC
     */
    @Override
    public void destroy() {
        MDC.remove(IDENTIFIER);
        MDC.remove(URI_KEY);
    }


}
