# Week 05：把数据装进容器

> "程序 = 算法 + 数据结构。"
> —— Niklaus Wirth

想象一下，你正在开发一个图书馆管理系统。用户不断录入新书——书名、作者、ISBN、馆藏位置。用数组存储？你得在写代码时就确定最多能存多少本书。100 本？万一第 101 本来了呢？1000 本？内存又白白浪费。

这不仅是 Java 的问题。从 Python 的 `list` 和 `dict`，到 JavaScript 的 `Array` 和 `Object`，现代编程语言都提供了一套"弹性容器"来解决这个困境。但 Java 的做法有些特别——它不仅提供容器，还通过**泛型**（Generics）让你在编译期就确保容器里装的是对的东西。

在 AI 辅助编程的时代，你或许会让 AI 帮你生成集合操作代码。但问题是：AI 不会替你思考该用 `ArrayList` 还是 `HashMap`，也不会提醒你"原始类型"（raw type）带来的类型安全隐患。这些判断，必须掌握在你自己手中。

<!--
贯穿案例设计：【图书借阅追踪器】
- 第 1 节（数组的局限）：从固定大小的数组存储图书开始，体验扩容困难和类型不安全
- 第 2 节（ArrayList）：用 ArrayList 重构图书存储，实现动态添加和遍历查询
- 第 3 节（HashMap）：引入 ISBN 快速查找，体验从 O(n) 到 O(1) 的检索效率
- 第 4 节（泛型基础）：给 ArrayList 和 HashMap 添加类型参数，捕获编译期类型错误
- 第 5 节（迭代器与增强 for）：优雅地遍历集合，避免索引陷阱
- 第 6 节（综合应用）：完成支持添加、查询、删除的完整版图书借阅追踪器
最终成果：一个可运行的 CLI 图书借阅追踪器，使用 ArrayList 和 HashMap 组合存储
-->

<!--
认知负荷预算检查：
- 本周新概念（6 个，系统化工程阶段上限 6 个）：
  1. ArrayList（动态数组）
  2. HashMap（键值对映射）
  3. 泛型（类型参数）
  4. 原始类型与类型安全
  5. 迭代器（Iterator）
  6. 增强 for 循环（for-each）
- 结论：✅ 在预算内

回顾桥设计（至少 2 个）：
- [类定义/封装]（来自 week_02）：在第 1 节，通过 Book 类的设计回顾封装原则
- [try-catch-finally]（来自 week_03）：在第 2 节，处理集合操作中的 IndexOutOfBoundsException
- [Scanner 输入]（来自 week_01）：贯穿案例中使用 Scanner 获取用户输入
-->

---

## 前情提要

过去四周，你已经掌握了 Java 的基础：从第一个 "Hello, World" 到设计完整的领域模型，从处理异常到与团队协作。你的 CampusFlow 项目已经有了清晰的类结构和异常处理策略。

但有一个问题始终悬而未决：数据存在哪里？目前你的项目可能还在用固定大小的数组，或者每次重启数据就消失。本周我们要解决这个问题——用 Java 的集合框架搭建一个灵活、可扩展的内存存储系统。

---

## 学习目标

完成本周学习后，你将能够：

1. 解释数组与 ArrayList 的核心差异，并在合适场景选择正确的容器
2. 使用 HashMap 实现基于键的高效查找（O(1) 时间复杂度）
3. 运用泛型确保集合的类型安全，避免 ClassCastException
4. 使用增强 for 循环和迭代器优雅地遍历集合
5. 识别并修复集合操作中的常见边界错误

---

## 1. 数组的困境——为什么需要集合

<!--
**Bloom 层次**：理解
**学习目标**：理解数组的局限性，感受集合框架的必要性
**贯穿案例推进**：从固定大小数组存储图书开始，体验扩容困难和类型不安全
**建议示例文件**：01_array_limitations.java
**叙事入口**：小北尝试用数组管理 50 本图书，第 51 本书录入时系统崩溃
**角色出场**：小北——"明明还有内存，为什么存不进去？"
**回顾桥**：[类定义/封装]（week_02）：展示 Book 类的封装，为后续集合存储做铺垫
**AI 融合阶段**：观察期——了解集合框架的存在，但不依赖 AI
-->

