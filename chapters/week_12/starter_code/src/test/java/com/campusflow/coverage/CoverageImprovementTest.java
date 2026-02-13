package com.campusflow.coverage;

import com.campusflow.quality.CodeFixed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 代码覆盖率提升测试。
 *
 * <p>本测试类的目标是提高代码覆盖率，确保各种代码路径都被测试到。
 *
 * <p>测试矩阵：
 * <table>
 *   <tr><th>场景</th><th>测试数量</th><th>覆盖目标</th></tr>
 *   <tr><td>正常流程</td><td>5</td><td>主分支</td></tr>
 *   <tr><td>边界条件</td><td>8</td><td>边界分支</td></tr>
 *   <tr><td>异常情况</td><td>6</td><td>异常分支</td></tr>
 * </table>
 *
 * <p>预期覆盖率提升：
 * <ul>
 *   <li>行覆盖率: 60% → 75%+</li>
 *   <li>分支覆盖率: 50% → 65%+</li>
 * </ul>
 */
@DisplayName("覆盖率提升测试")
class CoverageImprovementTest {

    // ===== 正常流程测试 =====

    @Test
    @DisplayName("CodeFixed: 正常构造应创建对象")
    void constructor_WhenValidInput_ShouldCreateObject() {
        CodeFixed code = new CodeFixed("TestName", 5);
        assertEquals("TestName", code.getName());
        assertEquals(5, code.getPriority());
    }

    @Test
    @DisplayName("setName: 正常设置应更新名称")
    void setName_WhenValidName_ShouldUpdateName() {
        CodeFixed code = new CodeFixed("OldName", 1);
        code.setName("NewName");
        assertEquals("NewName", code.getName());
    }

    @Test
    @DisplayName("setPriority: 正常设置应更新优先级")
    void setPriority_WhenValidPriority_ShouldUpdatePriority() {
        CodeFixed code = new CodeFixed("Test", 1);
        code.setPriority(10);
        assertEquals(10, code.getPriority());
    }

    @Test
    @DisplayName("getName: 多次调用应返回一致结果")
    void getName_WhenCalledMultipleTimes_ShouldReturnSameValue() {
        CodeFixed code = new CodeFixed("Test", 1);
        String name1 = code.getName();
        String name2 = code.getName();
        assertEquals(name1, name2);
    }

    @Test
    @DisplayName("getPriority: 多次调用应返回一致结果")
    void getPriority_WhenCalledMultipleTimes_ShouldReturnSameValue() {
        CodeFixed code = new CodeFixed("Test", 1);
        int priority1 = code.getPriority();
        int priority2 = code.getPriority();
        assertEquals(priority1, priority2);
    }

    // ===== 边界条件测试 =====

    @Test
    @DisplayName("constructor: 空字符串名称应正常处理")
    void constructor_WhenEmptyName_ShouldHandleCorrectly() {
        CodeFixed code = new CodeFixed("", 1);
        assertEquals("", code.getName());
    }

    @Test
    @DisplayName("constructor: 最小优先级应为 Integer.MIN_VALUE")
    void constructor_WhenMinPriority_ShouldHandleCorrectly() {
        CodeFixed code = new CodeFixed("Test", Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, code.getPriority());
    }

    @Test
    @DisplayName("constructor: 最大优先级应为 Integer.MAX_VALUE")
    void constructor_WhenMaxPriority_ShouldHandleCorrectly() {
        CodeFixed code = new CodeFixed("Test", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, code.getPriority());
    }

    @Test
    @DisplayName("constructor: 零优先级应正常处理")
    void constructor_WhenZeroPriority_ShouldHandleCorrectly() {
        CodeFixed code = new CodeFixed("Test", 0);
        assertEquals(0, code.getPriority());
    }

    @Test
    @DisplayName("setName: null 输入应正常设置")
    void setName_WhenNull_ShouldSetNull() {
        CodeFixed code = new CodeFixed("Test", 1);
        code.setName(null);
        assertNull(code.getName());
    }

