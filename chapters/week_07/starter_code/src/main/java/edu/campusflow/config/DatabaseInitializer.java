package edu.campusflow.config;

public class DatabaseInitializer {
    private final String url;

    public DatabaseInitializer(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Database URL 不能为空");
        }
        this.url = url;
    }

    public void initialize() {
        // 待办：从 classpath 读取 schema.sql，并用 try-with-resources 执行每条 SQL。
        throw new UnsupportedOperationException("待办：为该 URL 实现 initialize：" + url);
    }
}
