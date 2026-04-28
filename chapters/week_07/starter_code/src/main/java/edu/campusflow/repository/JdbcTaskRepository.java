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
            throw new IllegalArgumentException("Database URL cannot be blank");
        }
        this.url = url;
    }

    @Override
    public void save(Task task) {
        // TODO: validate task, then use PreparedStatement and INSERT ... ON CONFLICT update.
        throw new UnsupportedOperationException("TODO: implement save using " + url);
    }

    @Override
    public Optional<Task> findById(String id) {
        // TODO: validate id, query by primary key, and map the ResultSet to Task.
        throw new UnsupportedOperationException("TODO: implement findById");
    }

    @Override
    public List<Task> findAll() {
        // TODO: SELECT all tasks ordered by created_at descending.
        throw new UnsupportedOperationException("TODO: implement findAll");
    }

    @Override
    public void delete(String id) {
        // TODO: validate id and delete with a PreparedStatement.
        throw new UnsupportedOperationException("TODO: implement delete");
    }

    public List<Task> findByStatus(String status) {
        // TODO: query by status with a PreparedStatement.
        throw new UnsupportedOperationException("TODO: implement findByStatus");
    }

    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        // TODO: map id, title, description, status, and created_at.
        throw new UnsupportedOperationException("TODO: implement mapResultSetToTask");
    }
}
