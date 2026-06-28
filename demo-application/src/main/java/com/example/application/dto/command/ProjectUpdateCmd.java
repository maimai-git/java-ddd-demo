package com.example.application.dto.command;

import java.util.List;

public class ProjectUpdateCmd {

    private String name;
    private String description;
    private ProjectCreateCmd.Settings settings;
    private List<ProjectCreateCmd.Tag> tags;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ProjectCreateCmd.Settings getSettings() { return settings; }
    public void setSettings(ProjectCreateCmd.Settings settings) { this.settings = settings; }
    public List<ProjectCreateCmd.Tag> getTags() { return tags; }
    public void setTags(List<ProjectCreateCmd.Tag> tags) { this.tags = tags; }
}
