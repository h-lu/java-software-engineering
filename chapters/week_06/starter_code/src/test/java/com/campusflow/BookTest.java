package com.campusflow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Book 实体类测试
 * 覆盖构造器验证、借阅状态转换、equals/hashCode 等
 */
public class BookTest {

    // ==================== 构造器测试 ====================

    @Test
    void constructor_WithValidInput_ShouldCreateBook() {
        Book book = new Book("Java 核心技术", "Cay Horstmann", "978-111");

        assertAll("图书属性应正确设置",
            () -> assertEquals("Java 核心技术", book.getTitle()),
            () -> assertEquals("Cay Horstmann", book.getAuthor()),
            () -> assertEquals("978-111", book.getIsbn()),
            () -> assertFalse(book.isBorrowed()),
            () -> assertNull(book.getBorrower())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void constructor_WithInvalidTitle_ShouldThrowException(String invalidTitle) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Book(invalidTitle, "作者", "ISBN-1");
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void constructor_WithInvalidAuthor_ShouldThrowException(String invalidAuthor) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Book("书名", invalidAuthor, "ISBN-1");
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void constructor_WithInvalidIsbn_ShouldThrowException(String invalidIsbn) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Book("书名", "作者", invalidIsbn);
        });
    }

    // ==================== 借阅状态测试 ====================

    @Test
    void markAsBorrowed_WithValidBorrower_ShouldSetBorrowedState() {
        Book book = new Book("测试书", "测试作者", "ISBN-1");

        book.markAsBorrowed("小北");

        assertAll("借阅后状态",
            () -> assertTrue(book.isBorrowed()),
            () -> assertEquals("小北", book.getBorrower())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void markAsBorrowed_WithInvalidBorrower_ShouldThrowException(String invalidBorrower) {
        Book book = new Book("测试书", "测试作者", "ISBN-1");

        assertThrows(IllegalArgumentException.class, () -> {
            book.markAsBorrowed(invalidBorrower);
        });
    }

    @Test
    void markAsReturned_ShouldClearBorrowedState() {
        Book book = new Book("测试书", "测试作者", "ISBN-1");
        book.markAsBorrowed("小北");
        assertTrue(book.isBorrowed());

        book.markAsReturned();

        assertAll("归还后状态",
            () -> assertFalse(book.isBorrowed()),
            () -> assertNull(book.getBorrower())
        );
    }

    // ==================== equals/hashCode 测试 ====================

    @Test
    void equals_WithSameIsbn_ShouldReturnTrue() {
        Book book1 = new Book("书1", "作者A", "ISBN-001");
        Book book2 = new Book("书2", "作者B", "ISBN-001");

        assertEquals(book1, book2, "相同 ISBN 的图书应相等");
    }

    @Test
    void equals_WithDifferentIsbn_ShouldReturnFalse() {
        Book book1 = new Book("书1", "作者A", "ISBN-001");
        Book book2 = new Book("书1", "作者A", "ISBN-002");

        assertNotEquals(book1, book2, "不同 ISBN 的图书不应相等");
    }

    @Test
    void equals_WithSameObject_ShouldReturnTrue() {
        Book book = new Book("书1", "作者", "ISBN-001");

        assertEquals(book, book, "同一对象应相等");
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        Book book = new Book("书1", "作者", "ISBN-001");

        assertNotEquals(null, book, "与 null 比较应返回 false");
    }

    @Test
    void equals_WithDifferentClass_ShouldReturnFalse() {
        Book book = new Book("书1", "作者", "ISBN-001");

        assertNotEquals(book, "ISBN-001", "与不同类对象比较应返回 false");
    }

    @Test
    void hashCode_WithSameIsbn_ShouldBeEqual() {
        Book book1 = new Book("书1", "作者A", "ISBN-001");
        Book book2 = new Book("书2", "作者B", "ISBN-001");

        assertEquals(book1.hashCode(), book2.hashCode(), "相同 ISBN 的图书 hashCode 应相等");
    }

    // ==================== toString 测试 ====================

    @Test
    void toString_ShouldContainBookInfo() {
        Book book = new Book("Java编程", "张三", "ISBN-123");

        String str = book.toString();

        assertAll("toString 应包含图书信息",
            () -> assertTrue(str.contains("Java编程")),
            () -> assertTrue(str.contains("张三")),
            () -> assertTrue(str.contains("ISBN-123"))
        );
    }

    @Test
    void toString_WithBorrowedBook_ShouldContainBorrowerInfo() {
        Book book = new Book("Java编程", "张三", "ISBN-123");
        book.markAsBorrowed("小北");

        String str = book.toString();

        assertTrue(str.contains("小北"), "toString 应包含借阅人信息");
    }

    // ==================== 完整生命周期测试 ====================

    @ParameterizedTest(name = "借阅人={0}")
    @CsvSource({
        "小北",
        "阿码",
        "老潘"
    })
    void bookFullLifecycle_ShouldWorkCorrectly(String borrower) {
        // 创建
        Book book = new Book("测试书", "测试作者", "ISBN-LIFE");
        assertFalse(book.isBorrowed());

        // 借阅
        book.markAsBorrowed(borrower);
        assertTrue(book.isBorrowed());
        assertEquals(borrower, book.getBorrower());

        // 归还
        book.markAsReturned();
        assertFalse(book.isBorrowed());
        assertNull(book.getBorrower());
    }
}
