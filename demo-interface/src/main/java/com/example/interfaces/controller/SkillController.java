package com.example.interfaces.controller;

import com.example.application.service.SkillAppService;
import com.example.application.common.MultiResponse;
import com.example.application.common.Response;
import com.example.application.common.SingleResponse;
import com.example.application.dto.co.SkillCO;
import com.example.application.dto.command.SkillChangeNameCmd;
import com.example.application.dto.command.SkillChangeStatusCmd;
import com.example.application.dto.command.SkillCreateCmd;
import com.example.application.dto.command.SkillUpdateCmd;
import com.example.application.dto.query.SkillPageQry;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillAppService skillAppService;

    public SkillController(SkillAppService skillAppService) {
        this.skillAppService = skillAppService;
    }

    @PostMapping
    public SingleResponse<SkillCO> create(@RequestBody @Valid SkillCreateCmd cmd) {
        return skillAppService.create(cmd);
    }

    @GetMapping("/{id}")
    public SingleResponse<SkillCO> get(@PathVariable Long id) {
        return skillAppService.getById(id);
    }

    @GetMapping
    public MultiResponse<SkillCO> list(SkillPageQry qry) {
        return skillAppService.list(qry);
    }

    @PutMapping("/{id}")
    public Response update(@PathVariable Long id, @RequestBody @Valid SkillUpdateCmd cmd) {
        return skillAppService.update(id, cmd);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        return skillAppService.delete(id);
    }

    @PostMapping("/{id}/activate")
    public Response activate(@PathVariable Long id) {
        return skillAppService.activate(id);
    }

    @PostMapping("/{id}/archive")
    public Response archive(@PathVariable Long id) {
        return skillAppService.archive(id);
    }

    @PutMapping("/{id}/name")
    public Response changeName(@PathVariable Long id, @RequestBody @Valid SkillChangeNameCmd cmd) {
        return skillAppService.changeName(id, cmd);
    }

    @PutMapping("/{id}/status")
    public Response changeStatus(@PathVariable Long id, @RequestBody @Valid SkillChangeStatusCmd cmd) {
        return skillAppService.changeStatus(id, cmd);
    }

    @PostMapping("/batch-archive")
    public Response batchArchive(@RequestParam String category) {
        return skillAppService.batchArchive(category);
    }
}
