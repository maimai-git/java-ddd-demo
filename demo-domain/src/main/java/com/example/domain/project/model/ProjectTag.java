package com.example.domain.project.model;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;

public record ProjectTag(String name, String color) {
    public ProjectTag {
        if (name == null || name.isBlank()) {
            throw new BizException(ErrorCode.INVALID_PARAM, "tag name must not be blank");
        }
    }
}
