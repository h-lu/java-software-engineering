package com.campusflow.model;

import java.util.Objects;

/**
 * 图书实体类
 * 表示图书馆中的一本书，包含基本信息和借阅状态
 */
public class Book {
    private final String isbn;
    private String title;
    private String author;
    private boolean available;

    /**
     * 创建一本新书
     *
     * @param isbn     ISBN编号（主键）
     * @param title    书名
     * @param author   作者
     * @param available 是否可借
     * @throws IllegalArgumentException 如果 isbn、title、author 为空
     */
    public Book(String isbn, String title, String author, boolean available) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("书名不能为空");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("作者不能为空");
        }
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    /**
     * 创建一本默认可借的新书
     *
     * @param isbn   ISBN编号
     * @param title  书名
     * @param author 作者
     */
    public Book(String isbn, String title, String author) {
        this(isbn, title, author, true);
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("书名不能为空");
        }
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("作者不能为空");
        }
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", available=" + available +
                '}';
    }
}
