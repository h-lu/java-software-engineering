/*
 * 示例：API 契约测试
 * 运行方式：阅读理解（需 JUnit 5 + Javalin）
 * 预期输出：理解契约测试的价值和实现
 *
 * 本例演示：
 * 1. 验证请求/响应格式
 * 2. OpenAPI/Swagger 契约文档
 * 3. 前后端契约同步
 */
package examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API 契约测试示例
 *
 * <p>契约测试（Contract Test）验证 API 契约（接口规范）是否被破坏。
 *
 * <p>契约测试的核心思想：
 * <ul>
 *   <li>定义契约：用 OpenAPI/Swagger 文档定义 API 规范</li>
 *   <li>测试契约：后端测试确保'实现符合契约'，前端测试确保'调用符合契约'</li>
 *   <li>持续验证：每次代码变更都运行契约测试，确保不被破坏</li>
 * </ul>
 *
 * <p>契约 vs 集成测试：
 * <table>
 *   <tr><th>维度</th><th>契约测试</th><th>集成测试</th></tr>
 *   <tr><td>关注点</th><td>接口规范</td><td>功能实现</td></tr>
 *   <tr><td>验证内容</th><td>字段名、类型、状态码</td><td>业务逻辑</td></tr>
 *   <tr><td>破坏条件</th><td>契约变更</td><td>代码变更</td></tr>
 * </table>
 */
public class _12_contract_test {

    private static Javalin app;
    private static final int TEST_PORT = 8082;
    private static final String BASE_URL = "http://localhost:" + TEST_PORT;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // ========== API 契约定义 ==========

    /**
     * API 契约：GET /api/tasks/{id}
     *
     * <p>OpenAPI 规范：
     * <pre>
     * /api/tasks/{id}:
     *   get:
     *     summary: Get task by ID
     *     parameters:
     *       - name: id
     *         in: path
     *         required: true
     *         schema:
     *           type: string
     *     responses:
     *       200:
     *         description: Task found
     *         content:
     *           application/json:
     *             schema:
     *               type: object
     *               required: [id, title, completed]
     *               properties:
     *                 id:
     *                   type: string
     *                 title:
     *                   type: string
     *                 completed:
     *                   type: boolean
     *       404:
     *         description: Task not found
     *         content:
     *           application/json:
     *             schema:
     *               type: object
     *               properties:
     *                 error:
     *                   type: string
     * </pre>
     */

    // ========== 生命周期管理 ==========

    @BeforeAll
    static void setUpAll() {
        app = Javalin.create().start(TEST_PORT);
        configureRoutes();
    }

    @AfterAll
    static void tearDownAll() {
        if (app != null) {
            app.stop();
        }
    }

    private static void configureRoutes() {
        // ✅ 正确实现：符合契约
        app.get("/api/tasks/:id", ctx -> {
            String id = ctx.pathParam("id");
            if ("1".equals(id)) {
                ctx.json(new TaskResponse("1", "学习契约测试", false));
            } else {
                ctx.status(404).json(new ErrorResponse("Task not found"));
            }
        });

        // ❌ 错误实现：违反契约（字段名错误）
        app.get("/api/wrong/:id", ctx -> {
            // 这个端点返回 taskName 而不是 title，违反契约
            ctx.json(new WrongTaskResponse("1", "错误的字段名", false));
        });
    }

    // ========== 契约测试 ==========

    /**
     * 契约测试 1：验证成功响应的字段名
     *
     * <p>🔴 契约测试能发现的问题：
     * <ul>
     *   <li>字段名拼写错误（如 taskName vs title）</li>
     *   <li>字段类型错误（如字符串 vs 数字）</li>
     *   <li>必填字段缺失</li>
     * </ul>
     */
    @Test
    @DisplayName("契约测试 1: 验证成功响应的字段名")
    void contractTest_successResponseHasCorrectFields() throws Exception {
        // when: 请求任务详情
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/1"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证契约（字段名必须正确）
        assertEquals(200, response.statusCode());

        String json = response.body();

        // ✅ 验证必填字段存在
        assertTrue(json.contains("\"id\":"), "必须包含 id 字段");
        assertTrue(json.contains("\"title\":"), "必须包含 title 字段（不是 taskName）");
        assertTrue(json.contains("\"completed\":"), "必须包含 completed 字段");

        // ✅ 验证字段类型
        assertTrue(json.matches(".*\"id\"\\s*:\\s*\"[^\"]+\".*"), "id 必须是字符串");
        assertTrue(json.matches(".*\"title\"\\s*:\\s*\"[^\"]+\".*"), "title 必须是字符串");
        assertTrue(json.matches(".*\"completed\"\\s*:\\s*(true|false).*"), "completed 必须是布尔值");
    }

