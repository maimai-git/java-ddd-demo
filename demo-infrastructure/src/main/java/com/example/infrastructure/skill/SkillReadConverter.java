package com.example.infrastructure.skill;

import com.example.domain.skill.model.SkillReadModel;

/** SkillPO → 读投影，不经过领域模型 */
class SkillReadConverter {

    static SkillReadModel toReadModel(SkillPO po) {
        return new SkillReadModel(
                po.getId(),
                po.getName(),
                po.getCategory(),
                po.getLevel().name(),
                po.getDescription(),
                po.getStatus().name(),
                po.getCreatedAt(),
                po.getUpdatedAt()
        );
    }
}
