package com.campusflow.util;

public record Version(int major, int minor, int patch) implements Comparable<Version> {

    public Version {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version numbers 不能为负数");
        }
    }

    public static Version parse(String text) {
        // 待办：解析 1.0.0 这类 Semantic Versioning 字符串。
        // 待办：拒绝 null、空白、不完整、非数字版本。
        throw new UnsupportedOperationException("待办：实现 SemVer parsing");
    }

    public Version nextMajor() {
        // 待办：递增 MAJOR，并重置 MINOR/PATCH。
        throw new UnsupportedOperationException("待办：实现 nextMajor");
    }

    public Version nextMinor() {
        // 待办：递增 MINOR，并重置 PATCH。
        throw new UnsupportedOperationException("待办：实现 nextMinor");
    }

    public Version nextPatch() {
        // 待办：递增 PATCH。
        throw new UnsupportedOperationException("待办：实现 nextPatch");
    }

    @Override
    public int compareTo(Version other) {
        // 待办：依次比较 MAJOR、MINOR、PATCH。
        throw new UnsupportedOperationException("待办：实现 version comparison");
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
