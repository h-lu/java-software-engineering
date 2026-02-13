/*
 * ApiContractTest - API 契约测试
 *
 * Week 12 重点：契约测试 - 验证 API 契约不被破坏
 *
 * 本测试类演示了如何编写契约测试：
 * 1. 验证响应 JSON 格式符合预期
 * 2. 验证字段名、字段类型正确
 * 3. 验证必填字段存在
 * 4. 验证错误响应格式一致
 *
 * 契约测试的价值：
 * - 前后端分离开发时，确保 API 契约不被破坏
 * - 修改 API 时，测试会失败，提醒开发者检查影响
 * - 可以作为 API 文档的补充验证
 */
package com.campusflow.contract;

import com.campusflow.App;
import com.campusflow.dto.TaskRequest;
import com.campusflow.model.Task;
import com.fasterxml.jackson.databind.JsonNode;
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
 * API 契约测试 - 验证 API 响应格式
 *
 * <p>什么是契约？
 * <ul>
 *   <li>契约 = 前后端之间的"合同"</li>
 *   <li>定义了 API 的请求格式、响应格式、字段名、字段类型</li>
 *   <li>契约被破坏 = 前端调用失败或数据解析错误</li>
 * </ul>
 *
 * <p>契约测试 vs 功能测试：
 * <ul>
 *   <li>功能测试：验证"功能是否正确"</li>
 *   <li>契约测试：验证"接口是否稳定"</li>
 * </ul>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("API 契约测试 - 验证 API 响应格式")
public class ApiContractTest {

    private static Javalin app;
    private static final int TEST_PORT = 7081;
    private static final String BASE_URL = "http://localhost:" + TEST_PORT;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

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

    // ========== 健康检查端点契约 ==========

    @Test
    @Order(1)
    @DisplayName("GET /health - 响应应包含 service、version、status 字段")
    void healthContract_ShouldHaveRequiredFields() throws Exception {
        // when: 发送请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应格式
        assertEquals(200, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());

        // 验证必填字段存在
        assertTrue(json.has("service"), "响应应包含 service 字段");
        assertTrue(json.has("version"), "响应应包含 version 字段");
        assertTrue(json.has("status"), "响应应包含 status 字段");
        assertTrue(json.has("features"), "响应应包含 features 字段");

        // 验证字段类型
        assertTrue(json.get("service").isTextual(), "service 应为字符串");
        assertTrue(json.get("version").isTextual(), "version 应为字符串");
        assertTrue(json.get("status").isTextual(), "status 应为字符串");
        assertTrue(json.get("features").isObject(), "features 应为对象");

        // 验证字段值
        assertEquals("CampusFlow", json.get("service").asText());
        assertEquals("UP", json.get("status").asText());
    }

    // ========== GET /tasks 契约 ==========

    @Test
    @Order(2)
    @DisplayName("GET /tasks - 响应应包含 data 和 total 字段")
    void getTasksContract_ShouldReturnDataAndTotal() throws Exception {
        // when: 发送请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应格式
        assertEquals(200, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());

        // 验证必填字段
        assertTrue(json.has("data"), "响应应包含 data 字段");
        assertTrue(json.has("total"), "响应应包含 total 字段");

        // 验证字段类型
        assertTrue(json.get("data").isArray(), "data 应为数组");
        assertTrue(json.get("total").isNumber(), "total 应为数字");

        // 验证 data 中每个 Task 对象的格式
        if (json.get("data").size() > 0) {
            JsonNode firstTask = json.get("data").get(0);
            validateTaskContract(firstTask);
        }
    }

    // ========== POST /tasks 契约 ==========

    @Test
    @Order(3)
    @DisplayName("POST /tasks - 创建成功应返回完整 Task 对象")
    void postTaskContract_ShouldReturnFullTaskObject() throws Exception {
        // given: 准备请求
        String jsonBody = """
                {
                    "title": "契约测试任务",
                    "description": "验证契约",
                    "dueDate": "2026-12-31"
                }
                """;

        // when: 发送请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应格式
        assertEquals(201, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());
        validateTaskContract(json);

        // 验证 ID 不为空
        assertTrue(json.has("id"), "Task 应包含 id 字段");
        assertNotNull(json.get("id").asText(), "id 不应为 null");

        // 验证初始状态
        assertEquals("pending", json.get("status").asText(),
                "新任务状态应为 pending");
    }

    // ========== GET /tasks/{id} 契约 ==========

