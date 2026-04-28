package com.campusflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Week16ReadinessCheck {

    private static final List<String> REQUIRED_FILES = List.of(
            "01_showcase_feedback_summary.md",
            "02_retrospective_report.md",
            "03_knowledge_map.md",
            "04_career_plan.md",
            "submission_checklist.md"
    );

    public static void main(String[] args) throws IOException {
        Path root = Path.of("").toAbsolutePath();

        System.out.println("Week 16 retrospective readiness check");
        for (String fileName : REQUIRED_FILES) {
            Path file = root.resolve(fileName);
            long todoCount = Files.exists(file)
                    ? Files.readString(file).lines().filter(line -> line.contains("待办")).count()
                    : -1;
            System.out.printf("%s: %s%n", fileName, todoCount >= 0 ? "待办数量=" + todoCount : "missing");
        }
    }

    public static List<String> requiredFiles() {
        return REQUIRED_FILES;
    }
}
