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
        // TODO: Read CAMPUSFLOW_ENV from the environment. Default to dev when it is absent.
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
        // TODO: Allow DB_PATH to override this value in deployed environments.
        return values.getProperty("db.path", "campusflow.db");
    }

    public int port() {
        // TODO: Support values such as ${PORT:8080} and validate the 1..65535 range.
        return Integer.parseInt(values.getProperty("server.port", "8080"));
    }

    public String logLevel() {
        return values.getProperty("log.level", "INFO");
    }
}
