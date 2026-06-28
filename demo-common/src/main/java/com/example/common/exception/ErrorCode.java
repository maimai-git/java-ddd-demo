package com.example.common.exception;

public enum ErrorCode {

    SUCCESS("SUCCESS", "成功"),

    // 业务异常
    SKILL_NOT_FOUND("SKILL_NOT_FOUND", "技能不存在"),
    SKILL_NAME_REQUIRED("SKILL_NAME_REQUIRED", "技能名称不能为空"),
    SKILL_CATEGORY_REQUIRED("SKILL_CATEGORY_REQUIRED", "技能分类不能为空"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "订单不存在"),
    ORDER_NO_REQUIRED("ORDER_NO_REQUIRED", "订单号不能为空"),
    ORDER_NO_DUPLICATE("ORDER_NO_DUPLICATE", "订单号重复"),
    ORDER_STATUS_INVALID("ORDER_STATUS_INVALID", "订单状态不合法"),
    CURRENCY_MISMATCH("CURRENCY_MISMATCH", "币种不匹配"),
    PROJECT_NAME_REQUIRED("PROJECT_NAME_REQUIRED", "项目名称不能为空"),
    PROJECT_NOT_FOUND("PROJECT_NOT_FOUND", "项目不存在"),
    INVALID_PARAM("INVALID_PARAM", "参数不合法"),

    // 系统异常
    SYS_ERROR("SYS_ERROR", "系统异常"),
    DB_ERROR("DB_ERROR", "数据库异常");

    private final String errCode;
    private final String errDesc;

    ErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public String getErrCode() { return errCode; }
    public String getErrDesc() { return errDesc; }
}
