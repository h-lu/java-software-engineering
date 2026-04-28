package edu.campusflow.config;

public class DatabaseInitializer {
    private final String url;

    public DatabaseInitializer(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Database URL cannot be blank");
        }
        this.url = url;
    }

    public void initialize() {
        // TODO: read schema.sql from the classpath and execute each SQL statement with try-with-resources.
        throw new UnsupportedOperationException("TODO: implement initialize for " + url);
    }
}
