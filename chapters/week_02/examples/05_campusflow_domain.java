import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 示例 05：CampusFlow 领域模型设计（Week 02 进度）
 *
 * 本例演示 CampusFlow 项目 Week 02 的核心领域模型设计。
 * CampusFlow 是一个团队项目框架，每 2-3 人团队选择一个具体场景，
 * 实现一个真实可用的 Web 应用。
 *
 * 本示例以 TaskFlow（个人任务管理）为选题，展示：
 * - 核心实体类设计（Task）
 * - 管理类设计（TaskManager）
 * - 验证类设计（TaskValidator）
 * - 显示类设计（TaskPrinter）
 *
 * 运行方式：
 *   javac 05_campusflow_domain.java
 *   java CampusFlowDemo
 *
 * 预期输出：
 *   交互式任务管理菜单，展示领域模型的使用
 */

// ===== 核心实体类 =====

/**
 * Task：任务实体类
 *
 * 职责：存储任务数据（封装）
 * 设计原则：
 * - 所有字段都是 private（封装）
 * - 大部分字段是 final（不可变，避免并发问题）
 * - 只有 completed 状态可以修改
 */
class Task {
    private final String title;          // 任务标题（不可变）
    private final String description;    // 任务描述（不可变）
    private final String priority;       // 优先级：高/中/低（不可变）
    private final String dueDate;        // 截止日期（不可变）
    private boolean completed;           // 完成状态（可变）

    public Task(String title, String description, String priority, String dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = false;
    }

    // Getter 方法（只读）
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    // 状态修改方法
    public void markCompleted() {
        this.completed = true;
    }

    public void markIncomplete() {
        this.completed = false;
    }
}

/**
 * Category：分类实体类（可选扩展）
 *
 * 职责：存储分类信息
 */
class Category {
    private final String name;
    private final String color;

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}

// ===== 管理类 =====

/**
 * TaskManager：任务管理器
 *
 * 职责：管理任务的增删改查（服务）
 * 设计原则：
 * - 单一职责：只负责任务管理，不负责存储和显示
 * - 对修改关闭：通过扩展来支持新功能
 */
class TaskManager {
    private final List<Task> tasks;
    private final TaskValidator validator;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.validator = new TaskValidator();
    }

    /**
     * 添加任务（带验证）
     */
    public boolean addTask(Task task) {
        if (!validator.isValid(task)) {
            return false;
        }
        tasks.add(task);
        return true;
    }

    /**
     * 标记任务完成
     * @return true 如果找到并标记成功，false 如果任务不存在
     */
    public boolean markCompleted(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                task.markCompleted();
                return true;
            }
        }
        // 任务不存在时返回 false，调用者应处理这种情况
        return false;
    }

    /**
     * 获取所有任务
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * 获取未完成的任务
     */
    public List<Task> getIncompleteTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * 按优先级过滤任务
     */
    public List<Task> getTasksByPriority(String priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority().equals(priority)) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * 统计任务数量
     */
    public int getTotalCount() {
        return tasks.size();
    }

    public int getCompletedCount() {
        int count = 0;
        for (Task task : tasks) {
            if (task.isCompleted()) {
                count++;
            }
        }
        return count;
    }
}

// ===== 验证类 =====

/**
 * TaskValidator：任务验证器
 *
 * 职责：验证任务数据是否合法（验证）
 * 设计原则：
 * - 单一职责：只负责验证，不负责存储和管理
 * - 静态方法：无状态，可以设计成工具类
 */
class TaskValidator {

    /**
     * 验证任务是否合法
     */
    public boolean isValid(Task task) {
        if (task == null) {
            return false;
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            return false;
        }
        if (!isValidPriority(task.getPriority())) {
            return false;
        }
        return true;
    }

    /**
     * 验证优先级是否合法
     */
    public boolean isValidPriority(String priority) {
        return priority != null &&
               (priority.equals("高") || priority.equals("中") || priority.equals("低"));
    }
}

// ===== 显示类 =====

/**
 * TaskPrinter：任务打印器
 *
 * 职责：负责任务的格式化输出（显示）
 * 设计原则：
 * - 单一职责：只负责显示，不负责存储和管理
 * - 静态方法：无状态
 */
class TaskPrinter {

    /**
     * 打印单个任务
     */
    public static void print(Task task) {
        System.out.println("────────────────────────────");
        System.out.println("任务：" + task.getTitle());
        System.out.println("  描述：" + task.getDescription());
        System.out.println("  优先级：" + task.getPriority());
        System.out.println("  截止日期：" + task.getDueDate());
        System.out.println("  状态：" + (task.isCompleted() ? "✓ 已完成" : "○ 进行中"));
    }

