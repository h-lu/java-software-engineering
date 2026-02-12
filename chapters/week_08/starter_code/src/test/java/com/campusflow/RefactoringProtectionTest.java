package com.campusflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 重构保护测试
 * 验证重构前后的行为一致性
 *
 * <p>重构的黄金法则：先写测试，再改代码。这些测试确保重构不改变外在行为。
 */
public class RefactoringProtectionTest {

    private OverdueFeeCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new OverdueFeeCalculator();
    }

    // ==================== 正例：正常输入的预期行为 ====================

    @Test
    @DisplayName("重构前后高优先级10天逾期费用计算结果应一致")
    void calculateFee_HighPriority10Days_ShouldBeConsistent() {
        String priority = "high";
        int daysOverdue = 10;

        double legacyResult = calculator.calculateFeeLegacy(priority, daysOverdue);
        double refactoredResult = calculator.calculateFeeRefactored(priority, daysOverdue);

        assertEquals(legacyResult, refactoredResult,
            "重构前后高优先级10天逾期费用计算结果应相同");
        // 高优先级基础倍率 3.0，10天超过7天，乘以1.2
        // 10 * 10 * 3.0 * 1.2 = 360
        assertEquals(360.0, refactoredResult, 0.001);
    }

    @ParameterizedTest(name = "优先级={0}, 逾期天数={1}")
    @CsvSource({
        "high, 5",
        "high, 10",
        "high, 35",
        "medium, 5",
        "medium, 15",
        "low, 3",
        "low, 20"
    })
    @DisplayName("重构前后各种输入组合的计算结果应保持一致")
    void calculateFee_VariousInputs_ShouldBeConsistent(String priority, int daysOverdue) {
        double legacyResult = calculator.calculateFeeLegacy(priority, daysOverdue);
        double refactoredResult = calculator.calculateFeeRefactored(priority, daysOverdue);

        assertEquals(legacyResult, refactoredResult, 0.001,
            String.format("优先级=%s, 逾期天数=%d 时重构前后结果应相同", priority, daysOverdue));
    }

    @Test
    @DisplayName("提取方法后零逾期天数应返回零费用")
    void calculateFee_ZeroDaysOverdue_ShouldReturnZero() {
        double legacyResult = calculator.calculateFeeLegacy("high", 0);
        double refactoredResult = calculator.calculateFeeRefactored("high", 0);

        assertEquals(0.0, legacyResult, 0.001);
        assertEquals(legacyResult, refactoredResult, 0.001);
    }

    // ==================== 边界值测试 ====================

    @Test
    @DisplayName("边界值：刚好7天逾期（不触发额外倍率）")
    void calculateFee_Exactly7Days_ShouldNotApplyExtraMultiplier() {
        String priority = "medium";
        int daysOverdue = 7;

        double legacyResult = calculator.calculateFeeLegacy(priority, daysOverdue);
        double refactoredResult = calculator.calculateFeeRefactored(priority, daysOverdue);

        assertEquals(legacyResult, refactoredResult, 0.001);
        // 中优先级倍率 2.0，7天不触发额外倍率
        // 10 * 7 * 2.0 = 140
        assertEquals(140.0, refactoredResult, 0.001);
    }

    @Test
    @DisplayName("边界值：刚好8天逾期（触发7天倍率）")
    void calculateFee_Exactly8Days_ShouldApply7DayMultiplier() {
        String priority = "medium";
        int daysOverdue = 8;

        double legacyResult = calculator.calculateFeeLegacy(priority, daysOverdue);
        double refactoredResult = calculator.calculateFeeRefactored(priority, daysOverdue);

        assertEquals(legacyResult, refactoredResult, 0.001);
        // 中优先级倍率 2.0，8天超过7天，乘以1.2
        // 10 * 8 * 2.0 * 1.2 = 192
        assertEquals(192.0, refactoredResult, 0.001);
    }

    @Test
    @DisplayName("边界值：刚好30天逾期（触发7天倍率，不触发30天倍率）")
    void calculateFee_Exactly30Days_ShouldApply7DayMultiplier() {
        String priority = "low";
        int daysOverdue = 30;

        double legacyResult = calculator.calculateFeeLegacy(priority, daysOverdue);
        double refactoredResult = calculator.calculateFeeRefactored(priority, daysOverdue);

        assertEquals(legacyResult, refactoredResult, 0.001);
        // 低优先级倍率 1.0，30天超过7天但不超过30天，乘以1.2
        // 10 * 30 * 1.0 * 1.2 = 360
        assertEquals(360.0, refactoredResult, 0.001);
    }

    @Test
    @DisplayName("边界值：刚好31天逾期（触发30天倍率）")
    void calculateFee_Exactly31Days_ShouldApply30DayMultiplier() {
        String priority = "low";
        int daysOverdue = 31;

        double legacyResult = calculator.calculateFeeLegacy(priority, daysOverdue);
        double refactoredResult = calculator.calculateFeeRefactored(priority, daysOverdue);

        assertEquals(legacyResult, refactoredResult, 0.001);
        // 低优先级倍率 1.0，31天超过30天，乘以1.5
        // 10 * 31 * 1.0 * 1.5 = 465
        assertEquals(465.0, refactoredResult, 0.001);
    }

    @Test
    @DisplayName("边界值：极长逾期天数计算结果应一致")
    void calculateFee_VeryLongOverdue_ShouldBeConsistent() {
        String priority = "high";
        int daysOverdue = 365;

        double legacyResult = calculator.calculateFeeLegacy(priority, daysOverdue);
        double refactoredResult = calculator.calculateFeeRefactored(priority, daysOverdue);

        assertEquals(legacyResult, refactoredResult, 0.001);
    }

    // ==================== 反例：异常情况处理 ====================

    @Test
    @DisplayName("重构前后空优先级都应抛出异常")
    void calculateFee_NullPriority_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFeeLegacy(null, 10);
        }, "重构前版本应抛出异常");

        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFeeRefactored(null, 10);
        }, "重构后版本应抛出异常");
    }

    @Test
    @DisplayName("重构前后负逾期天数都应抛出异常")
    void calculateFee_NegativeDays_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFeeLegacy("high", -1);
        }, "重构前版本应抛出异常");

        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFeeRefactored("high", -1);
        }, "重构后版本应抛出异常");
    }

    @Test
    @DisplayName("重构前后不支持的优先级都应抛出异常")
    void calculateFee_UnsupportedPriority_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFeeLegacy("urgent", 10);
        }, "重构前版本应抛出异常");

        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFeeRefactored("urgent", 10);
        }, "重构后版本应抛出异常");
    }

    // ==================== 提取方法单独测试 ====================

    @Test
    @DisplayName("提取方法后各优先级倍率计算应正确")
    void getPriorityMultiplier_AllPriorities_ShouldReturnCorrectValues() {
        // 通过重构后方法的输出来验证提取的方法逻辑正确
        // 10天逾期超过7天，触发1.2倍率
        // high: 10 * 10 * 3.0 * 1.2 = 360
        // medium: 10 * 10 * 2.0 * 1.2 = 240
        // low: 10 * 10 * 1.0 * 1.2 = 120
        assertEquals(360.0, calculator.calculateFeeRefactored("high", 10), 0.001,
            "高优先级10天逾期应使用3.0倍率并触发1.2倍率");
        assertEquals(240.0, calculator.calculateFeeRefactored("medium", 10), 0.001,
            "中优先级10天逾期应使用2.0倍率并触发1.2倍率");
        assertEquals(120.0, calculator.calculateFeeRefactored("low", 10), 0.001,
            "低优先级10天逾期应使用1.0倍率并触发1.2倍率");
    }
}
