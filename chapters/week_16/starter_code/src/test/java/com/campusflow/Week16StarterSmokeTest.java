package com.campusflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Week 16 starter package smoke tests")
class Week16StarterSmokeTest {

    private final Path root = Path.of("").toAbsolutePath();

    @Test
    void requiredRetrospectiveTemplatesExist() throws IOException {
        for (String fileName : Week16ReadinessCheck.requiredFiles()) {
            Path file = root.resolve(fileName);
            assertTrue(Files.isRegularFile(file), fileName + " should exist");
            assertTrue(Files.readString(file).contains("待办"), fileName + " should contain 学生待办项");
        }
    }

    @Test
    void checkerHasSubmissionChecklistConfigured() {
        assertFalse(Week16ReadinessCheck.requiredFiles().isEmpty());
        assertTrue(Week16ReadinessCheck.requiredFiles().contains("submission_checklist.md"));
    }
}
