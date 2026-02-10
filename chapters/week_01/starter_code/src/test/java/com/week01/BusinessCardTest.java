package com.week01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 名片生成器的单元测试。
 *
 * <p>这些测试验证程序的基本功能。
 * 注意：Week 01 的重点是理解 Java 基础，测试是可选的。
 * 更完整的测试策略会在 Week 06 讲解。
 */
class BusinessCardTest {

    @Test
    void testStringLength() {
        // 基础测试：验证字符串操作
        String name = "张三";
        assertEquals(2, name.length());
    }

    @Test
    void testIntegerParsing() {
        // 测试整数转换
        String ageStr = "25";
        int age = Integer.parseInt(ageStr);
        assertEquals(25, age);
    }

    @Test
    void testDoubleParsing() {
        // 测试浮点数转换
        String expStr = "5.5";
        double exp = Double.parseDouble(expStr);
        assertEquals(5.5, exp, 0.01);
    }

    @Test
    void testStringConcatenation() {
        // 测试字符串拼接
        String name = "李四";
        String title = name + " 的个人名片";
        assertEquals("李四 的个人名片", title);
    }

    @Test
    void testEmailContains() {
        // 测试邮箱验证（简单版）
        String email = "test@example.com";
        assertTrue(email.contains("@"));
    }
}
