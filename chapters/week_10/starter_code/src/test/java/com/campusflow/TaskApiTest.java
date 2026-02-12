package com.campusflow;

import com.campusflow.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Task API 集成测试
 *
 * 测试 CampusFlow REST API 的所有端点
 * 使用 HttpClient 发送真实 HTTP 请求
 */
public class TaskApiTest {

    private static Javalin app;
    private static final int TEST_PORT = 7071;
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

    @BeforeEach
    void resetData() {
        // 重置内存中的数据（实际项目中可能需要更复杂的清理逻辑）
    }

    // ========== GET /tasks 测试 ==========

    @Test
    @DisplayName("GET /tasks - 获取所有任务列表")
    void shouldGetAllTasks() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("[")); // 返回 JSON 数组
    }

    @Test
    @DisplayName("GET /tasks?status=pending - 按状态过滤任务")
    void shouldFilterTasksByStatus() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks?status=pending"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    // ========== GET /tasks/{id} 测试 ==========

    @Test
    @DisplayName("GET /tasks/{id} - 获取存在的任务")
    void shouldGetTaskById() throws Exception {
        // 先创建一个任务
        String json = """
                {
                    "title": "测试任务",
                    "description": "测试描述",
                    "dueDate": "2024-12-31"
                }
                """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, createResponse.statusCode());

        // 提取创建的任务 ID
        Task created = mapper.readValue(createResponse.body(), Task.class);
        String taskId = created.getId();

        // 获取该任务
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + taskId))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, getResponse.statusCode());
        assertTrue(getResponse.body().contains("测试任务"));
    }

    @Test
    @DisplayName("GET /tasks/{id} - 获取不存在的任务返回 404")
    void shouldReturn404ForNonExistentTask() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent-id"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    // ========== POST /tasks 测试 ==========

    @Test
    @DisplayName("POST /tasks - 创建有效任务")
    void shouldCreateTask() throws Exception {
        String json = """
                {
                    "title": "新任务",
                    "description": "任务描述",
                    "dueDate": "2024-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("新任务"));
        assertTrue(response.body().contains("id")); // 返回的任务有 ID
    }

    @Test
    @DisplayName("POST /tasks - 无效 JSON 返回 400 或 500")
    void shouldReturnErrorForInvalidJson() throws Exception {
        String invalidJson = "{ invalid json }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 无效 JSON 可能返回 400 或 500，取决于实现
        assertTrue(response.statusCode() == 400 || response.statusCode() == 500);
    }

    @Test
    @DisplayName("POST /tasks - 空标题返回 400")
    void shouldReturn400ForEmptyTitle() throws Exception {
        String json = """
                {
                    "title": "",
                    "description": "描述",
                    "dueDate": "2024-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    // ========== PUT /tasks/{id} 测试 ==========

    @Test
    @DisplayName("PUT /tasks/{id} - 更新存在的任务")
    void shouldUpdateTask() throws Exception {
        // 先创建任务
        String createJson = """
                {
                    "title": "原任务",
                    "description": "原描述",
                    "dueDate": "2024-12-31"
                }
                """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(createJson))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        Task created = mapper.readValue(createResponse.body(), Task.class);

        // 更新任务
        String updateJson = """
                {
                    "title": "更新后的任务",
                    "description": "更新后的描述",
                    "dueDate": "2025-01-31"
                }
                """;

        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + created.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(updateJson))
                .build();

        HttpResponse<String> updateResponse = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, updateResponse.statusCode());
        assertTrue(updateResponse.body().contains("更新后的任务"));
    }

    @Test
    @DisplayName("PUT /tasks/{id} - 更新不存在的任务返回 404")
    void shouldReturn404WhenUpdatingNonExistentTask() throws Exception {
        String json = """
                {
                    "title": "任务",
                    "description": "描述",
                    "dueDate": "2024-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    // ========== DELETE /tasks/{id} 测试 ==========

    @Test
    @DisplayName("DELETE /tasks/{id} - 删除存在的任务")
    void shouldDeleteTask() throws Exception {
        // 先创建任务
        String createJson = """
                {
                    "title": "待删除任务",
                    "description": "描述",
                    "dueDate": "2024-12-31"
                }
                """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(createJson))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        Task created = mapper.readValue(createResponse.body(), Task.class);

        // 删除任务
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + created.getId()))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, deleteResponse.statusCode());

        // 再次获取应返回 404
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + created.getId()))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, getResponse.statusCode());
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - 删除不存在的任务返回 404")
    void shouldReturn404WhenDeletingNonExistentTask() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    // ========== 边界情况测试 ==========

    @Test
    @DisplayName("POST /tasks - 超长标题边界测试")
    void shouldHandleLongTitle() throws Exception {
        String longTitle = "a".repeat(200);
        String json = String.format("""
                {
                    "title": "%s",
                    "description": "描述",
                    "dueDate": "2024-12-31"
                }
                """, longTitle);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 应该成功创建或返回 400（取决于实现）
        assertTrue(response.statusCode() == 201 || response.statusCode() == 400);
    }

    @Test
    @DisplayName("POST /tasks - 无效日期格式")
    void shouldHandleInvalidDateFormat() throws Exception {
        String json = """
                {
                    "title": "任务",
                    "description": "描述",
                    "dueDate": "invalid-date"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 应该返回 400 或接受并使用默认值
        assertTrue(response.statusCode() == 201 || response.statusCode() == 400);
    }
}
