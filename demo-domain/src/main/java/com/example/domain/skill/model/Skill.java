package com.example.domain.skill.model;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.domain.shared.model.BaseEntity;
import com.example.domain.skill.event.SkillStatusChangedEvent;

import java.time.Instant;

public class Skill extends BaseEntity {

    private String name;
    private String category;
    private Level level;
    private String description;
    private Status status;

    public enum Level { BEGINNER, INTERMEDIATE, ADVANCED, EXPERT }

    public enum Status { DRAFT, ACTIVE, ARCHIVED }

    /* ===== 工厂方法 ===== */

    public static Skill register(String name, String category, Level level, String description) {
        return new Skill(name, category, level, description);
    }

    /** 从持久层重建 — 不走 create 语义，不设时间戳 */
    public static Skill reconstitute(Long id, String name, String category, Level level,
                                     String description, Status status,
                                     Instant createdAt, Instant updatedAt) {
        Skill skill = new Skill(name, category, level, description);
        skill.setId(id);
        skill.status = status;
        skill.setCreatedAt(createdAt);
        skill.setUpdatedAt(updatedAt);
        return skill;
    }

    private Skill(String name, String category, Level level, String description) {
        if (name == null || name.isBlank()) {
            throw new BizException(ErrorCode.SKILL_NAME_REQUIRED);
        }
        if (category == null || category.isBlank()) {
            throw new BizException(ErrorCode.SKILL_CATEGORY_REQUIRED);
        }
        this.name = name;
        this.category = category;
        this.level = level != null ? level : Level.BEGINNER;
        this.description = description;
        this.status = Status.DRAFT;
        var now = Instant.now();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    /* ===== 详情更新（null 字段表示不修改） ===== */

    public void updateProfile(SkillProfile profile) {
        if (profile.name() != null && !profile.name().isBlank()) this.name = profile.name();
        if (profile.category() != null && !profile.category().isBlank()) this.category = profile.category();
        if (profile.level() != null) this.level = profile.level();
        if (profile.description() != null) this.description = profile.description();
        setUpdatedAt(Instant.now());
    }

    /* ===== 状态变更（行为方法，含业务规则） ===== */

    public void activate() {
        if (this.status == Status.ARCHIVED) {
            throw new BizException(ErrorCode.INVALID_PARAM, "已归档的技能无法启用");
        }
        this.status = Status.ACTIVE;
        setUpdatedAt(Instant.now());
        registerEvent(new SkillStatusChangedEvent(getId(), this.name, Status.ACTIVE));
    }

    public void archive() {
        if (this.status == Status.ARCHIVED) {
            return; // 幂等
        }
        this.status = Status.ARCHIVED;
        setUpdatedAt(Instant.now());
        registerEvent(new SkillStatusChangedEvent(getId(), this.name, Status.ARCHIVED));
    }

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new BizException(ErrorCode.SKILL_NAME_REQUIRED);
        }
        this.name = newName;
        setUpdatedAt(Instant.now());
    }

    public void changeStatus(Status newStatus) {
        if (newStatus == null || newStatus == this.status) return;
        switch (this.status) {
            case DRAFT -> {
                if (newStatus != Status.ACTIVE)
                    throw new BizException(ErrorCode.INVALID_PARAM, "草稿状态只能切换为启用");
            }
            case ACTIVE -> {
                if (newStatus != Status.ARCHIVED)
                    throw new BizException(ErrorCode.INVALID_PARAM, "启用状态只能切换为归档");
            }
            case ARCHIVED ->
                    throw new BizException(ErrorCode.INVALID_PARAM, "已归档状态不可变更");
        }
        Status oldStatus = this.status;
        this.status = newStatus;
        setUpdatedAt(Instant.now());
        registerEvent(new SkillStatusChangedEvent(getId(), this.name, newStatus));
    }

    /* ===== getters ===== */

    public String getName() { return name; }
    public String getCategory() { return category; }
    public Level getLevel() { return level; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
}
