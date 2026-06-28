package com.example.common.exception;

public class SysException extends RuntimeException {

    private final String errCode;

    public SysException(ErrorCode errorCode) {
        super(errorCode.getErrDesc());
        this.errCode = errorCode.getErrCode();
    }

    public SysException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrDesc(), cause);
        this.errCode = errorCode.getErrCode();
    }

    public String getErrCode() { return errCode; }
}
