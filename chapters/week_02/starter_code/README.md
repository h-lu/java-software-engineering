# Week 02 Starter Code：图书管理领域模型

本周作业是设计一个**图书管理系统**，核心目标是从需求中识别类、应用封装、拆分上帝类，并撰写 ADR-001。

本目录是独立 Maven 起步项目，只提供骨架和 TODO，不包含完整答案。

## 项目结构

```text
starter_code/
├── pom.xml
├── README.md
├── docs/adr/001-领域模型设计.md
└── src/
    ├── main/java/com/week02/
    │   ├── Book.java
    │   ├── BookCollection.java
    │   ├── BookValidator.java
    │   └── BookPrinter.java
    └── test/java/com/week02/
        └── StarterSmokeTest.java
```

## 运行命令

```bash
cd chapters/week_02/starter_code
mvn test
```

## 你需要完成的 TODO

- `Book`：补全封装、构造方法验证、`setPrice`、`setYear`、展示文本。
- `BookCollection`：实现添加、总价计算、按作者过滤、按年份排序。
- `BookValidator`：放复杂验证规则，避免集合类承担过多职责。
- `BookPrinter`：实现普通文本和可选 JSON 格式输出。
- `docs/adr/001-领域模型设计.md`：记录你的领域模型设计决策。
- 至少补充 2 个你自己的测试方法，覆盖封装和职责分离。

`mvn test` 只确认 starter 可以编译运行。你完成作业后，应按 `ASSIGNMENT.md` 增加更严格的测试。
