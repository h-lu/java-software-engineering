package edu.campusflow.domain;

/**
 * Week 04 任务实体骨架。
 */
public class Task {
    private final String title;
    private final String priority;
    private boolean completed;

    public Task(String title, String priority) {
        // TODO: 添加防御式检查，避免 null 或空标题进入系统。
        this.title = title;
        this.priority = priority;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }
}
