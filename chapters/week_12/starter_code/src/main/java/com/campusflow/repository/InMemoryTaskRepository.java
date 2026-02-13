/*
 * InMemoryTaskRepository - 内存版任务 Repository 实现。
 *
 * 使用 ConcurrentHashMap 存储任务，线程安全。
 * 适用于开发和测试环境。
 */
package com.campusflow.repository;

import com.campusflow.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 内存版任务 Repository 实现。
 */
public class InMemoryTaskRepository implements TaskRepository {

    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(String.valueOf(nextId.getAndIncrement()));
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> findByStatus(String status) {
        return tasks.values().stream()
            .filter(t -> status.equals(t.getStatus()))
            .toList();
    }

    @Override
    public void delete(String id) {
        tasks.remove(id);
    }

    @Override
    public long count() {
        return tasks.size();
    }
}
