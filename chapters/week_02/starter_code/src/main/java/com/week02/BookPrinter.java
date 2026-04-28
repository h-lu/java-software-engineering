package com.week02;

/**
 * Week 02 骨架：负责格式化输出，不负责修改图书集合。
 */
public class BookPrinter {

    public String print(Book book) {
        // TODO: 返回用户友好的单本图书文本。
        return book == null ? "TODO: no book" : book.toDisplayText();
    }

    public String print(BookCollection collection) {
        // TODO: 返回 ASSIGNMENT.md 示例中的图书列表文本。
        return "TODO: format collection with " + collection.getBooks().size() + " books";
    }
}
