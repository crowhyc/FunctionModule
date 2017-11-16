package com.javanewb.common.exception;


/**
 * <p>
 * Title: BusinessException
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 2016年11月3日
 */
public class BusinessException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3216184101082900979L;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;
    /**
     * 消息参数
     */
    private final String errorMsg;


    public BusinessException(ErrorCode errorCode, String errorMsg, Exception e) {
        super(errorMsg, e);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMsg(), e);
        this.errorCode = errorCode;
        this.errorMsg = errorCode.getMsg();
    }

    public BusinessException(ErrorCode errorCode, String errorMsg) {
        super(errorCode.toString() + "," + errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
        this.errorMsg = errorCode.getMsg();
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        this.errorCode = new ErrorCode(errorCode, errorMsg);
        this.errorMsg = errorMsg;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }


    public String getErrorMsg() {
        return errorMsg;
    }


    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
