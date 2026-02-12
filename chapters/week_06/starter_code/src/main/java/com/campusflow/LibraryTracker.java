package com.campusflow;

import java.util.*;

/**
 * 图书借阅追踪器
 * 管理图书的添加、查询、借阅和归还操作
 */
public class LibraryTracker {

    private final Map<String, Book> booksByIsbn;

    public LibraryTracker() {
        this.booksByIsbn = new HashMap<>();
    }

    /**
     * 添加图书到追踪器
     *
     * @param book 要添加的图书
     * @throws IllegalArgumentException 如果 book 为 null 或 ISBN 为空
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("图书不能为空");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        // 如果 ISBN 已存在，更新图书信息（覆盖）
        booksByIsbn.put(book.getIsbn(), book);
    }

    /**
     * 根据 ISBN 查找图书
     *
     * @param isbn ISBN 编号
     * @return 找到的图书，如果不存在返回 null
     * @throws IllegalArgumentException 如果 isbn 为 null
     */
    public Book findByIsbn(String isbn) {
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN 不能为 null");
        }
        return booksByIsbn.get(isbn);
    }

    /**
     * 借阅图书
     *
     * @param isbn     图书 ISBN
     * @param borrower 借阅人姓名
     * @throws IllegalArgumentException 如果图书不存在、已被借出或借阅人为空
     */
    public void borrowBook(String isbn, String borrower) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        if (borrower == null || borrower.trim().isEmpty()) {
            throw new IllegalArgumentException("借阅人不能为空");
        }

        Book book = booksByIsbn.get(isbn);
        if (book == null) {
            throw new IllegalArgumentException("图书不存在: " + isbn);
        }
        if (book.isBorrowed()) {
            throw new IllegalArgumentException("图书已被借出: " + isbn);
        }

        book.markAsBorrowed(borrower);
    }

    /**
     * 归还图书
     *
     * @param isbn     图书 ISBN
     * @param borrower 借阅人姓名
     * @throws IllegalArgumentException 如果图书不存在、未被借出或借阅人不匹配
     */
    public void returnBook(String isbn, String borrower) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        if (borrower == null || borrower.trim().isEmpty()) {
            throw new IllegalArgumentException("借阅人不能为空");
        }

        Book book = booksByIsbn.get(isbn);
        if (book == null) {
            throw new IllegalArgumentException("图书不存在: " + isbn);
        }
        if (!book.isBorrowed()) {
            throw new IllegalArgumentException("图书未被借出: " + isbn);
        }
        if (!borrower.equals(book.getBorrower())) {
            throw new IllegalArgumentException("归还人与借阅人不符，借阅人是: " + book.getBorrower());
        }

        book.markAsReturned();
    }

    /**
     * 检查是否存在指定的借阅记录
     *
     * @param isbn     图书 ISBN
     * @param borrower 借阅人姓名
     * @return 如果存在该借阅记录返回 true
     */
    public boolean hasBorrowRecord(String isbn, String borrower) {
        if (isbn == null || borrower == null) {
            return false;
        }
        Book book = booksByIsbn.get(isbn);
        if (book == null) {
            return false;
        }
        return book.isBorrowed() && borrower.equals(book.getBorrower());
    }

    /**
     * 列出所有图书
     *
     * @return 所有图书的列表
     */
    public List<Book> listAllBooks() {
        return new ArrayList<>(booksByIsbn.values());
    }

    /**
     * 移除图书
     *
     * @param isbn 图书 ISBN
     * @return 如果成功移除返回 true，如果图书不存在返回 false
     */
    public boolean removeBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        return booksByIsbn.remove(isbn) != null;
    }

    /**
     * 获取图书总数
     *
     * @return 图书总数
     */
    public int getBookCount() {
        return booksByIsbn.size();
    }
}
