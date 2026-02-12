/**
 * 示例 04：防御式编程（Defensive Programming）
 *
 * 本例演示如何在代码边界处验证输入，预防异常发生。
 * 核心思想：最好的异常处理是不让异常发生。
 *
 * 运行方式：
 *   javac 04_defensive_programming.java
 *   java DefensiveProgrammingDemo
 *
 * 预期输出：
 *   展示防御式编程 vs 捕获异常的区别，以及尽早失败原则
 */

// ===== 反示例：依赖异常捕获 =====

/**
 * ❌ 反示例：依赖 try-catch 处理 null
 *
 * 问题：
 * 1. 让 null 进入系统深处，可能导致难以调试的错误
 * 2. 异常处理有性能开销
 * 3. 代码意图不清晰
 */
class TaskManagerBad {
    private java.util.List<String> tasks = new java.util.ArrayList<>();

    /**
     * 不好的做法：在 catch 块里处理 null
     */
    public void addTaskBad(String task) {
        try {
            tasks.add(task);  // 如果 task 是 null，这里会成功添加 null
            System.out.println("  任务已添加，长度: " + task.length());  // 这里才崩溃
        } catch (NullPointerException e) {
            System.out.println("  错误：任务不能为 null");
        }
    }

    /**
     * 另一个不好的做法：让错误扩散
     */
    public void processTaskBad(String task) {
        // 假设 task 不为 null，继续处理
        String upper = task.toUpperCase();  // 如果 task 为 null，这里崩溃
        System.out.println("  处理: " + upper);
    }
}

// ===== 正示例：防御式编程 =====

/**
 * ✅ 正示例：在入口处防御
 *
 * 核心原则：
 * 1. 尽早失败（Fail Fast）
 * 2. 验证所有输入
 * 3. 使用断言记录假设
 */
class TaskManagerGood {
    private java.util.List<String> tasks = new java.util.ArrayList<>();

    /**
     * 好的做法：在入口处检查 null
     *
     * 尽早失败：在错误扩散之前就报错
     */
    public void addTask(String task) {
        if (task == null) {
            throw new IllegalArgumentException("任务不能为 null");
        }
        if (task.trim().isEmpty()) {
            throw new IllegalArgumentException("任务不能为空字符串");
        }

        tasks.add(task);
        System.out.println("  任务已添加: " + task);
    }

    /**
     * 好的做法：防御式检查 + 业务规则验证
     */
    public void addTaskWithValidation(String task, java.util.Set<String> existingTasks) {
        // 防御 1：null 检查
        if (task == null) {
            throw new IllegalArgumentException("任务不能为 null");
        }

        // 防御 2：空字符串检查
        String trimmed = task.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("任务不能为空");
        }

        // 防御 3：长度限制
        if (trimmed.length() > 100) {
            throw new IllegalArgumentException("任务不能超过 100 字符");
        }

        // 防御 4：重复检查
        if (existingTasks.contains(trimmed)) {
            throw new IllegalArgumentException("任务 '" + trimmed + "' 已存在");
        }

        tasks.add(trimmed);
        System.out.println("  任务已添加: " + trimmed);
    }

    /**
     * 好的做法：尽早检查，安全执行
     */
    public void processTask(String task) {
        // 尽早检查
        if (task == null) {
            throw new IllegalArgumentException("task 不能为 null");
        }

        // 现在可以安全地假设 task 不为 null
        String upper = task.toUpperCase();
        System.out.println("  处理: " + upper);
    }
}

// ===== 示例：在封装中实现防御 =====

/**
 * 带防御式验证的 Task 类
 *
 * 演示：在 setter 中进行输入验证
 */
class DefensiveTask {
    private String title;
    private String dueDate;
    private int priority;  // 1=高, 2=中, 3=低
    private String description;

    public DefensiveTask(String title, String dueDate, int priority) {
        setTitle(title);
        setDueDate(dueDate);
        setPriority(priority);
        this.description = "";
    }

