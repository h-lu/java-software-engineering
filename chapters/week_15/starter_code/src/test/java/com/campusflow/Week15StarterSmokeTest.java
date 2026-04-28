package com.campusflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Week 15 starter package smoke tests")
class Week15StarterSmokeTest {

    private final Path root = Path.of("").toAbsolutePath();

    @Test
    void requiredPresentationTemplatesExist() throws IOException {
        for (String fileName : ShowcaseReadinessCheck.requiredFiles()) {
            Path file = root.resolve(fileName);
            assertTrue(Files.isRegularFile(file), fileName + " should exist");
            assertTrue(Files.readString(file).contains("TODO"), fileName + " should contain student TODOs");
        }
    }

    @Test
    void checkerHasRequiredFilesConfigured() {
        assertFalse(ShowcaseReadinessCheck.requiredFiles().isEmpty());
        assertTrue(ShowcaseReadinessCheck.requiredFiles().contains("qa_prep.md"));
    }
}
