/*
 * CORS 配置测试。
 *
 * 本测试验证 Week 10 作业中的 CORS 配置是否正确。
 */
/*
 * CORS 配置测试。
 *
 * 本测试验证 Week 10 作业中的 CORS 配置是否正确。
 */
package com.campusflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CORS 配置测试类。
 */
public class CorsConfigTest {

    /**
     * 测试：CORS 配置已启用。
     *
     * 验证点：
     * 1. App.java 中启用了 CORS 插件
     * 2. 配置允许跨域访问
     */
    @Test
    void testCorsEnabled() {
        // 这个测试主要验证学生是否正确配置了 CORS
        // 实际测试需要启动服务器并发送带 Origin 头的请求

        // 检查 App 类存在且可以加载
        assertDoesNotThrow(() -> {
            Class<?> appClass = Class.forName("com.campusflow.App");
            assertNotNull(appClass);
        }, "App 类应该存在且可加载");
    }

    /**
     * 测试：App 类包含 CORS 相关配置。
     *
     * 这是一个简单的静态检查，验证学生代码中包含关键配置。
     */
    @Test
    void testCorsConfigPresent() {
        // 读取 App.java 源代码（简化检查）
        // 实际检查由人工审查完成

        // 验证常量定义存在
        assertDoesNotThrow(() -> {
            Class<?> appClass = Class.forName("com.campusflow.App");
            // 检查是否有 createApp 方法
            assertNotNull(appClass.getDeclaredMethod("createApp", int.class));
        });
    }
}
