package com.campusflow;

public class VipDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double total) {
        // 待办：应用 VIP discount。
        throw new UnsupportedOperationException("待办：实现 VIP discount");
    }

    @Override
    public boolean isApplicable(String context) {
        // 待办：决定重构后的代码如何表示 VIP context。
        throw new UnsupportedOperationException("待办：实现 VIP applicability");
    }
}