小北接到了一个新任务：给图书馆写一个简单的图书管理程序。作为从 Python 转过来的学生，他心想这还不简单——用数组存书就行了。

"让我看看……一个图书馆大概有多少本书？"小北估算了一下，"先定 50 本应该够用了。"

```java
// 文件：Book.java
public class Book {
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
```

这是 Week 02 学过的**封装**（encapsulation）——把数据藏在 `private` 字段里，只通过公开方法访问。小北对自己的领域建模很满意。

接下来是管理类：

```java
// 文件：LibraryWithArray.java
public class LibraryWithArray {
    private Book[] books;  // 固定大小的数组
    private int count;     // 当前实际存储的数量

    public LibraryWithArray() {
        books = new Book[50];  // 最多存 50 本
        count = 0;
    }

    public void addBook(Book book) {
        books[count] = book;
        count++;
    }

    public void listBooks() {
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + books[i]);
        }
    }
}
```

程序跑起来了。小北陆续录入了《Java 核心技术》《Effective Java》《Clean Code》……一切顺利，直到第 51 本书。

```java
LibraryWithArray library = new LibraryWithArray();

// 录入 50 本书……没问题
for (int i = 0; i < 50; i++) {
    library.addBook(new Book("Book " + i, "Author " + i, "ISBN" + i));
}

// 第 51 本
library.addBook(new Book("第 51 本书", "某作者", "ISBN999"));
```

运行后，控制台一片血红：

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 50
    at LibraryWithArray.addBook(LibraryWithArray.java:15)
```

"明明还有内存，为什么存不进去？"小北盯着屏幕，一脸茫然。

这就是数组的**第一个困境**：大小固定。你在创建数组时就必须决定它能装多少元素，一旦填满，再想扩容就得手动创建更大的数组，然后把旧数据一个个复制过去。

小北试着修复这个问题。他在 `addBook` 里加了个检查：

```java
public void addBook(Book book) {
    if (count >= books.length) {
        // 数组满了，扩容！
        Book[] newBooks = new Book[books.length * 2];  // 翻倍
        for (int i = 0; i < books.length; i++) {
            newBooks[i] = books[i];  // 手动复制，好麻烦
        }
        books = newBooks;
    }
    books[count] = book;
    count++;
}
```

代码能跑了，但小北皱起了眉头。这种"手动扩容"的代码每个项目都要写一遍吗？而且还有一个隐患——数组是**类型不安全**的。理论上 `Book[]` 只能存 `Book` 对象，但 Java 的数组在运行期其实允许一些危险的类型转换，稍不注意就会踩坑。

"一定有更好的办法。"小北想。

---

## 2. 追问——ArrayList 为什么能自动扩容？

<!--
**Bloom 层次**：应用
**学习目标**：掌握 ArrayList 的基本操作（添加、获取、删除、遍历）
**贯穿案例推进**：用 ArrayList 重构图书存储，实现动态添加和遍历查询
**建议示例文件**：02_arraylist_basic.java
**叙事入口**：阿码追问内部机制，从"怎么用"深入到"为什么"
**角色出场**：阿码——"它自动扩容？那它到底创建了几个数组？"
**回顾桥**：[try-catch-finally]（week_03）：处理可能的 IndexOutOfBoundsException
-->

小北用 `ArrayList` 重写了图书馆程序，第 51 本书顺利存了进去。阿码在旁边看着，突然问："你说它自动扩容——那它到底创建了几个数组？"

"什么意思？"

"我的意思是，数组的大小不是固定的吗？`ArrayList` 说自己底层也是数组，那它怎么做到自动扩容的？"

好问题。`ArrayList` 的秘密是：**它确实会创建新数组，只是这个过程对你隐藏了**。

```java
import java.util.ArrayList;

