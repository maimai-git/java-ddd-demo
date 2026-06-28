package com.example.domain.skill.service;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.domain.skill.model.Skill;
import com.example.domain.skill.model.SkillProfile;
import com.example.domain.skill.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class SkillDomainService {

    private final SkillRepository skillRepository;

    public SkillDomainService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill create(String name, String category, Skill.Level level, String description) {
        Skill skill = Skill.register(name, category, level, description);
        return skillRepository.save(skill);
    }

    public Skill update(Long id, SkillProfile profile) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        skill.updateProfile(profile);
        return skillRepository.save(skill);
    }

    public void delete(Long id) {
        skillRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        skillRepository.deleteById(id);
    }

    public Optional<Skill> findById(Long id) {
        return skillRepository.findById(id);
    }

    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    public List<Skill> findByCategory(String category) {
        return skillRepository.findByCategory(category);
    }

    public void updateName(Long id, String name, Instant updatedAt) {
        skillRepository.updateName(id, name, updatedAt);
    }

    public void updateStatus(Long id, String status, Instant updatedAt) {
        skillRepository.updateStatus(id, status, updatedAt);
    }
}
