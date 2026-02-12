/*
 * 示例：CampusFlow JDBC Repository 实现
 * 运行方式：mvn -q -f chapters/week_07/starter_code/pom.xml test \
 *          -Dtest=examples._07_campusflow_jdbc_repository
 * 预期输出：
 *   所有测试通过
 */
package examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * CampusFlow 的 JDBC Repository 实现示例
 *
 * <p>本周任务：将 Week 05-06 的内存版 Repository 迁移到 JDBC 实现
 *
 * <p>涉及概念：
 * <ul>
 *   <li>JDBC 连接管理</li>
 *   <li>PreparedStatement 参数化查询</li>
 *   <li>try-with-resources 资源管理</li>
 *   <li>Repository 模式</li>
 *   <li>H2 内存数据库测试</li>
 * </ul>
 */
public class _07_campusflow_jdbc_repository {

    private static final String TEST_DB_URL = "jdbc:h2:mem:campusflow_test;DB_CLOSE_DELAY=-1";

    private JdbcTaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // 初始化数据库 schema
        initializeSchema();

        // 创建 Repository 实例
        taskRepository = new JdbcTaskRepository(TEST_DB_URL);
    }

    @Test
    void shouldSaveAndFindTask() {
        // 准备
        Task task = new Task("task-001", "完成 Week 07 作业", "编写 JDBC 示例代码", "pending");

        // 执行
        taskRepository.save(task);

        // 验证
        Optional<Task> found = taskRepository.findById("task-001");
        assertTrue(found.isPresent(), "应该能找到任务");
        assertEquals("完成 Week 07 作业", found.get().getTitle());
        assertEquals("pending", found.get().getStatus());
    }

    @Test
    void shouldUpdateExistingTask() {
        // 先保存
        Task task = new Task("task-002", "原标题", "原描述", "pending");
        taskRepository.save(task);

        // 更新（使用 UPSERT 语义）
        Task updated = new Task("task-002", "新标题", "新描述", "done");
        taskRepository.save(updated);

        // 验证
        Optional<Task> found = taskRepository.findById("task-002");
        assertTrue(found.isPresent());
        assertEquals("新标题", found.get().getTitle());
        assertEquals("done", found.get().getStatus());
    }

    @Test
    void shouldFindAllTasks() {
        // 保存多个任务
        taskRepository.save(new Task("t1", "任务1", "描述1", "pending"));
        taskRepository.save(new Task("t2", "任务2", "描述2", "in_progress"));
        taskRepository.save(new Task("t3", "任务3", "描述3", "done"));

        // 查询
        List<Task> tasks = taskRepository.findAll();

        // 验证
        assertEquals(3, tasks.size());
    }

    @Test
    void shouldDeleteTask() {
        // 先保存
        Task task = new Task("task-del", "待删除", "描述", "pending");
        taskRepository.save(task);
        assertTrue(taskRepository.findById("task-del").isPresent());

        // 删除
        taskRepository.delete("task-del");

        // 验证
        assertFalse(taskRepository.findById("task-del").isPresent());
    }

    @Test
    void shouldFindByStatus() {
        // 准备不同状态的任务
        taskRepository.save(new Task("s1", "任务1", "", "pending"));
        taskRepository.save(new Task("s2", "任务2", "", "pending"));
        taskRepository.save(new Task("s3", "任务3", "", "done"));

        // 查询 pending 状态
        List<Task> pendingTasks = taskRepository.findByStatus("pending");

        // 验证
        assertEquals(2, pendingTasks.size());
        assertTrue(pendingTasks.stream().allMatch(t -> t.getStatus().equals("pending")));
    }

    @Test
    void shouldValidateTaskData() {
        // 测试 null 任务
        assertThrows(IllegalArgumentException.class, () -> {
            taskRepository.save(null);
        });

        // 测试 null ID
        assertThrows(IllegalArgumentException.class, () -> {
            taskRepository.save(new Task(null, "标题", "", "pending"));
        });
    }

    // ============ 辅助方法 ============

    private void initializeSchema() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {

            // 创建 tasks 表
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                    id VARCHAR(50) PRIMARY KEY,
                    title VARCHAR(200) NOT NULL,
                    description TEXT,
                    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'in_progress', 'done')),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """);

        } catch (SQLException e) {
            throw new RuntimeException("初始化 schema 失败", e);
        }
    }
}

/**
 * Task 实体类（CampusFlow 核心领域对象）
 */
class Task {
    private final String id;
    private final String title;
    private final String description;
    private final String status;
    private final LocalDateTime createdAt;

    public Task(String id, String title, String description, String status) {
        this(id, title, description, status, LocalDateTime.now());
    }

    public Task(String id, String title, String description, String status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}

/**
 * Task Repository 接口
 */
interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(String id);
    List<Task> findAll();
    List<Task> findByStatus(String status);
    void delete(String id);
}

/**
 * JDBC 版 Task Repository 实现
 *
 * <p>从 Week 05 的内存版迁移而来，使用 SQLite/H2 持久化数据
 */
class JdbcTaskRepository implements TaskRepository {
    private final String url;

    public JdbcTaskRepository(String url) {
        this.url = url;
    }

    @Override
    public void save(Task task) {
        // 防御式编程：参数校验
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task 和 ID 不能为空");
        }

        // UPSERT 操作：如果存在则更新，不存在则插入
        // SQLite/H2 都支持 ON CONFLICT/ON DUPLICATE KEY 语法
        String sql = """
            MERGE INTO tasks (id, title, description, status, created_at)
            KEY (id)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getStatus());
            pstmt.setString(5, task.getCreatedAt().toString());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("保存任务失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Task> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTask(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询任务失败: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询任务列表失败: " + e.getMessage(), e);
        }

        return tasks;
    }

    @Override
    public List<Task> findByStatus(String status) {
        if (status == null || status.isBlank()) {
            return List.of();
        }

        String sql = "SELECT * FROM tasks WHERE status = ? ORDER BY created_at DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("按状态查询任务失败: " + e.getMessage(), e);
        }

        return tasks;
    }

    @Override
    public void delete(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID 不能为空");
        }

        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("删除任务失败: " + e.getMessage(), e);
        }
    }

    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        return new Task(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("status"),
            LocalDateTime.parse(rs.getString("created_at"))
        );
    }
}

/*
 * 迁移说明（从内存版到 JDBC 版）：
 *
 * Week 05-06 的内存版实现：
 * public class InMemoryTaskRepository {
 *     private final Map<String, Task> tasks = new HashMap<>();
 *
 *     public void save(Task task) { tasks.put(task.getId(), task); }
 *     public Optional<Task> findById(String id) { return Optional.ofNullable(tasks.get(id)); }
 *     // ...
 * }
 *
 * Week 07 的 JDBC 版改进：
 * 1. 数据持久化到 SQLite/H2 数据库
 * 2. 使用 try-with-resources 管理连接
 * 3. 使用 PreparedStatement 防止 SQL 注入
 * 4. 保持相同的 Repository 接口，上层代码无需修改
 *
 * 设计决策：
 * - 使用 MERGE/UPSERT 简化 save 操作（同时支持插入和更新）
 * - 数据库 CHECK 约束确保 status 只能是预定义值
 * - 保留 created_at 字段用于排序
 */
