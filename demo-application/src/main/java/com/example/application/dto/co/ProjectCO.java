package com.example.application.dto.co;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Client Object — 对外暴露的项目数据。零领域依赖。
 */
public class ProjectCO {

    private Long id;
    private String name;
    private String description;
    private String status;
    private Settings settings;
    private List<Tag> tags;
    private List<Stage> stages;
    private Instant createdAt;
    private Instant updatedAt;

    public static class Settings {
        private String visibility;
        private int priority;
        private LocalDate deadline;

        public String getVisibility() { return visibility; }
        public void setVisibility(String visibility) { this.visibility = visibility; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public LocalDate getDeadline() { return deadline; }
        public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
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
        private Long id;
        private String name;
        private int displayOrder;
        private String status;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Settings getSettings() { return settings; }
    public void setSettings(Settings settings) { this.settings = settings; }
    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }
    public List<Stage> getStages() { return stages; }
    public void setStages(List<Stage> stages) { this.stages = stages; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
