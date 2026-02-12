# Week 06：用测试守护代码质量

> "未测试的代码就是未完成的代码。"
> —— 软件工程格言

想象一下，你正在维护一个图书馆管理系统。某天你优化了图书查询逻辑，自以为只是"小改动"，结果上线后用户反馈"借书记录全乱了"。你花了整个周末排查，最后发现——新代码在边界情况下覆盖了原来的借阅状态。

这不是技术能力不足，而是缺乏**安全网**。在 2026 年的今天，AI 可以帮你生成代码，但无法替你承担线上事故的责任。测试，就是工程师为自己编织的安全网。

根据 World Quality Report 2025-26 报告显示，拥有成熟自动化测试体系的组织，其关键缺陷进入生产环境的比例降低了 30-35%。但测试的价值不止于"防故障"——它强迫你思考边界情况，让你的设计更健壮。本周，你将学会用 JUnit 5 为代码构建这道防线。

<!--
贯穿案例设计：【图书借阅追踪器的测试守护】
- 第 1 节（为什么需要测试）：从 Week 05 的图书借阅追踪器开始，发现手动验证的痛苦，引出自动化测试的必要性
- 第 2 节（JUnit 基础）：编写第一个测试，用 @Test 和断言验证图书添加功能
- 第 3 节（测试生命周期）：用 @BeforeEach 重构测试，消除重复代码，测试借阅/归还流程
- 第 4 节（异常测试）：用 assertThrows 验证防御式编程的异常抛出
- 第 5 节（参数化测试）：用 @ParameterizedTest 批量测试多种边界情况
- 第 6 节（测试覆盖率）：理解覆盖率概念，识别测试盲区
最终成果：一个拥有完整单元测试覆盖的图书借阅追踪器，测试覆盖率 80%+
-->

<!--
认知负荷预算检查：
- 本周新概念（6 个，系统化工程阶段上限 6 个）：
  1. JUnit 5 基础（@Test, 断言方法）
  2. 测试生命周期（@BeforeEach, @AfterEach）
  3. 异常测试（assertThrows）
  4. 参数化测试（@ParameterizedTest）
  5. 测试覆盖率概念（行覆盖、分支覆盖）
  6. TDD 基本流程（红-绿-重构）
- 结论：✅ 在预算内

回顾桥设计（至少 2 个）：
- [ArrayList/HashMap]（来自 week_05）：在第 2-3 节，测试 Repository 层的集合操作
- [防御式编程]（来自 week_03）：在第 4 节，测试异常抛出是否符合预期
- [Repository 模式]（来自 week_05）：贯穿全部测试案例，测试 Repository 的各种方法
-->

---

## 前情提要

上周你学会了用 Java 集合框架管理数据——`ArrayList` 让存储弹性扩展，`HashMap` 让查找瞬间完成。你的图书借阅追踪器已经能动态管理图书、快速查询、优雅遍历。

但有一个问题始终存在：你怎么知道代码是对的？目前你可能靠"手动运行、肉眼观察"来验证——添加一本书，看看能不能查到；借出一本书，看看状态变没变。这种验证方式不仅枯燥，而且不可重复。每次修改代码后，你都要重新手动验证所有功能，稍有不慎就会遗漏。

本周我们要解决这个问题——用 JUnit 5 搭建自动化测试体系，让机器帮你验证代码的正确性。

---

## 学习目标

完成本周学习后，你将能够：

1. 解释单元测试的价值，区分手动验证与自动化测试的差异
2. 使用 JUnit 5 编写基本测试（@Test, 断言方法）
3. 运用测试生命周期注解（@BeforeEach, @AfterEach）消除测试重复代码
4. 使用 assertThrows 验证异常抛出行为
5. 使用 @ParameterizedTest 批量测试多组边界情况
6. 解读测试覆盖率报告，识别测试盲区

---

## 1. 手动验证的困境

<!--
**Bloom 层次**：理解
**学习目标**：理解手动验证的局限性，感受自动化测试的必要性
**贯穿案例推进**：从手动验证图书借阅追踪器开始，体验"改一行代码，测全部功能"的痛苦
**建议示例文件**：01_manual_testing_pain.java
**叙事入口**：小北修改了一个方法，自以为没问题，结果破坏了其他功能
**角色出场**：小北——"我就改了一行代码，怎么借书功能就坏了？"
**回顾桥**：[Repository 模式]（week_05）：测试对象就是上周完成的 Repository 层
-->

