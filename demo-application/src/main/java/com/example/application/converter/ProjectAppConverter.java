package com.example.application.converter;

import com.example.application.dto.co.ProjectCO;
import com.example.application.dto.command.ProjectCreateCmd;
import com.example.domain.project.model.Project;
import com.example.domain.project.model.ProjectSettings;
import com.example.domain.project.model.ProjectTag;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * client DTO ↔ 领域对象转换（放 app 层）
 */
public class ProjectAppConverter {

    /* ===== Cmd → 领域值对象 ===== */

    public static ProjectSettings toSettings(ProjectCreateCmd.Settings s) {
        if (s == null) return null;
        return new ProjectSettings(
                ProjectSettings.Visibility.valueOf(s.getVisibility()),
                s.getPriority(),
                s.getDeadline() != null ? LocalDate.parse(s.getDeadline()) : null
        );
    }

    public static List<ProjectTag> toTags(List<ProjectCreateCmd.Tag> tags) {
        if (tags == null) return Collections.emptyList();
        return tags.stream().map(t -> new ProjectTag(t.getName(), t.getColor())).toList();
    }

    /* ===== Entity → CO（from 逻辑不放 CO 身上） ===== */

    public static ProjectCO toCO(Project project) {
        ProjectCO co = new ProjectCO();
        co.setId(project.getId());
        co.setName(project.getName());
        co.setDescription(project.getDescription());
        co.setStatus(project.getStatus().name());
        co.setCreatedAt(project.getCreatedAt());
        co.setUpdatedAt(project.getUpdatedAt());

        if (project.getSettings() != null) {
            ProjectCO.Settings s = new ProjectCO.Settings();
            s.setVisibility(project.getSettings().visibility().name());
            s.setPriority(project.getSettings().priority());
            s.setDeadline(project.getSettings().deadline());
            co.setSettings(s);
        }

        co.setTags(project.getTags().stream().map(t -> {
            ProjectCO.Tag d = new ProjectCO.Tag();
            d.setName(t.name());
            d.setColor(t.color());
            return d;
        }).toList());

        co.setStages(project.getStages().stream().map(s -> {
            ProjectCO.Stage d = new ProjectCO.Stage();
            d.setId(s.getId());
            d.setName(s.getName());
            d.setDisplayOrder(s.getDisplayOrder());
            d.setStatus(s.getStatus().name());
            return d;
        }).toList());

        return co;
    }
}
