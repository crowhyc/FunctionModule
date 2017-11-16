package com.javanewb.common.response;


import com.javanewb.common.exception.ExistedStatusException;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Dean.Hwang
 * date: 2016/10/9 13:47
 */
public enum Status {

    OK(200, "message.isOk"),
    REDIRECT(302, "message.redirect"),
    VALIDATION_ERROR(1010, "message.validation"),
    FAIL(1011, "message.fail");


    private int code;
    private String messageKey;

    private static Map<Integer, Status> cachedMap = new HashMap<>();

    static {
        for (Status rc : Status.values()) {
            if (cachedMap.containsKey(rc.code)) {
                throw new ExistedStatusException("重复的状态码: " + rc.toString());
            }
            cachedMap.put(rc.getCode(), rc);
        }
    }

    Status(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public static Status getStatus(int code) {
        return valueOf(code);
    }

    public static Status valueOf(int code) {
        return cachedMap.get(code);
    }

    public String getMessageKey() {
        return messageKey;
    }

    public int getCode() {
        return code;
    }


    @Override
    public String toString() {
        return "Status:[code=" + code + ",messageKey=" + messageKey + "]";
    }

}
