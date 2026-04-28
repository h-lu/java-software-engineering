package edu.campusflow.domain;

/**
 * Week 04 统计结果值对象骨架。
 */
public class TaskStats {
    private final int totalCount;
    private final int completedCount;
    private final int pendingCount;

    public TaskStats(int totalCount, int completedCount, int pendingCount) {
        // TODO: 验证计数不能为负，并确认 total = completed + pending。
        this.totalCount = totalCount;
        this.completedCount = completedCount;
        this.pendingCount = pendingCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    @Override
    public String toString() {
        // TODO: 按 ASSIGNMENT.md 示例完善输出。
        return "TODO TaskStats{totalCount=" + totalCount
            + ", completedCount=" + completedCount
            + ", pendingCount=" + pendingCount + "}";
    }
}
