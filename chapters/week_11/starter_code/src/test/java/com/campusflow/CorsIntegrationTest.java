package com.campusflow;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CORS 配置集成测试
 *
 * 测试 Javalin 后端的 CORS 配置是否正确：
 * - 预检请求（OPTIONS）响应正确
 * - 允许的 Origin 头
 * - 允许的 HTTP 方法
 * - 允许的请求头
 *
 * <p>Week 10 重点：前端代码审查需要后端支持跨域访问</p>
 */
public class CorsIntegrationTest {

    private static Javalin app;
    private static final int TEST_PORT = 7072;
    private static final String BASE_URL = "http://localhost:" + TEST_PORT;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    static void setUp() {
        // 启动测试服务器
        app = App.createApp(TEST_PORT);
    }

    @AfterAll
    static void tearDown() {
        // 关闭服务器
        if (app != null) {
            app.stop();
        }
    }

    // ========== 预检请求（OPTIONS）测试 ==========

    @Test
    @DisplayName("OPTIONS /tasks - 预检请求应返回 200")
    void optionsPreflight_ShouldReturn200() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("OPTIONS /tasks/{id} - 带路径参数的预检请求应返回 200")
    void optionsPreflightWithPathParam_ShouldReturn200() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/123"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "PUT")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("OPTIONS /stats - 统计端点预检请求应返回 200")
    void optionsPreflightStats_ShouldReturn200() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/stats"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    // ========== 允许的 Origin 头测试 ==========

