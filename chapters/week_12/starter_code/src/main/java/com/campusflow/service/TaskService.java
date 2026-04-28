package com.campusflow.service;

import com.campusflow.model.Task;
import com.campusflow.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTask(String id) {
        return repository.findById(id);
    }

    public Task createTask(String title) {
        // 待办：校验 title，创建 Task 并保存。
        throw new UnsupportedOperationException("待办：实现 createTask");
    }
}
