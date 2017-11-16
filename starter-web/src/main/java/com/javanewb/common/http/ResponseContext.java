package com.javanewb.common.http;

import javax.servlet.http.HttpServletResponse;

/**
 * 工具类，可在controller，service及同线程下，随时获取当前请求相关信息。 注意：不能再新起线程，及发布的service服务中使用ResponseContext
 */
public class ResponseContext {

    private ResponseContext() {
    }

    private static final ThreadLocal<HttpServletResponse> responses = new ThreadLocal<>();


    public static void setResponse(HttpServletResponse response) {
        responses.set(response);
    }


    public static HttpServletResponse getResponse() {
        return responses.get();
    }


    public static void remove() {
        responses.remove();
    }


}
