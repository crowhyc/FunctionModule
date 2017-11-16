package com.javanewb.common.redis.operations;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * Description: com.javanewb.common.redis.operations
 * </p>
 * <p>
 * </p>
 * date：2017/10/9
 *
 * @author Dean.Hwang
 */
public class CommonRedisTemplate<V> extends RedisTemplate<String, V> {
    private CommonValueOperations<V> commonValueOperations;
    private CommonSetOperations<V> commonSetOperations;

    public CommonValueOperations<V> opsCommonValue() {
        if (commonValueOperations == null) {
            return new CommonValueOperations<>(this);
        }
        return commonValueOperations;
    }

    public CommonSetOperations<V> opsCommonSet() {
        if (commonSetOperations == null) {
            return new CommonSetOperations<>(this);
        }
        return commonSetOperations;
    }
}
