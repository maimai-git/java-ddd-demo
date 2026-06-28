package com.example.infrastructure.tracing;

import com.example.common.trace.TraceContext;
import com.example.domain.shared.external.TracingGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class TracingReporter implements TracingGateway {

    private static final Logger log = LoggerFactory.getLogger(TracingReporter.class);

    @Override
    public String startSpan(String operationName) {
        String traceId = TraceContext.getTraceId();
        MDC.put("traceId", traceId);
        log.info("Tracing start: op={}, traceId={}", operationName, traceId);
        return traceId;
    }

    @Override
    public void endSpan() {
        MDC.remove("traceId");
    }
}
