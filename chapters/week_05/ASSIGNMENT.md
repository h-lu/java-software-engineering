# Week 05 作业：集合与泛型实践

> "选择正确的数据结构，比优化算法更重要。"
> —— 老潘

---

## 作业概述

本周作业分为三层：基础、进阶、挑战。你至少需要完成基础题才能通过本周考核。进阶题和挑战题是可选的，但强烈推荐尝试——它们能帮你真正掌握"在正确场景选择正确容器"的能力。

| 项目 | 说明 |
|------|------|
| 预估耗时 | 6-8 小时（基础题 3-4 小时，进阶/挑战 3-4 小时） |
| 截止日期 | 本周日 23:59 |
| 提交方式 | Git 仓库链接 + 测试运行截图 |

---

## 学习目标

完成本周作业后，你将能够：

1. 解释数组与 ArrayList 的核心差异，并在合适场景选择正确的容器
2. 使用 HashMap 实现基于键的高效查找（O(1) 时间复杂度）
3. 运用泛型确保集合的类型安全，避免 ClassCastException
4. 使用增强 for 循环和迭代器优雅地遍历集合
5. 识别并修复集合操作中的常见边界错误

---

## 基础题（必做）

### 任务 1：完善图书借阅追踪器（基础）

**场景**：小北按照 CHAPTER.md 的示例写了一个图书借阅追踪器，但功能还不完整。请帮他完成以下功能。

**任务**：

基于以下代码框架，完成 `LibraryTracker` 类的剩余功能：

```java
// Book.java
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

// BorrowRecord.java
import java.time.LocalDate;

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
    public LocalDate getBorrowDate() { return borrowDate; }
}

// LibraryTracker.java（需要完善的类）
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LibraryTracker {
    private HashMap<String, Book> booksByIsbn;        // 图书库存
    private ArrayList<BorrowRecord> borrowRecords;    // 借阅记录

    public LibraryTracker() {
        booksByIsbn = new HashMap<>();
        borrowRecords = new ArrayList<>();
    }

    // TODO 1: 添加图书（需检查 null 和重复 ISBN）
    public void addBook(Book book) {
        // 你的代码
    }

    // TODO 2: 根据 ISBN 查找图书（O(1) 查找）
    public Book findBook(String isbn) {
        // 你的代码
    }

    // TODO 3: 获取所有图书列表（返回副本，保护内部数据）
    public List<Book> listAllBooks() {
        // 你的代码
    }

    // TODO 4: 借阅图书（需检查图书是否存在、参数合法性）
    public void borrowBook(String isbn, String borrower) {
        // 你的代码
    }

    // TODO 5: 查询某用户的所有借阅记录
    public List<BorrowRecord> getBorrowRecordsByUser(String borrower) {
        // 你的代码
    }

    // TODO 6: 归还图书（使用 Iterator 安全删除）
    public void returnBook(String isbn, String borrower) {
        // 你的代码
    }

    // TODO 7: 删除图书（需同时从库存和借阅记录中处理）
    public void removeBook(String isbn) {
        // 你的代码
    }
}
```

**要求**：

1. **addBook**：检查 book 为 null、isbn 为 null、isbn 是否已存在
2. **findBook**：使用 HashMap 实现 O(1) 查找，图书不存在返回 null
3. **listAllBooks**：返回 `new ArrayList<>(booksByIsbn.values())`，防止外部修改内部数据
4. **borrowBook**：检查 isbn 和 borrower 不为 null/空，图书必须存在
5. **getBorrowRecordsByUser**：遍历 borrowRecords，收集匹配的记录到新列表返回
6. **returnBook**：使用 Iterator 遍历 borrowRecords，安全删除匹配的记录
7. **removeBook**：从 booksByIsbn 删除，同时清理相关的 borrowRecords

**输入/输出示例**：

