package com.campusflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;

/**
 * 文档质量测试 - 验证项目文档的完整性。
 */
@DisplayName("文档质量测试 - README 和 ADR 验证")
public class DocumentationQualityTest {

    private static final String PROJECT_ROOT = Paths.get("").toAbsolutePath().toString();

    @Test
    @DisplayName("README.md 文件应该存在")
    void readme_ShouldExist() {
        Path readmePath = Paths.get(PROJECT_ROOT, "README.md");
        assertTrue(Files.exists(readmePath), "项目根目录应包含 README.md 文件");
    }

    @Test
    @DisplayName("README 应包含项目标题")
    void readme_ShouldContainProjectTitle() throws IOException {
        Path readmePath = Paths.get(PROJECT_ROOT, "README.md");
        Assumptions.assumeTrue(Files.exists(readmePath), "README.md 应存在");

        String content = Files.readString(readmePath);
        assertTrue(content.contains("CampusFlow") || content.contains("校园"),
                "README 应包含项目标题");
    }

    @Test
    @DisplayName("README 应包含快速开始章节")
    void readme_ShouldContainQuickStartSection() throws IOException {
        Path readmePath = Paths.get(PROJECT_ROOT, "README.md");
        Assumptions.assumeTrue(Files.exists(readmePath), "README.md 应存在");

        String content = Files.readString(readmePath).toLowerCase();
        boolean hasQuickStart = content.contains("快速开始") ||
                               content.contains("quick start");

        assertTrue(hasQuickStart, "README 应包含快速开始章节");
    }

    @Test
    @DisplayName("docs/ADR 目录应该存在")
    void adrDirectory_ShouldExist() {
        Path adrDir = Paths.get(PROJECT_ROOT, "docs", "ADR");
        assertTrue(Files.exists(adrDir), "应存在 docs/ADR 目录");
    }

    @Test
    @DisplayName("ADR 目录应包含至少一个 ADR 文件")
    void adrDirectory_ShouldContainADRFiles() throws IOException {
        Path adrDir = Paths.get(PROJECT_ROOT, "docs", "ADR");
        Assumptions.assumeTrue(Files.exists(adrDir), "ADR 目录应存在");

        List<Path> files = Files.list(adrDir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".md"))
                .toList();

        assertTrue(files.size() >= 1, "ADR 目录应包含至少一个 ADR 文件");
    }
}
