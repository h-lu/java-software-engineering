package com.campusflow;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DiscountStrategyTodoTest {

    @Disabled("TODO: remove @Disabled after implementing strategies")
    @Test
    void shouldApplyVipAndCouponStrategiesInOrder() {
        // TODO: total=100, VIP=true, coupon=SAVE20 should become 72.0.
    }

    @Disabled("TODO: remove @Disabled after implementing factory")
    @Test
    void factoryShouldReturnOnlyApplicableStrategies() {
        // TODO: verify strategy count/order for VIP, coupon, and no-discount cases.
    }
}
