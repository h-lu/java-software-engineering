/*
 * 示例：策略模式在任务状态流转中的应用。
 * 本例演示：策略模式（Strategy Pattern）如何消除复杂的条件判断。
 * 运行方式：javac 03_strategy_pattern_task_status.java && java StrategyPatternDemo
 * 预期输出：展示策略模式如何封装不同的状态流转算法
 */

import java.util.*;

// 文件：Task.java（任务实体）
class TaskV3 {
    private final String id;
    private String title;
    private TaskStatus status;

    public TaskV3(String id, String title) {
        this.id = id;
        this.title = title;
        this.status = TaskStatus.PENDING;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
}

// 文件：TaskStatus.java（状态枚举）
enum TaskStatus {
    PENDING("待处理"),
    IN_PROGRESS("进行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

// 文件：TaskStatusStrategy.java（策略接口）
// 策略模式核心：定义一系列算法，把它们一个个封装起来，并且使它们可以互相替换
interface TaskStatusStrategy {
    // 是否可以执行状态流转
    boolean canTransition(TaskV3 task, TaskStatus newStatus);

    // 执行状态流转
    void transition(TaskV3 task, TaskStatus newStatus);

    // 获取当前策略处理的状态
    TaskStatus getCurrentStatus();

    // 获取允许的目标状态列表
    List<TaskStatus> getAllowedTransitions();
}

// 文件：PendingStrategy.java（具体策略：待处理状态）
// 待处理状态可以流转到：进行中、已完成（直接完成）、已取消
class PendingStrategy implements TaskStatusStrategy {

    @Override
    public boolean canTransition(TaskV3 task, TaskStatus newStatus) {
        return newStatus == TaskStatus.IN_PROGRESS
            || newStatus == TaskStatus.COMPLETED
            || newStatus == TaskStatus.CANCELLED;
    }

    @Override
    public void transition(TaskV3 task, TaskStatus newStatus) {
        if (!canTransition(null, newStatus)) {
            throw new IllegalStateException(
                String.format("无法从 %s 流转到 %s", TaskStatus.PENDING, newStatus)
            );
        }

        task.setStatus(newStatus);

        // 执行状态特定的业务逻辑
        switch (newStatus) {
            case IN_PROGRESS -> System.out.println("  → 任务开始执行，已通知负责人");
            case COMPLETED -> System.out.println("  → 任务直接完成，记录快速处理");
            case CANCELLED -> System.out.println("  → 任务已取消，释放资源");
            default -> { }
        }
    }

    @Override
    public TaskStatus getCurrentStatus() {
        return TaskStatus.PENDING;
    }

    @Override
    public List<TaskStatus> getAllowedTransitions() {
        return List.of(TaskStatus.IN_PROGRESS, TaskStatus.COMPLETED, TaskStatus.CANCELLED);
    }
}

// 文件：InProgressStrategy.java（具体策略：进行中状态）
// 进行中状态可以流转到：已完成、已取消（中止）
class InProgressStrategy implements TaskStatusStrategy {

    @Override
    public boolean canTransition(TaskV3 task, TaskStatus newStatus) {
        return newStatus == TaskStatus.COMPLETED
            || newStatus == TaskStatus.CANCELLED;
    }

    @Override
    public void transition(TaskV3 task, TaskStatus newStatus) {
        if (!canTransition(null, newStatus)) {
            throw new IllegalStateException(
                String.format("无法从 %s 流转到 %s", TaskStatus.IN_PROGRESS, newStatus)
            );
        }

        task.setStatus(newStatus);

        switch (newStatus) {
            case COMPLETED -> System.out.println("  → 任务完成，发送完成通知，更新统计");
            case CANCELLED -> System.out.println("  → 任务中止，记录中止原因，回滚已执行操作");
            default -> { }
        }
    }

    @Override
    public TaskStatus getCurrentStatus() {
        return TaskStatus.IN_PROGRESS;
    }

    @Override
    public List<TaskStatus> getAllowedTransitions() {
        return List.of(TaskStatus.COMPLETED, TaskStatus.CANCELLED);
    }
}

// 文件：CompletedStrategy.java（具体策略：已完成状态）
// 已完成状态是终态，不允许流转
class CompletedStrategy implements TaskStatusStrategy {

    @Override
    public boolean canTransition(TaskV3 task, TaskStatus newStatus) {
        return false; // 已完成是终态
    }

    @Override
    public void transition(TaskV3 task, TaskStatus newStatus) {
        throw new IllegalStateException("已完成状态不允许流转到其他状态");
    }

    @Override
    public TaskStatus getCurrentStatus() {
        return TaskStatus.COMPLETED;
    }

    @Override
    public List<TaskStatus> getAllowedTransitions() {
        return List.of(); // 空列表
    }
}

// 文件：CancelledStrategy.java（具体策略：已取消状态）
// 已取消状态是终态，不允许流转
class CancelledStrategy implements TaskStatusStrategy {

    @Override
    public boolean canTransition(TaskV3 task, TaskStatus newStatus) {
        return false; // 已取消是终态
    }

    @Override
    public void transition(TaskV3 task, TaskStatus newStatus) {
        throw new IllegalStateException("已取消状态不允许流转到其他状态");
    }

    @Override
    public TaskStatus getCurrentStatus() {
        return TaskStatus.CANCELLED;
    }

    @Override
    public List<TaskStatus> getAllowedTransitions() {
        return List.of(); // 空列表
    }
}

// 文件：TaskStatusContext.java（策略上下文）
// 上下文类：维护对策略对象的引用，并委托执行
class TaskStatusContext {
    private final List<TaskStatusStrategy> strategies;

    public TaskStatusContext(List<TaskStatusStrategy> strategies) {
        this.strategies = strategies;
    }

    // 根据当前状态找到对应的策略
    private TaskStatusStrategy findStrategy(TaskStatus currentStatus) {
        return strategies.stream()
            .filter(s -> s.getCurrentStatus() == currentStatus)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("未知状态: " + currentStatus));
    }

    // 执行状态流转
    public void transition(TaskV3 task, TaskStatus newStatus) {
        TaskStatusStrategy strategy = findStrategy(task.getStatus());
        strategy.transition(task, newStatus);
    }

    // 检查是否可以流转
    public boolean canTransition(TaskV3 task, TaskStatus newStatus) {
        TaskStatusStrategy strategy = findStrategy(task.getStatus());
        return strategy.canTransition(null, newStatus);
    }

    // 获取允许的目标状态
    public List<TaskStatus> getAllowedTransitions(TaskV3 task) {
        TaskStatusStrategy strategy = findStrategy(task.getStatus());
        return strategy.getAllowedTransitions();
    }
}

// 文件：StrategyPatternDemo.java（演示入口）
class StrategyPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== 策略模式：任务状态流转演示 ===\n");

