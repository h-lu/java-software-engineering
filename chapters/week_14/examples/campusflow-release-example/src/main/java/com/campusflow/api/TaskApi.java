/*
 * 示例：任务 REST API
 * 功能：提供任务的 CRUD 操作（GET/POST/PUT/DELETE）
 * 运行方式：被 Main 类注册到 Javalin 服务器
 * 预期输出：提供 /api/tasks 等 HTTP 端点
 */
package com.campusflow.api;

import com.campusflow.model.Task;
import com.campusflow.repository.TaskRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.javalin.http.Context;

import java.util.List;

/**
 * 任务 REST API
 * 提供 CRUD 操作的 HTTP 端点
 */
public class TaskApi {
    private final TaskRepository taskRepository;
    private final Gson gson = new Gson();

    public TaskApi(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void registerRoutes(io.javalin.Javalin app, String basePath) {
        // 获取所有任务
        app.get(basePath + "/tasks", this::getAllTasks);

        // 获取单个任务
        app.get(basePath + "/tasks/{id}", this::getTaskById);

        // 创建任务
        app.post(basePath + "/tasks", this::createTask);

        // 更新任务
        app.put(basePath + "/tasks/{id}", this::updateTask);

        // 删除任务
        app.delete(basePath + "/tasks/{id}", this::deleteTask);

        // 标记任务完成
        app.patch(basePath + "/tasks/{id}/complete", this::markTaskComplete);
    }

    private void getAllTasks(Context ctx) {
        List<Task> tasks = taskRepository.findAll();
        ctx.json(tasks);
    }

    private void getTaskById(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
        ctx.json(task);
    }

    private void createTask(Context ctx) {
        Task task = gson.fromJson(ctx.body(), Task.class);
        Task savedTask = taskRepository.save(task);
        ctx.status(201);
        ctx.json(savedTask);
    }

    private void updateTask(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Task task = gson.fromJson(ctx.body(), Task.class);
        task.setId(id);
        Task updatedTask = taskRepository.save(task);
        ctx.json(updatedTask);
    }

    private void deleteTask(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        taskRepository.deleteById(id);
        ctx.status(204);
    }

    private void markTaskComplete(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);
        ctx.json(updatedTask);
    }
}
