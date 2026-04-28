package com.campusflow.repository;

import com.campusflow.model.Task;

import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {
    @Override
    public List<Task> findAll() {
        // 待办：按插入顺序保存 tasks，并返回防御性副本。
        return List.of();
    }

    @Override
    public Optional<Task> findById(String id) {
        // 待办：按 id 查找 task。
        return Optional.empty();
    }

    @Override
    public Task save(Task task) {
        // 待办：保存新增和更新后的 tasks。
        return task;
    }

    @Override
    public boolean deleteById(String id) {
        // 待办：删除 task，并返回是否真的删除了内容。
        return false;
    }
}
