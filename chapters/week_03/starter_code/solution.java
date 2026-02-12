/**
 * CampusFlow 异常处理增强版本 - Week 03 参考实现
 *
 * 本文件展示如何为 CampusFlow 添加完整的异常处理机制，包括：
 * - 自定义异常类体系
 * - 防御式输入验证
 * - 异常处理策略
 * - FMEA 风险预防
 *
 * 运行方式：
 *   javac solution.java
 *   java CampusFlowExceptionDemo
 *
 * 预期输出：
 *   交互式任务管理演示，展示异常处理的各种场景
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

// ==================== 自定义异常类 ====================

/**
 * CampusFlow 基础领域异常（受检异常）
 *
 * 所有业务异常的基类，便于上层统一处理
 */
class CampusFlowException extends Exception {

    public CampusFlowException(String message) {
        super(message);
    }

    public CampusFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * 任务不存在异常
 *
 * 当根据标题或ID找不到任务时抛出
 */
class TaskNotFoundException extends CampusFlowException {

    public TaskNotFoundException(String taskTitle) {
        super("找不到任务: " + taskTitle);
    }
}

/**
 * 任务数据无效异常
 *
 * 当任务数据不符合业务规则时抛出
 */
class InvalidTaskDataException extends CampusFlowException {

    public InvalidTaskDataException(String field, String reason) {
        super("任务数据无效 [" + field + "]: " + reason);
    }
}

/**
 * 任务IO异常
 *
 * 文件读写操作失败时抛出
 */
class TaskIOException extends CampusFlowException {

    public TaskIOException(String message, Throwable cause) {
        super(message, cause);
    }
}

// ==================== 任务实体类（带防御式验证） ====================

/**
 * 任务实体类
 *
 * 职责：存储任务数据，保护数据完整性
 * 防御：在 setter 中进行输入验证，预防非法数据
 */
class Task {
    private String title;
    private String description;
    private String dueDate;
    private int priority;  // 1=高, 2=中, 3=低
    private boolean completed;

    /**
     * 构造方法（带验证）
     */
    public Task(String title, String description, String dueDate, int priority)
            throws InvalidTaskDataException {
        setTitle(title);
        setDescription(description);
        setDueDate(dueDate);
        setPriority(priority);
        this.completed = false;
    }

    // ===== Getters =====

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    // ===== Setters with Defensive Validation =====

    /**
     * 设置标题（多层防御）
     *
     * 防御1：null 检查
     * 防御2：空字符串检查
     * 防御3：长度限制
     * 防御4：去除首尾空格
     */
    public void setTitle(String title) throws InvalidTaskDataException {
        if (title == null) {
            throw new InvalidTaskDataException("title", "标题不能为 null");
        }

        String trimmed = title.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidTaskDataException("title", "标题不能为空");
        }
        if (trimmed.length() > 100) {
            throw new InvalidTaskDataException("title", "标题不能超过100字符");
        }

        this.title = trimmed;
    }

    /**
     * 设置描述（可选字段）
     */
    public void setDescription(String description) throws InvalidTaskDataException {
        if (description != null && description.length() > 500) {
            throw new InvalidTaskDataException("description", "描述不能超过500字符");
        }
        this.description = description != null ? description.trim() : "";
    }

    /**
     * 设置截止日期（格式验证）
     */
    public void setDueDate(String dueDate) throws InvalidTaskDataException {
        if (dueDate == null) {
            throw new InvalidTaskDataException("dueDate", "截止日期不能为 null");
        }

        // 格式验证：YYYY-MM-DD
        if (!dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new InvalidTaskDataException("dueDate", "日期格式无效，应为 YYYY-MM-DD");
        }

        // 值验证
        if (!isValidDate(dueDate)) {
            throw new InvalidTaskDataException("dueDate", "日期值无效（如 2025-02-30）");
        }

        this.dueDate = dueDate;
    }

    /**
     * 设置优先级（枚举值验证）
     */
    public void setPriority(int priority) throws InvalidTaskDataException {
        if (priority < 1 || priority > 3) {
            throw new InvalidTaskDataException("priority", "优先级必须是 1（高）、2（中）或 3（低）");
        }
        this.priority = priority;
    }

