/*
 * 示例：测试生命周期 - 使用 @BeforeEach 消除重复代码。
 * 运行方式：mvn -q -f chapters/week_06/starter_code/pom.xml test -Dtest=LibraryTrackerLifecycleTest
 * 预期输出：测试通过，每个测试方法前都会执行 setUp()
 *
 * 关键概念：
 * - @BeforeEach: 每个测试方法执行前运行
 * - @AfterEach: 每个测试方法执行后运行（本例未展示）
 * - 测试隔离：每个测试都应该从干净状态开始
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
 * 图书追踪器
 */
class LibraryTracker {
    private java.util.HashMap<String, Book> booksByIsbn;
    private java.util.ArrayList<BorrowRecord> borrowRecords;

    public LibraryTracker() {
        booksByIsbn = new java.util.HashMap<>();
        borrowRecords = new java.util.ArrayList<>();
    }

    public void addBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("图书信息不完整");
        }
        booksByIsbn.put(book.getIsbn(), book);
    }

    public Book findBook(String isbn) {
        return booksByIsbn.get(isbn);
    }

    public java.util.List<Book> listAllBooks() {
        return new java.util.ArrayList<>(booksByIsbn.values());
    }

    public void removeBook(String isbn) {
        booksByIsbn.remove(isbn);
    }

    public void borrowBook(String isbn, String borrower) {
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

    public void returnBook(String isbn, String borrower) {
        java.util.Iterator<BorrowRecord> it = borrowRecords.iterator();
        while (it.hasNext()) {
            BorrowRecord record = it.next();
            if (record.getIsbn().equals(isbn) && record.getBorrower().equals(borrower)) {
                it.remove();
                return;
            }
        }
        throw new IllegalStateException("未找到对应的借阅记录");
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
 * 展示测试生命周期的测试类
 *
 * 反例（不使用 @BeforeEach 时的重复代码）：
 * ```java
 * @Test
 * void test1() {
 *     LibraryTracker tracker = new LibraryTracker();  // 重复
 *     Book testBook = new Book("测试书", "作者", "TEST-ISBN");  // 重复
 *     // ... 测试逻辑
 * }
 *
 * @Test
 * void test2() {
 *     LibraryTracker tracker = new LibraryTracker();  // 重复
 *     Book testBook = new Book("测试书", "作者", "TEST-ISBN");  // 重复
 *     // ... 测试逻辑
 * }
 * ```
 */
class LibraryTrackerLifecycleTest {

    // 实例变量，在 setUp() 中初始化
    private LibraryTracker tracker;
    private Book testBook;

    /**
     * @BeforeEach 标记的方法会在每个 @Test 方法执行前运行。
     * 这确保了每个测试都从一个干净、一致的状态开始。
     *
     * 为什么重要？
     * - 测试隔离：一个测试的失败不应该影响其他测试
     * - 可重复性：无论单独运行还是批量运行，结果都一致
     * - DRY 原则：避免在每个测试方法中重复初始化代码
     */
    @BeforeEach
    void setUp() {
        // 每个测试方法前都会执行
        tracker = new LibraryTracker();
        testBook = new Book("测试书", "测试作者", "TEST-ISBN");
    }

    @Test
    void shouldAddBook() {
        // 直接使用 setUp() 创建的 tracker 和 testBook
        tracker.addBook(testBook);
        assertNotNull(tracker.findBook("TEST-ISBN"));
    }

    @Test
    void shouldBorrowBook() {
        // 借阅流程测试
        tracker.addBook(testBook);
        tracker.borrowBook("TEST-ISBN", "小北");

        // 验证借阅记录存在
        assertTrue(tracker.hasBorrowRecord("TEST-ISBN", "小北"));
    }

    @Test
    void shouldReturnBook() {
        // 归还流程测试
        tracker.addBook(testBook);
        tracker.borrowBook("TEST-ISBN", "小北");
        tracker.returnBook("TEST-ISBN", "小北");

        // 验证借阅记录已清除
        assertFalse(tracker.hasBorrowRecord("TEST-ISBN", "小北"));
    }

    @Test
    void shouldListAllBooks() {
        tracker.addBook(testBook);
        tracker.addBook(new Book("书2", "作者B", "ISBN-2"));

        var books = tracker.listAllBooks();

        assertEquals(2, books.size());
    }

    @Test
    void shouldRemoveBook() {
        tracker.addBook(testBook);
        assertNotNull(tracker.findBook("TEST-ISBN"));

        tracker.removeBook("TEST-ISBN");

        assertNull(tracker.findBook("TEST-ISBN"));
    }

    /**
     * @AfterEach 示例（如果需要清理资源）
     *
     * @AfterEach
     * void tearDown() {
     *     // 清理临时文件、关闭数据库连接等
     *     // 对于内存存储，通常不需要显式清理
     * }
     */
}
