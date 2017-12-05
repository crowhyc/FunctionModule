package com.javanewb.common.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <p>
 * Description: com.javanewb.common.configuration.filter
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 17/1/2
 */
@Configuration
public class CorsConfig {
    @Value("${info.active}")
    private String active;


    private String[] getOrigins() {
        if (active.matches("^dev$")) {
            return new String[]{"http://dev.javanewb.com"};
        } else if (active.matches("^test$")) {
            return new String[]{
                    "http://testb.javanewb.com"
            };
        } else if (active.matches("^gld$")) {
            return new String[]{"http://gld.javanewb.com"};
        } else if (active.matches("^prod$")) {
            return new String[]{
                    "http://www.javanewb.com"
            };
        } else if (active.matches("^citest$")) {
            return new String[]{
                    "http://citest.javanewb.com/"
            };
        }
        return new String[]{"*"};
    }

    @Bean
    public WebMvcConfigurerAdapter corsFilter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins(getOrigins()).allowCredentials(true);
            }
        };
    }
}
