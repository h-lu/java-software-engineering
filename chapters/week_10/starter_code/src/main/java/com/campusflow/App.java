package com.campusflow;

import com.campusflow.dto.TaskRequest;
import com.campusflow.exception.NotFoundException;
import com.campusflow.exception.ValidationException;
import com.campusflow.repository.InMemoryTaskRepository;
import com.campusflow.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class App {
    private static final int PORT = 7070;
    private static final Charset WINDOWS_CHINESE_CHARSET = Charset.forName("GB18030");
    private static final ObjectMapper JSON = new ObjectMapper();

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
        app.get("/health", ctx -> json(ctx, Map.of(
            "service", "CampusFlow",
            "week", "10",
            "status", "backend-base"
        )));

        app.get("/tasks", ctx -> json(ctx, taskService.findAllEnvelope()));

        app.post("/tasks", ctx -> {
            TaskRequest request = readTaskRequest(ctx);
            ctx.status(201);
            json(ctx, taskService.create(request));
        });

        app.get("/tasks/{id}", ctx -> json(ctx, taskService.findById(ctx.pathParam("id"))));

        app.put("/tasks/{id}", ctx -> {
            TaskRequest request = readTaskRequest(ctx);
            json(ctx, taskService.update(ctx.pathParam("id"), request));
        });

        app.delete("/tasks/{id}", ctx -> {
            taskService.delete(ctx.pathParam("id"));
            ctx.status(204);
        });
    }

    private static TaskRequest readTaskRequest(Context ctx) {
        try {
            return JSON.readValue(decodeRequestBody(ctx), TaskRequest.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("请求体不是有效的 JSON，或字段格式不正确");
        }
    }

    private static String decodeRequestBody(Context ctx) {
        byte[] bytes = ctx.bodyAsBytes();
        Charset declaredCharset = charsetFromContentType(ctx.header("Content-Type"));
        if (declaredCharset != null) {
            return new String(bytes, declaredCharset);
        }
        try {
            return decodeStrict(bytes, StandardCharsets.UTF_8);
        } catch (CharacterCodingException e) {
            // Windows cmd / some localized tools may send JSON bytes in GBK/GB18030 without a charset.
            // Fall back to GB18030 so Chinese titles/descriptions are not stored as mojibake.
            return new String(bytes, WINDOWS_CHINESE_CHARSET);
        }
    }

    private static Charset charsetFromContentType(String contentType) {
        if (contentType == null) {
            return null;
        }
        for (String part : contentType.split(";")) {
            String trimmed = part.trim();
            if (trimmed.toLowerCase(Locale.ROOT).startsWith("charset=")) {
                String charsetName = trimmed.substring("charset=".length()).trim().replace("\"", "");
                if (!charsetName.isEmpty()) {
                    return Charset.forName(charsetName);
                }
            }
        }
        return null;
    }

    private static String decodeStrict(byte[] bytes, Charset charset) throws CharacterCodingException {
        return charset.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT)
            .decode(ByteBuffer.wrap(bytes))
            .toString();
    }

    private static void json(Context ctx, Object value) {
        try {
            ctx.contentType("application/json; charset=utf-8");
            ctx.result(JSON.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    private static void registerErrorHandlers(Javalin app) {
        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400);
            json(ctx, errorBody("ValidationError", e.getMessage(), ctx.path()));
        });
        app.exception(NotFoundException.class, (e, ctx) -> {
            ctx.status(404);
            json(ctx, errorBody("NotFound", e.getMessage(), ctx.path()));
        });
        app.exception(BadRequestResponse.class, (e, ctx) -> {
            ctx.status(400);
            json(ctx, errorBody("BadRequest", "请求体不是有效的 JSON，或字段格式不正确", ctx.path()));
        });
        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            json(ctx, errorBody("InternalServerError", e.getMessage(), ctx.path()));
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
