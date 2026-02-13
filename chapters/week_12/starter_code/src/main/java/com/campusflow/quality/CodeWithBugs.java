package com.campusflow.quality;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 包含常见静态分析问题的示例代码。
 *
 * <p>本类故意包含了一些代码问题，用于演示 SpotBugs 等静态分析工具如何发现潜在 bug。
 * 学生将学习如何识别和修复这些问题。
 *
 * <p>常见问题类型：
 * <ul>
 *   <li>NP_NULL_ON_SOME_PATH: 可能的空指针解引用</li>
 *   <li>OBL_UNSATISFIED_OBLIGATION: 流未关闭</li>
 *   <li>URF_UNREAD_FIELD: 未读取的字段</li>
 *   <li>REC_CATCH_EXCEPTION: 直接捕获 Exception</li>
 *   <li>DM_EXIT: 调用 System.exit()</li>
 *   <li>EQ_COMPARETO_USE_OBJECT_EQUALS: compareTo 与 equals 不一致</li>
 * </ul>
 */
public class CodeWithBugs implements Comparable<CodeWithBugs> {

    // ===== 问题 1: 未读取的字段 (URF_UNREAD_FIELD) =====
    private String unusedField = "This field is never read";  // SpotBugs 警告

    private String name;
    private int priority;

    // ===== 问题 2: 公开静态可变字段 (MS_PKGPROTECT, MS_FINAL) =====
    public static List<String> sharedData = new ArrayList<>();  // SpotBugs 警告

