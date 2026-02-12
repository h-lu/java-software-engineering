package edu.campusflow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 团队协作场景测试。
 *
 * <p>本测试类模拟多人协作开发场景，验证：
 * 1. 多人同时操作数据的一致性
 * 2. 冲突解决逻辑
 * 3. 代码审查场景模拟
 * 4. Repository 接口设计验证（ADR-002）
 *
 * <p>测试目标：
 * - 理解团队协作中的数据一致性问题
 * - 学习 Repository 模式的设计
 * - 掌握冲突检测和解决策略
 *
 * <p>本周学习目标：
 * - 理解 Feature Branch 工作流
 * - 学习 Pull Request 的审查流程
 * - 掌握 ADR-002 数据存储方案决策
 */
@DisplayName("团队协作场景测试 - 模拟多人协作开发")
class TeamCollaborationTest {

    private TaskManager taskManager;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
        taskRepository = new InMemoryTaskRepository();
    }

    // ==================== 场景 1: 多人同时添加任务 ====================

    @Test
    @DisplayName("test_multi_user_add_tasks_should_maintain_consistency")
    void test_multi_user_add_tasks_should_maintain_consistency() {
        // 场景：小北和阿码同时向任务管理器添加任务

        // Given: 模拟两个用户的操作
        Task xiaobeiTask = new Task("小北的任务");
        Task amaTask = new Task("阿码的任务");

        // When: 两个用户分别添加任务（模拟并发操作）
        taskManager.addTask(xiaobeiTask);
        taskManager.addTask(amaTask);

        // Then: 所有任务都应该被正确记录
        List<Task> allTasks = taskManager.getAllTasks();
        assertEquals(2, allTasks.size(),
            "两个用户添加的任务都应该存在");
        assertTrue(allTasks.stream().anyMatch(t -> t.getTitle().equals("小北的任务")),
            "应该包含小北的任务");
        assertTrue(allTasks.stream().anyMatch(t -> t.getTitle().equals("阿码的任务")),
            "应该包含阿码的任务");
    }

    @Test
    @DisplayName("test_multi_user_complete_tasks_should_not_affect_each_other")
    void test_multi_user_complete_tasks_should_not_affect_each_other() {
        // 场景：小北完成任务 A，阿码完成任务 B，互不影响

        // Given
        Task taskA = new Task("任务A");
        Task taskB = new Task("任务B");
        taskManager.addTask(taskA);
        taskManager.addTask(taskB);

        // When: 小北完成任务A
        taskA.markCompleted();

        // Then: 任务B应该仍然是未完成状态
        assertTrue(taskA.isCompleted(), "任务A应该已完成");
        assertFalse(taskB.isCompleted(), "任务B应该仍然是未完成状态");

        // When: 阿码完成任务B
        taskB.markCompleted();

        // Then: 两个任务都已完成
        assertTrue(taskA.isCompleted(), "任务A应该已完成");
        assertTrue(taskB.isCompleted(), "任务B应该已完成");
    }

    // ==================== 场景 2: Repository 模式验证（ADR-002）====================

    @Test
    @DisplayName("test_repository_interface_should_allow_different_implementations")
    void test_repository_interface_should_allow_different_implementations() {
        // 场景：验证 Repository 接口设计，支持不同存储实现

        // Given: 使用内存实现
        TaskRepository memoryRepo = new InMemoryTaskRepository();

        // When: 添加任务到 Repository
        Task task = new Task("Repository测试任务");
        memoryRepo.save(task);

        // Then: 应该能够检索到
        Task retrieved = memoryRepo.findByTitle("Repository测试任务");
        assertNotNull(retrieved, "Repository 应该能够保存和检索任务");
        assertEquals("Repository测试任务", retrieved.getTitle(),
            "检索到的任务标题应该正确");
    }

    @Test
    @DisplayName("test_repository_should_isolate_storage_from_manager")
    void test_repository_should_isolate_storage_from_manager() {
        // 场景：验证 Repository 层隔离了存储细节

        // Given: TaskManager 使用 Repository 而不是直接操作列表
        TaskManager managerWithRepo = new TaskManager(taskRepository);

        // When: 通过 Manager 添加任务
        Task task = new Task("隔离测试任务");
        managerWithRepo.addTask(task);

        // Then: 任务应该被保存到 Repository
        assertEquals(1, taskRepository.count(),
            "Repository 应该保存了任务");
        assertNotNull(taskRepository.findByTitle("隔离测试任务"),
            "应该能够通过 Repository 检索到任务");
    }

    @Test
    @DisplayName("test_repository_should_support_crud_operations")
    void test_repository_should_support_crud_operations() {
        // 场景：验证 Repository 支持完整的 CRUD 操作

        // Create
        Task task = new Task("CRUD任务");
        taskRepository.save(task);
        assertEquals(1, taskRepository.count(), "创建后应该有1个任务");

        // Read
        Task found = taskRepository.findByTitle("CRUD任务");
        assertNotNull(found, "应该能够读取任务");

        // Update
        found.markCompleted();
        taskRepository.save(found);
        Task updated = taskRepository.findByTitle("CRUD任务");
        assertTrue(updated.isCompleted(), "更新后任务应该标记为完成");

        // Delete
        taskRepository.deleteByTitle("CRUD任务");
        assertEquals(0, taskRepository.count(), "删除后应该没有任务");
        assertNull(taskRepository.findByTitle("CRUD任务"), "删除后应该找不到任务");
    }

    // ==================== 场景 3: 代码审查场景模拟 ====================

    @Test
    @DisplayName("test_code_review_should_catch_null_pointer_risk")
    void test_code_review_should_catch_null_pointer_risk() {
        // 场景：模拟 Code Review 中发现 NPE 风险

        // Given: 一个可能导致 NPE 的操作
        taskManager.addTask(new Task("测试任务"));

        // When & Then: 审查发现 filterByPriority 对 null 参数的处理
        // 好的实现应该抛出有意义的异常，而不是抛出 NPE
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> taskManager.filterByPriority(null),
            "Code Review 检查点：filterByPriority 应该对 null 参数进行验证");

        assertNotNull(exception.getMessage(),
            "异常消息不应该为 null，这是 Code Review 的要求");
    }

    @Test
    @DisplayName("test_code_review_should_verify_exception_messages")
    void test_code_review_should_verify_exception_messages() {
        // 场景：验证异常消息是否清晰，便于调试

        // When: 触发异常
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> taskManager.addTask(null));

        // Then: 异常消息应该包含有意义的信息
        String message = exception.getMessage();
        assertNotNull(message, "异常消息不应该为 null");
        assertFalse(message.isEmpty(), "异常消息不应该为空");

        // 好的异常消息应该包含参数名或上下文
        boolean hasMeaningfulContent =
            message.toLowerCase().contains("null") ||
            message.toLowerCase().contains("task") ||
            message.toLowerCase().contains("任务") ||
            message.toLowerCase().contains("empty");

        assertTrue(hasMeaningfulContent,
            "异常消息应该包含有意义的上下文信息，实际消息: " + message);
    }

    @Test
    @DisplayName("test_code_review_should_check_srp_compliance")
    void test_code_review_should_check_srp_compliance() {
        // 场景：验证 TaskManager 是否符合单一职责原则

        // Given: TaskManager 实例
        TaskManager manager = new TaskManager();

        // When: 添加任务
        manager.addTask(new Task("SRP测试"));

        // Then: TaskManager 不应该直接处理文件操作（这是 Repository 的职责）
        // 这个测试通过检查 TaskManager 的方法列表来验证
        java.lang.reflect.Method[] methods = TaskManager.class.getDeclaredMethods();
        for (java.lang.reflect.Method method : methods) {
            String name = method.getName().toLowerCase();
            assertFalse(name.contains("file") || name.contains("save") || name.contains("load"),
                "TaskManager 不应该包含文件操作方法（违反 SRP），方法名: " + method.getName());
        }
    }

    // ==================== 场景 4: 冲突解决场景 ====================

    @Test
    @DisplayName("test_concurrent_read_should_not_conflict")
    void test_concurrent_read_should_not_conflict() {
        // 场景：多个用户同时读取任务列表，不应该产生冲突

        // Given: 准备测试数据
        for (int i = 0; i < 10; i++) {
            taskManager.addTask(new Task("任务" + i));
        }

        // When: 多次读取
        List<Task> read1 = taskManager.getAllTasks();
        List<Task> read2 = taskManager.getAllTasks();
        List<Task> read3 = taskManager.getIncompleteTasks();

        // Then: 所有读取都应该返回一致的结果
        assertEquals(read1.size(), read2.size(),
            "多次读取应该返回相同数量的任务");
        assertEquals(10, read3.size(),
            "未完成列表应该包含所有任务");
    }

    @Test
    @DisplayName("test_repository_should_handle_duplicate_task_gracefully")
    void test_repository_should_handle_duplicate_task_gracefully() {
        // 场景：Repository 处理重复任务的方式

        // Given: 添加一个任务
        Task task = new Task("重复任务测试");
        taskRepository.save(task);

        // When: 尝试添加同名任务（不同对象）
        Task duplicateTask = new Task("重复任务测试");

        // Then: 根据实现，可能覆盖或保留两者
        // 这里我们验证 Repository 有确定的行为
        assertDoesNotThrow(() -> taskRepository.save(duplicateTask),
            "Repository 应该能够处理重复任务，不抛出异常");

        // 验证 Repository 的状态是一致的
        int count = taskRepository.count();
        assertTrue(count >= 1, "Repository 应该至少包含一个任务");
    }

    // ==================== 场景 5: 数据一致性验证 ====================

    @Test
    @DisplayName("test_task_state_consistency_after_multiple_operations")
    void test_task_state_consistency_after_multiple_operations() {
        // 场景：多次操作后数据保持一致性

        // Given: 初始状态
        Task task = new Task("一致性测试");
        task.setPriority(1);
        taskManager.addTask(task);

        // When: 执行一系列操作
        taskManager.markCompleted("一致性测试");
        List<Task> highPriorityTasks = taskManager.getTasksByPriority(1);
        List<Task> completedTasks = taskManager.filterByStatus(true);
        List<Task> allTasks = taskManager.getAllTasks();

        // Then: 所有视图应该保持一致
        assertEquals(1, allTasks.size(), "总任务数应该为1");
        assertTrue(allTasks.get(0).isCompleted(), "任务应该标记为完成");

        // 验证通过不同方式获取的任务状态一致
        if (!highPriorityTasks.isEmpty()) {
            assertTrue(highPriorityTasks.get(0).isCompleted(),
                "高优先级列表中的任务也应该标记为完成");
        }

        if (!completedTasks.isEmpty()) {
            assertEquals("一致性测试", completedTasks.get(0).getTitle(),
                "已完成列表中的任务标题应该正确");
        }
    }

    @Test
    @DisplayName("test_delete_task_should_remove_from_all_views")
    void test_delete_task_should_remove_from_all_views() {
        // 场景：删除任务后，所有视图都不应该再显示该任务

        // Given: 添加并完成任务
        Task task = new Task("待删除任务");
        task.setPriority(2);
        taskManager.addTask(task);
        task.markCompleted();

        // When: 删除任务
        boolean removed = taskManager.removeTask("待删除任务");

        // Then: 任务应该被完全删除
        assertTrue(removed, "删除应该成功");
        assertEquals(0, taskManager.getTaskCount(), "任务总数应该为0");
        assertTrue(taskManager.getAllTasks().isEmpty(), "所有任务列表应该为空");
        assertTrue(taskManager.getIncompleteTasks().isEmpty(), "未完成任务列表应该为空");
        assertTrue(taskManager.filterByStatus(true).isEmpty(), "已完成任务列表应该为空");
    }

    // ==================== 场景 6: 协作边界情况 ====================

    @Test
    @DisplayName("test_empty_repository_should_return_empty_results_not_null")
    void test_empty_repository_should_return_empty_results_not_null() {
        // 场景：空 Repository 应该返回空列表而不是 null

        // Given: 空的 Repository
        assertEquals(0, taskRepository.count(), "Repository 应该为空");

        // When: 查询所有任务
        List<Task> allTasks = taskRepository.findAll();

        // Then: 应该返回空列表而不是 null
        assertNotNull(allTasks, "Repository 不应该返回 null");
        assertTrue(allTasks.isEmpty(), "应该返回空列表");
    }

    @Test
    @DisplayName("test_find_nonexistent_task_should_return_null_or_empty")
    void test_find_nonexistent_task_should_return_null_or_empty() {
        // 场景：查找不存在的任务应该有确定的行为

        // When: 查找不存在的任务
        Task found = taskRepository.findByTitle("不存在的任务");

        // Then: 应该返回 null（或者根据实现返回 Optional.empty）
        // 这里我们验证行为是确定的
        assertNull(found, "查找不存在的任务应该返回 null");
    }

    @Test
    @DisplayName("test_task_priority_boundary_values")
    void test_task_priority_boundary_values() {
        // 场景：测试优先级边界值

        // Given: 创建不同优先级的任务
        Task highPriority = new Task("高优先级");
        Task mediumPriority = new Task("中优先级");
        Task lowPriority = new Task("低优先级");

        highPriority.setPriority(1);
        mediumPriority.setPriority(3);
        lowPriority.setPriority(5);

        taskManager.addTask(highPriority);
        taskManager.addTask(mediumPriority);
        taskManager.addTask(lowPriority);

        // When & Then: 按优先级筛选
        List<Task> highTasks = taskManager.getTasksByPriority(1);
        List<Task> mediumTasks = taskManager.getTasksByPriority(3);
        List<Task> lowTasks = taskManager.getTasksByPriority(5);

        assertEquals(1, highTasks.size(), "应该有1个高优先级任务");
        assertEquals(1, mediumTasks.size(), "应该有1个中优先级任务");
        assertEquals(1, lowTasks.size(), "应该有1个低优先级任务");
    }

    // ==================== 内部辅助类 ====================

    /**
     * InMemoryTaskRepository - 内存实现（过渡方案）
     * 实现了主代码中的 TaskRepository 接口
     */
    class InMemoryTaskRepository implements TaskRepository {
        private final List<Task> tasks = new ArrayList<>();

        @Override
        public void save(Task task) {
            // 如果存在同名任务，先删除再添加（更新操作）
            tasks.removeIf(t -> t.getTitle().equals(task.getTitle()));
            tasks.add(task);
        }

        @Override
        public Task findByTitle(String title) {
            return tasks.stream()
                .filter(t -> t.getTitle().equals(title))
                .findFirst()
                .orElse(null);
        }

        @Override
        public List<Task> findAll() {
            return new ArrayList<>(tasks); // 防御性拷贝
        }

        @Override
        public void deleteByTitle(String title) {
            tasks.removeIf(t -> t.getTitle().equals(title));
        }

        @Override
        public int count() {
            return tasks.size();
        }
    }
}
