// 文件：BranchWorkflowDemo.java
// 演示 Feature Branch 工作流的核心概念

package edu.campusflow.examples;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示分支工作流中的任务管理场景。
 *
 * <p>这个类模拟了在 feature 分支上开发新功能的过程，
 * 展示了如何在隔离的环境中进行实验性开发。
 */
public class BranchWorkflowDemo {

    private List<Task> tasks = new ArrayList<>();
    private String branchName;

    public BranchWorkflowDemo(String branchName) {
        this.branchName = branchName;
    }

    /**
     * 在 feature 分支上添加实验性功能。
     */
    public void addExperimentalFeature(Task task) {
        System.out.println("[" + branchName + "] 添加任务: " + task.getTitle());
        tasks.add(task);
    }

    /**
     * 模拟合并前的审查检查。
     */
    public boolean preMergeCheck() {
        System.out.println("[" + branchName + "] 执行合并前检查...");

        // 检查 1：是否有空标题
        for (Task task : tasks) {
            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                System.err.println("错误：发现空标题任务");
                return false;
            }
        }

        // 检查 2：代码风格
        System.out.println("✓ 代码风格检查通过");

        // 检查 3：功能完整性
        System.out.println("✓ 功能完整性检查通过");

        return true;
    }

    /**
     * 获取当前分支的任务数。
     */
    public int getTaskCount() {
        return tasks.size();
    }

    public String getBranchName() {
        return branchName;
    }

    // 内部类：简单任务表示
    public static class Task {
        private String title;
        private String priority;

        public Task(String title, String priority) {
            this.title = title;
            this.priority = priority;
        }

        public String getTitle() {
            return title;
        }

        public String getPriority() {
            return priority;
        }
    }

    // 演示 main 方法
    public static void main(String[] args) {
        // 模拟小北在 feature-filter 分支开发
        BranchWorkflowDemo xiaobeiBranch = new BranchWorkflowDemo("feature/task-filter");
        xiaobeiBranch.addExperimentalFeature(new Task("实现按优先级筛选", "高"));
        xiaobeiBranch.addExperimentalFeature(new Task("添加筛选结果排序", "中"));

        System.out.println();

        // 模拟阿码在 feature-sort 分支开发
        BranchWorkflowDemo amaBranch = new BranchWorkflowDemo("feature/task-sort");
        amaBranch.addExperimentalFeature(new Task("实现按截止日期排序", "高"));

        System.out.println();

        // 合并前检查
        System.out.println("=== 合并前检查 ===");
        boolean xiaobeiReady = xiaobeiBranch.preMergeCheck();
        boolean amaReady = amaBranch.preMergeCheck();

        if (xiaobeiReady && amaReady) {
            System.out.println("\n✓ 两个分支都准备好合并到 main");
        }
    }
}
