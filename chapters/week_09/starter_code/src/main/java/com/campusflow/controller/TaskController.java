package com.campusflow.controller;

import com.campusflow.service.TaskService;

import java.util.Map;

public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public Map<String, Object> getAllTasks() {
        // TODO: In your Javalin route, call taskService.findAll() and return {"data": [...], "total": n}.
        return notImplemented("Implement GET /tasks");
    }

    public Map<String, Object> getTask(String id) {
        // TODO: In your Javalin route, read ctx.pathParam("id") and return 404 when missing.
        return notImplemented("Implement GET /tasks/{id}");
    }

    public Map<String, Object> createTask() {
        // TODO: Parse TaskRequest, validate it, create a task, and return 201.
        return notImplemented("Implement POST /tasks");
    }

    public Map<String, Object> updateTask(String id) {
        // TODO: Replace an existing task or return 404.
        return notImplemented("Implement PUT /tasks/{id}");
    }

    public Map<String, Object> deleteTask(String id) {
        // TODO: Delete an existing task and return 204, or return 404.
        return notImplemented("Implement DELETE /tasks/{id}");
    }

    private Map<String, Object> notImplemented(String todo) {
        return Map.of(
            "error", "NotImplemented",
            "message", todo,
            "nextStep", "Wire this method to a Javalin route"
        );
    }
}
