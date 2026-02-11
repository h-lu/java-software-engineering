# Week 02 作业：类设计与领域建模

> **"设计一个类，就是在设计一个小世界。"**
> — Grady Booch

---

## 作业概述

本周作业分为三层：基础、进阶、挑战。你至少需要完成基础题才能通过本周考核。进阶题和挑战题是可选的，但强烈推荐尝试——它们能帮你真正掌握"设计思维"，而不只是语法。

**本周核心目标**：
1. 从需求中识别核心类（名词提取法）
2. 设计封装良好的类（private 字段 + public 方法）
3. 拆分"上帝类"，应用单一职责原则
4. 撰写第一份 ADR（领域模型设计决策）

---

## 基础题（必做）

### 任务 1：识别并设计核心类

**场景**：你正在做一个图书管理系统。产品经理给了你以下需求：

> 用户可以创建图书（Book），每本书有 ISBN、标题、作者、出版年份、价格。
> 图书可以被添加到收藏夹（BookCollection）。
> 收藏夹可以计算总价、按作者过滤图书、按年份排序。
> 系统能显示图书的详细信息（文本格式）。

**任务**：

1. 使用"名词提取法"从需求中识别核心类
2. 区分"实体类"和"服务类"
3. 为每个类设计：
   - 字段（全部 private）
   - 构造方法
   - Getter/Setter（根据需要，不是所有字段都要 setter）
   - 核心方法

**输入示例**（图书信息）：
```
ISBN: 978-7-111-12345-6
标题：Effective Java
作者：Joshua Bloch
出版年份：2018
价格：89.00
```

**输出示例**（程序运行结果）：
```
图书：Effective Java (978-7-111-12345-6)
作者：Joshua Bloch
年份：2018
价格：¥89.00

收藏夹总价：¥267.00
包含图书：3 本
```

**提示**：
- `Book` 是实体类，只负责存储数据
- `BookCollection` 是服务类，负责管理图书集合
- 代码中不需要持久化（读写文件），只需在内存中操作
- 如果遇到困难，可以参考 `starter_code/` 目录中的起步代码（但不要照抄）

---

### 任务 2：应用封装原则

**场景**：小北写了一个 `Book` 类，但他把所有字段都设为 `public`：

```java
// 小北的代码（有问题）
public class Book {
    public String isbn;
    public String title;
    public String author;
    public int year;
    public double price;
}
```

老潘看了直摇头："这样写有安全隐患——价格可以被设为负数，年份可以是未来。"

**任务**：

1. 重构 `Book` 类，应用封装原则：
   - 所有字段改为 `private`
   - 添加带验证的 setter（price 不能为负，year 不能超过当前年份）
   - 提供必要的 getter
   - `isbn` 字段设为 `final`（创建后不能修改）

2. 编写测试代码验证：
   - 尝试设置负数价格（应该抛出异常）
   - 尝试设置无效年份（应该抛出异常）
   - 正常创建图书并访问信息

**输入示例**：
```java
Book book = new Book("978-7-111-12345-6", "Effective Java", "Joshua Bloch", 2018, 89.00);
System.out.println(book.getTitle());  // Effective Java
book.setPrice(-10);                    // 应该抛出 IllegalArgumentException
```

**输出示例**：
```
图书创建成功
书名：Effective Java
价格：¥89.00

java.lang.IllegalArgumentException: 价格不能为负数
    at Book.setPrice(Book.java:XX)
```

**提示**：
- setter 中的验证逻辑应该抛出 `IllegalArgumentException`
- 可以使用 `java.time.Year.now().getValue()` 获取当前年份
- 错误信息要清晰，帮助调试

---

### 任务 3：拆分"上帝类"

**场景**：阿码用 AI 生成了一个 `BookService` 类，但老潘看了说这是典型的"上帝类"：

