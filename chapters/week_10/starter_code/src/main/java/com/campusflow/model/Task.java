package com.campusflow.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String description;
    private String dueDate;
    private boolean completed;
    private long overdueDays;
    private String createdAt;

    public Task() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = java.time.Instant.now().toString();
    }

    public Task(String title, String description, String dueDate) {
        this();
        this.title = title;
        this.description = description == null ? "" : description;
        this.dueDate = dueDate;
        recalculateOverdueDays();
    }

    public void recalculateOverdueDays() {
        if (completed || dueDate == null || dueDate.isBlank()) {
            overdueDays = 0;
            return;
        }
        try {
            LocalDate due = LocalDate.parse(dueDate);
            overdueDays = Math.max(0, ChronoUnit.DAYS.between(due, LocalDate.now()));
        } catch (DateTimeParseException ignored) {
            overdueDays = 0;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(long overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
