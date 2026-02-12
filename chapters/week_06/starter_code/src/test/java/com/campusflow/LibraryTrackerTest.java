package com.campusflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LibraryTracker 完整测试类
 * 覆盖 addBook, findByIsbn, borrowBook, returnBook 等核心方法
 *
 * 测试用例矩阵：
 * - addBook: 正例(正常添加)、边界(null/空ISBN)、反例(重复添加覆盖)
 * - findByIsbn: 正例(找到图书)、边界(不存在返回null)、边界(传入null抛异常)
 * - borrowBook: 正例(正常借书)、边界(书不存在抛异常)、边界(借阅人为空抛异常)
 * - returnBook: 正例(正常还书)、边界(未借出抛异常)、边界(错误借阅人抛异常)
 */
public class LibraryTrackerTest {

    private LibraryTracker tracker;
    private Book testBook;

    @BeforeEach
    void setUp() {
        tracker = new LibraryTracker();
        testBook = new Book("Java 核心技术", "Cay Horstmann", "978-111");
    }

    // ==================== addBook 测试 ====================

    /**
     * 测试类型：正例
     * 输入数据：正常图书对象
     * 预期结果：图书成功添加，可通过 ISBN 找到
     * JUnit 特性：基本断言 assertNotNull, assertEquals
     */
    @Test
    void addBook_WithValidBook_ShouldAddSuccessfully() {
        // 执行
        tracker.addBook(testBook);

        // 验证
        Book found = tracker.findByIsbn("978-111");
        assertNotNull(found, "添加的图书应该能被找到");
        assertEquals("Java 核心技术", found.getTitle());
        assertEquals("Cay Horstmann", found.getAuthor());
    }

