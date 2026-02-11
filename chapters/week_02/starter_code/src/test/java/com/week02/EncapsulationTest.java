package com.week02;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

/**
 * 封装原则的验证测试。
 *
 * <p>这些测试验证：
 * 1. 字段是 private 的（外部不能直接访问）
 * 2. 数据验证在 setter 中进行
 * 3. 内部实现可以变化而不影响外部代码
 *
 * <p>本周学习目标：
 * - 理解为什么要封装（保护数据、控制变化）
 * - 理解 public 字段 vs private 字段 + getter/setter 的区别
 * - 理解如何用反射测试封装（高级技巧）
 */
@DisplayName("封装原则测试 - 验证数据保护")
class EncapsulationTest {

    // ==================== 封装验证测试 ====================

    @Test
    @DisplayName("test_task_fields_should_be_private")
    void test_task_fields_should_be_private() {
        // Given
        Task task = new Task("测试任务");

        // When & Then - 尝试用反射访问 private 字段
        // 这验证了字段确实是 private 的
        try {
            Field titleField = Task.class.getDeclaredField("title");
            Field completedField = Task.class.getDeclaredField("completed");

            // 验证字段是 private 的
            assertTrue(java.lang.reflect.Modifier.isPrivate(titleField.getModifiers()),
                "title 字段应该是 private");
            assertTrue(java.lang.reflect.Modifier.isPrivate(completedField.getModifiers()),
                "completed 字段应该是 private");

            // 验证默认情况下无法访问（需要 setAccessible(true)）
            assertFalse(titleField.canAccess(task),
                "private 字段默认不能从外部访问");

        } catch (NoSuchFieldException e) {
            fail("Task 类应该有 title 和 completed 字段");
        }
    }

