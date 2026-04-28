package com.campusflow;

import com.campusflow.config.Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Week 14 starter package smoke tests")
class Week14StarterSmokeTest {

    private final Path root = Path.of("").toAbsolutePath();

    @Test
    void starterProjectCompilesAndLoadsDevConfig() {
        Config config = Config.loadFor("dev");

        assertEquals("dev", config.env());
        assertTrue(config.port() > 0);
        assertTrue(config.databasePath().contains("campusflow"));
    }

    @Test
    void releaseTemplatesContainStudentTodos() throws IOException {
        assertContainsTodo("README.md");
        assertContainsTodo("CHANGELOG_TODO.md");
        assertContainsTodo("DEPLOYMENT_TODO.md");
        assertContainsTodo("src/main/java/com/campusflow/util/Version.java");
        assertContainsTodo("src/main/java/com/campusflow/config/Config.java");
    }

    private void assertContainsTodo(String relativePath) throws IOException {
        Path file = root.resolve(relativePath);

        assertTrue(Files.isRegularFile(file), relativePath + " should exist");
        String text = Files.readString(file);
        assertTrue(text.contains("待办") || text.contains("待办"), relativePath + " 应该保留学生可填写的 TODO/待办 标记");
    }
}
