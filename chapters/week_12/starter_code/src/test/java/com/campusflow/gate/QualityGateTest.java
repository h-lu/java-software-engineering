package com.campusflow.gate;

import com.campusflow.quality.QualityGate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 质量门禁测试。
 *
 * <p>本测试类验证质量门禁的各种配置和评估逻辑。
 *
 * <p>测试矩阵：
 * <table>
 *   <tr><th>场景</th><th>正例</th><th>边界</th><th>反例</th></tr>
 *   <tr><th>默认门禁</th><th>所有规则通过</th><th>覆盖率为 70%</th><th>高优先级 bug 阻断</th></tr>
 *   <tr><th>严格门禁</th><th>所有规则通过</th><th>覆盖率为 90%</th><th>中优先级 bug 阻断</th></tr>
 *   <tr><th>宽松门禁</th><th>所有规则通过</th><th>覆盖率为 50%</th><th>仅警告不阻断</th></tr>
 * </table>
 */
@DisplayName("质量门禁测试")
class QualityGateTest {

    // ===== 默认门禁测试 =====

    @Test
    @DisplayName("默认门禁: 应包含正确的规则数量")
    void defaultGate_ShouldHaveCorrectRuleCount() {
        QualityGate gate = QualityGate.createDefault();
        assertEquals(3, gate.getRules().size());
    }

    @Test
    @DisplayName("默认门禁: 应包含 SpotBugs 高优先级规则")
    void defaultGate_ShouldContainSpotBugsHighPriorityRule() {
        QualityGate gate = QualityGate.createDefault();
        boolean hasSpotBugsRule = gate.getRules().stream()
                .anyMatch(rule -> "spotbugs".equals(rule.getTool())
                        && "high_priority_bugs".equals(rule.getMetric())
                        && rule.getAction() == QualityGate.RuleAction.BLOCK);
        assertTrue(hasSpotBugsRule);
    }

    @Test
    @DisplayName("默认门禁: 应包含 JaCoCo 行覆盖率规则（警告）")
    void defaultGate_ShouldContainJacocoLineCoverageRule() {
        QualityGate gate = QualityGate.createDefault();
        boolean hasJacocoRule = gate.getRules().stream()
                .anyMatch(rule -> "jacoco".equals(rule.getTool())
                        && "line_coverage".equals(rule.getMetric())
                        && rule.getAction() == QualityGate.RuleAction.WARN
                        && rule.getThreshold() == 0.70);
        assertTrue(hasJacocoRule);
    }

    @Test
    @DisplayName("默认门禁: 应包含 JaCoCo 分支覆盖率规则（警告）")
    void defaultGate_ShouldContainJacocoBranchCoverageRule() {
        QualityGate gate = QualityGate.createDefault();
        boolean hasJacocoRule = gate.getRules().stream()
                .anyMatch(rule -> "jacoco".equals(rule.getTool())
                        && "branch_coverage".equals(rule.getMetric())
                        && rule.getAction() == QualityGate.RuleAction.WARN
                        && rule.getThreshold() == 0.50);
        assertTrue(hasJacocoRule);
    }

    // ===== 严格门禁测试 =====

    @Test
    @DisplayName("严格门禁: 应包含正确的规则数量")
    void strictGate_ShouldHaveCorrectRuleCount() {
        QualityGate gate = QualityGate.createStrict();
        assertEquals(4, gate.getRules().size());
    }

    @Test
    @DisplayName("严格门禁: 应包含 SpotBugs 中优先级规则")
    void strictGate_ShouldContainSpotBugsMediumPriorityRule() {
        QualityGate gate = QualityGate.createStrict();
        boolean hasMediumRule = gate.getRules().stream()
                .anyMatch(rule -> "spotbugs".equals(rule.getTool())
                        && "medium_priority_bugs".equals(rule.getMetric())
                        && rule.getAction() == QualityGate.RuleAction.BLOCK);
        assertTrue(hasMediumRule);
    }

    @Test
    @DisplayName("严格门禁: 行覆盖率阈值应为 90%")
    void strictGate_ShouldHave90PercentLineCoverage() {
        QualityGate gate = QualityGate.createStrict();
        boolean hasHighCoverage = gate.getRules().stream()
                .anyMatch(rule -> "jacoco".equals(rule.getTool())
                        && "line_coverage".equals(rule.getMetric())
                        && rule.getThreshold() == 0.90);
        assertTrue(hasHighCoverage);
    }

