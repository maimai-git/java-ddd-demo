package com.example.application.dto.command;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public class SendNotificationCmd {

    @NotBlank(message = "模板ID不能为空")
    private String templateId;
    @NotBlank(message = "接收人不能为空")
    private String recipient;
    private Map<String, String> params;

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public Map<String, String> getParams() { return params; }
    public void setParams(Map<String, String> params) { this.params = params; }
}
