package com.example.application.dto.command;

import jakarta.validation.constraints.NotBlank;

public class SkillChangeNameCmd {

    @NotBlank(message = "名称不能为空")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
