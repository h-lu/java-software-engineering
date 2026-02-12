/**
 * 示例 03：受检异常 vs 运行时异常
 *
 * 本例演示 Java 中两种异常类型的区别：
 * - 受检异常（Checked Exception）：编译器强迫处理
 * - 运行时异常（Runtime Exception）：不强制处理，代表编程错误
 *
 * 运行方式：
 *   javac 03_checked_vs_unchecked.java
 *   java CheckedVsUncheckedDemo
 *
 * 预期输出：
 *   展示两种异常的区别，以及何时使用哪种异常
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// ===== 自定义异常类 =====

/**
 * CampusFlow 基础领域异常（受检异常）
 *
 * 继承自 Exception，表示调用者必须处理的业务异常
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
 * 任务不存在异常（受检异常）
 *
 * 当调用者可以合理恢复时使用（如提示用户重新输入）
 */
class TaskNotFoundException extends CampusFlowException {

    public TaskNotFoundException(String taskTitle) {
        super("找不到任务: " + taskTitle);
    }
}

/**
 * 任务数据无效异常（受检异常）
 */
class InvalidTaskDataException extends CampusFlowException {

    public InvalidTaskDataException(String field, String reason) {
        super("任务数据无效 [" + field + "]: " + reason);
    }
}

/**
 * 任务不存在运行时异常
 *
 * 当调用者无法恢复时使用（编程错误）
 */
class TaskNotFoundRuntimeException extends RuntimeException {

    public TaskNotFoundRuntimeException(String taskTitle) {
        super("找不到任务: " + taskTitle);
    }
}

// ===== 任务类（带防御式验证） =====

/**
 * 任务实体类
 *
 * 演示：在 setter 中抛出运行时异常（编程错误不应该发生）
 */
class Task {
    private String title;
    private String dueDate;
    private int priority;  // 1=高, 2=中, 3=低

    public Task(String title, String dueDate, int priority) {
        setTitle(title);
        setDueDate(dueDate);
        setPriority(priority);
    }

    public String getTitle() {
        return title;
    }

    /**
     * 设置标题（带验证）
     *
     * 抛出运行时异常：空标题是编程错误，不应该发生
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("标题不能超过 100 字符");
        }
        this.title = title.trim();
    }

    public String getDueDate() {
        return dueDate;
    }

    /**
     * 设置截止日期（带验证）
     */
    public void setDueDate(String dueDate) {
        if (!isValidDateFormat(dueDate)) {
            throw new IllegalArgumentException("日期格式无效，应为 YYYY-MM-DD");
        }
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * 设置优先级（带验证）
     */
    public void setPriority(int priority) {
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1（高）、2（中）或 3（低）");
        }
        this.priority = priority;
    }

    private boolean isValidDateFormat(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    @Override
    public String toString() {
        return "Task{title='" + title + "', dueDate='" + dueDate + "', priority=" + priority + "}";
    }
}

// ===== 任务管理器（展示两种异常的使用场景） =====

/**
 * 任务管理器
 *
 * 演示：何时使用受检异常，何时使用运行时异常
 */
class TaskManager {
    private java.util.List<Task> tasks = new java.util.ArrayList<>();

    /**
     * 添加任务
     *
     * 不声明异常：输入验证在 Task 类中完成，抛出运行时异常
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("任务不能为 null");
        }
        tasks.add(task);
    }

    /**
     * 查找任务（方案 A：返回 null）
     *
     * 问题：调用者可能忘记检查 null，导致 NullPointerException
     */
    public Task findTaskByTitle(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                return task;
            }
        }
        return null;  // 可能忘记检查！
    }

    /**
     * 查找任务（方案 B：抛出受检异常）
     *
     * 适用场景：调用者可以合理恢复（如提示用户重新输入）
     * 强制调用者处理：要么 try-catch，要么继续 throws
     */
    public Task findTaskChecked(String title) throws TaskNotFoundException {
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                return task;
            }
        }
        throw new TaskNotFoundException(title);
    }

    /**
     * 查找任务（方案 C：抛出运行时异常）
     *
     * 适用场景：调用者无法恢复，这是编程错误
     * 不强制处理：如果发生，说明代码有 bug
     */
    public Task getTask(String title) {
        Task task = findTaskByTitle(title);
        if (task == null) {
            throw new TaskNotFoundRuntimeException(title);
        }
        return task;
    }

    /**
     * 从文件加载任务（受检异常示例）
     *
     * 文件是否存在是外部因素，调用者应该决定如何处理
     */
    public void loadFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("  读取: " + line);
        }
        scanner.close();
    }

    public java.util.List<Task> getAllTasks() {
        return new java.util.ArrayList<>(tasks);
    }
}

