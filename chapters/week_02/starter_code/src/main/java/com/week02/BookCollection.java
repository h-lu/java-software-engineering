package com.week02;

import java.util.ArrayList;
import java.util.List;

/**
 * Week 02 服务类骨架：管理图书集合。
 */
public class BookCollection {
    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        // TODO: 处理 null、重复 ISBN 等边界情况。
        books.add(book);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public double totalPrice() {
        // TODO: 计算所有图书价格总和。
        return 0.0;
    }

    public List<Book> filterByAuthor(String author) {
        // TODO: 返回指定作者的图书列表。
        return new ArrayList<>();
    }

    public List<Book> sortByYear() {
        // TODO: 返回按出版年份排序后的新列表，不要直接暴露内部列表。
        return new ArrayList<>(books);
    }
}
