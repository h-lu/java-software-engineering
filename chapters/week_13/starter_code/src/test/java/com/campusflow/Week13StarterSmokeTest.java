package com.campusflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Week 13 starter package smoke tests")
class Week13StarterSmokeTest {

    private final Path root = Path.of("").toAbsolutePath();

    @Test
    void starterReadmeExplainsCommandsAndTodoFiles() throws IOException {
        String readme = Files.readString(root.resolve("README.md"));

        assertTrue(readme.contains("mvn test"));
        assertTrue(readme.contains("openapi.yaml"));
        assertTrue(readme.contains("PROJECT_README_TODO.md"));
    }

    @Test
    void documentationTemplatesExistAndStillContainStudentTodos() throws IOException {
        assertContainsTodo("openapi.yaml");
        assertContainsTodo("PROJECT_README_TODO.md");
        assertContainsTodo("docs/ADR_INDEX.md");
        assertContainsTodo("docs/ADR/001-domain-model.md");
    }

    private void assertContainsTodo(String relativePath) throws IOException {
        Path file = root.resolve(relativePath);

        assertTrue(Files.isRegularFile(file), relativePath + " should exist");
        String text = Files.readString(file);
        assertTrue(text.contains("待办") || text.contains("待办"), relativePath + " 应该保留学生可填写的 TODO/待办 标记");
    }
}
