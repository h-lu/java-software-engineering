package com.campusflow;

import com.campusflow.config.Config;
import com.campusflow.util.Version;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Week 14 starter package smoke tests")
class Week14StarterSmokeTest {

    private final Path root = Path.of("").toAbsolutePath();

    @Test
    void starterProjectCompilesAndLoadsDevConfig() {
        Config config = Config.loadFor("dev");

        assertEquals("dev", config.env());
        assertEquals("dev", config.getEnv());
        assertEquals(7070, config.port());
        assertEquals(config.port(), config.getPort());
        assertTrue(config.databasePath().contains("campusflow"));
        assertEquals(config.databasePath(), config.getDbPath());
    }

    @Test
    void versionUtilitySupportsSemVerBasics() {
        Version version = Version.parse("1.2.3");

        assertEquals("1.2.3", version.toString());
        assertEquals("2.0.0", version.nextMajor().toString());
        assertEquals("1.3.0", version.nextMinor().toString());
        assertEquals("1.2.4", version.nextPatch().toString());
        assertTrue(Version.parse("1.2.4").compareTo(version) > 0);
    }

    @Test
    void releaseDeliverableTemplatesExist() throws IOException {
        assertReadableContent("README.md", "Week 14");
        assertReadableContent("CHANGELOG.md", "Changelog");
        assertReadableContent("DEPLOYMENT.md", "Deployment");
    }

    private void assertReadableContent(String relativePath, String expectedText) throws IOException {
        Path file = root.resolve(relativePath);

        assertTrue(Files.isRegularFile(file), relativePath + " should exist");
        String text = Files.readString(file);
        assertFalse(text.isBlank(), relativePath + " should not be blank");
        assertTrue(text.contains(expectedText), relativePath + " should contain " + expectedText);
    }
}
