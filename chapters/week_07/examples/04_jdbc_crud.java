/*
 * 示例：完整的 JDBC CRUD 操作
 * 运行方式：mvn -q -f chapters/week_07/starter_code/pom.xml exec:java \
 *          -Dexec.mainClass="examples._04_jdbc_crud"
 * 预期输出：
 *   创建图书...
 *   查询图书...
 *   更新图书...
 *   删除图书...
 *   所有操作成功完成
 */
package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 演示完整的 CRUD（增删改查）操作
 *
 * <p>使用 PreparedStatement 防止 SQL 注入攻击，
 * 这是生产环境必须遵守的安全准则。
 */
public class _04_jdbc_crud {

    private static final String DB_URL = "jdbc:sqlite:crud_demo.db";

    public static void main(String[] args) {
        System.out.println("=== JDBC CRUD 演示 ===\n");

        // 初始化数据库
        initializeDatabase();

        BookRepository repository = new BookRepository(DB_URL);

        // ===== CREATE =====
        System.out.println("--- CREATE: 添加图书 ---");
        Book book1 = new Book("978-TEST-001", "Java 编程思想", "Bruce Eckel", true);
        Book book2 = new Book("978-TEST-002", "深入理解 JVM", "周志明", true);

        repository.save(book1);
        repository.save(book2);
        System.out.println("✓ 已添加 2 本书\n");

        // ===== READ =====
        System.out.println("--- READ: 查询图书 ---");
        Optional<Book> found = repository.findByIsbn("978-TEST-001");
        found.ifPresentOrElse(
            book -> System.out.println("✓ 找到图书: " + book.title + " by " + book.author),
            () -> System.out.println("✗ 未找到图书")
        );

        System.out.println("\n所有图书列表:");
        List<Book> allBooks = repository.findAll();
        allBooks.forEach(book -> System.out.println("  - " + book.title));
        System.out.println();

        // ===== UPDATE =====
        System.out.println("--- UPDATE: 更新图书 ---");
        Book updated = new Book("978-TEST-001", "Java 编程思想（第5版）", "Bruce Eckel", false);
        repository.update(updated);
        System.out.println("✓ 图书信息已更新\n");

        // 验证更新
        repository.findByIsbn("978-TEST-001").ifPresent(book ->
            System.out.println("  更新后: " + book.title + ", 可借: " + book.available)
        );
        System.out.println();

        // ===== DELETE =====
        System.out.println("--- DELETE: 删除图书 ---");
        repository.delete("978-TEST-002");
        System.out.println("✓ 图书已删除\n");

        // 验证删除
        System.out.println("剩余图书数量: " + repository.findAll().size());

        System.out.println("\n=== 安全提醒 ===");
        System.out.println("本示例使用 PreparedStatement 防止 SQL 注入");
        System.out.println("永远不要使用字符串拼接 SQL！");
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS books (
                    isbn TEXT PRIMARY KEY,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    available BOOLEAN DEFAULT 1
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("初始化数据库失败", e);
        }
    }
}

/**
 * JDBC 版图书 Repository
 *
 * <p>展示了 Repository 模式在 JDBC 中的实现，
 * 使用 PreparedStatement 实现安全的参数化查询。
 */
class BookRepository {
    private final String url;

    public BookRepository(String url) {
        this.url = url;
    }

    /**
     * CREATE: 插入新图书
     *
     * <p>使用 PreparedStatement 防止 SQL 注入
     */
    public void save(Book book) {
        // 防御式编程：参数校验
        if (book == null || book.isbn == null) {
            throw new IllegalArgumentException("Book 和 ISBN 不能为空");
        }

        // ✅ 正确做法：使用 ? 占位符
        String sql = "INSERT INTO books (isbn, title, author, available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.isbn);
            pstmt.setString(2, book.title);
            pstmt.setString(3, book.author);
            pstmt.setBoolean(4, book.available);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("保存图书失败: " + e.getMessage(), e);
        }
    }

    /**
     * READ: 根据 ISBN 查询图书
     */
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

    /**
     * READ: 查询所有图书
     */
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
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

    /**
     * UPDATE: 更新图书信息
     */
    public void update(Book book) {
        if (book == null || book.isbn == null) {
            throw new IllegalArgumentException("Book 和 ISBN 不能为空");
        }

        String sql = "UPDATE books SET title = ?, author = ?, available = ? WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.title);
            pstmt.setString(2, book.author);
            pstmt.setBoolean(3, book.available);
            pstmt.setString(4, book.isbn);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("图书不存在: " + book.isbn);
            }

        } catch (SQLException e) {
            throw new RuntimeException("更新图书失败: " + e.getMessage(), e);
        }
    }

    /**
     * DELETE: 删除图书
     */
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

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getBoolean("available")
        );
    }
}

/**
 * 图书实体类
 */
class Book {
    String isbn;
    String title;
    String author;
    boolean available;

    public Book(String isbn, String title, String author, boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = available;
    }
}

/*
 * ❌ 反例：SQL 注入漏洞
 *
 * 永远不要这样做：
 * String sql = "SELECT * FROM books WHERE isbn = '" + userInput + "'";
 *
 * 如果 userInput = "' OR '1'='1"
 * 实际执行的 SQL 变成：SELECT * FROM books WHERE isbn = '' OR '1'='1'
 * 这会返回所有图书！
 *
 * 更危险的输入："'; DROP TABLE books; --"
 * 这会导致整个表被删除！
 *
 * ✅ 正确做法：始终使用 PreparedStatement
 */