    @Test
    @DisplayName("test_setter_should_validate_input")
    void test_setter_should_validate_input() {
        // Given
        Task task = new Task("原始标题");

        // When & Then - 验证 setter 会拒绝非法输入
        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle(""),
            "setter 应该拒绝空字符串");

        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle("   "),
            "setter 应该拒绝纯空格字符串");

        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle(null),
            "setter 应该拒绝 null");

        // 验证合法输入能成功设置
        task.setTitle("新标题");
        assertEquals("新标题", task.getTitle(),
            "合法的标题应该能成功设置");
    }

    @Test
    @DisplayName("test_priority_setter_should_validate_range")
    void test_priority_setter_should_validate_range() {
        // Given
        Task task = new Task("测试");

        // When & Then - 验证优先级范围检查
        // 有效范围：1-3
        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(0),
            "优先级 0 应该无效");

        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(4),
            "优先级 4 应该无效");

        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(-10),
            "负数优先级应该无效");

        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(100),
            "过大的优先级应该无效");

        // 验证合法的优先级
        task.setPriority(1);
        assertEquals(1, task.getPriority(), "优先级 1 应该有效");

        task.setPriority(2);
        assertEquals(2, task.getPriority(), "优先级 2 应该有效");

        task.setPriority(3);
        assertEquals(3, task.getPriority(), "优先级 3 应该有效");
    }

    // ==================== 不变性测试 ====================

    @Test
    @DisplayName("test_task_title_should_be_immutable_if_designed_so")
    void test_task_title_should_be_immutable_if_designed_so() {
        // Given
        Task task = new Task("原始标题");

        // When - 如果 Task 设计为不可变（没有 setTitle 方法）
        // Then - 下面的代码应该编译失败或抛出异常
        // 注意：这取决于你的设计选择
        // 如果 Task 有 setTitle，那么这个测试会通过（可变）
        // 如果 Task 没有 setTitle，那么 task.setTitle() 会编译失败

        // 当前设计假设 Task 有 setter（可变对象）
        task.setTitle("新标题");
        assertEquals("新标题", task.getTitle(),
            "当前设计允许修改标题（可变对象）");
    }

    @Test
    @DisplayName("test_immutable_object_should_not_allow_state_change")
    void test_immutable_object_should_not_allow_state_change() {
        // 这个测试演示不可变对象的概念
        // 如果你选择将 Task 设计为不可变（final 字段，无 setter）

        // Given
        String originalTitle = "原始标题";

        // When - 创建对象后，如果对象是不可变的
        // Then - 对象的状态不应该能被改变

        // 注意：这只是一个概念演示
        // 实际的实现取决于你的设计选择

        Task task = new Task(originalTitle);

        // 如果 Task 是不可变的，这里不应该有 setTitle 方法
        // 或者应该抛出 UnsupportedOperationException

        // 当前假设 Task 是可变的（有 setter）
        task.setTitle("新标题");
        assertEquals("新标题", task.getTitle(),
            "当前设计是可变对象");
    }

    // ==================== 内部实现变化测试 ====================

    @Test
    @DisplayName("test_implementation_can_change_without_breaking_external_code")
    void test_implementation_can_change_without_breaking_external_code() {
        // 这个测试演示封装的核心价值：
        // 内部实现可以改变，只要公共接口不变，外部代码不受影响

        // Given - 外部代码只使用公共接口
        Task task = new Task("测试任务");

        // When - 内部实现从 boolean completed 改成 enum Status
        // （这只是一个假设，实际代码可能还是 boolean）

        // Then - 外部代码仍然能用同样的方式访问
        // 无论是 isCompleted() 还是 getStatus() == COMPLETED
        // 外部代码不需要知道内部是怎么存储的

        boolean completed = task.isCompleted();
        assertFalse(completed,
            "外部代码通过方法访问，不关心内部实现");

        // 如果内部从 boolean 改成 enum，只需要修改 getter 实现
        // 外部代码完全不受影响
    }

    // ==================== 数据保护测试 ====================

    @Test
    @DisplayName("test_public_field_would_allow_invalid_state")
    void test_public_field_would_allow_invalid_state() {
        // 这个测试演示：如果字段是 public 的，数据可能被破坏

        // 假设有一个"糟糕的"设计，所有字段都是 public
        // （这个类不存在，只是概念演示）

        // 糟糕的设计：
        // public class BadTask {
        //     public String title;
        //     public int priority; // 1-3
        // }
        //
        // BadTask task = new BadTask();
        // task.priority = 100; // ❌ 无效的优先级！但代码能跑

        // 好的设计：
        Task task = new Task("测试");
        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(100),
            "好的设计通过 setter 验证，防止无效状态");
    }

    @Test
    @DisplayName("test_getter_should_return_defensive_copy_for_mutable_fields")
    void test_getter_should_return_defensive_copy_for_mutable_fields() {
        // 如果 Task 有可变对象字段（如 List、Date）
        // getter 应该返回防御性拷贝，而不是直接返回引用

        // 这是一个高级概念，Week 09 会详细讲
        // 当前 Task 只有 String 和 boolean，都是不可变的或基本类型

        // 示例（假设）：
        // private List<String> tags;
        //
        // 糟糕的 getter：
        // public List<String> getTags() { return tags; } // ❌
        //
        // 好的 getter：
        // public List<String> getTags() { return new ArrayList<>(tags); } // ✅

        // 当前 Task 没有可变对象字段，所以这个测试只是概念说明
        Task task = new Task("测试");
        String title = task.getTitle(); // String 是不可变的，安全
        assertEquals("测试", title,
            "String 是不可变的，不需要防御性拷贝");
    }

    // ==================== 封装的好处测试 ====================

    @Test
    @DisplayName("test_encapsulation_allows_controlled_change")
    void test_encapsulation_allows_controlled_change() {
        // 封装允许你在未来改变内部实现，而不影响外部代码

        // 场景 1：从 boolean completed 改成 enum Status
        // 外部代码：
        Task task = new Task("测试");
        if (task.isCompleted()) {
            // 处理已完成的任务
        }

        // 即使内部从 boolean 改成 enum：
        // public enum Status { NOT_STARTED, IN_PROGRESS, COMPLETED }
        // private Status status = Status.NOT_STARTED;
        //
        // public boolean isCompleted() {
        //     return status == Status.COMPLETED; // ✅ 方法签名不变
        // }
        //
        // 外部代码完全不需要改！

        // 这个测试验证 isCompleted() 方法存在且工作正常
        assertFalse(task.isCompleted(),
            "isCompleted() 方法提供了稳定的接口");

        task.markCompleted();
        assertTrue(task.isCompleted(),
            "外部代码不关心内部实现细节");
    }

    @Test
    @DisplayName("test_encapsulation_allows_validation_logic")
    void test_encapsulation_allows_validation_logic() {
        // 封装允许你在 setter 里加验证逻辑

        Task task = new Task("测试");

        // 验证逻辑在 setter 里，外部代码无法绕过
        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle(""),
            "setter 里的验证逻辑保护数据完整性");

        // 如果字段是 public 的，外部代码可以直接赋值：
        // task.title = ""; // ❌ 无法阻止

        // 但因为是 private + setter，必须经过验证：
        task.setTitle("有效的标题"); // ✅ 通过验证
        assertEquals("有效的标题", task.getTitle(),
            "只有合法的数据能被设置");
    }
}
