package com.week02;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SOLID 原则的验证测试。
 *
 * <p>这些测试验证：
 * 1. 单一职责原则（SRP）- 每个类只有一个职责
 * 2. 开闭原则（OCP）- 对扩展开放，对修改关闭
 * 3. 依赖倒置原则（DIP）- 依赖抽象而非具体
 *
 * <p>本周学习目标：
 * - 理解什么是"上帝类"（God Class）
 * - 理解如何判断一个类是否违反 SRP
 * - 理解如何拆分职责过大的类
 */
@DisplayName("SOLID 原则测试 - 验证设计质量")
class SolidPrinciplesTest {

    // ==================== 单一职责原则（SRP）测试 ====================

    @Test
    @DisplayName("test_task_should_only_store_data")
    void test_task_should_only_store_data() {
        // Task 类应该只负责存储数据，不应该有其他职责

        // Given
        Task task = new Task("测试任务");

        // When - Task 应该提供 getter/setter
        String title = task.getTitle();
        boolean completed = task.isCompleted();

        // Then - Task 不应该负责：
        // ❌ 验证数据（应该由 TaskValidator 负责）
        // ❌ 持久化（应该由 TaskRepository 负责）
        // ❌ 显示（应该由 TaskPrinter 负责）
        // ❌ 邮件通知（应该由 EmailNotifier 负责）

        // Task 只负责存储和访问数据
        assertEquals("测试任务", title);
        assertFalse(completed);
    }

    @Test
    @DisplayName("test_task_manager_should_only_manage_tasks")
    void test_task_manager_should_only_manage_tasks() {
        // TaskManager 应该只负责管理任务，不应该有其他职责

        // Given
        TaskManager manager = new TaskManager();
        Task task = new Task("测试任务");

        // When - TaskManager 应该提供管理方法
        manager.addTask(task);
        manager.markCompleted("测试任务");

        // Then - TaskManager 不应该负责：
        // ❌ 存储任务数据（那是 Task 的职责）
        // ❌ 验证任务数据（那是 TaskValidator 的职责）
        // ❌ 持久化（那是 TaskRepository 的职责）
        // ❌ 显示输出（那是 TaskPrinter 的职责）

        // TaskManager 只负责管理
        assertEquals(1, manager.getTaskCount());
        assertTrue(task.isCompleted());
    }

    @Test
    @DisplayName("test_classes_should_have_clear_single_responsibility")
    void test_classes_should_have_clear_single_responsibility() {
        // 这个测试验证每个类都有一个清晰的职责

        // 验证 Task 的职责：存储数据
        Task task = new Task("测试");
        assertEquals("测试", task.getTitle(),
            "Task 应该能存储和返回数据");

        // 验证 TaskManager 的职责：管理任务
        TaskManager manager = new TaskManager();
        manager.addTask(task);
        assertEquals(1, manager.getTaskCount(),
            "TaskManager 应该能管理任务数量");

        // 如果未来有 TaskValidator：
        // TaskValidator validator = new TaskValidator();
        // validator.validate(task); // 验证数据合法性

        // 如果未来有 TaskPrinter：
        // TaskPrinter printer = new TaskPrinter();
        // printer.print(task); // 显示任务

        // 每个类只做一件事，职责清晰
    }

    // ==================== 开闭原则（OCP）测试 ====================

    @Test
    @DisplayName("test_task_manager_should_be_open_for_extension")
    void test_task_manager_should_be_open_for_extension() {
        // 开闭原则：对扩展开放，对修改关闭
        // 当需要新功能时，应该通过扩展实现，而不是修改现有代码

        // Given
        TaskManager manager = new TaskManager();

        // When - 可以添加新方法而不影响现有方法
        // 例如：添加 getTasksByPriority() 方法
        Task highPriorityTask = new Task("高优先级任务");
        highPriorityTask.setPriority(1);

        Task lowPriorityTask = new Task("低优先级任务");
        lowPriorityTask.setPriority(3);

        manager.addTask(highPriorityTask);
        manager.addTask(lowPriorityTask);

        // Then - 新方法不影响现有方法
        assertEquals(2, manager.getAllTasks().size(),
            "现有方法 getAllTasks() 仍然工作");
        assertEquals(1, manager.getTasksByPriority(1).size(),
            "新方法 getTasksByPriority() 也能工作");

        // 这就是"对扩展开放"：可以添加新功能
        // 而"对修改关闭"：不需要修改现有代码
    }

    @Test
    @DisplayName("test_polymorphism_allows_extension_without_modification")
    void test_polymorphism_allows_extension_without_modification() {
        // 这个测试演示如何通过多态实现扩展

        // 假设我们有一个 TaskFormatter 接口（Week 08 会讲接口）
        // public interface TaskFormatter {
        //     String format(Task task);
        // }
        //
        // 可以通过实现新类来扩展功能，而不修改现有代码：
        //
        // public class TextFormatter implements TaskFormatter {
        //     public String format(Task task) {
        //         return "任务：" + task.getTitle();
        //     }
        // }
        //
        // public class JsonFormatter implements TaskFormatter {
        //     public String format(Task task) {
        //         return "{ \"title\": \"" + task.getTitle() + "\" }";
        //     }
        // }
        //
        // TaskPrinter printer = new TaskPrinter();
        // printer.print(task, new TextFormatter());   // 文本格式
        // printer.print(task, new JsonFormatter());   // JSON 格式
        // printer.print(task, new XmlFormatter());    // 新增 XML 格式，不需要修改 TaskPrinter

        // 当前 Week 02 还没讲接口，这是一个预告
        // 重点理解：扩展 = 新建类，修改 = 改现有类
        // 开闭原则鼓励前者，避免后者

        // 简单验证：我们可以创建新的 Task 对象而不修改 Task 类
        Task task1 = new Task("任务1");
        Task task2 = new Task("任务2");

        // 每个 Task 对象是独立的，我们可以"扩展"创建更多对象
        // 而不需要修改 Task 类的定义
        assertEquals("任务1", task1.getTitle());
        assertEquals("任务2", task2.getTitle());
    }

