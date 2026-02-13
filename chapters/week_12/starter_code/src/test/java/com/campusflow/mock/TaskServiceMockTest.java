/*
 * TaskServiceMockTest - Service 层 Mock 测试
 *
 * Week 12 重点：测试替身（Test Double）- 使用 Mock 隔离外部依赖
 *
 * 本测试类演示了如何使用 Mockito 进行 Mock 测试：
 * 1. 使用 @Mock 创建 Mock 对象
 * 2. 使用 @InjectMocks 自动注入 Mock 依赖
 * 3. 使用 when().thenReturn() 设置 Mock 行为
 * 4. 使用 verify() 验证方法调用
 *
 * 测试替身类型：
 * - Dummy: 占位符，不使用
 * - Stub: 预设返回值，不验证调用
 * - Mock: 预设返回值，验证调用次数、参数等
 * - Spy: 部分方法真实实现，部分方法 Mock
 * - Fake: 简化实现（如内存数据库），但功能真实
 */
package com.campusflow.mock;

import com.campusflow.dto.TaskRequest;
import com.campusflow.exception.NotFoundException;
import com.campusflow.exception.ValidationException;
import com.campusflow.model.Task;
import com.campusflow.repository.TaskRepository;
import com.campusflow.service.TaskService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.InOrder;

