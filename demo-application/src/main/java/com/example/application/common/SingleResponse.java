package com.example.application.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleResponse<T> extends Response {

    private T data;

    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> r = new SingleResponse<>();
        r.setSuccess(true);
        r.data = data;
        return r;
    }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
