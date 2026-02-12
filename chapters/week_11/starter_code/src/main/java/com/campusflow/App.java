/*
 * CampusFlow Web 版入口类。
 *
 * 本文件是 Week 11 作业的参考实现，增加了质量门禁配置。
 * 学生可以在作业中遇到困难时参考此代码，但建议先尝试自己实现。
 *
 * 运行方式：
 *   mvn -q compile exec:java
 *   或
 *   mvn -q compile exec:java -Dexec.mainClass="com.campusflow.App"
 *
 * 质量检查命令（Week 11 新增）：
 *   mvn test                          - 运行单元测试
 *   mvn jacoco:report                 - 生成覆盖率报告
 *   mvn spotbugs:check                - 运行静态分析
 *   mvn verify                        - 完整验证（测试 + 覆盖率 + 静态分析）
 *
 * 查看报告：
 *   open target/site/jacoco/index.html
 *   open target/site/spotbugs.html
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
 * </ul>
 */
public class App {

    // 服务端口
    private static final int PORT = 7070;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           CampusFlow Web 服务                            ║");
        System.out.println("║           Week 11 - Quality Gates                        ║");
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
            "version", "2.2.0",
            "status", "UP",
            "features", Map.of(
                "cors", "enabled",
                "quality_gates", "enabled"
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
            "完成 Week 11 作业",
            "配置 SpotBugs 和 JaCoCo 质量门禁",
            LocalDate.now().plusDays(3).toString()
        ));

        service.createTask(new com.campusflow.dto.TaskRequest(
            "修复 SpotBugs 报告的高优先级问题",
            "添加 null 检查和防御式编程",
            LocalDate.now().plusDays(5).toString()
        ));

        System.out.println("预置数据加载完成\n");
    }

    /**
     * 打印使用说明。
     */
    private static void printUsage() {
        System.out.println("服务已启动！");
        System.out.println();
        System.out.println("质量门禁（Week 11 新增）：");
        System.out.println("  覆盖率目标：70%+");
        System.out.println("  静态分析：SpotBugs 高优先级问题为 0");
        System.out.println("  运行检查：mvn verify");
        System.out.println();
        System.out.println("CORS 配置：");
        System.out.println("  - 允许任何 Origin（开发模式）");
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
        System.out.println("  curl http://localhost:7070/tasks");
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
        System.out.println("服务地址: http://localhost:" + PORT);
        System.out.println("按 Ctrl+C 停止服务");
    }
}
