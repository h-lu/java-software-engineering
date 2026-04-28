package com.campusflow.repository;

import com.campusflow.model.Task;

import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {
    @Override
    public List<Task> findAll() {
        // TODO: Store tasks in insertion order and return a defensive copy.
        return List.of();
    }

    @Override
    public Optional<Task> findById(String id) {
        // TODO: Look up the task by id.
        return Optional.empty();
    }

    @Override
    public Task save(Task task) {
        // TODO: Save new and updated tasks.
        return task;
    }

    @Override
    public boolean deleteById(String id) {
        // TODO: Remove the task and report whether anything was deleted.
        return false;
    }
}
