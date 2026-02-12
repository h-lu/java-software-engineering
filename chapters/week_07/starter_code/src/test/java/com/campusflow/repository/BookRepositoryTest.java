package com.campusflow.repository;

import com.campusflow.model.Book;
import com.campusflow.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BookRepository 完整测试类
 * 使用 H2 内存数据库测试 JDBC Repository 实现
 *
 * 测试方法矩阵：
 * - save: 正例(正常添加)、边界(null/空ISBN)、反例(重复ISBN)
 * - findByIsbn: 正例(找到图书)、边界(不存在返回empty)
 * - findAll: 正例(返回所有图书)、边界(空表)
 * - update: 正例(正常更新)、边界(更新不存在的书)
 * - delete: 正例(正常删除)、边界(删除不存在的书)
 * - SQL注入: 安全测试
 */
public class BookRepositoryTest {

    private BookRepository repository;
    private final String testUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    @BeforeEach
    void setUp() {
        // 每个测试前初始化内存数据库
        DatabaseInitializer initializer = new DatabaseInitializer(testUrl);
        initializer.initializeSchema();

        repository = new JdbcBookRepository(testUrl);
    }

    @AfterEach
    void tearDown() {
        // 每个测试后清理数据
        try (Connection conn = DriverManager.getConnection(testUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM books");
        } catch (Exception e) {
            // 忽略清理错误
        }
    }

    // ==================== save 测试 ====================

    @Test
    @DisplayName("正例：正常添加图书应成功")
    void save_WithValidBook_ShouldAddSuccessfully() {
        // 准备
        Book book = new Book("978-TEST-001", "测试书", "测试作者", true);

        // 执行
        repository.save(book);

        // 验证
        Optional<Book> found = repository.findByIsbn("978-TEST-001");
        assertTrue(found.isPresent(), "添加的图书应该能被找到");
        assertEquals("测试书", found.get().getTitle());
        assertEquals("测试作者", found.get().getAuthor());
        assertTrue(found.get().isAvailable());
    }

    @Test
    @DisplayName("正例：添加不可借图书应成功")
    void save_WithUnavailableBook_ShouldAddSuccessfully() {
        // 准备
        Book book = new Book("978-TEST-002", "已借出书", "作者", false);

        // 执行
        repository.save(book);

        // 验证
        Optional<Book> found = repository.findByIsbn("978-TEST-002");
        assertTrue(found.isPresent());
        assertFalse(found.get().isAvailable(), "图书应标记为不可借");
    }

    @Test
    @DisplayName("边界：添加 null 图书应抛出异常")
    void save_WithNullBook_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
        assertEquals("Book 和 ISBN 不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("反例：添加重复 ISBN 应抛出异常")
    void save_WithDuplicateIsbn_ShouldThrowException() {
        // 准备：先添加一本
        Book book1 = new Book("978-DUP-001", "原书名", "作者", true);
        repository.save(book1);

        // 执行 & 验证：再添加相同 ISBN 的图书
        Book book2 = new Book("978-DUP-001", "新书名", "新作者", false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.save(book2);
        });
        assertTrue(exception.getMessage().contains("ISBN 已存在"));
    }

    // ==================== findByIsbn 测试 ====================

    @Test
    @DisplayName("正例：通过 ISBN 查找存在的图书")
    void findByIsbn_WithExistingBook_ShouldReturnBook() {
        // 准备
        Book book = new Book("978-FIND-001", "可查找的书", "作者", true);
        repository.save(book);

        // 执行
        Optional<Book> found = repository.findByIsbn("978-FIND-001");

        // 验证
        assertTrue(found.isPresent());
        assertEquals("可查找的书", found.get().getTitle());
    }

    @Test
    @DisplayName("边界：查找不存在的 ISBN 应返回 empty")
    void findByIsbn_WithNonExistentIsbn_ShouldReturnEmpty() {
        Optional<Book> found = repository.findByIsbn("NOT-EXIST-ISBN");
        assertFalse(found.isPresent(), "不存在的 ISBN 应返回 empty");
    }

    @Test
    @DisplayName("边界：传入 null ISBN 应返回 empty")
    void findByIsbn_WithNullIsbn_ShouldReturnEmpty() {
        Optional<Book> found = repository.findByIsbn(null);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("边界：传入空字符串 ISBN 应返回 empty")
    void findByIsbn_WithEmptyIsbn_ShouldReturnEmpty() {
        Optional<Book> found = repository.findByIsbn("   ");
        assertFalse(found.isPresent());
    }

    // ==================== findAll 测试 ====================

    @Test
    @DisplayName("正例：查找所有图书应返回全部")
    void findAll_WithMultipleBooks_ShouldReturnAllBooks() {
        // 准备
        repository.save(new Book("ISBN-1", "书1", "作者", true));
        repository.save(new Book("ISBN-2", "书2", "作者", true));
        repository.save(new Book("ISBN-3", "书3", "作者", true));

        // 执行
        List<Book> books = repository.findAll();

        // 验证
        assertEquals(3, books.size());
    }

    @Test
    @DisplayName("边界：空表时应返回空列表")
    void findAll_WithEmptyTable_ShouldReturnEmptyList() {
        List<Book> books = repository.findAll();
        assertTrue(books.isEmpty(), "空表应返回空列表");
    }

    @Test
    @DisplayName("正例：返回的列表应按书名排序")
    void findAll_ShouldReturnBooksOrderedByTitle() {
        // 准备：按非字母顺序添加
        repository.save(new Book("ISBN-C", "C书", "作者", true));
        repository.save(new Book("ISBN-A", "A书", "作者", true));
        repository.save(new Book("ISBN-B", "B书", "作者", true));

        // 执行
        List<Book> books = repository.findAll();

        // 验证
        assertEquals("A书", books.get(0).getTitle());
        assertEquals("B书", books.get(1).getTitle());
        assertEquals("C书", books.get(2).getTitle());
    }

    // ==================== update 测试 ====================

    @Test
    @DisplayName("正例：正常更新图书信息")
    void update_WithValidBook_ShouldUpdateSuccessfully() {
        // 准备：先保存
        Book book = new Book("978-UPDATE-001", "原书名", "原作者", true);
        repository.save(book);

        // 执行：更新
        Book updated = new Book("978-UPDATE-001", "新书名", "新作者", false);
        repository.update(updated);

        // 验证
        Optional<Book> found = repository.findByIsbn("978-UPDATE-001");
        assertTrue(found.isPresent());
        assertEquals("新书名", found.get().getTitle());
        assertEquals("新作者", found.get().getAuthor());
        assertFalse(found.get().isAvailable());
    }

    @Test
    @DisplayName("边界：更新不存在的图书应抛出异常")
    void update_WithNonExistentBook_ShouldThrowException() {
        Book book = new Book("978-NOT-EXIST", "书名", "作者", true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.update(book);
        });
        assertTrue(exception.getMessage().contains("图书不存在"));
    }

    @Test
    @DisplayName("边界：更新 null 图书应抛出异常")
    void update_WithNullBook_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.update(null);
        });
    }

    // ==================== delete 测试 ====================

    @Test
    @DisplayName("正例：正常删除图书")
    void delete_WithExistingBook_ShouldDeleteSuccessfully() {
        // 准备
        Book book = new Book("978-DELETE-001", "待删除", "作者", true);
        repository.save(book);
        assertTrue(repository.findByIsbn("978-DELETE-001").isPresent());

        // 执行
        repository.delete("978-DELETE-001");

        // 验证
        assertFalse(repository.findByIsbn("978-DELETE-001").isPresent());
    }

    @Test
    @DisplayName("边界：删除不存在的 ISBN 不应抛出异常")
    void delete_WithNonExistentIsbn_ShouldNotThrowException() {
        // 执行 & 验证：不应抛出异常
        assertDoesNotThrow(() -> repository.delete("NOT-EXIST"));
    }

    @Test
    @DisplayName("边界：传入 null ISBN 应抛出异常")
    void delete_WithNullIsbn_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.delete(null);
        });
    }

    @Test
    @DisplayName("边界：传入空字符串 ISBN 应抛出异常")
    void delete_WithEmptyIsbn_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.delete("   ");
        });
    }

    // ==================== existsByIsbn 测试 ====================

    @Test
    @DisplayName("正例：存在的 ISBN 应返回 true")
    void existsByIsbn_WithExistingBook_ShouldReturnTrue() {
        repository.save(new Book("978-EXIST-001", "书", "作者", true));
        assertTrue(repository.existsByIsbn("978-EXIST-001"));
    }

    @Test
    @DisplayName("边界：不存在的 ISBN 应返回 false")
    void existsByIsbn_WithNonExistentBook_ShouldReturnFalse() {
        assertFalse(repository.existsByIsbn("978-NOT-EXIST"));
    }

    @Test
    @DisplayName("边界：null ISBN 应返回 false")
    void existsByIsbn_WithNullIsbn_ShouldReturnFalse() {
        assertFalse(repository.existsByIsbn(null));
    }

    // ==================== SQL 注入安全测试 ====================

    @Test
    @DisplayName("安全：ISBN 中的单引号不应导致 SQL 注入")
    void save_WithSingleQuoteInIsbn_ShouldHandleSafely() {
        // 准备：包含单引号的 ISBN（可能的 SQL 注入尝试）
        String maliciousIsbn = "978-TEST'O'R'1'='1";
        Book book = new Book(maliciousIsbn, "正常书名", "作者", true);

        // 执行：应能正常保存
        assertDoesNotThrow(() -> repository.save(book));

        // 验证：能正确查回
        Optional<Book> found = repository.findByIsbn(maliciousIsbn);
        assertTrue(found.isPresent());
        assertEquals("正常书名", found.get().getTitle());
    }

    @Test
    @DisplayName("安全：书名中的 SQL 关键字不应导致注入")
    void save_WithSqlKeywordsInTitle_ShouldHandleSafely() {
        // 准备：包含 SQL 关键字的书名
        String maliciousTitle = "书名'; DROP TABLE books; --";
        Book book = new Book("978-SQL-001", maliciousTitle, "作者", true);

        // 执行：应能正常保存
        assertDoesNotThrow(() -> repository.save(book));

        // 验证：数据存在且表未被删除
        Optional<Book> found = repository.findByIsbn("978-SQL-001");
        assertTrue(found.isPresent());
        assertEquals(maliciousTitle, found.get().getTitle());

        // 验证：其他操作仍正常（表未被删除）
        repository.save(new Book("978-AFTER-001", "之后的书", "作者", true));
        // 应该能找到两本书
        List<Book> allBooks = repository.findAll();
        assertTrue(allBooks.size() >= 2, "表未被删除，应至少有两本书");
    }

    @Test
    @DisplayName("安全：作者名中的特殊字符不应导致 SQL 注入")
    void save_WithSpecialCharsInAuthor_ShouldHandleSafely() {
        // 准备：包含多种特殊字符的作者名
        String maliciousAuthor = "作者'); DELETE FROM books WHERE ('1'='1";
        Book book = new Book("978-AUTH-001", "书名", maliciousAuthor, true);

        // 执行：应能正常保存
        assertDoesNotThrow(() -> repository.save(book));

        // 验证：数据正确保存
        Optional<Book> found = repository.findByIsbn("978-AUTH-001");
        assertTrue(found.isPresent());
        assertEquals(maliciousAuthor, found.get().getAuthor());
    }

    @Test
    @DisplayName("安全：查找时使用恶意 ISBN 不应导致 SQL 注入")
    void findByIsbn_WithSqlInjectionAttempt_ShouldReturnEmpty() {
        // 准备：先保存一本正常图书
        repository.save(new Book("978-NORMAL-001", "正常书", "作者", true));

        // 执行：尝试 SQL 注入
        Optional<Book> found = repository.findByIsbn("' OR '1'='1");

        // 验证：应返回 empty，不应返回所有图书
        assertFalse(found.isPresent());
    }

    // ==================== 完整生命周期测试 ====================

    @Test
    @DisplayName("完整生命周期：增删改查流程")
    void fullLifecycle_ShouldWorkCorrectly() {
        // 1. 创建
        Book book = new Book("978-LIFE-001", "生命周期测试书", "测试作者", true);
        repository.save(book);
        assertTrue(repository.existsByIsbn("978-LIFE-001"));

        // 2. 读取
        Optional<Book> found = repository.findByIsbn("978-LIFE-001");
        assertTrue(found.isPresent());
        assertEquals("生命周期测试书", found.get().getTitle());

        // 3. 更新
        Book updated = new Book("978-LIFE-001", "更新后的书名", "更新后的作者", false);
        repository.update(updated);
        Optional<Book> updatedFound = repository.findByIsbn("978-LIFE-001");
        assertTrue(updatedFound.isPresent());
        assertEquals("更新后的书名", updatedFound.get().getTitle());
        assertFalse(updatedFound.get().isAvailable());

        // 4. 删除
        repository.delete("978-LIFE-001");
        assertFalse(repository.existsByIsbn("978-LIFE-001"));
        assertFalse(repository.findByIsbn("978-LIFE-001").isPresent());
    }

    @Test
    @DisplayName("边界：长字符串处理（在列长度限制内）")
    void save_WithLongStringsWithinLimit_ShouldHandleCorrectly() {
        // 准备：较长的书名和作者名（但在 VARCHAR(500) 限制内）
        String longTitle = "A".repeat(400);
        String longAuthor = "B".repeat(150);
        Book book = new Book("978-LONG-001", longTitle, longAuthor, true);

        // 执行
        repository.save(book);

        // 验证
        Optional<Book> found = repository.findByIsbn("978-LONG-001");
        assertTrue(found.isPresent());
        assertEquals(longTitle, found.get().getTitle());
        assertEquals(longAuthor, found.get().getAuthor());
    }

    @Test
    @DisplayName("边界：超长字符串应抛出异常（超过 VARCHAR(500) 限制）")
    void save_WithTooLongStrings_ShouldThrowException() {
        // 准备：超过 500 字符的书名
        String tooLongTitle = "A".repeat(1000);
        Book book = new Book("978-LONG-001", tooLongTitle, "作者", true);

        // 执行 & 验证：应抛出 RuntimeException（数据库限制）
        assertThrows(RuntimeException.class, () -> {
            repository.save(book);
        });
    }

    @Test
    @DisplayName("边界：特殊字符 ISBN 处理")
    void save_WithSpecialCharactersInIsbn_ShouldHandleCorrectly() {
        // 准备：包含各种特殊字符的 ISBN
        String specialIsbn = "978-TEST-@#$%^&*()_+[]{}|;':\",./<>?";
        Book book = new Book(specialIsbn, "书名", "作者", true);

        // 执行
        repository.save(book);

        // 验证
        Optional<Book> found = repository.findByIsbn(specialIsbn);
        assertTrue(found.isPresent());
    }
}
