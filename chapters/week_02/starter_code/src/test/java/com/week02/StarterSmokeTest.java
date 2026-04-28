package com.week02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StarterSmokeTest {

    @Test
    void bookSkeletonCompilesAndKeepsFields() {
        Book book = new Book("978-7-111-12345-6", "Effective Java", "Joshua Bloch", 2018, 89.0);

        assertEquals("978-7-111-12345-6", book.getIsbn());
        assertTrue(book.toDisplayText().contains("Effective Java"));
    }

    @Test
    void collectionSkeletonIsRunnable() {
        BookCollection collection = new BookCollection();
        collection.addBook(new Book("isbn", "title", "author", 2020, 10.0));

        assertEquals(1, collection.getBooks().size());
        assertTrue(collection.totalPrice() >= 0.0);
        assertTrue(new BookPrinter().printAsJson(collection).contains("books"));
    }
}
