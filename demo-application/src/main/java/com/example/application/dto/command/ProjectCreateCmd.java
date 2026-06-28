package com.example.application.dto.command;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ProjectCreateCmd {

    @NotBlank(message = "项目名称不能为空")
    private String name;
    private String description;
    private Settings settings;
    private List<Tag> tags;
    private List<Stage> stages;

    public static class Settings {
        private String visibility;
        private int priority;
        private String deadline;

        public String getVisibility() { return visibility; }
        public void setVisibility(String visibility) { this.visibility = visibility; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public String getDeadline() { return deadline; }
        public void setDeadline(String deadline) { this.deadline = deadline; }
    }

    public static class Tag {
        private String name;
        private String color;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
    }

    public static class Stage {
        private String name;
        private int displayOrder;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Settings getSettings() { return settings; }
    public void setSettings(Settings settings) { this.settings = settings; }
    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }
    public List<Stage> getStages() { return stages; }
    public void setStages(List<Stage> stages) { this.stages = stages; }
}
