package com.example.application.service;

import com.example.application.common.DomainEvents;
import com.example.application.common.MultiResponse;
import com.example.application.common.Response;
import com.example.application.common.SingleResponse;
import com.example.application.converter.ProjectAppConverter;
import com.example.application.dto.co.ProjectCO;
import com.example.application.dto.command.ProjectCreateCmd;
import com.example.application.dto.command.ProjectUpdateCmd;
import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.common.log.BizLog;
import com.example.domain.project.model.Project;
import com.example.domain.project.model.ProjectProfile;
import com.example.domain.project.service.ProjectDomainService;
import com.example.domain.shared.external.EventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectAppService {

    private final ProjectDomainService projectDomainService;
    private final EventPublisher eventPublisher;

    public ProjectAppService(ProjectDomainService projectDomainService, EventPublisher eventPublisher) {
        this.projectDomainService = projectDomainService;
        this.eventPublisher = eventPublisher;
    }

    @BizLog("创建项目")
    @Transactional
    public SingleResponse<ProjectCO> create(ProjectCreateCmd cmd) {
        var settings = ProjectAppConverter.toSettings(cmd.getSettings());
        var tags = ProjectAppConverter.toTags(cmd.getTags());
        List<String> stageNames = cmd.getStages() != null
                ? cmd.getStages().stream().map(ProjectCreateCmd.Stage::getName).toList()
                : List.of();
        Project project = projectDomainService.create(cmd.getName(), cmd.getDescription(), settings, tags, stageNames);
        return SingleResponse.of(ProjectAppConverter.toCO(project));
    }

    @Transactional(readOnly = true)
    public SingleResponse<ProjectCO> getById(Long id) {
        Project project = projectDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.PROJECT_NOT_FOUND));
        return SingleResponse.of(ProjectAppConverter.toCO(project));
    }

    @BizLog("更新项目")
    @Transactional
    public Response update(Long id, ProjectUpdateCmd cmd) {
        var settings = ProjectAppConverter.toSettings(cmd.getSettings());
        var tags = ProjectAppConverter.toTags(cmd.getTags());
        var profile = new ProjectProfile(cmd.getName(), cmd.getDescription(), settings, tags);
        projectDomainService.update(id, profile);
        return Response.buildSuccess();
    }

    @BizLog("删除项目")
    @Transactional
    public Response delete(Long id) {
        projectDomainService.delete(id);
        return Response.buildSuccess();
    }

    /* ===== 状态变更 ===== */

    @BizLog("激活项目")
    @Transactional
    public Response activate(Long id) {
        Project project = projectDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.PROJECT_NOT_FOUND));
        project.activate();
        DomainEvents.saveAndPublish(project, projectDomainService::save, eventPublisher);
        return Response.buildSuccess();
    }

    @BizLog("完成项目")
    @Transactional
    public Response complete(Long id) {
        Project project = projectDomainService.findById(id).orElseThrow(() -> new BizException(ErrorCode.PROJECT_NOT_FOUND));
        project.complete();
        DomainEvents.saveAndPublish(project, projectDomainService::save, eventPublisher);
        return Response.buildSuccess();
    }

    @Transactional(readOnly = true)
    public MultiResponse<ProjectCO> listAll() {
        List<ProjectCO> list = projectDomainService.findAll().stream().map(ProjectAppConverter::toCO).toList();
        return MultiResponse.of(list);
    }
}
