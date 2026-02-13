package com.campusflow;

import com.campusflow.config.Config;
import com.campusflow.util.Version;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AppTest - CampusFlow 基线测试
 *
 * 这是脚手架自带的基线测试，用于验证核心功能。
 * 请勿修改此文件。
 *
 * 新增测试请写到独立文件中（如 VersionTest.java、ConfigTest.java）
 */
class AppTest {

    @Test
    void testVersionParse() {
        Version v = Version.parse("1.0.0");
        assertEquals(1, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getPatch());
    }

    @Test
    void testVersionIncrement() {
        Version v1 = new Version(1, 0, 0);
        Version v2 = v1.incrementPatch();
        assertEquals("1.0.1", v2.toString());
    }

    @Test
    void testConfigDevEnvironment() {
        Config config = new Config("dev");
        assertEquals("dev", config.getEnv());
        assertEquals("campusflow.db", config.getDbPath());
        assertEquals(8080, config.getPort());
    }

    @Test
    void testConfigProdEnvironment() {
        Config config = new Config("prod");
        assertEquals("prod", config.getEnv());
        assertEquals("/var/data/campusflow.db", config.getDbPath());
        assertEquals(80, config.getPort());
    }
}
