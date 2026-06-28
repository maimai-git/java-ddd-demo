package com.example.interfaces.controller;

import com.example.application.service.ProjectAppService;
import com.example.application.common.MultiResponse;
import com.example.application.common.Response;
import com.example.application.common.SingleResponse;
import com.example.application.dto.co.ProjectCO;
import com.example.application.dto.command.ProjectCreateCmd;
import com.example.application.dto.command.ProjectUpdateCmd;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectAppService projectAppService;

    public ProjectController(ProjectAppService projectAppService) {
        this.projectAppService = projectAppService;
    }

    @PostMapping
    public SingleResponse<ProjectCO> create(@RequestBody @Valid ProjectCreateCmd cmd) {
        return projectAppService.create(cmd);
    }

    @GetMapping("/{id}")
    public SingleResponse<ProjectCO> get(@PathVariable Long id) {
        return projectAppService.getById(id);
    }

    @GetMapping
    public MultiResponse<ProjectCO> list() {
        return projectAppService.listAll();
    }

    @PutMapping("/{id}")
    public Response update(@PathVariable Long id, @RequestBody @Valid ProjectUpdateCmd cmd) {
        return projectAppService.update(id, cmd);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        return projectAppService.delete(id);
    }

    @PostMapping("/{id}/activate")
    public Response activate(@PathVariable Long id) {
        return projectAppService.activate(id);
    }

    @PostMapping("/{id}/complete")
    public Response complete(@PathVariable Long id) {
        return projectAppService.complete(id);
    }
}
