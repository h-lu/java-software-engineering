# Week 02 评分标准

## 评分维度概览

| 维度 | 占比 | 说明 |
|------|------|------|
| **代码质量** | 30% | 命名规范、代码格式、注释完整性 |
| **设计合理性** | 30% | 类职责清晰、封装良好、符合 SOLID 原则 |
| **文档完整性** | 20% | ADR 质量高、解释清晰、有决策过程 |
| **测试覆盖** | 20% | 有测试代码、验证关键功能、有边界测试 |

---

## 详细评分标准

### 一、代码质量（30 分）

#### 1.1 命名规范（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 类名 | 3 分 | PascalCase，名词或名词短语（如 `BookCollection`） |
| 方法名 | 3 分 | camelCase，动词或动词短语（如 `addBook`、`sortByYear`） |
| 变量名 | 2 分 | camelCase，有意义的描述（如 `totalPrice`，不是 `tp`） |
| 常量 | 2 分 | UPPER_SNAKE_CASE（如 `MAX_BOOK_COUNT`） |

**评分细则**：
- **9-10 分**：所有命名符合规范，清晰易懂
- **6-8 分**：大部分命名符合规范，少数地方可以改进
- **3-5 分**：命名有问题（如用拼音、无意义缩写）
- **0-2 分**：命名混乱，严重影响可读性

---

#### 1.2 代码格式（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 缩进 | 3 分 | 4 个空格，层级清晰 |
| 大括号 | 3 分 | K&R 风格（左大括号不换行） |
| 行长度 | 2 分 | 每行不超过 100 字符 |
| 空行 | 2 分 | 方法之间、逻辑段落之间有空行 |

**示例**：
```java
// ✅ 好的格式
public class BookCollection {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        books.add(book);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }
}

// ❌ 不好的格式
public class BookCollection{
    private List<Book> books=new ArrayList<>();  // 缺少空格
    public void addBook(Book book){  // 大括号应该换行风格统一
    if(book==null){throw new IllegalArgumentException("Book cannot be null");}  // 缩进混乱
    books.add(book);}}
```

**评分细则**：
- **9-10 分**：格式完全符合规范，代码整洁
- **6-8 分**：大部分格式正确，偶有问题
- **3-5 分**：格式混乱，影响阅读
- **0-2 分**：格式完全不符合规范

---

#### 1.3 注释完整性（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 类注释 | 3 分 | 每个类有 Javadoc，说明类的职责 |
| 方法注释 | 4 分 | 公共方法有 Javadoc，说明参数、返回值、异常 |
| 行内注释 | 2 分 | 复杂逻辑有解释性注释 |
| Javadoc 格式 | 1 分 | 使用 `@param`、`@return`、`@throws` 标签 |

**示例**：
```java
/**
 * 管理图书集合的类。
 *
 * <p>提供图书的增删改查、过滤、排序等功能。
 * 支持按作者过滤、按年份排序等操作。
 *
 * @author Your Name
 * @version 1.0
 */
public class BookCollection {
    private final List<Book> books = new ArrayList<>();

    /**
     * 添加图书到集合。
     *
     * <p>如果添加的是 null 或 ISBN 重复的图书，会抛出异常。
     *
     * @param book 要添加的图书
     * @throws IllegalArgumentException 如果 book 为 null 或 ISBN 重复
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        // 检查 ISBN 是否重复
        for (Book b : books) {
            if (b.getIsbn().equals(book.getIsbn())) {
                throw new IllegalArgumentException("ISBN already exists: " + book.getIsbn());
            }
        }
        books.add(book);
    }
}
```

**评分细则**：
- **9-10 分**：注释完整，Javadoc 规范，行内注释恰当
- **6-8 分**：有基本注释，但不够完整或格式不规范
- **3-5 分**：注释很少，或只有行内注释没有 Javadoc
- **0-2 分**：没有注释

---

### 二、设计合理性（30 分）

#### 2.1 类职责清晰度（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 单一职责 | 5 分 | 每个类只有一个职责，能用一句话描述 |
| 实体 vs 服务 | 3 分 | 实体类管数据，服务类管行为，边界清晰 |
| 没有"上帝类" | 2 分 | 没有超过 20 个方法或 500 行的类 |

**检查点**：
- **实体类**（`Book`）：只负责存储数据，有 getter/setter
- **服务类**（`BookCollection`）：负责管理图书集合，提供增删过滤排序
- **验证类**（`BookValidator`）：负责验证数据合法性
- **显示类**（`BookPrinter`）：负责格式化输出

