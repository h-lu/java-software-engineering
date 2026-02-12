// 文件：CodeReviewExample.java
// 演示 Code Review 中常见的问题和改进

package edu.campusflow.examples;

import java.util.ArrayList;
import java.util.List;

/**
 * Code Review 示例：展示审查前后的代码对比。
 *
 * <p>本文件包含两个版本：
 * <ul>
 *   <li>版本 1：有问题的代码（违反 SOLID、缺少异常处理）</li>
 *   <li>版本 2：审查后的改进代码</li>
 * </ul>
 */
public class CodeReviewExample {

    // ===== 版本 1：审查前（有问题） =====
    // 问题：
    // 1. 上帝类 - 承担了数据存储、验证、显示多个职责
    // 2. 公开字段 - 数据不安全
    // 3. 缺少异常处理 - 可能抛出 NullPointerException
    // 4. 魔法数字 - 100 没有解释

    public static class TaskManagerV1 {
        public List<TaskV1> tasks = new ArrayList<>();  // ❌ 公开字段

        public void addTask(TaskV1 task) {
            tasks.add(task);  // ❌ 没有验证
        }

        public void printTasks() {
            for (TaskV1 task : tasks) {
                System.out.println(task.title);  // ❌ 直接访问字段
            }
        }

        public boolean validate(TaskV1 task) {
            return task.title.length() < 100;  // ❌ 魔法数字
        }
    }

    public static class TaskV1 {
        public String title;      // ❌ 公开字段
        public String priority;   // ❌ 公开字段
    }

    // ===== 版本 2：审查后（改进） =====
    // 改进：
    // 1. 职责分离 - TaskManager 只负责管理
    // 2. 封装 - private 字段 + getter
    // 3. 防御式编程 - 输入验证
    // 4. 常量 - MAX_TITLE_LENGTH 有明确含义

    public static class TaskManagerV2 {
        private static final int MAX_TITLE_LENGTH = 100;  // ✅ 命名常量
        private final List<TaskV2> tasks = new ArrayList<>();  // ✅ private

        public void addTask(TaskV2 task) {
            // ✅ 防御式编程：尽早检查
            if (task == null) {
                throw new IllegalArgumentException("任务不能为 null");
            }
            if (!isValid(task)) {
                throw new IllegalArgumentException("任务数据无效");
            }
            tasks.add(task);
        }

        public List<TaskV2> getTasks() {
            // ✅ 防御性拷贝
            return new ArrayList<>(tasks);
        }

        private boolean isValid(TaskV2 task) {
            String title = task.getTitle();
            return title != null
                && !title.trim().isEmpty()
                && title.length() <= MAX_TITLE_LENGTH;
        }
    }

    public static class TaskV2 {
        private final String title;      // ✅ private
        private final String priority;   // ✅ private

        public TaskV2(String title, String priority) {
            this.title = title;
            this.priority = priority;
        }

        public String getTitle() {       // ✅ getter
            return title;
        }

        public String getPriority() {    // ✅ getter
            return priority;
        }
    }

    // ===== Code Review 审查清单（参考） =====
    /*
     * □ 设计层面
     *   □ 类是否符合单一职责原则？
     *   □ 是否有上帝类（承担过多职责）？
     *   □ 封装是否到位（private 字段）？
     *
     * □ 代码层面
     *   □ 是否有 NPE 风险？
     *   □ 异常处理是否完善？
     *   □ 输入验证是否到位？
     *   □ 是否有魔法数字？
     *
     * □ 可维护性
     *   □ 命名是否清晰？
     *   □ 方法是否过长？
     *   □ 是否有重复代码？
     */
}
