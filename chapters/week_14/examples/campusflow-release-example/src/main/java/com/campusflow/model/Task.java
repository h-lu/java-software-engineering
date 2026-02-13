/*
 * 示例：任务实体类
 * 功能：定义任务的数据结构（属性和 getter/setter）
 * 运行方式：被 TaskRepository 和 TaskApi 调用
 * 预期输出：提供任务数据的 Java 对象表示
 */
package com.campusflow.model;

import java.time.LocalDateTime;

/**
 * 任务实体类
 */
public class Task {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

    public Task() {
        this.createdAt = LocalDateTime.now();
        this.completed = false;
    }

    public Task(Long id, String title, String description) {
        this();
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
}
