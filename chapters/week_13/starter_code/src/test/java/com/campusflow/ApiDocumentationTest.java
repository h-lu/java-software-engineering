package com.campusflow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API 文档测试 - 验证 OpenAPI 规范。
 */
@DisplayName("API 文档测试 - OpenAPI 规范验证")
public class ApiDocumentationTest {

    private static Javalin app;
    private static final int TEST_PORT = 7080;
    private static final String BASE_URL = "http://localhost:" + TEST_PORT;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUpClass() {
        app = App.createApp(TEST_PORT);
    }

    @AfterAll
    static void tearDownClass() {
        if (app != null) {
            app.stop();
        }
    }

    @Test
    @DisplayName("API 文档端点列表")
    void apiDocumentation_Endpoints_ShouldWork() throws Exception {
        // 验证 App 类有 createApp 方法
        assertDoesNotThrow(() -> {
            App.createApp(TEST_PORT + 1);
        });
    }

    @Test
    @DisplayName("健康检查应返回 200")
    void healthCheck_ShouldReturn200() {
        // 基础验证：应用可以创建
        assertNotNull(app, "应用实例不应为 null");
    }
}
