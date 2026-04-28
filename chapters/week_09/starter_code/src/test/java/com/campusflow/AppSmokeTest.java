package com.campusflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppSmokeTest {
    @Test
    void healthPayloadShowsStarterIsWired() {
        assertTrue(App.healthJson().contains("CampusFlow"));
    }

    @Test
    void taskPayloadRemainsAStudentTodo() {
        assertTrue(App.tasksPlaceholderJson().contains("TODO"));
    }
}
