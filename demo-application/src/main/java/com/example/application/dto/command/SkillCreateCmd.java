package com.example.application.dto.command;

import jakarta.validation.constraints.NotBlank;

public class SkillCreateCmd {

    @NotBlank(message = "技能名称不能为空")
    private String name;
    @NotBlank(message = "技能分类不能为空")
    private String category;
    private String level;
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
