# Week 13 测试设计说明

Week 13 的 starter_code 不是完整应用，而是文档交付物骨架。因此测试重点不是启动 API 服务，而是确认学生拿到的模板结构完整、可编译、可作为文档作业起点。

## 当前测试目标

- `pom.xml` 能运行 JUnit 5 smoke test
- `openapi.yaml`、`PROJECT_README_TODO.md`、`docs/ADR_INDEX.md` 存在
- ADR 单篇模板位于 `docs/ADR/001-domain-model.md`
- 模板保留 `TODO` / `待办` 标记，方便学生知道需要填写的位置

## 运行方式

```bash
cd chapters/week_13/starter_code
mvn test
```

## 文件结构

```text
starter_code/
├── openapi.yaml
├── PROJECT_README_TODO.md
├── docs/
│   ├── ADR_INDEX.md
│   └── ADR/001-domain-model.md
├── TEST_MATRIX.md
├── TEST_SUMMARY.md
├── TEST_CASES.md
└── src/test/java/com/campusflow/Week13StarterSmokeTest.java
```

## 设计边界

- smoke test 只保证起步包结构，不替代最终作业评分。
- 学生完成最终交付时，可以把模板中的 TODO 清理掉；最终质量应按 `ASSIGNMENT.md` 和 `RUBRIC.md` 人工/自动结合检查。
- 如需验证 OpenAPI 语义，请使用 Swagger Editor 或额外的 OpenAPI validator。

## 技术栈

- **Java**: 21
- **JUnit**: 5
- **Maven**: 3.9+
