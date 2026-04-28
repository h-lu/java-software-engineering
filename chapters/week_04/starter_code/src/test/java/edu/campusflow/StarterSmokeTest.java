package edu.campusflow;

import edu.campusflow.domain.Task;
import edu.campusflow.domain.TaskStats;
import edu.campusflow.manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StarterSmokeTest {

    @Test
    void taskStatsSkeletonCompiles() {
        TaskStats stats = new TaskStats(2, 1, 1);

        assertEquals(2, stats.getTotalCount());
        assertTrue(stats.toString().contains("totalCount=2"));
        assertTrue(stats.toString().contains("completedCount=1"));
        assertTrue(stats.toString().contains("pendingCount=1"));
    }

    @Test
    void taskManagerSkeletonIsRunnable() {
        TaskManager manager = new TaskManager();
        manager.addTask(new Task("写 PR 描述", "高"));

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(1, manager.getStatistics().getTotalCount());
        assertEquals(1, manager.countByPriority().getOrDefault("高", 0));
    }
}
