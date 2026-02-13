/*
 * 示例：测试金字塔概念演示
 * 运行方式：阅读理解
 * 预期输出：理解测试策略的平衡
 *
 * 本例演示：
 * 1. 单元测试（底层，多且快）
 * 2. 集成测试（中层，适量）
 * 3. E2E 测试（顶层，少且慢）
 */
package examples;

/**
 * 测试金字塔概念演示
 *
 * <p>测试金字塔是测试策略的经典模型，强调不同层次的测试应该有不同的比例。
 *
 * <pre>
 *           /\
 *          /  \        E2E 测试（10%）
 *         /____\       - 启动真实浏览器
 *        /      \      - 模拟用户操作
 *       /        \     - 速度慢，维护成本高
 *      / 集成测试 \    集成测试（20%）
 *     /  (20%)    \   - 启动真实服务
 *    /______________\  - 测试组件交互
 *   /   单元测试     \ 单元测试（70%）
 *  /    (70%)        \ - 直接调用方法
 * /____________________\ - 速度快，发现问题早
 * </pre>
 *
 * <p>各层测试的特点：
 *
 * <table>
 *   <tr><th>层次</th><th>比例</th><th>速度</th><th>>成本</th><th>发现问题</th></tr>
 *   <tr><td>单元测试</td><td>70%</td><td>快（秒级）</td><td>低</td><td>逻辑错误</td></tr>
 *   <tr><td>集成测试</td><td>20%</td><td>中（分钟级）</td><td>中</td><td>契约问题</td></tr>
 *   <tr><td>E2E 测试</td><td>10%</td><td>慢（10分钟级）</td><td>高</td><td>用户体验问题</td></tr>
 * </table>
 */
public class _12_test_pyramid {

    // ========== 第 1 层：单元测试（70%）==========

    /**
     * 单元测试示例：测试单个方法
     *
     * <p>特点：
     * <ul>
     *   <li>不启动服务</li>
     *   <li>不依赖外部系统</li>
     *   <li>使用 Mock 隔离依赖</li>
     *   <li>速度：毫秒级</li>
     * </ul>
     *
     * <p>适用场景：
     * <ul>
     *   <li>算法和计算逻辑</li>
     *   <li>业务规则验证</li>
     *   <li>边界条件测试</li>
     * </ul>
     */
    static class UnitTestExample {
        /**
         * 测试计算逾期费用的逻辑。
         *
         * <p>单元测试关注：输入 → 输出
         */
        void testCalculateOverdueFee() {
            // given: 准备数据
            Task task = new Task();
            task.setDueDate(java.time.LocalDate.now().minusDays(5));

            // when: 调用方法
            double fee = calculateFee(task);

            // then: 验证结果
            assert fee == 50.0 : "5 天逾期应收费 50 元";
        }

        /**
         * 测试空标题验证。
         *
         * <p>单元测试关注：边界条件
         */
        void testValidateTitle_EmptyTitle_ReturnsFalse() {
            // given
            String title = "";

            // when
            boolean isValid = validateTitle(title);

            // then
            assert !isValid : "空标题应该无效";
        }

        // 辅助方法
        private double calculateFee(Task task) {
            long days = task.getOverdueDays();
            if (days <= 3) return days * 10;
            else if (days <= 7) return 3 * 10 + (days - 3) * 20;
            else return 3 * 10 + 4 * 20 + (days - 7) * 50;
        }

        private boolean validateTitle(String title) {
            return title != null && !title.trim().isEmpty();
        }
    }

    // ========== 第 2 层：集成测试（20%）==========

