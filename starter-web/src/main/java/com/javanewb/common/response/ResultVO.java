package com.javanewb.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Dean.Hwang
 * date: 16/3/24 17:38
 */
@ApiModel(value = "API接口返回对象", description = "API接口返回对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -5587859954446690142L;
    public static final String UN_FIELD_ERRORS = "un_field_errors";
    public static final String UN_BUSINESS_OPERATION_ERRORS = "business_operation_errors";
    public static final String INTERNAL_SERVER_ERROR = "internal_server_error";
    public static final String VALIDATE_ERROR = "validate_error";

    public static final ResultVO OK = new ResultVO();
    public static final ResultVO FAIL = new ResultVO();
    public static final ResultVO INVALID = new ResultVO();
    public static final ResultVO REDIRECT = new ResultVO();

    static {
        OK.status(Status.OK);
        REDIRECT.status(Status.REDIRECT);
        FAIL.status(Status.FAIL);
        INVALID.status(Status.VALIDATION_ERROR);
    }

    @ApiModelProperty(value = "时间戳", required = true, dataType = "long")
    private long timestamp;

    @ApiModelProperty(value = "返回状态码", required = true, dataType = "long",
            example = "1000 : message.isOk ; 2001 : message.validation ; 3001 : message.fail ")
    private int code;

    @ApiModelProperty(value = "Response返回描述", required = true, dataType = "string",
            example = "1000 : message.isOk ; 2001 : message.validation ; 3001 : message.fail")
    private String msg;

    @ApiModelProperty(value = "ResultVO msgId", required = true, dataType = "string",
            example = "请求返回的唯一消息ID")
    protected String msgId;

    @ApiModelProperty(value = "ResultVO 返回的数据", required = true, dataType = "string",
            example = "响应数据")
    protected T data;

    public static <E> ResultVO<E> isOk() {
        return (ResultVO<E>) OK;
    }

    public static <E> ResultVO<E> isRedirect() {
        return (ResultVO<E>) REDIRECT;
    }

    public static <E> ResultVO<E> isFailed() {
        return (ResultVO<E>) FAIL;
    }

    public static <E> ResultVO<E> isInvalid() {
        return (ResultVO<E>) INVALID;
    }

    public <E> ResultVO<E> build() {
        this.setTimestamp(System.currentTimeMillis());
        return (ResultVO<E>) this;
    }


    public void status(Status status) {
        this.code = status.getCode();
        this.msg = message(status);
    }


    private static String message(Status status) {
        return "status:" + status.getCode() + ":" + status.getMessageKey();
    }


}
