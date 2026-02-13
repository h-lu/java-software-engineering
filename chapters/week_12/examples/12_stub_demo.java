/*
 * 示例：Stub 示例
 * 运行方式：阅读理解（需 JUnit 5）
 * 预期输出：理解 Stub 与 Mock 的区别
 *
 * 本例演示：
 * 1. 展示 Stub 与 Mock 的区别
 * 2. Stub 返回预定义值
 * 3. Stub 没有验证功能
 */
package examples;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Stub 示例：测试替身的另一种类型
 *
 * <p>Stub vs Mock 对比：
 *
 * <table>
 *   <tr><th>维度</th><th>Stub</th><th>Mock</th></tr>
 *   <tr><td>返回值</td><td>预设返回值</td><td>预设返回值</td></tr>
 *   <tr><td>验证</td><td>❌ 无验证功能</td><td>✅ 验证调用</td></tr>
 *   <tr><td>关注点</td><td>输入 → 输出</td><td>交互行为</td></tr>
 *   <tr><td>用途</td><td>隔离依赖</td><td>行为验证</td></tr>
 * </table>
 *
 * <p>通俗解释：
 * <ul>
 *   <li>Stub：像录音机，按下播放键就返回预设的内容</li>
 *   <li>Mock：像间谍，不仅返回预设内容，还记录"谁调用了它"</li>
 * </ul>
 */
public class _12_stub_demo {

    // ========== Stub 示例：手动实现 ==========

    /**
     * 示例 1：手动 Stub（最简单的形式）
     *
     * <p>手动创建一个返回固定值的 Stub 对象。
     */
    @Test
    @DisplayName("Stub 1: 手动实现 Stub")
    void manualStub() {
        // given: 手动创建 Stub
        TaskRepository stubRepo = new TaskRepositoryStub();

        // when: 调用方法
        Optional<Task> task = stubRepo.findById("1");

        // then: 返回预设的值
        assertTrue(task.isPresent());
        assertEquals("Stub 任务", task.get().title);

        // ❌ Stub 没有验证功能
        // 我们无法验证 findById("1") 是否被调用
    }

    /**
     * 手动实现的 Stub 类。
     *
     * <p>这个类只返回预设的值，不关心是否被调用。
     */
    static class TaskRepositoryStub implements TaskRepository {
        @Override
        public Optional<Task> findById(String id) {
            // 总是返回预设的任务
            return Optional.of(new Task("1", "Stub 任务", false));
        }

        @Override
        public List<Task> findAll() {
            return List.of(new Task("1", "Stub 任务", false));
        }

        @Override
        public Task save(Task task) {
            return task;
        }

        @Override
        public void delete(String id) {
            // 什么也不做
        }
    }

    // ========== Mockito Stub 示例 ==========

    /**
     * 示例 2：使用 Mockito 创建 Stub
     *
     * <p>Mockito 的 mock() 方法创建的对象既是 Stub 也是 Mock。
     * 如果只使用 when().thenReturn()，它就是 Stub。
     */
    @Test
    @DisplayName("Stub 2: Mockito Stub（只用返回值）")
    void mockitoStub() {
        // given: 创建 Mock 对象并预设返回值（Stub 模式）
        TaskRepository stubRepo = mock(TaskRepository.class);
        when(stubRepo.findById("1")).thenReturn(
            Optional.of(new Task("1", "Mockito Stub 任务", false)));

        // when: 调用方法
        Optional<Task> task = stubRepo.findById("1");

        // then: 验证返回值
        assertTrue(task.isPresent());
        assertEquals("Mockito Stub 任务", task.get());

        // ❌ 这是 Stub 模式，我们不验证调用
        // 我们只关心"返回了什么"，不关心"是否被调用"
    }

