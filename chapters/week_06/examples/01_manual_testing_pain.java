/*
 * 示例：手动验证的困境——展示为什么需要自动化测试。
 * 运行方式：javac 01_manual_testing_pain.java && java ManualTestingPainDemo
 * 预期输出：程序运行正常，但借书功能实际上有 bug（未正确验证图书是否存在）
 */

/**
 * 图书类 - 来自 Week 05
 */
class Book {
    private String title;
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
}

/**
 * 有 bug 的图书追踪器 - 模拟小北改代码后破坏功能的情况
 */
class LibraryTrackerWithBug {
    private java.util.HashMap<String, Book> booksByIsbn;
    private java.util.ArrayList<BorrowRecord> borrowRecords;

    public LibraryTrackerWithBug() {
        booksByIsbn = new java.util.HashMap<>();
        borrowRecords = new java.util.ArrayList<>();
    }

    public void addBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("图书信息不完整");
        }
        booksByIsbn.put(book.getIsbn(), book);
    }

    // 小北重构了这个方法，返回 Optional，但忘记同步修改 borrowBook
    public java.util.Optional<Book> findBook(String isbn) {
        return java.util.Optional.ofNullable(booksByIsbn.get(isbn));
    }

    // BUG: 这个方法没有更新，仍然按旧逻辑处理
    public void borrowBook(String isbn, String borrower) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }

        // BUG: 小北忘了这里需要调整！findBook 现在返回 Optional
        // 但代码仍然直接判断，导致逻辑错误
        Book book = findBook(isbn).orElse(null);

        // 这个检查本应该阻止借阅不存在的书，但由于上面的问题...
        if (book == null) {
            // 实际上这里应该抛异常，但小北在重构时注释掉了
            // throw new IllegalArgumentException("图书不存在：" + isbn);
            System.out.println("警告：图书不存在，但继续执行...");
        }

        // BUG: 即使书不存在，也会创建借阅记录！
        borrowRecords.add(new BorrowRecord(isbn, borrower));
        System.out.println("借阅成功：" + borrower + " 借走了 ISBN:" + isbn);
    }

    public boolean hasBorrowRecord(String isbn, String borrower) {
        for (BorrowRecord record : borrowRecords) {
            if (record.getIsbn().equals(isbn) && record.getBorrower().equals(borrower)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * 借阅记录类
 */
class BorrowRecord {
    private String isbn;
    private String borrower;

    public BorrowRecord(String isbn, String borrower) {
        this.isbn = isbn;
        this.borrower = borrower;
    }

    public String getIsbn() { return isbn; }
    public String getBorrower() { return borrower; }
}

/**
 * 手动验证的主程序 - 只能测试小北"想到"的场景
 */
class ManualTestingPainDemo {
    public static void main(String[] args) {
        LibraryTrackerWithBug tracker = new LibraryTrackerWithBug();

        // 小北手动测试：添加一本书，然后借阅它
        System.out.println("=== 手动测试场景1：正常借阅 ===");
        tracker.addBook(new Book("Java 核心技术", "Cay Horstmann", "978-111"));
        tracker.borrowBook("978-111", "小北");
        System.out.println("借阅记录存在: " + tracker.hasBorrowRecord("978-111", "小北"));

        // 小北手动测试：查询功能
        System.out.println("\n=== 手动测试场景2：查询图书 ===");
        var book = tracker.findBook("978-111");
        System.out.println("找到图书: " + book.isPresent());

        // 小北忘了测试这个场景：借阅不存在的书！
        System.out.println("\n=== 遗漏的测试场景：借阅不存在的书 ===");
        System.out.println("（小北忘了测试这个边界情况）");
        tracker.borrowBook("不存在的ISBN", "阿码");
        System.out.println("借阅记录存在: " + tracker.hasBorrowRecord("不存在的ISBN", "阿码"));
        System.out.println("BUG: 不存在的书也被借走了！");

        System.out.println("\n=== 问题总结 ===");
        System.out.println("手动验证的问题：");
        System.out.println("1. 只能覆盖'想到'的场景，容易遗漏边界情况");
        System.out.println("2. 每次改代码后都要重新手动验证所有功能");
        System.out.println("3. 无法自动化执行，容易因疏忽而遗漏");
        System.out.println("4. 重构时如果没有测试守护，很容易引入 bug");
    }
}
