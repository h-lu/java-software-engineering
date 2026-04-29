package com.campusflow.util;

public record Version(int major, int minor, int patch) implements Comparable<Version> {

    public Version {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version numbers 不能为负数");
        }
    }

    public static Version parse(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Version text must not be blank");
        }

        String[] parts = text.trim().split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Version must use MAJOR.MINOR.PATCH: " + text);
        }

        try {
            return new Version(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Version numbers must be numeric: " + text, e);
        }
    }

    public Version nextMajor() {
        return new Version(major + 1, 0, 0);
    }

    public Version nextMinor() {
        return new Version(major, minor + 1, 0);
    }

    public Version nextPatch() {
        return new Version(major, minor, patch + 1);
    }

    @Override
    public int compareTo(Version other) {
        int majorComparison = Integer.compare(major, other.major);
        if (majorComparison != 0) {
            return majorComparison;
        }
        int minorComparison = Integer.compare(minor, other.minor);
        if (minorComparison != 0) {
            return minorComparison;
        }
        return Integer.compare(patch, other.patch);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
