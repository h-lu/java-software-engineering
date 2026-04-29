package com.campusflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppSmokeTest {
    @Test
    void appEntryPointClassIsAvailable() {
        assertEquals("com.campusflow.App", App.class.getName());
    }
}
