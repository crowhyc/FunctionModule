package com.javanewb.common.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * Description: com.javanewb.common.autoconfigure
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 17/2/23
 */
@ConfigurationProperties("datasource.master")
public class MasterDBProperties extends DBProperties {

}
