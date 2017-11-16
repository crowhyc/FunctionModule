/*
 * Copyright 2011-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javanewb.common.redis.operations;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation of {@link ValueOperations}.
 *
 * @author Costin Leau
 * @author Jennifer Hickey
 * @author Christoph Strobl
 */
class DefaultValueOperations<K, V> extends AbstractOperations<K, V> implements ValueOperations<K, V> {

    DefaultValueOperations(RedisTemplate<K, V> template) {
        super(template);
    }


    public V get(final Object key) {

        return execute(new ValueDeserializingRedisCallback(key) {

            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.get(rawKey);
            }
        }, true);
    }

    public V getAndSet(K key, V newValue) {
        final byte[] rawValue = rawValue(newValue);
        return execute(new ValueDeserializingRedisCallback(key) {

            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.getSet(rawKey, rawValue);
            }
        }, true);
    }

    public Long increment(K key, final long delta) {
        final byte[] rawKey = rawKey(key);
        return execute(connection -> connection.incrBy(rawKey, delta), true);
    }

    public Double increment(K key, final double delta) {
        final byte[] rawKey = rawKey(key);
        return execute(connection -> connection.incrBy(rawKey, delta), true);
    }

    public Integer append(K key, String value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawString = rawString(value);

        return execute(connection -> {
            final Long result = connection.append(rawKey, rawString);
            return (result != null) ? result.intValue() : null;
        }, true);
    }

    public String get(K key, final long start, final long end) {
        final byte[] rawKey = rawKey(key);

        byte[] rawReturn = execute(connection -> connection.getRange(rawKey, start, end), true);

        return deserializeString(rawReturn);
    }

    public List<V> multiGet(Collection<K> keys) {
        if (keys.isEmpty()) {
            return Collections.emptyList();
        }

        final byte[][] rawKeys = new byte[keys.size()][];

        int counter = 0;
        for (K hashKey : keys) {
            rawKeys[counter++] = rawKey(hashKey);
        }

        List<byte[]> rawValues = execute(connection -> connection.mGet(rawKeys), true);

        return deserializeValues(rawValues);
    }

    public void multiSet(Map<? extends K, ? extends V> m) {
        if (m.isEmpty()) {
            return;
        }

        final Map<byte[], byte[]> rawKeys = new LinkedHashMap<>(m.size());

        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            rawKeys.put(rawKey(entry.getKey()), rawValue(entry.getValue()));
        }

        execute(connection -> {
            connection.mSet(rawKeys);
            return null;
        }, true);
    }

    public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> m) {
        if (m.isEmpty()) {
            return true;
        }

        final Map<byte[], byte[]> rawKeys = new LinkedHashMap<>(m.size());

        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            rawKeys.put(rawKey(entry.getKey()), rawValue(entry.getValue()));
        }

        return execute(connection -> connection.mSetNX(rawKeys), true);
    }

    public void set(K key, V value) {
        final byte[] rawValue = rawValue(value);
        execute(new ValueDeserializingRedisCallback(key) {

            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                connection.set(rawKey, rawValue);
                return new byte[]{};
            }
        }, true);
    }

    public void setex(K key, V value, Long expireTime) {
        final byte[] rawValue = rawValue(value);
        execute(new ValueDeserializingRedisCallback(key) {

            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                connection.setEx(rawKey, expireTime, rawValue);
                return new byte[]{};
            }
        }, true);
    }

    public void set(K key, V value, final long timeout, final TimeUnit unit) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);

        execute(new RedisCallback<Object>() {

            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                potentiallyUsePsetEx(connection);
                return null;
            }

            public void potentiallyUsePsetEx(RedisConnection connection) {

                if (!TimeUnit.MILLISECONDS.equals(unit) || !failsafeInvokePsetEx(connection)) {
                    connection.setEx(rawKey, TimeoutUtils.toSeconds(timeout, unit), rawValue);
                }
            }

            private boolean failsafeInvokePsetEx(RedisConnection connection) {

                boolean failed = false;
                try {
                    connection.pSetEx(rawKey, timeout, rawValue);
                } catch (UnsupportedOperationException e) {
                    // in case the connection does not support pSetEx return false to allow fallback to other operation.
                    failed = true;
                }
                return !failed;
            }

        }, true);
    }

    public Boolean setIfAbsent(K key, V value) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);

        return execute(connection -> connection.setNX(rawKey, rawValue), true);
    }

    public void set(K key, final V value, final long offset) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawValue = rawValue(value);

        execute(connection -> {
            connection.setRange(rawKey, rawValue, offset);
            return null;
        }, true);
    }

    public Long size(K key) {
        final byte[] rawKey = rawKey(key);

        return execute(connection -> connection.strLen(rawKey), true);
    }

    @Override
    public Boolean setBit(K key, final long offset, final boolean value) {

        final byte[] rawKey = rawKey(key);
        return execute(connection -> connection.setBit(rawKey, offset, value), true);
    }

    @Override
    public Boolean getBit(K key, final long offset) {

        final byte[] rawKey = rawKey(key);
        return execute(connection -> connection.getBit(rawKey, offset), true);
    }

}
