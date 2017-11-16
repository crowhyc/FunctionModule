package com.javanewb.common.redis.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Dean.Hwang on 16/12/12.
 */
@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class MybatisRedisConfig extends JedisPoolConfig {
    private String host = "localhost";
    private int port = 6379;
    private int connectionTimeout = 2000;
    private int soTimeout = 2000;
    private String password;
    private int database = 0;
    private String clientName;
}

