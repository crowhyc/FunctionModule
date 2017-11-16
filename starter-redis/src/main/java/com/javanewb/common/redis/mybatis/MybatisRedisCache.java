package com.javanewb.common.redis.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.mybatis.caches.redis.RedisCallback;
import org.mybatis.caches.redis.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by Dean.Hwang on 16/12/11.
 */
@Slf4j
@Configuration
public class MybatisRedisCache implements Cache {
    @Autowired
    private MybatisRedisConfig mybatisRedisConfig;

    private final ReadWriteLock readWriteLock = new DummyReadWriteLock();
    private String id;
    private JedisPool pool;

    public MybatisRedisCache() {
    }

    public MybatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        } else {
            this.id = id;
        }
    }

    @PostConstruct
    public void init() {
        pool = new JedisPool(mybatisRedisConfig, mybatisRedisConfig.getHost());
    }

    private Object execute(RedisCallback callback) {

        Object var3;
        try (Jedis jedis = pool.getResource()) {
            var3 = callback.doWithRedis(jedis);
        }

        return var3;
    }

    public String getId() {
        return this.id;
    }

    public int getSize() {
        return (Integer) this.execute(jedis -> {
            Map result = jedis.hgetAll(MybatisRedisCache.this.id.getBytes());
            return result.size();
        });
    }

    public void putObject(final Object key, final Object value) {
        this.execute(jedis -> {
            jedis.hset(MybatisRedisCache.this.id.getBytes(), key.toString().getBytes(),
                    SerializeUtil.serialize(value));
            return null;
        });
    }

    public Object getObject(final Object key) {
        return this.execute(jedis -> SerializeUtil.unserialize(
                jedis.hget(MybatisRedisCache.this.id.getBytes(), key.toString().getBytes())));
    }

    public Object removeObject(final Object key) {
        return this.execute(jedis -> jedis.hdel(MybatisRedisCache.this.id, new String[]{key.toString()}));
    }

    public void clear() {
        this.execute(jedis -> {
            jedis.del(MybatisRedisCache.this.id);
            return null;
        });
    }

    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    public String toString() {
        return "Redis {" + this.id + "}";
    }
}
