package com.example.interfaces.config;

import com.example.application.common.Response;
import com.example.common.exception.BizException;
import com.example.common.exception.SysException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleValidation(MethodArgumentNotValidException e) {
        String detail = e.getBindingResult().getFieldErrors().stream().map(f -> f.getField() + ": " + f.getDefaultMessage()).collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", detail);
        return Response.buildFailure("VALIDATION_ERROR", detail);
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleBizException(BizException e) {
        log.warn("BizException: {} - {}", e.getErrCode(), e.getMessage());
        return Response.buildFailure(e.getErrCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleIllegalArgument(IllegalArgumentException e) {
        return Response.buildFailure("PARAM_ERROR", e.getMessage());
    }

    @ExceptionHandler(SysException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleSysException(SysException e) {
        log.error("SysException: {} - {}", e.getErrCode(), e.getMessage());
        return Response.buildFailure(e.getErrCode(), e.getMessage());
    }
}
