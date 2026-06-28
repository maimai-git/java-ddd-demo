package com.example.infrastructure.project;

import com.example.domain.project.model.Project;
import com.example.domain.project.model.ProjectSettings;
import com.example.domain.project.model.ProjectStage;
import com.example.domain.project.model.ProjectStage.StageStatus;
import com.example.domain.project.model.ProjectTag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 复杂对象双向转换——展示嵌套子实体、值对象展开/合并
 */
public class ProjectConverter {

    /* ===== Domain → PO ===== */

    public static ProjectPO toPO(Project project) {
        ProjectPO po = new ProjectPO();
        po.setId(project.getId());
        po.setName(project.getName());
        po.setDescription(project.getDescription());
        po.setStatus(ProjectPO.ProjectStatus.valueOf(project.getStatus().name()));

        // 值对象: settings 展开为平面字段
        if (project.getSettings() != null) {
            po.setVisibility(project.getSettings().visibility().name());
            po.setPriority(project.getSettings().priority());
            po.setDeadline(project.getSettings().deadline());
        }

        // 值对象列表: tags → JSON 字符串
        po.setTagsJson(tagsToJson(project.getTags()));

        // 子实体列表: stages (只在聚合根存在时用数据库已有 stages)
        po.setCreatedAt(project.getCreatedAt());
        po.setUpdatedAt(project.getUpdatedAt());

        // 子实体映射
        for (ProjectStage stage : project.getStages()) {
            ProjectStagePO stagePO = new ProjectStagePO();
            stagePO.setId(stage.getId());
            stagePO.setName(stage.getName());
            stagePO.setDisplayOrder(stage.getDisplayOrder());
            stagePO.setStatus(ProjectStagePO.StageStatus.valueOf(stage.getStatus().name()));
            po.addStage(stagePO);
        }
        return po;
    }

    /* ===== PO → Domain ===== */

    public static Project toDomain(ProjectPO po) {
        // 值对象: settings 从平面字段重建
        ProjectSettings settings = null;
        if (po.getVisibility() != null) {
            settings = new ProjectSettings(
                    ProjectSettings.Visibility.valueOf(po.getVisibility()),
                    po.getPriority(),
                    po.getDeadline()
            );
        }

        // 值对象列表: JSON 字符串 → tags
        List<ProjectTag> tags = jsonToTags(po.getTagsJson());

        Project project = Project.reconstitute(po.getId(), po.getName(), po.getDescription(),
                Project.Status.valueOf(po.getStatus().name()), settings, tags,
                po.getCreatedAt(), po.getUpdatedAt());

        // 子实体列表重建
        for (ProjectStagePO stagePO : po.getStages()) {
            ProjectStage.StageStatus stageStatus = stagePO.getStatus() != null
                    ? ProjectStage.StageStatus.valueOf(stagePO.getStatus().name())
                    : ProjectStage.StageStatus.TODO;
            project.addStage(ProjectStage.reconstitute(stagePO.getId(), stagePO.getName(),
                    stagePO.getDisplayOrder(), stageStatus));
        }
        return project;
    }

    /* ===== Tags 序列化 ===== */

    private static String tagsToJson(List<ProjectTag> tags) {
        if (tags == null || tags.isEmpty()) return "[]";
        return tags.stream()
                .map(t -> "{\"name\":\"" + t.name() + "\",\"color\":\"" + t.color() + "\"}")
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static List<ProjectTag> jsonToTags(String json) {
        if (json == null || json.isBlank() || "[]".equals(json.trim())) return new ArrayList<>();
        List<ProjectTag> tags = new ArrayList<>();
        String[] parts = json.replace("[", "").replace("]", "").split("},");
        for (String part : parts) {
            part = part.replace("{", "").replace("}", "").replace("\"", "");
            String[] kv = part.split(",");
            String name = kv.length > 0 ? kv[0].split(":")[1] : "";
            String color = kv.length > 1 ? kv[1].split(":")[1] : "";
            tags.add(new ProjectTag(name, color));
        }
        return tags;
    }
}
