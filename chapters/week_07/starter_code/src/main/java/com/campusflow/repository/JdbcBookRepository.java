package com.campusflow.repository;

import com.campusflow.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC 实现的图书仓储
 * 使用 SQLite 进行数据持久化
 */
public class JdbcBookRepository implements BookRepository {
    private final String url;

    public JdbcBookRepository(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("数据库 URL 不能为空");
        }
        this.url = url;
    }

    @Override
    public void save(Book book) {
        // 防御式编程：参数校验
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Book 和 ISBN 不能为空");
        }

        String sql = "INSERT INTO books (isbn, title, author, available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setBoolean(4, book.isAvailable());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            // 处理重复 ISBN 的情况（支持 SQLite 和 H2 的错误消息）
            String errorMessage = e.getMessage();
            if (errorMessage != null &&
                (errorMessage.contains("UNIQUE constraint failed") ||
                 errorMessage.contains("Unique index or primary key violation"))) {
                throw new IllegalArgumentException("ISBN 已存在: " + book.getIsbn(), e);
            }
            throw new RuntimeException("保存图书失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询图书失败: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books ORDER BY title";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询图书列表失败: " + e.getMessage(), e);
        }

        return books;
    }

    @Override
    public void update(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Book 和 ISBN 不能为空");
        }

        String sql = "UPDATE books SET title = ?, author = ?, available = ? WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setBoolean(3, book.isAvailable());
            pstmt.setString(4, book.getIsbn());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("图书不存在: " + book.getIsbn());
            }

        } catch (SQLException e) {
            throw new RuntimeException("更新图书失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }

        String sql = "DELETE FROM books WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("删除图书失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return false;
        }

        String sql = "SELECT 1 FROM books WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("检查 ISBN 失败: " + e.getMessage(), e);
        }
    }

    /**
     * 辅助方法：将 ResultSet 映射为 Book 对象
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getBoolean("available")
        );
    }
}
