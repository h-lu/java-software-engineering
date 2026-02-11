package com.week02;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TaskManager 类 - 任务管理器。
 *
 * <p>职责：管理任务的增删改查（服务类）
 *
 * <p>设计原则：
 * - 单一职责：只负责管理任务，不负责存储数据、验证数据、显示数据
 * - 封装：内部的 tasks 列表是 private 的，外部只能通过方法访问
 * - 防御性拷贝：getAllTasks() 返回列表的拷贝，防止外部修改
 *
 * <p>本周学习目标：
 * - 理解什么是"服务类"（Service Class）
 * - 理解服务类与实体类的区别
 * - 理解单一职责原则（SRP）- TaskManager 只管理，不存储数据本身
 * - 理解"管理者"模式 - 封装对集合的操作
 *
 * <p>TODO: 完成以下内容
 * 1. 实现 addTask() 方法
 * 2. 实现 markCompleted() 方法（按标题查找任务并标记）
 * 3. 实现 getAllTasks() 方法（返回防御性拷贝）
 * 4. 实现 getIncompleteTasks() 方法（过滤未完成任务）
 * 5. （可选）实现 getTasksByPriority() 方法
 */
public class TaskManager {

    // ==================== 字段 ====================

    private final List<Task> tasks = new ArrayList<>();

    // ==================== 核心方法 ====================

    /**
     * 添加一个任务到管理器。
     *
     * @param task 要添加的任务（不能为 null）
     * @throws IllegalArgumentException 如果 task 为 null
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("任务不能为 null");
        }
        tasks.add(task);
    }

    /**
     * 根据标题标记任务为已完成。
     *
     * @param title 任务标题
     * @throws IllegalArgumentException 如果 title 为 null
     */
    public void markCompleted(String title) {
        if (title == null) {
            throw new IllegalArgumentException("任务标题不能为 null");
        }

        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                task.markCompleted();
                break; // 找到第一个匹配的任务就停止
            }
        }
        // 如果没找到，什么都不做（静默失败）
        // 也可以选择抛出异常，取决于设计决策
    }

    /**
     * 获取所有任务（防御性拷贝）。
     *
     * <p>为什么返回拷贝而不是原始列表？
     * - 防止外部代码修改 TaskManager 的内部状态
     * - 例如：manager.getAllTasks().clear() 不应该影响 TaskManager
     *
     * @return 所有任务的列表拷贝
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * 获取所有未完成的任务。
     *
     * @return 未完成任务的列表
     */
    public List<Task> getIncompleteTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * 根据优先级获取任务。
     *
     * @param priority 优先级（1=高, 2=中, 3=低）
     * @return 指定优先级的任务列表
     * @throws IllegalArgumentException 如果优先级不在 1-3 范围内
     */
    public List<Task> getTasksByPriority(int priority) {
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1-3");
        }

        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority() == priority) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * 获取任务总数。
     *
     * @return 任务数量
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * 清空所有任务。
     *
     * <p>注意：这个方法会删除所有任务，慎用！
     */
    public void clearAllTasks() {
        tasks.clear();
    }

    /**
     * 删除指定标题的任务。
     *
     * @param title 任务标题
     * @return true 如果删除成功，false 如果任务不存在
     */
    public boolean removeTask(String title) {
        return tasks.removeIf(task -> task.getTitle().equals(title));
    }

    // ==================== Object 方法 ====================

    @Override
    public String toString() {
        return "TaskManager{" +
                "taskCount=" + tasks.size() +
                ", tasks=" + tasks +
                '}';
    }
}
