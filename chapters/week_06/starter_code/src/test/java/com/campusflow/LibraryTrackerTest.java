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

    @Disabled("TODO: remove @Disabled after you complete this test")
    @Test
    void shouldBorrowAndReturnBook() {
        // TODO: arrange a book, borrow it, assert hasBorrowRecord, return it, assert record is gone.
    }

    @Disabled("TODO: remove @Disabled after you complete this test")
    @Test
    void shouldThrowWhenBorrowingMissingBook() {
        // TODO: use assertThrows for a missing ISBN.
    }

    @Disabled("TODO: remove @Disabled after you complete this test")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void shouldRejectInvalidBorrower(String borrower) {
        // TODO: add javaBook, then assert borrowBook rejects each invalid borrower value.
    }

    @Disabled("TODO: remove @Disabled after you complete this test")
    @Test
    void shouldProtectListAllBooksFromExternalModification() {
        // TODO: call listAllBooks(), mutate the returned list, then verify tracker state is unchanged.
    }
}
