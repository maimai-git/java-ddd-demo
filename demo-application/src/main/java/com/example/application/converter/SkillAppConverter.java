package com.example.application.converter;

import com.example.application.dto.co.SkillCO;
import com.example.domain.skill.model.Skill;
import com.example.domain.skill.model.SkillReadModel;

public class SkillAppConverter {

    private SkillAppConverter() {}

    public static SkillCO toCO(Skill skill) {
        SkillCO co = new SkillCO();
        co.setId(skill.getId());
        co.setName(skill.getName());
        co.setCategory(skill.getCategory());
        co.setLevel(skill.getLevel().name());
        co.setDescription(skill.getDescription());
        co.setStatus(skill.getStatus().name());
        co.setCreatedAt(skill.getCreatedAt());
        co.setUpdatedAt(skill.getUpdatedAt());
        return co;
    }

    public static SkillCO toCO(SkillReadModel m) {
        SkillCO co = new SkillCO();
        co.setId(m.id());
        co.setName(m.name());
        co.setCategory(m.category());
        co.setLevel(m.level());
        co.setDescription(m.description());
        co.setStatus(m.status());
        co.setCreatedAt(m.createdAt());
        co.setUpdatedAt(m.updatedAt());
        return co;
    }
}