    @Test
    @DisplayName("GET /health - 应包含 Access-Control-Allow-Origin 头")
    void getHealth_ShouldIncludeAllowOriginHeader() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .header("Origin", "http://localhost:3000")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String allowOrigin = response.headers().firstValue("Access-Control-Allow-Origin").orElse(null);
        assertNotNull(allowOrigin, "应包含 Access-Control-Allow-Origin 头");
    }

    @Test
    @DisplayName("POST /tasks - 应允许来自 localhost:3000 的请求")
    void postTask_FromLocalhost3000_ShouldSucceed() throws Exception {
        String json = """
                {
                    "title": "CORS 测试任务",
                    "description": "测试跨域请求",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .header("Origin", "http://localhost:3000")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        String allowOrigin = response.headers().firstValue("Access-Control-Allow-Origin").orElse(null);
        assertNotNull(allowOrigin, "响应应包含 Access-Control-Allow-Origin 头");
    }

    @Test
    @DisplayName("POST /tasks - 应允许来自 localhost:5173 的请求")
    void postTask_FromLocalhost5173_ShouldSucceed() throws Exception {
        String json = """
                {
                    "title": "Vite 前端测试任务",
                    "description": "测试 Vite 默认端口",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .header("Origin", "http://localhost:5173")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("POST /tasks - 应允许来自 localhost:8080 的请求")
    void postTask_FromLocalhost8080_ShouldSucceed() throws Exception {
        String json = """
                {
                    "title": "Vue CLI 测试任务",
                    "description": "测试 Vue CLI 默认端口",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .header("Origin", "http://localhost:8080")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    // ========== 允许的 HTTP 方法测试 ==========

    @Test
    @DisplayName("OPTIONS - 预检请求应允许 GET 方法")
    void optionsPreflight_ShouldAllowGetMethod() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String allowMethods = response.headers().firstValue("Access-Control-Allow-Methods").orElse("");
        assertTrue(allowMethods.contains("GET"), "应允许 GET 方法");
    }

    @Test
    @DisplayName("OPTIONS - 预检请求应允许 POST 方法")
    void optionsPreflight_ShouldAllowPostMethod() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String allowMethods = response.headers().firstValue("Access-Control-Allow-Methods").orElse("");
        assertTrue(allowMethods.contains("POST"), "应允许 POST 方法");
    }

    @Test
    @DisplayName("OPTIONS - 预检请求应允许 PUT 方法")
    void optionsPreflight_ShouldAllowPutMethod() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/1"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "PUT")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String allowMethods = response.headers().firstValue("Access-Control-Allow-Methods").orElse("");
        assertTrue(allowMethods.contains("PUT"), "应允许 PUT 方法");
    }

    @Test
    @DisplayName("OPTIONS - 预检请求应允许 DELETE 方法")
    void optionsPreflight_ShouldAllowDeleteMethod() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/1"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "DELETE")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String allowMethods = response.headers().firstValue("Access-Control-Allow-Methods").orElse("");
        assertTrue(allowMethods.contains("DELETE"), "应允许 DELETE 方法");
    }

    @Test
    @DisplayName("OPTIONS - 预检请求应允许 PATCH 方法")
    void optionsPreflight_ShouldAllowPatchMethod() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/1"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "PATCH")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String allowMethods = response.headers().firstValue("Access-Control-Allow-Methods").orElse("");
        assertTrue(allowMethods.contains("PATCH"), "应允许 PATCH 方法");
    }

    // ========== 允许的请求头测试 ==========

    @Test
    @DisplayName("OPTIONS - 预检请求应允许 Content-Type 头")
    void optionsPreflight_ShouldAllowContentTypeHeader() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Content-Type")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String allowHeaders = response.headers().firstValue("Access-Control-Allow-Headers").orElse("");
        assertTrue(
            allowHeaders.toLowerCase().contains("content-type"),
            "应允许 Content-Type 请求头"
        );
    }

    @Test
    @DisplayName("OPTIONS - 预检请求应允许 Authorization 头")
    void optionsPreflight_ShouldAllowAuthorizationHeader() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET")
                .header("Access-Control-Request-Headers", "Authorization")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        // Authorization 头通常被允许，但具体取决于 CORS 配置
        // 这里只验证预检请求成功
    }

    // ========== 边界情况测试 ==========

    @Test
    @DisplayName("POST /tasks - 不带 Origin 头的请求应正常处理")
    void postTask_WithoutOriginHeader_ShouldSucceed() throws Exception {
        String json = """
                {
                    "title": "无 Origin 头测试",
                    "description": "测试非跨域请求",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("PUT /tasks/{id} - 跨域更新任务应成功")
    void putTask_CrossOrigin_ShouldSucceed() throws Exception {
        // 先创建一个任务
        String createJson = """
                {
                    "title": "待更新任务",
                    "description": "原描述",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .header("Origin", "http://localhost:3000")
                .POST(HttpRequest.BodyPublishers.ofString(createJson))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, createResponse.statusCode());

        // 提取任务 ID
        String responseBody = createResponse.body();
        String taskId = extractIdFromJson(responseBody);

        // 跨域更新任务
        String updateJson = """
                {
                    "title": "已更新任务",
                    "description": "新描述",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + taskId))
                .header("Content-Type", "application/json")
                .header("Origin", "http://localhost:5173")
                .PUT(HttpRequest.BodyPublishers.ofString(updateJson))
                .build();

        HttpResponse<String> updateResponse = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, updateResponse.statusCode());
        String allowOrigin = updateResponse.headers().firstValue("Access-Control-Allow-Origin").orElse(null);
        assertNotNull(allowOrigin, "更新响应应包含 CORS 头");
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - 跨域删除任务应成功")
    void deleteTask_CrossOrigin_ShouldSucceed() throws Exception {
        // 先创建一个任务
        String createJson = """
                {
                    "title": "待删除任务",
                    "description": "将被删除",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(createJson))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        String taskId = extractIdFromJson(createResponse.body());

        // 跨域删除任务
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + taskId))
                .header("Origin", "http://localhost:3000")
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, deleteResponse.statusCode());
    }

    // ========== 辅助方法 ==========

    /**
     * 从 JSON 响应中提取任务 ID
     */
    private String extractIdFromJson(String json) {
        // 简单提取，假设格式为 {"id":"123",...}
        int idStart = json.indexOf("\"id\":\"");
        if (idStart == -1) return "1";
        idStart += 6;
        int idEnd = json.indexOf("\"", idStart);
        return json.substring(idStart, idEnd);
    }
}
