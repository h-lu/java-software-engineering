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

@Disabled("Acceptance template: implement the Week 09 REST contract, then remove @Disabled.")
class TaskApiContractTemplateTest {
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
    void getTasksShouldReturnEnvelopeWithTotal() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + "/tasks"))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"data\""));
        assertTrue(response.body().contains("\"total\""));
    }

    @Test
    void createTaskShouldRejectBlankTitleWithErrorBody() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"\"}"))
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("\"error\""));
        assertTrue(response.body().contains("\"message\""));
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }
}
