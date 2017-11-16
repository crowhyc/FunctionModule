/**
 *
 */
package com.javanewb.common.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * <p>
 * Title: ClientHttpRequestInterceptorImpl
 * </p>
 *
 * @author Dean.Hwang
 * date 2016年5月23日
 */
public class ClientHttpRequestInterceptorImpl implements ClientHttpRequestInterceptor {

    /**
     * 日志
     */
    private Log LOG = LogFactory.getLog("PartnerRequest");

    /**
     * aplication/json;charset=UTF-8
     */
    private final MediaType application_json_utf8 = MediaType
            .parseMediaType("application/json;charset=UTF-8");

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        HttpMethod httpMethod = request.getMethod();
        HttpHeaders headers = request.getHeaders();
        if (httpMethod == HttpMethod.POST) {
            headers.setContentType(application_json_utf8);
        }

        long start = System.currentTimeMillis();
        EnhancerClientHttpResponse resp = null;
        try {
            resp = new EnhancerClientHttpResponse(execution.execute(request,
                    body));
        } finally {

            String respText = resp == null ? "" : resp.responseText();

            LOG.info(MessageFormat.format(
                    "请求地址: {0}, 请求参数: {1} => 返回结果: {2}。 [{3}]ms。 ", request
                            .getURI().toString(), new String(body), respText,
                    System.currentTimeMillis() - start));
        }

        return resp;
    }

}