// 文件：LibraryWithArrayList.java
public class LibraryWithArrayList {
    private ArrayList<Book> books;  // 注意这个 <Book>

    public LibraryWithArrayList() {
        books = new ArrayList<>();  // 初始为空，不需要预设大小
    }

    public void addBook(Book book) {
        books.add(book);  // 简单一行，内部可能正在创建新数组、复制数据
    }

    public void listBooks() {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println((i + 1) + ". " + book);
        }
    }
}
```

那个尖括号里的 `Book` 叫**泛型**（Generics），它告诉 Java："这个列表里只能放 `Book` 对象。"

```java
ArrayList<Book> books = new ArrayList<>();
books.add(new Book("Java 核心技术", "Cay Horstmann", "978-7-111-12345"));

// 这行代码编译都过不了！
// books.add("我不是一本书");  // ❌ 编译错误：类型不匹配
```

阿码点点头："所以 `<Book>` 是安全检查？"

"对，编译期检查。类型不对直接报错，不会等到运行时才崩溃。"

`ArrayList` 默认创建容量为 10 的数组。当第 11 本书加进来时，它会创建一个更大的数组（通常是 1.5 倍），把旧数据复制过去，然后丢弃旧数组。这一切都在 `add()` 方法内部完成，所以你只需要写一行 `books.add(book)`。

但扩容是有代价的——复制数据需要时间。如果你事先知道大概要存多少本书，可以指定初始容量：

```java
ArrayList<Book> books = new ArrayList<>(1000);  // 一开始就申请 1000 个位置
```

小北很快遇到了一个新问题。他试着访问一个不存在的索引：

```java
Book book = books.get(100);  // 假设只有 10 本书
```

程序又抛异常了——这让他想起了 Week 03 学的**异常处理**：

```java
public Book findBook(int index) {
    try {
        return books.get(index);
    } catch (IndexOutOfBoundsException e) {
        System.out.println("索引 " + index + " 超出范围");
        return null;
    }
}
```

"至少现在不用担心第 51 本书的问题了。"小北松了口气。

---

> **AI 时代小专栏：向 AI 提问获取准确的类型解释**
>
> 当你面对 `<Book>` 这样的语法感到困惑时，AI 助手可以帮你——但前提是你知道怎么问。
>
> **❌ 不太好的提问**：
> "Java 的 ArrayList 怎么用？"
> 这种问法太宽泛，AI 可能给你一堆基础示例，但泛型的核心概念反而被淹没。
>
> **✅ 更好的提问**：
> "Java ArrayList 的 `<Book>` 语法叫什么？如果不写它会怎样？请用对比代码说明。"
> 这样问，AI 会直接告诉你这叫"泛型"，并展示"有泛型"和"原始类型"的区别。
>
> **验证 AI 答案的技巧**：
> 1. **让 AI 给代码，你亲手编译**：AI 可能会生成语法正确的代码，但类型安全需要你自己验证
> 2. **追问边界情况**："如果我把 String 放进 ArrayList<Book> 会怎样？"
> 3. **交叉验证**：把 AI 的解释和官方文档对比，特别是涉及类型擦除（type erasure）的部分
>
> 根据 JetBrains 2024 开发者生态系统调查，Java 在服务端开发领域仍保持强劲地位，而泛型是 Java 代码库中最常用的特性之一。理解泛型不仅能帮你写出更安全的代码，也能让你在审查 AI 生成代码时发现问题——比如 AI 偶尔会遗漏类型参数，生成原始类型的代码。
>
> 参考（访问日期：2026-02-11）：
> - https://www.javaguides.net/2018/09/java-generics-best-practices.html
> - https://www.digitalocean.com/community/tutorials/java-generics-example-method-class-interface
> - https://www.jetbrains.com/lp/devecosystem-2024/

---

## 3. 老潘的工程视角——为什么需要 HashMap？

<!--
**Bloom 层次**：应用/分析
**学习目标**：理解 HashMap 的键值对模型，掌握基于键的增删改查
**贯穿案例推进**：引入 ISBN 作为 key，实现图书的快速查找（对比线性搜索）
**建议示例文件**：03_hashmap_lookup.java
**叙事入口**：老潘从工程角度指出线性查找的性能问题
**角色出场**：老潘——"生产环境里，O(n) 和 O(1) 的区别可能是系统能不能扛住流量"
**回顾桥**：[Scanner 输入]（week_01）：用 Scanner 获取用户输入的 ISBN 进行查询
-->

`ArrayList` 解决了存储问题，但小北发现了一个新的痛点：查找。

图书管理员说："帮我找一下 ISBN 978-7-111-12345 的那本书。"

小北的程序只能这样做：

```java
public Book findByIsbn(String isbn) {
    for (int i = 0; i < books.size(); i++) {
        Book book = books.get(i);
        if (book.getIsbn().equals(isbn)) {  // 逐个比较
            return book;
        }
    }
    return null;  // 没找到
}
```

这叫做**线性搜索**（linear search）。如果图书馆有 1000 本书，平均要找 500 次才能找到目标；如果有 100 万本书呢？

老潘路过看了一眼："生产环境里，O(n) 和 O(1) 的区别可能是系统能不能扛住流量。你这查找复杂度是线性的，用户多了肯定崩。"

"那怎么办？"

"用 `HashMap`。把 ISBN 当钥匙（key），书当值（value），直接开门取物。"

```java
import java.util.HashMap;

