package com.campusflow;

import com.campusflow.config.Config;
import com.campusflow.util.Version;

/**
 * Main - CampusFlow 应用入口
 *
 * Week 14: 发布与部署
 * - 版本管理
 * - 环境配置管理
 */
public class Main {
    public static void main(String[] args) {
        // 显示版本信息
        Version version = Version.parse("1.0.0");
        System.out.println("CampusFlow v" + version);

        // 加载配置
        Config config = new Config();
        config.validate();

        System.out.println("Environment: " + config.getEnv());
        System.out.println("Database: " + config.getDbPath());
        System.out.println("Port: " + config.getPort());
        System.out.println("Log Level: " + config.getLogLevel());

        System.out.println("\nCampusFlow is ready!");
    }
}
