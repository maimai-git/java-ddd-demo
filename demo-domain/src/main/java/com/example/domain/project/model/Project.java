package com.example.domain.project.model;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.domain.project.event.ProjectStatusChangedEvent;
import com.example.domain.shared.model.BaseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project extends BaseEntity {

    private String name;
    private String description;
    private Status status;
    private ProjectSettings settings;
    private List<ProjectTag> tags = new ArrayList<>();
    private List<ProjectStage> stages = new ArrayList<>();

    public enum Status { DRAFT, ACTIVE, PAUSED, COMPLETED, CANCELLED }

    public static Project initiate(String name, String description, ProjectSettings settings, List<ProjectTag> tags) {
        return new Project(name, description, settings, tags);
    }

    /** 从持久层重建 */
    public static Project reconstitute(Long id, String name, String description, Status status,
                                       ProjectSettings settings, List<ProjectTag> tags,
                                       Instant createdAt, Instant updatedAt) {
        Project project = new Project(name, description, settings, tags);
        project.setId(id);
        project.status = status;
        project.setCreatedAt(createdAt);
        project.setUpdatedAt(updatedAt);
        return project;
    }

    private Project(String name, String description, ProjectSettings settings, List<ProjectTag> tags) {
        if (name == null || name.isBlank()) {
            throw new BizException(ErrorCode.PROJECT_NAME_REQUIRED);
        }
        this.name = name;
        this.description = description;
        this.status = Status.DRAFT;
        this.settings = settings;
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        var now = Instant.now();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    public void addStage(ProjectStage stage) { if (stage != null) this.stages.add(stage); }

    public void updateProfile(ProjectProfile profile) {
        if (profile.name() != null && !profile.name().isBlank()) this.name = profile.name();
        if (profile.description() != null) this.description = profile.description();
        if (profile.settings() != null) this.settings = profile.settings();
        if (profile.tags() != null) this.tags = new ArrayList<>(profile.tags());
        setUpdatedAt(Instant.now());
    }

    public void activate() {
        if (this.status == Status.COMPLETED) {
            throw new BizException(ErrorCode.INVALID_PARAM, "已完成的项目无法重新激活");
        }
        this.status = Status.ACTIVE;
        setUpdatedAt(Instant.now());
        registerEvent(new ProjectStatusChangedEvent(getId(), this.name, Status.ACTIVE));
    }

    public void complete() {
        if (this.status != Status.ACTIVE) {
            throw new BizException(ErrorCode.INVALID_PARAM, "只有进行中的项目才能标记完成");
        }
        this.status = Status.COMPLETED;
        setUpdatedAt(Instant.now());
        registerEvent(new ProjectStatusChangedEvent(getId(), this.name, Status.COMPLETED));
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public ProjectSettings getSettings() { return settings; }
    public List<ProjectTag> getTags() { return Collections.unmodifiableList(tags); }
    public List<ProjectStage> getStages() { return Collections.unmodifiableList(stages); }
}
