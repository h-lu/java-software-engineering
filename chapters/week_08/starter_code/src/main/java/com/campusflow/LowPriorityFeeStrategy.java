package com.campusflow;

/**
 * 低优先级任务费用计算策略
 */
public class LowPriorityFeeStrategy implements FeeCalculationStrategy {
    private static final double BASE_FEE = 10.0;
    private static final double PRIORITY_MULTIPLIER = 1.0;

    @Override
    public double calculateFee(int daysOverdue) {
        if (daysOverdue < 0) {
            throw new IllegalArgumentException("逾期天数不能为负数");
        }
        if (daysOverdue == 0) {
            return 0.0;
        }
        double multiplier = applyOverdueMultiplier(PRIORITY_MULTIPLIER, daysOverdue);
        return BASE_FEE * daysOverdue * multiplier;
    }

    @Override
    public boolean supports(String priority) {
        return "low".equals(priority);
    }

    private double applyOverdueMultiplier(double multiplier, int daysOverdue) {
        if (daysOverdue > 30) {
            return multiplier * 1.5;
        } else if (daysOverdue > 7) {
            return multiplier * 1.2;
        }
        return multiplier;
    }
}
