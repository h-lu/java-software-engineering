/*
 * 示例：内存存储的问题 - 程序关闭后数据丢失
 * 运行方式：mvn -q -f chapters/week_07/starter_code/pom.xml exec:java \
 *          -Dexec.mainClass="examples._01_memory_storage_problem"
 * 预期输出：
 *   第一次运行：保存了 3 本书
 *   第二次运行：图书列表为空（数据已丢失）
 */
package examples;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示内存存储的局限性：程序重启后数据全部丢失
 *
 * <p>这是 Week 05-06 使用的内存版存储方式，使用 ArrayList 和 HashMap
 * 存储数据。虽然操作简单，但数据只存在于内存中，程序关闭即消失。
 *
 * <p>对比：持久化存储（如 SQLite）将数据写入磁盘，程序重启后依然存在。
 */
public class _01_memory_storage_problem {

    // 内存中的图书列表 - 程序关闭后全部丢失！
    private static List<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== 内存存储演示 ===");
        System.out.println("注意：每次运行程序，内存都会重新初始化\n");

        // 检查当前是否有数据
        if (books.isEmpty()) {
            System.out.println("图书列表为空！");
            System.out.println("原因：这是新启动的程序，内存中没有数据\n");

            // 添加一些图书
            System.out.println("正在添加图书到内存...");
            books.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch"));
            books.add(new Book("978-0132350884", "Clean Code", "Robert C. Martin"));
            books.add(new Book("978-0321356680", "Java Concurrency in Practice", "Brian Goetz"));

            System.out.println("已保存 " + books.size() + " 本书到内存");
            System.out.println("\n⚠️  警告：这些数据只在当前程序运行期间存在！");
            System.out.println("   关闭程序后重新运行，你会看到图书列表再次为空。");
        } else {
            System.out.println("找到 " + books.size() + " 本书：");
            books.forEach(book -> System.out.println("  - " + book.title));
        }

        System.out.println("\n=== 生产环境风险 ===");
        System.out.println("如果电商订单、银行交易、用户信息都存在内存中：");
        System.out.println("  - 服务器重启 = 数据全部丢失");
        System.out.println("  - 这是 P0 级事故，可能导致公司倒闭");
        System.out.println("\n解决方案：使用数据库进行持久化存储（见下一个示例）");
    }

    // 简单的图书类
    static class Book {
        String isbn;
        String title;
        String author;

        Book(String isbn, String title, String author) {
            this.isbn = isbn;
            this.title = title;
            this.author = author;
        }
    }
}

/*
 * ❌ 反例：错误的持久化尝试
 *
 * 有人可能想："我用 static 变量，数据会不会保留？"
 * 答案：不会！static 变量也在内存中，程序关闭后同样丢失。
 *
 * 还有人想："我把数据序列化到文件？"
 * 这确实是一种持久化方案，但对于复杂查询和并发访问，
 * 文件操作既麻烦又容易出错。数据库是更好的选择。
 */
