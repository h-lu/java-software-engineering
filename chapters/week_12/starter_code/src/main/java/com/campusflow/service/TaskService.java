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
        // TODO: Validate title, create a Task, and save it.
        throw new UnsupportedOperationException("TODO: implement createTask");
    }
}
