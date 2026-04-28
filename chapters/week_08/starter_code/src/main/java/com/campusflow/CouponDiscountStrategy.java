package com.campusflow;

public class CouponDiscountStrategy implements DiscountStrategy {
    private final String couponCode;

    public CouponDiscountStrategy(String couponCode) {
        this.couponCode = couponCode;
    }

    @Override
    public double applyDiscount(double total) {
        // 待办：处理 SAVE10、SAVE20、HALF，以及未知 coupon 的行为。
        throw new UnsupportedOperationException("待办：为 coupon 实现折扣：" + couponCode);
    }

    @Override
    public boolean isApplicable(String context) {
        // 待办：当该 strategy 应参与计算时返回 true。
        throw new UnsupportedOperationException("待办：实现 coupon applicability");
    }
}
