import java.time.LocalDate;

/**
 * 借阅记录类
 * 用于图书馆借阅追踪器
 */
public class BorrowRecord {
    private String isbn;
    private String borrower;
    private LocalDate borrowDate;

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

    @Override
    public String toString() {
        return "BorrowRecord{isbn='" + isbn + "', borrower='" + borrower + "', date=" + borrowDate + "}";
    }
}
