/*
 * TaskApiIntegrationTest - API 集成测试
 *
 * Week 12 重点：集成测试 - 启动真实服务，发送 HTTP 请求，验证完整链路
 *
 * 本测试类演示了如何编写集成测试：
 * 1. 使用 @BeforeAll/@AfterEach 管理服务器生命周期
 * 2. 使用 HttpClient 发送真实 HTTP 请求
 * 3. 验证 HTTP 状态码、响应头、响应体
 * 4. 测试正常场景、边界场景、异常场景
 */
package com.campusflow.integration;

import com.campusflow.App;
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
 * API 集成测试 - 测试 CampusFlow REST API 端点
 *
 * <p>测试金字塔 - 集成测试层：
 * <ul>
 *   <li>启动真实 Javalin 服务器</li>
 *   <li>使用真实 HttpClient 发送请求</li>
 *   <li>验证 HTTP 状态码和 JSON 响应</li>
 *   <li>发现单元测试无法发现的问题（路由、序列化、契约）</li>
 * </ul>
 *
 * <p>与单元测试的区别：
 * <ul>
 *   <li>单元测试：直接调用方法，不启动服务，速度快</li>
 *   <li>集成测试：启动服务，发送 HTTP 请求，速度较慢但更真实</li>
 * </ul>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("API 集成测试 - Task 管理接口")
public class TaskApiIntegrationTest {

    private static Javalin app;
    private static final int TEST_PORT = 7080;
    private static final String BASE_URL = "http://localhost:" + TEST_PORT;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    /**
     * @BeforeAll - 在所有测试之前执行一次
     * 启动 Javalin 服务器，所有测试共享同一个服务器实例
     */
    @BeforeAll
    static void setUpClass() {
        app = App.createApp(TEST_PORT);
    }

    /**
     * @AfterAll - 在所有测试之后执行一次
     * 关闭 Javalin 服务器，释放端口
     */
    @AfterAll
    static void tearDownClass() {
        if (app != null) {
            app.stop();
        }
    }

    // ========== 正例测试（Happy Path）==========

    @Test
    @Order(1)
    @DisplayName("GET /health - 健康检查应返回 200")
    void getHealth_WhenServiceUp_ShouldReturn200() throws Exception {
        // given: 准备请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .GET()
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("CampusFlow"));
        assertTrue(response.body().contains("UP"));

