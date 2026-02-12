/*
 * 示例：完整测试覆盖 - 展示高覆盖率测试的写法。
 * 运行方式：mvn -q -f chapters/week_06/starter_code/pom.xml test -Dtest=LibraryTrackerCompleteTest
 * 预期输出：测试通过，覆盖率报告生成在 target/site/jacoco/index.html
 *
 * 关键概念：
 * - 行覆盖率：多少比例的代码行被执行
 * - 分支覆盖率：多少比例的 if/else 分支被执行
 * - 测试盲区：未被测试覆盖的代码区域
 *
 * JaCoCo Maven 插件配置（pom.xml）:
 * <plugin>
 *     <groupId>org.jacoco</groupId>
 *     <artifactId>jacoco-maven-plugin</artifactId>
 *     <version>0.8.12</version>
 *     <executions>
 *         <execution>
 *             <goals><goal>prepare-agent</goal></goals>
 *         </execution>
 *         <execution>
 *             <id>report</id>
 *             <phase>test</phase>
 *             <goals><goal>report</goal></goals>
 *         </execution>
 *     </executions>
 * </plugin>
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @Override
    public String toString() {
        return title + " (" + author + ")";
    }
}

/**
 * 借阅记录类
 */
class BorrowRecord {
    private String isbn;
    private String borrower;
    private java.time.LocalDate borrowDate;

    public BorrowRecord(String isbn, String borrower) {
        this.isbn = isbn;
        this.borrower = borrower;
        this.borrowDate = java.time.LocalDate.now();
    }

    public String getIsbn() { return isbn; }
    public String getBorrower() { return borrower; }
    public java.time.LocalDate getBorrowDate() { return borrowDate; }
}

/**
 * 完整的图书追踪器实现
 * 目标：通过完整测试达到 80%+ 覆盖率
 */
class LibraryTracker {
    private java.util.HashMap<String, Book> booksByIsbn;
    private java.util.ArrayList<BorrowRecord> borrowRecords;

    public LibraryTracker() {
        booksByIsbn = new java.util.HashMap<>();
        borrowRecords = new java.util.ArrayList<>();
    }

    // ========== 图书管理 ==========

    public void addBook(Book book) {
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

    public java.util.List<Book> listAllBooks() {
        return new java.util.ArrayList<>(booksByIsbn.values());
    }

    public void removeBook(String isbn) {
        booksByIsbn.remove(isbn);
        // 同时清理相关借阅记录
        borrowRecords.removeIf(r -> r.getIsbn().equals(isbn));
    }

    public int getBookCount() {
        return booksByIsbn.size();
    }

    // ========== 借阅管理 ==========

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

    public java.util.List<BorrowRecord> getAllBorrowRecords() {
        return new java.util.ArrayList<>(borrowRecords);
    }

    public int getBorrowRecordCount() {
        return borrowRecords.size();
    }
}

/**
 * 完整测试类 - 目标覆盖率 80%+
 *
 * 测试策略：
 * 1. 正常路径测试：验证功能正确性
 * 2. 异常路径测试：验证防御式编程
 * 3. 边界情况测试：空值、空集合等
 * 4. 集成场景测试：多操作组合
 */
class LibraryTrackerCompleteTest {

    private LibraryTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new LibraryTracker();
    }

    // ========== 图书管理测试 ==========

    @Test
    void shouldAddBookSuccessfully() {
        Book book = new Book("Java 核心技术", "Cay Horstmann", "978-111");

        tracker.addBook(book);

        Book found = tracker.findBook("978-111");
        assertNotNull(found);
        assertEquals("Java 核心技术", found.getTitle());
    }

    @Test
    void shouldListAllBooks() {
        tracker.addBook(new Book("书1", "作者A", "ISBN-1"));
        tracker.addBook(new Book("书2", "作者B", "ISBN-2"));

        var books = tracker.listAllBooks();

        assertEquals(2, books.size());
    }

    @Test
    void shouldRemoveBook() {
        tracker.addBook(new Book("书", "作者", "ISBN-1"));
        assertNotNull(tracker.findBook("ISBN-1"));

        tracker.removeBook("ISBN-1");

        assertNull(tracker.findBook("ISBN-1"));
    }

