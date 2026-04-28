import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LibraryTracker {
    private final HashMap<String, Book> booksByIsbn;
    private final ArrayList<BorrowRecord> borrowRecords;

    public LibraryTracker() {
        booksByIsbn = new HashMap<>();
        borrowRecords = new ArrayList<>();
    }

    public void addBook(Book book) {
        // TODO: 检查 null、空 ISBN、重复 ISBN，然后放入 booksByIsbn。
        throw new UnsupportedOperationException("TODO: implement addBook");
    }

    public Book findBook(String isbn) {
        // TODO: 使用 HashMap 按 ISBN 做 O(1) 查询。
        throw new UnsupportedOperationException("TODO: implement findBook");
    }

    public List<Book> listAllBooks() {
        // TODO: 返回副本，避免外部代码修改内部 Map。
        throw new UnsupportedOperationException("TODO: implement listAllBooks");
    }

    public boolean hasBook(String isbn) {
        return booksByIsbn.containsKey(isbn);
    }

    public void removeBook(String isbn) {
        // TODO: 删除图书，并清理该 ISBN 对应的借阅记录。
        throw new UnsupportedOperationException("TODO: implement removeBook");
    }

    public void borrowBook(String isbn, String borrower) {
        // TODO: 校验参数、确认图书存在，然后新增 BorrowRecord。
        throw new UnsupportedOperationException("TODO: implement borrowBook");
    }

    public List<BorrowRecord> getBorrowRecordsByUser(String borrower) {
        // TODO: 遍历 borrowRecords，返回指定借阅人的记录副本。
        throw new UnsupportedOperationException("TODO: implement getBorrowRecordsByUser");
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }

    public void returnBook(String isbn, String borrower) {
        // TODO: 使用 Iterator 安全删除匹配的借阅记录。
        throw new UnsupportedOperationException("TODO: implement returnBook");
    }

    public int getBookCount() {
        return booksByIsbn.size();
    }

    public int getBorrowRecordCount() {
        return borrowRecords.size();
    }
}