    // ===== 业务方法 =====

    public void markCompleted() {
        this.completed = true;
    }

    public void markIncomplete() {
        this.completed = false;
    }

    /**
     * 转换为文件存储格式
     */
    public String toFileString() {
        return title + "|" + description + "|" + dueDate + "|" + priority + "|" + completed;
    }

    /**
     * 从文件格式解析
     */
    public static Task fromFileString(String line) throws InvalidTaskDataException {
        String[] parts = line.split("\\|");
        if (parts.length < 4) {
            throw new InvalidTaskDataException("format", "格式错误，需要至少4个字段");
        }

        String title = parts[0];
        String description = parts.length > 1 ? parts[1] : "";
        String dueDate = parts.length > 2 ? parts[2] : "";
        int priority = Integer.parseInt(parts[3]);

        Task task = new Task(title, description, dueDate, priority);
        if (parts.length > 4 && Boolean.parseBoolean(parts[4])) {
            task.markCompleted();
        }
        return task;
    }

    // ===== 辅助方法 =====

    private boolean isValidDate(String date) {
        try {
            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;

            int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            // 简单闰年检查
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                daysInMonth[2] = 29;
            }

            return day <= daysInMonth[month];
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        String priorityStr = priority == 1 ? "高" : priority == 2 ? "中" : "低";
        String statusStr = completed ? "✓ 已完成" : "○ 进行中";
        return String.format("Task{title='%s', dueDate='%s', priority=%s, status=%s}",
                title, dueDate, priorityStr, statusStr);
    }
}

// ==================== 任务管理器（带异常处理） ====================

/**
 * 任务管理器
 *
 * 职责：管理任务的增删改查，处理异常
 * 策略：
 * - 受检异常：文件操作等外部风险
 * - 运行时异常：编程错误（如 null 参数）
 */
class TaskManager {
    private final List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    /**
     * 添加任务
     *
     * @throws IllegalArgumentException 如果 task 为 null
     * @throws InvalidTaskDataException 如果任务数据无效
     */
    public void addTask(Task task) throws InvalidTaskDataException {
        // 防御式检查：null 检查
        if (task == null) {
            throw new IllegalArgumentException("任务不能为 null");
        }

        // 防御式检查：重复检查
        if (findTask(task.getTitle()).isPresent()) {
            throw new InvalidTaskDataException("title", "任务 '" + task.getTitle() + "' 已存在");
        }

        tasks.add(task);
    }

    /**
     * 查找任务（返回 Optional，避免 null）
     *
     * FMEA 策略：任务不存在是正常情况，不抛异常
     */
    public Optional<Task> findTask(String title) {
        return tasks.stream()
                .filter(t -> t.getTitle().equals(title))
                .findFirst();
    }

    /**
     * 获取任务（抛出受检异常）
     *
     * FMEA 策略：强制调用者处理"任务不存在"的情况
     */
    public Task getTask(String title) throws TaskNotFoundException {
        return findTask(title)
                .orElseThrow(() -> new TaskNotFoundException(title));
    }

    /**
     * 标记任务完成
     *
     * @throws TaskNotFoundException 如果任务不存在
     */
    public void markCompleted(String title) throws TaskNotFoundException {
        Task task = getTask(title);
        task.markCompleted();
    }

    /**
     * 从文件加载任务
     *
     * FMEA 策略：
     * - 文件不存在：不是致命错误，返回空列表
     * - 格式错误：记录错误，继续处理其他行
     */
    public void loadFromFile(String filename) throws TaskIOException {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int lineNumber = 0;
            int successCount = 0;
            int errorCount = 0;

            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();

                // 跳过空行
                if (line.isEmpty()) continue;

                try {
                    Task task = Task.fromFileString(line);
                    // 检查重复
                    if (findTask(task.getTitle()).isEmpty()) {
                        tasks.add(task);
                        successCount++;
                    } else {
                        System.err.println("  警告：第 " + lineNumber + " 行任务重复，已跳过");
                        errorCount++;
                    }
                } catch (InvalidTaskDataException e) {
                    // FMEA 策略：记录错误，继续处理其他行
                    System.err.println("  错误：第 " + lineNumber + " 行解析失败: " + e.getMessage());
                    errorCount++;
                }
            }

