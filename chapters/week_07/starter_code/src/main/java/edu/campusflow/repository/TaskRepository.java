package edu.campusflow.repository;

import edu.campusflow.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void save(Task task);

    Optional<Task> findById(String id);

    List<Task> findAll();

    void delete(String id);
}
