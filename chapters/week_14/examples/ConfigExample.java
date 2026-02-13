package examples;

import com.campusflow.config.Config;

/**
 * ConfigExample - 环境配置示例
 *
 * 展示多环境配置管理
 */
public class ConfigExample {

    public static void main(String[] args) {
        System.out.println("=== 环境配置管理示例 ===\n");

        // 示例 1: 开发环境配置
        System.out.println("【开发环境】");
        Config devConfig = new Config("dev");
        printConfig(devConfig);
        System.out.println();

        // 示例 2: 测试环境配置
        System.out.println("【测试环境】");
        Config testConfig = new Config("test");
        printConfig(testConfig);
        System.out.println();

        // 示例 3: 生产环境配置
        System.out.println("【生产环境】");
        Config prodConfig = new Config("prod");
        printConfig(prodConfig);
        System.out.println();

        // 示例 4: 配置验证
        System.out.println("【配置验证】");
        System.out.println("开发环境配置验证: " + devConfig.validate());
        System.out.println("生产环境配置验证: " + prodConfig.validate());
        System.out.println();

        // 示例 5: 环境差异对比
        System.out.println("【环境差异对比】");
        System.out.println("数据库路径:");
        System.out.println("  dev:  " + devConfig.getDbPath());
        System.out.println("  test: " + testConfig.getDbPath());
        System.out.println("  prod: " + prodConfig.getDbPath());
        System.out.println();
        System.out.println("端口号:");
        System.out.println("  dev:  " + devConfig.getPort());
        System.out.println("  test: " + testConfig.getPort());
        System.out.println("  prod: " + prodConfig.getPort());
        System.out.println();
        System.out.println("日志级别:");
        System.out.println("  dev:  " + devConfig.getLogLevel());
        System.out.println("  test: " + testConfig.getLogLevel());
        System.out.println("  prod: " + prodConfig.getLogLevel());

        // 示例 6: 12-Factor App 原则
        System.out.println("\n【12-Factor App 原则】");
        System.out.println("配置可以通过环境变量覆盖:");
        System.out.println("  export CAMPUSFLOW_ENV=prod");
        System.out.println("  export DB_PATH=/custom/path/db.db");
        System.out.println("  export SERVER_PORT=3000");
        System.out.println("环境变量优先于配置文件中的值");
    }

    private static void printConfig(Config config) {
        System.out.println("环境: " + config.getEnv());
        System.out.println("数据库路径: " + config.getDbPath());
        System.out.println("端口: " + config.getPort());
        System.out.println("日志级别: " + config.getLogLevel());
    }
}
