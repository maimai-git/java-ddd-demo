package com.example.domain.project.model;

import java.util.List;

/**
 * 项目的可更新属性集合——null 字段表示不修改。
 * settings 粒度较粗：传整个对象替换，不传保持原值。按需细化时可拆成独立字段。
 */
public record ProjectProfile(
        String name,
        String description,
        ProjectSettings settings,
        List<ProjectTag> tags
) {}
