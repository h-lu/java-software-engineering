package com.campusflow;

import java.util.ArrayList;
import java.util.List;

/**
 * 费用策略工厂
 * 工厂模式：封装策略对象的创建逻辑
 */
public class FeeStrategyFactory {

    /**
     * 创建默认的策略列表（包含所有优先级）
     *
     * @return 策略列表
     */
    public static List<FeeCalculationStrategy> createDefaultStrategies() {
        List<FeeCalculationStrategy> strategies = new ArrayList<>();
        strategies.add(new HighPriorityFeeStrategy());
        strategies.add(new MediumPriorityFeeStrategy());
        strategies.add(new LowPriorityFeeStrategy());
        return strategies;
    }

    /**
     * 根据优先级创建对应的策略
     *
     * @param priority 优先级字符串
     * @return 对应的策略实例
     * @throws IllegalArgumentException 如果不支持的优先级
     */
    public static FeeCalculationStrategy createStrategy(String priority) {
        if (priority == null) {
            throw new IllegalArgumentException("优先级不能为空");
        }
        return switch (priority) {
            case "high" -> new HighPriorityFeeStrategy();
            case "medium" -> new MediumPriorityFeeStrategy();
            case "low" -> new LowPriorityFeeStrategy();
            default -> throw new IllegalArgumentException("不支持的优先级: " + priority);
        };
    }

    /**
     * 检查是否支持指定的优先级
     *
     * @param priority 优先级字符串
     * @return 如果支持返回 true
     */
    public static boolean isSupported(String priority) {
        if (priority == null) {
            return false;
        }
        return priority.equals("high") || priority.equals("medium") || priority.equals("low");
    }
}
