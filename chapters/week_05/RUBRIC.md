# Week 05 评分标准

## 评分维度概览

| 维度 | 占比 | 说明 |
|------|------|------|
| **功能正确性** | 35% | 集合操作是否正确，泛型使用是否规范 |
| **代码质量** | 25% | 边界处理，类型安全，数据结构选择 |
| **测试覆盖** | 20% | 边界情况测试，异常场景覆盖 |
| **工程实践** | 20% | Git 规范，代码注释，文档完整 |

---

## 详细评分标准

### 一、功能正确性（35 分）

#### 1.1 集合框架使用正确性（15 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| ArrayList 操作正确 | 5 分 | 正确使用 add/get/remove/size，处理边界情况 |
| HashMap 操作正确 | 5 分 | 正确使用 put/get/remove/containsKey，理解 O(1) 查找 |
| 迭代器使用正确 | 5 分 | 正确使用 Iterator 遍历和删除，避免 ConcurrentModificationException |

**示例**（好的集合使用）：
```java
// ✅ 好的做法：使用泛型，返回副本
public List<Book> listAllBooks() {
    return new ArrayList<>(booksByIsbn.values());
}

// ✅ 好的做法：使用 Iterator 安全删除
public void removeAnonymousBooks() {
    Iterator<Book> it = books.iterator();
    while (it.hasNext()) {
        Book book = it.next();
        if ("佚名".equals(book.getAuthor())) {
            it.remove();
        }
    }
}
```

**示例**（不好的集合使用）：
```java
// ❌ 不好的做法：原始类型，失去类型安全
ArrayList books = new ArrayList();

// ❌ 不好的做法：遍历时直接删除
for (Book book : books) {
    books.remove(book);  // 抛出 ConcurrentModificationException
}
```

**评分细则**：
- **13-15 分**：集合使用熟练，正确处理所有边界情况
- **10-12 分**：基本正确，个别边界情况处理可以改进
- **6-9 分**：能使用集合，但存在问题（如原始类型、边界错误）
- **0-5 分**：集合使用不正确或缺失

---

#### 1.2 泛型使用规范性（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 无原始类型 | 4 分 | 所有集合声明都有类型参数，不使用 raw type |
| 类型安全 | 3 分 | 无 unchecked 警告，无强制类型转换 |
| 泛型方法 | 3 分 | 工具类正确使用泛型方法（<T, K>） |

**示例**（好的泛型使用）：
```java
// ✅ 好的做法：完整的泛型声明
public static <T, K> Map<K, List<T>> groupBy(List<T> list, KeyExtractor<T, K> extractor) {
    Map<K, List<T>> result = new HashMap<>();
    // ...
    return result;
}
```

**示例**（不好的泛型使用）：
```java
// ❌ 不好的做法：原始类型
Map map = new HashMap();  // 警告：raw type

// ❌ 不好的做法：不安全的转换
List list = new ArrayList();
Book book = (Book) list.get(0);  // 可能 ClassCastException
```

**评分细则**：
- **9-10 分**：泛型使用规范，无原始类型，类型安全
- **7-8 分**：基本规范，个别地方可以改进
- **4-6 分**：有原始类型或类型安全问题
- **0-3 分**：泛型使用不正确或缺失

---

#### 1.3 边界情况处理（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| null 参数检查 | 4 分 | 方法入口检查 null 参数，抛出 IllegalArgumentException |
| 空集合处理 | 3 分 | 正确处理空集合，不抛出异常 |
| 重复数据处理 | 3 分 | 正确处理重复 key（如重复 ISBN） |

**示例**（好的边界处理）：
```java
// ✅ 好的做法：尽早失败
public void addBook(Book book) {
    if (book == null) {
        throw new IllegalArgumentException("图书不能为 null");
    }
    if (book.getIsbn() == null) {
        throw new IllegalArgumentException("ISBN 不能为 null");
    }
    if (booksByIsbn.containsKey(book.getIsbn())) {
        throw new IllegalArgumentException("ISBN 已存在: " + book.getIsbn());
    }
    booksByIsbn.put(book.getIsbn(), book);
}
```

**评分细则**：
- **9-10 分**：边界处理完善，所有方法都有参数校验
- **7-8 分**：基本完善，个别方法可以补充
- **4-6 分**：有边界处理，但不完整
- **0-3 分**：缺少边界处理

---

### 二、代码质量（25 分）

