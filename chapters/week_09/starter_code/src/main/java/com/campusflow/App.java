/*
 * CampusFlow Web 版入口类。
 *
 * 本文件是 Week 09 作业的参考实现。
 * 学生可以在作业中遇到困难时参考此代码，但建议先尝试自己实现。
 *
 * 运行方式：
 *   mvn -q compile exec:java
 *   或
 *   mvn -q compile exec:java -Dexec.mainClass="com.campusflow.App"
 *
 * 测试命令：
 *   curl http://localhost:7070/health
 *   curl http://localhost:7070/tasks
 *   curl -X POST http://localhost:7070/tasks \
 *     -H "Content-Type: application/json" \
 *     -d '{"title":"新任务","description":"任务描述","dueDate":"2026-02-20"}'
 */
package com.campusflow;

import com.campusflow.controller.TaskController;
import com.campusflow.exception.NotFoundException;
import com.campusflow.exception.ValidationException;
import com.campusflow.repository.InMemoryTaskRepository;
import com.campusflow.repository.TaskRepository;
import com.campusflow.service.TaskService;
import io.javalin.Javalin;

import java.time.LocalDate;
import java.util.List;
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
 */
public class App {

    // 服务端口
    private static final int PORT = 7070;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           CampusFlow Web 服务                            ║");
        System.out.println("║           Week 09 - REST API 实现                        ║");
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
        })
        // 健康检查
        .get("/health", ctx -> ctx.json(Map.of(
            "service", "CampusFlow",
            "version", "2.0.0",
            "status", "UP"
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
            "完成 Week 09 作业",
            "编写 Javalin REST API 代码",
            LocalDate.now().plusDays(3).toString()
        ));

        service.createTask(new com.campusflow.dto.TaskRequest(
            "阅读 RESTful API 设计指南",
            "学习资源命名和 HTTP 方法",
            LocalDate.now().plusDays(5).toString()
        ));

        System.out.println("预置数据加载完成\n");
    }

    /**
     * 打印使用说明。
     */
    private static void printUsage() {
        System.out.println("✅ 服务已启动！");
        System.out.println();
        System.out.println("API 端点：");
        System.out.println("  基础：");
        System.out.println("    GET    /health              - 健康检查");
        System.out.println("    GET    /stats               - 统计信息");
        System.out.println();
        System.out.println("  任务管理：");
        System.out.println("    GET    /tasks               - 获取所有任务");
        System.out.println("    GET    /tasks/{id}          - 获取指定任务");
        System.out.println("    POST   /tasks               - 创建任务");
        System.out.println("    PUT    /tasks/{id}          - 全量更新任务");
        System.out.println("    PATCH  /tasks/{id}          - 部分更新任务");
        System.out.println("    DELETE /tasks/{id}          - 删除任务");
        System.out.println();
        System.out.println("  业务功能：");
        System.out.println("    GET    /tasks/{id}/overdue-fee  - 计算逾期费用");
        System.out.println("    POST   /tasks/{id}/complete     - 标记任务完成");
        System.out.println();
        System.out.println("测试命令：");
        System.out.println("  curl http://localhost:7070/tasks");
        System.out.println("  curl -X POST http://localhost:7070/tasks \\");
        System.out.println("    -H 'Content-Type: application/json' \\");
        System.out.println("    -d '{\"title\":\"新任务\",\"description\":\"描述\",\"dueDate\":\"2026-02-20\"}'");
        System.out.println();
        System.out.println("服务地址: http://localhost:" + PORT);
        System.out.println("按 Ctrl+C 停止服务");
    }
}