**评分细则**：
- **9-10 分**：职责完全分离，每个类单一职责
- **6-8 分**：大部分类职责清晰，个别地方可以改进
- **3-5 分**：存在职责混淆（如实体类里有验证逻辑）
- **0-2 分**：出现"上帝类"，职责严重混乱

---

#### 2.2 封装合理性（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| private 字段 | 4 分 | 所有字段都是 private |
| getter/setter 合理 | 4 分 | 不是所有字段都有 setter，需要验证 |
| final 使用恰当 | 2 分 | 不应修改的字段（如 isbn）设为 final |

**示例**：
```java
// ✅ 好的封装
public class Book {
    private final String isbn;      // 创建后不能修改 → final
    private String title;
    private double price;

    public String getIsbn() {
        return isbn;  // 没有 setter
    }

    public void setPrice(double price) {  // 有验证
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }
}

// ❌ 封装问题
public class Book {
    public String isbn;      // ❌ public 字段
    public double price;     // ❌ public 字段，无验证

    public void setIsbn(String isbn) {  // ❌ isbn 不应该能修改
        this.isbn = isbn;
    }
}
```

**评分细则**：
- **9-10 分**：封装完全合理，字段全部 private，验证逻辑完整
- **6-8 分**：封装基本合理，个别字段可以改进
- **3-5 分**：存在 public 字段，或缺少验证
- **0-2 分**：严重违反封装原则

---

#### 2.3 SOLID 原则应用（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 单一职责（SRP） | 4 分 | 每个类只有一个引起变化的原因 |
| 开闭原则（OCP） | 3 分 | 通过扩展添加功能，而不是修改现有代码 |
| 其他原则（可选） | 3 分 | 接口隔离、依赖倒置（进阶题） |

**检查点**：
- **SRP**：`Book` 只管数据，`BookCollection` 只管管理，`BookPrinter` 只管显示
- **OCP**：添加新的图书类型（`Ebook`、`Audiobook`）时，不需要修改 `Book` 类

**评分细则**：
- **9-10 分**：完全符合 SRP 和 OCP，设计灵活可扩展
- **6-8 分**：基本符合 SRP，OCP 应用有瑕疵
- **3-5 分**：部分违反 SRP，但整体设计可接受
- **0-2 分**：严重违反 SOLID 原则

---

### 三、文档完整性（20 分）

#### 3.1 ADR 结构完整性（8 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 所有章节齐全 | 4 分 | 状态、背景、决策、理由、替代方案、后果、验证 |
| 每个章节有内容 | 2 分 | 不是模板，有实质性内容 |
| 格式规范 | 2 分 | Markdown 格式正确，层级清晰 |

**必须包含的章节**：
```markdown
## 状态
已采纳

## 背景
[你的项目是什么，要解决什么问题]

## 决策
[核心类设计 + 职责分配]

## 理由
[为什么这样设计，引用 SOLID 原则]

## 替代方案
[至少 2 个替代方案，说明为什么不采纳]

## 后果
[正面影响、负面影响、风险]

## 验证方式
- [ ] Week 04：...
- [ ] Week 07：...

## 首席架构师
[姓名] - 第 1 轮（Week 02-03）

## 记录日期
2026-02-11
```

---

#### 3.2 ADR 内容质量（8 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 决策清晰 | 2 分 | 用图或文字清晰描述类设计 |
| 理由充分 | 3 分 | 解释为什么这样设计，引用本周学到的原则 |
| 替代方案具体 | 3 分 | 列出至少 2 个替代方案，说明优缺点和为什么不选 |

**示例**（好的"替代方案"）：
```markdown
## 替代方案

### 方案 A：单一 Book 类（上帝类）
- **描述**：所有功能（数据、验证、存储、显示）都放在 Book 类里
- **优点**：代码少，初期开发快
- **缺点**：
  - 职责过多，每次需求变都要改 Book 类
  - 难以测试（验证、存储、显示耦合在一起）
  - 违反单一职责原则
- **结论**：不采纳 —— 虽然初期快，但后期维护成本高

### 方案 B：使用 Lombok 自动生成 getter/setter
- **描述**：用 Lombok 的 `@Data` 注解自动生成 getter/setter
- **优点**：减少样板代码
- **缺点**：
  - 教学阶段手写更有助于理解封装原理
  - Lombok 会隐藏设计意图（哪些字段不应该有 setter）
  - 引入额外依赖，增加学习成本
- **结论**：暂不采纳 —— 课程目标是理解原理，不是减少代码量
```