    /**
     * 打印任务列表（简短格式）
     */
    public static void printList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("  （无任务）");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String status = task.isCompleted() ? "✓" : "○";
            System.out.printf("  %d. [%s] %s (%s)%n",
                (i + 1),
                task.getPriority(),
                task.getTitle(),
                status
            );
        }
    }

    /**
     * 打印统计信息
     */
    public static void printStatistics(TaskManager manager) {
        int total = manager.getTotalCount();
        int completed = manager.getCompletedCount();
        int incomplete = total - completed;

        System.out.println("────────────────────────────");
        System.out.println("统计信息：");
        System.out.println("  总任务数：" + total);
        System.out.println("  已完成：" + completed);
        System.out.println("  进行中：" + incomplete);
        System.out.println("  完成率：" + (total > 0 ? (completed * 100 / total) : 0) + "%");
    }
}

// ===== 演示类：交互式菜单 =====

/**
 * CampusFlow Demo：交互式任务管理演示
 *
 * 演示领域模型的使用，展示职责分离的设计
 */
class CampusFlowDemo {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     CampusFlow - TaskFlow 演示        ║");
        System.out.println("║     Week 02: 领域模型设计             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        // 添加一些初始任务
        taskManager.addTask(new Task("写作业", "完成 Java 类设计练习", "高", "2026-02-15"));
        taskManager.addTask(new Task("复习 Java", "复习 Week 01 和 Week 02", "中", "2026-02-14"));
        taskManager.addTask(new Task("做项目", "CampusFlow 项目设计", "高", "2026-02-20"));

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("请选择 (1-6)：", 1, 6);

            switch (choice) {
                case 1:
                    listAllTasks();
                    break;
                case 2:
                    listIncompleteTasks();
                    break;
                case 3:
                    addNewTask();
                    break;
                case 4:
                    markTaskCompleted();
                    break;
                case 5:
                    listTasksByPriority();
                    break;
                case 6:
                    running = false;
                    System.out.println("再见！");
                    break;
            }
            System.out.println();
        }

        scanner.close();
    }

    /**
     * 打印菜单
     */
    private static void printMenu() {
        System.out.println("┌──────────────────────────────────┐");
        System.out.println("│           主菜单                  │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│ 1. 查看所有任务                  │");
        System.out.println("│ 2. 查看未完成任务                │");
        System.out.println("│ 3. 添加新任务                    │");
        System.out.println("│ 4. 标记任务完成                  │");
        System.out.println("│ 5. 按优先级查看                  │");
        System.out.println("│ 6. 退出                          │");
        System.out.println("└──────────────────────────────────┘");
    }

    /**
     * 列出所有任务
     */
    private static void listAllTasks() {
        System.out.println("\n>>> 所有任务");
        TaskPrinter.printList(taskManager.getAllTasks());
        TaskPrinter.printStatistics(taskManager);
    }

    /**
     * 列出未完成的任务
     */
    private static void listIncompleteTasks() {
        System.out.println("\n>>> 未完成的任务");
        TaskPrinter.printList(taskManager.getIncompleteTasks());
    }

    /**
     * 添加新任务
     */
    private static void addNewTask() {
        System.out.println("\n>>> 添加新任务");
        System.out.print("  标题：");
        String title = scanner.nextLine();

        System.out.print("  描述：");
        String description = scanner.nextLine();

        System.out.print("  优先级（高/中/低）：");
        String priority = scanner.nextLine();

        System.out.print("  截止日期（YYYY-MM-DD）：");
        String dueDate = scanner.nextLine();

        Task task = new Task(title, description, priority, dueDate);
        if (taskManager.addTask(task)) {
            System.out.println("  ✓ 任务添加成功");
        } else {
            System.out.println("  ✗ 任务添加失败（数据不合法）");
        }
    }

    /**
     * 标记任务完成
     */
    private static void markTaskCompleted() {
        System.out.println("\n>>> 标记任务完成");
        List<Task> incompleteTasks = taskManager.getIncompleteTasks();

        if (incompleteTasks.isEmpty()) {
            System.out.println("  没有未完成的任务");
            return;
        }

        TaskPrinter.printList(incompleteTasks);
        int index = getIntInput("  请输入任务编号：", 1, incompleteTasks.size());

        Task task = incompleteTasks.get(index - 1);
        if (taskManager.markCompleted(task.getTitle())) {
            System.out.println("  ✓ 任务已标记为完成");
        } else {
            System.out.println("  ✗ 操作失败");
        }
    }

    /**
     * 按优先级查看任务
     */
    private static void listTasksByPriority() {
        System.out.println("\n>>> 按优先级查看");
        System.out.println("  1. 高优先级");
        System.out.println("  2. 中优先级");
        System.out.println("  3. 低优先级");

        int choice = getIntInput("  请选择：", 1, 3);
        String priority = (choice == 1) ? "高" : (choice == 2) ? "中" : "低";

        System.out.println("\n  " + priority + "优先级任务：");
        TaskPrinter.printList(taskManager.getTasksByPriority(priority));
    }

    /**
     * 获取整数输入（带验证）
     */
    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("  请输入 " + min + " 到 " + max + " 之间的数字");
            } catch (NumberFormatException e) {
                System.out.println("  请输入有效的数字");
            }
        }
    }
}
