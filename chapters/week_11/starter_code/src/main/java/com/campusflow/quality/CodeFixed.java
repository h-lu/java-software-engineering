package com.campusflow.quality;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 修复后的代码示例。
 *
 * <p>本类展示了如何修复 {@link CodeWithBugs} 中的常见静态分析问题。
 * 所有 SpotBugs 警告都已修复。
 */
public class CodeFixed implements Comparable<CodeFixed> {

    // 修复: 移除未使用的字段，或添加 getter/setter 使其有意义
    // private String unusedField;  // 已删除

    // 修复: 使用不可变的、线程安全的集合，或使用 getter
    private static final List<String> SHARED_DATA = new ArrayList<>();

    private String name;
    private int priority;

    public CodeFixed(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    // ===== 修复 1: 空指针解引用 =====
    /**
     * 获取用户全名（修复版）。
     *
     * <p>使用防御式编程检查 null 输入。
     *
     * @param firstName 名，可能为 null
     * @param lastName 姓，可能为 null
     * @return 全名
     * @throws IllegalArgumentException 如果任一参数为 null
     */
    public String getFullName(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("First name and last name cannot be null");
        }
        return firstName.trim() + " " + lastName.trim();
    }

    // ===== 修复 2: 资源泄漏 =====
    /**
     * 读取文件内容（修复版）。
     *
     * <p>使用 try-with-resources 确保流正确关闭。
     *
     * @param filename 文件名
     * @return 文件内容
     * @throws IOException 如果读取失败
     */
    public String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        // 修复: 使用 try-with-resources 自动关闭资源
        try (FileInputStream fis = new FileInputStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    // ===== 修复 3: 过于宽泛的异常捕获 =====
    /**
     * 解析数字（修复版）。
     *
     * <p>捕获具体的 NumberFormatException 而不是通用的 Exception。
     *
     * @param input 输入字符串
     * @return 解析后的数字，如果解析失败返回 0
     */
    public int parseNumber(String input) {
        // 修复: 捕获具体的异常类型
        if (input == null) {
            return 0;
        }
        // 检查带加号的数字（Integer.parseInt 支持，但我们希望返回 0）
        if (input.startsWith("+")) {
            return 0;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ===== 修复 4: 移除 System.exit() =====
    /**
     * 严重错误处理（修复版）。
     *
     * <p>抛出异常而不是调用 System.exit()，让调用者决定如何处理。
     *
     * @param message 错误消息
     * @throws IllegalStateException 发生致命错误时
     */
    public void handleFatalError(String message) {
        // 修复: 抛出异常而不是调用 System.exit()
        throw new IllegalStateException("Fatal error: " + message);
    }

    // ===== 修复 5: compareTo 与 equals 一致 =====
    /**
     * 比较两个对象的优先级（修复版）。
     *
     * <p>本方法比较 name 和 priority，与 equals 方法保持一致。
     *
     * @param other 另一个对象
     * @return 比较结果
     */
    @Override
    public int compareTo(CodeFixed other) {
        if (other == null) {
            return 1;
        }

        // 修复: 先比较 name，再比较 priority，与 equals 保持一致
        // 处理 null name 的情况
        if (this.name == null && other.name == null) {
            return Integer.compare(this.priority, other.priority);
        }
        if (this.name == null) {
            return -1;  // null 排在前面
        }
        if (other.name == null) {
            return 1;   // this.name 不是 null，排在前面
        }

        int nameCompare = this.name.compareTo(other.name);
        if (nameCompare != 0) {
            return nameCompare;
        }
        return Integer.compare(this.priority, other.priority);
    }

    /**
     * 判断两个对象是否相等。
     *
     * @param obj 另一个对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CodeFixed other = (CodeFixed) obj;
        // 修复: 正确处理 null name
        if (priority != other.priority) return false;
        if (name == null && other.name == null) return true;
        if (name == null || other.name == null) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        // 修复: 正确处理 null name
        int result = priority;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    // ===== 修复 6: 字符串比较使用 equals() =====
    /**
     * 检查命令是否为退出命令（修复版）。
     *
     * @param command 命令，可能为 null
     * @return 是否为退出命令
     */
    public boolean isExitCommand(String command) {
        // 修复: 使用 equals() 并检查 null
        return command != null && command.equals("exit");
    }

    // ===== 修复 7: 移除不必要的 String 构造方法 =====
    /**
     * 转换字符串为小写（修复版）。
     *
     * @param input 输入字符串
     * @return 小写字符串
     */
    public String toLowerCase(String input) {
        // 修复: 直接返回结果，不创建不必要的 String 对象
        if (input == null) {
            return null;
        }
        return input.toLowerCase();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * 获取共享数据的只读视图。
     *
     * @return 共享数据
     */
    public static List<String> getSharedData() {
        return new ArrayList<>(SHARED_DATA);
    }
}
