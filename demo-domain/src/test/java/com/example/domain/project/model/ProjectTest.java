package com.example.domain.project.model;

import com.example.common.event.DomainEvent;
import com.example.common.exception.BizException;
import com.example.domain.project.event.ProjectStatusChangedEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectTest {

    private static final ProjectSettings settings = new ProjectSettings(
            ProjectSettings.Visibility.TEAM, 1, null);
    private static final List<ProjectTag> tags = List.of(new ProjectTag("DDD", "blue"));

    @Test
    void shouldInitiateWithDefaultStatus() {
        Project project = Project.initiate("重构", "desc", settings, tags);
        assertThat(project.getName()).isEqualTo("重构");
        assertThat(project.getStatus()).isEqualTo(Project.Status.DRAFT);
        assertThat(project.getTags()).hasSize(1);
    }

    @Test
    void shouldRejectEmptyName() {
        assertThatThrownBy(() -> Project.initiate("", "desc", null, null))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("项目名称不能为空");
    }

    @Test
    void shouldPartialUpdate() {
        Project project = Project.initiate("老名字", "老描述", settings, tags);
        project.updateProfile(new ProjectProfile("新名字", null, null, null));
        assertThat(project.getName()).isEqualTo("新名字");
        assertThat(project.getDescription()).isEqualTo("老描述"); // 没改
    }

    @Test
    void shouldActivateDraftProject() {
        Project project = Project.initiate("项目", "desc", null, null);
        project.activate();
        assertThat(project.getStatus()).isEqualTo(Project.Status.ACTIVE);
    }

    @Test
    void shouldPublishEventOnActivate() {
        Project project = Project.initiate("项目", "desc", null, null);
        project.activate();
        List<DomainEvent> events = project.getDomainEvents();
        assertThat(events).hasSize(1);
        assertThat(((ProjectStatusChangedEvent) events.get(0)).newStatus())
                .isEqualTo(Project.Status.ACTIVE);
    }

    @Test
    void shouldRejectActivateWhenCompleted() {
        Project project = Project.initiate("项目", "desc", null, null);
        project.activate();
        project.complete();
        assertThatThrownBy(project::activate)
                .isInstanceOf(BizException.class)
                .hasMessageContaining("已完成");
    }

    @Test
    void shouldCompleteActiveProject() {
        Project project = Project.initiate("项目", "desc", null, null);
        project.activate();
        project.complete();
        assertThat(project.getStatus()).isEqualTo(Project.Status.COMPLETED);
    }

    @Test
    void shouldRejectCompleteWhenDraft() {
        Project project = Project.initiate("项目", "desc", null, null);
        assertThatThrownBy(project::complete)
                .isInstanceOf(BizException.class)
                .hasMessageContaining("只有进行中的项目");
    }

    @Test
    void shouldAddStage() {
        Project project = Project.initiate("项目", "desc", null, null);
        project.addStage(ProjectStage.of("设计", 1));
        project.addStage(ProjectStage.of("开发", 2));
        assertThat(project.getStages()).hasSize(2);
    }

    @Test
    void shouldPublishEventOnComplete() {
        Project project = Project.initiate("项目", "desc", null, null);
        project.activate();
        project.complete();
        assertThat(project.getDomainEvents()).hasSize(2); // activate + complete
    }
}