    // ===== 带防御的 Setter =====

    /**
     * 设置标题（多层防御）
     */
    public void setTitle(String title) {
        // 防御 1：不能为空
        if (title == null) {
            throw new IllegalArgumentException("标题不能为 null");
        }

        // 防御 2：不能为空字符串
        String trimmed = title.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }

        // 防御 3：长度限制
        if (trimmed.length() > 100) {
            throw new IllegalArgumentException("标题不能超过 100 字符");
        }

        // 防御 4：去除首尾空格（数据清洗）
        this.title = trimmed;
    }

    /**
     * 设置截止日期（格式验证）
     */
    public void setDueDate(String dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("截止日期不能为 null");
        }

        // 使用正则表达式验证格式
        if (!dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("日期格式无效，应为 YYYY-MM-DD");
        }

        // 进一步验证日期值是否合法
        if (!isValidDate(dueDate)) {
            throw new IllegalArgumentException("日期值无效（如 2025-13-45）");
        }

        this.dueDate = dueDate;
    }

    /**
     * 设置优先级（枚举值验证）
     */
    public void setPriority(int priority) {
        // 防御：只允许特定值
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1（高）、2（中）或 3（低）");
        }
        this.priority = priority;
    }

    /**
     * 设置描述（可选字段的防御）
     */
    public void setDescription(String description) {
        // 可选字段可以为 null，但如果是字符串就要验证
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("描述不能超过 500 字符");
        }
        this.description = description != null ? description.trim() : null;
    }

    // ===== 断言示例 =====

    /**
     * 标记任务完成（使用断言记录假设）
     *
     * 注意：断言默认关闭，不要用它替代输入验证！
     */
    public void markCompleted() {
        // 断言：根据前置条件，title 不应该为 null
        assert this.title != null : "任务标题不应该为 null";

        System.out.println("  任务 '" + title + "' 已标记为完成");
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

            // 简单检查（不考虑闰年等复杂情况）
            int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            return day <= daysInMonth[month];
        } catch (Exception e) {
            return false;
        }
    }

    // Getters
    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public int getPriority() { return priority; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Task{title='" + title + "', dueDate='" + dueDate + "', priority=" + priority + "}";
    }
}

// ===== 演示类 =====

/**
 * 防御式编程演示主类
 */
class DefensiveProgrammingDemo {