    // ==================== "上帝类"反模式测试 ====================

    @Test
    @DisplayName("test_god_class_anti_pattern_should_be_avoided")
    void test_god_class_anti_pattern_should_be_avoided() {
        // 上帝类（God Class）是一个承担了太多职责的类

        // 糟糕的设计（上帝类）：
        // public class Task {
        //     public String title;
        //     public boolean completed;
        //
        //     // 职责 1：存储数据 ✅ 这是应该的
        //
        //     // 职责 2：验证数据 ❌ 应该由 TaskValidator 负责
        //     public boolean isValid() { ... }
        //
        //     // 职责 3：持久化 ❌ 应该由 TaskRepository 负责
        //     public void saveToFile(String filename) { ... }
        //
        //     // 职责 4：显示 ❌ 应该由 TaskPrinter 负责
        //     public void print() { ... }
        //
        //     // 职责 5：邮件通知 ❌ 应该由 EmailNotifier 负责
        //     public void sendEmailNotification(String email) { ... }
        // }

        // 好的设计（职责分离）：
        Task task = new Task("测试任务");           // 职责 1：存储数据
        TaskManager manager = new TaskManager();    // 职责 2：管理任务
        // TaskValidator validator = new TaskValidator(); // 职责 3：验证数据
        // TaskRepository repository = new TaskRepository(); // 职责 4：持久化
        // TaskPrinter printer = new TaskPrinter();  // 职责 5：显示

        // 验证职责分离：
        // 1. Task 只存储数据
        assertEquals("测试任务", task.getTitle());

        // 2. TaskManager 只管理任务
        manager.addTask(task);
        assertEquals(1, manager.getTaskCount());

        // 每个类职责单一，代码清晰，易于维护
    }

    @Test
    @DisplayName("test_responsibility_separation_makes_code_easier_to_maintain")
    void test_responsibility_separation_makes_code_easier_to_maintain() {
        // 这个测试验证职责分离的好处

        // Given - 创建一个任务管理器
        TaskManager manager = new TaskManager();
        Task task = new Task("测试任务");

        // When - 添加任务
        manager.addTask(task);

        // Then - 如果需要修改"任务存储方式"
        // 只需要修改 Task 类
        // TaskManager、TaskValidator、TaskPrinter 都不需要改

        // 如果需要修改"验证规则"
        // 只需要修改 TaskValidator 类
        // Task、TaskManager、TaskPrinter 都不需要改

        // 这就是单一职责原则的价值：
        // 一个需求变化，只需要改一个类

        // 验证当前设计的清晰性：
        assertEquals(1, manager.getTaskCount(),
            "TaskManager 负责管理");
        assertEquals("测试任务", task.getTitle(),
            "Task 负责存储");
    }

    // ==================== 判断 SRP 违反的测试 ====================

    @Test
    @DisplayName("test_how_to_detect_srp_violation")
    void test_how_to_detect_srp_violation() {
        // 如何判断一个类是否违反单一职责原则？

        // 问题 1：这个类有哪些"变化的原因"？
        // - 如果你能说出 2 个以上的原因，说明职责过多

        // 例如，如果一个 Task 类有这些方法：
        // - getTitle(), setTitle() → 原因 1：任务数据结构变化
        // - isValid() → 原因 2：验证规则变化
        // - saveToFile() → 原因 3：存储方式变化
        // - print() → 原因 4：显示格式变化
        // - sendEmailNotification() → 原因 5：通知方式变化
        //
        // 结论：5 个变化原因 = 职责过多 = 违反 SRP

        // 问题 2：能不能用一句话描述这个类的职责？
        // - 如果你的描述是"它负责 A，还有 B，还有 C……"
        // 说明职责过多

        // 好的描述：
        // - Task："它存储任务数据"（一句话，一个职责）✅
        // - TaskManager："它管理任务的增删改查"（一句话，一个职责）✅

        // 糟糕的描述：
        // - 上帝类 Task："它存储数据，还验证数据，还保存文件，还显示，还发邮件" ❌

        // 问题 3：如果需求变了，需要改几个类？
        // - 理想情况：一个需求变，只需要改 1 个类

        // 例如：验证规则变了
        // → 只需要改 TaskValidator（不应该影响 Task）

        // 验证当前设计：
        Task task = new Task("测试");
        TaskManager manager = new TaskManager();

        // Task 和 TaskManager 职责清晰
        // 修改 Task 的内部实现不应该影响 TaskManager
        // 修改 TaskManager 的管理逻辑不应该影响 Task
    }
}
