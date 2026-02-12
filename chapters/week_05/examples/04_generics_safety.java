/**
 * 示例：展示泛型的类型安全 vs 原始类型的危险。
 *
 * 运行方式：javac chapters/week_05/examples/04_generics_safety.java && \
 *          java -cp chapters/week_05/examples GenericsSafetyDemo
 *
 * 预期输出：
 * - 展示原始类型导致的 ClassCastException
 * - 展示泛型如何在编译期捕获类型错误
 * - 展示泛型方法的使用
 */

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// Book 类定义
class Book4 {
    private String title;
    private String author;

    public Book4(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }

    @Override
    public String toString() {
        return title + " (" + author + ")";
    }
}

// 另一个类，用于展示类型混淆
class Magazine {
    private String name;
    private int issueNumber;

    public Magazine(String name, int issueNumber) {
        this.name = name;
        this.issueNumber = issueNumber;
    }

    @Override
    public String toString() {
        return name + " 第" + issueNumber + "期";
    }
}

// 反例：使用原始类型的危险
class RawTypeDanger {
    // ❌ 原始类型：没有类型参数
    private List books = new ArrayList();

    public void addBookUnsafe(Object anything) {
        books.add(anything);  // 任何东西都能加
    }

    public void processBooks() {
        System.out.println("处理原始类型列表中的 '图书'：");
        for (int i = 0; i < books.size(); i++) {
            try {
                // 必须强制转换，运行时可能失败
                Book4 book = (Book4) books.get(i);
                System.out.println("  " + (i + 1) + ". 书名: " + book.getTitle());
            } catch (ClassCastException e) {
                System.out.println("  " + (i + 1) + ". ❌ 类型转换失败: " + books.get(i));
            }
        }
    }
}

// 正例：使用泛型确保安全
class GenericSafety {
    // ✅ 泛型类型：明确指定只能放 Book
    private List<Book4> books = new ArrayList<>();

    public void addBook(Book4 book) {
        books.add(book);
        // books.add("字符串");  // ❌ 编译错误！
        // books.add(new Magazine("时代", 1));  // ❌ 编译错误！
    }

    public void processBooks() {
        System.out.println("处理泛型列表中的图书：");
        for (Book4 book : books) {  // 不需要强制转换
            System.out.println("  - " + book.getTitle());  // 类型安全
        }
    }

    // 返回安全的副本
    public List<Book4> getBooks() {
        return new ArrayList<>(books);
    }
}

// 泛型方法示例
class GenericMethods {
    // 泛型方法：可以处理任何类型的列表
    public <T> void printList(List<T> list) {
        System.out.println("列表内容（" + list.size() + " 项）：");
        for (T item : list) {
            System.out.println("  - " + item);
        }
    }

