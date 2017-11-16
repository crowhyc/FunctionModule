package com.javanewb.common.configuration.util;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Title: BeanUtil
 * </p>
 * <p>
 * Description: com.javanewb.common.util
 * </p>
 *
 * @author Dean.Hwang
 * date 2017/6/26 下午4:57
 */
public class BeanUtil {

    private static Field[] getAllFields(Class clzz, Field[] fields) {
        if (clzz != Object.class) {
            Field[] newFields = ArrayUtils.addAll(fields,
                    clzz.getDeclaredFields());
            return getAllFields(clzz.getSuperclass(), newFields);
        }
        return fields;
    }

    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return null;
        }
        if (Map.class.isAssignableFrom(bean.getClass())) {
            return (Map<String, Object>) bean;
        }
        Field[] fields = getAllFields(bean.getClass(), new Field[0]);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers())) {
                    map.put(field.getName(), field.get(bean));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
