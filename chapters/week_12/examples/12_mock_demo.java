/*
 * 示例：Mockito Mock 示例
 * 运行方式：阅读理解（需 JUnit 5 + Mockito）
 * 预期输出：理解 Mock 对象的创建和使用
 *
 * 本例演示：
 * 1. 展示 Mock 对象的创建
 * 2. when().thenReturn() 语法
 * 3. verify() 验证方法调用
 * 4. 对比真实依赖和 Mock 依赖
 */
package examples;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Mock 示例：测试替身的使用
 *
 * <p>Mock 是一种测试替身（Test Double），用于隔离外部依赖。
 *
 * <p>Mock 的特点：
 * <ul>
 *   <li>预设返回值（when().thenReturn()）</li>
 *   <li>验证方法调用（verify()）</li>
 *   <li>隔离外部依赖（数据库、网络、文件系统）</li>
 *   <li>加快测试速度</li>
 * </ul>
 */
public class _12_mock_demo {

    /**
     * 示例 1：最简单的 Mock
     *
     * <p>使用 Mockito.mock() 创建 Mock 对象。
     */
    @Test
    @DisplayName("Mock 1: 创建 Mock 对象")
    void createMock() {
        // given: 创建 Mock 对象
        List<String> mockList = mock(List.class);

        // when: 调用 Mock 对象的方法
        mockList.add("Hello");
        mockList.clear();

        // then: 调用不会真正执行，但不会报错
        // Mock 对象会"假装"执行了这些方法

        // 验证：检查方法是否被调用
        verify(mockList).add("Hello");      // ✅ 验证通过
        verify(mockList).clear();           // ✅ 验证通过
    }

    /**
     * 示例 2：预设返回值（Stubbing）
     *
     * <p>使用 when().thenReturn() 预设 Mock 对象的返回值。
     */
    @Test
    @DisplayName("Mock 2: 预设返回值")
    void stubReturnValue() {
        // given: 创建 Mock 对象并预设返回值
        List<String> mockList = mock(List.class);
        when(mockList.get(0)).thenReturn("First Element");
        when(mockList.get(1)).thenReturn("Second Element");

        // when: 调用方法
        String first = mockList.get(0);
        String second = mockList.get(1);

        // then: 返回预设的值
        assertEquals("First Element", first);
        assertEquals("Second Element", second);

        // 未预设的方法返回默认值
        assertNull(mockList.get(999));  // null 是 List.get() 的默认返回值
    }

