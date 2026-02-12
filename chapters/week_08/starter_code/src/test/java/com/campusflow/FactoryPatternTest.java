package com.campusflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 工厂模式测试
 * 验证工厂创建对象的功能和边界处理
 *
 * <p>工厂模式核心：把对象创建的复杂性封装起来
 */
public class FactoryPatternTest {

    // ==================== 正例：工厂创建对象 ====================

    @Test
    @DisplayName("创建默认策略列表应包含所有三种策略")
    void createDefaultStrategies_ShouldContainAllStrategies() {
        List<FeeCalculationStrategy> strategies = FeeStrategyFactory.createDefaultStrategies();

        assertEquals(3, strategies.size(), "应包含3个策略");

        // 验证包含各优先级的策略
        boolean hasHigh = strategies.stream().anyMatch(s -> s.supports("high"));
        boolean hasMedium = strategies.stream().anyMatch(s -> s.supports("medium"));
        boolean hasLow = strategies.stream().anyMatch(s -> s.supports("low"));

        assertTrue(hasHigh, "应包含高优先级策略");
        assertTrue(hasMedium, "应包含中优先级策略");
        assertTrue(hasLow, "应包含低优先级策略");
    }

    @ParameterizedTest(name = "优先级={0}")
    @CsvSource({
        "high, HighPriorityFeeStrategy",
        "medium, MediumPriorityFeeStrategy",
        "low, LowPriorityFeeStrategy"
    })
    @DisplayName("根据优先级创建策略应返回正确类型")
    void createStrategy_WithValidPriority_ShouldReturnCorrectType(String priority, String expectedClassName) {
        FeeCalculationStrategy strategy = FeeStrategyFactory.createStrategy(priority);

        assertNotNull(strategy, "策略不应为null");
        assertTrue(strategy.supports(priority), "策略应支持对应的优先级");
        assertEquals(expectedClassName, strategy.getClass().getSimpleName(),
            "应创建正确的策略类型");
    }

    @Test
    @DisplayName("创建的策略应能正确计算费用")
    void createdStrategy_ShouldCalculateFeeCorrectly() {
        FeeCalculationStrategy highStrategy = FeeStrategyFactory.createStrategy("high");
        FeeCalculationStrategy mediumStrategy = FeeStrategyFactory.createStrategy("medium");
        FeeCalculationStrategy lowStrategy = FeeStrategyFactory.createStrategy("low");

        // 验证各策略计算正确
        assertEquals(360.0, highStrategy.calculateFee(10), 0.001);
        assertEquals(240.0, mediumStrategy.calculateFee(10), 0.001);
        assertEquals(120.0, lowStrategy.calculateFee(10), 0.001);
    }

    // ==================== 边界值测试 ====================

    @Test
    @DisplayName("每次创建策略应返回新实例")
    void createStrategy_ShouldReturnNewInstances() {
        FeeCalculationStrategy strategy1 = FeeStrategyFactory.createStrategy("high");
        FeeCalculationStrategy strategy2 = FeeStrategyFactory.createStrategy("high");

        assertNotSame(strategy1, strategy2, "每次创建应返回不同实例");
        assertEquals(strategy1.getClass(), strategy2.getClass(), "但类型应相同");
    }

    @Test
    @DisplayName("默认策略列表中的策略应能正常工作")
    void defaultStrategies_ShouldBeFunctional() {
        List<FeeCalculationStrategy> strategies = FeeStrategyFactory.createDefaultStrategies();

        // 验证每个策略都能计算费用
        for (FeeCalculationStrategy strategy : strategies) {
            assertDoesNotThrow(() -> {
                double fee = strategy.calculateFee(10);
                assertTrue(fee > 0, "费用应大于0");
            });
        }
    }

    // ==================== 反例：异常情况处理 ====================

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"urgent", "critical", "normal", "HIGH", "High"})
    @DisplayName("不支持的优先级应抛出异常")
    void createStrategy_WithInvalidPriority_ShouldThrowException(String invalidPriority) {
        assertThrows(IllegalArgumentException.class, () -> {
            FeeStrategyFactory.createStrategy(invalidPriority);
        }, "不支持的优先级应抛出IllegalArgumentException");
    }

    @Test
    @DisplayName("空优先级创建策略应抛出异常并包含提示信息")
    void createStrategy_NullPriority_ShouldThrowWithMessage() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            FeeStrategyFactory.createStrategy(null);
        });

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("优先级") || exception.getMessage().contains("null"),
            "异常信息应提示优先级问题");
    }

    // ==================== 支持检查测试 ====================

    @ParameterizedTest
    @CsvSource({
        "high, true",
        "medium, true",
        "low, true",
        "urgent, false",
        "critical, false",
        "HIGH, false",
        "High, false"
    })
    @DisplayName("isSupported应正确判断优先级是否受支持")
    void isSupported_ShouldReturnCorrectResult(String priority, boolean expected) {
        assertEquals(expected, FeeStrategyFactory.isSupported(priority),
            "优先级 '" + priority + "' 的支持状态应正确");
    }

    @Test
    @DisplayName("空优先级isSupported应返回false")
    void isSupported_NullPriority_ShouldReturnFalse() {
        assertFalse(FeeStrategyFactory.isSupported(null),
            "null优先级应返回false");
    }

    // ==================== 工厂与策略协作测试 ====================

    @Test
    @DisplayName("工厂创建的策略应与手动创建的策略行为一致")
    void factoryCreatedStrategy_ShouldBehaveSameAsManual() {
        // 工厂创建
        FeeCalculationStrategy factoryHigh = FeeStrategyFactory.createStrategy("high");
        // 手动创建
        FeeCalculationStrategy manualHigh = new HighPriorityFeeStrategy();

        // 相同输入应产生相同输出
        assertEquals(manualHigh.calculateFee(5), factoryHigh.calculateFee(5), 0.001);
        assertEquals(manualHigh.calculateFee(10), factoryHigh.calculateFee(10), 0.001);
        assertEquals(manualHigh.calculateFee(30), factoryHigh.calculateFee(30), 0.001);
    }

    @Test
    @DisplayName("使用工厂简化客户端代码")
    void factorySimplifiesClientCode() {
        // 客户端只需要知道优先级，不需要知道具体策略类
        String[] priorities = {"high", "medium", "low"};

        for (String priority : priorities) {
            // 简单调用，无需了解策略实现细节
            FeeCalculationStrategy strategy = FeeStrategyFactory.createStrategy(priority);
            double fee = strategy.calculateFee(15);

            assertTrue(fee > 0, priority + " 优先级应计算出正数费用");
        }
    }

    @Test
    @DisplayName("默认策略列表可用于TaskService构造")
    void defaultStrategies_CanBeUsedWithTaskService() {
        List<FeeCalculationStrategy> strategies = FeeStrategyFactory.createDefaultStrategies();
        InMemoryTaskRepository repository = new InMemoryTaskRepository();

        // 使用工厂创建的策略列表构造TaskService
        TaskService service = new TaskService(repository, strategies);

        assertNotNull(service, "TaskService应成功创建");

        // 验证服务能正常工作
        Task task = service.createTask("测试任务", "描述", "high");
        assertNotNull(task);
        assertEquals("high", task.getPriority());
    }
}
