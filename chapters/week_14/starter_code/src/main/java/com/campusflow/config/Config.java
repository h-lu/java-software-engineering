package com.campusflow.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class Config {

    private final String env;
    private final Properties values;

    private Config(String env, Properties values) {
        this.env = env;
        this.values = values;
    }

    public static Config loadDefault() {
        // 待办：从环境变量读取 CAMPUSFLOW_ENV；未设置时默认使用 dev。
        return loadFor("dev");
    }

    public static Config loadFor(String env) {
        Objects.requireNonNull(env, "env");
        String resourceName = "config-" + env + ".properties";
        Properties properties = new Properties();

        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IllegalArgumentException("Missing config resource: " + resourceName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load " + resourceName, e);
        }

        return new Config(env, properties);
    }

    public String env() {
        return env;
    }

    public String databasePath() {
        // 待办：部署环境中允许 DB_PATH 覆盖这个值。
        return values.getProperty("db.path", "campusflow.db");
    }

    public int port() {
        // 待办：支持 ${PORT:8080} 这类写法，并校验端口范围 1..65535。
        return Integer.parseInt(values.getProperty("server.port", "8080"));
    }

    public String logLevel() {
        return values.getProperty("log.level", "INFO");
    }
}
