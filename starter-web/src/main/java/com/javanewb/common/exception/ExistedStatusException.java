package com.javanewb.common.exception;

/**
 * @author Dean.Hwang
 * date: 16/3/24 17:45
 */
public class ExistedStatusException extends RuntimeException {

    public ExistedStatusException(String message) {
        super(message);
    }
}
