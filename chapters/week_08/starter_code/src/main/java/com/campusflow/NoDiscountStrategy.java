package com.campusflow;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double total) {
        // 待办：原样返回 total。
        throw new UnsupportedOperationException("待办：实现 no-discount strategy");
    }

    @Override
    public boolean isApplicable(String context) {
        // 待办：决定 no-discount 在什么情况下适用。
        throw new UnsupportedOperationException("待办：实现 no-discount applicability");
    }
}
