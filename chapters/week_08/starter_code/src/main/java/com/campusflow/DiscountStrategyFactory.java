package com.campusflow;

import java.util.List;

public class DiscountStrategyFactory {
    private DiscountStrategyFactory() {
    }

    public static List<DiscountStrategy> createStrategies(boolean isVip, String couponCode) {
        // TODO: create applicable strategies in the order the assignment requires.
        throw new UnsupportedOperationException("TODO: implement discount strategy factory");
    }
}
