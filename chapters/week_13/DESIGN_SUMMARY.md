# Week 13 设计总结：文档型 starter 与 Docs-as-Code

Week 13 的重点是技术文档与知识管理。当前 `starter_code` 不再提供完整 Web 应用实现，而是提供文档交付物骨架和 smoke test，帮助学生把已有 CampusFlow 项目整理成可交接、可审查、可维护的文档体系。

## Starter Code 结构

```text
chapters/week_13/starter_code/
├── pom.xml
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

## 测试设计

`Week13StarterSmokeTest` 只验证起步包是否完整：

- 必需模板文件存在
- ADR 索引和单篇 ADR 示例路径正确
- 模板中保留 `TODO` / `待办`，方便学生识别要填写的位置
- Maven + JUnit 5 测试链路可运行

它不是最终作业评分器。学生完成作业后，应按 `ASSIGNMENT.md` / `RUBRIC.md` 检查文档质量、API 一致性和人工审查记录。

## 学习目标覆盖

| 学习目标 | 对应内容 |
|---------|---------|
| 理解 API 文档价值 | `openapi.yaml` 模板 |
| 应用 OpenAPI 规范 | Swagger Editor / OpenAPI validator 验证 |
| 分析 README 结构 | `PROJECT_README_TODO.md` |
| 记录架构决策 | `docs/ADR_INDEX.md` + `docs/ADR/001-domain-model.md` |
| 实践 Docs-as-Code | 文档与代码同仓库、同评审流程 |

## 推荐验证命令

```bash
cd chapters/week_13/starter_code
mvn test
```

如需验证 OpenAPI 语义，可额外使用 Swagger Editor、Redocly CLI 或课程指定的 OpenAPI validator。

## 技术栈

- **Java**: 21
- **JUnit**: 5
- **Maven**: 3.9+
- **文档格式**: Markdown + OpenAPI YAML

## 学生下一步

1. 将 `PROJECT_README_TODO.md` 改写为真实项目 README。
2. 根据自己的 API 实现更新 `openapi.yaml`。
3. 补充 ADR，并维护 `docs/ADR_INDEX.md`。
4. 写一份文档审查记录，说明 AI 生成内容经过了哪些人工校验。
