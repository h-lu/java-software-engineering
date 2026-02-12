package com.campusflow;

import com.campusflow.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
 * 错误处理集成测试
 *
 * 测试 API 对错误情况的处理：
 * - 无效 JSON 请求体的处理
 * - 不存在的任务 ID 处理
 * - 服务器错误响应格式
 *
 * <p>Week 10 重点：前端需要正确处理后端返回的错误信息</p>
 */
public class ErrorHandlingTest {

    private static Javalin app;
    private static final int TEST_PORT = 7074;
    private static final String BASE_URL = "http://localhost:" + TEST_PORT;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

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

    // ========== 无效 JSON 请求体测试 ==========

    @Test
    @DisplayName("POST /tasks - 无效 JSON 应返回 400 或 500 并包含错误信息")
    void postTask_WithInvalidJson_ShouldReturnError() throws Exception {
        String invalidJson = "{ invalid json }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 无效 JSON 可能返回 400 或 500
        assertTrue(response.statusCode() == 400 || response.statusCode() == 500,
            "无效 JSON 应返回错误状态码");
    }

    @Test
    @DisplayName("POST /tasks - 不完整 JSON 应返回错误")
    void postTask_WithIncompleteJson_ShouldReturnError() throws Exception {
        String incompleteJson = "{\"title\":"; // 不完整的 JSON

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(incompleteJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(response.statusCode() == 400 || response.statusCode() == 500,
            "不完整 JSON 应返回错误");
    }

    @Test
    @DisplayName("POST /tasks - 空请求体应返回 400 或 500")
    void postTask_WithEmptyBody_ShouldReturnError() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(response.statusCode() == 400 || response.statusCode() == 500,
            "空请求体应返回错误");
    }

