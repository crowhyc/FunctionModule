package com.javanewb.common.redis;

import com.javanewb.common.redis.exception.RedisKeyException;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Description: redis
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 16/12/7
 */
public class RedisKey extends AbstractKey {
    private final static String PREFIX = ":";
    private int expiredTime;
    /**
     * cache forever
     */
    private final int CACHE_EXPIRE_TIME_FOREVER = 0;
    /**
     * redis public cache expire time : 2 hours(60*60*2)
     */
    private static final int CACHE_DEFAULT_EXPIRE_TIME = 60 * 60 * 2;
    /**
     * 20分钟缓存
     */
    private static final int CACHE_EXPIRE_TIME_20MINUTES = 60 * 20;
    /**
     * 15分钟缓存
     */
    private static final int CACHE_EXPIRE_TIME_15MINUTES = 60 * 15;

    /**
     * 10分钟缓存
     */
    private static final int CACHE_EXPIRE_TIME_10MINUTES = 60 * 10;
    /**
     * 5分钟缓存
     */
    private static final int CACHE_EXPIRE_TIME_5MINUTES = 60 * 5;
    /**
     * 1分钟缓存
     */
    private static final int CACHE_EXPIRE_TIME_1MINUTES = 60;


    RedisKey(String pdu, int expiredTime, String... keys) {
        if (keys.length == 0) {
            throw new RedisKeyException("Redis Key 生成失败 子key不能为空");
        }
        setPdu(pdu);
        this.expiredTime = expiredTime;

        setKeys(keys);
    }

    RedisKey(String pdu, String[] keys) {
        if (keys.length == 0) {
            throw new RedisKeyException("Redis Key 生成失败 子key不能为空");
        }
        setPdu(pdu);
        this.expiredTime = CACHE_EXPIRE_TIME_FOREVER;
        setKeys(keys);
    }

    @Override
    public String generatorKey(Object id) {
        List<String> keyList = Arrays.asList(getKeys());
        final Optional<String> makeKey = keyList.stream().reduce(((a, b) -> a + PREFIX + b));
        final String keyStr = StringUtils.isEmpty(id) ? "" : PREFIX;
        return makeKey.map(s -> getPdu() + PREFIX + s + keyStr + id).orElse("");
    }

    public int getExpiredTime() {
        return expiredTime;
    }
}
