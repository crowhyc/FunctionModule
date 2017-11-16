package com.javanewb.common.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javanewb.common.http.ClientHttpRequestInterceptorImpl;
import com.javanewb.common.http.HttpClientComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Title: HttpClientConfiguration
 * </p>
 * <p>
 * Description: com.javanewb.common.configuration.http
 * </p>
 *
 * @author Dean.Hwang
 * date 2017/6/26 下午4:14
 */
@Configuration
public class HttpClientConfiguration {
    @Value("${httpclient.connectTimeout:3000}")
    int connectTimeout;
    @Value("${httpclient.readTimeout:20000}")
    int readTimeout;

    @ConditionalOnMissingBean(ObjectMapper.class)
    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper;
    }

    @ConditionalOnMissingBean(RestTemplate.class)
    @Bean
    public RestTemplate getRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory,
                                        ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new ClientHttpRequestInterceptorImpl());
        restTemplate.setInterceptors(interceptors);

        final MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        mappingJacksonHttpMessageConverter.setObjectMapper(objectMapper);

        restTemplate.setMessageConverters(new ArrayList<HttpMessageConverter<?>>() {
            /** serialVersionUID */
            private static final long serialVersionUID = -7902530205707433058L;

            {
                StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
                stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text",
                                "html", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET), new MediaType("text",
                                "plain", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET), new MediaType(
                                "application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET),
                        new MediaType("application", "octet-stream",
                                MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)));
                add(new ByteArrayHttpMessageConverter());
                add(stringHttpMessageConverter);
                add(new ResourceHttpMessageConverter());
                add(new SourceHttpMessageConverter());
                add(mappingJacksonHttpMessageConverter);
            }
        });
        return restTemplate;
    }

    @ConditionalOnMissingBean(ClientHttpRequestFactory.class)
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        return clientHttpRequestFactory;
    }

    @ConditionalOnMissingBean(HttpClientComponent.class)
    @Bean
    public HttpClientComponent httpClientComponent() {
        return new HttpClientComponent();
    }
}
