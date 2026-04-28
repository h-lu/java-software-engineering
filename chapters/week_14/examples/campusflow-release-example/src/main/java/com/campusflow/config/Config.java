/*
 * 示例：配置管理类
 * 功能：加载多环境配置（开发/测试/生产），支持配置文件和环境变量
 * 运行方式：CAMPUSFLOW_ENV=prod java -jar campusflow-1.0.0.jar
 * 预期输出：加载 config-prod.properties，提供生产环境配置
 */
package com.campusflow.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理类
 * 支持多环境配置（开发/测试/生产）
 *
 * 本例演示：
 * 1. 从环境变量读取环境名称（CAMPUSFLOW_ENV）
 * 2. 加载对应环境的配置文件
 * 3. 提供配置访问方法
 *
 * 运行方式：
 * - 开发环境：CAMPUSFLOW_ENV=dev java -jar campusflow-1.0.0.jar
 * - 生产环境：CAMPUSFLOW_ENV=prod java -jar campusflow-1.0.0.jar
 *
 * 预期输出：
 * - 加载 config-dev.properties 或 config-prod.properties
 * - 提供数据库路径、端口、日志级别等配置
 */
public class Config {
    private final Properties props;
    private final String env;

    public Config() {
        // 从环境变量读取环境名称（dev/test/prod）
        this.env = System.getenv("CAMPUSFLOW_ENV") != null
            ? System.getenv("CAMPUSFLOW_ENV")
            : "dev";  // 默认开发环境

        this.props = loadConfig();

        System.out.println("Environment: " + env);
        System.out.println("Database: " + getDbPath());
        System.out.println("Port: " + getPort());
    }

    private Properties loadConfig() {
        Properties props = new Properties();
        String configFile = "config-" + env + ".properties";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new RuntimeException("Config file not found: " + configFile);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + configFile, e);
        }
        return props;
    }

    public String getDbPath() {
        return props.getProperty("db.path", "campusflow.db");
    }

    public int getPort() {
        return Integer.parseInt(props.getProperty("server.port", "8080"));
    }

    public String getLogLevel() {
        return props.getProperty("log.level", "INFO");
    }

    public String getApiBasePath() {
        return props.getProperty("api.base_path", "/api");
    }

    public String getEnv() {
        return env;
    }
}
