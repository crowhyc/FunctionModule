package com.javanewb.common.http;

import com.javanewb.common.http.exceptions.HttpErrorInfo;
import com.javanewb.common.http.exceptions.HttpFailedException;
import com.javanewb.common.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * Description: com.javanewb.common.util
 * </p>
 * <p>
 * </p>
 * 基于OkHttp的http请求工具类，目前只支持返回结果为json的接口
 * 如果需要添加其他功能，请联系我
 *
 * @author Dean.Hwang
 * @version 1.0
 * date 17/6/13
 */
@Slf4j
public class HttpUtil {
    private HttpUtil() {
    }

    private static final OkHttpClient okClient = new OkHttpClient();
    private static final MediaType JSON_META_TYPE = MediaType.parse("application/json;charset=utf-8");

    public static <T> T sendGet(String url, Class<T> tClass, Map<String, String> params) {
        if (params != null && params.size() > 0) {
            url += formatGetParam(params);
        }
        Request request = new Request.Builder().url(url).build();
        HttpRequestData httpRequestData = new HttpRequestData(url, params, request, "GET").invoke();
        if (httpRequestData.is())
            return JsonMapper.nonDefaultMapper().fromJson(httpRequestData.getResult(), tClass);
        Integer httpCode = httpRequestData.getHttpCode();
        String result = httpRequestData.getResult();
        HttpErrorInfo info = new HttpErrorInfo(httpCode, result, url, JsonMapper.nonDefaultMapper().toJson(params), "GET");
        throw new HttpFailedException(info);
    }

    public static <T> T sendPost(String url, Class<T> tClass, Map<String, String> params) {
        RequestBody body = RequestBody.create(JSON_META_TYPE, JsonMapper.nonDefaultMapper().toJson(params));
        Request request = new Request.Builder().url(url).post(body).build();
        HttpRequestData httpRequestData = new HttpRequestData(url, params, request, "POST").invoke();
        if (httpRequestData.is())
            return JsonMapper.nonDefaultMapper().fromJson(httpRequestData.getResult(), tClass);
        Integer httpCode = httpRequestData.getHttpCode();
        String result = httpRequestData.getResult();
        HttpErrorInfo info = new HttpErrorInfo(httpCode, result, url, JsonMapper.nonDefaultMapper().toJson(params), "POST");
        throw new HttpFailedException(info);
    }

    public static String formatGetParam(Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return "";
        }
        List<String> paramStr = new ArrayList<>();
        params.entrySet().parallelStream().forEach(e -> paramStr.add(e.getKey() + "=" + e.getValue()));
        return "?" + paramStr.stream().collect(Collectors.joining("&"));
    }

    private static class HttpRequestData {
        private boolean myResult;
        private String url;
        private Map<String, String> params;
        private Request request;
        private Integer httpCode;
        private String result;
        private String type;

        public HttpRequestData(String url, Map<String, String> params, Request request, String type) {
            this.url = url;
            this.params = params;
            this.request = request;
            this.type = type;
        }

        boolean is() {
            return myResult;
        }

        public Integer getHttpCode() {
            return httpCode;
        }

        public String getResult() {
            return result;
        }

        public HttpRequestData invoke() {
            Response response;
            httpCode = 0;
            result = "";
            try {
                response = okClient.newCall(request).execute();
                httpCode = response.code();
                result = response.body().string();
                if (httpCode == 200) {
                    log.info("Http {} Request Success Url:{}  Result:{}", type, url, result);
                    myResult = true;
                    return this;
                }
            } catch (IOException e) {
                log.error("Http {} Request Failed Url:{} Params:{}", type, url, JsonMapper.nonDefaultMapper().toJson(params));
            }
            myResult = false;
            return this;
        }
    }
}
