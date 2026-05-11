package com.campusflow.service;

import com.campusflow.dto.TaskRequest;
import com.campusflow.exception.NotFoundException;
import com.campusflow.exception.ValidationException;
import com.campusflow.model.Task;
import com.campusflow.repository.TaskRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> findAllEnvelope() {
        List<Task> tasks = repository.findAll();
        tasks.forEach(Task::recalculateOverdueDays);
        return Map.of(
            "data", tasks,
            "total", tasks.size()
        );
    }

    public Task findById(String id) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("任务不存在: " + id));
        task.recalculateOverdueDays();
        return task;
    }

    public Task create(TaskRequest request) {
        validateForCreate(request);
        Task task = new Task(
            request.getTitle().trim(),
            normalizeOptionalText(request.getDescription()),
            request.getDueDate().trim()
        );
        if (request.getCompleted() != null) {
            task.setCompleted(request.getCompleted());
        }
        task.recalculateOverdueDays();
        return repository.save(task);
    }

    public Task update(String id, TaskRequest request) {
        Task existing = findById(id);
        if (request.getTitle() != null) {
            validateTitle(request.getTitle());
            existing.setTitle(request.getTitle().trim());
        }
        if (request.getDescription() != null) {
            existing.setDescription(normalizeOptionalText(request.getDescription()));
        }
        if (request.getDueDate() != null) {
            validateDueDate(request.getDueDate());
            existing.setDueDate(request.getDueDate().trim());
        }
        if (request.getCompleted() != null) {
            existing.setCompleted(request.getCompleted());
        }
        existing.recalculateOverdueDays();
        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("任务不存在: " + id);
        }
    }

    private void validateForCreate(TaskRequest request) {
        if (request == null) {
            throw new ValidationException("请求体不能为空");
        }
        validateTitle(request.getTitle());
        validateDueDate(request.getDueDate());
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("title 不能为空");
        }
    }

    private void validateDueDate(String dueDate) {
        if (dueDate == null || dueDate.isBlank()) {
            throw new ValidationException("dueDate 不能为空，格式示例: 2026-05-11");
        }
        try {
            LocalDate.parse(dueDate.trim());
        } catch (DateTimeParseException e) {
            throw new ValidationException("dueDate 必须使用 ISO 日期格式，例如 2026-05-11");
        }
    }

    private String normalizeOptionalText(String text) {
        return text == null ? "" : text.trim();
    }
}
