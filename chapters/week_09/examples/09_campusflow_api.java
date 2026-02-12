/*
 * 示例：CampusFlow Web 化改造——从 CLI 到 REST API。
 * 本例演示：将 Week 08 的 CampusFlow CLI 版改造为 Web 服务。
 * 运行方式：mvn -q -f chapters/week_09/starter_code/pom.xml compile exec:java \
 *          -Dexec.mainClass="examples._09_campusflow_api"
 * 预期输出：服务启动，提供 CampusFlow 的 REST API
 *
 * 本周改造重点：
 *   1. 复用 Week 08 的 Service 和 Repository 层
 *   2. 新增 Controller 层处理 HTTP 请求
 *   3. 实现统一的异常处理
 *   4. 复用 Week 08 的策略模式计算逾期费用
 *
 * 测试命令：
 *   # 获取所有任务
 *   curl http://localhost:7070/tasks
 *
 *   # 创建任务
 *   curl -X POST http://localhost:7070/tasks \
 *     -H "Content-Type: application/json" \
 *     -d '{"title":"完成Week09作业","description":"编写代码示例","dueDate":"2026-02-20"}'
 *
 *   # 计算逾期费用（复用 Week 08 策略模式）
 *   curl http://localhost:7070/tasks/1/overdue-fee
 */
package examples;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CampusFlow Web 版入口。
 *
 * <p>架构演进（Week 08 → Week 09）：
 * <pre>
 * Week 08 (CLI):
 *   CLI 层 → Service 层 → Repository 层
 *      ↓
 * Week 09 (Web):
 *   Controller 层 → Service 层 → Repository 层
 *        ↑
 *   HTTP 请求/响应
 * </pre>
 *
 * <p>关键设计决策：
 * <ul>
 *   <li>Service 和 Repository 层完全复用 Week 08 代码</li>
 *   <li>新增 Controller 层作为 HTTP 翻译层</li>
 *   <li>统一异常处理，返回标准错误格式</li>
 *   <li>复用 Week 08 的策略模式计算逾期费用</li>
 * </ul>
 */
public class _09_campusflow_api {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     CampusFlow Web 版 - REST API 服务                    ║");
        System.out.println("║     Week 09：从 CLI 到 Web 的进化                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // 初始化各层组件（依赖注入）
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(repository);
        TaskController controller = new TaskController(taskService);

        // 预置一些测试数据
        seedData(taskService);

        // 创建 Javalin 应用
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        // ===== 健康检查 =====
        app.get("/health", ctx -> ctx.json(Map.of(
            "service", "CampusFlow",
            "version", "2.0.0-web",
            "status", "UP",
            "features", List.of("task-management", "overdue-fee-calculation")
        )));

        // ===== 任务管理 API =====
        app.get("/tasks", controller::getAllTasks);              // 获取所有任务
        app.get("/tasks/{id}", controller::getTask);             // 获取指定任务
        app.post("/tasks", controller::createTask);              // 创建任务
        app.put("/tasks/{id}", controller::updateTask);          // 更新任务
        app.patch("/tasks/{id}", controller::patchTask);         // 部分更新
        app.delete("/tasks/{id}", controller::deleteTask);       // 删除任务

        // ===== 业务功能 API（复用 Week 08 策略模式） =====
        app.get("/tasks/{id}/overdue-fee", controller::calculateOverdueFee);
        app.post("/tasks/{id}/complete", controller::completeTask);

        // ===== 统计 API =====
        app.get("/stats", controller::getStats);

        // ===== 异常处理 =====
        app.exception(NotFoundException.class, (e, ctx) -> {
            ctx.status(404).json(Map.of(
                "code", 404,
                "message", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        });

        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400).json(Map.of(
                "code", 400,
                "message", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        });

        app.exception(Exception.class, (e, ctx) -> {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                "code", 500,
                "message", "Internal server error",
                "timestamp", System.currentTimeMillis()
            ));
        });

        // 启动服务
        app.start(7070);

