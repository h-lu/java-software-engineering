package com.week02;

/**
 * Week 02 实体类骨架：图书。
 */
public class Book {
    private final String isbn;
    private String title;
    private String author;
    private int year;
    private double price;

    public Book(String isbn, String title, String author, int year, double price) {
        // 待办： 为所有参数添加必要验证，尤其是 isbn、year、price。
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // 待办： 标题不能为空。
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        // 待办： 作者不能为空。
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        // 待办： 年份不能早于合理范围，不能超过当前年份。
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        // 待办： 价格不能为负数。
        this.price = price;
    }

    public String toDisplayText() {
        // 待办： 按作业示例返回图书详情文本。
        return "待办： " + title + " (" + isbn + ")";
    }
}
