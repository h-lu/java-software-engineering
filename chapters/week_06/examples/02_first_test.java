/*
 * 示例：第一个 JUnit 5 测试。
 * 运行方式：mvn -q -f chapters/week_06/starter_code/pom.xml test -Dtest=LibraryTrackerTest
 * 预期输出：测试通过，显示 "Tests run: 2, Failures: 0"
 *
 * Maven 依赖（pom.xml）:
 * <dependency>
 *     <groupId>org.junit.jupiter</groupId>
 *     <artifactId>junit-jupiter</artifactId>
 *     <version>5.11.0</version>
 *     <scope>test</scope>
 * </dependency>
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 图书类 - 被测试的实体
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
 * 正确的图书追踪器实现
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

    public void borrowBook(String isbn, String borrower) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
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
 * 第一个 JUnit 5 测试类
 *
 * 关键概念：
 * - @Test: 标记这是一个测试方法
 * - assertNotNull: 验证对象不为 null
 * - assertEquals: 验证两个值相等
 * - 测试方法命名规范：shouldXxxWhenYyy 或 should_xxx_when_yyy
 */
class LibraryTrackerTest {

    @Test
    void shouldAddBookSuccessfully() {
        // 准备 (Arrange)
        LibraryTracker tracker = new LibraryTracker();
        Book book = new Book("Java 核心技术", "Cay Horstmann", "978-111");

        // 执行 (Act)
        tracker.addBook(book);

        // 验证 (Assert)
        Book found = tracker.findBook("978-111");
        assertNotNull(found, "应该能找到添加的图书");
        assertEquals("Java 核心技术", found.getTitle(), "书名应该匹配");
        assertEquals("Cay Horstmann", found.getAuthor(), "作者应该匹配");
    }

    @Test
    void shouldFindBookByIsbn() {
        // 准备
        LibraryTracker tracker = new LibraryTracker();
        Book book1 = new Book("书1", "作者A", "ISBN-001");
        Book book2 = new Book("书2", "作者B", "ISBN-002");

        // 执行
        tracker.addBook(book1);
        tracker.addBook(book2);

        // 验证 - 用 HashMap 实现的查找应该是 O(1)
        Book found = tracker.findBook("ISBN-002");
        assertNotNull(found);
        assertEquals("书2", found.getTitle());
    }
}
