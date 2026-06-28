package com.example.domain.project.model;

import java.time.LocalDate;

public record ProjectSettings(
    Visibility visibility,
    int priority,
    LocalDate deadline
) {
    public enum Visibility { PRIVATE, TEAM, PUBLIC }
}
