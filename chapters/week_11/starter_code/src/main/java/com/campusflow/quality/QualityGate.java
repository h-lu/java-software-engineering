package com.campusflow.quality;

import java.util.ArrayList;
import java.util.List;

/**
 * 质量门禁配置。
 *
 * <p>本类定义了代码质量检查的规则和阈值。
 * 质量门禁确保代码在合并前达到预设的质量标准。
 *
 * <p>支持的检查类型：
 * <ul>
 *   <li>SpotBugs 静态分析</li>
 *   <li>JaCoCo 代码覆盖率</li>
 *   <li>自定义规则</li>
 * </ul>
 */
public class QualityGate {

    private final String name;
    private final List<Rule> rules;
    private GateStatus status;

    /**
     * 质量门禁状态。
     */
    public enum GateStatus {
        /** 通过：所有检查都通过 */
        PASSED,
        /** 警告：某些检查未通过，但允许合并 */
        WARNING,
        /** 失败：某些检查未通过，阻止合并 */
        FAILED,
        /** 跳过：检查未执行 */
        SKIPPED
    }

    /**
     * 规则动作。
     */
    public enum RuleAction {
        /** 阻断：不通过就阻止合并 */
        BLOCK,
        /** 警告：不通过仅警告，允许合并 */
        WARN,
        /** 信息：仅显示信息 */
        INFO
    }

    /**
     * 质量规则。
     */
    public static class Rule {
        private final String tool;
        private final String metric;
        private final double threshold;
        private final RuleAction action;

        public Rule(String tool, String metric, double threshold, RuleAction action) {
            this.tool = tool;
            this.metric = metric;
            this.threshold = threshold;
            this.action = action;
        }

        public String getTool() {
            return tool;
        }

        public String getMetric() {
            return metric;
        }

        public double getThreshold() {
            return threshold;
        }

        public RuleAction getAction() {
            return action;
        }
    }

    /**
     * 质量检查结果。
     */
    public static class CheckResult {
        private final Rule rule;
        private final double actualValue;
        private final boolean passed;
        private final String message;

        public CheckResult(Rule rule, double actualValue, boolean passed, String message) {
            this.rule = rule;
            this.actualValue = actualValue;
            this.passed = passed;
            this.message = message;
        }

        public Rule getRule() {
            return rule;
        }

        public double getActualValue() {
            return actualValue;
        }

        public boolean isPassed() {
            return passed;
        }

        public String getMessage() {
            return message;
        }
    }

    public QualityGate(String name) {
        this.name = name;
        this.rules = new ArrayList<>();
        this.status = GateStatus.SKIPPED;
    }

    /**
     * 添加规则。
     *
     * @param rule 规则
     * @return 本对象，用于链式调用
     */
    public QualityGate addRule(Rule rule) {
        rules.add(rule);
        return this;
    }

    /**
     * 创建默认的质量门禁配置。
     *
     * @return 默认质量门禁
     */
    public static QualityGate createDefault() {
        QualityGate gate = new QualityGate("Default Quality Gate");

        // SpotBugs: 高优先级问题必须修复
        gate.addRule(new Rule("spotbugs", "high_priority_bugs", 0, RuleAction.BLOCK));

        // JaCoCo: 行覆盖率至少 70%
        gate.addRule(new Rule("jacoco", "line_coverage", 0.70, RuleAction.WARN));

        // JaCoCo: 分支覆盖率至少 50%
        gate.addRule(new Rule("jacoco", "branch_coverage", 0.50, RuleAction.WARN));

        return gate;
    }

    /**
     * 创建严格的质量门禁配置（用于核心库）。
     *
     * @return 严格质量门禁
     */
    public static QualityGate createStrict() {
        QualityGate gate = new QualityGate("Strict Quality Gate");

        // SpotBugs: 高和中优先级问题必须修复
        gate.addRule(new Rule("spotbugs", "high_priority_bugs", 0, RuleAction.BLOCK));
        gate.addRule(new Rule("spotbugs", "medium_priority_bugs", 0, RuleAction.BLOCK));

        // JaCoCo: 行覆盖率至少 90%
        gate.addRule(new Rule("jacoco", "line_coverage", 0.90, RuleAction.BLOCK));

        // JaCoCo: 分支覆盖率至少 80%
        gate.addRule(new Rule("jacoco", "branch_coverage", 0.80, RuleAction.WARN));

        return gate;
    }

    /**
     * 创建宽松的质量门禁配置（用于实验性功能）。
     *
     * @return 宽松质量门禁
     */
    public static QualityGate createLenient() {
        QualityGate gate = new QualityGate("Lenient Quality Gate");

        // SpotBugs: 仅高优先级问题警告
        gate.addRule(new Rule("spotbugs", "high_priority_bugs", 0, RuleAction.WARN));

        // JaCoCo: 行覆盖率至少 50%
        gate.addRule(new Rule("jacoco", "line_coverage", 0.50, RuleAction.INFO));

        return gate;
    }

    /**
     * 评估质量门禁。
     *
     * @param checkResults 检查结果列表
     * @return 门禁状态
     */
    public GateStatus evaluate(List<CheckResult> checkResults) {
        boolean hasBlockFailure = false;
        boolean hasWarningFailure = false;

        for (CheckResult result : checkResults) {
            if (!result.isPassed()) {
                if (result.getRule().getAction() == RuleAction.BLOCK) {
                    hasBlockFailure = true;
                } else if (result.getRule().getAction() == RuleAction.WARN) {
                    hasWarningFailure = true;
                }
            }
        }

        if (hasBlockFailure) {
            this.status = GateStatus.FAILED;
        } else if (hasWarningFailure) {
            this.status = GateStatus.WARNING;
        } else {
            this.status = GateStatus.PASSED;
        }

        return this.status;
    }

    public String getName() {
        return name;
    }

    public List<Rule> getRules() {
        return new ArrayList<>(rules);
    }

    public GateStatus getStatus() {
        return status;
    }
}