            System.out.println("  加载完成：成功 " + successCount + " 条，失败 " + errorCount + " 条");

        } catch (FileNotFoundException e) {
            // FMEA 策略：文件不存在不是致命错误
            System.out.println("  提示：文件 '" + filename + "' 不存在，将创建新文件");
        } catch (Exception e) {
            throw new TaskIOException("加载文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 保存任务到文件
     *
     * FMEA 策略：先写入临时文件，成功后再替换
     */
    public void saveToFile(String filename) throws TaskIOException {
        File tempFile = new File(filename + ".tmp");

        try (FileWriter writer = new FileWriter(tempFile)) {
            for (Task task : tasks) {
                writer.write(task.toFileString() + "\n");
            }

            // 成功后再替换原文件
            File targetFile = new File(filename);
            if (!tempFile.renameTo(targetFile)) {
                throw new TaskIOException("无法保存文件：重命名失败", null);
            }

        } catch (IOException e) {
            tempFile.delete();  // 清理临时文件
            throw new TaskIOException("保存文件失败: " + e.getMessage(), e);
        }
    }

    // ===== 查询方法 =====

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getIncompleteTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                result.add(task);
            }
        }
        return result;
    }

    public List<Task> getTasksByPriority(int priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority() == priority) {
                result.add(task);
            }
        }
        return result;
    }

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

// ==================== 任务打印机 ====================

/**
 * 任务打印机
 *
 * 职责：格式化输出任务信息
 */
class TaskPrinter {

    public static void print(Task task) {
        System.out.println("────────────────────────────");
        System.out.println("任务：" + task.getTitle());
        System.out.println("  描述：" + task.getDescription());
        String priorityStr = task.getPriority() == 1 ? "高" :
                            task.getPriority() == 2 ? "中" : "低";
        System.out.println("  优先级：" + priorityStr);
        System.out.println("  截止日期：" + task.getDueDate());
        System.out.println("  状态：" + (task.isCompleted() ? "✓ 已完成" : "○ 进行中"));
    }

    public static void printList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("  （无任务）");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String status = task.isCompleted() ? "✓" : "○";
            String priorityStr = task.getPriority() == 1 ? "高" :
                                task.getPriority() == 2 ? "中" : "低";
            System.out.printf("  %d. [%s] %s (%s)%n",
                    (i + 1), priorityStr, task.getTitle(), status);
        }
    }

    public static void printStatistics(TaskManager manager) {
        System.out.println("────────────────────────────");
        System.out.println("统计信息：");
        System.out.println("  总任务数：" + manager.getTotalCount());
        System.out.println("  已完成：" + manager.getCompletedCount());
        System.out.println("  进行中：" + (manager.getTotalCount() - manager.getCompletedCount()));
        int rate = manager.getTotalCount() > 0
                ? (manager.getCompletedCount() * 100 / manager.getTotalCount())
                : 0;
        System.out.println("  完成率：" + rate + "%");
    }
}

// ==================== 演示主类 ====================

/**
 * CampusFlow 异常处理演示
 */
