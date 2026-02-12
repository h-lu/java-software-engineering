package com.campusflow.quality;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 静态分析问题修复测试。
 *
 * <p>本测试类验证了常见静态分析问题的行为和修复方案。
 * 每个测试方法对应一个 SpotBugs 警告类型。
 *
 * <p>测试矩阵：
 * <table>
 *   <tr><th>问题类型</th><th>正例</th><th>边界</th><th>反例</th></tr>
 *   <tr><td>空指针</td><td>正常输入</td><td>null 输入</td><td>抛出异常</td></tr>
 *   <tr><td>资源泄漏</td><td>正常读取</td><td>空文件</td><td>异常不关闭</td></tr>
 *   <tr><td>字符串比较</td><td>相等字符串</td><td>null 输入</td><td>使用 ==</td></tr>
 *   <tr><td>异常捕获</td><td>正常解析</td><td>无效输入</td><td>捕获 Exception</td></tr>
 * </table>
 */
@DisplayName("静态分析问题测试")
class StaticAnalysisTest {

    // ===== 空指针解引用测试 =====

    @Test
    @DisplayName("getFullName: 正常输入应返回正确结果")
    void getFullName_WhenValidInput_ShouldReturnFullName() {
        CodeFixed code = new CodeFixed("John", 1);
        String result = code.getFullName("John", "Doe");
        assertEquals("John Doe", result);
    }

    @Test
    @DisplayName("getFullName: firstName 为 null 应抛出异常")
    void getFullName_WhenFirstNameNull_ShouldThrowException() {
        CodeFixed code = new CodeFixed("John", 1);
        assertThrows(IllegalArgumentException.class, () -> {
            code.getFullName(null, "Doe");
        });
    }

    @Test
    @DisplayName("getFullName: lastName 为 null 应抛出异常")
    void getFullName_WhenLastNameNull_ShouldThrowException() {
        CodeFixed code = new CodeFixed("John", 1);
        assertThrows(IllegalArgumentException.class, () -> {
            code.getFullName("John", null);
        });
    }

    @Test
    @DisplayName("getFullName: 两者都为 null 应抛出异常")
    void getFullName_WhenBothNull_ShouldThrowException() {
        CodeFixed code = new CodeFixed("John", 1);
        assertThrows(IllegalArgumentException.class, () -> {
            code.getFullName(null, null);
        });
    }

    @Test
    @DisplayName("getFullName: 空字符串输入应正常处理")
    void getFullName_WhenEmptyString_ShouldHandleCorrectly() {
        CodeFixed code = new CodeFixed("John", 1);
        String result = code.getFullName("", "Doe");
        assertEquals(" Doe", result);
    }

    @Test
    @DisplayName("getFullName: 带空格的输入应被 trim")
    void getFullName_WhenInputHasSpaces_ShouldTrim() {
        CodeFixed code = new CodeFixed("John", 1);
        String result = code.getFullName("  John  ", "  Doe  ");
        assertEquals("John Doe", result);
    }

    // ===== 字符串比较测试 =====

