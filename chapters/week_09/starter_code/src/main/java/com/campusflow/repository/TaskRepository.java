package com.campusflow.repository;

import com.campusflow.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(String id);

    Task save(Task task);

    boolean deleteById(String id);
}
