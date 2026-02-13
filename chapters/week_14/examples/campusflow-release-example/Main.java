/*
 * 示例：CampusFlow 主类
 * 功能：启动 Javalin Web 服务器，注册 REST API 端点
 * 运行方式：java -jar campusflow-1.0.0.jar
 * 预期输出：Server started on port 8080
 */
package com.campusflow;

import com.campusflow.config.Config;
import com.campusflow.api.TaskApi;
import com.campusflow.repository.TaskRepository;
import io.javalin.Javalin;

/**
 * CampusFlow 主类
 *
 * 本例演示：
 * 1. 使用 Config 类加载环境配置
 * 2. 启动 Javalin Web 服务器
 * 3. 注册 REST API 端点
 * 4. 支持开发和生产环境配置
 *
 * 运行方式：
 * - 开发环境：java -jar campusflow-1.0.0.jar
 * - 生产环境：CAMPUSFLOW_ENV=prod java -jar campusflow-1.0.0.jar
 *
 * 预期输出：
 * - Server started on port 8080
 * - 访问 http://localhost:8080/api/tasks
 */
public class Main {
    public static void main(String[] args) {
        // 加载配置
        Config config = new Config();

        // 初始化 Repository
        TaskRepository taskRepository = new TaskRepository(config.getDbPath());

        // 创建 Javalin 应用
        Javalin app = Javalin.create(javalinConfig -> {
            // 启用 CORS（开发环境）
            if ("dev".equals(config.getEnv())) {
                javalinConfig.plugins.enableCors(cors -> {
                    cors.add(it -> {
                        it.allowHost("http://localhost:3000");
                        it.allowHost("http://localhost:8080");
                    });
                });
            }
        });

        // 启动服务器
        app.start(config.getPort());

        // 注册 API 端点
        TaskApi taskApi = new TaskApi(taskRepository);
        taskApi.registerRoutes(app, config.getApiBasePath());

        // 健康检查端点
        app.get("/api/health", ctx -> {
            ctx.result("{\"status\":\"ok\",\"version\":\"1.0.0\",\"env\":\"" + config.getEnv() + "\"}");
        });

        System.out.println("Server started on port " + config.getPort());
        System.out.println("Environment: " + config.getEnv());
        System.out.println("API base path: " + config.getApiBasePath());
    }
}
