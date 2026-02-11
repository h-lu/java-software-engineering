package com.week02;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Task 类的单元测试。
 *
 * <p>测试覆盖：
 * 1. 封装原则 - 验证 private 字段 + public getter/setter
 * 2. 对象状态 - 验证每个对象有独立状态
 * 3. 边界情况 - 空标题、特殊字符等
 * 4. 不变性 - 验证对象创建后某些字段不可变
 *
 * <p>本周学习目标：
 * - 理解封装的价值（private 字段保护数据）
 * - 理解 getter/setter 的作用（控制访问）
 * - 理解对象与类的区别
 */
@DisplayName("Task 类测试 - 验证封装和对象状态")
class TaskTest {

    // ==================== 正例测试（Happy Path）====================

    @Test
    @DisplayName("test_create_task_with_valid_title_should_success")
    void test_create_task_with_valid_title_should_success() {
        // Given & When
        Task task = new Task("写作业");

        // Then
        assertEquals("写作业", task.getTitle(),
            "新创建的任务应该有正确的标题");
    }

    @Test
    @DisplayName("test_new_task_should_have_default_completed_false")
    void test_new_task_should_have_default_completed_false() {
        // Given & When
        Task task = new Task("复习Java");

        // Then
        assertFalse(task.isCompleted(),
            "新创建的任务默认应该是未完成状态");
    }

    @Test
    @DisplayName("test_mark_completed_should_change_state_to_true")
    void test_mark_completed_should_change_state_to_true() {
        // Given
        Task task = new Task("做项目");

        // When
        task.markCompleted();

        // Then
        assertTrue(task.isCompleted(),
            "标记完成后，任务状态应该变为已完成");
    }

    @Test
    @DisplayName("test_multiple_tasks_should_have_independent_state")
    void test_multiple_tasks_should_have_independent_state() {
        // Given
        Task task1 = new Task("任务1");
        Task task2 = new Task("任务2");
        Task task3 = new Task("任务3");

        // When - 只标记 task1 为完成
        task1.markCompleted();

        // Then - task2 和 task3 应该仍然是未完成
        assertTrue(task1.isCompleted(),
            "task1 应该已完成");
        assertFalse(task2.isCompleted(),
            "task2 应该未完成（不受 task1 影响）");
        assertFalse(task3.isCompleted(),
            "task3 应该未完成（不受 task1 影响）");
    }

    // ==================== 边界测试（Edge Cases）====================

    @Test
    @DisplayName("test_task_with_empty_title_should_still_create_object")
    void test_task_with_empty_title_should_still_create_object() {
        // Given & When
        Task task = new Task("");

        // Then - 对象创建成功（但可能在业务逻辑中被验证器拒绝）
        assertEquals("", task.getTitle(),
            "空标题的任务应该能创建对象，但可能被验证器标记为无效");
    }

    @Test
    @DisplayName("test_task_with_special_characters_in_title")
    void test_task_with_special_characters_in_title() {
        // Given & When
        Task task = new Task("完成《Java编程》复习！@#$%");

        // Then
        assertEquals("完成《Java编程》复习！@#$%", task.getTitle(),
            "标题应支持特殊字符");
    }

    @Test
    @DisplayName("test_task_with_very_long_title")
    void test_task_with_very_long_title() {
        // Given - 创建一个 1000 字符的标题
        StringBuilder longTitle = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longTitle.append("字");
        }

        // When
        Task task = new Task(longTitle.toString());

        // Then
        assertEquals(1000, task.getTitle().length(),
            "标题应支持长文本");
    }

    @Test
    @DisplayName("test_mark_completed_twice_should_still_be_completed")
    void test_mark_completed_twice_should_still_be_completed() {
        // Given
        Task task = new Task("重复测试");

        // When
        task.markCompleted();
        task.markCompleted(); // 第二次调用

        // Then
        assertTrue(task.isCompleted(),
            "重复标记完成，状态应保持已完成");
    }

    // ==================== 反例测试（Negative Cases）====================

    @Test
    @DisplayName("test_title_setter_with_null_should_throw_exception")
    void test_title_setter_with_null_should_throw_exception() {
        // Given
        Task task = new Task("原始标题");

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle(null),
            "设置为 null 标题时应抛出异常");
    }

    @Test
    @DisplayName("test_title_setter_with_empty_string_should_throw_exception")
    void test_title_setter_with_empty_string_should_throw_exception() {
        // Given
        Task task = new Task("原始标题");

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle(""),
            "设置为空标题时应抛出异常");
    }

    @Test
    @DisplayName("test_title_setter_with_whitespace_only_should_throw_exception")
    void test_title_setter_with_whitespace_only_should_throw_exception() {
        // Given
        Task task = new Task("原始标题");

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle("   "),
            "设置为纯空格标题时应抛出异常");
    }

    @Test
    @DisplayName("test_priority_setter_with_invalid_value_should_throw_exception")
    void test_priority_setter_with_invalid_value_should_throw_exception() {
        // Given
        Task task = new Task("测试优先级");

        // When & Then - 测试超出范围的优先级
        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(0),
            "优先级 0 应该无效");

        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(4),
            "优先级 4 应该无效");

        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(-1),
            "优先级 -1 应该无效");
    }

    // ==================== Getter/Setter 封装测试 ====================

    @Test
    @DisplayName("test_getter_should_return_correct_value")
    void test_getter_should_return_correct_value() {
        // Given
        Task task = new Task("测试任务");

        // When
        String title = task.getTitle();
        boolean completed = task.isCompleted();
        int priority = task.getPriority();

        // Then
        assertEquals("测试任务", title, "getter 应返回正确的标题");
        assertFalse(completed, "getter 应返回正确的完成状态");
        assertEquals(2, priority, "默认优先级应该是 2（中）");
    }

    @Test
    @DisplayName("test_setter_should_update_value_with_validation")
    void test_setter_should_update_value_with_validation() {
        // Given
        Task task = new Task("原始标题");

        // When
        task.setTitle("新标题");
        task.setPriority(1); // 高优先级

        // Then
        assertEquals("新标题", task.getTitle(), "setter 应更新标题");
        assertEquals(1, task.getPriority(), "setter 应更新优先级");
    }
}
