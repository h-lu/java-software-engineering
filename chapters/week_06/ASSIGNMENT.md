# Week 06 作业：为图书借阅追踪器构建测试防线

## 作业概述

本周你将基于 Week 05 的图书借阅追踪器（`LibraryTracker`），使用 JUnit 5 构建完整的单元测试体系。目标是实现核心方法 80%+ 的测试覆盖率，并学会用测试守护代码质量。

**预计耗时**：4-6 小时
**截止日期**：2026-02-15（周日）23:59

---

## 学习目标关联

完成本作业后，你将能够：
- 使用 JUnit 5 编写包含断言的基本测试
- 使用 `@BeforeEach` 消除测试代码的重复初始化
- 使用 `assertThrows` 验证异常抛出行为
- 使用 `@ParameterizedTest` 批量测试多组边界情况
- 运行测试覆盖率工具并解读报告

---

## 基础作业（必做，60 分）

### 任务 1：为 LibraryTracker 添加单元测试

基于 Week 05 的 `LibraryTracker` 类，创建完整的测试类 `LibraryTrackerTest.java`。

**要求**：

1. **使用 `@BeforeEach` 消除重复代码**
   - 每个测试方法执行前，创建新的 `LibraryTracker` 实例
   - 准备常用的测试数据（如几本图书、借阅人信息）

2. **测试所有 Repository 方法**
   - `addBook(Book book)` - 添加图书
   - `findBook(String isbn)` - 查找图书
   - `listAllBooks()` - 列出所有图书
   - `hasBook(String isbn)` - 检查图书是否存在
   - `removeBook(String isbn)` - 删除图书
   - `borrowBook(String isbn, String borrower)` - 借阅图书
   - `returnBook(String isbn, String borrower)` - 归还图书
   - `getBorrowRecordsByUser(String borrower)` - 获取用户的借阅记录
   - `getAllBorrowRecords()` - 获取所有借阅记录

3. **使用 `assertThrows` 测试异常场景**
   - 测试 `addBook(null)` 抛出 `IllegalArgumentException`
   - 测试 `borrowBook(null, "小北")` 抛出 `IllegalArgumentException`
   - 测试 `borrowBook("不存在的ISBN", "小北")` 抛出 `IllegalArgumentException`
   - 测试 `returnBook` 归还不存在的记录时抛出 `IllegalStateException`

4. **至少使用 1 处 `@ParameterizedTest`**
   - 使用 `@CsvSource` 或 `@ValueSource` 批量测试多种边界情况
   - 示例：测试多种无效 ISBN 格式的处理

**输入/输出示例**：

```
// 测试 addBook 和 findBook
输入：添加 Book("Java核心技术", "Cay", "978-111")，然后查找 ISBN "978-111"
预期输出：返回的 Book 对象 title 为 "Java核心技术"

// 测试异常场景
输入：调用 borrowBook(null, "小北")
预期输出：抛出 IllegalArgumentException，消息包含 "ISBN 不能为空"

// 测试参数化（多种无效 ISBN）
输入："", "   ", "invalid-isbn", null
预期输出：每种情况都抛出 IllegalArgumentException
```

**提示**：
- 测试方法命名建议：`shouldXXXWhenYYY` 格式，如 `shouldThrowExceptionWhenBorrowingWithNullIsbn`
- 每个测试只验证一个概念，不要在一个测试里验证多个不相关的行为
- 使用 `assertEquals`、`assertNotNull`、`assertTrue`、`assertFalse` 等断言方法

---

## 进阶作业（选做，+20 分）

### 任务 2：实现测试覆盖率 90%+

1. **配置 JaCoCo 插件**
   - 在 `pom.xml` 中添加 JaCoCo Maven 插件（参考 CHAPTER.md 第 6 节）
   - 运行 `mvn test` 生成覆盖率报告

2. **识别测试盲区**
   - 打开 `target/site/jacoco/index.html` 查看覆盖率报告
   - 找出未被测试覆盖的代码行（通常显示为红色）

3. **补充边界情况测试**
   - 测试空列表场景（如没有图书时 `listAllBooks()` 返回空列表）
   - 测试重复添加同一 ISBN 的图书（后添加的应覆盖先添加的）
   - 测试同一用户借阅多本图书的场景
   - 测试图书删除后相关借阅记录的处理（如果业务逻辑有此设计）

4. **提交覆盖率报告截图**
   - 截图显示整体覆盖率 >= 90%
   - 截图显示 `LibraryTracker` 类的覆盖率详情

**常见错误**：
- 只关注行覆盖率，忽略分支覆盖率（if/else 是否都执行过）
- 为了覆盖率而写无意义的测试（如测试 getter/setter）
- 覆盖率 100% 但断言很弱，测试实际上没有验证正确行为

---

## CampusFlow 团队作业（必做，20 分）

### 任务 3：为 CampusFlow Repository 层编写单元测试

为你的团队 CampusFlow 项目的 Repository 层编写单元测试。

**要求**：
- 为所有 Repository 类（如 `TaskRepository`、`UserRepository` 等）编写测试
- 测试核心方法：`save`、`findById`、`findAll`、`delete`、`update`
- 使用 `@BeforeEach` 初始化测试数据
- 使用 `assertThrows` 测试异常场景（如 `save(null)`）
- 至少使用 1 处 `@ParameterizedTest`
- **目标：核心方法测试覆盖率 80%+**