小北觉得上周写的 `LibraryTracker` 有个地方可以优化。`findBook` 方法在找不到书时返回 `null`，这会让调用方不得不写一堆空检查。他想改成返回 `Optional<Book>`，这样更"Java 8"。

"就改这一个方法，应该没问题。"小北自信满满地改了代码，编译通过，运行也没报错。

但第二天，图书管理员反馈："借书功能坏了，系统说找不到书，但书明明在架子上！"

小北排查了一下午，终于发现——`borrowBook` 方法里调用了 `findBook`，而他在重构时把 `findBook` 的返回值从 `Book` 改成了 `Optional<Book>`，却忘了同步修改 `borrowBook` 里的调用代码。结果 `borrowBook` 以为找到了书（实际上是一个 `Optional` 对象），但后续的借阅记录逻辑全错了。

"我就改了一行代码，怎么借书功能就坏了？"小北盯着屏幕，一脸沮丧。

这就是**手动验证**的困境。小北只测试了"修改的方法本身"，没测试"所有调用这个方法的地方"。在真实的项目中，一个方法可能被几十个地方调用，手动验证所有场景几乎不可能。

更糟糕的是，这个问题本可以在编译期发现——如果小北有测试覆盖 `borrowBook`，测试会在他重构后立即失败，提醒他哪里出了问题。

自动化测试的价值就在于此：**它是一张安全网，让你在修改代码时知道是否破坏了现有功能**。每次运行测试，都是在验证"所有我之前认为正确的行为，现在依然正确"。

---

## 2. 第一个测试

<!--
**Bloom 层次**：应用
**学习目标**：掌握 JUnit 5 基本注解和断言方法
**贯穿案例推进**：为 LibraryTracker 编写第一个测试——测试 addBook 方法
**建议示例文件**：02_first_junit_test.java
**叙事入口**：从最简单的测试开始，让读者快速获得"测试通过"的成就感
**角色出场**：阿码——"测试代码也是代码？那谁来测试测试代码？"
**回顾桥**：[ArrayList/HashMap]（week_05）：测试集合操作的正确性
-->

小北决定给 `LibraryTracker` 加上测试。他听说 JUnit 是 Java 世界最流行的测试框架，而 JUnit 5（代号 Jupiter）是最新版本。

他兴冲冲地创建了第一个测试类：

```java
// 文件：src/test/java/LibraryTrackerTest.java
import org.junit.jupiter.api.Test;

public class LibraryTrackerTest {

    @Testing  // 小北手滑写错了
    void shouldAddBookSuccessfully() {
        // ...
    }
}
```

编译报错：`cannot find symbol @Testing`。小北愣了三秒，才反应过来——是 `@Test` 不是 `@Testing`。

"写测试的第一步，就被自己的手残打败了。"小北苦笑着改了过来。

修正后，确保你的 `pom.xml` 包含 JUnit 5 依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

然后，创建第一个测试类：

```java
// 文件：src/test/java/LibraryTrackerTest.java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTrackerTest {

    @Test
    void shouldAddBookSuccessfully() {
        // 准备
        LibraryTracker tracker = new LibraryTracker();
        Book book = new Book("Java 核心技术", "Cay Horstmann", "978-111");

        // 执行
        tracker.addBook(book);

        // 验证
        Book found = tracker.findBook("978-111");
        assertNotNull(found);
        assertEquals("Java 核心技术", found.getTitle());
    }
}
```

运行测试（在 Maven 项目中）：

```bash
mvn test
```

如果一切正常，你会看到：

