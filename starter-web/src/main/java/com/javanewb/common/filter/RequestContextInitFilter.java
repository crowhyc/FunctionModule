package com.javanewb.common.filter;

import com.javanewb.common.http.RequestContext;
import com.javanewb.common.http.ResponseContext;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RequestContext初始化过滤器，用于在请求前后设置request到
 *
 * @author Dean.Hwang
 */
@Component
@Data
public class RequestContextInitFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(RequestContextInitFilter.class);


    @Override
    public void destroy() {
        //无处理
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        RequestContext.setRequest(req);
        ResponseContext.setResponse(resp);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // 无处理
    }
}