```java
LibraryTracker tracker = new LibraryTracker();

// 添加图书
tracker.addBook(new Book("Java 核心技术", "Cay Horstmann", "978-111"));
tracker.addBook(new Book("Effective Java", "Joshua Bloch", "978-222"));

// 列出所有图书
List<Book> books = tracker.listAllBooks();
// 输出：[Java 核心技术 (Cay Horstmann), Effective Java (Joshua Bloch)]

// 查找图书
Book found = tracker.findBook("978-111");
// 输出：Java 核心技术 (Cay Horstmann)

// 借阅图书
tracker.borrowBook("978-111", "小北");
// 成功，无输出

// 查询用户借阅记录
List<BorrowRecord> records = tracker.getBorrowRecordsByUser("小北");
// 输出：[(978-111, 小北, 2026-02-11)]

// 归还图书
tracker.returnBook("978-111", "小北");
// 成功，借阅记录被删除

// 删除图书
tracker.removeBook("978-222");
// 图书从库存中移除
```

**边界情况处理**：

```java
// 添加 null 图书
tracker.addBook(null);
// 抛出：IllegalArgumentException: 图书不能为 null

// 添加重复 ISBN
tracker.addBook(new Book("书1", "作者1", "978-111"));
tracker.addBook(new Book("书2", "作者2", "978-111"));
// 抛出：IllegalArgumentException: ISBN 978-111 已存在

// 借阅不存在的图书
tracker.borrowBook("999", "小北");
// 抛出：IllegalArgumentException: 图书不存在：999

// 归还未借阅的图书
tracker.returnBook("978-111", "小北");
// 抛出：IllegalStateException: 未找到对应的借阅记录
```

**提示**：
- 使用泛型确保类型安全：`ArrayList<Book>` 而不是 `ArrayList`
- 返回集合时返回副本，保护内部数据不被外部修改
- 使用 Iterator 的 `remove()` 方法在遍历时安全删除元素

---

### 任务 2：实现泛型工具类

**场景**：阿码发现项目中经常需要对集合进行各种转换操作，他想写一些通用的工具方法。

**任务**：

实现以下泛型工具类：

```java
// CollectionUtils.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

    // TODO 1: 将 List 中的元素按某个条件分组
    // 示例：按图书作者分组 Map<String, List<Book>>
    public static <T, K> Map<K, List<T>> groupBy(List<T> list, KeyExtractor<T, K> extractor) {
        // 你的代码
    }

    // TODO 2: 过滤列表中的元素
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        // 你的代码
    }

    // TODO 3: 查找第一个匹配的元素
    public static <T> T findFirst(List<T> list, Predicate<T> predicate) {
        // 你的代码
    }

    // 函数式接口定义
    @FunctionalInterface
    public interface KeyExtractor<T, K> {
        K extract(T item);
    }

    @FunctionalInterface
    public interface Predicate<T> {
        boolean test(T item);
    }
}
```

**要求**：

1. **groupBy**：遍历列表，使用 extractor 提取 key，将相同 key 的元素放入同一列表
2. **filter**：遍历列表，只保留满足 predicate 的元素
3. **findFirst**：遍历列表，返回第一个满足 predicate 的元素，没有则返回 null
4. 所有方法都要处理 list 为 null 的情况（返回空集合或 null）

**输入/输出示例**：

```java
List<Book> books = new ArrayList<>();
books.add(new Book("Java 核心技术", "Cay", "111"));
books.add(new Book("Effective Java", "Joshua", "222"));
books.add(new Book("Java 并发编程", "Cay", "333"));

// 按作者分组
Map<String, List<Book>> byAuthor = CollectionUtils.groupBy(books, book -> book.getAuthor());
// byAuthor.get("Cay") -> [Java 核心技术, Java 并发编程]
// byAuthor.get("Joshua") -> [Effective Java]

// 过滤：找出作者为 Cay 的图书
List<Book> cayBooks = CollectionUtils.filter(books, book -> "Cay".equals(book.getAuthor()));
// 输出：[Java 核心技术, Java 并发编程]

// 查找：找 ISBN 为 222 的图书
Book found = CollectionUtils.findFirst(books, book -> "222".equals(book.getIsbn()));
// 输出：Effective Java (Joshua)

// 查找不存在的
Book notFound = CollectionUtils.findFirst(books, book -> "999".equals(book.getIsbn()));
// 输出：null
```

**提示**：
- 使用泛型 `<T, K>` 让方法适用于任何类型
- 返回空集合而不是 null，避免调用方出现 NullPointerException
- 考虑使用 `HashMap<K, ArrayList<T>>` 作为分组结果的内部结构

---

### 任务 3：边界情况与类型安全测试

**场景**：老潘说："生产环境里，集合操作最容易出现空指针和类型转换错误。"

**任务**：

