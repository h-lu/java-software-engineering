/*
 * 示例：参数化测试 - 使用 @ParameterizedTest 批量验证边界情况。
 * 运行方式：mvn -q -f chapters/week_06/starter_code/pom.xml test -Dtest=LibraryTrackerParameterizedTest
 * 预期输出：测试通过，每组参数生成一个独立的测试用例
 *
 * 关键概念：
 * - @ParameterizedTest: 标记参数化测试
 * - @ValueSource: 提供简单值列表
 * - @NullAndEmptySource: 专门提供 null 和空值
 * - @CsvSource: CSV 格式的表格数据
 * - @MethodSource: 引用方法提供参数（本例未展示）
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
 * 参数化测试类
 *
 * 反例（不使用参数化测试时的代码膨胀）：
 * ```java
 * @Test
 * void shouldRejectEmptyIsbn() { ... }
 *
 * @Test
 * void shouldRejectBlankIsbn() { ... }
 *
 * @Test
 * void shouldRejectNullIsbn() { ... }
 *
 * @Test
 * void shouldRejectInvalidIsbn() { ... }
 * // 重复的方法，几乎相同的代码
 * ```
 */
class LibraryTrackerParameterizedTest {

    private LibraryTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new LibraryTracker();
    }

    /**
     * @ValueSource 用于提供简单的值列表（字符串、整数等）
     * 每个值会生成一个独立的测试用例
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "invalid-isbn", "123", "ISBN@#$"})
    void shouldRejectInvalidIsbn(String invalidIsbn) {
        tracker.addBook(new Book("书", "作者", "VALID-ISBN"));

        // 验证所有无效 ISBN 都应该抛出异常
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook(invalidIsbn, "小北");
        });
    }

    /**
     * @NullAndEmptySource 专门用于测试 null 和空字符串
     * 这是边界测试的必备组合
     */
    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullOrEmptyIsbn(String invalidIsbn) {
        tracker.addBook(new Book("书", "作者", "VALID-ISBN"));

        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook(invalidIsbn, "小北");
        });
    }

    /**
     * @CsvSource 用于表格形式的数据
     * 格式："值1, 值2, 期望值"
     * 适合测试输入-输出映射关系
     */
    @ParameterizedTest
    @CsvSource({
        "ISBN-1, 小北, true",    // 正常借阅
        "ISBN-2, 小北, false",   // 书不存在
        "ISBN-1, 阿码, false"    // 书存在但借给其他人了
    })
    void shouldCheckBorrowStatus(String isbn, String borrower, boolean expectedExists) {
        // 准备数据
        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "小北");

        // 验证
        boolean exists = tracker.hasBorrowRecord(isbn, borrower);
        assertEquals(expectedExists, exists,
            String.format("查询 ISBN=%s, borrower=%s 应该返回 %b", isbn, borrower, expectedExists));
    }

    /**
     * 更复杂的 CSV 场景：多字段验证
     */
    @ParameterizedTest
    @CsvSource({
        "T1, 任务1, pending",
        "T2, 任务2, done",
        "T3, 任务3, in_progress"
    })
    void shouldAddAndRetrieveMultipleBooks(String isbn, String title, String author) {
        Book book = new Book(title, author, isbn);
        tracker.addBook(book);

        Book found = tracker.findBook(isbn);
        assertNotNull(found);
        assertEquals(title, found.getTitle());
    }

    /**
     * 可以组合多个数据源
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @NullAndEmptySource
    void shouldRejectAllInvalidBorrowers(String invalidBorrower) {
        tracker.addBook(new Book("书", "作者", "ISBN-1"));

        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("ISBN-1", invalidBorrower);
        });
    }

    /**
     * 使用 name 属性自定义测试报告中的显示名称
     */
    @ParameterizedTest(name = "[{index}] ISBN={0}, borrower={1} 应该返回 {2}")
    @CsvSource({
        "ISBN-1, 小北, true",
        "ISBN-2, 小北, false"
    })
    void shouldCheckBorrowStatusWithCustomName(String isbn, String borrower, boolean expected) {
        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "小北");

        assertEquals(expected, tracker.hasBorrowRecord(isbn, borrower));
    }
}
