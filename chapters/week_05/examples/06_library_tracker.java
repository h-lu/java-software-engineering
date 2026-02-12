/**
 * 示例：完整的图书借阅追踪器（综合示例）。
 *
 * 运行方式：javac chapters/week_05/examples/06_library_tracker.java && \
 *          java -cp chapters/week_05/examples LibraryTrackerDemo
 *
 * 预期输出：
 * - 展示完整的图书管理功能（添加、查询、删除）
 * - 展示借阅记录管理
 * - 展示防御式编程和异常处理
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

// ========== 实体类 ==========

/**
 * 图书实体类。
 */
class Book6 {
    private final String isbn;
    private String title;
    private String author;
    private boolean available;

    public Book6(String isbn, String title, String author) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("书名不能为空");
        }
        this.isbn = isbn;
        this.title = title;
        this.author = author != null ? author : "佚名";
        this.available = true;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return title + " (" + author + ") [" + isbn + "] " +
               (available ? "可借阅" : "已借出");
    }
}

/**
 * 借阅记录实体类。
 */
class BorrowRecord {
    private final String isbn;
    private final String borrower;
    private final LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowRecord(String isbn, String borrower) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        if (borrower == null || borrower.isEmpty()) {
            throw new IllegalArgumentException("借阅人不能为空");
        }
        this.isbn = isbn;
        this.borrower = borrower;
        this.borrowDate = LocalDate.now();
        this.returnDate = null;
    }

    public String getIsbn() { return isbn; }
    public String getBorrower() { return borrower; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public boolean isReturned() { return returnDate != null; }

    public void markReturned() {
        this.returnDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return borrower + " 借阅 " + isbn + " 于 " + borrowDate +
               (isReturned() ? "，已归还" : "，未归还");
    }
}

// ========== Repository 层 ==========

/**
 * 图书仓储类——封装图书数据访问逻辑。
 *
 * <p>使用 HashMap 实现 ISBN 快速查找，使用 ArrayList 支持遍历。
 * 这是生产代码中 Repository 层的雏形。
 */
class BookRepository {
    // 主存储：ISBN -> Book，支持 O(1) 查找
    private final HashMap<String, Book6> booksByIsbn;
    // 辅助存储：支持按顺序遍历
    private final ArrayList<Book6> bookList;

    public BookRepository() {
        this.booksByIsbn = new HashMap<>();
        this.bookList = new ArrayList<>();
    }

    /**
     * 保存图书（新增或更新）。
     */
    public void save(Book6 book) {
        if (book == null) {
            throw new IllegalArgumentException("图书不能为空");
        }
        // 更新 HashMap
        booksByIsbn.put(book.getIsbn(), book);

        // 更新列表：如果已存在则替换，否则添加
        bookList.removeIf(b -> b.getIsbn().equals(book.getIsbn()));
        bookList.add(book);
    }

    /**
     * 按 ISBN 查找图书。
     */
    public Optional<Book6> findByIsbn(String isbn) {
        return Optional.ofNullable(booksByIsbn.get(isbn));
    }

    /**
     * 获取所有图书（返回副本保护内部数据）。
     */
    public List<Book6> findAll() {
        return new ArrayList<>(bookList);
    }

    /**
     * 按作者查找图书。
     */
    public List<Book6> findByAuthor(String author) {
        List<Book6> result = new ArrayList<>();
        for (Book6 book : bookList) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        return result;
    }

    /**
     * 删除图书。
     */
    public boolean delete(String isbn) {
        Book6 removed = booksByIsbn.remove(isbn);
        if (removed != null) {
            bookList.removeIf(b -> b.getIsbn().equals(isbn));
            return true;
        }
        return false;
    }

    /**
     * 获取图书总数。
     */
    public int count() {
        return booksByIsbn.size();
    }

    /**
     * 检查是否存在。
     */
    public boolean exists(String isbn) {
        return booksByIsbn.containsKey(isbn);
    }
}

/**
 * 借阅记录仓储类。
 */