```
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

阿码在旁边看着，突然问："测试代码也是代码？那谁来测试测试代码？"

好问题。测试代码确实可能写错，但测试代码通常比业务代码简单得多——它只做三件事：准备数据、调用被测方法、验证结果。而且测试代码的"正确性"可以通过"它能否发现已知的 bug"来验证。

更重要的是，**测试代码是业务代码的调用方**。如果测试能跑通，至少说明业务代码的公开 API 是可用的。这比"手动运行 main 方法看一眼输出"要可靠得多。

小北继续写第二个测试——验证上周学的 `HashMap` 集合操作：

```java
@Test
void shouldFindBookByIsbn() {
    LibraryTracker tracker = new LibraryTracker();
    Book book1 = new Book("书1", "作者A", "ISBN-001");
    Book book2 = new Book("书2", "作者B", "ISBN-002");

    tracker.addBook(book1);
    tracker.addBook(book2);

    // 用 HashMap 实现的查找应该是 O(1)
    Book found = tracker.findBook("ISBN-002");
    assertEquals("书2", found.getTitle());
}
```

注意这里的 `assertEquals`——它比较两个值是否相等。如果测试失败，JUnit 会告诉你"期望值是什么，实际值是什么"，这比你自己打印输出要清晰得多。

---

## 3. 消除重复

<!--
**Bloom 层次**：应用
**学习目标**：掌握 @BeforeEach, @AfterEach 的使用场景
**贯穿案例推进**：重构测试代码，用 @BeforeEach 初始化测试数据，测试借阅/归还流程
**建议示例文件**：03_test_lifecycle.java
**叙事入口**：发现多个测试方法都在重复创建相同的测试数据
**角色出场**：老潘——"测试代码也是工程代码，DRY 原则同样适用"
**回顾桥**：[Repository 模式]（week_05）：每个测试方法都需要一个干净的 Repository 实例
-->

小北写了几个测试后，发现每个测试方法的开头都差不多：

```java
LibraryTracker tracker = new LibraryTracker();
Book book = new Book("...", "...", "...");
```

"这重复代码也太多了。"小北皱眉。

老潘路过看了一眼："测试代码也是工程代码，DRY 原则同样适用。用 `@BeforeEach` 吧。"

`@BeforeEach` 是 JUnit 5 提供的**生命周期注解**，它标记的方法会在每个测试方法执行前自动运行。这样你可以把重复的初始化代码抽出来：

```java
// 文件：src/test/java/LibraryTrackerLifecycleTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTrackerLifecycleTest {

    private LibraryTracker tracker;
    private Book testBook;

    @BeforeEach
    void setUp() {
        // 每个测试方法前都会执行
        tracker = new LibraryTracker();
        testBook = new Book("测试书", "测试作者", "TEST-ISBN");
    }

    @Test
    void shouldAddBook() {
        tracker.addBook(testBook);
        assertNotNull(tracker.findBook("TEST-ISBN"));
    }

    @Test
    void shouldBorrowBook() {
        // 借阅流程测试
        tracker.addBook(testBook);
        tracker.borrowBook("TEST-ISBN", "小北");

        // 验证借阅记录存在
        assertTrue(tracker.hasBorrowRecord("TEST-ISBN", "小北"));
    }

    @Test
    void shouldReturnBook() {
        // 归还流程测试
        tracker.addBook(testBook);
        tracker.borrowBook("TEST-ISBN", "小北");
        tracker.returnBook("TEST-ISBN", "小北");

        // 验证借阅记录已清除
        assertFalse(tracker.hasBorrowRecord("TEST-ISBN", "小北"));
    }
}
```

这里的关键是：**每个测试方法都应该从一个干净的状态开始**。如果三个测试方法共享同一个 `tracker` 实例，一个测试添加的书会影响到另一个测试，测试之间就会产生依赖——这是测试的大忌。

`@BeforeEach` 确保每个测试方法执行前都会创建新的 `tracker` 实例，测试之间相互隔离，互不影响。

对应地，还有 `@AfterEach`——在每个测试方法执行后运行，用于清理资源：

```java
@AfterEach
void tearDown() {
    // 清理临时文件、关闭数据库连接等
}
```

对于小北的内存存储实现，`@AfterEach` 不是必须的（因为 `tracker` 实例会被垃圾回收）。但在涉及文件或数据库的测试中，`@AfterEach` 就很重要了。

---

> **AI 时代小专栏：AI 生成测试代码的边界**
>
> 2025 年的研究显示，GitHub Copilot 生成的测试代码有 91.5% 能编译运行（有效性率），但只有 28.7% 真正验证了正确的行为（正确率）。这意味着什么？AI 生成的测试看起来"像那么回事"，但很可能只是在"假装测试"。
>
> 2025 年 6 月的更新确实带来了突破——Copilot 消除了语法错误和幻觉方法，生成的代码都能编译通过。但编译通过不等于测试有效。研究发现，AI 生成的测试覆盖率可达 75%，高于人工编写的 60%，但复杂业务逻辑和边界情况仍需人工干预。
>
> 所以当你用 AI 生成测试时，务必检查：这个测试真的验证了正确行为吗？还是只是让覆盖率数字好看？新加坡政府技术局的报告也指出，AI 编码助手能提升 21-28% 的编码速度，但测试质量仍需人类把关。
>
> 回到你刚学的 JUnit 基础——`assertEquals`、`assertNotNull` 这些断言不是装饰，它们是测试的"灵魂"。AI 可能生成一个调用被测方法的测试，但断言可能写得很弱（比如只检查返回值不为 null）。这时候，你在 Week 03 学的防御式编程思维就派上用场了：问自己，"什么情况下这个测试会通过，但代码其实是错的？"
>
> 参考（访问日期：2026-02-12）：
> - [GitHub Copilot Quality Advancement](https://www.c-sharpcorner.com/article/github-copilot-huge-quality-advancement-in-3-months-june-2025/)
> - [AI Testing Code Research](https://computerfraudsecurity.com/index.php/journal/article/view/784)
> - [Singapore Government AI Coding Assistant Report](https://docs.developer.tech.gov.sg/docs/ai-coding-assistants/training/files/update-on-ship-hats-coding-assistant-programme.pdf)

---

## 4. 测试异常情况

<!--
**Bloom 层次**：应用
**学习目标**：掌握 assertThrows 验证异常抛出
**贯穿案例推进**：测试 borrowBook 方法的参数校验逻辑，验证异常抛出
**建议示例文件**：04_exception_testing.java
**叙事入口**：阿码问"如果传入 null 会怎样？"——这正是测试要覆盖的边界
**角色出场**：阿码——"那如果用户传个 null 进去呢？"; 小北——"让我写个测试看看..."
**回顾桥**：[防御式编程]（week_03）：测试 Week 03 写的防御式代码是否按预期抛出异常
-->

阿码看着小北的测试代码，突然问："那如果用户传个 null 进去呢？"

小北愣了一下。他想起了 Week 03 学的**防御式编程**——`borrowBook` 方法里应该有参数校验，如果传入 null 或空字符串，应该抛出异常。

"让我写个测试看看..."

JUnit 5 提供了 `assertThrows` 来验证异常抛出：

```java
// 文件：src/test/java/LibraryTrackerExceptionTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTrackerExceptionTest {

    private LibraryTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new LibraryTracker();
    }

    @Test
    void shouldThrowExceptionWhenBorrowingWithNullIsbn() {
        tracker.addBook(new Book("书", "作者", "ISBN-1"));

        // 验证调用 borrowBook(null, "小北") 时会抛出 IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook(null, "小北");
        });

        // 还可以验证异常消息
        assertEquals("ISBN 不能为空", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBorrowingNonExistentBook() {
        // 尝试借阅不存在的书
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook("不存在的ISBN", "小北");
        });

        assertTrue(exception.getMessage().contains("图书不存在"));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            tracker.addBook(null);
        });
    }
}
```

`assertThrows` 的用法有点特别——它接收两个参数：期望的异常类型，和一个 Lambda 表达式（要执行的代码）。如果 Lambda 执行时抛出了指定类型的异常，测试通过；如果没有抛出异常，或者抛出了其他类型的异常，测试失败。

这种写法叫**行为验证**：你不是在验证返回值，而是在验证"当发生某种情况时，代码会以某种方式响应"。

阿码点点头："所以测试不仅能验证'正常情况能工作'，还能验证'异常情况有保护'？"

"对，这正是防御式编程的验证。Week 03 你写的那些参数检查，现在可以用测试来确保它们真的在工作。"

---

## 5. 批量验证

<!--
**Bloom 层次**：应用/分析
**学习目标**：掌握 @ParameterizedTest 和 @CsvSource 的使用
**贯穿案例推进**：用参数化测试批量验证多种边界 ISBN 格式
**建议示例文件**：05_parameterized_tests.java
**叙事入口**：发现需要测试 10 种不同的无效 ISBN，复制粘贴测试代码太蠢
**角色出场**：老潘——"生产环境里，边界情况往往比正常情况多"
**回顾桥**：[防御式编程]（week_03）：验证各种非法输入的处理
-->

小北想给 ISBN 校验加更多测试。无效的 ISBN 有很多种：null、空字符串、全是空格、格式不对......如果每种情况都写一个测试方法，代码会膨胀到失控。

老潘看了一眼："用参数化测试。生产环境里，边界情况往往比正常情况多，批量验证是常态。"

JUnit 5 的 `@ParameterizedTest` 让你可以用多组数据运行同一个测试逻辑：

```java
// 文件：src/test/java/LibraryTrackerParameterizedTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTrackerParameterizedTest {

    private LibraryTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new LibraryTracker();
    }

    // 用 @ValueSource 测试多组无效 ISBN
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "invalid-isbn", "123"})
    void shouldRejectInvalidIsbn(String invalidIsbn) {
        tracker.addBook(new Book("书", "作者", "VALID-ISBN"));

        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook(invalidIsbn, "小北");
        });
    }

    // 用 @NullAndEmptySource 专门测试 null 和空字符串
    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullOrEmptyIsbn(String invalidIsbn) {
        tracker.addBook(new Book("书", "作者", "VALID-ISBN"));

        assertThrows(IllegalArgumentException.class, () -> {
            tracker.borrowBook(invalidIsbn, "小北");
        });
    }

    // 用 @CsvSource 测试多组输入输出组合
    @ParameterizedTest
    @CsvSource({
        "ISBN-1, 小北, true",    // 正常借阅
        "ISBN-2, 小北, false",   // 书不存在
        "ISBN-1, 阿码, false"    // 书存在但借给其他人了
    })
    void shouldCheckBorrowStatus(String isbn, String borrower, boolean expectedExists) {
        tracker.addBook(new Book("书1", "作者", "ISBN-1"));
        tracker.borrowBook("ISBN-1", "小北");

        boolean exists = tracker.hasBorrowRecord(isbn, borrower);
        assertEquals(expectedExists, exists);
    }
}
```

`@ParameterizedTest` 告诉 JUnit："这个方法要用多组参数运行"。参数来源可以是 `@ValueSource`（简单的值列表）、`@NullAndEmptySource`（专门提供 null 和空值）、`@CsvSource`（CSV 格式的表格数据），以及更灵活的 `@MethodSource`（引用一个提供参数的方法）。

运行这个测试类，JUnit 会为每组参数生成一个独立的测试用例。如果某组参数失败，你会清楚地看到是哪一组——比如 `"ISBN-2, 小北, false"` 失败了。

小北看着测试报告："一行测试代码，跑了十几种情况。这效率......"

"这就是自动化测试的威力。"老潘说，"手动测试不可能覆盖这么多边界，但机器可以。"

---

> **AI 时代小专栏：用 AI 辅助理解复杂测试场景**
>
> JUnit 5.11.x 是当前稳定版本（截至 2026 年初），参数化测试已经成为现代 Java 测试的标配。但面对 `@CsvSource`、`@ValueSource`、`@MethodSource` 这么多选择，初学者常常困惑：什么时候用哪个？
>
> 这正是 AI 可以帮上忙的地方——不是让它替你写测试，而是让它帮你理解不同参数源的适用场景。比如你可以问 AI："我有三组输入输出数据要测试，该用 @CsvSource 还是 @MethodSource？"AI 会告诉你：数据简单用 `@CsvSource`，需要计算生成数据才用 `@MethodSource`。
>
> 最佳实践告诉我们：保持数据源"纯粹"——数据提供者中不应包含逻辑，只应返回原始数据。这意味着 `@MethodSource` 里不应该有复杂的 if-else，它只是数据的"搬运工"。另外，使用描述性测试名称（如 `@ParameterizedTest(name = "{index} => Check if {0} results in {1}")`）能让测试报告更易读。
>
> 回到你刚写的参数化测试——当你用 AI 辅助时，记得检查它是否遵循了这些最佳实践。AI 有时会为了"炫技"而使用过度复杂的 `@MethodSource`，而简单的 `@CsvSource` 就能搞定。你在 Week 02 学的 KISS 原则（保持简单）在这里同样适用。
>
> 参考（访问日期：2026-02-12）：
> - [JUnit 5 Parameterized Test Complete Guide](https://ankurm.com/the-complete-guide-to-junit-5-parameterizedtest-write-smarter-faster-and-cleaner-java-tests/)
> - [Baeldung Parameterized Tests](https://www.baeldung.com/parameterized-tests-junit-5)

---

## 6. 测试的盲区

<!--
**Bloom 层次**：理解/评价
**学习目标**：理解测试覆盖率概念，能识别测试盲区
**贯穿案例推进**：运行覆盖率工具，发现 Repository 中未被测试的方法
**建议示例文件**：06_coverage_analysis.java
**叙事入口**：所有测试都通过了，但老潘说"还有没测到的代码"
**角色出场**：老潘——"100% 覆盖率不代表没有 bug，但低覆盖率一定代表有盲区"
**回顾桥**：[SOLID 原则]（week_02）：测试也是验证设计是否遵循单一职责的手段
-->

小北写了十几个测试，全部通过。他信心满满地把代码给老潘看。

老潘扫了一眼："测试都过了？那覆盖率多少？"

"覆盖率？"

"就是你写了测试的代码占总代码的比例。100% 覆盖率不代表没有 bug，但低覆盖率一定代表有盲区。"

Maven 可以用 JaCoCo 插件生成覆盖率报告。在 `pom.xml` 中添加：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.12</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

然后运行：

```bash
mvn test
```

测试完成后，在 `target/site/jacoco/index.html` 中打开覆盖率报告。你会看到行覆盖率（多少比例的代码行被执行过）、分支覆盖率（多少比例的 if/else 分支被执行过）、方法覆盖率（多少比例的方法被调用过）。

小北打开报告，发现 `LibraryTracker` 的覆盖率只有 62%。点击类名查看详情——原来 `listAllBooks()` 和 `removeBook()` 方法完全没有被测试到。

"我忘了测这两个方法......"小北有些尴尬。

"正常。人总是会遗漏。覆盖率工具的价值就是帮你发现这些遗漏。"

小北补上了缺失的测试：

```java
@Test
void shouldListAllBooks() {
    tracker.addBook(new Book("书1", "作者A", "ISBN-1"));
    tracker.addBook(new Book("书2", "作者B", "ISBN-2"));

    var books = tracker.listAllBooks();

    assertEquals(2, books.size());
}

