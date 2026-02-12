/**
 * 示例：展示 ArrayList 的基本操作——add、get、size、remove。
 *
 * 运行方式：javac chapters/week_05/examples/02_arraylist_basic.java && \
 *          java -cp chapters/week_05/examples ArrayListBasicDemo
 *
 * 预期输出：
 * - 动态添加超过初始容量的元素（自动扩容）
 * - 展示索引访问和异常处理
 * - 对比安全与不安全的使用方式
 */

import java.util.ArrayList;
import java.util.List;

// Book 类定义
class Book2 {
    private String title;
    private String author;
    private String isbn;

    public Book2(String title, String author, String isbn) {
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

// 使用 ArrayList 的图书馆（推荐做法）
class LibraryWithArrayList {
    // 使用泛型确保类型安全
    private ArrayList<Book2> books;

    public LibraryWithArrayList() {
        // 初始为空，不需要预设大小
        books = new ArrayList<>();
    }

    // 添加图书——自动扩容，无需关心容量
    public void addBook(Book2 book) {
        books.add(book);
    }

    // 在指定位置插入
    public void addBookAt(int index, Book2 book) {
        books.add(index, book);  // 其他元素自动后移
    }

    // 按索引获取（需要处理越界异常）
    public Book2 getBook(int index) {
        try {
            return books.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  ⚠️ 索引 " + index + " 超出范围（当前共 " + books.size() + " 本）");
            return null;
        }
    }

    // 删除图书
    public boolean removeBook(Book2 book) {
        return books.remove(book);  // 按对象删除
    }

    public Book2 removeBookAt(int index) {
        try {
            return books.remove(index);  // 按索引删除
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  ⚠️ 无法删除：索引 " + index + " 超出范围");
            return null;
        }
    }

    // 检查是否包含
    public boolean hasBook(Book2 book) {
        return books.contains(book);
    }

    // 获取数量
    public int getBookCount() {
        return books.size();  // size() 而非 length
    }

    // 清空
    public void clear() {
        books.clear();
    }

    // 列出所有图书
    public void listBooks() {
        System.out.println("图书馆藏书（" + books.size() + " 本）：");
        if (books.isEmpty()) {
            System.out.println("  (暂无图书)");
            return;
        }
        for (int i = 0; i < books.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + books.get(i));
        }
    }

    // 返回安全的副本（防止外部修改内部数据）
    public List<Book2> getAllBooks() {
        return new ArrayList<>(books);  // 返回副本
    }
}

// 反例：不使用泛型的危险做法
class UnsafeLibrary {
    // ❌ 原始类型（raw type）——为了兼容旧代码，但不应该使用
    private ArrayList books = new ArrayList();

    public void addBookUnsafe(Object anything) {
        books.add(anything);  // 什么东西都能加！
    }

    public void demonstrateProblem() {
        // 混入不同类型的数据
        addBookUnsafe(new Book2("Java 核心技术", "Cay", "111"));
        addBookUnsafe("我不是一本书");
        addBookUnsafe(12345);

        System.out.println("\n原始类型的问题：");
        for (int i = 0; i < books.size(); i++) {
            try {
                // 必须强制转换，可能失败
                Book2 book = (Book2) books.get(i);
                System.out.println("  " + (i + 1) + ". " + book.getTitle());
            } catch (ClassCastException e) {
                System.out.println("  " + (i + 1) + ". ❌ 类型错误: " + books.get(i));
            }
        }
    }
}

// 主程序
class ArrayListBasicDemo {
    public static void main(String[] args) {
        System.out.println("=== ArrayList 基本操作演示 ===\n");

        LibraryWithArrayList library = new LibraryWithArrayList();

        // 1. 添加图书——自动扩容
        System.out.println("1. 添加图书（自动扩容）：");
        library.addBook(new Book2("Java 核心技术", "Cay Horstmann", "978-111"));
        library.addBook(new Book2("Effective Java", "Joshua Bloch", "978-222"));
        library.addBook(new Book2("Clean Code", "Robert Martin", "978-333"));
        System.out.println("   已添加 3 本书，当前数量: " + library.getBookCount());

        // 继续添加更多，展示自动扩容
        for (int i = 4; i <= 10; i++) {
            library.addBook(new Book2("Book " + i, "Author " + i, "ISBN" + i));
        }
        System.out.println("   已添加 10 本书，当前数量: " + library.getBookCount());

        // 2. 列出所有图书
        System.out.println("\n2. 列出所有图书：");
        library.listBooks();

        // 3. 在指定位置插入
        System.out.println("\n3. 在位置 0 插入新书：");
        library.addBookAt(0, new Book2("插入到开头", "特殊作者", "INSERT"));
        library.listBooks();

        // 4. 按索引获取
        System.out.println("\n4. 按索引获取：");
        System.out.println("   索引 0: " + library.getBook(0));
        System.out.println("   索引 5: " + library.getBook(5));
        System.out.println("   尝试获取无效索引：");
        library.getBook(100);  // 会触发异常处理

        // 5. 删除操作
        System.out.println("\n5. 删除操作：");
        Book2 toRemove = new Book2("将被删除", "临时作者", "TEMP");
        library.addBook(toRemove);
        System.out.println("   添加临时图书后数量: " + library.getBookCount());
        library.removeBook(toRemove);
        System.out.println("   删除后数量: " + library.getBookCount());

        System.out.println("   删除索引 0：" + library.removeBookAt(0));
        System.out.println("   尝试删除无效索引：");
        library.removeBookAt(100);  // 会触发异常处理

        // 6. 检查是否为空
        System.out.println("\n6. 清空检查：");
        System.out.println("   是否为空: " + library.getAllBooks().isEmpty());

        // 7. 原始类型的危险
        System.out.println("\n7. 原始类型（raw type）的危险：");
        UnsafeLibrary unsafe = new UnsafeLibrary();
        unsafe.demonstrateProblem();

        System.out.println("\n=== 最佳实践 ===");
        System.out.println("✅ 始终使用泛型：ArrayList<Book> 而非 ArrayList");
        System.out.println("✅ 使用 isEmpty() 而非 size() == 0");
        System.out.println("✅ 返回集合时返回副本，保护内部数据");
        System.out.println("✅ 处理可能的 IndexOutOfBoundsException");
    }
}
