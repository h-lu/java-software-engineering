package com.campusflow.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * 数据库初始化工具
 * 负责执行 schema.sql 和 data.sql 初始化数据库
 */
public class DatabaseInitializer {
    private final String url;

    public DatabaseInitializer(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("数据库 URL 不能为空");
        }
        this.url = url;
    }

    /**
     * 初始化数据库：创建表并插入初始数据
     */
    public void initialize() {
        executeScript("schema.sql");
        executeScript("data.sql");
    }

    /**
     * 仅创建表结构
     */
    public void initializeSchema() {
        executeScript("schema.sql");
    }

    /**
     * 从 classpath 读取并执行 SQL 脚本
     */
    private void executeScript(String resourceName) {
        try (Connection conn = DriverManager.getConnection(url)) {
            // 从 classpath 读取 SQL 文件
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
            if (is == null) {
                // 如果文件不存在，跳过（data.sql 可能是可选的）
                if (resourceName.equals("data.sql")) {
                    return;
                }
                throw new RuntimeException("找不到资源文件: " + resourceName);
            }

            String sql = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // 使用单个 Statement 执行所有 SQL，确保顺序执行
            try (Statement stmt = conn.createStatement()) {
                // 分割并执行每条语句
                StringBuilder currentStatement = new StringBuilder();
                for (String line : sql.split("\n")) {
                    String trimmed = line.trim();
                    // 跳过空行和注释
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                        continue;
                    }
                    currentStatement.append(line).append("\n");
                    // 如果遇到分号，执行当前语句
                    if (trimmed.endsWith(";")) {
                        String statement = currentStatement.toString().trim();
                        if (!statement.isEmpty()) {
                            stmt.execute(statement);
                        }
                        currentStatement = new StringBuilder();
                    }
                }
                // 执行最后一条语句（如果没有以分号结尾）
                String lastStatement = currentStatement.toString().trim();
                if (!lastStatement.isEmpty()) {
                    stmt.execute(lastStatement);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("执行 " + resourceName + " 失败: " + e.getMessage(), e);
        }
    }
}
