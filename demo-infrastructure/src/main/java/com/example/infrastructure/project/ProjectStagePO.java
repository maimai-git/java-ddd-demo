package com.example.infrastructure.project;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_project_stage")
public class ProjectStagePO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectPO project;

    private String name;
    private int displayOrder;

    @Enumerated(EnumType.STRING)
    private StageStatus status;

    public enum StageStatus { TODO, IN_PROGRESS, DONE }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProjectPO getProject() { return project; }
    public void setProject(ProjectPO project) { this.project = project; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    public StageStatus getStatus() { return status; }
    public void setStatus(StageStatus status) { this.status = status; }
}