        System.out.println("✅ CampusFlow Web 服务已启动！");
        System.out.println();
        System.out.println("API 端点：");
        System.out.println("  基础：");
        System.out.println("    GET    /health              - 健康检查");
        System.out.println("    GET    /stats               - 统计信息");
        System.out.println();
        System.out.println("  任务管理：");
        System.out.println("    GET    /tasks               - 获取所有任务");
        System.out.println("    GET    /tasks/{id}          - 获取指定任务");
        System.out.println("    POST   /tasks               - 创建任务");
        System.out.println("    PUT    /tasks/{id}          - 全量更新任务");
        System.out.println("    PATCH  /tasks/{id}          - 部分更新任务");
        System.out.println("    DELETE /tasks/{id}          - 删除任务");
        System.out.println();
        System.out.println("  业务功能（复用 Week 08 策略模式）：");
        System.out.println("    GET    /tasks/{id}/overdue-fee  - 计算逾期费用");
        System.out.println("    POST   /tasks/{id}/complete     - 标记任务完成");
        System.out.println();
        System.out.println("测试命令：");
        System.out.println("  curl http://localhost:7070/tasks");
        System.out.println("  curl -X POST http://localhost:7070/tasks \\");
        System.out.println("    -H 'Content-Type: application/json' \\");
        System.out.println("    -d '{\"title\":\"新任务\",\"description\":\"描述\",\"dueDate\":\"2026-02-20\"}'");
        System.out.println("  curl http://localhost:7070/tasks/1/overdue-fee");
        System.out.println();
        System.out.println("服务地址: http://localhost:7070");
        System.out.println("按 Ctrl+C 停止服务");
    }

    private static void seedData(TaskService service) {
        // 预置一些任务，包含已逾期和未逾期的
        Task task1 = service.createTask(new TaskRequest(
            "完成 Week 09 作业",
            "编写 Javalin 示例代码",
            LocalDate.now().minusDays(2).toString()  // 已逾期
        ));

        Task task2 = service.createTask(new TaskRequest(
            "准备技术分享",
            "REST API 设计最佳实践",
            LocalDate.now().plusDays(3).toString()   // 未逾期
        ));

        Task task3 = service.createTask(new TaskRequest(
            "代码审查",
            "审查团队成员的 PR",
            LocalDate.now().minusDays(5).toString()  // 已逾期较久
        ));

        System.out.println("预置数据：");
        System.out.println("  任务 1: " + task1.getTitle() + " (逾期 " + ChronoUnit.DAYS.between(task1.getDueDate(), LocalDate.now()) + " 天)");
        System.out.println("  任务 2: " + task2.getTitle() + " (还有 " + ChronoUnit.DAYS.between(LocalDate.now(), task2.getDueDate()) + " 天)");
        System.out.println("  任务 3: " + task3.getTitle() + " (逾期 " + ChronoUnit.DAYS.between(task3.getDueDate(), LocalDate.now()) + " 天)");
        System.out.println();
    }
}

// ===== 实体类（Week 08 复用 + Web 适配） =====

/**
 * 任务实体（复用 Week 08，添加 JSON 支持）。
 */
class Task {
    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;  // pending, in_progress, completed
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public Task() {}

    public Task(String id, String title, String description, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = "pending";
        this.createdAt = LocalDateTime.now();
    }

    // Getters 和 Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public boolean isOverdue() {
        return !"completed".equals(status) && dueDate.isBefore(LocalDate.now());
    }

    public long getOverdueDays() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
}

// ===== DTO 类（用于请求/响应） =====

/**
 * 创建任务请求。
 */
class TaskRequest {
    private String title;
    private String description;
    private String dueDate;  // ISO-8601 格式: 2026-02-20

    public TaskRequest() {}

    public TaskRequest(String title, String description, String dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
}

/**
 * 逾期费用响应。
 */
class OverdueFeeResponse {
    private final String taskId;
    private final String taskTitle;
    private final long overdueDays;
    private final double fee;
    private final String calculationStrategy;

    public OverdueFeeResponse(String taskId, String taskTitle, long overdueDays,
                               double fee, String calculationStrategy) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.overdueDays = overdueDays;
        this.fee = fee;
        this.calculationStrategy = calculationStrategy;
    }

