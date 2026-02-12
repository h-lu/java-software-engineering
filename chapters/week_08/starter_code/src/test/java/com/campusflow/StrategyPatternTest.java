package com.campusflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 策略模式测试
 * 验证策略接口、具体策略实现和策略切换功能
 *
 * <p>策略模式核心：定义一系列算法，把它们一个个封装起来，并且使它们可以互相替换
 */
public class StrategyPatternTest {

    private HighPriorityFeeStrategy highStrategy;
    private MediumPriorityFeeStrategy mediumStrategy;
    private LowPriorityFeeStrategy lowStrategy;

    @BeforeEach
    void setUp() {
        highStrategy = new HighPriorityFeeStrategy();
        mediumStrategy = new MediumPriorityFeeStrategy();
        lowStrategy = new LowPriorityFeeStrategy();
    }

    // ==================== 正例：各策略正常计算 ====================

    @Test
    @DisplayName("高优先级策略应正确计算逾期费用")
    void highPriorityStrategy_ShouldCalculateCorrectly() {
        // 高优先级：基础费10，倍率3.0，5天不触发额外倍率
        // 10 * 5 * 3.0 = 150
        assertEquals(150.0, highStrategy.calculateFee(5), 0.001);

        // 高优先级：10天超过7天，乘以1.2
        // 10 * 10 * 3.0 * 1.2 = 360
        assertEquals(360.0, highStrategy.calculateFee(10), 0.001);

        // 高优先级：35天超过30天，乘以1.5
        // 10 * 35 * 3.0 * 1.5 = 1575
        assertEquals(1575.0, highStrategy.calculateFee(35), 0.001);
    }

    @Test
    @DisplayName("中优先级策略应正确计算逾期费用")
    void mediumPriorityStrategy_ShouldCalculateCorrectly() {
        // 中优先级：基础费10，倍率2.0
        assertEquals(100.0, mediumStrategy.calculateFee(5), 0.001);  // 10 * 5 * 2.0
        assertEquals(240.0, mediumStrategy.calculateFee(10), 0.001); // 10 * 10 * 2.0 * 1.2
        assertEquals(1050.0, mediumStrategy.calculateFee(35), 0.001); // 10 * 35 * 2.0 * 1.5
    }

    @Test
    @DisplayName("低优先级策略应正确计算逾期费用")
    void lowPriorityStrategy_ShouldCalculateCorrectly() {
        // 低优先级：基础费10，倍率1.0
        assertEquals(50.0, lowStrategy.calculateFee(5), 0.001);   // 10 * 5 * 1.0
        assertEquals(120.0, lowStrategy.calculateFee(10), 0.001);  // 10 * 10 * 1.0 * 1.2
        assertEquals(525.0, lowStrategy.calculateFee(35), 0.001);  // 10 * 35 * 1.0 * 1.5
    }

    @ParameterizedTest(name = "逾期天数={0}")
    @CsvSource({
        "1, 30.0",   // 10 * 1 * 3.0
        "5, 150.0",  // 10 * 5 * 3.0
        "10, 360.0", // 10 * 10 * 3.0 * 1.2
        "20, 720.0", // 10 * 20 * 3.0 * 1.2
        "31, 1395.0" // 10 * 31 * 3.0 * 1.5
    })
    @DisplayName("高优先级策略参数化测试")
    void highPriorityStrategy_Parameterized(int daysOverdue, double expectedFee) {
        assertEquals(expectedFee, highStrategy.calculateFee(daysOverdue), 0.001);
    }

    // ==================== 边界值测试 ====================

    @Test
    @DisplayName("所有策略零逾期天数应返回零费用")
    void allStrategies_ZeroDays_ShouldReturnZero() {
        assertEquals(0.0, highStrategy.calculateFee(0), 0.001);
        assertEquals(0.0, mediumStrategy.calculateFee(0), 0.001);
        assertEquals(0.0, lowStrategy.calculateFee(0), 0.001);
    }

    @Test
    @DisplayName("所有策略刚好7天逾期不应触发额外倍率")
    void allStrategies_Exactly7Days_ShouldNotApplyExtraMultiplier() {
        assertEquals(210.0, highStrategy.calculateFee(7), 0.001);   // 10 * 7 * 3.0
        assertEquals(140.0, mediumStrategy.calculateFee(7), 0.001); // 10 * 7 * 2.0
        assertEquals(70.0, lowStrategy.calculateFee(7), 0.001);     // 10 * 7 * 1.0
    }

    @Test
    @DisplayName("所有策略刚好8天逾期应触发7天倍率")
    void allStrategies_Exactly8Days_ShouldApply7DayMultiplier() {
        assertEquals(288.0, highStrategy.calculateFee(8), 0.001);   // 10 * 8 * 3.0 * 1.2
        assertEquals(192.0, mediumStrategy.calculateFee(8), 0.001); // 10 * 8 * 2.0 * 1.2
        assertEquals(96.0, lowStrategy.calculateFee(8), 0.001);     // 10 * 8 * 1.0 * 1.2
    }

