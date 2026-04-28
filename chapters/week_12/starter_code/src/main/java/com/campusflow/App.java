package com.campusflow;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {
    private static final int PORT = 7070;

    public static void main(String[] args) throws IOException {
        HttpServer server = createServer(PORT);
        server.start();
        System.out.println("CampusFlow Week 12 starter running at http://localhost:" + PORT);
    }

    public static HttpServer createServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/health", exchange -> writeJson(exchange, 200, healthJson()));
        server.createContext("/api/tasks", exchange -> writeJson(exchange, 501, tasksPlaceholderJson()));
        return server;
    }

    public static String healthJson() {
        return "{\"service\":\"CampusFlow\",\"week\":\"12\",\"status\":\"starter\"}";
    }

    public static String tasksPlaceholderJson() {
        return "{\"error\":\"TODO\",\"message\":\"Implement this route with Javalin for integration tests\"}";
    }

    private static void writeJson(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
