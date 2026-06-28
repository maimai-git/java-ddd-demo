package com.example.domain.project.service;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.domain.project.model.Project;
import com.example.domain.project.model.ProjectProfile;
import com.example.domain.project.model.ProjectSettings;
import com.example.domain.project.model.ProjectStage;
import com.example.domain.project.model.ProjectTag;
import com.example.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectDomainService {

    private final ProjectRepository projectRepository;

    public ProjectDomainService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project create(String name, String description, ProjectSettings settings, List<ProjectTag> tags, List<String> stageNames) {
        Project project = Project.initiate(name, description, settings, tags);
        if (stageNames != null) {
            for (int i = 0; i < stageNames.size(); i++) {
                project.addStage(ProjectStage.of(stageNames.get(i), i + 1));
            }
        }
        return projectRepository.save(project);
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project update(Long id, ProjectProfile profile) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.PROJECT_NOT_FOUND));
        project.updateProfile(profile);
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.findById(id).orElseThrow(() -> new BizException(ErrorCode.PROJECT_NOT_FOUND));
        projectRepository.deleteById(id);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }
}