@Test
void shouldRemoveBook() {
    tracker.addBook(new Book("书", "作者", "ISBN-1"));
    assertNotNull(tracker.findBook("ISBN-1"));

    tracker.removeBook("ISBN-1");

    assertNull(tracker.findBook("ISBN-1"));
}
```

重新运行测试，覆盖率提升到了 87%。

老潘点点头："80% 以上算合格，90% 以上算优秀。但记住，覆盖率只是指标，不是目标。一个 100% 覆盖率但断言很弱的测试集，还不如一个 70% 覆盖率但断言精确的测试集。"

"那怎么判断测试质量？"

"看测试失败时，你能否快速定位问题。好的测试像一张细密的网，bug 一出现就会被捕获；差的测试像一张破网，bug 穿过去了你都不知道。"

---

## CampusFlow 进度

<!--
CampusFlow 本周推进：
- 上周状态：使用 ArrayList/HashMap 重构了数据存储层，Repository 模式落地
- 本周改进：为 Repository 层添加单元测试，确保核心增删改查方法有测试覆盖
- 涉及的本周概念：JUnit 5 基础、测试生命周期、异常测试、参数化测试
- 具体落盘：
  1. 为 TaskRepository/UserRepository 编写单元测试
  2. 测试 save/findById/findAll/delete 等核心方法
  3. 测试异常抛出（如 save(null) 应该抛异常）
  4. 使用 @BeforeEach 初始化测试数据
  5. 目标：核心方法测试覆盖率 80%+
- 建议示例文件：examples/CampusFlowRepositoryTest.java
-->

到目前为止，CampusFlow 的 Repository 层已经用 `ArrayList` 和 `HashMap` 重构完成，但还没有任何自动化测试。

想象一下：如果 CampusFlow 的 Repository 层没有测试，下周你要重构存储结构时敢动手吗？小北想起自己改 `findBook` 引发的惨案——没有测试的保护，每次重构都像走钢丝。

本周我们为 `TaskRepository` 添加单元测试，确保核心方法有 80%+ 的覆盖率。这样下次重构时，你就能放心大胆地动手了。

```java
// 文件：src/test/java/TaskRepositoryTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class TaskRepositoryTest {

    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TaskRepository();
    }

    @Test
    void shouldSaveTask() {
        Task task = new Task("T1", "完成 Week 06 作业", "pending");

        repository.save(task);

        assertTrue(repository.findById("T1").isPresent());
    }

    @Test
    void shouldFindAllTasks() {
        repository.save(new Task("T1", "任务1", "pending"));
        repository.save(new Task("T2", "任务2", "done"));

        var tasks = repository.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    void shouldDeleteTask() {
        repository.save(new Task("T1", "任务", "pending"));
        assertTrue(repository.findById("T1").isPresent());

        repository.delete("T1");

        assertFalse(repository.findById("T1").isPresent());
    }

    @Test
    void shouldThrowExceptionWhenSavingNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
    }

    @ParameterizedTest
    @CsvSource({
        "T1, 任务1, pending",
        "T2, 任务2, done",
        "T3, 任务3, in_progress"
    })
    void shouldSaveAndRetrieveMultipleTasks(String id, String title, String status) {
        Task task = new Task(id, title, status);
        repository.save(task);

        Task found = repository.findById(id).orElseThrow();
        assertEquals(title, found.getTitle());
        assertEquals(status, found.getStatus());
    }
}
```

老潘点评："注意 `shouldSaveAndRetrieveMultipleTasks` 这个参数化测试——它用三组不同的数据验证了同一个逻辑。这比写三个几乎一样的测试方法要干净得多。"

"还有 `shouldThrowExceptionWhenSavingNull`——这是防御式编程的测试。你的 Repository 在 `save` 方法里检查了 null，测试确保这个检查真的在工作。"

运行 `mvn test`，确保所有测试通过，然后用 JaCoCo 检查覆盖率。核心方法（save/findById/findAll/delete）应该达到 80% 以上。

---

## Git 本周要点

本周 Git 的重点是**忽略测试生成的文件**。Maven 测试会生成 `target/surefire-reports/` 目录，这些报告文件不需要提交到 Git。

更新 `.gitignore` 文件：

```gitignore
# 编译输出
target/
*.class

