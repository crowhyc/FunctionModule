package com.javanewb.common.configuration.util;

import com.javanewb.common.exception.BusinessException;
import com.javanewb.common.exception.ErrorCode;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Collection;

/**
 * <p>
 * Title: AssertUtil
 * </p>
 * <p>
 * Description: com.javanewb.common.util
 * </p>
 *
 * @author Dean.Hwang
 * date 2017/6/23 下午1:07
 */
public class AssertUtil {

    public static void assertNotNull(Object obj, ErrorCode errorCode) {
        if (obj == null) {
            throw new BusinessException(errorCode);
        }
    }

    public static void assertNotNull(Object obj, ErrorCode errorCode, String errorMsg)
            throws BusinessException {
        if (obj == null) {
            throw new BusinessException(errorCode, errorMsg);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void assertNotEmpty(Collection obj, ErrorCode errorCode, String errorMsg) {
        if (CollectionUtils.isEmpty(obj)) {
            throw new BusinessException(errorCode, errorMsg);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void assertNotEmpty(Collection obj, ErrorCode errorCode) {
        assertNotEmpty(obj, errorCode, errorCode.getMsg());
    }

    public static void assertNotEmpty(Object[] objects, ErrorCode errorCode, String errorMsg) {
        if (ArrayUtils.isEmpty(objects)) {
            throw new BusinessException(errorCode, errorMsg);
        }
    }

    public static void assertEqual(int int1, int in2, ErrorCode errorCode, String errorMsg,
                                   Object... args) {
        if (int1 != in2) {
            String msg = MessageFormat.format(errorMsg, args);
            throw new BusinessException(errorCode, msg);
        }
    }

    public static void assertEqual(int int1, int in2, ErrorCode errorCode) {
        if (int1 != in2) {
            throw new BusinessException(errorCode);
        }
    }

    public static void assertNotEqual(int int1, int in2, ErrorCode errorCode) {
        if (int1 == in2) {
            throw new BusinessException(errorCode);
        }
    }

    public static void assertNotEqual(int int1, int in2, ErrorCode errorCode, String errorMsg) {
        if (int1 == in2) {
            throw new BusinessException(errorCode, errorMsg);
        }
    }

    public static void assertNotEqual(long int1, long in2, ErrorCode errorCode, String errorMsg) {
        if (int1 == in2) {
            throw new BusinessException(errorCode, errorMsg);
        }
    }

    public static void assertEqual(long int1, long in2, ErrorCode errorCode, String errorMsg) {
        if (int1 != in2) {
            throw new BusinessException(errorCode, errorMsg);
        }
    }

    public static void assertNull(Object obj) throws BusinessException {
        assertNull(obj, ErrorCode.getParamError());
    }

    public static void assertNull(Object obj, ErrorCode errorCode) throws BusinessException {
        if (obj != null) {
            throw new BusinessException(errorCode);
        }
    }

    public static void assertNotEqual(Integer obj1, Integer obj2, ErrorCode errorCode)
            throws BusinessException {
        if (obj1 == null || obj2 == null) {
            return;
        }
        if (obj1.intValue() == obj2.intValue()) {
            throw new BusinessException(errorCode);
        }
    }

    public static void assertTrue(boolean condition, ErrorCode errorCode) throws BusinessException {
        if (!condition) {
            throw new BusinessException(errorCode);
        }
    }

    public static void assertTrue(boolean condition, ErrorCode errorCode, String errorMsg)
            throws BusinessException {
        if (!condition) {
            throw new BusinessException(errorCode, errorMsg);
        }
    }

    public static void assertFalse(boolean condition, ErrorCode errorCode) throws BusinessException {
        if (condition) {
            throw new BusinessException(errorCode);
        }
    }

    public static void assertFalse(boolean condition, ErrorCode errorCode, String errorMsg)
            throws BusinessException {
        if (condition) {
            throw new BusinessException(errorCode, errorMsg);
        }
    }
}
