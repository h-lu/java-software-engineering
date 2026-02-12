package com.campusflow;

import java.util.List;

/**
 * 任务服务类（重构后版本）
 * 使用策略模式处理费用计算
 */
public class TaskService {
    private final TaskRepository taskRepository;
    private final List<FeeCalculationStrategy> feeStrategies;

    public TaskService(TaskRepository taskRepository, List<FeeCalculationStrategy> feeStrategies) {
        this.taskRepository = taskRepository;
        this.feeStrategies = feeStrategies;
    }

    /**
     * 创建任务
     *
     * @param title       标题
     * @param description 描述
     * @param priority    优先级
     * @return 创建的任务
     */
    public Task createTask(String title, String description, String priority) {
        validateTaskInput(title, priority);
        String id = generateTaskId();
        Task task = new Task(id, title, description, priority, "pending");
        taskRepository.save(task);
        return task;
    }

    /**
     * 计算逾期费用
     *
     * @param taskId       任务ID
     * @param daysOverdue  逾期天数
     * @return 逾期费用
     * @throws IllegalArgumentException 如果任务不存在
     */
    public double calculateOverdueFee(String taskId, int daysOverdue) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("任务不存在: " + taskId));

        FeeCalculationStrategy strategy = feeStrategies.stream()
                .filter(s -> s.supports(task.getPriority()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("不支持的优先级: " + task.getPriority()));

        return strategy.calculateFee(daysOverdue);
    }

    /**
     * 验证任务输入
     */
    private void validateTaskInput(String title, String priority) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (priority == null || (!priority.equals("high") && !priority.equals("medium") && !priority.equals("low"))) {
            throw new IllegalArgumentException("优先级必须是 high/medium/low");
        }
    }

    /**
     * 生成任务ID
     */
    private String generateTaskId() {
        return "TASK-" + System.currentTimeMillis();
    }
}
