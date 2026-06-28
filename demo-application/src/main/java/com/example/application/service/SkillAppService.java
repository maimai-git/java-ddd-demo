package com.example.application.service;

import com.example.application.common.DomainEvents;
import com.example.application.common.MultiResponse;
import com.example.application.common.Response;
import com.example.application.common.SingleResponse;
import com.example.application.converter.SkillAppConverter;
import com.example.application.dto.co.SkillCO;
import com.example.application.dto.command.SkillChangeNameCmd;
import com.example.application.dto.command.SkillChangeStatusCmd;
import com.example.application.dto.command.SkillCreateCmd;
import com.example.application.dto.command.SkillUpdateCmd;
import com.example.application.dto.query.SkillPageQry;
import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.common.log.BizLog;
import com.example.domain.skill.model.Skill;
import com.example.domain.skill.model.SkillFilter;
import com.example.domain.skill.model.SkillProfile;
import com.example.domain.skill.repository.SkillReadRepository;
import com.example.domain.skill.service.SkillDomainService;
import com.example.domain.shared.external.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillAppService {

    private static final Logger log = LoggerFactory.getLogger(SkillAppService.class);

    private final SkillDomainService skillDomainService;
    private final SkillReadRepository skillReadRepository;
    private final EventPublisher eventPublisher;

    public SkillAppService(SkillDomainService skillDomainService,
                           SkillReadRepository skillReadRepository,
                           EventPublisher eventPublisher) {
        this.skillDomainService = skillDomainService;
        this.skillReadRepository = skillReadRepository;
        this.eventPublisher = eventPublisher;
    }

    @BizLog("创建技能")
    @Transactional
    public SingleResponse<SkillCO> create(SkillCreateCmd cmd) {
        Skill.Level level = cmd.getLevel() != null ? Skill.Level.valueOf(cmd.getLevel()) : Skill.Level.BEGINNER;
        Skill skill = skillDomainService.create(cmd.getName(), cmd.getCategory(), level, cmd.getDescription());
        return SingleResponse.of(SkillAppConverter.toCO(skill));
    }

    @Transactional(readOnly = true)
    public SingleResponse<SkillCO> getById(Long id) {
        Skill skill = skillDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        return SingleResponse.of(SkillAppConverter.toCO(skill));
    }

    @Transactional(readOnly = true)
    public MultiResponse<SkillCO> list(SkillPageQry qry) {
        int pageSize = Math.max(1, qry.getPageSize());
        int pageIndex = Math.max(1, qry.getPageIndex());
        int offset = (pageIndex - 1) * pageSize;
        Skill.Level level = qry.getLevel() != null ? Skill.Level.valueOf(qry.getLevel()) : null;
        Skill.Status status = qry.getStatus() != null ? Skill.Status.valueOf(qry.getStatus()) : null;
        var filter = new SkillFilter(qry.getKeyword(), qry.getCategory(), level, status);
        List<SkillCO> list = skillReadRepository.findPaged(filter, offset, pageSize).stream().map(SkillAppConverter::toCO).toList();
        long total = skillReadRepository.count(filter);
        return MultiResponse.of(list, total, pageIndex, pageSize);
    }

    @BizLog("更新技能")
    @Transactional
    public Response update(Long id, SkillUpdateCmd cmd) {
        Skill.Level level = cmd.getLevel() != null ? Skill.Level.valueOf(cmd.getLevel()) : null;
        var profile = new SkillProfile(cmd.getName(), cmd.getCategory(), level, cmd.getDescription());
        skillDomainService.update(id, profile);
        return Response.buildSuccess();
    }

    @BizLog("删除技能")
    @Transactional
    public Response delete(Long id) {
        skillDomainService.delete(id);
        return Response.buildSuccess();
    }

    @BizLog("修改技能名称")
    @Transactional
    public Response changeName(Long id, SkillChangeNameCmd cmd) {
        // 方案A: 全字段 save — skillDomainService.save(skill)
        // 方案B: 精准字段更新 ↓
        Skill skill = skillDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        skill.changeName(cmd.getName());
        skillDomainService.updateName(skill.getId(), skill.getName(), skill.getUpdatedAt());
        return Response.buildSuccess();
    }

    @BizLog("修改技能状态")
    @Transactional
    public Response changeStatus(Long id, SkillChangeStatusCmd cmd) {
        // 方案A: 全字段 save + 自动事件 — DomainEvents.saveAndPublish(skill, skillDomainService::save, eventPublisher)
        // 方案B: 精准字段更新 + 手动发布事件 ↓
        Skill skill = skillDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        Skill.Status newStatus = Skill.Status.valueOf(cmd.getStatus());
        skill.changeStatus(newStatus);
        skillDomainService.updateStatus(skill.getId(), skill.getStatus().name(), skill.getUpdatedAt());
        skill.getDomainEvents().forEach(eventPublisher::publish);
        return Response.buildSuccess();
    }

    /* ===== 状态变更（走行为方法 + 领域事件） ===== */

    @BizLog("启用技能")
    @Transactional
    public Response activate(Long id) {
        Skill skill = skillDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        skill.activate();
        DomainEvents.saveAndPublish(skill, skillDomainService::save, eventPublisher);
        return Response.buildSuccess();
    }

    @BizLog("归档技能")
    @Transactional
    public Response archive(Long id) {
        Skill skill = skillDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        skill.archive();
        DomainEvents.saveAndPublish(skill, skillDomainService::save, eventPublisher);
        return Response.buildSuccess();
    }

    @BizLog("批量归档")
    @Transactional
    public Response batchArchive(String category) {
        List<Skill> skills = skillDomainService.findByCategory(category);
        int count = 0;
        for (Skill skill : skills) {
            skill.archive();
            DomainEvents.saveAndPublish(skill, skillDomainService::save, eventPublisher);
            count++;
        }
        log.info("Batch archived {} skills in category '{}'", count, category);
        return Response.buildSuccess();
    }
}
