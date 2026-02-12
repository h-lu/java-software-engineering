/**
 * 示例 02：try-catch-finally 基本用法
 *
 * 本例演示如何使用 try-catch-finally 结构捕获和处理异常，
 * 让程序在遇到错误时不崩溃，而是优雅地处理。
 *
 * 运行方式：
 *   javac 02_try_catch_basic.java
 *   java TryCatchDemo
 *
 * 预期输出：
 *   展示 try-catch-finally 的执行流程，以及多 catch 块的处理
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

// ===== 示例 1：基本 try-catch =====

/**
 * 基础文件加载器（单 catch 块）
 */
class BasicFileLoader {

    /**
     * 从文件加载任务（基础版 try-catch）
     *
     * 执行流程：
     * 1. 进入 try 块，正常执行
     * 2. 如果一切顺利，catch 块被跳过
     * 3. 如果在 try 块中抛出异常，立即跳转到匹配的 catch 块
     * 4. 执行 catch 块中的代码，然后继续执行 catch 块之后的代码
     */
    public void loadTasksFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("  读取到: " + line);
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            // 异常处理：给用户友好的提示，而不是崩溃
            System.out.println("  错误：找不到文件 '" + filename + "'");
        }

        // catch 块之后的代码会继续执行
        System.out.println("  方法执行完毕");
    }
}

// ===== 示例 2：多 catch 块 =====

/**
 * 增强文件加载器（多 catch 块）
 */
class MultiCatchFileLoader {

    /**
     * 从文件加载任务（多 catch 块版本）
     *
     * 注意：子类异常的 catch 块要放在父类之前！
     * 如果先 catch Exception，后面更具体的 catch 块就永远不会被执行
     */
    public void loadTasksFromFile(String filename) {
        try {
            // 防御式检查：在 try 块开始处验证输入
            if (filename == null) {
                throw new IllegalArgumentException("文件名不能为 null");
            }

            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("  读取到: " + line);
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            // 处理文件不存在的情况
            System.out.println("  错误：找不到文件 '" + filename + "'");

        } catch (IllegalArgumentException e) {
            // 处理非法参数的情况
            System.out.println("  错误：" + e.getMessage());
        }
    }
}

// ===== 示例 3：finally 块 =====

/**
 * 安全文件加载器（带 finally 块）
 */
class SafeFileLoader {

    /**
     * 从文件加载任务（带 finally 块）
     *
     * finally 块的典型用途是资源清理：
     * - 关闭文件
     * - 关闭数据库连接
     * - 释放锁
     *
     * 无论是否发生异常，finally 块都会执行
     */
    public void loadTasksFromFile(String filename) {
        Scanner scanner = null;

        try {
            File file = new File(filename);
            scanner = new Scanner(file);

            System.out.println("  开始读取文件...");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("  读取到: " + line);
            }

        } catch (FileNotFoundException e) {
            System.out.println("  错误：找不到文件 '" + filename + "'");

        } finally {
            // 无论是否发生异常，都会执行这里
            System.out.println("  finally 块执行：清理资源");
            if (scanner != null) {
                scanner.close();
                System.out.println("  文件已关闭");
            }
        }
    }
}

// ===== 示例 4：处理用户输入异常 =====

/**
 * 用户输入处理器
 *
 * 回顾 Week 01 的 Scanner：如果用户输入的不是数字，
 * nextInt() 会抛出 InputMismatchException
 */
class UserInputHandler {

    private Scanner scanner = new Scanner(System.in);

    /**
     * 获取用户输入的整数（带异常处理）
     */
    public int getIntInput(String prompt) {
        System.out.print(prompt);

        try {
            int value = scanner.nextInt();
            scanner.nextLine();  // 消耗换行符
            return value;

        } catch (InputMismatchException e) {
            System.out.println("  错误：请输入有效的数字");
            scanner.nextLine();  // 清除错误的输入
            return -1;  // 返回默认值表示错误
        }
    }

    /**
     * 循环直到获取有效输入
     */
    public int getIntInputWithRetry(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                int value = scanner.nextInt();
                scanner.nextLine();  // 消耗换行符
                return value;

            } catch (InputMismatchException e) {
                System.out.println("  输入无效，请重新输入数字");
                scanner.nextLine();  // 清除错误的输入
            }
        }
    }
}

// ===== 演示类 =====

/**
 * try-catch-finally 演示主类
 */
class TryCatchDemo {

    public static void main(String[] args) {
        System.out.println("=== try-catch-finally 演示 ===\n");

        // 演示 1：基本 try-catch
        System.out.println("演示 1：基本 try-catch");
        System.out.println("─".repeat(50));

        BasicFileLoader basicLoader = new BasicFileLoader();

        System.out.println("尝试读取存在的文件：");
        // 先创建一个临时文件用于演示
        createTempFile("demo.txt", "任务1\n任务2\n任务3");
        basicLoader.loadTasksFromFile("demo.txt");
        System.out.println();

        System.out.println("尝试读取不存在的文件：");
        basicLoader.loadTasksFromFile("不存在.txt");
        System.out.println();

        // 演示 2：多 catch 块
        System.out.println("演示 2：多 catch 块处理不同异常");
        System.out.println("─".repeat(50));

        MultiCatchFileLoader multiLoader = new MultiCatchFileLoader();

        System.out.println("传入 null 文件名：");
        multiLoader.loadTasksFromFile(null);
        System.out.println();

        System.out.println("传入不存在的文件名：");
        multiLoader.loadTasksFromFile("missing.txt");
        System.out.println();

        // 演示 3：finally 块
        System.out.println("演示 3：finally 块（无论是否异常都执行）");
        System.out.println("─".repeat(50));

        SafeFileLoader safeLoader = new SafeFileLoader();

        System.out.println("文件存在的情况：");
        safeLoader.loadTasksFromFile("demo.txt");
        System.out.println();

        System.out.println("文件不存在的情况：");
        safeLoader.loadTasksFromFile("missing.txt");
        System.out.println();

        // 演示 4：用户输入异常
        System.out.println("演示 4：处理用户输入异常（模拟）");
        System.out.println("─".repeat(50));

        UserInputHandler inputHandler = new UserInputHandler();

        System.out.println("模拟用户输入 'abc' 而不是数字：");
        // 由于无法真正模拟用户输入，这里只是展示代码逻辑
        System.out.println("  代码：scanner.nextInt() 会抛出 InputMismatchException");
        System.out.println("  处理：捕获异常，提示用户，清除错误输入");
        System.out.println();

        // 清理临时文件
        deleteTempFile("demo.txt");

        // 总结
        System.out.println("=== 关键概念 ===");
        System.out.println();
        System.out.println("try-catch-finally 执行流程：");
        System.out.println("  try    : 放置可能抛出异常的代码");
        System.out.println("  catch  : 处理特定类型的异常");
        System.out.println("  finally: 无论是否异常都执行（资源清理）");
        System.out.println();
        System.out.println("多 catch 块的顺序规则：");
        System.out.println("  • 子类异常的 catch 块要放在父类之前");
        System.out.println("  • 如果先 catch Exception，后面更具体的 catch 不会执行");
        System.out.println();
        System.out.println("异常处理的目标：");
        System.out.println("  • 把'程序崩溃'变成'用户友好的错误提示'");
        System.out.println("  • 优雅降级，而不是直接退出");
    }

    // 辅助方法：创建临时文件
    private static void createTempFile(String filename, String content) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            // 忽略
        }
    }

    // 辅助方法：删除临时文件
    private static void deleteTempFile(String filename) {
        new File(filename).delete();
    }
}
