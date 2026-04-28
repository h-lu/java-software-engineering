package com.campusflow;

public class VipDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double total) {
        // TODO: apply the VIP discount.
        throw new UnsupportedOperationException("TODO: implement VIP discount");
    }

    @Override
    public boolean isApplicable(String context) {
        // TODO: decide how your refactored code represents VIP context.
        throw new UnsupportedOperationException("TODO: implement VIP applicability");
    }
}