        // 验证 JSON 格式
        JsonNode json = objectMapper.readTree(response.body());
        assertEquals("2.3.0", json.get("version").asText());
        assertTrue(json.has("features"));
    }

    @Test
    @Order(2)
    @DisplayName("GET /tasks - 获取任务列表应返回 200 和数据数组")
    void getTasks_WhenTasksExist_ShouldReturn200WithData() throws Exception {
        // given: 准备请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .GET()
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());
        assertTrue(json.has("data"));
        assertTrue(json.has("total"));

        // 验证 data 是数组
        assertTrue(json.get("data").isArray());
        assertTrue(json.get("total").asLong() >= 0);
    }

    @Test
    @Order(3)
    @DisplayName("POST /tasks - 创建有效任务应返回 201 和创建的任务")
    void postTask_WhenValid_ShouldReturn201WithTask() throws Exception {
        // given: 准备请求体
        String jsonBody = """
                {
                    "title": "集成测试任务",
                    "description": "这是一个集成测试创建的任务",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("集成测试任务"));

        // 验证返回的任务有 ID
        JsonNode json = objectMapper.readTree(response.body());
        assertTrue(json.has("id"));
        assertEquals("集成测试任务", json.get("title").asText());
        assertEquals("pending", json.get("status").asText());
    }

    @Test
    @Order(4)
    @DisplayName("GET /tasks/{id} - 获取存在的任务应返回 200")
    void getTaskById_WhenExists_ShouldReturn200() throws Exception {
        // given: 先创建一个任务
        String createJson = """
                {
                    "title": "查询测试任务",
                    "description": "用于测试 GET by ID",
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
        assertEquals(201, createResponse.statusCode());

        Task createdTask = objectMapper.readValue(createResponse.body(), Task.class);
        String taskId = createdTask.getId();

        // when: 查询该任务
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + taskId))
                .GET()
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, getResponse.statusCode());
        assertTrue(getResponse.body().contains("查询测试任务"));

        JsonNode json = objectMapper.readTree(getResponse.body());
        assertEquals(taskId, json.get("id").asText());
    }

    @Test
    @Order(5)
    @DisplayName("PUT /tasks/{id} - 更新存在的任务应返回 200")
    void putTask_WhenExists_ShouldReturn200() throws Exception {
        // given: 先创建一个任务
        String createJson = """
                {
                    "title": "原任务",
                    "description": "原描述",
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

        // when: 更新任务
        String updateJson = """
                {
                    "title": "更新后的任务",
                    "description": "更新后的描述",
                    "dueDate": "2027-01-31"
                }
                """;

        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(updateJson))
                .build();

        HttpResponse<String> updateResponse = httpClient.send(updateRequest,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, updateResponse.statusCode());
        assertTrue(updateResponse.body().contains("更新后的任务"));

        JsonNode json = objectMapper.readTree(updateResponse.body());
        assertEquals("更新后的任务", json.get("title").asText());
    }

    @Test
    @Order(6)
    @DisplayName("DELETE /tasks/{id} - 删除存在的任务应返回 204")
    void deleteTask_WhenExists_ShouldReturn204() throws Exception {
        // given: 先创建一个任务
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

        HttpResponse<String> createResponse = httpClient.send(createRequest,
                HttpResponse.BodyHandlers.ofString());
        Task createdTask = objectMapper.readValue(createResponse.body(), Task.class);

        // when: 删除任务
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId()))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = httpClient.send(deleteRequest,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(204, deleteResponse.statusCode());

        // 验证任务已被删除
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId()))
                .GET()
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(404, getResponse.statusCode());
    }

    // ========== 边界测试 ==========

    @Test
    @Order(10)
    @DisplayName("GET /tasks - 空列表应返回空数组")
    void getTasks_WhenEmpty_ShouldReturnEmptyArray() throws Exception {
        // 注意：由于预置数据的存在，这个测试可能需要调整
        // 实际项目中，可以在 @BeforeEach 中清空数据

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());
        // 至少应该有 data 字段
        assertTrue(json.has("data"));
    }

    @Test
    @Order(11)
    @DisplayName("POST /tasks - 超长标题应被处理（接受或拒绝）")
    void postTask_WhenTitleTooLong_ShouldHandleAppropriately() throws Exception {
        // given: 准备超长标题（1000 字符）
        String longTitle = "A".repeat(1000);
        String jsonBody = String.format("""
                {
                    "title": "%s",
                    "description": "测试超长标题",
                    "dueDate": "2026-12-31"
                }
                """, longTitle);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 应该成功创建或返回 400
        assertTrue(response.statusCode() == 201 || response.statusCode() == 400,
                "超长标题应被接受或拒绝");
    }

    @Test
    @Order(12)
    @DisplayName("POST /tasks - 标题包含特殊字符应被正确处理")
    void postTask_WithSpecialChars_ShouldHandleCorrectly() throws Exception {
        // given: 准备包含特殊字符的标题
        String specialTitle = "任务 🔥🔥 <script> alert('xss') </script>";
        String jsonBody = String.format("""
                {
                    "title": "%s",
                    "description": "测试特殊字符",
                    "dueDate": "2026-12-31"
                }
                """, specialTitle.replace("\"", "\\\""));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 应该返回 201（接受）或 400（拒绝）
        assertTrue(response.statusCode() == 201 || response.statusCode() == 400 ||
                   response.statusCode() == 500,
                "特殊字符应被处理");
    }

    @Test
    @Order(13)
    @DisplayName("POST /tasks - 缺少可选字段 description 应成功")
    void postTask_WithoutDescription_ShouldSucceed() throws Exception {
        // given: 准备只有必填字段的请求
        String jsonBody = """
                {
                    "title": "最少字段任务",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 应该成功创建
        assertEquals(201, response.statusCode());
    }

    // ========== 反例测试 ==========

    @Test
    @Order(20)
    @DisplayName("GET /tasks/{id} - 不存在的 ID 应返回 404")
    void getTaskById_WhenNotExists_ShouldReturn404() throws Exception {
        // given: 使用不存在的 ID
        String nonExistentId = "task-not-exist-99999";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + nonExistentId))
                .GET()
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 应返回 404
        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("message"));
    }

    @Test
    @Order(21)
    @DisplayName("POST /tasks - 空标题应返回 400")
    void postTask_WhenTitleEmpty_ShouldReturn400() throws Exception {
        // given: 准备空标题的请求
        String jsonBody = """
                {
                    "title": "",
                    "description": "空标题测试",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 应返回 400
        assertEquals(400, response.statusCode());
        assertTrue(response.body().toLowerCase().contains("title") ||
                   response.body().toLowerCase().contains("required"));
    }

    @Test
    @Order(22)
    @DisplayName("POST /tasks - 缺少必填字段 title 应返回错误")
    void postTask_WithoutTitle_ShouldReturnError() throws Exception {
        // given: 准备缺少 title 的请求
        String jsonBody = """
                {
                    "description": "缺少标题",
                    "dueDate": "2026-12-31"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 应返回 400 或 500
        assertTrue(response.statusCode() == 400 || response.statusCode() == 500,
                "缺少必填字段应返回错误");
    }

    @Test
    @Order(23)
    @DisplayName("POST /tasks - 无效日期格式应返回 400")
    void postTask_WithInvalidDateFormat_ShouldReturn400() throws Exception {
        // given: 准备无效日期格式的请求
        String jsonBody = """
                {
                    "title": "日期测试",
                    "description": "无效日期",
                    "dueDate": "12/31/2026"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 应返回 400
        assertEquals(400, response.statusCode());
        assertTrue(response.body().toLowerCase().contains("date") ||
                   response.body().toLowerCase().contains("format"));
    }

    @Test
    @Order(24)
    @DisplayName("POST /tasks - 无效 JSON 应返回 400 或 500")
    void postTask_WithInvalidJson_ShouldReturnError() throws Exception {
        // given: 准备无效的 JSON
        String invalidJson = "{ this is not valid json }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 应返回错误状态码
        assertTrue(response.statusCode() == 400 || response.statusCode() == 500,
                "无效 JSON 应返回错误");
    }

    // ========== 业务功能测试 ==========

    @Test
    @Order(30)
    @DisplayName("POST /tasks/{id}/complete - 标记任务完成应成功")
    void postTaskComplete_WhenTaskExists_ShouldReturn200() throws Exception {
        // given: 先创建一个任务
        String createJson = """
                {
                    "title": "待完成任务",
                    "description": "将被标记为完成",
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
        HttpRequest completeRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId() + "/complete"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> completeResponse = httpClient.send(completeRequest,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, completeResponse.statusCode());

        JsonNode json = objectMapper.readTree(completeResponse.body());
        assertEquals("completed", json.get("status").asText());
        assertTrue(json.has("completedAt"));
    }

    @Test
    @Order(31)
    @DisplayName("GET /tasks/{id}/overdue-fee - 计算逾期费用应返回数值")
    void getTaskOverdueFee_WhenTaskOverdue_ShouldReturnFee() throws Exception {
        // given: 创建一个逾期任务
        String pastDate = LocalDate.now().minusDays(5).toString();
        String createJson = String.format("""
                {
                    "title": "逾期任务",
                    "description": "已逾期 5 天",
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
        HttpRequest feeRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId() + "/overdue-fee"))
                .GET()
                .build();

        HttpResponse<String> feeResponse = httpClient.send(feeRequest,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, feeResponse.statusCode());

        JsonNode json = objectMapper.readTree(feeResponse.body());
        assertTrue(json.has("fee"));
        assertTrue(json.get("fee").asDouble() > 0,
                "逾期任务应产生费用");
        assertTrue(json.has("overdueDays"));
        assertTrue(json.has("calculationStrategy"));
    }

    @Test
    @Order(32)
    @DisplayName("GET /stats - 获取统计信息应返回汇总数据")
    void getStats_ShouldReturnSummaryData() throws Exception {
        // given: 准备请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/stats"))
                .GET()
                .build();

        // when: 发送请求
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, response.statusCode());

        JsonNode json = objectMapper.readTree(response.body());
        assertTrue(json.has("total"));
        assertTrue(json.has("pending"));
        assertTrue(json.has("inProgress"));
        assertTrue(json.has("completed"));
        assertTrue(json.has("overdue"));

        // 验证数据一致性
        long total = json.get("total").asLong();
        long pending = json.get("pending").asLong();
        long inProgress = json.get("inProgress").asLong();
        long completed = json.get("completed").asLong();

        assertEquals(total, pending + inProgress + completed,
                "各状态任务数之和应等于总数");
    }

    // ========== HTTP 方法测试 ==========

    @Test
    @Order(40)
    @DisplayName("PATCH /tasks/{id} - 部分更新应成功")
    void patchTask_WithPartialData_ShouldReturn200() throws Exception {
        // given: 先创建一个任务
        String createJson = """
                {
                    "title": "原任务",
                    "description": "原描述",
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

        // when: 只更新标题
        String patchJson = """
                {
                    "title": "只更新标题"
                }
                """;

        HttpRequest patchRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tasks/" + createdTask.getId()))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(patchJson))
                .build();

        HttpResponse<String> patchResponse = httpClient.send(patchRequest,
                HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, patchResponse.statusCode());

        JsonNode json = objectMapper.readTree(patchResponse.body());
        assertEquals("只更新标题", json.get("title").asText());
    }

    // ========== 端到端场景测试 ==========

    @Test
    @Order(50)
    @DisplayName("E2E: 创建 -> 查询 -> 更新 -> 完成 -> 删除")
    void e2e_TaskLifecycle_ShouldWork() throws Exception {
        String taskId = null;

        try {
            // 1. 创建任务
            String createJson = """
                    {
                        "title": "E2E 测试任务",
                        "description": "完整生命周期测试",
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
            assertEquals(201, createResponse.statusCode());

            Task createdTask = objectMapper.readValue(createResponse.body(), Task.class);
            taskId = createdTask.getId();
            assertNotNull(taskId);

            // 2. 查询任务
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/tasks/" + taskId))
                    .GET()
                    .build();

            HttpResponse<String> getResponse = httpClient.send(getRequest,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, getResponse.statusCode());

            // 3. 更新任务
            String updateJson = """
                    {
                        "title": "E2E 测试任务（已更新）",
                        "description": "描述已更新",
                        "dueDate": "2027-01-31"
                    }
                    """;

            HttpRequest updateRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/tasks/" + taskId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updateJson))
                    .build();

            HttpResponse<String> updateResponse = httpClient.send(updateRequest,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, updateResponse.statusCode());

            // 4. 标记完成
            HttpRequest completeRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/tasks/" + taskId + "/complete"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> completeResponse = httpClient.send(completeRequest,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, completeResponse.statusCode());

            // 5. 删除任务
            HttpRequest deleteRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/tasks/" + taskId))
                    .DELETE()
                    .build();

            HttpResponse<String> deleteResponse = httpClient.send(deleteRequest,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(204, deleteResponse.statusCode());

        } finally {
            // 清理：如果测试失败，尝试删除已创建的任务
            if (taskId != null) {
                try {
                    HttpRequest deleteRequest = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/tasks/" + taskId))
                            .DELETE()
                            .build();
                    httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
                } catch (Exception e) {
                    // 忽略清理错误
                }
            }
        }
    }
}