    public String getTaskId() { return taskId; }
    public String getTaskTitle() { return taskTitle; }
    public long getOverdueDays() { return overdueDays; }
    public double getFee() { return fee; }
    public String getCalculationStrategy() { return calculationStrategy; }
}

// ===== Repository 层（复用 Week 08 模式） =====

interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(String id);
    List<Task> findAll();
    List<Task> findByStatus(String status);
    void delete(String id);
    long count();
}

class InMemoryTaskRepository implements TaskRepository {
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(String.valueOf(nextId.getAndIncrement()));
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> findByStatus(String status) {
        return tasks.values().stream()
            .filter(t -> status.equals(t.getStatus()))
            .toList();
    }

    @Override
    public void delete(String id) {
        tasks.remove(id);
    }

    @Override
    public long count() {
        return tasks.size();
    }
}

// ===== Service 层（复用 Week 08 业务逻辑） =====

class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task createTask(TaskRequest request) {
        // 验证
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new ValidationException("Title is required");
        }

        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(request.getDueDate());
        } catch (Exception e) {
            throw new ValidationException("Invalid dueDate format, expected: YYYY-MM-DD");
        }

        Task task = new Task(null, request.getTitle(), request.getDescription(), dueDate);
        return repository.save(task);
    }

    public Optional<Task> findById(String id) {
        return repository.findById(id);
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Task updateTask(String id, TaskRequest request) {
        Task existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));

        if (request.getTitle() != null) {
            existing.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }
        if (request.getDueDate() != null) {
            existing.setDueDate(LocalDate.parse(request.getDueDate()));
        }

        return repository.save(existing);
    }

    public void deleteTask(String id) {
        Task existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));
        repository.delete(id);
    }

    public Task completeTask(String id) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));

        task.setStatus("completed");
        task.setCompletedAt(LocalDateTime.now());
        return repository.save(task);
    }

    /**
     * 计算逾期费用（复用 Week 08 策略模式思想）。
     */
    public double calculateOverdueFee(String id) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));

        if (!task.isOverdue()) {
            return 0.0;
        }

        long overdueDays = task.getOverdueDays();

        // 使用 Week 08 的策略模式思想：根据逾期天数选择不同费率
        FeeCalculationStrategy strategy = selectStrategy(overdueDays);
        return strategy.calculate(overdueDays);
    }

    public String getCalculationStrategyName(String id) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));

        if (!task.isOverdue()) {
            return "NoOverdueStrategy";
        }

        FeeCalculationStrategy strategy = selectStrategy(task.getOverdueDays());
        return strategy.getClass().getSimpleName();
    }

    private FeeCalculationStrategy selectStrategy(long overdueDays) {
        if (overdueDays <= 3) {
            return new StandardFeeStrategy();
        } else if (overdueDays <= 7) {
            return new EscalatingFeeStrategy();
        } else {
            return new SevereFeeStrategy();
        }
    }

    public Map<String, Object> getStats() {
        List<Task> all = repository.findAll();
        long total = all.size();
        long pending = all.stream().filter(t -> "pending".equals(t.getStatus())).count();
        long inProgress = all.stream().filter(t -> "in_progress".equals(t.getStatus())).count();
        long completed = all.stream().filter(t -> "completed".equals(t.getStatus())).count();
        long overdue = all.stream().filter(Task::isOverdue).count();

        return Map.of(
            "total", total,
            "pending", pending,
            "inProgress", inProgress,
            "completed", completed,
            "overdue", overdue
        );
    }
}

// ===== 策略模式：逾期费用计算（复用 Week 08） =====

interface FeeCalculationStrategy {
    double calculate(long overdueDays);
}

/**
 * 标准费率：每天 10 元。
 */
class StandardFeeStrategy implements FeeCalculationStrategy {
    private static final double DAILY_RATE = 10.0;

    @Override
    public double calculate(long overdueDays) {
        return overdueDays * DAILY_RATE;
    }
}

/**
 * 递升费率：前 3 天每天 10 元，之后每天 20 元。
 */
class EscalatingFeeStrategy implements FeeCalculationStrategy {
    private static final double BASE_RATE = 10.0;
    private static final double ESCALATED_RATE = 20.0;
    private static final int ESCALATION_THRESHOLD = 3;

