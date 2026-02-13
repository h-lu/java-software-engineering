package com.campusflow.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * VersionTest - 语义化版本管理测试
 *
 * 测试 Semantic Versioning 规范的实现
 * 包括版本解析、比较、升级等功能
 */
@DisplayName("Version - 语义化版本管理测试")
class VersionTest {

    // ==================== 正例测试（Happy Path）====================

    @Test
    @DisplayName("parse_WhenValidVersionString_ShouldCreateVersionObject")
    void parse_WhenValidVersionString_ShouldCreateVersionObject() {
        // 标准版本号
        Version v1 = Version.parse("1.0.0");
        assertEquals(1, v1.getMajor());
        assertEquals(0, v1.getMinor());
        assertEquals(0, v1.getPatch());
        assertNull(v1.getPreRelease());

        // 带预发布标识
        Version v2 = Version.parse("2.3.4-beta.1");
        assertEquals(2, v2.getMajor());
        assertEquals(3, v2.getMinor());
        assertEquals(4, v2.getPatch());
        assertEquals("beta.1", v2.getPreRelease());
    }

    @Test
    @DisplayName("compareTo_WhenSameVersion_ShouldReturnZero")
    void compareTo_WhenSameVersion_ShouldReturnZero() {
        Version v1 = new Version(1, 2, 3);
        Version v2 = new Version(1, 2, 3);
        assertEquals(0, v1.compareTo(v2));
        assertTrue(v1.equals(v2));
    }

    @Test
    @DisplayName("compareTo_WhenMajorDiffers_ShouldCompareMajor")
    void compareTo_WhenMajorDiffers_ShouldCompareMajor() {
        Version v1 = new Version(2, 0, 0);
        Version v2 = new Version(1, 9, 9);
        assertTrue(v1.compareTo(v2) > 0);
        assertTrue(v2.compareTo(v1) < 0);
    }

    @Test
    @DisplayName("compareTo_WhenMinorDiffers_ShouldCompareMinor")
    void compareTo_WhenMinorDiffers_ShouldCompareMinor() {
        Version v1 = new Version(1, 2, 0);
        Version v2 = new Version(1, 1, 9);
        assertTrue(v1.compareTo(v2) > 0);
        assertTrue(v2.compareTo(v1) < 0);
    }

    @Test
    @DisplayName("compareTo_WhenPatchDiffers_ShouldComparePatch")
    void compareTo_WhenPatchDiffers_ShouldComparePatch() {
        Version v1 = new Version(1, 2, 3);
        Version v2 = new Version(1, 2, 2);
        assertTrue(v1.compareTo(v2) > 0);
        assertTrue(v2.compareTo(v1) < 0);
    }

    @Test
    @DisplayName("compareTo_WhenPreReleaseDiffers_ShouldComparePreRelease")
    void compareTo_WhenPreReleaseDiffers_ShouldComparePreRelease() {
        Version v1 = new Version(1, 0, 0, "beta.2");
        Version v2 = new Version(1, 0, 0, "beta.1");
        assertTrue(v1.compareTo(v2) > 0);

        // 正式版本 > 预发布版本
        Version stable = new Version(1, 0, 0);
        Version pre = new Version(1, 0, 0, "beta.1");
        assertTrue(stable.compareTo(pre) > 0);
    }

    @Test
    @DisplayName("incrementMajor_WhenCalled_ShouldResetMinorAndPatch")
    void incrementMajor_WhenCalled_ShouldResetMinorAndPatch() {
        Version v1 = new Version(1, 5, 10);
        Version v2 = v1.incrementMajor();
        assertEquals(2, v2.getMajor());
        assertEquals(0, v2.getMinor());
        assertEquals(0, v2.getPatch());
    }

    @Test
    @DisplayName("incrementMinor_WhenCalled_ShouldResetPatch")
    void incrementMinor_WhenCalled_ShouldResetPatch() {
        Version v1 = new Version(1, 2, 10);
        Version v2 = v1.incrementMinor();
        assertEquals(1, v2.getMajor());
        assertEquals(3, v2.getMinor());
        assertEquals(0, v2.getPatch());
    }