// 文件：LibraryWithHashMap.java
public class LibraryWithHashMap {
    private HashMap<String, Book> bookByIsbn;  // key: ISBN, value: Book

    public LibraryWithHashMap() {
        bookByIsbn = new HashMap<>();
    }

    public void addBook(Book book) {
        bookByIsbn.put(book.getIsbn(), book);  // 放进去
    }

    public Book findByIsbn(String isbn) {
        return bookByIsbn.get(isbn);  // 直接取，几乎是瞬间完成
    }

    public boolean hasBook(String isbn) {
        return bookByIsbn.containsKey(isbn);  // 检查有没有这个 key
    }

    public void removeBook(String isbn) {
        bookByIsbn.remove(isbn);  // 按 key 删除
    }
}
```

`HashMap` 的魔法在于**哈希函数**。当你调用 `put("ISBN123", book)` 时，它会把 `"ISBN123"` 转换成一个数字（哈希码），然后根据这个数字直接算出书应该存在内存的哪个位置。查找时同样算一次哈希，直接跳到那个位置——**O(1)** 时间复杂度，无论有 10 本书还是 100 万本书，速度都差不多。

小北用 Week 01 学的 `Scanner` 写了个交互式查询：

```java
import java.util.Scanner;

public class LibraryApp {
    public static void main(String[] args) {
        LibraryWithHashMap library = new LibraryWithHashMap();
        Scanner scanner = new Scanner(System.in);

        // 先加几本书
        library.addBook(new Book("Java 核心技术", "Cay Horstmann", "978-111"));
        library.addBook(new Book("Effective Java", "Joshua Bloch", "978-222"));

        System.out.print("请输入 ISBN 查询：");
        String isbn = scanner.nextLine();

        Book found = library.findByIsbn(isbn);
        if (found != null) {
            System.out.println("找到：" + found);
        } else {
            System.out.println("未找到 ISBN 为 " + isbn + " 的图书");
        }
    }
}
```

"等等，"小北突然想到一个问题，"如果两本书的 ISBN 算出来哈希码一样怎么办？"

老潘点点头："这叫**哈希冲突**。`HashMap` 内部用链表（或红黑树）解决，你不需要操心。但有个事你得注意——当 key 是自定义对象时，记得重写 `equals()` 和 `hashCode()`，否则可能找不到东西。"

对于 ISBN 这种唯一的字符串，Java 已经帮我们处理好了，直接用就行。

---

## 4. 阿码的陷阱——原始类型有多危险？

<!--
**Bloom 层次**：理解/应用
**学习目标**：理解泛型的类型安全价值，掌握泛型类的声明和使用
**贯穿案例推进**：阿码先犯错（用原始类型），小北顿悟泛型的必要性
**建议示例文件**：04_generics_safety.java
**叙事入口**：阿码觉得"<Book>好麻烦"，故意省略后踩坑
**角色出场**：阿码——"不写<Book>也能跑啊，你看……"；小北——"等等，这代码有问题！"
**回顾桥**：[静态类型]（week_01）：从 Java 的静态类型系统延伸到泛型的编译期检查
-->

阿码看着满屏的 `<Book>`，有点不耐烦："每次都要写这个尖括号，好麻烦。不写也能跑吧？"

他偷偷改了一行代码：

```java
// 故意不写 <Book>，变成了"原始类型"
ArrayList books = new ArrayList();
books.add(new Book("Java 核心技术", "Cay", "111"));
books.add("我不是一本书");  // 居然编译通过了！
```

没有 `<Book>` 的 `ArrayList` 叫做**原始类型**（raw type）。它为了兼容 Java 1.5 之前的代码而存在，但就像没有安检的机场——什么东西都能进去。

"你看，运行正常！"阿码得意地说。

小北皱起眉头："但你这列表里现在既有 `Book` 又有 `String`，后面怎么处理？"

果然，问题在运行时才暴露：

```java
for (int i = 0; i < books.size(); i++) {
    Book book = (Book) books.get(i);  // 强制转换
    System.out.println(book.getTitle());  // 第二遍循环时崩溃！
}
```

```
java.lang.ClassCastException: class java.lang.String cannot be cast to class Book
```

阿码愣住了："明明编译过了……"

"编译器只是给了你一个警告，"小北指着 IDE 的黄色波浪线，"你看这里写着 'Raw use of parameterized class'。原始类型绕过了类型检查，把炸弹留到了运行期。"

加上 `<Book>` 后：

```java
ArrayList<Book> books = new ArrayList<>();
books.add("我不是一本书");  // ❌ 编译错误！红色波浪线立刻出现
```

错误在写代码时就被捕获，而不是等到用户使用时才崩溃。这正是 Java **静态类型**系统的核心思想——把错误从运行期提前到编译期。

泛型还有一些约定俗成的类型参数命名：

| 参数 | 含义 | 使用场景 |
|------|------|----------|
| `E` | Element | 列表、集合中的元素 |
| `K` | Key | 映射中的键 |
| `V` | Value | 映射中的值 |
| `T` | Type | 通用类型 |

所以 `ArrayList<E>` 表示"元素类型为 E 的列表"，`HashMap<K, V>` 表示"键类型为 K、值类型为 V 的映射"。

小北现在的图书管理器长这样：

```java
public class Library {
    private ArrayList<Book> books;           // 用列表存所有书（支持遍历）
    private HashMap<String, Book> isbnIndex; // 用映射做 ISBN 索引（支持快速查找）

