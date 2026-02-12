package com.campusflow;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 内存任务仓库实现
 * 用于测试环境
 */
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<String, Task> tasks = new HashMap<>();

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public void save(Task task) {
        tasks.put(task.getId(), task);
    }

    public void clear() {
        tasks.clear();
    }
}
