package edu.campusflow.model;

import java.time.Instant;
import java.util.Objects;

public class Task {
    private final String id;
    private final String title;
    private final String description;
    private String status;
    private final Instant createdAt;

    public Task(String id, String title, String description, String status) {
        this(id, title, description, status, Instant.now());
    }

    public Task(String id, String title, String description, String status, Instant createdAt) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Task id cannot be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title cannot be blank");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status == null || status.isBlank() ? "pending" : status;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Task status cannot be blank");
        }
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Task task)) {
            return false;
        }
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
