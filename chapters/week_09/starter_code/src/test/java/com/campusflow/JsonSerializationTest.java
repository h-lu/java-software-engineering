package com.campusflow;

import com.campusflow.dto.TaskRequest;
import com.campusflow.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSON 序列化/反序列化测试
 *
 * 测试 Jackson 对 Task 对象的序列化和反序列化
 */
public class JsonSerializationTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Task 对象应正确序列化为 JSON")
    void shouldSerializeTaskToJson() throws Exception {
        Task task = new Task("test-id", "测试任务", "描述", LocalDate.of(2024, 12, 31));

        String json = mapper.writeValueAsString(task);

        assertTrue(json.contains("\"id\":\"test-id\""));
        assertTrue(json.contains("\"title\":\"测试任务\""));
        assertTrue(json.contains("\"status\":\"pending\""));
    }

    @Test
    @DisplayName("JSON 应正确反序列化为 Task 对象")
    void shouldDeserializeJsonToTask() throws Exception {
        String json = """
                {
                    "id": "test-123",
                    "title": "反序列化测试",
                    "description": "测试描述",
                    "status": "completed"
                }
                """;

        Task task = mapper.readValue(json, Task.class);

        assertEquals("test-123", task.getId());
        assertEquals("反序列化测试", task.getTitle());
        assertEquals("测试描述", task.getDescription());
        assertEquals("completed", task.getStatus());
    }

    @Test
    @DisplayName("TaskRequest 应正确反序列化")
    void shouldDeserializeTaskRequest() throws Exception {
        String json = """
                {
                    "title": "新任务",
                    "description": "新描述",
                    "dueDate": "2024-12-31"
                }
                """;

        TaskRequest request = mapper.readValue(json, TaskRequest.class);

        assertEquals("新任务", request.getTitle());
        assertEquals("新描述", request.getDescription());
        assertEquals("2024-12-31", request.getDueDate());
    }

    @Test
    @DisplayName("应处理缺少可选字段的 JSON")
    void shouldHandleMissingOptionalFields() throws Exception {
        String json = """
                {
                    "title": "只有标题"
                }
                """;

        TaskRequest request = mapper.readValue(json, TaskRequest.class);

        assertEquals("只有标题", request.getTitle());
        assertNull(request.getDescription());
        assertNull(request.getDueDate());
    }

    @Test
    @DisplayName("无效 JSON 应抛出异常")
    void shouldThrowExceptionForInvalidJson() {
        String invalidJson = "{ invalid json }";

        assertThrows(Exception.class, () -> {
            mapper.readValue(invalidJson, Task.class);
        });
    }

    @Test
    @DisplayName("空 JSON 对象应创建空 Task")
    void shouldHandleEmptyJsonObject() throws Exception {
        String json = "{}";

        Task task = mapper.readValue(json, Task.class);

        assertNotNull(task);
        assertNull(task.getId());
        assertNull(task.getTitle());
    }
}
