package com.campusflow.quality;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeWithIssues {
    private String unusedNote = "TODO: decide whether this field belongs here";
    public static List<String> sharedTags = new ArrayList<>();

    public int titleLength(String title) {
        // TODO: SpotBugs should push you to handle null intentionally.
        return title.trim().length();
    }

    public byte[] readFirstBytes(String path) throws IOException {
        // TODO: Replace with try-with-resources when you address resource leaks.
        FileInputStream input = new FileInputStream(path);
        return input.readNBytes(16);
    }

    public boolean isExitCommand(String command) {
        // TODO: Fix string comparison during the SpotBugs task.
        return command == "exit";
    }
}
