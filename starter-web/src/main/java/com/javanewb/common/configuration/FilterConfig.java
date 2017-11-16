package com.javanewb.common.configuration;

import com.javanewb.common.filter.RequestContextInitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * <p>
 * Description: com.javanewb.common.configuration
 * </p>
 * <p>
 * </p>
 * date：2017/9/25
 *
 * @author Dean.Hwang
 */
public class FilterConfig {
    @Bean
    public FilterRegistrationBean requestContextInitFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new RequestContextInitFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean multipartFileFilter() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(multipartFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER
                - 10001);
        return registrationBean;
    }

}
