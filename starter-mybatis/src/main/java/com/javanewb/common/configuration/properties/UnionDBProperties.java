package com.javanewb.common.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * Description: com.javanewb.common.configuration.properties
 * </p>
 * <p>
 * </p>
 * date：2017/10/9
 *
 * @author Dean.Hwang
 */
@ConfigurationProperties("datasource.union")
public class UnionDBProperties extends DBProperties {
}
