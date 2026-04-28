package edu.campusflow.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskSmokeTest {

    @Test
    void taskShouldKeepRequiredFields() {
        Task task = new Task("T1", "完成 Week 07 作业", "实现 JDBC Repository", "pending");

        assertEquals("T1", task.getId());
        assertEquals("完成 Week 07 作业", task.getTitle());
        assertEquals("pending", task.getStatus());
    }

    @Test
    void taskShouldRejectBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Task("T1", " ", "desc", "pending"));
    }
}
