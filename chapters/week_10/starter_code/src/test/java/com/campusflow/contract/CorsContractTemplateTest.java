package com.campusflow.contract;

import com.campusflow.App;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Acceptance template: wire standalone CORS and /tasks support, then remove @Disabled.")
class CorsContractTemplateTest {
    private HttpServer server;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        server = App.createServer(0);
        server.start();
        port = server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
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

        assertTrue(response.statusCode() < 500, "实现后至少要让 /tasks 端点存在并返回可解释的 HTTP 状态码");
        assertEquals("*", response.headers().firstValue("Access-Control-Allow-Origin").orElse(""));
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }
}
