package com.javanewb.common.redis;

import lombok.Data;

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
@Data
public class RedisKeyFactory {
    private String pdu;

    public RedisKeyFactory(String pdu) {
        this.pdu = pdu;
    }

    public RedisKey getRedisKey(int expiredTime, String... keys) {
        return new RedisKey(pdu, expiredTime, keys);
    }

    public RedisKey getRedisKey(String... keys) {
        return new RedisKey(pdu, keys);
    }

}
