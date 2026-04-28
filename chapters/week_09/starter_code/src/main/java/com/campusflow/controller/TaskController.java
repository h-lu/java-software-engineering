package com.campusflow.controller;

import com.campusflow.service.TaskService;

import java.util.Map;

public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public Map<String, Object> getAllTasks() {
        // 待办：在 Javalin route 中调用 taskService.findAll()，并返回 {"data": [...], "total": n}。
        return notImplemented("实现 GET /tasks");
    }

    public Map<String, Object> getTask(String id) {
        // 待办：在 Javalin route 中读取 ctx.pathParam("id")，找不到时返回 404。
        return notImplemented("实现 GET /tasks/{id}");
    }

    public Map<String, Object> createTask() {
        // 待办：解析 TaskRequest，完成校验，创建 task，并返回 201。
        return notImplemented("实现 POST /tasks");
    }

    public Map<String, Object> updateTask(String id) {
        // 待办：替换已有 task；不存在时返回 404。
        return notImplemented("实现 PUT /tasks/{id}");
    }

    public Map<String, Object> deleteTask(String id) {
        // 待办：删除已有 task 并返回 204；不存在时返回 404。
        return notImplemented("实现 DELETE /tasks/{id}");
    }

    private Map<String, Object> notImplemented(String todo) {
        return Map.of(
            "error", "NotImplemented",
            "message", todo,
            "nextStep", "把这个方法接到 Javalin route 上"
        );
    }
}