---

#### 3.3 可读性和表达（4 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 语言清晰 | 2 分 | 没有含糊不清的表达，术语使用正确 |
| 逻辑连贯 | 2 分 | 决策、理由、后果之间逻辑一致 |

**评分细则**：
- **4 分**：语言清晰，逻辑连贯，读一遍就明白
- **3 分**：语言基本清晰，个别地方可以改进
- **2 分**：语言表达有困难，需要反复阅读才能理解
- **0-1 分**：表达混乱，难以理解

---

### 四、测试覆盖（20 分）

#### 4.1 测试完整性（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 基本功能测试 | 4 分 | 测试增删改查等核心功能 |
| 边界情况测试 | 3 分 | 测试空输入、负数、null 等边界 |
| 异常测试 | 3 分 | 测试异常抛出是否正确 |

**示例**（好的测试）：
```java
@Test
public void testAddBook() {
    BookCollection collection = new BookCollection();
    Book book = new Book("978-7-111-12345-6", "Effective Java", "Joshua Bloch", 2018, 89.00);

    collection.addBook(book);

    assertEquals(1, collection.getBookCount());
    assertEquals("Effective Java", collection.getBooks().get(0).getTitle());
}

@Test
public void testSetNegativePrice() {
    Book book = new Book("978-7-111-12345-6", "Effective Java", "Joshua Bloch", 2018, 89.00);

    assertThrows(IllegalArgumentException.class, () -> {
        book.setPrice(-10);
    });
}

@Test
public void testAddNullBook() {
    BookCollection collection = new BookCollection();

    assertThrows(IllegalArgumentException.class, () -> {
        collection.addBook(null);
    });
}
```

**评分细则**：
- **9-10 分**：测试覆盖全面，包括边界和异常
- **6-8 分**：有基本功能测试，缺少边界测试
- **3-5 分**：测试很少，只测了"正常情况"
- **0-2 分**：没有测试

---

#### 4.2 测试质量（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 断言合理 | 4 分 | 断言检查了正确的值，不是"assertTrue(true)" |
| 测试命名 | 3 分 | 方法名清晰描述测试内容（如 `testSetNegativePrice`） |
| 测试独立 | 3 分 | 每个测试独立，不依赖其他测试 |

**示例**（不好的测试）：
```java
// ❌ 没有断言
@Test
public void testAddBook() {
    BookCollection collection = new BookCollection();
    Book book = new Book("978-7-111-12345-6", "Effective Java", "Joshua Bloch", 2018, 89.00);
    collection.addBook(book);
    // 没有任何断言！
}

// ❌ 无意义的断言
@Test
public void testAddBook() {
    BookCollection collection = new BookCollection();
    Book book = new Book("978-7-111-12345-6", "Effective Java", "Joshua Bloch", 2018, 89.00);
    collection.addBook(book);
    assertTrue(true);  // 这个断言永远通过，没有意义
}

// ❌ 命名不清晰
@Test
public void test1() {  // test1 是什么？
    BookCollection collection = new BookCollection();
    // ...
}
```

**评分细则**：
- **9-10 分**：断言合理，命名清晰，测试独立
- **6-8 分**：断言基本合理，命名可以改进
- **3-5 分**：断言不足或测试命名混乱
- **0-2 分**：测试质量差，没有实际意义

---

### 五、进阶题与挑战题（额外加分）

#### 5.1 进阶题（+20 分）

| 任务 | 分数 | 评分标准 |
|------|------|---------|
| 任务 5：多种图书类型 | 10 分 | 设计灵活，符合开闭原则，有 ADR 说明设计选择 |
| 任务 6：CLI 界面 | 10 分 | 菜单完整，交互流畅，有基本错误处理 |

**评分细则**：
- **9-10 分**：完全实现，设计合理，有文档
- **6-8 分**：基本实现，部分功能可以改进
- **3-5 分**：实现不完整或有明显问题
- **0-2 分**：尝试了但未能完成

---

#### 5.2 挑战题（+20 分）

| 任务 | 分数 | 评分标准 |
|------|------|---------|
| 任务 7：AI 代码审查 | 20 分 | 找出至少 3 个问题，修复正确，反思有深度 |

**评分细则**：
- **18-20 分**：找出 5+ 个问题，修复正确，反思深刻
- **15-17 分**：找出 3-4 个问题，修复正确，反思有内容
- **10-14 分**：找出 2-3 个问题，修复基本正确
- **5-9 分**：找出 1-2 个问题，修复有瑕疵
- **0-4 分**：尝试了但审查质量不高

