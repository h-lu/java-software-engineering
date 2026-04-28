package com.campusflow.quality;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeWithIssues {
    private String unusedNote = "待办：判断这个字段是否应该留在这里";
    public static List<String> sharedTags = new ArrayList<>();

    public int titleLength(String title) {
        // 待办：让 SpotBugs 推动你有意识地处理 null。
        return title.trim().length();
    }

    public byte[] readFirstBytes(String path) throws IOException {
        // 待办：处理资源泄漏时，改成 try-with-resources。
        FileInputStream input = new FileInputStream(path);
        return input.readNBytes(16);
    }

    public boolean isExitCommand(String command) {
        // 待办：在 SpotBugs 任务中修复字符串比较问题。
        return command == "exit";
    }
}
