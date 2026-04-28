package com.campusflow.service;

import com.campusflow.dto.TaskRequest;
import com.campusflow.model.Task;
import com.campusflow.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Optional<Task> findById(String id) {
        return repository.findById(id);
    }

    public Task createTask(TaskRequest request) {
        // 待办：校验 title，解析 dueDate，创建 Task 并保存。
        throw new UnsupportedOperationException("待办：实现 createTask");
    }

    public Task updateTask(String id, TaskRequest request) {
        // 待办：id 不存在时返回 NotFoundException。
        throw new UnsupportedOperationException("待办：实现 updateTask");
    }

    public void deleteTask(String id) {
        // 待办：删除已有 task；不存在时抛出 NotFoundException。
        throw new UnsupportedOperationException("待办：实现 deleteTask");
    }
}
