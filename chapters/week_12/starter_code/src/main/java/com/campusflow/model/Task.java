/*
 * Task 实体类 - CampusFlow 核心领域对象。
 *
 * 本类对应 Week 09 中的任务概念，用于表示一个待办事项。
 */
package com.campusflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 任务实体类。
 *
 * <p>一个任务包含：
 * <ul>
 *   <li>基本信息：标题、描述</li>
 *   <li>时间信息：截止日期、创建时间、完成时间</li>
 *   <li>状态：pending（待办）、in_progress（进行中）、completed（已完成）</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    /**
     * 无参构造（Jackson 反序列化需要）。
     */
    public Task() {
    }

    /**
     * 创建新任务。
     *
     * @param id          任务 ID
     * @param title       任务标题
     * @param description 任务描述
     * @param dueDate     截止日期
     */
    public Task(String id, String title, String description, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = "pending";
        this.createdAt = LocalDateTime.now();
    }

    // Getters 和 Setters

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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * 检查任务是否已逾期。
     *
     * @return true 如果任务未完成且已逾期
     */
    public boolean isOverdue() {
        return !"completed".equals(status) && dueDate.isBefore(LocalDate.now());
    }

    /**
     * 获取逾期天数。
     *
     * @return 逾期天数，如果未逾期返回 0
     */
    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * 标记任务为已完成。
     */
    public void markCompleted() {
        this.status = "completed";
        this.completedAt = LocalDateTime.now();
    }
}
