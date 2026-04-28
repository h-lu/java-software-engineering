package com.campusflow.util;

public record Version(int major, int minor, int patch) implements Comparable<Version> {

    public Version {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version numbers must be non-negative");
        }
    }

    public static Version parse(String text) {
        // TODO: Parse Semantic Versioning strings such as 1.0.0.
        // TODO: Reject null, blank, incomplete, and non-numeric versions.
        throw new UnsupportedOperationException("TODO: implement SemVer parsing");
    }

    public Version nextMajor() {
        // TODO: Increment MAJOR and reset MINOR/PATCH.
        throw new UnsupportedOperationException("TODO: implement nextMajor");
    }

    public Version nextMinor() {
        // TODO: Increment MINOR and reset PATCH.
        throw new UnsupportedOperationException("TODO: implement nextMinor");
    }

    public Version nextPatch() {
        // TODO: Increment PATCH.
        throw new UnsupportedOperationException("TODO: implement nextPatch");
    }

    @Override
    public int compareTo(Version other) {
        // TODO: Compare MAJOR, then MINOR, then PATCH.
        throw new UnsupportedOperationException("TODO: implement version comparison");
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
