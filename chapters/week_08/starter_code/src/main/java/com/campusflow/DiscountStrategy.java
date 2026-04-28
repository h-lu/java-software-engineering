package com.campusflow;

public interface DiscountStrategy {
    double applyDiscount(double total);

    boolean isApplicable(String context);
}
