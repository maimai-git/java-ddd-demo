package com.example.domain.skill.model;

/**
 * 技能的可更新属性集合——局部更新时 null 字段表示不修改。
 * 比散传 4 个参数更内聚，字段多了也不会让方法签名爆炸。
 */
public record SkillProfile(String name, String category, Skill.Level level, String description) {
}