    @Test
    @DisplayName("所有策略刚好30天逾期应触发7天倍率")
    void allStrategies_Exactly30Days_ShouldApply7DayMultiplier() {
        assertEquals(1080.0, highStrategy.calculateFee(30), 0.001);   // 10 * 30 * 3.0 * 1.2
        assertEquals(720.0, mediumStrategy.calculateFee(30), 0.001);  // 10 * 30 * 2.0 * 1.2
        assertEquals(360.0, lowStrategy.calculateFee(30), 0.001);     // 10 * 30 * 1.0 * 1.2
    }

    @Test
    @DisplayName("所有策略刚好31天逾期应触发30天倍率")
    void allStrategies_Exactly31Days_ShouldApply30DayMultiplier() {
        assertEquals(1395.0, highStrategy.calculateFee(31), 0.001);   // 10 * 31 * 3.0 * 1.5
        assertEquals(930.0, mediumStrategy.calculateFee(31), 0.001);  // 10 * 31 * 2.0 * 1.5
        assertEquals(465.0, lowStrategy.calculateFee(31), 0.001);     // 10 * 31 * 1.0 * 1.5
    }

    // ==================== 策略支持判断测试 ====================

    @Test
    @DisplayName("高优先级策略应只支持high优先级")
    void highPriorityStrategy_ShouldOnlySupportHigh() {
        assertTrue(highStrategy.supports("high"));
        assertFalse(highStrategy.supports("medium"));
        assertFalse(highStrategy.supports("low"));
        assertFalse(highStrategy.supports("urgent"));
        assertFalse(highStrategy.supports(null));
    }

    @Test
    @DisplayName("中优先级策略应只支持medium优先级")
    void mediumPriorityStrategy_ShouldOnlySupportMedium() {
        assertFalse(mediumStrategy.supports("high"));
        assertTrue(mediumStrategy.supports("medium"));
        assertFalse(mediumStrategy.supports("low"));
        assertFalse(mediumStrategy.supports("urgent"));
        assertFalse(mediumStrategy.supports(null));
    }

    @Test
    @DisplayName("低优先级策略应只支持low优先级")
    void lowPriorityStrategy_ShouldOnlySupportLow() {
        assertFalse(lowStrategy.supports("high"));
        assertFalse(lowStrategy.supports("medium"));
        assertTrue(lowStrategy.supports("low"));
        assertFalse(lowStrategy.supports("urgent"));
        assertFalse(lowStrategy.supports(null));
    }

    // ==================== 反例：异常情况处理 ====================

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    @DisplayName("所有策略负逾期天数应抛出异常")
    void allStrategies_NegativeDays_ShouldThrowException(int daysOverdue) {
        assertThrows(IllegalArgumentException.class, () -> {
            highStrategy.calculateFee(daysOverdue);
        }, "高优先级策略应抛出异常");

        assertThrows(IllegalArgumentException.class, () -> {
            mediumStrategy.calculateFee(daysOverdue);
        }, "中优先级策略应抛出异常");

        assertThrows(IllegalArgumentException.class, () -> {
            lowStrategy.calculateFee(daysOverdue);
        }, "低优先级策略应抛出异常");
    }

    // ==================== 策略切换功能测试 ====================

    @Test
    @DisplayName("策略切换应能正确委托到对应策略")
    void strategySwitching_ShouldDelegateCorrectly() {
        // 模拟策略切换：根据优先级选择不同策略
        FeeCalculationStrategy selectedStrategy;
        String priority = "high";

        if (highStrategy.supports(priority)) {
            selectedStrategy = highStrategy;
        } else if (mediumStrategy.supports(priority)) {
            selectedStrategy = mediumStrategy;
        } else {
            selectedStrategy = lowStrategy;
        }

        double fee = selectedStrategy.calculateFee(10);
        assertEquals(360.0, fee, 0.001); // 高优先级10天费用

        // 切换到中优先级
        priority = "medium";
        if (highStrategy.supports(priority)) {
            selectedStrategy = highStrategy;
        } else if (mediumStrategy.supports(priority)) {
            selectedStrategy = mediumStrategy;
        } else {
            selectedStrategy = lowStrategy;
        }

        fee = selectedStrategy.calculateFee(10);
        assertEquals(240.0, fee, 0.001); // 中优先级10天费用
    }

    @Test
    @DisplayName("策略接口多态性测试")
    void strategyInterface_Polymorphism_ShouldWork() {
        // 使用接口类型引用具体策略
        FeeCalculationStrategy strategy1 = new HighPriorityFeeStrategy();
        FeeCalculationStrategy strategy2 = new MediumPriorityFeeStrategy();
        FeeCalculationStrategy strategy3 = new LowPriorityFeeStrategy();

        // 通过接口调用，实际执行各自策略
        assertEquals(360.0, strategy1.calculateFee(10), 0.001);
        assertEquals(240.0, strategy2.calculateFee(10), 0.001);
        assertEquals(120.0, strategy3.calculateFee(10), 0.001);
    }
}
