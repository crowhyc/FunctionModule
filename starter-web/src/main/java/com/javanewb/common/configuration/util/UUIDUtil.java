/**
 *
 */
package com.javanewb.common.configuration.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author Dean.Hwang
 */
public class UUIDUtil {
    private UUIDUtil() {

    }

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