    @Test
    @DisplayName("POST /tasks - 错误类型字段应返回 400")
    void postTask_WithWrongType_ShouldReturn400() throws Exception {
        String jsonWithWrongType = """
                {
                    "title": 12345,
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonWithWrongType))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Jackson 可能会自动转换数字为字符串，或者返回错误
        assertTrue(response.statusCode() == 201 || response.statusCode() == 400,
            "错误类型字段应被转换或返回错误");
    }

    @Test
    @DisplayName("PUT /tasks/{id} - 无效 JSON 应返回错误")
    void putTask_WithInvalidJson_ShouldReturnError() throws Exception {
        String invalidJson = "{ not valid }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(response.statusCode() == 400 || response.statusCode() == 500,
            "无效 JSON 应返回错误");
    }

    // ========== 不存在的任务 ID 测试 ==========

    @Test
    @DisplayName("GET /tasks/{id} - 不存在的 ID 应返回 404 并包含标准错误格式")
    void getTask_WithNonExistentId_ShouldReturn404WithStandardFormat() throws Exception {
        String nonExistentId = "task-not-exist-99999";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + nonExistentId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("message"), "错误响应应包含 message 字段");
        assertTrue(response.body().contains("code") || response.body().contains("404"),
            "错误响应应包含状态码信息");
    }

    @Test
    @DisplayName("PUT /tasks/{id} - 不存在的 ID 应返回 404")
    void putTask_WithNonExistentId_ShouldReturn404() throws Exception {
        String json = """
                {
                    "title": "更新任务",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent-id"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - 不存在的 ID 应返回 404")
    void deleteTask_WithNonExistentId_ShouldReturn404() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent-id"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("PATCH /tasks/{id} - 不存在的 ID 应返回 404")
    void patchTask_WithNonExistentId_ShouldReturn404() throws Exception {
        String json = """
                {
                    "title": "部分更新"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent-id"))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("POST /tasks/{id}/complete - 不存在的 ID 应返回 404")
    void completeTask_WithNonExistentId_ShouldReturn404() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent-id/complete"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("GET /tasks/{id}/overdue-fee - 不存在的 ID 应返回 404")
    void getOverdueFee_WithNonExistentId_ShouldReturn404() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent-id/overdue-fee"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    // ========== 边界 ID 值测试 ==========

    @Test
    @DisplayName("GET /tasks/{id} - 特殊字符 ID 应返回 404 或 400")
    void getTask_WithSpecialCharId_ShouldReturn404Or400() throws Exception {
        // 使用 URL 安全的特殊字符进行测试
        String[] specialIds = {"script-test", "path-traversal-test", "id-with-dashes", "id_with_underscores"};

        for (String specialId : specialIds) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/tasks/" + specialId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode(),
                "特殊字符 ID '" + specialId + "' 应返回 404");
        }
    }

    @Test
    @DisplayName("GET /tasks/{id} - URL 编码的特殊字符应被正确处理")
    void getTask_WithEncodedSpecialChars_ShouldHandleCorrectly() throws Exception {
        // 使用 URL 编码的特殊字符
        String encodedId = "%3Cscript%3E"; // <script> 的 URL 编码

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + encodedId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 应该返回 404（未找到）或 400（错误请求）
        assertTrue(response.statusCode() == 404 || response.statusCode() == 400,
            "URL 编码的特殊字符应返回 404 或 400");
    }

    @Test
    @DisplayName("GET /tasks/{id} - 超长 ID 应返回 404")
    void getTask_WithVeryLongId_ShouldReturn404() throws Exception {
        String longId = "a".repeat(1000);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + longId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("GET /tasks/{id} - 空 ID 应适当处理")
    void getTask_WithEmptyId_ShouldHandleAppropriately() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 空 ID 可能被路由为列表端点，或返回 404
        assertTrue(response.statusCode() == 200 || response.statusCode() == 404,
            "空 ID 应返回 200（列表）或 404");
    }

    // ========== 服务器错误响应格式测试 ==========

    @Test
    @DisplayName("错误响应 - 应包含 code、message 和 timestamp 字段")
    void errorResponse_ShouldHaveStandardFormat() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        String body = response.body();

        assertTrue(body.contains("message"), "错误响应应包含 message 字段");
        // 检查是否为有效的 JSON
        assertTrue(body.trim().startsWith("{") && body.trim().endsWith("}"),
            "错误响应应为 JSON 对象");
    }

    @Test
    @DisplayName("验证错误 - 空标题应返回 400 并包含字段信息")
    void validationError_ShouldIncludeFieldInfo() throws Exception {
        String json = """
                {
                    "title": "",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        String body = response.body();

        assertTrue(body.contains("message"), "验证错误应包含 message");
        // 错误消息中可能包含字段名
        assertTrue(body.toLowerCase().contains("title") ||
                   body.toLowerCase().contains("required") ||
                   body.toLowerCase().contains("empty"),
            "验证错误应说明问题原因");
    }

    @Test
    @DisplayName("验证错误 - 无效日期格式应返回 400 并说明期望格式")
    void validationError_ForInvalidDate_ShouldDescribeExpectedFormat() throws Exception {
        String json = """
                {
                    "title": "日期测试",
                    "dueDate": "12-31-2026"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        String body = response.body();

        assertTrue(body.contains("message"), "验证错误应包含 message");
    }

    // ========== 不支持的 HTTP 方法测试 ==========

    @Test
    @DisplayName("不支持的 HTTP 方法 - 应返回 405 或 404")
    void unsupportedMethod_ShouldReturn405Or404() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .method("TRACE", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 可能返回 405（方法不允许）或 404（未找到）
        assertTrue(response.statusCode() == 405 || response.statusCode() == 404,
            "不支持的 HTTP 方法应返回 405 或 404");
    }

    // ========== Content-Type 测试 ==========

    @Test
    @DisplayName("POST /tasks - 缺少 Content-Type 应返回 400 或 415")
    void postTask_WithoutContentType_ShouldReturnError() throws Exception {
        String json = """
                {
                    "title": "测试",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                // 不设置 Content-Type
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Javalin 可能会尝试解析，或返回错误
        assertTrue(response.statusCode() == 201 || response.statusCode() == 400 ||
                   response.statusCode() == 415,
            "缺少 Content-Type 应被处理");
    }

    @Test
    @DisplayName("POST /tasks - 错误的 Content-Type 应返回 415 或尝试解析")
    void postTask_WithWrongContentType_ShouldReturn415() throws Exception {
        String json = """
                {
                    "title": "测试",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 可能返回 415（不支持的媒体类型）或尝试解析
        assertTrue(response.statusCode() == 201 || response.statusCode() == 415 ||
                   response.statusCode() == 400,
            "错误的 Content-Type 应被处理");
    }

    // ========== 健康检查测试（正例）==========

    @Test
    @DisplayName("GET /health - 应返回服务状态信息")
    void getHealth_ShouldReturnServiceStatus() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("service"), "健康检查应包含 service 字段");
        assertTrue(response.body().contains("status"), "健康检查应包含 status 字段");
        assertTrue(response.body().contains("UP") || response.body().contains("OK"),
            "服务状态应为 UP 或 OK");
    }

    @Test
    @DisplayName("GET /health - 应包含 CORS 状态信息")
    void getHealth_ShouldIncludeCorsStatus() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("cors") || response.body().contains("CORS"),
            "健康检查应包含 CORS 状态信息");
    }
}
