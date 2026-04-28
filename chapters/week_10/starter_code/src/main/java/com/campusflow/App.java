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
        System.out.println("CampusFlow Week 10 starter running at http://localhost:" + PORT);
    }

    public static HttpServer createServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/health", exchange -> {
            // TODO: Replace this placeholder with Javalin CORS configuration if you use this backend.
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            writeJson(exchange, 200, healthJson());
        });
        return server;
    }

    public static String healthJson() {
        return "{\"service\":\"CampusFlow\",\"week\":\"10\",\"status\":\"starter\"}";
    }

    private static void writeJson(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
