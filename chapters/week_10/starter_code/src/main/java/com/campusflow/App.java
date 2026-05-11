package com.campusflow;

import com.campusflow.dto.TaskRequest;
import com.campusflow.exception.NotFoundException;
import com.campusflow.exception.ValidationException;
import com.campusflow.repository.InMemoryTaskRepository;
import com.campusflow.service.TaskService;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.util.Map;

public class App {
    private static final int PORT = 7070;

    public static void main(String[] args) {
        createApp().start(PORT);
        System.out.println("CampusFlow Week 10 backend base running at http://localhost:" + PORT);
    }

    public static Javalin createApp() {
        return createApp(new TaskService(new InMemoryTaskRepository()));
    }

    public static Javalin createApp(TaskService taskService) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                // 教学环境默认允许本机 file:// / localhost 前端访问。
                // 生产环境请改成 allowHost("https://your-domain.example")。
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        });

        registerErrorHandlers(app);
        registerRoutes(app, taskService);
        return app;
    }

    private static void registerRoutes(Javalin app, TaskService taskService) {
        app.get("/health", ctx -> ctx.json(Map.of(
            "service", "CampusFlow",
            "week", "10",
            "status", "backend-base"
        )));

        app.get("/tasks", ctx -> ctx.json(taskService.findAllEnvelope()));

        app.post("/tasks", ctx -> {
            TaskRequest request = ctx.bodyAsClass(TaskRequest.class);
            ctx.status(201).json(taskService.create(request));
        });

        app.get("/tasks/{id}", ctx -> ctx.json(taskService.findById(ctx.pathParam("id"))));

        app.put("/tasks/{id}", ctx -> {
            TaskRequest request = ctx.bodyAsClass(TaskRequest.class);
            ctx.json(taskService.update(ctx.pathParam("id"), request));
        });

        app.delete("/tasks/{id}", ctx -> {
            taskService.delete(ctx.pathParam("id"));
            ctx.status(204);
        });
    }

    private static void registerErrorHandlers(Javalin app) {
        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400).json(errorBody("ValidationError", e.getMessage(), ctx.path()));
        });
        app.exception(NotFoundException.class, (e, ctx) -> {
            ctx.status(404).json(errorBody("NotFound", e.getMessage(), ctx.path()));
        });
        app.exception(BadRequestResponse.class, (e, ctx) -> {
            ctx.status(400).json(errorBody("BadRequest", "请求体不是有效的 JSON，或字段格式不正确", ctx.path()));
        });
        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500).json(errorBody("InternalServerError", e.getMessage(), ctx.path()));
        });
    }

    private static Map<String, String> errorBody(String error, String message, String path) {
        return Map.of(
            "error", error,
            "message", message,
            "path", path
        );
    }
}