    public Library() {
        books = new ArrayList<>();
        isbnIndex = new HashMap<>();
    }

    public void addBook(Book book) {
        books.add(book);
        isbnIndex.put(book.getIsbn(), book);  // 两边都存
    }

    public Book findByIsbn(String isbn) {
        return isbnIndex.get(isbn);  // O(1) 查找
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);  // 返回副本，保护内部数据
    }
}
```

阿码挠挠头："好吧，原来 `<>` 不是装饰，是安全网。"

---

> **AI 时代小专栏：审查 AI 生成的集合代码**
>
> 让 AI 写集合代码很方便，但审查时要盯紧三个陷阱：
>
> **陷阱 1：原始类型**
> ```java
> List books = new ArrayList();  // ❌ AI 偶尔会漏掉类型参数
> ```
> 必须改成 `List<Book> books = new ArrayList<>();`
>
> **陷阱 2：不安全的类型转换**
> ```java
> Book b = (Book) list.get(0);  // ❌ 运行时可能爆炸
> ```
> 用泛型消除强制转换。
>
> **陷阱 3：遗漏边界检查**
> AI 可能忘记检查 null 或空集合。记住：用 `isEmpty()` 而非 `size() == 0`，先 `containsKey()` 再 `get()`。
>
> 所以阿码在第 4 节踩的坑，AI 也会踩。你的审查清单：类型参数、强制转换、边界检查——三样缺一不可。
>
> 参考（访问日期：2026-02-11）：
> - https://www.javaguides.net/2025/02/top-15-java-collections-and-generics.html
> - https://www.javaguides.net/2024/05/java-collections-best-practices-for-developers.html

---

## 5. 遍历的艺术——何时用哪种方式

<!--
**Bloom 层次**：应用
**学习目标**：掌握增强 for 循环和 Iterator 的使用场景和区别
**贯穿案例推进**：为图书管理器添加"列出所有图书"和"删除指定图书"功能
**建议示例文件**：05_iteration_patterns.java
**叙事入口**：需要遍历图书列表打印报表，但遍历时还想删除某些记录——怎么办？
**角色出场**：阿码——"为什么 Iterator 比 for 循环麻烦？什么时候必须用 Iterator？"
**回顾桥**：[for 循环]（week_01 隐含）：对比传统 for 循环和增强 for 的语法差异
-->

现在小北要加一个功能：打印图书馆的所有藏书清单。他已经知道可以用索引遍历：

```java
for (int i = 0; i < books.size(); i++) {
    Book book = books.get(i);
    System.out.println(book);
}
```

但 Java 提供了更简洁的写法——**增强 for 循环**（enhanced for loop，也叫 for-each）：

```java
for (Book book : books) {
    System.out.println(book);
}
```

读作"对于 books 中的每一个 Book 类型的 book"。不需要索引变量，不需要 `get(i)`，代码干净多了。

阿码在旁边看着："这和 Python 的 `for book in books` 差不多嘛。但如果我想在遍历的时候删除某些书呢？比如把作者叫 '佚名' 的书都删掉。"

小北试着写：

```java
for (Book book : books) {
    if ("佚名".equals(book.getAuthor())) {
        books.remove(book);  // ❌ 危险！
    }
}
```

运行后抛出异常：

```
java.util.ConcurrentModificationException
```

"并发修改异常？我又没写多线程代码……"

问题在于：增强 for 循环内部在用 **Iterator**（迭代器）遍历，而直接调用 `remove()` 破坏了迭代器的状态。正确的做法是显式使用 Iterator：

```java
Iterator<Book> iterator = books.iterator();
while (iterator.hasNext()) {
    Book book = iterator.next();
    if ("佚名".equals(book.getAuthor())) {
        iterator.remove();  // ✅ 用迭代器的 remove，安全
    }
}
```

`Iterator` 提供三个核心方法：`hasNext()` 检查还有没有下一个元素，`next()` 取出下一个元素并前移指针，`remove()` 删除当前元素（必须在 `next()` 之后调用）。

"为什么 Iterator 比 for 循环麻烦？什么时候必须用 Iterator？"阿码追问。

答案是：**遍历时需要删除元素，必须用 Iterator**。如果只是读取，增强 for 更简洁；如果需要索引位置，用传统 for。

小北给图书馆加了一个清理功能：

```java
public void removeAnonymousBooks() {
    Iterator<Book> it = books.iterator();
    while (it.hasNext()) {
        Book book = it.next();
        if ("佚名".equals(book.getAuthor())) {
            it.remove();
            isbnIndex.remove(book.getIsbn());  // 同步删除索引
        }
    }
}
```

注意：他同时维护了 `books` 列表和 `isbnIndex` 映射，删除时要两边都删，否则数据会不一致。

---

## 6. 渐进式构建——从简单到完整

<!--
**Bloom 层次**：创造
**学习目标**：综合运用集合框架设计小型数据存储系统
**贯穿案例推进**：分两步完成图书借阅追踪器：版本1（基础）→ 版本2（完善）
**建议示例文件**：06_complete_tracker.java
**叙事入口**：先实现核心功能跑起来，再逐步添加健壮性
**角色出场**：老潘——"这就是生产代码里 Repository 层的雏形，注意封装和异常处理"
**回顾桥**：[防御式编程]（week_03）：在数据层添加参数校验和异常处理
-->

现在把前面学的全部组合起来。但这一次，我们不一次性写出完美代码，而是**渐进式构建**——先让核心功能跑起来，再逐步完善。

### 版本 1：基础骨架

先实现最简单的功能：加书、查书。

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LibraryTrackerV1 {
    private HashMap<String, Book> booksByIsbn;  // ISBN → 图书

    public LibraryTrackerV1() {
        booksByIsbn = new HashMap<>();
    }

    public void addBook(Book book) {
        booksByIsbn.put(book.getIsbn(), book);
    }

    public Book findByIsbn(String isbn) {
        return booksByIsbn.get(isbn);
    }

    public List<Book> listAllBooks() {
        return new ArrayList<>(booksByIsbn.values());
    }
}
```