为任务 1 和任务 2 的代码编写完整的边界情况测试：

**测试场景清单**：

| 测试目标 | 测试场景 | 期望结果 |
|---------|---------|---------|
| addBook | 传入 null | 抛出 IllegalArgumentException |
| addBook | ISBN 已存在 | 抛出 IllegalArgumentException |
| findBook | ISBN 不存在 | 返回 null |
| listAllBooks | 修改返回的列表 | 不影响内部数据 |
| borrowBook | borrower 为空字符串 | 抛出 IllegalArgumentException |
| returnBook | 记录不存在 | 抛出 IllegalStateException |
| groupBy | 传入 null 列表 | 返回空 Map |
| filter | 没有匹配元素 | 返回空 List |
| findFirst | 传入 null 列表 | 返回 null |

**测试代码示例**：

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTrackerTest {

    @Test
    public void testAddNullBook() {
        LibraryTracker tracker = new LibraryTracker();
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tracker.addBook(null)
        );
        assertEquals("图书不能为 null", exception.getMessage());
    }

    @Test
    public void testListAllBooksReturnsCopy() {
        LibraryTracker tracker = new LibraryTracker();
        tracker.addBook(new Book("Test", "Author", "123"));

        List<Book> books = tracker.listAllBooks();
        books.clear();  // 清空返回的列表

        // 内部数据不应被影响
        assertEquals(1, tracker.listAllBooks().size());
    }

    // 更多测试...
}
```

**要求**：

1. 为每个边界情况编写至少一个测试方法
2. 使用 `assertThrows` 验证异常类型和消息
3. 验证返回的集合是副本（修改返回的集合不影响内部数据）
4. 测试 null 参数的处理

---

## 进阶题（选做）

### 任务 4：CampusFlow 数据层重构

**场景**：CampusFlow 目前使用固定大小的数组存储任务，现在需要重构为使用集合框架。

**任务**：

使用 ArrayList 和 HashMap 重构 CampusFlow 的数据存储层：

```java
// TaskRepository.java
import java.util.*;

public class TaskRepository {
    private final Map<String, Task> tasksById;      // ID 快速查找
    private final List<Task> taskList;               // 保持插入顺序

    public TaskRepository() {
        // 你的代码
    }

    // TODO 1: 保存任务（新增或更新）
    public void save(Task task) {
        // 你的代码
    }

    // TODO 2: 根据 ID 查找（使用 Optional）
    public Optional<Task> findById(String id) {
        // 你的代码
    }

    // TODO 3: 获取所有任务（返回不可修改的副本）
    public List<Task> findAll() {
        // 你的代码
    }

    // TODO 4: 删除任务
    public void delete(String id) {
        // 你的代码
    }

    // TODO 5: 按状态筛选任务
    public List<Task> findByStatus(TaskStatus status) {
        // 你的代码
    }

    // TODO 6: 统计各状态任务数量（使用 Map）
    public Map<TaskStatus, Integer> countByStatus() {
        // 你的代码
    }
}
```

**要求**：

1. **save**：如果 task 为 null 或 id 为 null 抛出异常；如果已存在则更新，否则添加
2. **findById**：返回 `Optional<Task>`，避免调用方处理 null
3. **findAll**：返回 `Collections.unmodifiableList(new ArrayList<>(taskList))`
4. **delete**：从两个数据结构中同时删除
5. **findByStatus**：遍历 taskList，返回匹配状态的任务列表
6. **countByStatus**：遍历 taskList，统计各状态数量到 Map

**输入/输出示例**：

```java
TaskRepository repo = new TaskRepository();

// 保存任务
repo.save(new Task("1", "买牛奶", TaskStatus.PENDING));
repo.save(new Task("2", "写作业", TaskStatus.IN_PROGRESS));
repo.save(new Task("3", "去健身房", TaskStatus.PENDING));

// 查找任务
Optional<Task> task = repo.findById("1");
task.ifPresent(t -> System.out.println(t.getTitle()));
// 输出：买牛奶

// 获取所有任务
List<Task> all = repo.findAll();
// 输出：[买牛奶, 写作业, 去健身房]

// 按状态筛选
List<Task> pending = repo.findByStatus(TaskStatus.PENDING);
// 输出：[买牛奶, 去健身房]

