package com.campusflow.quality;

import java.time.LocalDate;

public class TaskStatusCalculator {
    public String status(boolean completed, LocalDate dueDate, LocalDate today) {
        if (completed) {
            return "completed";
        }
        if (dueDate == null) {
            return "unscheduled";
        }
        if (dueDate.isBefore(today)) {
            return "overdue";
        }
        return "pending";
    }
}