    /**
     * 示例 3：预设异常
     *
     * <p>使用 when().thenThrow() 预设 Mock 对象抛出异常。
     */
    @Test
    @DisplayName("Mock 3: 预设异常")
    void stubException() {
        // given: 创建 Mock 对象并预设异常
        List<String> mockList = mock(List.class);
        when(mockList.get(anyInt())).thenThrow(
            new IndexOutOfBoundsException("Index out of bounds"));

        // when & then: 调用方法会抛出预设的异常
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mockList.get(0);
        });
    }

    /**
     * 示例 4：验证调用次数
     *
     * <p>使用 verify() 验证方法被调用的次数。
     */
    @Test
    @DisplayName("Mock 4: 验证调用次数")
    void verifyInvocationCount() {
        // given: 创建 Mock 对象
        List<String> mockList = mock(List.class);

        // when: 调用方法多次
        mockList.add("once");
        mockList.add("twice");
        mockList.add("twice");

        // then: 验证调用次数
        verify(mockList).add("once");        // 调用 1 次
        verify(mockList, times(2)).add("twice");  // 调用 2 次
        verify(mockList, never()).add("never");   // 从未调用
    }

    /**
     * 示例 5：验证调用顺序
     *
     * <p>使用 InOrder 验证方法的调用顺序。
     */
    @Test
    @DisplayName("Mock 5: 验证调用顺序")
    void verifyInvocationOrder() {
        // given: 创建 Mock 对象
        List<String> mockList = mock(List.class);

        // when: 按顺序调用方法
        mockList.add("first");
        mockList.add("second");
        mockList.clear();

        // then: 验证调用顺序
        InOrder inOrder = inOrder(mockList);
        inOrder.verify(mockList).add("first");
        inOrder.verify(mockList).add("second");
        inOrder.verify(mockList).clear();
    }

    /**
     * 示例 6：使用 @Mock 注解
     *
     * <p>使用 @Mock 注解自动创建 Mock 对象（需要 MockitoExtension）。
     */
    @Test
    @DisplayName("Mock 6: 使用 @Mock 注解")
    @ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
    void useMockAnnotation(@Mock List<String> mockList) {
        // given: Mock 对象已自动创建

        // when: 预设返回值
        when(mockList.size()).thenReturn(100);

        // then: 验证返回值
        assertEquals(100, mockList.size());
    }

    // ========== 实际场景示例 ==========

    /**
     * 示例 7：Mock Repository（真实场景）
     *
     * <p>在单元测试中 Mock 数据库访问层。
     */
    @Test
    @DisplayName("Mock 7: Mock Repository")
    void mockRepository() {
        // given: Mock Repository
        TaskRepository mockRepo = mock(TaskRepository.class);
        when(mockRepo.findById("1")).thenReturn(
            Optional.of(new Task("1", "学习 Mockito", false)));
        when(mockRepo.findById("999")).thenReturn(Optional.empty());

        TaskService service = new TaskService(mockRepo);

        // when: 调用 Service 方法
        Task task = service.getTask("1").orElse(null);

        // then: 验证返回值
        assertNotNull(task);
        assertEquals("学习 Mockito", task.title);

        // 验证 Repository 方法被调用
        verify(mockRepo).findById("1");
    }

    /**
     * 示例 8：验证参数匹配
     *
     * <p>使用 ArgumentMatchers 匹配任意参数。
     */
    @Test
    @DisplayName("Mock 8: 参数匹配")
    void argumentMatchers() {
        // given: Mock Repository
        TaskRepository mockRepo = mock(TaskRepository.class);
        when(mockRepo.findById(anyString())).thenReturn(
            Optional.of(new Task("1", "任意任务", false)));

        TaskService service = new TaskService(mockRepo);

        // when: 使用任意 ID 调用
        Task task = service.getTask("any-id").orElse(null);

        // then: 返回预设的值
        assertNotNull(task);
        assertEquals("任意任务", task.title);

        // 验证方法被调用（任意参数）
        verify(mockRepo).findById(anyString());
    }

    /**
     * 示例 9：验证未调用的方法
     *
     * <p>使用 verifyNoMoreInteractions() 确保没有意外调用。
     */
    @Test
    @DisplayName("Mock 9: 验证未调用的方法")
    void verifyNoMoreInteractions() {
        // given: Mock Repository
        TaskRepository mockRepo = mock(TaskRepository.class);
        when(mockRepo.findById("1")).thenReturn(
            Optional.of(new Task("1", "任务", false)));

        // when: 只调用 findById
        mockRepo.findById("1");

        // then: 验证只调用了 findById
        verify(mockRepo).findById("1");
        verifyNoMoreInteractions(mockRepo);  // ✅ 没有其他调用

        // 如果调用了其他方法，这个测试会失败：
        // mockRepo.findAll();  // 取消注释会失败
    }

    /**
     * 示例 10：Mock vs 真实对象对比
     *
     * <p>对比 Mock 对象和真实对象的行为。
     */
    @Test
    @DisplayName("Mock 10: Mock vs 真实对象")
    void mockVsReal() {
        // given: Mock 对象和真实对象
        TaskRepository mockRepo = mock(TaskRepository.class);
        TaskRepository realRepo = new InMemoryTaskRepository();

        // Mock: 预设返回值
        when(mockRepo.findById("1")).thenReturn(
            Optional.of(new Task("1", "Mock 任务", false)));

        // 真实对象: 需要先保存数据
        realRepo.save(new Task("1", "真实任务", false));

        // when: 查询数据
        Task mockResult = mockRepo.findById("1").orElse(null);
        Task realResult = realRepo.findById("1").orElse(null);

        // then: Mock 返回预设值，真实对象返回实际数据
        assertEquals("Mock 任务", mockResult.title);   // Mock 数据
        assertEquals("真实任务", realResult.title);    // 真实数据

        // Mock 不会真正调用数据库，速度更快
        // 但如果 SQL 有错误，Mock 测试无法发现
    }

    // ========== 辅助类 ==========

    /**
     * Task 实体（示例用）。
     */
    static class Task {
        String id;
        String title;
        boolean completed;

        Task(String id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }
    }

    /**
     * Task Repository 接口（示例用）。
     */
    interface TaskRepository {
        Optional<Task> findById(String id);
        List<Task> findAll();
        Task save(Task task);
        void delete(String id);
    }

    /**
     * Task Service（示例用）。
     */
    static class TaskService {
        private final TaskRepository repository;

        TaskService(TaskRepository repository) {
            this.repository = repository;
        }

        public Optional<Task> getTask(String id) {
            return repository.findById(id);
        }

        public List<Task> getAllTasks() {
            return repository.findAll();
        }
    }

    /**
     * 内存 Repository 实现（真实对象，用于对比）。
     */
    static class InMemoryTaskRepository implements TaskRepository {
        private final java.util.Map<String, Task> tasks = new java.util.HashMap<>();

        @Override
        public Optional<Task> findById(String id) {
            return Optional.ofNullable(tasks.get(id));
        }

        @Override
        public List<Task> findAll() {
            return new java.util.ArrayList<>(tasks.values());
        }

        @Override
        public Task save(Task task) {
            tasks.put(task.id, task);
            return task;
        }

        @Override
        public void delete(String id) {
            tasks.remove(id);
        }
    }
}