// 统计数量
Map<TaskStatus, Integer> counts = repo.countByStatus();
// 输出：{PENDING=2, IN_PROGRESS=1, COMPLETED=0}
```

**提示**：
- 使用 `Optional.ofNullable()` 包装可能为 null 的返回值
- 使用 `Collections.unmodifiableList()` 返回只读视图，防止外部修改
- 保持 tasksById 和 taskList 的数据一致性

---

### 任务 5：实现迭代器模式

**场景**：阿码想理解 Iterator 的工作原理，他想自己实现一个自定义迭代器。

**任务**：

实现一个支持过滤功能的自定义迭代器：

```java
// FilteringIterator.java
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteringIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final Predicate<T> predicate;
    private T nextElement;
    private boolean hasNext;

    public FilteringIterator(Iterator<T> source, Predicate<T> predicate) {
        this.source = source;
        this.predicate = predicate;
        advance();  // 找到第一个匹配的元素
    }

    private void advance() {
        // TODO: 遍历 source，找到下一个满足 predicate 的元素
    }

    @Override
    public boolean hasNext() {
        // TODO
    }

    @Override
    public T next() {
        // TODO: 返回当前元素，并 advance 到下一个
    }

    @FunctionalInterface
    public interface Predicate<T> {
        boolean test(T item);
    }
}
```

**要求**：

1. **advance**：遍历 source，找到下一个满足 predicate 的元素，保存到 nextElement
2. **hasNext**：返回 hasNext 标志
3. **next**：返回 nextElement，然后调用 advance 准备下一个元素；如果没有元素则抛出 NoSuchElementException

**输入/输出示例**：

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 只遍历偶数
FilteringIterator<Integer> evenIterator = new FilteringIterator<>(
    numbers.iterator(),
    n -> n % 2 == 0
);

while (evenIterator.hasNext()) {
    System.out.print(evenIterator.next() + " ");
}
// 输出：2 4 6 8 10
```

**提示**：
- 使用 "look-ahead" 策略：在 hasNext() 被调用前就找到下一个有效元素
- next() 方法需要先检查是否有元素，然后返回当前元素并 advance

---

## 挑战题（可选）

### 任务 6：性能对比分析

**场景**：老潘问："ArrayList 和 LinkedList 有什么区别？HashMap 在什么情况下会退化成 O(n)？"

**任务**：

编写一个性能测试程序，对比不同集合操作的性能：

```java
// CollectionPerformanceTest.java
import java.util.*;

public class CollectionPerformanceTest {

    public static void main(String[] args) {
        int n = 100000;

        // TODO 1: 对比 ArrayList vs LinkedList 的随机访问性能
        testRandomAccess(n);

        // TODO 2: 对比 ArrayList vs LinkedList 的头部插入性能
        testInsertAtHead(n);

        // TODO 3: 对比 HashMap vs 线性搜索的查找性能
        testLookup(n);

        // TODO 4: 分析 HashMap 在哈希冲突严重时的性能退化
        testHashCollision(n);
    }

    // 实现上述测试方法...
}
```

**要求**：

1. **testRandomAccess**：对比 ArrayList 和 LinkedList 随机访问第 n/2 个元素 10000 次的时间
2. **testInsertAtHead**：对比 ArrayList 和 LinkedList 在头部插入 10000 个元素的时间
3. **testLookup**：对比 HashMap 查找和 ArrayList 线性搜索 10000 次的时间
4. **testHashCollision**：创建一个所有元素哈希码都相同的 HashMap，测试其查找性能

**输出示例**：

```
=== 随机访问性能测试（10000 次）===
ArrayList: 5 ms
LinkedList: 850 ms
ArrayList 快 170 倍

=== 头部插入性能测试（10000 次）===
ArrayList: 120 ms
LinkedList: 3 ms
LinkedList 快 40 倍

=== 查找性能测试（10000 次）===
HashMap: 2 ms
ArrayList 线性搜索: 450 ms
HashMap 快 225 倍

=== HashMap 哈希冲突测试 ===
正常 HashMap 查找: 2 ms
冲突严重 HashMap 查找: 150 ms
性能退化约 75 倍
```

**提示**：
- 使用 `System.currentTimeMillis()` 或 `System.nanoTime()` 计时
- 预热 JVM（先运行几次再正式计时）
- 创建一个哈希冲突严重的类：重写 `hashCode()` 返回常量

