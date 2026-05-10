package com.campusflow.mock;

import com.campusflow.model.Task;
import com.campusflow.repository.TaskRepository;
import com.campusflow.service.TaskService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Isolation template: implement TaskService behavior, then remove @Disabled or replace with Mockito.")
class TaskServiceIsolationTemplateTest {
    @Test
    void createTaskShouldPersistIntoRepository() {
        FakeTaskRepository repository = new FakeTaskRepository();
        TaskService service = new TaskService(repository);

        Task created = service.createTask("完成 Week 12 集成测试");

        assertEquals("完成 Week 12 集成测试", created.getTitle());
        assertEquals(1, repository.savedTasks.size());
    }

    @Test
    void getTaskShouldDelegateToRepository() {
        FakeTaskRepository repository = new FakeTaskRepository();
        repository.savedTasks.add(new Task("task-1", "已存在任务", false));
        TaskService service = new TaskService(repository);

        Optional<Task> result = service.getTask("task-1");

        assertTrue(result.isPresent());
        assertEquals("已存在任务", result.get().getTitle());
    }

    private static final class FakeTaskRepository implements TaskRepository {
        private final List<Task> savedTasks = new ArrayList<>();

        @Override
        public List<Task> findAll() {
            return List.copyOf(savedTasks);
        }

        @Override
        public Optional<Task> findById(String id) {
            return savedTasks.stream().filter(task -> task.getId().equals(id)).findFirst();
        }

        @Override
        public Task save(Task task) {
            savedTasks.add(task);
            return task;
        }
    }
}
