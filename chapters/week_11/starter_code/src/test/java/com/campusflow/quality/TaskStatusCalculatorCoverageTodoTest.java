package com.campusflow.quality;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("Coverage template: add the missing branch assertions, then remove @Disabled.")
class TaskStatusCalculatorCoverageTodoTest {
    private final TaskStatusCalculator calculator = new TaskStatusCalculator();

    @Test
    void statusReturnsCompletedForFinishedTask() {
        String status = calculator.status(true, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 2, 20));

        assertEquals("completed", status);
    }

    @Test
    void statusReturnsUnscheduledWhenDueDateMissing() {
        String status = calculator.status(false, null, LocalDate.of(2026, 2, 20));

        assertEquals("unscheduled", status);
    }

    @Test
    void statusReturnsOverdueWhenPastDue() {
        String status = calculator.status(false, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 20));

        assertEquals("overdue", status);
    }
}
