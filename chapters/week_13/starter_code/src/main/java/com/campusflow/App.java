/*
 * CampusFlow Web 版入口类。
 *
 * 本文件是 Week 13 作业的参考实现，展示了完整的文档体系。
 * 学生可以在作业中遇到困难时参考此代码，但建议先尝试自己实现。
 *
 * 运行方式：
 *   mvn -q compile exec:java
 *   或
 *   mvn -q compile exec:java -Dexec.mainClass="com.campusflow.App"
 *
 * 质量检查命令：
 *   mvn test                          - 运行单元测试
 *   mvn test -Dtest=TaskApiIntegrationTest - 运行集成测试
 *   mvn jacoco:report                 - 生成覆盖率报告
 *   mvn spotbugs:check                - 运行静态分析
 *   mvn verify                        - 完整验证（测试 + 覆盖率 + 静态分析）
 *
 * 查看报告：
 *   open target/site/jacoco/index.html
 *   open target/site/spotbugs.html
 *
 * Week 13 新增：
 *   - API 文档示例（examples/13_openapi_spec.yaml）
 *   - README 示例（examples/13_README.md）
 *   - ADR 索引示例（examples/13_adr_index.md）
 */
package com.campusflow;

import com.campusflow.controller.TaskController;
import com.campusflow.exception.NotFoundException;
import com.campusflow.exception.ValidationException;
import com.campusflow.repository.InMemoryTaskRepository;
import com.campusflow.repository.TaskRepository;
import com.campusflow.service.TaskService;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.time.LocalDate;
import java.util.Map;

/**
 * CampusFlow Web 应用程序入口。
 *
 * <p>架构分层：
 * <ul>
 *   <li>App: 应用程序入口，组装各层依赖</li>
 *   <li>Controller: 处理 HTTP 请求/响应</li>
 *   <li>Service: 业务逻辑层</li>
 *   <li>Repository: 数据访问层</li>
 *   <li>Model: 领域模型</li>
 * </ul>
 *
 * <p>版本历史：
 * <ul>
 *   <li>Week 10: CORS 配置，支持前端跨域访问</li>
 *   <li>Week 11: 质量门禁配置（SpotBugs + JaCoCo）</li>
 *   <li>Week 12: 集成测试支持（createApp 方法用于测试）</li>
 *   <li>Week 13: 文档体系完善（API 文档、README、ADR 索引）</li>
 * </ul>
 */
public class App {

    // 服务端口
    private static final int PORT = 7070;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           CampusFlow Web 服务                            ║");
        System.out.println("║           Week 13 - Documentation                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // 1. 创建各层组件（依赖注入）
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(repository);
        TaskController controller = new TaskController(taskService);

        // 2. 预置测试数据
        seedData(taskService);

        // 3. 创建 Javalin 应用
        Javalin app = createApp(controller);

        // 4. 启动服务
        app.start(PORT);

        printUsage();
    }

    /**
     * 创建并配置 Javalin 应用（用于测试）。
     *
     * <p>这个方法供集成测试使用，测试时可以在不同端口启动服务器。
     *
     * @param port 端口号
     * @return Javalin 应用实例
     */
    public static Javalin createApp(int port) {
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(repository);
        TaskController controller = new TaskController(taskService);
        seedData(taskService);

        Javalin app = createApp(controller);
        app.start(port);
        return app;
    }

