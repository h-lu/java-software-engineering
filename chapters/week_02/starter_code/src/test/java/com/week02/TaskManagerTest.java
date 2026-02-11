package com.week02;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * TaskManager 类的单元测试。
 *
 * <p>测试覆盖：
 * 1. 任务管理功能 - 添加、标记完成、过滤
 * 2. 单一职责原则 - 验证 TaskManager 只负责管理，不负责存储
 * 3. 边界情况 - 空列表、重复任务、不存在的任务
 * 4. 防御式编程 - 处理 null 输入
 *
 * <p>本周学习目标：
 * - 理解服务类与实体类的区别
 * - 理解单一职责原则（SRP）
 * - 理解"管理者"类的设计
 */
@DisplayName("TaskManager 类测试 - 验证任务管理功能")
class TaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    void setUp() {
        // 每个测试前创建一个新的 TaskManager
        manager = new TaskManager();
    }

    // ==================== 正例测试（Happy Path）====================

    @Test
    @DisplayName("test_add_task_should_increase_task_count")
    void test_add_task_should_increase_task_count() {
        // Given
        Task task1 = new Task("任务1");
        Task task2 = new Task("任务2");

        // When
        manager.addTask(task1);
        manager.addTask(task2);

        // Then
        assertEquals(2, manager.getAllTasks().size(),
            "添加 2 个任务后，任务列表应该有 2 个任务");
    }

    @Test
    @DisplayName("test_add_task_and_retrieve_it")
    void test_add_task_and_retrieve_it() {
        // Given
        Task task = new Task("学习 Java");

        // When
        manager.addTask(task);
        List<Task> allTasks = manager.getAllTasks();

        // Then
        assertEquals(1, allTasks.size(), "应该有 1 个任务");
        assertEquals("学习 Java", allTasks.get(0).getTitle(), "任务标题应该正确");
    }

    @Test
    @DisplayName("test_mark_completed_by_title_should_change_state")
    void test_mark_completed_by_title_should_change_state() {
        // Given
        Task task = new Task("写作业");
        manager.addTask(task);

        // When
        manager.markCompleted("写作业");

        // Then
        assertTrue(task.isCompleted(), "任务应该被标记为已完成");
    }

    @Test
    @DisplayName("test_get_incomplete_tasks_should_return_only_active")
    void test_get_incomplete_tasks_should_return_only_active() {
        // Given
        Task task1 = new Task("未完成任务1");
        Task task2 = new Task("已完成任务");
        Task task3 = new Task("未完成任务2");

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        // When - 标记 task2 为已完成
        task2.markCompleted();

        List<Task> incompleteTasks = manager.getIncompleteTasks();

        // Then
        assertEquals(2, incompleteTasks.size(),
            "应该只返回未完成的任务");
        assertTrue(incompleteTasks.contains(task1), "应包含 task1");
        assertFalse(incompleteTasks.contains(task2), "不应包含已完成的 task2");
        assertTrue(incompleteTasks.contains(task3), "应包含 task3");
    }

    @Test
    @DisplayName("test_get_all_tasks_should_return_defensive_copy")
    void test_get_all_tasks_should_return_defensive_copy() {
        // Given
        manager.addTask(new Task("任务1"));
        List<Task> tasks1 = manager.getAllTasks();

        // When - 尝试修改返回的列表
        tasks1.clear();
        List<Task> tasks2 = manager.getAllTasks();

        // Then - 原始列表应该不受影响（防御性拷贝）
        assertEquals(1, tasks2.size(),
            "修改返回的列表不应影响 TaskManager 的内部状态");
    }

    // ==================== 边界测试（Edge Cases）====================

    @Test
    @DisplayName("test_get_all_tasks_from_empty_manager_should_return_empty_list")
    void test_get_all_tasks_from_empty_manager_should_return_empty_list() {
        // When
        List<Task> tasks = manager.getAllTasks();

        // Then
        assertTrue(tasks.isEmpty(),
            "空 TaskManager 应该返回空列表");
        assertEquals(0, tasks.size(),
            "空 TaskManager 的任务数量应该是 0");
    }

    @Test
    @DisplayName("test_get_incomplete_tasks_from_empty_manager_should_return_empty_list")
    void test_get_incomplete_tasks_from_empty_manager_should_return_empty_list() {
        // When
        List<Task> incompleteTasks = manager.getIncompleteTasks();

        // Then
        assertTrue(incompleteTasks.isEmpty(),
            "空 TaskManager 的未完成任务应该是空列表");
    }

    @Test
    @DisplayName("test_add_multiple_tasks_with_same_title")
    void test_add_multiple_tasks_with_same_title() {
        // Given
        Task task1 = new Task("重复标题");
        Task task2 = new Task("重复标题");

        // When
        manager.addTask(task1);
        manager.addTask(task2);

        // Then - 应该允许添加同名任务（它们是不同的对象）
        assertEquals(2, manager.getAllTasks().size(),
            "应该允许添加多个同名任务");
    }

    @Test
    @DisplayName("test_mark_completed_non_existent_task_should_do_nothing")
    void test_mark_completed_non_existent_task_should_do_nothing() {
        // Given
        Task existingTask = new Task("存在的任务");
        manager.addTask(existingTask);

        // When - 尝试标记不存在的任务
        manager.markCompleted("不存在的任务");

        // Then - 不应该抛出异常，只是没有效果
        assertFalse(existingTask.isCompleted(),
            "标记不存在的任务不应影响其他任务");
        assertEquals(1, manager.getAllTasks().size(),
            "不应该添加新任务");
    }

    @Test
    @DisplayName("test_mark_completed_empty_string_should_do_nothing")
    void test_mark_completed_empty_string_should_do_nothing() {
        // Given
        manager.addTask(new Task("任务"));

        // When - 尝试标记空字符串标题
        manager.markCompleted("");

        // Then - 不应该抛出异常
        assertEquals(1, manager.getAllTasks().size(),
            "空字符串标题不应导致错误");
    }

    @Test
    @DisplayName("test_all_tasks_completed_should_return_empty_incomplete_list")
    void test_all_tasks_completed_should_return_empty_incomplete_list() {
        // Given
        Task task1 = new Task("任务1");
        Task task2 = new Task("任务2");

        manager.addTask(task1);
        manager.addTask(task2);

        // When - 所有任务都标记为已完成
        task1.markCompleted();
        task2.markCompleted();

        List<Task> incompleteTasks = manager.getIncompleteTasks();

        // Then
        assertTrue(incompleteTasks.isEmpty(),
            "所有任务完成后，未完成列表应该为空");
    }

    // ==================== 反例测试（Negative Cases）====================

    @Test
    @DisplayName("test_add_null_task_should_throw_exception")
    void test_add_null_task_should_throw_exception() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> manager.addTask(null),
            "添加 null 任务应该抛出异常");
    }

    @Test
    @DisplayName("test_mark_completed_null_title_should_throw_exception")
    void test_mark_completed_null_title_should_throw_exception() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> manager.markCompleted(null),
            "标记 null 标题为完成应该抛出异常");
    }

    // ==================== 单一职责原则测试 ====================

    @Test
    @DisplayName("test_task_manager_should_not_store_task_data_itself")
    void test_task_manager_should_not_store_task_data_itself() {
        // Given
        Task task = new Task("原始任务");
        manager.addTask(task);

        // When - 直接修改 Task 对象的状态
        task.markCompleted();

        // Then - TaskManager 应该反映 Task 的变化
        // 这验证了 TaskManager 不负责存储 Task 的数据，只负责管理
        List<Task> incompleteTasks = manager.getIncompleteTasks();
        assertFalse(incompleteTasks.contains(task),
            "TaskManager 应该引用 Task 对象，而不是复制数据");
    }

    @Test
    @DisplayName("test_task_manager_should_manage_multiple_tasks_independently")
    void test_task_manager_should_manage_multiple_tasks_independently() {
        // Given
        Task task1 = new Task("任务1");
        Task task2 = new Task("任务2");
        Task task3 = new Task("任务3");

        // When
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        task1.markCompleted(); // 只完成 task1

        // Then
        assertEquals(3, manager.getAllTasks().size(),
            "TaskManager 应该管理所有任务");
        assertEquals(1, manager.getAllTasks().stream()
            .filter(Task::isCompleted).count(),
            "只有 1 个任务已完成");
        assertEquals(2, manager.getIncompleteTasks().size(),
            "有 2 个任务未完成");
    }

    @Test
    @DisplayName("test_task_manager_should_filter_by_priority")
    void test_task_manager_should_filter_by_priority() {
        // Given
        Task highPriorityTask = new Task("高优先级任务");
        highPriorityTask.setPriority(1);

        Task mediumPriorityTask = new Task("中优先级任务");
        mediumPriorityTask.setPriority(2);

        Task lowPriorityTask = new Task("低优先级任务");
        lowPriorityTask.setPriority(3);

        manager.addTask(highPriorityTask);
        manager.addTask(mediumPriorityTask);
        manager.addTask(lowPriorityTask);

        // When
        List<Task> highPriorityTasks = manager.getTasksByPriority(1);

        // Then
        assertEquals(1, highPriorityTasks.size(),
            "应该只返回高优先级任务");
        assertEquals("高优先级任务", highPriorityTasks.get(0).getTitle(),
            "应该返回正确的任务");
    }
}