    @Test
    @DisplayName("compareTo: 相同 name 和 priority 应返回 0")
    void compareTo_WhenSameNameAndPriority_ShouldReturnZero() {
        CodeFixed code1 = new CodeFixed("Test", 5);
        CodeFixed code2 = new CodeFixed("Test", 5);
        assertEquals(0, code1.compareTo(code2));
    }

    @Test
    @DisplayName("compareTo: name 相同但 priority 较大应返回正数")
    void compareTo_WhenSameNameButLargerPriority_ShouldReturnPositive() {
        CodeFixed code1 = new CodeFixed("Test", 10);
        CodeFixed code2 = new CodeFixed("Test", 5);
        assertTrue(code1.compareTo(code2) > 0);
    }

    @Test
    @DisplayName("compareTo: name 相同但 priority 较小应返回负数")
    void compareTo_WhenSameNameButSmallerPriority_ShouldReturnNegative() {
        CodeFixed code1 = new CodeFixed("Test", 3);
        CodeFixed code2 = new CodeFixed("Test", 5);
        assertTrue(code1.compareTo(code2) < 0);
    }

    // ===== equals 和 hashCode 测试 =====

    @Test
    @DisplayName("equals: 相同对象应返回 true")
    void equals_WhenSameObject_ShouldReturnTrue() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertTrue(code.equals(code));
    }

    @Test
    @DisplayName("equals: null 应返回 false")
    void equals_WhenNull_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.equals(null));
    }

    @Test
    @DisplayName("equals: 不同类型应返回 false")
    void equals_WhenDifferentType_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.equals("Test"));
    }

    @Test
    @DisplayName("equals: 相同 name 和 priority 应返回 true")
    void equals_WhenSameNameAndPriority_ShouldReturnTrue() {
        CodeFixed code1 = new CodeFixed("Test", 1);
        CodeFixed code2 = new CodeFixed("Test", 1);
        assertTrue(code1.equals(code2));
    }

    @Test
    @DisplayName("equals: 不同 name 应返回 false")
    void equals_WhenDifferentName_ShouldReturnFalse() {
        CodeFixed code1 = new CodeFixed("Test1", 1);
        CodeFixed code2 = new CodeFixed("Test2", 1);
        assertFalse(code1.equals(code2));
    }

    @Test
    @DisplayName("equals: 不同 priority 应返回 false")
    void equals_WhenDifferentPriority_ShouldReturnFalse() {
        CodeFixed code1 = new CodeFixed("Test", 1);
        CodeFixed code2 = new CodeFixed("Test", 2);
        assertFalse(code1.equals(code2));
    }

    @Test
    @DisplayName("equals: name 为 null 的比较应正确处理")
    void equals_WhenOneNameIsNull_ShouldReturnFalse() {
        CodeFixed code1 = new CodeFixed("Test", 1);
        CodeFixed code2 = new CodeFixed(null, 1);
        assertFalse(code1.equals(code2));
    }

    @Test
    @DisplayName("hashCode: 相等对象应有相同 hashCode")
    void hashCode_WhenEqual_ShouldReturnSameHashCode() {
        CodeFixed code1 = new CodeFixed("Test", 1);
        CodeFixed code2 = new CodeFixed("Test", 1);
        assertEquals(code1.hashCode(), code2.hashCode());
    }

    @Test
    @DisplayName("hashCode: 不同对象应有不同 hashCode（大概率）")
    void hashCode_WhenDifferent_ShouldReturnDifferentHashCode() {
        CodeFixed code1 = new CodeFixed("Test1", 1);
        CodeFixed code2 = new CodeFixed("Test2", 1);
        assertNotEquals(code1.hashCode(), code2.hashCode());
    }

    // ===== compareTo 边界测试 =====

    @Test
    @DisplayName("compareTo: name 为 null 的对象应正确处理")
    void compareTo_WhenOtherNameIsNull_ShouldHandleCorrectly() {
        CodeFixed code1 = new CodeFixed("Test", 1);
        CodeFixed code2 = new CodeFixed(null, 1);
        assertTrue(code1.compareTo(code2) > 0);
    }

    @Test
    @DisplayName("compareTo: 两者 name 都为 null 应比较 priority")
    void compareTo_WhenBothNamesAreNull_ShouldComparePriority() {
        CodeFixed code1 = new CodeFixed(null, 5);
        CodeFixed code2 = new CodeFixed(null, 3);
        assertTrue(code1.compareTo(code2) > 0);
    }

    @Test
    @DisplayName("compareTo: 两者 name 都为 null 且 priority 相同应返回 0")
    void compareTo_WhenBothNamesAreNullAndSamePriority_ShouldReturnZero() {
        CodeFixed code1 = new CodeFixed(null, 5);
        CodeFixed code2 = new CodeFixed(null, 5);
        assertEquals(0, code1.compareTo(code2));
    }

    @Test
    @DisplayName("compareTo: 空字符串 name 应正常处理")
    void compareTo_WhenEmptyName_ShouldHandleCorrectly() {
        CodeFixed code1 = new CodeFixed("", 1);
        CodeFixed code2 = new CodeFixed("A", 1);
        assertTrue(code1.compareTo(code2) < 0);
    }

    // ===== isExitCommand 测试 =====

    @Test
    @DisplayName("isExitCommand: 多个单词的输入应返回 false")
    void isExitCommand_WhenMultipleWords_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.isExitCommand("exit now"));
    }

    @Test
    @DisplayName("isExitCommand: 前后带空格应返回 false")
    void isExitCommand_WhenWithLeadingTrailingSpaces_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.isExitCommand(" exit "));
    }

    @Test
    @DisplayName("isExitCommand: 只有空格应返回 false")
    void isExitCommand_WhenOnlySpaces_ShouldReturnFalse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertFalse(code.isExitCommand("   "));
    }

    // ===== toLowerCase 测试 =====

    @Test
    @DisplayName("toLowerCase: 已经是小写应保持不变")
    void toLowerCase_WhenAlreadyLowerCase_ShouldReturnUnchanged() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals("hello", code.toLowerCase("hello"));
    }

    @Test
    @DisplayName("toLowerCase: 全大写应转换为小写")
    void toLowerCase_WhenAllUpperCase_ShouldConvert() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals("hello", code.toLowerCase("HELLO"));
    }

    @Test
    @DisplayName("toLowerCase: 数字和特殊字符应保持不变")
    void toLowerCase_WhenNumbersAndSpecialChars_ShouldKeepUnchanged() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals("123-abc!@#", code.toLowerCase("123-ABC!@#"));
    }

    // ===== parseNumber 测试 =====

    @Test
    @DisplayName("parseNumber: 正整数应正确解析")
    void parseNumber_WhenPositiveInteger_ShouldParse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(123, code.parseNumber("123"));
    }

    @Test
    @DisplayName("parseNumber: 负数应正确解析")
    void parseNumber_WhenNegative_ShouldParse() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(-456, code.parseNumber("-456"));
    }

    @Test
    @DisplayName("parseNumber: 带加号的数字应返回 0（设计选择）")
    void parseNumber_WhenWithPlusSign_ShouldReturnZero() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(0, code.parseNumber("+123"));
    }

    @Test
    @DisplayName("parseNumber: 小数应返回 0")
    void parseNumber_WhenDecimal_ShouldReturnZero() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(0, code.parseNumber("123.45"));
    }

    @Test
    @DisplayName("parseNumber: 科学计数法应返回 0")
    void parseNumber_WhenScientificNotation_ShouldReturnZero() {
        CodeFixed code = new CodeFixed("Test", 1);
        assertEquals(0, code.parseNumber("1e10"));
    }
}