class CampusFlowExceptionDemo {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = new TaskManager();
    private static final String DATA_FILE = "tasks.txt";

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     CampusFlow - 异常处理增强版       ║");
        System.out.println("║     Week 03: 异常处理与防御式编程     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        // 加载已有任务
        System.out.println("正在加载任务...");
        try {
            taskManager.loadFromFile(DATA_FILE);
        } catch (TaskIOException e) {
            System.out.println("加载失败：" + e.getMessage());
        }
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("请选择 (1-7)：", 1, 7);

            try {
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
                        saveTasks();
                        break;
                    case 6:
                        testExceptionScenarios();
                        break;
                    case 7:
                        running = false;
                        System.out.println("正在保存...");
                        try {
                            taskManager.saveToFile(DATA_FILE);
                            System.out.println("保存成功！");
                        } catch (TaskIOException e) {
                            System.out.println("保存失败：" + e.getMessage());
                        }
                        System.out.println("再见！");
                        break;
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("┌──────────────────────────────────┐");
        System.out.println("│           主菜单                  │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│ 1. 查看所有任务                  │");
        System.out.println("│ 2. 查看未完成任务                │");
        System.out.println("│ 3. 添加新任务                    │");
        System.out.println("│ 4. 标记任务完成                  │");
        System.out.println("│ 5. 保存任务到文件                │");
        System.out.println("│ 6. 测试异常场景                  │");
        System.out.println("│ 7. 退出                          │");
        System.out.println("└──────────────────────────────────┘");
    }

    private static void listAllTasks() {
        System.out.println("\n>>> 所有任务");
        TaskPrinter.printList(taskManager.getAllTasks());
        TaskPrinter.printStatistics(taskManager);
    }

    private static void listIncompleteTasks() {
        System.out.println("\n>>> 未完成的任务");
        TaskPrinter.printList(taskManager.getIncompleteTasks());
    }

    private static void addNewTask() {
        System.out.println("\n>>> 添加新任务");

        System.out.print("  标题：");
        String title = scanner.nextLine();

        System.out.print("  描述：");
        String description = scanner.nextLine();

        System.out.print("  截止日期（YYYY-MM-DD）：");
        String dueDate = scanner.nextLine();

        int priority = getIntInput("  优先级（1=高, 2=中, 3=低）：", 1, 3);

        try {
            Task task = new Task(title, description, dueDate, priority);
            taskManager.addTask(task);
            System.out.println("  ✓ 任务添加成功");
        } catch (InvalidTaskDataException e) {
            System.out.println("  ✗ 添加失败：" + e.getMessage());
        }
    }

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
        try {
            taskManager.markCompleted(task.getTitle());
            System.out.println("  ✓ 任务已标记为完成");
        } catch (TaskNotFoundException e) {
            System.out.println("  ✗ 操作失败：" + e.getMessage());
        }
    }

    private static void saveTasks() {
        System.out.println("\n>>> 保存任务");
        try {
            taskManager.saveToFile(DATA_FILE);
            System.out.println("  ✓ 保存成功（" + taskManager.getTotalCount() + " 个任务）");
        } catch (TaskIOException e) {
            System.out.println("  ✗ 保存失败：" + e.getMessage());
        }
    }

    /**
     * 测试各种异常场景
     */
    private static void testExceptionScenarios() {
        System.out.println("\n>>> 测试异常场景");
        System.out.println("─".repeat(40));

        // 测试 1：空标题
        System.out.println("\n测试 1：空标题");
        try {
            new Task("", "描述", "2026-02-15", 1);
        } catch (InvalidTaskDataException e) {
            System.out.println("  ✓ 捕获异常：" + e.getMessage());
        }

        // 测试 2：无效日期
        System.out.println("\n测试 2：无效日期格式");
        try {
            new Task("测试任务", "描述", "2025-13-45", 1);
        } catch (InvalidTaskDataException e) {
            System.out.println("  ✓ 捕获异常：" + e.getMessage());
        }

        // 测试 3：无效优先级
        System.out.println("\n测试 3：无效优先级");
        try {
            new Task("测试任务", "描述", "2026-02-15", 5);
        } catch (InvalidTaskDataException e) {
            System.out.println("  ✓ 捕获异常：" + e.getMessage());
        }

        // 测试 4：任务不存在
        System.out.println("\n测试 4：查找不存在的任务");
        try {
            taskManager.getTask("不存在的任务");
        } catch (TaskNotFoundException e) {
            System.out.println("  ✓ 捕获异常：" + e.getMessage());
        }

        // 测试 5：重复添加
        System.out.println("\n测试 5：重复添加任务");
        try {
            // 先添加一个任务
            Task task = new Task("唯一任务", "描述", "2026-02-15", 1);
            taskManager.addTask(task);
            // 再尝试添加同名任务
            Task duplicate = new Task("唯一任务", "另一个描述", "2026-02-16", 2);
            taskManager.addTask(duplicate);
        } catch (InvalidTaskDataException e) {
            System.out.println("  ✓ 捕获异常：" + e.getMessage());
        }

        System.out.println("\n所有测试完成！");
    }

    /**
     * 获取整数输入（带异常处理）
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
