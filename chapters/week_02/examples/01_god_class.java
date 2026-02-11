import java.util.*;
import java.io.*;

/**
 * 示例 01：上帝类反示例（God Class Anti-Pattern）
 *
 * 本例演示什么是"上帝类"——一个承担了太多职责的类。
 * 这个类同时负责：数据存储、数据验证、文件操作、显示输出、邮件通知。
 *
 * 运行方式：
 *   javac 01_god_class.java
 *   java GodClassTask
 *
 * 预期输出：
 *   演示一个"什么都能做"的类，但职责混乱，难以维护
 *
 * 问题：
 *   - 职责过多（5+ 个不相关职责）
 *   - 方法数量过多（20+ 个方法）
 *   - 代码行数过多（500+ 行）
 *   - 可测试性差（依赖太多）
 *   - 可维护性低（改一个功能可能影响多个地方）
 */

/**
 * ❌ 反示例：上帝类
 *
 * 这个类承担了太多职责：
 * - 职责 1：存储任务数据（title, description, completed, priority）
 * - 职责 2：验证数据合法性（isValid）
 * - 职责 3：文件读写（saveToFile, loadFromFile）
 * - 职责 4：显示输出（print, printAsJson）
 * - 职责 5：邮件通知（sendEmailNotification）
 *
 * 问题：每次需求变更，都需要修改这个类。
 * 例如：把 priority 从字符串改成数字，需要改 5 个地方。
 */
class GodClassTask {
    // ===== 职责 1：数据存储 =====
    public String title;
    public String description;
    public boolean completed;
    public String priority;  // "高"、"中"、"低"
    public String dueDate;

    // ===== 职责 2：数据验证 =====
    public boolean isValid() {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        if (priority == null ||
            (!priority.equals("高") && !priority.equals("中") && !priority.equals("低"))) {
            return false;
        }
        return true;
    }

    // 验证优先级
    public boolean isValidPriority() {
        return priority != null &&
               (priority.equals("高") || priority.equals("中") || priority.equals("低"));
    }

    // 验证日期格式
    public boolean isValidDueDate() {
        if (dueDate == null || dueDate.isEmpty()) {
            return true;  // 可选字段
        }
        // 简单验证：格式应该是 YYYY-MM-DD
        return dueDate.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // ===== 职责 3：文件操作 =====
    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("title=" + title);
            writer.println("description=" + description);
            writer.println("completed=" + completed);
            writer.println("priority=" + priority);
            writer.println("dueDate=" + dueDate);
            System.out.println("任务已保存到 " + filename);
        } catch (IOException e) {
            System.err.println("保存失败: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    switch (key) {
                        case "title":
                            title = value;
                            break;
                        case "description":
                            description = value;
                            break;
                        case "completed":
                            completed = Boolean.parseBoolean(value);
                            break;
                        case "priority":
                            priority = value;
                            break;
                        case "dueDate":
                            dueDate = value;
                            break;
                    }
                }
            }
            System.out.println("任务已从 " + filename + " 加载");
        } catch (IOException e) {
            System.err.println("加载失败: " + e.getMessage());
        }
    }

    // ===== 职责 4：显示输出 =====
    public void print() {
        System.out.println("========== 任务 ==========");
        System.out.println("标题：" + title);
        System.out.println("描述：" + description);
        System.out.println("状态：" + (completed ? "已完成" : "进行中"));
        System.out.println("优先级：" + priority);
        System.out.println("截止日期：" + (dueDate != null ? dueDate : "无"));
        System.out.println("==========================");
    }

    public void printAsJson() {
        System.out.println("{");
        System.out.println("  \"title\": \"" + title + "\",");
        System.out.println("  \"description\": \"" + description + "\",");
        System.out.println("  \"completed\": " + completed + ",");
        System.out.println("  \"priority\": \"" + priority + "\",");
        System.out.println("  \"dueDate\": \"" + (dueDate != null ? dueDate : "") + "\"");
        System.out.println("}");
    }

    public void printAsCsv() {
        System.out.println(title + "," + description + "," + completed + "," + priority + "," + dueDate);
    }

    public void printShort() {
        System.out.println("[" + priority + "] " + title + " - " + (completed ? "✓" : "○"));
    }

    // ===== 职责 5：邮件通知 =====
    public void sendEmailNotification(String emailAddress) {
        // 模拟发送邮件
        System.out.println("===== 发送邮件通知 =====");
        System.out.println("收件人：" + emailAddress);
        System.out.println("主题：任务更新 - " + title);
        System.out.println("正文：");
        System.out.println("  您的任务 \"" + title + "\" 状态已更新");
        System.out.println("  当前状态：" + (completed ? "已完成" : "进行中"));
        System.out.println("========================");
    }

    public void sendEmailNotification(String emailAddress, String subject, String body) {
        System.out.println("===== 发送邮件通知 =====");
        System.out.println("收件人：" + emailAddress);
        System.out.println("主题：" + subject);
        System.out.println("正文：" + body);
        System.out.println("========================");
    }

    // ===== 还可以加更多职责... =====
    // 例如：设置提醒、分享任务、生成统计报告...

    public static void main(String[] args) {
        // 演示上帝类的使用
        GodClassTask task = new GodClassTask();
        task.title = "完成作业";
        task.description = "编写 Java 类设计练习";
        task.completed = false;
        task.priority = "高";
        task.dueDate = "2026-02-15";

        System.out.println("=== 演示上帝类的问题 ===\n");

        // 调用各种职责的方法
        System.out.println("1. 验证数据：");
        System.out.println("   是否合法：" + task.isValid());

        System.out.println("\n2. 显示输出：");
        task.print();

        System.out.println("\n3. JSON 格式：");
        task.printAsJson();

        System.out.println("\n4. 简短格式：");
        task.printShort();

        System.out.println("\n5. 发送通知：");
        task.sendEmailNotification("student@example.com");

        System.out.println("\n=== 问题分析 ===");
        System.out.println("这个类承担了 5+ 个职责：");
        System.out.println("  - 数据存储");
        System.out.println("  - 数据验证");
        System.out.println("  - 文件操作");
        System.out.println("  - 显示输出");
        System.out.println("  - 邮件通知");
        System.out.println("");
        System.out.println("问题：");
        System.out.println("  ✗ 每次需求变更都要修改这个类");
        System.out.println("  ✗ 难以测试（依赖太多外部资源）");
        System.out.println("  ✗ 代码行数过多（500+ 行）");
        System.out.println("  ✗ 方法数量过多（20+ 个）");
        System.out.println("");
        System.out.println("应该拆分成多个职责单一的类！");
    }
}
