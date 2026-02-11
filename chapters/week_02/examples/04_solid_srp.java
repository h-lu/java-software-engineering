import java.util.ArrayList;
import java.util.List;

/**
 * 示例 04：SOLID 原则入门——单一职责原则（Single Responsibility Principle）
 *
 * 本例演示如何应用单一职责原则（SRP）来拆分"上帝类"。
 *
 * SRP 定义：一个类应该只有一个引起它变化的原因。
 *
 * 运行方式：
 *   javac 04_solid_srp.java
 *   java SOLIDSRPDemo
 *
 * 预期输出：
 *   对比拆分前后的代码，展示单一职责原则的价值
 */

// ===== 拆分前：上帝类（违反 SRP）=====

/**
 * ❌ 反示例：上帝类——违反单一职责原则
 *
 * 问题：这个类有 5 个"引起变化的原因"
 * 1. 任务的数据结构变了 → 需要修改 Task
 * 2. 验证规则变了 → 需要修改 Task
 * 3. 存储方式变了 → 需要修改 Task
 * 4. 显示格式变了 → 需要修改 Task
 * 5. 通知方式变了 → 需要修改 Task
 */
class GodTask {
    // 职责 1：存储数据
    public String title;
    public boolean completed;

    // 职责 2：验证数据
    public boolean isValid() {
        return title != null && !title.trim().isEmpty();
    }

    // 职责 3：文件存储
    public void saveToFile(String filename) {
        System.out.println("保存到文件: " + filename);
    }

    // 职责 4：显示
    public void print() {
        System.out.println("任务：" + title);
    }

    // 职责 5：邮件通知
    public void sendEmailNotification(String email) {
        System.out.println("发送邮件到: " + email);
    }
}

// ===== 拆分后：单一职责原则 =====

/**
 * ✅ 正示例 1：Task——只负责存储数据
 *
 * 职责：存储任务数据
 * 变化原因：任务的数据结构变了
 */
class Task {
    private String title;
    private boolean completed;

    public Task(String title) {
        this.title = title;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }
}

/**
 * ✅ 正示例 2：TaskValidator——只负责验证数据
 *
 * 职责：验证任务数据是否合法
 * 变化原因：验证规则变了
 */
class TaskValidator {
    public static boolean isValid(Task task) {
        if (task == null) {
            return false;
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    // 可以有更多验证方法
    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }
}

/**
 * ✅ 正示例 3：TaskRepository——只负责持久化
 *
 * 职责：负责任务的存储和加载
 * 变化原因：存储方式变了（文件 → 数据库）
 */
class TaskRepository {
    // 模拟文件存储
    public void save(Task task, String filename) {
        System.out.println("保存任务到文件: " + filename);
        // 实际实现：写文件
    }

    public Task load(String filename) {
        System.out.println("从文件加载任务: " + filename);
        // 实际实现：读文件
        return null;
    }

    // 未来可以扩展：数据库存储
    // public void saveToDatabase(Task task) { ... }
}

/**
 * ✅ 正示例 4：TaskPrinter——只负责显示
 *
 * 职责：负责任务的格式化输出
 * 变化原因：显示格式变了（文本 → JSON → XML）
 */
class TaskPrinter {
    // 文本格式
    public void print(Task task) {
        System.out.println("任务：" + task.getTitle());
        System.out.println("状态：" + (task.isCompleted() ? "已完成" : "进行中"));
    }

    // JSON 格式
    public void printAsJson(Task task) {
        System.out.println("{ \"title\": \"" + task.getTitle() + "\" }");
    }

    // 简短格式
    public void printShort(Task task) {
        System.out.println("[" + (task.isCompleted() ? "✓" : "○") + "] " + task.getTitle());
    }
}

/**
 * ✅ 正示例 5：EmailNotifier——只负责邮件通知
 *
 * 职责：负责发送邮件通知
 * 变化原因：通知方式变了（邮件 → 短信 → 推送）
 */
class EmailNotifier {
    public void sendTaskNotification(Task task, String emailAddress) {
        System.out.println("发送邮件到: " + emailAddress);
        System.out.println("主题：任务更新 - " + task.getTitle());
        // 实际实现：发送邮件
    }

    // 未来可以扩展：短信通知
    // public void sendSMSNotification(Task task, String phoneNumber) { ... }
}

/**
 * ✅ 正示例 6：TaskManager——管理任务的集合
 *
 * 职责：管理任务的增删改查
 * 变化原因：管理逻辑变了（添加过滤、排序等）
 */
class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void markCompleted(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                task.markCompleted();
                break;
            }
        }
    }

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
}

// ===== 开闭原则（OCP）简介 =====

/**
 * 开闭原则：对扩展开放，对修改关闭
 *
 * 问题：如果要支持新的输出格式，怎么改？
 *
 * ❌ 不符合 OCP 的设计：每次要新格式，都要修改 TaskPrinter
 */
class BadTaskFormatter {
    public String format(Task task, String format) {
        if (format.equals("text")) {
            return "任务：" + task.getTitle();
        } else if (format.equals("json")) {
            return "{ \"title\": \"" + task.getTitle() + "\" }";
        } else if (format.equals("xml")) {
            // 每次要支持新格式，都要加一个 else if → 违反开闭原则
            return "<task><title>" + task.getTitle() + "</title></task>";
        }
        return "";
    }
}

/**
 * ✅ 符合 OCP 的设计：通过接口扩展
 */

