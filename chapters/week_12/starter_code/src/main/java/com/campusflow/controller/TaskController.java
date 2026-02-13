/*
 * TaskController - 任务 HTTP 控制器。
 *
 * Controller 层：处理 HTTP 请求/响应，调用 Service 完成业务操作。
 * 本类实现 CampusFlow 的 REST API 端点。
 */
package com.campusflow.controller;

import com.campusflow.dto.TaskRequest;
import com.campusflow.model.Task;
import com.campusflow.service.TaskService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

/**
 * 任务 HTTP 控制器。
 */
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /tasks - 获取所有任务。
     */
    public void getAllTasks(Context ctx) {
        List<Task> tasks = taskService.findAll();
        ctx.json(Map.of(
            "data", tasks,
            "total", tasks.size()
        ));
    }

    /**
     * GET /tasks/{id} - 获取指定任务。
     */
    public void getTask(Context ctx) {
        String id = ctx.pathParam("id");
        Task task = taskService.findById(id)
            .orElseThrow(() -> new com.campusflow.exception.NotFoundException("Task", id));
        ctx.json(task);
    }

    /**
     * POST /tasks - 创建任务。
     */
    public void createTask(Context ctx) {
        TaskRequest request = ctx.bodyAsClass(TaskRequest.class);
        Task created = taskService.createTask(request);
        ctx.status(201).json(created);
    }

    /**
     * PUT /tasks/{id} - 全量更新任务。
     */
    public void updateTask(Context ctx) {
        String id = ctx.pathParam("id");
        TaskRequest request = ctx.bodyAsClass(TaskRequest.class);
        Task updated = taskService.updateTask(id, request);
        ctx.json(updated);
    }

    /**
     * PATCH /tasks/{id} - 部分更新任务。
     */
    public void patchTask(Context ctx) {
        String id = ctx.pathParam("id");
        // 使用 Map 接收部分字段
        Map<String, Object> updates = ctx.bodyAsClass(Map.class);

        TaskRequest request = new TaskRequest();
        if (updates.containsKey("title")) {
            request.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description")) {
            request.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("dueDate")) {
            request.setDueDate((String) updates.get("dueDate"));
        }

        Task updated = taskService.updateTask(id, request);
        ctx.json(updated);
    }

    /**
     * DELETE /tasks/{id} - 删除任务。
     */
    public void deleteTask(Context ctx) {
        String id = ctx.pathParam("id");
        taskService.deleteTask(id);
        ctx.status(204);
    }

    /**
     * GET /tasks/{id}/overdue-fee - 计算逾期费用。
     */
    public void calculateOverdueFee(Context ctx) {
        String id = ctx.pathParam("id");
        Task task = taskService.findById(id)
            .orElseThrow(() -> new com.campusflow.exception.NotFoundException("Task", id));

        double fee = taskService.calculateOverdueFee(id);
        String strategyName = taskService.getCalculationStrategyName(id);

        ctx.json(Map.of(
            "taskId", id,
            "taskTitle", task.getTitle(),
            "overdueDays", task.getOverdueDays(),
            "fee", fee,
            "calculationStrategy", strategyName
        ));
    }

    /**
     * POST /tasks/{id}/complete - 标记任务完成。
     */
    public void completeTask(Context ctx) {
        String id = ctx.pathParam("id");
        Task completed = taskService.completeTask(id);
        ctx.json(completed);
    }

    /**
     * GET /stats - 获取统计信息。
     */
    public void getStats(Context ctx) {
        ctx.json(taskService.getStats());
    }
}