---

### 任务 7：设计文档

**场景**：老潘要求团队为 CampusFlow 的数据层设计编写 ADR（架构决策记录）。

**任务**：

编写 ADR-003：数据存储方案决策，说明为什么选择 ArrayList + HashMap 的组合。

**ADR 模板**：

```markdown
# ADR-003: 数据存储方案决策

## 背景

CampusFlow 需要一种内存数据存储方案，支持：
- 快速查找（通过 ID）
- 遍历所有记录
- 按条件筛选

## 决策

选择 ArrayList + HashMap 组合存储方案。

## 考虑过的选项

| 方案 | 优点 | 缺点 |
|------|------|------|
| 纯 ArrayList | 内存占用小，遍历快 | 查找慢 O(n) |
| 纯 HashMap | 查找快 O(1) | 遍历顺序不确定 |
| ArrayList + HashMap | 查找快，遍历有序 | 内存占用稍大，需维护一致性 |
| 数组 | 最简单 | 大小固定，扩容麻烦 |

## 决策理由

1. ...
2. ...

## 权衡

- 内存 vs 速度：...
- 复杂度 vs 性能：...

## 实施计划

1. 创建 TaskRepository 类
2. 使用 HashMap<String, Task> 存储 ID -> Task 映射
3. 使用 ArrayList<Task> 保持插入顺序
4. 所有修改操作同步更新两个数据结构
```

**要求**：

1. 至少对比 3 种方案
2. 说明每种方案的时间复杂度（查找、插入、删除、遍历）
3. 解释为什么选择最终方案
4. 说明潜在风险和缓解措施

---

## AI 协作练习（可选）

根据 AI 融合路径，Week 05 属于"系统化工程"阶段，你可以使用 AI 辅助完成以下任务，并提交审查报告。

### 练习：AI 生成泛型代码的审查

下面这段代码是某个 AI 工具生成的泛型缓存工具类：

```java
import java.util.HashMap;
import java.util.Map;

public class SimpleCache<K, V> {
    private Map<K, V> cache = new HashMap();

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void clear() {
        cache.clear();
    }

    public List<V> getAllValues() {
        return new ArrayList(cache.values());
    }
}
```

**请审查这段代码**：

- [ ] 代码能编译吗？有没有原始类型（raw type）问题？
- [ ] 变量命名清晰吗？
- [ ] 有没有缺少错误处理的地方？（如 null key）
- [ ] 边界情况处理了吗？（如 key 不存在时的 get）
- [ ] 返回的集合是安全的吗？
- [ ] 你能写一个让它失败的测试吗？

**提交**：修复后的代码 + 你发现了哪些问题的简短说明。

---

## 提交要求

### 必交内容（基础题）

1. **源代码**：
   - `src/main/java/edu/campusflow/library/Book.java`
   - `src/main/java/edu/campusflow/library/BorrowRecord.java`
   - `src/main/java/edu/campusflow/library/LibraryTracker.java`（任务 1）
   - `src/main/java/edu/campusflow/util/CollectionUtils.java`（任务 2）

2. **测试代码**：
   - `src/test/java/edu/campusflow/library/LibraryTrackerTest.java`（任务 3）
   - `src/test/java/edu/campusflow/util/CollectionUtilsTest.java`（任务 3）

3. **测试运行截图**：显示所有测试通过

### 选交内容（进阶/挑战）

1. **进阶题代码**：
   - `src/main/java/edu/campusflow/repository/TaskRepository.java`（任务 4）
   - `src/main/java/edu/campusflow/util/FilteringIterator.java`（任务 5）

2. **挑战题**：
   - `src/test/java/edu/campusflow/performance/CollectionPerformanceTest.java`（任务 6）
   - `docs/adr/ADR-003-data-storage.md`（任务 7）

3. **AI 协作练习**（如完成）：
   - `docs/ai-review-cache.md`

### 提交格式

```bash
# Git 提交
git add src/main/java/edu/campusflow/library/
git add src/main/java/edu/campusflow/util/
git add src/test/java/edu/campusflow/
git commit -m "feat: 完成 Week 05 基础题 - 集合与泛型实践"

# 如果做了进阶题
git add src/main/java/edu/campusflow/repository/
git commit -m "feat: 完成 Week 05 进阶题 - 数据层重构"

# 如果做了挑战题
git add docs/adr/
git commit -m "docs: 添加 ADR-003 数据存储方案决策"
```