小北测试了一下，能加书、能查询，基本功能正常。但老潘看了一眼："如果传入 `null` 呢？如果 ISBN 是空字符串呢？"

果然，程序在边缘情况下会崩溃。

### 版本 2：增加健壮性

现在加上借阅管理和防御式编程：

```java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BorrowRecord {
    private String isbn;
    private String borrower;
    private LocalDate borrowDate;

    public BorrowRecord(String isbn, String borrower) {
        this.isbn = isbn;
        this.borrower = borrower;
        this.borrowDate = LocalDate.now();
    }

    public String getIsbn() { return isbn; }
    public String getBorrower() { return borrower; }
}

public class LibraryTracker {
    private HashMap<String, Book> booksByIsbn;
    private ArrayList<BorrowRecord> borrowRecords;

    public LibraryTracker() {
        booksByIsbn = new HashMap<>();
        borrowRecords = new ArrayList<>();
    }

    // 图书管理（带参数校验）
    public void addBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("图书信息不完整");
        }
        booksByIsbn.put(book.getIsbn(), book);
    }

    public Book findBook(String isbn) {
        return booksByIsbn.get(isbn);
    }

    // 借阅管理（防御式编程）
    public void borrowBook(String isbn, String borrower) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }
        if (borrower == null || borrower.isEmpty()) {
            throw new IllegalArgumentException("借阅人不能为空");
        }
        if (!booksByIsbn.containsKey(isbn)) {
            throw new IllegalArgumentException("图书不存在：" + isbn);
        }
        borrowRecords.add(new BorrowRecord(isbn, borrower));
    }

    // 归还（使用 Iterator 安全删除）
    public void returnBook(String isbn, String borrower) {
        Iterator<BorrowRecord> it = borrowRecords.iterator();
        while (it.hasNext()) {
            BorrowRecord record = it.next();
            if (record.getIsbn().equals(isbn) && record.getBorrower().equals(borrower)) {
                it.remove();
                return;
            }
        }
        throw new IllegalStateException("未找到对应的借阅记录");
    }
}
```

