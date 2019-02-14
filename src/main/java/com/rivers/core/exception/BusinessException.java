package com.rivers.core.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String errcode = null;

    public BusinessException(String errorCode) {
        this.errcode = errorCode;
    }

    public BusinessException(String errorCode, String msg) {
        super(msg);
        this.errcode = errorCode;
    }

    public BusinessException(String errorCode, Throwable msg) {
        super(msg);
        this.errcode = errorCode;
    }

    public String getErrcode() {
        return this.errcode;
    }


}
