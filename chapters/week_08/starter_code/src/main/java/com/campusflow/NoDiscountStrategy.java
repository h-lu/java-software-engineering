package com.campusflow;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double total) {
        // TODO: return total unchanged.
        throw new UnsupportedOperationException("TODO: implement no-discount strategy");
    }

    @Override
    public boolean isApplicable(String context) {
        // TODO: decide when no-discount applies.
        throw new UnsupportedOperationException("TODO: implement no-discount applicability");
    }
}