    /**
     * 测试类型：边界 - null 输入
     * 输入数据：null
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：assertThrows
     */
    @Test
    void addBook_WithNullBook_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.addBook(null);
        });
        assertEquals("图书不能为空", exception.getMessage());
    }

    /**
     * 测试类型：边界 - 空 ISBN
     * 输入数据：ISBN 为空的图书
     * 预期结果：抛出 IllegalArgumentException（在 Book 构造时）
     * JUnit 特性：assertThrows
     */
    @Test
    void addBook_WithEmptyIsbn_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Book bookWithEmptyIsbn = new Book("测试书", "测试作者", "");
            tracker.addBook(bookWithEmptyIsbn);
        });
    }

    /**
     * 测试类型：反例 - 重复添加（覆盖更新）
     * 输入数据：相同 ISBN 的新图书对象
     * 预期结果：新图书覆盖旧图书
     * JUnit 特性：assertAll 分组断言
     */
    @Test
    void addBook_WithDuplicateIsbn_ShouldUpdateExistingBook() {
        // 准备：先添加一本图书
        Book originalBook = new Book("旧书名", "旧作者", "ISBN-001");
        tracker.addBook(originalBook);
        assertEquals(1, tracker.getBookCount(), "初始应该只有1本书");

        // 执行：添加相同 ISBN 的新图书
        Book newBook = new Book("新书名", "新作者", "ISBN-001");
        tracker.addBook(newBook);

        // 验证：使用 assertAll 进行分组断言
        assertAll("重复添加应覆盖更新",
            () -> assertEquals(1, tracker.getBookCount(), "图书总数应保持为1"),
            () -> {
                Book found = tracker.findByIsbn("ISBN-001");
                assertNotNull(found);
                assertEquals("新书名", found.getTitle(), "书名应被更新");
                assertEquals("新作者", found.getAuthor(), "作者应被更新");
            }
        );
    }

    // ==================== findByIsbn 测试 ====================

    /**
     * 测试类型：正例
     * 输入数据：存在的 ISBN
     * 预期结果：返回对应的图书对象
     * JUnit 特性：基本断言
     */
    @Test
    void findByIsbn_WithExistingIsbn_ShouldReturnBook() {
        // 准备
        tracker.addBook(testBook);

        // 执行
        Book found = tracker.findByIsbn("978-111");

        // 验证
        assertNotNull(found);
        assertEquals("Java 核心技术", found.getTitle());
    }

    /**
     * 测试类型：边界 - 不存在的 ISBN
     * 输入数据：不存在的 ISBN
     * 预期结果：返回 null
     * JUnit 特性：assertNull
     */
    @Test
    void findByIsbn_WithNonExistentIsbn_ShouldReturnNull() {
        // 执行
        Book found = tracker.findByIsbn("NOT-EXIST");

        // 验证
        assertNull(found, "不存在的 ISBN 应该返回 null");
    }

    /**
     * 测试类型：边界 - null 输入
     * 输入数据：null
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：assertThrows
     */
    @Test
    void findByIsbn_WithNullIsbn_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.findByIsbn(null);
        });
        assertEquals("ISBN 不能为 null", exception.getMessage());
    }

    // ==================== borrowBook 测试 ====================

    /**
     * 测试类型：正例
     * 输入数据：存在的 ISBN 和有效借阅人
     * 预期结果：图书被标记为已借出，借阅记录正确
     * JUnit 特性：assertTrue, assertEquals
     */
    @Test
    void borrowBook_WithValidInput_ShouldBorrowSuccessfully() {
        // 准备
        tracker.addBook(testBook);

        // 执行
        tracker.borrowBook("978-111", "小北");

        // 验证
        Book found = tracker.findByIsbn("978-111");
        assertTrue(found.isBorrowed(), "图书应被标记为已借出");
        assertEquals("小北", found.getBorrower(), "借阅人应正确记录");
        assertTrue(tracker.hasBorrowRecord("978-111", "小北"), "应有借阅记录");
    }

    /**
     * 测试类型：边界 - 图书不存在
     * 输入数据：不存在的 ISBN
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：assertThrows, 验证异常消息
     */
    @Test
    void borrowBook_WithNonExistentBook_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("NOT-EXIST", "小北");
        });
        assertTrue(exception.getMessage().contains("图书不存在"));
    }

    /**
     * 测试类型：边界 - 借阅人为空
     * 输入数据：空借阅人
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：@ParameterizedTest + @NullAndEmptySource 批量测试
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void borrowBook_WithEmptyBorrower_ShouldThrowException(String invalidBorrower) {
        // 准备
        tracker.addBook(testBook);

        // 验证
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("978-111", invalidBorrower);
        });
    }

    /**
     * 测试类型：边界 - 图书已被借出
     * 输入数据：已借出图书的 ISBN
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：assertThrows
     */
    @Test
    void borrowBook_WithAlreadyBorrowedBook_ShouldThrowException() {
        // 准备
        tracker.addBook(testBook);
        tracker.borrowBook("978-111", "小北");

        // 验证
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("978-111", "阿码");
        });
        assertTrue(exception.getMessage().contains("已被借出"));
    }

    /**
     * 测试类型：边界 - 无效 ISBN 格式（参数化测试）
     * 输入数据：多种无效 ISBN
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：@ParameterizedTest + @ValueSource 批量测试
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    void borrowBook_WithInvalidIsbn_ShouldThrowException(String invalidIsbn) {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook(invalidIsbn, "小北");
        });
    }

    // ==================== returnBook 测试 ====================

    /**
     * 测试类型：正例
     * 输入数据：已借出图书的正确 ISBN 和借阅人
     * 预期结果：图书被标记为未借出，借阅记录清除
     * JUnit 特性：assertFalse, assertNull
     */
    @Test
    void returnBook_WithValidInput_ShouldReturnSuccessfully() {
        // 准备
        tracker.addBook(testBook);
        tracker.borrowBook("978-111", "小北");
        assertTrue(tracker.hasBorrowRecord("978-111", "小北"));

        // 执行
        tracker.returnBook("978-111", "小北");

        // 验证
        Book found = tracker.findByIsbn("978-111");
        assertFalse(found.isBorrowed(), "图书应被标记为未借出");
        assertNull(found.getBorrower(), "借阅人应被清除");
        assertFalse(tracker.hasBorrowRecord("978-111", "小北"), "借阅记录应被清除");
    }

    /**
     * 测试类型：边界 - 图书未被借出
     * 输入数据：在架图书的 ISBN
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：assertThrows
     */
    @Test
    void returnBook_WithNotBorrowedBook_ShouldThrowException() {
        // 准备
        tracker.addBook(testBook);

        // 验证
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.returnBook("978-111", "小北");
        });
        assertTrue(exception.getMessage().contains("未被借出"));
    }

    /**
     * 测试类型：边界 - 错误的借阅人
     * 输入数据：正确的 ISBN 但错误的借阅人
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：assertThrows, 验证异常消息包含预期内容
     */
    @Test
    void returnBook_WithWrongBorrower_ShouldThrowException() {
        // 准备
        tracker.addBook(testBook);
        tracker.borrowBook("978-111", "小北");

        // 验证
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.returnBook("978-111", "阿码");
        });
        assertTrue(exception.getMessage().contains("借阅人不符"));
    }

    /**
     * 测试类型：边界 - 图书不存在
     * 输入数据：不存在的 ISBN
     * 预期结果：抛出 IllegalArgumentException
     * JUnit 特性：assertThrows
     */
    @Test
    void returnBook_WithNonExistentBook_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.returnBook("NOT-EXIST", "小北");
        });
    }

    // ==================== 参数化批量测试 ====================

    /**
     * 测试类型：正例/边界（批量）
     * 输入数据：多组借阅场景
     * 预期结果：hasBorrowRecord 返回正确值
     * JUnit 特性：@ParameterizedTest + @CsvSource
     */
    @ParameterizedTest(name = "ISBN={0}, 借阅人={1}, 预期存在={2}")
    @CsvSource({
        "ISBN-1, 小北, true",     // 正常借阅记录
        "ISBN-1, 阿码, false",    // 书存在但借给其他人
        "ISBN-2, 小北, false",    // 书不存在
        "ISBN-1, '', false"       // 空借阅人
    })
    void hasBorrowRecord_WithVariousScenarios_ShouldReturnCorrectResult(
            String isbn, String borrower, boolean expectedExists) {
        // 准备
        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "小北");

        // 执行
        boolean exists = tracker.hasBorrowRecord(isbn, borrower);

        // 验证
        assertEquals(expectedExists, exists,
            String.format("ISBN=%s, 借阅人=%s 的预期结果应为 %b", isbn, borrower, expectedExists));
    }

    // ==================== 附加方法测试（提升覆盖率）====================

    /**
     * 测试 listAllBooks 方法
     * JUnit 特性：assertEquals
     */
    @Test
    void listAllBooks_WithMultipleBooks_ShouldReturnAllBooks() {
        // 准备
        tracker.addBook(new Book("书1", "作者A", "ISBN-1"));
        tracker.addBook(new Book("书2", "作者B", "ISBN-2"));
        tracker.addBook(new Book("书3", "作者C", "ISBN-3"));

        // 执行
        var books = tracker.listAllBooks();

        // 验证
        assertEquals(3, books.size(), "应返回所有图书");
    }

    /**
     * 测试 removeBook 方法 - 成功移除
     */
    @Test
    void removeBook_WithExistingBook_ShouldRemoveSuccessfully() {
        // 准备
        tracker.addBook(testBook);
        assertNotNull(tracker.findByIsbn("978-111"));

        // 执行
        boolean removed = tracker.removeBook("978-111");

        // 验证
        assertTrue(removed, "应成功移除");
        assertNull(tracker.findByIsbn("978-111"), "移除后应找不到");
    }

    /**
     * 测试 removeBook 方法 - 移除不存在的书
     */
    @Test
    void removeBook_WithNonExistentBook_ShouldReturnFalse() {
        boolean removed = tracker.removeBook("NOT-EXIST");
        assertFalse(removed, "移除不存在的书应返回 false");
    }

    /**
     * 测试 removeBook 方法 - null 输入
     */
    @Test
    void removeBook_WithNullIsbn_ShouldReturnFalse() {
        boolean removed = tracker.removeBook(null);
        assertFalse(removed, "null ISBN 应返回 false");
    }

    /**
     * 测试 getBookCount 方法
     */
    @Test
    void getBookCount_ShouldReturnCorrectCount() {
        assertEquals(0, tracker.getBookCount(), "初始应为 0");

        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        assertEquals(1, tracker.getBookCount());

        tracker.addBook(new Book("书2", "作者", "ISBN-2"));
        assertEquals(2, tracker.getBookCount());

        tracker.removeBook("ISBN-1");
        assertEquals(1, tracker.getBookCount());
    }

    /**
     * 完整借阅流程测试
     * JUnit 特性：assertAll 进行多维度验证
     */
    @Test
    void fullBorrowAndReturnCycle_ShouldWorkCorrectly() {
        // 准备
        Book book = new Book("设计模式", "GoF", "ISBN-DP");
        tracker.addBook(book);

        // 执行：借阅流程
        tracker.borrowBook("ISBN-DP", "小北");

        // 验证：借阅状态
        assertAll("借阅后状态验证",
            () -> assertTrue(tracker.findByIsbn("ISBN-DP").isBorrowed()),
            () -> assertEquals("小北", tracker.findByIsbn("ISBN-DP").getBorrower()),
            () -> assertTrue(tracker.hasBorrowRecord("ISBN-DP", "小北"))
        );

        // 执行：归还流程
        tracker.returnBook("ISBN-DP", "小北");

        // 验证：归还状态
        assertAll("归还后状态验证",
            () -> assertFalse(tracker.findByIsbn("ISBN-DP").isBorrowed()),
            () -> assertNull(tracker.findByIsbn("ISBN-DP").getBorrower()),
            () -> assertFalse(tracker.hasBorrowRecord("ISBN-DP", "小北"))
        );
    }
}
