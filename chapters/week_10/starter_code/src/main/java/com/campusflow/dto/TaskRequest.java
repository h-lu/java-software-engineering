/*
 * TaskRequest - 创建/更新任务的请求 DTO。
 *
 * DTO（Data Transfer Object）：用于在层之间传输数据的对象。
 * 本类用于接收客户端创建任务的请求数据。
 */
package com.campusflow.dto;

/**
 * 任务请求 DTO。
 *
 * <p>字段说明：
 * <ul>
 *   <li>title: 任务标题（必填）</li>
 *   <li>description: 任务描述（可选）</li>
 *   <li>dueDate: 截止日期，格式 ISO-8601: YYYY-MM-DD（必填）</li>
 * </ul>
 */
public class TaskRequest {
    private String title;
    private String description;
    private String dueDate;

    /**
     * 无参构造（Jackson 需要）。
     */
    public TaskRequest() {
    }

    public TaskRequest(String title, String description, String dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
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
}
