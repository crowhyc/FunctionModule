package com.javanewb.common.http;

import com.javanewb.common.configuration.util.AssertUtil;
import com.javanewb.common.configuration.util.BeanUtil;
import com.javanewb.common.exception.BusinessException;
import com.javanewb.common.exception.ErrorCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;

public class HttpClientComponent {

    @Autowired
    private RestTemplate restTemplate;

    private static RestTemplate staticRestTemplate;

    @PostConstruct
    public void initStatic() {
        staticRestTemplate = restTemplate;
    }

    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog("PartnerRequest");

    /**
     * @param httpRequestParam
     * @param clazz            结果数据模型
     * @return
     * @author Dean.Hwang
     * date 2016年5月26日 下午2:27:27
     */
    public <T> ResponseEntity<T> execute(HttpRequestParam httpRequestParam,
                                         Class<T> clazz) {
        AssertUtil.assertTrue(httpRequestParam.isValid(), ErrorCode.getParamError(), "接口调用参数异常.");

        ResponseEntity<T> response = null;
        try {
            if (httpRequestParam.requestMethod == RequestMethod.POST) {
                response = restTemplate.postForEntity(httpRequestParam.url,
                        httpRequestParam.httpEntity, clazz);
            } else if (httpRequestParam.requestMethod == RequestMethod.GET) {
                httpRequestParam.buildHttpGetUrl();
                response = restTemplate.getForEntity(httpRequestParam.url,
                        clazz,
                        BeanUtil.toMap(httpRequestParam.httpEntity.getBody()));
            }
        } catch (Exception e) {
            LOG.error(MessageFormat.format("调用接口失败! => {0}",
                    httpRequestParam.url), e);
            throw new BusinessException(ErrorCode.getNetError(), "网络异常", e);
        }

        return response;
    }

    public static HttpParam makeParam(Object paramValue, String url) {
        HttpParam httpRequestParam = new HttpParam(paramValue);
        httpRequestParam.setUrl(url);
        return httpRequestParam;
    }


    public static class HttpParam extends HttpRequestParam {

        public HttpParam(Object content) {
            super(content);
        }

        public HttpParam(Object content, HttpHeaders headers) {
            super(content, headers);
        }

        public <T> ResponseEntity<T> get(Class<T> tClass) {
            AssertUtil.assertTrue(this.isValid(), ErrorCode.getParamError(), "接口调用参数异常.");

            ResponseEntity<T> response;
            try {
                this.buildHttpGetUrl();
                response = staticRestTemplate.getForEntity(this.url,
                        tClass,
                        BeanUtil.toMap(this.httpEntity.getBody()));
            } catch (Exception e) {
                LOG.error(MessageFormat.format("调用接口失败! => {0}",
                        this.url), e);
                throw new BusinessException(ErrorCode.getNetError(), "网络异常", e);
            }

            return response;
        }

        public <T> ResponseEntity<T> post(Class<T> tClass) {
            AssertUtil.assertTrue(this.isValid(), ErrorCode.getParamError(), "接口调用参数异常.");

            ResponseEntity<T> response;
            try {
                response = staticRestTemplate.postForEntity(this.url,
                        this.httpEntity, tClass);
            } catch (Exception e) {
                LOG.error(MessageFormat.format("调用接口失败! => {0}",
                        this.url), e);
                throw new BusinessException(ErrorCode.getNetError(), "网络异常", e);
            }

            return response;
        }
    }
}
