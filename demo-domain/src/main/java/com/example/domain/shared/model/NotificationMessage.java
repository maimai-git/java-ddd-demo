package com.example.domain.shared.model;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;

import java.util.Map;

public record NotificationMessage(
    String templateId,
    String recipient,
    Map<String, String> params
) {
    public NotificationMessage {
        if (templateId == null || templateId.isBlank()) {
            throw new BizException(ErrorCode.INVALID_PARAM, "templateId must not be blank");
        }
        if (recipient == null || recipient.isBlank()) {
            throw new BizException(ErrorCode.INVALID_PARAM, "recipient must not be blank");
        }
        params = params != null ? Map.copyOf(params) : Map.of();
    }
}
