package com.javanewb.common.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求记录,耗时记录 Created by Dean.Hwang on 16/11/24.
 */
public class ValidAndLoggingInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger("MVCLOG");

    private static final String SHOP_ID = "shopId";
    private static final String BRAND_ID = "brandId";
    private long startTime = 0;
    @Value("${info.active}")
    private String activeProfile;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        startTime = System.currentTimeMillis();
        Map<String, String> pathVariableMaps = new HashMap<>();

        logger.info("Method [{}], PathVariables {}", request.getMethod(), pathVariableMaps);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        logger.info("Method [{}], Status Code [{}], Time Spent {}ms", request.getMethod(),
                response.getStatus(), costTime);
        MDC.remove(SHOP_ID);
        MDC.remove(BRAND_ID);
    }

}