// ===== 演示类 =====

/**
 * 受检异常 vs 运行时异常 演示主类
 */
class CheckedVsUncheckedDemo {

    public static void main(String[] args) {
        System.out.println("=== 受检异常 vs 运行时异常 ===\n");

        // 演示异常继承层次
        System.out.println("异常继承层次：");
        System.out.println("─".repeat(50));
        System.out.println("  Throwable");
        System.out.println("  ├── Error（系统级错误，不处理）");
        System.out.println("  └── Exception");
        System.out.println("      ├── RuntimeException（运行时异常，不强制处理）");
        System.out.println("      │   ├── NullPointerException");
        System.out.println("      │   ├── IllegalArgumentException");
        System.out.println("      │   └── IndexOutOfBoundsException");
        System.out.println("      └── 其他 Exception（受检异常，强制处理）");
        System.out.println("          ├── FileNotFoundException");
        System.out.println("          ├── IOException");
        System.out.println("          └── SQLException");
        System.out.println();

        // 演示运行时异常
        System.out.println("演示 1：运行时异常（编程错误）");
        System.out.println("─".repeat(50));

        System.out.println("尝试创建标题为空的任务：");
        try {
            Task badTask = new Task("", "2026-02-15", 1);
        } catch (IllegalArgumentException e) {
            System.out.println("  捕获到运行时异常：" + e.getMessage());
        }
        System.out.println();

        System.out.println("尝试设置无效优先级：");
        try {
            Task task = new Task("写作业", "2026-02-15", 5);
        } catch (IllegalArgumentException e) {
            System.out.println("  捕获到运行时异常：" + e.getMessage());
        }
        System.out.println();

        // 演示受检异常
        System.out.println("演示 2：受检异常（外部风险）");
        System.out.println("─".repeat(50));

        TaskManager manager = new TaskManager();

        System.out.println("尝试读取不存在的文件：");
        System.out.println("  方法声明：loadFromFile(String filename) throws FileNotFoundException");
        System.out.println("  编译器强迫调用者处理这个异常");

        try {
            manager.loadFromFile("不存在.txt");
        } catch (FileNotFoundException e) {
            System.out.println("  捕获到受检异常：" + e.getMessage());
        }
        System.out.println();

        // 演示自定义异常
        System.out.println("演示 3：自定义异常的使用");
        System.out.println("─".repeat(50));

        // 添加一些任务
        manager.addTask(new Task("写作业", "2026-02-15", 1));
        manager.addTask(new Task("复习", "2026-02-16", 2));

        System.out.println("使用受检异常（TaskNotFoundException）：");
        try {
            Task task = manager.findTaskChecked("不存在的任务");
            System.out.println("  找到任务：" + task);
        } catch (TaskNotFoundException e) {
            System.out.println("  捕获到自定义受检异常：" + e.getMessage());
        }
        System.out.println();

        System.out.println("使用运行时异常（TaskNotFoundRuntimeException）：");
        try {
            Task task = manager.getTask("不存在的任务");
            System.out.println("  找到任务：" + task);
        } catch (TaskNotFoundRuntimeException e) {
            System.out.println("  捕获到自定义运行时异常：" + e.getMessage());
        }
        System.out.println();

        // 总结
        System.out.println("=== 如何选择异常类型 ===");
        System.out.println();
        System.out.println("使用受检异常（Checked Exception）当：");
        System.out.println("  • 调用者可以合理地恢复");
        System.out.println("  • 代表外部不确定性（文件不存在、网络断开）");
        System.out.println("  • 编译器应该强迫调用者考虑这种情况");
        System.out.println();
        System.out.println("使用运行时异常（Runtime Exception）当：");
        System.out.println("  • 调用者无法恢复，这是编程错误");
        System.out.println("  • 应该通过修复代码来避免");
        System.out.println("  • 如：空指针、数组越界、非法参数");
        System.out.println();
        System.out.println("判断标准：");
        System.out.println("  '如果调用者可以合理地恢复，用受检异常。'");
        System.out.println("  '如果调用者无法恢复，用运行时异常。'");
    }
}