/**
 * Mockito 常用方法速查表
 *
 * <table>
 *   <tr><th>方法</th><th>说明</th></tr>
 *   <tr><td>mock(Class)</td><td>创建 Mock 对象</td></tr>
 *   <tr><td>when(method).thenReturn(value)</td><td>预设返回值</td></tr>
 *   <tr><td>when(method).thenThrow(exception)</td><td>预设异常</td></tr>
 *   <tr><td>verify(mock).method()</td><td>验证方法被调用 1 次</td></tr>
 *   <tr><td>verify(mock, times(n)).method()</td><td>验证方法被调用 n 次</td></tr>
 *   <tr><td>verify(mock, never()).method()</td><td>验证方法从未被调用</td></tr>
 *   <tr><td>verifyNoMoreInteractions(mock)</td><td>验证没有其他调用</td></tr>
 *   <tr><td>inOrder(mock).verify(mock).method()</td><td>验证调用顺序</td></tr>
 *   <tr><td>any() / anyString() / anyInt()</td><td>匹配任意参数</td></tr>
 * </table>
 *
 * <p>Mockito 注解：
 * <ul>
 *   <li>@Mock: 创建 Mock 对象</li>
 *   <li>@Spy: 创建 Spy 对象（部分真实实现）</li>
 *   <li>@InjectMocks: 自动注入 Mock 对象到被测类</li>
 * </ul>
 *
 * <p>需要添加依赖：
 * <pre>
 * &lt;dependency&gt;
 *   &lt;groupId&gt;org.mockito&lt;/groupId&gt;
 *   &lt;artifactId&gt;mockito-core&lt;/artifactId&gt;
 *   &lt;version&gt;5.12.0&lt;/version&gt;
 *   &lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * &lt;dependency&gt;
 *   &lt;groupId&gt;org.mockito&lt;/groupId&gt;
 *   &lt;artifactId&gt;mockito-junit-jupiter&lt;/artifactId&gt;
 *   &lt;version&gt;5.12.0&lt;/version&gt;
 *   &lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * </pre>
 */
