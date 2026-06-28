package com.example.domain.shared.external;

public interface TracingGateway {

    String startSpan(String operationName);

    void endSpan();
}
