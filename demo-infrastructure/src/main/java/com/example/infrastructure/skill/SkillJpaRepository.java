package com.example.infrastructure.skill;

import com.example.infrastructure.skill.SkillPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillJpaRepository extends JpaRepository<SkillPO, Long> {
}
