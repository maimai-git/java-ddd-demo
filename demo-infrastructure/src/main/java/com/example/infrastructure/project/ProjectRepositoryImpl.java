package com.example.infrastructure.project;

import com.example.domain.project.model.Project;
import com.example.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository jpaRepository;

    public ProjectRepositoryImpl(ProjectJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Project save(Project project) {
        var po = ProjectConverter.toPO(project);
        var saved = jpaRepository.save(po);
        return ProjectConverter.toDomain(saved);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return jpaRepository.findById(id).map(ProjectConverter::toDomain);
    }

    @Override
    public List<Project> findAll() {
        return jpaRepository.findAll().stream().map(ProjectConverter::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
