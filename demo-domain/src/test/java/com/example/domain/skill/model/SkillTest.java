package com.example.domain.skill.model;

import com.example.common.event.DomainEvent;
import com.example.common.exception.BizException;
import com.example.domain.skill.event.SkillStatusChangedEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SkillTest {

    @Test
    void shouldRegisterWithDefaults() {
        Skill skill = Skill.register("Java", "dev", Skill.Level.BEGINNER, "desc");
        assertThat(skill.getName()).isEqualTo("Java");
        assertThat(skill.getCategory()).isEqualTo("dev");
        assertThat(skill.getLevel()).isEqualTo(Skill.Level.BEGINNER);
        assertThat(skill.getStatus()).isEqualTo(Skill.Status.DRAFT);
    }

    @Test
    void shouldDefaultToBeginnerWhenLevelNull() {
        Skill skill = Skill.register("Go", "dev", null, null);
        assertThat(skill.getLevel()).isEqualTo(Skill.Level.BEGINNER);
    }

    @Test
    void shouldRejectEmptyName() {
        assertThatThrownBy(() -> Skill.register("", "dev", Skill.Level.BEGINNER, ""))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("技能名称不能为空");
    }

    @Test
    void shouldRejectEmptyCategory() {
        assertThatThrownBy(() -> Skill.register("Python", "", Skill.Level.BEGINNER, ""))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("技能分类不能为空");
    }

    @Test
    void shouldPartialUpdateOnlyNonNullFields() {
        Skill skill = Skill.register("Java", "dev", Skill.Level.BEGINNER, "old desc");
        skill.updateProfile(new SkillProfile("Kotlin", null, Skill.Level.ADVANCED, null));
        assertThat(skill.getName()).isEqualTo("Kotlin");
        assertThat(skill.getCategory()).isEqualTo("dev");       // 没改
        assertThat(skill.getLevel()).isEqualTo(Skill.Level.ADVANCED);
        assertThat(skill.getDescription()).isEqualTo("old desc"); // 没改
    }

    @Test
    void shouldActivateDraftSkill() {
        Skill skill = Skill.register("Rust", "systems", Skill.Level.EXPERT, null);
        skill.activate();
        assertThat(skill.getStatus()).isEqualTo(Skill.Status.ACTIVE);
    }

    @Test
    void shouldPublishEventOnActivate() {
        Skill skill = Skill.register("Rust", "systems", Skill.Level.EXPERT, null);
        skill.activate();
        List<DomainEvent> events = skill.getDomainEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(SkillStatusChangedEvent.class);
        assertThat(((SkillStatusChangedEvent) events.get(0)).newStatus())
                .isEqualTo(Skill.Status.ACTIVE);
    }

    @Test
    void shouldRejectActivateWhenArchived() {
        Skill skill = Skill.register("C++", "legacy", Skill.Level.BEGINNER, null);
        skill.archive();
        assertThatThrownBy(skill::activate)
                .isInstanceOf(BizException.class)
                .hasMessageContaining("已归档");
    }

    @Test
    void shouldArchiveIdempotently() {
        Skill skill = Skill.register("Perl", "legacy", Skill.Level.BEGINNER, null);
        skill.archive();
        skill.archive(); // 不应抛异常
        assertThat(skill.getStatus()).isEqualTo(Skill.Status.ARCHIVED);
    }

    @Test
    void shouldPublishEventOnArchive() {
        Skill skill = Skill.register("PHP", "web", Skill.Level.BEGINNER, null);
        skill.archive();
        assertThat(skill.getDomainEvents()).hasSize(1);
    }

    /* ===== changeName ===== */

    @Test
    void shouldChangeName() {
        Skill skill = Skill.register("Java", "dev", Skill.Level.BEGINNER, "desc");
        skill.changeName("Kotlin");
        assertThat(skill.getName()).isEqualTo("Kotlin");
    }

    @Test
    void shouldRejectEmptyNameInChangeName() {
        Skill skill = Skill.register("Java", "dev", Skill.Level.BEGINNER, "desc");
        assertThatThrownBy(() -> skill.changeName(""))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("技能名称不能为空");
    }

    /* ===== changeStatus ===== */

    @Test
    void shouldChangeStatusDraftToActive() {
        Skill skill = Skill.register("Go", "dev", Skill.Level.BEGINNER, null);
        skill.changeStatus(Skill.Status.ACTIVE);
        assertThat(skill.getStatus()).isEqualTo(Skill.Status.ACTIVE);
    }

    @Test
    void shouldChangeStatusActiveToArchived() {
        Skill skill = Skill.register("Go", "dev", Skill.Level.BEGINNER, null);
        skill.activate();
        skill.changeStatus(Skill.Status.ARCHIVED);
        assertThat(skill.getStatus()).isEqualTo(Skill.Status.ARCHIVED);
    }

    @Test
    void shouldPublishEventOnChangeStatus() {
        Skill skill = Skill.register("Go", "dev", Skill.Level.BEGINNER, null);
        skill.changeStatus(Skill.Status.ACTIVE);
        assertThat(skill.getDomainEvents()).hasSize(1);
    }

    @Test
    void shouldRejectDraftToArchived() {
        Skill skill = Skill.register("Go", "dev", Skill.Level.BEGINNER, null);
        assertThatThrownBy(() -> skill.changeStatus(Skill.Status.ARCHIVED))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("草稿状态只能切换为启用");
    }

    @Test
    void shouldRejectActiveToDraft() {
        Skill skill = Skill.register("Go", "dev", Skill.Level.BEGINNER, null);
        skill.activate();
        assertThatThrownBy(() -> skill.changeStatus(Skill.Status.DRAFT))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("启用状态只能切换为归档");
    }

    @Test
    void shouldRejectArchivedStatusChange() {
        Skill skill = Skill.register("Go", "dev", Skill.Level.BEGINNER, null);
        skill.archive();
        assertThatThrownBy(() -> skill.changeStatus(Skill.Status.DRAFT))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("已归档状态不可变更");
    }

    @Test
    void shouldNoopWhenSameStatusInChangeStatus() {
        Skill skill = Skill.register("Go", "dev", Skill.Level.BEGINNER, null);
        skill.changeStatus(Skill.Status.DRAFT); // same as initial
        assertThat(skill.getStatus()).isEqualTo(Skill.Status.DRAFT);
        assertThat(skill.getDomainEvents()).isEmpty();
    }
}
