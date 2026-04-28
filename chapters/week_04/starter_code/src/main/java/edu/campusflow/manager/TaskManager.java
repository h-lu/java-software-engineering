package edu.campusflow.manager;

import edu.campusflow.domain.Task;
import edu.campusflow.domain.TaskStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Week 04 任务统计功能骨架。
 */
public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        // 待办： 添加 null 检查，并考虑是否允许重复标题。
        tasks.add(task);
    }

    public void markCompleted(String title) {
        // 待办： 根据标题查找任务并标记完成；考虑找不到任务时的处理策略。
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public TaskStats getStatistics() {
        int completed = 0;
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completed++;
            }
        }
        return new TaskStats(tasks.size(), completed, tasks.size() - completed);
    }

    public Map<String, Integer> countByPriority() {
        Map<String, Integer> counts = new HashMap<>();
        for (Task task : tasks) {
            counts.merge(task.getPriority(), 1, Integer::sum);
        }
        return counts;
    }
}
