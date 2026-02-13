/*
 * 示例：项目集市反馈收集工具
 * 运行方式：javac CampusFlowFeedbackCollector.java && java CampusFlowFeedbackCollector
 * 预期输出：交互式收集反馈，导出到 feedback_report.md
 *
 * Week 16: 项目展示与工程复盘
 * 本例演示如何用简单工具收集和整理项目集市反馈
 */

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 反馈条目
 */
class FeedbackEntry {
    private String category; // KEEP / PROBLEM / TRY
    private String content;
    private String source; // 来源：同学/老师/评委
    private int priority; // 优先级：1-5

    public FeedbackEntry(String category, String content, String source, int priority) {
        this.category = category;
        this.content = content;
        this.source = source;
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getSource() {
        return source;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (来源: %s, 优先级: %d)", category, content, source, priority);
    }
}

/**
 * 反馈收集器
 */
class FeedbackCollector {
    private final String projectName;
    private final List<FeedbackEntry> feedbacks;
    private final LocalDate collectDate;

    public FeedbackCollector(String projectName) {
        this.projectName = projectName;
        this.feedbacks = new ArrayList<>();
        this.collectDate = LocalDate.now();
    }

    /**
     * 添加反馈
     */
    public void addFeedback(String category, String content, String source, int priority) {
        if (!isValidCategory(category)) {
            throw new IllegalArgumentException("分类必须是 KEEP / PROBLEM / TRY");
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("优先级必须在 1-5 之间");
        }
        feedbacks.add(new FeedbackEntry(category, content, source, priority));
    }

    private boolean isValidCategory(String category) {
        return category.equals("KEEP") || category.equals("PROBLEM") || category.equals("TRY");
    }

    /**
     * 按分类统计反馈数量
     */
    public void printStatistics() {
        long keepCount = feedbacks.stream().filter(f -> f.getCategory().equals("KEEP")).count();
        long problemCount = feedbacks.stream().filter(f -> f.getCategory().equals("PROBLEM")).count();
        long tryCount = feedbacks.stream().filter(f -> f.getCategory().equals("TRY")).count();

        System.out.println("\n=== 反馈统计 ===");
        System.out.println("KEEP（做得好）: " + keepCount);
        System.out.println("PROBLEM（问题）: " + problemCount);
        System.out.println("TRY（未来尝试）: " + tryCount);
        System.out.println("总计: " + feedbacks.size());
    }

    /**
     * 导出为 Markdown 报告
     */
    public void exportToMarkdown(String filename) throws IOException {
        StringBuilder report = new StringBuilder();
        report.append("# ").append(projectName).append(" 项目集市反馈报告\n\n");
        report.append("收集日期: ").append(collectDate).append("\n");
        report.append("反馈数量: ").append(feedbacks.size()).append("\n\n");
        report.append("---\n\n");

        // 按分类组织
        report.append("## KEEP（做得好的，继续坚持）\n\n");
        feedbacks.stream()
            .filter(f -> f.getCategory().equals("KEEP"))
            .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
            .forEach(f -> report.append("- **").append(f.getContent())
                .append("** (来源: ").append(f.getSource()).append(")\n"));

        report.append("\n## PROBLEM（遇到的问题，需要改进）\n\n");
        feedbacks.stream()
            .filter(f -> f.getCategory().equals("PROBLEM"))
            .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
            .forEach(f -> report.append("- [优先级").append(f.getPriority()).append("] ")
                .append(f.getContent()).append(" (来源: ").append(f.getSource()).append(")\n"));

        report.append("\n## TRY（下次尝试的方法）\n\n");
        feedbacks.stream()
            .filter(f -> f.getCategory().equals("TRY"))
            .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
            .forEach(f -> report.append("- ").append(f.getContent())
                .append(" (来源: ").append(f.getSource()).append(")\n"));

        report.append("\n---\n\n");
        report.append("## 下一步行动\n\n");
        report.append("### 本周内（P0 - 优先级 5）\n");
        feedbacks.stream()
            .filter(f -> f.getCategory().equals("PROBLEM") && f.getPriority() >= 4)
            .forEach(f -> report.append("- [ ] ").append(f.getContent()).append("\n"));

        report.append("\n### 下周内（P1 - 优先级 3-4）\n");
        feedbacks.stream()
            .filter(f -> f.getCategory().equals("PROBLEM") && f.getPriority() >= 3 && f.getPriority() < 4)
            .forEach(f -> report.append("- [ ] ").append(f.getContent()).append("\n"));

        Files.write(Paths.get(filename), report.toString().getBytes());
        System.out.println("报告已导出到: " + filename);
    }
}

/**
 * 主程序：交互式收集反馈
 */
public class CampusFlowFeedbackCollector {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FeedbackCollector collector = new FeedbackCollector("CampusFlow");

        System.out.println("=== CampusFlow 项目集市反馈收集 ===");
        System.out.println("输入反馈，输入 'done' 完成收集\n");

        while (true) {
            System.out.print("反馈分类 (KEEP/PROBLEM/TRY): ");
            String category = scanner.nextLine().trim().toUpperCase();

            if (category.equals("DONE")) {
                break;
            }

            if (!category.equals("KEEP") && !category.equals("PROBLEM") && !category.equals("TRY")) {
                System.out.println("无效分类，请输入 KEEP / PROBLEM / TRY");
                continue;
            }

            System.out.print("反馈内容: ");
            String content = scanner.nextLine().trim();

            if (content.isEmpty()) {
                System.out.println("内容不能为空");
                continue;
            }

            System.out.print("来源 (同学/老师/评委): ");
            String source = scanner.nextLine().trim();

            System.out.print("优先级 (1-5, 5最高): ");
            int priority;
            try {
                priority = Integer.parseInt(scanner.nextLine());
                if (priority < 1 || priority > 5) {
                    System.out.println("优先级必须在 1-5 之间");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("无效数字");
                continue;
            }

            collector.addFeedback(category, content, source, priority);
            System.out.println("已记录: " + content + "\n");
        }

        collector.printStatistics();

        System.out.print("\n是否导出报告？(y/n): ");
        String export = scanner.nextLine().trim();
        if (export.equalsIgnoreCase("y")) {
            try {
                collector.exportToMarkdown("feedback_report.md");
            } catch (IOException e) {
                System.err.println("导出失败: " + e.getMessage());
            }
        }

        System.out.println("\n感谢参与 CampusFlow 项目集市！");
        scanner.close();
    }
}
