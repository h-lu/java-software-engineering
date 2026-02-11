import java.util.ArrayList;
import java.util.List;

/**
 * 示例 02：名词提取法（Noun Extraction Method）
 *
 * 本例演示如何从需求文档中识别核心类。
 * 名词提取法的步骤：
 * 1. 写需求文档
 * 2. 圈出名词
 * 3. 区分核心名词和属性
 * 4. 识别"管理者"类
 *
 * 运行方式：
 *   javac 02_noun_extraction.java
 *   java NounExtractionDemo
 *
 * 预期输出：
 *   演示如何从需求中识别类，展示实体类和服务类的区别
 */

/**
 * 步骤 1：需求文档（模拟注释）
 *
 * TaskManager 的核心需求：
 * - 用户可以创建任务（Task）
 * - 每个任务有标题、描述、截止日期、优先级、完成状态
 * - 任务可以被标记为已完成
 * - 任务可以按优先级排序
 * - 系统可以过滤出已过期的任务
 *
 * 步骤 2：圈出名词
 * - 用户（User）、任务（Task）、标题、描述、截止日期、优先级、完成状态、系统
 *
 * 步骤 3：区分核心名词和属性
 * - Task：核心类（有独立存在价值，有属性和行为）
 * - User：核心类（有独立存在价值）
 * - 标题：Task 的属性
 * - 描述：Task 的属性
 * - 截止日期：Task 的属性
 * - 优先级：Task 的属性
 * - 完成状态：Task 的属性
 * - 系统：不是类（是"TaskManager"的通俗说法）
 *
 * 步骤 4：识别"管理者"类
 * - TaskManager：管理任务的增删改查
 * - TaskValidator：验证任务数据是否合法
 * - TaskRepository：负责任务的持久化（存储）
 */

// ===== 实体类（Entity Class）：存储数据，有状态 =====

/**
 * Task：核心实体类
 *
 * 职责：只负责存储任务数据
 *
 * 特征：
 * - 有字段（属性）：title, description, completed, priority
 * - 有 getter/setter：提供受控的访问
 * - 有状态：每个 Task 对象有自己独立的数据
 */
class Task {
    // 字段声明：必须指定类型（Week 01 的知识：静态类型）
    private String title;
    private String description;
    private String dueDate;
    private String priority; // "高"、"中"、"低"
    private boolean completed;

    // 构造方法：创建对象时初始化
    public Task(String title) {
        this.title = title;
        this.completed = false;
        this.priority = "中"; // 默认优先级
    }

    // Getter：返回字段值
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    // Setter：设置字段值（可以加验证）
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    // 语义化方法：更符合业务含义的方法名
    public void markCompleted() {
        this.completed = true;
    }

    public void markIncomplete() {
        this.completed = false;
    }
}

/**
 * User：核心实体类（本例暂不实现详细逻辑）
 *
 * 职责：存储用户信息
 *
 * 特征：
 * - 有字段：name, email
 * - 有状态
 */
class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

// ===== 服务类（Service Class）：提供行为，无状态或状态可变 =====

/**
 * TaskManager：服务类
 *
 * 职责：管理任务的增删改查
 *
 * 特征：
 * - 主要是方法
 * - 有一个 Task 列表作为字段
 * - 提供管理任务的操作
 */
class TaskManager {
    // 使用 List 存储多个 Task 对象
    private List<Task> tasks = new ArrayList<>();

    // 添加任务
    public void addTask(Task task) {
        tasks.add(task);
    }

    // 标记任务完成
    public void markCompleted(String taskTitle) {
        for (Task task : tasks) {
            if (task.getTitle().equals(taskTitle)) {
                task.markCompleted();
                break;
            }
        }
    }

    // 获取所有任务
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    // 过滤未完成的任务
    public List<Task> getIncompleteTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                result.add(task);
            }
        }
        return result;
    }

    // 按优先级过滤任务
    public List<Task> getTasksByPriority(String priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority().equals(priority)) {
                result.add(task);
            }
        }
        return result;
    }
}

/**
 * TaskValidator：服务类
 *
 * 职责：验证任务数据是否合法
 *
 * 特征：
 * - 无状态（只有静态方法）
 * - 提供验证逻辑
 */
class TaskValidator {
    // 验证任务是否合法
    public static boolean isValid(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    // 验证优先级是否合法
    public static boolean isValidPriority(String priority) {
        return priority != null &&
               (priority.equals("高") || priority.equals("中") || priority.equals("低"));
    }
}

/**
 * TaskPrinter：服务类
 *
 * 职责：负责任务的显示输出
 *
 * 特征：
 * - 无状态
 * - 提供格式化输出的方法
 */
class TaskPrinter {
    // 打印单个任务
    public static void print(Task task) {
        System.out.println("任务：" + task.getTitle());
        System.out.println("  状态：" + (task.isCompleted() ? "已完成" : "进行中"));
        System.out.println("  优先级：" + task.getPriority());
    }

    // 打印任务列表
    public static void printList(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.print((i + 1) + ". ");
            System.out.print(tasks.get(i).getTitle());
            System.out.print(" [" + tasks.get(i).getPriority() + "] ");
            System.out.println(tasks.get(i).isCompleted() ? "✓" : "○");
        }
    }
}

/**
 * 演示类：展示名词提取法的使用
 */
class NounExtractionDemo {
    public static void main(String[] args) {
        System.out.println("=== 名词提取法演示 ===\n");

        // 步骤 1：从需求中识别类（见上方注释）

        // 步骤 2：创建对象
        Task task1 = new Task("写作业");
        task1.setDescription("完成 Java 类设计练习");
        task1.setPriority("高");

        Task task2 = new Task("复习 Java");
        task2.setDescription("复习 Week 01 和 Week 02");
        task2.setPriority("中");

        Task task3 = new Task("做项目");
        task3.setDescription("CampusFlow 项目设计");
        task3.setPriority("高");

        // 步骤 3：使用管理者类
        TaskManager manager = new TaskManager();
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        // 步骤 4：验证数据
        System.out.println("1. 验证数据：");
        System.out.println("   task1 是否合法: " + TaskValidator.isValid(task1));
        System.out.println("   task1 优先级是否合法: " + TaskValidator.isValidPriority(task1.getPriority()));
        System.out.println();

        // 步骤 5：显示输出
        System.out.println("2. 所有任务：");
        TaskPrinter.printList(manager.getAllTasks());
        System.out.println();

        // 步骤 6：标记完成
        manager.markCompleted("写作业");

        System.out.println("3. 标记完成后，未完成的任务：");
        TaskPrinter.printList(manager.getIncompleteTasks());
        System.out.println();

        // 步骤 7：按优先级过滤
        System.out.println("4. 高优先级任务：");
        TaskPrinter.printList(manager.getTasksByPriority("高"));
        System.out.println();

        // 总结：名词提取法的价值
        System.out.println("=== 名词提取法的价值 ===");
        System.out.println("1. 从需求中系统地识别类，而不是凭空想象");
        System.out.println("2. 区分'实体类'（Task、User）和'服务类'（TaskManager、TaskValidator）");
        System.out.println("3. 实体类：存储数据，有状态");
        System.out.println("4. 服务类：提供行为，无状态或状态可变");
        System.out.println("5. 职责清晰：每个类只负责一件事");
    }
}
