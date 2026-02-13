package com.campusflow.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config - 环境配置管理类
 *
 * 支持多环境配置（dev/test/prod）
 * 从配置文件或环境变量加载配置
 */
public class Config {
    private final Properties props;
    private final String env;

    /**
     * 构造函数：从环境变量 CAMPUSFLOW_ENV 读取环境名称
     * 默认为 "dev"
     */
    public Config() {
        this.env = System.getenv().getOrDefault("CAMPUSFLOW_ENV", "dev");
        this.props = loadConfig();
    }

    /**
     * 构造函数：指定环境名称
     *
     * @param env 环境名称（dev/test/prod）
     */
    public Config(String env) {
        if (env == null || env.trim().isEmpty()) {
            throw new IllegalArgumentException("Environment name cannot be null or empty");
        }
        this.env = env;
        this.props = loadConfig();
    }

    /**
     * 加载配置文件
     *
     * @return Properties 对象
     * @throws RuntimeException 如果配置文件不存在或加载失败
     */
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

    /**
     * 获取数据库路径
     * 优先从环境变量读取，否则使用配置文件中的值
     *
     * @return 数据库文件路径
     */
    public String getDbPath() {
        return System.getenv().getOrDefault("DB_PATH",
                props.getProperty("db.path", "campusflow.db"));
    }

    /**
     * 获取服务器端口
     * 优先从环境变量读取，否则使用配置文件中的值
     *
     * @return 端口号
     */
    public int getPort() {
        String portStr = System.getenv().getOrDefault("SERVER_PORT",
                props.getProperty("server.port", "8080"));
        try {
            int port = Integer.parseInt(portStr);
            validatePort(port);
            return port;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid port number: " + portStr, e);
        }
    }

    /**
     * 获取日志级别
     *
     * @return 日志级别（DEBUG/INFO/WARN/ERROR）
     */
    public String getLogLevel() {
        return props.getProperty("log.level", "INFO");
    }

    /**
     * 获取当前环境名称
     *
     * @return 环境名称（dev/test/prod）
     */
    public String getEnv() {
        return env;
    }

    /**
     * 验证端口号是否有效
     *
     * @param port 端口号
     * @throws IllegalArgumentException 如果端口号无效
     */
    private void validatePort(int port) {
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException(
                    "Port must be between 1 and 65535, got: " + port);
        }
    }

    /**
     * 验证配置是否有效
     *
     * @return true 如果配置有效
     * @throws IllegalStateException 如果配置无效
     */
    public boolean validate() {
        String dbPath = getDbPath();
        if (dbPath == null || dbPath.trim().isEmpty()) {
            throw new IllegalStateException("Database path cannot be empty");
        }

        int port = getPort();
        // Port validation is already done in getPort()

        return true;
    }
}