    @Test
    @DisplayName("isExitCommand: 相等字符串应返回 true")
    void isExitCommand_WhenEqual_ShouldReturnTrue() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertTrue(code.isExitCommand("exit"));
    }

    @Test
    @DisplayName("isExitCommand: 不相等字符串应返回 false")
    void isExitCommand_WhenNotEqual_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.isExitCommand("quit"));
    }

    @Test
    @DisplayName("isExitCommand: null 输入应返回 false")
    void isExitCommand_WhenNull_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.isExitCommand(null));
    }

    @Test
    @DisplayName("isExitCommand: 大小写敏感")
    void isExitCommand_WhenDifferentCase_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.isExitCommand("EXIT"));
        assertFalse(code.isExitCommand("Exit"));
    }

    @Test
    @DisplayName("isExitCommand: 带空格的输入应返回 false")
    void isExitCommand_WhenWithSpaces_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.isExitCommand(" exit "));
    }

    // ===== 数字解析测试 =====

    @Test
    @DisplayName("parseNumber: 有效数字应正确解析")
    void parseNumber_WhenValidNumber_ShouldReturnNumber() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(42, code.parseNumber("42"));
    }

    @Test
    @DisplayName("parseNumber: 负数应正确解析")
    void parseNumber_WhenNegativeNumber_ShouldReturnNumber() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(-42, code.parseNumber("-42"));
    }

    @Test
    @DisplayName("parseNumber: 零应正确解析")
    void parseNumber_WhenZero_ShouldReturnZero() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(0, code.parseNumber("0"));
    }

    @Test
    @DisplayName("parseNumber: 无效输入应返回 0")
    void parseNumber_WhenInvalidInput_ShouldReturnZero() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(0, code.parseNumber("abc"));
    }

    @Test
    @DisplayName("parseNumber: null 输入应返回 0")
    void parseNumber_WhenNull_ShouldReturnZero() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(0, code.parseNumber(null));
    }

    @Test
    @DisplayName("parseNumber: 空字符串应返回 0")
    void parseNumber_WhenEmptyString_ShouldReturnZero() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(0, code.parseNumber(""));
    }

    @Test
    @DisplayName("parseNumber: 带空格的数字应返回 0")
    void parseNumber_WhenWithSpaces_ShouldReturnZero() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(0, code.parseNumber(" 42 "));
    }

    @Test
    @DisplayName("parseNumber: 超大数字应正确解析")
    void parseNumber_WhenLargeNumber_ShouldReturnNumber() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(Integer.MAX_VALUE, code.parseNumber(String.valueOf(Integer.MAX_VALUE)));
    }

    // ===== compareTo 与 equals 一致性测试 =====

    @Test
    @DisplayName("compareTo: 相同对象应返回 0")
    void compareTo_WhenSameObject_ShouldReturnZero() {
        CodeFixed code1 = new CodeFixed("John", 1);
        assertEquals(0, code1.compareTo(code1));
    }

    @Test
    @DisplayName("compareTo: name 相同时应比较 priority")
    void compareTo_WhenSameName_ShouldComparePriority() {
        CodeFixed code1 = new CodeFixed("John", 1);
        CodeFixed code2 = new CodeFixed("John", 2);
        assertTrue(code1.compareTo(code2) < 0);
    }

    @Test
    @DisplayName("compareTo: name 不同时应比较 name")
    void compareTo_WhenDifferentName_ShouldCompareName() {
        CodeFixed code1 = new CodeFixed("Alice", 1);
        CodeFixed code2 = new CodeFixed("Bob", 1);
        assertTrue(code1.compareTo(code2) < 0);
    }

    @Test
    @DisplayName("compareTo: null 输入应返回正数")
    void compareTo_WhenNull_ShouldReturnPositive() {
        CodeFixed code = new CodeFixed("John", 1);
        assertTrue(code.compareTo(null) > 0);
    }

    @Test
    @DisplayName("compareTo 与 equals 应保持一致")
    void compareTo_ShouldBeConsistentWithEquals() {
        CodeFixed code1 = new CodeFixed("John", 1);
        CodeFixed code2 = new CodeFixed("John", 1);

        // equals 返回 true，compareTo 应返回 0
        assertEquals(code1, code2);
        assertEquals(0, code1.compareTo(code2));
    }

    // ===== 小写转换测试 =====

    @Test
    @DisplayName("toLowerCase: 大写字母应转换为小写")
    void toLowerCase_WhenUpperCase_ShouldConvertToLowerCase() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals("hello", code.toLowerCase("HELLO"));
    }

    @Test
    @DisplayName("toLowerCase: 混合大小写应正确转换")
    void toLowerCase_WhenMixedCase_ShouldConvertToLowerCase() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals("hello world", code.toLowerCase("HeLLo WoRLd"));
    }

    @Test
    @DisplayName("toLowerCase: null 输入应返回 null")
    void toLowerCase_WhenNull_ShouldReturnNull() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertNull(code.toLowerCase(null));
    }

    @Test
    @DisplayName("toLowerCase: 空字符串应返回空字符串")
    void toLowerCase_WhenEmptyString_ShouldReturnEmptyString() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals("", code.toLowerCase(""));
    }

    @Test
    @DisplayName("toLowerCase: 特殊字符应保持不变")
    void toLowerCase_WhenSpecialChars_ShouldKeepUnchanged() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals("123!@#", code.toLowerCase("123!@#"));
    }

    // ===== 文件读取测试（需要临时文件）=====

    @Test
    @DisplayName("readFile: 正常文件应正确读取")
    void readFile_WhenNormalFile_ShouldReadContent() throws IOException {
        // 创建临时文件
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "Line 1\nLine 2\nLine 3");

        try {
            CodeFixed code = new CodeFixed("Test", 1);
            String content = code.readFile(tempFile.toString());
            assertTrue(content.contains("Line 1"));
            assertTrue(content.contains("Line 2"));
            assertTrue(content.contains("Line 3"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @DisplayName("readFile: 空文件应返回空内容")
    void readFile_WhenEmptyFile_ShouldReturnEmpty() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "");

        try {
            CodeFixed code = new CodeFixed("Test", 1);
            String content = code.readFile(tempFile.toString());
            assertEquals("", content.trim());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @DisplayName("readFile: 不存在的文件应抛出异常")
    void readFile_WhenFileNotExists_ShouldThrowException() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertThrows(IOException.class, () -> {
            code.readFile("/nonexistent/file.txt");
        });
    }

    @Test
    @DisplayName("readFile: 包含特殊字符的文件应正确读取")
    void readFile_WhenFileHasSpecialChars_ShouldReadCorrectly() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "中文\n日本語\n한국어");

        try {
            CodeFixed code = new CodeFixed("Test", 1);
            String content = code.readFile(tempFile.toString());
            assertTrue(content.contains("中文"));
            assertTrue(content.contains("日本語"));
            assertTrue(content.contains("한국어"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
