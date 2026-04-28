package com.campusflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppSmokeTest {
    @Test
    void healthPayloadShowsStarterIsWired() {
        assertTrue(App.healthJson().contains("CampusFlow"));
    }
}
