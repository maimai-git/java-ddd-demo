package com.example.domain.project.model;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;

import java.time.Instant;

public class ProjectStage {

    private Long id;
    private String name;
    private int displayOrder;
    private StageStatus status;

    public static ProjectStage of(String name, int displayOrder) {
        return new ProjectStage(name, displayOrder);
    }

    /** 从持久层重建 */
    public static ProjectStage reconstitute(Long id, String name, int displayOrder, StageStatus status) {
        ProjectStage stage = new ProjectStage(name, displayOrder);
        stage.id = id;
        stage.status = status;
        return stage;
    }

    private ProjectStage(String name, int displayOrder) {
        if (name == null || name.isBlank()) {
            throw new BizException(ErrorCode.INVALID_PARAM, "stage name must not be blank");
        }
        this.name = name;
        this.displayOrder = displayOrder;
        this.status = StageStatus.TODO;
    }

    public void start() {
        if (this.status == StageStatus.TODO) {
            this.status = StageStatus.IN_PROGRESS;
        }
    }

    public void complete() {
        this.status = StageStatus.DONE;
    }

    public enum StageStatus { TODO, IN_PROGRESS, DONE }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getDisplayOrder() { return displayOrder; }
    public StageStatus getStatus() { return status; }
}
