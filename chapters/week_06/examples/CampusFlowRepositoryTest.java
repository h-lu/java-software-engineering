/*
 * 示例：CampusFlow Repository 层单元测试。
 * 运行方式：mvn -q -f chapters/week_06/starter_code/pom.xml test -Dtest=CampusFlowRepositoryTest
 * 预期输出：测试通过，核心方法覆盖率 80%+
 *
 * 本周 CampusFlow 推进：
 * - 上周状态：使用 ArrayList/HashMap 重构了数据存储层，Repository 模式落地
 * - 本周改进：为 Repository 层添加单元测试，确保核心增删改查方法有测试覆盖
 * - 目标：核心方法（save/findById/findAll/delete）测试覆盖率 80%+
 *
 * 涉及的本周概念：
 * - JUnit 5 基础（@Test, 断言方法）
 * - 测试生命周期（@BeforeEach）
 * - 异常测试（assertThrows）
 * - 参数化测试（@ParameterizedTest, @CsvSource）
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Task 实体类 - CampusFlow 的核心领域对象
 * 对应 Week 05 的 TaskRepository 中的 Task
 */
class Task {
    private String id;
    private String title;
    private String status;
    private String assignee;

    public Task(String id, String title, String status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public Task(String id, String title, String status, String assignee) {
        this(id, title, status);
        this.assignee = assignee;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getAssignee() { return assignee; }

    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setAssignee(String assignee) { this.assignee = assignee; }
}

/**
 * TaskRepository - CampusFlow 的任务仓储层
 * 基于 Week 05 的实现，使用 HashMap + ArrayList 组合
 */
class TaskRepository {
    private final java.util.HashMap<String, Task> tasksById;
    private final java.util.ArrayList<Task> taskList;

    public TaskRepository() {
        this.tasksById = new java.util.HashMap<>();
        this.taskList = new java.util.ArrayList<>();
    }

    /**
     * 保存任务（新增或更新）
     */
    public void save(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task 不能为空");
        }
        if (task.getId() == null || task.getId().isEmpty()) {
            throw new IllegalArgumentException("Task ID 不能为空");
        }

        tasksById.put(task.getId(), task);

        // 如果列表中已存在，先删除旧引用
        taskList.removeIf(t -> t.getId().equals(task.getId()));
        taskList.add(task);
    }

    /**
     * 根据 ID 查找任务
     */
    public java.util.Optional<Task> findById(String id) {
        return java.util.Optional.ofNullable(tasksById.get(id));
    }

    /**
     * 获取所有任务
     */
    public java.util.List<Task> findAll() {
        return new java.util.ArrayList<>(taskList);
    }

    /**
     * 根据状态查找任务
     */
    public java.util.List<Task> findByStatus(String status) {
        return taskList.stream()
            .filter(t -> t.getStatus().equals(status))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 删除任务
     */
    public void delete(String id) {
        Task removed = tasksById.remove(id);
        if (removed != null) {
            taskList.remove(removed);
        }
    }

    /**
     * 检查任务是否存在
     */
    public boolean exists(String id) {
        return tasksById.containsKey(id);
    }

    /**
     * 获取任务数量
     */
    public int count() {
        return tasksById.size();
    }

    /**
     * 清空所有任务
     */
    public void clear() {
        tasksById.clear();
        taskList.clear();
    }
}

/**
 * CampusFlow Repository 测试类
 *
 * 测试覆盖目标：
 * - save: 正常保存、更新、异常输入
 * - findById: 存在、不存在
 * - findAll: 空集合、多个元素
 * - findByStatus: 按状态筛选
 * - delete: 删除存在/不存在的任务
 * - exists: 存在性检查
 */
class CampusFlowRepositoryTest {

    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TaskRepository();
    }

    // ========== save 方法测试 ==========

    @Test
    void shouldSaveTask() {
        Task task = new Task("T1", "完成 Week 06 作业", "pending");

        repository.save(task);

        assertTrue(repository.findById("T1").isPresent());
    }

    @Test
    void shouldUpdateExistingTask() {
        Task task = new Task("T1", "完成任务", "pending");
        repository.save(task);

        // 更新任务
        Task updatedTask = new Task("T1", "完成任务（已修改）", "done");
        repository.save(updatedTask);

        Task found = repository.findById("T1").orElseThrow();
        assertEquals("完成任务（已修改）", found.getTitle());
        assertEquals("done", found.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenSavingNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenSavingTaskWithNullId() {
        Task task = new Task(null, "任务", "pending");
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(task);
        });
    }

    @Test
    void shouldThrowExceptionWhenSavingTaskWithEmptyId() {
        Task task = new Task("", "任务", "pending");
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(task);
        });
    }

    // ========== findById 方法测试 ==========

    @Test
    void shouldFindTaskById() {
        repository.save(new Task("T1", "任务1", "pending"));

        java.util.Optional<Task> found = repository.findById("T1");

        assertTrue(found.isPresent());
        assertEquals("任务1", found.get().getTitle());
    }

    @Test
    void shouldReturnEmptyOptionalWhenTaskNotFound() {
        java.util.Optional<Task> found = repository.findById("不存在的ID");

        assertFalse(found.isPresent());
    }

    // ========== findAll 方法测试 ==========

    @Test
    void shouldFindAllTasks() {
        repository.save(new Task("T1", "任务1", "pending"));
        repository.save(new Task("T2", "任务2", "done"));

        var tasks = repository.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoTasks() {
        var tasks = repository.findAll();

        assertTrue(tasks.isEmpty());
    }

    @Test
    void findAllShouldReturnDefensiveCopy() {
        repository.save(new Task("T1", "任务1", "pending"));

        var tasks = repository.findAll();
        tasks.clear(); // 修改返回的列表

        // 原始数据不应受影响
        assertEquals(1, repository.count());
    }

    // ========== findByStatus 方法测试 ==========

    @Test
    void shouldFindTasksByStatus() {
        repository.save(new Task("T1", "任务1", "pending"));
        repository.save(new Task("T2", "任务2", "done"));
        repository.save(new Task("T3", "任务3", "pending"));

        var pendingTasks = repository.findByStatus("pending");

        assertEquals(2, pendingTasks.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoTasksWithStatus() {
        repository.save(new Task("T1", "任务1", "done"));

        var pendingTasks = repository.findByStatus("pending");

        assertTrue(pendingTasks.isEmpty());
    }

    // ========== delete 方法测试 ==========

    @Test
    void shouldDeleteTask() {
        repository.save(new Task("T1", "任务", "pending"));
        assertTrue(repository.findById("T1").isPresent());

        repository.delete("T1");

        assertFalse(repository.findById("T1").isPresent());
    }

    @Test
    void shouldNotThrowWhenDeletingNonExistentTask() {
        // 删除不存在的任务不应该抛出异常
        assertDoesNotThrow(() -> {
            repository.delete("不存在的ID");
        });
    }

    // ========== exists 方法测试 ==========

    @Test
    void shouldReturnTrueWhenTaskExists() {
        repository.save(new Task("T1", "任务", "pending"));

        assertTrue(repository.exists("T1"));
    }

    @Test
    void shouldReturnFalseWhenTaskNotExists() {
        assertFalse(repository.exists("不存在的ID"));
    }

    // ========== count 方法测试 ==========

    @Test
    void shouldReturnCorrectCount() {
        assertEquals(0, repository.count());

        repository.save(new Task("T1", "任务1", "pending"));
        assertEquals(1, repository.count());

        repository.save(new Task("T2", "任务2", "done"));
        assertEquals(2, repository.count());
    }

    // ========== 参数化测试（批量验证） ==========

    @ParameterizedTest
    @CsvSource({
        "T1, 任务1, pending",
        "T2, 任务2, done",
        "T3, 任务3, in_progress"
    })
    void shouldSaveAndRetrieveMultipleTasks(String id, String title, String status) {
        Task task = new Task(id, title, status);
        repository.save(task);

        Task found = repository.findById(id).orElseThrow();
        assertEquals(title, found.getTitle());
        assertEquals(status, found.getStatus());
    }

    // ========== 集成场景测试 ==========

    @Test
    void shouldHandleCompleteTaskLifecycle() {
        // 创建 -> 查询 -> 更新 -> 查询 -> 删除

        // 创建
        repository.save(new Task("T1", "完成任务", "pending"));
        assertTrue(repository.exists("T1"));

        // 查询
        Task task = repository.findById("T1").orElseThrow();
        assertEquals("pending", task.getStatus());

        // 更新（通过重新保存）
        repository.save(new Task("T1", "完成任务", "done"));
        Task updated = repository.findById("T1").orElseThrow();
        assertEquals("done", updated.getStatus());

        // 删除
        repository.delete("T1");
        assertFalse(repository.exists("T1"));
    }

    @Test
    void shouldMaintainDataConsistencyBetweenFindByIdAndFindAll() {
        repository.save(new Task("T1", "任务1", "pending"));
        repository.save(new Task("T2", "任务2", "done"));

        // findById 和 findAll 应该看到一致的数据
        assertEquals(2, repository.findAll().size());
        assertTrue(repository.findById("T1").isPresent());
        assertTrue(repository.findById("T2").isPresent());

        // 删除后两者都应该看不到
        repository.delete("T1");
        assertEquals(1, repository.findAll().size());
        assertFalse(repository.findById("T1").isPresent());
    }
}