```java
// AI 生成的代码（职责过多）
public class BookService {
    private List<Book> books = new ArrayList<>();

    // 职责 1：数据验证
    public boolean isValid(Book book) { /* ... */ }

    // 职责 2：数据存储
    public void saveToFile(String filename) { /* ... */ }

    // 职责 3：数据显示
    public void print(Book book) { /* ... */ }

    // 职责 4：集合管理
    public void addBook(Book book) { /* ... */ }

    // 职责 5：过滤和排序
    public List<Book> filterByAuthor(String author) { /* ... */ }
    public List<Book> sortByYear() { /* ... */ }

    // ... 还有 10 多个方法
}
```

**任务**：

1. 分析 `BookService` 有哪些职责（至少找出 5 个）
2. 将其拆分成多个职责单一的类：
   - `BookCollection`：管理图书集合（增删过滤排序）
   - `BookValidator`：验证图书数据合法性
   - `BookPrinter`：负责格式化输出
   - （不需要实现 `BookRepository`，Week 07 会讲）

3. 重写代码，确保每个类只负责一件事

**输入示例**：
```java
BookCollection collection = new BookCollection();
collection.addBook(book1);
collection.addBook(book2);

BookPrinter printer = new BookPrinter();
printer.print(collection);  // 打印所有图书
printer.printAsJson(collection);  // 以 JSON 格式打印
```

**输出示例**：
```
=== 图书列表 ===
1. Effective Java - Joshua Bloch (2018) - ¥89.00
2. Clean Code - Robert C. Martin (2009) - ¥78.00
=== 共 2 本 ===

// JSON 格式
[
  {"title": "Effective Java", "author": "Joshua Bloch", "price": 89.00},
  {"title": "Clean Code", "author": "Robert C. Martin", "price": 78.00}
]
```

**提示**：
- 使用 SOLID 原则中的"单一职责原则"指导拆分
- 每个类应该能用一句话描述其职责
- 如果拆分后发现某个类只有一行代码，可能是过度拆分

---

### 任务 4：撰写 ADR-001

**场景**：你在任务 1-3 中设计了一个图书管理系统的领域模型。现在需要记录你的设计决策。

**任务**：

撰写 `docs/adr/001-领域模型设计.md`，包含以下部分：

1. **状态**：已采纳
2. **背景**：你的项目是什么？要解决什么问题？
3. **决策**：
   - 核心类设计（用 ASCII 图或文字描述）
   - 职责分配（每个类负责什么）
   - 设计约束（哪些字段是 final，为什么）
4. **理由**：为什么这样设计？引用 SOLID 原则
5. **替代方案**：你考虑过哪些其他方案？为什么不选？
6. **后果**：正面影响、负面影响、风险
7. **验证方式**：如何验证这个设计是合理的？（Week 04 / Week 07 的具体检查点）

**输入示例**（ADR 片段）：
```markdown
## 决策

### 核心类设计
```
┌─────────────────┐
│  BookCollection │ (服务类)
├─────────────────┤
│ - books: List   │
│ + addBook()     │
│ + filterByAuthor() │
│ + sortByYear()  │
└────────┬────────┘
         │ 使用
         ▼
┌─────────────────┐
│      Book       │ (实体类)
├─────────────────┤
│ - isbn: String  │ (final)
│ - title: String │
│ - price: double │
│ + getTitle()    │
│ + setPrice()    │
└─────────────────┘
```

### 设计约束
- 所有 Book 字段都是 private（封装）
- isbn 字段是 final（创建后不能修改）
- setPrice() 中验证价格不能为负（防御式编程）
```

**输出示例**（完整 ADR 文档，1-2 页 Markdown）：
- 文档应该清晰解释"为什么这样设计"
- 替代方案至少列出 2 个，并说明为什么不采纳
- 验证方式要有具体的检查点（Week 04 要做什么，Week 07 要验证什么）

**提示**：
- ADR 不是学术论文，不需要长篇大论
- 重点写"为什么"和"替代方案"——这是最有价值的部分
- ADR 必须由人写，不能用 AI 代劳（AI 可以润色语言，但不能替你做决策）
- 可以参考正文第 5 节的 ADR 模板

---

## 进阶题（选做）