---

## 常见错误与避坑指南

### 错误 1：使用原始类型（Raw Types）

**问题**：忘记写类型参数，失去类型安全

```java
// ❌ 不好的做法
ArrayList books = new ArrayList();  // 原始类型！
books.add("我不是书");  // 编译通过，运行时才报错
Book book = (Book) books.get(0);  // ClassCastException

// ✅ 好的做法
ArrayList<Book> books = new ArrayList<>();  // 泛型
books.add("我不是书");  // 编译错误！
```

---

### 错误 2：返回内部集合的引用

**问题**：外部代码可以修改内部数据

```java
// ❌ 不好的做法
public List<Book> getBooks() {
    return books;  // 返回内部引用
}

// 调用方可以破坏内部状态
library.getBooks().clear();  // 内部数据被清空了！

// ✅ 好的做法
public List<Book> getBooks() {
    return new ArrayList<>(books);  // 返回副本
}
```

---

### 错误 3：遍历时直接删除元素

**问题**：使用增强 for 循环遍历时删除元素会抛出 ConcurrentModificationException

```java
// ❌ 不好的做法
for (Book book : books) {
    if (book.getAuthor().equals("佚名")) {
        books.remove(book);  // 抛出 ConcurrentModificationException
    }
}

// ✅ 好的做法：使用 Iterator
Iterator<Book> it = books.iterator();
while (it.hasNext()) {
    Book book = it.next();
    if (book.getAuthor().equals("佚名")) {
        it.remove();  // 安全删除
    }
}

// ✅ 或者使用 removeIf（Java 8+）
books.removeIf(book -> book.getAuthor().equals("佚名"));
```

---

### 错误 4：忽略 null 检查

**问题**：集合方法通常不接受 null，或者需要特殊处理

```java
// ❌ 不好的做法
public void addBook(Book book) {
    books.add(book);  // 如果 book 为 null，后续操作可能出错
}

// ✅ 好的做法
public void addBook(Book book) {
    if (book == null) {
        throw new IllegalArgumentException("图书不能为 null");
    }
    if (book.getIsbn() == null) {
        throw new IllegalArgumentException("ISBN 不能为 null");
    }
    books.add(book);
}
```

---

### 错误 5：用错误的数据结构

**问题**：在需要快速查找的场景使用 List

```java
// ❌ 不好的做法：在 10000 个元素中查找
List<Book> books = new ArrayList<>();
// ... 添加 10000 本书
for (Book book : books) {  // O(n) 线性搜索
    if (book.getIsbn().equals(target)) {
        return book;
    }
}

// ✅ 好的做法：使用 HashMap
Map<String, Book> bookMap = new HashMap<>();
// ... 添加 10000 本书
Book book = bookMap.get(target);  // O(1) 直接查找
```

---

## 评分标准

详细评分标准见 `RUBRIC.md`。

简要说明：
- **基础题**：60 分 - 完成任务 1-3，集合使用正确，泛型使用规范
- **进阶题**：+20 分 - 完成任务 4 或 5，Repository 设计合理
- **挑战题**：+20 分 - 完成任务 6 或 7，性能测试或设计文档有深度

---

## 学习资源

- **本周章节**：`chapters/week_05/CHAPTER.md`
- **起步代码**：`starter_code/src/main/java/`（如果你遇到困难）
- **风格指南**：`shared/style_guide.md`
- **Java 集合框架文档**：https://docs.oracle.com/javase/8/docs/technotes/guides/collections/

---

## 起步代码使用说明

如果你遇到困难，可以参考 `starter_code/` 目录中的起步代码：

```bash
# 起步代码位置
starter_code/
├── src/main/java/edu/campusflow/library/       # 图书追踪器相关类
├── src/main/java/edu/campusflow/util/          # 工具类
└── src/main/java/edu/campusflow/repository/    # Repository 层模板
```

**使用建议**：
1. 先自己尝试完成任务
2. 遇到困难时参考起步代码的**思路**，不要直接复制
3. 起步代码可能故意留有一些问题，你需要发现并修复

---

## 截止日期

**提交截止**：本周日 23:59

**延迟提交**：每晚一天扣 10% 分数，最多延迟 3 天
