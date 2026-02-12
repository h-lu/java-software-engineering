/**
 * 示例：展示增强 for 循环和 Iterator 的使用场景。
 *
 * 运行方式：javac chapters/week_05/examples/05_iteration_patterns.java && \
 *          java -cp chapters/week_05/examples IterationPatternsDemo
 *
 * 预期输出：
 * - 展示三种遍历方式的区别
 * - 展示遍历时删除的正确做法
 * - 展示 ConcurrentModificationException 的产生和避免
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// Book 类定义
class Book5 {
    private String title;
    private String author;
    private String isbn;

    public Book5(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }

    @Override
    public String toString() {
        return title + " (" + author + ")";
    }
}

// 遍历方式演示
class IterationExamples {
    private List<Book5> books;

    public IterationExamples() {
        books = new ArrayList<>();
        books.add(new Book5("Java 核心技术", "Cay Horstmann", "111"));
        books.add(new Book5("Effective Java", "Joshua Bloch", "222"));
        books.add(new Book5("Clean Code", "Robert Martin", "333"));
        books.add(new Book5("佚名作品", "佚名", "444"));
        books.add(new Book5("重构", "Martin Fowler", "555"));
    }

    // 方式 1：传统 for 循环（需要索引时）
    public void traditionalForLoop() {
        System.out.println("方式 1：传统 for 循环");
        System.out.println("适用场景：需要索引位置");
        for (int i = 0; i < books.size(); i++) {
            Book5 book = books.get(i);
            System.out.println("  [" + i + "] " + book);
        }
    }

    // 方式 2：增强 for 循环（for-each）
    public void enhancedForLoop() {
        System.out.println("\n方式 2：增强 for 循环（推荐用于只读遍历）");
        System.out.println("适用场景：简单遍历，不需要修改集合");
        for (Book5 book : books) {
            System.out.println("  - " + book);
        }
    }

    // 方式 3：Iterator（需要遍历时修改集合）
    public void iteratorLoop() {
        System.out.println("\n方式 3：Iterator（遍历时安全删除）");
        System.out.println("适用场景：需要在遍历过程中删除元素");
        Iterator<Book5> iterator = books.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Book5 book = iterator.next();
            System.out.println("  [" + index + "] " + book);
            index++;
        }
    }

    // 错误示范：增强 for 中删除元素
    public void wrongWayToRemove() {
        System.out.println("\n❌ 错误示范：增强 for 中直接删除");
        // 创建副本进行演示，避免破坏原始数据
        List<Book5> copy = new ArrayList<>(books);
        try {
            for (Book5 book : copy) {
                if ("佚名".equals(book.getAuthor())) {
                    copy.remove(book);  // 危险！会抛出 ConcurrentModificationException
                }
            }
        } catch (Exception e) {
            System.out.println("  捕获异常: " + e.getClass().getSimpleName());
            System.out.println("  原因: 增强 for 内部使用 Iterator，直接调用 remove() 破坏了迭代器状态");
        }
    }

    // 正确做法：使用 Iterator.remove()
    public void correctWayToRemove() {
        System.out.println("\n✅ 正确做法：使用 Iterator.remove()");
        List<Book5> copy = new ArrayList<>(books);
        System.out.println("删除前数量: " + copy.size());

        Iterator<Book5> iterator = copy.iterator();
        while (iterator.hasNext()) {
            Book5 book = iterator.next();
            if ("佚名".equals(book.getAuthor())) {
                iterator.remove();  // 使用迭代器的 remove，安全
                System.out.println("  已删除: " + book.getTitle());
            }
        }
        System.out.println("删除后数量: " + copy.size());
    }

    // Java 8+ 的 removeIf 方法
    public void java8RemoveIf() {
        System.out.println("\n✅ Java 8+ 推荐做法：removeIf()");
        List<Book5> copy = new ArrayList<>(books);
        System.out.println("删除前数量: " + copy.size());

        // 一行代码完成条件删除
        copy.removeIf(book -> "佚名".equals(book.getAuthor()));

        System.out.println("删除后数量: " + copy.size());
        System.out.println("  优点: 简洁、内部使用 Iterator 安全删除");
    }

    // 遍历 Map 的方式
    public void mapIterationDemo() {
        System.out.println("\n=== Map 的遍历方式 ===");

        Map<String, Book5> bookMap = new HashMap<>();
        for (Book5 book : books) {
            bookMap.put(book.getIsbn(), book);
        }

        // 方式 1：遍历 keySet（效率低）
        System.out.println("\n方式 1：遍历 keySet（不推荐）");
        System.out.println("问题: 每次 get(key) 都是一次额外的哈希查找");
        for (String isbn : bookMap.keySet()) {
            Book5 book = bookMap.get(isbn);  // 额外的查找操作
            System.out.println("  " + isbn + " -> " + book);
        }

        // 方式 2：遍历 entrySet（推荐）
        System.out.println("\n方式 2：遍历 entrySet（推荐）");
        System.out.println("优点: 直接获取键值对，效率高");
        for (Map.Entry<String, Book5> entry : bookMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        // 方式 3：Java 8+ forEach
        System.out.println("\n方式 3：Java 8+ forEach（最简洁）");
        bookMap.forEach((isbn, book) ->
            System.out.println("  " + isbn + " -> " + book)
        );
    }
}

// 主程序
class IterationPatternsDemo {
    public static void main(String[] args) {
        System.out.println("=== 集合遍历方式演示 ===\n");

        IterationExamples examples = new IterationExamples();

        // 三种基本遍历方式
        examples.traditionalForLoop();
        examples.enhancedForLoop();
        examples.iteratorLoop();

        // 删除元素的正确与错误方式
        System.out.println("\n=== 遍历时删除元素 ===");
        examples.wrongWayToRemove();
        examples.correctWayToRemove();
        examples.java8RemoveIf();

        // Map 遍历
        examples.mapIterationDemo();

        // 总结
        System.out.println("\n=== 遍历方式选择指南 ===");
        System.out.println("┌─────────────────────┬──────────────────────────────────────────┐");
        System.out.println("│ 场景                │ 推荐方式                                 │");
        System.out.println("├─────────────────────┼──────────────────────────────────────────┤");
        System.out.println("│ 需要索引位置        │ 传统 for (int i = 0; ...)               │");
        System.out.println("│ 只读遍历            │ 增强 for (Book b : books)               │");
        System.out.println("│ 遍历时删除          │ Iterator + iterator.remove()            │");
        System.out.println("│ 条件删除（Java 8+） │ removeIf()                              │");
        System.out.println("│ 遍历 Map            │ entrySet() 或 forEach()                 │");
        System.out.println("└─────────────────────┴──────────────────────────────────────────┘");

        System.out.println("\n=== 常见陷阱 ===");
        System.out.println("❌ 不要在增强 for 中直接调用集合的 remove()");
        System.out.println("❌ 不要遍历 keySet() 然后反复 get()");
        System.out.println("❌ 不要在遍历时修改集合（除非用 Iterator）");
        System.out.println("\n✅ 优先使用增强 for 进行只读遍历");
        System.out.println("✅ 需要删除时用 Iterator 或 removeIf()");
        System.out.println("✅ 遍历 Map 时用 entrySet() 而非 keySet()");
    }
}
