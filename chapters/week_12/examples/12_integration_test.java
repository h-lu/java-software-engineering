/*
 * 示例：集成测试完整示例
 * 运行方式：阅读理解（需 JUnit 5 + Javalin 环境）
 * 预期输出：理解集成测试与单元测试的区别
 *
 * 本例演示：
 * 1. 使用 HttpClient 发送真实 HTTP 请求
 * 2. @BeforeEach/@AfterEach 生命周期管理
 * 3. 测试 REST API 端点
 * 4. 对比单元测试和集成测试的区别
 */
package examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 集成测试示例：测试 REST API 端点
 *
 * <p>集成测试特点：
 * <ul>
 *   <li>启动真实的 Javalin 服务器</li>
 *   <li>发送真实的 HTTP 请求</li>
 *   <li>验证完整的请求-响应链路</li>
 *   <li>发现单元测试发现不了的问题（路由、JSON 序列化、HTTP 状态码）</li>
 * </ul>
 */
class _12_integration_test {

    private static Javalin app;
    private static final int TEST_PORT = 8081;
    private static final String BASE_URL = "http://localhost:" + TEST_PORT;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // ========== 生命周期管理 ==========

    /**
     * @BeforeAll：在所有测试开始前执行一次
     *
     * <p>适合做：启动服务器、创建数据库连接、加载配置
     */
    @BeforeAll
    static void setUpAll() {
        System.out.println("=== 启动测试服务器 ===");

        // 启动 Javalin 服务器
        app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();  // 开发模式日志
        }).start(TEST_PORT);

        // 配置路由
        configureRoutes();

        System.out.println("服务器已启动: http://localhost:" + TEST_PORT);
    }

    /**
     * @AfterAll：在所有测试结束后执行一次
     *
     * <p>适合做：关闭服务器、清理数据库连接
     */
    @AfterAll
    static void tearDownAll() {
        System.out.println("=== 关闭测试服务器 ===");
        if (app != null) {
            app.stop();
        }
        System.out.println("服务器已关闭");
    }

    /**
     * @BeforeEach：在每个测试开始前执行
     *
     * <p>适合做：重置测试数据、准备测试环境
     */
    @BeforeEach
    void setUp() {
        System.out.println("--- 准备测试数据 ---");
        // 清理数据、准备测试环境
        tasks.clear();  // 清空任务列表
    }

    /**
     * @AfterEach：在每个测试结束后执行
     *
     * <p>适合做：验证测试状态、清理临时资源
     */
    @AfterEach
    void tearDown() {
        System.out.println("--- 清理测试环境 ---");
    }

    // ========== 配置路由 ==========

    /**
     * 配置 Javalin 路由。
     *
     * <p>这个方法配置了 REST API 的所有端点。
     */
    private static void configureRoutes() {
        // 健康检查
        app.get("/health", ctx -> ctx.json(Map.of(
            "status", "UP",
            "service", "TaskAPI"
        )));

        // 获取所有任务
        app.get("/api/tasks", ctx -> {
            ctx.json(tasks);
        });

        // 获取单个任务
        app.get("/api/tasks/:id", ctx -> {
            String id = ctx.pathParam("id");
            Task task = findTaskById(id);
            if (task == null) {
                ctx.status(404).json(Map.of("error", "Task not found"));
            } else {
                ctx.json(task);
            }
        });

        // 创建任务
        app.post("/api/tasks", ctx -> {
            Task task = ctx.bodyAsClass(Task.class);
            task.id = String.valueOf(tasks.size() + 1);
            tasks.add(task);
            ctx.status(201).json(task);
        });

        // 更新任务
        app.put("/api/tasks/:id", ctx -> {
            String id = ctx.pathParam("id");
            Task updated = ctx.bodyAsClass(Task.class);
            Task existing = findTaskById(id);
            if (existing == null) {
                ctx.status(404).json(Map.of("error", "Task not found"));
            } else {
                existing.title = updated.title;
                existing.completed = updated.completed;
                ctx.json(existing);
            }
        });

        // 删除任务
        app.delete("/api/tasks/:id", ctx -> {
            String id = ctx.pathParam("id");
            Task existing = findTaskById(id);
            if (existing == null) {
                ctx.status(404).json(Map.of("error", "Task not found"));
            } else {
                tasks.remove(existing);
                ctx.status(204);
            }
        });
    }

    // ========== 测试用例 ==========

    /**
     * 测试 1：健康检查端点
     *
     * <p>最简单的集成测试，验证服务器是否正常启动。
     */
    @Test
    @DisplayName("GET /health - 返回服务状态")
    void healthCheck_returnsUp() throws Exception {
        // when: 发送 GET 请求
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/health"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"status\":\"UP\""));
        assertTrue(response.body().contains("\"service\":\"TaskAPI\""));
    }

    /**
     * 测试 2：获取空任务列表
     *
     * <p>验证 API 能正确返回空数组（不是 null）。
     */
    @Test
    @DisplayName("GET /api/tasks - 初始返回空列表")
    void getTasks_returnsEmptyList_initially() throws Exception {
        // when: 发送 GET 请求
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());  // 空数组，不是 null
    }

    /**
     * 测试 3：创建任务
     *
     * <p>验证 POST 请求和 JSON 序列化。
     *
     * <p>🔴 集成测试能发现的问题：
     * <ul>
     *   <li>Content-Type 头是否正确</li>
     *   <li>JSON 字段名是否匹配</li>
     *   <li>HTTP 状态码是否符合 REST 规范</li>
     * </ul>
     */
    @Test
    @DisplayName("POST /api/tasks - 创建新任务")
    void createTask_returns201AndTask() throws Exception {
        // given: 准备请求体
        String jsonBody = mapper.writeValueAsString(
            new Task(null, "学习集成测试", false));

        // when: 发送 POST 请求
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(201, response.statusCode());  // Created
        assertTrue(response.body().contains("\"title\":\"学习集成测试\""));
        assertTrue(response.body().contains("\"id\":"));
    }

    /**
     * 测试 4：获取创建的任务
     *
     * <p>验证完整链路：创建 → 查询 → 验证
     */
    @Test
    @DisplayName("GET /api/tasks/{id} - 获取已创建的任务")
    void getTask_afterCreating_returnsTask() throws Exception {
        // given: 先创建一个任务
        String jsonBody = mapper.writeValueAsString(
            new Task(null, "集成测试任务", false));

        HttpRequest createRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> createResponse = client.send(createRequest,
            HttpResponse.BodyHandlers.ofString());

        Task created = mapper.readValue(createResponse.body(), Task.class);
        String taskId = created.id;

        // when: 获取刚创建的任务
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/" + taskId))
            .GET()
            .build();

        HttpResponse<String> getResponse = client.send(getRequest,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, getResponse.statusCode());
        assertTrue(getResponse.body().contains("\"title\":\"集成测试任务\""));
    }

    /**
     * 测试 5：获取不存在的任务返回 404
     *
     * <p>🔴 集成测试能发现的问题：
     * <ul>
     *   <li>错误处理是否正确</li>
     *   <li>HTTP 状态码是否符合 REST 规范</li>
     *   <li>错误消息是否友好</li>
     * </ul>
     */
    @Test
    @DisplayName("GET /api/tasks/{id} - 不存在的任务返回 404")
    void getTask_nonExistent_returns404() throws Exception {
        // when: 请求不存在的任务
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/non-existent"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("\"error\":"));
    }

    /**
     * 测试 6：更新任务
     *
     * <p>验证 PUT 请求和部分更新逻辑。
     */
    @Test
    @DisplayName("PUT /api/tasks/{id} - 更新任务")
    void updateTask_returnsUpdatedTask() throws Exception {
        // given: 先创建任务
        Task created = createTaskViaApi("原任务");

        // when: 更新任务
        String updateJson = mapper.writeValueAsString(
            new Task(null, "更新后的任务", true));

        HttpRequest updateRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/" + created.id))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(updateJson))
            .build();

        HttpResponse<String> updateResponse = client.send(updateRequest,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(200, updateResponse.statusCode());
        assertTrue(updateResponse.body().contains("\"title\":\"更新后的任务\""));
    }

    /**
     * 测试 7：删除任务
     *
     * <p>验证 DELETE 请求和 204 状态码。
     */
    @Test
    @DisplayName("DELETE /api/tasks/{id} - 删除任务")
    void deleteTask_returns204() throws Exception {
        // given: 先创建任务
        Task created = createTaskViaApi("待删除任务");

        // when: 删除任务
        HttpRequest deleteRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/" + created.id))
            .DELETE()
            .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest,
            HttpResponse.BodyHandlers.ofString());

        // then: 验证响应
        assertEquals(204, deleteResponse.statusCode());

        // 再次获取应返回 404
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks/" + created.id))
            .GET()
            .build();

        HttpResponse<String> getResponse = client.send(getRequest,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(404, getResponse.statusCode());
    }

    // ========== 对比：单元测试 vs 集成测试 ==========

    /**
     * 单元测试示例（不启动服务器）。
     *
     * <p>单元测试特点：
     * <ul>
     *   <li>直接调用方法，不经过 HTTP</li>
     *   <li>速度更快</li>
     *   <li>但无法发现集成问题（路由、JSON、状态码）</li>
     * </ul>
     */
    @Test
    @DisplayName("单元测试示例：直接调用业务逻辑")
    void unitTest_example() {
        // given
        tasks.add(new Task("1", "单元测试任务", false));

        // when: 直接调用方法，不发送 HTTP 请求
        Task task = findTaskById("1");

        // then: 验证结果
        assertNotNull(task);
        assertEquals("单元测试任务", task.title);

        // ❌ 但这个测试无法发现：
        //    - 路由是否正确注册
        //    - JSON 序列化是否正确
        //    - HTTP 状态码是否正确
    }

    // ========== 辅助方法 ==========

    /**
     * 通过 API 创建任务（辅助方法）。
     */
    private Task createTaskViaApi(String title) throws Exception {
        String jsonBody = mapper.writeValueAsString(
            new Task(null, title, false));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/tasks"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        return mapper.readValue(response.body(), Task.class);
    }

    /**
     * 根据 ID 查找任务（辅助方法）。
     */
    private static Task findTaskById(String id) {
        return tasks.stream()
            .filter(t -> t.id.equals(id))
            .findFirst()
            .orElse(null);
    }

    // ========== 测试数据 ==========

    /** 模拟数据库 */
    private static final List<Task> tasks = new java.util.ArrayList<>();

    /** 任务实体 */
    static class Task {
        String id;
        String title;
        boolean completed;

        Task(String id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }

        // Getters and setters (Jackson 需要公开的字段或 getter)
    }
}

/**
 * 集成测试 vs 单元测试对比表
 *
 * <table>
 *   <tr><th>维度</th><th>单元测试</th><th>集成测试</th></tr>
 *   <tr><td>依赖</td><td>Mock/隔离</td><td>真实服务</td></tr>
 *   <tr><td>速度</td><td>快（毫秒级）</td><td>慢（秒级）</td></tr>
 *   <tr><td>覆盖</td><td>代码逻辑</td><td>系统组装</td></tr>
 *   <tr><td>发现问题</td><td>逻辑错误</td><td>契约问题</td></tr>
 *   <tr><td>维护成本</td><td>低</td><td>高（环境依赖）</td></tr>
 *   <tr><td>示例</td><td>service.getAllTasks()</td><td>HTTP GET /api/tasks</td></tr>
 * </table>
 *
 * <p>集成测试发现的问题类型：
 * <ul>
 *   <li>路由未注册或路径错误</li>
 *   <li>JSON 字段名不匹配</li>
 *   <li>HTTP 状态码不符合 REST 规范</li>
 *   <li>Content-Type 头缺失或错误</li>
 *   <li>请求/响应序列化问题</li>
 * </ul>
 */
