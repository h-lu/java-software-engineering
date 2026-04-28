package edu.campusflow.library;

import java.time.LocalDate;

public class BorrowRecord {
    private final String isbn;
    private final String borrower;
    private final LocalDate borrowDate;

    public BorrowRecord(String isbn, String borrower) {
        this.isbn = isbn;
        this.borrower = borrower;
        this.borrowDate = LocalDate.now();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBorrower() {
        return borrower;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }
}
