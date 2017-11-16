package com.javanewb.common.http.exceptions;

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
public class HttpFailedException extends RuntimeException implements Serializable {
    private final HttpErrorInfo httpErrorInfo;

    public HttpFailedException(HttpErrorInfo info, Exception e) {
        super(info.toString(), e);
        this.httpErrorInfo = info;
    }

    public HttpFailedException(HttpErrorInfo httpErrorInfo) {
        super(httpErrorInfo.toString());
        this.httpErrorInfo = httpErrorInfo;
    }

    public HttpErrorInfo getHttpErrorInfo() {
        return httpErrorInfo;
    }

}