### 任务 5：支持多种图书类型

**场景**：产品经理说："我们除了普通图书，还有电子书（ebook）和有声书（audiobook）。电子书有文件大小，有声书有时长。"

**任务**：

设计一个支持多种图书类型的领域模型。思考：
1. 是否需要继承？（`Ebook extends Book`？）
2. 是否需要接口？
3. 如果未来要加更多类型（纸质书、杂志），设计是否容易扩展？

**输入示例**：
```java
Book ebook = new Ebook("978-7-111-12345-6", "Effective Java", "Joshua Bloch", 2018, 89.00, 15.5);
System.out.println(ebook.getFileSize());  // 15.5 MB

Book audiobook = new Audiobook("978-7-111-23456-7", "Clean Code", "Robert C. Martin", 2009, 78.00, 360);
System.out.println(audiobook.getDuration());  // 360 分钟
```

**输出示例**：
```
电子书：Effective Java
文件大小：15.5 MB
格式：PDF

有声书：Clean Code
时长：360 分钟（6 小时）
朗读者：作者本人
```

**提示**：
- 考虑开闭原则（OCP）：添加新类型时不修改现有代码
- 可以使用抽象基类 + 子类，也可以使用组合
- 写一段简短的 ADR 说明你的选择和理由

---

### 任务 6：交互式命令行界面

**场景**：老潘说："你的类设计得很好，但用户怎么用？加个命令行菜单吧。"

**任务**：

为你的图书管理系统添加交互式 CLI：
1. 添加图书
2. 显示所有图书
3. 按作者过滤
4. 按年份排序
5. 计算总价
6. 退出

**输入示例**：
```
=== 图书管理系统 ===
1. 添加图书
2. 显示所有图书
3. 按作者过滤
4. 按年份排序
5. 计算总价
6. 退出

请选择 (1-6)：1

ISBN: 978-7-111-12345-6
标题: Effective Java
作者: Joshua Bloch
年份: 2018
价格: 89.00

图书添加成功！

请选择 (1-6)：2
=== 图书列表 ===
1. Effective Java - Joshua Bloch (2018) - ¥89.00
```

**输出示例**：
```
程序运行正常，菜单可以循环执行
输入无效选项时提示"请输入 1-6 之间的数字"
输入非数字时不会崩溃（虽然 Week 03 才讲异常处理，但你可以尝试用 try-catch）
```

**提示**：
- 使用 `while (true)` 循环和 `switch` 或 `if-else`
- 可以复用 Week 01 学的 Scanner 输入
- 不需要持久化，程序退出后数据丢失即可

---

## 挑战题（可选）

### 任务 7：AI 代码审查训练

**场景**：阿码让 AI 生成了一个图书管理系统的类设计。代码能跑，但老潘说"有设计问题"。

**任务**：

以下是一段 AI 生成的代码。请使用"AI 代码审查清单"找出至少 3 个问题，并修复：

```java
// AI 生成的代码（故意包含问题）
public class BookManager {
    public List<Book> books = new ArrayList<>();

    public void addBook(String isbn, String title, String author, int year, double price) {
        Book b = new Book();
        b.isbn = isbn;
        b.title = title;
        b.author = author;
        b.year = year;
        b.price = price;
        books.add(b);
    }

    public void printBooks() {
        for (Book b : books) {
            System.out.println(b.title + " - " + b.author + " - $" + b.price);
        }
    }

    public void saveToFileAndSendEmail(String filename, String email) {
        // ... 保存文件
        // ... 发送邮件
    }
}

class Book {
    public String isbn;
    public String title;
    public String author;
    public int year;
    public double price;
}
```

**审查清单**：
- [ ] 代码能运行吗？
- [ ] 变量命名清晰吗？（`b` 是什么？）
- [ ] 有没有缺少错误处理的地方？（price 为负怎么办？）
- [ ] 封装做对了吗？（public 字段）
- [ ] 类职责清晰吗？（`saveToFileAndSendEmail` 做了两件事）
- [ ] 有没有"魔法数字"或硬编码？
- [ ] 代码重复了吗？

