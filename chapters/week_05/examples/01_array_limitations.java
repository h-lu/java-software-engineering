/**
 * 示例：展示数组的局限性——固定大小、扩容困难、类型不安全。
 *
 * 运行方式：javac chapters/week_05/examples/01_array_limitations.java && \
 *          java -cp chapters/week_05/examples ArrayLimitationsDemo
 *
 * 预期输出：
 * - 正常添加 3 本书
 * - 尝试添加第 4 本时抛出 ArrayIndexOutOfBoundsException
 * - 展示手动扩容的繁琐代码
 */

// 简单的 Book 类定义（与示例在同一文件）
class Book {
    private String title;
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
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

// 使用固定大小数组的图书馆（有问题版本）
class LibraryWithFixedArray {
    private Book[] books;  // 固定大小的数组
    private int count;     // 当前实际存储的数量

    public LibraryWithFixedArray(int capacity) {
        books = new Book[capacity];  // 创建时就必须确定大小
        count = 0;
    }

    // 问题 1：没有边界检查，超过容量会崩溃
    public void addBookUnsafe(Book book) {
        books[count] = book;  // 如果 count >= books.length，这里会崩溃
        count++;
    }

    // 问题 2：手动扩容非常繁琐
    public void addBookWithManualExpansion(Book book) {
        if (count >= books.length) {
            // 手动扩容：创建新数组 + 复制元素
            System.out.println("数组满了，正在手动扩容...");
            Book[] newBooks = new Book[books.length * 2];
            for (int i = 0; i < books.length; i++) {
                newBooks[i] = books[i];  // 逐个复制，效率低
            }
            books = newBooks;
            System.out.println("扩容完成，新容量: " + books.length);
        }
        books[count] = book;
        count++;
    }

    public void listBooks() {
        System.out.println("图书馆藏书（" + count + " 本）：");
        for (int i = 0; i < count; i++) {
            System.out.println("  " + (i + 1) + ". " + books[i]);
        }
    }
}

// 问题 3：数组的类型不安全示例
class UnsafeArrayExample {
    public void demonstrateUnsafeCasting() {
        // 原始类型的数组可以混入不同类型的对象
        Object[] objects = new Object[3];
        objects[0] = new Book("Java 核心技术", "Cay", "111");
        objects[1] = "我是一个字符串，不是书";  // 编译通过！
        objects[2] = 12345;  // 整数也能放进去！

        // 运行时才会发现类型错误
        for (int i = 0; i < objects.length; i++) {
            try {
                Book book = (Book) objects[i];  // 强制转换可能失败
                System.out.println("书名: " + book.getTitle());
            } catch (ClassCastException e) {
                System.out.println("第 " + i + " 个元素不是 Book: " + objects[i]);
            }
        }
    }
}

// 主程序
class ArrayLimitationsDemo {
    public static void main(String[] args) {
        System.out.println("=== 演示 1：数组容量限制 ===\n");

        // 创建一个只能存 3 本书的图书馆
        LibraryWithFixedArray library = new LibraryWithFixedArray(3);

        // 添加 3 本书，没问题
        library.addBookUnsafe(new Book("Java 核心技术", "Cay Horstmann", "978-111"));
        library.addBookUnsafe(new Book("Effective Java", "Joshua Bloch", "978-222"));
        library.addBookUnsafe(new Book("Clean Code", "Robert Martin", "978-333"));
        library.listBooks();

        System.out.println("\n尝试添加第 4 本书（将崩溃）...");
        try {
            library.addBookUnsafe(new Book("重构", "Martin Fowler", "978-444"));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("❌ 崩溃：" + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        System.out.println("\n=== 演示 2：手动扩容 ===\n");

        // 使用支持扩容的版本
        LibraryWithFixedArray expandableLibrary = new LibraryWithFixedArray(2);
        expandableLibrary.addBookWithManualExpansion(new Book("书 1", "作者 A", "001"));
        expandableLibrary.addBookWithManualExpansion(new Book("书 2", "作者 B", "002"));
        expandableLibrary.addBookWithManualExpansion(new Book("书 3", "作者 C", "003"));  // 触发扩容
        expandableLibrary.listBooks();

        System.out.println("\n=== 演示 3：类型不安全 ===\n");

        UnsafeArrayExample unsafe = new UnsafeArrayExample();
        unsafe.demonstrateUnsafeCasting();

        System.out.println("\n=== 结论 ===");
        System.out.println("数组的问题：");
        System.out.println("1. 大小固定，扩容需手动处理");
        System.out.println("2. 边界检查容易遗漏，导致 ArrayIndexOutOfBoundsException");
        System.out.println("3. 类型不安全，运行时才可能发现类型错误");
        System.out.println("\n解决方案：使用 ArrayList（见下一个示例）");
    }
}
