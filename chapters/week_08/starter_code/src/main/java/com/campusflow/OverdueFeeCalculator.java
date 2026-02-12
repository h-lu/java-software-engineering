package com.campusflow;

/**
 * 逾期费用计算器（重构前版本）
 * 用于对比重构前后的行为一致性
 * 包含长方法和重复代码等坏味道
 */
public class OverdueFeeCalculator {
    private static final double BASE_FEE = 10.0;

    /**
     * 计算逾期费用（重构前：长方法，复杂条件判断）
     *
     * @param priority     任务优先级
     * @param daysOverdue  逾期天数
     * @return 逾期费用
     */
    public double calculateFeeLegacy(String priority, int daysOverdue) {
        // 参数校验
        if (priority == null) {
            throw new IllegalArgumentException("优先级不能为空");
        }
        if (daysOverdue < 0) {
            throw new IllegalArgumentException("逾期天数不能为负数");
        }
        if (daysOverdue == 0) {
            return 0.0;
        }

        // 复杂的条件判断（坏味道：长方法、复杂条件）
        double multiplier = 1.0;
        if (priority.equals("high")) {
            multiplier = 3.0;
        } else if (priority.equals("medium")) {
            multiplier = 2.0;
        } else if (priority.equals("low")) {
            multiplier = 1.0;
        } else {
            throw new IllegalArgumentException("不支持的优先级: " + priority);
        }

        // 逾期天数倍率（更多条件判断）
        if (daysOverdue > 30) {
            multiplier = multiplier * 1.5;
        } else if (daysOverdue > 7) {
            multiplier = multiplier * 1.2;
        }

        return BASE_FEE * daysOverdue * multiplier;
    }

    /**
     * 计算逾期费用（重构后：提取方法，使用策略模式）
     *
     * @param priority     任务优先级
     * @param daysOverdue  逾期天数
     * @return 逾期费用
     */
    public double calculateFeeRefactored(String priority, int daysOverdue) {
        validateInput(priority, daysOverdue);

        if (daysOverdue == 0) {
            return 0.0;
        }

        double multiplier = getPriorityMultiplier(priority);
        multiplier = applyOverdueMultiplier(multiplier, daysOverdue);

        return BASE_FEE * daysOverdue * multiplier;
    }

    /**
     * 验证输入参数
     */
    private void validateInput(String priority, int daysOverdue) {
        if (priority == null) {
            throw new IllegalArgumentException("优先级不能为空");
        }
        if (daysOverdue < 0) {
            throw new IllegalArgumentException("逾期天数不能为负数");
        }
    }

    /**
     * 获取优先级倍率（重构后：使用 switch 表达式）
     */
    private double getPriorityMultiplier(String priority) {
        return switch (priority) {
            case "high" -> 3.0;
            case "medium" -> 2.0;
            case "low" -> 1.0;
            default -> throw new IllegalArgumentException("不支持的优先级: " + priority);
        };
    }

    /**
     * 应用逾期倍率
     */
    private double applyOverdueMultiplier(double multiplier, int daysOverdue) {
        if (daysOverdue > 30) {
            return multiplier * 1.5;
        } else if (daysOverdue > 7) {
            return multiplier * 1.2;
        }
        return multiplier;
    }
}
