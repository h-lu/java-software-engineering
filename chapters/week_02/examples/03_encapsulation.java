/**
 * 示例 03：封装（Encapsulation）
 *
 * 本例演示为什么需要封装，以及如何正确使用 getter/setter。
 *
 * 运行方式：
 *   javac 03_encapsulation.java
 *   java EncapsulationDemo
 *
 * 预期输出：
 *   对比 public 字段和 private 字段 + getter/setter 的区别
 *   演示封装的保护作用和控制变化的能力
 */

// ===== 反示例：所有字段都是 public =====

/**
 * ❌ 反示例：没有封装的类
 *
 * 问题：
 * 1. 数据可能被破坏（外部可以直接设置无效值）
 * 2. 无法控制变化（内部实现变化会影响外部代码）
 * 3. 无法追踪修改（不知道何时被修改）
 */
class TaskWithoutEncapsulation {
    public String title;
    public boolean completed;
    public int priority; // 1=高, 2=中, 3=低

    // 没有任何保护，外部代码可以直接修改
}

// ===== 正示例：使用封装 =====

/**
 * ✅ 正示例：使用封装的类
 *
 * 优点：
 * 1. 数据保护（setter 可以验证输入）
 * 2. 控制变化（内部实现可以改变，外部接口不变）
 * 3. 可追踪性（可以在方法里加日志）
 */
class TaskWithEncapsulation {
    // 字段：全部 private，外部不能直接访问
    private String title;
    private boolean completed;
    private int priority; // 1=高, 2=中, 3=低

    // 构造方法
    public TaskWithEncapsulation(String title, int priority) {
        this.title = title;
        setPriority(priority); // 使用 setter 来验证
        this.completed = false;
    }

    // ===== Getter：返回字段值 =====

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getPriority() {
        return priority;
    }

    // ===== Setter：设置字段值（可以加验证） =====

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // 带验证的 setter：防止无效值
    public void setPriority(int priority) {
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1-3（1=高, 2=中, 3=低）");
        }
        this.priority = priority;
    }

    // 语义化方法：更符合业务含义
    public void markCompleted() {
        this.completed = true;
    }

    // 可以没有 setter（只读字段）
    // 例如：如果某些字段不应该被外部修改，就不提供 setter
}

// ===== 进阶示例：封装支持内部实现变化 =====

/**
 * ✅ 进阶示例：内部实现可以改变，外部代码不受影响
 *
 * 场景：最初用 boolean completed，后来需要支持"进行中"状态
 * 如果使用封装，内部实现可以改成 enum，外部代码完全不受影响
 */
class TaskWithEncapsulationAdvanced {
    // 内部实现：使用 enum（对外部代码透明）
    private enum Status {
        NOT_STARTED,    // 未开始
        IN_PROGRESS,    // 进行中
        COMPLETED       // 已完成
    }

    private String title;
    private Status status;

    public TaskWithEncapsulationAdvanced(String title) {
        this.title = title;
        this.status = Status.NOT_STARTED;
    }

    // 公共接口：保持不变（向后兼容）
    public boolean isCompleted() {
        return status == Status.COMPLETED;
    }

    // 新增方法：支持更细粒度的状态
    public boolean isInProgress() {
        return status == Status.IN_PROGRESS;
    }

    public boolean isNotStarted() {
        return status == Status.NOT_STARTED;
    }

    // 语义化方法
    public void markCompleted() {
        this.status = Status.COMPLETED;
    }

    public void markInProgress() {
        this.status = Status.IN_PROGRESS;
    }

    public void markNotStarted() {
        this.status = Status.NOT_STARTED;
    }

    // 外部代码仍然可以调用 isCompleted()，不需要修改
}

/**
 * 演示类：对比封装前后的区别
 */
class EncapsulationDemo {
    public static void main(String[] args) {
        System.out.println("=== 封装演示 ===\n");

        // ===== 问题 1：数据可能被破坏 =====
        System.out.println("问题 1：数据可能被破坏");
        System.out.println("─".repeat(40));

        // 反示例：没有封装
        TaskWithoutEncapsulation badTask = new TaskWithoutEncapsulation();
        badTask.title = "写作业";
        badTask.completed = false;
        badTask.priority = 5; // ❌ 无效的优先级！但没有报错

        System.out.println("没有封装：priority = " + badTask.priority);
        System.out.println("问题：可以被设为无效值（5），程序可能在后续操作中崩溃");
        System.out.println();

        // 正示例：有封装
        try {
            TaskWithEncapsulation goodTask = new TaskWithEncapsulation("写作业", 2);
            System.out.println("有封装：priority = " + goodTask.getPriority());

            // 尝试设置无效值
            goodTask.setPriority(5); // ❌ 会抛出异常
        } catch (IllegalArgumentException e) {
            System.out.println("保护生效：" + e.getMessage());
        }
        System.out.println();

        // ===== 问题 2：无法控制变化 =====
        System.out.println("问题 2：无法控制变化");
        System.out.println("─".repeat(40));

        TaskWithEncapsulationAdvanced task = new TaskWithEncapsulationAdvanced("写作业");

        // 外部代码只调用公共接口
        System.out.println("初始状态：" + (task.isCompleted() ? "已完成" : "未完成"));
        task.markInProgress();
        System.out.println("标记进行中：" + (task.isInProgress() ? "进行中" : "其他"));
        task.markCompleted();
        System.out.println("标记完成：" + (task.isCompleted() ? "已完成" : "未完成"));
        System.out.println();

        System.out.println("优势：");
        System.out.println("- 内部从 boolean 改成 enum，外部代码完全不受影响");
        System.out.println("- 可以新增方法（isInProgress()），而不破坏旧代码");
        System.out.println();

        // ===== 封装的三大原则 =====
        System.out.println("=== 封装的三大原则 ===");
        System.out.println("1. 隐藏数据：字段用 private，外部不能直接访问");
        System.out.println("2. 提供方法：通过 public 方法暴露有限的访问权限");
        System.out.println("3. 控制变化：将来可以修改内部实现，而不影响外部代码");
        System.out.println();

        // ===== 对比表格 =====
        System.out.println("=== public 字段 vs private + getter/setter ===");
        System.out.println();
        System.out.println("| 维度           | public 字段                    | private + getter/setter       |");
        System.out.println("| -------------- | ------------------------------ | ----------------------------- |");
        System.out.println("| 外部访问       | 直接访问（危险）               | 通过方法（可控）              |");
        System.out.println("| 数据验证       | 无（可被设为任何值）           | 可以在 setter 里验证          |");
        System.out.println("| 实现变化       | 外部代码受影响                 | 外部代码不受影响              |");
        System.out.println("| 调试           | 无法追踪修改                   | 可以在方法里加日志            |");
        System.out.println();

        // ===== Getter 和 Setter 的规范 =====
        System.out.println("=== Getter 和 Setter 的规范 ===");
        System.out.println("1. Getter 命名：get + 字段名（如 getTitle()）");
        System.out.println("2. boolean 的 Getter：is + 字段名（如 isCompleted()）");
        System.out.println("3. Setter 命名：set + 字段名（如 setTitle(String)）");
        System.out.println("4. 可以没有 setter（只读字段）");
        System.out.println("5. 可以在 setter 里加验证逻辑");
        System.out.println("6. 可以使用语义化方法（如 markCompleted()）代替 setCompleted(true)");
    }
}
