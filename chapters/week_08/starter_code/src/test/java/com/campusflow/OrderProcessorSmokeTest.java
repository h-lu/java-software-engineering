package com.campusflow;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProcessorSmokeTest {

    @Test
    void legacyProcessorShouldKeepKnownTotalBeforeRefactoring() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        OrderProcessor processor = new OrderProcessor(repository);

        Order order = processor.processOrder(
            "C001",
            List.of("book", "pen", "notebook"),
            "alipay",
            "standard",
            true,
            "SAVE10"
        );

        assertEquals(74.8, order.getTotal(), 0.001);
        assertEquals(1, repository.findByCustomerId("C001").size());
    }
}
