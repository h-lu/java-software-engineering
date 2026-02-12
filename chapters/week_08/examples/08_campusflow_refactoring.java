/*
 * 示例：CampusFlow 代码重构示例。
 * 本例演示：从"上帝类"到职责分离的重构过程。
 * 运行方式：javac 08_campusflow_refactoring.java && java CampusFlowRefactoringDemo
 * 预期输出：展示 CampusFlow 中 God Class 的重构过程
 */

import java.sql.*;
import java.util.*;

// ==================== 重构前：上帝类版本 ====================

// 文件：CampusFlowServiceBefore.java（上帝类 - 反例）
// 问题：一个类承担了所有职责
class CampusFlowServiceBefore {
    private final String dbUrl = "jdbc:sqlite:campusflow.db";
    private final Map<String, Object> cache = new HashMap<>();

    // 任务管理
    public void createTask(String title, String description, String priority, String assignee) {
        // 输入验证
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (!isValidPriority(priority)) {
            throw new IllegalArgumentException("无效的优先级");
        }

        // 生成ID
        String id = UUID.randomUUID().toString();

        // 数据库操作
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO tasks (id, title, description, priority, assignee, status) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, priority);
            pstmt.setString(5, assignee);
            pstmt.setString(6, "pending");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("创建任务失败", e);
        }

        // 更新缓存
        cache.put(id, new Object[]{title, priority, assignee});

        // 发送通知
        System.out.println("[通知] 任务 '" + title + "' 已分配给 " + assignee);

        // 记录日志
        System.out.println("[日志] 创建任务: " + id);
    }

    // 用户管理
    public void createUser(String username, String email, String role) {
        // 又是类似的验证、数据库操作、缓存、通知、日志...
        System.out.println("[CampusFlow] 创建用户: " + username);
    }

    // 报表生成
    public String generateReport(String type, String dateRange) {
        // 复杂的报表逻辑...
        return "报表内容";
    }

    // 费用计算
    public double calculateFee(String taskId, int days) {
        // 费用计算逻辑...
        return days * 10.0;
    }

    // 缓存管理
    public void clearCache() { cache.clear(); }

    // 辅助方法
    private boolean isValidPriority(String priority) {
        return priority != null &&
               (priority.equals("urgent") || priority.equals("high") ||
                priority.equals("normal") || priority.equals("low"));
    }
}

// ==================== 重构后：职责分离版本 ====================

// 文件：Task.java（实体类）
class CampusFlowTask {
    private final String id;
    private final String title;
    private final String description;
    private final String priority;
    private final String assignee;
    private String status;