// 为了演示开闭原则，我们创建不同的格式器
// 注意：这里使用继承（Week 08 会深入讲接口，更优雅的方式）
class TextFormatter {
    public String format(Task task) {
        return "任务：" + task.getTitle();
    }
}

class JsonFormatter {
    public String format(Task task) {
        return "{ \"title\": \"" + task.getTitle() + "\" }";
    }
}

// 使用多态（父类引用指向子类）
class BetterTaskPrinter {
    public void print(Task task, Object formatter) {
        if (formatter instanceof TextFormatter) {
            System.out.println(((TextFormatter)formatter).format(task));
        } else if (formatter instanceof JsonFormatter) {
            System.out.println(((JsonFormatter)formatter).format(task));
        }
    }
}

/**
 * 演示类：对比拆分前后的区别
 */
class SOLIDSRPDemo {
    public static void main(String[] args) {
        System.out.println("=== SOLID 原则演示：单一职责原则 ===\n");

        // ===== 拆分前：上帝类 =====
        System.out.println("拆分前：上帝类（违反 SRP）");
        System.out.println("─".repeat(50));

        GodTask godTask = new GodTask();
        godTask.title = "写作业";
        godTask.completed = false;

        godTask.print();
        godTask.saveToFile("task.txt");
        godTask.sendEmailNotification("user@example.com");

        System.out.println();
        System.out.println("问题：");
        System.out.println("  ✗ 这个类有 5 个职责");
        System.out.println("  ✗ 每次需求变更，都要修改这个类");
        System.out.println("  ✗ 难以测试（依赖太多外部资源）");
        System.out.println();

        // ===== 拆分后：单一职责 =====
        System.out.println("拆分后：单一职责（符合 SRP）");
        System.out.println("─".repeat(50));

        // 创建任务
        Task task = new Task("写作业");

        // 验证数据（职责 2：TaskValidator）
        System.out.println("1. 验证数据：");
        System.out.println("   是否合法: " + TaskValidator.isValid(task));
        System.out.println();

        // 显示输出（职责 4：TaskPrinter）
        System.out.println("2. 显示输出：");
        TaskPrinter printer = new TaskPrinter();
        printer.print(task);
        System.out.println();

        // 保存文件（职责 3：TaskRepository）
        System.out.println("3. 保存文件：");
        TaskRepository repository = new TaskRepository();
        repository.save(task, "task.txt");
        System.out.println();

        // 邮件通知（职责 5：EmailNotifier）
        System.out.println("4. 邮件通知：");
        EmailNotifier notifier = new EmailNotifier();
        notifier.sendTaskNotification(task, "user@example.com");
        System.out.println();

        // ===== 如何判断类是否违反 SRP =====
        System.out.println("=== 如何判断类是否违反 SRP ===");
        System.out.println();
        System.out.println("问题 1：这个类有哪些'变化的原因'？");
        System.out.println("  → 如果能说出 2 个以上的原因，说明职责过多");
        System.out.println();
        System.out.println("问题 2：能不能用一句话描述这个类的职责？");
        System.out.println("  → 好的描述：'它负责……'（一个完整的句子）");
        System.out.println("  → 坏的描述：'它负责 A，还有 B，还有 C……'");
        System.out.println();
        System.out.println("问题 3：如果需求变了，需要改几个类？");
        System.out.println("  → 理想情况：一个需求变，只需要改 1 个类");
        System.out.println();

        // ===== 开闭原则演示 =====
        System.out.println("=== 开闭原则（OCP）演示 ===");
        System.out.println();
        System.out.println("不符合 OCP 的设计：");
        System.out.println("  → 每次要支持新格式，都要修改类（加 else if）");
        System.out.println();
        System.out.println("符合 OCP 的设计：");
        System.out.println("  → 要支持新格式，只需要新建一个类");
        System.out.println();

        GoodTaskPrinter goodPrinter = new GoodTaskPrinter();
        Task task2 = new Task("复习 Java");

        // 想要文本格式？
        System.out.print("文本格式：");
        goodPrinter.print(task2, new TextFormatter());

        // 想要 JSON 格式？
        System.out.print("JSON 格式：");
        goodPrinter.print(task2, new JsonFormatter());

        // 想要新格式？新建一个 XmlFormatter，不用改 GoodTaskPrinter
        System.out.print("XML 格式：");
        goodPrinter.print(task2, new XmlFormatter());

        System.out.println();
        System.out.println("优势：新增格式不需要修改现有代码（对修改关闭）");
        System.out.println("      只需要新建一个类（对扩展开放）");
        System.out.println();

        // ===== SOLID 总结 =====
        System.out.println("=== SOLID 原则总结 ===");
        System.out.println();
        System.out.println("S - 单一职责原则（Single Responsibility Principle）");
        System.out.println("    → 一个类应该只有一个引起它变化的原因");
        System.out.println();
        System.out.println("O - 开闭原则（Open/Closed Principle）");
        System.out.println("    → 对扩展开放，对修改关闭");
        System.out.println();
        System.out.println("L - 里氏替换原则（Liskov Substitution Principle）");
        System.out.println("    → 子类可以替换父类（Week 05 深入）");
        System.out.println();
        System.out.println("I - 接口隔离原则（Interface Segregation Principle）");
        System.out.println("    → 不要强迫类实现它不需要的方法（Week 08 深入）");
        System.out.println();
        System.out.println("D - 依赖倒置原则（Dependency Inversion Principle）");
        System.out.println("    → 依赖抽象，不依赖具体（Week 08 深入）");
    }
}
