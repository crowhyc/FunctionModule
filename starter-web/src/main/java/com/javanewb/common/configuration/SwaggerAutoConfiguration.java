package com.javanewb.common.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

/**
 * Created by Dean.Hwang on 2016/8/24.
 */
@EnableSwagger2
@ConditionalOnWebApplication
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ConditionalOnProperty(prefix = "common.swagger", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SwaggerConfigProperties.class)
public class SwaggerAutoConfiguration extends WebMvcConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);

    private SwaggerConfigProperties configProperties;
    @Value("${info.active}")
    private String active;

    public SwaggerAutoConfiguration(SwaggerConfigProperties properties) {
        this.configProperties = properties;
    }

    @PostConstruct
    public void validate() {
        logger.info("============================>>Swagger auto config : {}", this.configProperties);
    }

    @Bean
    public Docket swaggerSpringfoxDocket() {
        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .pathMapping(this.configProperties.getPathMapping())
                .select().apis(RequestHandlerSelectors.basePackage(configProperties.getBasePackage()))
                .paths(PathSelectors.regex(this.configProperties.getPathRegex())) // and by paths
                .build();

        if (!active.matches("^dev|test|local$")) {
            swaggerSpringMvcPlugin.enable(false);
        }
        return swaggerSpringMvcPlugin;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(this.configProperties.getApiInfo().getTitle())
                .description(this.configProperties.getApiInfo().getDescription())
                .termsOfServiceUrl("http://www.javanewb.com")
                .contact(new Contact("javanewb", "http://www.javanewb.com", ""))
                .version(this.configProperties.getApiInfo().getVersion())
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
