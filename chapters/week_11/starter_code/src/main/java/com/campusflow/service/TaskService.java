/*
 * TaskService - 任务业务逻辑服务。
 *
 * Service 层：封装业务逻辑，协调 Repository 完成业务操作。
 * 本类实现 CampusFlow 的核心业务功能。
 */
package com.campusflow.service;

import com.campusflow.dto.TaskRequest;
import com.campusflow.exception.NotFoundException;
import com.campusflow.exception.ValidationException;
import com.campusflow.model.Task;
import com.campusflow.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 任务业务逻辑服务。
 */
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * 创建新任务。
     *
     * @param request 创建任务请求
     * @return 创建后的任务
     * @throws ValidationException 如果请求数据无效
     */
    public Task createTask(TaskRequest request) {
        // 验证标题
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new ValidationException("title", "Title is required");
        }

        // 验证并解析日期
        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(request.getDueDate());
        } catch (Exception e) {
            throw new ValidationException("dueDate", "Invalid format, expected: YYYY-MM-DD");
        }

        Task task = new Task(null, request.getTitle(), request.getDescription(), dueDate);
        return repository.save(task);
    }

    /**
     * 根据 ID 查找任务。
     *
     * @param id 任务 ID
     * @return 任务 Optional
     */
    public Optional<Task> findById(String id) {
        return repository.findById(id);
    }

    /**
     * 查找所有任务。
     *
     * @return 任务列表
     */
    public List<Task> findAll() {
        return repository.findAll();
    }

    /**
     * 更新任务。
     *
     * @param id      任务 ID
     * @param request 更新请求
     * @return 更新后的任务
     * @throws NotFoundException   如果任务不存在
     * @throws ValidationException 如果请求数据无效
     */
    public Task updateTask(String id, TaskRequest request) {
        Task existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task", id));

        // 只更新提供的字段
        if (request.getTitle() != null) {
            if (request.getTitle().trim().isEmpty()) {
                throw new ValidationException("title", "Title cannot be empty");
            }
            existing.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }

        if (request.getDueDate() != null) {
            try {
                existing.setDueDate(LocalDate.parse(request.getDueDate()));
            } catch (Exception e) {
                throw new ValidationException("dueDate", "Invalid format, expected: YYYY-MM-DD");
            }
        }

        return repository.save(existing);
    }

    /**
     * 删除任务。
     *
     * @param id 任务 ID
     * @throws NotFoundException 如果任务不存在
     */
    public void deleteTask(String id) {
        Task existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task", id));
        repository.delete(id);
    }

    /**
     * 标记任务为已完成。
     *
     * @param id 任务 ID
     * @return 完成后的任务
     * @throws NotFoundException 如果任务不存在
     */
    public Task completeTask(String id) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task", id));

        task.markCompleted();
        return repository.save(task);
    }

    /**
     * 计算逾期费用。
     *
     * <p>使用策略模式根据逾期天数选择不同费率：
     * <ul>
     *   <li>1-3 天：每天 10 元</li>
     *   <li>4-7 天：前 3 天每天 10 元，之后每天 20 元</li>
     *   <li>8 天以上：前 3 天每天 10 元，4-7 天每天 20 元，之后每天 50 元</li>
     * </ul>
     *
     * @param id 任务 ID
     * @return 逾期费用
     * @throws NotFoundException 如果任务不存在
     */
    public double calculateOverdueFee(String id) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task", id));

        if (!task.isOverdue()) {
            return 0.0;
        }

        long overdueDays = task.getOverdueDays();
        return calculateFeeByDays(overdueDays);
    }

    /**
     * 获取计算逾期费用时使用的策略名称。
     *
     * @param id 任务 ID
     * @return 策略名称
     */
    public String getCalculationStrategyName(String id) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task", id));

        if (!task.isOverdue()) {
            return "NoOverdueStrategy";
        }

        long overdueDays = task.getOverdueDays();
        if (overdueDays <= 3) {
            return "StandardFeeStrategy";
        } else if (overdueDays <= 7) {
            return "EscalatingFeeStrategy";
        } else {
            return "SevereFeeStrategy";
        }
    }

    /**
     * 根据逾期天数计算费用。
     */
    private double calculateFeeByDays(long overdueDays) {
        if (overdueDays <= 3) {
            // 标准费率
            return overdueDays * 10.0;
        } else if (overdueDays <= 7) {
            // 递升费率
            return 3 * 10.0 + (overdueDays - 3) * 20.0;
        } else {
            // 严厉费率
            return 3 * 10.0 + 4 * 20.0 + (overdueDays - 7) * 50.0;
        }
    }

    /**
     * 获取统计信息。
     *
     * @return 统计数据
     */
    public Map<String, Object> getStats() {
        List<Task> all = repository.findAll();
        long total = all.size();
        long pending = all.stream().filter(t -> "pending".equals(t.getStatus())).count();
        long inProgress = all.stream().filter(t -> "in_progress".equals(t.getStatus())).count();
        long completed = all.stream().filter(t -> "completed".equals(t.getStatus())).count();
        long overdue = all.stream().filter(Task::isOverdue).count();

        return Map.of(
            "total", total,
            "pending", pending,
            "inProgress", inProgress,
            "completed", completed,
            "overdue", overdue
        );
    }
}
