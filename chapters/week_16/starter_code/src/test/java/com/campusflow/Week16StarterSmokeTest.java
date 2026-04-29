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
            String text = Files.readString(file);
            assertFalse(text.isBlank(), fileName + " should not be blank");
            assertTrue(text.lines().count() >= 3, fileName + " should keep the starter structure");
        }
    }

    @Test
    void checkerHasSubmissionChecklistConfigured() {
        assertFalse(Week16ReadinessCheck.requiredFiles().isEmpty());
        assertTrue(Week16ReadinessCheck.requiredFiles().contains("submission_checklist.md"));
    }
}
