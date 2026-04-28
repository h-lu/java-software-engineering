package com.campusflow;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DiscountStrategyTodoTest {

    @Disabled("待办：实现 strategies 后移除 @Disabled")
    @Test
    void shouldApplyVipAndCouponStrategiesInOrder() {
        // 待办：total=100、VIP=true、coupon=SAVE20 时，结果应为 72.0。
    }

    @Disabled("待办：实现 factory 后移除 @Disabled")
    @Test
    void factoryShouldReturnOnlyApplicableStrategies() {
        // 待办：验证 VIP、coupon、no-discount 情况下的 strategy 数量和顺序。
    }
}
