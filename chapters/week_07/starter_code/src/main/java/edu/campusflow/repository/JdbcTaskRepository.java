package edu.campusflow.repository;

import edu.campusflow.model.Task;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTaskRepository implements TaskRepository {
    private final String url;

    public JdbcTaskRepository(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Database URL 不能为空");
        }
        this.url = url;
    }

    @Override
    public void save(Task task) {
        // 待办：校验 task，然后使用 PreparedStatement 和 INSERT ... ON CONFLICT 更新。
        throw new UnsupportedOperationException("待办：基于该 URL 实现 save：" + url);
    }

    @Override
    public Optional<Task> findById(String id) {
        // 待办：校验 id，按主键查询，并把 ResultSet 映射为 Task。
        throw new UnsupportedOperationException("待办：实现 findById");
    }

    @Override
    public List<Task> findAll() {
        // 待办：查询所有 tasks，并按 created_at 倒序排列。
        throw new UnsupportedOperationException("待办：实现 findAll");
    }

    @Override
    public void delete(String id) {
        // 待办：校验 id，并用 PreparedStatement 删除记录。
        throw new UnsupportedOperationException("待办：实现 delete");
    }

    public List<Task> findByStatus(String status) {
        // 待办：用 PreparedStatement 按 status 查询。
        throw new UnsupportedOperationException("待办：实现 findByStatus");
    }

    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        // 待办：映射 id、title、description、status 和 created_at。
        throw new UnsupportedOperationException("待办：实现 mapResultSetToTask");
    }
}
