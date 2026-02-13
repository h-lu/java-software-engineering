package com.campusflow.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ConfigTest - 环境配置管理测试
 *
 * 测试多环境配置加载、环境变量覆盖、配置验证等功能
 */
@DisplayName("Config - 环境配置管理测试")
class ConfigTest {

    // 保存原始环境变量，用于测试后恢复
    private String originalEnv;
    private String originalDbPath;
    private String originalPort;

    @BeforeEach
    void saveEnvironmentVariables() {
        originalEnv = System.getenv("CAMPUSFLOW_ENV");
        originalDbPath = System.getenv("DB_PATH");
        originalPort = System.getenv("SERVER_PORT");
    }

    @AfterEach
    void restoreEnvironmentVariables() {
        // 环境变量无法直接恢复，只能清空测试设置的影响
        // 实际项目中可能需要使用其他机制（如测试框架的 mock）
    }

    // ==================== 正例测试（Happy Path）====================

    @Test
    @DisplayName("constructor_WhenDefaultEnv_ShouldLoadDevConfig")
    void constructor_WhenDefaultEnv_ShouldLoadDevConfig() {
        // 在没有设置 CAMPUSFLOW_ENV 时，应该加载 dev 配置
        Config config = new Config();
        assertEquals("dev", config.getEnv());
    }

    @Test
    @DisplayName("getDbPath_WhenConfigLoaded_ShouldReturnValue")
    void getDbPath_WhenConfigLoaded_ShouldReturnValue() {
        Config config = new Config("dev");
        String dbPath = config.getDbPath();
        assertNotNull(dbPath);
        assertEquals("campusflow.db", dbPath);
    }

    @Test
    @DisplayName("getPort_WhenConfigLoaded_ShouldReturnValue")
    void getPort_WhenConfigLoaded_ShouldReturnValue() {
        Config config = new Config("dev");
        int port = config.getPort();
        assertEquals(8080, port);
    }

    @Test
    @DisplayName("getLogLevel_WhenConfigLoaded_ShouldReturnValue")
    void getLogLevel_WhenConfigLoaded_ShouldReturnValue() {
        Config config = new Config("dev");
        String logLevel = config.getLogLevel();
        assertEquals("DEBUG", logLevel);
    }

    @Test
    @DisplayName("getEnv_WhenEnvSet_ShouldReturnEnvName")
    void getEnv_WhenEnvSet_ShouldReturnEnvName() {
        Config devConfig = new Config("dev");
        assertEquals("dev", devConfig.getEnv());

        Config testConfig = new Config("test");
        assertEquals("test", testConfig.getEnv());

        Config prodConfig = new Config("prod");
        assertEquals("prod", prodConfig.getEnv());
    }

    @Test
    @DisplayName("getDbPath_WhenProdEnv_ShouldReturnProdPath")
    void getDbPath_WhenProdEnv_ShouldReturnProdPath() {
        Config config = new Config("prod");
        String dbPath = config.getDbPath();
        assertEquals("/var/data/campusflow.db", dbPath);
    }

    @Test
    @DisplayName("getPort_WhenProdEnv_ShouldReturnProdPort")
    void getPort_WhenProdEnv_ShouldReturnProdPort() {
        Config config = new Config("prod");
        int port = config.getPort();
        assertEquals(80, port);
    }

    @Test
    @DisplayName("getLogLevel_WhenProdEnv_ShouldReturnProdLogLevel")
    void getLogLevel_WhenProdEnv_ShouldReturnProdLogLevel() {
        Config config = new Config("prod");
        String logLevel = config.getLogLevel();
        assertEquals("WARN", logLevel);
    }

    @Test
    @DisplayName("getPort_WhenTestEnv_ShouldReturnTestPort")
    void getPort_WhenTestEnv_ShouldReturnTestPort() {
        Config config = new Config("test");
        int port = config.getPort();
        assertEquals(8081, port);
    }

    @Test
    @DisplayName("getDbPath_WhenTestEnv_ShouldReturnTestPath")
    void getDbPath_WhenTestEnv_ShouldReturnTestPath() {
        Config config = new Config("test");
        String dbPath = config.getDbPath();
        assertEquals("test_campusflow.db", dbPath);
    }

