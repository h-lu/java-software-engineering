/*
 * 示例：任务数据访问层
 * 功能：提供任务的 CRUD 操作，使用 SQLite 持久化存储
 * 运行方式：被 Main 类初始化，被 TaskApi 调用
 * 预期输出：将任务数据保存到 campusflow.db 文件
 */
package com.campusflow.repository;

import com.campusflow.model.Task;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 任务数据访问层
 * 使用 SQLite 持久化存储
 */
public class TaskRepository {
    private final String dbPath;

    public TaskRepository(String dbPath) {
        this.dbPath = dbPath;
        initDatabase();
    }

    private void initDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                completed BOOLEAN DEFAULT 0,
                created_at TEXT NOT NULL,
                due_date TEXT
            )
            """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find tasks", e);
        }
        return tasks;
    }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToTask(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find task by id", e);
        }
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            return insert(task);
        } else {
            return update(task);
        }
    }

    private Task insert(Task task) {
        String sql = """
            INSERT INTO tasks (title, description, completed, created_at, due_date)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isCompleted());
            pstmt.setString(4, task.getCreatedAt().toString());
            pstmt.setString(5, task.getDueDate() != null ? task.getDueDate().toString() : null);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                task.setId(rs.getLong(1));
            }
            return task;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save task", e);
        }
    }

    private Task update(Task task) {
        String sql = """
            UPDATE tasks
            SET title = ?, description = ?, completed = ?, due_date = ?
            WHERE id = ?
            """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isCompleted());
            pstmt.setString(4, task.getDueDate() != null ? task.getDueDate().toString() : null);
            pstmt.setLong(5, task.getId());

            pstmt.executeUpdate();
            return task;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update task", e);
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete task", e);
        }
    }

    private Task mapRowToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setCompleted(rs.getBoolean("completed"));
        task.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));

        String dueDate = rs.getString("due_date");
        if (dueDate != null) {
            task.setDueDate(LocalDateTime.parse(dueDate));
        }
        return task;
    }
}
