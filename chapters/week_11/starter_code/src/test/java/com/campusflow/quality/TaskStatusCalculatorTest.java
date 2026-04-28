package com.campusflow.quality;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskStatusCalculatorTest {
    private final TaskStatusCalculator calculator = new TaskStatusCalculator();

    @Test
    void statusReturnsPendingForFutureTask() {
        String status = calculator.status(false, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 2, 20));

        assertEquals("pending", status);
    }

    // 待办：查看 JaCoCo 后，为 completed、unscheduled、overdue 分支补测试。
}
