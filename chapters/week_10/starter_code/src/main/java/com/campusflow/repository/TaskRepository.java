/*
 * TaskRepository - 任务数据访问接口。
 *
 * Repository 模式：将数据访问逻辑与业务逻辑分离。
 * 本接口定义了任务数据的 CRUD 操作。
 */
package com.campusflow.repository;

import com.campusflow.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * 任务 Repository 接口。
 */
public interface TaskRepository {

    /**
     * 保存任务（插入或更新）。
     *
     * @param task 要保存的任务
     * @return 保存后的任务（包含生成的 ID）
     */
    Task save(Task task);

    /**
     * 根据 ID 查找任务。
     *
     * @param id 任务 ID
     * @return 任务 Optional，不存在则返回 empty
     */
    Optional<Task> findById(String id);

    /**
     * 查找所有任务。
     *
     * @return 任务列表
     */
    List<Task> findAll();

    /**
     * 根据状态查找任务。
     *
     * @param status 任务状态
     * @return 符合条件的任务列表
     */
    List<Task> findByStatus(String status);

    /**
     * 删除任务。
     *
     * @param id 任务 ID
     */
    void delete(String id);

    /**
     * 获取任务总数。
     *
     * @return 任务数量
     */
    long count();
}
