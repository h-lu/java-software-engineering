/*
 * AppTest - 基线测试
 *
 * 这是 starter_code 自带的基线测试，用于验证项目能正常运行。
 * 学生可以在此基础上添加更多测试。
 */
package com.campusflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 基线测试 - 验证项目能正常运行
 */
public class AppTest {

    @Test
    void app_ShouldHaveCreateAppMethod() {
        // 验证 App 类有 createApp 方法
        assertDoesNotThrow(() -> {
            App.createApp(7090);
        });
    }

    @Test
    void basicAssertion_ShouldWork() {
        // 基础断言测试
        assertEquals(1, 1);
        assertTrue(true);
        assertFalse(false);
        assertNotNull("not null");
    }
}
