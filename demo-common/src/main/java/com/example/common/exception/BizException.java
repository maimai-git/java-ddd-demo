package com.example.common.exception;

public class BizException extends RuntimeException {

    private final String errCode;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getErrDesc());
        this.errCode = errorCode.getErrCode();
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.errCode = errorCode.getErrCode();
    }

    public String getErrCode() { return errCode; }
}
