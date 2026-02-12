import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LibraryTracker 综合测试类
 * 测试图书借阅追踪器的完整功能
 */
@DisplayName("图书借阅追踪器综合测试")
public class LibraryTrackerTest {

    private LibraryTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new LibraryTracker();
    }

    // ========== 图书管理测试 ==========

    @Test
    @DisplayName("添加图书后应能通过 ISBN 查找")
    void shouldFindBookByIsbnAfterAdding() {
        // Given
        Book book = new Book("Java 核心技术", "Cay Horstmann", "978-111");

        // When
        tracker.addBook(book);

        // Then
        Book found = tracker.findBook("978-111");
        assertNotNull(found);
        assertEquals("Java 核心技术", found.getTitle());
        assertEquals("Cay Horstmann", found.getAuthor());
    }

    @Test
    @DisplayName("添加多本图书后应能列出所有图书")
    void shouldListAllBooksAfterAddingMultiple() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));
        tracker.addBook(new Book("Book 2", "Author 2", "ISBN-2"));
        tracker.addBook(new Book("Book 3", "Author 3", "ISBN-3"));

        // When
        List<Book> books = tracker.listAllBooks();

        // Then
        assertEquals(3, books.size());
    }

    @Test
    @DisplayName("删除图书后应无法查找")
    void shouldNotFindBookAfterRemoval() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));

        // When
        tracker.removeBook("ISBN-1");

        // Then
        assertNull(tracker.findBook("ISBN-1"));
        assertEquals(0, tracker.getBookCount());
    }

    @Test
    @DisplayName("检查图书是否存在应正确")
    void shouldCorrectlyCheckIfBookExists() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));

        // Then
        assertTrue(tracker.hasBook("ISBN-1"));
        assertFalse(tracker.hasBook("ISBN-NOT-EXIST"));
    }

    @Test
    @DisplayName("相同 ISBN 的图书应覆盖")
    void shouldOverrideBookWithSameIsbn() {
        // Given
        tracker.addBook(new Book("Old Title", "Old Author", "ISBN-1"));
        tracker.addBook(new Book("New Title", "New Author", "ISBN-1"));

        // When
        Book found = tracker.findBook("ISBN-1");

        // Then
        assertEquals("New Title", found.getTitle());
        assertEquals(1, tracker.getBookCount());
    }

    // ========== 借阅管理测试 ==========

    @Test
    @DisplayName("借阅图书后应生成借阅记录")
    void shouldCreateBorrowRecordWhenBorrowingBook() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));

        // When
        tracker.borrowBook("ISBN-1", "张三");

        // Then
        assertEquals(1, tracker.getBorrowRecordCount());
    }

    @Test
    @DisplayName("应能查询用户的所有借阅记录")
    void shouldQueryBorrowRecordsByUser() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));
        tracker.addBook(new Book("Book 2", "Author 2", "ISBN-2"));
        tracker.addBook(new Book("Book 3", "Author 3", "ISBN-3"));

        tracker.borrowBook("ISBN-1", "张三");
        tracker.borrowBook("ISBN-2", "张三");
        tracker.borrowBook("ISBN-3", "李四");

        // When
        List<BorrowRecord> zhangRecords = tracker.getBorrowRecordsByUser("张三");
        List<BorrowRecord> liRecords = tracker.getBorrowRecordsByUser("李四");

        // Then
        assertEquals(2, zhangRecords.size());
        assertEquals(1, liRecords.size());
    }

    @Test
    @DisplayName("归还图书后应删除借阅记录")
    void shouldRemoveBorrowRecordWhenReturningBook() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "张三");
        assertEquals(1, tracker.getBorrowRecordCount());

        // When
        tracker.returnBook("ISBN-1", "张三");

        // Then
        assertEquals(0, tracker.getBorrowRecordCount());
        assertTrue(tracker.getBorrowRecordsByUser("张三").isEmpty());
    }

    @Test
    @DisplayName("归还错误的借阅应抛出异常")
    void shouldThrowExceptionWhenReturningWrongBorrow() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "张三");

        // When & Then - 尝试归还错误的借阅
        assertThrows(IllegalStateException.class, () -> {
            tracker.returnBook("ISBN-1", "李四");  // 借阅人是张三，不是李四
        });

        assertThrows(IllegalStateException.class, () -> {
            tracker.returnBook("ISBN-2", "张三");  // ISBN 不存在
        });
    }

    // ========== 边界测试 ==========

    @Test
    @DisplayName("添加 null 图书应抛出异常")
    void shouldThrowExceptionWhenAddingNullBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.addBook(null);
        });
    }

    @Test
    @DisplayName("添加 ISBN 为 null 的图书应抛出异常")
    void shouldThrowExceptionWhenAddingBookWithNullIsbn() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.addBook(new Book("Title", "Author", null));
        });
    }

    @Test
    @DisplayName("借阅 null ISBN 应抛出异常")
    void shouldThrowExceptionWhenBorrowingWithNullIsbn() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook(null, "张三");
        });
    }

    @Test
    @DisplayName("借阅空 ISBN 应抛出异常")
    void shouldThrowExceptionWhenBorrowingWithEmptyIsbn() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("", "张三");
        });
    }

    @Test
    @DisplayName("借阅 null 借阅人应抛出异常")
    void shouldThrowExceptionWhenBorrowingWithNullBorrower() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("ISBN-1", null);
        });
    }

    @Test
    @DisplayName("借阅空借阅人应抛出异常")
    void shouldThrowExceptionWhenBorrowingWithEmptyBorrower() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("ISBN-1", "");
        });
    }

    @Test
    @DisplayName("借阅不存在的图书应抛出异常")
    void shouldThrowExceptionWhenBorrowingNonExistentBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("ISBN-NOT-EXIST", "张三");
        });
    }

    @Test
    @DisplayName("空追踪器的计数应为 0")
    void shouldHaveZeroCountsWhenEmpty() {
        assertEquals(0, tracker.getBookCount());
        assertEquals(0, tracker.getBorrowRecordCount());
        assertTrue(tracker.listAllBooks().isEmpty());
    }

    @Test
    @DisplayName("查询不存在的用户应返回空列表")
    void shouldReturnEmptyListForNonExistentUser() {
        List<BorrowRecord> records = tracker.getBorrowRecordsByUser("不存在的用户");
        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    // ========== 集成测试 ==========

    @Test
    @DisplayName("完整借阅流程应正常工作")
    void shouldHandleCompleteBorrowingWorkflow() {
        // 1. 添加图书
        tracker.addBook(new Book("Java 核心技术", "Cay", "978-111"));
        tracker.addBook(new Book("Effective Java", "Joshua", "978-222"));
        assertEquals(2, tracker.getBookCount());

        // 2. 借阅
        tracker.borrowBook("978-111", "张三");
        tracker.borrowBook("978-222", "张三");
        tracker.borrowBook("978-111", "李四");
        assertEquals(3, tracker.getBorrowRecordCount());

        // 3. 查询
        List<BorrowRecord> zhangRecords = tracker.getBorrowRecordsByUser("张三");
        assertEquals(2, zhangRecords.size());

        // 4. 归还
        tracker.returnBook("978-111", "张三");
        assertEquals(2, tracker.getBorrowRecordCount());

        // 5. 验证
        zhangRecords = tracker.getBorrowRecordsByUser("张三");
        assertEquals(1, zhangRecords.size());
        assertEquals("978-222", zhangRecords.get(0).getIsbn());
    }

    @Test
    @DisplayName("删除图书不影响借阅记录查询（数据一致性测试）")
    void shouldHandleBookRemovalIndependently() {
        // Given
        tracker.addBook(new Book("Book 1", "Author 1", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "张三");

        // When - 删除图书（注意：实际系统中可能需要先归还）
        tracker.removeBook("ISBN-1");

        // Then - 借阅记录仍然存在（这是当前设计的特性）
        List<BorrowRecord> records = tracker.getBorrowRecordsByUser("张三");
        assertEquals(1, records.size());
        assertEquals("ISBN-1", records.get(0).getIsbn());
    }
}