class BorrowRecordRepository {
    private final ArrayList<BorrowRecord> records;

    public BorrowRecordRepository() {
        this.records = new ArrayList<>();
    }

    /**
     * 添加借阅记录。
     */
    public void add(BorrowRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("记录不能为空");
        }
        records.add(record);
    }

    /**
     * 查找用户的借阅记录。
     */
    public List<BorrowRecord> findByBorrower(String borrower) {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord record : records) {
            if (record.getBorrower().equals(borrower) && !record.isReturned()) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 查找图书的当前借阅记录。
     */
    public Optional<BorrowRecord> findActiveByIsbn(String isbn) {
        for (BorrowRecord record : records) {
            if (record.getIsbn().equals(isbn) && !record.isReturned()) {
                return Optional.of(record);
            }
        }
        return Optional.empty();
    }

    /**
     * 归还图书。
     */
    public boolean returnBook(String isbn, String borrower) {
        Iterator<BorrowRecord> it = records.iterator();
        while (it.hasNext()) {
            BorrowRecord record = it.next();
            if (record.getIsbn().equals(isbn) &&
                record.getBorrower().equals(borrower) &&
                !record.isReturned()) {
                record.markReturned();
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有记录。
     */
    public List<BorrowRecord> findAll() {
        return new ArrayList<>(records);
    }
}

// ========== 服务层 ==========

/**
 * 图书馆服务类——业务逻辑层。
 */
class LibraryService {
    private final BookRepository bookRepo;
    private final BorrowRecordRepository recordRepo;

    public LibraryService() {
        this.bookRepo = new BookRepository();
        this.recordRepo = new BorrowRecordRepository();
    }

    // ===== 图书管理 =====

    public void addBook(String isbn, String title, String author) {
        Book6 book = new Book6(isbn, title, author);
        bookRepo.save(book);
        System.out.println("✅ 已添加图书: " + book.getTitle());
    }

    public void listBooks() {
        List<Book6> books = bookRepo.findAll();
        System.out.println("\n图书馆藏书（" + books.size() + " 本）：");
        if (books.isEmpty()) {
            System.out.println("  (暂无图书)");
            return;
        }
        for (int i = 0; i < books.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + books.get(i));
        }
    }

    public void searchByIsbn(String isbn) {
        Optional<Book6> book = bookRepo.findByIsbn(isbn);
        if (book.isPresent()) {
            System.out.println("找到图书: " + book.get());
        } else {
            System.out.println("未找到 ISBN 为 " + isbn + " 的图书");
        }
    }

    public void searchByAuthor(String author) {
        List<Book6> books = bookRepo.findByAuthor(author);
        System.out.println("作者 '" + author + "' 的图书（" + books.size() + " 本）：");
        for (Book6 book : books) {
            System.out.println("  - " + book);
        }
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

        // 检查图书是否存在
        Book6 book = bookRepo.findByIsbn(isbn)
            .orElseThrow(() -> new IllegalArgumentException("图书不存在: " + isbn));

        // 检查是否可借阅
        if (!book.isAvailable()) {
            throw new IllegalStateException("图书已被借出: " + isbn);
        }

        // 创建借阅记录
        BorrowRecord record = new BorrowRecord(isbn, borrower);
        recordRepo.add(record);

        // 更新图书状态
        book.setAvailable(false);
        bookRepo.save(book);

        System.out.println("✅ " + borrower + " 成功借阅《" + book.getTitle() + "》");
    }

    public void returnBook(String isbn, String borrower) {
        // 检查图书是否存在
        Book6 book = bookRepo.findByIsbn(isbn)
            .orElseThrow(() -> new IllegalArgumentException("图书不存在: " + isbn));

        // 归还
        boolean success = recordRepo.returnBook(isbn, borrower);
        if (!success) {
            throw new IllegalStateException("未找到 " + borrower + " 借阅 " + isbn + " 的记录");
        }

        // 更新图书状态
        book.setAvailable(true);
        bookRepo.save(book);

        System.out.println("✅ " + borrower + " 成功归还《" + book.getTitle() + "》");
    }

    public void listBorrowRecords() {
        List<BorrowRecord> records = recordRepo.findAll();
        System.out.println("\n借阅记录（" + records.size() + " 条）：");
        if (records.isEmpty()) {
            System.out.println("  (暂无记录)");
            return;
        }
        for (BorrowRecord record : records) {
            System.out.println("  - " + record);
        }
    }

    public void listUserBorrows(String borrower) {
        List<BorrowRecord> records = recordRepo.findByBorrower(borrower);
        System.out.println("\n" + borrower + " 的当前借阅（" + records.size() + " 本）：");
        for (BorrowRecord record : records) {
            Book6 book = bookRepo.findByIsbn(record.getIsbn()).orElse(null);
            System.out.println("  - " + (book != null ? book.getTitle() : record.getIsbn()) +
                             " 借阅于 " + record.getBorrowDate());
        }
    }
}

// ========== 主程序 ==========

class LibraryTrackerDemo {
    public static void main(String[] args) {
        System.out.println("=== 图书借阅追踪器演示 ===\n");

        LibraryService library = new LibraryService();

        // 1. 添加图书
        System.out.println("【1. 添加图书】");
        library.addBook("978-111-111", "Java 核心技术", "Cay Horstmann");
        library.addBook("978-222-222", "Effective Java", "Joshua Bloch");
        library.addBook("978-333-333", "Clean Code", "Robert Martin");
        library.addBook("978-444-444", "重构", "Martin Fowler");
        library.addBook("978-555-555", "设计模式", "GoF");

        // 2. 列出所有图书
        library.listBooks();

        // 3. 查询图书
        System.out.println("\n【2. 查询图书】");
        library.searchByIsbn("978-222-222");
        library.searchByIsbn("999-999-999");
        library.searchByAuthor("Martin Fowler");

        // 4. 借阅图书
        System.out.println("\n【3. 借阅图书】");
        library.borrowBook("978-222-222", "小北");
        library.borrowBook("978-333-333", "小北");
        library.borrowBook("978-444-444", "阿码");

        // 借阅后查看状态
        System.out.println("\n借阅后的图书状态：");
        library.listBooks();

        // 5. 查看借阅记录
        System.out.println("\n【4. 查看借阅记录】");
        library.listBorrowRecords();
        library.listUserBorrows("小北");

        // 6. 归还图书
        System.out.println("\n【5. 归还图书】");
        library.returnBook("978-222-222", "小北");

        // 归还后查看
        System.out.println("\n归还后的图书状态：");
        library.listBooks();
        library.listUserBorrows("小北");

        // 7. 异常处理演示
        System.out.println("\n【6. 异常处理演示】");

        // 尝试借阅不存在的书
        try {
            library.borrowBook("999-999-999", "小北");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 预期异常: " + e.getMessage());
        }

        // 尝试重复借阅已借出的书
        try {
            library.borrowBook("978-333-333", "阿码");  // 已被小北借走
        } catch (IllegalStateException e) {
            System.out.println("❌ 预期异常: " + e.getMessage());
        }

        // 尝试归还错误的借阅
        try {
            library.returnBook("978-333-333", "阿码");  // 实际是小北借的
        } catch (IllegalStateException e) {
            System.out.println("❌ 预期异常: " + e.getMessage());
        }

        // 8. 最终状态
        System.out.println("\n【7. 最终状态】");
        library.listBooks();
        library.listBorrowRecords();

        System.out.println("\n=== 设计亮点 ===");
        System.out.println("✅ Repository 模式：分离数据访问逻辑");
        System.out.println("✅ Optional 使用：避免 null 检查");
        System.out.println("✅ 防御式编程：参数校验和业务规则检查");
        System.out.println("✅ 异常处理：清晰的错误信息");
        System.out.println("✅ 数据一致性：图书状态与借阅记录同步");
    }
}
