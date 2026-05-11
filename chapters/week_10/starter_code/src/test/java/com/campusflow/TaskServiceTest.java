package com.campusflow;

import com.campusflow.dto.TaskRequest;
import com.campusflow.exception.NotFoundException;
import com.campusflow.exception.ValidationException;
import com.campusflow.model.Task;
import com.campusflow.repository.InMemoryTaskRepository;
import com.campusflow.service.TaskService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceTest {
    @Test
    void findAllEnvelopeReturnsDataAndTotal() {
        TaskService service = new TaskService(new InMemoryTaskRepository());
        service.create(new TaskRequest("写 Prompt", "生成前端初稿", "2026-05-11", false));

        Map<String, Object> envelope = service.findAllEnvelope();

        assertEquals(1, envelope.get("total"));
        assertInstanceOf(List.class, envelope.get("data"));
    }

    @Test
    void createRejectsBlankTitle() {
        TaskService service = new TaskService(new InMemoryTaskRepository());

        assertThrows(ValidationException.class,
            () -> service.create(new TaskRequest(" ", "desc", "2026-05-11", false)));
    }

    @Test
    void putStyleUpdateCanMarkTaskCompleted() {
        TaskService service = new TaskService(new InMemoryTaskRepository());
        Task created = service.create(new TaskRequest("审查 AI 代码", "检查 XSS", "2026-05-11", false));

        Task updated = service.update(created.getId(),
            new TaskRequest(created.getTitle(), created.getDescription(), created.getDueDate(), true));

        assertTrue(updated.isCompleted());
        assertEquals(0, updated.getOverdueDays());
    }

    @Test
    void deleteMissingTaskThrowsNotFound() {
        TaskService service = new TaskService(new InMemoryTaskRepository());

        assertThrows(NotFoundException.class, () -> service.delete("missing-id"));
    }
}
