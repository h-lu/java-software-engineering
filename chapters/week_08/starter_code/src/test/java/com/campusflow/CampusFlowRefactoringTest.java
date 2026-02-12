package com.campusflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CampusFlow 重构综合测试
 * 验证重构后的 TaskService 功能完整性和设计改进
 *
 * <p>本测试类覆盖：
 * - 重构后的 TaskService 功能测试
 * - 策略模式集成测试
 * - 仓库层委托测试
 * - 整体架构验证
 */
public class CampusFlowRefactoringTest {

    private InMemoryTaskRepository repository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        List<FeeCalculationStrategy> strategies = FeeStrategyFactory.createDefaultStrategies();
        taskService = new TaskService(repository, strategies);
    }

    // ==================== 正例：重构后功能正常 ====================

    @Test
    @DisplayName("重构后应能正常创建任务")
    void createTask_AfterRefactoring_ShouldWork() {
        Task task = taskService.createTask("完成Week08作业", "编写重构测试", "high");

        assertNotNull(task);
        assertNotNull(task.getId());
        assertEquals("完成Week08作业", task.getTitle());
        assertEquals("编写重构测试", task.getDescription());
        assertEquals("high", task.getPriority());
        assertEquals("pending", task.getStatus());
    }

    @ParameterizedTest(name = "优先级={0}")
    @CsvSource({
        "high",
        "medium",
        "low"
    })
    @DisplayName("重构后应支持所有优先级任务创建")
    void createTask_AllPriorities_ShouldWork(String priority) {
        Task task = taskService.createTask("测试任务", "测试描述", priority);

        assertEquals(priority, task.getPriority());
    }

    @Test
    @DisplayName("重构后应能通过仓库检索到创建的任务")
    void createTask_ShouldBeRetrievableFromRepository() {
        Task createdTask = taskService.createTask("测试任务", "描述", "medium");
        String taskId = createdTask.getId();

        Task retrievedTask = repository.findById(taskId).orElse(null);

        assertNotNull(retrievedTask);
        assertEquals(createdTask.getTitle(), retrievedTask.getTitle());
        assertEquals(createdTask.getPriority(), retrievedTask.getPriority());
    }

    // ==================== 策略模式集成测试 ====================

    @Test
    @DisplayName("重构后费用计算应使用策略模式")
    void calculateOverdueFee_ShouldUseStrategyPattern() {
        Task task = taskService.createTask("测试任务", "描述", "high");
        String taskId = task.getId();

        double fee = taskService.calculateOverdueFee(taskId, 10);

        // 高优先级10天：10 * 10 * 3.0 * 1.2 = 360
        assertEquals(360.0, fee, 0.001);
    }

    @ParameterizedTest(name = "优先级={0}, 逾期天数={1}, 预期费用={2}")
    @CsvSource({
        "high, 5, 150.0",      // 10 * 5 * 3.0
        "high, 10, 360.0",     // 10 * 10 * 3.0 * 1.2
        "high, 35, 1575.0",    // 10 * 35 * 3.0 * 1.5
        "medium, 5, 100.0",    // 10 * 5 * 2.0
        "medium, 10, 240.0",   // 10 * 10 * 2.0 * 1.2
        "low, 5, 50.0",        // 10 * 5 * 1.0
        "low, 10, 120.0"       // 10 * 10 * 1.0 * 1.2
    })
    @DisplayName("重构后各优先级费用计算应正确")
    void calculateOverdueFee_VariousPriorities_ShouldBeCorrect(String priority, int days, double expected) {
        Task task = taskService.createTask("测试任务", "描述", priority);

        double fee = taskService.calculateOverdueFee(task.getId(), days);

        assertEquals(expected, fee, 0.001);
    }

    // ==================== 边界值测试 ====================

    @Test
    @DisplayName("零逾期天数应返回零费用")
    void calculateOverdueFee_ZeroDays_ShouldReturnZero() {
        Task task = taskService.createTask("测试任务", "描述", "high");

        double fee = taskService.calculateOverdueFee(task.getId(), 0);

        assertEquals(0.0, fee, 0.001);
    }

    @Test
    @DisplayName("刚好7天逾期不应触发额外倍率")
    void calculateOverdueFee_Exactly7Days_ShouldNotApplyExtraMultiplier() {
        Task task = taskService.createTask("测试任务", "描述", "medium");

        double fee = taskService.calculateOverdueFee(task.getId(), 7);

        // 中优先级7天：10 * 7 * 2.0 = 140（不乘以1.2）
        assertEquals(140.0, fee, 0.001);
    }

    @Test
    @DisplayName("刚好8天逾期应触发7天倍率")
    void calculateOverdueFee_Exactly8Days_ShouldApply7DayMultiplier() {
        Task task = taskService.createTask("测试任务", "描述", "medium");

        double fee = taskService.calculateOverdueFee(task.getId(), 8);

        // 中优先级8天：10 * 8 * 2.0 * 1.2 = 192
        assertEquals(192.0, fee, 0.001);
    }

    // ==================== 反例：异常情况处理 ====================

    @Test
    @DisplayName("不存在的任务ID应抛出异常")
    void calculateOverdueFee_NonExistentTask_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.calculateOverdueFee("NON-EXISTENT-ID", 10);
        });
    }

    @Test
    @DisplayName("空标题应抛出异常")
    void createTask_EmptyTitle_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask("", "描述", "high");
        });
    }

    @Test
    @DisplayName("null标题应抛出异常")
    void createTask_NullTitle_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(null, "描述", "high");
        });
    }

    @Test
    @DisplayName("无效优先级应抛出异常")
    void createTask_InvalidPriority_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask("标题", "描述", "urgent");
        });
    }

    @Test
    @DisplayName("负逾期天数应抛出异常")
    void calculateOverdueFee_NegativeDays_ShouldThrowException() {
        Task task = taskService.createTask("测试任务", "描述", "high");

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.calculateOverdueFee(task.getId(), -1);
        });
    }

    // ==================== 架构设计验证 ====================

    @Test
    @DisplayName("TaskService应通过构造函数注入依赖")
    void taskService_ShouldUseDependencyInjection() {
        // 验证可以通过不同参数创建TaskService
        InMemoryTaskRepository anotherRepo = new InMemoryTaskRepository();
        List<FeeCalculationStrategy> strategies = List.of(new HighPriorityFeeStrategy());

        TaskService service = new TaskService(anotherRepo, strategies);

        assertNotNull(service);

        // 验证注入的依赖被使用
        Task task = service.createTask("测试", "描述", "high");
        assertTrue(anotherRepo.findById(task.getId()).isPresent());
    }

    @Test
    @DisplayName("仓库层应正确委托数据访问")
    void repositoryLayer_ShouldHandleDataAccess() {
        // 创建任务
        Task task = taskService.createTask("测试任务", "描述", "low");

        // 验证数据通过仓库层存储
        assertTrue(repository.findById(task.getId()).isPresent());

        // 验证仓库层独立工作
        repository.clear();
        assertFalse(repository.findById(task.getId()).isPresent());
    }

    @Test
    @DisplayName("策略列表为空时应抛出异常")
    void taskService_WithEmptyStrategies_ShouldHandleGracefully() {
        TaskService serviceWithEmptyStrategies = new TaskService(repository, List.of());
        Task task = serviceWithEmptyStrategies.createTask("测试", "描述", "high");

        // 当策略列表为空时，计算费用应抛出异常
        assertThrows(IllegalStateException.class, () -> {
            serviceWithEmptyStrategies.calculateOverdueFee(task.getId(), 10);
        });
    }

    // ==================== 重构收益验证 ====================

    @Test
    @DisplayName("重构后添加新策略应无需修改TaskService")
    void addingNewStrategy_ShouldNotRequireTaskServiceModification() {
        // 模拟添加一个新的"紧急"优先级策略
        FeeCalculationStrategy urgentStrategy = new FeeCalculationStrategy() {
            @Override
            public double calculateFee(int daysOverdue) {
                return daysOverdue * 100.0; // 紧急任务每天100
            }

            @Override
            public boolean supports(String priority) {
                return "urgent".equals(priority);
            }
        };

        // 使用包含新策略的列表创建TaskService
        List<FeeCalculationStrategy> extendedStrategies = List.of(
            new HighPriorityFeeStrategy(),
            new MediumPriorityFeeStrategy(),
            new LowPriorityFeeStrategy(),
            urgentStrategy
        );

        InMemoryTaskRepository newRepo = new InMemoryTaskRepository();
        TaskService extendedService = new TaskService(newRepo, extendedStrategies);

        // 手动添加一个紧急任务到仓库（因为createTask不直接支持urgent）
        Task urgentTask = new Task("URG-001", "紧急任务", "描述", "high", "pending");
        newRepo.save(urgentTask);

        // 验证高优先级任务仍能正常计算
        double fee = extendedService.calculateOverdueFee(urgentTask.getId(), 5);
        assertTrue(fee > 0);
    }

    @Test
    @DisplayName("重构后代码职责应清晰分离")
    void responsibilities_ShouldBeClearlySeparated() {
        // TaskService：业务逻辑协调
        // TaskRepository：数据访问（通过接口）
        // FeeCalculationStrategy：费用计算算法

        // 验证TaskService不包含具体计算逻辑（委托给策略）
        // 验证TaskService不包含数据访问实现（委托给仓库）

        Task task = taskService.createTask("测试", "描述", "medium");

        // 费用计算委托给策略
        double fee1 = taskService.calculateOverdueFee(task.getId(), 10);
        double fee2 = taskService.calculateOverdueFee(task.getId(), 10);
        assertEquals(fee1, fee2, "相同输入应产生相同输出");

        // 数据访问委托给仓库
        assertTrue(repository.findById(task.getId()).isPresent());
    }
}
