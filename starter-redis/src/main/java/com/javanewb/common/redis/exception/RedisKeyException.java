package com.javanewb.common.redis.exception;

/**
 * <p>
 * Description: com.javanewb.common.exceptions
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 17/6/19
 */
public class RedisKeyException extends RuntimeException {

    public RedisKeyException(String message) {
        super(message);
    }
}