/**
 * TaskService Mock 测试
 *
 * <p>为什么使用 Mock？
 * <ul>
 *   <li>隔离外部依赖（数据库、第三方 API）</li>
 *   <li>测试速度更快</li>
 *   <li>可以精确控制测试场景</li>
 *   <li>验证方法调用次数和参数</li>
 * </ul>
 *
 * <p>Mock vs 真实依赖：
 * <ul>
 *   <li>Mock 测试：关注逻辑正确性，不关心外部系统</li>
 *   <li>集成测试：关注系统集成，使用真实依赖</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService Mock 测试 - Service 层业务逻辑")
public class TaskServiceMockTest {

    /**
     * @Mock - Mockito 创建的 Mock 对象
     * TaskRepository 是外部依赖，我们不想在单元测试中访问真实数据库
     */
    @Mock
    private TaskRepository mockRepository;

    /**
     * @InjectMocks - 自动将 Mock 对象注入到被测试类中
     * TaskService 依赖 TaskRepository，Mockito 会自动把 mockRepository 注入
     */
    @InjectMocks
    private TaskService taskService;

    // ========== 正例测试（Happy Path）==========

    @Test
    @DisplayName("createTask - 有效任务应保存并返回")
    void createTask_WhenValid_ShouldSaveAndReturn() {
        // given: 准备测试数据
        TaskRequest request = new TaskRequest(
                "测试任务",
                "测试描述",
                "2026-12-31"
        );

        Task savedTask = new Task("1", "测试任务", "测试描述",
                LocalDate.parse("2026-12-31"));

        // when().thenReturn() - 设置 Mock 行为：当调用 save() 时返回 savedTask
        when(mockRepository.save(any(Task.class))).thenReturn(savedTask);

        // when: 执行被测试方法
        Task result = taskService.createTask(request);

        // then: 验证结果
        assertNotNull(result);
        assertEquals("测试任务", result.getTitle());
        assertEquals("测试描述", result.getDescription());
        assertEquals(LocalDate.parse("2026-12-31"), result.getDueDate());

        // verify() - 验证 save() 被调用了一次，且参数是 Task 类型
        verify(mockRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("findById - 存在的任务 ID 应返回任务")
    void findById_WhenExists_ShouldReturnTask() {
        // given: 准备测试数据
        String taskId = "task-123";
        Task task = new Task(taskId, "查询测试", "描述",
                LocalDate.parse("2026-12-31"));

        // 设置 Mock 行为：当调用 findById("task-123") 时返回 Optional.of(task)
        when(mockRepository.findById(taskId)).thenReturn(Optional.of(task));

        // when: 执行被测试方法
        Optional<Task> result = taskService.findById(taskId);

        // then: 验证结果
        assertTrue(result.isPresent());
        assertEquals(taskId, result.get().getId());
        assertEquals("查询测试", result.get().getTitle());

        // 验证 findById() 被调用了一次
        verify(mockRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("findAll - 应返回所有任务列表")
    void findAll_ShouldReturnAllTasks() {
        // given: 准备测试数据
        List<Task> tasks = List.of(
                new Task("1", "任务1", "描述1", LocalDate.now()),
                new Task("2", "任务2", "描述2", LocalDate.now().plusDays(1))
        );

        when(mockRepository.findAll()).thenReturn(tasks);

        // when: 执行被测试方法
        List<Task> result = taskService.findAll();

        // then: 验证结果
        assertEquals(2, result.size());
        assertEquals("任务1", result.get(0).getTitle());
        assertEquals("任务2", result.get(1).getTitle());

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("updateTask - 存在的任务应更新并返回")
    void updateTask_WhenExists_ShouldUpdateAndReturn() {
        // given: 准备测试数据
        String taskId = "task-456";
        Task existingTask = new Task(taskId, "原任务", "原描述",
                LocalDate.parse("2026-12-31"));

        TaskRequest updateRequest = new TaskRequest(
                "更新后的任务",
                "更新后的描述",
                "2027-01-31"
        );

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(mockRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            return t; // 返回更新后的任务
        });

        // when: 执行被测试方法
        Task result = taskService.updateTask(taskId, updateRequest);

        // then: 验证结果
        assertEquals("更新后的任务", result.getTitle());
        assertEquals("更新后的描述", result.getDescription());
        assertEquals(LocalDate.parse("2027-01-31"), result.getDueDate());

        verify(mockRepository, times(1)).findById(taskId);
        verify(mockRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("deleteTask - 存在的任务应删除")
    void deleteTask_WhenExists_ShouldDelete() {
        // given: 准备测试数据
        String taskId = "task-789";
        Task existingTask = new Task(taskId, "待删除任务", "描述",
                LocalDate.now());

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        doNothing().when(mockRepository).delete(taskId);

        // when: 执行被测试方法
        taskService.deleteTask(taskId);

        // then: 验证调用
        verify(mockRepository, times(1)).findById(taskId);
        verify(mockRepository, times(1)).delete(taskId);
    }

    @Test
    @DisplayName("completeTask - 存在的任务应标记为完成")
    void completeTask_WhenExists_ShouldMarkCompleted() {
        // given: 准备测试数据
        String taskId = "task-complete";
        Task task = new Task(taskId, "待完成任务", "描述",
                LocalDate.now());

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mockRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when: 执行被测试方法
        Task result = taskService.completeTask(taskId);

        // then: 验证结果
        assertEquals("completed", result.getStatus());
        assertNotNull(result.getCompletedAt());

        verify(mockRepository, times(1)).findById(taskId);
        verify(mockRepository, times(1)).save(any(Task.class));
    }

    // ========== 边界测试 ==========

    @Test
    @DisplayName("createTask - 缺少可选字段 description 应成功")
    void createTask_WithoutDescription_ShouldSucceed() {
        // given: 准备测试数据（description = null）
        TaskRequest request = new TaskRequest(
                "最少字段任务",
                null,
                "2026-12-31"
        );

        Task savedTask = new Task("1", "最少字段任务", null,
                LocalDate.parse("2026-12-31"));

        when(mockRepository.save(any(Task.class))).thenReturn(savedTask);

        // when: 执行被测试方法
        Task result = taskService.createTask(request);

        // then: 验证结果
        assertNotNull(result);
        assertEquals("最少字段任务", result.getTitle());
        assertNull(result.getDescription());

        verify(mockRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask - 只更新部分字段应保留其他字段")
    void updateTask_WithPartialData_ShouldPreserveOtherFields() {
        // given: 准备测试数据
        String taskId = "task-partial";
        Task existingTask = new Task(taskId, "原标题", "原描述",
                LocalDate.parse("2026-12-31"));

        // 只更新标题
        TaskRequest partialRequest = new TaskRequest();
        partialRequest.setTitle("新标题");
        partialRequest.setDescription(null);
        partialRequest.setDueDate(null);

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(mockRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when: 执行被测试方法
        Task result = taskService.updateTask(taskId, partialRequest);

        // then: 验证结果
        assertEquals("新标题", result.getTitle());
        assertEquals("原描述", result.getDescription()); // 保留原描述
        assertEquals(LocalDate.parse("2026-12-31"), result.getDueDate()); // 保留原日期

        verify(mockRepository, times(1)).findById(taskId);
        verify(mockRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("calculateOverdueFee - 未逾期任务应返回 0")
    void calculateOverdueFee_WhenNotOverdue_ShouldReturnZero() {
        // given: 准备测试数据（未来日期）
        String taskId = "task-not-overdue";
        Task futureTask = new Task(taskId, "未来任务", "描述",
                LocalDate.now().plusDays(10));

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(futureTask));

        // when: 执行被测试方法
        double fee = taskService.calculateOverdueFee(taskId);

        // then: 验证结果
        assertEquals(0.0, fee, 0.001);

        verify(mockRepository, times(1)).findById(taskId);
        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("calculateOverdueFee - 逾期 3 天应返回标准费用")
    void calculateOverdueFee_When3DaysOverdue_ShouldReturnStandardFee() {
        // given: 准备测试数据（逾期 3 天）
        String taskId = "task-3days-overdue";
        Task overdueTask = new Task(taskId, "逾期任务", "描述",
                LocalDate.now().minusDays(3));

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(overdueTask));

        // when: 执行被测试方法
        double fee = taskService.calculateOverdueFee(taskId);

        // then: 验证结果（3 天 * 10 元/天 = 30 元）
        assertEquals(30.0, fee, 0.001);

        verify(mockRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("calculateOverdueFee - 逾期 7 天应返回递升费用")
    void calculateOverdueFee_When7DaysOverdue_ShouldReturnEscalatingFee() {
        // given: 准备测试数据（逾期 7 天）
        String taskId = "task-7days-overdue";
        Task overdueTask = new Task(taskId, "严重逾期任务", "描述",
                LocalDate.now().minusDays(7));

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(overdueTask));

        // when: 执行被测试方法
        double fee = taskService.calculateOverdueFee(taskId);

        // then: 验证结果（3*10 + 4*20 = 30 + 80 = 110 元）
        assertEquals(110.0, fee, 0.001);

        verify(mockRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("calculateOverdueFee - 逾期 10 天应返回严厉费用")
    void calculateOverdueFee_When10DaysOverdue_ShouldReturnSevereFee() {
        // given: 准备测试数据（逾期 10 天）
        String taskId = "task-10days-overdue";
        Task severeOverdueTask = new Task(taskId, "严重逾期任务", "描述",
                LocalDate.now().minusDays(10));

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(severeOverdueTask));

        // when: 执行被测试方法
        double fee = taskService.calculateOverdueFee(taskId);

        // then: 验证结果（3*10 + 4*20 + 3*50 = 30 + 80 + 150 = 260 元）
        assertEquals(260.0, fee, 0.001);

        verify(mockRepository, times(1)).findById(taskId);
    }

    // ========== 反例测试 ==========

    @Test
    @DisplayName("createTask - 空标题应抛出 ValidationException")
    void createTask_WhenTitleEmpty_ShouldThrowException() {
        // given: 准备无效数据
        TaskRequest request = new TaskRequest(
                "",  // 空标题
                "描述",
                "2026-12-31"
        );

        // when & then: 验证抛出异常
        assertThrows(ValidationException.class, () -> {
            taskService.createTask(request);
        });

        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("createTask - null 标题应抛出 ValidationException")
    void createTask_WhenTitleNull_ShouldThrowException() {
        // given: 准备无效数据
        TaskRequest request = new TaskRequest(
                null,  // null 标题
                "描述",
                "2026-12-31"
        );

        // when & then: 验证抛出异常
        assertThrows(ValidationException.class, () -> {
            taskService.createTask(request);
        });

        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("createTask - 只有空格的标题应抛出 ValidationException")
    void createTask_WhenTitleBlank_ShouldThrowException() {
        // given: 准备无效数据
        TaskRequest request = new TaskRequest(
                "   ",  // 只有空格
                "描述",
                "2026-12-31"
        );

        // when & then: 验证抛出异常
        assertThrows(ValidationException.class, () -> {
            taskService.createTask(request);
        });

        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("createTask - 无效日期格式应抛出 ValidationException")
    void createTask_WhenInvalidDateFormat_ShouldThrowException() {
        // given: 准备无效数据
        TaskRequest request = new TaskRequest(
                "任务",
                "描述",
                "12/31/2026"  // 错误的日期格式
        );

        // when & then: 验证抛出异常
        assertThrows(ValidationException.class, () -> {
            taskService.createTask(request);
        });

        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("findById - 不存在的 ID 应返回空 Optional")
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // given: 准备测试数据
        String nonExistentId = "non-existent";
        when(mockRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when: 执行被测试方法
        Optional<Task> result = taskService.findById(nonExistentId);

        // then: 验证结果
        assertFalse(result.isPresent());

        verify(mockRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("updateTask - 不存在的任务 ID 应抛出 NotFoundException")
    void updateTask_WhenNotExists_ShouldThrowNotFoundException() {
        // given: 准备测试数据
        String nonExistentId = "non-existent";
        TaskRequest request = new TaskRequest(
                "更新",
                "描述",
                "2026-12-31"
        );

        when(mockRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then: 验证抛出异常
        assertThrows(NotFoundException.class, () -> {
            taskService.updateTask(nonExistentId, request);
        });

        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask - 空标题应抛出 ValidationException")
    void updateTask_WhenEmptyTitle_ShouldThrowException() {
        // given: 准备测试数据
        String taskId = "task-123";
        Task existingTask = new Task(taskId, "原标题", "描述",
                LocalDate.now());

        TaskRequest request = new TaskRequest();
        request.setTitle("");  // 空标题
        request.setDueDate(null);

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // when & then: 验证抛出异常
        assertThrows(ValidationException.class, () -> {
            taskService.updateTask(taskId, request);
        });

        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask - 无效日期格式应抛出 ValidationException")
    void updateTask_WhenInvalidDateFormat_ShouldThrowException() {
        // given: 准备测试数据
        String taskId = "task-123";
        Task existingTask = new Task(taskId, "原标题", "描述",
                LocalDate.now());

        TaskRequest request = new TaskRequest();
        request.setTitle("新标题");
        request.setDueDate("invalid-date");

        when(mockRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // when & then: 验证抛出异常
        assertThrows(ValidationException.class, () -> {
            taskService.updateTask(taskId, request);
        });

        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("deleteTask - 不存在的任务 ID 应抛出 NotFoundException")
    void deleteTask_WhenNotExists_ShouldThrowNotFoundException() {
        // given: 准备测试数据
        String nonExistentId = "non-existent";
        when(mockRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then: 验证抛出异常
        assertThrows(NotFoundException.class, () -> {
            taskService.deleteTask(nonExistentId);
        });

        // 验证没有调用 delete()
        verify(mockRepository, never()).delete(anyString());
    }

    @Test
    @DisplayName("completeTask - 不存在的任务 ID 应抛出 NotFoundException")
    void completeTask_WhenNotExists_ShouldThrowNotFoundException() {
        // given: 准备测试数据
        String nonExistentId = "non-existent";
        when(mockRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then: 验证抛出异常
        assertThrows(NotFoundException.class, () -> {
            taskService.completeTask(nonExistentId);
        });

        // 验证没有调用 save()
        verify(mockRepository, never()).save(any(Task.class));
    }

    // ========== verify() 高级用法 ==========

    @Test
    @DisplayName("verify - 验证方法调用次数")
    void verify_ShouldCheckCallCount() {
        // given: 准备测试数据
        when(mockRepository.findById("task-1")).thenReturn(Optional.empty());
        when(mockRepository.findById("task-2")).thenReturn(Optional.empty());

        // when: 多次调用
        taskService.findById("task-1");
        taskService.findById("task-1");
        taskService.findById("task-2");

        // then: 验证调用次数
        verify(mockRepository, times(2)).findById("task-1");
        verify(mockRepository, times(1)).findById("task-2");
        verify(mockRepository, never()).findById("task-3");
        verify(mockRepository, atLeastOnce()).findById(anyString());
        verify(mockRepository, atMost(3)).findById(anyString());
    }

    @Test
    @DisplayName("verify - 验证方法调用顺序")
    @Disabled("演示测试，实际项目中可启用")
    void verify_ShouldCheckCallOrder() {
        // given: 准备测试数据
        Task task = new Task("1", "任务", "描述", LocalDate.now());
        when(mockRepository.findById("1")).thenReturn(Optional.of(task));
        when(mockRepository.save(any(Task.class))).thenReturn(task);

        // when: 执行操作
        taskService.completeTask("1");

        // then: 验证调用顺序（先 findById，再 save）
        InOrder inOrder = org.mockito.Mockito.inOrder(mockRepository);
        inOrder.verify(mockRepository).findById("1");
        inOrder.verify(mockRepository).save(any(Task.class));
    }

    // ========== 统计功能测试 ==========

    @Test
    @DisplayName("getStats - 应返回正确的统计数据")
    void getStats_ShouldReturnCorrectStats() {
        // given: 准备测试数据
        List<Task> tasks = List.of(
                new Task("1", "任务1", "描述1", LocalDate.now().minusDays(5)),  // 逾期
                new Task("2", "任务2", "描述2", LocalDate.now().plusDays(5)),  // pending
                new Task("3", "任务3", "描述3", LocalDate.now().plusDays(3))   // pending
        );
        tasks.get(0).setStatus("pending");  // 逾期但未完成
        tasks.get(1).setStatus("completed");

        when(mockRepository.findAll()).thenReturn(tasks);

        // when: 执行被测试方法
        var stats = taskService.getStats();

        // then: 验证结果
        assertEquals(3L, stats.get("total"));
        assertEquals(2L, stats.get("pending"));  // 任务1和3
        assertEquals(0L, stats.get("inProgress"));
        assertEquals(1L, stats.get("completed"));  // 任务2
        assertEquals(1L, stats.get("overdue"));  // 任务1

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getStats - 空列表应返回零值")
    void getStats_WhenEmpty_ShouldReturnZeros() {
        // given: 准备测试数据
        when(mockRepository.findAll()).thenReturn(List.of());

        // when: 执行被测试方法
        var stats = taskService.getStats();

        // then: 验证结果
        assertEquals(0L, stats.get("total"));
        assertEquals(0L, stats.get("pending"));
        assertEquals(0L, stats.get("inProgress"));
        assertEquals(0L, stats.get("completed"));
        assertEquals(0L, stats.get("overdue"));

        verify(mockRepository, times(1)).findAll();
    }
}