    /**
     * 示例 3：Mock 模式（验证调用）
     *
     * <p>如果使用 verify()，Mock 对象就变成了 Mock 模式。
     */
    @Test
    @DisplayName("Mock 3: Mockito Mock（验证调用）")
    void mockitoMock() {
        // given: 创建 Mock 对象
        TaskRepository mockRepo = mock(TaskRepository.class);
        when(mockRepo.findById("1")).thenReturn(
            Optional.of(new Task("1", "Mock 任务", false)));

        TaskService service = new TaskService(mockRepo);

        // when: 调用 Service
        service.getTask("1");

        // then: 验证返回值（Stub 功能）
        assertEquals("Mock 任务", service.getTask("1").get().title);

        // ✅ 验证调用（Mock 功能）
        verify(mockRepo).findById("1");  // 验证是否调用了 findById("1")
    }

    // ========== 对比：Stub vs Mock ==========

    /**
     * 示例 4：Stub 适用于"输入 → 输出"测试
     *
     * <p>当测试只关注返回值时，使用 Stub。
     */
    @Test
    @DisplayName("示例 4: Stub 适用场景 - 输入输出测试")
    void stubUseCase() {
        // given: Stub 只返回固定值
        TaxRateService stubTaxService = mock(TaxRateService.class);
        when(stubTaxService.getRate("CN")).thenReturn(0.13);  // 中国税率 13%

        Calculator calculator = new Calculator(stubTaxService);

        // when: 计算税后价格
        double priceWithTax = calculator.calculatePrice(100, "CN");

        // then: 验证结果
        assertEquals(113.0, priceWithTax, 0.001);

        // ❌ 不验证调用，因为这不重要
        // 重要的是"给定 100 元，税率 13%，结果应该是 113 元"
    }

    /**
     * 示例 5：Mock 适用于"交互行为"测试
     *
     * <p>当测试需要验证"是否调用了某个方法"时，使用 Mock。
     */
    @Test
    @DisplayName("示例 5: Mock 适用场景 - 交互行为测试")
    void mockUseCase() {
        // given: Mock 对象
        NotificationService mockNotification = mock(NotificationService.class);

        OrderService orderService = new OrderService(mockNotification);

        // when: 完成订单
        orderService.completeOrder("order-123");

        // then: 验证交互行为
        verify(mockNotification).sendEmail("order-123");  // ✅ 验证是否发送邮件
        verify(mockNotification).sendSms("order-123");    // ✅ 验证是否发送短信

        // 重要的是"订单完成后，是否发送了邮件和短信"
    }

    /**
     * 示例 6：Stub 的局限性
     *
     * <p>Stub 无法验证"没有发生的事情"。
     */
    @Test
    @DisplayName("示例 6: Stub 的局限性")
    void stubLimitation() {
        // given: Stub 对象
        TaskRepository stubRepo = mock(TaskRepository.class);
        when(stubRepo.findById("1")).thenReturn(
            Optional.of(new Task("1", "任务", false)));

        // when: 只调用 findById
        stubRepo.findById("1");

        // then: Stub 无法检测"未调用的方法"
        // ❌ 我们无法知道 delete() 是否被调用
        // 如果需要验证，必须使用 Mock 模式
    }

    /**
     * 示例 7：Mock 可以验证"未调用"
     *
     * <p>Mock 可以验证"某个方法从未被调用"。
     */
    @Test
    @DisplayName("示例 7: Mock 可以验证未调用")
    void mockCanVerifyNeverCalled() {
        // given: Mock 对象
        TaskRepository mockRepo = mock(TaskRepository.class);
        when(mockRepo.findById("1")).thenReturn(
            Optional.of(new Task("1", "任务", false)));

        // when: 只调用 findById
        mockRepo.findById("1");

        // then: ✅ Mock 可以验证 delete() 未被调用
        verify(mockRepo, never()).delete("1");
    }

    // ========== 决策树：Stub vs Mock ==========