    /**
     * 创建并配置 Javalin 应用。
     */
    private static Javalin createApp(TaskController controller) {
        return Javalin.create(config -> {
            // 启用开发日志（开发环境）
            config.bundledPlugins.enableDevLogging();

            // 配置 CORS - 允许前端跨域访问
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        })
        // 健康检查
        .get("/health", ctx -> ctx.json(Map.of(
            "service", "CampusFlow",
            "version", "2.4.0",
            "status", "UP",
            "features", Map.of(
                "cors", "enabled",
                "quality_gates", "enabled",
                "integration_tests", "enabled",
                "documentation", "enabled"
            )
        )))

        // 任务管理 API
        .get("/tasks", controller::getAllTasks)
        .get("/tasks/{id}", controller::getTask)
        .post("/tasks", controller::createTask)
        .put("/tasks/{id}", controller::updateTask)
        .patch("/tasks/{id}", controller::patchTask)
        .delete("/tasks/{id}", controller::deleteTask)

        // 业务功能 API
        .get("/tasks/{id}/overdue-fee", controller::calculateOverdueFee)
        .post("/tasks/{id}/complete", controller::completeTask)

        // 统计 API
        .get("/stats", controller::getStats)

        // 异常处理
        .exception(NotFoundException.class, (e, ctx) -> {
            ctx.status(404).json(Map.of(
                "code", 404,
                "message", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        })
        .exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400).json(Map.of(
                "code", 400,
                "message", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        })
        .exception(Exception.class, (e, ctx) -> {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                "code", 500,
                "message", "Internal server error",
                "timestamp", System.currentTimeMillis()
            ));
        });
    }

    /**
     * 预置测试数据。
     */
    private static void seedData(TaskService service) {
        service.createTask(new com.campusflow.dto.TaskRequest(
            "编写 API 文档",
            "使用 OpenAPI 规范描述 CampusFlow 的 REST API",
            LocalDate.now().plusDays(3).toString()
        ));

        service.createTask(new com.campusflow.dto.TaskRequest(
            "完善 README",
            "添加快速开始、功能特性、常见问题等章节",
            LocalDate.now().plusDays(5).toString()
        ));

        service.createTask(new com.campusflow.dto.TaskRequest(
            "汇总 ADR",
            "创建架构决策索引，整理所有设计决策",
            LocalDate.now().plusDays(7).toString()
        ));

        System.out.println("预置数据加载完成\n");
    }

    /**
     * 打印使用说明。
     */
    private static void printUsage() {
        System.out.println("服务已启动！");
        System.out.println();
        System.out.println("Week 13 新增：");
        System.out.println("  - API 文档示例（examples/13_openapi_spec.yaml）");
        System.out.println("  - README 示例（examples/13_README.md）");
        System.out.println("  - ADR 索引示例（examples/13_adr_index.md）");
        System.out.println();
        System.out.println("质量门禁：");
        System.out.println("  覆盖率目标：70%+");
        System.out.println("  静态分析：SpotBugs 高优先级问题为 0");
        System.out.println("  运行检查：mvn verify");
        System.out.println();
        System.out.println("API 端点：");
        System.out.println("  基础：");
        System.out.println("    GET    /health              - 健康检查");
        System.out.println("    GET    /stats               - 统计信息");
        System.out.println();
        System.out.println("  任务管理：");
        System.out.println("    GET    /tasks               - 获取所有任务");
        System.out.println("    POST   /tasks               - 创建任务");
        System.out.println("    DELETE /tasks/{id}          - 删除任务");
        System.out.println();
        System.out.println("  业务功能：");
        System.out.println("    GET    /tasks/{id}/overdue-fee  - 计算逾期费用");
        System.out.println("    POST   /tasks/{id}/complete     - 标记任务完成");
        System.out.println();
        System.out.println("测试命令：");
        System.out.println("  mvn test                              - 运行所有测试");
        System.out.println("  mvn test -Dtest=TaskApiIntegrationTest - 运行集成测试");
        System.out.println();
        System.out.println("质量检查命令：");
        System.out.println("  mvn test                    - 运行单元测试");
        System.out.println("  mvn jacoco:report           - 生成覆盖率报告");
        System.out.println("  mvn spotbugs:check          - 运行静态分析");
        System.out.println("  mvn verify                  - 完整验证");
        System.out.println();
        System.out.println("查看报告：");
        System.out.println("  open target/site/jacoco/index.html");
        System.out.println("  open target/site/spotbugs.html");
        System.out.println();
        System.out.println("文档位置：");
        System.out.println("  API 文档：examples/13_openapi_spec.yaml");
        System.out.println("  README：  examples/13_README.md");
        System.out.println("  ADR 索引：examples/13_adr_index.md");
        System.out.println();
        System.out.println("服务地址: http://localhost:" + PORT);
        System.out.println("按 Ctrl+C 停止服务");
    }
}
