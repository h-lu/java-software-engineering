package com.campusflow.repository;

import com.campusflow.model.Task;

import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {
    @Override
    public List<Task> findAll() {
        // 待办：为 integration tests 实现存储逻辑。
        return List.of();
    }

    @Override
    public Optional<Task> findById(String id) {
        // 待办：实现按 id 查询。
        return Optional.empty();
    }

    @Override
    public Task save(Task task) {
        // 待办：分配 id 并保存 tasks。
        return task;
    }
}