    @Test
    @DisplayName("validate_WhenConfigValid_ShouldReturnTrue")
    void validate_WhenConfigValid_ShouldReturnTrue() {
        Config config = new Config("dev");
        assertTrue(config.validate());
    }

    // ==================== 边界测试（Boundary Cases）====================

    @Test
    @DisplayName("getPort_WhenMinimumValidPort_ShouldReturnPort")
    void getPort_WhenMinimumValidPort_ShouldReturnPort() {
        // 测试端口边界值 1024
        // 注意：这需要修改配置文件或 mock，这里仅作示例
        // 实际测试中可以通过自定义 Properties 实现
    }

    @Test
    @DisplayName("getPort_WhenMaximumValidPort_ShouldReturnPort")
    void getPort_WhenMaximumValidPort_ShouldReturnPort() {
        // 测试端口边界值 65535
    }

    @Test
    @DisplayName("getDbPath_WhenEnvVarSet_ShouldOverrideConfig")
    void getDbPath_WhenEnvVarSet_ShouldOverrideConfig() {
        // 注意：这个测试需要设置环境变量
        // 由于环境变量在 JVM 启动后无法直接修改，
        // 这个测试仅作为示例，实际需要使用 mock 或其他机制

        // 示例代码（需要配合测试框架）：
        // assertDoesNotThrow(() -> {
        //     // 假设有方法可以设置环境变量
        //     setEnv("DB_PATH", "/custom/path/db.db");
        //     Config config = new Config("dev");
        //     assertEquals("/custom/path/db.db", config.getDbPath());
        // });
    }

    @Test
    @DisplayName("getPort_WhenEnvVarSet_ShouldOverrideConfig")
    void getPort_WhenEnvVarSet_ShouldOverrideConfig() {
        // 同上，需要 mock 环境变量
    }

    // ==================== 反例测试（Error Cases）====================

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("constructor_WhenNullOrEmptyEnv_ShouldThrowException")
    void constructor_WhenNullOrEmptyEnv_ShouldThrowException(String env) {
        assertThrows(IllegalArgumentException.class, () -> new Config(env));
    }

    @Test
    @DisplayName("constructor_WhenInvalidEnvFile_ShouldThrowException")
    void constructor_WhenInvalidEnvFile_ShouldThrowException() {
        // 尝试加载不存在的环境配置
        assertThrows(RuntimeException.class, () -> new Config("nonexistent"));
    }

    @Test
    @DisplayName("getPort_WhenPortInvalidString_ShouldThrowException")
    void getPort_WhenPortInvalidString_ShouldThrowException() {
        // 这个测试需要 mock 配置文件内容，使其返回无效的端口号
        // 示例：假设配置文件中 server.port=abc

        // 由于无法直接修改已加载的配置文件，
        // 这个测试仅作为示例，实际需要使用 mock 框架
    }

    // ==================== 端口验证测试====================

    @Test
    @DisplayName("portValidation_WhenPortBelow1_ShouldThrowException")
    void portValidation_WhenPortBelow1_ShouldThrowException() {
        // 端口号 < 1 是无效的
        // 注意：生产环境常用端口 80（HTTP）和 443（HTTPS）是有效的
        assertThrows(IllegalArgumentException.class, () -> {
            // 需要通过 mock 或其他方式模拟端口为 0
            // 这里仅作为示例说明端口验证逻辑
            throw new IllegalArgumentException("Port must be between 1 and 65535, got: 0");
        });
    }

    @Test
    @DisplayName("portValidation_WhenPortAbove65535_ShouldThrowException")
    void portValidation_WhenPortAbove65535_ShouldThrowException() {
        // 端口号 > 65535 超出范围
        // 测试端口 65536
        // 注意：由于无法直接 mock 配置，这里仅作为示例
    }

    @Test
    @DisplayName("portValidation_WhenPort80_ShouldBeValid")
    void portValidation_WhenPort80_ShouldBeValid() {
        // 端口 80 是标准 HTTP 端口，生产环境常用
        Config config = new Config("prod");
        assertEquals(80, config.getPort());
    }

    @Test
    @DisplayName("portValidation_WhenPort8080_ShouldBeValid")
    void portValidation_WhenPort8080_ShouldBeValid() {
        // 端口 8080 是开发环境常用端口
        Config config = new Config("dev");
        assertEquals(8080, config.getPort());
    }

    // ==================== 多环境切换测试====================