老潘点点头："这就是生产代码里 **Repository 层**的雏形。注意三个细节：

第一，**参数校验**。`borrowBook` 里检查了 null 和空字符串——这是 Week 03 学的**防御式编程**。第二，**返回副本**。`listAllBooks` 返回的是 `new ArrayList<>(...)`，防止外部直接修改内部数据。第三，**Iterator 删除**。遍历时删除必须用迭代器，否则抛 `ConcurrentModificationException`。

"从版本 1 到版本 2，代码量翻倍了，但健壮性也翻倍了。真实开发就是这样——先跑起来，再慢慢加固。"

---

## CampusFlow 进度

<!--
CampusFlow 本周推进：
- 上周状态：团队协作流程建立，ADR-002（数据存储方案决策）完成
- 本周改进：使用集合重构数据存储层，从数组升级到 ArrayList/HashMap 组合
- 涉及的本周概念：ArrayList、HashMap、泛型、迭代器
- 具体落盘：
  1. 创建 Repository 层类（如 TaskRepository、UserRepository）
  2. 使用 ArrayList 存储实体对象
  3. 使用 HashMap 建立 ID 到对象的快速查找索引
  4. 添加基本的增删改查方法
- 建议示例文件：examples/CampusFlowRepository.java
-->

到目前为止，CampusFlow 的数据都存在固定大小的数组里——或者更糟，每次重启数据就消失。本周我们用集合框架给它搭建一个像样的内存存储层。

