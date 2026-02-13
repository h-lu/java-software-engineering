import com.campusflow.dto.TaskRequest;
import com.campusflow.model.Task;
import com.campusflow.repository.InMemoryTaskRepository;
import com.campusflow.repository.TaskRepository;
import com.campusflow.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 任务服务单元测试。
 */
class TaskServiceTest {

    private TaskRepository repository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        taskService = new TaskService(repository);
    }

    @Test
    void createTask_ValidInput_ShouldSucceed() {
        TaskRequest request = new TaskRequest(
            "测试任务",
            "这是一个测试任务",
            "2026-12-31"
        );

        Task created = taskService.createTask(request);

        assertNotNull(created.getId());
        assertEquals("测试任务", created.getTitle());
        assertEquals("这是一个测试任务", created.getDescription());
    }

    @Test
    void createTask_EmptyTitle_ShouldThrow() {
        TaskRequest request = new TaskRequest("", "描述", "2026-12-31");

        assertThrows(Exception.class, () -> taskService.createTask(request));
    }

    @Test
    void findAll_ShouldReturnTasks() {
        taskService.createTask(new TaskRequest("任务1", null, "2026-12-31"));
        taskService.createTask(new TaskRequest("任务2", null, "2026-12-31"));

        var tasks = taskService.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    void completeTask_ShouldMarkAsCompleted() {
        TaskRequest request = new TaskRequest("任务", null, "2026-12-31");
        Task created = taskService.createTask(request);

        Task completed = taskService.completeTask(created.getId());

        assertEquals("completed", completed.getStatus());
        assertNotNull(completed.getCompletedAt());
    }

    @Test
    void deleteTask_ShouldRemoveFromRepository() {
        TaskRequest request = new TaskRequest("任务", null, "2026-12-31");
        Task created = taskService.createTask(request);

        taskService.deleteTask(created.getId());

        var found = taskService.findById(created.getId());
        assertTrue(found.isEmpty());
    }
}