    /**
     * 集成测试示例：测试组件交互
     *
     * <p>特点：
     * <ul>
     *   <li>启动真实服务</li>
     *   <li>连接真实数据库（或内存数据库）</li>
     *   <li>发送真实 HTTP 请求</li>
     *   <li>速度：分钟级</li>
     * </ul>
     *
     * <p>适用场景：
     * <ul>
     *   <li>API 端点测试</li>
     *   <li>前后端通信</li>
     *   <li>数据持久化</li>
     * </ul>
     */
    static class IntegrationTestExample {
        /**
         * 测试创建任务的 API。
         *
         * <p>集成测试关注：完整链路
         * <pre>
         * HTTP 请求 → Javalin 路由 → Controller → Service → Repository → 响应
         * </pre>
         */
        void testCreateTask_ApiReturns201() throws Exception {
            // given: 启动服务器
            Javalin app = Javalin.create().start(8080);
            configureRoutes(app);

            // when: 发送 HTTP POST 请求
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(
                    "{\"title\":\"新任务\",\"dueDate\":\"2024-12-31\"}"))
                .build();

            java.net.http.HttpResponse<String> response = client.send(request,
                java.net.http.HttpResponse.BodyHandlers.ofString());

            // then: 验证 HTTP 状态码和响应体
            assert response.statusCode() == 201 : "应该返回 201 Created";
            assert response.body().contains("\"title\":\"新任务\"") : "响应应包含任务标题";

            // cleanup
            app.stop();
        }

        /**
         * 测试契约：验证 API 响应格式。
         *
         * <p>集成测试关注：接口契约
         */
        void testGetTask_ApiReturnsCorrectJsonFormat() throws Exception {
            // given: 启动服务器，创建测试数据
            Javalin app = Javalin.create().start(8080);
            configureRoutes(app);

            // when: 发送 HTTP GET 请求
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

            java.net.http.HttpResponse<String> response = client.send(request,
                java.net.http.HttpResponse.BodyHandlers.ofString());

            // then: 验证 JSON 契约（字段名、类型）
            String json = response.body();
            assert json.contains("\"id\":") : "必须包含 id 字段";
            assert json.contains("\"title\":") : "必须包含 title 字段（不是 taskName）";
            assert json.contains("\"status\":") : "必须包含 status 字段";

            // cleanup
            app.stop();
        }

