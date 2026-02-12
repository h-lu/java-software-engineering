/**
 * CampusFlow 超级线：Repository 层实现。
 *
 * <p>本周任务：使用集合框架重构数据存储层
 * - 用 ArrayList 存储实体对象
 * - 用 HashMap 建立 ID 到对象的快速查找索引
 * - 添加基本的增删改查方法
 *
 * 运行方式：javac chapters/week_05/examples/CampusFlowRepository.java && \
 *          java -cp chapters/week_05/examples CampusFlowRepositoryDemo
 *
 * 预期输出：
 * - 展示 TaskRepository 的完整 CRUD 操作
 * - 展示内存存储层的实现模式
 */

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// ========== 实体类（简化版 Task） ==========

/**
 * 任务实体类。
 */
class Task {
    private final String id;
    private String title;
    private String description;
    private boolean completed;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String id, String title, String description) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("任务 ID 不能为空");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("任务标题不能为空");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters with update timestamp
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Task[%s] %s %s", id, title, completed ? "✓" : "○");
    }
}

// ========== Repository 层 ==========

/**
 * Task 仓储类——内存存储实现。
 *
 * <p>设计要点：
 * <ul>
 *   <li>使用 HashMap 实现 O(1) 的 ID 查找</li>
 *   <li>使用 ArrayList 支持遍历和排序</li>
 *   <li>返回副本保护内部数据</li>
 *   <li>使用 Optional 避免 null</li>
 * </ul>
 *
 * <p>这是生产环境中 Repository 层的简化版，
 * 后续周次会添加持久化支持。
 */
class TaskRepository {
    // 主索引：ID -> Task，O(1) 查找
    private final HashMap<String, Task> tasksById;
    // 辅助存储：支持遍历和排序
    private final ArrayList<Task> taskList;

    public TaskRepository() {
        this.tasksById = new HashMap<>();
        this.taskList = new ArrayList<>();
    }

    /**
     * 保存任务（新增或更新）。
     *
     * @param task 要保存的任务
     * @throws IllegalArgumentException 如果 task 或 id 为 null
     */
    public void save(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task 或 ID 不能为空");
        }

        // 更新 HashMap
        tasksById.put(task.getId(), task);

        // 更新列表：如果已存在则替换
        taskList.removeIf(t -> t.getId().equals(task.getId()));
        taskList.add(task);
    }

    /**
     * 根据 ID 查找任务。
     *
     * @param id 任务 ID
     * @return Optional<Task> 可能包含任务
     */
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasksById.get(id));
    }

    /**
     * 获取所有任务（返回副本）。
     *
     * @return 任务列表的副本
     */
    public List<Task> findAll() {
        return new ArrayList<>(taskList);
    }

    /**
     * 查找未完成的任务。
     */
    public List<Task> findPending() {
        return taskList.stream()
            .filter(t -> !t.isCompleted())
            .collect(Collectors.toList());
    }

    /**
     * 查找已完成的任务。
     */
    public List<Task> findCompleted() {
        return taskList.stream()
            .filter(Task::isCompleted)
            .collect(Collectors.toList());
    }

    /**
     * 根据标题模糊搜索。
     */
    public List<Task> findByTitleContaining(String keyword) {
        return taskList.stream()
            .filter(t -> t.getTitle().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    /**
     * 删除任务。
     *
     * @param id 任务 ID
     * @return true 如果删除成功
     */
    public boolean delete(String id) {
        Task removed = tasksById.remove(id);
        if (removed != null) {
            taskList.removeIf(t -> t.getId().equals(id));
            return true;
        }
        return false;
    }

    /**
     * 批量删除已完成的任务。
     *
     * <p>使用 removeIf（Java 8+），内部使用 Iterator 安全删除。
     */
    public int deleteCompleted() {
        int before = taskList.size();

        // 从列表中删除
        taskList.removeIf(Task::isCompleted);

        // 同步更新 Map
        tasksById.entrySet().removeIf(entry -> entry.getValue().isCompleted());

        return before - taskList.size();
    }

    /**
     * 检查任务是否存在。
     */
    public boolean exists(String id) {
        return tasksById.containsKey(id);
    }

    /**
     * 获取任务总数。
     */
    public int count() {
        return tasksById.size();
    }

    /**
     * 获取未完成任务数。
     */
    public int countPending() {
        return (int) taskList.stream().filter(t -> !t.isCompleted()).count();
    }

    /**
     * 清空所有任务。
     */
    public void clear() {
        tasksById.clear();
        taskList.clear();
    }
}

// ========== 服务层（简化版） ==========

/**
 * Task 服务类——业务逻辑层。
 */
class TaskService {
    private final TaskRepository repository;

    public TaskService() {
        this.repository = new TaskRepository();
    }

    public Task createTask(String id, String title, String description) {
        if (repository.exists(id)) {
            throw new IllegalArgumentException("任务 ID 已存在: " + id);
        }
        Task task = new Task(id, title, description);
        repository.save(task);
        return task;
    }

    public Task updateTask(String id, String newTitle, String newDescription) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在: " + id));

        if (newTitle != null) {
            task.setTitle(newTitle);
        }
        if (newDescription != null) {
            task.setDescription(newDescription);
        }
        repository.save(task);
        return task;
    }

    public void completeTask(String id) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在: " + id));
        task.setCompleted(true);
        repository.save(task);
    }

    public boolean deleteTask(String id) {
        return repository.delete(id);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTask(String id) {
        return repository.findById(id);
    }

    public void printStatistics() {
        System.out.println("任务统计：");
        System.out.println("  总计: " + repository.count());
        System.out.println("  待完成: " + repository.countPending());
        System.out.println("  已完成: " + (repository.count() - repository.countPending()));
    }
}

