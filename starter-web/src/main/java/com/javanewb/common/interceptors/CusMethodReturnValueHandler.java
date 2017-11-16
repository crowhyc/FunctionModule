package com.javanewb.common.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.javanewb.common.filter.LoggerMDCFilter;
import com.javanewb.common.http.RequestContext;
import com.javanewb.common.http.ResponseContext;
import com.javanewb.common.response.ResultVO;
import com.javanewb.common.response.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Title: CusMethodReturnValueHandler
 * </p>
 * <p>
 * Description: com.javanewb.common.configuration.swagger
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 2016年11月30日
 */
public class CusMethodReturnValueHandler extends RequestResponseBodyMethodProcessor {

    private Logger log = LoggerFactory.getLogger("MVCLOG");
    private ObjectMapper objectMapper;

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param converters
     */
    public CusMethodReturnValueHandler(List<HttpMessageConverter<?>> converters) {
        super(converters);
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        this.objectMapper = new ObjectMapper();
        /**
         * 序列换成json时,将所有的long变成string
         * 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        this.objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }

    private String[] skipUrls = {"/v2/api-docs", "/swagger-resources", "/info", "/error", "/health", "/pause", "/refresh", "/env", "/restart", "/resume"};

    private boolean isSkipUrl(String url) {
        return Arrays.asList(skipUrls).parallelStream().filter(url::endsWith
        ).count() > 0;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage;
        if (RequestContext.getRequest() != null) {
            inputMessage = new ServletServerHttpRequest(RequestContext.getRequest());
        } else {
            inputMessage = createInputMessage(webRequest);
        }
        ServletServerHttpResponse outputMessage;
        if (ResponseContext.getResponse() != null) {

            outputMessage = new ServletServerHttpResponse(ResponseContext.getResponse());
        } else {
            outputMessage = createOutputMessage(webRequest);
        }
        try {
            if (isSkipUrl(RequestContext.getRequest().getServletPath())) {
                writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
                return;
            }
            //包装数据
            ResultVO<Object> vo = new ResultVO<>();
            vo.status(Status.OK);
            if (returnValue instanceof Boolean) {
                vo.setData(new BooleanData((Boolean) returnValue));
            } else {
                vo.setData(returnValue);
            }
            Object resp = vo.build();// 包装数据完成
            vo.setMsgId(MDC.get(LoggerMDCFilter.IDENTIFIER));
            String body = this.objectMapper.writeValueAsString(resp);
            Long time = Long.valueOf(MDC.get(LoggerMDCFilter.TIME));
            log.info("Response Body RequestURL{} {} cost time {}: [{}]", inputMessage.getServletRequest().getRequestURI(), MDC.get(LoggerMDCFilter.IDENTIFIER), System.currentTimeMillis() - time, body);
            // Try even with null return value. ResponseBodyAdvice could get involved.
            writeWithMessageConverters(resp, returnType, inputMessage, outputMessage);
        } finally {
            outputMessage.close();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BooleanData {
        private Boolean result;
    }

}

