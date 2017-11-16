package com.javanewb.common.redis.operations;

import com.javanewb.common.redis.RedisKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * Description: redis
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 16/12/9
 */
public class CommonValueOperations<V> extends DefaultValueOperations<String, V> {
    public CommonValueOperations(RedisTemplate<String, V> template) {
        super(template);
    }

    public V get(RedisKey redisKey, Object id) {
        return super.get(redisKey.generatorKey(id));
    }

    public V get(RedisKey redisKey) {
        return get(redisKey, null);
    }

    public V getAndSet(RedisKey redisKey, Object id, V newValue) {
        return super.getAndSet(redisKey.generatorKey(id), newValue);
    }

    public V getAndSet(RedisKey redisKey, V newValue) {
        return getAndSet(redisKey, null, newValue);
    }

    public Long increment(RedisKey redisKey, Object id, long delta) {
        return super.increment(redisKey.generatorKey(id), delta);
    }

    public Long increment(RedisKey redisKey, long delta) {
        return increment(redisKey, null, delta);
    }

    public Double increment(RedisKey redisKey, Object id, double delta) {
        return super.increment(redisKey.generatorKey(id), delta);
    }

    public Double increment(RedisKey redisKey, double delta) {
        return increment(redisKey, null, delta);
    }

    public Integer append(RedisKey redisKey, Object id, String value) {
        return super.append(redisKey.generatorKey(id), value);
    }

    public Integer append(RedisKey redisKey, String value) {
        return append(redisKey, null, value);
    }

    public String get(RedisKey redisKey, Object id, long start, long end) {
        return super.get(redisKey.generatorKey(id), start, end);
    }

    public String get(RedisKey redisKey, long start, long end) {
        return get(redisKey, null, start, end);
    }

    public List<V> multiGet(Map<RedisKey, Object> keys) {
        if (keys.size() == 0) {
            return Collections.emptyList();
        }
        return super.multiGet(makeKeyMapToList(keys));

    }

    public List<V> multiGetWithoutVal(Collection<RedisKey> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        Map<RedisKey, Object> retMap = new HashMap<>();
        for (RedisKey redisKey : keys) {
            retMap.put(redisKey, null);
        }
        return multiGet(retMap);

    }

    public void multiSet(Map<RedisKey, Object> keysMap, Collection<V> m) {
        List<String> keys = makeKeyMapToList(keysMap);
        super.multiSet(makeKeyValToMap((keys), m));
    }

    public void multiSet(Collection<RedisKey> keys, Collection<V> m) {
        Map<RedisKey, Object> retMap = new HashMap<>();
        for (RedisKey key : keys) {
            retMap.put(key, null);
        }
        multiSet(retMap, m);
    }

    public Boolean multiSetIfAbsent(Map<RedisKey, Object> keysMap, Collection<V> m) {
        List<String> keys = makeKeyMapToList(keysMap);
        return super.multiSetIfAbsent(makeKeyValToMap((keys), m));
    }

    public Boolean multiSetIfAbsent(Collection<RedisKey> keys, Collection<V> m) {
        Map<RedisKey, Object> retMap = new HashMap<>();
        for (RedisKey key : keys) {
            retMap.put(key, null);
        }
        return multiSetIfAbsent(retMap, m);
    }

    public void set(RedisKey redisKey, Object id, V value) {

        if (redisKey.getExpiredTime() > 0) {
            super.set(redisKey.generatorKey(id), value, redisKey.getExpiredTime(), TimeUnit.SECONDS);
        } else {
            super.set(redisKey.generatorKey(id), value);
        }
    }

    public void set(RedisKey redisKey, V value) {
        set(redisKey, null, value);
    }

    public Boolean setIfAbsent(RedisKey redisKey, Object id, V value) {
        return super.setIfAbsent(redisKey.generatorKey(id), value);
    }

    public Boolean setIfAbsent(RedisKey redisKey, V value) {
        return setIfAbsent(redisKey.generatorKey(null), value);
    }

    public void set(RedisKey redisKey, Object id, V value, long offset) {
        super.set(redisKey.generatorKey(id), value, offset);
    }

    public void set(RedisKey redisKey, V value, long offset) {
        set(redisKey, null, value, offset);
    }

    public Long size(RedisKey redisKey, Object id) {
        return super.size(redisKey.generatorKey(id));
    }

    public Long size(RedisKey redisKey) {
        return size(redisKey, null);
    }

    public Boolean setBit(RedisKey redisKey, Object id, long offset, boolean value) {
        return super.setBit(redisKey.generatorKey(id), offset, value);
    }

    public Boolean setBit(RedisKey redisKey, long offset, boolean value) {
        return setBit(redisKey, null, offset, value);
    }

    public Boolean getBit(RedisKey redisKey, Object id, long offset) {
        return super.getBit(redisKey.generatorKey(id), offset);
    }

    public Boolean getBit(RedisKey redisKey, long offset) {
        return getBit(redisKey, null, offset);
    }

    /**
     * 设置同步锁，默认过期时间10s避免死锁
     *
     * @param key
     * @return
     */
    public Boolean setnx(String key) {
        boolean flag = execute(connection ->
                        connection.setNX(key.getBytes(), String.valueOf(System.currentTimeMillis()).getBytes())
                , true);
        if (flag) {
            execute(connection ->
                            connection.expire(key.getBytes(), 10L)
                    , true);
        }
        return flag;
    }


    public Long del(RedisKey redisKey, Object id) {
        if (id == null) {
            return 0L;
        }
        if (redisKey == null) {
            return 0L;
        }
        try {
            return del(redisKey.generatorKey(id).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public Long del(byte[] ids) {
        if (ids.length == 0) {
            return 0L;
        }
        return execute(connection -> connection.del(ids), true);
    }

    private List<String> makeKeyMapToList(Map<RedisKey, Object> keys) {
        return keys.entrySet().parallelStream().map(redisKeyObjectEntry -> redisKeyObjectEntry.getKey().generatorKey(redisKeyObjectEntry.getValue())
        ).collect(Collectors.toList());
    }

    private Map<String, V> makeKeyValToMap(List<String> keys, Collection<V> vals) {
        Assert.isTrue(keys.size() == vals.size(), "Tow Collection Size Must Equals");
        int counter = 0;
        Map<String, V> retMap = new HashMap<>();
        for (V val : vals) {
            retMap.put(keys.get(counter), val);
            counter++;
        }
        return retMap;
    }


}