    /**
     * 契约测试 2：验证错误响应的字段名
     *
     * <p>🔴 契约测试能发现的问题：
     * <ul>
     *   <li>错误响应字段名不一致</li>
     *   <li>HTTP 状态码不符合 REST 规范</li>
     * </ul>
     */
    @Test
    @DisplayName("契约测试 2: 验证错误响应的字段名")
    void contractTest_errorResponseHasCorrectFields() throws Exception {
        // when: 请求不存在的任务
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/999"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证契约
        assertEquals(404, response.statusCode());

        String json = response.body();

        // ✅ 验证错误字段存在
        assertTrue(json.contains("\"error\":"), "必须包含 error 字段");
    }

    /**
     * 契约测试 3：验证 Content-Type 头
     *
     * <p>🔴 契约测试能发现的问题：
     * <ul>
     *   <li>Content-Type 头缺失</li>
     *   <li>Content-Type 不是 application/json</li>
     * </ul>
     */
    @Test
    @DisplayName("契约测试 3: 验证 Content-Type 头")
    void contractTest_hasCorrectContentType() throws Exception {
        // when: 请求任务详情
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/1"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证 Content-Type
        String contentType = response.headers()
            .firstValue("Content-Type")
            .orElse("");

        assertTrue(contentType.contains("application/json"),
            "Content-Type 必须是 application/json，实际是: " + contentType);
    }

    /**
     * 契约测试 4：检测契约被破坏
     *
     * <p>这个测试演示了当字段名错误时，契约测试如何失败。
     */
    @Test
    @DisplayName("契约测试 4: 检测字段名错误（契约破坏）")
    void contractTest_detectsBrokenContract() throws Exception {
        // when: 请求返回错误字段的端点
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/wrong/1"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 契约测试失败（字段名错误）
        String json = response.body();

        // ❌ 这个断言会失败，因为响应包含 taskName 而不是 title
        assertFalse(json.contains("\"taskName\":"),
            "违反契约：字段名应该是 title，不是 taskName");

        // ✅ 契约要求的字段
        assertTrue(json.contains("\"title\":"),
            "契约要求：必须包含 title 字段");
    }

    /**
     * 契约测试 5：验证必填字段不能为 null
     *
     * <p>🔴 契约测试能发现的问题：
     * <ul>
     *   <li>必填字段为 null</li>
     *   <li>必填字段缺失</li>
     * </ul>
     */
    @Test
    @DisplayName("契约测试 5: 验证必填字段不能为 null")
    void contractTest_requiredFieldsNotNull() throws Exception {
        // when: 请求任务详情
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/1"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证必填字段不为 null
        String json = response.body();

        assertFalse(json.contains("\"id\":null"), "id 不能为 null");
        assertFalse(json.contains("\"title\":null"), "title 不能为 null");
        assertFalse(json.contains("\"completed\":null"), "completed 不能为 null");
    }

    // ========== 请求契约测试 ==========

    /**
     * 契约测试 6：验证请求格式
     *
     * <p>🔴 契约测试能发现的问题：
     * <ul>
     *   <li>请求格式错误</li>
     *   <li>Content-Type 头缺失</li>
     * </ul>
     */
    @Test
    @DisplayName("契约测试 6: 验证请求格式")
    void contractTest_requestFormat() throws Exception {
        // given: 正确的请求格式
        String jsonBody = mapper.writeValueAsString(
            new CreateTaskRequest("新任务", false));

        // when: 发送 POST 请求
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        // then: 验证请求格式
        String contentType = request.headers()
            .firstValue("Content-Type")
            .orElse("");

        assertEquals("application/json", contentType,
            "Content-Type 必须是 application/json");

        // 验证请求体包含必填字段
        assertTrue(jsonBody.contains("\"title\""), "请求必须包含 title 字段");
        assertTrue(jsonBody.contains("\"completed\""), "请求必须包含 completed 字段");
    }

