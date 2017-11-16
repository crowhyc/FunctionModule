package com.javanewb.common.configuration;

import com.javanewb.common.redis.RedisKeyFactory;
import com.javanewb.common.redis.operations.CommonRedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Dean.Huwang
 * date: 2016/10/11 15:05
 */
@EnableRedisRepositories
@Configuration
@RefreshScope
public class RedisConfig {
    @Value("${info.appName}")
    private String pdu;

    @Bean
    public RedisKeyFactory redisKeyFactory() {
        return new RedisKeyFactory(pdu);
    }

    @Bean
    public <V> RedisTemplate<String, V> redisTemplate(
            JedisConnectionFactory jedisConnectionFactory) {
        jedisConnectionFactory.afterPropertiesSet();
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setValueSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }


    @Bean(name = "commonRedisTemplate")
    public <V> CommonRedisTemplate<V> commonRedisTemplate(
            JedisConnectionFactory jedisConnectionFactory) {
        jedisConnectionFactory.afterPropertiesSet();
        CommonRedisTemplate<V> template = new CommonRedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setValueSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