    @Test
    @DisplayName("严格门禁: 分支覆盖率阈值应为 80%")
    void strictGate_ShouldHave80PercentBranchCoverage() {
        QualityGate gate = QualityGate.createStrict();
        boolean hasHighCoverage = gate.getRules().stream()
                .anyMatch(rule -> "jacoco".equals(rule.getTool())
                        && "branch_coverage".equals(rule.getMetric())
                        && rule.getThreshold() == 0.80);
        assertTrue(hasHighCoverage);
    }

    // ===== 宽松门禁测试 =====

    @Test
    @DisplayName("宽松门禁: 应包含正确的规则数量")
    void lenientGate_ShouldHaveCorrectRuleCount() {
        QualityGate gate = QualityGate.createLenient();
        assertEquals(2, gate.getRules().size());
    }

    @Test
    @DisplayName("宽松门禁: SpotBugs 应仅警告不阻断")
    void lenientGate_SpotBugsShouldWarnNotBlock() {
        QualityGate gate = QualityGate.createLenient();
        boolean warnsOnly = gate.getRules().stream()
                .anyMatch(rule -> "spotbugs".equals(rule.getTool())
                        && rule.getAction() == QualityGate.RuleAction.WARN);
        assertTrue(warnsOnly);
    }

    @Test
    @DisplayName("宽松门禁: 行覆盖率阈值应为 50%")
    void lenientGate_ShouldHave50PercentLineCoverage() {
        QualityGate gate = QualityGate.createLenient();
        boolean hasLowCoverage = gate.getRules().stream()
                .anyMatch(rule -> "jacoco".equals(rule.getTool())
                        && "line_coverage".equals(rule.getMetric())
                        && rule.getThreshold() == 0.50);
        assertTrue(hasLowCoverage);
    }

    @Test
    @DisplayName("宽松门禁: 分支覆盖率规则应为 INFO 级别")
    void lenientGate_BranchCoverageShouldBeInfo() {
        QualityGate gate = QualityGate.createLenient();
        boolean hasInfoRule = gate.getRules().stream()
                .anyMatch(rule -> "jacoco".equals(rule.getTool())
                        && rule.getAction() == QualityGate.RuleAction.INFO);
        assertTrue(hasInfoRule);
    }

    // ===== 门禁评估测试 =====

    @Test
    @DisplayName("评估: 所有检查通过应返回 PASSED")
    void evaluate_WhenAllChecksPass_ShouldReturnPassed() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        // 所有检查都通过
        QualityGate.Rule rule1 = new QualityGate.Rule("spotbugs", "high_priority_bugs", 0, QualityGate.RuleAction.BLOCK);
        results.add(new QualityGate.CheckResult(rule1, 0, true, "No bugs"));