    /**
     * 决策：什么时候用 Stub，什么时候用 Mock？
     *
     * <table>
     *   <tr><th>场景</th><th>选择</th></tr>
     *   <tr><td>只关心返回值</td><td>Stub（when().thenReturn()）</td></tr>
     *   <tr><td>需要验证交互</td><td>Mock（verify()）</td></tr>
     *   <tr><td>测试算法/计算</td><td>Stub</td></tr>
     *   <tr><td>测试事件/通知</td><td>Mock</td></tr>
     *   <tr><td>测试查询方法</td><td>Stub</td></tr>
     *   <tr><td>测试命令方法</td><td>Mock</td></tr>
     * </table>
     *
     * <p>记忆口诀：
     * <ul>
     *   <li>Stub = 回答"返回了什么"</li>
     *   <li>Mock = 回答"发生了什么"</li>
     * </ul>
     */
    @Test
    @DisplayName("决策示例: 查询用 Stub，命令用 Mock")
    void queryVsCommand() {
        // 场景 1: 查询方法（用 Stub）
        TaskRepository stubRepo = mock(TaskRepository.class);
        when(stubRepo.findById("1")).thenReturn(
            Optional.of(new Task("1", "任务", false)));

        Task task = stubRepo.findById("1").get();
        assertEquals("任务", task.title);  // ✅ 只验证返回值

        // 场景 2: 命令方法（用 Mock）
        TaskRepository mockRepo = mock(TaskRepository.class);

        mockRepo.delete("1");

        verify(mockRepo).delete("1");  // ✅ 验证方法被调用
    }

    // ========== 辅助类 ==========

    /**
     * Task 实体。
     */
    static class Task {
        String id;
        String title;
        boolean completed;

        Task(String id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }
    }

    /**
     * Task Repository 接口。
     */
    interface TaskRepository {
        Optional<Task> findById(String id);
        List<Task> findAll();
        Task save(Task task);
        void delete(String id);
    }

    /**
     * Task Service。
     */
    static class TaskService {
        private final TaskRepository repository;

        TaskService(TaskRepository repository) {
            this.repository = repository;
        }

        public Optional<Task> getTask(String id) {
            return repository.findById(id);
        }
    }

    /**
     * 税率服务（用于 Stub 示例）。
     */
    interface TaxRateService {
        double getRate(String countryCode);
    }

    /**
     * 计算器（查询方法测试示例）。
     */
    static class Calculator {
        private final TaxRateService taxService;

        Calculator(TaxRateService taxService) {
            this.taxService = taxService;
        }

        public double calculatePrice(double basePrice, String countryCode) {
            double rate = taxService.getRate(countryCode);
            return basePrice * (1 + rate);
        }
    }

    /**
     * 通知服务（用于 Mock 示例）。
     */
    interface NotificationService {
        void sendEmail(String orderId);
        void sendSms(String orderId);
    }

    /**
     * 订单服务（命令方法测试示例）。
     */
    static class OrderService {
        private final NotificationService notificationService;

        OrderService(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        public void completeOrder(String orderId) {
            // 完成订单后发送通知
            notificationService.sendEmail(orderId);
            notificationService.sendSms(orderId);
        }
    }
}

/**
 * 总结：Stub vs Mock
 *
 * <table>
 *   <tr>
 *     <th>维度</th>
 *     <th>Stub</th>
 *     <th>Mock</th>
 *   </tr>
 *   <tr>
 *     <td><strong>核心功能</strong></td>
 *     <td>返回预设值</td>
 *     <td>返回预设值 + 验证调用</td>
 *   </tr>
 *   <tr>
 *     <td><strong>验证能力</strong></td>
 *     <td>❌ 无</td>
 *     <td>✅ 有</td>
 *   </tr>
 *   <tr>
 *     <td><strong>关注点</strong></td>
 *     <td>输入 → 输出</td>
 *     <td>交互行为</td>
 *   </tr>
 *   <tr>
 *     <td><strong>测试类型</strong></td>
 *     <td>状态验证</td>
 *     <td>行为验证</td>
 *   </tr>
 *   <tr>
 *     <td><strong>适用场景</strong></td>
 *     <td>查询方法、算法、计算</td>
 *     <td>命令方法、事件、通知</td>
 *   </tr>
 *   <tr>
 *     <td><strong>记忆口诀</strong></td>
 *     <td>"返回了什么？"</td>
 *     <td>"发生了什么？"</td>
 *   </tr>
 * </table>
 *
 * <p>在 Mockito 中：
 * <ul>
 *   <li>如果只用 when().thenReturn()，它就是 Stub</li>
 *   <li>如果加了 verify()，它就是 Mock</li>
 *   <li>Mockito 的对象可以同时是 Stub 和 Mock</li>
 * </ul>
 */
