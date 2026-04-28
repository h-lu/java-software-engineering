package com.campusflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LibraryTrackerTest {
    private LibraryTracker tracker;
    private Book javaBook;

    @BeforeEach
    void setUp() {
        tracker = new LibraryTracker();
        javaBook = new Book("Java 核心技术", "Cay Horstmann", "978-111");
    }

    @Test
    void smokeTestProjectAndFixtureAreReady() {
        tracker.addBook(javaBook);

        Book found = tracker.findByIsbn("978-111");

        assertNotNull(found);
        assertEquals("Java 核心技术", found.getTitle());
    }

    @Disabled("待办：完成这个测试后移除 @Disabled")
    @Test
    void shouldBorrowAndReturnBook() {
        // 待办：准备一本书，借出后断言 hasBorrowRecord，再归还并断言记录已消失。
    }

    @Disabled("待办：完成这个测试后移除 @Disabled")
    @Test
    void shouldThrowWhenBorrowingMissingBook() {
        // 待办：对不存在的 ISBN 使用 assertThrows。
    }

    @Disabled("待办：完成这个测试后移除 @Disabled")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void shouldRejectInvalidBorrower(String borrower) {
        // 待办：先添加 javaBook，再断言 borrowBook 会拒绝每个非法 borrower。
    }

    @Disabled("待办：完成这个测试后移除 @Disabled")
    @Test
    void shouldProtectListAllBooksFromExternalModification() {
        // 待办：调用 listAllBooks()，修改返回列表，再验证 tracker 内部状态不受影响。
    }
}
