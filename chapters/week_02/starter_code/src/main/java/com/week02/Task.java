package com.week02;

/**
 * Task 类 - 任务实体类。
 *
 * <p>职责：存储任务的数据（标题、描述、优先级、完成状态）
 *
 * <p>设计原则：
 * - 封装：所有字段都是 private，外部通过 getter/setter 访问
 * - 单一职责：只负责存储数据，不负责验证、持久化、显示
 * - 不变性：某些字段（如 ID）可以是 final，创建后不可变
 *
 * <p>本周学习目标：
 * - 理解什么是"实体类"（Entity Class）
 * - 理解为什么要封装（private 字段 + public 方法）
 * - 理解 getter/setter 的作用（控制访问、验证数据）
 * - 理解对象与类的区别（每个对象有独立状态）
 *
 * <p>TODO: 完成以下内容
 * 1. 添加私有字段：title, description, priority, completed
 * 2. 实现构造方法
 * 3. 实现 getter 和 setter（带验证）
 * 4. 实现 markCompleted() 方法
 * 5. （可选）添加更多字段，如 dueDate, tags
 */
public class Task {

    // ==================== 字段（全部 private）====================

    private String title;           // 任务标题
    private String description;     // 任务描述
    private int priority;           // 优先级：1=高, 2=中, 3=低
    private boolean completed;      // 完成状态

    // ==================== 构造方法 ====================

    /**
     * 创建一个新任务。
     *
     * @param title 任务标题（不能为空）
     */
    public Task(String title) {
        this.title = title;
        this.completed = false;    // 默认未完成
        this.priority = 2;         // 默认中优先级
        this.description = "";     // 默认空描述
    }

    /**
     * 创建一个完整描述的任务。
     *
     * @param title 任务标题
     * @param description 任务描述
     */
    public Task(String title, String description) {
        this(title);
        this.description = description;
    }

    // ==================== Getter 方法 ====================

    /**
     * 获取任务标题。
     *
     * @return 任务标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 获取任务描述。
     *
     * @return 任务描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取优先级。
     *
     * @return 优先级（1=高, 2=中, 3=低）
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 获取完成状态。
     *
     * @return true 如果已完成，false 如果未完成
     */
    public boolean isCompleted() {
        return completed;
    }

    // ==================== Setter 方法（带验证）====================

    /**
     * 设置任务标题。
     *
     * @param title 新标题（不能为 null 或空字符串）
     * @throws IllegalArgumentException 如果标题为 null 或空字符串
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        this.title = title;
    }

    /**
     * 设置任务描述。
     *
     * @param description 新描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 设置优先级。
     *
     * @param priority 优先级（必须是 1-3）
     * @throws IllegalArgumentException 如果优先级不在 1-3 范围内
     */
    public void setPriority(int priority) {
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1-3");
        }
        this.priority = priority;
    }

    // ==================== 业务方法 ====================

    /**
     * 标记任务为已完成。
     */
    public void markCompleted() {
        this.completed = true;
    }

    /**
     * 重置任务为未完成状态。
     */
    public void markIncomplete() {
        this.completed = false;
    }

    // ==================== Object 方法 ====================

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", completed=" + completed +
                ", priority=" + priority +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Task task = (Task) obj;
        return title.equals(task.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
