package edu.campusflow;

/**
 * Task 类 - 任务实体类。
 *
 * <p>职责：存储任务的数据（标题、描述、优先级、完成状态）
 *
 * <p>设计原则：
 * - 封装：所有字段都是 private，外部通过 getter/setter 访问
 * - 单一职责：只负责存储数据，不负责验证、持久化、显示
 * - 防御式编程：setter 方法包含输入验证
 */
public class Task {

    // ==================== 字段（全部 private）====================

    private String title;           // 任务标题
    private String description;     // 任务描述
    private int priority;           // 优先级：1=高, 2=中, 3=低, 4, 5
    private boolean completed;      // 完成状态

    // ==================== 构造方法 ====================

    /**
     * 创建一个新任务。
     *
     * @param title 任务标题（不能为空）
     * @throws IllegalArgumentException 如果标题为 null 或空字符串
     */
    public Task(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        this.title = title;
        this.completed = false;    // 默认未完成
        this.priority = 3;         // 默认中优先级（3）
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
     * @return 优先级（1=高, 2-5=中到低）
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
     * @param priority 优先级（必须是 1-5）
     * @throws IllegalArgumentException 如果优先级不在 1-5 范围内
     */
    public void setPriority(int priority) {
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("优先级必须是 1-5");
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
