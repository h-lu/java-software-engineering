package com.campusflow.repository;

import com.campusflow.model.Task;

import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {
    @Override
    public List<Task> findAll() {
        // TODO: Implement storage for integration tests.
        return List.of();
    }

    @Override
    public Optional<Task> findById(String id) {
        // TODO: Implement lookup by id.
        return Optional.empty();
    }

    @Override
    public Task save(Task task) {
        // TODO: Assign ids and store tasks.
        return task;
    }
}
