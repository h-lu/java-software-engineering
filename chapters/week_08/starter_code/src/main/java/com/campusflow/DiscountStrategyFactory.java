package com.campusflow;

import java.util.List;

public class DiscountStrategyFactory {
    private DiscountStrategyFactory() {
    }

    public static List<DiscountStrategy> createStrategies(boolean isVip, String couponCode) {
        // 待办：按作业要求的顺序创建适用的 strategies。
        throw new UnsupportedOperationException("待办：实现 discount strategy factory");
    }
}