#### 2.1 数据结构选择（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| ArrayList vs HashMap 选择 | 4 分 | 在合适场景选择合适的数据结构 |
| 数据结构组合 | 3 分 | 合理使用组合（如 ArrayList + HashMap） |
| 性能意识 | 3 分 | 理解时间复杂度，避免 O(n) 查找 |

**示例**（好的数据结构选择）：
```java
// ✅ 好的做法：HashMap 用于快速查找
private HashMap<String, Book> booksByIsbn;  // O(1) 查找

// ✅ 好的做法：ArrayList 用于保持顺序
private ArrayList<BorrowRecord> borrowRecords;  // 按时间顺序
```

**示例**（不好的数据结构选择）：
```java
// ❌ 不好的做法：用 List 做频繁查找
private ArrayList<Book> books;
// 查找时需要遍历 O(n)
```

**评分细则**：
- **9-10 分**：数据结构选择合理，有性能意识
- **7-8 分**：基本合理，个别选择可以优化
- **4-6 分**：数据结构选择有问题
- **0-3 分**：数据结构使用不当

---

#### 2.2 防御式编程（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 返回副本 | 4 分 | 返回集合时返回副本或不可修改视图，保护内部数据 |
| 参数校验 | 3 分 | 方法入口验证参数合法性 |
| 异常信息 | 3 分 | 异常信息具体，帮助定位问题 |

**示例**（好的防御式编程）：
```java
// ✅ 好的做法：返回副本
public List<Book> listAllBooks() {
    return new ArrayList<>(booksByIsbn.values());
}

// ✅ 好的做法：返回不可修改视图
public List<Task> findAll() {
    return Collections.unmodifiableList(new ArrayList<>(taskList));
}
```

**评分细则**：
- **9-10 分**：防御式编程应用到位，数据封装良好
- **7-8 分**：基本应用，个别地方可以改进
- **4-6 分**：有防御式编程意识，但应用不完整
- **0-3 分**：缺少防御式编程实践

---

#### 2.3 代码风格（5 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 命名规范 | 2 分 | 类名、方法名、变量名符合 Java 规范 |
| 代码格式 | 2 分 | 缩进、括号、空格规范 |
| 注释 | 1 分 | 复杂逻辑有注释说明 |

**评分细则**：
- **5 分**：风格完全符合规范
- **4 分**：基本符合，小问题
- **2-3 分**：有风格问题
- **0-1 分**：风格混乱

---

### 三、测试覆盖（20 分）

#### 3.1 边界情况测试（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| null 测试 | 4 分 | 测试 null 参数的处理 |
| 空集合测试 | 3 分 | 测试空集合的操作 |
| 异常测试 | 3 分 | 使用 assertThrows 验证异常类型和消息 |

**示例**（好的边界测试）：
```java
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
```

**评分细则**：
- **9-10 分**：边界测试覆盖全面，包括 null、空集合、异常
- **7-8 分**：有基本边界测试，可以补充更多场景
- **4-6 分**：测试很少，只覆盖正常情况
- **0-3 分**：缺少边界测试

---

#### 3.2 测试质量（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 断言合理 | 4 分 | 使用合适的断言方法，验证关键结果 |
| 测试命名 | 3 分 | 方法名清晰描述测试场景 |
| 测试独立 | 3 分 | 每个测试独立，不依赖其他测试 |

**示例**（不好的测试）：
```java
// ❌ 不好的做法：没有断言异常类型
@Test
public void testInvalidTitle() {
    LibraryTracker tracker = new LibraryTracker();
    tracker.addBook(null);  // 会抛出异常，但没有断言
}

// ❌ 不好的做法：命名不清晰
@Test
public void test1() {  // test1 是什么？
    // ...
}
```

**评分细则**：
- **9-10 分**：测试质量高，断言合理，命名清晰
- **7-8 分**：基本合理，个别可以改进
- **4-6 分**：测试质量一般
- **0-3 分**：测试质量差

---

### 四、工程实践（20 分）

#### 4.1 Git 提交规范（8 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 提交信息 | 4 分 | 使用规范格式（feat:, fix:, docs: 等） |
| 提交粒度 | 4 分 | 小步提交，每个提交有明确目的 |

**示例**（好的提交信息）：
```bash
feat: 实现图书借阅追踪器核心功能

test: 添加边界情况测试
docs: 添加 ADR-003 数据存储方案决策
```

**评分细则**：
- **7-8 分**：提交规范，信息清晰，粒度合适
- **5-6 分**：基本规范，个别可以改进
- **3-4 分**：提交信息不够清晰
- **0-2 分**：提交混乱

---