    @Test
    void shouldRemoveBookAndRelatedBorrowRecords() {
        // 添加书并借阅
        tracker.addBook(new Book("书", "作者", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "小北");
        assertTrue(tracker.hasBorrowRecord("ISBN-1", "小北"));

        // 删除书时应该同时清理借阅记录
        tracker.removeBook("ISBN-1");

        assertEquals(0, tracker.getBorrowRecordCount());
    }

    @Test
    void shouldReturnEmptyListWhenNoBooks() {
        var books = tracker.listAllBooks();
        assertTrue(books.isEmpty());
    }

    @Test
    void shouldReturnNullForNonExistentBook() {
        Book found = tracker.findBook("不存在的ISBN");
        assertNull(found);
    }

    @Test
    void shouldGetBookCount() {
        assertEquals(0, tracker.getBookCount());

        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        assertEquals(1, tracker.getBookCount());

        tracker.addBook(new Book("书2", "作者", "ISBN-2"));
        assertEquals(2, tracker.getBookCount());
    }

    // ========== 借阅管理测试 ==========

    @Test
    void shouldBorrowBookSuccessfully() {
        tracker.addBook(new Book("书", "作者", "ISBN-1"));

        tracker.borrowBook("ISBN-1", "小北");

        assertTrue(tracker.hasBorrowRecord("ISBN-1", "小北"));
    }

    @Test
    void shouldReturnBookSuccessfully() {
        tracker.addBook(new Book("书", "作者", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "小北");
        assertTrue(tracker.hasBorrowRecord("ISBN-1", "小北"));

        tracker.returnBook("ISBN-1", "小北");

        assertFalse(tracker.hasBorrowRecord("ISBN-1", "小北"));
    }

    @Test
    void shouldGetAllBorrowRecords() {
        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        tracker.addBook(new Book("书2", "作者", "ISBN-2"));
        tracker.borrowBook("ISBN-1", "小北");
        tracker.borrowBook("ISBN-2", "阿码");

        var records = tracker.getAllBorrowRecords();

        assertEquals(2, records.size());
    }

    @Test
    void shouldGetBorrowRecordCount() {
        assertEquals(0, tracker.getBorrowRecordCount());

        tracker.addBook(new Book("书", "作者", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "小北");

        assertEquals(1, tracker.getBorrowRecordCount());
    }

    // ========== 异常测试 ==========

    @Test
    void shouldThrowExceptionWhenAddingNullBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.addBook(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenAddingBookWithNullIsbn() {
        Book book = new Book("书", "作者", null);
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.addBook(book);
        });
    }

    @Test
    void shouldThrowExceptionWhenBorrowingNonExistentBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("不存在的ISBN", "小北");
        });
    }

    @Test
    void shouldThrowExceptionWhenReturningNonExistentRecord() {
        assertThrows(IllegalStateException.class, () -> {
            tracker.returnBook("ISBN-1", "小北");
        });
    }

    // ========== 参数化测试（批量边界验证） ==========

    @ParameterizedTest
    @CsvSource({
        "ISBN-1, 小北, true",
        "ISBN-2, 小北, false",
        "ISBN-1, 阿码, false"
    })
    void shouldCheckBorrowStatus(String isbn, String borrower, boolean expected) {
        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "小北");

        assertEquals(expected, tracker.hasBorrowRecord(isbn, borrower));
    }

    // ========== 集成场景测试 ==========

    @Test
    void shouldHandleCompleteBorrowingWorkflow() {
        // 完整借阅流程：添加 -> 借阅 -> 验证 -> 归还 -> 验证
        tracker.addBook(new Book("Java 核心技术", "Cay", "978-111"));
        tracker.addBook(new Book("Effective Java", "Joshua", "978-222"));

        // 借阅
        tracker.borrowBook("978-111", "小北");
        tracker.borrowBook("978-222", "阿码");

        assertEquals(2, tracker.getBorrowRecordCount());
        assertTrue(tracker.hasBorrowRecord("978-111", "小北"));
        assertTrue(tracker.hasBorrowRecord("978-222", "阿码"));

        // 归还
        tracker.returnBook("978-111", "小北");

        assertEquals(1, tracker.getBorrowRecordCount());
        assertFalse(tracker.hasBorrowRecord("978-111", "小北"));
        assertTrue(tracker.hasBorrowRecord("978-222", "阿码"));
    }

    @Test
    void shouldAllowMultipleBorrowsBySamePerson() {
        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        tracker.addBook(new Book("书2", "作者", "ISBN-2"));

        tracker.borrowBook("ISBN-1", "小北");
        tracker.borrowBook("ISBN-2", "小北");

        assertEquals(2, tracker.getBorrowRecordCount());
        assertTrue(tracker.hasBorrowRecord("ISBN-1", "小北"));
        assertTrue(tracker.hasBorrowRecord("ISBN-2", "小北"));
    }
}