以 TaskRepository 为例：

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TaskRepository {
    private final HashMap<String, Task> tasksById;
    private final ArrayList<Task> taskList;

    public TaskRepository() {
        this.tasksById = new HashMap<>();
        this.taskList = new ArrayList<>();
    }

    public void save(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task 或 ID 不能为空");
        }
        tasksById.put(task.getId(), task);

        // 如果列表中已存在，先删除旧引用
        taskList.removeIf(t -> t.getId().equals(task.getId()));
        taskList.add(task);
    }

    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasksById.get(id));
    }

    public List<Task> findAll() {
        return new ArrayList<>(taskList);  // 返回副本
    }

    public void delete(String id) {
        Task removed = tasksById.remove(id);
        if (removed != null) {
            taskList.remove(removed);
        }
    }
}
```

这里用了 `Optional<Task>` 作为返回值——这是 Java 8 引入的容器类型，表示"可能有，也可能没有"，避免返回 null 导致调用方忘记检查。

老潘点评："注意 `save` 方法里的 `removeIf`——这是 Java 8 在 `ArrayList` 上加的便利方法，内部用 Iterator 安全删除。你不需要自己写 Iterator 循环了。"

现在 CampusFlow 有了真正的数据层，虽然数据还是存在内存里（重启会丢失），但结构已经清晰——下周我们把它持久化到数据库。

---

## Git 本周要点

本周 Git 的重点是**忽略不需要跟踪的文件**。当你用 Maven 构建项目时，会生成 `target/` 目录；如果你用 IntelliJ IDEA，会有 `.idea/` 目录。这些都不应该提交到 Git。

在项目根目录创建 `.gitignore` 文件：

```gitignore
# 编译输出
target/
*.class

# IDE 配置
.idea/
*.iml
.vscode/

# 依赖（Maven 会自动下载）
# 但保留 pom.xml！
```

然后执行：

```bash
git add .gitignore
git commit -m "chore: 添加 .gitignore 忽略构建输出"
```

常见坑：忘记 `add` 新创建的 `.gitignore` 文件，或者把 `target/` 里的 `.class` 文件也提交了。记住：**只提交源代码和配置文件，不提交生成的文件**。

---

## 本周小结（供下周参考）

本周你学会了用 Java 集合框架管理数据。`ArrayList` 让存储从"固定大小"变成"弹性扩展"，`HashMap` 让查找从"逐个扫描"变成"瞬间定位"，泛型让类型错误从"运行期崩溃"变成"编译期拦截"。

关键对比记住这三组：

- **ArrayList vs 数组**：前者自动扩容、功能丰富，后者性能略优但大小固定
- **HashMap vs 线性搜索**：前者 O(1) 查找但需要额外内存，后者简单但慢
- **泛型 vs 原始类型**：前者编译期安全，后者运行期埋雷

小北的金句："原来 Java 的严格是为了让我少加班。"

但还有一个问题没解决：数据存在内存里，程序重启就全没了。下周我们要引入**持久化**——让数据真正"存"下来。

---

## Definition of Done（学生自测清单）

- [ ] 我能解释数组和 ArrayList 的 3 个核心差异（大小、类型、功能）
- [ ] 我能写出使用 HashMap 进行增删改查的代码
- [ ] 我能在代码中正确使用泛型，不使用原始类型
- [ ] 我能用增强 for 循环遍历集合，并在需要时使用 Iterator
- [ ] 我的图书借阅追踪器可以添加、查询、删除图书且不崩溃
- [ ] 我的 CampusFlow 项目已使用集合重构了数据存储层
- [ ] 我能在代码审查时识别 AI 生成的集合代码中的类型安全问题
