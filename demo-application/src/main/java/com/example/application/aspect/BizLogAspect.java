package com.example.application.aspect;

import com.example.common.log.BizLog;
import com.example.common.trace.TraceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BizLogAspect {

    private static final Logger log = LoggerFactory.getLogger(BizLogAspect.class);

    @Around("@annotation(bizLog)")
    public Object around(ProceedingJoinPoint joinPoint, BizLog bizLog) throws Throwable {
        String action = bizLog.value();
        log.info("[{}] start, traceId={}", action, TraceContext.getTraceId());
        try {
            Object result = joinPoint.proceed();
            log.info("[{}] success, traceId={}", action, TraceContext.getTraceId());
            return result;
        } catch (Throwable e) {
            log.error("[{}] failed: {}", action, e.getMessage());
            throw e;
        }
    }
}