# 测试报告（CI 会重新生成）
target/surefire-reports/

# IDE 配置
.idea/
*.iml
.vscode/
```

常见坑：测试失败后生成的报告文件被意外提交。记住：**测试报告是生成的，不是源代码**。

---

## 本周小结（供下周参考）

本周你学会了用 JUnit 5 为代码构建安全网。从第一个简单的 `@Test`，到使用 `@BeforeEach` 消除重复，再到 `assertThrows` 验证异常、`@ParameterizedTest` 批量验证边界情况——测试让你的代码变得可信赖。

小北现在明白了：测试是投资，写测试需要时间，但省去了反复手动验证的麻烦；测试即文档，好的测试用例展示了代码的预期用法；覆盖率是指标不是目标，追求 100% 覆盖率不如追求关键路径的可靠覆盖；测试也要维护，测试代码也是工程代码，要遵循同样的质量标准。

老潘路过，拍拍小北肩膀："不写测试的代码，就像不带降落伞跳伞——可能没事，但出事就是大事。"

但还有一个问题：目前你的测试只验证了"代码逻辑正确"，还没验证"代码设计良好"。下周我们要引入**测试驱动开发（TDD）**——让测试不仅验证代码，更指导设计。

---

## Definition of Done（学生自测清单）

- [ ] 我能解释单元测试与手动验证的 3 个核心差异
- [ ] 我能使用 JUnit 5 编写包含断言的基本测试
- [ ] 我能使用 @BeforeEach 消除测试代码的重复初始化
- [ ] 我能使用 assertThrows 验证异常抛出行为
- [ ] 我能使用 @ParameterizedTest 批量测试多组数据
- [ ] 我能运行测试覆盖率工具并解读报告
- [ ] 我的图书借阅追踪器拥有 80%+ 的测试覆盖率
- [ ] 我的 CampusFlow Repository 层有完整的单元测试
- [ ] 我能在审查 AI 生成的测试代码时发现边界情况遗漏
