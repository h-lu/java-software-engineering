/**
 * 示例 01：程序崩溃演示
 *
 * 本例演示没有异常处理的代码如何崩溃。
 * 展示 FileNotFoundException 等受检异常的场景。
 *
 * 运行方式：
 *   javac 01_crash_demo.java
 *   java CrashDemo
 *
 * 预期输出：
 *   程序在尝试读取不存在的文件时抛出 FileNotFoundException 并崩溃
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// ===== 反示例：没有异常处理 =====

/**
 * ❌ 反示例：没有异常处理的文件读取
 *
 * 问题：
 * 1. 编译器会报错：未报告的异常错误 FileNotFoundException
 * 2. 如果强制运行，文件不存在时程序会崩溃
 */
class TaskFileLoaderUnsafe {

    /**
     * 尝试从文件加载任务（没有异常处理）
     *
     * 编译错误：未报告的异常错误 FileNotFoundException; 必须对其进行捕获或声明以便抛出
     */
    public void loadTasksFromFile(String filename) {
        // 小北想：用 Scanner 读取文件，和从控制台读取应该差不多
        File file = new File(filename);

        // 下面这行会导致编译错误！
        // Scanner scanner = new Scanner(file);  // ← 编译器报错：未处理的 FileNotFoundException

        // 为了演示崩溃，我们暂时不处理这个错误（实际开发中不要这样做）
        // 这里使用 throws 声明来让代码能编译，但运行时仍会崩溃
        throw new RuntimeException("演示：这行代码如果执行，会因 FileNotFoundException 编译失败");
    }
}

// ===== 正示例：正确处理受检异常 =====

/**
 * ✅ 正示例：使用 throws 声明异常
 *
 * 方法 1：使用 throws 关键字声明方法可能抛出的异常
 * 这样调用者就知道需要处理这个异常
 */
class TaskFileLoaderWithThrows {

    /**
     * 从文件加载任务（声明可能抛出的异常）
     *
     * @param filename 文件名
     * @throws FileNotFoundException 当文件不存在时抛出
     */
    public void loadTasksFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);  // ✅ 现在编译器满意了

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("读取到: " + line);
        }
        scanner.close();
    }
}

// ===== 演示类 =====

/**
 * 崩溃演示主类
 */
class CrashDemo {

    public static void main(String[] args) {
        System.out.println("=== 程序崩溃演示 ===\n");

        // 演示 1：展示异常信息
        System.out.println("演示 1：FileNotFoundException 是什么？");
        System.out.println("─".repeat(50));
        System.out.println("当尝试打开一个不存在的文件时，Java 会抛出：");
        System.out.println();
        System.out.println("  java.io.FileNotFoundException: tasks.txt (系统找不到指定的文件。)");
        System.out.println("      at java.io.FileInputStream.open0(Native Method)");
        System.out.println("      at java.io.FileInputStream.open(FileInputStream.java:211)");
        System.out.println("      at java.util.Scanner.<init>(Scanner.java:610)");
        System.out.println();
        System.out.println("这个红色的'堆栈跟踪'告诉你：");
        System.out.println("  1. 发生了什么异常（FileNotFoundException）");
        System.out.println("  2. 在哪里发生的（Scanner.java:610）");
        System.out.println("  3. 调用链是什么（谁调了谁）");
        System.out.println();

        // 演示 2：实际运行崩溃场景
        System.out.println("演示 2：实际运行崩溃场景");
        System.out.println("─".repeat(50));

        TaskFileLoaderWithThrows loader = new TaskFileLoaderWithThrows();
        String nonExistentFile = "不存在的文件.txt";

        System.out.println("尝试读取文件：" + nonExistentFile);
        System.out.println();

        try {
            // 这里会抛出 FileNotFoundException
            loader.loadTasksFromFile(nonExistentFile);
        } catch (FileNotFoundException e) {
            System.out.println("✓ 捕获到异常（程序没有崩溃）：");
            System.out.println("  异常类型：" + e.getClass().getSimpleName());
            System.out.println("  异常消息：" + e.getMessage());
            System.out.println();
            System.out.println("堆栈跟踪（简化）：");
            // 只打印前 3 行堆栈
            StackTraceElement[] stack = e.getStackTrace();
            for (int i = 0; i < Math.min(3, stack.length); i++) {
                System.out.println("  at " + stack[i]);
            }
        }

        System.out.println();
        System.out.println("=== 关键概念 ===");
        System.out.println();
        System.out.println("异常（Exception）是 Java 的错误处理机制：");
        System.out.println("  • 异常是一个对象，包含错误类型、消息和发生位置");
        System.out.println("  • 异常被'抛出'（throw），沿着调用链向上传递");
        System.out.println("  • 如果没人'捕获'（catch），程序就会崩溃");
        System.out.println();
        System.out.println("受检异常（Checked Exception）：");
        System.out.println("  • 编译器强迫你处理或声明的异常");
        System.out.println("  • 如：FileNotFoundException、IOException");
        System.out.println("  • 代表'可预见的、应该恢复的'错误");
        System.out.println();
        System.out.println("为什么 Java 要这样设计？");
        System.out.println("  • 编译期能发现的错误，比运行期崩溃要便宜得多");
        System.out.println("  • 强迫程序员考虑'文件可能不存在'这种情况");
        System.out.println("  • 生产环境中这些情况真的会发生");
    }
}
