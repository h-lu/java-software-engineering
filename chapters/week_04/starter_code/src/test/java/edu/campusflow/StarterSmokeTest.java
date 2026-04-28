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
        TaskStats stats = new TaskStats(0, 0, 0);

        assertEquals(0, stats.getTotalCount());
        assertTrue(stats.toString().contains("待办"));
    }

    @Test
    void taskManagerSkeletonIsRunnable() {
        TaskManager manager = new TaskManager();
        manager.addTask(new Task("写 PR 描述", "高"));

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(0, manager.getStatistics().getTotalCount());
        assertTrue(manager.countByPriority().isEmpty());
    }
}
