package com.example.application.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private boolean success;
    private String errCode;
    private String errMessage;

    public static Response buildSuccess() {
        Response r = new Response();
        r.success = true;
        return r;
    }

    public static Response buildFailure(String errCode, String errMessage) {
        Response r = new Response();
        r.success = false;
        r.errCode = errCode;
        r.errMessage = errMessage;
        return r;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getErrCode() { return errCode; }
    public void setErrCode(String errCode) { this.errCode = errCode; }
    public String getErrMessage() { return errMessage; }
    public void setErrMessage(String errMessage) { this.errMessage = errMessage; }
}
