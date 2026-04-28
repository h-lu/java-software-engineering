import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StarterSmokeTest {

    @Test
    void bookShouldExposeConstructorValues() {
        Book book = new Book("Java 核心技术", "Cay Horstmann", "978-111");

        assertEquals("Java 核心技术", book.getTitle());
        assertEquals("Cay Horstmann", book.getAuthor());
        assertEquals("978-111", book.getIsbn());
    }

    @Test
    void trackerShouldStartEmpty() {
        LibraryTracker tracker = new LibraryTracker();

        assertEquals(0, tracker.getBookCount());
        assertEquals(0, tracker.getBorrowRecordCount());
    }
}