#### 4.2 代码组织（7 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 包结构 | 3 分 | 包名有意义，类放在合适的包中 |
| 类职责 | 2 分 | 每个类职责单一，符合 SRP |
| 访问控制 | 2 分 | 合理使用 public/private/protected |

**评分细则**：
- **6-7 分**：代码组织良好，结构清晰
- **4-5 分**：基本合理，个别可以改进
- **2-3 分**：组织有问题
- **0-1 分**：组织混乱

---

#### 4.3 文档完整（5 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| README | 2 分 | 有 README 说明如何运行项目 |
| 代码注释 | 2 分 | 复杂逻辑有注释 |
| ADR（如做挑战题）| 1 分 | ADR 结构完整，决策理由清晰 |

**评分细则**：
- **5 分**：文档完整清晰
- **4 分**：基本完整
- **2-3 分**：文档简单
- **0-1 分**：缺少文档

---

## 进阶题与挑战题（额外加分）

### 进阶题（+20 分）

| 任务 | 分数 | 评分标准 |
|------|------|---------|
| 任务 4：CampusFlow 数据层重构 | 10 分 | Repository 设计合理，使用 Optional，返回不可修改视图 |
| 任务 5：自定义迭代器 | 10 分 | Iterator 实现正确，hasNext/next 逻辑正确 |

**评分细则**：
- **9-10 分**：完全实现，设计合理
- **7-8 分**：基本实现，部分功能可以改进
- **4-6 分**：实现不完整或有明显问题
- **0-3 分**：尝试了但未能完成

---

### 挑战题（+20 分）

| 任务 | 分数 | 评分标准 |
|------|------|---------|
| 任务 6：性能对比分析 | 10 分 | 测试方法完整，结果分析有深度 |
| 任务 7：ADR 设计文档 | 10 分 | 对比方案全面，决策理由充分，有复杂度分析 |

**评分细则**：
- **9-10 分**：实现/文档全面，有深度思考
- **7-8 分**：完整，有设计思考
- **4-6 分**：有内容，但深度不足
- **0-3 分**：未完成或质量不高

---

## AI 协作练习（额外加分）

如完成 AI 协作练习，根据审查质量评分：

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 问题发现 | 3 分 | 发现 AI 代码中的 3 个以上问题 |
| 修复质量 | 2 分 | 修复后的代码规范，无新问题 |

---

## 总分计算

### 基础分（100 分）
- 功能正确性：35 分
- 代码质量：25 分
- 测试覆盖：20 分
- 工程实践：20 分

### 额外加分（最多 +40 分）
- 进阶题：+20 分
- 挑战题：+20 分
- AI 协作练习：+5 分

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
| 使用原始类型（raw type） | -10 分 | 如 `ArrayList` 而不是 `ArrayList<Book>` |
| 返回内部集合引用 | -10 分 | 外部可修改内部数据 |
| 遍历时直接删除元素 | -10 分 | 导致 ConcurrentModificationException |
| 缺少 null 检查 | -8 分 | 方法入口未验证参数 |
| 数据结构选择不当 | -8 分 | 如用 List 做频繁查找 |
| 缺少边界测试 | -5 分 | 只有正常情况测试 |
| 泛型使用不规范 | -5 分 | 有不安全的类型转换 |
| 命名不规范 | -3 分 | 不符合 Java 命名规范 |

---

## 快速自查清单

提交前请检查：

### 集合使用
- [ ] 所有集合声明都有类型参数（无 raw type）
- [ ] 使用 ArrayList 还是 LinkedList 有明确理由
- [ ] 需要快速查找时使用 HashMap
- [ ] 遍历时需要删除使用 Iterator

### 泛型
- [ ] 无 unchecked 警告
- [ ] 无强制类型转换（cast）
- [ ] 泛型方法声明正确（<T, K>）

### 边界处理
- [ ] 所有 public 方法都检查 null 参数
- [ ] 空集合操作不抛出异常
- [ ] 重复 key 处理有明确策略

### 防御式编程
- [ ] 返回集合时返回副本或不可修改视图
- [ ] 异常信息具体，帮助定位问题
- [ ] 方法职责单一

### 测试
- [ ] 测试 null 参数处理
- [ ] 测试空集合操作
- [ ] 测试异常抛出（使用 assertThrows）
- [ ] 测试返回的集合是副本

### 提交
- [ ] 代码在 `src/main/java/edu/campusflow/` 目录下
- [ ] 测试在 `src/test/java/edu/campusflow/` 目录下
- [ ] Git commit message 清晰规范
- [ ] 有 README 说明如何运行
