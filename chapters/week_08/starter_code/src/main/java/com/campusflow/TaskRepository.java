package com.campusflow;

import java.util.Optional;

/**
 * 任务仓库接口
 * 定义数据访问操作
 */
public interface TaskRepository {
    Optional<Task> findById(String id);
    void save(Task task);
}
