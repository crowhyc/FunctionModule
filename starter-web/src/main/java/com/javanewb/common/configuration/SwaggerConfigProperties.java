package com.javanewb.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Dean.Hwang on 2016/8/24.
 */
@ConfigurationProperties(prefix = "common.swagger")
@Data
public class SwaggerConfigProperties {
    private boolean enabled = false;
    private String pathRegex = "/demo.*";
    private String pathMapping = "/";
    private String basePackage = "com.javanewb";
    private ApiInfoConfig apiInfo;

    public SwaggerConfigProperties() {
        apiInfo = new ApiInfoConfig();
        apiInfo.setVersion("1.0.0");
        apiInfo.setTitle("Common API");
        apiInfo.setDescription("javanewb application rest api.");
    }


    @Override
    public String toString() {
        return "SwaggerConfigProperties{" +
                "enabled=" + enabled +
                ", pathRegex='" + pathRegex + '\'' +
                ", pathMapping='" + pathMapping + '\'' +
                ", basePackage='" + basePackage + '\'' +
                ", apiInfo=" + apiInfo +
                '}';
    }

    @Data
    public static class ApiInfoConfig {
        private String version;
        private String title;
        private String description;


        @Override
        public String toString() {
            return "ApiInfoConfig{" +
                    "version='" + version + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
