package com.example.infrastructure.project;

import com.example.infrastructure.project.ProjectPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJpaRepository extends JpaRepository<ProjectPO, Long> {
}
