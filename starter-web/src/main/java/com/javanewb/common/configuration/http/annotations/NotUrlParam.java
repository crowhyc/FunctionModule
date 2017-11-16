package com.javanewb.common.configuration.http.annotations;

import java.lang.annotation.*;

/**
 * 标识性注解。 所标识的字段，不参与url链接参数拼装
 * <p>
 * Title: NotUrlParam
 * </p>
 *
 * @author Dean.Hwang
 * date 2016年8月10日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotUrlParam {

}
