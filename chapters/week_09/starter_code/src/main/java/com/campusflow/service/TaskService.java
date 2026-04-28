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
        // TODO: Validate title, parse dueDate, create Task, and save it.
        throw new UnsupportedOperationException("TODO: implement createTask");
    }

    public Task updateTask(String id, TaskRequest request) {
        // TODO: Return NotFoundException when id does not exist.
        throw new UnsupportedOperationException("TODO: implement updateTask");
    }

    public void deleteTask(String id) {
        // TODO: Delete existing task or throw NotFoundException.
        throw new UnsupportedOperationException("TODO: implement deleteTask");
    }
}
