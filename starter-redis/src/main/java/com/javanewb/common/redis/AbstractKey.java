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
 * date 16/12/7
 */
@Data
public abstract class AbstractKey {
    private String pdu;
    private String[] keys;

    public abstract String generatorKey(Object id);

}
