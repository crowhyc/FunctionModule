package com.javanewb.common.configuration.properties;

import lombok.Data;

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
@Data
public class DBProperties {
    String driverClassName;
    String url;
    String username;
    String password;
    Integer initialSize;
    Integer maxTotal;
    Integer maxIdle;
    Integer minIdle;
    String validationQuery;
    Boolean testOnBorrow;
    Boolean testWhileIdle;
    Long maxWait;
    Boolean removeAbandonedOnBorrow;
    Integer removeAbandonedTimeout;
    Long timeBetweenEvictionRunsMillis;
}
