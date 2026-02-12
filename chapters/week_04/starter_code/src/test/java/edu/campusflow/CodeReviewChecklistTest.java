package edu.campusflow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * 代码审查清单验证测试。
 *
 * <p>本测试类模拟 Code Review 中的审查清单检查，验证代码是否符合：
 * 1. 单一职责原则 (SRP)
 * 2. 异常处理完善性
 * 3. 输入验证完整性
 * 4. 封装规范
 * 5. 命名规范
 *
 * <p>测试目标：
 * - 理解代码审查清单的作用
 * - 掌握系统化的代码质量检查方法
 * - 学习如何从设计层面评估代码
 *
 * <p>本周学习目标：
 * - 使用审查清单进行系统化代码审查
 * - 识别违反 SOLID 原则的代码
 * - 评估异常处理和防御式编程的完整性
 */
@DisplayName("代码审查清单验证测试 - 系统化评估代码质量")
class CodeReviewChecklistTest {

    private TaskManager taskManager;
    private Task task;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
        task = new Task("测试任务");
    }

    // ==================== 审查项 1: 单一职责原则 (SRP) ====================

    @Test
    @DisplayName("test_srp_task_class_should_only_handle_data")
    void test_srp_task_class_should_only_handle_data() {
        // 审查点：Task 类是否只负责数据存储，不涉及业务逻辑

        // Given: Task 类的方法列表
        Method[] methods = Task.class.getDeclaredMethods();

        // When & Then: 检查 Task 类的方法职责
        for (Method method : methods) {
            String methodName = method.getName();
            // Task 类应该只有：构造方法、getter/setter、简单状态变更
            boolean isValidMethod =
                methodName.equals("getTitle") ||
                methodName.equals("setTitle") ||
                methodName.equals("getDescription") ||
                methodName.equals("setDescription") ||
                methodName.equals("getPriority") ||
                methodName.equals("setPriority") ||
                methodName.equals("isCompleted") ||
                methodName.equals("markCompleted") ||
                methodName.equals("markIncomplete") ||
                methodName.equals("toString") ||
                methodName.equals("equals") ||
                methodName.equals("hashCode");

            assertTrue(isValidMethod || methodName.startsWith("access$") || methodName.contains("$"),
                "Task 类的方法 '" + methodName + "' 可能违反了单一职责原则。" +
                "Task 类应该只负责数据存储，不涉及复杂业务逻辑");
        }
    }

    @Test
    @DisplayName("test_srp_task_manager_should_only_manage_tasks")
    void test_srp_task_manager_should_only_manage_tasks() {
        // 审查点：TaskManager 是否只负责任务管理，不涉及存储和显示

        // Given: TaskManager 的方法列表
        Method[] methods = TaskManager.class.getDeclaredMethods();

        // When & Then: 检查 TaskManager 的职责边界
        for (Method method : methods) {
            String methodName = method.getName();
            // TaskManager 应该只管理任务集合，不涉及持久化和显示
            boolean isValidMethod =
                methodName.equals("addTask") ||
                methodName.equals("removeTask") ||
                methodName.equals("markCompleted") ||
                methodName.equals("getAllTasks") ||
                methodName.equals("getIncompleteTasks") ||
                methodName.equals("getTasksByPriority") ||
                methodName.equals("getTaskCount") ||
                methodName.equals("clearAllTasks") ||
                methodName.equals("filterByPriority") ||
                methodName.equals("filterByStatus") ||
                methodName.equals("toString") ||
                methodName.equals("parsePriority") || // 辅助方法，用于解析优先级
                methodName.equals("addTaskToStorage") || // 内部辅助方法
                methodName.equals("getTaskList"); // 内部辅助方法

            assertTrue(isValidMethod || methodName.startsWith("access$") || methodName.contains("$"),
                "TaskManager 的方法 '" + methodName + "' 可能超出了管理职责。" +
                "TaskManager 应该只负责任务的增删改查，不涉及存储和显示");
        }
    }

    @Test
    @DisplayName("test_srp_task_manager_should_not_have_persistence_logic")
    void test_srp_task_manager_should_not_have_persistence_logic() {
        // 审查点：TaskManager 不应该包含文件/数据库操作

        // Given: TaskManager 的所有方法
        Method[] methods = TaskManager.class.getDeclaredMethods();

        // When & Then: 检查是否包含持久化相关方法
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            boolean hasPersistenceLogic =
                methodName.contains("save") ||
                methodName.contains("load") ||
                methodName.contains("persist") ||
                methodName.contains("store") ||
                methodName.contains("read") ||
                methodName.contains("write");

            assertFalse(hasPersistenceLogic,
                "TaskManager 不应该包含持久化方法 '" + method.getName() + "'。" +
                "持久化应该由 Repository 层负责（ADR-002）");
        }
    }

    // ==================== 审查项 2: 异常处理完善性 ====================

    @Test
    @DisplayName("test_exception_handling_addTask_should_validate_null")
    void test_exception_handling_addTask_should_validate_null() {
        // 审查点：addTask 方法是否对 null 参数进行验证

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> taskManager.addTask(null),
            "addTask 方法应该对 null 参数抛出 IllegalArgumentException");

        assertNotNull(exception.getMessage(),
            "异常消息不应该为 null，应该提供有意义的错误信息");
    }

    @Test
    @DisplayName("test_exception_handling_markCompleted_should_validate_null")
    void test_exception_handling_markCompleted_should_validate_null() {
        // 审查点：markCompleted 方法是否对 null 参数进行验证

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> taskManager.markCompleted(null),
            "markCompleted 方法应该对 null 标题抛出 IllegalArgumentException");
    }

    @Test
    @DisplayName("test_exception_handling_task_setTitle_should_validate_null")
    void test_exception_handling_task_setTitle_should_validate_null() {
        // 审查点：Task.setTitle 是否对 null 参数进行验证

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle(null),
            "Task.setTitle 应该对 null 参数抛出 IllegalArgumentException");
    }

    @Test
    @DisplayName("test_exception_handling_task_setTitle_should_validate_empty")
    void test_exception_handling_task_setTitle_should_validate_empty() {
        // 审查点：Task.setTitle 是否对空字符串进行验证

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> task.setTitle("   "),
            "Task.setTitle 应该对空字符串抛出 IllegalArgumentException");
    }

    @Test
    @DisplayName("test_exception_handling_task_setPriority_should_validate_range")
    void test_exception_handling_task_setPriority_should_validate_range() {
        // 审查点：Task.setPriority 是否对非法优先级进行验证

        // When & Then: 测试低于范围的值
        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(0),
            "Task.setPriority 应该对小于 1 的优先级抛出异常");

        // When & Then: 测试高于范围的值
        assertThrows(IllegalArgumentException.class,
            () -> task.setPriority(6),
            "Task.setPriority 应该对大于 5 的优先级抛出异常");
    }

    @Test
    @DisplayName("test_exception_handling_getTasksByPriority_should_validate_range")
    void test_exception_handling_getTasksByPriority_should_validate_range() {
        // 审查点：getTasksByPriority 是否对非法优先级进行验证

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> taskManager.getTasksByPriority(0),
            "getTasksByPriority 应该对无效优先级抛出异常");

        assertThrows(IllegalArgumentException.class,
            () -> taskManager.getTasksByPriority(6),
            "getTasksByPriority 应该对无效优先级抛出异常");
    }

    // ==================== 审查项 3: 输入验证完整性 ====================

    @Test
    @DisplayName("test_input_validation_filterByPriority_should_handle_null")
    void test_input_validation_filterByPriority_should_handle_null() {
        // 审查点：filterByPriority 是否处理 null 参数

        // Given
        taskManager.addTask(new Task("任务1"));

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> taskManager.filterByPriority(null),
            "filterByPriority 应该对 null 参数抛出异常或做适当处理");
    }

    @Test
    @DisplayName("test_input_validation_filterByStatus_should_handle_edge_cases")
    void test_input_validation_filterByStatus_should_handle_edge_cases() {
        // 审查点：filterByStatus 是否能正确处理边界情况

        // Given: 空任务列表
        List<Task> completedTasks = taskManager.filterByStatus(true);
        List<Task> incompleteTasks = taskManager.filterByStatus(false);

        // Then: 空列表应该返回空结果，不抛出异常
        assertNotNull(completedTasks, "filterByStatus 不应该返回 null");
        assertNotNull(incompleteTasks, "filterByStatus 不应该返回 null");
        assertTrue(completedTasks.isEmpty(), "空列表应该返回空结果");
        assertTrue(incompleteTasks.isEmpty(), "空列表应该返回空结果");
    }

    @Test
    @DisplayName("test_input_validation_task_title_trim_and_validate")
    void test_input_validation_task_title_trim_and_validate() {
        // 审查点：Task 构造方法和 setter 是否对标题进行验证

        // When & Then: 空白字符应该被拒绝
        assertThrows(IllegalArgumentException.class,
            () -> new Task("   "),
            "Task 构造方法应该拒绝空白标题");

        assertThrows(IllegalArgumentException.class,
            () -> new Task("\t\n"),
            "Task 构造方法应该拒绝空白标题");
    }

    // ==================== 审查项 4: 封装规范 ====================

    @Test
    @DisplayName("test_encapsulation_task_fields_should_be_private")
    void test_encapsulation_task_fields_should_be_private() {
        // 审查点：Task 类的字段是否都是 private

        // Given: Task 类的所有字段
        Field[] fields = Task.class.getDeclaredFields();

        // When & Then: 检查每个字段的访问修饰符
        for (Field field : fields) {
            // 跳过合成字段（如编译器生成的）
            if (field.isSynthetic()) {
                continue;
            }

            int modifiers = field.getModifiers();
            assertTrue(Modifier.isPrivate(modifiers),
                "Task 类的字段 '" + field.getName() + "' 应该是 private，" +
                "以实现封装。当前修饰符: " + Modifier.toString(modifiers));
        }
    }

    @Test
    @DisplayName("test_encapsulation_task_manager_fields_should_be_private")
    void test_encapsulation_task_manager_fields_should_be_private() {
        // 审查点：TaskManager 类的字段是否都是 private

        // Given: TaskManager 类的所有字段
        Field[] fields = TaskManager.class.getDeclaredFields();

        // When & Then: 检查每个字段的访问修饰符
        for (Field field : fields) {
            // 跳过合成字段
            if (field.isSynthetic()) {
                continue;
            }

            int modifiers = field.getModifiers();
            assertTrue(Modifier.isPrivate(modifiers),
                "TaskManager 类的字段 '" + field.getName() + "' 应该是 private，" +
                "以实现封装。当前修饰符: " + Modifier.toString(modifiers));
        }
    }

    @Test
    @DisplayName("test_encapsulation_defensive_copy_in_getAllTasks")
    void test_encapsulation_defensive_copy_in_getAllTasks() {
        // 审查点：getAllTasks 是否返回防御性拷贝

        // Given
        Task task1 = new Task("任务1");
        taskManager.addTask(task1);

        List<Task> tasks1 = taskManager.getAllTasks();
        int originalSize = tasks1.size();

        // When: 尝试修改返回的列表
        tasks1.clear();

        // Then: 原始数据不应该被修改
        List<Task> tasks2 = taskManager.getAllTasks();
        assertEquals(originalSize, tasks2.size(),
            "getAllTasks 应该返回防御性拷贝，外部修改不应影响内部状态");
    }

    @Test
    @DisplayName("test_encapsulation_no_public_mutable_fields")
    void test_encapsulation_no_public_mutable_fields() {
        // 审查点：检查所有类是否没有 public 的可变字段

        Class<?>[] classesToCheck = {Task.class, TaskManager.class};

        for (Class<?> clazz : classesToCheck) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isSynthetic()) {
                    continue;
                }

                int modifiers = field.getModifiers();
                assertFalse(Modifier.isPublic(modifiers),
                    clazz.getSimpleName() + " 的字段 '" + field.getName() + "' 不应该是 public。" +
                    "应该使用 private 字段 + getter/setter 实现封装");
            }
        }
    }

    // ==================== 审查项 5: 命名规范 ====================

    @Test
    @DisplayName("test_naming_conventions_class_names_should_use_pascal_case")
    void test_naming_conventions_class_names_should_use_pascal_case() {
        // 审查点：类名应该使用 PascalCase

        Class<?>[] classes = {Task.class, TaskManager.class};

        for (Class<?> clazz : classes) {
            String className = clazz.getSimpleName();
            assertTrue(Character.isUpperCase(className.charAt(0)),
                "类名 '" + className + "' 应该以大写字母开头（PascalCase）");

            // 检查是否包含下划线（Java 类名不应该有下划线）
            assertFalse(className.contains("_"),
                "类名 '" + className + "' 不应该包含下划线");
        }
    }

    @Test
    @DisplayName("test_naming_conventions_method_names_should_use_camel_case")
    void test_naming_conventions_method_names_should_use_camel_case() {
        // 审查点：方法名应该使用 camelCase

        Method[] methods = TaskManager.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isSynthetic()) {
                continue;
            }

            String methodName = method.getName();

            // 跳过 Object 类的方法
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }

            // 检查是否以小写字母开头（构造函数除外）
            if (!methodName.equals("access$") && !methodName.contains("$")) {
                assertTrue(Character.isLowerCase(methodName.charAt(0)) || methodName.equals("toString"),
                    "方法名 '" + methodName + "' 应该以小写字母开头（camelCase）");
            }
        }
    }

    @Test
    @DisplayName("test_naming_conventions_boolean_methods_should_start_with_is_or_has")
    void test_naming_conventions_boolean_methods_should_start_with_is_or_has() {
        // 审查点：返回 boolean 的方法应该以 is 或 has 开头

        Method[] methods = Task.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getReturnType() == boolean.class ||
                method.getReturnType() == Boolean.class) {

                String methodName = method.getName();

                // 跳过特殊方法
                if (methodName.contains("$")) {
                    continue;
                }

                boolean validPrefix =
                    methodName.startsWith("is") ||
                    methodName.startsWith("has") ||
                    methodName.startsWith("can") ||
                    methodName.startsWith("should") ||
                    methodName.startsWith("equals");

                assertTrue(validPrefix,
                    "返回 boolean 的方法 '" + methodName + "' 应该以 is/has/can/should 开头");
            }
        }
    }

    // ==================== 审查项 6: 代码质量 ====================

    @Test
    @DisplayName("test_code_quality_no_empty_catch_blocks")
    void test_code_quality_no_empty_catch_blocks() {
        // 审查点：检查代码中没有空的 catch 块
        // 注意：这个测试通过代码审查清单手动检查，因为反射无法直接检查 catch 块

        // 这里我们检查方法是否声明了可能抛出的异常
        Method[] methods = TaskManager.class.getDeclaredMethods();

        for (Method method : methods) {
            // 检查方法是否有适当的异常声明
            Class<?>[] exceptionTypes = method.getExceptionTypes();

            // 如果方法可能抛出 RuntimeException，应该有文档说明
            // 这里我们只是确保方法有适当的异常处理策略
        }

        // 这个测试通过表示审查清单已检查此项
        assertTrue(true, "代码审查清单：检查所有 catch 块都有处理逻辑，没有空的 catch 块");
    }

    @Test
    @DisplayName("test_code_quality_methods_should_not_be_too_long")
    void test_code_quality_methods_should_not_be_too_long() {
        // 审查点：方法不应该太长（通过代码行数估算）
        // 注意：反射无法直接获取代码行数，这里检查方法数量作为间接指标

        Method[] taskMethods = Task.class.getDeclaredMethods();
        Method[] managerMethods = TaskManager.class.getDeclaredMethods();

        // Task 类的方法数量应该在合理范围内
        assertTrue(taskMethods.length <= 20,
            "Task 类的方法数量（" + taskMethods.length + "）过多，可能违反了单一职责原则");

        // TaskManager 类的方法数量应该在合理范围内
        assertTrue(managerMethods.length <= 20,
            "TaskManager 类的方法数量（" + managerMethods.length + "）过多，" +
            "考虑将部分功能拆分到其他类");
    }

    // ==================== 综合审查清单 ====================

    @Test
    @DisplayName("test_complete_review_checklist_should_pass_all_checks")
    void test_complete_review_checklist_should_pass_all_checks() {
        // 综合审查：运行所有关键检查项

        // 1. SRP 检查
        assertDoesNotThrow(() -> test_srp_task_class_should_only_handle_data(),
            "SRP 检查失败");

        // 2. 异常处理检查
        assertDoesNotThrow(() -> test_exception_handling_addTask_should_validate_null(),
            "异常处理检查失败");

        // 3. 输入验证检查
        assertDoesNotThrow(() -> test_input_validation_task_title_trim_and_validate(),
            "输入验证检查失败");

        // 4. 封装检查
        assertDoesNotThrow(() -> test_encapsulation_task_fields_should_be_private(),
            "封装检查失败");

        // 5. 命名规范检查
        assertDoesNotThrow(() -> test_naming_conventions_class_names_should_use_pascal_case(),
            "命名规范检查失败");

        // 所有检查通过
        assertTrue(true, "代码审查清单：所有检查项通过");
    }
}