**提交物**：
1. 修复后的代码
2. 你发现的问题清单（每个问题说明：问题所在、为什么是问题、如何修复）
3. 简短反思：AI 代码最常犯哪些错误？你如何审查？

---

## 提交要求

### 必交内容（基础题）

1. **源代码**：
   - `src/main/java/edu/campusflow/domain/Book.java`
   - `src/main/java/edu/campusflow/domain/BookCollection.java`
   - `src/main/java/edu/campusflow/domain/BookValidator.java`（任务 3）
   - `src/main/java/edu/campusflow/domain/BookPrinter.java`（任务 3）

2. **ADR 文档**：
   - `docs/adr/001-领域模型设计.md`

3. **测试代码**：
   - 至少 2 个测试方法，验证封装和职责分离

### 选交内容（进阶/挑战）

1. **进阶题代码**：
   - `Ebook.java`、`Audiobook.java`（任务 5）
   - `BookCLI.java`（任务 6）

2. **挑战题审查报告**（任务 7）：
   - `AI_REVIEW_REPORT.md`

### 提交格式

```bash
# Git 提交
git add src/main/java/edu/campusflow/domain/
git add docs/adr/001-领域模型设计.md
git commit -m "feat: 完成 Week 02 基础题 - 图书管理领域模型"

# 如果做了进阶题
git add src/main/java/edu/campusflow/cli/
git commit -m "feat: 完成 Week 02 进阶题 - CLI 界面"
```

---

## 常见错误与避坑指南

### 错误 1：getter/setter 滥用

**问题**：给所有字段都生成 getter/setter
```java
// ❌ 不好的做法
public class Book {
    private String isbn;
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }  // isbn 不应该能修改
}
```

**正确做法**：
- `isbn` 是标识符，创建后不应修改 → 不提供 setter，或字段设为 `final`
- 只在"外部需要修改这个字段"时才提供 setter

---

### 错误 2：职责不清晰

**问题**：把验证逻辑写在实体类里
```java
// ❌ Book 既管数据又管验证
public class Book {
    private double price;

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException();
        this.price = price;
    }

    public boolean isValid() {  // 验证逻辑应该在 TaskValidator
        return price > 0;
    }
}
```

**正确做法**：
- `Book` 只负责存储数据和基本验证（setter 中的参数检查）
- `BookValidator` 负责复杂验证规则（比如"价格必须在合理范围内"）

---

### 错误 3：过度拆分

**问题**：把一个简单的类拆成 5 个类，每个类只有 1 行代码
```java
// ❌ 过度设计
public class BookTitleGetter {
    public String getTitle(Book book) {
        return book.title;
    }
}
```

**正确做法**：
- 保持设计简单，避免为了模式而模式
- 一个类有 5-10 个方法是正常的
- 只有当"职责明显不同"时才拆分

---

### 错误 4：ADR 用 AI 生成

**问题**：让 AI 生成 ADR，得到一堆"正确的废话"
```markdown
## 决策
我们采用了面向对象的设计方法，遵循了 SOLID 原则...
```

**正确做法**：
- ADR 记录的是**你的决策过程**：为什么选 A 而不是 B？你的项目有什么特殊约束？
- AI 可以润色语言，但不能替你做决策
- 写 ADR 的过程本身就是"深度思考"

---

## 评分标准

详细评分标准见 `RUBRIC.md`。

简要说明：
- **基础题**：60 分 - 完成任务 1-4，代码能运行，ADR 文档完整
- **进阶题**：+20 分 - 完成任务 5 或 6
- **挑战题**：+20 分 - 完成任务 7，审查报告有深度

---

## 学习资源

- **本周章节**：`chapters/week_02/CHAPTER.md`
- **起步代码**：`starter_code/src/main/java/`（如果你遇到困难）
- **风格指南**：`shared/style_guide.md`
- **AI 辅助**：可以用 AI 生成代码骨架，但必须自己审查和修改
