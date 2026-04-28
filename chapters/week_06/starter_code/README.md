# Week 06 Starter Code：JUnit 5 测试骨架

这个 starter 用来练习针对一个可工作的 `LibraryTracker` 编写 JUnit 5 测试。生产代码故意保持很小，让你把注意力放在测试设计上。

## 目录结构

```text
starter_code/
├── pom.xml
├── src/main/java/com/campusflow/
│   ├── Book.java
│   └── LibraryTracker.java
└── src/test/java/com/campusflow/
    └── LibraryTrackerTest.java
```

## 运行命令

```bash
cd chapters/week_06/starter_code
mvn test
```

初始测试会通过，因为只有一个 smoke test 是启用状态。带 `@Disabled` 的测试是给你补全并启用的待办模板。

## 你需要编辑的文件

- `src/test/java/com/campusflow/LibraryTrackerTest.java`
- 如果更清晰，可以在 `src/test/java/com/campusflow/` 下新增测试类。
- 可以阅读 `src/main/java/com/campusflow/LibraryTracker.java`，但不要为了让测试通过而削弱业务行为。

## 待办清单

- 使用 `@BeforeEach` 准备每个测试都独立的新数据。
- 覆盖 `addBook`, `findByIsbn`, `listAllBooks`, `removeBook`, `borrowBook`, `returnBook`, `hasBorrowRecord`。
- 对非法输入和缺失记录使用 `assertThrows`。
- 至少添加一个 `@ParameterizedTest`。
- 可选：补完真实测试后再跑覆盖率；JaCoCo 报告会生成到 `target/site/jacoco/index.html`。
