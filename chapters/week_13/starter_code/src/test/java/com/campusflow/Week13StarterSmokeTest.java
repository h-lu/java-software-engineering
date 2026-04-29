package com.campusflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Week 13 starter package smoke tests")
class Week13StarterSmokeTest {

    private final Path root = Path.of("").toAbsolutePath();

    @Test
    void starterReadmeExplainsCommandsAndDeliverables() throws IOException {
        String readme = Files.readString(root.resolve("README.md"));

        assertTrue(readme.contains("mvn test"));
        assertTrue(readme.contains("openapi.yaml"));
        assertTrue(readme.contains("PROJECT_README_TODO.md"));
    }

    @Test
    void documentationDeliverablesExistAndHaveContent() throws IOException {
        assertReadableContent("openapi.yaml", "openapi:");
        assertAnyReadableContent(List.of("PROJECT_README_TODO.md", "PROJECT_README.md"), "#");
        assertReadableContent("docs/ADR_INDEX.md", "ADR");
        assertReadableContent("docs/ADR/001-domain-model.md", "ADR");
    }

    private void assertAnyReadableContent(List<String> relativePaths, String expectedText) throws IOException {
        for (String relativePath : relativePaths) {
            Path file = root.resolve(relativePath);
            if (Files.isRegularFile(file)) {
                String text = Files.readString(file);
                assertFalse(text.isBlank(), relativePath + " should not be blank");
                assertTrue(text.contains(expectedText), relativePath + " should contain " + expectedText);
                return;
            }
        }

        throw new AssertionError("One of these files should exist: " + relativePaths);
    }

    private void assertReadableContent(String relativePath, String expectedText) throws IOException {
        Path file = root.resolve(relativePath);

        assertTrue(Files.isRegularFile(file), relativePath + " should exist");
        String text = Files.readString(file);
        assertFalse(text.isBlank(), relativePath + " should not be blank");
        assertTrue(text.contains(expectedText), relativePath + " should contain " + expectedText);
    }
}