    @Test
    @DisplayName("incrementPatch_WhenCalled_ShouldIncrementPatchOnly")
    void incrementPatch_WhenCalled_ShouldIncrementPatchOnly() {
        Version v1 = new Version(1, 2, 3);
        Version v2 = v1.incrementPatch();
        assertEquals(1, v2.getMajor());
        assertEquals(2, v2.getMinor());
        assertEquals(4, v2.getPatch());
    }

    @Test
    @DisplayName("toString_WhenCalled_ShouldReturnFormattedString")
    void toString_WhenCalled_ShouldReturnFormattedString() {
        Version v1 = new Version(1, 2, 3);
        assertEquals("1.2.3", v1.toString());

        Version v2 = new Version(2, 0, 0, "beta.1");
        assertEquals("2.0.0-beta.1", v2.toString());
    }

    @Test
    @DisplayName("equalsAndHashCode_WhenSameVersion_ShouldBeEqual")
    void equalsAndHashCode_WhenSameVersion_ShouldBeEqual() {
        Version v1 = new Version(1, 2, 3);
        Version v2 = new Version(1, 2, 3);

        assertTrue(v1.equals(v2));
        assertTrue(v2.equals(v1));
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    @DisplayName("equals_WhenDifferentVersion_ShouldNotBeEqual")
    void equals_WhenDifferentVersion_ShouldNotBeEqual() {
        Version v1 = new Version(1, 2, 3);
        Version v2 = new Version(1, 2, 4);

        assertFalse(v1.equals(v2));
    }

    // ==================== 边界测试（Boundary Cases）====================

    @Test
    @DisplayName("parse_WhenMinimumVersion_ShouldParseCorrectly")
    void parse_WhenMinimumVersion_ShouldParseCorrectly() {
        Version v = Version.parse("0.0.1");
        assertEquals(0, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(1, v.getPatch());
    }

    @Test
    @DisplayName("parse_WhenLargeVersionNumbers_ShouldParseCorrectly")
    void parse_WhenLargeVersionNumbers_ShouldParseCorrectly() {
        Version v = Version.parse("999.999.999");
        assertEquals(999, v.getMajor());
        assertEquals(999, v.getMinor());
        assertEquals(999, v.getPatch());
    }

    @Test
    @DisplayName("constructor_WhenZeroVersion_ShouldCreateValidVersion")
    void constructor_WhenZeroVersion_ShouldCreateValidVersion() {
        Version v = new Version(0, 0, 0);
        assertEquals(0, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getPatch());
    }

    @ParameterizedTest
    @CsvSource({
        "1.0.0, 1.0.1, -1",
        "1.0.1, 1.0.0, 1",
        "1.2.0, 1.1.9, 1",
        "2.0.0, 1.9.9, 1"
    })
    @DisplayName("compareTo_WhenBoundaryVersions_ShouldCompareCorrectly")
    void compareTo_WhenBoundaryVersions_ShouldCompareCorrectly(String v1Str, String v2Str, int expectedSign) {
        Version v1 = Version.parse(v1Str);
        Version v2 = Version.parse(v2Str);
        int result = v1.compareTo(v2);

        if (expectedSign > 0) {
            assertTrue(result > 0, String.format("Expected %s > %s", v1Str, v2Str));
        } else if (expectedSign < 0) {
            assertTrue(result < 0, String.format("Expected %s < %s", v1Str, v2Str));
        } else {
            assertEquals(0, result, String.format("Expected %s == %s", v1Str, v2Str));
        }
    }

    @Test
    @DisplayName("incrementMajor_WhenFromZero_ShouldBecomeOne")
    void incrementMajor_WhenFromZero_ShouldBecomeOne() {
        Version v1 = new Version(0, 0, 0);
        Version v2 = v1.incrementMajor();
        assertEquals(1, v2.getMajor());
        assertEquals(0, v2.getMinor());
        assertEquals(0, v2.getPatch());
    }

    @Test
    @DisplayName("toString_WhenEmptyPreRelease_ShouldNotIncludeDash")
    void toString_WhenEmptyPreRelease_ShouldNotIncludeDash() {
        Version v = new Version(1, 0, 0, "");
        assertEquals("1.0.0", v.toString());
    }

    // ==================== 反例测试（Error Cases）====================

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("parse_WhenNullOrEmpty_ShouldThrowException")
    void parse_WhenNullOrEmpty_ShouldThrowException(String versionString) {
        assertThrows(IllegalArgumentException.class, () -> Version.parse(versionString));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "1",           // 只有主版本
        "1.0",         // 只有主版本和次版本
        "a.b.c",       // 非数字
        "1.0.0.0",     // 四段
        "1..0",        // 双点号
        ".1.0.0",      // 开头点号
        "1.0.0.",      // 结尾点号
        "-beta.1"      // 只有预发布标识
    })
    @DisplayName("parse_WhenInvalidFormat_ShouldThrowException")
    void parse_WhenInvalidFormat_ShouldThrowException(String invalidVersion) {
        assertThrows(IllegalArgumentException.class, () -> Version.parse(invalidVersion));
    }

    @Test
    @DisplayName("constructor_WhenNegativeMajor_ShouldThrowException")
    void constructor_WhenNegativeMajor_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Version(-1, 0, 0));
    }

    @Test
    @DisplayName("constructor_WhenNegativeMinor_ShouldThrowException")
    void constructor_WhenNegativeMinor_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Version(1, -1, 0));
    }

    @Test
    @DisplayName("constructor_WhenNegativePatch_ShouldThrowException")
    void constructor_WhenNegativePatch_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Version(1, 0, -1));
    }

    @Test
    @DisplayName("equals_WhenComparedWithNull_ShouldReturnFalse")
    void equals_WhenComparedWithNull_ShouldReturnFalse() {
        Version v = new Version(1, 0, 0);
        assertFalse(v.equals(null));
    }

    @Test
    @DisplayName("equals_WhenComparedWithDifferentType_ShouldReturnFalse")
    void equals_WhenComparedWithDifferentType_ShouldReturnFalse() {
        Version v = new Version(1, 0, 0);
        assertFalse(v.equals("1.0.0"));
        assertFalse(v.equals(100));
    }

    // ==================== 场景测试（Semantic Versioning 规则）====================

    @Test
    @DisplayName("semanticVersioning_WhenPatchUpgrade_ShouldBeBackwardCompatible")
    void semanticVersioning_WhenPatchUpgrade_ShouldBeBackwardCompatible() {
        // 1.0.0 → 1.0.1：Bug 修复，向后兼容
        Version v1 = new Version(1, 0, 0);
        Version v2 = v1.incrementPatch();
        assertEquals(1, v2.getMajor());
        assertEquals(0, v2.getMinor());
        assertEquals(1, v2.getPatch());
    }

    @Test
    @DisplayName("semanticVersioning_WhenMinorUpgrade_ShouldBeBackwardCompatible")
    void semanticVersioning_WhenMinorUpgrade_ShouldBeBackwardCompatible() {
        // 1.0.0 → 1.1.0：新功能，向后兼容
        Version v1 = new Version(1, 0, 0);
        Version v2 = v1.incrementMinor();
        assertEquals(1, v2.getMajor());
        assertEquals(1, v2.getMinor());
        assertEquals(0, v2.getPatch());
    }

    @Test
    @DisplayName("semanticVersioning_WhenMajorUpgrade_ShouldBeIncompatible")
    void semanticVersioning_WhenMajorUpgrade_ShouldBeIncompatible() {
        // 1.0.0 → 2.0.0：破坏性变更
        Version v1 = new Version(1, 0, 0);
        Version v2 = v1.incrementMajor();
        assertEquals(2, v2.getMajor());
        assertEquals(0, v2.getMinor());
        assertEquals(0, v2.getPatch());
    }

    @Test
    @DisplayName("semanticVersioning_WhenPreReleaseVsStable_StableShouldBeGreater")
    void semanticVersioning_WhenPreReleaseVsStable_StableShouldBeGreater() {
        // 1.0.0-beta.1 < 1.0.0
        Version beta = new Version(1, 0, 0, "beta.1");
        Version stable = new Version(1, 0, 0);
        assertTrue(beta.compareTo(stable) < 0);
        assertTrue(stable.compareTo(beta) > 0);
    }
}
