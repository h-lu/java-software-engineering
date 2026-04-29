package com.campusflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ShowcaseReadinessCheck {

    private static final List<String> REQUIRED_FILES = List.of(
            "presentation_script.md",
            "slides_outline.md",
            "poster_checklist.md",
            "qa_prep.md"
    );

    private static final List<String> OPTIONAL_FILES = List.of(
            "showcase_practice.md",
            "ai_ppt_review.md"
    );

    public static void main(String[] args) throws IOException {
        Path root = Path.of("").toAbsolutePath();

        System.out.println("Week 15 showcase readiness check");
        for (String fileName : REQUIRED_FILES) {
            Path file = root.resolve(fileName);
            long todoCount = Files.exists(file)
                    ? Files.readString(file).lines().filter(line -> line.contains("待办")).count()
                    : -1;
            System.out.printf("%s: %s%n", fileName, todoCount >= 0 ? "待办数量=" + todoCount : "missing");
        }
        System.out.println("Optional practice files: " + OPTIONAL_FILES);
    }

    public static List<String> requiredFiles() {
        return REQUIRED_FILES;
    }

    public static List<String> optionalFiles() {
        return OPTIONAL_FILES;
    }
}
