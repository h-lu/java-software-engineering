package com.campusflow.contract;

import com.campusflow.App;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Acceptance template: remove @Disabled if you want to run the CORS contract manually.")
class CorsContractTemplateTest {
    private Javalin app;
    private int port;

    @BeforeEach
    void setUp() {
        app = App.createApp().start(0);
        port = app.port();
    }

    @AfterEach
    void tearDown() {
        if (app != null) {
            app.stop();
        }
    }

    @Test
    void healthEndpointShouldExposeCorsHeaderForFrontendOrigin() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + "/health"))
                .header("Origin", "http://localhost:3000")
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode());
        assertEquals("*", response.headers().firstValue("Access-Control-Allow-Origin").orElse(""));
    }

    @Test
    void tasksEndpointShouldShareTheSameCorsPolicy() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + "/tasks"))
                .header("Origin", "http://localhost:3000")
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertTrue(response.statusCode() < 500, "至少要让 /tasks 端点存在并返回可解释的 HTTP 状态码");
        assertEquals("*", response.headers().firstValue("Access-Control-Allow-Origin").orElse(""));
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }
}
