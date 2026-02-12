package com.campusflow;

import com.campusflow.model.Book;
import com.campusflow.repository.JdbcBookRepository;
import com.campusflow.util.DatabaseInitializer;

import java.util.List;
import java.util.Optional;

/**
 * Week 07 入口类：JDBC 版图书借阅追踪器
 *
 * <p>本示例展示如何将 Week 05-06 的内存版 LibraryTracker 迁移到 JDBC + SQLite 实现。
 * 数据现在持久化到磁盘，程序重启后不会丢失。
 *
 * <p>运行方式：
 * <pre>
 * mvn -q compile exec:java -Dexec.mainClass="com.campusflow.App"
 * </pre>
 */
public class App {

    // SQLite 数据库文件路径
    private static final String DB_URL = "jdbc:sqlite:library.db";

    public static void main(String[] args) {
        System.out.println("=== CampusFlow Week 07: JDBC 持久化演示 ===\n");

        // 初始化数据库（创建表结构）
        DatabaseInitializer initializer = new DatabaseInitializer(DB_URL);
        initializer.initialize();

        // 创建 JDBC 版 Repository
        JdbcBookRepository repository = new JdbcBookRepository(DB_URL);

        // 演示 CRUD 操作
        demonstrateCrud(repository);

        System.out.println("\n=== 演示完成 ===");
        System.out.println("数据已保存到 library.db 文件");
        System.out.println("再次运行程序，数据依然存在！");
    }

    private static void demonstrateCrud(JdbcBookRepository repository) {
        // 添加图书
        System.out.println("1. 添加图书...");
        Book book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch");
        Book book2 = new Book("978-0132350884", "Clean Code", "Robert C. Martin");

        try {
            repository.save(book1);
            repository.save(book2);
            System.out.println("   已添加 2 本图书\n");
        } catch (IllegalArgumentException e) {
            System.out.println("   图书已存在，跳过添加\n");
        }

        // 查询所有图书
        System.out.println("2. 查询所有图书...");
        List<Book> books = repository.findAll();
        books.forEach(book ->
            System.out.println("   - " + book.getTitle() + " by " + book.getAuthor())
        );
        System.out.println();

        // 根据 ISBN 查询
        System.out.println("3. 根据 ISBN 查询...");
        Optional<Book> found = repository.findByIsbn("978-0134685991");
        found.ifPresent(book ->
            System.out.println("   找到: " + book.getTitle())
        );
        System.out.println();

        // 更新图书状态
        System.out.println("4. 更新图书状态（标记为不可借）...");
        Book updated = new Book(
            book1.getIsbn(),
            book1.getTitle(),
            book1.getAuthor(),
            false
        );
        repository.update(updated);
        System.out.println("   图书状态已更新\n");

        // 验证更新
        System.out.println("5. 验证更新...");
        repository.findByIsbn("978-0134685991").ifPresent(book ->
            System.out.println("   借阅状态: " + (book.isAvailable() ? "可借" : "已借出"))
        );
    }
}
