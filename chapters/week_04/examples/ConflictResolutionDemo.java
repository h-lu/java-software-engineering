// 文件：ConflictResolutionDemo.java
// 演示 Git 冲突解决的场景和策略

package edu.campusflow.examples;

import java.util.HashMap;
import java.util.Map;

/**
 * 冲突解决演示：模拟多人协作时的代码冲突场景。
 *
 * <p>本演示展示了：
 * <ul>
 *   <li>冲突是如何产生的</li>
 *   <li>如何检测冲突</li>
 *   <li>解决冲突的策略</li>
 * </ul>
 */
public class ConflictResolutionDemo {

    // 模拟 Git 仓库的状态
    private Map<String, String> fileContents = new HashMap<>();
    private String baseVersion;

    public ConflictResolutionDemo() {
        // 初始版本
        this.baseVersion = "public class TaskManager {\n" +
                          "    private List<Task> tasks = new ArrayList<>();\n" +
                          "}";
        fileContents.put("TaskManager.java", baseVersion);
    }

    /**
     * 模拟小北的修改：添加筛选功能
     */
    public String xiaobeiChanges() {
        return "public class TaskManager {\n" +
               "    private List<Task> tasks = new ArrayList<>();\n" +
               "\n" +
               "    // 小北添加：按优先级筛选\n" +
               "    public List<Task> filterByPriority(String priority) {\n" +
               "        return tasks.stream()\n" +
               "            .filter(t -> t.getPriority().equals(priority))\n" +
               "            .collect(Collectors.toList());\n" +
               "    }\n" +
               "}";
    }

    /**
     * 模拟阿码的修改：添加排序功能
     */
    public String amaChanges() {
        return "public class TaskManager {\n" +
               "    private List<Task> tasks = new ArrayList<>();\n" +
               "\n" +
               "    // 阿码添加：按截止日期排序\n" +
               "    public List<Task> sortByDueDate() {\n" +
               "        return tasks.stream()\n" +
               "            .sorted(Comparator.comparing(Task::getDueDate))\n" +
               "            .collect(Collectors.toList());\n" +
               "    }\n" +
               "}";
    }

    /**
     * 检测冲突：如果两人修改了同一文件的同一区域，就会产生冲突
     */
    public boolean detectConflict(String change1, String change2) {
        // 简化演示：如果都包含新方法的添加，就认为是冲突
        return change1.contains("filterByPriority") &&
               change2.contains("sortByDueDate");
    }

    /**
     * 解决冲突：合并两人的改动
     */
    public String resolveConflict(String change1, String change2) {
        // 实际 Git 冲突解决需要人工介入
        // 这里展示合并后的理想结果
        return "public class TaskManager {\n" +
               "    private List<Task> tasks = new ArrayList<>();\n" +
               "\n" +
               "    // 小北添加：按优先级筛选\n" +
               "    public List<Task> filterByPriority(String priority) {\n" +
               "        return tasks.stream()\n" +
               "            .filter(t -> t.getPriority().equals(priority))\n" +
               "            .collect(Collectors.toList());\n" +
               "    }\n" +
               "\n" +
               "    // 阿码添加：按截止日期排序\n" +
               "    public List<Task> sortByDueDate() {\n" +
               "        return tasks.stream()\n" +
               "            .sorted(Comparator.comparing(Task::getDueDate))\n" +
               "            .collect(Collectors.toList());\n" +
               "    }\n" +
               "}";
    }

    /**
     * 展示冲突标记（Git 冲突时的格式）
     */
    public void showConflictMarkers() {
        System.out.println("冲突时的文件内容：");
        System.out.println("==================");
        System.out.println("public class TaskManager {");
        System.out.println("    private List<Task> tasks = new ArrayList<>();");
        System.out.println();
        System.out.println("<<<<<<< HEAD  (当前分支 - 小北的改动)");
        System.out.println("    public List<Task> filterByPriority(String priority) {");
        System.out.println("        // ... 筛选逻辑");
        System.out.println("    }");
        System.out.println("=======");
        System.out.println("    public List<Task> sortByDueDate() {");
        System.out.println("        // ... 排序逻辑");
        System.out.println("    }");
        System.out.println(">>>>>>> feature/task-sort  (合并分支 - 阿码的改动)");
        System.out.println("}");
        System.out.println();
        System.out.println("解决策略：保留两个方法，因为它们功能独立不冲突");
    }

    public static void main(String[] args) {
        ConflictResolutionDemo demo = new ConflictResolutionDemo();

        System.out.println("=== 冲突解决演示 ===\n");

        // 展示两人的改动
        System.out.println("小北的改动：");
        System.out.println(demo.xiaobeiChanges());
        System.out.println();

        System.out.println("阿码的改动：");
        System.out.println(demo.amaChanges());
        System.out.println();

        // 检测冲突
        boolean hasConflict = demo.detectConflict(
            demo.xiaobeiChanges(),
            demo.amaChanges()
        );
        System.out.println("是否产生冲突: " + hasConflict);
        System.out.println();

        // 展示冲突标记
        demo.showConflictMarkers();
        System.out.println();

        // 展示解决后的结果
        System.out.println("=== 解决冲突后的代码 ===");
        System.out.println(demo.resolveConflict(
            demo.xiaobeiChanges(),
            demo.amaChanges()
        ));
    }
}
