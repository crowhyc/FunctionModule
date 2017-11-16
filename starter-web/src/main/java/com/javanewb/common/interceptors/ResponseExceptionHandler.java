package com.javanewb.common.interceptors;

import com.javanewb.common.exception.BusinessException;
import com.javanewb.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Description: com.javanewb.common.interceptors
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 17/6/20
 */
@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {
    @Value("${info.active}")
    private String active;

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ErrorCode businessException(HttpServletRequest req, BusinessException e) {
        return e.getErrorCode();

    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorCode defaultExceptionHandler(HttpServletRequest req, Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            String errorMsg = methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage();
            return new ErrorCode(ErrorCode.getParamError().getCode(), errorMsg);
        }
        log.error(e.getMessage(), e);
        if (active.matches("^dev|test|local$")) {
            return ErrorCode.getInternalError(e);
        }
        return ErrorCode.getInternalError();
    }


}
