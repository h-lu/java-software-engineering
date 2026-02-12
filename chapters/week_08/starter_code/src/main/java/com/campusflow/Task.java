package com.campusflow;

import java.util.Objects;

/**
 * 任务实体类
 * 表示 CampusFlow 中的一个任务
 */
public class Task {
    private final String id;
    private final String title;
    private final String description;
    private final String priority;
    private String status;

    public Task(String id, String title, String description, String priority, String status) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("任务ID不能为空");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (priority == null || (!priority.equals("high") && !priority.equals("medium") && !priority.equals("low"))) {
            throw new IllegalArgumentException("优先级必须是 high/medium/low");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status != null ? status : "pending";
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

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