    // ========== 契约破坏检测 ==========

    /**
     * 契约破坏场景 1：字段名变更
     *
     * <p>当后端把 title 改成 taskName 时，前端会崩溃。
     * 契约测试能在开发阶段发现这个问题。
     */
    @Test
    @DisplayName("契约破坏 1: 字段名变更")
    void contractBroken_fieldNameChanged() throws Exception {
        // 场景：后端开发者把 title 改成了 taskName
        // 契约测试会失败，阻止这个变更

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/wrong/1"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        // ✅ 契约测试检测到字段名错误
        assertFalse(json.contains("\"taskName\":"),
            "契约被破坏：字段名应该是 title");
    }

    /**
     * 契约破坏场景 2：字段类型变更
     *
     * <p>当后端把 completed 从布尔值改成字符串时，前端会崩溃。
     * 契约测试能发现类型不匹配。
     */
    @Test
    @DisplayName("契约破坏 2: 字段类型变更")
    void contractBroken_fieldTypeChanged() {
        // 场景：后端把 completed 从布尔值改成了字符串 "true"/"false"
        // 前端会把它当成布尔值，导致逻辑错误

        String wrongJson = "{\"id\":\"1\",\"title\":\"任务\",\"completed\":\"true\"}";
        String correctJson = "{\"id\":\"1\",\"title\":\"任务\",\"completed\":true}";

        // ✅ 契约测试验证类型
        assertTrue(correctJson.matches(".*\"completed\"\\s*:\\s*(true|false).*"),
            "completed 必须是布尔值");
        assertFalse(wrongJson.matches(".*\"completed\"\\s*:\\s*(true|false).*"),
            "completed 不能是字符串");
    }

    // ========== 辅助类 ==========

    /**
     * 任务响应（符合契约）。
     */
    static class TaskResponse {
        String id;
        String title;
        boolean completed;

        TaskResponse(String id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }
    }

    /**
     * 错误响应（符合契约）。
     */
    static class ErrorResponse {
        String error;

        ErrorResponse(String error) {
            this.error = error;
        }
    }

    /**
     * 创建任务请求（符合契约）。
     */
    static class CreateTaskRequest {
        String title;
        boolean completed;

        CreateTaskRequest(String title, boolean completed) {
            this.title = title;
            this.completed = completed;
        }
    }

    /**
     * 错误的任务响应（违反契约）。
     *
     * <p>❌ 这个类使用 taskName 而不是 title，违反契约。
     */
    static class WrongTaskResponse {
        String id;
        String taskName;  // ❌ 错误：应该是 title
        boolean completed;

        WrongTaskResponse(String id, String taskName, boolean completed) {
            this.id = id;
            this.taskName = taskName;
            this.completed = completed;
        }
    }
}

/**
 * 契约测试最佳实践
 *
 * <p>1. **契约定义**
 * <ul>
 *   <li>使用 OpenAPI/Swagger 文档定义 API 规范</li>
 *   <li>文档包含：路径、方法、参数、请求体、响应体、状态码</li>
 *   <li>文档是"活的契约"，不是静态文档</li>
 * </ul>
 *
 * <p>2. **契约测试**
 * <ul>
 *   <li>后端测试：验证"实现符合契约"</li>
 *   <li>前端测试：验证"调用符合契约"</li>
 *   <li>持续集成：每次代码变更都运行契约测试</li>
 * </ul>
 *
 * <p>3. **契约变更流程**
 * <ul>
 *   <li>更新 OpenAPI 文档</li>
 *   <li>更新契约测试</li>
 *   <li>通知前后端团队</li>
 *   <li>同步修改代码</li>
 * </ul>
 *
 * <p>4. **工具支持**
 * <ul>
 *   <li>OpenAPI Generator：根据文档生成代码</li>
 *   <li>Pact：契约测试框架</li>
 *   <li>Postman/Newman：API 测试工具</li>
 * </ul>
 *
 * <p>5. **常见契约问题**
 * <ul>
 *   <li>字段名拼写错误（title vs taskName）</li>
 *   <li>字段类型错误（字符串 vs 数字）</li>
 *   <li>必填字段缺失</li>
 *   <li>HTTP 状态码不正确</li>
 *   <li>Content-Type 头错误</li>
 * </ul>
 */