    public static void main(String[] args) {
        System.out.println("=== 防御式编程演示 ===\n");

        // 演示 1：依赖异常捕获 vs 防御式编程
        System.out.println("演示 1：依赖异常捕获 vs 防御式编程");
        System.out.println("─".repeat(50));

        System.out.println("❌ 不好的做法（依赖 catch）：");
        TaskManagerBad badManager = new TaskManagerBad();
        badManager.addTaskBad(null);
        System.out.println();

        System.out.println("✅ 好的做法（防御式检查）：");
        TaskManagerGood goodManager = new TaskManagerGood();
        try {
            goodManager.addTask(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  提前捕获：" + e.getMessage());
        }
        System.out.println();

        // 演示 2：尽早失败原则
        System.out.println("演示 2：尽早失败（Fail Fast）");
        System.out.println("─".repeat(50));

        System.out.println("❌ 让错误扩散到深处：");
        try {
            badManager.processTaskBad(null);  // 在深处崩溃
        } catch (NullPointerException e) {
            System.out.println("  在深处崩溃，难以定位问题");
        }
        System.out.println();

        System.out.println("✅ 在入口处检查：");
        try {
            goodManager.processTask(null);  // 在入口处报错
        } catch (IllegalArgumentException e) {
            System.out.println("  在入口处捕获：" + e.getMessage());
        }
        System.out.println();

        // 演示 3：多层防御
        System.out.println("演示 3：多层防御验证");
        System.out.println("─".repeat(50));

        java.util.Set<String> existing = new java.util.HashSet<>();
        existing.add("已有任务");

        // 测试各种非法输入
        String[] testInputs = {
            null,
            "",
            "   ",
            "a".repeat(101),  // 超长字符串
            "已有任务"        // 重复
        };

        for (String input : testInputs) {
            try {
                if (input != null && input.length() > 50) {
                    System.out.println("  测试：超长字符串（" + input.length() + "字符）");
                } else {
                    System.out.println("  测试：" + (input == null ? "null" : "'" + input + "'"));
                }
                goodManager.addTaskWithValidation(input, existing);
            } catch (IllegalArgumentException e) {
                System.out.println("    → 被拒绝：" + e.getMessage());
            }
        }
        System.out.println();

        // 演示 4：Task 类的防御式验证
        System.out.println("演示 4：Task 类的防御式验证");
        System.out.println("─".repeat(50));

        // 测试无效标题
        try {
            new DefensiveTask("", "2026-02-15", 1);
        } catch (IllegalArgumentException e) {
            System.out.println("  空标题被拒绝：" + e.getMessage());
        }

        // 测试无效日期格式
        try {
            new DefensiveTask("写作业", "2025-13-45", 1);
        } catch (IllegalArgumentException e) {
            System.out.println("  无效日期被拒绝：" + e.getMessage());
        }

        // 测试无效日期值
        try {
            DefensiveTask task = new DefensiveTask("写作业", "2026-02-15", 1);
            task.setDueDate("2025-02-30");  // 2月没有30日
        } catch (IllegalArgumentException e) {
            System.out.println("  无效日期值被拒绝：" + e.getMessage());
        }

        // 测试无效优先级
        try {
            new DefensiveTask("写作业", "2026-02-15", 5);
        } catch (IllegalArgumentException e) {
            System.out.println("  无效优先级被拒绝：" + e.getMessage());
        }

        // 测试有效输入
        DefensiveTask validTask = new DefensiveTask("  写作业  ", "2026-02-15", 1);
        System.out.println("  有效输入被接受：" + validTask.getTitle());
        System.out.println("  （注意：首尾空格已被去除）");
        System.out.println();

        // 总结
        System.out.println("=== 防御式编程原则 ===");
        System.out.println();
        System.out.println("1. 尽早失败（Fail Fast）：");
        System.out.println("   在入口处检查，不要让错误扩散到系统深处");
        System.out.println();
        System.out.println("2. 验证所有输入：");
        System.out.println("   用户输入、文件内容、网络响应、数据库记录");
        System.out.println("   不要假设它们会符合预期");
        System.out.println();
        System.out.println("3. 多层防御：");
        System.out.println("   • null 检查");
        System.out.println("   • 空字符串检查");
        System.out.println("   • 长度限制");
        System.out.println("   • 格式验证");
        System.out.println("   • 业务规则验证（如重复检查）");
        System.out.println();
        System.out.println("4. 使用断言记录假设：");
        System.out.println("   assert condition : \"错误消息\"");
        System.out.println("   注意：断言默认关闭，不要替代输入验证！");
        System.out.println();
        System.out.println("5. 防御式编程 vs 异常处理：");
        System.out.println("   ┌─────────────┬─────────────────┬─────────────────┐");
        System.out.println("   │             │ 防御式编程      │ 异常处理        │");
        System.out.println("   ├─────────────┼─────────────────┼─────────────────┤");
        System.out.println("   │ 目标        │ 预防错误发生    │ 处理无法预防    │");
        System.out.println("   │ 时机        │ 在边界处验证    │ 在异常发生时    │");
        System.out.println("   │ 异常类型    │ 运行时异常      │ 受检异常        │");
        System.out.println("   │ 关系        │ 互补，先用防御  │ 再用异常处理    │");
        System.out.println("   └─────────────┴─────────────────┴─────────────────┘");
    }
}