    // 泛型方法：查找最大值（元素必须实现 Comparable）
    public <T extends Comparable<T>> T findMax(List<T> list) {
        if (list.isEmpty()) return null;
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    // 泛型方法：交换列表中的两个元素
    public <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}

// 类型参数命名约定示例
class TypeParameterNaming {
    // 实际使用中的命名
    public void demonstrateNaming() {
        // E - Element
        List<String> stringList = new ArrayList<>();

        // K, V - Key, Value
        Map<String, Integer> scores = new HashMap<>();

        // T - Type（通用）
        // 在方法签名中使用

        System.out.println("类型参数命名约定：");
        System.out.println("  E = Element（集合元素）");
        System.out.println("  K = Key（映射的键）");
        System.out.println("  V = Value（映射的值）");
        System.out.println("  T = Type（通用类型）");
        System.out.println("  N = Number（数值类型）");
    }
}

// 菱形操作符 <> 演示
class DiamondOperatorDemo {
    public void demonstrate() {
        // Java 7 之前：需要重复写类型参数
        List<String> oldStyle = new ArrayList<String>();
        Map<String, List<Integer>> complexOld = new HashMap<String, List<Integer>>();

        // Java 7+：菱形操作符，编译器自动推断
        List<String> newStyle = new ArrayList<>();
        Map<String, List<Integer>> complexNew = new HashMap<>();

        System.out.println("菱形操作符 <> 让代码更简洁");
    }
}

// 主程序
class GenericsSafetyDemo {
    public static void main(String[] args) {
        System.out.println("=== 泛型类型安全演示 ===\n");

        // 1. 原始类型的危险
        System.out.println("1. 原始类型（raw type）的危险：");
        RawTypeDanger rawDanger = new RawTypeDanger();
        rawDanger.addBookUnsafe(new Book4("Java 核心技术", "Cay"));
        rawDanger.addBookUnsafe("我不是一本书");  // 字符串也能加！
        rawDanger.addBookUnsafe(new Magazine("程序员", 42));  // 杂志也能加！
        rawDanger.addBookUnsafe(12345);  // 整数也能加！
        rawDanger.processBooks();

        // 2. 泛型的编译期保护
        System.out.println("\n2. 泛型的编译期保护：");
        GenericSafety genericSafe = new GenericSafety();
        genericSafe.addBook(new Book4("Effective Java", "Joshua Bloch"));
        genericSafe.addBook(new Book4("Clean Code", "Robert Martin"));
        genericSafe.processBooks();

        // 以下代码如果取消注释，会在编译期报错：
        // genericSafe.addBook("字符串");  // ❌ 编译错误
        // genericSafe.addBook(new Magazine("时代", 1));  // ❌ 编译错误

        System.out.println("\n   ✅ 泛型在编译期就阻止了类型错误！");

        // 3. 泛型方法
        System.out.println("\n3. 泛型方法的使用：");
        GenericMethods gm = new GenericMethods();

        // 处理 String 列表
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        gm.printList(names);

        // 处理 Integer 列表
        List<Integer> numbers = new ArrayList<>();
        numbers.add(42);
        numbers.add(17);
        numbers.add(99);
        gm.printList(numbers);

        // 查找最大值
        Integer max = gm.findMax(numbers);
        System.out.println("\n最大值: " + max);

        // 交换元素
        System.out.println("交换前: " + numbers);
        gm.swap(numbers, 0, 2);
        System.out.println("交换后: " + numbers);

        // 4. 类型参数命名约定
        System.out.println("\n4. 类型参数命名约定：");
        System.out.println("   E = Element（列表、集合元素）");
        System.out.println("   K = Key（映射的键）");
        System.out.println("   V = Value（映射的值）");
        System.out.println("   T = Type（通用类型）");
        System.out.println("   N = Number（数值类型）");

        // 5. 菱形操作符
        System.out.println("\n5. 菱形操作符 <>：");
        DiamondOperatorDemo diamond = new DiamondOperatorDemo();
        diamond.demonstrate();

        // 6. 对比总结
        System.out.println("\n=== 原始类型 vs 泛型 对比 ===");
        System.out.println("┌─────────────┬─────────────────┬─────────────────┐");
        System.out.println("│   特性      │    原始类型      │    泛型         │");
        System.out.println("├─────────────┼─────────────────┼─────────────────┤");
        System.out.println("│ 语法        │ ArrayList       │ ArrayList<Book> │");
        System.out.println("│ 类型检查    │ 运行期          │ 编译期          │");
        System.out.println("│ 强制转换    │ 需要            │ 不需要          │");
        System.out.println("│ 错误发现    │ 运行时崩溃      │ 编译时报错      │");
        System.out.println("│ 代码安全    │ 低              │ 高              │");
        System.out.println("└─────────────┴─────────────────┴─────────────────┘");

        System.out.println("\n=== 最佳实践 ===");
        System.out.println("✅ 始终使用泛型，避免原始类型");
        System.out.println("✅ 使用菱形操作符 <> 简化代码");
        System.out.println("✅ 遵循类型参数命名约定（E, K, V, T）");
        System.out.println("✅ 返回集合时返回副本，防止外部修改");
    }
}
