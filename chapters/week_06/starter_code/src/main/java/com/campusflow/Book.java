package com.campusflow;

import java.util.Objects;

/**
 * 图书实体类
 * 表示图书馆中的一本书，包含基本信息和借阅状态
 */
public class Book {
    private final String title;
    private final String author;
    private final String isbn;
    private boolean borrowed;
    private String borrower;

    /**
     * 创建一本新书
     *
     * @param title  书名
     * @param author 作者
     * @param isbn   ISBN编号
     * @throws IllegalArgumentException 如果 title、author 或 isbn 为空
     */
    public Book(String title, String author, String isbn) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("书名不能为空");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("作者不能为空");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.borrowed = false;
        this.borrower = null;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public String getBorrower() {
        return borrower;
    }

    /**
     * 标记图书为已借出
     *
     * @param borrowerName 借阅人姓名
     * @throws IllegalArgumentException 如果 borrowerName 为空
     */
    public void markAsBorrowed(String borrowerName) {
        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            throw new IllegalArgumentException("借阅人不能为空");
        }
        this.borrowed = true;
        this.borrower = borrowerName;
    }

    /**
     * 标记图书为已归还
     */
    public void markAsReturned() {
        this.borrowed = false;
        this.borrower = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", borrowed=" + borrowed +
                ", borrower='" + borrower + '\'' +
                '}';
    }
}
