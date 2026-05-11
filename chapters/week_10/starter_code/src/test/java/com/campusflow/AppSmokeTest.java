package com.campusflow;

import io.javalin.Javalin;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppSmokeTest {
    @Test
    void backendBaseExposesHealthAndTasksApi() throws Exception {
        Javalin app = App.createApp().start(0);
        try {
            String baseUrl = "http://localhost:" + app.port();
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> health = client.send(
                HttpRequest.newBuilder(URI.create(baseUrl + "/health")).GET().build(),
                HttpResponse.BodyHandlers.ofString()
            );
            assertEquals(200, health.statusCode());
            assertTrue(health.body().contains("backend-base"));

            HttpResponse<String> created = client.send(
                HttpRequest.newBuilder(URI.create(baseUrl + "/tasks"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("""
                        {"title":"审查 AI 代码","description":"检查幻觉 API","dueDate":"2026-05-11"}
                        """))
                    .build(),
                HttpResponse.BodyHandlers.ofString()
            );
            assertEquals(201, created.statusCode());
            assertTrue(created.body().contains("审查 AI 代码"));

            HttpResponse<String> tasks = client.send(
                HttpRequest.newBuilder(URI.create(baseUrl + "/tasks")).GET().build(),
                HttpResponse.BodyHandlers.ofString()
            );
            assertEquals(200, tasks.statusCode());
            assertTrue(tasks.body().contains("\"data\""));
            assertTrue(tasks.body().contains("\"total\":1"));
        } finally {
            app.stop();
        }
    }
}
