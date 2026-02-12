package edu.campusflow;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskManager 类 - 任务管理器。
 *
 * <p>职责：管理任务的增删改查（服务类）
 *
 * <p>设计原则：
 * - 单一职责：只负责管理任务，不负责存储数据、验证数据、显示数据
 * - 封装：内部的 tasks 列表是 private 的，外部只能通过方法访问
 * - 防御性拷贝：getAllTasks() 返回列表的拷贝，防止外部修改
 */
public class TaskManager {

    // ==================== 字段 ====================

    private final List<Task> tasks = new ArrayList<>();
    private TaskRepository repository;
    private boolean useRepository = false;

    // ==================== 构造方法 ====================

    /**
     * 创建一个新的 TaskManager。
     */
    public TaskManager() {
        // 默认构造方法，使用内存列表存储
        this.useRepository = false;
    }

    /**
     * 创建 TaskManager 并关联 Repository（Week 04 新增）。
     *
     * @param repository 任务存储仓库
     */
    public TaskManager(TaskRepository repository) {
        this.repository = repository;
        this.useRepository = true;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 获取当前使用的任务列表（来自内存或 Repository）。
     */
    private List<Task> getTaskList() {
        if (useRepository && repository != null) {
            return repository.findAll();
        }
        return tasks;
    }

    /**
     * 将任务添加到当前存储（内存列表或 Repository）。
     */
    private void addTaskToStorage(Task task) {
        if (useRepository && repository != null) {
            repository.save(task);
        } else {
            tasks.add(task);
        }
    }

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
        addTaskToStorage(task);
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

        List<Task> taskList = getTaskList();
        for (Task task : taskList) {
            if (task.getTitle().equals(title)) {
                task.markCompleted();
                // 如果使用 Repository，需要更新保存
                if (useRepository && repository != null) {
                    repository.save(task);
                }
                break; // 找到第一个匹配的任务就停止
            }
        }
        // 如果没找到，什么都不做（静默失败）
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
        return new ArrayList<>(getTaskList());
    }

    /**
     * 获取所有未完成的任务。
     *
     * @return 未完成任务的列表
     */
    public List<Task> getIncompleteTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : getTaskList()) {
            if (!task.isCompleted()) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * 根据优先级获取任务。
     *
     * @param priority 优先级（1=高, 2-5=中到低）
     * @return 指定优先级的任务列表
     * @throws IllegalArgumentException 如果优先级不在 1-5 范围内
     */
    public List<Task> getTasksByPriority(int priority) {
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("优先级必须是 1-5");
        }

        List<Task> result = new ArrayList<>();
        for (Task task : getTaskList()) {
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
        if (useRepository && repository != null) {
            return repository.count();
        }
        return tasks.size();
    }

    /**
     * 清空所有任务。
     *
     * <p>注意：这个方法会删除所有任务，慎用！
     */
    public void clearAllTasks() {
        if (useRepository && repository != null) {
            for (Task task : repository.findAll()) {
                repository.deleteByTitle(task.getTitle());
            }
        } else {
            tasks.clear();
        }
    }

    /**
     * 删除指定标题的任务。
     *
     * @param title 任务标题
     * @return true 如果删除成功，false 如果任务不存在
     */
    public boolean removeTask(String title) {
        if (useRepository && repository != null) {
            Task task = repository.findByTitle(title);
            if (task != null) {
                repository.deleteByTitle(title);
                return true;
            }
            return false;
        }
        return tasks.removeIf(task -> task.getTitle().equals(title));
    }

    // ==================== Week 04 新增：筛选功能 ====================

    /**
     * 按优先级筛选任务（Week 04 Code Review 示例）。
     *
     * @param priority 优先级字符串（"高"/"中"/"低" 或 "1"/"2"/"3"/"4"/"5"）
     * @return 符合条件的任务列表
     * @throws IllegalArgumentException 如果 priority 为 null
     */
    public List<Task> filterByPriority(String priority) {
        if (priority == null) {
            throw new IllegalArgumentException("优先级不能为 null");
        }

        int priorityValue = parsePriority(priority);

        List<Task> result = new ArrayList<>();
        for (Task task : getTaskList()) {
            if (task.getPriority() == priorityValue) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * 按完成状态筛选任务（Week 04 Code Review 示例）。
     *
     * @param completed true 表示已完成，false 表示未完成
     * @return 符合条件的任务列表
     */
    public List<Task> filterByStatus(boolean completed) {
        List<Task> result = new ArrayList<>();
        for (Task task : getTaskList()) {
            if (task.isCompleted() == completed) {
                result.add(task);
            }
        }
        return result;
    }

    // ==================== 辅助方法 ====================

    /**
     * 解析优先级字符串为数值。
     *
     * @param priority 优先级字符串
     * @return 优先级数值（1-5）
     */
    private int parsePriority(String priority) {
        switch (priority) {
            case "高":
            case "1":
                return 1;
            case "中高":
            case "2":
                return 2;
            case "中":
            case "3":
                return 3;
            case "中低":
            case "4":
                return 4;
            case "低":
            case "5":
                return 5;
            default:
                // 尝试解析为数字
                try {
                    int value = Integer.parseInt(priority);
                    if (value >= 1 && value <= 5) {
                        return value;
                    }
                } catch (NumberFormatException e) {
                    // 忽略解析错误
                }
                return 3; // 默认返回中优先级
        }
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

/**
 * TaskRepository 接口 - 数据存储契约（ADR-002）。
 *
 * <p>定义任务存储的标准接口，支持不同实现：
 * - InMemoryTaskRepository：内存存储（过渡方案）
 * - FileTaskRepository：文件存储（Week 05）
 * - SQLiteTaskRepository：数据库存储（Week 06）
 */
interface TaskRepository {
    /**
     * 保存任务。
     *
     * @param task 要保存的任务
     */
    void save(Task task);

    /**
     * 根据标题查找任务。
     *
     * @param title 任务标题
     * @return 找到的任务，如果不存在返回 null
     */
    Task findByTitle(String title);

    /**
     * 查找所有任务。
     *
     * @return 所有任务的列表
     */
    List<Task> findAll();

    /**
     * 根据标题删除任务。
     *
     * @param title 任务标题
     */
    void deleteByTitle(String title);

    /**
     * 获取任务数量。
     *
     * @return 任务数量
     */
    int count();
}