        // 创建策略列表
        List<TaskStatusStrategy> strategies = List.of(
            new PendingStrategy(),
            new InProgressStrategy(),
            new CompletedStrategy(),
            new CancelledStrategy()
        );

        // 创建上下文
        TaskStatusContext context = new TaskStatusContext(strategies);

        // 创建任务
        TaskV3 task = new TaskV3("TASK-001", "完成 Week 08 作业");
        System.out.println("创建任务: " + task.getTitle());
        System.out.println("当前状态: " + task.getStatus().getDisplayName());
        System.out.println("允许流转到: " + context.getAllowedTransitions(task));
        System.out.println();

        // 演示 1：待处理 -> 进行中
        System.out.println("操作：将任务设为进行中");
        context.transition(task, TaskStatus.IN_PROGRESS);
        System.out.println("当前状态: " + task.getStatus().getDisplayName());
        System.out.println("允许流转到: " + context.getAllowedTransitions(task));
        System.out.println();

        // 演示 2：进行中 -> 已完成
        System.out.println("操作：将任务设为已完成");
        context.transition(task, TaskStatus.COMPLETED);
        System.out.println("当前状态: " + task.getStatus().getDisplayName());
        System.out.println("允许流转到: " + context.getAllowedTransitions(task));
        System.out.println();

        // 演示 3：尝试非法流转（已完成 -> 进行中）
        System.out.println("操作：尝试将已完成的任务重新设为进行中（非法操作）");
        try {
            context.transition(task, TaskStatus.IN_PROGRESS);
        } catch (IllegalStateException e) {
            System.out.println("错误捕获: " + e.getMessage());
        }
        System.out.println();

        // 演示 4：另一个任务 - 待处理直接完成
        TaskV3 task2 = new TaskV3("TASK-002", "简单任务");
        System.out.println("创建新任务: " + task2.getTitle());
        System.out.println("当前状态: " + task2.getStatus().getDisplayName());
        System.out.println("操作：直接设为已完成");
        context.transition(task2, TaskStatus.COMPLETED);
        System.out.println("当前状态: " + task2.getStatus().getDisplayName());
        System.out.println();

        // 演示 5：待处理 -> 取消
        TaskV3 task3 = new TaskV3("TASK-003", "废弃任务");
        System.out.println("创建新任务: " + task3.getTitle());
        System.out.println("操作：设为已取消");
        context.transition(task3, TaskStatus.CANCELLED);
        System.out.println("当前状态: " + task3.getStatus().getDisplayName());
        System.out.println();

        System.out.println("=== 策略模式的优势 ===");
        System.out.println("1. 消除条件判断：不再有一长串 if-else 或 switch");
        System.out.println("2. 易于扩展：添加新状态只需实现新策略类");
        System.out.println("3. 单一职责：每个策略类只负责一种状态的处理逻辑");
        System.out.println("4. 开闭原则：对扩展开放（添加新状态），对修改关闭（不改现有代码）");
        System.out.println("5. 可测试性：每个策略可以独立测试");
    }
}