        QualityGate.Rule rule2 = new QualityGate.Rule("jacoco", "line_coverage", 0.70, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule2, 0.80, true, "Good coverage"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.PASSED, status);
    }

    @Test
    @DisplayName("评估: BLOCK 规则失败应返回 FAILED")
    void evaluate_WhenBlockRuleFails_ShouldReturnFailed() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        // BLOCK 规则失败
        QualityGate.Rule rule1 = new QualityGate.Rule("spotbugs", "high_priority_bugs", 0, QualityGate.RuleAction.BLOCK);
        results.add(new QualityGate.CheckResult(rule1, 5, false, "Found 5 high priority bugs"));

        QualityGate.Rule rule2 = new QualityGate.Rule("jacoco", "line_coverage", 0.70, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule2, 0.80, true, "Good coverage"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.FAILED, status);
    }

    @Test
    @DisplayName("评估: WARN 规则失败应返回 WARNING")
    void evaluate_WhenWarnRuleFails_ShouldReturnWarning() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        QualityGate.Rule rule1 = new QualityGate.Rule("spotbugs", "high_priority_bugs", 0, QualityGate.RuleAction.BLOCK);
        results.add(new QualityGate.CheckResult(rule1, 0, true, "No bugs"));

        // WARN 规则失败
        QualityGate.Rule rule2 = new QualityGate.Rule("jacoco", "line_coverage", 0.70, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule2, 0.60, false, "Coverage below threshold"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.WARNING, status);
    }

    @Test
    @DisplayName("评估: BLOCK 和 WARN 都失败应返回 FAILED（阻断优先）")
    void evaluate_WhenBothBlockAndWarnFail_ShouldReturnFailed() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        // BLOCK 规则失败
        QualityGate.Rule rule1 = new QualityGate.Rule("spotbugs", "high_priority_bugs", 0, QualityGate.RuleAction.BLOCK);
        results.add(new QualityGate.CheckResult(rule1, 3, false, "Found bugs"));

        // WARN 规则也失败
        QualityGate.Rule rule2 = new QualityGate.Rule("jacoco", "line_coverage", 0.70, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule2, 0.50, false, "Low coverage"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.FAILED, status);
    }

    @Test
    @DisplayName("评估: 空结果列表应返回 PASSED")
    void evaluate_WhenEmptyResults_ShouldReturnPassed() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.PASSED, status);
    }

    @Test
    @DisplayName("评估: INFO 规则失败不应影响门禁状态")
    void evaluate_WhenInfoRuleFails_ShouldReturnPassed() {
        QualityGate gate = QualityGate.createLenient();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        QualityGate.Rule rule1 = new QualityGate.Rule("spotbugs", "high_priority_bugs", 0, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule1, 0, true, "No bugs"));

        // INFO 规则失败（但不应影响状态）
        QualityGate.Rule rule2 = new QualityGate.Rule("jacoco", "line_coverage", 0.50, QualityGate.RuleAction.INFO);
        results.add(new QualityGate.CheckResult(rule2, 0.30, false, "Low coverage but INFO only"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.PASSED, status);
    }

    // ===== 自定义门禁测试 =====

    @Test
    @DisplayName("自定义门禁: 应能添加自定义规则")
    void customGate_ShouldAllowAddingCustomRule() {
        QualityGate gate = new QualityGate("Custom Gate");
        QualityGate.Rule customRule = new QualityGate.Rule("custom", "metric", 0.80, QualityGate.RuleAction.BLOCK);

        gate.addRule(customRule);

        assertEquals(1, gate.getRules().size());
        assertEquals(customRule, gate.getRules().get(0));
    }

    @Test
    @DisplayName("自定义门禁: 应支持链式调用")
    void customGate_ShouldSupportFluentApi() {
        QualityGate gate = new QualityGate("Fluent Gate")
                .addRule(new QualityGate.Rule("tool1", "metric1", 0.5, QualityGate.RuleAction.WARN))
                .addRule(new QualityGate.Rule("tool2", "metric2", 0.8, QualityGate.RuleAction.BLOCK));

        assertEquals(2, gate.getRules().size());
    }

    @Test
    @DisplayName("自定义门禁: 应保持设置的名称")
    void customGate_ShouldKeepSetName() {
        QualityGate gate = new QualityGate("My Custom Gate");
        assertEquals("My Custom Gate", gate.getName());
    }

    // ===== 边界条件测试 =====

    @Test
    @DisplayName("边界: 覆盖率恰好等于阈值应通过")
    void boundary_WhenCoverageEqualsThreshold_ShouldPass() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        QualityGate.Rule rule = new QualityGate.Rule("jacoco", "line_coverage", 0.70, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule, 0.70, true, "Exactly at threshold"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.PASSED, status);
    }

    @Test
    @DisplayName("边界: 覆盖率略低于阈值应失败")
    void boundary_WhenCoverageSlightlyBelowThreshold_ShouldFail() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        QualityGate.Rule rule = new QualityGate.Rule("jacoco", "line_coverage", 0.70, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule, 0.699, false, "Just below threshold"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.WARNING, status);
    }

    @Test
    @DisplayName("边界: 覆盖率为 0 应失败")
    void boundary_WhenCoverageIsZero_ShouldFail() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        QualityGate.Rule rule = new QualityGate.Rule("jacoco", "line_coverage", 0.70, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule, 0.0, false, "No coverage"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.WARNING, status);
    }

    @Test
    @DisplayName("边界: 覆盖率为 1 应通过")
    void boundary_WhenCoverageIsFull_ShouldPass() {
        QualityGate gate = QualityGate.createDefault();
        List<QualityGate.CheckResult> results = new ArrayList<>();

        QualityGate.Rule rule = new QualityGate.Rule("jacoco", "line_coverage", 0.70, QualityGate.RuleAction.WARN);
        results.add(new QualityGate.CheckResult(rule, 1.0, true, "Full coverage"));

        QualityGate.GateStatus status = gate.evaluate(results);
        assertEquals(QualityGate.GateStatus.PASSED, status);
    }
}