    @Override
    public double calculate(long overdueDays) {
        long baseDays = Math.min(overdueDays, ESCALATION_THRESHOLD);
        long escalatedDays = Math.max(0, overdueDays - ESCALATION_THRESHOLD);
        return baseDays * BASE_RATE + escalatedDays * ESCALATED_RATE;
    }
}

/**
 * 严厉费率：前 3 天每天 10 元，4-7 天每天 20 元，之后每天 50 元。
 */
class SevereFeeStrategy implements FeeCalculationStrategy {
    @Override
    public double calculate(long overdueDays) {
        if (overdueDays <= 3) {
            return overdueDays * 10.0;
        } else if (overdueDays <= 7) {
            return 3 * 10.0 + (overdueDays - 3) * 20.0;
        } else {
            return 3 * 10.0 + 4 * 20.0 + (overdueDays - 7) * 50.0;
        }
    }
}

// ===== Controller 层（新增：HTTP 翻译层） =====

class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void getAllTasks(Context ctx) {
        List<Task> tasks = taskService.findAll();
        ctx.json(Map.of(
            "data", tasks,
            "total", tasks.size()
        ));
    }

    public void getTask(Context ctx) {
        String id = ctx.pathParam("id");
        Task task = taskService.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));
        ctx.json(task);
    }

    public void createTask(Context ctx) {
        TaskRequest request = ctx.bodyAsClass(TaskRequest.class);
        Task created = taskService.createTask(request);
        ctx.status(201).json(created);
    }

    public void updateTask(Context ctx) {
        String id = ctx.pathParam("id");
        TaskRequest request = ctx.bodyAsClass(TaskRequest.class);
        Task updated = taskService.updateTask(id, request);
        ctx.json(updated);
    }

    public void patchTask(Context ctx) {
        String id = ctx.pathParam("id");
        // 使用 Map 接收部分字段
        Map<String, Object> updates = ctx.bodyAsClass(Map.class);

        TaskRequest request = new TaskRequest();
        if (updates.containsKey("title")) {
            request.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description")) {
            request.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("dueDate")) {
            request.setDueDate((String) updates.get("dueDate"));
        }

        Task updated = taskService.updateTask(id, request);
        ctx.json(updated);
    }

    public void deleteTask(Context ctx) {
        String id = ctx.pathParam("id");
        taskService.deleteTask(id);
        ctx.status(204);
    }

    public void calculateOverdueFee(Context ctx) {
        String id = ctx.pathParam("id");
        Task task = taskService.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));

        double fee = taskService.calculateOverdueFee(id);
        String strategyName = taskService.getCalculationStrategyName(id);

        OverdueFeeResponse response = new OverdueFeeResponse(
            id,
            task.getTitle(),
            task.getOverdueDays(),
            fee,
            strategyName
        );

        ctx.json(response);
    }

    public void completeTask(Context ctx) {
        String id = ctx.pathParam("id");
        Task completed = taskService.completeTask(id);
        ctx.json(completed);
    }

    public void getStats(Context ctx) {
        ctx.json(taskService.getStats());
    }
}

// ===== 异常类 =====

class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

/*
 * CampusFlow Web 化改造总结：
 *
 * 1. 复用 Week 08 代码
 *    - Task 实体类（添加 JSON 支持）
 *    - TaskRepository 接口和实现
 *    - TaskService 业务逻辑
 *    - 策略模式（逾期费用计算）
 *
 * 2. 新增代码
 *    - TaskController：HTTP 请求处理
 *    - TaskRequest：请求 DTO
 *    - OverdueFeeResponse：响应 DTO
 *    - 异常处理配置
 *    - Javalin 路由配置
 *
 * 3. 架构优势
 *    - 分层清晰：Controller → Service → Repository
 *    - 业务逻辑复用：Week 08 代码几乎未改动
 *    - 易于测试：各层可独立测试
 *    - 易于扩展：新增端点只需添加 Controller 方法
 *
 * 4. 与 Week 08 CLI 版对比
 *    - CLI 版：命令行交互，本地运行
 *    - Web 版：HTTP API，支持远程访问
 *    - 业务逻辑完全一致，只有交互层不同
 */
