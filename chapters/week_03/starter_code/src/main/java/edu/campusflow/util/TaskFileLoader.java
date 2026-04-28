package edu.campusflow.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Week 03 文件读取异常处理骨架。
 */
public class TaskFileLoader {

    public List<String> loadTasksFromFile(String filename) {
        if (filename == null) {
            // 待办： 作业要求这里抛出 IllegalArgumentException，并给出中文错误信息。
            return new ArrayList<>();
        }

        // 待办： 使用 try-catch-finally 或 try-with-resources 读取文件。
        // 待办： 文件不存在时输出友好错误信息并返回空列表。
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        TaskFileLoader loader = new TaskFileLoader();
        System.out.println("Loaded tasks: " + loader.loadTasksFromFile("tasks.txt").size());
    }
}
