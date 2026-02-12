/*
 * 示例：异常测试 - 使用 assertThrows 验证防御式编程。
 * 运行方式：mvn -q -f chapters/week_06/starter_code/pom.xml test -Dtest=LibraryTrackerExceptionTest
 * 预期输出：测试通过，所有异常场景都被正确捕获
 *
 * 关键概念：
 * - assertThrows: 验证代码是否抛出指定异常
 * - Lambda 表达式: 将待测代码包装为可执行块
 * - 异常消息验证: 确保异常信息准确
 *
 * 回顾桥：Week 03 学习的防御式编程，现在用测试来验证
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 图书类
 */
class Book {
    private String title;
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
}

/**
 * 借阅记录类
 */
class BorrowRecord {
    private String isbn;
    private String borrower;

    public BorrowRecord(String isbn, String borrower) {
        this.isbn = isbn;
        this.borrower = borrower;
    }

    public String getIsbn() { return isbn; }
    public String getBorrower() { return borrower; }
}

/**
 * 带防御式编程的图书追踪器
 */
class LibraryTracker {
    private java.util.HashMap<String, Book> booksByIsbn;
    private java.util.ArrayList<BorrowRecord> borrowRecords;

    public LibraryTracker() {
        booksByIsbn = new java.util.HashMap<>();
        borrowRecords = new java.util.ArrayList<>();
    }

    public void addBook(Book book) {
        // 防御式编程：参数校验
        if (book == null) {
            throw new IllegalArgumentException("图书不能为空");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        booksByIsbn.put(book.getIsbn(), book);
    }

    public Book findBook(String isbn) {
        return booksByIsbn.get(isbn);
    }

    public void borrowBook(String isbn, String borrower) {
        // 防御式编程：多层参数校验
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        if (borrower == null || borrower.isEmpty()) {
            throw new IllegalArgumentException("借阅人不能为空");
        }
        if (!booksByIsbn.containsKey(isbn)) {
            throw new IllegalArgumentException("图书不存在：" + isbn);
        }
        borrowRecords.add(new BorrowRecord(isbn, borrower));
    }

    public boolean hasBorrowRecord(String isbn, String borrower) {
        for (BorrowRecord record : borrowRecords) {
            if (record.getIsbn().equals(isbn) && record.getBorrower().equals(borrower)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * 异常测试类
 *
 * 反例（错误的异常测试写法）：
 * ```java
 * @Test
 * void badTest() {
 *     try {
 *         tracker.borrowBook(null, "小北");
 *         fail("应该抛出异常");  // 容易忘记写这行
 *     } catch (IllegalArgumentException e) {
 *         // 测试通过
 *     }
 * }
 * ```
 *
 * 上面的写法容易出错，JUnit 5 提供了 assertThrows 来简化：
 */
class LibraryTrackerExceptionTest {

    private LibraryTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new LibraryTracker();
    }

    @Test
    void shouldThrowExceptionWhenBorrowingWithNullIsbn() {
        tracker.addBook(new Book("书", "作者", "ISBN-1"));

        // assertThrows 接收两个参数：
        // 1. 期望的异常类型（Class 对象）
        // 2. 待执行的代码（Lambda 表达式）
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook(null, "小北");
        });

        // 还可以验证异常消息
        assertEquals("ISBN 不能为空", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBorrowingWithEmptyIsbn() {
        tracker.addBook(new Book("书", "作者", "ISBN-1"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("", "小北");
        });

        assertEquals("ISBN 不能为空", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBorrowingNonExistentBook() {
        // 尝试借阅不存在的书
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("不存在的ISBN", "小北");
        });

        // 使用 contains 验证消息包含特定内容（不要求完全匹配）
        assertTrue(exception.getMessage().contains("图书不存在"));
    }

    @Test
    void shouldThrowExceptionWhenBorrowerIsNull() {
        tracker.addBook(new Book("书", "作者", "ISBN-1"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("ISBN-1", null);
        });

        assertEquals("借阅人不能为空", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.addBook(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenAddingBookWithNullIsbn() {
        Book bookWithNullIsbn = new Book("书", "作者", null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.addBook(bookWithNullIsbn);
        });

        assertEquals("ISBN 不能为空", exception.getMessage());
    }

    /**
     * assertThrows 的返回值是抛出的异常对象，可以进一步验证：
     * - 异常消息
     * - 异常原因（cause）
     * - 异常类型（如果是自定义异常）
     */
    @Test
    void shouldPreserveExceptionDetails() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tracker.borrowBook("未知ISBN", "小北")
        );

        // 验证异常消息包含 ISBN 信息
        String message = exception.getMessage();
        assertNotNull(message);
        assertTrue(message.contains("未知ISBN"), "异常消息应该包含 ISBN 信息");
    }
}