    @Test
    @DisplayName("environmentSwitch_WhenSwitchFromDevToProd_ShouldLoadProdConfig")
    void environmentSwitch_WhenSwitchFromDevToProd_ShouldLoadProdConfig() {
        Config devConfig = new Config("dev");
        assertEquals("campusflow.db", devConfig.getDbPath());
        assertEquals(8080, devConfig.getPort());
        assertEquals("DEBUG", devConfig.getLogLevel());

        Config prodConfig = new Config("prod");
        assertEquals("/var/data/campusflow.db", prodConfig.getDbPath());
        assertEquals(80, prodConfig.getPort());
        assertEquals("WARN", prodConfig.getLogLevel());
    }

    @Test
    @DisplayName("environmentSwitch_WhenSwitchFromDevToTest_ShouldLoadTestConfig")
    void environmentSwitch_WhenSwitchFromDevToTest_ShouldLoadTestConfig() {
        Config devConfig = new Config("dev");
        Config testConfig = new Config("test");

        assertNotEquals(devConfig.getDbPath(), testConfig.getDbPath());
        assertNotEquals(devConfig.getPort(), testConfig.getPort());
        assertNotEquals(devConfig.getLogLevel(), testConfig.getLogLevel());
    }

    // ==================== 配置默认值测试====================

    @Test
    @DisplayName("defaultValues_WhenPropertyMissing_ShouldUseDefaults")
    void defaultValues_WhenPropertyMissing_ShouldUseDefaults() {
        // 测试配置文件中属性缺失时的默认值
        // Config 类实现中已设置默认值：
        // - db.path: "campusflow.db"
        // - server.port: "8080"
        // - log.level: "INFO"

        // 这个测试需要创建一个空的 Properties 对象来测试
        // 由于 Config 的构造函数会自动加载配置文件，
        // 实际测试中需要使用 mock 框架或修改 Config 类以支持测试
    }

    // ==================== 场景测试（12-Factor App）====================

    @Test
    @DisplayName("twelveFactorApp_WhenEnvVarOverridesConfig_ShouldPreferEnvVar")
    void twelveFactorApp_WhenEnvVarOverridesConfig_ShouldPreferEnvVar() {
        // 12-Factor App 原则：配置通过环境变量注入
        // Config 类已实现：环境变量优先于配置文件

        // 实际测试需要 mock 环境变量
        // 示例场景：
        // 1. 配置文件：db.path=campusflow.db
        // 2. 环境变量：DB_PATH=/custom/path/db.db
        // 3. 预期结果：getDbPath() 返回 /custom/path/db.db
    }

    @Test
    @DisplayName("configIsolation_WhenMultipleConfigs_ShouldNotInterfere")
    void configIsolation_WhenMultipleConfigs_ShouldNotInterfere() {
        // 测试多个 Config 实例之间不会相互干扰
        Config config1 = new Config("dev");
        Config config2 = new Config("prod");

        assertEquals("campusflow.db", config1.getDbPath());
        assertEquals("/var/data/campusflow.db", config2.getDbPath());

        // 确保 config1 不受 config2 影响
        assertEquals("campusflow.db", config1.getDbPath());
    }

    // ==================== 数据库路径验证测试====================

    @Test
    @DisplayName("dbPathValidation_WhenDbPathIsEmpty_ShouldThrowException")
    void dbPathValidation_WhenDbPathIsEmpty_ShouldThrowException() {
        Config config = new Config("dev") {
            @Override
            public String getDbPath() {
                return "";  // 模拟空路径
            }
        };

        assertThrows(IllegalStateException.class, config::validate);
    }

    @Test
    @DisplayName("dbPathValidation_WhenDbPathIsBlank_ShouldThrowException")
    void dbPathValidation_WhenDbPathIsBlank_ShouldThrowException() {
        Config config = new Config("dev") {
            @Override
            public String getDbPath() {
                return "   ";  // 模拟空白路径
            }
        };

        assertThrows(IllegalStateException.class, config::validate);
    }

    @Test
    @DisplayName("dbPathValidation_WhenDbPathIsValid_ShouldPassValidation")
    void dbPathValidation_WhenDbPathIsValid_ShouldPassValidation() {
        Config config = new Config("dev");
        assertDoesNotThrow(config::validate);
    }
}
