package com.javanewb.common.http.exceptions;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * Description: com.javanewb.common.exceptions
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 17/6/13
 */
@Data
@ToString
public class HttpErrorInfo implements Serializable {
    /**
     * Http Code
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String errorInfo;
    /**
     * 请求的url
     */
    private String url;
    /**
     * 请求参数的json
     */
    private String params;

    /**
     * 请求类型：GET POST PUT DELETE
     */
    private String type;

    public HttpErrorInfo(Integer code, String errorInfo, String url, String params, String type) {
        this.code = code;
        this.errorInfo = errorInfo;
        this.url = url;
        this.params = params;
        this.type = type;
    }

}
