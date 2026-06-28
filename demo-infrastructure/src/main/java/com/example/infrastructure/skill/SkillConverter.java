package com.example.infrastructure.skill;

import com.example.domain.skill.model.Skill;

public class SkillConverter {

    public static SkillPO toPO(Skill skill) {
        SkillPO po = new SkillPO();
        po.setId(skill.getId());
        po.setName(skill.getName());
        po.setCategory(skill.getCategory());
        po.setLevel(SkillPO.Level.valueOf(skill.getLevel().name()));
        po.setDescription(skill.getDescription());
        po.setStatus(SkillPO.Status.valueOf(skill.getStatus().name()));
        po.setCreatedAt(skill.getCreatedAt());
        po.setUpdatedAt(skill.getUpdatedAt());
        return po;
    }

    public static Skill toDomain(SkillPO po) {
        Skill.Status status = po.getStatus() != null
                ? Skill.Status.valueOf(po.getStatus().name())
                : Skill.Status.DRAFT;
        return Skill.reconstitute(po.getId(), po.getName(), po.getCategory(),
                Skill.Level.valueOf(po.getLevel().name()), po.getDescription(),
                status, po.getCreatedAt(), po.getUpdatedAt());
    }
}