// ========== 主程序 ==========

class CampusFlowRepositoryDemo {
    public static void main(String[] args) {
        System.out.println("=== CampusFlow Repository 层演示 ===\n");

        TaskService service = new TaskService();

        // 1. 创建任务
        System.out.println("【1. 创建任务】");
        service.createTask("T001", "设计数据库模型", "设计用户和任务表结构");
        service.createTask("T002", "实现用户注册", "编写注册接口和验证逻辑");
        service.createTask("T003", "编写单元测试", "为核心服务编写 JUnit 测试");
        service.createTask("T004", "代码审查", "审查团队成员的 PR");
        service.createTask("T005", "部署到测试环境", "配置 CI/CD 流程");

        // 2. 列出所有任务
        System.out.println("\n【2. 所有任务】");
        service.getAllTasks().forEach(t -> System.out.println("  " + t));

        // 3. 完成任务
        System.out.println("\n【3. 完成任务】");
        service.completeTask("T003");
        service.completeTask("T004");
        System.out.println("  T003 和 T004 已标记为完成");

        // 4. 更新任务
        System.out.println("\n【4. 更新任务】");
        service.updateTask("T001", "设计数据库模型（已评审）", null);
        System.out.println("  T001 标题已更新");

        // 5. 统计信息
        System.out.println("\n【5. 统计信息】");
        service.printStatistics();

        // 6. 查询演示
        System.out.println("\n【6. 查询演示】");

        TaskRepository repo = new TaskRepository();
        // 直接操作 Repository 展示更多功能
        repo.save(new Task("T006", "编写文档", "API 文档和使用指南"));
        repo.save(new Task("T007", "性能优化", "优化数据库查询"));

        System.out.println("查找 ID=T006: " + repo.findById("T006").orElse(null));
        System.out.println("是否存在 T999: " + repo.exists("T999"));

        // 7. 批量删除演示
        System.out.println("\n【7. 批量删除已完成任务】");
        System.out.println("删除前数量: " + repo.count());

        // 先标记一些为完成
        repo.findById("T006").ifPresent(t -> {
            t.setCompleted(true);
            repo.save(t);
        });

        int deleted = repo.deleteCompleted();
        System.out.println("已删除 " + deleted + " 个已完成的任务");
        System.out.println("删除后数量: " + repo.count());

        // 8. 异常处理演示
        System.out.println("\n【8. 异常处理演示】");

        try {
            service.createTask("T001", "重复 ID", "这会失败");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 预期异常: " + e.getMessage());
        }

        try {
            service.updateTask("T999", "不存在的任务", null);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 预期异常: " + e.getMessage());
        }

        // 9. 最终状态
        System.out.println("\n【9. CampusFlow 当前任务状态】");
        service.getAllTasks().forEach(t -> System.out.println("  " + t));
        service.printStatistics();

        System.out.println("\n=== 架构说明 ===");
        System.out.println("本示例展示了 CampusFlow 的 Repository 层设计：");
        System.out.println("┌─────────────────────────────────────┐");
        System.out.println("│  Service 层  - 业务逻辑             │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ Repository 层 - 数据访问（本示例）  │");
        System.out.println("│  - HashMap<String, Task> 快速查找   │");
        System.out.println("│  - ArrayList<Task> 支持遍历         │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│   Entity 层  - Task 实体            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("\n下周：添加 SQLite 持久化支持");
    }
}