    public CampusFlowTask(String id, String title, String description,
                          String priority, String assignee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.assignee = assignee;
        this.status = "pending";
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getAssignee() { return assignee; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

// 文件：TaskRepository.java（数据访问层）
interface CampusFlowTaskRepository {
    void save(CampusFlowTask task);
    Optional<CampusFlowTask> findById(String id);
    List<CampusFlowTask> findByAssignee(String assignee);
}

// 文件：InMemoryTaskRepository.java（内存实现 - 用于演示）
class CampusFlowInMemoryTaskRepository implements CampusFlowTaskRepository {
    private final Map<String, CampusFlowTask> tasks = new HashMap<>();

    @Override
    public void save(CampusFlowTask task) {
        tasks.put(task.getId(), task);
        System.out.println("  [Repository] 任务已保存到内存存储: " + task.getId());
    }

    @Override
    public Optional<CampusFlowTask> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<CampusFlowTask> findByAssignee(String assignee) {
        return tasks.values().stream()
            .filter(t -> t.getAssignee().equals(assignee))
            .toList();
    }
}

// 文件：TaskValidator.java（输入验证）
class CampusFlowTaskValidator {
    public void validateCreateTask(String title, String priority) {
        validateTitle(title);
        validatePriority(priority);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
    }

    private void validatePriority(String priority) {
        Set<String> validPriorities = Set.of("urgent", "high", "normal", "low");
        if (!validPriorities.contains(priority)) {
            throw new IllegalArgumentException("无效的优先级: " + priority);
        }
    }
}

// 文件：NotificationService.java（通知服务）
class CampusFlowNotificationService {
    public void sendTaskAssignedNotification(String taskTitle, String assignee) {
        System.out.println("[通知] 任务 '" + taskTitle + "' 已分配给 " + assignee);
    }
}

// 文件：Logger.java（日志服务）
class CampusFlowLogger {
    public void logTaskCreated(String taskId) {
        System.out.println("[日志] 创建任务: " + taskId);
    }
}

// 文件：IdGenerator.java（ID生成）
class CampusFlowIdGenerator {
    public String generateTaskId() {
        return "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

// 文件：TaskService.java（业务逻辑层 - 重构后）
// 只负责业务逻辑，其他职责委托给专门的类
class CampusFlowTaskService {
    private final CampusFlowTaskRepository taskRepository;
    private final CampusFlowTaskValidator validator;
    private final CampusFlowNotificationService notificationService;
    private final CampusFlowLogger logger;
    private final CampusFlowIdGenerator idGenerator;

    public CampusFlowTaskService(CampusFlowTaskRepository taskRepository,
                                  CampusFlowTaskValidator validator,
                                  CampusFlowNotificationService notificationService,
                                  CampusFlowLogger logger,
                                  CampusFlowIdGenerator idGenerator) {
        this.taskRepository = taskRepository;
        this.validator = validator;
        this.notificationService = notificationService;
        this.logger = logger;
        this.idGenerator = idGenerator;
    }

    public String createTask(String title, String description, String priority, String assignee) {
        // 1. 验证输入
        validator.validateCreateTask(title, priority);

        // 2. 生成ID
        String id = idGenerator.generateTaskId();

        // 3. 创建任务对象
        CampusFlowTask task = new CampusFlowTask(id, title, description, priority, assignee);

        // 4. 保存（委托给Repository）
        taskRepository.save(task);

        // 5. 发送通知（委托给NotificationService）
        notificationService.sendTaskAssignedNotification(title, assignee);

        // 6. 记录日志（委托给Logger）
        logger.logTaskCreated(id);

        return id;
    }

    public List<CampusFlowTask> getTasksByAssignee(String assignee) {
        return taskRepository.findByAssignee(assignee);
    }
}

// 文件：CampusFlowRefactoringDemo.java（演示入口）
class CampusFlowRefactoringDemo {
    public static void main(String[] args) {
        System.out.println("=== CampusFlow 代码重构演示 ===\n");

        System.out.println("--- 重构前：上帝类 ---");
        System.out.println("CampusFlowServiceBefore 类的问题：");
        System.out.println("  1. 上帝类：承担了所有职责");
        System.out.println("     - 任务管理 (createTask)");
        System.out.println("     - 用户管理 (createUser)");
        System.out.println("     - 报表生成 (generateReport)");
        System.out.println("     - 费用计算 (calculateFee)");
        System.out.println("     - 缓存管理 (clearCache)");
        System.out.println("     - 输入验证");
        System.out.println("     - 数据库操作");
        System.out.println("     - 通知发送");
        System.out.println("     - 日志记录");
        System.out.println();
        System.out.println("  2. 代码行数：200+ 行");
        System.out.println("  3. 圈复杂度：高");
        System.out.println("  4. 测试困难：无法单独测试各个职责");
        System.out.println();

        System.out.println("--- 重构后：职责分离 ---");
        System.out.println("重构后的类结构：");
        System.out.println();
        System.out.println("  实体层：");
        System.out.println("    - CampusFlowTask：任务实体");
        System.out.println();
        System.out.println("  数据访问层：");
        System.out.println("    - CampusFlowTaskRepository（接口）");
        System.out.println("    - CampusFlowJdbcTaskRepository（实现）");
        System.out.println();
        System.out.println("  业务逻辑层：");
        System.out.println("    - CampusFlowTaskService：只负责业务逻辑");
        System.out.println();
        System.out.println("  支撑服务层：");
        System.out.println("    - CampusFlowTaskValidator：输入验证");
        System.out.println("    - CampusFlowNotificationService：通知发送");
        System.out.println("    - CampusFlowLogger：日志记录");
        System.out.println("    - CampusFlowIdGenerator：ID生成");
        System.out.println();

        System.out.println("--- 重构效果对比 ---");
        System.out.println("指标                重构前          重构后");
        System.out.println("----------------    ----------      ----------");
        System.out.println("类数量              1               8");
        System.out.println("TaskService 行数    200+            50");
        System.out.println("圈复杂度            高              低");
        System.out.println("测试覆盖率          难测试          易测试");
        System.out.println("可维护性            差              好");
        System.out.println();

        // 演示重构后的代码使用
        System.out.println("--- 重构后代码演示 ---");

        // 组装依赖（依赖注入）
        CampusFlowTaskRepository repository = new CampusFlowInMemoryTaskRepository();
        CampusFlowTaskValidator validator = new CampusFlowTaskValidator();
        CampusFlowNotificationService notificationService = new CampusFlowNotificationService();
        CampusFlowLogger logger = new CampusFlowLogger();
        CampusFlowIdGenerator idGenerator = new CampusFlowIdGenerator();

        CampusFlowTaskService taskService = new CampusFlowTaskService(
            repository, validator, notificationService, logger, idGenerator
        );

        // 创建任务
        System.out.println("创建任务：");
        String taskId = taskService.createTask(
            "完成 Week 08 作业",
            "编写代码示例和文档",
            "high",
            "小北"
        );
        System.out.println("任务ID: " + taskId);
        System.out.println();

        // 验证输入
        System.out.println("验证输入：");
        try {
            taskService.createTask("", "描述", "high", "小北");
        } catch (IllegalArgumentException e) {
            System.out.println("捕获验证错误: " + e.getMessage());
        }

        try {
            taskService.createTask("标题", "描述", "invalid", "小北");
        } catch (IllegalArgumentException e) {
            System.out.println("捕获验证错误: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== 重构遵循的原则 ===");
        System.out.println("1. 单一职责原则（SRP）：每个类只有一个改变的理由");
        System.out.println("2. 依赖倒置原则（DIP）：Service 依赖接口，不依赖具体实现");
        System.out.println("3. 开闭原则（OCP）：对扩展开放，对修改关闭");
        System.out.println("4. 依赖注入：通过构造函数注入依赖，便于测试和替换");
    }
}
