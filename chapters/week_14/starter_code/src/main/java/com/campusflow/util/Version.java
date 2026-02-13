package com.campusflow.util;

/**
 * Version - 语义化版本管理类
 *
 * 支持 Semantic Versioning 规范（MAJOR.MINOR.PATCH）
 * 示例：1.0.0、2.3.4、1.0.0-beta.1
 */
public class Version implements Comparable<Version> {
    private final int major;
    private final int minor;
    private final int patch;
    private final String preRelease;

    /**
     * 构造函数
     *
     * @param major 主版本号（不兼容的 API 变更）
     * @param minor 次版本号（向后兼容的功能新增）
     * @param patch 补丁版本号（向后兼容的问题修复）
     */
    public Version(int major, int minor, int patch) {
        this(major, minor, patch, null);
    }

    /**
     * 构造函数（带预发布版本）
     *
     * @param major 主版本号
     * @param minor 次版本号
     * @param patch 补丁版本号
     * @param preRelease 预发布版本标识（如 "beta.1"、"rc.1"）
     */
    public Version(int major, int minor, int patch, String preRelease) {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version numbers must be non-negative");
        }
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.preRelease = preRelease;
    }

    /**
     * 从字符串解析版本号
     *
     * @param versionString 版本字符串（如 "1.0.0"、"2.3.4-beta.1"）
     * @return Version 对象
     * @throws IllegalArgumentException 如果格式无效
     */
    public static Version parse(String versionString) {
        if (versionString == null || versionString.trim().isEmpty()) {
            throw new IllegalArgumentException("Version string cannot be null or empty");
        }

        String trimmed = versionString.trim();

        // 检查是否以 - 开头（只有预发布标识）
        if (trimmed.startsWith("-")) {
            throw new IllegalArgumentException("Invalid version format: " + trimmed);
        }

        String[] parts = trimmed.split("-", 2);
        String versionPart = parts[0];
        String preReleasePart = parts.length > 1 ? parts[1] : null;

        // 使用 -1 参数保留尾随的空字符串（用于检测 "1.0.0." 这种格式）
        String[] numbers = versionPart.split("\\.", -1);
        if (numbers.length != 3) {
            throw new IllegalArgumentException("Invalid version format: " + trimmed);
        }

        // 检查是否有空的部分（如 "1..0" 或 ".1.0.0"）
        for (String num : numbers) {
            if (num.isEmpty()) {
                throw new IllegalArgumentException("Invalid version format: " + trimmed);
            }
        }

        try {
            int major = Integer.parseInt(numbers[0]);
            int minor = Integer.parseInt(numbers[1]);
            int patch = Integer.parseInt(numbers[2]);
            return new Version(major, minor, patch, preReleasePart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version numbers: " + trimmed, e);
        }
    }

    /**
     * 升级主版本号（MAJOR）
     * 重置 MINOR 和 PATCH 为 0
     *
     * @return 新的 Version 对象
     */
    public Version incrementMajor() {
        return new Version(this.major + 1, 0, 0);
    }

    /**
     * 升级次版本号（MINOR）
     * 重置 PATCH 为 0
     *
     * @return 新的 Version 对象
     */
    public Version incrementMinor() {
        return new Version(this.major, this.minor + 1, 0);
    }

    /**
     * 升级补丁版本号（PATCH）
     *
     * @return 新的 Version 对象
     */
    public Version incrementPatch() {
        return new Version(this.major, this.minor, this.patch + 1);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String getPreRelease() {
        return preRelease;
    }

    @Override
    public String toString() {
        String base = String.format("%d.%d.%d", major, minor, patch);
        if (preRelease != null && !preRelease.isEmpty()) {
            return base + "-" + preRelease;
        }
        return base;
    }

    @Override
    public int compareTo(Version other) {
        // 先比较 MAJOR
        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }
        // 再比较 MINOR
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }
        // 最后比较 PATCH
        if (this.patch != other.patch) {
            return Integer.compare(this.patch, other.patch);
        }
        // 预发布版本比较：有预发布标识的版本 < 无预发布标识的版本
        if (this.preRelease == null && other.preRelease != null) {
            return 1;
        }
        if (this.preRelease != null && other.preRelease == null) {
            return -1;
        }
        if (this.preRelease != null && other.preRelease != null) {
            return this.preRelease.compareTo(other.preRelease);
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Version version = (Version) obj;
        return major == version.major &&
                minor == version.minor &&
                patch == version.patch &&
                (preRelease == null ? version.preRelease == null : preRelease.equals(version.preRelease));
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        result = 31 * result + (preRelease != null ? preRelease.hashCode() : 0);
        return result;
    }
}
