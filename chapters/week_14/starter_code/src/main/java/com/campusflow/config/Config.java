package com.campusflow.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class Config {

    private final String env;
    private final Properties values;

    public Config() {
        this(System.getenv().getOrDefault("CAMPUSFLOW_ENV", "dev"));
    }

    public Config(String env) {
        this(env, loadProperties(env));
    }

    private Config(String env, Properties values) {
        this.env = env;
        this.values = values;
    }

    public static Config loadDefault() {
        return new Config();
    }

    public static Config loadFor(String env) {
        Objects.requireNonNull(env, "env");
        return new Config(env, loadProperties(env));
    }

    private static Properties loadProperties(String env) {
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

        return properties;
    }

    public String env() {
        return env;
    }

    public String databasePath() {
        return System.getenv().getOrDefault("DB_PATH", values.getProperty("db.path", "campusflow.db"));
    }

    public int port() {
        String configured = values.getProperty("server.port", "8080");
        int parsed = Integer.parseInt(resolvePlaceholder(configured));
        if (parsed < 1 || parsed > 65535) {
            throw new IllegalArgumentException("server.port must be between 1 and 65535: " + parsed);
        }
        return parsed;
    }

    public String logLevel() {
        return values.getProperty("log.level", "INFO");
    }

    public String getEnv() {
        return env();
    }

    public String getDbPath() {
        return databasePath();
    }

    public int getPort() {
        return port();
    }

    private static String resolvePlaceholder(String value) {
        if (!value.startsWith("${") || !value.endsWith("}")) {
            return value;
        }

        String expression = value.substring(2, value.length() - 1);
        int separator = expression.indexOf(':');
        if (separator < 1) {
            throw new IllegalArgumentException("Invalid placeholder: " + value);
        }

        String envName = expression.substring(0, separator);
        String defaultValue = expression.substring(separator + 1);
        return System.getenv().getOrDefault(envName, defaultValue);
    }
}
