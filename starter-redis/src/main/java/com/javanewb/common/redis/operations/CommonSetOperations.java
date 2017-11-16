package com.javanewb.common.redis.operations;

import com.javanewb.common.redis.RedisKey;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * <p>
 * Description: redis.operations
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 16/12/27
 */
public class CommonSetOperations<V> extends DefaultSetOperations<String, V> {


    public CommonSetOperations(RedisTemplate<String, V> template) {
        super(template);
    }

    public Long add(RedisKey key, Object id, V... values) {
        return super.add(key.generatorKey(id), values);
    }

    public V pop(RedisKey key, Object id) {
        return super.pop(key.generatorKey(id));
    }

    public Set<V> members(RedisKey key, Object id) {
        return super.members(key.generatorKey(id));
    }

    public Long size(RedisKey key, Object id) {
        return super.size(key.generatorKey(id));
    }

    public long remove(RedisKey key, Object id, Object... values) {
        return super.remove(key.generatorKey(id), values);
    }
}
