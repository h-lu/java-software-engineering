package edu.campusflow.domain;

import edu.campusflow.exception.InvalidTaskDataException;

/**
 * Week 03 防御式编程骨架。
 */
public class Task {
    private String title;
    private String dueDate;
    private int priority;
    private boolean completed;

    public Task() {
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws InvalidTaskDataException {
        // TODO: null、空字符串、长度 1-100 验证。
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) throws InvalidTaskDataException {
        // TODO: 验证 YYYY-MM-DD 格式，并检查年份不能早于 2000。
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) throws InvalidTaskDataException {
        // TODO: 只允许 1、2、3。
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }
}
