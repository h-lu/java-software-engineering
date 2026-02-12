/*
 * 示例：展示"上帝类"问题的 TaskService（代码坏味道版本）。
 * 本例演示：上帝类、重复代码、长方法、紧耦合等代码坏味道。
 * 运行方式：javac 01_task_service_before.java && java TaskServiceDemo
 * 预期输出：展示代码结构问题，程序可运行但设计糟糕
 */

import java.sql.*;
import java.util.*;

// 文件：Task.java（简化实体类）
class Task {
    private final String id;
    private final String title;
    private final String description;
    private final String priority;
    private final String status;

    public Task(String id, String title, String description, String priority, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
}

// 文件：TaskService.java（上帝类 - 代码坏味道版本）
// 问题：
// 1. 上帝类：承担了业务逻辑、数据访问、输入验证、费用计算、缓存管理、通知发送等所有职责
// 2. 重复代码：数据库连接代码在每个方法里重复
// 3. 长方法：createTask 和 calculateOverdueFee 做了太多事情
// 4. 紧耦合：直接依赖 JDBC 细节
class TaskService {
    private final String dbUrl = "jdbc:sqlite:campusflow.db";
    private final List<Task> cache = new ArrayList<>();

    // ===== 坏味道 1：长方法 + 多职责 =====
    // 这个方法做了：输入验证、生成ID、数据库操作、缓存更新、发送通知
    public void createTask(String title, String description, String priority) {
        // 输入验证（应该分离）
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (priority == null || (!priority.equals("high") && !priority.equals("medium") && !priority.equals("low"))) {
            throw new IllegalArgumentException("优先级必须是 high/medium/low");
        }

        // 生成 ID
        String id = UUID.randomUUID().toString();

        // 数据库操作（直接写在 Service 里！坏味道：紧耦合 + 重复代码）
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO tasks (id, title, description, priority, status) VALUES (?, ?, ?, ?, ?)")) {

            pstmt.setString(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, priority);
            pstmt.setString(5, "pending");
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("创建任务失败", e);
        }

        // 更新缓存（又一项职责！）
        Task task = new Task(id, title, description, priority, "pending");
        cache.add(task);

        // 发送通知（又一项职责！）
        System.out.println("任务已创建: " + title);
    }

    // ===== 坏味道 2：长方法 + 重复代码 =====
    // 这个方法做了：数据库查询、费用计算、复杂的条件判断
    public double calculateOverdueFee(String taskId, int daysOverdue) {
        // 从数据库读取任务（重复的数据库连接代码！）
        Task task = null;
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tasks WHERE id = ?")) {
            pstmt.setString(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                task = new Task(rs.getString("id"), rs.getString("title"),
                              rs.getString("description"), rs.getString("priority"),
                              rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询失败", e);
        }

        if (task == null) return 0.0;

        // 复杂的费用计算逻辑（应该独立出来）
        double baseFee = 10.0;
        String priority = task.getPriority();
        double multiplier = 1.0;

        // 坏味道 3：复杂的条件判断链
        if (priority.equals("high")) {
            multiplier = 3.0;
        } else if (priority.equals("medium")) {
            multiplier = 2.0;
        } else if (priority.equals("low")) {
            multiplier = 1.0;
        }

        // 更多条件判断……
        if (daysOverdue > 30) {
            multiplier *= 1.5;
        } else if (daysOverdue > 7) {
            multiplier *= 1.2;
        }

        return baseFee * daysOverdue * multiplier;
    }

    // ===== 坏味道 4：重复代码再次出现 =====
    public Task findTaskById(String taskId) {
        // 又是同样的数据库连接代码！
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tasks WHERE id = ?")) {
            pstmt.setString(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Task(rs.getString("id"), rs.getString("title"),
                              rs.getString("description"), rs.getString("priority"),
                              rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询失败", e);
        }
        return null;
    }

    // ===== 坏味道 5：长方法，复杂的报表生成 =====
    public String generateTaskReport(String priority) {
        // 又是数据库连接代码！
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT * FROM tasks WHERE priority = ?")) {
            pstmt.setString(1, priority);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tasks.add(new Task(rs.getString("id"), rs.getString("title"),
                                 rs.getString("description"), rs.getString("priority"),
                                 rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询失败", e);
        }

        // 复杂的报表格式化逻辑
        StringBuilder report = new StringBuilder();
        report.append("===== 任务报表 =====\n");
        report.append("优先级: ").append(priority).append("\n");
        report.append("生成时间: ").append(new java.util.Date()).append("\n");
        report.append("任务数量: ").append(tasks.size()).append("\n");
        report.append("--------------------\n");

        for (Task task : tasks) {
            report.append("ID: ").append(task.getId()).append("\n");
            report.append("标题: ").append(task.getTitle()).append("\n");
            report.append("状态: ").append(task.getStatus()).append("\n");
            report.append("--------------------\n");
        }

        return report.toString();
    }

    // 坏味道 6：缓存管理逻辑散落在各处
    public void clearCache() {
        cache.clear();
        System.out.println("缓存已清空");
    }

    public List<Task> getCachedTasks() {
        return new ArrayList<>(cache);
    }
}

// 文件：TaskServiceDemo.java（演示入口）
class TaskServiceDemo {
    public static void main(String[] args) {
        System.out.println("=== TaskService 代码坏味道演示 ===\n");

        TaskService service = new TaskService();

        System.out.println("问题 1：上帝类 - TaskService 承担了太多职责：");
        System.out.println("  - 业务逻辑（createTask）");
        System.out.println("  - 数据访问（JDBC 代码）");
        System.out.println("  - 输入验证");
        System.out.println("  - 费用计算（calculateOverdueFee）");
        System.out.println("  - 缓存管理");
        System.out.println("  - 报表生成（generateTaskReport）");
        System.out.println("  - 通知发送\n");

        System.out.println("问题 2：重复代码 - 数据库连接代码在多个方法中重复出现");
        System.out.println("  - createTask() 中有 JDBC 代码");
        System.out.println("  - calculateOverdueFee() 中有 JDBC 代码");
        System.out.println("  - findTaskById() 中有 JDBC 代码");
        System.out.println("  - generateTaskReport() 中有 JDBC 代码\n");

        System.out.println("问题 3：长方法 - calculateOverdueFee 做了太多事情");
        System.out.println("  - 数据库查询");
        System.out.println("  - 费用计算");
        System.out.println("  - 复杂的条件判断\n");

        System.out.println("问题 4：紧耦合 - Service 层直接依赖 JDBC 细节");
        System.out.println("  - 如果换成 MySQL 或 MongoDB，需要重写整个类\n");

        System.out.println("=== 这个类有 300+ 行，违反了单一职责原则 ===");
        System.out.println("=== 请看 02_task_service_after_refactoring.java 了解如何改进 ===");
    }
}
