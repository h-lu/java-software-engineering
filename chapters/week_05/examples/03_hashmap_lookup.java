/**
 * 示例：展示 HashMap 的使用——put、get、containsKey、遍历。
 *
 * 运行方式：javac chapters/week_05/examples/03_hashmap_lookup.java && \
 *          java -cp chapters/week_05/examples HashMapLookupDemo
 *
 * 预期输出：
 * - 展示 O(1) 时间复杂度的快速查找
 * - 对比 HashMap 与线性搜索的性能差异
 * - 展示正确的遍历方式
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

// Book 类定义
class Book3 {
    private String title;
    private String author;
    private String isbn;

    public Book3(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}

// 使用 HashMap 实现快速查找的图书馆
class LibraryWithHashMap {
    // key: ISBN, value: Book
    private HashMap<String, Book3> booksByIsbn;

    public LibraryWithHashMap() {
        booksByIsbn = new HashMap<>();
    }

    // 添加图书——O(1)
    public void addBook(Book3 book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("图书或 ISBN 不能为空");
        }
        booksByIsbn.put(book.getIsbn(), book);
    }

    // 按 ISBN 查找——O(1) 时间复杂度
    public Book3 findByIsbn(String isbn) {
        return booksByIsbn.get(isbn);  // 直接定位，无需遍历
    }

    // 检查是否存在——O(1)
    public boolean hasBook(String isbn) {
        return booksByIsbn.containsKey(isbn);
    }

    // 删除图书
    public Book3 removeBook(String isbn) {
        return booksByIsbn.remove(isbn);
    }

    // 获取图书数量
    public int getBookCount() {
        return booksByIsbn.size();
    }

    // 列出所有图书（遍历方式 1：遍历 keySet）
    public void listBooksByKeySet() {
        System.out.println("图书列表（遍历 keySet）：");
        Set<String> isbns = booksByIsbn.keySet();
        for (String isbn : isbns) {
            Book3 book = booksByIsbn.get(isbn);
            System.out.println("  ISBN: " + isbn + " -> " + book);
        }
    }

    // 列出所有图书（遍历方式 2：遍历 entrySet——推荐）
    public void listBooksByEntrySet() {
        System.out.println("图书列表（遍历 entrySet——推荐）：");
        // entrySet 直接获取键值对，效率更高
        for (Map.Entry<String, Book3> entry : booksByIsbn.entrySet()) {
            System.out.println("  ISBN: " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    // 按作者查找（需要遍历，O(n)）
    public void findByAuthor(String author) {
        System.out.println("查找作者 '" + author + "' 的图书：");
        boolean found = false;
        for (Book3 book : booksByIsbn.values()) {  // 遍历所有值
            if (book.getAuthor().equals(author)) {
                System.out.println("  找到: " + book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("  未找到该作者的图书");
        }
    }
}

// 反例：线性搜索的低效实现
class LibraryWithLinearSearch {
    private java.util.ArrayList<Book3> books = new java.util.ArrayList<>();

    public void addBook(Book3 book) {
        books.add(book);
    }

    // 线性搜索——O(n) 时间复杂度
    public Book3 findByIsbn(String isbn) {
        for (Book3 book : books) {  // 逐个比较
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    // 模拟大量数据的查找性能
    public void demonstratePerformance(int dataSize) {
        // 添加大量数据
        for (int i = 0; i < dataSize; i++) {
            books.add(new Book3("Book " + i, "Author " + i, "ISBN" + i));
        }

        // 测试查找性能
        long start = System.nanoTime();
        Book3 found = findByIsbn("ISBN" + (dataSize - 1));  // 查找最后一本
        long end = System.nanoTime();

        System.out.println("  线性搜索 " + dataSize + " 条数据耗时: " + (end - start) / 1000 + " 微秒");
    }
}

// 主程序
class HashMapLookupDemo {
    public static void main(String[] args) {
        System.out.println("=== HashMap 快速查找演示 ===\n");

        LibraryWithHashMap library = new LibraryWithHashMap();

        // 1. 添加图书
        System.out.println("1. 添加图书：");
        library.addBook(new Book3("Java 核心技术", "Cay Horstmann", "978-111-111"));
        library.addBook(new Book3("Effective Java", "Joshua Bloch", "978-222-222"));
        library.addBook(new Book3("Clean Code", "Robert Martin", "978-333-333"));
        library.addBook(new Book3("重构", "Martin Fowler", "978-444-444"));
        System.out.println("   已添加 " + library.getBookCount() + " 本书");

        // 2. 快速查找（O(1)）
        System.out.println("\n2. 按 ISBN 快速查找（O(1)）：");
        String searchIsbn = "978-222-222";
        Book3 found = library.findByIsbn(searchIsbn);
        System.out.println("   查找 '" + searchIsbn + "': " + (found != null ? found : "未找到"));

        // 查找不存在的
        Book3 notFound = library.findByIsbn("999-999-999");
        System.out.println("   查找 '999-999-999': " + (notFound != null ? notFound : "未找到"));

        // 3. 检查是否存在
        System.out.println("\n3. 检查图书是否存在：");
        System.out.println("   包含 '978-111-111': " + library.hasBook("978-111-111"));
        System.out.println("   包含 '999-999-999': " + library.hasBook("999-999-999"));

        // 4. 遍历方式对比
        System.out.println("\n4. 遍历方式对比：");
        library.listBooksByKeySet();
        System.out.println();
        library.listBooksByEntrySet();

        // 5. 按作者查找（O(n)，因为没有作者索引）
        System.out.println("\n5. 按作者查找（需要遍历，O(n)）：");
        library.findByAuthor("Joshua Bloch");

        // 6. 删除操作
        System.out.println("\n6. 删除操作：");
        System.out.println("   删除前数量: " + library.getBookCount());
        Book3 removed = library.removeBook("978-333-333");
        System.out.println("   已删除: " + removed);
        System.out.println("   删除后数量: " + library.getBookCount());

        // 7. 性能对比演示
        System.out.println("\n7. 性能对比（线性搜索 vs HashMap）：");

        // 线性搜索性能
        LibraryWithLinearSearch linearLibrary = new LibraryWithLinearSearch();
        linearLibrary.demonstratePerformance(1000);
        linearLibrary = new LibraryWithLinearSearch();
        linearLibrary.demonstratePerformance(10000);

        // HashMap 性能
        System.out.println("\n  HashMap 查找（即使 10000 条数据也是 O(1)）：");
        HashMap<String, Book3> hashMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            hashMap.put("ISBN" + i, new Book3("Book " + i, "Author " + i, "ISBN" + i));
        }
        long start = System.nanoTime();
        Book3 result = hashMap.get("ISBN9999");
        long end = System.nanoTime();
        System.out.println("  HashMap 查找 10000 条数据耗时: " + (end - start) / 1000 + " 微秒");

        // 8. 交互式查询示例（注释掉，避免阻塞）
        /*
        System.out.println("\n8. 交互式查询（输入 ISBN 查询，输入 'exit' 退出）：");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("请输入 ISBN: ");
            String input = scanner.nextLine();
            if ("exit".equals(input)) break;

            Book3 book = library.findByIsbn(input);
            if (book != null) {
                System.out.println("  找到: " + book);
            } else {
                System.out.println("  未找到 ISBN 为 " + input + " 的图书");
            }
        }
        */

        System.out.println("\n=== 最佳实践 ===");
        System.out.println("✅ 需要快速查找时用 HashMap（O(1)）");
        System.out.println("✅ 遍历时使用 entrySet() 而非 keySet() + get()");
        System.out.println("✅ 始终检查 get() 返回 null 的情况");
        System.out.println("✅ key 选择不可变且唯一的字段（如 ISBN、ID）");
    }
}
