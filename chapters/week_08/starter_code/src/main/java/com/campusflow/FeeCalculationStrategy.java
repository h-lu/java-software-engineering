package com.campusflow;

/**
 * 费用计算策略接口
 * 策略模式：定义一系列算法，把它们一个个封装起来，并且使它们可以互相替换
 */
public interface FeeCalculationStrategy {
    /**
     * 计算逾期费用
     *
     * @param daysOverdue 逾期天数
     * @return 计算出的费用
     */
    double calculateFee(int daysOverdue);

    /**
     * 判断是否支持指定的优先级
     *
     * @param priority 优先级字符串
     * @return 如果支持返回 true
     */
    boolean supports(String priority);
}
