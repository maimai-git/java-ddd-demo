package com.example.domain.skill.model;

/**
 * 技能查询条件——CQRS 读模型用，null 字段表示不过滤该维度。
 */
public record SkillFilter(
        String keyword,
        String category,
        Skill.Level level,
        Skill.Status status
) {
    public static SkillFilter all() {
        return new SkillFilter(null, null, null, null);
    }

    public boolean isEmpty() {
        return (keyword == null || keyword.isBlank())
                && category == null && level == null && status == null;
    }
}