**AI 代码审查示例**（好的审查报告）：
```markdown
## 问题 1：封装违反

**问题所在**：
```java
class Book {
    public String isbn;  // public 字段
    public String title;
}
```

**为什么是问题**：
- 违反封装原则，外部代码可以直接修改字段，绕过验证
- 无法控制数据的变化（如 isbn 被修改为无效值）

**如何修复**：
- 字段改为 `private`
- 添加 getter 方法
- `isbn` 设为 `final`，不提供 setter

---

## 问题 2：命名不清晰

**问题所在**：
```java
for (Book b : books) {  // b 是什么？
    System.out.println(b.title);
}
```

**为什么是问题**：
- 单字母变量名没有语义
- 代码可读性差，新成员难以理解

**如何修复**：
```java
for (Book book : books) {
    System.out.println(book.getTitle());
}
```

---

## 问题 3：职责混乱

**问题所在**：
```java
public void saveToFileAndSendEmail(String filename, String email) {
    // 做了两件事：保存文件 + 发送邮件
}
```

**为什么是问题**：
- 违反单一职责原则
- 将来可能只需要保存文件，不需要发邮件，但无法单独使用
- 难以测试（测试时需要模拟文件系统和邮件服务器）

**如何修复**：
- 拆分成两个方法：`saveToFile(String filename)` 和 `sendEmail(String email)`
- 由调用者决定需要执行哪些操作

---

## 反思：AI 代码最常犯的错误

1. **违反封装**：AI 为了"快速生成"，经常使用 public 字段
2. **职责混淆**：AI 生成的类经常承担多个不相关的职责
3. **缺少验证**：AI 生成的 setter 通常没有参数验证
4. **命名随意**：AI 喜欢用单字母变量名（如 `b`、`obj`）
5. **没有注释**：AI 生成的代码通常缺少 Javadoc

**我的改进**：
- 设计优先：先用 ADR 明确设计，再写代码
- 审查习惯：使用检查清单逐项检查 AI 代码
- 测试驱动：先写测试，确保 AI 代码符合预期
```

---

## 总分计算

### 基础分（100 分）
- 代码质量：30 分
- 设计合理性：30 分
- 文档完整性：20 分
- 测试覆盖：20 分

### 额外加分（最多 +40 分）
- 进阶题：+20 分
- 挑战题：+20 分

### 最终等级
- **A（90-100 分）**：基础分 90+，或基础分 80+ + 至少一个进阶题
- **B（80-89 分）**：基础分 80+
- **C（70-79 分）**：基础分 70+
- **D（60-69 分）**：基础分 60+
- **F（<60 分）**：基础分 <60

---

## 常见扣分项

| 问题 | 扣分 | 说明 |
|------|------|------|
| 公共字段 | -10 分 | 违反封装原则 |
| 上帝类 | -10 分 | 类职责过多 |
| 缺少 Javadoc | -5 分 | 文档不完整 |
| 测试覆盖不足 | -5 分 | 只有基本功能测试，缺少边界测试 |
| ADR 缺少替代方案 | -5 分 | 没有说明为什么选择当前方案 |
| 命名不规范 | -3 分 | 变量、方法命名混乱 |
| 格式问题 | -2 分 | 缩进、大括号、行长度问题 |

---

## 快速自查清单

提交前请检查：

### 代码
- [ ] 所有字段都是 private
- [ ] getter/setter 合理（不是所有字段都有 setter）
- [ ] 验证逻辑在 setter 中（负数价格、无效年份）
- [ ] 每个类职责清晰（能用一句话描述）
- [ ] 没有"上帝类"（超过 20 个方法）

### 文档
- [ ] ADR 所有章节齐全（状态、背景、决策、理由、替代方案、后果、验证）
- [ ] ADR 有至少 2 个替代方案
- [ ] 每个公共方法有 Javadoc
- [ ] 复杂逻辑有行内注释

### 测试
- [ ] 至少 2 个测试方法
- [ ] 测试命名清晰（如 `testSetNegativePrice`）
- [ ] 测试边界情况（null、负数、空集合）
- [ ] 断言合理（不是 `assertTrue(true)`）

### 提交
- [ ] 代码在 `src/main/java/edu/campusflow/domain/` 目录下
- [ ] ADR 在 `docs/adr/001-领域模型设计.md`
- [ ] Git commit message 清晰（如 `feat: 完成 Week 02 基础题`）