        private void configureRoutes(Javalin app) {
            app.get("/tasks/{id}", ctx -> ctx.json(new Task("1", "任务", "pending")));
            app.post("/tasks", ctx -> ctx.status(201).json(new Task("1", "新任务", "pending")));
        }
    }

    // ========== 第 3 层：E2E 测试（10%）==========

    /**
     * E2E 测试示例：测试完整用户流程
     *
     * <p>特点：
     * <ul>
     *   <li>启动真实浏览器（Selenium、Playwright）</li>
     *   <li>测试完整用户场景</li>
     *   <li>最接近真实用户体验</li>
     *   <li>速度：10分钟级</li>
     * </ul>
     *
     * <p>适用场景：
     * <ul>
     *   <li>关键业务流程（登录、支付）</li>
     *   <li>跨系统集成</li>
     *   <li>UI 交互验证</li>
     * </ul>
     *
     * <p>注意：E2E 测试代码示例需要 Selenium 或 Playwright，
     * 这里用伪代码演示概念。
     */
    static class E2ETestExample {
        /**
         * 测试完整的任务创建流程。
         *
         * <p>E2E 测试关注：用户体验
         * <pre>
         * 1. 打开浏览器
         * 2. 导航到任务列表页面
         * 3. 点击"创建任务"按钮
         * 4. 填写表单
         * 5. 提交
         * 6. 验证任务出现在列表中
         * </pre>
         */
        void testCreateTask_CompleteUserFlow() {
            // given: 打开浏览器
            // WebDriver driver = new ChromeDriver();
            // driver.get("http://localhost:3000");

            // when: 模拟用户操作
            // driver.findElement(By.id("create-task-button")).click();
            // driver.findElement(By.id("title-input")).sendKeys("E2E 测试任务");
            // driver.findElement(By.id("due-date-input")).sendKeys("2024-12-31");
            // driver.findElement(By.id("submit-button")).click();

            // then: 验证结果
            // String pageTitle = driver.findElement(By.cssSelector(".task-list .task:first-child .title")).getText();
            // assertEquals("E2E 测试任务", pageTitle);

            // cleanup
            // driver.quit();
        }
    }

    // ========== 测试比例建议 ==========

    /**
     * 测试金字塔的最佳实践。
     *
     * <p>比例建议：
     * <ul>
     *   <li>单元测试：70% - 快速反馈，早期发现问题</li>
     *   <li>集成测试：20% - 验证组件交互</li>
     *   <li>E2E 测试：10% - 验证关键用户流程</li>
     * </ul>
     *
     * <p>为什么这样分配？
     * <ul>
     *   <li>单元测试成本低、速度快、维护简单</li>
     *   <li>E2E 测试成本高、速度慢、容易失败（脆弱）</li>
     *   <li>集成测试在中间，平衡成本和真实性</li>
     * </ul>
     *
     * <p>常见错误：
     * <ul>
     *   <li>❌ 冰淇淋筒：大量 E2E，少量单元测试（慢、贵、脆弱）</li>
     *   <li>❌ 只写 E2E：测试运行太慢，反馈不及时</li>
     *   <li>✅ 金字塔：单元为主，集成和 E2E 为辅</li>
     * </ul>
     */
    static class TestPyramidGuidelines {
        /**
         * 什么时候写单元测试？
         *
         * <ul>
         *   <li>业务逻辑和算法</li>
         *   <li>边界条件和异常处理</li>
         *   <li>工具类和辅助方法</li>
         * </ul>
         */
        void whenToWriteUnitTest() {
            // 示例：计算逾期费用的逻辑
            double fee = calculateOverdueFee(5);  // 5 天逾期
            assert fee == 50.0;
        }

        /**
         * 什么时候写集成测试？
         *
         * <ul>
         *   <li>API 端点验证</li>
         *   <li>前后端通信</li>
         *   <li>数据库交互</li>
         *   <li>第三方服务集成</li>
         * </ul>
         */
        void whenToWriteIntegrationTest() {
            // 示例：验证 API 返回正确的 JSON 格式
            // send GET /tasks/1
            // verify response contains {"id":"1","title":"..."}
        }

        /**
         * 什么时候写 E2E 测试？
         *
         * <ul>
         *   <li>关键业务流程（支付、注册）</li>
         *   <li>跨系统集成</li>
         *   <li>复杂的 UI 交互</li>
         * </ul>
         */
        void whenToWriteE2ETest() {
            // 示例：用户注册流程
            // 1. 打开注册页面
            // 2. 填写表单
            // 3. 提交
            // 4. 验证登录成功
        }

        private double calculateOverdueFee(long days) {
            if (days <= 3) return days * 10;
            else if (days <= 7) return 3 * 10 + (days - 3) * 20;
            else return 3 * 10 + 4 * 20 + (days - 7) * 50;
        }
    }

    // ========== 辅助类 ==========

    /**
     * Javalin 简化类型（用于演示）。
     */
    static class Javalin {
        static Javalin create() { return new Javalin(); }
        Javalin start(int port) { return this; }
        void stop() {}
        Javalin get(String path, io.javalin.http.Handler handler) { return this; }
        Javalin post(String path, io.javalin.http.Handler handler) { return this; }
    }

    /**
     * Task 实体。
     */
    static class Task {
        String id;
        String title;
        String status;

        Task(String id, String title, String status) {
            this.id = id;
            this.title = title;
            this.status = status;
        }

        long getOverdueDays() { return 5; }
        void setDueDate(java.time.LocalDate date) {}
    }
}

/**
 * 测试金字塔总结
 *
 * <p>核心原则：
 * <ul>
 *   <li>大量单元测试：快速反馈，早期发现问题</li>
 *   <li>适量集成测试：验证组件交互</li>
 *   <li>少量 E2E 测试：验证关键用户流程</li>
 * </ul>
 *
 * <p>记忆口诀：
 * <ul>
 *   <li>单元测试：测"方法对不对"</li>
 *   <li>集成测试：测"接口对不对"</li>
 *   <li>E2E 测试：测"体验对不对"</li>
 * </ul>
 *
 * <p>避免的错误：
 * <ul>
 *   <li>❌ 冰淇淋筒：E2E 太多，单元太少</li>
 *   <li>❌ 只写 E2E：测试太慢，维护困难</li>
 *   <li>✅ 金字塔：单元为主，逐步向上</li>
 * </ul>
 */
