package com.campusflow;

public class CouponDiscountStrategy implements DiscountStrategy {
    private final String couponCode;

    public CouponDiscountStrategy(String couponCode) {
        this.couponCode = couponCode;
    }

    @Override
    public double applyDiscount(double total) {
        // TODO: apply SAVE10, SAVE20, HALF, and unknown-coupon behavior.
        throw new UnsupportedOperationException("TODO: implement coupon discount for " + couponCode);
    }

    @Override
    public boolean isApplicable(String context) {
        // TODO: return true when this strategy should participate.
        throw new UnsupportedOperationException("TODO: implement coupon applicability");
    }
}