    @Test
    @Order(4)
    @DisplayName("GET /tasks/{id} - 响应应符合 Task 契约")
    void getTaskByIdContract_ShouldReturnValidTask() throws Exception {
        // given: 先创建一个任务
        String createJson = """
                {
                    "title": "契约测试",
                    "description": "测试",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(createJson))
                .build();

        HttpResponse<String> createResponse = httpClient.send(createRequest,
                HttpResponse.BodyHandlers.ofString());
        Task createdTask = objectMapper.readValue(createResponse.body(), Task.class);

        // when: 查询任务
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId()))
                .GET()
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应格式
        assertEquals(200, getResponse.statusCode());

        JsonNode json = objectMapper.readTree(getResponse.body());
        validateTaskContract(json);
    }

    // ========== 错误响应契约 ==========

    @Test
    @Order(10)
    @DisplayName("404 错误响应应包含 code、message、timestamp 字段")
    void error404Contract_ShouldHaveStandardFormat() throws Exception {
        // when: 请求不存在的资源
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/non-existent-id"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证错误响应格式
        assertEquals(404, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());
        validateErrorContract(json, 404);
    }

    @Test
    @Order(11)
    @DisplayName("400 验证错误响应应包含 code、message、timestamp 字段")
    void error400Contract_ShouldHaveStandardFormat() throws Exception {
        // when: 发送无效请求
        String jsonBody = """
                {
                    "title": "",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证错误响应格式
        assertEquals(400, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());
        validateErrorContract(json, 400);

        // 验证错误消息包含有用信息
        String message = json.get("message").asText().toLowerCase();
        assertTrue(message.contains("title") || message.contains("required") || message.contains("empty"),
                "错误消息应说明问题字段");
    }

    @Test
    @Order(12)
    @DisplayName("无效 JSON 请求应返回错误响应")
    void invalidJsonContract_ShouldReturnErrorResponse() throws Exception {
        // when: 发送无效 JSON
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{ invalid json }"))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证错误响应格式
        assertTrue(response.statusCode() == 400 || response.statusCode() == 500,
                "无效 JSON 应返回错误状态码");

        // 如果有响应体，验证格式
        if (response.body() != null && !response.body().isEmpty()) {
            JsonNode json = objectMapper.readTree(response.body());
            assertTrue(json.has("message") || json.has("error"),
                    "错误响应应包含 message 或 error 字段");
        }
    }

    // ========== 业务功能端点契约 ==========

    @Test
    @Order(20)
    @DisplayName("POST /tasks/{id}/complete - 响应应包含 completedAt 字段")
    void completeTaskContract_ShouldIncludeCompletedAt() throws Exception {
        // given: 先创建一个任务
        String createJson = """
                {
                    "title": "待完成",
                    "description": "测试",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(createJson))
                .build();

        HttpResponse<String> createResponse = httpClient.send(createRequest,
                HttpResponse.BodyHandlers.ofString());
        Task createdTask = objectMapper.readValue(createResponse.body(), Task.class);

        // when: 标记为完成
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId() + "/complete"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应格式
        assertEquals(200, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());
        validateTaskContract(json);

        // 验证完成状态
        assertEquals("completed", json.get("status").asText());
        assertTrue(json.has("completedAt"), "完成的任务应包含 completedAt 字段");
    }

    @Test
    @Order(21)
    @DisplayName("GET /tasks/{id}/overdue-fee - 响应应包含费用和策略字段")
    void overdueFeeContract_ShouldIncludeFeeAndStrategy() throws Exception {
        // given: 创建一个逾期任务
        String pastDate = LocalDate.now().minusDays(5).toString();
        String createJson = String.format("""
                {
                    "title": "逾期任务",
                    "description": "测试",
                    "dueDate": "%s"
                }
                """, pastDate);

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(createJson))
                .build();

        HttpResponse<String> createResponse = httpClient.send(createRequest,
                HttpResponse.BodyHandlers.ofString());
        Task createdTask = objectMapper.readValue(createResponse.body(), Task.class);

        // when: 计算逾期费用
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId() + "/overdue-fee"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应格式
        assertEquals(200, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());

        // 验证必填字段
        assertTrue(json.has("taskId"), "响应应包含 taskId 字段");
        assertTrue(json.has("taskTitle"), "响应应包含 taskTitle 字段");
        assertTrue(json.has("overdueDays"), "响应应包含 overdueDays 字段");
        assertTrue(json.has("fee"), "响应应包含 fee 字段");
        assertTrue(json.has("calculationStrategy"), "响应应包含 calculationStrategy 字段");

        // 验证字段类型
        assertTrue(json.get("taskId").isTextual(), "taskId 应为字符串");
        assertTrue(json.get("taskTitle").isTextual(), "taskTitle 应为字符串");
        assertTrue(json.get("overdueDays").isNumber(), "overdueDays 应为数字");
        assertTrue(json.get("fee").isNumber(), "fee 应为数字");
        assertTrue(json.get("calculationStrategy").isTextual(), "calculationStrategy 应为字符串");
    }

    @Test
    @Order(22)
    @DisplayName("GET /stats - 响应应包含统计字段")
    void statsContract_ShouldIncludeAllStats() throws Exception {
        // when: 发送请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/stats"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应格式
        assertEquals(200, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());

        // 验证必填字段
        assertTrue(json.has("total"), "统计应包含 total 字段");
        assertTrue(json.has("pending"), "统计应包含 pending 字段");
        assertTrue(json.has("inProgress"), "统计应包含 inProgress 字段");
        assertTrue(json.has("completed"), "统计应包含 completed 字段");
        assertTrue(json.has("overdue"), "统计应包含 overdue 字段");

        // 验证字段类型（所有统计都应为数字）
        assertTrue(json.get("total").isNumber(), "total 应为数字");
        assertTrue(json.get("pending").isNumber(), "pending 应为数字");
        assertTrue(json.get("inProgress").isNumber(), "inProgress 应为数字");
        assertTrue(json.get("completed").isNumber(), "completed 应为数字");
        assertTrue(json.get("overdue").isNumber(), "overdue 应为数字");

        // 验证数据一致性
        long total = json.get("total").asLong();
        long pending = json.get("pending").asLong();
        long inProgress = json.get("inProgress").asLong();
        long completed = json.get("completed").asLong();

        assertEquals(total, pending + inProgress + completed,
                "各状态任务数之和应等于总数");
    }

    // ========== Content-Type 契约 ==========

    @Test
    @Order(30)
    @DisplayName("所有响应应设置正确的 Content-Type")
    void allResponses_ShouldHaveCorrectContentType() throws Exception {
        // 测试健康检查
        HttpRequest healthRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .GET()
                .build();

        HttpResponse<String> healthResponse = httpClient.send(healthRequest,
                HttpResponse.BodyHandlers.ofString());

        assertTrue(healthResponse.headers().map().containsKey("content-type"),
                "响应应包含 Content-Type 头");

        String contentType = healthResponse.headers().firstValue("content-type").orElse("");
        assertTrue(contentType.contains("application/json"),
                "Content-Type 应为 application/json，实际: " + contentType);
    }

    // ========== 辅助验证方法 ==========

    /**
     * 验证 Task 对象的契约（必填字段和字段类型）
     */
    private void validateTaskContract(JsonNode taskJson) {
        // 验证必填字段存在
        assertTrue(taskJson.has("id"), "Task 应包含 id 字段");
        assertTrue(taskJson.has("title"), "Task 应包含 title 字段");
        assertTrue(taskJson.has("status"), "Task 应包含 status 字段");
        assertTrue(taskJson.has("createdAt"), "Task 应包含 createdAt 字段");

        // 验证字段类型
        assertTrue(taskJson.get("id").isTextual(), "id 应为字符串");
        assertTrue(taskJson.get("title").isTextual(), "title 应为字符串");
        assertTrue(taskJson.get("status").isTextual(), "status 应为字符串");

        // 验证 status 的有效值
        String status = taskJson.get("status").asText();
        assertTrue(
                status.equals("pending") || status.equals("in_progress") || status.equals("completed"),
                "status 应为 pending、in_progress 或 completed，实际: " + status
        );

        // description 和 dueDate 是可选的，但如果存在，验证类型
        if (taskJson.has("description") && !taskJson.get("description").isNull()) {
            assertTrue(taskJson.get("description").isTextual(), "description 应为字符串");
        }

        // dueDate 可能是字符串（ISO-8601）或数组（Jackson 默认序列化）
        if (taskJson.has("dueDate") && !taskJson.get("dueDate").isNull()) {
            JsonNode dueDateNode = taskJson.get("dueDate");
            assertTrue(
                dueDateNode.isTextual() || dueDateNode.isArray(),
                "dueDate 应为字符串或数组"
            );
        }
    }

    /**
     * 验证错误响应的契约（必填字段和字段类型）
     */
    private void validateErrorContract(JsonNode errorJson, int expectedCode) {
        // 验证必填字段存在
        assertTrue(errorJson.has("code") || errorJson.has("status"),
                "错误响应应包含 code 或 status 字段");

        assertTrue(errorJson.has("message"),
                "错误响应应包含 message 字段");

        assertTrue(errorJson.has("timestamp"),
                "错误响应应包含 timestamp 字段");

        // 验证字段类型
        if (errorJson.has("code")) {
            assertTrue(errorJson.get("code").isNumber(), "code 应为数字");
            assertEquals(expectedCode, errorJson.get("code").asInt(),
                    "错误代码应匹配 HTTP 状态码");
        }

        assertTrue(errorJson.get("message").isTextual(), "message 应为字符串");
        assertTrue(errorJson.get("timestamp").isNumber(), "timestamp 应为数字");

        // 验证错误消息不为空
        assertFalse(errorJson.get("message").asText().trim().isEmpty(),
                "错误消息不应为空");
    }
}