**提交物**：
1. 测试代码（`src/test/java/` 下的所有测试类）
2. 覆盖率报告截图（显示整体覆盖率和各类覆盖率）
3. 简要说明：你们如何分工编写测试？遇到了哪些挑战？

**参考示例**（来自 CHAPTER.md）：

```java
@Test
void shouldSaveTask() {
    Task task = new Task("T1", "完成 Week 06 作业", "pending");
    repository.save(task);
    assertTrue(repository.findById("T1").isPresent());
}

@ParameterizedTest
@CsvSource({"T1, 任务1, pending", "T2, 任务2, done"})
void shouldSaveAndRetrieveMultipleTasks(String id, String title, String status) {
    Task task = new Task(id, title, status);
    repository.save(task);
    Task found = repository.findById(id).orElseThrow();
    assertEquals(title, found.getTitle());
}
```

---

## AI 协作练习（可选，+10 分附加分）

根据 CLAUDE.md，Week 04-07 处于"识别期"——学会审视 AI 生成的代码。

### 练习：审查 AI 生成的测试代码

下面这段代码是某个 AI 工具为 `LibraryTracker` 生成的测试：

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTrackerAITest {

    @Test
    void testAddBook() {
        LibraryTracker tracker = new LibraryTracker();
        Book book = new Book("Test", "Author", "123");
        tracker.addBook(book);
        assertNotNull(tracker.findBook("123"));
    }

    @Test
    void testBorrowBook() {
        LibraryTracker tracker = new LibraryTracker();
        Book book = new Book("Test", "Author", "123");
        tracker.addBook(book);
        tracker.borrowBook("123", "User");
        assertEquals(1, tracker.getBorrowRecordCount());
    }

    @Test
    void testRemoveBook() {
        LibraryTracker tracker = new LibraryTracker();
        Book book = new Book("Test", "Author", "123");
        tracker.addBook(book);
        tracker.removeBook("123");
        assertNull(tracker.findBook("123"));
    }
}
```

**请审查这段代码**：

- [ ] 代码能运行吗？
- [ ] 变量命名清晰吗？
- [ ] 有没有重复代码需要重构？
- [ ] 边界情况处理了吗？（如 null 输入、空字符串）
- [ ] 异常场景测试了吗？
- [ ] 测试名称描述性足够吗？
- [ ] 你能写一个让这段测试通过的"错误实现"吗？（即测试通过了，但代码逻辑是错的）

**提交要求**：
1. 问题清单（至少发现 3 个问题）
2. 修复后的完整测试代码
3. 简短反思：AI 生成测试代码时容易遗漏什么？

---

## 提交要求

### 提交物清单

1. **代码仓库链接**（GitHub/GitLab）
   - 包含完整的 `src/main/java/` 和 `src/test/java/`
   - 包含 `pom.xml`（配置了 JUnit 5 和 JaCoCo）

2. **测试运行截图**
   - 运行 `mvn test` 的结果截图
   - 显示 "Tests run: X, Failures: 0, Errors: 0"

3. **覆盖率报告**（如完成进阶作业）
   - `target/site/jacoco/index.html` 截图
   - 显示整体覆盖率和 `LibraryTracker` 类覆盖率

4. **AI 协作练习报告**（如完成）
   - 原始 AI 代码
   - 发现的问题清单
   - 修复后的代码
   - 反思总结

5. **CampusFlow 团队作业**
   - 测试代码
   - 覆盖率报告截图
   - 分工与挑战说明

### 提交方式

- 个人作业：通过课程平台提交仓库链接和截图
- 团队作业：在团队仓库中提交，组长汇总提交链接

---

## 评分标准

详见 `RUBRIC.md`

| 维度 | 权重 |
|------|------|
| 功能完成度 | 30% |
| 测试质量 | 30% |
| 代码规范 | 20% |
| AI 协作能力 | 20% |

---

## 常见问题

**Q：Week 05 的代码需要修改吗？**
A：不需要修改业务逻辑代码，只需要添加测试。但如果发现 Week 05 的代码有 bug，可以修复。

**Q：测试覆盖率达不到 80% 怎么办？**
A：先确保所有 public 方法都有测试。如果还有未覆盖的代码，检查是否是死代码（可以删除）或难以测试的代码（可能需要重构设计）。

**Q：可以使用 AI 生成测试代码吗？**
A：可以，但必须人工审查。参考上面的 AI 协作练习——AI 生成的测试往往有边界情况遗漏、断言不够精准等问题。

**Q：CampusFlow 的 Repository 和 LibraryTracker 类似吗？**
A：概念类似，都是数据访问层。CampusFlow 可能是 `TaskRepository`、`UserRepository` 等，根据你们的领域模型设计。

---

## 参考资源

- 本周章节：`chapters/week_06/CHAPTER.md`
- 示例代码：`chapters/week_06/examples/`
- 如果你遇到困难，可以参考 `starter_code/src/main/java/com/campusflow/App.java` 中的提示

---

> "未测试的代码就是未完成的代码。" —— 软件工程格言
