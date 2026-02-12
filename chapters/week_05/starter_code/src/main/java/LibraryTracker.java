import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 图书借阅追踪器
 * 综合运用 ArrayList、HashMap 和泛型
 */
public class LibraryTracker {
    private HashMap<String, Book> booksByIsbn;        // 图书库存
    private ArrayList<BorrowRecord> borrowRecords;    // 借阅记录

    public LibraryTracker() {
        booksByIsbn = new HashMap<>();
        borrowRecords = new ArrayList<>();
    }

    // ===== 图书管理 =====

    public void addBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("图书信息不完整");
        }
        booksByIsbn.put(book.getIsbn(), book);
    }

    public Book findBook(String isbn) {
        return booksByIsbn.get(isbn);
    }

    public List<Book> listAllBooks() {
        return new ArrayList<>(booksByIsbn.values());
    }

    public boolean hasBook(String isbn) {
        return booksByIsbn.containsKey(isbn);
    }

    public void removeBook(String isbn) {
        booksByIsbn.remove(isbn);
    }

    // ===== 借阅管理 =====

    public void borrowBook(String isbn, String borrower) {
        // 防御式编程：检查参数
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

    public List<BorrowRecord> getBorrowRecordsByUser(String borrower) {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getBorrower().equals(borrower)) {
                result.add(record);
            }
        }
        return result;
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }

    public void returnBook(String isbn, String borrower) {
        Iterator<BorrowRecord> it = borrowRecords.iterator();
        while (it.hasNext()) {
            BorrowRecord record = it.next();
            if (record.getIsbn().equals(isbn) && record.getBorrower().equals(borrower)) {
                it.remove();  // 删除借阅记录
                return;
            }
        }
        throw new IllegalStateException("未找到对应的借阅记录");
    }

    public int getBookCount() {
        return booksByIsbn.size();
    }

    public int getBorrowRecordCount() {
        return borrowRecords.size();
    }
}