    public CodeWithBugs(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    // ===== 问题 3: 可能的空指针解引用 (NP_NULL_ON_SOME_PATH) =====
    /**
     * 获取用户全名（可能抛出 NullPointerException）。
     *
     * <p>本方法没有检查 null 输入，SpotBots 会警告可能的空指针解引用。
     *
     * @param firstName 名，可能为 null
     * @param lastName 姓，可能为 null
     * @return 全名
     */
    public String getFullName(String firstName, String lastName) {
        // BUG: 如果 firstName 或 lastName 为 null，会抛出 NullPointerException
        // SpotBugs 警告: NP_NULL_ON_SOME_PATH
        return firstName.trim() + " " + lastName.trim();  // 潜在的空指针
    }

    // ===== 问题 4: 资源泄漏 - 流未关闭 (OBL_UNSATISFIED_OBLIGATION) =====
    /**
     * 读取文件内容（有资源泄漏风险）。
     *
     * <p>本方法没有正确关闭 FileInputStream，SpotBots 会警告资源泄漏。
     *
     * @param filename 文件名
     * @return 文件内容
     * @throws IOException 如果读取失败
     */
    public String readFile(String filename) throws IOException {
        // BUG: FileInputStream 没有在 finally 块中关闭
        // SpotBugs 警告: OBL_UNSATISFIED_OBLIGATION
        FileInputStream fis = new FileInputStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        // 资源未关闭！
        return content.toString();
    }

    // ===== 问题 5: 直接捕获 Exception (REC_CATCH_EXCEPTION) =====
    /**
     * 解析数字（使用了过于宽泛的异常捕获）。
     *
     * <p>本方法捕获了 Exception 而不是更具体的异常类型，SpotBots 会警告。
     *
     * @param input 输入字符串
     * @return 解析后的数字
     */
    public int parseNumber(String input) {
        // BUG: 捕获了过于宽泛的 Exception
        // SpotBugs 警告: REC_CATCH_EXCEPTION
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {  // 应该捕获 NumberFormatException
            return 0;
        }
    }

    // ===== 问题 6: 调用 System.exit() (DM_EXIT) =====
    /**
     * 严重错误处理（使用了 System.exit）。
     *
     * <p>本方法在库代码中调用了 System.exit()，SpotBots 会警告。
     *
     * @param message 错误消息
     */
    public void handleFatalError(String message) {
        // BUG: 库代码不应该调用 System.exit()
        // SpotBugs 警告: DM_EXIT
        System.err.println("FATAL: " + message);
        System.exit(1);  // 这会终止整个 JVM！
    }

    // ===== 问题 7: compareTo 与 equals 不一致 (EQ_COMPARETO_USE_OBJECT_EQUALS) =====
    /**
     * 比较两个对象的优先级。
     *
     * <p>本方法只比较 priority 字段，但 equals 方法会比较 name 和 priority。
     * 这会导致 compareTo 和 equals 不一致，SpotBots 会警告。
     *
     * @param other 另一个对象
     * @return 比较结果
     */
    @Override
    public int compareTo(CodeWithBugs other) {
        // BUG: 只比较 priority，但 equals 会比较 name 和 priority
        // SpotBugs 警告: EQ_COMPARETO_USE_OBJECT_EQUALS
        return Integer.compare(this.priority, other.priority);
    }

    /**
     * 判断两个对象是否相等。
     *
     * <p>本方法比较 name 和 priority，但 compareTo 只比较 priority。
     * 这会导致 TreeSet/TreeMap 等基于 compareTo 的集合行为不一致。
     *
     * @param obj 另一个对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CodeWithBugs other = (CodeWithBugs) obj;
        return priority == other.priority && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + priority;
    }

    // ===== 问题 8: 字符串比较使用 == (ES_COMPARING_STRINGS_WITH_EQ) =====
    /**
     * 检查命令是否为退出命令（使用 == 比较字符串）。
     *
     * <p>本方法使用 == 比较字符串，应该使用 equals()，SpotBots 会警告。
     *
     * @param command 命令
     * @return 是否为退出命令
     */
    public boolean isExitCommand(String command) {
        // BUG: 字符串比较应该使用 equals()
        // SpotBugs 警告: ES_COMPARING_STRINGS_WITH_EQ
        return command == "exit";  // 永远返回 true！应该使用 equals()
    }

    // ===== 问题 9: 自增操作符副作用 (ISM_DEREFERENCE_OF_READLINE_VALUE) =====
    /**
     * 读取多行输入（有潜在的副作用问题）。
     *
     * @param reader 读取器
     * @return 读取的行数
     * @throws IOException 如果读取失败
     */
    public int readMultipleLines(BufferedReader reader) throws IOException {
        int count = 0;
        String line;
        // BUG: readLine() 在条件中调用，如果有副作用会有问题
        // SpotBugs 警告: ISM_DEREFERENCE_OF_READLINE_VALUE
        while ((line = reader.readLine()) != null && !line.equals("done")) {
            count++;
        }
        return count;
    }

    // ===== 问题 10: 创建新字符串但忽略结果 (DM_STRING_CTOR) =====
    /**
     * 转换字符串为小写（使用了不必要的 String 构造方法）。
     *
     * <p>本方法使用了 new String() 构造方法，但这是不必要的，SpotBots 会警告。
     *
     * @param input 输入字符串
     * @return 小写字符串
     */
    public String toLowerCase(String input) {
        // BUG: new String(str) 是不必要的
        // SpotBugs 警告: DM_STRING_CTOR
        return new String(input.toLowerCase());  // 应该直接 return input.toLowerCase()
    }

    // ===== 修复示例：防御式编程 =====

    /**
     * 获取用户全名（修复版）。
     *
     * <p>本方法检查 null 输入，展示了防御式编程的最佳实践。
     *
     * @param firstName 名，可能为 null
     * @param lastName 姓，可能为 null
     * @return 全名
     */
    public String getFullNameFixed(String firstName, String lastName) {
        // 修复：检查 null
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("First name and last name cannot be null");
        }
        return firstName.trim() + " " + lastName.trim();
    }

    /**
     * 读取文件内容（修复版）。
     *
     * <p>本方法使用 try-with-resources 正确关闭流，展示了资源管理的最佳实践。
     *
     * @param filename 文件名
     * @return 文件内容
     * @throws IOException 如果读取失败
     */
    public String readFileFixed(String filename) throws IOException {
        // 修复：使用 try-with-resources
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    /**
     * 检查命令是否为退出命令（修复版）。
     *
     * <p>本方法使用 equals() 比较字符串。
     *
     * @param command 命令
     * @return 是否为退出命令
     */
    public boolean isExitCommandFixed(String command) {
        // 修复：使用 equals()
        if (command == null) {
            return false;
        }
        return command.equals("exit");
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
}
